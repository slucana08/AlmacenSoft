package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idNivel")
public class JerarquiaClasificacion {

    @SerializedName("IDNIVEL")
    private int idNivel;
    @SerializedName("ID_EMPRESA")
    private int idEmpresa;
    @SerializedName("DSC_NIVEL")
    private String dscNivel;

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getDscNivel() {
        return dscNivel;
    }

    public void setDscNivel(String dscNivel) {
        this.dscNivel = dscNivel;
    }
}
