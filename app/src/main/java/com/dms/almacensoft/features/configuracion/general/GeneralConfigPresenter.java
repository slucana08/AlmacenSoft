package com.dms.almacensoft.features.configuracion.general;

import android.content.Context;
import android.text.TextUtils;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.entities.dbalmacen.Ubicacion;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumento;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumentoOri;
import com.dms.almacensoft.data.models.BodyUbicacion;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link GeneralConfigPresenter} realiza las llamadas al WS y registro en la preferencias de usuarios
 * para {@link GeneralConfigFragment}
 */

public class GeneralConfigPresenter implements GeneralConfigContract.Presenter {

    private GeneralConfigContract.View view;
    private Context context;
    private CompositeDisposable disposable = new CompositeDisposable();
    private DataSourceRepository dataSourceRepository;
    private PreferenceManager preferenceManager;

    int countEliminar = 0;

    int detalleIndex = 0;
    int documentoIndex = 0;

    private double cantidadAtendida = 0.0;

    // Verifica si todos los detalles de un documento se cerraron
    private boolean cerradoCheck = false;
    private boolean firstDetalleDocumentoOri = true;
    private boolean detalleRegistrado = false;

    @Inject
    public GeneralConfigPresenter(Context context, DataSourceRepository dataSourceRepository,
                                  PreferenceManager preferenceManager){
        this.context = context;
        this.dataSourceRepository = dataSourceRepository;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void attachView(GeneralConfigContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        disposable.clear();
        this.view = null;
    }

    @Override
    public GeneralConfigContract.View getView() {
        return view;
    }

    @Override
    public void listAlmacen() {
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.getAllAlmacenByUsuarioBD(Integer.parseInt(preferenceManager.getConfig().getIdUsuario()))
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(almacens -> getView().setUpSpinner(almacens,preferenceManager.getConfig().getIdAlmacen()), throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(this::listAlmacen, null, 0);
            } else {
                disposable.add(dataSourceRepository.getAllAlmacenByUsuario(preferenceManager.getConfig().getIdUsuario())
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(almacens -> GeneralConfigPresenter.this.getView().setUpSpinner(almacens, preferenceManager.getConfig().getIdAlmacen())
                                , throwable -> GeneralConfigPresenter.this.getView().onError(GeneralConfigPresenter.this::listAlmacen, "Error de conexión con WS", 1)));
            }
        }
    }

    @Override
    public void getConfig() {
        if (TextUtils.isEmpty(preferenceManager.getConfig().getZonaRecepcion())) preferenceManager.saveRecepcionUbi(new Ubicacion());
        if (TextUtils.isEmpty(preferenceManager.getConfig().getZonaDespacho())) preferenceManager.saveDespachoUbi(new Ubicacion());
        if (TextUtils.isEmpty(preferenceManager.getConfig().getZonaInventario())) preferenceManager.saveInventarioUbi(new Ubicacion());
        getView().setUpConfig(preferenceManager.getConfig()) ;
    }

    @Override
    public void saveAlmacenData(int idAlmacen, String dscAlmacen, String codAlmacen, String codEmpresa, int idEmpresa) {
        Configuracion configuracion = preferenceManager.getConfig();
        configuracion.setIdAlmacen(idAlmacen);
        configuracion.setDscAlmacen(dscAlmacen);
        configuracion.setCodAlmacen(codAlmacen);
        configuracion.setCodEmpresa(codEmpresa);
        configuracion.setIdEmpresa(idEmpresa);
        preferenceManager.saveConfig(configuracion);
    }

    /**
     * Verifica la disponibilidad de una ubicación
     * @param codUbicacion es el código de ubicación ingresado
     * @param type es el tipo de consulta a realizar
     *             0 - recepción
     *             1 - despacho
     *             2 - inventario
     * @param idAlmacen
     */
    @Override
    public void verifyUbicacion(String codUbicacion, int type, int idAlmacen) {
        if (TextUtils.isEmpty(codUbicacion)){
            switch (type){
                case 0:
                    preferenceManager.saveRecepcionUbi(new Ubicacion());
                    break;
                case 1:
                    preferenceManager.saveDespachoUbi(new Ubicacion());
                    break;
                case 2:
                    preferenceManager.saveInventarioUbi(new Ubicacion());
                    break;
            }
            return;
        }
        BodyUbicacion body = new BodyUbicacion();
        body.setIdAlmacen(idAlmacen);
        body.setCodProducto("");
        body.setCodUbicacion(codUbicacion);
        if (type == 0){
            body.setTipoResultado(type);
        } else {
            body.setTipoResultado(1); // se envia 1 para despacho e inventario
        }
        if (preferenceManager.getConfig().getTipoConexion()){
            // Se ejecuta si esta en modo Batch
            disposable.add(dataSourceRepository.verifyUbicacionBD(idAlmacen,codUbicacion)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(ubicacions -> {
                        if (ubicacions.isEmpty()) {
                            getView().updateUbicacion(false, type);
                        } else {
                            getView().updateUbicacion(true, type);
                            switch (type) {
                                case 0:
                                    preferenceManager.saveRecepcionUbi(ubicacions.get(0));
                                    break;
                                case 1:
                                    preferenceManager.saveDespachoUbi(ubicacions.get(0));
                                    break;
                                case 2:
                                    preferenceManager.saveInventarioUbi(ubicacions.get(0));
                                    break;
                            }
                        }
                    }, throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> verifyUbicacion(codUbicacion, type, idAlmacen), null, 0);
            } else {
                disposable.add(dataSourceRepository.getUbicacion(body)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(ubicacions -> {
                            if (ubicacions.isEmpty()) {
                                getView().updateUbicacion(false, type);
                            } else {
                                getView().updateUbicacion(true, type);
                                switch (type) {
                                    case 0:
                                        preferenceManager.saveRecepcionUbi(ubicacions.get(0));
                                        break;
                                    case 1:
                                        preferenceManager.saveDespachoUbi(ubicacions.get(0));
                                        break;
                                    case 2:
                                        preferenceManager.saveInventarioUbi(ubicacions.get(0));
                                        break;
                                }
                            }
                        }, throwable -> getView().onError(() -> verifyUbicacion(codUbicacion, type, idAlmacen), context.getString(R.string.error_WS), 1)));
            }
        }
    }

    @Override
    public void setBatchMode(boolean isBatch) {
        Configuracion config = preferenceManager.getConfig();
        config.setTipoConexion(isBatch);
        preferenceManager.saveConfig(config);
    }

    @Override
    public void limpiarBD() {
        List<Observable> observableList = new ArrayList<>(Arrays.asList(
                dataSourceRepository.countLectura(),
                dataSourceRepository.countDetalle(),
                dataSourceRepository.countMovimiento(),
                dataSourceRepository.countDocumento(),
                dataSourceRepository.countDetalleOri(),
                dataSourceRepository.countDetalleInventario(),
                dataSourceRepository.countLecturasInventarioBD()
        ));

        disposable.add(Observable.fromIterable(observableList)
                .flatMap((Function<Observable, Observable<Integer>>) observable -> observable)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    if (countEliminar == 0){
                        ProgressDialog.dismiss();
                        UtilMethods.showToast(context,context.getString(R.string.no_datos_eliminar));
                    } else {
                        dataSourceRepository.limpiarBdAdmin();
                        AndroidSchedulers.mainThread().scheduleDirect(() -> {
                            preferenceManager.setUltimaLimpiezaBD(new Date().getTime());
                            listAlmacen();
                            getConfig();
                            ProgressDialog.dismiss();
                            UtilMethods.showToast(context,context.getString(R.string.bd_limpiada));
                        },countEliminar * 10, TimeUnit.MILLISECONDS);
                    }
                })
                .subscribe(integer -> countEliminar += integer, throwable -> {}));
    }

    @Override
    public boolean getBatchMode() {
        return preferenceManager.getConfig().getTipoConexion();
    }

    @Override
    public String getTiempoUltimaLimpiezaBD() {
        String tiempo;
        long tiempoUltimaEliminacion = new Date().getTime() - preferenceManager.getUltimaLimpiezaBD();
        int minutos = (int) ((tiempoUltimaEliminacion / 1000) / 60);
        if (minutos < 60) {
            tiempo = context.getString(R.string.no_dias);
        } else {
            int horas = minutos / 60;
            if (horas < 24) {
                tiempo = "0 días " + horas + " horas";
            } else {
                int dias = horas / 24;
                int res = horas % 24;
                tiempo = "" + dias + " días " + res + " horas";
            }
        }
        return tiempo;
    }

    @Override
    public void countDespachoCerrar(boolean isChecked, int type) {
        disposable.add(dataSourceRepository.getDetalleDespachoCerrar()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(detalleDocumentos -> {
                    if (detalleDocumentos.isEmpty()){
                        ProgressDialog.dismiss();
                        getView().enviarDatos(isChecked,type);
                    } else {
                        cerrarLecturasDespacho(isChecked,type,detalleDocumentos);
                    }
                }, throwable -> {}));
    }

    @Override
    public void cerrarLecturasDespacho(boolean isChecked, int type, List<DetalleDocumento> detalleDocumentoList) {
        for (int i = 0; i < detalleDocumentoList.size() ; i++) {
            dataSourceRepository.cerrarLecturaDocumentoByIdDetalle(detalleDocumentoList.get(i).getIdDetalleDocumento()
                    , preferenceManager.getConfig().getCodUsuario(), UtilMethods.formatCurrentDate(new Date()));
        }
        Schedulers.single().scheduleDirect(() -> {
            cerrarDetalleDespacho(isChecked,type,detalleDocumentoList);
            cerrarMovimientoDespacho(detalleDocumentoList);
        },15 * detalleDocumentoList.size(),TimeUnit.MILLISECONDS);
    }

    @Override
    public void cerrarDetalleDespacho(boolean isChecked, int type, List<DetalleDocumento> detalleDocumentoList) {
        for (DetalleDocumento detalleDocumento : detalleDocumentoList){
            dataSourceRepository.cerrarDetalleDocumento(preferenceManager.getConfig().getCodUsuario(),
                    UtilMethods.formatCurrentDate(new Date()),detalleDocumento.getIdDetalleDocumento());
        }
        Schedulers.single().scheduleDirect(() -> {
            orderDocumentos(isChecked,type,detalleDocumentoList);
        },15 * detalleDocumentoList.size(),TimeUnit.MILLISECONDS);
    }

    @Override
    public void cerrarMovimientoDespacho(List<DetalleDocumento> detalleDocumentoList) {
        if (detalleIndex < detalleDocumentoList.size()){
            disposable.add(dataSourceRepository.getIdLecturaDocumentoByIdDetalle(detalleDocumentoList.
                    get(detalleIndex).getIdDetalleDocumento())
                    .subscribe(idLecturaList -> {
                        for (int id : idLecturaList) {
                            dataSourceRepository.cerrarMovimiento(id,preferenceManager.getConfig().getCodUsuario(),
                                    UtilMethods.formatCurrentDate(new Date()));
                        }
                        detalleIndex += 1;
                        cerrarMovimientoDespacho(detalleDocumentoList);
                    }, throwable -> { }));
        } else {
            detalleIndex = 0;
        }
    }

    @Override
    public void orderDocumentos(boolean isChecked, int type, List<DetalleDocumento> detalleDocumentoList) {
        List<List<DetalleDocumento>> sortedList = new ArrayList<>();
        List<Integer> idDocumentoList = new ArrayList<>();
        disposable.add(Observable.fromIterable(detalleDocumentoList)
                .groupBy(DetalleDocumento::getIdDocumento)
                .flatMapSingle(Observable::toList)
                .doOnComplete(() -> cerrarDocumentoDespacho(isChecked,type,sortedList,idDocumentoList))
                .subscribe(detalleDocumentos -> {
                    sortedList.add(detalleDocumentos);
                    idDocumentoList.add(detalleDocumentos.get(0).getIdDocumento());
                }, throwable -> {}));
    }

    @Override
    public void cerrarDocumentoDespacho(boolean isChecked, int type, List<List<DetalleDocumento>> sortedList,
                                        List<Integer> idDocumentoList) {
        if (documentoIndex < idDocumentoList.size()){
            disposable.add(dataSourceRepository.getConSinDoc(idDocumentoList.get(documentoIndex))
                    .subscribeOn(Schedulers.single())
                    .subscribe(s -> {
                        if (TextUtils.equals("0",s)){
                            cerrarDetalleDocumentoOri(sortedList.get(documentoIndex));
                        } else {
                            finalizarDocumento(sortedList.get(documentoIndex));
                        }
                        AndroidSchedulers.mainThread().scheduleDirect(() -> {
                            documentoIndex += 1;
                            cerrarDocumentoDespacho(isChecked,type,sortedList,idDocumentoList);
                        },10 * sortedList.get(documentoIndex).size(),TimeUnit.MILLISECONDS);
                    }, throwable -> { }));
        } else {
            documentoIndex = 0;
            ProgressDialog.dismiss();
            getView().enviarDatos(isChecked,type);
            cerradoCheck = false;
            firstDetalleDocumentoOri = true;
            detalleRegistrado = false;
        }
    }

    @Override
    public void finalizarDocumento(List<DetalleDocumento> detalleDocumentos) {
        dataSourceRepository.cerrarDocumento(preferenceManager.getConfig().getCodUsuario(),UtilMethods.formatCurrentDate(new Date()),
                detalleDocumentos.get(0).getIdDocumento());
        for (DetalleDocumento detalleDocumento : detalleDocumentos){
            dataSourceRepository.finalizarDetalleDocumento(preferenceManager.getConfig().getCodUsuario(),
                    UtilMethods.formatCurrentDate(new Date()),detalleDocumento.getIdDetalleDocumento());
        }
    }

    @Override
    public void cerrarDetalleDocumentoOri(List<DetalleDocumento> detalleDocumentos) {
        List<DetalleDocumentoOri> detalleDocumentoOriInsert = new ArrayList<>();
        disposable.add(dataSourceRepository.getDetalleDocumentoOri(detalleDocumentos.get(0).getIdDocumento())
                .flatMapIterable(detalleDocumentoOris -> detalleDocumentoOris)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    dataSourceRepository.insertDetalleDocumentoOri(detalleDocumentoOriInsert);
                    if (cerradoCheck) {
                        finalizarDocumento(detalleDocumentos);
                    }
                })
                .subscribe(detalleDocumentoOri -> {
                    for (DetalleDocumento detalleDocumento : detalleDocumentos) {
                        if (detalleDocumentoOri.getLinea() == detalleDocumento.getLinea()){
                            cantidadAtendida += detalleDocumento.getCtdAtendida();
                            detalleRegistrado = true;
                        }
                    }
                    if (detalleRegistrado) {
                        detalleDocumentoOri.setCtdAtendidaCliente(detalleDocumentoOri.getCtdAtendidaCliente() + cantidadAtendida);
                        detalleDocumentoOri.setCtdPendiente((int) (detalleDocumentoOri.getCtdRequerida() - detalleDocumentoOri.getCtdAtendidaCliente()));
                        cantidadAtendida = 0.0;
                        detalleRegistrado = false;
                    }
                    detalleDocumentoOri.setCodUsuarioModificacion(preferenceManager.getConfig().getCodUsuario());
                    detalleDocumentoOri.setFchModificacion(UtilMethods.formatCurrentDate(new Date()));
                    if (detalleDocumentoOri.getCtdPendiente() == 0) {
                        if (firstDetalleDocumentoOri) {
                            cerradoCheck = true;
                            firstDetalleDocumentoOri = false;
                        } else {
                            if (cerradoCheck) cerradoCheck = true;
                            else cerradoCheck = false;
                        }
                    } else {
                        cerradoCheck = false;
                    }
                    detalleDocumentoOriInsert.add(detalleDocumentoOri);
                }, throwable -> {
                }));
    }
}
