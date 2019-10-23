package com.dms.almacensoft.data.source.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbalmacen.Almacen;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface AlmacenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Almacen> entities);

    @Query("SELECT * FROM Almacen")
    Single<List<Almacen>> getListAlmacen();

    @Query("DELETE FROM Almacen")
    void deleteAll();

    @Query("SELECT A.idAlmacen,A.idEmpresa,E.codEmpresa,A.codAlmacen,A.tipoAlmacen,A.dscAlmacen FROM Almacen A " +
            "INNER JOIN UsuarioxAlmacen U ON A.idAlmacen = U.idAlmacen " +
            "INNER JOIN Empresa E ON A.idEmpresa = E.idEmpresa " +
            "WHERE U.idUsuario=:idUsuario")
    Single<List<Almacen>> getAllAlmacenByUsuarioBD(int idUsuario);
}
