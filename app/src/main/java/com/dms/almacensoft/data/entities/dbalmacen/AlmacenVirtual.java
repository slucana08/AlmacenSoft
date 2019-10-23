package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idAlmvirtual")
public class AlmacenVirtual {

    @SerializedName("ID_ALMVIRTUAL")
    private int idAlmvirtual;
    @SerializedName("COD_ALMVIRTUAL")
    private String codAlmvirtual;
    @SerializedName("DSC_ALMVIRTUAL")
    private String dscAlmvirtual;

    public int getIdAlmvirtual() {
        return idAlmvirtual;
    }

    public void setIdAlmvirtual(int idAlmvirtual) {
        this.idAlmvirtual = idAlmvirtual;
    }

    public String getCodAlmvirtual() {
        return codAlmvirtual;
    }

    public void setCodAlmvirtual(String codAlmvirtual) {
        this.codAlmvirtual = codAlmvirtual;
    }

    public String getDscAlmvirtual() {
        return dscAlmvirtual;
    }

    public void setDscAlmvirtual(String dscAlmvirtual) {
        this.dscAlmvirtual = dscAlmvirtual;
    }
}
