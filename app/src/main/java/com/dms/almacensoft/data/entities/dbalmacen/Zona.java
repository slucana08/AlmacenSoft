package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idZona")
public class Zona {

    @SerializedName("ID_ZONA")
    private int idZona;
    @SerializedName("ID_ALMACEN")
    private int idAlmacen;
    @SerializedName("COD_ZONA")
    private String codZona;
    @SerializedName("DSC_ZONA")
    private String dscZona;

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getCodZona() {
        return codZona;
    }

    public void setCodZona(String codZona) {
        this.codZona = codZona;
    }

    public String getDscZona() {
        return dscZona;
    }

    public void setDscZona(String dscZona) {
        this.dscZona = dscZona;
    }
}
