package com.dms.almacensoft.features.configuracion;

import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

interface ConfiguracionContract {

    interface View extends BaseContractView {

        void showMainMenu();

        void onError(CustomDialog.IButton event, String message, int type);

        void checkUbicacion(boolean isDefined, int type);
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<ConfiguracionContract.View>{

        void saveConfig (Configuracion generalConfig, Configuracion impresionConfig);

        boolean checkImpresoraBluetooth();

        String getPrinterAddress();

        void crearConfig(Configuracion configuracion);

        void updateConfig(Configuracion configuracion, String codUsuario);

        void verifyUbicacion(String codUbicacion, int type);
    }
}
