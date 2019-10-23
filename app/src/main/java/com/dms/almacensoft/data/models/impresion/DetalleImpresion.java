package com.dms.almacensoft.data.models.impresion;

import android.arch.persistence.room.Ignore;

import com.google.gson.annotations.SerializedName;

public class DetalleImpresion {

    @SerializedName("ID_DETALLE_DOCUMENTO")
    private int idDetalleDocumento;
    @Ignore
    @SerializedName("ID_DOCUMENTO")
    private int idDocumento;
    @Ignore
    @SerializedName("NUM_DOCUMENTO")
    private String numDocumento;
    @Ignore
    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("COD_PRODUCTO")
    private String codProducto;
    @SerializedName("DSC_PRODUCTO")
    private String dscProducto;
    @SerializedName("LOTE_PRODUCTO")
    private String loteProducto;
    @SerializedName("SERIE_PRODUCTO")
    private String serieProducto;
    @Ignore
    @SerializedName("CTD_REQUERIDA")
    private double ctdRequerida;
    @Ignore
    @SerializedName("CTD_ATENDIDA")
    private double ctdAtendida;
    @Ignore
    @SerializedName("CTD_ATENDIDA_CLIENTE")
    private double ctdAtendidadCliente;
    @Ignore
    @SerializedName("LINEA")
    private int linea;
    @SerializedName("NUM_DOC_INTERNO")
    private String numDocInterno;
    @Ignore
    @SerializedName("UNIDADMEDIDA")
    private String unidadMedida;
    @Ignore
    @SerializedName("TIPOPROYECTO")
    private int tipoProyecto;
    @Ignore
    @SerializedName("NUMPROYECTO")
    private String numProyecto;
    @Ignore
    @SerializedName("ETIQUETADO")
    private String etiquetado;
    @Ignore
    @SerializedName("PROCEDENCIA")
    private String procedencia;
    @Ignore
    @SerializedName("FCH_REGISTRO")
    private String fchRegistro;
    @SerializedName("ID_LECTURA_DOCUMENTO")
    private int idLecturaDocumento;

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

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
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

    public String getLoteProducto() {
        return loteProducto;
    }

    public void setLoteProducto(String loteProducto) {
        this.loteProducto = loteProducto;
    }

    public String getSerieProducto() {
        return serieProducto;
    }

    public void setSerieProducto(String serieProducto) {
        this.serieProducto = serieProducto;
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

    public double getCtdAtendidadCliente() {
        return ctdAtendidadCliente;
    }

    public void setCtdAtendidadCliente(double ctdAtendidadCliente) {
        this.ctdAtendidadCliente = ctdAtendidadCliente;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public String getNumDocInterno() {
        return numDocInterno;
    }

    public void setNumDocInterno(String numDocInterno) {
        this.numDocInterno = numDocInterno;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public int getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(int tipoProyecto) {
        this.tipoProyecto = tipoProyecto;
    }

    public String getNumProyecto() {
        return numProyecto;
    }

    public void setNumProyecto(String numProyecto) {
        this.numProyecto = numProyecto;
    }

    public String getEtiquetado() {
        return etiquetado;
    }

    public void setEtiquetado(String etiquetado) {
        this.etiquetado = etiquetado;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    public String getFchRegistro() {
        return fchRegistro;
    }

    public void setFchRegistro(String fchRegistro) {
        this.fchRegistro = fchRegistro;
    }

    public int getIdLecturaDocumento() {
        return idLecturaDocumento;
    }

    public void setIdLecturaDocumento(int idLecturaDocumento) {
        this.idLecturaDocumento = idLecturaDocumento;
    }
}
