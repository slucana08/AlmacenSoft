package com.dms.almacensoft.features.configuracion.general;

import com.dms.almacensoft.data.entities.dbalmacen.Almacen;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumento;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import java.util.List;

interface GeneralConfigContract {

    interface View extends BaseContractView {

        void setUpSpinner(List<Almacen> almacenList, int idAlmacen);

        void setUpConfig(Configuracion config);

        void updateUbicacion(boolean result, int type);

        void onError(CustomDialog.IButton event, String message, int type);

        void procesarLimpiezaBD();

        void enviarDatos(boolean isChecked, int type);
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<GeneralConfigContract.View> {

        void listAlmacen();

        void getConfig();

        void saveAlmacenData(int idAlmacen, String dscAlmacen, String codAlmacen, String codEmpresa,int idEmpresa);

        void verifyUbicacion(String codUbicacion,int type, int idAlmacen);

        void setBatchMode(boolean isBatch);

        void limpiarBD();

        boolean getBatchMode();

        String getTiempoUltimaLimpiezaBD();

        void countDespachoCerrar(boolean isChecked, int type);

        void cerrarLecturasDespacho(boolean isChecked, int type, List<DetalleDocumento> detalleDocumentoList);

        void cerrarDetalleDespacho(boolean isChecked, int type, List<DetalleDocumento> detalleDocumentoList);

        void cerrarMovimientoDespacho(List<DetalleDocumento> detalleDocumentoList);

        void orderDocumentos(boolean isChecked, int type, List<DetalleDocumento> detalleDocumentoList);

        void cerrarDocumentoDespacho(boolean isChecked, int type, List<List<DetalleDocumento>> sortedList, List<Integer> idDocumentoList);

        void finalizarDocumento(List<DetalleDocumento> detalleDocumentos);

        void cerrarDetalleDocumentoOri(List<DetalleDocumento> detalleDocumentos);
    }
}
