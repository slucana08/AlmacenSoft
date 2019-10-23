package com.dms.almacensoft.features.configuracion.impresion;

import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.features.shared.BaseContractView;

public interface ImpresionConfigContract {

    interface View extends BaseContractView {

        void setUpConfig(Configuracion config);
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<ImpresionConfigContract.View>{

        void getConfig();

        String getPrinterAddress();

        void savePrinterAddress(String printerAddress);
    }
}
