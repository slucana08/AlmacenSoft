package com.dms.almacensoft.features.impresion.etiquetalibre;

import com.dms.almacensoft.data.entities.dbalmacen.Producto;
import com.dms.almacensoft.data.models.impresion.BodyImprimirEtiqueta;
import com.dms.almacensoft.data.models.impresion.DetalleImpresion;
import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import java.util.List;

interface EtiquetaLibreContract {

    interface View extends BaseContractView {

        void setUpViews();

        void setUpProducto(List<Producto> list);

        void onError(CustomDialog.IButton event, String message, int type);

        void checkPrintResult();

    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<EtiquetaLibreContract.View>{

        void getProducto(String producto);

        void imprimirEtiquetas(BodyImprimirEtiqueta etiqueta);

    }

}
