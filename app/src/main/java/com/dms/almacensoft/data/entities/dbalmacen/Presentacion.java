package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idPresentacion")
public class Presentacion {

    @SerializedName("ID_PRESENTACION")
    private int idPresentacion;
    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("ID_UNIDAD_MEDIDA")
    private int idUnidadMedida;
    @SerializedName("COD_PRESENTACION")
    private String codPresentacion;
    @SerializedName("DSC_PRESENTACION")
    private String dscPresentacion;
    @SerializedName("CANTIDAD_PRESENTACION")
    private double cantidadPresentacion;
    @SerializedName("PESO_PRESENTACION")
    private double pesoPresentacion;

    public int getIdPresentacion() {
        return idPresentacion;
    }

    public void setIdPresentacion(int idPresentacion) {
        this.idPresentacion = idPresentacion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdUnidadMedida() {
        return idUnidadMedida;
    }

    public void setIdUnidadMedida(int idUnidadMedida) {
        this.idUnidadMedida = idUnidadMedida;
    }

    public String getCodPresentacion() {
        return codPresentacion;
    }

    public void setCodPresentacion(String codPresentacion) {
        this.codPresentacion = codPresentacion;
    }

    public String getDscPresentacion() {
        return dscPresentacion;
    }

    public void setDscPresentacion(String dscPresentacion) {
        this.dscPresentacion = dscPresentacion;
    }

    public double getCantidadPresentacion() {
        return cantidadPresentacion;
    }

    public void setCantidadPresentacion(double cantidadPresentacion) {
        this.cantidadPresentacion = cantidadPresentacion;
    }

    public double getPesoPresentacion() {
        return pesoPresentacion;
    }

    public void setPesoPresentacion(double pesoPresentacion) {
        this.pesoPresentacion = pesoPresentacion;
    }
}
