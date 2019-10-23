package com.dms.almacensoft.data.models.recepciondespacho;

import com.google.gson.annotations.SerializedName;

public class BodyCrearDocumento {

    @SerializedName("cod_almacen")
    private String codAlmacen;
    @SerializedName("cod_tipo_documento")
    private String codTipoDocumento;
    @SerializedName("cod_clase_documento")
    private String codClaseDocumento;
    private int idClaseDocumento;
    @SerializedName("num_documento")
    private String numDocumento;
    @SerializedName("observacion")
    private String observacion;
    @SerializedName("tipo_doc_destino")
    private String tipoDocDestino;
    @SerializedName("cod_alm_destino")
    private String codAlmDestino;
    @SerializedName("cod_alm_virtual")
    private String codAlmVirtual;
    @SerializedName("cod_usuario")
    private String codUsuario;

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public String getCodTipoDocumento() {
        return codTipoDocumento;
    }

    public void setCodTipoDocumento(String codTipoDocumento) {
        this.codTipoDocumento = codTipoDocumento;
    }

    public String getCodClaseDocumento() {
        return codClaseDocumento;
    }

    public void setCodClaseDocumento(String codClaseDocumento) {
        this.codClaseDocumento = codClaseDocumento;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getTipoDocDestino() {
        return tipoDocDestino;
    }

    public void setTipoDocDestino(String tipoDocDestino) {
        this.tipoDocDestino = tipoDocDestino;
    }

    public String getCodAlmDestino() {
        return codAlmDestino;
    }

    public void setCodAlmDestino(String codAlmDestino) {
        this.codAlmDestino = codAlmDestino;
    }

    public String getCodAlmVirtual() {
        return codAlmVirtual;
    }

    public void setCodAlmVirtual(String codAlmVirtual) {
        this.codAlmVirtual = codAlmVirtual;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public int getIdClaseDocumento() {
        return idClaseDocumento;
    }

    public void setIdClaseDocumento(int idClaseDocumento) {
        this.idClaseDocumento = idClaseDocumento;
    }
}
