package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idUbicacion", indices = {@Index(value = "codUbicacion", unique = true)})
public class Ubicacion {

    @SerializedName("ID_UBICACION")
    private int idUbicacion;
    @SerializedName("ID_ALMACEN")
    private int idAlmacen;
    @SerializedName("COD_UBICACION")
    private String codUbicacion;
    @SerializedName("DSC_UBICACION")
    private String dscUbicacion;
    @SerializedName("DSC_ESTADO")
    private String dscEstado;
    @SerializedName("ESTADO")
    private String estado;
    @SerializedName("COD_ISLA")
    private String codIsla;
    @SerializedName("COD_ZONA")
    private String codZona;
    @SerializedName("ANCHO")
    private int ancho;
    @SerializedName("ALTO")
    private int alto;
    @SerializedName("PROFUNDIDAD")
    private int profundidad;

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
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

    public String getDscUbicacion() {
        return dscUbicacion;
    }

    public void setDscUbicacion(String dscUbicacion) {
        this.dscUbicacion = dscUbicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodIsla() {
        return codIsla;
    }

    public void setCodIsla(String codIsla) {
        this.codIsla = codIsla;
    }

    public String getCodZona() {
        return codZona;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(int profundidad) {
        this.profundidad = profundidad;
    }

    public String getDscEstado() {
        return dscEstado;
    }

    public void setDscEstado(String dscEstado) {
        this.dscEstado = dscEstado;
    }
}
