package com.dms.almacensoft.data.source.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbalmacen.Producto;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ProductoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Producto> entities);

    @Query("DELETE FROM Producto")
    void deleteAll();

    @Query("SELECT * FROM Producto WHERE codProducto=:codProducto")
    Single<List<Producto>> verifyProductoDB(String codProducto);

    @Query("SELECT * FROM Producto WHERE idProducto=:idProducto")
    Single<List<Producto>> getProductoByID(int idProducto);

    @Query("SELECT COUNT(*) FROM Producto")
    Single<Integer> countProductos();
}
