package com.dms.almacensoft.data.models.exportar;

import com.dms.almacensoft.data.entities.dbalmacen.Inventario;
import com.dms.almacensoft.data.entities.dbtransact.LecturaInventario;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BodyInventario {

    @SerializedName("Inventario")
    public List<Inventario> inventarioList;
    @SerializedName("Lectura_Inventario")
    public List<LecturaInventario> lecturaInventarioList;

    public List<Inventario> getInventarioList() {
        return inventarioList;
    }

    public void setInventarioList(List<Inventario> inventarioList) {
        this.inventarioList = inventarioList;
    }

    public List<LecturaInventario> getLecturaInventarioList() {
        return lecturaInventarioList;
    }

    public void setLecturaInventarioList(List<LecturaInventario> lecturaInventarioList) {
        this.lecturaInventarioList = lecturaInventarioList;
    }
}
