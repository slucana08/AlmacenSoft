package com.dms.almacensoft.features.impresion.copiadocumento;

import com.dms.almacensoft.data.entities.dbalmacen.ClaseDocumento;
import com.dms.almacensoft.data.models.impresion.DetalleImpresion;
import com.dms.almacensoft.data.models.impresion.DocumentosCerrados;
import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import java.util.List;

interface CopiaDocumentoContract {

    interface View extends BaseContractView {

        void setUpViews();

        void setUpDocumentosCerrados(List<DocumentosCerrados> list);

        void setUpClases(List<ClaseDocumento> list);

        void setUpDetalle(List<DetalleImpresion> list);

        void onError(CustomDialog.IButton event, String message, int type);

        void checkPrintResult(List<DetalleImpresion> list);

    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<CopiaDocumentoContract.View>{

        void getDataClase();

        void getDocumentoCerrados(int idClaseDocumento, String numDocumento);

        void getDetalle(String numDocumento, String nroGuia);

        void imprimirEtiquetas(List<DetalleImpresion> list, int nroCopias);

        void actualizarImpresion(List<DetalleImpresion> list, int nroCopias);
    }

}
