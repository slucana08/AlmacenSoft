package com.dms.almacensoft.data.entities.dbalmacen;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "idClaseDocumento")
public class ClaseDocumento {

    @SerializedName("ID_CLASE_DOCUMENTO")
    private int idClaseDocumento;
    @SerializedName("COD_CLASE_DOCUMENTO")
    private String codClaseDocumento;
    @SerializedName("DSC_CLASE_DOCUMENTO")
    private String dscClaseDocumento;
    @SerializedName("COD_TIPO_DOCUMENTO")
    private String codTipoDocumento;
    @SerializedName("FLG_FILTRO")
    private String flgFiltro;
    @SerializedName("FLG_TRANSFERENCIA")
    private String flgTransferencia;
    @SerializedName("FLG_CON_SIN_DOC")
    private String flgConSinDoc;


    public int getIdClaseDocumento() {
        return idClaseDocumento;
    }

    public void setIdClaseDocumento(int idClaseDocumento) {
        this.idClaseDocumento = idClaseDocumento;
    }

    public String getCodClaseDocumento() {
        return codClaseDocumento;
    }

    public void setCodClaseDocumento(String codClaseDocumento) {
        this.codClaseDocumento = codClaseDocumento;
    }

    public String getDscClaseDocumento() {
        return dscClaseDocumento;
    }

    public void setDscClaseDocumento(String dscClaseDocumento) {
        this.dscClaseDocumento = dscClaseDocumento;
    }

    public String getCodTipoDocumento() {
        return codTipoDocumento;
    }

    public void setCodTipoDocumento(String codTipoDocumento) {
        this.codTipoDocumento = codTipoDocumento;
    }

    public String getFlgFiltro() {
        return flgFiltro;
    }

    public void setFlgFiltro(String flgFiltro) {
        this.flgFiltro = flgFiltro;
    }

    public String getFlgTransferencia() {
        return flgTransferencia;
    }

    public void setFlgTransferencia(String flgTransferencia) {
        this.flgTransferencia = flgTransferencia;
    }

    public String getFlgConSinDoc() {
        return flgConSinDoc;
    }

    public void setFlgConSinDoc(String flgConSinDoc) {
        this.flgConSinDoc = flgConSinDoc;
    }
}
