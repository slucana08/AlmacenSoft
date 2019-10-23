package com.dms.almacensoft.data.source.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbalmacen.Ubicacion;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface UbicacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Ubicacion> entities);

    @Query("DELETE FROM Ubicacion")
    void deleteAll();

    @Query("SELECT * FROM Ubicacion WHERE idAlmacen=:idAlmacen AND codUbicacion=:codUbicacion")
    Single<List<Ubicacion>> verifyUbicacionBD(int idAlmacen, String codUbicacion);
}
