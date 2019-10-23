package com.dms.almacensoft.features.configuracion.impresionbluetooth;

import com.dms.almacensoft.features.shared.BaseContractView;

interface ImpresionBluetoothContract {

    interface View extends BaseContractView {

    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<ImpresionBluetoothContract.View>{

        String getPrinterAddress();

        void savePrinterAddress(String printerAddress);
    }
}
