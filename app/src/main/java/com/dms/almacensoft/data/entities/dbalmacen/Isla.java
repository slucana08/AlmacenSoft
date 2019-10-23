package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idIsla")
public class Isla {

    @SerializedName("ID_ISLA")
    private int idIsla;
    @SerializedName("COD_EMPRESA")
    private String codEmpresa;
    @SerializedName("ID_ALMACEN")
    private int idAlmacen;
    @SerializedName("ID_ZONA")
    private int idZona;
    @SerializedName("COD_ISLA")
    private String codIsla;
    @SerializedName("DSC_ISLA")
    private String dscIsla;
    @SerializedName("CANT_PROFUNDIDAD")
    private int cantProfundidad;
    @SerializedName("CANT_ALTURA")
    private int cantAltura;
    @SerializedName("CANT_ANCHO")
    private int cantAncho;
    @SerializedName("FLG_ACTIVO")
    private int flgActivo;
    @SerializedName("FCH_REGISTRO_IMPORTACION")
    private String fchRegistroImportacion;
    @SerializedName("FCH_ULTIMA_IMPORTACION")
    private String fchUltimaImportacion;
    @SerializedName("POS_X")
    private double posX;
    @SerializedName("POS_Y")
    private double posY;
    @SerializedName("FLG_VERTICAL")
    private int flgVertical;

    public int getIdIsla() {
        return idIsla;
    }

    public void setIdIsla(int idIsla) {
        this.idIsla = idIsla;
    }

    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public String getCodIsla() {
        return codIsla;
    }

    public void setCodIsla(String codIsla) {
        this.codIsla = codIsla;
    }

    public String getDscIsla() {
        return dscIsla;
    }

    public void setDscIsla(String dscIsla) {
        this.dscIsla = dscIsla;
    }

    public int getCantProfundidad() {
        return cantProfundidad;
    }

    public void setCantProfundidad(int cantProfundidad) {
        this.cantProfundidad = cantProfundidad;
    }

    public int getCantAltura() {
        return cantAltura;
    }

    public void setCantAltura(int cantAltura) {
        this.cantAltura = cantAltura;
    }

    public int getCantAncho() {
        return cantAncho;
    }

    public void setCantAncho(int cantAncho) {
        this.cantAncho = cantAncho;
    }

    public int getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(int flgActivo) {
        this.flgActivo = flgActivo;
    }

    public String getFchRegistroImportacion() {
        return fchRegistroImportacion;
    }

    public void setFchRegistroImportacion(String fchRegistroImportacion) {
        this.fchRegistroImportacion = fchRegistroImportacion;
    }

    public String getFchUltimaImportacion() {
        return fchUltimaImportacion;
    }

    public void setFchUltimaImportacion(String fchUltimaImportacion) {
        this.fchUltimaImportacion = fchUltimaImportacion;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public int getFlgVertical() {
        return flgVertical;
    }

    public void setFlgVertical(int flgVertical) {
        this.flgVertical = flgVertical;
    }
}
