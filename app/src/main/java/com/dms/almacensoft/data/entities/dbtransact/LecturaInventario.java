package com.dms.almacensoft.data.entities.dbtransact;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity (indices = {@Index(value = "idLecturaInventario" , unique = true)})
public class LecturaInventario {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("ID_LECTURA_INVENTARIO")
    private int idLecturaInventario;
    @SerializedName("ID_INVENTARIO")
    private int idInventario;
    @SerializedName("ID_DETALLE_INVENTARIO")
    private int idDetalleInventario;
    @SerializedName("ID_EMPRESA")
    private int idEmpresa;
    @SerializedName("ID_ALMACEN")
    private int idAlmacen;
    @SerializedName("ID_UBICACION")
    private int idUbicacion;
    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("COD_PRODUCTO")
    private String codProducto;
    @SerializedName("DSC_PRODUCTO")
    private String dscProducto;
    @SerializedName("LOTE_PRODUCTO")
    private String loteProducto;
    @SerializedName("SERIE_PRODUCTO")
    private String serieProducto;
    @SerializedName("STOCK_INVENTARIADO")
    private double stockInventariado;
    @SerializedName("COD_USUARIO_REGISTRO")
    private String codUsuarioRegistro;
    @SerializedName("FCH_REGISTRO")
    private String fchRegistro;
    @SerializedName("COD_USUARIO_MODIFICACION")
    private String codUsuarioModificacion;
    @SerializedName("FCH_MODIFICACION")
    private String fchModificacion;
    @SerializedName("ID_TERMINAL")
    private String idTerminal;
    @SerializedName("FLG_ACTIVO")
    private String flgActivo;
    @SerializedName("FLG_DESCARGADO")
    private String flgDescargado;
    @SerializedName("FCH_DESCARGADO")
    private String fchDescargado;
    @SerializedName("COD_PROD_CAJA")
    private String codProdCaja;
    @SerializedName("NRO_CAJA")
    private int nroCaja;

    public int getIdLecturaInventario() {
        return idLecturaInventario;
    }

    public void setIdLecturaInventario(int idLecturaInventario) {
        this.idLecturaInventario = idLecturaInventario;
    }

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public int getIdDetalleInventario() {
        return idDetalleInventario;
    }

    public void setIdDetalleInventario(int idDetalleInventario) {
        this.idDetalleInventario = idDetalleInventario;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
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

    public String getSerieProducto() {
        return serieProducto;
    }

    public void setSerieProducto(String serieProducto) {
        this.serieProducto = serieProducto;
    }

    public double getStockInventariado() {
        return stockInventariado;
    }

    public void setStockInventariado(double stockInventariado) {
        this.stockInventariado = stockInventariado;
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

    public String getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(String idTerminal) {
        this.idTerminal = idTerminal;
    }

    public String getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(String flgActivo) {
        this.flgActivo = flgActivo;
    }

    public String getFlgDescargado() {
        return flgDescargado;
    }

    public void setFlgDescargado(String flgDescargado) {
        this.flgDescargado = flgDescargado;
    }

    public String getFchDescargado() {
        return fchDescargado;
    }

    public void setFchDescargado(String fchDescargado) {
        this.fchDescargado = fchDescargado;
    }

    public String getCodProdCaja() {
        return codProdCaja;
    }

    public void setCodProdCaja(String codProdCaja) {
        this.codProdCaja = codProdCaja;
    }

    public int getNroCaja() {
        return nroCaja;
    }

    public void setNroCaja(int nroCaja) {
        this.nroCaja = nroCaja;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public String getDscProducto() {
        return dscProducto;
    }

    public void setDscProducto(String dscProducto) {
        this.dscProducto = dscProducto;
    }
}

