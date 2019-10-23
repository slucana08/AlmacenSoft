package com.dms.almacensoft.features.inventario.diferencial;

import com.dms.almacensoft.data.models.inventario.BodyRegistrarInventario;
import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import java.util.List;

interface DifeferencialContract {

    interface View extends BaseContractView {

        void setUpDiferencial(List<HeaderDiferencial> diferencialList);

        void onError(CustomDialog.IButton event, String message, int type);

        void showRegistroScreen(BodyRegistrarInventario body);

    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<DifeferencialContract.View> {

        void getConteo();

        void getDiferencial(int type); // 0 - ubicación // 1 - producto

        void determinarRegistro(BodyRegistrarInventario body);

        void registrarDiferencial(BodyRegistrarInventario body);

        void verifyDetalleInventario(BodyRegistrarInventario body);

        void registrarLecturaBD(BodyRegistrarInventario body);
    }
}
