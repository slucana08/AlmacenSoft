package com.dms.almacensoft.features.principal;

import android.support.v4.app.Fragment;

import com.dms.almacensoft.data.entities.dbalmacen.Inventario;
import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import java.util.List;

interface PrincipalContract {

    interface View extends BaseContractView {

        void replaceFragment(Fragment fragment, String message);

        void showInventario(List<Inventario> inventarios);

        void onError(CustomDialog.IButton event, String message, int type);
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<PrincipalContract.View>{

        void selectFragment(int selection, String title, String type);

        boolean getGuiaCerrada();

        int getConSinDoc();

        void getInventario();

        void getEtiquetaBluetooth();

        boolean getImpresionActiva();

        boolean isModoBatch();

        String getTiempoUltimaLimpiezaBD();
    }
}
