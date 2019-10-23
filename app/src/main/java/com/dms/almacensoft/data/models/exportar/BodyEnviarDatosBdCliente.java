package com.dms.almacensoft.data.models.exportar;

import com.dms.almacensoft.data.entities.dbalmacen.VerificacionDocumento;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumentoOri;
import com.dms.almacensoft.data.entities.dbtransact.Documento;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BodyEnviarDatosBdCliente {

    @SerializedName("Documento")
    public List<Documento> documentoList;
    @SerializedName("Detalle_Documento_Ori")
    public List<DetalleDocumentoOri> detalleDocumentoOriList;
    @SerializedName("Verificacion_Documento")
    public List<VerificacionDocumento> verificacionDocumentoList;

    public List<Documento> getDocumentoList() {
        return documentoList;
    }

    public void setDocumentoList(List<Documento> documentoList) {
        this.documentoList = documentoList;
    }

    public List<DetalleDocumentoOri> getDetalleDocumentoOriList() {
        return detalleDocumentoOriList;
    }

    public void setDetalleDocumentoOriList(List<DetalleDocumentoOri> detalleDocumentoOriList) {
        this.detalleDocumentoOriList = detalleDocumentoOriList;
    }

    public List<VerificacionDocumento> getVerificacionDocumentoList() {
        return verificacionDocumentoList;
    }

    public void setVerificacionDocumentoList(List<VerificacionDocumento> verificacionDocumentoList) {
        this.verificacionDocumentoList = verificacionDocumentoList;
    }
}
