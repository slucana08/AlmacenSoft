package com.dms.almacensoft.data.entities.dbtransact;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity (indices = {@Index(value = "idLecturaDocumento" , unique = true)})
public class LecturaDocumento {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("ID_LECTURA_DOCUMENTO")
    private int idLecturaDocumento;
    @SerializedName("ID_DETALLE_DOCUMENTO")
    private int idDetalleDocumento;
    @SerializedName("ID_UBICACION")
    private int idUbicacion;
    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("LOTE_PRODUCTO")
    private String loteProducto;
    @SerializedName("SERIE_PRODUCTO")
    private String serieProducto;
    @SerializedName("CTD_ASIGNADA")
    private double ctdAsignada;
    @SerializedName("FLG_ACTIVO")
    private String flgActivo;
    @SerializedName("COD_USUARIO_REGISTRO")
    private String codUsarioRegistro;
    @SerializedName("FCH_REGISTRO")
    private String fchRegistro;
    @SerializedName("COD_USUARIO_MODIFICACION")
    private String codUsuarioModificacion;
    @SerializedName("FCH_MODIFICACION")
    private String fchModificacion;
    @SerializedName("ID_TERMINAL")
    private String idTerminal;
    @SerializedName("FCH_REGISTRO_TERMINAL")
    private String fchRegistroTerminal;
    @SerializedName("FLG_DESCARGADO")
    private int flgDescargado;
    @SerializedName("FCH_DESCARGADO")
    private String fchDescargado;
    @SerializedName("GUIA")
    private String guia;
    @SerializedName("FCH_ENVIADO")
    private String fchEnviado;
    @SerializedName("FLG_ENVIADO")
    private String flgEnviado;
    @SerializedName("FCH_PRODUCCION")
    private String fchProduccion;
    @SerializedName("COD_PROD_CAJA")
    private String codProdCaja;
    @SerializedName("NRO_CAJA")
    private int nroCaja;

    public int getIdLecturaDocumento() {
        return idLecturaDocumento;
    }

    public void setIdLecturaDocumento(int idLecturaDocumento) {
        this.idLecturaDocumento = idLecturaDocumento;
    }

    public int getIdDetalleDocumento() {
        return idDetalleDocumento;
    }

    public void setIdDetalleDocumento(int idDetalleDocumento) {
        this.idDetalleDocumento = idDetalleDocumento;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
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

    public String getCodUsarioRegistro() {
        return codUsarioRegistro;
    }

    public void setCodUsarioRegistro(String codUsarioRegistro) {
        this.codUsarioRegistro = codUsarioRegistro;
    }

    public String getFchRegistro() {
        return fchRegistro;
    }

    public void setFchRegistro(String fchRegistro) {
        this.fchRegistro = fchRegistro;
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

    public int getFlgDescargado() {
        return flgDescargado;
    }

    public void setFlgDescargado(int flgDescargado) {
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

    public int getNroCaja() {
        return nroCaja;
    }

    public void setNroCaja(int nroCaja) {
        this.nroCaja = nroCaja;
    }

    public String getCodUsuarioModificacion() {
        return codUsuarioModificacion;
    }

    public void setCodUsuarioModificacion(String codUsuarioModificacion) {
        this.codUsuarioModificacion = codUsuarioModificacion;
    }

    public String getFchRegistroTerminal() {
        return fchRegistroTerminal;
    }

    public void setFchRegistroTerminal(String fchRegistroTerminal) {
        this.fchRegistroTerminal = fchRegistroTerminal;
    }
}
