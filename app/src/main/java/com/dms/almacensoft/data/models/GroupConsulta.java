package com.dms.almacensoft.data.models;

import com.dms.almacensoft.features.inventario.diferencial.DiferencialFragment;
import com.dms.almacensoft.features.inventario.lecturas.LecturaInventarioFragment;
import com.dms.almacensoft.features.recepciondespacho.lectura.LecturaFragment;

/**
 * Se utiliza a {@link GroupConsulta} como cabecera para las listas agrupadas en {@link LecturaFragment},
 * {@link LecturaInventarioFragment} y {@link DiferencialFragment}
 */

public class GroupConsulta {

    private String titulo;
    private String total;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
