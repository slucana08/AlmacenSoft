package com.dms.almacensoft.data.source.local.daotransact;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumento;
import com.dms.almacensoft.data.models.impresion.DetalleImpresion;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface DetalleDocumentoDao {

    @Query("SELECT COUNT(*) FROM DetalleDocumento WHERE loteProducto=:loteProducto AND numDocInterno=:numDocInterno" +
            " AND linea=:linea AND idDocumento=:idDocumento AND idProducto=:idProducto AND flgActivo='1' AND " +
            "flgDescargado = 0")
    Single<Integer> verifyDetalleDocumentoBD(String loteProducto, String numDocInterno, int linea, int idDocumento, int idProducto);

    @Query("SELECT idDetalleDocumento FROM DetalleDocumento WHERE loteProducto=:loteProducto AND numDocInterno=:numDocInterno" +
            " AND linea=:linea AND idDocumento=:idDocumento AND idProducto=:idProducto AND flgActivo='1' AND flgDescargado = 0")
    Single<Integer> getIDDetalleDocumentoBD(String loteProducto, String numDocInterno, int linea, int idDocumento, int idProducto);

    @Query("UPDATE DetalleDocumento SET ctdAtendida = ctdAtendida + :ctdAtendidaUpdate,codUsuarioModificacion=:codUsuarioModificacion" +
            ", fchModificacion=:fchModificacion, flgCerrarEnvio='0' WHERE loteProducto=:loteProducto" +
            " AND numDocInterno=:numDocInterno AND linea=:linea AND idDocumento=:idDocumento AND idProducto=:idProducto AND flgActivo='1' AND " +
            "flgDescargado = 0")
    void updateDetalleDocumentoBD(double ctdAtendidaUpdate, String loteProducto, String numDocInterno, int linea, int idDocumento,
                                  String codUsuarioModificacion, String fchModificacion, int idProducto);

    @Query("SELECT * FROM DetalleDocumento WHERE idProducto=:idProducto AND idDocumento=:idDocumento AND flgActivo='1' " +
            "AND flgCerrarEnvio = '0' AND flgDescargado = 0")
    Single<List<DetalleDocumento>> getDetalleDocumentoByProductoBD(int idProducto, int idDocumento);

    @Insert
    long insertDetalleDocumentoBD(DetalleDocumento detalleDocumento);

    @Query("UPDATE DetalleDocumento SET ctdAtendida = ctdAtendida - :ctdAtendidaRestar," +
            "codUsuarioModificacion=:codUsuario,fchModificacion=:fchModificacion WHERE idDetalleDocumento=:idDetalleDocumento" +
            " AND flgActivo='1' AND flgDescargado = 0")
    void updateDetalleDocumentoEliminacion(double ctdAtendidaRestar, int idDetalleDocumento, String codUsuario, String fchModificacion);

    @Query("UPDATE DetalleDocumento SET flgActivo='0',codUsuarioModificacion=:codUsuario,fchModificacion=:fchModificacion" +
            " WHERE idDetalleDocumento=:idDetalleDocumento AND flgActivo='1' AND flgCerrarEnvio='0' AND flgDescargado = 0")
    void cerrarDetalleDocumentoEliminacion(String codUsuario,String fchModificacion,int idDetalleDocumento);

    @Query("DELETE FROM DetalleDocumento")
    void eliminarDetalleAdmin();

    @Query("SELECT * FROM DetalleDocumento WHERE idDocumento=:idDocumento AND numDocInterno=:numDocInterno AND flgActivo = '1' " +
            "AND flgCerrarEnvio = '0' AND flgDescargado = 0")
    Single<List<DetalleDocumento>> getDetalleDocumentoCerrar(int idDocumento, String numDocInterno);

    @Query("UPDATE DetalleDocumento SET ctdAtendidaCliente = ctdAtendida + ctdAtendidaCliente,ctdAtendida = 0.0,flgCerrarEnvio = '1'," +
            "codUsuarioModificacion=:codUsuario,fchModificacion=:fchModificacion WHERE idDetalleDocumento=:idDetalleDocumento " +
            "AND flgDescargado = 0")
    void cerrarDetalleDocumento(String codUsuario, String fchModificacion, int idDetalleDocumento);

    @Query("UPDATE DetalleDocumento SET flgCierreDoc = '1',codUsuarioModificacion=:codUsuario,fchModificacion=:fchModificacion " +
            "WHERE idDetalleDocumento=:idDetalleDocumento AND flgDescargado = 0")
    void finalizarDetalleDocumento(String codUsuario, String fchModificacion, int idDetalleDocumento);

    @Query("SELECT * FROM DetalleDocumento WHERE idDocumento=:idDocumento AND flgActivo='1' AND flgCerrarEnvio = '0' AND flgDescargado = 0")
    Single<List<DetalleDocumento>> getDetalleDocumentoByDocumentoBD(int idDocumento);

    @Query("SELECT DISTINCT DD.numDocInterno FROM DetalleDocumento DD " +
            "INNER JOIN Documento D ON D.idDocumento = DD.idDocumento " +
            "AND D.numDocumento=:numDocumento AND D.codTipoDocumento = 'R' " +
            "AND D.idClaseDocumento=:idClaseDocumento WHERE DD.flgCerrarEnvio = '1'")
    Single<List<String>> getGuiaCerradas(String numDocumento, int idClaseDocumento);

    @Query("SELECT DD.idDetalleDocumento,P.codProducto,P.dscProducto,DD.loteProducto,'' AS serieProducto," +
            "DD.numDocInterno, 0 AS idLecturaDocumento FROM DetalleDocumento DD " +
            "INNER JOIN Producto P ON P.idProducto = DD.idProducto " +
            "INNER JOIN Documento D ON D.idDocumento = DD.idDocumento " +
            "WHERE D.numDocumento=:numDocumento AND DD.numDocInterno=:numDocInterno AND DD.flgCerrarEnvio = '1'")
    Single<List<DetalleImpresion>> getDetalleImpresion(String numDocumento, String numDocInterno);

    @Query("SELECT COUNT(*) FROM DetalleDocumento")
    Single<Integer> countDetalle();

    @Query("SELECt * FROM Detalledocumento WHERE flgDescargado = 0 AND flgActivo = '1'")
    Single<List<DetalleDocumento>> getAllDetalleExportar();

    @Query("UPDATE DetalleDocumento SET flgDescargado = 1,codUsuarioModificacion=:codUsuarioModificacion, " +
            "fchModificacion=:fchModificacion WHERE idDetalleDocumento=:idDetalleDocumento")
    void updateDetalleDescargado(String codUsuarioModificacion, String fchModificacion, int idDetalleDocumento);

    @Query("SELECT * FROM DetalleDocumento DD " +
            "INNER JOIN Documento D ON DD.idDocumento = D.idDocumento " +
            "WHERE D.codTipoDocumento = 'D' AND DD.flgCerrarEnvio = '0'")
    Single<List<DetalleDocumento>> getDetalleDespachoCerrar();
}
