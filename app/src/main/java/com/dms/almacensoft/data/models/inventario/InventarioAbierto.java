package com.dms.almacensoft.data.models.inventario;

import com.google.gson.annotations.SerializedName;

public class InventarioAbierto {

    @SerializedName("ID_INVENTARIO")
    private int idInventario;
    @SerializedName("COD_INVENTARIO")
    private String codInventario;
    @SerializedName("NRO_CONTEO")
    private int nroConteo;

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public String getCodInventario() {
        return codInventario;
    }

    public void setCodInventario(String codInventario) {
        this.codInventario = codInventario;
    }

    public int getNroConteo() {
        return nroConteo;
    }

    public void setNroConteo(int nroConteo) {
        this.nroConteo = nroConteo;
    }
}
