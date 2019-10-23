package com.dms.almacensoft.data.models.recepciondespacho;

import com.google.gson.annotations.SerializedName;

public class BodyEliminarLecturas {

    @SerializedName("id_detalle_documento")
    private int idDetalleDocumento;
    @SerializedName("id_lectura_documento")
    private int idLecturaDocumento;
    @SerializedName("serie_producto")
    private String serieProducto;
    @SerializedName("cod_usuario")
    private String codUsuario;
    @SerializedName("terminal")
    private String terminal;

    public int getIdDetalleDocumento() {
        return idDetalleDocumento;
    }

    public void setIdDetalleDocumento(int idDetalleDocumento) {
        this.idDetalleDocumento = idDetalleDocumento;
    }

    public int getIdLecturaDocumento() {
        return idLecturaDocumento;
    }

    public void setIdLecturaDocumento(int idLecturaDocumento) {
        this.idLecturaDocumento = idLecturaDocumento;
    }

    public String getSerieProducto() {
        return serieProducto;
    }

    public void setSerieProducto(String serieProducto) {
        this.serieProducto = serieProducto;
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
}
