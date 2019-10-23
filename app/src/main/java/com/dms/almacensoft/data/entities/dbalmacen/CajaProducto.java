package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idCajaProducto")
public class CajaProducto {

    @SerializedName("ID_CAJA_PRODUCTO")
    private int idCajaProducto;
    @SerializedName("COD_PROD_CAJA")
    private String codProdCaja;
    @SerializedName("COD_PRODUCTO")
    private String codProducto;
    @SerializedName("CANT_EQUIVALENTE")
    private int cantEquivalente;

    public int getIdCajaProducto() {
        return idCajaProducto;
    }

    public void setIdCajaProducto(int idCajaProducto) {
        this.idCajaProducto = idCajaProducto;
    }

    public String getCodProdCaja() {
        return codProdCaja;
    }

    public void setCodProdCaja(String codProdCaja) {
        this.codProdCaja = codProdCaja;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public int getCantEquivalente() {
        return cantEquivalente;
    }

    public void setCantEquivalente(int cantEquivalente) {
        this.cantEquivalente = cantEquivalente;
    }
}
