package com.dms.almacensoft.features.recepciondespacho.sindocumento;

import com.dms.almacensoft.data.entities.dbalmacen.ClaseDocumento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyCrearDocumento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import java.util.List;

interface SinDocumentoConfigContract {

    interface View extends BaseContractView {

        void setUpViews(List<ClaseDocumento> list);

        void showDetalle(BodyDetalleDocumentoPendiente body);

        void onError(CustomDialog.IButton event, String message, int type);
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<SinDocumentoConfigContract.View>{

        void getDataClase(String type);

        void crearDocumento(BodyCrearDocumento body);

        void setConSinDoc(int value);

        void verifyDocumento(String numDocumento);

        void showDetalle();

    }
}
