package com.dms.almacensoft.data.source.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbalmacen.Almacen;
import com.dms.almacensoft.data.entities.dbalmacen.Inventario;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface InventarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Inventario> entities);

    @Query("SELECT * FROM Inventario")
    Single<List<Inventario>> getAllInventarioDB();

    @Query("DELETE FROM Inventario")
    void deleteInventario();

    @Query("SELECT * FROM Inventario WHERE idAlmacen=:idAlmacen")
    Single<List<Inventario>> getAllInventarioByAlmacen(int idAlmacen);

    @Query("SELECT * FROM Inventario")
    Single<List<Inventario>> getAllInventarioExportar();

}
