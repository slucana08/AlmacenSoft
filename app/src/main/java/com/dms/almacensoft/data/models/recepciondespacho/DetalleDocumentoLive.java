package com.dms.almacensoft.data.models.recepciondespacho;

import android.arch.persistence.room.Ignore;

import com.google.gson.annotations.SerializedName;

public class DetalleDocumentoLive {

    @SerializedName("LINEA")
    private int linea;
    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("COD_PRODUCTO")
    private String codProducto;
    @SerializedName("DSC_PRODUCTO")
    private String dscProducto;
    @SerializedName("CTD_REQUERIDA")
    private double ctdRequerida;
    @SerializedName("CTD_ATENDIDA")
    private double ctdAtendida;
    @SerializedName("CTD_PENDIENTE")
    private double ctdPendiente;
    @Ignore
    private int ctdCerrada;
    @SerializedName("ID_DETALLE_DOCUMENTO")
    private int idDetalleDocumento;
    @SerializedName("ID_DOCUMENTO")
    private int idDocumento;

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

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

    public double getCtdRequerida() {
        return ctdRequerida;
    }

    public void setCtdRequerida(double ctdRequerida) {
        this.ctdRequerida = ctdRequerida;
    }

    public double getCtdAtendida() {
        return ctdAtendida;
    }

    public void setCtdAtendida(double ctdAtendida) {
        this.ctdAtendida = ctdAtendida;
    }

    public double getCtdPendiente() {
        return ctdPendiente;
    }

    public void setCtdPendiente(double ctdPendiente) {
        this.ctdPendiente = ctdPendiente;
    }

    public int getCtdCerrada() {
        return ctdCerrada;
    }

    public void setCtdCerrada(int ctdCerrada) {
        this.ctdCerrada = ctdCerrada;
    }

    public int getIdDetalleDocumento() {
        return idDetalleDocumento;
    }

    public void setIdDetalleDocumento(int idDetalleDocumento) {
        this.idDetalleDocumento = idDetalleDocumento;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }
}
