package com.dms.almacensoft.data.source.local.daotransact;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbtransact.Documento;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface DocumentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Documento> documentos);

    @Query("SELECT * FROM Documento WHERE codTipoDocumento=:codTipoDocumento AND codEstado = 'P' AND " +
            "codClaseDocumento=:codClaseDocumento AND numDocumento LIKE :numDocumento AND idAlmacen=:idAlmacen")
    Single<List<Documento>> getListDocumento(String codTipoDocumento, String codClaseDocumento, String numDocumento, int idAlmacen);

    @Query("SELECT * FROM Documento WHERE codTipoDocumento=:codTipoDocumento AND codEstado = 'P' AND " +
            "codClaseDocumento=:codClaseDocumento AND idAlmacen=:idAlmacen")
    Single<List<Documento>> getListDocumento(String codTipoDocumento, String codClaseDocumento, int idAlmacen);

    @Query("DELETE FROM Documento")
    void deleteDocumento();

    @Query("UPDATE Documento SET codEstado = 'F',codUsuarioModificacion=:codUsuario,fchModificacion=:fchModificacion " +
            "WHERE idDocumento=:idDocumento AND codEstado = 'P'")
    void cerrarDocumento(String codUsuario, String fchModificacion, int idDocumento);

    @Query("SELECT COUNT(*) FROM Documento WHERE numDocumento=:numDocumento")
    Single<Integer> verifyDocumento(String numDocumento);

    @Insert()
    long insertDocumento(Documento documento);

    @Query("SELECT idDocumento FROM Documento WHERE numDocumento=:numDocumento")
    Single<Integer> getIdDocumento(String numDocumento);

    @Query("UPDATE Documento SET codEstado = 'E',dscObservacion = 'Cancelado por el usuario',codUsuarioModificacion=:codUsuario,fchModificacion=:fchModificacion " +
            "WHERE idDocumento=:idDocumento")
    void cancelarDocumento(String codUsuario, String fchModificacion, int idDocumento);

    @Query("SELECT * FROM Documento")
    Single<List<Documento>> getAllDocumentosExportar();

    @Query("UPDATE Documento SET codUsuarioModificacion=:codUsuarioModificacion, fchModificacion=:fchModificacion " +
            "WHERE idDocumento=:idDocumento")
    void updateDocumentoTrabajado(String codUsuarioModificacion, String fchModificacion, int idDocumento);

    @Query("SELECT COUNT(*) FROM Documento")
    Single<Integer> countDocumento();

    @Query("SELECT flgCreadoDoc FROM Documento WHERE idDocumento =:idDocumento")
    Single<String> getConSinDoc(int idDocumento);

}
