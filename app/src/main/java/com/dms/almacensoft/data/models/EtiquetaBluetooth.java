package com.dms.almacensoft.data.models;

import com.google.gson.annotations.SerializedName;

public class EtiquetaBluetooth {

    @SerializedName("status")
    private boolean status;
    @SerializedName("mensaje")
    private String mensaje;
    @SerializedName("valor")
    private String valor;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
