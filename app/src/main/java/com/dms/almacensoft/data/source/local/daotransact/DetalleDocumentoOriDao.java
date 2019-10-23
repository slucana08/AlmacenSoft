package com.dms.almacensoft.data.source.local.daotransact;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumentoOri;
import com.dms.almacensoft.data.models.recepciondespacho.DetalleDocumentoLive;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface DetalleDocumentoOriDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<DetalleDocumentoOri> entities);

    @Query("DELETE FROM DetalleDocumentoOri")
    void deleteDetalleDocumentoOri();

    @Query("SELECT * FROM DetalleDocumentoOri WHERE idDocumento=:idDocumento")
    Single<List<DetalleDocumentoLive>> getDetalleListBD(int idDocumento);

    @Query("SELECT * FROM DetalleDocumentoOri WHERE idDocumento=:idDocumento ORDER BY linea ASC")
    Single<List<DetalleDocumentoOri>> getDetalleDocumentoOri(int idDocumento);

    @Query("SELECT COUNT(*) FROM DetalleDocumentoOri")
    Single<Integer> countDetalleOri();

    @Query("SELECT * FROM DetalleDocumentoOri")
    Single<List<DetalleDocumentoOri>> getAllDetalleDocumentoOriExportar();

}
