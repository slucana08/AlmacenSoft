package com.dms.almacensoft.features.configuracion.dodumentospendientes;

import android.content.Context;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link DocumentosPendientesPresenter} se encarga de realizar las llamadas al WS para
 * {@link DocumentosPendientesActivity}
 */

public class DocumentosPendientesPresenter implements DocumentosPendientesContract.Presenter {

    private DocumentosPendientesContract.View view;

    private Context context;
    private DataSourceRepository dataSourceRepository;
    private PreferenceManager preferenceManager;
    private CompositeDisposable disposable = new CompositeDisposable();

    private int dataClasesCount = 0;

    @Inject
    public DocumentosPendientesPresenter(Context context, DataSourceRepository dataSourceRepository,
                                         PreferenceManager preferenceManager) {
        this.context = context;
        this.dataSourceRepository = dataSourceRepository;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void attachView(DocumentosPendientesContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
        disposable.clear();
    }

    @Override
    public DocumentosPendientesContract.View getView() {
        return view;
    }

    @Override
    public void getDataClasesOnline() {
        if (!UtilMethods.checkConnection(context)) {
            ProgressDialog.dismiss();
            getView().onError(this::getDataClasesOnline,context.getString(R.string.recopilando),0);
        } else {
            disposable.add(dataSourceRepository.getAllClaseDocumentoMaestro()
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(claseDocumentos -> {
                        dataClasesCount = 0;
                        Schedulers.single().scheduleDirect(this::checkClaseDocumentoCount, 80, TimeUnit.MILLISECONDS);
                    }, throwable -> {
                        if (dataClasesCount < 2){
                            dataClasesCount += 1;
                            getView().onError(this::getDataClasesOnline,context.getString(R.string.recopilando),1);
                        } else {
                            dataClasesCount = 0;
                            ProgressDialog.dismiss();
                            getView().onError(null,null,2);
                        }
                    }));
        }
    }

    @Override
    public void checkClaseDocumentoCount(){
        disposable.add(dataSourceRepository.claseDocumentoCount()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> {
                    if (count != 0){
                        getView().setUpViews();
                        ProgressDialog.dismiss();
                    } else {
                        checkClaseDocumentoCount();
                    }
                }, throwable -> { }));
    }

    @Override
    public void deleteDocumentos() {
        dataSourceRepository.deleteDocumentos();
    }
}
