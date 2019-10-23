package com.dms.almacensoft.features.sync;

import com.dms.almacensoft.data.entities.dbalmacen.Producto;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.features.shared.BaseContractView;

import java.util.List;

interface SyncContract {

    interface View extends BaseContractView {

        void setupDialog(String title, String message, boolean download);

        void startSync();

        void updateProgress(int doneItems, int totalItems);

        void syncSuccess();

        void syncError(String message);
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<SyncContract.View> {

        void setupView();

        void startSync(List<BodyDetalleDocumentoPendiente> bodyDetalleList);

        void syncSuccess();

        void startDownload(List<BodyDetalleDocumentoPendiente> bodyDetalleList);

        void getDetalleDocumentos(List<BodyDetalleDocumentoPendiente> bodyDetalleList);

        void countProductos();

        void countProductosServidor();

        void getUbicacion();

        void getProducto();

        void verificarInventario();

        void cargarDiferencial(int idInventario, int conteo);

        void getDocumentoExportar();

        void exportarDocumento();

        void getDetalleDocumentoExportar();

        void getLecturaDocumentoExportar();

        void exportarDetalleDocumento();

        void updateDetalleDescargado();

        void updateLecturaDescargada();

        void getDetalleDocumentoOriExportar();

        void getVerificacionExportar();

        void exportarDetalleDocumentoOri();

        void getInventarioExportar();

        void getLecturaInventarioCount();

        void getLecturaInventarioExportar();

        void exportarInventario();

        void updateLecturaInventarioDescargada();

        void enviarDatosBdCliente();

        void clearDisposable();
    }
}
