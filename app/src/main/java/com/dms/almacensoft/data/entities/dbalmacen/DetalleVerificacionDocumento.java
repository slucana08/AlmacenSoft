package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idDetalleVerificacionDocumento", indices = {@Index(value = "idDetalleVerificacionDocumento" ,
        unique = true)})
public class DetalleVerificacionDocumento {

    @SerializedName("ID_DETALLE_VERIFICACION_DOCUMENTO")
    private int idDetalleVerificacionDocumento;
    @SerializedName("ID_VERIFICACION_DOCUMENTO")
    private int idVerificacionDocumento;
    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("LINEA")
    private int linea;
    @SerializedName("LOTE_PRODUCTO")
    private String loteProducto;
    @SerializedName("CTD_REQUERIDA")
    private double ctdRequerida;
    @SerializedName("CTD_ATENDIDA")
    private double ctdAtendida;
    @SerializedName("CTD_ATENDIDA_CLIENTE")
    private double ctdAtendidaCliente;
    @SerializedName("NUM_DOC_INTERNO")
    private String numDocInterno;
    @SerializedName("FLG_ACTIVO")
    private String flgActivo;
    @SerializedName("FLG_CERRAR_ENVIO")
    private String flgCerrarEnvio;
    @SerializedName("ETIQUETADO")
    private String etiquetado;
    @SerializedName("COD_USUARIO_REGISTRO")
    private String codUsuarioRegistro;
    @SerializedName("FCH_REGISTRO")
    private String fchRegistro;
    @SerializedName("COD_USUARIO_MODIFICACION")
    private String codUsuarioModificacion;
    @SerializedName("FCH_MODIFICACION")
    private String fchModificacion;
    @SerializedName("CTD_TRANSPORTAR")
    private double ctdTransportar;

    public int getIdDetalleVerificacionDocumento() {
        return idDetalleVerificacionDocumento;
    }

    public void setIdDetalleVerificacionDocumento(int idDetalleVerificacionDocumento) {
        this.idDetalleVerificacionDocumento = idDetalleVerificacionDocumento;
    }

    public int getIdVerificacionDocumento() {
        return idVerificacionDocumento;
    }

    public void setIdVerificacionDocumento(int idVerificacionDocumento) {
        this.idVerificacionDocumento = idVerificacionDocumento;
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

    public String getLoteProducto() {
        return loteProducto;
    }

    public void setLoteProducto(String loteProducto) {
        this.loteProducto = loteProducto;
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

    public double getCtdAtendidaCliente() {
        return ctdAtendidaCliente;
    }

    public void setCtdAtendidaCliente(double ctdAtendidaCliente) {
        this.ctdAtendidaCliente = ctdAtendidaCliente;
    }

    public String getNumDocInterno() {
        return numDocInterno;
    }

    public void setNumDocInterno(String numDocInterno) {
        this.numDocInterno = numDocInterno;
    }

    public String getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(String flgActivo) {
        this.flgActivo = flgActivo;
    }

    public String getFlgCerrarEnvio() {
        return flgCerrarEnvio;
    }

    public void setFlgCerrarEnvio(String flgCerrarEnvio) {
        this.flgCerrarEnvio = flgCerrarEnvio;
    }

    public String getEtiquetado() {
        return etiquetado;
    }

    public void setEtiquetado(String etiquetado) {
        this.etiquetado = etiquetado;
    }

    public String getCodUsuarioRegistro() {
        return codUsuarioRegistro;
    }

    public void setCodUsuarioRegistro(String codUsuarioRegistro) {
        this.codUsuarioRegistro = codUsuarioRegistro;
    }

    public String getFchRegistro() {
        return fchRegistro;
    }

    public void setFchRegistro(String fchRegistro) {
        this.fchRegistro = fchRegistro;
    }

    public String getCodUsuarioModificacion() {
        return codUsuarioModificacion;
    }

    public void setCodUsuarioModificacion(String codUsuarioModificacion) {
        this.codUsuarioModificacion = codUsuarioModificacion;
    }

    public String getFchModificacion() {
        return fchModificacion;
    }

    public void setFchModificacion(String fchModificacion) {
        this.fchModificacion = fchModificacion;
    }

    public double getCtdTransportar() {
        return ctdTransportar;
    }

    public void setCtdTransportar(double ctdTransportar) {
        this.ctdTransportar = ctdTransportar;
    }
}
