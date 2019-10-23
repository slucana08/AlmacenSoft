package com.dms.almacensoft.data.models;

import com.google.gson.annotations.SerializedName;

public class BodyUbicacion {

    @SerializedName("id_almacen")
    private int idAlmacen;
    @SerializedName("cod_ubicacion")
    private String codUbicacion;
    @SerializedName("cod_producto")
    private String codProducto;
    @SerializedName("tipo_resultado")
    private int tipoResultado; // 0 - Recepcion // 1 - Despacho/Inventario

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getCodUbicacion() {
        return codUbicacion;
    }

    public void setCodUbicacion(String codUbicacion) {
        this.codUbicacion = codUbicacion;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public int getTipoResultado() {
        return tipoResultado;
    }

    public void setTipoResultado(int tipoResultado) {
        this.tipoResultado = tipoResultado;
    }
}

