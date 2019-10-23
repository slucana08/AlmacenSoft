package com.dms.almacensoft.data.models.exportar;

import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumento;
import com.dms.almacensoft.data.entities.dbtransact.Documento;
import com.dms.almacensoft.data.entities.dbtransact.LecturaDocumento;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BodyDetalleDocumento {

    @SerializedName("Documento")
    public List<Documento> documentoList;
    @SerializedName("Detalle_Documento")
    public List<DetalleDocumento> detalleDocumentoList;
    @SerializedName("Lectura_Documento")
    public List<LecturaDocumento> lecturaDocumentoList;

    public List<Documento> getDocumentoList() {
        return documentoList;
    }

    public void setDocumentoList(List<Documento> documentoList) {
        this.documentoList = documentoList;
    }

    public List<DetalleDocumento> getDetalleDocumentoList() {
        return detalleDocumentoList;
    }

    public void setDetalleDocumentoList(List<DetalleDocumento> detalleDocumentoList) {
        this.detalleDocumentoList = detalleDocumentoList;
    }

    public List<LecturaDocumento> getLecturaDocumentoList() {
        return lecturaDocumentoList;
    }

    public void setLecturaDocumentoList(List<LecturaDocumento> lecturaDocumentoList) {
        this.lecturaDocumentoList = lecturaDocumentoList;
    }
}
