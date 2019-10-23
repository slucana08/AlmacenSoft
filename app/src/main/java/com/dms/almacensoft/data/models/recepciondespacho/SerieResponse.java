package com.dms.almacensoft.data.models.recepciondespacho;

import com.google.gson.annotations.SerializedName;

public class SerieResponse {

    @SerializedName("ID_LECTURA_DOCUMENTO")
    private int idLecturaDocumento;
    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("DSC_PRODUCTO")
    private String dscProducto;
    @SerializedName("COD_PRODUCTO")
    private String codProducto;
    @SerializedName("SERIE_PRODUCTO")
    private String serieProducto;

    public int getIdLecturaDocumento() {
        return idLecturaDocumento;
    }

    public void setIdLecturaDocumento(int idLecturaDocumento) {
        this.idLecturaDocumento = idLecturaDocumento;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getDscProducto() {
        return dscProducto;
    }

    public void setDscProducto(String dscProducto) {
        this.dscProducto = dscProducto;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public String getSerieProducto() {
        return serieProducto;
    }

    public void setSerieProducto(String serieProducto) {
        this.serieProducto = serieProducto;
    }
}
