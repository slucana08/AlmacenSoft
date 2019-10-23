package com.dms.almacensoft.data.models.inventario;

import com.google.gson.annotations.SerializedName;

public class SerieResponseInventario {

    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("COD_PRODUCTO")
    private String codProducto;
    @SerializedName("DSC_PRODUCTO")
    private String dscProducto;
    @SerializedName("SERIE_PRODUCTO")
    private String serieProducto;
    @SerializedName("ID_LECTURA_INVENTARIO")
    private int idLecturaInventario;

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }


    public String getDscProducto() {
        return dscProducto;
    }

    public void setDscProducto(String dscProducto) {
        this.dscProducto = dscProducto;
    }

    public String getSerieProducto() {
        return serieProducto;
    }

    public void setSerieProducto(String serieProducto) {
        this.serieProducto = serieProducto;
    }

    public int getIdLecturaInventario() {
        return idLecturaInventario;
    }

    public void setIdLecturaInventario(int idLecturaInventario) {
        this.idLecturaInventario = idLecturaInventario;
    }
}

