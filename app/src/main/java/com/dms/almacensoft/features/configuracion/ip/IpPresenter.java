package com.dms.almacensoft.features.configuracion.ip;

import android.content.Context;

import com.dms.almacensoft.data.PreferenceManager;

import javax.inject.Inject;

/**
 * {@link IpPresenter} realiza las operaciones para {@link IpActivity}
 */

public class IpPresenter implements IpContract.Presenter {

    private IpContract.view view;
    private Context context;
    private PreferenceManager preferenceManager;

    @Inject
    public IpPresenter (Context context,PreferenceManager preferenceManager){
        this.context = context;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void onViewCreated() {
        getView().displayIp(preferenceManager.getBaseURL());
    }

    /**
     * Guarda la dirección base del WS en las preferencias del equipo
     * @param ip es la dirección base ingresada
     */
    @Override
    public void updateIp(String ip) {
        preferenceManager.saveBaseURL(ip);
    }

    @Override
    public void attachView(IpContract.view mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public IpContract.view getView() {
        return view;
    }
}
