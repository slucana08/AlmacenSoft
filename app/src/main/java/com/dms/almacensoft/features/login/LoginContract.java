package com.dms.almacensoft.features.login;

import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

interface LoginContract {

    interface View extends BaseContractView{

        void showAlmacenSelección();

        void showLicenciaResult(int action);

        void clearValues();

        void showNoInternet(CustomDialog.IButton iButton, String message);

        void onError(CustomDialog.IButton iButton, String message, int type);

    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<LoginContract.View>{

        void processLogin(String codUsuario, String claveUsuario);

        void getConfig(String idUsuario, String codUsuario, String perfilTipo);

        void listAlmacen(String idUsuario);

        void verificarLicencia(String imei, String codUsuario, String claveUsuario);

        void verificarLicencia(String imei);

        void registrarLicencia(String imei);

        boolean isModoBatch();
    }
}
