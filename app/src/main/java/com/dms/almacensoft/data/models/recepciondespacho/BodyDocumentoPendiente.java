package com.dms.almacensoft.data.models.recepciondespacho;

import com.google.gson.annotations.SerializedName;

public class BodyDocumentoPendiente {

    @SerializedName("cod_empresa")
    private String codEmpresa;
    @SerializedName("cod_almacen")
    private String codAlmacen;
    @SerializedName("cod_tipo_documento")
    private String codTipoDocumento;
    @SerializedName("cod_clase_documento")
    private String codClaseDocumento;
    @SerializedName("num_documento")
    private String numDocumento;
    @SerializedName("id_documento")
    private int idDocumento;
    @SerializedName("cod_usuario")
    private String codUsuario;
    @SerializedName("flg_con_sin_doc")
    private String flgConSinDoc;

    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public String getCodTipoDocumento() {
        return codTipoDocumento;
    }

    public void setCodTipoDocumento(String codTipoDocumento) {
        this.codTipoDocumento = codTipoDocumento;
    }

    public String getCodClaseDocumento() {
        return codClaseDocumento;
    }

    public void setCodClaseDocumento(String codClaseDocumento) {
        this.codClaseDocumento = codClaseDocumento;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getFlgConSinDoc() {
        return flgConSinDoc;
    }

    public void setFlgConSinDoc(String flgConSinDoc) {
        this.flgConSinDoc = flgConSinDoc;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }
}
