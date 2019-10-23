package com.dms.almacensoft.data.models.inventario;

import com.google.gson.annotations.SerializedName;

import butterknife.BindView;

public class BodyRegistrarInventario {

    @SerializedName("id_inventario")
    private int idInventario;
    @SerializedName("id_ubicacion")
    private int idUbicacion;
    @SerializedName("id_producto")
    private int idProducto;
    @SerializedName("lote_producto")
    private String loteProducto;
    @SerializedName("serie_producto")
    private String serieProducto;
    @SerializedName("cantidad_leida")
    private double cantidadLeida;
    @SerializedName("id_terminal")
    private String idTerminal;
    @SerializedName("cod_usuario")
    private String codUsuario;
    @SerializedName("id_almacen")
    private int idAlmacen;
    @SerializedName("espacio_lleno")
    private int espacioLleno;
    @SerializedName("cod_prod_caja")
    private String codProdCaja;
    @SerializedName("num_caja")
    private int numCaja;
    @SerializedName("cod_presentacion")
    private String codPresentacion;
    @SerializedName("ctd_presentacion")
    private String ctdPresentacion;
    private String codProducto;
    private String dscProducto;
    private String codUbicacion;

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
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

    public double getCantidadLeida() {
        return cantidadLeida;
    }

    public void setCantidadLeida(double cantidadLeida) {
        this.cantidadLeida = cantidadLeida;
    }

    public String getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(String idTerminal) {
        this.idTerminal = idTerminal;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public int getEspacioLleno() {
        return espacioLleno;
    }

    public void setEspacioLleno(int espacioLleno) {
        this.espacioLleno = espacioLleno;
    }

    public String getCodProdCaja() {
        return codProdCaja;
    }

    public void setCodProdCaja(String codProdCaja) {
        this.codProdCaja = codProdCaja;
    }

    public int getNumCaja() {
        return numCaja;
    }

    public void setNumCaja(int numCaja) {
        this.numCaja = numCaja;
    }

    public String getCodPresentacion() {
        return codPresentacion;
    }

    public void setCodPresentacion(String codPresentacion) {
        this.codPresentacion = codPresentacion;
    }

    public String getCtdPresentacion() {
        return ctdPresentacion;
    }

    public void setCtdPresentacion(String ctdPresentacion) {
        this.ctdPresentacion = ctdPresentacion;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public String getCodUbicacion() {
        return codUbicacion;
    }

    public void setCodUbicacion(String codUbicacion) {
        this.codUbicacion = codUbicacion;
    }

    public String getDscProducto() {
        return dscProducto;
    }

    public void setDscProducto(String dscProducto) {
        this.dscProducto = dscProducto;
    }
}
