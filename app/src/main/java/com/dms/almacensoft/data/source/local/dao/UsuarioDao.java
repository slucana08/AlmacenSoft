package com.dms.almacensoft.data.source.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbalmacen.Usuario;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Usuario> entities);

    @Query("DELETE FROM Usuario")
    void deteleAll();

    @Query("Select * FROM Usuario WHERE codUsuario=:codUsuario")
    Single<List<Usuario>> getUsuario(String codUsuario);

}
