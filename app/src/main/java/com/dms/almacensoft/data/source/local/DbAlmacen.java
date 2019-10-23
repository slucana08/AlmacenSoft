package com.dms.almacensoft.data.source.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.dms.almacensoft.data.entities.dbalmacen.Almacen;
import com.dms.almacensoft.data.entities.dbalmacen.AlmacenVirtual;
import com.dms.almacensoft.data.entities.dbalmacen.CajaProducto;
import com.dms.almacensoft.data.entities.dbalmacen.ClaseDocumento;
import com.dms.almacensoft.data.entities.dbalmacen.Clasificacion;
import com.dms.almacensoft.data.entities.dbalmacen.Cliente;
import com.dms.almacensoft.data.entities.dbalmacen.Destino;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumentoOri;
import com.dms.almacensoft.data.entities.dbtransact.DetalleInventario;
import com.dms.almacensoft.data.entities.dbalmacen.DetalleVerificacionDocumento;
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
import com.dms.almacensoft.data.entities.dbalmacen.VerificacionDocumento;
import com.dms.almacensoft.data.entities.dbalmacen.Zona;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumento;
import com.dms.almacensoft.data.entities.dbtransact.EnlaceDocumento;
import com.dms.almacensoft.data.entities.dbtransact.LecturaDocumento;
import com.dms.almacensoft.data.entities.dbtransact.LecturaInventario;
import com.dms.almacensoft.data.entities.dbtransact.LecturaVerificacionDocumento;
import com.dms.almacensoft.data.entities.dbtransact.MovimientoInterno;
import com.dms.almacensoft.data.entities.dbtransact.ProductoDiferencial;
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

@Database(entities = {Usuario.class, UsuarioxAlmacen.class, Empresa.class, Almacen.class, Ubicacion.class,
        ClaseDocumento.class, Producto.class, AlmacenVirtual.class, Documento.class, DetalleDocumentoOri.class,
        Inventario.class, DetalleInventario.class, CajaProducto.class, Presentacion.class, VerificacionDocumento.class,
        DetalleVerificacionDocumento.class, JerarquiaClasificacion.class, Clasificacion.class, Destino.class,
        Cliente.class, Vehiculo.class, Zona.class, Isla.class, StockAlmacen.class, ProductoUbicacion.class,
        UnidadMedida.class, DetalleDocumento.class, EnlaceDocumento.class, LecturaDocumento.class, LecturaInventario.class,
        LecturaVerificacionDocumento.class, MovimientoInterno.class, ProductoDiferencial.class}, version = 1,exportSchema = false)
public abstract class DbAlmacen extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract UsuarioxAlmacenDao usuarioxAlmacenDao();
    public abstract EmpresaDao empresaDao();
    public abstract AlmacenDao almacenDao();
    public abstract UbicacionDao ubicacionDao();
    public abstract ClaseDocumentoDao claseDocumentoDao();
    public abstract ProductoDao productoDao();
    public abstract AlmacenVirtualDao almacenVirtualDao();
    public abstract DocumentoDao documentoDao();
    public abstract DetalleDocumentoOriDao detalleDocumentoOriDao();
    public abstract InventarioDao inventarioDao();
    public abstract DetalleInventarioDao detalleInventarioDao();
    public abstract CajaProductoDao cajaProductoDao();
    public abstract PresentacionDao presentacionDao();
    public abstract VerificacionDocumentoDao verificacionDocumentoDao();
    public abstract DetalleVerificacionDocumentoDao detalleVerificacionDocumentoDao();
    public abstract JerarquiaClasificacionDao jerarquiaClasificacionDao();
    public abstract ClasificacionDao clasificacionDao();
    public abstract DestinoDao destinoDao();
    public abstract ClienteDao clienteDao();
    public abstract VehiculoDao vehiculoDao();
    public abstract ZonaDao zonaDao();
    public abstract IslaDao islaDao();
    public abstract StockAlmacenDao stockAlmacenDao();
    public abstract ProductoUbicacionDao productoUbicacionDao();
    public abstract UnidadMedidaDao unidadMedidaDao();
    public abstract DetalleDocumentoDao detalleDocumentoDao();
    public abstract EnlaceDocumentoDao enlaceDocumentoDao();
    public abstract LecturaDocumentoDao lecturaDocumentoDao();
    public abstract LecturaInventarioDao lecturaInventarioDao();
    public abstract LecturaVerificacionDocumentoDao lecturaVerificacionDocumentoDao();
    public abstract MovimientoInternoDao movimientoInternoDao();
    public abstract ProductoDiferencialDao productoDiferencialDao();

    private static volatile DbAlmacen INSTANCE;

    public static DbAlmacen getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (DbAlmacen.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DbAlmacen.class, "DB_ALMACEN")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                }
            };
}
