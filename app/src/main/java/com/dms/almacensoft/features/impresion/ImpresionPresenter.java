package com.dms.almacensoft.features.impresion;

import android.content.Context;

import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.models.Configuracion;

import javax.inject.Inject;

/**
 * {@link ImpresionPresenter} realiza el registro en preferencias para {@link ImpresionFragment}
 */

public class ImpresionPresenter implements ImpresionContract.Presenter {

    private ImpresionContract.View view;
    private Context context;
    private PreferenceManager preferenceManager;

    @Inject
    public ImpresionPresenter (Context context, PreferenceManager preferenceManager){
        this.context = context;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void attachView(ImpresionContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public ImpresionContract.View getView() {
        return view;
    }

    @Override
    public void checkType(int type) {
        if (type == 0) getView().setUpNavigationUp();
        getView().setUpViews();
    }

    @Override
    public boolean getTipoImpresora() {
        return preferenceManager.getConfig().getTipoImpresora();
    }

    @Override
    public void setTipoImpresoraRed() {
        Configuracion config = preferenceManager.getConfig();
        config.setTipoImpresora(false);
        preferenceManager.saveConfig(config);
    }
}
