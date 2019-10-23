package com.dms.almacensoft.data.source.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbalmacen.ProductoUbicacion;

import java.util.List;

@Dao
public interface ProductoUbicacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<ProductoUbicacion> entities);

    @Query("DELETE FROM ProductoUbicacion")
    void deleteAll();

}
