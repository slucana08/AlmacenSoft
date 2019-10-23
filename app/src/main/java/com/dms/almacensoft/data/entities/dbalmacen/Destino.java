package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idDestino")
public class Destino {

    @SerializedName("ID_DESTINO")
    private int idDestino;
    @SerializedName("ID_EMPRESA")
    private int idEmpresa;
    @SerializedName("COD_DESTINO")
    private String codDestino;
    @SerializedName("DSC_DESTINO")
    private String dscDestino;
    @SerializedName("FLG_ACTIVO")
    private String flgActivo;

    public int getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(int idDestino) {
        this.idDestino = idDestino;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getCodDestino() {
        return codDestino;
    }

    public void setCodDestino(String codDestino) {
        this.codDestino = codDestino;
    }

    public String getDscDestino() {
        return dscDestino;
    }

    public void setDscDestino(String dscDestino) {
        this.dscDestino = dscDestino;
    }

    public String getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(String flgActivo) {
        this.flgActivo = flgActivo;
    }
}
