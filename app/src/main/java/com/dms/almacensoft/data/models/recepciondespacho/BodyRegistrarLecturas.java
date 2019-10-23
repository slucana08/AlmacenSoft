package com.dms.almacensoft.data.models.recepciondespacho;

import com.google.gson.annotations.SerializedName;

public class BodyRegistrarLecturas {

    @SerializedName("id_documento")
    private int idDocumento;
    @SerializedName("id_detalle_documento")
    private int idDetalleDocumento;
    @SerializedName("id_producto")
    private int idProducto;
    @SerializedName("linea")
    private int linea;
    @SerializedName("ctd_requerida")
    private double ctdRequerida;
    @SerializedName("ctd_atendida")
    private double ctdAtendida;
    @SerializedName("flg_con_sin_doc")
    private boolean flgConSinDoc;  // true -- con documento // false -- sin documento
    @SerializedName("id_ubicacion")
    private int idUbicacion;
    @SerializedName("lote_producto")
    private String loteProducto;
    @SerializedName("serie_producto")
    private String serieProducto;
    @SerializedName("ctd_confirmada")
    private double ctdConfirmada;
    @SerializedName("num_doc_interno")
    private String numDocInterno; // nro de guia
    @SerializedName("cod_usuario")
    private String codUsuario;
    @SerializedName("terminal")
    private String terminal;
    @SerializedName("cod_ubi_des")
    private String codUbiDes;
    @SerializedName("cod_ubicacion")
    private String codUbicacion;
    @SerializedName("espacio_lleno")
    private String espacioLleno;
    @SerializedName("fch_produccion")
    private String fchProduccion; /// 20190326 formato
    @SerializedName("cod_prod_caja")
    private String codProdCaja;
    @SerializedName("num_caja")
    private int numCaja;
    @SerializedName("cod_presentacion")
    private String codPresentacion;
    @SerializedName("ctd_presentacion")
    private int ctdPresentacion;

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public int getIdDetalleDocumento() {
        return idDetalleDocumento;
    }

    public void setIdDetalleDocumento(int idDetalleDocumento) {
        this.idDetalleDocumento = idDetalleDocumento;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
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

    public boolean getFlgConSinDoc() {
        return flgConSinDoc;
    }

    public void setFlgConSinDoc(boolean flgConSinDoc) {
        this.flgConSinDoc = flgConSinDoc;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
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

    public double getCtdConfirmada() {
        return ctdConfirmada;
    }

    public void setCtdConfirmada(double ctdConfirmada) {
        this.ctdConfirmada = ctdConfirmada;
    }

    public String getNumDocInterno() {
        return numDocInterno;
    }

    public void setNumDocInterno(String numDocInterno) {
        this.numDocInterno = numDocInterno;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getCodUbiDes() {
        return codUbiDes;
    }

    public void setCodUbiDes(String codUbiDes) {
        this.codUbiDes = codUbiDes;
    }

    public String getCodUbicacion() {
        return codUbicacion;
    }

    public void setCodUbicacion(String codUbicacion) {
        this.codUbicacion = codUbicacion;
    }

    public String getEspacioLleno() {
        return espacioLleno;
    }

    public void setEspacioLleno(String espacioLleno) {
        this.espacioLleno = espacioLleno;
    }

    public String getFchProduccion() {
        return fchProduccion;
    }

    public void setFchProduccion(String fchProduccion) {
        this.fchProduccion = fchProduccion;
    }

    public String getCodProdCaja() {
        return codProdCaja;
    }

    public void setCodProdCaja(String codProdCaja) {
        this.codProdCaja = codProdCaja;
    }

    public int getNumCaja() {
        return numCaja;
    }

    public void setNumCaja(int numCaja) {
        this.numCaja = numCaja;
    }

    public String getCodPresentacion() {
        return codPresentacion;
    }

    public void setCodPresentacion(String codPresentacion) {
        this.codPresentacion = codPresentacion;
    }

    public int getCtdPresentacion() {
        return ctdPresentacion;
    }

    public void setCtdPresentacion(int ctdPresentacion) {
        this.ctdPresentacion = ctdPresentacion;
    }
}
