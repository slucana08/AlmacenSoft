package com.dms.almacensoft.data.models.impresion;

import com.google.gson.annotations.SerializedName;

public class DocumentosCerrados {

    @SerializedName("NUM_DOC_INTERNO")
    private String numDocInterno;
    @SerializedName("CONTROL_IMP")
    private String controlImp;

    public String getNumDocInterno() {
        return numDocInterno;
    }

    public void setNumDocInterno(String numDocInterno) {
        this.numDocInterno = numDocInterno;
    }

    public String getControlImp() {
        return controlImp;
    }

    public void setControlImp(String controlImp) {
        this.controlImp = controlImp;
    }
}
