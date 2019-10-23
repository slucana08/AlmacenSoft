package com.dms.almacensoft.data.models.inventario;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HijoConsultaInventario implements Parcelable {

    @SerializedName("ID_LECTURA_INVENTARIO")
    private int idLecturaInventario;
    @SerializedName("ID_INVENTARIO")
    private int idInventario;
    @SerializedName("ID_DETALLE_INVENTARIO")
    private int idDetalleInventario;
    @SerializedName("ID_UBICACION")
    private int idUbicacion;
    @SerializedName("COD_UBICACION")
    private String codUbicacion;
    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("COD_PRODUCTO")
    private String codProducto;
    @SerializedName("DSC_PRODUCTO")
    private String dscProducto;
    @SerializedName("LOTE_PRODUCTO")
    private String loteProducto;
    @SerializedName("SERIE_PRODUCTO")
    private String serieProducto;
    @SerializedName("STOCK_INVENTARIADO")
    private double stockInventariado;

    public HijoConsultaInventario (){

    }

    protected HijoConsultaInventario(Parcel in) {
        idLecturaInventario = in.readInt();
        idInventario = in.readInt();
        idDetalleInventario = in.readInt();
        idUbicacion = in.readInt();
        codUbicacion = in.readString();
        idProducto = in.readInt();
        codProducto = in.readString();
        dscProducto = in.readString();
        loteProducto = in.readString();
        serieProducto = in.readString();
        stockInventariado = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idLecturaInventario);
        dest.writeInt(idInventario);
        dest.writeInt(idDetalleInventario);
        dest.writeInt(idUbicacion);
        dest.writeString(codUbicacion);
        dest.writeInt(idProducto);
        dest.writeString(codProducto);
        dest.writeString(dscProducto);
        dest.writeString(loteProducto);
        dest.writeString(serieProducto);
        dest.writeDouble(stockInventariado);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HijoConsultaInventario> CREATOR = new Creator<HijoConsultaInventario>() {
        @Override
        public HijoConsultaInventario createFromParcel(Parcel in) {
            return new HijoConsultaInventario(in);
        }

        @Override
        public HijoConsultaInventario[] newArray(int size) {
            return new HijoConsultaInventario[size];
        }
    };

    public int getIdLecturaInventario() {
        return idLecturaInventario;
    }

    public void setIdLecturaInventario(int idLecturaInventario) {
        this.idLecturaInventario = idLecturaInventario;
    }

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public int getIdDetalleInventario() {
        return idDetalleInventario;
    }

    public void setIdDetalleInventario(int idDetalleInventario) {
        this.idDetalleInventario = idDetalleInventario;
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

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public String getDscProducto() {
        return dscProducto;
    }

    public void setDscProducto(String dscProducto) {
        this.dscProducto = dscProducto;
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

    public double getStockInventariado() {
        return stockInventariado;
    }

    public void setStockInventariado(double stockInventariado) {
        this.stockInventariado = stockInventariado;
    }


}
