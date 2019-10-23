package com.dms.almacensoft.data.models.recepciondespacho;

import com.google.gson.annotations.SerializedName;

public class BodyCancelarDocumento {

    @SerializedName("cod_estado")
    private String codEstado;
    @SerializedName("observacion")
    private String observacion;
    @SerializedName("cod_usuario")
    private String codUsuario;

    public BodyCancelarDocumento (String codUsuario){
        this.codEstado = "E";
        this.observacion = "Cancelado por el usuario";
        this.codUsuario = codUsuario;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }
}
