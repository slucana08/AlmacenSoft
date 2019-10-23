package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = "idUsuario")
public class Usuario {

    @SerializedName("ID_USUARIO")
    private int idUsuario;
    @SerializedName("COD_USUARIO")
    private String codUsuario;
    @SerializedName("CLAVE_USUARIO")
    private String claveUsuario;
    @SerializedName("NOMBRE_USUARIO")
    private String nombreUsuario;
    @SerializedName("PERFIL_USUARIO")
    private String perfilUsuario;
    @SerializedName("FLG_HABILITADO")
    private String flgHabilitado;


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPerfilUsuario() {
        return perfilUsuario;
    }

    public void setPerfilUsuario(String perfilUsuario) {
        this.perfilUsuario = perfilUsuario;
    }

    public String getFlgHabilitado() {
        return flgHabilitado;
    }

    public void setFlgHabilitado(String flgHabilitado) {
        this.flgHabilitado = flgHabilitado;
    }
}
