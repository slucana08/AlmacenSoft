package com.dms.almacensoft.data.source.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbalmacen.VerificacionDocumento;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface VerificacionDocumentoDao {

    @Query("SELECT * FROM VerificacionDocumento")
    Single<List<VerificacionDocumento>> getAllVerificacionExportar();

}
