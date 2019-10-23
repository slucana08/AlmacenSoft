package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idCliente")
public class Cliente {

    @SerializedName("ID_CLIENTE")
    private int idCliente;
    @SerializedName("ID_EMPRESA")
    private int idEmpresa;
    @SerializedName("COD_CLIENTE")
    private String codCliente;
    @SerializedName("DSC_CLIENTE")
    private String dscCliente;
    @SerializedName("FLG_ACTIVO")
    private String flgActivo;
    @SerializedName("DIRECCION")
    private String direccion;

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getDscCliente() {
        return dscCliente;
    }

    public void setDscCliente(String dscCliente) {
        this.dscCliente = dscCliente;
    }

    public String getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(String flgActivo) {
        this.flgActivo = flgActivo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
