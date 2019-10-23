package com.dms.almacensoft.data.models;

import com.google.gson.annotations.SerializedName;

public class Configuracion {

    private int idAlmacen;
    private String dscAlmacen;
    private String codAlmacen;
    private String codEmpresa;
    private int idEmpresa;
    @SerializedName("modo_lectura")
    private boolean modoLectura;
    @SerializedName("lote")
    private boolean lote;
    @SerializedName("serie")
    private boolean serie;
    @SerializedName("tipo_conexion")
    private boolean tipoConexion;
    @SerializedName("habilitar_scanner")
    private boolean habilitarScanner;
    @SerializedName("activar_impresora")
    private boolean activarImpresora;
    @SerializedName("tipo_impresora")
    private boolean tipoImpresora; // false en red // true con bluetooth
    @SerializedName("nombre_impresora")
    private String nombreImpresora;
    private boolean impresionRegistros;
    private boolean codigoProductoImpresion;
    private boolean loteImpresion;
    private boolean serieImpresion;
    @SerializedName("atencion_sin_documento")
    private boolean atencionSinDocumento;
    private String zonaRecepcion;
    private String zonaDespacho;
    private String zonaInventario;
    @SerializedName("mod_recepcion")
    private boolean modRecepcion;
    @SerializedName("mod_despacho")
    private boolean modDespacho;
    @SerializedName("todo_documento")
    private boolean todoDocumento;
    @SerializedName("solo_numero")
    private boolean soloNumero;
    @SerializedName("cod_usuario")
    private String codUsuario;
    private String idUsuario;
    private int perfilTipo; // 0 - ADM // 1 - OPE

    public Configuracion() {
    }

    public boolean getLote() {
        return lote;
    }

    public void setLote(boolean lote) {
        this.lote = lote;
    }

    public boolean getSerie() {
        return serie;
    }

    public void setSerie(boolean serie) {
        this.serie = serie;
    }

    public boolean getTipoConexion() {
        return tipoConexion;
    }

    public void setTipoConexion(boolean tipoConexion) {
        this.tipoConexion = tipoConexion;
    }

    public boolean getHabilitarScanner() {
        return habilitarScanner;
    }

    public void setHabilitarScanner(boolean habilitarScanner) {
        this.habilitarScanner = habilitarScanner;
    }

    public boolean getTipoImpresora() {
        return tipoImpresora;
    }

    public void setTipoImpresora(boolean tipoImpresora) {
        this.tipoImpresora = tipoImpresora;
    }

    public boolean getModoLectura() {
        return modoLectura;
    }

    public void setModoLectura(boolean modoLectura) {
        this.modoLectura = modoLectura;
    }

    public String getNombreImpresora() {
        return nombreImpresora;
    }

    public void setNombreImpresora(String nombreImpresora) {
        this.nombreImpresora = nombreImpresora;
    }

    public boolean getAtencionSinDocumento() {
        return atencionSinDocumento;
    }

    public void setAtencionSinDocumento(boolean atencionSinDocumento) {
        this.atencionSinDocumento = atencionSinDocumento;
    }

    public boolean getModRecepcion() {
        return modRecepcion;
    }

    public void setModRecepcion(boolean modRecepcion) {
        this.modRecepcion = modRecepcion;
    }

    public boolean getModDespacho() {
        return modDespacho;
    }

    public void setModDespacho(boolean modDespacho) {
        this.modDespacho = modDespacho;
    }

    public boolean getTodoDocumento() {
        return todoDocumento;
    }

    public void setTodoDocumento(boolean todoDocumento) {
        this.todoDocumento = todoDocumento;
    }

    public boolean getSoloNumero() {
        return soloNumero;
    }

    public void setSoloNumero(boolean soloNumero) {
        this.soloNumero = soloNumero;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public boolean getActivarImpresora() {
        return activarImpresora;
    }

    public void setActivarImpresora(boolean activarImpresora) {
        this.activarImpresora = activarImpresora;
    }

    public boolean getImpresionRegistros() {
        return impresionRegistros;
    }

    public void setImpresionRegistros(boolean impresionRegistros) {
        this.impresionRegistros = impresionRegistros;
    }

    public boolean getCodigoProductoImpresion() {
        return codigoProductoImpresion;
    }

    public void setCodigoProductoImpresion(boolean codigoProductoImpresion) {
        this.codigoProductoImpresion = codigoProductoImpresion;
    }

    public boolean getLoteImpresion() {
        return loteImpresion;
    }

    public void setLoteImpresion(boolean loteImpresion) {
        this.loteImpresion = loteImpresion;
    }

    public boolean getSerieImpresion() {
        return serieImpresion;
    }

    public void setSerieImpresion(boolean serieImpresion) {
        this.serieImpresion = serieImpresion;
    }

    public String getZonaRecepcion() {
        return zonaRecepcion;
    }

    public void setZonaRecepcion(String zonaRecepcion) {
        this.zonaRecepcion = zonaRecepcion;
    }

    public String getZonaDespacho() {
        return zonaDespacho;
    }

    public void setZonaDespacho(String zonaDespacho) {
        this.zonaDespacho = zonaDespacho;
    }

    public String getZonaInventario() {
        return zonaInventario;
    }

    public void setZonaInventario(String zonaInventario) {
        this.zonaInventario = zonaInventario;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getDscAlmacen() {
        return dscAlmacen;
    }

    public void setDscAlmacen(String dscAlmacen) {
        this.dscAlmacen = dscAlmacen;
    }

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public int getPerfilTipo() {
        return perfilTipo;
    }

    public void setPerfilTipo(int perfilTipo) {
        this.perfilTipo = perfilTipo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
}
