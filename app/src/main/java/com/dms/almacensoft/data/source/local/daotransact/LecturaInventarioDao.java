package com.dms.almacensoft.data.source.local.daotransact;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.dms.almacensoft.data.entities.dbtransact.LecturaInventario;
import com.dms.almacensoft.data.models.inventario.HijoConsultaInventario;
import com.dms.almacensoft.data.models.inventario.SerieResponseInventario;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Single;

@Dao
public interface LecturaInventarioDao {

    @Query("SELECT COUNT(*) FROM LecturaInventario")
    Single<Integer> countLecturasInventarioBD();

    @Query("SELECT COUNT(*) FROM LecturaInventario WHERE idInventario=:idInventario AND codUsuarioRegistro=:codUsuarioRegistro")
    Single<Integer> getLecturasCountBD(int idInventario, String codUsuarioRegistro);

    @Insert()
    long insertLecturaInventario(LecturaInventario lecturaInventario);

    @Query("SELECT LI.idProducto,P.codProducto, P.dscProducto, LI.serieProducto, LI.idLecturaInventario " +
            "FROM lecturainventario LI INNER JOIN Producto P ON LI.idProducto = P.idProducto " +
            "WHERE idInventario=:idInventario AND LI.idProducto=:idProducto AND LI.serieProducto=:serieProducto")
    Single<List<SerieResponseInventario>> obtenerSerieInventarioBD(int idInventario, int idProducto, String serieProducto);

    @Query("SELECT LI.idLecturaInventario,LI.idInventario,LI.idDetalleInventario,LI.idUbicacion,U.codUbicacion," +
            "LI.idProducto,P.codProducto,P.dscProducto,LI.loteProducto,LI.serieProducto,LI.stockInventariado " +
            "FROM LecturaInventario LI " +
            "INNER JOIN Ubicacion U ON U.idUbicacion = LI.idUbicacion " +
            "INNER JOIN Producto P ON P.idProducto = LI.idProducto " +
            "WHERE LI.idInventario=:idInventario AND LI.idUbicacion=:idUbicacion " +
            "AND LI.codUsuarioRegistro=:codUsuarioRegistro AND LI.nroCaja = 0")
    Single<List<HijoConsultaInventario>> getLecturaInventarioByUbicacion(int idInventario, int idUbicacion, String codUsuarioRegistro);

    @Query("SELECT LI.idLecturaInventario,LI.idInventario,LI.idDetalleInventario,LI.idUbicacion,U.codUbicacion," +
            "LI.idProducto,P.codProducto,P.dscProducto,LI.loteProducto,LI.serieProducto,LI.stockInventariado " +
            "FROM LecturaInventario LI " +
            "INNER JOIN Ubicacion U ON U.idUbicacion = LI.idUbicacion " +
            "INNER JOIN Producto P ON P.idProducto = LI.idProducto " +
            "WHERE LI.idInventario=:idInventario AND LI.idProducto=:idProducto " +
            "AND LI.codUsuarioRegistro=:codUsuarioRegistro AND LI.nroCaja = 0")
    Single<List<HijoConsultaInventario>> getLecturaInventarioByProducto(int idInventario, int idProducto, String codUsuarioRegistro);

    @Query("SELECT LI.idLecturaInventario,LI.idInventario,LI.idDetalleInventario,LI.idUbicacion,U.codUbicacion," +
            "LI.idProducto,P.codProducto,P.dscProducto,LI.loteProducto,LI.serieProducto,LI.stockInventariado " +
            "FROM LecturaInventario LI " +
            "INNER JOIN Ubicacion U ON U.idUbicacion = LI.idUbicacion " +
            "INNER JOIN Producto P ON P.idProducto = LI.idProducto " +
            "WHERE LI.idInventario=:idInventario AND LI.loteProducto=:loteProducto " +
            "AND LI.codUsuarioRegistro=:codUsuarioRegistro AND LI.nroCaja = 0")
    Single<List<HijoConsultaInventario>> getLecturaInventarioByLote(int idInventario, String loteProducto, String codUsuarioRegistro);

    @Query("SELECT LI.idLecturaInventario,LI.idInventario,LI.idDetalleInventario,LI.idUbicacion,U.codUbicacion," +
            "LI.idProducto,P.codProducto,P.dscProducto,LI.loteProducto,LI.serieProducto,LI.stockInventariado " +
            "FROM LecturaInventario LI " +
            "INNER JOIN Ubicacion U ON U.idUbicacion = LI.idUbicacion " +
            "INNER JOIN Producto P ON P.idProducto = LI.idProducto " +
            "WHERE LI.idInventario=:idInventario AND LI.serieProducto=:serieProducto " +
            "AND LI.codUsuarioRegistro=:codUsuarioRegistro AND LI.nroCaja = 0")
    Single<List<HijoConsultaInventario>> getLecturaInventarioBySerie(int idInventario, String serieProducto, String codUsuarioRegistro);

    @Query("SELECT * FROM LecturaInventario WHERE idLecturaInventario=:idLecturaInventario AND " +
            "codUsuarioRegistro=:codUsuarioRegistro")
    Single<LecturaInventario> getLecturaEliminar(int idLecturaInventario, String codUsuarioRegistro);

    @Query("DELETE FROM LecturaInventario WHERE idLecturaInventario=:idLecturaInventario AND " +
            "codUsuarioRegistro=:codUsuarioRegistro")
    void eliminarLectura(int idLecturaInventario, String codUsuarioRegistro);

    @Query("SELECT * FROM Lecturainventario WHERE idDetalleInventario=:idDetalleInventario AND " +
            "codUsuarioRegistro=:codUsuarioRegistro")
    Single<List<LecturaInventario>> getLecturaEliminarByDetalle(int idDetalleInventario, String codUsuarioRegistro);

    @Query("DELETE FROM LecturaInventario")
    void eliminarLecturaInventarioAdmin();

    @Query("SELECT COUNT(*) FROM LecturaInventario WHERE flgDescargado = '0' AND flgActivo = '1'")
    Single<Integer> getCountLecturaInventarioExportar();

    @Query("UPDATE LecturaInventario SET flgDescargado='1',fchDescargado=:fchDescargado, " +
            "codUsuarioModificacion=:codUsuarioModificacion, fchModificacion=:fchModificacion " +
            "WHERE idLecturaInventario=:idLecturaInventario")
    void updateLecturaInventarioDescargada(String fchDescargado,String codUsuarioModificacion, String fchModificacion,
                                 int idLecturaInventario);

    @Query("SELECT * FROM LecturaInventario WHERE flgDescargado = '0' AND flgActivo = '1' LIMIT :limit")
    Single<List<LecturaInventario>> getLecturaInventarioExportar(int limit);
}
