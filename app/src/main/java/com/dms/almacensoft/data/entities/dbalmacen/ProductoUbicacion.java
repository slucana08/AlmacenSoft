package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idProductoUbicacion")
public class ProductoUbicacion {

    @SerializedName("ID_PRODUCTOUBICACION")
    private int idProductoUbicacion;
    @SerializedName("ID_ALMACEN")
    private int idAlmacen;
    @SerializedName("COD_UBICACION")
    private String codUbicacion;
    @SerializedName("COD_PRODUCTO")
    private String codProducto;
    @SerializedName("CANTIDAD")
    private double cantidad;
    @SerializedName("LOTE_PRODUCTO")
    private String loteProducto;
    @SerializedName("FCH_PRODUCCION")
    private String fchProduccion;

    public int getIdProductoUbicacion() {
        return idProductoUbicacion;
    }

    public void setIdProductoUbicacion(int idProductoUbicacion) {
        this.idProductoUbicacion = idProductoUbicacion;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getCodUbicacion() {
        return codUbicacion;
    }

    public void setCodUbicacion(String codUbicacion) {
        this.codUbicacion = codUbicacion;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getLoteProducto() {
        return loteProducto;
    }

    public void setLoteProducto(String loteProducto) {
        this.loteProducto = loteProducto;
    }

    public String getFchProduccion() {
        return fchProduccion;
    }

    public void setFchProduccion(String fchProduccion) {
        this.fchProduccion = fchProduccion;
    }
}
