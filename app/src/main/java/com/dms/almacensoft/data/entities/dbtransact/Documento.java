package com.dms.almacensoft.data.entities.dbtransact;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Documento {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("ID_DOCUMENTO")
    private int idDocumento;
    @SerializedName("ID_ALMACEN")
    private int idAlmacen;
    @SerializedName("COD_TIPO_DOCUMENTO")
    private String codTipoDocumento;
    @SerializedName("NUM_DOCUMENTO")
    private String numDocumento;
    @SerializedName("FCH_DOCUMENTO")
    private String fchDocumento;
    @SerializedName("COD_ESTADO")
    private String codEstado;
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
    @SerializedName("DSC_OBSERVACION")
    private String dscObservacion;
    @SerializedName("FCH_REGISTRO_IMPORTACION")
    private String fchRegistroImportacion;
    @SerializedName("FCH_ULTIMA_IMPORTACION")
    private String fchUltimaImportacion;
    @SerializedName("ID_CLASE_DOCUMENTO")
    private int idClaseDocumento;
    @SerializedName("COD_CLASE_DOCUMENTO")
    private String codClaseDocumento;
    @SerializedName("TIPO_DOCUMENTO_DEST")
    private String tipoDocumentoDest;
    @SerializedName("MOVIMIENTO")
    private String movimiento;
    @SerializedName("TIPO_APLICACION")
    private String tipoAplicacion;
    @SerializedName("OBS_DOCUMENTO")
    private String obsDocumento;
    @SerializedName("COD_ALMDESTINO")
    private String codAlmDestino;
    @SerializedName("DSC_ALMDESTINO")
    private String dscAlmDestino;
    @SerializedName("COD_ALMVIRTUAL")
    private String codAlmVirtual;
    @SerializedName("DSC_ALMVIRTUAL")
    private String dscAlmVirtual;
    @SerializedName("TIPO_DOCUMENTO_ORI")
    private int tipoDocumentoOri;
    @SerializedName("EJERCICIO_CONTB_ORI")
    private int ejercicioContbOri;
    @SerializedName("PERIODO_ORI")
    private int periodoOri;
    @SerializedName("DIVISION")
    private int division;
    @SerializedName("UNIDAD")
    private int unidad;
    @SerializedName("ORDNUMD")
    private int ordnumd;
    @SerializedName("FOLIO")
    private int folio;
    @SerializedName("TIPO_OPERACION")
    private int tipoOperacion;
    @SerializedName("SOLICITUD")
    private int solicitud;
    @SerializedName("EMBNUMID")
    private int embNumId;
    @SerializedName("RECNUMID")
    private int recNumId;
    @SerializedName("PERIODOCONTROL")
    private int periodoControl;
    @SerializedName("EJERCICIO_CONTABLE")
    private int ejercicioContable;
    @SerializedName("TIPO_ORDEN_COMPRA")
    private int tipoOrdenCompra;
    @SerializedName("FOLIO_DOC_EXIS")
    private int folioDocExis;
    @SerializedName("PROCEDENCIA")
    private String procedencia;
    @SerializedName("FLG_CREADO_DOC")
    private String flgCreadoDoc;
    @SerializedName("COD_CLIENTE")
    private String codCliente;
    @SerializedName("PLACA_VEHICULO")
    private String placaVehiculo;
    @SerializedName("NRO_ERROR_KIT")
    private int nroErrorKit;

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getCodTipoDocumento() {
        return codTipoDocumento;
    }

    public void setCodTipoDocumento(String codTipoDocumento) {
        this.codTipoDocumento = codTipoDocumento;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getFchDocumento() {
        return fchDocumento;
    }

    public void setFchDocumento(String fchDocumento) {
        this.fchDocumento = fchDocumento;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
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

    public String getDscObservacion() {
        return dscObservacion;
    }

    public void setDscObservacion(String dscObservacion) {
        this.dscObservacion = dscObservacion;
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

    public int getIdClaseDocumento() {
        return idClaseDocumento;
    }

    public void setIdClaseDocumento(int idClaseDocumento) {
        this.idClaseDocumento = idClaseDocumento;
    }

    public String getTipoDocumentoDest() {
        return tipoDocumentoDest;
    }

    public void setTipoDocumentoDest(String tipoDocumentoDest) {
        this.tipoDocumentoDest = tipoDocumentoDest;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public String getTipoAplicacion() {
        return tipoAplicacion;
    }

    public void setTipoAplicacion(String tipoAplicacion) {
        this.tipoAplicacion = tipoAplicacion;
    }

    public String getObsDocumento() {
        return obsDocumento;
    }

    public void setObsDocumento(String obsDocumento) {
        this.obsDocumento = obsDocumento;
    }

    public String getCodAlmDestino() {
        return codAlmDestino;
    }

    public void setCodAlmDestino(String codAlmDestino) {
        this.codAlmDestino = codAlmDestino;
    }

    public String getDscAlmDestino() {
        return dscAlmDestino;
    }

    public void setDscAlmDestino(String dscAlmDestino) {
        this.dscAlmDestino = dscAlmDestino;
    }

    public String getCodAlmVirtual() {
        return codAlmVirtual;
    }

    public void setCodAlmVirtual(String codAlmVirtual) {
        this.codAlmVirtual = codAlmVirtual;
    }

    public String getDscAlmVirtual() {
        return dscAlmVirtual;
    }

    public void setDscAlmVirtual(String dscAlmVirtual) {
        this.dscAlmVirtual = dscAlmVirtual;
    }

    public int getTipoDocumentoOri() {
        return tipoDocumentoOri;
    }

    public void setTipoDocumentoOri(int tipoDocumentoOri) {
        this.tipoDocumentoOri = tipoDocumentoOri;
    }

    public int getEjercicioContbOri() {
        return ejercicioContbOri;
    }

    public void setEjercicioContbOri(int ejercicioContbOri) {
        this.ejercicioContbOri = ejercicioContbOri;
    }

    public int getPeriodoOri() {
        return periodoOri;
    }

    public void setPeriodoOri(int periodoOri) {
        this.periodoOri = periodoOri;
    }

    public int getDivision() {
        return division;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    public int getUnidad() {
        return unidad;
    }

    public void setUnidad(int unidad) {
        this.unidad = unidad;
    }

    public int getOrdnumd() {
        return ordnumd;
    }

    public void setOrdnumd(int ordnumd) {
        this.ordnumd = ordnumd;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public int getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(int tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public int getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(int solicitud) {
        this.solicitud = solicitud;
    }

    public int getEmbNumId() {
        return embNumId;
    }

    public void setEmbNumId(int embNumId) {
        this.embNumId = embNumId;
    }

    public int getRecNumId() {
        return recNumId;
    }

    public void setRecNumId(int recNumId) {
        this.recNumId = recNumId;
    }

    public int getPeriodoControl() {
        return periodoControl;
    }

    public void setPeriodoControl(int periodoControl) {
        this.periodoControl = periodoControl;
    }

    public int getEjercicioContable() {
        return ejercicioContable;
    }

    public void setEjercicioContable(int ejercicioContable) {
        this.ejercicioContable = ejercicioContable;
    }

    public int getTipoOrdenCompra() {
        return tipoOrdenCompra;
    }

    public void setTipoOrdenCompra(int tipoOrdenCompra) {
        this.tipoOrdenCompra = tipoOrdenCompra;
    }

    public int getFolioDocExis() {
        return folioDocExis;
    }

    public void setFolioDocExis(int folioDocExis) {
        this.folioDocExis = folioDocExis;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    public String getFlgCreadoDoc() {
        return flgCreadoDoc;
    }

    public void setFlgCreadoDoc(String flgCreadoDoc) {
        this.flgCreadoDoc = flgCreadoDoc;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public int getNroErrorKit() {
        return nroErrorKit;
    }

    public void setNroErrorKit(int nroErrorKit) {
        this.nroErrorKit = nroErrorKit;
    }

    public String getCodClaseDocumento() {
        return codClaseDocumento;
    }

    public void setCodClaseDocumento(String codClaseDocumento) {
        this.codClaseDocumento = codClaseDocumento;
    }
}
