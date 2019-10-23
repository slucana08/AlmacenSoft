package com.dms.almacensoft.data.source;

import com.dms.almacensoft.data.entities.dbalmacen.Almacen;
import com.dms.almacensoft.data.entities.dbalmacen.AlmacenVirtual;
import com.dms.almacensoft.data.entities.dbalmacen.CajaProducto;
import com.dms.almacensoft.data.entities.dbalmacen.ClaseDocumento;
import com.dms.almacensoft.data.entities.dbalmacen.Clasificacion;
import com.dms.almacensoft.data.entities.dbalmacen.Cliente;
import com.dms.almacensoft.data.entities.dbalmacen.Destino;
import com.dms.almacensoft.data.entities.dbalmacen.VerificacionDocumento;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumentoOri;
import com.dms.almacensoft.data.entities.dbtransact.DetalleInventario;
import com.dms.almacensoft.data.entities.dbtransact.Documento;
import com.dms.almacensoft.data.entities.dbalmacen.Empresa;
import com.dms.almacensoft.data.entities.dbalmacen.Inventario;
import com.dms.almacensoft.data.entities.dbalmacen.Isla;
import com.dms.almacensoft.data.entities.dbalmacen.JerarquiaClasificacion;
import com.dms.almacensoft.data.entities.dbalmacen.Presentacion;
import com.dms.almacensoft.data.entities.dbalmacen.Producto;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumento;
import com.dms.almacensoft.data.entities.dbtransact.LecturaDocumento;
import com.dms.almacensoft.data.entities.dbtransact.LecturaInventario;
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
import com.dms.almacensoft.injection.annotations.ApplicationScope;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

@ApplicationScope
public class DataSourceRepository implements DataSource.DataSourceRemote, DataSource.DataSourceShared {

    private DataSourceLocal local;
    private DataSourceRemote remote;

    @Inject
    public DataSourceRepository(DataSourceLocal dataSourceLocal,DataSourceRemote dataSourceRemote){
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
    }

    // Carga Maestros

    @Override
    public Observable<List<Almacen>> getAllAlmacen() {
        return remote.getAllAlmacen().
                doOnNext(local::insertAllAlmacen);
    }

    @Override
    public Observable<List<Empresa>> getAllEmpresa(){
        return remote.getAllEmpresa()
                .doOnNext(local::insertAllEmpresa);
    }

    @Override
    public Observable<List<Usuario>> getAllUsuario(){
        return remote.getAllUsuario()
                .doOnNext(local::insertAllUsuario);
    }

    @Override
    public Observable<List<UsuarioxAlmacen>> getAllUsuarioxAlmacen(){
        return remote.getAllUsuarioxAlmacen()
                .doOnNext(local::insertAllUsuarioxAlmacen);
    }

    @Override
    public Observable<List<Ubicacion>> getAllUbicacion(){
        return remote.getAllUbicacion();
    }

    @Override
    public void insertAllUbicacion(List<Ubicacion> ubicacion) {
        local.insertAllUbicacion(ubicacion);
    }

    @Override
    public Observable<List<ClaseDocumento>> getAllClaseDocumentoMaestro(){
        return remote.getAllClaseDocumentoMaestro()
                .doOnNext(local::insertAllClaseDocumento);
    }

    @Override
    public Observable<List<Producto>> getAllProducto(int pagIni, int pagFin){
        return remote.getAllProducto(pagIni, pagFin);
    }

    @Override
    public Observable<Integer []> getProductoCount(){
        return remote.getProductoCount();
    }

    @Override
    public void insertAllProducto(List<Producto> productos) {
        local.insertAllProducto(productos);
    }

    @Override
    public void deleteProductos(){
        local.deleteProductos();
    }

    @Override
    public Observable<Integer> countProductos() {
        return local.countProductos();
    }

    @Override
    public Observable<List<AlmacenVirtual>> getAllAlmacenVirtual(){
        return remote.getAllAlmacenVirtual()
                .doOnNext(local::insertAllAlmacenVirtual);
    }

    @Override
    public Observable<List<JerarquiaClasificacion>> getAllJerarquiaClasificacion(){
        return remote.getAllJerarquiaClasificacion()
                .doOnNext(local::insertAllJerarquiaClasificacion);
    }

    @Override
    public Observable<List<Clasificacion>> getAllClasificacion(){
        return remote.getAllClasificacion()
                .doOnNext(local::insertAllClasificacion);
    }

    @Override
    public Observable<List<Destino>> getAllDestino(){
        return remote.getAllDestino()
                .doOnNext(local::insertAllDestino);
    }

    @Override
    public Observable<List<Cliente>> getAllCliente(){
        return remote.getAllCliente()
                .doOnNext(local::insertAllCliente);
    }

    @Override
    public Observable<List<Vehiculo>> getAllVehiculo(){
        return remote.getAllVehiculo()
                .doOnNext(local::insertAllVehiculo);
    }

    @Override
    public Observable<List<Zona>> getAllZona(){
        return remote.getAllZona()
                .doOnNext(local::insertAllZona);
    }

    @Override
    public Observable<List<Isla>> getAllIsla(){
        return remote.getAllIsla()
                .doOnNext(local::insertAllIsla);
    }

    @Override
    public Observable<List<ProductoUbicacion>> getAllProductoUbicacion(){
        return remote.getAllProductoUbicacion()
                .doOnNext(local::insertAllProductoUbicacion);
    }

    @Override
    public Observable<List<CajaProducto>> getAllCajaProducto(){
        return remote.getAllCajaProducto()
                .doOnNext(local::insertAllCajaProducto);
    }

    @Override
    public Observable<List<UnidadMedida>> getAllUnidadMedida(){
        return remote.getAllUnidadMedida()
                .doOnNext(local::insertAllUnidadMedida);
    }

    @Override
    public Observable<List<Presentacion>> getAllPresentacion(){
        return remote.getAllPresentacion()
                .doOnNext(local::insertAllPresentacion);
    }

    @Override
    public Observable<List<StockAlmacen>> getAllStockAlmacen(){
        return remote.getAllStockAlmacen()
                .doOnNext(local::insertAllStockAlmacen);
    }

    // Procesos

    @Override
    public Observable<MsgResponse> processLogin(BodyLogin body){
        return remote.processLogin(body);
    }

    @Override
    public Observable<List<Almacen>> getListAlmacen() {
        return local.getListAlmacen();
    }

    @Override
    public Observable<List<Inventario>> getListInventarioDB(){
        return local.getListInventarioDB();
    }

    @Override
    public Observable<List<Almacen>> getAllAlmacenByUsuario(String idUsuario){
        return remote.getAllAlmacenByUsuario(idUsuario)
                .doOnNext(local::insertAllAlmacen);
    }

    @Override
    public Observable<List<Configuracion>> getConfiguracioByUser(String codUsuario){
        return remote.getConfiguracioByUser(codUsuario);
    }

    @Override
    public Observable<MsgResponse> sendConfiguracionByUser(Configuracion configuracion, String codUsuario){
        return remote.sendConfiguracionByUser(configuracion, codUsuario);
    }

    @Override
    public Observable<MsgResponse> sendConfiguracion(Configuracion configuracion){
        return remote.sendConfiguracion(configuracion);
    }

    @Override
    public Observable<MsgResponse> verificarLicencia(String imei){
        return remote.verificarLicencia(imei);
    }

    @Override
    public Observable<MsgResponse> registrarLicencia(BodyLicencia body){
        return remote.registrarLicencia(body);
    }

    @Override
    public Observable<MsgResponse> bloquearDocumento(int idDocumento){
        return remote.bloquearDocumento(idDocumento);
    }


    // Recepcion - Despacho

    @Override
    public Observable<List<ClaseDocumento>> getClaseDocumentoModulo(String codTdoc,boolean conSinDoc){
        return remote.getClaseDocumentoModulo(codTdoc,conSinDoc);
    }

    @Override
    public Observable<List<Documento>> getDocumentosPendientes(BodyDocumentoPendiente body){
        return remote.getDocumentosPendientes(body);
    }

    @Override
    public void deleteClaseDocumento(){
        local.deleteClaseDocumento();
    }

    @Override
    public Observable<List<DetalleDocumentoLive>> getDetalleDocumento(BodyDetalleDocumentoPendiente body){
        return remote.getDetalleDocumento(body);
    }

    @Override
    public Observable<List<Ubicacion>> getUbicacion(BodyUbicacion body){
        return remote.getUbicacion(body);
    }

    @Override
    public Observable<MsgResponse> registrarLecturasOnline(BodyRegistrarLecturas body){
        return remote.registrarLecturasOnline(body);
    }

    @Override
    public Observable<List<HijoConsulta>> getAllLecturasOnline(BodyConsultarLecturas body){
        return remote.getAllLecturasOnline(body);
    }

    @Override
    public Observable<MsgResponse> deteleLecturas(BodyEliminarLecturas body){
        return remote.deteleLecturas(body);
    }

    @Override
    public Observable<MsgResponse> cerrarDocumentoInterno(BodyCerrarDocumento body){
        return remote.cerrarDocumentoInterno(body);
    }

    @Override
    public Observable<List<SerieResponse>> getSerie(BodyBuscarSerie body){
        return remote.getSerie(body);
    }

    @Override
    public Observable<MsgResponse> crearDocumento(BodyCrearDocumento body){
        return remote.crearDocumento(body);
    }

    @Override
    public Observable<MsgResponse> cancelarDocumento(BodyCancelarDocumento body, int idDocumento){
        return remote.cancelarDocumento(body,idDocumento);
    }

    // Inventario

    @Override
    public Observable<List<Inventario>> getInventarioAbierto(int idAlmacen){
        return remote.getInventarioAbierto(idAlmacen)
                .doOnNext(local::insertAllInventario);
    }

    @Override
    public Observable<List<Producto>> getProducto(String codProducto){
        return remote.getProducto(codProducto);
    }

    @Override
    public Observable<MsgResponse> getLecturasInventarioCount(int idInventario, String codUsuario){
        return remote.getLecturasInventarioCount(idInventario,codUsuario);
    }

    @Override
    public Observable<MsgResponse> registrarLecturaInventario(BodyRegistrarInventario body){
        return remote.registrarLecturaInventario(body);
    }

    @Override
    public Observable<List<HijoConsultaInventario>> consultarLecturaInventario(BodyConsultaLecturaInventario body){
        return remote.consultarLecturaInventario(body);
    }

    @Override
    public Observable<MsgResponse> deleteLecturaInventario(int idLecturaInventario,String codUsuario){
        return remote.deleteLecturaInventario(idLecturaInventario,codUsuario);
    }

    @Override
    public Observable<MsgResponse> deleteDetalleInventario(BodyEliminarDetalle body, int idDetalleInventario){
        return remote.deleteDetalleInventario(body,idDetalleInventario);
    }

    @Override
    public Observable<MsgResponse> deleteTotalInventario(int idInventario, String codUsuario){
        return remote.deleteTotalInventario(idInventario,codUsuario);
    }

    @Override
    public Observable<List<SerieResponseInventario>> getSerieInventario(int idInventario,int idProducto,String serieProducto){
        return remote.getSerieInventario(idInventario,idProducto,serieProducto);
    }

    @Override
    public Observable<List<ProductoDiferencial>> getDiferencial(int idInventario, int conteo){
        return remote.getDiferencial(idInventario,conteo)
                .doOnNext(local::insertProductoDiferencial);
    }

    @Override
    public Observable<List<DetalleInventario>> getDetalleInventarioAbierto(){
        return remote.getDetalleInventarioAbierto()
                .doOnNext(local::insertDetalleInventario);
    }

    // IMPRESION

    @Override
    public Observable<List<DocumentosCerrados>> getDocumentoCerrado(String numDocumento, int idClassDoc){
        return remote.getDocumentoCerrado(numDocumento,idClassDoc);
    }

    @Override
    public Observable<List<DetalleImpresion>> getDetalleImpresion(String numDocumento, String docInterno, boolean flgDetLec){
        return remote.getDetalleImpresion(numDocumento,docInterno,flgDetLec);
    }

    @Override
    public Observable<Boolean> imprimirEtiqueta(BodyImprimirEtiqueta body){
        return remote.imprimirEtiqueta(body);
    }

    @Override
    public Observable<Boolean> actualizarImpresion(int idImpresion, boolean flgDetLec){
        return remote.actualizarImpresion(idImpresion, flgDetLec);
    }

    @Override
    public Observable<EtiquetaBluetooth> getEtiquetaBluetooth(){
        return remote.getEtiquetaBluetooth();
    }

    //------------ MODO BATCH ------------------------ //
    // Recepcion - Despacho

    @Override
    public Observable<List<ClaseDocumento>> getListClaseDocumentoBatch(String codTipoDocumento, String flgConSinDoc){
        return local.getListClaseDocumentoBatch(codTipoDocumento, flgConSinDoc);
    }

    @Override
    public void insertDocumentos(List<Documento> documentos){
        local.insertDocumentos(documentos);
    }

    @Override
    public void deleteDocumentos(){
        local.deleteDocumentos();
        local.deleteDetalleDocumentoOri();
    }

    @Override
    public Observable<List<DetalleDocumentoOri>> getDetalleDocumentoBatch(BodyDetalleDocumentoPendiente body){
        return remote.getDetalleDocumentoBatch(body).
                doOnNext(local::insertDetalleDocumentoOri);
    }

    @Override
    public Observable<List<Documento>> getListDocumento(String codTipoDocumento, String codClaseDocumento,
                                                        String numDocumento, int idAlmacen){
        return local.getListDocumento(codTipoDocumento, codClaseDocumento, numDocumento, idAlmacen);
    }

    @Override
    public Observable<List<Documento>> getListDocumento(String codTipoDocumento, String codClaseDocumento, int idAlmacen){
        return local.getListDocumento(codTipoDocumento, codClaseDocumento, idAlmacen);
    }

    @Override
    public Observable<List<DetalleDocumentoLive>> getDetalleListBD(int idDocumento){
        return local.getDetalleListBD(idDocumento);
    }

    @Override
    public Observable<List<Ubicacion>> verifyUbicacionBD(int idAlmacen, String codUbicacion){
        return local.verifyUbicacionBD(idAlmacen, codUbicacion);
    }

    @Override
    public Observable<List<Producto>> verifyProductoDB(String codProducto){
        return local.verifyProductoDB(codProducto);
    }

    @Override
    public Observable<List<SerieResponse>> verifySerieRecepcionBD(int idProducto, String serieProducto, int idDocumento){
        return local.verifySerieRecepcionBD(idProducto, serieProducto, idDocumento);
    }

    @Override
    public Observable<Integer> verifyDetalleDocumentoBD(String loteProducto, String numDocInterno, int linea, int idDocumento, int idProducto){
        return local.verifyDetalleDocumentoBD(loteProducto, numDocInterno, linea, idDocumento, idProducto);
    }

    @Override
    public void updateDetalleDocumentoBD(double ctdAtendidaUpdate, String loteProducto, String numDocInterno, int linea,
                                         int idDocumento,String codUsuarioModificacion, String fchModificacion, int idProducto){
        local.updateDetalleDocumentoBD(ctdAtendidaUpdate, loteProducto, numDocInterno, linea, idDocumento,
                codUsuarioModificacion,fchModificacion, idProducto);
    }

    @Override
    public void insertDetalleDocumentoBD(DetalleDocumento detalleDocumento){
        local.insertDetalleDocumentoBD(detalleDocumento);
    }

    @Override
    public void insertLecturaDocumentoBD(LecturaDocumento lecturaDocumento){
        local.insertLecturaDocumentoBD(lecturaDocumento);
    }

    @Override
    public Observable<Integer> getIDDetalleDocumentoBD(String loteProducto, String numDocInterno, int linea, int idDocumento, int idProducto){
        return local.getIDDetalleDocumentoBD(loteProducto, numDocInterno, linea, idDocumento, idProducto);
    }

    @Override
    public Observable<List<DetalleDocumento>> getDetalleDocumentoByProductoBD(int idProducto, int idDocumento){
        return local.getDetalleDocumentoByProductoBD(idProducto,idDocumento);
    }

    @Override
    public void limpiarBdAdmin(){
        local.limpiarBdAdmin();
    }

    @Override
    public Observable<Integer> countDetalle(){
        return local.countDetalle();
    }

    @Override
    public Observable<Integer> countDetalleInventario(){
        return local.countDetalleInventario();
    }

    @Override
    public Observable<Integer> countLectura(){
        return local.countLectura();
    }

    @Override
    public Observable<Integer> countMovimiento(){
        return local.countMovimiento();
    }

    @Override
    public Observable<Integer> countDetalleOri(){
        return local.countDetalleOri();
    }

    @Override
    public Observable<Integer> countDocumento(){
        return local.countDocumento();
    }

    @Override
    public void insertMovimientoInterno(MovimientoInterno movimientoInterno){
        local.insertMovimientoInterno(movimientoInterno);
    }

    @Override
    public Observable<List<Integer>> getIDLecturaDocumento(int idDetalleDocumento, int idProducto,
                                                     String loteProducto, String serieProducto){
        return local.getIDLecturaDocumento(idDetalleDocumento, idProducto, loteProducto, serieProducto);
    }

    @Override
    public Observable<List<HijoConsulta>> getLecturasBD(String codTipoDocumento, int idClaseDocumento, String numDocumento,
                                                  String flgActivo, String flgEnviado, String codUsuarioRegistro){
        return local.getLecturasBD(codTipoDocumento, idClaseDocumento,numDocumento, flgActivo, flgEnviado, codUsuarioRegistro);
    }

    @Override
    public void updateDetalleDocumentoEliminacion(double ctdAtendidaRestar, int idDetalleDocumento,
                                                  String codUsuario, String fchModificacion){
        local.updateDetalleDocumentoEliminacion(ctdAtendidaRestar,idDetalleDocumento,codUsuario,fchModificacion);
    }

    @Override
    public void updateMovimientoEliminacion(String codUsuario,String fchModificacion, int idLecturaDocumento){
        local.updateMovimientoEliminacion(codUsuario, fchModificacion, idLecturaDocumento);
    }

    @Override
    public void deleteLecturaBD(String codUsuario, String fchModificacion, String idTerminal, String fchRegistroTerminal,
                                int idDetalleDocumento, int idLecturaDocumento){
        local.deleteLecturaBD(codUsuario, fchModificacion, idTerminal,fchRegistroTerminal, idDetalleDocumento, idLecturaDocumento);
    }

    @Override
    public Observable<Integer> getLecturasByIdDetalle(int idDetalleDocumento){
        return local.getLecturasByIdDetalle(idDetalleDocumento);
    }

    @Override
    public void cerrarDetalleDocumentoEliminacion(String codUsuario,String fchModificacion,int idDetalleDocumento){
        local.cerrarDetalleDocumentoEliminacion(codUsuario, fchModificacion, idDetalleDocumento);
    }

    @Override
    public void cerrarLecturaDocumento(int idLecturaDocumento, String fchModificacion, String guia){
        local.cerrarLecturaDocumento(idLecturaDocumento, fchModificacion, guia);
    }

    @Override
    public void cerrarDetalleDocumento(String codUsuario, String fchModificacion, int idDetalleDocumento){
        local.cerrarDetalleDocumento(codUsuario, fchModificacion, idDetalleDocumento);
    }

    @Override
    public Observable<List<Integer>> getIdLecturaDocumentoByIdDetalle(int idDetalleDocumento){
        return local.getIdLecturaDocumentoByIdDetalle(idDetalleDocumento);
    }

    @Override
    public void cerrarMovimiento(int idLecturaDocumento, String codUsuario, String fchModificacion){
        local.cerrarMovimiento(idLecturaDocumento,codUsuario,fchModificacion);
    }

    @Override
    public void cerrarDocumento(String codUsuario, String fchModificacion, int idDocumento){
        local.cerrarDocumento(codUsuario, fchModificacion, idDocumento);
    }

    @Override
    public Observable<List<LecturaDocumento>> getLecturaDocumentoCerrar(String guia){
        return local.getLecturaDocumentoCerrar(guia);
    }

    @Override
    public Observable<List<DetalleDocumento>> getDetalleDocumentoCerrar(int idDocumento, String numDocInterno){
        return local.getDetalleDocumentoCerrar(idDocumento, numDocInterno);
    }

    @Override
    public Observable<List<DetalleDocumentoOri>> getDetalleDocumentoOri(int idDocumento){
        return local.getDetalleDocumentoOri(idDocumento);
    }

    @Override
    public void insertDetalleDocumentoOri(List<DetalleDocumentoOri> entities){
        local.insertDetalleDocumentoOri(entities);
    }

    @Override
    public void finalizarDetalleDocumento(String codUsuario, String fchModificacion, int idDetalleDocumento){
        local.finalizarDetalleDocumento(codUsuario, fchModificacion, idDetalleDocumento);
    }

    @Override
    public Observable<Integer> verifyDocumento(String numDocumento){
        return local.verifyDocumento(numDocumento);
    }

    @Override
    public void insertDocumento(Documento documento){
        local.insertDocumento(documento);
    }

    @Override
    public Observable<Integer> getIdDocumento(String numDocumento){
        return local.getIdDocumento(numDocumento);
    }

    @Override
    public Observable<List<DetalleDocumento>> getDetalleDocumentoByDocumentoBD(int idDocumento){
        return local.getDetalleDocumentoByDocumentoBD(idDocumento);
    }

    @Override
    public Observable<List<Producto>> getProductoByID(int idProducto){
        return local.getProductoByID(idProducto);
    }

    @Override
    public void cerrarLecturaDocumentoByIdDetalle(int idDetalleDocumento, String codUsuario, String fchModificacion){
        local.cerrarLecturaDocumentoByIdDetalle(idDetalleDocumento, codUsuario, fchModificacion);
    }

    @Override
    public Observable<Integer> getLecturasCountBD(String codTipoDocumento, int idClaseDocumento, String numDocumento,
                                                  String flgActivo, String flgEnviado, String codUsuarioRegistro){
        return local.getLecturasCountBD(codTipoDocumento, idClaseDocumento, numDocumento,
                flgActivo, flgEnviado, codUsuarioRegistro);
    }

    @Override
    public void cancelarDocumentoBD(String codUsuario, String fchModificacion, int idDocumento){
        local.cancelarDocumentoBD(codUsuario,fchModificacion,idDocumento);
    }

    // Impresion //

    @Override
    public Observable<List<String>> getGuiaCerradas(String numDocumento, int idClaseDocumento){
        return local.getGuiaCerradas(numDocumento, idClaseDocumento);
    }

    @Override
    public Observable<List<DetalleImpresion>> getDetalleImpresionBD(String numDocumento, String numDocInterno){
        return local.getDetalleImpresionBD(numDocumento,numDocInterno);
    }

    @Override
    public Observable<List<DetalleImpresion>> getLecturaImpresion(String numDocumento, String numDocInterno){
        return local.getLecturaImpresion(numDocumento,numDocInterno);
    }

    @Override
    public Observable<List<Usuario>> getUsuario(String codUsuario){
        return local.getUsuario(codUsuario);
    }

    @Override
    public Observable<List<Almacen>> getAllAlmacenByUsuarioBD(int idUsuario){
        return local.getAllAlmacenByUsuarioBD(idUsuario);
    }

    @Override
    public Observable<List<Inventario>> getAllInventarioByAlmacen(int idAlmacen){
        return local.getAllInventarioByAlmacen(idAlmacen);
    }

    @Override
    public Observable<Integer> getLecturasInventarioCountBD(int idInventario, String codUsuarioRegistro){
        return local.getLecturasInventarioCountBD(idInventario, codUsuarioRegistro);
    }

    @Override
    public Observable<List<ProductoDiferencial>> getAllProductoDiferencial(){
        return local.getAllProductoDiferencial();
    }

    @Override
    public Observable<Integer> getProductoDiferencialCount(){
        return local.getProductoDiferencialCount();
    }

    @Override
    public Observable<List<SerieResponseInventario>> obtenerSerieInventarioBD(int idInventario, int idProducto,
                                                                              String serieProducto){
        return local.obtenerSerieInventarioBD(idInventario, idProducto, serieProducto);
    }

    @Override
    public Observable<List<HijoConsultaInventario>> getLecturaInventarioByUbicacion(int idInventario,
                                                                                    int idUbicacion,
                                                                                    String codUsuarioRegistro){
        return local.getLecturaInventarioByUbicacion(idInventario, idUbicacion, codUsuarioRegistro);
    }

    @Override
    public Observable<List<HijoConsultaInventario>> getLecturaInventarioByProducto(int idInventario,
                                                                                   int idProducto,
                                                                                   String codUsuarioRegistro){
        return local.getLecturaInventarioByProducto(idInventario, idProducto, codUsuarioRegistro);
    }

    @Override
    public Observable<List<HijoConsultaInventario>> getLecturaInventarioByLote(int idInventario,
                                                                               String loteProducto,
                                                                               String codUsuarioRegistro){
        return local.getLecturaInventarioByLote(idInventario, loteProducto, codUsuarioRegistro);
    }

    @Override
    public Observable<List<HijoConsultaInventario>> getLecturaInventarioBySerie(int idInventario,
                                                                                String serieProducto,
                                                                                String codUsuarioRegistro){
        return local.getLecturaInventarioBySerie(idInventario, serieProducto, codUsuarioRegistro);
    }

    @Override
    public Observable<Integer> verifyDetalleInventarioBD(int idInventario, int idUbicacion, int idProducto,
                                                         String loteProducto){
        return local.verifyDetalleInventarioBD(idInventario, idUbicacion, idProducto, loteProducto);
    }

    @Override
    public void updateDetalleInventarioBD(double stockInventariado, String codUsuarioModificacion, String fchModificacion,
                                          int idInventario,int idUbicacion,int idProducto,String loteProducto){
        local.updateDetalleInventarioBD(stockInventariado,codUsuarioModificacion, fchModificacion,
                idInventario, idUbicacion, idProducto, loteProducto);
    }

    @Override
    public void insertDetalleInventario(DetalleInventario detalleInventario){
        local.insertDetalleInventario(detalleInventario);
    }

    @Override
    public void insertLecturaInventario(LecturaInventario lecturaInventario){
        local.insertLecturaInventario(lecturaInventario);
    }

    @Override
    public Observable<Integer> getIdDetalleInventarioBD(int idInventario, int idUbicacion, int idProducto,
                                                        String loteProducto){
        return local.getIdDetalleInventarioBD(idInventario, idUbicacion, idProducto, loteProducto);
    }

    @Override
    public Observable<LecturaInventario> getLecturaEliminar(int idLecturaInventario, String codUsuarioRegistro){
        return local.getLecturaEliminar(idLecturaInventario, codUsuarioRegistro);
    }

    @Override
    public void eliminarLectura(int idLecturaInventario, String codUsuarioRegistro){
        local.eliminarLectura(idLecturaInventario, codUsuarioRegistro);
    }

    @Override
    public void updateDetalleLecturaEliminar(double cantidadEliminar, int idDetalleInventario,
                                             String codUsuarioModificacion, String fchModificacion){
        local.updateDetalleLecturaEliminar(cantidadEliminar, idDetalleInventario, codUsuarioModificacion,fchModificacion);
    }

    @Override
    public Observable<List<LecturaInventario>> getLecturaEliminarByDetalle(int idDetalleInventario,
                                                                           String codUsuarioRegistro){
        return local.getLecturaEliminarByDetalle(idDetalleInventario, codUsuarioRegistro);
    }

    @Override
    public void eliminarLecturasAdmin(){
        local.eliminarLecturasAdmin();
    }

    @Override
    public void eliminarDetallesAdmin(String codUsuarioModificacion, String fchModificacion){
        local.eliminarDetallesAdmin(codUsuarioModificacion, fchModificacion);
    }

    @Override
    public void updateDocumentoTrabajado(String codUsuarioModificacion, String fchModificacion, int idDocumento){
        local.updateDocumentoTrabajado(codUsuarioModificacion, fchModificacion, idDocumento);
    }

    @Override
    public Observable<Integer> countLecturasInventarioBD(){
        return local.countLecturasInventarioBD();
    }

    // EXPORTACION DE TABLAS

    @Override
    public Observable<List<Documento>> getAllDocumentosExportar(){
        return local.getAllDocumentosExportar();
    }

    @Override
    public Observable<MsgResponse> exportarDocumento(List<Documento> body){
        return remote.exportarDocumento(body);
    }

    @Override
    public Observable<List<DetalleDocumento>> getAllDetalleExportar(){
        return local.getAllDetalleExportar();
    }

    @Override
    public Observable<List<LecturaDocumento>> getAllLecturaExportar(){
        return local.getAllLecturaExportar();
    }

    @Override
    public void updateLecturaDescargada(String fchDescargado,String codUsuarioModificacion, String fchModificacion,
                                        int idLecturaDocumento){
        local.updateLecturaDescargada(fchDescargado, codUsuarioModificacion,fchModificacion, idLecturaDocumento);
    }

    @Override
    public Observable<List<DetalleDocumentoOri>> getAllDetalleDocumentoOriExportar(){
        return local.getAllDetalleDocumentoOriExportar();
    }

    @Override
    public Observable<List<VerificacionDocumento>> getAllVerificacionExportar(){
        return local.getAllVerificacionExportar();
    }

    @Override
    public Observable<MsgResponse> exportarDetalleDocumentoOri(BodyDetalleDocumentoOri body){
        return remote.exportarDetalleDocumentoOri(body);
    }

    @Override
    public Observable<MsgResponse> exportarDetalleDocumento(BodyDetalleDocumento body){
        return remote.exportarDetalleDocumento(body);
    }

    @Override
    public Observable<List<Inventario>> getAllInventarioExportar(){
        return local.getAllInventarioExportar();
    }

    @Override
    public Observable<Integer> getCountLecturaInventarioExportar(){
        return local.getCountLecturaInventarioExportar();
    }

    @Override
    public Observable<List<LecturaInventario>> getLecturaInventarioExportar(int limit){
        return local.getLecturaInventarioExportar(limit);
    }

    @Override
    public Observable<MsgResponse> exportarInventario(BodyInventario body){
        return remote.exportarInventario(body);
    }

    @Override
    public void updateLecturaInventarioDescargada(String fchDescargado,String codUsuarioModificacion, String fchModificacion,
                                                  int idLecturaInventario){
        local.updateLecturaInventarioDescargada(fchDescargado, codUsuarioModificacion,fchModificacion, idLecturaInventario);
    }

    @Override
    public Observable<MsgResponse> enviarDatosBdCliente(BodyEnviarDatosBdCliente body, String codAlmacen,
                                                        String codEmpresa){
        return remote.enviarDatosBdCliente(body, codAlmacen, codEmpresa);
    }

    @Override
    public void updateDetalleDescargado(String codUsuarioModificacion, String fchModificacion, int idDetalleDocumento){
        local.updateDetalleDescargado(codUsuarioModificacion, fchModificacion, idDetalleDocumento);
    }

    @Override
    public Observable<Integer> claseDocumentoCount(){
        return local.claseDocumentoCount();
    }

    @Override
    public Observable<List<DetalleDocumento>> getDetalleDespachoCerrar(){
        return local.getDetalleDespachoCerrar();
    }

    @Override
    public Observable<String> getConSinDoc(int idDocumento){
        return local.getConSinDoc(idDocumento);
    }
}
