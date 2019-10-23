package com.dms.almacensoft.features.sync;

import android.content.Context;
import android.util.Log;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.entities.dbalmacen.Inventario;
import com.dms.almacensoft.data.entities.dbalmacen.VerificacionDocumento;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumento;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumentoOri;
import com.dms.almacensoft.data.entities.dbtransact.Documento;
import com.dms.almacensoft.data.entities.dbtransact.LecturaDocumento;
import com.dms.almacensoft.data.entities.dbtransact.LecturaInventario;
import com.dms.almacensoft.data.models.exportar.BodyDetalleDocumento;
import com.dms.almacensoft.data.models.exportar.BodyDetalleDocumentoOri;
import com.dms.almacensoft.data.models.exportar.BodyEnviarDatosBdCliente;
import com.dms.almacensoft.data.models.exportar.BodyInventario;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.utils.ErrorObserver;
import com.dms.almacensoft.utils.UtilMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link SyncPresenter} realiza las llamadas el WS y preferencias del equipo para {@link SyncDialog}
 */
public class SyncPresenter implements SyncContract.Presenter {

    private SyncContract.View view;
    private DataSourceRepository dataSourceRepository;
    private PreferenceManager preferenceManager;
    private Context context;
    private CompositeDisposable disposables = new CompositeDisposable();
    private int doneItems;
    private int totalItems;

    private int lecturaCount = 0;
    private int detalleCount = 0;
    private int productoCount = 1;

    private int productoGroups = 1;

    private int pagIni = 1;
    private int pagFin = 5000; // define el límite de descarga de producto

    private List<Documento> documentoList;
    private List<DetalleDocumento> detalleDocumentoList;
    private List<LecturaDocumento> lecturaDocumentoList;
    private List<DetalleDocumentoOri> detalleDocumentoOriList;
    private List<VerificacionDocumento> verificacionDocumentoList;
    private List<Inventario> inventarioList;
    private List<LecturaInventario> lecturaInventarioList;

    private int lecturaGroups = 0;
    private int lecturaInventarioCount = 0;
    private int limit = 2000; // define el límete de subida de lecturas de inventario

    @Inject
    public SyncPresenter(DataSourceRepository dataSource, PreferenceManager preferenceManager, Context context) {
        this.dataSourceRepository = dataSource;
        this.preferenceManager = preferenceManager;
        this.context = context;
    }

    @Override
    public void attachView(SyncContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
        disposables.clear();
    }

    @Override
    public SyncContract.View getView() {
        return view;
    }

    @Override
    public void setupView() {
        boolean isBatch = preferenceManager.getConfig().getTipoConexion();
        String title, message;
        if (!isBatch) {
            title = context.getString(R.string.carga);
            message = context.getString(R.string.cargar_pregunta);
        } else {
            title = context.getString(R.string.descarga);
            message = context.getString(R.string.descarga_pregunta);
        }

        getView().setupDialog(title, message, isBatch);
    }

    /**
     * Ejecuta la opción necesaria de acuerdo al estado de la configuración
     */
    @Override
    public void startSync(List<BodyDetalleDocumentoPendiente> detalleBodyList) {
        boolean isBatch = preferenceManager.getConfig().getTipoConexion();
        getView().startSync();
        if (!isBatch) {
            // Si se está en batch se realiza el envío de datos hacía el servidor
            getDocumentoExportar();
        } else {
            // Si está en línea se realiza la descarga de datos maestros desde el servidor
            startDownload(detalleBodyList);
        }
    }

    /**
     * Realiza la descarga de datos maestros desde el servidor, cada método llamado en {@link DataSourceRepository}
     * captura la data desde el servidor y automaticamente la inserta en la base de datos interna
     */
    @Override
    public void startDownload(List<BodyDetalleDocumentoPendiente> bodyDetalleList) {
        List<Observable> observableList = new ArrayList<>(Arrays.asList(
                dataSourceRepository.getAllAlmacen(),
                dataSourceRepository.getAllEmpresa(),
                dataSourceRepository.getAllUsuario(),
                dataSourceRepository.getInventarioAbierto(0),
                dataSourceRepository.getAllUsuarioxAlmacen(),
                dataSourceRepository.getAllAlmacenVirtual(),
                dataSourceRepository.getAllJerarquiaClasificacion(),
                dataSourceRepository.getAllClasificacion(),
                dataSourceRepository.getAllDestino(),
                dataSourceRepository.getAllCliente(),
                dataSourceRepository.getAllVehiculo(),
                dataSourceRepository.getAllZona(),
                dataSourceRepository.getAllIsla(),
                dataSourceRepository.getAllProductoUbicacion(),
                dataSourceRepository.getAllCajaProducto(),
                dataSourceRepository.getAllUnidadMedida(),
                dataSourceRepository.getAllPresentacion(),
                dataSourceRepository.getAllStockAlmacen(),
                dataSourceRepository.getDetalleInventarioAbierto()
        ));

        doneItems = 0;
        totalItems = observableList.size() + 4;

        getView().updateProgress(doneItems, totalItems);

        Observable.fromIterable(observableList)
                .flatMap((Function<Observable, ObservableSource<?>>) observable -> observable
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(() -> {
                            doneItems += 1;
                            getView().updateProgress(doneItems, totalItems);
                            if (totalItems - doneItems == 4){
                                getDetalleDocumentos(bodyDetalleList);
                            }
                            }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((ErrorObserver<Object>) e -> SyncPresenter.this.getView().syncError(e.getMessage()));
    }

    @Override
    public void getDetalleDocumentos(List<BodyDetalleDocumentoPendiente> bodyDetalleList) {
        if (detalleCount < bodyDetalleList.size()){
            BodyDetalleDocumentoPendiente body = bodyDetalleList.get(detalleCount);
            disposables.add(dataSourceRepository.getDetalleDocumentoBatch(body)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(detalleDocumentoOris -> {
                        AndroidSchedulers.mainThread().scheduleDirect(() -> {
                            detalleCount += 1;
                            SyncPresenter.this.getDetalleDocumentos(bodyDetalleList);
                        }, detalleDocumentoOris.size() * 3, TimeUnit.MILLISECONDS);
                    }, throwable -> SyncPresenter.this.getView().syncError(throwable.getMessage())));
        } else {
           detalleCount = 0;
           doneItems += 1;
           getView().updateProgress(doneItems,totalItems);
           getUbicacion();
        }
    }

    @Override
    public void getUbicacion() {
        disposables.add(dataSourceRepository.getAllUbicacion()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ubicacions -> {
                    dataSourceRepository.insertAllUbicacion(ubicacions);
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        doneItems += 1;
                        getView().updateProgress(doneItems,totalItems);
                        SyncPresenter.this.countProductos();
                    },ubicacions.size() * 3,TimeUnit.MILLISECONDS);
                }, throwable -> SyncPresenter.this.getView().syncError(throwable.getMessage())));
    }

    @Override
    public void countProductos() {
        disposables.add(dataSourceRepository.countProductos()
                .subscribeOn(Schedulers.single())
                .subscribe(count -> {
                    dataSourceRepository.deleteProductos();
                    Schedulers.single().scheduleDirect(SyncPresenter.this::countProductosServidor,count * 2,TimeUnit.MILLISECONDS);
                }, throwable -> {}));
    }

    @Override
    public void countProductosServidor() {
        disposables.add(dataSourceRepository.getProductoCount()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    productoCount = integer[0];
                    if (productoCount > 5000) {
                        double residuo = productoCount % 5000;
                        productoGroups = productoCount / 5000;
                        if (residuo > 0.0) productoGroups += 1;
                    }
                    productoCount = 1;
                    SyncPresenter.this.getProducto();
                }, throwable -> SyncPresenter.this.getView().syncError(throwable.getMessage())));
    }

    @Override
    public void getProducto() {
        if (productoCount <= productoGroups){
            disposables.add(dataSourceRepository.getAllProducto(pagIni,pagFin)
                            .subscribeOn(Schedulers.single())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(productos -> {
                                pagIni += 5000;
                                pagFin += 5000;
                                dataSourceRepository.insertAllProducto(productos);
                                Schedulers.single().scheduleDirect(() -> {
                                    Log.i("Insert","Insertados" + productoCount);
                                    productoCount += 1;
                                    getProducto();
                                },productos.size() * 2,TimeUnit.MILLISECONDS);
                            }, throwable -> SyncPresenter.this.getView().syncError(throwable.getMessage())));
        } else {
            pagIni = 1;
            pagFin = 5000;
            productoCount = 1;
            productoGroups = 1;
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                doneItems += 1;
                getView().updateProgress(doneItems,totalItems);
                verificarInventario();
            });
        }

    }

    @Override
    public void verificarInventario() {
           disposables.add(dataSourceRepository.getAllInventarioByAlmacen(preferenceManager.getConfig().getIdAlmacen())
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(inventarios -> {
                    if (inventarios.isEmpty()) {
                        doneItems += 1;
                        getView().updateProgress(doneItems, totalItems);
                    } else {
                        if (inventarios.get(0).getNroConteo() > 1 && inventarios.get(0).getDiferenciado() == 1){
                            cargarDiferencial(inventarios.get(0).getIdInventario(),inventarios.get(0).getNroConteo());
                        } else {
                            doneItems += 1;
                            getView().updateProgress(doneItems, totalItems);
                        }
                    }
                }, throwable -> {}));
    }

    @Override
    public void cargarDiferencial(int idInventario, int conteo) {
        disposables.add(dataSourceRepository.getDiferencial(idInventario, conteo)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productoDiferencials -> {
                    doneItems += 1;
                    getView().updateProgress(doneItems, totalItems);
                }, throwable -> SyncPresenter.this.getView().syncError(throwable.getMessage())));
    }



    @Override
    public void syncSuccess() {
        getView().syncSuccess();
    }

    @Override
    public void getDocumentoExportar() {
        disposables.add(dataSourceRepository.getAllDocumentosExportar()
                .subscribeOn(Schedulers.single())
                .subscribe(documentos -> {
                    documentoList = documentos;
                    getDetalleDocumentoOriExportar();
                }, throwable -> {}));
    }

    @Override
    public void getDetalleDocumentoOriExportar() {
        disposables.add(dataSourceRepository.getAllDetalleDocumentoOriExportar()
                .subscribe(detalleDocumentoOris -> {
                    detalleDocumentoOriList = detalleDocumentoOris;
                    getVerificacionExportar();
                }, throwable -> {}));
    }

    @Override
    public void getVerificacionExportar() {
        disposables.add(dataSourceRepository.getAllVerificacionExportar()
                .subscribe(verificacionDocumentos -> {
                    verificacionDocumentoList = verificacionDocumentos;
                    getDetalleDocumentoExportar();
                }, throwable -> {}));
    }

    @Override
    public void getDetalleDocumentoExportar() {
        disposables.add(dataSourceRepository.getAllDetalleExportar()
                .subscribe(detalleDocumentos -> {
                    detalleDocumentoList = detalleDocumentos;
                    getLecturaDocumentoExportar();
                }, throwable -> {}));
    }

    @Override
    public void getLecturaDocumentoExportar() {
        disposables.add(dataSourceRepository.getAllLecturaExportar()
                .subscribe(lecturaDocumentos -> {
                    lecturaDocumentoList = lecturaDocumentos;
                    getInventarioExportar();
                }, throwable -> {
                }));
    }

    @Override
    public void getInventarioExportar() {
        disposables.add(dataSourceRepository.getAllInventarioExportar()
                .subscribe(inventarios -> {
                    inventarioList = inventarios;
                    getLecturaInventarioCount();
                }, throwable -> {}));
    }

    @Override
    public void getLecturaInventarioCount() {
        disposables.add(dataSourceRepository.getCountLecturaInventarioExportar()
                .subscribe(countLecturaInventario -> {
                    lecturaInventarioCount = countLecturaInventario;
                    doneItems = 0;
                    if (lecturaDocumentoList.isEmpty() && lecturaInventarioCount == 0) {
                        totalItems = 1;
                    } else if (lecturaInventarioCount != 0 && lecturaDocumentoList.isEmpty()) {
                        totalItems = 2;
                    } else if (lecturaInventarioCount == 0) {
                        totalItems = 4;
                    } else {
                        totalItems = 5;
                    }
                    SyncPresenter.this.getView().updateProgress(doneItems, totalItems);
                    if (lecturaInventarioCount == 0){
                        SyncPresenter.this.exportarDocumento();
                    } else {
                        if (lecturaInventarioCount < limit){
                            lecturaGroups = 1;
                        } else {
                            lecturaGroups = lecturaInventarioCount / limit;
                            if (lecturaInventarioCount % limit > 0.0) lecturaGroups += 1;
                        }
                        lecturaInventarioCount = 1;
                        SyncPresenter.this.exportarDocumento();
                    }
                }, throwable -> { }));
    }

    @Override
    public void exportarDocumento() {
        disposables.add(dataSourceRepository.exportarDocumento(documentoList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msgResponse -> {
                    if (msgResponse.getCodError() == 0){
                        doneItems += 1;
                        getView().updateProgress(doneItems,totalItems);
                        if (totalItems > 1){
                            if (totalItems > 2)
                                exportarDetalleDocumentoOri();
                            else
                                getLecturaInventarioExportar();
                        } else {
                            dataSourceRepository.deleteClaseDocumento();
                        }
                    } else {
                        SyncPresenter.this.getView().syncError(msgResponse.getMsgError());
                    }
                }, throwable -> SyncPresenter.this.getView().syncError(throwable.getMessage())));
    }


    @Override
    public void exportarDetalleDocumentoOri() {
        BodyDetalleDocumentoOri body = new BodyDetalleDocumentoOri();
        body.setVerificacionDocumentoList(verificacionDocumentoList);
        body.setDetalleDocumentoOriList(detalleDocumentoOriList);
        disposables.add(dataSourceRepository.exportarDetalleDocumentoOri(body)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msgResponse -> {
                    if (msgResponse.getCodError() == 0){
                        doneItems += 1;
                        getView().updateProgress(doneItems,totalItems);
                        exportarDetalleDocumento();
                    } else {
                        SyncPresenter.this.getView().syncError(msgResponse.getMsgError());
                    }
                }, throwable -> SyncPresenter.this.getView().syncError(throwable.getMessage())));
    }

    @Override
    public void exportarDetalleDocumento() {
        BodyDetalleDocumento body = new BodyDetalleDocumento();
        body.setDocumentoList(documentoList);
        body.setDetalleDocumentoList(detalleDocumentoList);
        body.setLecturaDocumentoList(lecturaDocumentoList);
        disposables.add(dataSourceRepository.exportarDetalleDocumento(body)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msgResponse -> {
                    if (msgResponse.getCodError() == 0){
                        updateDetalleDescargado();
                    } else {
                        SyncPresenter.this.getView().syncError(msgResponse.getMsgError());
                    }
                }, throwable -> SyncPresenter.this.getView().syncError(throwable.getMessage())));
    }

    @Override
    public void updateDetalleDescargado() {
        if (detalleCount < detalleDocumentoList.size()){
            dataSourceRepository.updateDetalleDescargado(preferenceManager.getConfig().getCodUsuario(),
                    UtilMethods.formatCurrentDate(new Date()),detalleDocumentoList.get(detalleCount).getIdDetalleDocumento());
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                detalleCount += 1;
                updateDetalleDescargado();
            }, 10, TimeUnit.MILLISECONDS);
        } else {
            detalleCount = 0;
            updateLecturaDescargada();
        }
    }

    @Override
    public void updateLecturaDescargada() {
        if (lecturaCount < lecturaDocumentoList.size()){
            dataSourceRepository.updateLecturaDescargada(UtilMethods.formatCurrentDate(new Date())
                    ,preferenceManager.getConfig().getCodUsuario(),UtilMethods.formatCurrentDate(new Date())
                    ,lecturaDocumentoList.get(lecturaCount).getIdLecturaDocumento());
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                lecturaCount += 1;
                updateLecturaDescargada();
            }, 10, TimeUnit.MILLISECONDS);
        } else {
            lecturaCount = 0;
            doneItems += 1;
            getView().updateProgress(doneItems,totalItems);
            if (totalItems > 4){
                getLecturaInventarioExportar();
            } else {
                enviarDatosBdCliente();
            }
        }
    }

    @Override
    public void getLecturaInventarioExportar() {
        if (lecturaInventarioCount <= lecturaGroups){
            disposables.add(dataSourceRepository.getLecturaInventarioExportar(limit)
                    .subscribeOn(Schedulers.single())
                    .subscribe(lecturaInventarios -> {
                        lecturaInventarioCount += 1;
                        lecturaInventarioList = lecturaInventarios;
                        Log.i("Uploaded","Size: " + lecturaInventarios.size());
                        exportarInventario();
                    }, throwable -> {}));
        } else {
            lecturaInventarioCount = 0;
            lecturaGroups = 0;
            doneItems += 1;
            getView().updateProgress(doneItems,totalItems);
            if (totalItems > 2) {
                enviarDatosBdCliente();
            } else {
                dataSourceRepository.deleteClaseDocumento();
            }
        }
    }

    @Override
    public void exportarInventario() {
        BodyInventario body = new BodyInventario();
        body.setInventarioList(inventarioList);
        body.setLecturaInventarioList(lecturaInventarioList);
        disposables.add(dataSourceRepository.exportarInventario(body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msgResponse -> {
                    if (msgResponse.getCodError() == 0){
                        updateLecturaInventarioDescargada();
                    } else {
                        SyncPresenter.this.getView().syncError(msgResponse.getMsgError());
                    }
                }, throwable -> SyncPresenter.this.getView().syncError(throwable.getMessage())));
    }

    @Override
    public void updateLecturaInventarioDescargada() {
        for (LecturaInventario lectura : lecturaInventarioList) {
            dataSourceRepository.updateLecturaInventarioDescargada(UtilMethods.formatCurrentDate(new Date())
                    , preferenceManager.getConfig().getCodUsuario(), UtilMethods.formatCurrentDate(new Date())
                    , lectura.getIdLecturaInventario());
        }
        AndroidSchedulers.mainThread().scheduleDirect(this::getLecturaInventarioExportar,
                lecturaInventarioList.size() * 2, TimeUnit.MILLISECONDS);
    }

    @Override
    public void enviarDatosBdCliente() {
        BodyEnviarDatosBdCliente body = new BodyEnviarDatosBdCliente();
        body.setDocumentoList(documentoList);
        body.setDetalleDocumentoOriList(detalleDocumentoOriList);
        body.setVerificacionDocumentoList(verificacionDocumentoList);
        disposables.add(dataSourceRepository.enviarDatosBdCliente(body,preferenceManager.getConfig().getCodAlmacen(),
                preferenceManager.getConfig().getCodEmpresa())
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msgResponse -> {
                    if (msgResponse.getCodError() == 0){
                        doneItems += 1;
                        getView().updateProgress(doneItems,totalItems);
                        dataSourceRepository.deleteClaseDocumento();
                    } else {
                        SyncPresenter.this.getView().syncError(msgResponse.getMsgError());
                    }
                }, throwable -> SyncPresenter.this.getView().syncError(throwable.getMessage())));
    }

    @Override
    public void clearDisposable() {
        disposables.clear();
    }
}
