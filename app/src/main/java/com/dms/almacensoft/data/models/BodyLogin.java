package com.dms.almacensoft.data.models;

import com.google.gson.annotations.SerializedName;

public class BodyLogin {

    @SerializedName("cod_usuario")
    private String codUsuario;
    @SerializedName("clave_usuario")
    private String claveUsuario;

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }
}
