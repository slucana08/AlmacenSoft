package com.dms.almacensoft.data.entities.dbtransact;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity (indices = {@Index(value = "idMovimientoInterno" , unique = true)})
public class MovimientoInterno {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("ID_MOVIMIENTO_INTERNO")
    private int idMovimientoInterno;
    @SerializedName("ID_LECTURA_DOCUMENTO")
    private int idLecturaDocumento;
    @SerializedName("ID_ALMACEN_ORIGEN") // null en recepcion
    private int idAlmacenOrigen;
    @SerializedName("ID_ALMACEN_DESTINO") // null en despacho
    private int idAlmacenDestino;
    @SerializedName("ID_UBICACION_ORIGEN") // null en recepcion
    private int idUbicacionOrigen;
    @SerializedName("ID_UBICACION_DESTINO") // null en despacho
    private int idUbicacionDestino;
    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("LOTE_PRODUCTO")
    private String loteProducto;
    @SerializedName("SERIE_PRODUCTO")
    private String serieProducto;
    @SerializedName("CTD_ASIGNADA")
    private double ctdAsignada;
    @SerializedName("COD_USUARIO_REGISTRO")
    private String codUsuarioRegistro;
    @SerializedName("FLG_ACTIVO")
    private String flgActivo;
    @SerializedName("FCH_REGISTRO")
    private String fchRegistro;
    @SerializedName("COD_USUARIO_MODIFICACION")
    private String codUsuarioModificacion;
    @SerializedName("FCH_MODIFICACION")
    private String fchModificacion;
    @SerializedName("ID_ALMVIRTUAL_ORIGEN")
    private int idAlmVirtualOrigen;
    @SerializedName("ID_ALMVIRTUAL_DESTINO")
    private int idAlmVirtualDestino;
    @SerializedName("FLG_CERRAR_ENVIO")
    private String flgCerrarEnvio;
    @SerializedName("ID_TERMINAL")
    private String idTerminal;
    @SerializedName("FCH_REGISTRO_TERMINAL")
    private String fchRegistroTerminal;

    public int getIdMovimientoInterno() {
        return idMovimientoInterno;
    }

    public void setIdMovimientoInterno(int idMovimientoInterno) {
        this.idMovimientoInterno = idMovimientoInterno;
    }

    public int getIdLecturaDocumento() {
        return idLecturaDocumento;
    }

    public void setIdLecturaDocumento(int idLecturaDocumento) {
        this.idLecturaDocumento = idLecturaDocumento;
    }

    public int getIdAlmacenOrigen() {
        return idAlmacenOrigen;
    }

    public void setIdAlmacenOrigen(int idAlmacenOrigen) {
        this.idAlmacenOrigen = idAlmacenOrigen;
    }

    public int getIdAlmacenDestino() {
        return idAlmacenDestino;
    }

    public void setIdAlmacenDestino(int idAlmacenDestino) {
        this.idAlmacenDestino = idAlmacenDestino;
    }

    public int getIdUbicacionOrigen() {
        return idUbicacionOrigen;
    }

    public void setIdUbicacionOrigen(int idUbicacionOrigen) {
        this.idUbicacionOrigen = idUbicacionOrigen;
    }

    public int getIdUbicacionDestino() {
        return idUbicacionDestino;
    }

    public void setIdUbicacionDestino(int idUbicacionDestino) {
        this.idUbicacionDestino = idUbicacionDestino;
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

    public String getCodUsuarioRegistro() {
        return codUsuarioRegistro;
    }

    public void setCodUsuarioRegistro(String codUsuarioRegistro) {
        this.codUsuarioRegistro = codUsuarioRegistro;
    }

    public String getFlgActivo() {
        return flgActivo;
    }

    public void setFlgActivo(String flgActivo) {
        this.flgActivo = flgActivo;
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

    public int getIdAlmVirtualOrigen() {
        return idAlmVirtualOrigen;
    }

    public void setIdAlmVirtualOrigen(int idAlmVirtualOrigen) {
        this.idAlmVirtualOrigen = idAlmVirtualOrigen;
    }

    public int getIdAlmVirtualDestino() {
        return idAlmVirtualDestino;
    }

    public void setIdAlmVirtualDestino(int idAlmVirtualDestino) {
        this.idAlmVirtualDestino = idAlmVirtualDestino;
    }

    public String getFlgCerrarEnvio() {
        return flgCerrarEnvio;
    }

    public void setFlgCerrarEnvio(String flgCerrarEnvio) {
        this.flgCerrarEnvio = flgCerrarEnvio;
    }

    public String getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(String idTerminal) {
        this.idTerminal = idTerminal;
    }

    public String getFchRegistroTerminal() {
        return fchRegistroTerminal;
    }

    public void setFchRegistroTerminal(String fchRegistroTerminal) {
        this.fchRegistroTerminal = fchRegistroTerminal;
    }
}
