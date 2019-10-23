package com.dms.almacensoft.data.entities.dbtransact;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Entity
public class ProductoDiferencial implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int idProductoDiferencial;
    @SerializedName("ID_PRODUCTO")
    private int idProducto;
    @SerializedName("COD_PRODUCTO")
    private String codProducto;
    @SerializedName("DSC_PRODUCTO")
    private String dscProducto;
    @SerializedName("COD_UBICACION")
    private String codUbicacion;
    @SerializedName("ID_UBICACION")
    private int idUbicacion;
    @SerializedName("LOTE_PRODUCTO")
    private String loteProducto;
    @SerializedName("STOCK_ANTERIOR")
    private int StockAnterior;

    public ProductoDiferencial(){

    }

    protected ProductoDiferencial(Parcel in) {
        idProductoDiferencial = in.readInt();
        idProducto = in.readInt();
        codProducto = in.readString();
        dscProducto = in.readString();
        codUbicacion = in.readString();
        idUbicacion = in.readInt();
        loteProducto = in.readString();
        StockAnterior = in.readInt();
    }

    public static final Creator<ProductoDiferencial> CREATOR = new Creator<ProductoDiferencial>() {
        @Override
        public ProductoDiferencial createFromParcel(Parcel in) {
            return new ProductoDiferencial(in);
        }

        @Override
        public ProductoDiferencial[] newArray(int size) {
            return new ProductoDiferencial[size];
        }
    };

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

    public String getCodUbicacion() {
        return codUbicacion;
    }

    public void setCodUbicacion(String codUbicacion) {
        this.codUbicacion = codUbicacion;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getLoteProducto() {
        return loteProducto;
    }

    public void setLoteProducto(String loteProducto) {
        this.loteProducto = loteProducto;
    }

    public int getStockAnterior() {
        return StockAnterior;
    }

    public void setStockAnterior(int stockAnterior) {
        StockAnterior = stockAnterior;
    }

    public int getIdProductoDiferencial() {
        return idProductoDiferencial;
    }

    public void setIdProductoDiferencial(int idProductoDiferencial) {
        this.idProductoDiferencial = idProductoDiferencial;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idProductoDiferencial);
        dest.writeInt(idProducto);
        dest.writeString(codProducto);
        dest.writeString(dscProducto);
        dest.writeString(codUbicacion);
        dest.writeInt(idUbicacion);
        dest.writeString(loteProducto);
        dest.writeInt(StockAnterior);
    }
}
