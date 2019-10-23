package com.dms.almacensoft.data.source.local.daotransact;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbtransact.LecturaDocumento;
import com.dms.almacensoft.data.models.impresion.DetalleImpresion;
import com.dms.almacensoft.data.models.recepciondespacho.HijoConsulta;
import com.dms.almacensoft.data.models.recepciondespacho.SerieResponse;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface LecturaDocumentoDao {

    @Insert
    long insertLecturaDocumentoBD(LecturaDocumento lecturaDocumento);

    @Query("SELECT LD.idProducto,P.codProducto, P.dscProducto, LD.serieProducto,LD.idLecturaDocumento FROM LecturaDocumento LD " +
            "INNER JOIN DetalleDocumento DD ON LD.idDetalleDocumento = DD.idDetalleDocumento " +
            "INNER JOIN PRODUCTO P ON LD.idProducto AND P.idProducto "+
            "WHERE LD.idProducto=:idProducto AND LD.serieProducto=:serieProducto AND DD.idDocumento=:idDocumento " +
            "AND LD.flgActivo = '1' AND DD.flgActivo = '1'")
    Single<List<SerieResponse>> verifySerieRecepcionBD(int idProducto, String serieProducto, int idDocumento);

    @Query("DELETE FROM LecturaDocumento")
    void eliminarLecturaAdmin();

    @Query("SELECT idLecturaDocumento FROM LecturaDocumento WHERE idDetalleDocumento=:idDetalleDocumento " +
            "AND idProducto=:idProducto AND loteProducto=:loteProducto AND serieProducto=:serieProducto")
    Single<List<Integer>> getIDLecturaDocumento(int idDetalleDocumento, int idProducto, String loteProducto, String serieProducto);

    @Query("SELECT DD.idDetalleDocumento,LD.idLecturaDocumento,P.dscProducto,P.codProducto,LD.loteProducto"+
            ",LD.serieProducto,LD.ctdAsignada,DD.numDocInterno,LD.idUbicacion,U.codUbicacion FROM LecturaDocumento LD " +
            "INNER JOIN DetalleDocumento DD ON DD.idDetalleDocumento = LD.idDetalleDocumento " +
            "INNER JOIN Documento D ON D.idDocumento = DD.idDocumento " +
            "INNER JOIN Producto P ON P.idProducto = DD.idProducto " +
            "INNER JOIN Ubicacion U ON LD.idUbicacion = U.idUbicacion " +
            "WHERE D.codTipoDocumento =:codTipoDocumento AND D.idClaseDocumento =:idClaseDocumento " +
            "AND D.numDocumento =:numDocumento AND LD.flgActivo =:flgActivo AND LD.flgEnviado =:flgEnviado " +
            "AND LD.codUsarioRegistro =:codUsuarioRegistro ORDER BY LD.idLecturaDocumento DESC")
    Single<List<HijoConsulta>> getLecturasBD(String codTipoDocumento, int idClaseDocumento, String numDocumento,
                                       String flgActivo, String flgEnviado, String codUsuarioRegistro);

    @Query("SELECT Count(*) FROM LecturaDocumento LD " +
            "INNER JOIN DetalleDocumento DD ON DD.idDetalleDocumento = LD.idDetalleDocumento " +
            "INNER JOIN Documento D ON D.idDocumento = DD.idDocumento " +
            "INNER JOIN Producto P ON P.idProducto = DD.idProducto " +
            "INNER JOIN Ubicacion U ON LD.idUbicacion = U.idUbicacion " +
            "WHERE D.codTipoDocumento =:codTipoDocumento AND D.idClaseDocumento =:idClaseDocumento " +
            "AND D.numDocumento =:numDocumento AND LD.flgActivo =:flgActivo AND LD.flgEnviado =:flgEnviado " +
            "AND LD.codUsarioRegistro =:codUsuarioRegistro")
    Single<Integer> getLecturasCountBD(String codTipoDocumento, int idClaseDocumento, String numDocumento,
                                             String flgActivo, String flgEnviado, String codUsuarioRegistro);

    @Query("UPDATE LecturaDocumento SET flgActivo='0',codUsuarioModificacion=:codUsuario,fchModificacion=:fchModificacion," +
            "idTerminal=:idTerminal, fchRegistroTerminal=:fchRegistroTerminal WHERE idDetalleDocumento=:idDetalleDocumento " +
            "AND idLecturaDocumento=:idLecturaDocumento AND codUsarioRegistro=:codUsuario AND flgActivo='1' AND flgEnviado='0'")
    void deleteLecturaBD(String codUsuario, String fchModificacion, String idTerminal, String fchRegistroTerminal,
                         int idDetalleDocumento, int idLecturaDocumento);

    @Query("SELECT COUNT(*) FROM LecturaDocumento WHERE idDetalleDocumento=:idDetalleDocumento AND flgActivo='1'")
    Single<Integer> getLecturasByIdDetalle(int idDetalleDocumento);

    @Query("SELECT * FROM LecturaDocumento WHERE guia=:guia AND flgActivo='1'")
    Single<List<LecturaDocumento>> getLecturaDocumentoCerrar(String guia);

    @Query("UPDATE LecturaDocumento SET flgEnviado = '1',codUsuarioModificacion=:codUsuario,fchModificacion=:fchModificacion," +
            "fchEnviado=:fchModificacion WHERE idLecturaDocumento=:idLecturaDocumento")
    void cerrarLecturaDocumento(int idLecturaDocumento, String codUsuario, String fchModificacion);

    @Query("UPDATE LecturaDocumento SET flgEnviado = '1',codUsuarioModificacion=:codUsuario,fchModificacion=:fchModificacion," +
            "fchEnviado=:fchModificacion WHERE idDetalleDocumento=:idDetalleDocumento")
    void cerrarLecturaDocumentoByIdDetalle(int idDetalleDocumento, String codUsuario, String fchModificacion);

    @Query("SELECT idLecturaDocumento FROM LecturaDocumento WHERE idDetalleDocumento=:idDetalleDocumento AND flgActivo='1'")
    Single<List<Integer>> getIdLecturaDocumentoByIdDetalle(int idDetalleDocumento);

    @Query("SELECT LD.idLecturaDocumento,P.codProducto,P.dscProducto,DD.loteProducto,LD.serieProducto," +
            "DD.numDocInterno,DD.idDetalleDocumento FROM LecturaDocumento LD " +
            "INNER JOIN DetalleDocumento DD ON DD.idDetalleDocumento = LD.idDetalleDocumento " +
            "INNER JOIN Producto P ON P.idProducto = DD.idProducto " +
            "INNER JOIN Documento D ON D.idDocumento = DD.idDocumento " +
            "WHERE D.numDocumento =:numDocumento AND  DD.numDocInterno=:numDocInterno")
    Single<List<DetalleImpresion>> getLecturaImpresion(String numDocumento, String numDocInterno);

    @Query("SELECT COUNT(*) FROM LecturaDocumento")
    Single<Integer> countLectura();

    @Query("SELECT * FROM LecturaDocumento WHERE flgDescargado = '0' AND flgActivo = '1'")
    Single<List<LecturaDocumento>> getAllLecturaExportar();

    @Query("UPDATE LecturaDocumento SET flgDescargado='1',fchDescargado=:fchDescargado, " +
            "codUsuarioModificacion=:codUsuarioModificacion, fchModificacion=:fchModificacion " +
            "WHERE idLecturaDocumento=:idLecturaDocumento")
    void updateLecturaDescargada(String fchDescargado,String codUsuarioModificacion, String fchModificacion,
                                 int idLecturaDocumento);
}
