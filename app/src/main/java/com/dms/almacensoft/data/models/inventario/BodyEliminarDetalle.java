package com.dms.almacensoft.data.models.inventario;

import com.google.gson.annotations.SerializedName;

public class BodyEliminarDetalle {

    @SerializedName("cod_usuario")
    private String codUsuario;
    @SerializedName("flg_todo")
    private int flgTodo; // enviar 1 por ahora
    @SerializedName("num_caja")
    private int numCaja;

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public int getFlgTodo() {
        return flgTodo;
    }

    public void setFlgTodo(int flgTodo) {
        this.flgTodo = flgTodo;
    }

    public int getNumCaja() {
        return numCaja;
    }

    public void setNumCaja(int numCaja) {
        this.numCaja = numCaja;
    }
}
