package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idStock")
public class StockAlmacen {

    @SerializedName("ID_STOCK")
    private int idStock;
    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("LOTE_PRODUCTO")
    private String loteProducto;
    @SerializedName("STOCK")
    private double stock;
    @SerializedName("COD_USUARIO_REGISTRO")
    private String codUsuarioRegistro;
    @SerializedName("FCH_REGISTRO")
    private String fchRegistro;
    @SerializedName("COD_USUARIO_MODIFICACION")
    private String codUsuarioModificacion;
    @SerializedName("FCH_MODIFICACION")
    private String fchModificacion;

    public int getIdStock() {
        return idStock;
    }

    public void setIdStock(int idStock) {
        this.idStock = idStock;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getLoteProducto() {
        return loteProducto;
    }

    public void setLoteProducto(String loteProducto) {
        this.loteProducto = loteProducto;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public String getCodUsuarioRegistro() {
        return codUsuarioRegistro;
    }

    public void setCodUsuarioRegistro(String codUsuarioRegistro) {
        this.codUsuarioRegistro = codUsuarioRegistro;
    }

    public String getFchRegistro() {
        return fchRegistro;
    }

    public void setFchRegistro(String fchRegistro) {
        this.fchRegistro = fchRegistro;
    }

    public String getCodUsuarioModificacion() {
        return codUsuarioModificacion;
    }

    public void setCodUsuarioModificacion(String codUsuarioModificacion) {
        this.codUsuarioModificacion = codUsuarioModificacion;
    }

    public String getFchModificacion() {
        return fchModificacion;
    }

    public void setFchModificacion(String fchModificacion) {
        this.fchModificacion = fchModificacion;
    }
}
