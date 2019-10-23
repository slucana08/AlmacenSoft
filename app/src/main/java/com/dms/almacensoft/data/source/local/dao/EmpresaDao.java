package com.dms.almacensoft.data.source.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbalmacen.Empresa;

import java.util.List;

@Dao
public interface EmpresaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Empresa> entities);

    @Query("DELETE FROM EMPRESA")
    void deleteAll();

}
