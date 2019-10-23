package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idVerificacionDocumento", indices = {@Index(value = "idVerificacionDocumento" , unique = true)})
public class VerificacionDocumento {

    @SerializedName("ID_VERIFICACION_DOCUMENTO")
    private int idVerificacionDocumento;
    @SerializedName("ID_ALMACEN")
    private int idAlmacen;
    @SerializedName("COD_TIPO_VERIFICACION_DOCUMENTO")
    private String codTipoVerificacionDocumento;
    @SerializedName("NUM_VERIFICACION_DOCUMENTO")
    private String numVerificacionDocumento;
    @SerializedName("FCH_VERIFICACION_DOCUMENTO")
    private String fchVerificacionDocumento;
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
    @SerializedName("TIPO_VERIFICACION_DOCUMENTO_DEST")
    private String tipoVerificacionDocumentoDest;
    @SerializedName("MOVIMIENTO")
    private String movimiento;
    @SerializedName("TIPO_APLICACION")
    private String tipoAplicacion;
    @SerializedName("OBS_VERIFICACION_DOCUMENTO")
    private String obsVerificacionDocumento;
    @SerializedName("COD_ALMDESTINO")
    private String codAlmdestino;
    @SerializedName("DSC_ALMDESTINO")
    private String dscAlmdestino;
    @SerializedName("COD_ALMVIRTUAL")
    private String codAlmvirtual;
    @SerializedName("DSC_ALMVIRTUAL")
    private String dscAlmvirtual;
    @SerializedName("TIPO_DOCUMENTO_ORI")
    private int tipoDocumentoOri;
    @SerializedName("EJERCICIO_CONTB_ORI")
    private int ejercicioContbOri;
    @SerializedName("PERIODO_ORI")
    private int periodo_ori;
    @SerializedName("DIVISION")
    private int division;
    @SerializedName("UNIDAD")
    private int unidad;
    @SerializedName("ORDNUMD")
    private int ordnumd;
    @SerializedName("FOLIO")
    private int folio;
    @SerializedName("TIPOOPERACION")
    private int tipoOperacion;
    @SerializedName("SOLICITUD")
    private int solicitud;
    @SerializedName("EMBNUMID")
    private int embNumId;
    @SerializedName("RECNUMID")
    private int recNumId;
    @SerializedName("PERIODOCONTROL")
    private int periodoControl;
    @SerializedName("EJERCICIOCONTABLE")
    private int ejercicioContable;
    @SerializedName("FOLIODOCEXIS")
    private int folioDocExis;
    @SerializedName("PROCEDENCIA")
    private String procedencia;
    @SerializedName("FLG_CREADO_DOC")
    private String flgCreadoDoc;
    @SerializedName("COD_CLIENTE")
    private String codCliente;
    @SerializedName("COD_DESTINO")
    private String codDestino;
    @SerializedName("PLACA_VEHICULO")
    private String placaVehiculo;

    public int getIdVerificacionDocumento() {
        return idVerificacionDocumento;
    }

    public void setIdVerificacionDocumento(int idVerificacionDocumento) {
        this.idVerificacionDocumento = idVerificacionDocumento;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getCodTipoVerificacionDocumento() {
        return codTipoVerificacionDocumento;
    }

    public void setCodTipoVerificacionDocumento(String codTipoVerificacionDocumento) {
        this.codTipoVerificacionDocumento = codTipoVerificacionDocumento;
    }

    public String getNumVerificacionDocumento() {
        return numVerificacionDocumento;
    }

    public void setNumVerificacionDocumento(String numVerificacionDocumento) {
        this.numVerificacionDocumento = numVerificacionDocumento;
    }

    public String getFchVerificacionDocumento() {
        return fchVerificacionDocumento;
    }

    public void setFchVerificacionDocumento(String fchVerificacionDocumento) {
        this.fchVerificacionDocumento = fchVerificacionDocumento;
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

    public String getTipoVerificacionDocumentoDest() {
        return tipoVerificacionDocumentoDest;
    }

    public void setTipoVerificacionDocumentoDest(String tipoVerificacionDocumentoDest) {
        this.tipoVerificacionDocumentoDest = tipoVerificacionDocumentoDest;
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

    public String getObsVerificacionDocumento() {
        return obsVerificacionDocumento;
    }

    public void setObsVerificacionDocumento(String obsVerificacionDocumento) {
        this.obsVerificacionDocumento = obsVerificacionDocumento;
    }

    public String getCodAlmdestino() {
        return codAlmdestino;
    }

    public void setCodAlmdestino(String codAlmdestino) {
        this.codAlmdestino = codAlmdestino;
    }

    public String getDscAlmdestino() {
        return dscAlmdestino;
    }

    public void setDscAlmdestino(String dscAlmdestino) {
        this.dscAlmdestino = dscAlmdestino;
    }

    public String getCodAlmvirtual() {
        return codAlmvirtual;
    }

    public void setCodAlmvirtual(String codAlmvirtual) {
        this.codAlmvirtual = codAlmvirtual;
    }

    public String getDscAlmvirtual() {
        return dscAlmvirtual;
    }

    public void setDscAlmvirtual(String dscAlmvirtual) {
        this.dscAlmvirtual = dscAlmvirtual;
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

    public int getPeriodo_ori() {
        return periodo_ori;
    }

    public void setPeriodo_ori(int periodo_ori) {
        this.periodo_ori = periodo_ori;
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

    public String getCodDestino() {
        return codDestino;
    }

    public void setCodDestino(String codDestino) {
        this.codDestino = codDestino;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }
}
