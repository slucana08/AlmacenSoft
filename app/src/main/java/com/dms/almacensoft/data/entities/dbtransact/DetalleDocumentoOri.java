package com.dms.almacensoft.data.entities.dbtransact;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "idDetalleDocumento", indices = {@Index(value = "idDetalleDocumento" , unique = true)})
public class DetalleDocumentoOri {

    @SerializedName("ID_DETALLE_DOCUMENTO")
    private int idDetalleDocumento;
    @SerializedName("ID_DOCUMENTO")
    private int idDocumento;
    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("LINEA")
    private int linea;
    @SerializedName("LOTE_PRODUCTO")
    private String loteProducto;
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
    @SerializedName("CTD_ATENDIDA_CLIENTE")
    private double ctdAtendidaCliente;
    @SerializedName("COD_USUARIO_MODIFICACION")
    private String codUsuarioModificacion;
    @SerializedName("FCH_MODIFICACION")
    private String fchModificacion;
    @SerializedName("FCH_REGISTRO_MODIFICACION")
    private String fchRegistroImportacion;
    @SerializedName("FCH_ULTIMA_IMPORTACION")
    private String fchUltimaImportacion;
    @SerializedName("UNIDADMEDIDA")
    private String unidadMedida;
    @SerializedName("TIPOPROYECTO")
    private int tipoProyecto;
    @SerializedName("NUMPROYECTO")
    private String numProyecto;
    @SerializedName("ETIQUETADO")
    private String etiquetado;
    @SerializedName("COD_UBICACIONES")
    private String codUbicaciones;


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

    public String getFchRegistroImportacion() {
        return fchRegistroImportacion;
    }

    public void setFchRegistroImportacion(String fchRegistroImportacion) {
        this.fchRegistroImportacion = fchRegistroImportacion;
    }

    public String getFchUltimaImportacion() {
        return fchUltimaImportacion;
    }

    public void setFchUltimaImportacion(String fchUltimaImportacion) {
        this.fchUltimaImportacion = fchUltimaImportacion;
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

    public String getCodUbicaciones() {
        return codUbicaciones;
    }

    public void setCodUbicaciones(String codUbicaciones) {
        this.codUbicaciones = codUbicaciones;
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

    public double getCtdPendiente() {
        return ctdPendiente;
    }

    public void setCtdPendiente(double ctdPendiente) {
        this.ctdPendiente = ctdPendiente;
    }
}
