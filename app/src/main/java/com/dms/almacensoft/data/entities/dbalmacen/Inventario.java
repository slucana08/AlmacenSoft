package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idInventario")
public class Inventario {

    @SerializedName("ID_INVENTARIO")
    private int idInventario;
    @SerializedName("ID_ALMACEN")
    private int idAlmacen;
    @SerializedName("COD_INVENTARIO")
    private String codInventario;
    @SerializedName("FCH_INICIO")
    private String fchInicio;
    @SerializedName("FCH_FIN")
    private String fchFin;
    @SerializedName("COD_ESTADO")
    private String codEstado;
    @SerializedName("DSC_OBSERVACION")
    private String dscObservacion;
    @SerializedName("FLG_ACTIVO")
    private String flgActivo;
    @SerializedName("COD_USUARIO_REGISTRO")
    private String codUsuarioRegistro;
    @SerializedName("FCH_REGISTRO")
    private String fchRegistro;
    @SerializedName("COD_USUARIO_MODIFICACION")
    private String codUsuarioModificacion;
    @SerializedName("FCH_MODIFICACION")
    private String fchModificacion;
    @SerializedName("NRO_CONTEO")
    private int nroConteo;
    @SerializedName("NRO_ERROR_KIT")
    private int nroErrorKit;
    @SerializedName("DIFERENCIADO")
    private int diferenciado;

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getCodInventario() {
        return codInventario;
    }

    public void setCodInventario(String codInventario) {
        this.codInventario = codInventario;
    }

    public String getFchInicio() {
        return fchInicio;
    }

    public void setFchInicio(String fchInicio) {
        this.fchInicio = fchInicio;
    }

    public String getFchFin() {
        return fchFin;
    }

    public void setFchFin(String fchFin) {
        this.fchFin = fchFin;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(String flgActivo) {
        this.flgActivo = flgActivo;
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

    public int getNroConteo() {
        return nroConteo;
    }

    public void setNroConteo(int nroConteo) {
        this.nroConteo = nroConteo;
    }

    public int getNroErrorKit() {
        return nroErrorKit;
    }

    public void setNroErrorKit(int nroErrorKit) {
        this.nroErrorKit = nroErrorKit;
    }

    public String getDscObservacion() {
        return dscObservacion;
    }

    public void setDscObservacion(String dscObservacion) {
        this.dscObservacion = dscObservacion;
    }

    public int getDiferenciado() {
        return diferenciado;
    }

    public void setDiferenciado(int diferenciado) {
        this.diferenciado = diferenciado;
    }
}
