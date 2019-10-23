package com.dms.almacensoft.data.source.remote;

import com.dms.almacensoft.data.entities.dbalmacen.Almacen;
import com.dms.almacensoft.data.entities.dbalmacen.AlmacenVirtual;
import com.dms.almacensoft.data.entities.dbalmacen.CajaProducto;
import com.dms.almacensoft.data.entities.dbalmacen.ClaseDocumento;
import com.dms.almacensoft.data.entities.dbalmacen.Clasificacion;
import com.dms.almacensoft.data.entities.dbalmacen.Cliente;
import com.dms.almacensoft.data.entities.dbalmacen.Destino;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumentoOri;
import com.dms.almacensoft.data.entities.dbtransact.DetalleInventario;
import com.dms.almacensoft.data.entities.dbtransact.Documento;
import com.dms.almacensoft.data.entities.dbalmacen.Empresa;
import com.dms.almacensoft.data.entities.dbalmacen.Inventario;
import com.dms.almacensoft.data.entities.dbalmacen.Isla;
import com.dms.almacensoft.data.entities.dbalmacen.JerarquiaClasificacion;
import com.dms.almacensoft.data.entities.dbalmacen.Presentacion;
import com.dms.almacensoft.data.entities.dbalmacen.Producto;
import com.dms.almacensoft.data.entities.dbtransact.MovimientoInterno;
import com.dms.almacensoft.data.models.EtiquetaBluetooth;
import com.dms.almacensoft.data.entities.dbtransact.ProductoDiferencial;
import com.dms.almacensoft.data.entities.dbalmacen.ProductoUbicacion;
import com.dms.almacensoft.data.entities.dbalmacen.StockAlmacen;
import com.dms.almacensoft.data.entities.dbalmacen.Ubicacion;
import com.dms.almacensoft.data.entities.dbalmacen.UnidadMedida;
import com.dms.almacensoft.data.entities.dbalmacen.Usuario;
import com.dms.almacensoft.data.entities.dbalmacen.UsuarioxAlmacen;
import com.dms.almacensoft.data.entities.dbalmacen.Vehiculo;
import com.dms.almacensoft.data.entities.dbalmacen.Zona;
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
import com.dms.almacensoft.data.models.BodyLicencia;
import com.dms.almacensoft.data.models.BodyLogin;
import com.dms.almacensoft.data.models.recepciondespacho.BodyEliminarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.BodyRegistrarLecturas;
import com.dms.almacensoft.data.models.BodyUbicacion;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.data.models.recepciondespacho.DetalleDocumentoLive;
import com.dms.almacensoft.data.models.recepciondespacho.HijoConsulta;
import com.dms.almacensoft.data.models.MsgResponse;
import com.dms.almacensoft.data.models.recepciondespacho.SerieResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebService {

    @GET(Urls.CARGA_ALMACEN)
    Observable<List<Almacen>> getAllAlmacen();

    @GET(Urls.CARGA_EMPRESA)
    Observable<List<Empresa>> getAllEmpresa();

    @GET(Urls.CARGA_USUARIOS)
    Observable<List<Usuario>> getAllUsuarios();

    @GET(Urls.CARGA_USUARIOSXALMACEN)
    Observable<List<UsuarioxAlmacen>> getAllUsuarioXAlmacen();

    @GET(Urls.CARGA_UBICACIONES)
    Observable<List<Ubicacion>> getAllUbicacion();

    @GET(Urls.CARGA_CLASE_DOCUMENTOS)
    Observable<List<ClaseDocumento>> getAllClaseDocumentoMaestro();

    @GET(Urls.CARGA_PRODUCTO)
    Observable<List<Producto>> getAllProducto(@Path(Urls.PATH_ID) int pagIni, @Path(Urls.PATH_ID_3) int pagFin);

    @GET(Urls.CANTIDAD_PRODUCTO)
    Observable<Integer []> getProductoCount();

    @GET(Urls.CARGA_ALMACEN_VIRTUAL)
    Observable<List<AlmacenVirtual>> getAllAlmacenVirtual();

    @GET(Urls.CARGA_JERARQUIA_CLASIFICACION)
    Observable<List<JerarquiaClasificacion>> getAllJerarquiaClasificacion();

    @GET(Urls.CARGA_CLASIFICACION)
    Observable<List<Clasificacion>> getAllClasificacion();

    @GET(Urls.CARGA_DESTINOS)
    Observable<List<Destino>> getAllDestino();

    @GET(Urls.CARGA_CLIENTES)
    Observable<List<Cliente>> getAllCliente();

    @GET(Urls.CARGA_VEHICULOS)
    Observable<List<Vehiculo>> getAllVehiculo();

    @GET(Urls.CARGA_ZONA)
    Observable<List<Zona>> getAllZona();

    @GET(Urls.CARGA_ISLA)
    Observable<List<Isla>> getAllIsla();

    @GET(Urls.CARGA_PRODUCTO_UBICACION)
    Observable<List<ProductoUbicacion>> getAllProductoUbicacion();

    @GET(Urls.CARGA_CAJA_PRODUCTO)
    Observable<List<CajaProducto>> getAllCajaProducto();

    @GET(Urls.CARGA_UNIDAD_MEDIDA)
    Observable<List<UnidadMedida>> getAllUnidadMedida();

    @GET(Urls.CARGA_PRESENTACION)
    Observable<List<Presentacion>> getAllPresentacion();

    @GET(Urls.CARGA_STOCK_ALMACEN)
    Observable<List<StockAlmacen>> getAllStockAlmacen();

    // Procesos

    @POST(Urls.USUARIO_LOGIN)
    Observable<MsgResponse> processLogin(@Body BodyLogin body);

    @GET(Urls.CARGA_CONFIGURACIONES)
    Observable<List<Configuracion>> getConfiguracioByUser(@Path(Urls.PATH_ID) String codUsuario);

    @POST(Urls.GUARDA_CONFIGURACIONES_EXISTE)
    Observable<MsgResponse> sendConfiguracionByUser(@Body Configuracion configuracion, @Path(Urls.PATH_ID) String codUsuario);

    @POST(Urls.GUARDA_CONFIGURACIONES)
    Observable<MsgResponse> sendConfiguracion(@Body Configuracion configuracion);

    @GET(Urls.CARGA_ALMACEN_LINEA)
    Observable<List<Almacen>> getAllAlmacenByUsuario(@Path(Urls.PATH_ID) String idUsuario);

    @GET(Urls.VERIFICAR_LICENCIA)
    Observable<MsgResponse> verificarLicencia(@Path(Urls.PATH_ID) String imei);

    @POST(Urls.REGISTRAR_LICENCIA)
    Observable<MsgResponse> registrarLicencia(@Body BodyLicencia body);

    @GET(Urls.BLOQUEAR_DOCUMENTO)
    Observable<MsgResponse> bloquearDocumento(@Path(Urls.PATH_ID) int idDocumento);

    // Recepcion - Despacho
    @GET(Urls.OBTENER_CLASE_DOCUMENTO)
    Observable<List<ClaseDocumento>> getClaseDocumentoModulo(@Query("cod_tdoc") String codTdoc,
                                                             @Query ("consindoc") boolean conSinDoc);

    @POST(Urls.OBTENER_DOCUMENTOS_PENDIENTES)
    Observable<List<Documento>> getDocumentosPendientes(@Body BodyDocumentoPendiente body);

    @POST(Urls.OBTENER_DETALLE_DOCUMENTO)
    Observable<List<DetalleDocumentoLive>> getDetalleDocumento(@Body BodyDetalleDocumentoPendiente body);

    @POST(Urls.OBTENER_DETALLE_DOCUMENTO)
    Observable<List<DetalleDocumentoOri>> getDetalleDocumentoBatch(@Body BodyDetalleDocumentoPendiente body);

    @POST(Urls.OBTENER_UBICACION)
    Observable<List<Ubicacion>> getUbicacion(@Body BodyUbicacion body);

    @POST(Urls.REGISTRAR_LECTURAS)
    Observable<MsgResponse> registrarLecturasOnline(@Body BodyRegistrarLecturas body);

    @POST(Urls.CONSULTAR_LECTURAS)
    Observable<List<HijoConsulta>> getAllLecturasOnline(@Body BodyConsultarLecturas body);

    @POST(Urls.ELIMINAR_LECTURAS)
    Observable<MsgResponse> deteleLecturas(@Body BodyEliminarLecturas body);

    @POST(Urls.CERRAR_DOCUMENTO_INTERNO)
    Observable<MsgResponse> cerrarDocumentoInterno(@Body BodyCerrarDocumento body);

    @POST(Urls.OBTENER_SERIE)
    Observable<List<SerieResponse>> getSerie(@Body BodyBuscarSerie body);

    @POST(Urls.CREAR_DOCUMENTO)
    Observable<MsgResponse> crearDocumento(@Body BodyCrearDocumento body);

    @POST(Urls.CANCELAR_DOCUMENTO)
    Observable<MsgResponse> cancelarDocumento(@Body BodyCancelarDocumento body, @Path(Urls.PATH_ID) int idDocumento);

    // Inventario

    @GET(Urls.OBTENER_INVENTARIO_ABIERTO)
    Observable<List<Inventario>> getInventarioAbierto(@Path(Urls.PATH_ID) int idAlmacen);

    @GET(Urls.OBTENER_PRODUCTO)
    Observable<List<Producto>> getProducto(@Path(Urls.PATH_ID) String codProducto);

    @GET(Urls.OBTENER_CANTIDAD_LECTURAS)
    Observable<MsgResponse> getLecturasCount(@Query("id_inventario") int idInventario,
                                             @Query ("cod_usuario") String codUsuario);

    @POST(Urls.REGISTRAR_LECTURA_INVENTARIO)
    Observable<MsgResponse> registrarLecturaInventario(@Body BodyRegistrarInventario body);

    @POST(Urls.CONSULTAR_LECTURA_INVENTARIO)
    Observable<List<HijoConsultaInventario>> consultarLecturaInventario(@Body BodyConsultaLecturaInventario body);

    @POST(Urls.ELIMINAR_LECTURA_INVENTARIO)
    Observable<MsgResponse> deleteLecturaInventario(@Path(Urls.PATH_ID) int idLecturaInventario,
                                                    @Query("cod_usuario") String codUsuario);

    @POST(Urls.ELIMINAR_DETALLE_INVENTARIO)
    Observable<MsgResponse> deleteDetalleInventario(@Body BodyEliminarDetalle body,
                                                    @Path(Urls.PATH_ID) int idDetalleInventario);

    @POST(Urls.ELIMINAR_LECTURAS_TOTAL_INVENTARIO)
    Observable<MsgResponse> deleteTotalInventario(@Path(Urls.PATH_ID) int idInventario,
                                                    @Query("cod_usuario") String codUsuario);

    @GET(Urls.OBTENER_SERIE_INVENTARIO)
    Observable<List<SerieResponseInventario>> getSerieInventario(@Path(Urls.PATH_ID) int idInventario,
                                                                 @Query("id_producto") int idProducto,
                                                                 @Query("serieProducto") String serieProducto);

    @GET(Urls.OBTENER_DIFERENCIAL)
    Observable<List<ProductoDiferencial>> getDiferencial(@Path(Urls.PATH_ID) int idInventario,
                                                         @Query("conteo") int conteo);

    @GET(Urls.CARGA_DETALLE_INVENTARIO)
    Observable<List<DetalleInventario>> getDetalleInventarioAbierto();

    // Impresion

    @GET(Urls.OBTENER_DOCUMENTOS_CERRADOS)
    Observable<List<DocumentosCerrados>> getDocumentoCerrado(@Query("num_documento") String numDocumento,
                                                             @Query("id_class_doc") int idClassDoc);

    @GET(Urls.OBTENER_DETALLE_IMPRESION)
    Observable<List<DetalleImpresion>> getDetalleImpresion(@Path(Urls.PATH_ID) String numDocumento,
                                                           @Query("doc_interno") String docInterno,
                                                           @Query("flg_Det_o_Lec") boolean flgDetLec);

    @POST(Urls.IMPRIMIR_ETIQUETA)
    Observable<Boolean> imprimirEtiqueta(@Body BodyImprimirEtiqueta body);

    @GET(Urls.ACTUALIZAR_IMPRESION)
    Observable<Boolean> actualizarImpresion(@Query("id") int idImpresion,
                                            @Query("flg_Det_o_Lec") boolean flgDetLec);

    @GET(Urls.OBTENER_ETIQUETA_BLUETOOTH)
    Observable<EtiquetaBluetooth> getEtiquetaBluetooth();

    // EXPORTACION DE TABLAS

    @POST(Urls.EXPORTAR_DOCUMENTO)
    Observable<MsgResponse> exportarDocumento(@Body List<Documento> body);

    @POST(Urls.EXPORTAR_DETALLE_DOCUMENTO_ORI)
    Observable<MsgResponse> exportarDetalleDocumentoOri(@Body BodyDetalleDocumentoOri body);

    @POST(Urls.EXPORTAR_DETALLE_DOCUMENTO)
    Observable<MsgResponse> exportarDetalleDocumento(@Body BodyDetalleDocumento body);

    @POST(Urls.EXPORTAR_MOVIMIENTO)
    Observable<MsgResponse> exportarMovimientoInterno(@Body List<MovimientoInterno> body);

    @POST(Urls.EXPORTAR_INVENTARIO)
    Observable<MsgResponse> exportarInventario(@Body BodyInventario body);

    @POST(Urls.ENVIAR_DATOS_BD_CLIENTE)
    Observable<MsgResponse> enviarDatosBdCliente(@Body BodyEnviarDatosBdCliente body,
                                                 @Path(Urls.PATH_ID) String codAlmacen,
                                                 @Path(Urls.PATH_ID_2) String codEmpresa);
}
