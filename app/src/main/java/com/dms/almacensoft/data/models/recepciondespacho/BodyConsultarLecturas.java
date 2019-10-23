package com.dms.almacensoft.data.models.recepciondespacho;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BodyConsultarLecturas implements Parcelable {

    @SerializedName("cod_tipo_documento")
    private String codTipodocumento;
    @SerializedName("id_clase_documento")
    private int idClaseDocumento;
    @SerializedName("num_documento")
    private String numDocumento;
    @SerializedName("cod_usuario")
    private String codUsuario;

    public BodyConsultarLecturas (){

    }

    protected BodyConsultarLecturas(Parcel in) {
        codTipodocumento = in.readString();
        idClaseDocumento = in.readInt();
        numDocumento = in.readString();
        codUsuario = in.readString();
    }

    public static final Creator<BodyConsultarLecturas> CREATOR = new Creator<BodyConsultarLecturas>() {
        @Override
        public BodyConsultarLecturas createFromParcel(Parcel in) {
            return new BodyConsultarLecturas(in);
        }

        @Override
        public BodyConsultarLecturas[] newArray(int size) {
            return new BodyConsultarLecturas[size];
        }
    };

    public String getCodTipodocumento() {
        return codTipodocumento;
    }

    public void setCodTipodocumento(String codTipodocumento) {
        this.codTipodocumento = codTipodocumento;
    }

    public int getIdClaseDocumento() {
        return idClaseDocumento;
    }

    public void setIdClaseDocumento(int idClaseDocumento) {
        this.idClaseDocumento = idClaseDocumento;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(codTipodocumento);
        dest.writeInt(idClaseDocumento);
        dest.writeString(numDocumento);
        dest.writeString(codUsuario);
    }
}
