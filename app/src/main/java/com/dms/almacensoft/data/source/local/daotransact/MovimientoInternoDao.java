package com.dms.almacensoft.data.source.local.daotransact;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbtransact.MovimientoInterno;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface MovimientoInternoDao {

    @Insert
    long insertMovimientoInterno(MovimientoInterno movimientoInterno);

    @Query("UPDATE MovimientoInterno SET flgActivo='0',codUsuarioModificacion =:codUsuario," +
            "fchModificacion=:fchModificacion WHERE idLecturaDocumento=:idLecturaDocumento AND flgActivo='1'")
    void updateMovimientoEliminacion(String codUsuario,String fchModificacion, int idLecturaDocumento);

    @Query("DELETE FROM MovimientoInterno")
    void eliminarMovimientoAdmin();

    @Query("UPDATE MovimientoInterno SET flgCerrarEnvio = '1',codUsuarioModificacion=:codUsuario,fchModificacion=:fchModificacion " +
            "WHERE idLecturaDocumento=:idLecturaDocumento AND flgActivo = '1'")
    void cerrarMovimiento(int idLecturaDocumento, String codUsuario, String fchModificacion);

    @Query("SELECT COUNT(*) FROM MovimientoInterno")
    Single<Integer> countMovimiento();

    @Query("SELECT * FROM MovimientoInterno")
    Single<List<MovimientoInterno>> getAllMovimientoExportar();
}
