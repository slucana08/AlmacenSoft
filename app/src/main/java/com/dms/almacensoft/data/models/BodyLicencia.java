package com.dms.almacensoft.data.models;

import com.google.gson.annotations.SerializedName;

public class BodyLicencia {

    @SerializedName("cod_asociado")
    private String codAsociado;
    @SerializedName("comentario")
    private String comentario;

    public String getCodAsociado() {
        return codAsociado;
    }

    public void setCodAsociado(String codAsociado) {
        this.codAsociado = codAsociado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
