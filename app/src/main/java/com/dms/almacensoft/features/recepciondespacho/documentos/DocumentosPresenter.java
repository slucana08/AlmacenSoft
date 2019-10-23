package com.dms.almacensoft.features.recepciondespacho.documentos;

import android.content.Context;
import android.text.TextUtils;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.entities.dbalmacen.ClaseDocumento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDocumentoPendiente;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.CustomDialog;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link DocumentosPresenter} realiza las llamadas al WS, preferencias del equipo y a BD interna para {@link DocumentosFragment}
 */

public class DocumentosPresenter implements DocumentosContract.Presenter {

    private DocumentosContract.View view;
    private Context context;
    private PreferenceManager preferenceManager;
    private DataSourceRepository dataSourceRepository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<ClaseDocumento> claseDocumentoList;

    // Contador de reintentos de conexión al WS al traer las clases
    private int cargaClasesCount = 0;

    @Inject
    public DocumentosPresenter(Context context, PreferenceManager preferenceManager,
                               DataSourceRepository dataSourceRepository){
        this.context = context;
        this.preferenceManager = preferenceManager;
        this.dataSourceRepository = dataSourceRepository;
    }

    @Override
    public void attachView(DocumentosContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        disposable.clear();
        this.view = null;
    }

    @Override
    public DocumentosContract.View getView() {
        return view;
    }

    /**
     * Trae las clases de la base de datos si ya fueron previamente cargadas
     * @param type determina el módulo en el que se trabaja
     *             "R" - recepción
     *             "D" - despacho
     */
    @Override
    public void getDataClase(String type) {
        if (preferenceManager.getConfig().getTipoConexion()){
            // Se ejecuta en el modo batch
            disposable.add(dataSourceRepository.getListClaseDocumentoBatch(type,"1")
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(claseDocumentos -> {
                        claseDocumentoList = claseDocumentos;
                        List<String> documentos = new ArrayList<>();
                        for (ClaseDocumento claseDocumento : claseDocumentos) {
                            documentos.add(claseDocumento.getDscClaseDocumento());
                        }
                        getView().setUpClasesSpinner(preferenceManager.getConfig().getDscAlmacen(), documentos);
                    }, throwable -> {}));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                ProgressDialog.dismiss();
                getView().showNoInternet(() -> {
                    DocumentosPresenter.this.getDataClase(type);
                }, context.getString(R.string.recopilando));
            } else {
                disposable.add(dataSourceRepository.getClaseDocumentoModulo(type, true)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(claseDocumentos -> {
                            if (claseDocumentos != null) {
                                claseDocumentoList = claseDocumentos;
                                List<String> clasedocumentos = new ArrayList<>();
                                for (ClaseDocumento claseDocumento : claseDocumentos) {
                                    clasedocumentos.add(claseDocumento.getDscClaseDocumento());
                                }
                                getView().setUpClasesSpinner(preferenceManager.getConfig().getDscAlmacen(), clasedocumentos);
                            }
                        }, throwable -> {
                            ProgressDialog.dismiss();
                            if (cargaClasesCount < 2) {
                                cargaClasesCount += 1;
                                getView().onError(() -> getDataClase(type), context.getString(R.string.recopilando), 0);
                            } else {
                                getView().onError(null, "", 1);
                            }
                        }));
            }
        }
    }

    @Override
    public void getDocumentoPendienteOnline(String claseDocumento, String nroDocumento) {
        BodyDocumentoPendiente body = new BodyDocumentoPendiente();
        body.setCodAlmacen(preferenceManager.getConfig().getCodAlmacen());
        body.setCodEmpresa(preferenceManager.getConfig().getCodEmpresa());
        for (ClaseDocumento clase : claseDocumentoList) {
            if (TextUtils.equals(claseDocumento, clase.getDscClaseDocumento())) {
                body.setCodTipoDocumento(clase.getCodTipoDocumento());
                body.setCodClaseDocumento(clase.getCodClaseDocumento());
                body.setFlgConSinDoc(clase.getFlgConSinDoc());
            }
        }
        body.setNumDocumento(nroDocumento);
        body.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
        if (preferenceManager.getConfig().getTipoConexion()){
            // Se ejecuta en el modo en batch
            if (TextUtils.isEmpty(body.getNumDocumento())) {
                // Se ejecuta cuando no se ingreso un número de documento
                disposable.add(dataSourceRepository.getListDocumento(body.getCodTipoDocumento(),
                        body.getCodClaseDocumento(), preferenceManager.getConfig().getIdAlmacen())
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(documentos -> {
                            getView().setUpDocumentosPendientes(documentos);
                            ProgressDialog.dismiss();
                        }, throwable -> {}));
            } else {
                // Se eejcuta cuando se ingresa un número de documento
                disposable.add(dataSourceRepository.getListDocumento(body.getCodTipoDocumento(),
                        body.getCodClaseDocumento(),"%" + body.getNumDocumento() + "%"
                        ,preferenceManager.getConfig().getIdAlmacen())
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(documentos -> {
                            getView().setUpDocumentosPendientes(documentos);
                            ProgressDialog.dismiss();
                        }, throwable -> {}));
            }
        } else {
            if (!UtilMethods.checkConnection(context)) {
                ProgressDialog.dismiss();
                getView().showNoInternet(() -> getDocumentoPendienteOnline(claseDocumento, nroDocumento),
                        context.getString(R.string.procesando));
            } else {
                disposable.add(dataSourceRepository.getDocumentosPendientes(body)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(documentos -> {
                            getView().setUpDocumentosPendientes(documentos);
                            ProgressDialog.dismiss();
                        }, throwable -> {
                            ProgressDialog.dismiss();
                            getView().onError(() -> getDocumentoPendienteOnline(claseDocumento, nroDocumento),
                                    context.getString(R.string.recopilando), 0);
                        }));
            }
        }
    }

    /**
     * @return datos necesarios desde las preferencias del equipo para la captura de los detalles de un documento
     * pendiente
     */
    @Override
    public BodyDetalleDocumentoPendiente getDataDetalleDocumento() {
        BodyDetalleDocumentoPendiente body = new BodyDetalleDocumentoPendiente();
        body.setCodEmpresa(preferenceManager.getConfig().getCodEmpresa());
        body.setCodAlmacen(preferenceManager.getConfig().getCodAlmacen());
        body.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
        return body;
    }

    /**
     * Setea el tipo de trabajo que se va a realizar
     * @param value determina el tipo
     *              0 - no asignado
     *              1 - con documento
     *              2 - sin documento
     */
    @Override
    public void setConSinDoc(int value) {
        preferenceManager.setConSinDoc(value);
    }


}
