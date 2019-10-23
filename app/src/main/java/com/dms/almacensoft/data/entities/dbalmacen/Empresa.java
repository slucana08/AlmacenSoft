package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "idEmpresa")
public class Empresa {

    @SerializedName("ID_EMPRESA")
    private int idEmpresa;
    @SerializedName("COD_EMPRESA")
    private String codEmpresa;
    @SerializedName("DSC_EMPRESA")
    private String dscEmpresa;
    @SerializedName("RUC_EMPRESA")
    private String rucEmpresa;
    @SerializedName("DIR_EMPRESA")
    private String dirEmpresa;
    @SerializedName("TLF_EMPRESA")
    private String tlfEmpresa;
    @SerializedName("FAX_EMPRESA")
    private String faxEmpresa;
    @SerializedName("RAZONSOCIAL_EMPRESA")
    private String razonSocialEmpresa;
    @SerializedName("FLG_ACTIVO")
    private String flgActivo;

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public String getDscEmpresa() {
        return dscEmpresa;
    }

    public void setDscEmpresa(String dscEmpresa) {
        this.dscEmpresa = dscEmpresa;
    }

    public String getRucEmpresa() {
        return rucEmpresa;
    }

    public void setRucEmpresa(String rucEmpresa) {
        this.rucEmpresa = rucEmpresa;
    }

    public String getDirEmpresa() {
        return dirEmpresa;
    }

    public void setDirEmpresa(String dirEmpresa) {
        this.dirEmpresa = dirEmpresa;
    }

    public String getTlfEmpresa() {
        return tlfEmpresa;
    }

    public void setTlfEmpresa(String tlfEmpresa) {
        this.tlfEmpresa = tlfEmpresa;
    }

    public String getFaxEmpresa() {
        return faxEmpresa;
    }

    public void setFaxEmpresa(String faxEmpresa) {
        this.faxEmpresa = faxEmpresa;
    }

    public String getRazonSocialEmpresa() {
        return razonSocialEmpresa;
    }

    public void setRazonSocialEmpresa(String razonSocialEmpresa) {
        this.razonSocialEmpresa = razonSocialEmpresa;
    }

    public String getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(String flgActivo) {
        this.flgActivo = flgActivo;
    }
}
