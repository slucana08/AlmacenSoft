package com.dms.almacensoft.data.models.impresion;

import com.google.gson.annotations.SerializedName;

public class BodyImprimirEtiqueta {

    @SerializedName("num_copias")
    private int numCopias;
    @SerializedName("codigoProducto")
    private String codigoProducto;
    @SerializedName("descripcionProducto")
    private String descripcionProducto;
    @SerializedName("lote")
    private String lote;
    @SerializedName("serie")
    private String serie;

    public int getNumCopias() {
        return numCopias;
    }

    public void setNumCopias(int numCopias) {
        this.numCopias = numCopias;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }
}
