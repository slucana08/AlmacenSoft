package com.dms.almacensoft.data.entities.dbtransact;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity (indices = {@Index(value = "idDetalleInventario" , unique = true)})
public class DetalleInventario {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("ID_DETALLE_INVENTARIO")
    private int idDetalleInventario;
    @SerializedName("ID_INVENTARIO")
    private int idInventario;
    @SerializedName("ID_UBICACION")
    private int idUbicacion;
    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("LOTE_PRODUCTO")
    private String loteProducto;
    @SerializedName("STOCK_INICIAL")
    private double stockInicial;
    @SerializedName("STOCK_FINAL")
    private double stockFinal;
    @SerializedName("FLG_ESNUEVO")
    private String flgEsnuevo;
    @SerializedName("COD_USUARIO_REGISTRO")
    private String codUsuarioRegistro;
    @SerializedName("FCH_REGISTRO")
    private String fchRegistro;
    @SerializedName("COD_USUARIO_MODIFICACION")
    private String codUsuarioModificacion;
    @SerializedName("FCH_MODIFICACION")
    private String fchModificacion;

    public int getIdDetalleInventario() {
        return idDetalleInventario;
    }

    public void setIdDetalleInventario(int idDetalleInventario) {
        this.idDetalleInventario = idDetalleInventario;
    }

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
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

    public double getStockInicial() {
        return stockInicial;
    }

    public void setStockInicial(double stockInicial) {
        this.stockInicial = stockInicial;
    }

    public double getStockFinal() {
        return stockFinal;
    }

    public void setStockFinal(double stockFinal) {
        this.stockFinal = stockFinal;
    }

    public String getFlgEsnuevo() {
        return flgEsnuevo;
    }

    public void setFlgEsnuevo(String flgEsnuevo) {
        this.flgEsnuevo = flgEsnuevo;
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
