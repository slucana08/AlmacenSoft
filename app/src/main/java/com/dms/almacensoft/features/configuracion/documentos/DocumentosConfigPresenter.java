package com.dms.almacensoft.features.configuracion.documentos;

import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.models.Configuracion;

import javax.inject.Inject;

public class DocumentosConfigPresenter implements DocumentosConfigContract.Presenter{

    private DocumentosConfigContract.View view;
    private PreferenceManager preferenceManager;
    private Configuracion configuracion;

    @Inject
    public DocumentosConfigPresenter(PreferenceManager preferenceManager){
        this.preferenceManager = preferenceManager;
        this.configuracion = preferenceManager.getConfig();
    }

    @Override
    public void getConfig() {
        getView().setUpConfig(configuracion);
    }

    @Override
    public void attachView(DocumentosConfigContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public DocumentosConfigContract.View getView() {
        return view;
    }
}
