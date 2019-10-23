package com.dms.almacensoft.data.models.recepciondespacho;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BodyDetalleDocumentoPendiente implements Parcelable {

    @SerializedName("cod_empresa")
    private String codEmpresa;
    @SerializedName("cod_almacen")
    private String codAlmacen;
    @SerializedName("cod_tipo_documento")
    private String codTipoDocumento;
    @SerializedName("cod_clase_documento")
    private String codClaseDocumento;
    private int idClaseDocumento;
    @SerializedName("num_documento")
    private String numDocumento;
    @SerializedName("id_documento")
    private int idDocumento;
    @SerializedName("clase_doc_filtro")
    private String claseDocFiltro;
    @SerializedName("cod_usuario")
    private String codUsuario;

    public BodyDetalleDocumentoPendiente(){

    }


    protected BodyDetalleDocumentoPendiente(Parcel in) {
        codEmpresa = in.readString();
        codAlmacen = in.readString();
        codTipoDocumento = in.readString();
        codClaseDocumento = in.readString();
        idClaseDocumento = in.readInt();
        numDocumento = in.readString();
        idDocumento = in.readInt();
        claseDocFiltro = in.readString();
        codUsuario = in.readString();
    }

    public static final Creator<BodyDetalleDocumentoPendiente> CREATOR = new Creator<BodyDetalleDocumentoPendiente>() {
        @Override
        public BodyDetalleDocumentoPendiente createFromParcel(Parcel in) {
            return new BodyDetalleDocumentoPendiente(in);
        }

        @Override
        public BodyDetalleDocumentoPendiente[] newArray(int size) {
            return new BodyDetalleDocumentoPendiente[size];
        }
    };

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

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getClaseDocFiltro() {
        return claseDocFiltro;
    }

    public void setClaseDocFiltro(String claseDocFiltro) {
        this.claseDocFiltro = claseDocFiltro;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }


    public int getIdClaseDocumento() {
        return idClaseDocumento;
    }

    public void setIdClaseDocumento(int idClaseDocumento) {
        this.idClaseDocumento = idClaseDocumento;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(codEmpresa);
        dest.writeString(codAlmacen);
        dest.writeString(codTipoDocumento);
        dest.writeString(codClaseDocumento);
        dest.writeInt(idClaseDocumento);
        dest.writeString(numDocumento);
        dest.writeInt(idDocumento);
        dest.writeString(claseDocFiltro);
        dest.writeString(codUsuario);
    }
}
