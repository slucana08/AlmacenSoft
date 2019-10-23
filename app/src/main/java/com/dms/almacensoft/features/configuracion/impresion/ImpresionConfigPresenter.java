package com.dms.almacensoft.features.configuracion.impresion;

import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.models.Configuracion;

import javax.inject.Inject;

/**
 * {@link ImpresionConfigPresenter} realiza el registro de datos en preferencias para {@link ImpresionConfigFragment}
 */

public class ImpresionConfigPresenter implements ImpresionConfigContract.Presenter {

    private ImpresionConfigContract.View view;
    private PreferenceManager preferenceManager;

    @Inject
    public ImpresionConfigPresenter(PreferenceManager preferenceManager){
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void getConfig() {
         getView().setUpConfig(preferenceManager.getConfig());
    }

    @Override
    public String getPrinterAddress() {
        return preferenceManager.getPrinterAddress();
    }

    @Override
    public void savePrinterAddress(String printerAddress) {
        preferenceManager.savePrinterAddress(printerAddress);
    }

    @Override
    public void attachView(ImpresionConfigContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public ImpresionConfigContract.View getView() {
        return view;
    }
}
