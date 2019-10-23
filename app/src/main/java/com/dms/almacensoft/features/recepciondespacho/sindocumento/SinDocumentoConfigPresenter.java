package com.dms.almacensoft.features.recepciondespacho.sindocumento;

import android.content.Context;
import android.text.TextUtils;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.entities.dbtransact.Documento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyCrearDocumento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.features.recepciondespacho.detalledocumento.DetalleDocumentoFragment;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link SinDocumentoConfigPresenter} realiza las llamadas al WS y preferencias del equipo para
 * {@link SinDocumentoConfigFragment}
 */

public class SinDocumentoConfigPresenter implements SinDocumentoConfigContract.Presenter {

    private SinDocumentoConfigContract.View view;
    private PreferenceManager preferenceManager;
    private DataSourceRepository dataSourceRepository;
    private Context context;
    private CompositeDisposable disposable = new CompositeDisposable();

    // Contador de intentos de reconexión al WS al traer las clases
    private int getClasesCount = 0;

    private BodyCrearDocumento bodyCrear;

    @Inject
    public SinDocumentoConfigPresenter (PreferenceManager preferenceManager, DataSourceRepository dataSourceRepository,
                                        Context context){
        this.preferenceManager = preferenceManager;
        this.dataSourceRepository = dataSourceRepository;
        this.context = context;
    }

    @Override
    public void attachView(SinDocumentoConfigContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        disposable.clear();
        this.view = null;
    }

    @Override
    public SinDocumentoConfigContract.View getView() {
        return view;
    }

    @Override
    public void getDataClase(String type) {
        if (preferenceManager.getConfig().getTipoConexion()) {
            disposable.add(dataSourceRepository.getListClaseDocumentoBatch(type, "0")
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(claseDocumentos -> getView().setUpViews(claseDocumentos), throwable -> {
                    }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> getDataClase(type),
                        context.getString(R.string.movil_sin_conexion), 0);
            } else {
                disposable.add(dataSourceRepository.getClaseDocumentoModulo(type, false)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(claseDocumentos -> getView().setUpViews(claseDocumentos),
                                throwable -> {
                                    if (getClasesCount < 2) {
                                        getClasesCount += 1;
                                        SinDocumentoConfigPresenter.this.getView().onError(() -> SinDocumentoConfigPresenter.this.getDataClase(type), "Error de conexión con WS", 1);
                                    } else {
                                        getClasesCount = 0;
                                        getView().onError(null, context.getString(R.string.limite_intentos), 2);
                                    }
                                }));
            }
        }
    }

    /**
     * Realiza la creación de un documento
     * @param bodyCrear contiene la data necesaria de la clase seleccionada para crear el documento
     */
    @Override
    public void crearDocumento(BodyCrearDocumento bodyCrear) {
        bodyCrear.setCodAlmacen(preferenceManager.getConfig().getCodAlmacen());
        bodyCrear.setObservacion("");
        bodyCrear.setCodAlmDestino(preferenceManager.getConfig().getCodAlmacen());
        bodyCrear.setCodAlmVirtual("0");
        bodyCrear.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
        if (preferenceManager.getConfig().getTipoConexion()){
            bodyCrear.setNumDocumento(TextUtils.isEmpty(bodyCrear.getNumDocumento())
                    ? UtilMethods.formatNroDocumento(new Date()) : bodyCrear.getNumDocumento());
            this.bodyCrear = bodyCrear;
            disposable.add(dataSourceRepository.verifyDocumento(bodyCrear.getNumDocumento())
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(count -> {
                        if (count != 0){
                            ProgressDialog.dismiss();
                            // Se muestra cuando se realiza la creación de documento con un número definido
                            // por el usuario y este número ya existe
                            getView().onError(null, null, 3);
                        } else {
                            Documento documento = new Documento();
                            documento.setIdAlmacen(preferenceManager.getConfig().getIdAlmacen());
                            documento.setCodTipoDocumento(bodyCrear.getCodTipoDocumento());
                            documento.setNumDocumento(bodyCrear.getNumDocumento());
                            documento.setFchDocumento(UtilMethods.formatCurrentDate(new Date()));
                            documento.setCodEstado("P");
                            documento.setFlgActivo("1");
                            documento.setCodUsuarioRegistro(bodyCrear.getCodUsuario());
                            documento.setFchRegistro(UtilMethods.formatCurrentDate(new Date()));
                            documento.setCodUsuarioModificacion(bodyCrear.getCodUsuario());
                            documento.setFchModificacion(UtilMethods.formatCurrentDate(new Date()));
                            documento.setDscObservacion("");
                            documento.setFchRegistroImportacion(UtilMethods.formatCurrentDate(new Date()));
                            documento.setFchUltimaImportacion(UtilMethods.formatCurrentDate(new Date()));
                            documento.setIdClaseDocumento(bodyCrear.getIdClaseDocumento());
                            documento.setCodClaseDocumento(bodyCrear.getCodClaseDocumento());
                            documento.setTipoDocumentoDest(bodyCrear.getTipoDocDestino());
                            documento.setMovimiento("");
                            documento.setTipoAplicacion("");
                            documento.setObsDocumento(bodyCrear.getObservacion());
                            documento.setCodAlmDestino(bodyCrear.getCodAlmDestino());
                            documento.setDscAlmDestino(preferenceManager.getConfig().getDscAlmacen());
                            documento.setCodAlmVirtual(bodyCrear.getCodAlmVirtual());
                            documento.setDscAlmVirtual(preferenceManager.getConfig().getDscAlmacen());
                            documento.setTipoDocumentoOri(0);
                            documento.setEjercicioContbOri(0);
                            documento.setPeriodoOri(0);
                            documento.setDivision(0);
                            documento.setUnidad(0);
                            documento.setOrdnumd(0);
                            documento.setFolio(0);
                            documento.setTipoOperacion(0);
                            documento.setSolicitud(0);
                            documento.setEmbNumId(0);
                            documento.setRecNumId(0);
                            documento.setPeriodoControl(0);
                            documento.setEjercicioContable(0);
                            documento.setTipoOrdenCompra(0);
                            documento.setFolioDocExis(0);
                            documento.setProcedencia("");
                            documento.setFlgCreadoDoc("1");
                            documento.setCodCliente("");
                            documento.setPlacaVehiculo("");
                            documento.setNroErrorKit(0);
                            // Se inserta el nuevo documento
                            dataSourceRepository.insertDocumento(documento);
                            Schedulers.single().scheduleDirect(() -> verifyDocumento(bodyCrear.getNumDocumento()),80, TimeUnit.MILLISECONDS);
                        }
                    }, throwable -> {}));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> crearDocumento(bodyCrear),
                        context.getString(R.string.movil_sin_conexion), 0);
            } else {
                disposable.add(dataSourceRepository.crearDocumento(bodyCrear)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(msgResponse -> {
                            if (msgResponse.getMsgError().contains("OK")) {
                                String[] data = msgResponse.getMsgError().split("\\|");
                                // Una vez creado el documento se procede a obtener la data necesaria para consultar sus
                                // detalles después de que se inicialicen sus lecturas
                                BodyDetalleDocumentoPendiente bodyDetalle = new BodyDetalleDocumentoPendiente();
                                bodyDetalle.setCodEmpresa(preferenceManager.getConfig().getCodEmpresa());
                                bodyDetalle.setCodAlmacen(preferenceManager.getConfig().getCodAlmacen());
                                bodyDetalle.setCodTipoDocumento(bodyCrear.getCodTipoDocumento());
                                bodyDetalle.setCodClaseDocumento(bodyCrear.getCodClaseDocumento());
                                bodyDetalle.setIdClaseDocumento(bodyCrear.getIdClaseDocumento());
                                bodyDetalle.setNumDocumento(data[2]);
                                bodyDetalle.setIdDocumento(Integer.parseInt(data[1]));
                                bodyDetalle.setClaseDocFiltro(null);
                                bodyDetalle.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
                                getView().showDetalle(bodyDetalle);
                            } else if (msgResponse.getMsgError().contains("ya existe")) {
                                ProgressDialog.dismiss();
                                // Se muestra cuando se realiza la creación de documento con un número definido
                                // por el usuario y este número ya existe
                                getView().onError(null, null, 3);
                            }
                        }, throwable -> {
                            ProgressDialog.dismiss();
                            getView().onError(null, context.getString(R.string.error_crear_documento), 4);
                        }));
            }
        }
    }

    /**
     * Verifica que el documento haya sido insertado para capturar el id de Documento
     * @param numDocumento es el número de documento a buscar
     */
    @Override
    public void verifyDocumento(String numDocumento) {
        disposable.add(dataSourceRepository.verifyDocumento(numDocumento)
                .subscribe(count -> {
                    if (count != 0){
                        showDetalle();
                    } else {
                        verifyDocumento(numDocumento);
                    }
                }, throwable -> { }));
    }

    /**
     * Envia la data necesaria para el registro de lecturas a {@link DetalleDocumentoFragment}
     */
    @Override
    public void showDetalle() {
        disposable.add(dataSourceRepository.getIdDocumento(bodyCrear.getNumDocumento())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(idDocumento -> {
                    BodyDetalleDocumentoPendiente bodyDetalle = new BodyDetalleDocumentoPendiente();
                    bodyDetalle.setCodEmpresa(preferenceManager.getConfig().getCodEmpresa());
                    bodyDetalle.setCodAlmacen(preferenceManager.getConfig().getCodAlmacen());
                    bodyDetalle.setCodTipoDocumento(bodyCrear.getCodTipoDocumento());
                    bodyDetalle.setCodClaseDocumento(bodyCrear.getCodClaseDocumento());
                    bodyDetalle.setIdClaseDocumento(bodyCrear.getIdClaseDocumento());
                    bodyDetalle.setNumDocumento(bodyCrear.getNumDocumento());
                    bodyDetalle.setIdDocumento(idDocumento);
                    bodyDetalle.setClaseDocFiltro(null);
                    bodyDetalle.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
                    SinDocumentoConfigPresenter.this.getView().showDetalle(bodyDetalle);
                }, throwable -> {}));
    }

    @Override
    public void setConSinDoc(int value) {
        preferenceManager.setConSinDoc(value);
    }
}
