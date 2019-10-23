package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idClasificacion", indices = {@Index(value = "idClasificacion", unique = true)})
public class Clasificacion {

    @SerializedName("IDCLASIFICACION")
    private int idClasificacion;
    @SerializedName("ID_EMPRESA")
    private int idEmpresa;
    @SerializedName("COD_CLASIFICACION")
    private String codClasificacion;
    @SerializedName("DSC_CLASIFICACION")
    private String dscClasificacion;
    @SerializedName("COD_JERARQUIA_PADRE")
    private String codJerarquiaPadre;
    @SerializedName("FLG_ACTIVO")
    private String flg_activo;
    @SerializedName("IDNIVEL")
    private int idNivel;

    public int getIdClasificacion() {
        return idClasificacion;
    }

    public void setIdClasificacion(int idClasificacion) {
        this.idClasificacion = idClasificacion;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getCodClasificacion() {
        return codClasificacion;
    }

    public void setCodClasificacion(String codClasificacion) {
        this.codClasificacion = codClasificacion;
    }

    public String getDscClasificacion() {
        return dscClasificacion;
    }

    public void setDscClasificacion(String dscClasificacion) {
        this.dscClasificacion = dscClasificacion;
    }

    public String getCodJerarquiaPadre() {
        return codJerarquiaPadre;
    }

    public void setCodJerarquiaPadre(String codJerarquiaPadre) {
        this.codJerarquiaPadre = codJerarquiaPadre;
    }

    public String getFlg_activo() {
        return flg_activo;
    }

    public void setFlg_activo(String flg_activo) {
        this.flg_activo = flg_activo;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }
}
