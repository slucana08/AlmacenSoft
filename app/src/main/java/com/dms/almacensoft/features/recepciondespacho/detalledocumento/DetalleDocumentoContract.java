package com.dms.almacensoft.features.recepciondespacho.detalledocumento;

import com.dms.almacensoft.data.entities.dbalmacen.Producto;
import com.dms.almacensoft.data.entities.dbalmacen.Ubicacion;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyBuscarSerie;
import com.dms.almacensoft.data.models.recepciondespacho.BodyCerrarDocumento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyConsultarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.data.models.recepciondespacho.BodyRegistrarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.DetalleDocumentoLive;
import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import java.util.List;

interface DetalleDocumentoContract {

    interface View extends BaseContractView {

        void setUpViews(boolean lote, boolean serie, boolean barrido, boolean impresora, List<DetalleDocumentoLive> documentos);

        void setUpUbicacion(Ubicacion ubicacion);

        void setUpGuia(boolean started, String nroGuia);

        void checkUbicacion(List<Ubicacion> ubicacion);

        void verifyDataToSubmit();

        void updateDetalle(List<DetalleDocumentoLive> detalleDocumentoLives);

        void consultarLecturas(BodyConsultarLecturas bodyConsulta);

        void cerrarDocumento();

        void checkSerieResult(int result);

        void checkProducto(List<Producto> productos);

        void cancelarDocumento();

        void onError(CustomDialog.IButton event, String message, int type);

    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<DetalleDocumentoContract.View> {

        void getData(BodyDetalleDocumentoPendiente body, int type, int conSinDoc);

        void verifyUbicacion(String codUbicacion);

        List<DetalleDocumentoLive> orderDocument(List<DetalleDocumentoLive> list);

        void registrarLecturas(BodyRegistrarLecturas body);

        void consultarLecturas(BodyConsultarLecturas body);

        void isUbicacionSet(String type);

        void cerrarDocumento(BodyCerrarDocumento body);

        void isGuiaStarted();

        void saveGuia(int type, String nroGuia);

        boolean getGuiaCerrada();

        void verifySerie(BodyBuscarSerie body);

        void verifyProducto(String codProducto);

        void setConSinDoc(int value);

        int getConSinDoc();

        void deleteDataClase();

        void cancelarDocumento();

        void verificarDetalleDocumentoBD(BodyRegistrarLecturas body);

        void verificarCantidadDetalles(List<DetalleDocumentoLive> detalleDocumentoLiveList);

        void registrarMovimientoInterno(BodyRegistrarLecturas body, int idDetalleDocumento);

        void registrarLecturaBD(BodyRegistrarLecturas body);

        void cerrarDetalleDocumentoBD(BodyCerrarDocumento bodyCerrar);

        void cerrarDetalleDocumentoOri(List<DetalleDocumento> detalleDocumentos,BodyCerrarDocumento bodyCerrar);

        void finalizarDocumento(List<DetalleDocumento> detalleDocumentos,BodyCerrarDocumento bodyCerrar);

        boolean isBatchMode();

        void setImprimirBluetooth();

        String getPrinterAddress();

        void getProductoByDetalle(List<DetalleDocumentoLive> detalleDocumentoLiveList);

        void cerrarMovimientoDespacho(List<DetalleDocumento> detalleDocumentos);

        void getLecturasDespacho(BodyCerrarDocumento bodyCerrar);
    }
}
