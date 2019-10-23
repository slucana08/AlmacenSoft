package com.dms.almacensoft.features.splash;

import android.content.Context;

import com.dms.almacensoft.data.PreferenceManager;

import java.util.Date;

import javax.inject.Inject;

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View view;
    private Context context;
    private PreferenceManager preferenceManager;

    @Inject
    public SplashPresenter (Context context, PreferenceManager preferenceManager){
        this.context = context;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void attachView(SplashContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public SplashContract.View getView() {
        return view;
    }


    @Override
    public void startTimer() {
        if (preferenceManager.getFirstLoad()){
            preferenceManager.setUltimaLimpiezaBD(new Date().getTime());
            preferenceManager.setFirstLoad(false);
        }
        getView().showLogin(2000);
    }
}
