package com.dms.almacensoft.features.recepciondespacho.lectura;

import android.content.Context;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.models.GroupConsulta;
import com.dms.almacensoft.data.models.recepciondespacho.BodyConsultarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.BodyEliminarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.HijoConsulta;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.utils.UtilMethods;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link LecturaPresenter} realiza las llamadas al WS y las preferencias del equipo para {@link LecturaFragment}
 */
public class LecturaPresenter implements LecturaContract.Presenter {

    private LecturaContract.View view;
    private PreferenceManager preferenceManager;
    private Context context;
    private CompositeDisposable disposable = new CompositeDisposable();
    private DataSourceRepository dataSourceRepository;

    // Data necesaria para realizar una consulta de lecturas luego de realizar una eliminación
    private BodyConsultarLecturas bodyConsulta;

    // Contador para la eliminación de lecturas grupales
    private int counter = 0;

    // Contador para los intentos de reconexión en eliminación de lectura individual
    private int deleteHijoCount = 0;

    @Inject
    public LecturaPresenter (PreferenceManager preferenceManager,Context context, DataSourceRepository dataSourceRepository){
        this.preferenceManager = preferenceManager;
        this.context = context;
        this.dataSourceRepository = dataSourceRepository;
    }

    /**
     * Obtiene las lecturas para el documento actual
     * @param bodyConsulta es la data necesaria para realizar la consulta de lecturas
     */
    @Override
    public void getLecturas(BodyConsultarLecturas bodyConsulta) {
        this.bodyConsulta = bodyConsulta;
        List<HeaderConsulta> list = new ArrayList<>();
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.getLecturasBD(bodyConsulta.getCodTipodocumento(),
                    bodyConsulta.getIdClaseDocumento(), bodyConsulta.getNumDocumento(),"1",
                    "0", bodyConsulta.getCodUsuario())
                    .subscribeOn(Schedulers.single())
                    // Divide la lista para que el Observable devuelva los items individualmente
                    .flatMapIterable(hijoConsultas -> hijoConsultas)
                    // Agrupa los items por el código de producto (codProducto)
                    .groupBy(HijoConsulta::getCodProducto)
                    // Crea listas de los grupos
                    .flatMapSingle(Observable::toList)
                    .observeOn(AndroidSchedulers.mainThread())
                    // Luego de crear la lista de HeaderConsulta(list) se envia la data para ser mostrada
                    .doOnComplete(() -> {
                        if (list.isEmpty()) {
                            preferenceManager.setNroGuia("");
                            preferenceManager.setGuiaCerrada(true);
                            preferenceManager.setGuiaIniciada(false);
                        }
                        LecturaPresenter.this.getView().showLecturas(list);
                    })
                    // Agrega las listas ya organizadas a la lista de HeaderConsulta(list) para que puedan ser
                    // mostrados las cabeceras de grupo con sus items individuales
                    .subscribe(hijoConsultas -> {
                        double total = 0;
                        for (HijoConsulta hijo : hijoConsultas) {
                            total += hijo.getCtdAsignada();
                        }
                        GroupConsulta group = new GroupConsulta();
                        group.setTitulo(hijoConsultas.get(0).getCodProducto());
                        group.setTotal(String.valueOf(total));
                        list.add(new HeaderConsulta(new Gson().toJson(group, GroupConsulta.class), hijoConsultas));
                    }, throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> getLecturas(bodyConsulta),
                        context.getString(R.string.movil_sin_conexion), 0);
            } else {
                disposable.add(dataSourceRepository.getAllLecturasOnline(bodyConsulta)
                        .subscribeOn(Schedulers.single())
                        // Divide la lista para que el Observable devuelva los items individualmente
                        .flatMapIterable(hijoConsultas -> hijoConsultas)
                        // Agrupa los items por el código de producto (codProducto)
                        .groupBy(HijoConsulta::getCodProducto)
                        // Crea listas de los grupos
                        .flatMapSingle(Observable::toList)
                        .observeOn(AndroidSchedulers.mainThread())
                        // Luego de crear la lista de HeaderConsulta(list) se envia la data para ser mostrada
                        .doOnComplete(() -> {
                            if (list.isEmpty()) {
                                preferenceManager.setNroGuia("");
                                preferenceManager.setGuiaCerrada(true);
                                preferenceManager.setGuiaIniciada(false);
                            }
                            LecturaPresenter.this.getView().showLecturas(list);
                        })
                        // Agrega las listas ya organizadas a la lista de HeaderConsulta(list) para que puedan ser
                        // mostrados las cabeceras de grupo con sus items individuales
                        .subscribe(hijoConsultas -> {
                            double total = 0;
                            for (HijoConsulta hijo : hijoConsultas) {
                                total += hijo.getCtdAsignada();
                            }
                            GroupConsulta group = new GroupConsulta();
                            group.setTitulo(hijoConsultas.get(0).getCodProducto());
                            group.setTotal(String.valueOf(total));
                            list.add(new HeaderConsulta(new Gson().toJson(group, GroupConsulta.class), hijoConsultas));
                        }, throwable -> getView().onError(() -> getLecturas(bodyConsulta),context.getString(R.string.error_WS),2)));
            }
        }

    }

    /**
     * Elimina lecturas individuales
     * @param hijoConsulta es la data necesaria para realizar la eliminación de una lectura
     */
    @Override
    public void deleteLectura(HijoConsulta hijoConsulta) {
        BodyEliminarLecturas body = new BodyEliminarLecturas();
        body.setIdDetalleDocumento(hijoConsulta.getIdDetalleDocumento());
        body.setIdLecturaDocumento(hijoConsulta.getIdLecturaDocumento());
        body.setSerieProducto(hijoConsulta.getSerieProducto());
        body.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
        body.setTerminal(preferenceManager.getImei());
        if (preferenceManager.getConfig().getTipoConexion()){
            // Si es modo batch eliminamos la lectura cambiando su flgActivo a '0'
            dataSourceRepository.deleteLecturaBD(body.getCodUsuario(),UtilMethods.formatCurrentDate(new Date()),
                    body.getTerminal(),UtilMethods.formatCurrentDate(new Date()),hijoConsulta.getIdDetalleDocumento(),
                    hijoConsulta.getIdLecturaDocumento());

            // TODO eliminación en a tabla ProductoUbicacion

            // Actualizamos la tabla MovimientoInterno
            dataSourceRepository.updateMovimientoEliminacion(body.getCodUsuario(),UtilMethods.formatCurrentDate(new Date()),
                    hijoConsulta.getIdLecturaDocumento());

            // Actualizamos la cantidad en DetalleDocumento
            dataSourceRepository.updateDetalleDocumentoEliminacion(hijoConsulta.getCtdAsignada(),
                    hijoConsulta.getIdDetalleDocumento(),body.getCodUsuario(),UtilMethods.formatCurrentDate(new Date()));

            // Verificamos que el detalle tenga más lecturas abiertas, si no se procede a cerrarlo
            Schedulers.single().scheduleDirect(() ->
                    disposable.add(dataSourceRepository.getLecturasByIdDetalle(hijoConsulta.getIdDetalleDocumento())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(lecturas -> {
                            if (lecturas == 0) {
                                dataSourceRepository.cerrarDetalleDocumentoEliminacion(body.getCodUsuario(),
                                        UtilMethods.formatCurrentDate(new Date()), hijoConsulta.getIdDetalleDocumento());
                            }
                            getLecturas(bodyConsulta);
                        }, throwable -> { })),100, TimeUnit.MILLISECONDS);
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> deleteLectura(hijoConsulta),
                        context.getString(R.string.movil_sin_conexion), 0);
            } else {
                disposable.add(dataSourceRepository.deteleLecturas(body)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(msgResponse -> {
                            if (msgResponse.getMsgError().contains("Eliminado con exito")) {
                                deleteHijoCount = 0;
                                getLecturas(bodyConsulta);
                            }
                        }, throwable -> {
                            if (deleteHijoCount < 2) {
                                deleteHijoCount += 1;
                                LecturaPresenter.this.getView().onError(() -> LecturaPresenter.this.deleteLectura(hijoConsulta), "No se pudo eliminar la lectura", 2);
                            } else {
                                deleteHijoCount = 0;
                                getView().onError(null, context.getString(R.string.limite_intentos), 3);
                            }
                        }));
            }
        }
    }

    /**
     * Realiza eliminación de un grupo de lecturas
     * @param hijos es la lista de lecturas que conforman el grupo a eliminar
     */
    @Override
    public void deleteLecturaGroup(List<HijoConsulta> hijos) {
        if (counter < hijos.size()){
            BodyEliminarLecturas bodyEliminar = new BodyEliminarLecturas();
            bodyEliminar.setIdDetalleDocumento(hijos.get(counter).getIdDetalleDocumento());
            bodyEliminar.setIdLecturaDocumento(hijos.get(counter).getIdLecturaDocumento());
            bodyEliminar.setSerieProducto(hijos.get(counter).getSerieProducto());
            bodyEliminar.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
            bodyEliminar.setTerminal(preferenceManager.getImei());
            if (preferenceManager.getConfig().getTipoConexion()){
                // Si es modo batch eliminamos la lectura cambiando su flgActivo a '0'
                dataSourceRepository.deleteLecturaBD(bodyEliminar.getCodUsuario(),UtilMethods.formatCurrentDate(new Date()),
                        bodyEliminar.getTerminal(),UtilMethods.formatCurrentDate(new Date()),hijos.get(counter).getIdDetalleDocumento(),
                        hijos.get(counter).getIdLecturaDocumento());

                // TODO eliminación en a tabla ProductoUbicacion

                // Actualizamos la tabla MovimientoInterno
                dataSourceRepository.updateMovimientoEliminacion(bodyEliminar.getCodUsuario(),UtilMethods.formatCurrentDate(new Date()),
                        hijos.get(counter).getIdLecturaDocumento());

                // Actualizamos la cantidad en DetalleDocumento
                dataSourceRepository.updateDetalleDocumentoEliminacion(hijos.get(counter).getCtdAsignada(),
                        hijos.get(counter).getIdDetalleDocumento(),bodyEliminar.getCodUsuario(),UtilMethods.formatCurrentDate(new Date()));

                // Verificamos que el detalle tenga más lecturas abiertas, si no se procede a cerrarlo
                Schedulers.single().scheduleDirect(() ->
                        disposable.add(dataSourceRepository.getLecturasByIdDetalle(hijos.get(counter).getIdDetalleDocumento())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(lecturas -> {
                                    if (lecturas == 0) {
                                        dataSourceRepository.cerrarDetalleDocumentoEliminacion(bodyEliminar.getCodUsuario(),
                                                UtilMethods.formatCurrentDate(new Date()), hijos.get(counter).getIdDetalleDocumento());
                                    }
                                    counter +=1;
                                    deleteLecturaGroup(hijos);
                                }, throwable -> { })),100, TimeUnit.MILLISECONDS);
            } else {
                if (!UtilMethods.checkConnection(context)) {
                    getView().onError(() -> deleteLecturaGroup(hijos),
                            context.getString(R.string.movil_sin_conexion), 0);
                } else {
                    disposable.add(dataSourceRepository.deteleLecturas(bodyEliminar)
                            .subscribeOn(Schedulers.single())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(msgResponse -> {
                                if (msgResponse.getMsgError().contains("Eliminado con exito")) {
                                    counter += 1;
                                    deleteLecturaGroup(hijos);
                                }
                            }, throwable -> getView().onError(() -> getLecturas(bodyConsulta),
                                    context.getString(R.string.error_eliminacion),4)));
                }
            }
        } else {
            getLecturas(bodyConsulta);
            counter = 0;
        }
    }

    @Override
    public void attachView(LecturaContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public LecturaContract.View getView() {
        return view;
    }
}
