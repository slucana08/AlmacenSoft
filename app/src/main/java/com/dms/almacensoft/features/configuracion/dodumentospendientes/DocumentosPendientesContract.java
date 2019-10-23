package com.dms.almacensoft.features.configuracion.dodumentospendientes;

import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

interface DocumentosPendientesContract {

    interface View extends BaseContractView {

        void onError(CustomDialog.IButton event, String message, int type);

        void setUpViews();
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<DocumentosPendientesContract.View>{

        void getDataClasesOnline();

        void deleteDocumentos();

        void checkClaseDocumentoCount();
    }
}
