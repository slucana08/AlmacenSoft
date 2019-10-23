package com.dms.almacensoft.data.models.recepciondespacho;


import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HijoConsulta implements Parcelable {

    @SerializedName("ID_DETALLE_DOCUMENTO")
    private int idDetalleDocumento;
    @SerializedName("ID_LECTURA_DOCUMENTO")
    private int idLecturaDocumento;
    @SerializedName("DSC_PRODUCTO")
    private String dscProducto;
    @SerializedName("COD_PRODUCTO")
    private String codProducto;
    @SerializedName("LOTE_PRODUCTO")
    private String loteProducto;
    @SerializedName("SERIE_PRODUCTO")
    private String serieProducto;
    @SerializedName("CTD_ASIGNADA")
    private double ctdAsignada;
    @SerializedName("NUM_DOC_INTERNO")
    private String numDocInterno;
    @SerializedName("ID_UBICACION")
    private int idUbicacion;
    @SerializedName("COD_UBICACION")
    private String codUbicacion;
    @Ignore
    @SerializedName("ESTADO")
    private String estado;
    @Ignore
    private int position;

    public HijoConsulta (){

    }

    protected HijoConsulta(Parcel in) {
        idDetalleDocumento = in.readInt();
        idLecturaDocumento = in.readInt();
        dscProducto = in.readString();
        codProducto = in.readString();
        loteProducto = in.readString();
        serieProducto = in.readString();
        ctdAsignada = in.readDouble();
        numDocInterno = in.readString();
        idUbicacion = in.readInt();
        codUbicacion = in.readString();
        estado = in.readString();
        position = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idDetalleDocumento);
        dest.writeInt(idLecturaDocumento);
        dest.writeString(dscProducto);
        dest.writeString(codProducto);
        dest.writeString(loteProducto);
        dest.writeString(serieProducto);
        dest.writeDouble(ctdAsignada);
        dest.writeString(numDocInterno);
        dest.writeInt(idUbicacion);
        dest.writeString(codUbicacion);
        dest.writeString(estado);
        dest.writeInt(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HijoConsulta> CREATOR = new Creator<HijoConsulta>() {
        @Override
        public HijoConsulta createFromParcel(Parcel in) {
            return new HijoConsulta(in);
        }

        @Override
        public HijoConsulta[] newArray(int size) {
            return new HijoConsulta[size];
        }
    };

    public int getIdDetalleDocumento() {
        return idDetalleDocumento;
    }

    public void setIdDetalleDocumento(int idDetalleDocumento) {
        this.idDetalleDocumento = idDetalleDocumento;
    }

    public int getIdLecturaDocumento() {
        return idLecturaDocumento;
    }

    public void setIdLecturaDocumento(int idLecturaDocumento) {
        this.idLecturaDocumento = idLecturaDocumento;
    }

    public String getDscProducto() {
        return dscProducto;
    }

    public void setDscProducto(String dscProducto) {
        this.dscProducto = dscProducto;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public String getLoteProducto() {
        return loteProducto;
    }

    public void setLoteProducto(String loteProducto) {
        this.loteProducto = loteProducto;
    }

    public String getSerieProducto() {
        return serieProducto;
    }

    public void setSerieProducto(String serieProducto) {
        this.serieProducto = serieProducto;
    }

    public double getCtdAsignada() {
        return ctdAsignada;
    }

    public void setCtdAsignada(double ctdAsignada) {
        this.ctdAsignada = ctdAsignada;
    }

    public String getNumDocInterno() {
        return numDocInterno;
    }

    public void setNumDocInterno(String numDocInterno) {
        this.numDocInterno = numDocInterno;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getCodUbicacion() {
        return codUbicacion;
    }

    public void setCodUbicacion(String codUbicacion) {
        this.codUbicacion = codUbicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }



}
