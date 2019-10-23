package com.dms.almacensoft.data.source.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbalmacen.ClaseDocumento;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ClaseDocumentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<ClaseDocumento> entities);

    @Query("DELETE FROM ClaseDocumento")
    void deleteAll();

    @Query("SELECT * FROM ClaseDocumento WHERE codTipoDocumento=:codTipoDocumento AND flgConSinDoc=:flgConSinDoc")
    Single<List<ClaseDocumento>> getListClaseDocumentoBatch(String codTipoDocumento, String flgConSinDoc);

    @Query("SELECT COUNT(*) FROM ClaseDocumento")
    Single<Integer> claseDocumentoCount();
}
