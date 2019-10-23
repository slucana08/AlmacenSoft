package com.dms.almacensoft.data.entities.dbtransact;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

@Entity(primaryKeys = "ID_ENLACE_DOCUMENTO")
public class EnlaceDocumento {

    @ColumnInfo(name = "ID_ENLACE_DOCUMENTO")
    private int idEnlaceDocumento;
    @ColumnInfo (name = "ID_DOCUMENTO")
    private int idDocumento;
    @ColumnInfo (name = "ID_VERIFICACION_DOCUMENTO")
    private int idVerificacionDocumento;

    public int getIdEnlaceDocumento() {
        return idEnlaceDocumento;
    }

    public void setIdEnlaceDocumento(int idEnlaceDocumento) {
        this.idEnlaceDocumento = idEnlaceDocumento;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public int getIdVerificacionDocumento() {
        return idVerificacionDocumento;
    }

    public void setIdVerificacionDocumento(int idVerificacionDocumento) {
        this.idVerificacionDocumento = idVerificacionDocumento;
    }
}
