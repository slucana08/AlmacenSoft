package com.dms.almacensoft.data.source;

import com.dms.almacensoft.data.entities.dbalmacen.Almacen;
import com.dms.almacensoft.data.entities.dbalmacen.AlmacenVirtual;
import com.dms.almacensoft.data.entities.dbalmacen.CajaProducto;
import com.dms.almacensoft.data.entities.dbalmacen.ClaseDocumento;
import com.dms.almacensoft.data.entities.dbalmacen.Clasificacion;
import com.dms.almacensoft.data.entities.dbalmacen.Cliente;
import com.dms.almacensoft.data.entities.dbalmacen.Destino;
import com.dms.almacensoft.data.entities.dbalmacen.Empresa;
import com.dms.almacensoft.data.entities.dbalmacen.Inventario;
import com.dms.almacensoft.data.entities.dbalmacen.Isla;
import com.dms.almacensoft.data.entities.dbalmacen.JerarquiaClasificacion;
import com.dms.almacensoft.data.entities.dbalmacen.Presentacion;
import com.dms.almacensoft.data.entities.dbalmacen.Producto;
import com.dms.almacensoft.data.entities.dbalmacen.ProductoUbicacion;
import com.dms.almacensoft.data.entities.dbalmacen.StockAlmacen;
import com.dms.almacensoft.data.entities.dbalmacen.Ubicacion;
import com.dms.almacensoft.data.entities.dbalmacen.UnidadMedida;
import com.dms.almacensoft.data.entities.dbalmacen.Usuario;
import com.dms.almacensoft.data.entities.dbalmacen.UsuarioxAlmacen;
import com.dms.almacensoft.data.entities.dbalmacen.Vehiculo;
import com.dms.almacensoft.data.entities.dbalmacen.VerificacionDocumento;
import com.dms.almacensoft.data.entities.dbalmacen.Zona;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumento;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumentoOri;
import com.dms.almacensoft.data.entities.dbtransact.DetalleInventario;
import com.dms.almacensoft.data.entities.dbtransact.Documento;
import com.dms.almacensoft.data.entities.dbtransact.LecturaDocumento;
import com.dms.almacensoft.data.entities.dbtransact.LecturaInventario;
import com.dms.almacensoft.data.entities.dbtransact.MovimientoInterno;
import com.dms.almacensoft.data.entities.dbtransact.ProductoDiferencial;
import com.dms.almacensoft.data.models.BodyLicencia;
import com.dms.almacensoft.data.models.BodyLogin;
import com.dms.almacensoft.data.models.BodyUbicacion;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.data.models.EtiquetaBluetooth;
import com.dms.almacensoft.data.models.MsgResponse;
import com.dms.almacensoft.data.models.exportar.BodyDetalleDocumento;
import com.dms.almacensoft.data.models.exportar.BodyDetalleDocumentoOri;
import com.dms.almacensoft.data.models.exportar.BodyEnviarDatosBdCliente;
import com.dms.almacensoft.data.models.exportar.BodyInventario;
import com.dms.almacensoft.data.models.impresion.BodyImprimirEtiqueta;
import com.dms.almacensoft.data.models.impresion.DetalleImpresion;
import com.dms.almacensoft.data.models.impresion.DocumentosCerrados;
import com.dms.almacensoft.data.models.inventario.BodyConsultaLecturaInventario;
import com.dms.almacensoft.data.models.inventario.BodyEliminarDetalle;
import com.dms.almacensoft.data.models.inventario.BodyRegistrarInventario;
import com.dms.almacensoft.data.models.inventario.HijoConsultaInventario;
import com.dms.almacensoft.data.models.inventario.SerieResponseInventario;
import com.dms.almacensoft.data.models.recepciondespacho.BodyBuscarSerie;
import com.dms.almacensoft.data.models.recepciondespacho.BodyCancelarDocumento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyCerrarDocumento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyConsultarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.BodyCrearDocumento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDocumentoPendiente;
import com.dms.almacensoft.data.models.recepciondespacho.BodyEliminarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.BodyRegistrarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.DetalleDocumentoLive;
import com.dms.almacensoft.data.models.recepciondespacho.HijoConsulta;
import com.dms.almacensoft.data.models.recepciondespacho.SerieResponse;

import java.util.List;

import io.reactivex.Observable;

interface DataSource {

    interface DataSourceLocal extends DataSource.DataSourceShared{

        void insertAllEmpresa(List<Empresa> empresa);

        void insertAllUsuario(List<Usuario> usuarios);

        void insertAllUsuarioxAlmacen(List<UsuarioxAlmacen> usuarioxAlmacen);

        void insertAllAlmacen(List<Almacen> almacen);

        void insertAllUbicacion(List<Ubicacion> ubicacion);

        void insertAllClaseDocumento(List<ClaseDocumento> claseDocumentos);

        void insertAllProducto(List<Producto> productos);

        void deleteProductos();

        Observable<Integer> countProductos();

        void insertAllAlmacenVirtual(List<AlmacenVirtual> almacenVirtual);

        void insertAllJerarquiaClasificacion(List<JerarquiaClasificacion> jerarquiaClasificacion);

        void insertAllClasificacion(List<Clasificacion> clasificacion);

        void insertAllDestino(List<Destino> destinos);

        void insertAllCliente(List<Cliente> clientes);

        void insertAllVehiculo(List<Vehiculo> vehiculos);

        void insertAllZona(List<Zona> zonas);

        void insertAllIsla(List<Isla> islas);

        void insertAllProductoUbicacion(List<ProductoUbicacion> productoUbicacion);

        void insertAllCajaProducto(List<CajaProducto> cajaProductos);

        void insertAllUnidadMedida(List<UnidadMedida> unidadMedidas);

        void insertAllPresentacion(List<Presentacion> presentacion);

        void insertAllStockAlmacen(List<StockAlmacen> stockAlmacen);

        void insertAllInventario(List<Inventario> inventarios);

        void insertDetalleInventario(List<DetalleInventario> detalleInventarios);

        void deleteDetalleDocumentoOri();

        void insertDetalleDocumentoOri(List<DetalleDocumentoOri> entities);

        void insertProductoDiferencial(List<ProductoDiferencial> entities);
    }

    interface DataSourceShared {

        // Carga Maestros

        void insertAllUbicacion(List<Ubicacion> ubicacion);

        void insertAllProducto(List<Producto> productos);

        void deleteProductos();

        Observable<Integer> countProductos();

        // Procesos

        Observable<List<Almacen>> getListAlmacen();

        Observable<List<Inventario>> getListInventarioDB();

        // Recepcion - Despacho

        void deleteClaseDocumento();

        //------------ MODO BATCH ------------------------ //

        // Recepcion - Despacho

        Observable<List<ClaseDocumento>> getListClaseDocumentoBatch(String codTipoDocumento, String flgConSinDoc);

        void insertDocumentos(List<Documento> documentos);

        void deleteDocumentos();

        Observable<List<Documento>> getListDocumento(String codTipoDocumento, String codClaseDocumento,
                                                            String numDocumento, int idAlmacen);

        Observable<List<Documento>> getListDocumento(String codTipoDocumento, String codClaseDocumento, int idAlmacen);

        Observable<List<DetalleDocumentoLive>> getDetalleListBD(int idDocumento);

        Observable<List<Ubicacion>> verifyUbicacionBD(int idAlmacen, String codUbicacion);

        Observable<List<Producto>> verifyProductoDB(String codProducto);

        Observable<List<SerieResponse>> verifySerieRecepcionBD(int idProducto, String serieProducto, int idDocumento);

        Observable<Integer> verifyDetalleDocumentoBD(String loteProducto, String numDocInterno, int linea,
                                                     int idDocumento, int idProducto);

        void updateDetalleDocumentoBD(double ctdAtendidaUpdate, String loteProducto, String numDocInterno, int linea,
                                      int idDocumento,String codUsuarioModificacion, String fchModificacion,
                                      int idProducto);

        void insertDetalleDocumentoBD(DetalleDocumento detalleDocumento);

        void insertLecturaDocumentoBD(LecturaDocumento lecturaDocumento);

        Observable<Integer> getIDDetalleDocumentoBD(String loteProducto, String numDocInterno, int linea,
                                                    int idDocumento, int idProducto);

        Observable<List<DetalleDocumento>> getDetalleDocumentoByProductoBD(int idProducto, int idDocumento);

        void limpiarBdAdmin();

        Observable<Integer> countDetalle();

        Observable<Integer> countDetalleInventario();

        Observable<Integer> countLectura();

        Observable<Integer> countMovimiento();

        Observable<Integer> countDetalleOri();

        Observable<Integer> countDocumento();

        void insertMovimientoInterno(MovimientoInterno movimientoInterno);

        Observable<List<Integer>> getIDLecturaDocumento(int idDetalleDocumento, int idProducto,
                                                               String loteProducto, String serieProducto);

        Observable<List<HijoConsulta>> getLecturasBD(String codTipoDocumento, int idClaseDocumento, String numDocumento,
                                                            String flgActivo, String flgEnviado, String codUsuarioRegistro);

        void updateDetalleDocumentoEliminacion(double ctdAtendidaRestar, int idDetalleDocumento,
                                                      String codUsuario, String fchModificacion);

        void updateMovimientoEliminacion(String codUsuario,String fchModificacion, int idLecturaDocumento);

        void deleteLecturaBD(String codUsuario, String fchModificacion, String idTerminal, String fchRegistroTerminal,
                                    int idDetalleDocumento, int idLecturaDocumento);

        Observable<Integer> getLecturasByIdDetalle(int idDetalleDocumento);

        void cerrarDetalleDocumentoEliminacion(String codUsuario,String fchModificacion,int idDetalleDocumento);

        void cerrarLecturaDocumento(int idLecturaDocumento, String fchModificacion, String guia);

        void cerrarDetalleDocumento(String codUsuario, String fchModificacion, int idDetalleDocumento);

        Observable<List<Integer>> getIdLecturaDocumentoByIdDetalle(int idDetalleDocumento);

        void cerrarMovimiento(int idLecturaDocumento, String codUsuario, String fchModificacion);

        void cerrarDocumento(String codUsuario, String fchModificacion, int idDocumento);

        Observable<List<LecturaDocumento>> getLecturaDocumentoCerrar(String guia);

        Observable<List<DetalleDocumento>> getDetalleDocumentoCerrar(int idDocumento, String numDocInterno);

        Observable<List<DetalleDocumentoOri>> getDetalleDocumentoOri(int idDocumento);

        void insertDetalleDocumentoOri(List<DetalleDocumentoOri> entities);

        void finalizarDetalleDocumento(String codUsuario, String fchModificacion, int idDetalleDocumento);

        Observable<Integer> verifyDocumento(String numDocumento);

        void insertDocumento(Documento documento);

        Observable<Integer> getIdDocumento(String numDocumento);

        Observable<List<DetalleDocumento>> getDetalleDocumentoByDocumentoBD(int idDocumento);

        Observable<List<Producto>> getProductoByID(int idProducto);

        void cerrarLecturaDocumentoByIdDetalle(int idDetalleDocumento, String codUsuario, String fchModificacion);

        Observable<Integer> getLecturasCountBD(String codTipoDocumento, int idClaseDocumento, String numDocumento,
                                                      String flgActivo, String flgEnviado, String codUsuarioRegistro);

        void cancelarDocumentoBD(String codUsuario, String fchModificacion, int idDocumento);

        // IMPRESION

        Observable<List<String>> getGuiaCerradas(String numDocumento, int idClaseDocumento);

        Observable<List<DetalleImpresion>> getDetalleImpresionBD(String numDocumento, String numDocInterno);

        Observable<List<DetalleImpresion>> getLecturaImpresion(String numDocumento, String numDocInterno);

        //INVENTARIO

        Observable<List<Usuario>> getUsuario(String codUsuario);

        Observable<List<Almacen>> getAllAlmacenByUsuarioBD(int idUsuario);

        Observable<List<Inventario>> getAllInventarioByAlmacen(int idAlmacen);

        Observable<Integer> getLecturasInventarioCountBD(int idInventario, String codUsuarioRegistro);

        Observable<List<ProductoDiferencial>> getAllProductoDiferencial();

        Observable<Integer> getProductoDiferencialCount();

        Observable<List<SerieResponseInventario>> obtenerSerieInventarioBD(int idInventario, int idProducto,
                                                                                  String serieProducto);

        Observable<List<HijoConsultaInventario>> getLecturaInventarioByUbicacion(int idInventario,
                                                                                        int idUbicacion,
                                                                                        String codUsuarioRegistro);

        Observable<List<HijoConsultaInventario>> getLecturaInventarioByProducto(int idInventario,
                                                                                       int idProducto,
                                                                                       String codUsuarioRegistro);

        Observable<List<HijoConsultaInventario>> getLecturaInventarioByLote(int idInventario,
                                                                                   String loteProducto,
                                                                                   String codUsuarioRegistro);

        Observable<List<HijoConsultaInventario>> getLecturaInventarioBySerie(int idInventario,
                                                                                    String serieProducto,
                                                                                    String codUsuarioRegistro);

        Observable<Integer> verifyDetalleInventarioBD(int idInventario, int idUbicacion, int idProducto,
                                                             String loteProducto);

        void updateDetalleInventarioBD(double stockInventariado, String codUsuarioModificacion, String fchModificacion,
                                              int idInventario,int idUbicacion,int idProducto,String loteProducto);

        void insertDetalleInventario(DetalleInventario detalleInventario);

        void insertLecturaInventario(LecturaInventario lecturaInventario);

        Observable<Integer> getIdDetalleInventarioBD(int idInventario, int idUbicacion, int idProducto,
                                                            String loteProducto);

        Observable<LecturaInventario> getLecturaEliminar(int idLecturaInventario, String codUsuarioRegistro);

        void eliminarLectura(int idLecturaInventario, String codUsuarioRegistro);

        void updateDetalleLecturaEliminar(double cantidadEliminar, int idDetalleInventario,
                                                 String codUsuarioModificacion, String fchModificacion);

        Observable<List<LecturaInventario>> getLecturaEliminarByDetalle(int idDetalleInventario,
                                                                               String codUsuarioRegistro);

        void eliminarLecturasAdmin();

        void eliminarDetallesAdmin(String codUsuarioModificacion, String fchModificacion);

        void updateDocumentoTrabajado(String codUsuarioModificacion, String fchModificacion, int idDocumento);

        Observable<Integer> countLecturasInventarioBD();

        // -----  EXPORTACION DE TABLAS ----- ////

        Observable<List<Documento>> getAllDocumentosExportar();

        Observable<List<DetalleDocumento>> getAllDetalleExportar();

        Observable<List<LecturaDocumento>> getAllLecturaExportar();

        void updateLecturaDescargada(String fchDescargado,String codUsuarioModificacion, String fchModificacion,
                                            int idLecturaDocumento);

        Observable<List<DetalleDocumentoOri>> getAllDetalleDocumentoOriExportar();

        Observable<List<VerificacionDocumento>> getAllVerificacionExportar();

        Observable<List<Inventario>> getAllInventarioExportar();

        Observable<Integer> getCountLecturaInventarioExportar();

        Observable<List<LecturaInventario>> getLecturaInventarioExportar(int limit);

        void updateLecturaInventarioDescargada(String fchDescargado,String codUsuarioModificacion, String fchModificacion,
                                                      int idLecturaInventario);

        void updateDetalleDescargado(String codUsuarioModificacion, String fchModificacion, int idDetalleDocumento);

        Observable<Integer> claseDocumentoCount();

        Observable<List<DetalleDocumento>> getDetalleDespachoCerrar();

        Observable<String> getConSinDoc(int idDocumento);
    }

    interface DataSourceRemote{

        Observable<List<Almacen>> getAllAlmacen();

        Observable<List<Empresa>> getAllEmpresa();

        Observable<List<Usuario>> getAllUsuario();

        Observable<List<UsuarioxAlmacen>> getAllUsuarioxAlmacen();

        Observable<List<Ubicacion>> getAllUbicacion();

        Observable<List<ClaseDocumento>> getAllClaseDocumentoMaestro();

        Observable<List<Producto>> getAllProducto(int pagIni, int pagFin);

        Observable<Integer []> getProductoCount();

        Observable<List<AlmacenVirtual>> getAllAlmacenVirtual();

        Observable<List<JerarquiaClasificacion>> getAllJerarquiaClasificacion();

        Observable<List<Clasificacion>> getAllClasificacion();

        Observable<List<Destino>> getAllDestino();

        Observable<List<Cliente>> getAllCliente();

        Observable<List<Vehiculo>> getAllVehiculo();

        Observable<List<Zona>> getAllZona();

        Observable<List<Isla>> getAllIsla();

        Observable<List<ProductoUbicacion>> getAllProductoUbicacion();

        Observable<List<CajaProducto>> getAllCajaProducto();

        Observable<List<UnidadMedida>> getAllUnidadMedida();

        Observable<List<Presentacion>> getAllPresentacion();

        Observable<List<StockAlmacen>> getAllStockAlmacen();

        // Procesos

        Observable<MsgResponse> processLogin(BodyLogin body);

        Observable<List<Configuracion>> getConfiguracioByUser(String codUsuario);

        Observable<MsgResponse> sendConfiguracionByUser(Configuracion configuracion, String codUsuario);

        Observable<MsgResponse> sendConfiguracion(Configuracion configuracion);

        Observable<List<Almacen>> getAllAlmacenByUsuario(String idUsuario);

        Observable<MsgResponse> verificarLicencia(String imei);

        Observable<MsgResponse> registrarLicencia(BodyLicencia body);

        Observable<MsgResponse> bloquearDocumento(int idDocumento);

        // Recepcion - Despacho

        Observable<List<ClaseDocumento>> getClaseDocumentoModulo(String codTdoc,boolean conSinDoc);

        Observable<List<Documento>> getDocumentosPendientes(BodyDocumentoPendiente body);

        Observable<List<DetalleDocumentoLive>> getDetalleDocumento(BodyDetalleDocumentoPendiente body);

        Observable<List<DetalleDocumentoOri>> getDetalleDocumentoBatch(BodyDetalleDocumentoPendiente body);

        Observable<List<Ubicacion>> getUbicacion(BodyUbicacion body);

        Observable<MsgResponse> registrarLecturasOnline(BodyRegistrarLecturas body);

        Observable<List<HijoConsulta>> getAllLecturasOnline(BodyConsultarLecturas body);

        Observable<MsgResponse> deteleLecturas(BodyEliminarLecturas body);

        Observable<MsgResponse> cerrarDocumentoInterno(BodyCerrarDocumento body);

        Observable<List<SerieResponse>> getSerie(BodyBuscarSerie body);

        Observable<MsgResponse> crearDocumento(BodyCrearDocumento body);

        Observable<MsgResponse> cancelarDocumento(BodyCancelarDocumento body, int idDocumento);

        // Inventario

        Observable<List<Inventario>> getInventarioAbierto(int idAlmacen);

        Observable<List<Producto>> getProducto(String codProducto);

        Observable<MsgResponse> getLecturasInventarioCount(int idInventario, String codUsuario);

        Observable<MsgResponse> registrarLecturaInventario(BodyRegistrarInventario body);

        Observable<List<HijoConsultaInventario>> consultarLecturaInventario(BodyConsultaLecturaInventario body);

        Observable<MsgResponse> deleteLecturaInventario(int idLecturaInventario, String codUsuario);

        Observable<MsgResponse> deleteDetalleInventario(BodyEliminarDetalle body, int idDetalleInventario);

        Observable<MsgResponse> deleteTotalInventario(int idInventario, String codUsuario);

        Observable<List<SerieResponseInventario>> getSerieInventario(int idInventario, int idProducto, String serieProducto);

        Observable<List<ProductoDiferencial>> getDiferencial(int idInventario, int conteo);

        Observable<List<DetalleInventario>> getDetalleInventarioAbierto();

        // IMPRESION

        Observable<List<DocumentosCerrados>> getDocumentoCerrado(String numDocumento, int idClassDoc);

        Observable<List<DetalleImpresion>> getDetalleImpresion(String numDocumento, String docInterno, boolean flgDetLec);

        Observable<Boolean> imprimirEtiqueta(BodyImprimirEtiqueta body);

        Observable<Boolean> actualizarImpresion(int idImpresion, boolean flgDetLec);

        Observable<EtiquetaBluetooth> getEtiquetaBluetooth();

        // EXPORTAR

        Observable<MsgResponse> exportarDocumento(List<Documento> body);

        Observable<MsgResponse> exportarDetalleDocumentoOri(BodyDetalleDocumentoOri body);

        Observable<MsgResponse> exportarDetalleDocumento(BodyDetalleDocumento body);

        Observable<MsgResponse> exportarInventario(BodyInventario body);

        Observable<MsgResponse> enviarDatosBdCliente(BodyEnviarDatosBdCliente body, String codAlmacen, String codEmpresa);
    }

}
