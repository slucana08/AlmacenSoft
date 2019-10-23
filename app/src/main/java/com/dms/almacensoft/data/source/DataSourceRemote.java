package com.dms.almacensoft.data.source;

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
import com.dms.almacensoft.data.source.remote.Urls;
import com.dms.almacensoft.data.source.remote.WebService;
import com.dms.almacensoft.injection.annotations.ApplicationScope;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

@ApplicationScope
public class DataSourceRemote implements DataSource.DataSourceRemote {

    private WebService webService;

    @Inject
    public DataSourceRemote(WebService webService){
        this.webService = webService;
    }

    @Override
    public Observable<List<Almacen>> getAllAlmacen() { return webService.getAllAlmacen(); }

    @Override
    public Observable<List<Empresa>> getAllEmpresa(){
        return webService.getAllEmpresa();
    }

    @Override
    public Observable<List<Usuario>> getAllUsuario(){
        return webService.getAllUsuarios();
    }

    @Override
    public Observable<List<UsuarioxAlmacen>> getAllUsuarioxAlmacen(){
        return webService.getAllUsuarioXAlmacen();
    }

    @Override
    public Observable<List<Ubicacion>> getAllUbicacion(){
        return webService.getAllUbicacion();
    }

    @Override
    public Observable<List<ClaseDocumento>> getAllClaseDocumentoMaestro(){
        return webService.getAllClaseDocumentoMaestro();
    }

    @Override
    public Observable<List<Producto>> getAllProducto(int pagIni, int pagFin){
        return webService.getAllProducto(pagIni, pagFin);
    }

    @Override
    public Observable<Integer []> getProductoCount(){
        return webService.getProductoCount();
    }

    @Override
    public Observable<List<AlmacenVirtual>> getAllAlmacenVirtual(){
        return webService.getAllAlmacenVirtual();
    }

    @Override
    public Observable<List<JerarquiaClasificacion>> getAllJerarquiaClasificacion(){
        return webService.getAllJerarquiaClasificacion();
    }

    @Override
    public Observable<List<Clasificacion>> getAllClasificacion(){
        return webService.getAllClasificacion();
    }

    @Override
    public Observable<List<Destino>> getAllDestino(){
        return webService.getAllDestino();
    }

    @Override
    public Observable<List<Cliente>> getAllCliente(){
        return webService.getAllCliente();
    }

    @Override
    public Observable<List<Vehiculo>> getAllVehiculo(){
        return webService.getAllVehiculo();
    }

    @Override
    public Observable<List<Zona>> getAllZona(){
        return webService.getAllZona();
    }

    @Override
    public Observable<List<Isla>> getAllIsla(){
        return webService.getAllIsla();
    }

    @Override
    public Observable<List<ProductoUbicacion>> getAllProductoUbicacion(){
        return webService.getAllProductoUbicacion();
    }

    @Override
    public Observable<List<CajaProducto>> getAllCajaProducto(){
        return webService.getAllCajaProducto();
    }

    @Override
    public Observable<List<UnidadMedida>> getAllUnidadMedida(){
        return webService.getAllUnidadMedida();
    }

    @Override
    public Observable<List<Presentacion>> getAllPresentacion(){
        return webService.getAllPresentacion();
    }

    @Override
    public Observable<List<StockAlmacen>> getAllStockAlmacen(){
        return webService.getAllStockAlmacen();
    }

    // Procesos

    @Override
    public Observable<MsgResponse> processLogin(BodyLogin body){
        return webService.processLogin(body);
    }

    @Override
    public Observable<List<Configuracion>> getConfiguracioByUser(String codUsuario){
        return webService.getConfiguracioByUser(codUsuario);
    }

    @Override
    public Observable<MsgResponse> sendConfiguracionByUser(Configuracion configuracion, String codUsuario){
        return webService.sendConfiguracionByUser(configuracion, codUsuario);
    }

    @Override
    public Observable<MsgResponse> sendConfiguracion(Configuracion configuracion){
        return webService.sendConfiguracion(configuracion);
    }

    @Override
    public Observable<List<Almacen>> getAllAlmacenByUsuario(String idUsuario){
        return webService.getAllAlmacenByUsuario(idUsuario);
    }

    @Override
    public Observable<MsgResponse> verificarLicencia(String imei){
        return webService.verificarLicencia(imei);
    }

    @Override
    public Observable<MsgResponse> registrarLicencia(BodyLicencia body){
        return webService.registrarLicencia(body);
    }

    @Override
    public Observable<MsgResponse> bloquearDocumento(int idDocumento){
        return webService.bloquearDocumento(idDocumento);
    }

    // Recepcion - Despacho

    @Override
    public Observable<List<ClaseDocumento>> getClaseDocumentoModulo(String codTdoc,boolean conSinDoc){
        return webService.getClaseDocumentoModulo(codTdoc,conSinDoc);
    }

    @Override
    public Observable<List<Documento>> getDocumentosPendientes(BodyDocumentoPendiente body){
        return webService.getDocumentosPendientes(body);
    }

    @Override
    public Observable<List<DetalleDocumentoLive>> getDetalleDocumento(BodyDetalleDocumentoPendiente body){
        return webService.getDetalleDocumento(body);
    }

    @Override
    public Observable<List<DetalleDocumentoOri>> getDetalleDocumentoBatch(BodyDetalleDocumentoPendiente body){
        return webService.getDetalleDocumentoBatch(body);
    }

    @Override
    public Observable<List<Ubicacion>> getUbicacion(BodyUbicacion body){
        return webService.getUbicacion(body);
    }

    @Override
    public Observable<MsgResponse> registrarLecturasOnline(BodyRegistrarLecturas body){
        return webService.registrarLecturasOnline(body);
    }

    @Override
    public Observable<List<HijoConsulta>> getAllLecturasOnline(BodyConsultarLecturas body){
        return webService.getAllLecturasOnline(body);
    }

    @Override
    public Observable<MsgResponse> deteleLecturas(BodyEliminarLecturas body){
        return webService.deteleLecturas(body);
    }

    @Override
    public Observable<MsgResponse> cerrarDocumentoInterno(BodyCerrarDocumento body){
        return webService.cerrarDocumentoInterno(body);
    }

    @Override
    public Observable<List<SerieResponse>> getSerie(BodyBuscarSerie body){
        return webService.getSerie(body);
    }

    @Override
    public Observable<MsgResponse> crearDocumento(BodyCrearDocumento body){
        return webService.crearDocumento(body);
    }

    @Override
    public Observable<MsgResponse> cancelarDocumento(BodyCancelarDocumento body, int idDocumento){
        return webService.cancelarDocumento(body,idDocumento);
    }

    // Inventario

    @Override
    public Observable<List<Inventario>> getInventarioAbierto(int idAlmacen){
        return webService.getInventarioAbierto(idAlmacen);
    }

    @Override
    public Observable<List<Producto>> getProducto(String codProducto){
        return webService.getProducto(codProducto);
    }

    @Override
    public Observable<MsgResponse> getLecturasInventarioCount(int idInventario, String codUsuario){
        return webService.getLecturasCount(idInventario,codUsuario);
    }

    @Override
    public Observable<MsgResponse> registrarLecturaInventario(BodyRegistrarInventario body){
        return webService.registrarLecturaInventario(body);
    }

    @Override
    public Observable<List<HijoConsultaInventario>> consultarLecturaInventario(BodyConsultaLecturaInventario body){
        return webService.consultarLecturaInventario(body);
    }

    @Override
    public Observable<MsgResponse> deleteLecturaInventario(int idLecturaInventario, String codUsuario){
        return webService.deleteLecturaInventario(idLecturaInventario,codUsuario);
    }

    @Override
    public Observable<MsgResponse> deleteDetalleInventario(BodyEliminarDetalle body,int idDetalleInventario){
        return webService.deleteDetalleInventario(body,idDetalleInventario);
    }

    @Override
    public Observable<MsgResponse> deleteTotalInventario(int idInventario, String codUsuario){
        return webService.deleteTotalInventario(idInventario,codUsuario);
    }

    @Override
    public Observable<List<SerieResponseInventario>> getSerieInventario(int idInventario, int idProducto, String serieProducto){
        return webService.getSerieInventario(idInventario,idProducto,serieProducto);
    }

    @Override
    public Observable<List<ProductoDiferencial>> getDiferencial(int idInventario, int conteo){
        return webService.getDiferencial(idInventario,conteo);
    }

    @Override
    public Observable<List<DetalleInventario>> getDetalleInventarioAbierto(){
        return webService.getDetalleInventarioAbierto();
    }

    // IMPRESION

    @Override
    public Observable<List<DocumentosCerrados>> getDocumentoCerrado(String numDocumento, int idClassDoc){
        return webService.getDocumentoCerrado(numDocumento,idClassDoc);
    }

    @Override
    public Observable<List<DetalleImpresion>> getDetalleImpresion(String numDocumento, String docInterno, boolean flgDetLec){
        return webService.getDetalleImpresion(numDocumento,docInterno,flgDetLec);
    }

    @Override
    public Observable<Boolean> imprimirEtiqueta(BodyImprimirEtiqueta body){
        return webService.imprimirEtiqueta(body);
    }

    @Override
    public Observable<Boolean> actualizarImpresion(int idImpresion, boolean flgDetLec){
        return webService.actualizarImpresion(idImpresion, flgDetLec);
    }

    @Override
    public Observable<EtiquetaBluetooth> getEtiquetaBluetooth(){
        return webService.getEtiquetaBluetooth();
    }

    // EXPORTAR

    @Override
    public Observable<MsgResponse> exportarDocumento(List<Documento> body){
        return webService.exportarDocumento(body);
    }

    @Override
    public Observable<MsgResponse> exportarDetalleDocumentoOri(BodyDetalleDocumentoOri body){
        return webService.exportarDetalleDocumentoOri(body);
    }

    @Override
    public Observable<MsgResponse> exportarDetalleDocumento(BodyDetalleDocumento body){
        return webService.exportarDetalleDocumento(body);
    }

    @Override
    public Observable<MsgResponse> exportarInventario(BodyInventario body){
        return webService.exportarInventario(body);
    }

    @Override
    public Observable<MsgResponse> enviarDatosBdCliente(BodyEnviarDatosBdCliente body, String codAlmacen,
                                                        String codEmpresa){
        return webService.enviarDatosBdCliente(body, codAlmacen, codEmpresa);
    }

}
