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
import com.dms.almacensoft.data.entities.dbalmacen.ProductoUbicacion;
import com.dms.almacensoft.data.entities.dbalmacen.StockAlmacen;
import com.dms.almacensoft.data.entities.dbalmacen.Ubicacion;
import com.dms.almacensoft.data.entities.dbalmacen.UnidadMedida;
import com.dms.almacensoft.data.entities.dbalmacen.Usuario;
import com.dms.almacensoft.data.entities.dbalmacen.UsuarioxAlmacen;
import com.dms.almacensoft.data.entities.dbalmacen.Vehiculo;
import com.dms.almacensoft.data.entities.dbalmacen.Zona;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumento;
import com.dms.almacensoft.data.entities.dbtransact.LecturaDocumento;
import com.dms.almacensoft.data.entities.dbtransact.LecturaInventario;
import com.dms.almacensoft.data.entities.dbtransact.MovimientoInterno;
import com.dms.almacensoft.data.entities.dbtransact.ProductoDiferencial;
import com.dms.almacensoft.data.models.impresion.DetalleImpresion;
import com.dms.almacensoft.data.models.inventario.HijoConsultaInventario;
import com.dms.almacensoft.data.models.inventario.SerieResponseInventario;
import com.dms.almacensoft.data.models.recepciondespacho.DetalleDocumentoLive;
import com.dms.almacensoft.data.models.recepciondespacho.HijoConsulta;
import com.dms.almacensoft.data.models.recepciondespacho.SerieResponse;
import com.dms.almacensoft.data.source.local.DbAlmacen;
import com.dms.almacensoft.data.source.local.dao.AlmacenDao;
import com.dms.almacensoft.data.source.local.dao.AlmacenVirtualDao;
import com.dms.almacensoft.data.source.local.dao.CajaProductoDao;
import com.dms.almacensoft.data.source.local.dao.ClaseDocumentoDao;
import com.dms.almacensoft.data.source.local.dao.ClasificacionDao;
import com.dms.almacensoft.data.source.local.dao.ClienteDao;
import com.dms.almacensoft.data.source.local.dao.DestinoDao;
import com.dms.almacensoft.data.source.local.daotransact.DetalleDocumentoOriDao;
import com.dms.almacensoft.data.source.local.daotransact.DetalleInventarioDao;
import com.dms.almacensoft.data.source.local.dao.DetalleVerificacionDocumentoDao;
import com.dms.almacensoft.data.source.local.daotransact.DocumentoDao;
import com.dms.almacensoft.data.source.local.dao.EmpresaDao;
import com.dms.almacensoft.data.source.local.dao.InventarioDao;
import com.dms.almacensoft.data.source.local.dao.IslaDao;
import com.dms.almacensoft.data.source.local.dao.JerarquiaClasificacionDao;
import com.dms.almacensoft.data.source.local.dao.PresentacionDao;
import com.dms.almacensoft.data.source.local.dao.ProductoDao;
import com.dms.almacensoft.data.source.local.dao.ProductoUbicacionDao;
import com.dms.almacensoft.data.source.local.dao.StockAlmacenDao;
import com.dms.almacensoft.data.source.local.dao.UbicacionDao;
import com.dms.almacensoft.data.source.local.dao.UnidadMedidaDao;
import com.dms.almacensoft.data.source.local.dao.UsuarioDao;
import com.dms.almacensoft.data.source.local.dao.UsuarioxAlmacenDao;
import com.dms.almacensoft.data.source.local.dao.VehiculoDao;
import com.dms.almacensoft.data.source.local.dao.VerificacionDocumentoDao;
import com.dms.almacensoft.data.source.local.dao.ZonaDao;
import com.dms.almacensoft.data.source.local.daotransact.DetalleDocumentoDao;
import com.dms.almacensoft.data.source.local.daotransact.EnlaceDocumentoDao;
import com.dms.almacensoft.data.source.local.daotransact.LecturaDocumentoDao;
import com.dms.almacensoft.data.source.local.daotransact.LecturaInventarioDao;
import com.dms.almacensoft.data.source.local.daotransact.LecturaVerificacionDocumentoDao;
import com.dms.almacensoft.data.source.local.daotransact.MovimientoInternoDao;
import com.dms.almacensoft.data.source.local.daotransact.ProductoDiferencialDao;
import com.dms.almacensoft.injection.annotations.ApplicationScope;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;

@ApplicationScope
public class DataSourceLocal implements DataSource.DataSourceLocal{

    private DbAlmacen dbAlmacen;

    // dao para DbAlmacen
    private AlmacenDao almacenDao;
    private AlmacenVirtualDao almacenVirtualDao;
    private CajaProductoDao cajaProductoDao;
    private ClaseDocumentoDao claseDocumentoDao;
    private ClasificacionDao clasificacionDao;
    private ClienteDao clienteDao;
    private DestinoDao destinoDao;
    private DetalleDocumentoOriDao detalleDocumentoOriDao;
    private DetalleInventarioDao detalleInventarioDao;
    private DetalleVerificacionDocumentoDao detalleVerificacionDocumentoDao;
    private DocumentoDao documentoDao;
    private EmpresaDao empresaDao;
    private InventarioDao inventarioDao;
    private IslaDao islaDao;
    private JerarquiaClasificacionDao jerarquiaClasificacionDao;
    private PresentacionDao presentacionDao;
    private ProductoDao productoDao;
    private ProductoUbicacionDao productoUbicacionDao;
    private StockAlmacenDao stockAlmacenDao;
    private UbicacionDao ubicacionDao;
    private UnidadMedidaDao unidadMedidaDao;
    private UsuarioDao usuarioDao;
    private UsuarioxAlmacenDao usuarioxAlmacenDao;
    private VehiculoDao vehiculoDao;
    private VerificacionDocumentoDao verificacionDocumentoDao;
    private ZonaDao zonaDao;

    // dao para transacciones
    private DetalleDocumentoDao detalleDocumentoDao;
    private EnlaceDocumentoDao enlaceDocumentoDao;
    private LecturaDocumentoDao lecturaDocumentoDao;
    private LecturaInventarioDao lecturaInventarioDao;
    private LecturaVerificacionDocumentoDao lecturaVerificacionDocumentoDao;
    private MovimientoInternoDao movimientoInternoDao;
    private ProductoDiferencialDao productoDiferencialDao;

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 0,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(30));

    @Inject
    public DataSourceLocal(DbAlmacen dbAlmacen) {
        this.dbAlmacen = dbAlmacen;

        almacenDao = dbAlmacen.almacenDao();
        almacenVirtualDao = dbAlmacen.almacenVirtualDao();
        cajaProductoDao = dbAlmacen.cajaProductoDao();
        claseDocumentoDao = dbAlmacen.claseDocumentoDao();
        clasificacionDao = dbAlmacen.clasificacionDao();
        clienteDao = dbAlmacen.clienteDao();
        destinoDao = dbAlmacen.destinoDao();
        detalleDocumentoOriDao = dbAlmacen.detalleDocumentoOriDao();
        detalleInventarioDao = dbAlmacen.detalleInventarioDao();
        detalleVerificacionDocumentoDao = dbAlmacen.detalleVerificacionDocumentoDao();
        documentoDao = dbAlmacen.documentoDao();
        empresaDao = dbAlmacen.empresaDao();
        inventarioDao = dbAlmacen.inventarioDao();
        islaDao = dbAlmacen.islaDao();
        jerarquiaClasificacionDao = dbAlmacen.jerarquiaClasificacionDao();
        presentacionDao = dbAlmacen.presentacionDao();
        productoDao = dbAlmacen.productoDao();
        productoUbicacionDao = dbAlmacen.productoUbicacionDao();
        stockAlmacenDao = dbAlmacen.stockAlmacenDao();
        ubicacionDao = dbAlmacen.ubicacionDao();
        unidadMedidaDao = dbAlmacen.unidadMedidaDao();
        usuarioDao = dbAlmacen.usuarioDao();
        usuarioxAlmacenDao = dbAlmacen.usuarioxAlmacenDao();
        vehiculoDao = dbAlmacen.vehiculoDao();
        verificacionDocumentoDao = dbAlmacen.verificacionDocumentoDao();
        zonaDao = dbAlmacen.zonaDao();

        detalleDocumentoDao = dbAlmacen.detalleDocumentoDao();
        enlaceDocumentoDao = dbAlmacen.enlaceDocumentoDao();
        lecturaDocumentoDao = dbAlmacen.lecturaDocumentoDao();
        lecturaInventarioDao = dbAlmacen.lecturaInventarioDao();
        lecturaVerificacionDocumentoDao = dbAlmacen.lecturaVerificacionDocumentoDao();
        movimientoInternoDao = dbAlmacen.movimientoInternoDao();
        productoDiferencialDao = dbAlmacen.productoDiferencialDao();
    }

    @Override
    public void insertAllEmpresa(List<Empresa> empresa) {
        threadPoolExecutor.execute(() -> {
            empresaDao.deleteAll();
            empresaDao.insertAll(empresa);
        });
    }

    @Override
    public void insertAllUsuario(List<Usuario> usuarios) {
        threadPoolExecutor.execute(() -> {
            usuarioDao.deteleAll();
            usuarioDao.insertAll(usuarios);
        });
    }

    @Override
    public void insertAllUsuarioxAlmacen(List<UsuarioxAlmacen> usuarioxAlmacen) {
        threadPoolExecutor.execute(() -> {
            usuarioxAlmacenDao.deleteAll();
            usuarioxAlmacenDao.insertAll(usuarioxAlmacen);
        });
    }

    @Override
    public void insertAllAlmacen(List<Almacen> almacen) {
        threadPoolExecutor.execute(() -> {
            almacenDao.deleteAll();
            almacenDao.insertAll(almacen);
        });
    }

    @Override
    public void insertAllUbicacion(List<Ubicacion> ubicacion) {
        threadPoolExecutor.execute(() -> {
            ubicacionDao.deleteAll();
            ubicacionDao.insertAll(ubicacion);
        });
    }

    @Override
    public void insertAllClaseDocumento(List<ClaseDocumento> claseDocumentos) {
        threadPoolExecutor.execute(() -> {
            claseDocumentoDao.deleteAll();
            claseDocumentoDao.insertAll(claseDocumentos);
        });
    }

    @Override
    public void insertAllProducto(List<Producto> productos) {
        threadPoolExecutor.execute(() -> {
            productoDao.insertAll(productos);
        });
    }

    @Override
    public void deleteProductos(){
        threadPoolExecutor.execute(() -> productoDao.deleteAll());
    }

    @Override
    public Observable<Integer> countProductos() {
        return productoDao.countProductos().toObservable();
    }

    @Override
    public void insertAllAlmacenVirtual(List<AlmacenVirtual> almacenVirtual) {
        threadPoolExecutor.execute(() -> {
            almacenVirtualDao.deleteAll();
            almacenVirtualDao.insertAll(almacenVirtual);
        });
    }

    @Override
    public void insertAllJerarquiaClasificacion(List<JerarquiaClasificacion> jerarquiaClasificacion) {
        threadPoolExecutor.execute(() -> {
            jerarquiaClasificacionDao.deleteAll();
            jerarquiaClasificacionDao.insertAll(jerarquiaClasificacion);
        });
    }

    @Override
    public void insertAllClasificacion(List<Clasificacion> clasificacion) {
        threadPoolExecutor.execute(() -> {
            clasificacionDao.deleteAll();
            clasificacionDao.insertAll(clasificacion);
        });
    }

    @Override
    public void insertAllDestino(List<Destino> destinos) {
        threadPoolExecutor.execute(() -> {
            destinoDao.deleteAll();
            destinoDao.insertAll(destinos);
        });
    }

    @Override
    public void insertAllCliente(List<Cliente> clientes) {
        threadPoolExecutor.execute(() -> {
            clienteDao.deleteAll();
            clienteDao.insertAll(clientes);
        });
    }

    @Override
    public void insertAllVehiculo(List<Vehiculo> vehiculos) {
        threadPoolExecutor.execute(() -> {
            vehiculoDao.deleteAll();
            vehiculoDao.insertAll(vehiculos);
        });
    }

    @Override
    public void insertAllZona(List<Zona> zonas) {
        threadPoolExecutor.execute(() -> {
            zonaDao.deleteAll();
            zonaDao.insertAll(zonas);
        });
    }

    @Override
    public void insertAllIsla(List<Isla> islas) {
        threadPoolExecutor.execute(() -> {
            islaDao.deleteAll();
            islaDao.insertAll(islas);
        });
    }

    @Override
    public void insertAllProductoUbicacion(List<ProductoUbicacion> productoUbicacion) {
        threadPoolExecutor.execute(() -> {
            productoUbicacionDao.deleteAll();
            productoUbicacionDao.insertAll(productoUbicacion);
        });
    }

    @Override
    public void insertAllCajaProducto(List<CajaProducto> cajaProductos) {
        threadPoolExecutor.execute(() -> {
            cajaProductoDao.deleteAll();
            cajaProductoDao.insertAll(cajaProductos);
        });
    }

    @Override
    public void insertAllUnidadMedida(List<UnidadMedida> unidadMedidas) {
        threadPoolExecutor.execute(() -> {
            unidadMedidaDao.deleteAll();
            unidadMedidaDao.insertAll(unidadMedidas);
        });
    }

    @Override
    public void insertAllPresentacion(List<Presentacion> presentacion) {
        threadPoolExecutor.execute(() -> {
            presentacionDao.deleteAll();
            presentacionDao.insertAll(presentacion);
        });
    }

    @Override
    public void insertAllStockAlmacen(List<StockAlmacen> stockAlmacen) {
        threadPoolExecutor.execute(() -> {
            stockAlmacenDao.deleteAll();
            stockAlmacenDao.insertAll(stockAlmacen);
        });
    }

    @Override
    public void insertAllInventario(List<Inventario> inventarios) {
        threadPoolExecutor.execute(() -> {
            inventarioDao.deleteInventario();
            inventarioDao.insertAll(inventarios);
        });
    }

    @Override
    public void insertDetalleInventario(List<DetalleInventario> detalleInventarios){
        threadPoolExecutor.execute(() -> {
            detalleInventarioDao.eliminarDetalleInventarioAdmin();
            detalleInventarioDao.insertAll(detalleInventarios);
        });
    }

    // Procesos

    @Override
    public Observable<List<Almacen>> getListAlmacen() {
        return almacenDao.getListAlmacen().toObservable();
    }

    @Override
    public Observable<List<Inventario>> getListInventarioDB(){
        return inventarioDao.getAllInventarioDB().toObservable();
    }

    @Override
    public void deleteClaseDocumento(){
        threadPoolExecutor.execute(() -> claseDocumentoDao.deleteAll());
    }

    //---- MODO BATCH --- ///

    @Override
    public Observable<List<ClaseDocumento>> getListClaseDocumentoBatch(String codTipoDocumento, String flgConSinDoc){
        return claseDocumentoDao.getListClaseDocumentoBatch(codTipoDocumento, flgConSinDoc).toObservable();
    }

    @Override
    public void insertDocumentos(List<Documento> documentos){
        threadPoolExecutor.execute(() -> documentoDao.insertAll(documentos));
    }

    @Override
    public void deleteDocumentos(){
        threadPoolExecutor.execute(() -> documentoDao.deleteDocumento());
    }

    @Override
    public void deleteDetalleDocumentoOri() { threadPoolExecutor.execute(() -> detalleDocumentoOriDao.deleteDetalleDocumentoOri()) ;}

    @Override
    public void insertDetalleDocumentoOri(List<DetalleDocumentoOri> entities){
        threadPoolExecutor.execute(() -> detalleDocumentoOriDao.insertAll(entities));
    }

    @Override
    public Observable<List<Documento>> getListDocumento(String codTipoDocumento, String codClaseDocumento,
                                             String numDocumento, int idAlmacen){
        return documentoDao.getListDocumento(codTipoDocumento, codClaseDocumento,numDocumento, idAlmacen).toObservable();
    }

    @Override
    public Observable<List<Documento>> getListDocumento(String codTipoDocumento, String codClaseDocumento, int idAlmacen){
        return documentoDao.getListDocumento(codTipoDocumento, codClaseDocumento, idAlmacen).toObservable();
    }

    @Override
    public Observable<List<DetalleDocumentoLive>> getDetalleListBD(int idDocumento){
        return detalleDocumentoOriDao.getDetalleListBD(idDocumento).toObservable();
    }

    @Override
    public Observable<List<Ubicacion>> verifyUbicacionBD(int idAlmacen, String codUbicacion){
        return ubicacionDao.verifyUbicacionBD(idAlmacen, codUbicacion).toObservable();
    }

    @Override
    public Observable<List<Producto>> verifyProductoDB(String codProducto){
        return productoDao.verifyProductoDB(codProducto).toObservable();
    }

    @Override
    public Observable<List<SerieResponse>> verifySerieRecepcionBD(int idProducto, String serieProducto, int idDocumento){
        return lecturaDocumentoDao.verifySerieRecepcionBD(idProducto, serieProducto, idDocumento).toObservable();
    }

    @Override
    public Observable<Integer> verifyDetalleDocumentoBD(String loteProducto, String numDocInterno, int linea, int idDocumento, int idProducto){
        return detalleDocumentoDao.verifyDetalleDocumentoBD(loteProducto, numDocInterno, linea, idDocumento, idProducto).toObservable();
    }

    @Override
    public void updateDetalleDocumentoBD(double ctdAtendidaUpdate, String loteProducto, String numDocInterno, int linea, int idDocumento,
                                         String codUsuarioModificacion, String fchModificacion, int idProducto){
        threadPoolExecutor.execute(() -> detalleDocumentoDao.updateDetalleDocumentoBD(ctdAtendidaUpdate, loteProducto,
                numDocInterno, linea, idDocumento,codUsuarioModificacion,fchModificacion, idProducto));
    }

    @Override
    public void insertDetalleDocumentoBD(DetalleDocumento detalleDocumento){
        threadPoolExecutor.execute(() -> detalleDocumentoDao.insertDetalleDocumentoBD(detalleDocumento));
    }

    @Override
    public void insertLecturaDocumentoBD(LecturaDocumento lecturaDocumento){
        threadPoolExecutor.execute(() -> lecturaDocumentoDao.insertLecturaDocumentoBD(lecturaDocumento));
    }

    @Override
    public Observable<Integer> getIDDetalleDocumentoBD(String loteProducto, String numDocInterno, int linea, int idDocumento, int idProducto){
        return detalleDocumentoDao.getIDDetalleDocumentoBD(loteProducto, numDocInterno, linea, idDocumento, idProducto).toObservable();
    }

    @Override
    public Observable<List<DetalleDocumento>> getDetalleDocumentoByProductoBD(int idProducto, int idDocumento){
        return detalleDocumentoDao.getDetalleDocumentoByProductoBD(idProducto,idDocumento).toObservable();
    }

    @Override
    public void limpiarBdAdmin(){
        threadPoolExecutor.execute(() -> {
            detalleDocumentoDao.eliminarDetalleAdmin();
            lecturaDocumentoDao.eliminarLecturaAdmin();
            movimientoInternoDao.eliminarMovimientoAdmin();
            detalleInventarioDao.eliminarDetalleInventarioAdmin();
            lecturaInventarioDao.eliminarLecturaInventarioAdmin();
            detalleDocumentoOriDao.deleteDetalleDocumentoOri();
            documentoDao.deleteDocumento();
        });
    }

    @Override
    public Observable<Integer> countDetalleOri(){
        return detalleDocumentoOriDao.countDetalleOri().toObservable();
    }

    @Override
    public Observable<Integer> countDocumento(){
        return documentoDao.countDocumento().toObservable();
    }

    @Override
    public Observable<Integer> countDetalle(){
        return detalleDocumentoDao.countDetalle().toObservable();
    }

    @Override
    public Observable<Integer> countDetalleInventario(){
        return detalleInventarioDao.countDetalleInventario().toObservable();
    }

    @Override
    public Observable<Integer> countLectura(){
        return lecturaDocumentoDao.countLectura().toObservable();
    }

    @Override
    public Observable<Integer> countMovimiento(){
        return movimientoInternoDao.countMovimiento().toObservable();
    }

    @Override
    public void insertMovimientoInterno(MovimientoInterno movimientoInterno){
        threadPoolExecutor.execute(() -> movimientoInternoDao.insertMovimientoInterno(movimientoInterno));
    }

    @Override
    public Observable<List<Integer>> getIDLecturaDocumento(int idDetalleDocumento, int idProducto,
                                                     String loteProducto, String serieProducto){
        return lecturaDocumentoDao.getIDLecturaDocumento(idDetalleDocumento, idProducto, loteProducto, serieProducto).toObservable();
    }

    @Override
    public Observable<List<HijoConsulta>> getLecturasBD(String codTipoDocumento, int idClaseDocumento, String numDocumento,
                                       String flgActivo, String flgEnviado, String codUsuarioRegistro){
        return lecturaDocumentoDao.getLecturasBD(codTipoDocumento, idClaseDocumento,
                numDocumento, flgActivo, flgEnviado, codUsuarioRegistro).toObservable();
    }

    @Override
    public void updateDetalleDocumentoEliminacion(double ctdAtendidaRestar, int idDetalleDocumento,
                                                  String codUsuario, String fchModificacion){
        threadPoolExecutor.execute(() -> detalleDocumentoDao.updateDetalleDocumentoEliminacion(ctdAtendidaRestar,
                idDetalleDocumento,codUsuario,fchModificacion));
    }

    @Override
    public void updateMovimientoEliminacion(String codUsuario,String fchModificacion, int idLecturaDocumento){
        threadPoolExecutor.execute(() -> movimientoInternoDao.updateMovimientoEliminacion(codUsuario, fchModificacion, idLecturaDocumento));
    }

    @Override
    public void deleteLecturaBD(String codUsuario, String fchModificacion, String idTerminal, String fchRegistroTerminal,
                         int idDetalleDocumento, int idLecturaDocumento){
        threadPoolExecutor.execute(() -> lecturaDocumentoDao.deleteLecturaBD(codUsuario, fchModificacion, idTerminal,
                fchRegistroTerminal, idDetalleDocumento, idLecturaDocumento));
    }

    @Override
    public Observable<Integer> getLecturasByIdDetalle(int idDetalleDocumento){
        return lecturaDocumentoDao.getLecturasByIdDetalle(idDetalleDocumento).toObservable();
    }

    @Override
    public void cerrarDetalleDocumentoEliminacion(String codUsuario,String fchModificacion,int idDetalleDocumento){
        threadPoolExecutor.execute(() -> detalleDocumentoDao.cerrarDetalleDocumentoEliminacion(codUsuario, fchModificacion, idDetalleDocumento));
    }

    @Override
    public void cerrarLecturaDocumento(int idLecturaDocumento, String fchModificacion, String guia){
        threadPoolExecutor.execute(() -> lecturaDocumentoDao.cerrarLecturaDocumento(idLecturaDocumento, fchModificacion, guia));
    }

    @Override
    public void cerrarDetalleDocumento(String codUsuario, String fchModificacion, int idDetalleDocumento){
        threadPoolExecutor.execute(() -> detalleDocumentoDao.cerrarDetalleDocumento(codUsuario, fchModificacion, idDetalleDocumento));
    }

    @Override
    public Observable<List<Integer>> getIdLecturaDocumentoByIdDetalle(int idDetalleDocumento){
        return lecturaDocumentoDao.getIdLecturaDocumentoByIdDetalle(idDetalleDocumento).toObservable();
    }

    @Override
    public void cerrarMovimiento(int idLecturaDocumento, String codUsuario, String fchModificacion){
        movimientoInternoDao.cerrarMovimiento(idLecturaDocumento,codUsuario,fchModificacion);
    }

    @Override
    public void cerrarDocumento(String codUsuario, String fchModificacion, int idDocumento){
        threadPoolExecutor.execute(() -> documentoDao.cerrarDocumento(codUsuario, fchModificacion, idDocumento));
    }

    @Override
    public Observable<List<LecturaDocumento>> getLecturaDocumentoCerrar(String guia){
        return lecturaDocumentoDao.getLecturaDocumentoCerrar(guia).toObservable();
    }

    @Override
    public Observable<List<DetalleDocumento>> getDetalleDocumentoCerrar(int idDocumento, String numDocInterno){
        return detalleDocumentoDao.getDetalleDocumentoCerrar(idDocumento, numDocInterno).toObservable();
    }

    @Override
    public Observable<List<DetalleDocumentoOri>> getDetalleDocumentoOri(int idDocumento){
        return detalleDocumentoOriDao.getDetalleDocumentoOri(idDocumento).toObservable();
    }

    @Override
    public void finalizarDetalleDocumento(String codUsuario, String fchModificacion, int idDetalleDocumento){
        threadPoolExecutor.execute(() -> detalleDocumentoDao.finalizarDetalleDocumento(codUsuario, fchModificacion, idDetalleDocumento));
    }

    @Override
    public Observable<Integer> verifyDocumento(String numDocumento){
        return documentoDao.verifyDocumento(numDocumento).toObservable();
    }

    @Override
    public void insertDocumento(Documento documento){
        threadPoolExecutor.execute(() -> documentoDao.insertDocumento(documento));
    }

    @Override
    public Observable<Integer> getIdDocumento(String numDocumento){
        return documentoDao.getIdDocumento(numDocumento).toObservable();
    }

    @Override
    public Observable<List<DetalleDocumento>> getDetalleDocumentoByDocumentoBD(int idDocumento){
        return detalleDocumentoDao.getDetalleDocumentoByDocumentoBD(idDocumento).toObservable();
    }

    @Override
    public Observable<List<Producto>> getProductoByID(int idProducto){
        return productoDao.getProductoByID(idProducto).toObservable();
    }

    @Override
    public void cerrarLecturaDocumentoByIdDetalle(int idDetalleDocumento, String codUsuario, String fchModificacion){
        threadPoolExecutor.execute(() -> lecturaDocumentoDao.cerrarLecturaDocumentoByIdDetalle(idDetalleDocumento, codUsuario, fchModificacion));
    }

    @Override
    public Observable<Integer> getLecturasCountBD(String codTipoDocumento, int idClaseDocumento, String numDocumento,
                                       String flgActivo, String flgEnviado, String codUsuarioRegistro){
        return lecturaDocumentoDao.getLecturasCountBD(codTipoDocumento, idClaseDocumento, numDocumento,
                flgActivo, flgEnviado, codUsuarioRegistro).toObservable();
    }

    @Override
    public void cancelarDocumentoBD(String codUsuario, String fchModificacion, int idDocumento){
        threadPoolExecutor.execute(() -> documentoDao.cancelarDocumento(codUsuario,fchModificacion,idDocumento));
    }

    @Override
    public Observable<List<String>> getGuiaCerradas(String numDocumento, int idClaseDocumento){
        return detalleDocumentoDao.getGuiaCerradas(numDocumento, idClaseDocumento).toObservable();
    }

    @Override
    public Observable<List<DetalleImpresion>> getDetalleImpresionBD(String numDocumento, String numDocInterno){
        return detalleDocumentoDao.getDetalleImpresion(numDocumento,numDocInterno).toObservable();
    }

    @Override
    public Observable<List<DetalleImpresion>> getLecturaImpresion(String numDocumento, String numDocInterno){
        return lecturaDocumentoDao.getLecturaImpresion(numDocumento,numDocInterno).toObservable();
    }

    @Override
    public Observable<List<Usuario>> getUsuario(String codUsuario){
        return usuarioDao.getUsuario(codUsuario).toObservable();
    }

    @Override
    public Observable<List<Almacen>> getAllAlmacenByUsuarioBD(int idUsuario){
        return almacenDao.getAllAlmacenByUsuarioBD(idUsuario).toObservable();
    }

    @Override
    public Observable<List<Inventario>> getAllInventarioByAlmacen(int idAlmacen){
        return inventarioDao.getAllInventarioByAlmacen(idAlmacen).toObservable();
    }

    @Override
    public Observable<Integer> getLecturasInventarioCountBD(int idInventario, String codUsuarioRegistro){
        return lecturaInventarioDao.getLecturasCountBD(idInventario, codUsuarioRegistro).toObservable();
    }

    @Override
    public void insertProductoDiferencial(List<ProductoDiferencial> entities) {
        threadPoolExecutor.execute(() -> {
            productoDiferencialDao.deleteAll();
            productoDiferencialDao.insertAll(entities);
        });
    }

    @Override
    public Observable<List<ProductoDiferencial>> getAllProductoDiferencial(){
        return productoDiferencialDao.getAllProductoDiferencial().toObservable();
    }

    @Override
    public Observable<Integer> getProductoDiferencialCount(){
        return productoDiferencialDao.getProductoDiferencialCount().toObservable();
    }

    @Override
    public void insertLecturaInventario(LecturaInventario lecturaInventario){
        threadPoolExecutor.execute(() -> lecturaInventarioDao.insertLecturaInventario(lecturaInventario));
    }

    @Override
    public Observable<List<SerieResponseInventario>> obtenerSerieInventarioBD(int idInventario, int idProducto,
                                                                              String serieProducto){
        return lecturaInventarioDao.obtenerSerieInventarioBD(idInventario, idProducto, serieProducto).toObservable();
    }

    @Override
    public Observable<List<HijoConsultaInventario>> getLecturaInventarioByUbicacion(int idInventario,
                                                                                    int idUbicacion,
                                                                                    String codUsuarioRegistro){
        return lecturaInventarioDao.getLecturaInventarioByUbicacion(idInventario, idUbicacion, codUsuarioRegistro).toObservable();
    }

    @Override
    public Observable<List<HijoConsultaInventario>> getLecturaInventarioByProducto(int idInventario,
                                                                                   int idProducto,
                                                                                   String codUsuarioRegistro){
        return lecturaInventarioDao.getLecturaInventarioByProducto(idInventario, idProducto, codUsuarioRegistro).toObservable();
    }

    @Override
    public Observable<List<HijoConsultaInventario>> getLecturaInventarioByLote(int idInventario,
                                                                               String loteProducto,
                                                                               String codUsuarioRegistro){
        return lecturaInventarioDao.getLecturaInventarioByLote(idInventario, loteProducto, codUsuarioRegistro).toObservable();
    }

    @Override
    public Observable<List<HijoConsultaInventario>> getLecturaInventarioBySerie(int idInventario,
                                                                                String serieProducto,
                                                                                String codUsuarioRegistro){
        return lecturaInventarioDao.getLecturaInventarioBySerie(idInventario, serieProducto, codUsuarioRegistro).toObservable();
    }

    @Override
    public Observable<Integer> verifyDetalleInventarioBD(int idInventario, int idUbicacion, int idProducto, String loteProducto){
        return detalleInventarioDao.verifyDetalleInventarioBD(idInventario, idUbicacion, idProducto, loteProducto).toObservable();
    }

    @Override
    public void updateDetalleInventarioBD(double stockInventariado, String codUsuarioModificacion, String fchModificacion,
                                   int idInventario,int idUbicacion,int idProducto,String loteProducto){
        threadPoolExecutor.execute(() -> detalleInventarioDao.updateDetalleInventarioBD(stockInventariado,
                codUsuarioModificacion, fchModificacion, idInventario, idUbicacion, idProducto, loteProducto));
    }

    @Override
    public void insertDetalleInventario(DetalleInventario detalleInventario){
        threadPoolExecutor.execute(() -> detalleInventarioDao.insertDetalleInventario(detalleInventario));
    }

    @Override
    public Observable<Integer> getIdDetalleInventarioBD(int idInventario, int idUbicacion, int idProducto,
                                                        String loteProducto){
        return detalleInventarioDao.getIdDetalleInventarioBD(idInventario, idUbicacion, idProducto, loteProducto).toObservable();
    }

    @Override
    public Observable<LecturaInventario> getLecturaEliminar(int idLecturaInventario, String codUsuarioRegistro){
        return lecturaInventarioDao.getLecturaEliminar(idLecturaInventario, codUsuarioRegistro).toObservable();
    }

    @Override
    public void eliminarLectura(int idLecturaInventario, String codUsuarioRegistro){
        threadPoolExecutor.execute(() -> lecturaInventarioDao.eliminarLectura(idLecturaInventario, codUsuarioRegistro));
    }

    @Override
    public void updateDetalleLecturaEliminar(double cantidadEliminar, int idDetalleInventario,
                                             String codUsuarioModificacion, String fchModificacion){
        threadPoolExecutor.execute(() -> detalleInventarioDao.updateDetalleLecturaEliminar(cantidadEliminar,
                idDetalleInventario, codUsuarioModificacion,fchModificacion));
    }

    @Override
    public Observable<List<LecturaInventario>> getLecturaEliminarByDetalle(int idDetalleInventario,
                                                                           String codUsuarioRegistro){
        return lecturaInventarioDao.getLecturaEliminarByDetalle(idDetalleInventario, codUsuarioRegistro).toObservable();
    }

    @Override
    public void eliminarLecturasAdmin(){
        threadPoolExecutor.execute(() -> lecturaInventarioDao.eliminarLecturaInventarioAdmin());
    }

    @Override
    public void eliminarDetallesAdmin(String codUsuarioModificacion, String fchModificacion){
        threadPoolExecutor.execute(() -> detalleInventarioDao.eliminarDetallesAdmin(codUsuarioModificacion, fchModificacion));
    }

    @Override
    public Observable<Integer> countLecturasInventarioBD(){
        return lecturaInventarioDao.countLecturasInventarioBD().toObservable();
    }

    @Override
    public Observable<List<Documento>> getAllDocumentosExportar(){
        return documentoDao.getAllDocumentosExportar().toObservable();
    }

    @Override
    public void updateDocumentoTrabajado(String codUsuarioModificacion, String fchModificacion, int idDocumento){
        threadPoolExecutor.execute(() -> documentoDao.updateDocumentoTrabajado(codUsuarioModificacion, fchModificacion, idDocumento));
    }

    @Override
    public Observable<List<DetalleDocumento>> getAllDetalleExportar(){
        return detalleDocumentoDao.getAllDetalleExportar().toObservable();
    }

    @Override
    public Observable<List<LecturaDocumento>> getAllLecturaExportar(){
        return lecturaDocumentoDao.getAllLecturaExportar().toObservable();
    }

    @Override
    public void updateLecturaDescargada(String fchDescargado,String codUsuarioModificacion, String fchModificacion,
                                 int idLecturaDocumento){
        threadPoolExecutor.execute(() -> lecturaDocumentoDao.updateLecturaDescargada(fchDescargado, codUsuarioModificacion,
                fchModificacion, idLecturaDocumento));
    }

    @Override
    public Observable<List<DetalleDocumentoOri>> getAllDetalleDocumentoOriExportar(){
        return detalleDocumentoOriDao.getAllDetalleDocumentoOriExportar().toObservable();
    }

    @Override
    public Observable<List<VerificacionDocumento>> getAllVerificacionExportar(){
        return verificacionDocumentoDao.getAllVerificacionExportar().toObservable();
    }

    @Override
    public Observable<List<Inventario>> getAllInventarioExportar(){
        return inventarioDao.getAllInventarioExportar().toObservable();
    }

    @Override
    public Observable<Integer> getCountLecturaInventarioExportar(){
        return lecturaInventarioDao.getCountLecturaInventarioExportar().toObservable();
    }

    @Override
    public Observable<List<LecturaInventario>> getLecturaInventarioExportar(int limit){
        return lecturaInventarioDao.getLecturaInventarioExportar(limit).toObservable();
    }

    @Override
    public void updateLecturaInventarioDescargada(String fchDescargado,String codUsuarioModificacion, String fchModificacion,
                                           int idLecturaInventario){
        threadPoolExecutor.execute(() -> lecturaInventarioDao.updateLecturaInventarioDescargada(fchDescargado, codUsuarioModificacion,
                fchModificacion, idLecturaInventario));
    }

    @Override
    public void updateDetalleDescargado(String codUsuarioModificacion, String fchModificacion, int idDetalleDocumento){
        threadPoolExecutor.execute(() -> detalleDocumentoDao.updateDetalleDescargado(codUsuarioModificacion, fchModificacion, idDetalleDocumento));
    }

    @Override
    public Observable<Integer> claseDocumentoCount(){
        return claseDocumentoDao.claseDocumentoCount().toObservable();
    }

    @Override
    public Observable<List<DetalleDocumento>> getDetalleDespachoCerrar(){
        return detalleDocumentoDao.getDetalleDespachoCerrar().toObservable();
    }

    @Override
    public Observable<String> getConSinDoc(int idDocumento){
        return documentoDao.getConSinDoc(idDocumento).toObservable();
    }

}
