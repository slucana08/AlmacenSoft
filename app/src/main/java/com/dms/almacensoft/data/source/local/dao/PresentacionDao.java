package com.dms.almacensoft.data.source.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbalmacen.Presentacion;

import java.util.List;

@Dao
public interface PresentacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Presentacion> entities);

    @Query("DELETE FROM Presentacion")
    void deleteAll();

}
