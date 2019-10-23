package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity (primaryKeys = {"idUsuario","idAlmacen"})
public class UsuarioxAlmacen {

    @SerializedName("ID_USUARIO")
    private int idUsuario;
    @SerializedName("ID_ALMACEN")
    private int idAlmacen;


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }
}
