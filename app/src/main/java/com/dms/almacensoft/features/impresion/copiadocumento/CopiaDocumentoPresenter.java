package com.dms.almacensoft.features.impresion.copiadocumento;

import android.content.Context;
import android.text.TextUtils;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.entities.dbalmacen.ClaseDocumento;
import com.dms.almacensoft.data.models.impresion.BodyImprimirEtiqueta;
import com.dms.almacensoft.data.models.impresion.DetalleImpresion;
import com.dms.almacensoft.data.models.impresion.DocumentosCerrados;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.PrinterHelper;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link CopiaDocumentoPresenter} realiza las llamadas al WS para {@link CopiaDocumentoFragment}
 */

public class CopiaDocumentoPresenter implements CopiaDocumentoContract.Presenter {

    private CopiaDocumentoContract.View view;

    private Context context;
    private DataSourceRepository dataSourceRepository;
    private PreferenceManager preferenceManager;

    private CompositeDisposable disposable = new CompositeDisposable();

    private int countEtiquetas = 0; // contador de cantidad de etiquetas impresas/actualizadas
    private int countClases = 0; // contador de tipo de clases cargadas : 0 - sin documento   1 - con documento

    // Contadores de reintento de reconexión con WS
    private int getClasescount = 0;
    private int imprimirCount = 0;

    private List<ClaseDocumento> clases = new ArrayList<>();

    @Inject
    public CopiaDocumentoPresenter (Context context, DataSourceRepository dataSourceRepository,
                                    PreferenceManager preferenceManager){
        this.context = context;
        this.dataSourceRepository = dataSourceRepository;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void attachView(CopiaDocumentoContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        disposable.clear();
        this.view = null;
    }

    @Override
    public CopiaDocumentoContract.View getView() {
        return view;
    }

    @Override
    public void getDataClase() {
        if (countClases < 2) {
            if (preferenceManager.getConfig().getTipoConexion()){
                disposable.add(dataSourceRepository.getListClaseDocumentoBatch(Constants.RECEPCION,
                        countClases == 1 ? "1" : "0")
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(claseDocumentos -> {
                            clases.addAll(claseDocumentos);
                            countClases += 1;
                            getDataClase();
                        }, throwable -> {}));
            } else {
                if (!UtilMethods.checkConnection(context)) {
                    getView().onError(this::getDataClase, context.getString(R.string.recopilando), 0);
                } else {
                    disposable.add(dataSourceRepository.getClaseDocumentoModulo(Constants.RECEPCION,
                            countClases == 1)
                            .subscribeOn(Schedulers.single())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(claseDocumentos -> {
                                clases.addAll(claseDocumentos);
                                countClases += 1;
                                getDataClase();
                            }, throwable -> {
                                ProgressDialog.dismiss();
                                if (getClasescount < 2) {
                                    getClasescount += 1;
                                    getView().onError(this::getDataClase, context.getString(R.string.recopilando), 2);
                                } else {
                                    getClasescount = 0;
                                    getView().onError(null, null, 3);
                                }
                            }));
                }
            }
        } else {
            countClases = 0;
            getView().setUpClases(clases);
            ProgressDialog.dismiss();
        }

    }

    @Override
    public void getDocumentoCerrados(int idClaseDocumento, String numDocumento) {
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.getGuiaCerradas(numDocumento, idClaseDocumento)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(guias -> {
                        List<DocumentosCerrados> documentosCerrados = new ArrayList<>();
                        for (String guia : guias){
                            DocumentosCerrados documentosCerrado = new DocumentosCerrados();
                            documentosCerrado.setNumDocInterno(guia);
                            documentosCerrados.add(documentosCerrado);
                        }
                        getView().setUpDocumentosCerrados(documentosCerrados);
                    }, throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> getDocumentoCerrados(idClaseDocumento, numDocumento), context.getString(R.string.procesando), 0);
            } else {
                disposable.add(dataSourceRepository.getDocumentoCerrado(numDocumento, idClaseDocumento)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(documentosCerrados ->
                                        CopiaDocumentoPresenter.this.getView().setUpDocumentosCerrados(documentosCerrados),
                                throwable -> {
                                    ProgressDialog.dismiss();
                                    UtilMethods.showToast(context, context.getString(R.string.error_WS));
                                }));
            }
        }
    }

    @Override
    public void getDetalle(String numDocumento, String nroGuia) {
        if (preferenceManager.getConfig().getTipoConexion()){
            if (preferenceManager.getConfig().getImpresionRegistros()){
                disposable.add(dataSourceRepository.getDetalleImpresionBD(numDocumento, nroGuia)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(detalleImpresions -> getView().setUpDetalle(detalleImpresions), throwable -> {}));
            } else {
                disposable.add(dataSourceRepository.getLecturaImpresion(numDocumento, nroGuia)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(detalleImpresions -> getView().setUpDetalle(detalleImpresions), throwable -> {}));
            }
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> getDetalle(numDocumento,nroGuia), context.getString(R.string.movil_sin_conexion), 1);
            } else {
                disposable.add(dataSourceRepository.getDetalleImpresion(numDocumento, nroGuia,
                        preferenceManager.getConfig().getImpresionRegistros())
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(detalleImpresions -> CopiaDocumentoPresenter.this.getView().setUpDetalle(detalleImpresions),
                                throwable -> CopiaDocumentoPresenter.this.getView().onError(() -> CopiaDocumentoPresenter.this.getDetalle(numDocumento, nroGuia),
                                        context.getString(R.string.error_WS), 1)));
            }
        }
    }

    @Override
    public void imprimirEtiquetas(List<DetalleImpresion> list, int nroCopias) {
        if (preferenceManager.getConfig().getTipoImpresora()) {
            // Realiza impresión de etiquetas por bluetooth
            if (!TextUtils.isEmpty(preferenceManager.getPrinterAddress())) {
                List<String> labelList = new ArrayList<>();
                for (DetalleImpresion detalle: list) {
                    // Definición de etiqueta basándose en formato de etiqueta obtenida desde el WS
                    String label = preferenceManager.getEtiquetaBluetooth();
                    label = label.replace("[CODBARRA]", detalle.getCodProducto());
                    label = label.replace("[DESCRIPCION]", detalle.getDscProducto());
                    label = label.replace("[LOTE]", TextUtils.isEmpty(detalle.getLoteProducto()) ? "" : detalle.getLoteProducto());
                    label = label.replace("[SERIE]", TextUtils.isEmpty(detalle.getSerieProducto()) ? "" : detalle.getSerieProducto());
                    labelList.add(label);
                }
                PrinterHelper.printBluetooth(preferenceManager.getPrinterAddress(), labelList, nroCopias);
                getView().checkPrintResult(list);
            } else {
                ProgressDialog.dismiss();
                UtilMethods.showToast(context, context.getString(R.string.seleccionar_impresora_configuracion));
            }
        } else {
            // Realiza impresión con impresora de red
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> imprimirEtiquetas(list, nroCopias),
                        context.getString(R.string.imprimiendo), 0);
            } else {
                BodyImprimirEtiqueta body = new BodyImprimirEtiqueta();
                body.setNumCopias(nroCopias);
                body.setCodigoProducto(list.get(countEtiquetas).getCodProducto());
                body.setDescripcionProducto(list.get(countEtiquetas).getDscProducto());
                body.setLote(TextUtils.isEmpty(list.get(countEtiquetas).getLoteProducto()) ? "" : list.get(countEtiquetas).getLoteProducto());
                body.setSerie(TextUtils.isEmpty(list.get(countEtiquetas).getSerieProducto()) ? "" : list.get(countEtiquetas).getSerieProducto());
                disposable.add(dataSourceRepository.imprimirEtiqueta(body)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            if (result) {
                                imprimirCount = 0;
                                actualizarImpresion(list, nroCopias);
                            } else {
                                ProgressDialog.dismiss();
                                UtilMethods.showToast(context,"Error de impresión, revisar impresora");
                            }
                        }, throwable -> {
                            ProgressDialog.dismiss();
                            if (imprimirCount < 2) {
                                imprimirCount += 1;
                                getView().onError(() -> imprimirEtiquetas(list, nroCopias), context.getString(R.string.imprimiendo), 4);
                            } else {
                                imprimirCount = 0;
                                getView().onError(null, null, 5);
                            }
                        }));
            }
        }
    }

    @Override
    public void actualizarImpresion(List<DetalleImpresion> list, int nroCopias) {
        if (preferenceManager.getConfig().getTipoConexion()){
            ProgressDialog.dismiss();
            UtilMethods.showToast(context, context.getString(R.string.impresion_realizada));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> actualizarImpresion(list, nroCopias),
                        context.getString(R.string.imprimiendo), 0);
            } else {
                disposable.add(dataSourceRepository.actualizarImpresion(preferenceManager.getConfig().getImpresionRegistros() ?
                                list.get(countEtiquetas).getIdDetalleDocumento() : list.get(countEtiquetas).getIdLecturaDocumento(),
                        preferenceManager.getConfig().getImpresionRegistros())
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            if (result) {
                                countEtiquetas++;
                                if (countEtiquetas < list.size()) {
                                    // Continua el ciclo de impresión si todavía hay items en la lista
                                    if (nroCopias == 0) {
                                        // Siginifica que la impresión fue por bluetooth
                                        actualizarImpresion(list, nroCopias);
                                    } else {
                                        // Significa que la impresión fue por red
                                        imprimirEtiquetas(list, nroCopias);
                                    }
                                } else {
                                    // Se culmina proceso de impresión y actualización
                                    countEtiquetas = 0;
                                    ProgressDialog.dismiss();
                                    UtilMethods.showToast(context, context.getString(R.string.impresion_realizada));
                                }
                            }
                        }, throwable -> {
                            ProgressDialog.dismiss();
                            if (imprimirCount < 2) {
                                imprimirCount += 1;
                                getView().onError(() -> imprimirEtiquetas(list, nroCopias), context.getString(R.string.imprimiendo), 4);
                            } else {
                                imprimirCount = 0;
                                getView().onError(null, null, 5);
                            }
                        }));
            }
        }
    }
}
