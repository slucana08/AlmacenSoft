package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idVehiculo")
public class Vehiculo {

    @SerializedName("ID_VEHICULO")
    private int idVehiculo;
    @SerializedName("ID_EMPRESA")
    private int idEmpresa;
    @SerializedName("PLACA_VEHICULO")
    private String placaVehiculo;
    @SerializedName("DSC_VEHICULO")
    private String dscVehiculo;
    @SerializedName("FLG_ACTIVO")
    private String flgActivo;
    @SerializedName("COD_USUARIO_REGISTRO")
    private String codUsuarioRegistro;
    @SerializedName("FCH_REGISTRO")
    private String fchRegistro;
    @SerializedName("COD_USUARIO_MODIFICACION")
    private String codUsuarioModificacion;
    @SerializedName("FCH_MODIFICACION")
    private String fchModificacion;

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public String getDscVehiculo() {
        return dscVehiculo;
    }

    public void setDscVehiculo(String dscVehiculo) {
        this.dscVehiculo = dscVehiculo;
    }

    public String getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(String flgActivo) {
        this.flgActivo = flgActivo;
    }

    public String getCodUsuarioRegistro() {
        return codUsuarioRegistro;
    }

    public void setCodUsuarioRegistro(String codUsuarioRegistro) {
        this.codUsuarioRegistro = codUsuarioRegistro;
    }

    public String getFchRegistro() {
        return fchRegistro;
    }

    public void setFchRegistro(String fchRegistro) {
        this.fchRegistro = fchRegistro;
    }

    public String getCodUsuarioModificacion() {
        return codUsuarioModificacion;
    }

    public void setCodUsuarioModificacion(String codUsuarioModificacion) {
        this.codUsuarioModificacion = codUsuarioModificacion;
    }

    public String getFchModificacion() {
        return fchModificacion;
    }

    public void setFchModificacion(String fchModificacion) {
        this.fchModificacion = fchModificacion;
    }
}
