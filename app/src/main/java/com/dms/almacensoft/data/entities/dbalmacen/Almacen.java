package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idAlmacen", indices = {@Index(value = "idAlmacen", unique = true)})
public class Almacen {

    @SerializedName("ID_ALMACEN")
    private int idAlmacen;
    @SerializedName("ID_EMPRESA")
    private int idEmpresa;
    @SerializedName("COD_EMPRESA")
    private String codEmpresa;
    @SerializedName("COD_ALMACEN")
    private String codAlmacen;
    @SerializedName("DSC_ALMACEN")
    private String dscAlmacen;
    @SerializedName("TIPO_ALMACEN")
    private String tipoAlmacen;

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public String getDscAlmacen() {
        return dscAlmacen;
    }

    public void setDscAlmacen(String dscAlmacen) {
        this.dscAlmacen = dscAlmacen;
    }

    public String getTipoAlmacen() {
        return tipoAlmacen;
    }

    public void setTipoAlmacen(String tipoAlmacen) {
        this.tipoAlmacen = tipoAlmacen;
    }

    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

}
