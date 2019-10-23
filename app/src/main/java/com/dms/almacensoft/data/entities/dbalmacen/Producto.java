package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idProducto", indices = {@Index(value = "codProducto", unique = true)})
public class Producto {

    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("COD_PRODUCTO")
    private String codProducto;
    @SerializedName("DSC_PRODUCTO")
    private String dscProducto;
    @SerializedName("LOTE_PRODUCTO")
    private String loteProducto;
    @SerializedName("FLG_REGISTRA_SERIE")
    private String flgRegistraSerie;
    @SerializedName("FLG_ESNUEVO")
    private String flgEsNuevo;
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
    @SerializedName("FLG_DESCARGADO")
    private String flgDescargado;
    @SerializedName("FCH_DESCARGADO")
    private String fchDescargado;

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
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

    public String getLoteProducto() {
        return loteProducto;
    }

    public void setLoteProducto(String loteProducto) {
        this.loteProducto = loteProducto;
    }

    public String getFlgRegistraSerie() {
        return flgRegistraSerie;
    }

    public void setFlgRegistraSerie(String flgRegistraSerie) {
        this.flgRegistraSerie = flgRegistraSerie;
    }

    public String getFlgEsNuevo() {
        return flgEsNuevo;
    }

    public void setFlgEsNuevo(String flgEsNuevo) {
        this.flgEsNuevo = flgEsNuevo;
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
}
