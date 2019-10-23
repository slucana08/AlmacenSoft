package com.dms.almacensoft.data.models.descargarmaestros;

import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumento;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BodyMaestro {

    @SerializedName("DetalleDocumento")
    private List<DetalleDocumento> detalleDocumentoList;

}
