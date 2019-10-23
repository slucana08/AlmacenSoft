package com.dms.almacensoft.data.entities.dbtransact;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idLecturaVerificacionDocumento", indices = {@Index(value = "idLecturaVerificacionDocumento" ,
        unique = true)})
public class LecturaVerificacionDocumento {

    @SerializedName("ID_LECTURA_VERIFICACION_DOCUMENTO")
    private int idLecturaVerificacionDocumento;
    @SerializedName("ID_DETALLE_VERIFICACION_DOCUMENTO")
    private int idDetalleVerificacionDocumento;
    @SerializedName("ID_UBICACION")
    private int idUbicacion;
    @SerializedName("LOTE_PRODUCTO")
    private String loteProducto;
    @SerializedName("SERIE_PRODUCTO")
    private String serieProducto;
    @SerializedName("CTD_ASIGNADA")
    private double ctdAsignada;
    @SerializedName("FLG_ACTIVO")
    private String flgActivo;
    @SerializedName("COD_USUARIO_REGISTRO")
    private String codUsuarioRegistro;
    @SerializedName("FCH_REGISTRO")
    private String fchRegistro;
    @SerializedName("COD_USUARIO_MODIFICACION")
    private String codUsuarioModificacion;
    @SerializedName("FCH_MODIFICACION")
    private String fchModificacion;
    @SerializedName("ID_TERMINAL")
    private String idTerminal;
    @SerializedName("FLG_DESCARGADO")
    private String flgDescargado;
    @SerializedName("FCH_DESCARGADO")
    private String fchDescargado;
    @SerializedName("GUIA")
    private String guia;
    @SerializedName("FCH_ENVIADO")
    private String fchEnviado;
    @SerializedName("FLG_ENVIADO")
    private String flgEnviado;

    public int getIdLecturaVerificacionDocumento() {
        return idLecturaVerificacionDocumento;
    }

    public void setIdLecturaVerificacionDocumento(int idLecturaVerificacionDocumento) {
        this.idLecturaVerificacionDocumento = idLecturaVerificacionDocumento;
    }

    public int getIdDetalleVerificacionDocumento() {
        return idDetalleVerificacionDocumento;
    }

    public void setIdDetalleVerificacionDocumento(int idDetalleVerificacionDocumento) {
        this.idDetalleVerificacionDocumento = idDetalleVerificacionDocumento;
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

    public double getCtdAsignada() {
        return ctdAsignada;
    }

    public void setCtdAsignada(double ctdAsignada) {
        this.ctdAsignada = ctdAsignada;
    }

    public String getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(String flgActivo) {
        this.flgActivo = flgActivo;
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

    public String getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(String idTerminal) {
        this.idTerminal = idTerminal;
    }

    public String getFlgDescargado() {
        return flgDescargado;
    }

    public void setFlgDescargado(String flgDescargado) {
        this.flgDescargado = flgDescargado;
    }

    public String getFchDescargado() {
        return fchDescargado;
    }

    public void setFchDescargado(String fchDescargado) {
        this.fchDescargado = fchDescargado;
    }

    public String getGuia() {
        return guia;
    }

    public void setGuia(String guia) {
        this.guia = guia;
    }

    public String getFchEnviado() {
        return fchEnviado;
    }

    public void setFchEnviado(String fchEnviado) {
        this.fchEnviado = fchEnviado;
    }

    public String getFlgEnviado() {
        return flgEnviado;
    }

    public void setFlgEnviado(String flgEnviado) {
        this.flgEnviado = flgEnviado;
    }
}
