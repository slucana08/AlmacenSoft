package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idUnidadMedida")
public class UnidadMedida {

    @SerializedName("ID_UNIDAD_MEDIDA")
    private int idUnidadMedida;
    @SerializedName("COD_UNIDAD_MEDIDA")
    private String codUnidadMedida;
    @SerializedName("DSC_UNIDAD_MEDIDA")
    private String dscUnidadMedida;

    public int getIdUnidadMedida() {
        return idUnidadMedida;
    }

    public void setIdUnidadMedida(int idUnidadMedida) {
        this.idUnidadMedida = idUnidadMedida;
    }

    public String getCodUnidadMedida() {
        return codUnidadMedida;
    }

    public void setCodUnidadMedida(String codUnidadMedida) {
        this.codUnidadMedida = codUnidadMedida;
    }

    public String getDscUnidadMedida() {
        return dscUnidadMedida;
    }

    public void setDscUnidadMedida(String dscUnidadMedida) {
        this.dscUnidadMedida = dscUnidadMedida;
    }
}
