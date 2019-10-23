package com.dms.almacensoft.features.recepciondespacho.documentos;

import com.dms.almacensoft.data.entities.dbtransact.Documento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import java.util.List;

interface DocumentosContract {

    interface View extends BaseContractView{

        void setUpClasesSpinner(String almacen, List<String> documentos);

        void setUpDocumentosPendientes(List<Documento> documentos);

        void onError(CustomDialog.IButton iButton, String message, int type);

        void showNoInternet(CustomDialog.IButton iButton, String message);

    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<DocumentosContract.View>{

        void getDataClase(String type);

        void getDocumentoPendienteOnline(String claseDocumento, String nroDocumento);

        BodyDetalleDocumentoPendiente getDataDetalleDocumento();

        void setConSinDoc(int value);
    }
}
