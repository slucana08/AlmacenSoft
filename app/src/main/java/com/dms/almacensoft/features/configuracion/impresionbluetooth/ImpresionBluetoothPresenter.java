package com.dms.almacensoft.features.configuracion.impresionbluetooth;

import android.content.Context;

import com.dms.almacensoft.data.PreferenceManager;

import javax.inject.Inject;

/**
 * {@link ImpresionBluetoothPresenter} realiza el registro en preferencias para {@link ImpresionBluetoothActivity}
 */

public class ImpresionBluetoothPresenter implements ImpresionBluetoothContract.Presenter {

    private ImpresionBluetoothContract.View view;

    private Context context;
    private PreferenceManager preferenceManager;

    @Inject
    public ImpresionBluetoothPresenter (Context context, PreferenceManager preferenceManager){
        this.context = context;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void attachView(ImpresionBluetoothContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public ImpresionBluetoothContract.View getView() {
        return view;
    }

    @Override
    public String getPrinterAddress() {
        return preferenceManager.getPrinterAddress();
    }

    @Override
    public void savePrinterAddress(String printerAddress) {
        preferenceManager.savePrinterAddress(printerAddress);
    }
}
