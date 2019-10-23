package com.dms.almacensoft.data.models.inventario;

import com.google.gson.annotations.SerializedName;

public class BodyConsultaLecturaInventario {

    @SerializedName("id_inventario")
    private int idInventario;
    @SerializedName("cod_usuario")
    private String codUsuario;
    @SerializedName("tipo_consulta")
    private int tipoConsulta;  // 0 - Ubicacion // 1 - Codigo Producto // 2 - Lote // 3 - Serie
    @SerializedName("dato")
    private String dato;

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public int getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(int tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }
}

