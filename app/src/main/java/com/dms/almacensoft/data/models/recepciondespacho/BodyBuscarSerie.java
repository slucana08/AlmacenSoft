package com.dms.almacensoft.data.models.recepciondespacho;

import com.google.gson.annotations.SerializedName;

public class BodyBuscarSerie {

    @SerializedName("serie_producto")
    private String serieProducto;
    @SerializedName("id_producto")
    private int idProducto;
    @SerializedName("id_documento")
    private int idDocumento;

    public String getSerieProducto() {
        return serieProducto;
    }

    public void setSerieProducto(String serieProducto) {
        this.serieProducto = serieProducto;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }
}
