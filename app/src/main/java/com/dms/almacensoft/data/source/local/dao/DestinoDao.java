package com.dms.almacensoft.data.source.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbalmacen.Destino;

import java.util.List;

@Dao
public interface DestinoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Destino> entities);

    @Query("DELETE FROM Destino")
    void deleteAll();
}
