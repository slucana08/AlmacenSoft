package com.dms.almacensoft.data.source.local.daotransact;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbtransact.DetalleInventario;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface DetalleInventarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long [] insertAll(List<DetalleInventario> entities);

    @Query("DELETE FROM DetalleInventario")
    void eliminarDetalleInventarioAdmin();

    @Query("SELECT COUNT(*) FROM DetalleInventario WHERE idInventario=:idInventario AND idUbicacion=:idUbicacion AND" +
            " idProducto=:idProducto AND loteProducto=:loteProducto")
    Single<Integer> verifyDetalleInventarioBD(int idInventario, int idUbicacion, int idProducto, String loteProducto);

    @Query("UPDATE DetalleInventario SET stockFinal = stockFinal + :stockInventariado, " +
            "codUsuarioModificacion=:codUsuarioModificacion, fchModificacion=:fchModificacion " +
            "WHERE idInventario=:idInventario AND idUbicacion=:idUbicacion AND idProducto=:idProducto AND " +
            "loteProducto=:loteProducto")
    void updateDetalleInventarioBD(double stockInventariado, String codUsuarioModificacion, String fchModificacion,
                                   int idInventario,int idUbicacion,int idProducto,String loteProducto);

    @Insert()
    long insertDetalleInventario(DetalleInventario detalleInventario);

    @Query("SELECT idDetalleInventario FROM DetalleInventario WHERE idInventario=:idInventario AND idUbicacion=:idUbicacion AND" +
            " idProducto=:idProducto AND loteProducto=:loteProducto")
    Single<Integer> getIdDetalleInventarioBD(int idInventario, int idUbicacion, int idProducto, String loteProducto);

    @Query("UPDATE DetalleInventario SET stockFinal = stockFinal - :cantidadEliminar, codUsuarioModificacion=:codUsuarioModificacion," +
            "fchModificacion=:fchModificacion WHERE idDetalleInventario =:idDetalleInventario")
    void updateDetalleLecturaEliminar(double cantidadEliminar, int idDetalleInventario,
                                      String codUsuarioModificacion, String fchModificacion);

    @Query("UPDATE Detalleinventario SET stockFinal = 0.0, codUsuarioModificacion=:codUsuarioModificacion, fchModificacion=:fchModificacion")
    void eliminarDetallesAdmin(String codUsuarioModificacion, String fchModificacion);

    @Query("SELECT COUNT(*) FROM DetalleInventario")
    Single<Integer> countDetalleInventario();
}
