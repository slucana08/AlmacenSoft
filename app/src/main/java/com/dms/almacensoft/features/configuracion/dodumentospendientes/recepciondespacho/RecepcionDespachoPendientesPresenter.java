package com.dms.almacensoft.features.configuracion.dodumentospendientes.recepciondespacho;

import android.content.Context;
import android.text.TextUtils;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.entities.dbalmacen.ClaseDocumento;
import com.dms.almacensoft.data.entities.dbtransact.Documento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDocumentoPendiente;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link RecepcionDespachoPendientesPresenter} realiza las llamadas al WS, base de datos y preferencias
 * del equipo para {@link RecepcionDespachoPendientesFragment}
 */

public class RecepcionDespachoPendientesPresenter implements RecepcionDespachoPendientesContract.Presenter {

    private RecepcionDespachoPendientesContract.View view;
    private Context context;
    private PreferenceManager preferenceManager;
    private DataSourceRepository dataSourceRepository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<ClaseDocumento> claseDocumentoList;

    private int bloqueoCount = 0;

    @Inject
    public RecepcionDespachoPendientesPresenter(Context context, PreferenceManager preferenceManager,
                                                DataSourceRepository dataSourceRepository){
        this.context = context;
        this.preferenceManager = preferenceManager;
        this.dataSourceRepository = dataSourceRepository;
    }

    @Override
    public void attachView(RecepcionDespachoPendientesContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public RecepcionDespachoPendientesContract.View getView() {
        return view;
    }

    /**
     * Trae las clases ya descargadas en la base de datos interna
     * @param type es el módulo del cual se cargan las clases
     *             "R" - Recepción
     *             "D" - Despacho
     */
    @Override
    public void getDataClaseBD(String type) {
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
    }

    /**
     * Trae los documentos pendientes para la clases seleccionada y guarda esta data en la base de
     * datos interna
     * @param claseDocumento es descripción de la case seleccionada
     * @param nroDocumento es el número de documento ingresado por el usuario
     */
    @Override
    public void getDocumentoPendienteOnline(String claseDocumento, String nroDocumento) {
        if (!UtilMethods.checkConnection(context)) {
            ProgressDialog.dismiss();
            getView().showNoInternet(() -> getDocumentoPendienteOnline(claseDocumento,nroDocumento),
                    context.getString(R.string.procesando));
        } else {
            BodyDocumentoPendiente body = new BodyDocumentoPendiente();
            body.setCodAlmacen(preferenceManager.getConfig().getCodAlmacen());
            body.setCodEmpresa(preferenceManager.getConfig().getCodEmpresa());
            for (ClaseDocumento clase : claseDocumentoList) {
                if (TextUtils.equals(claseDocumento, clase.getDscClaseDocumento())) {
                    body.setCodTipoDocumento(clase.getCodTipoDocumento());
                    body.setCodClaseDocumento(clase.getCodClaseDocumento());
                    body.setFlgConSinDoc(clase.getFlgConSinDoc());
                    break;
                }
            }
            body.setNumDocumento(nroDocumento);
            body.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
            disposable.add(dataSourceRepository.getDocumentosPendientes(body)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(documentos -> {
                        getView().setUpDocumentosPendientes(documentos);
                    }, throwable -> {
                        ProgressDialog.dismiss();
                        getView().onError(() -> getDocumentoPendienteOnline(claseDocumento,nroDocumento),
                                context.getString(R.string.recopilando),0);
                    }));
        }
    }

    @Override
    public BodyDetalleDocumentoPendiente completeData(String codTipoDocumento, String codClaseDocumento,
                                                      String numDocumento, int idDocumento, int idClaseDocumento) {
        BodyDetalleDocumentoPendiente body = new BodyDetalleDocumentoPendiente();
        body.setIdDocumento(idDocumento);
        body.setCodClaseDocumento(codClaseDocumento);
        body.setCodTipoDocumento(codTipoDocumento);
        body.setNumDocumento(numDocumento);
        body.setCodEmpresa(preferenceManager.getConfig().getCodEmpresa());
        body.setCodAlmacen(preferenceManager.getConfig().getCodAlmacen());
        body.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
        body.setIdClaseDocumento(idClaseDocumento);
        body.setClaseDocFiltro(null);
        return body;
    }

    @Override
    public void insertSelectedDocumentos(List<Documento> documentoList) {
        dataSourceRepository.insertDocumentos(documentoList);
    }

    @Override
    public void bloquearSelectedDocumentos(List<Documento> documentoList) {
        if (bloqueoCount < documentoList.size()){
            disposable.add(dataSourceRepository.bloquearDocumento(documentoList.get(bloqueoCount).getIdDocumento())
                    .subscribeOn(Schedulers.single())
                    .subscribe(msgResponse -> {
                        if (msgResponse.getMsgError().contains("OK")){
                            bloqueoCount += 1;
                            bloquearSelectedDocumentos(documentoList);
                        }
                    }, throwable -> {
                        bloquearSelectedDocumentos(documentoList);
                    }));
        } else {
            bloqueoCount = 0;
        }
    }
}
