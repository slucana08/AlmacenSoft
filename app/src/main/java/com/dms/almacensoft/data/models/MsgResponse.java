package com.dms.almacensoft.data.models;

import com.google.gson.annotations.SerializedName;

public class MsgResponse {

    @SerializedName("cod_error")
    private int codError;
    @SerializedName("msj_error")
    private String msgError;
    @SerializedName("modulo")
    private String modulo;

    public int getCodError() {
        return codError;
    }

    public void setCodError(int codError) {
        this.codError = codError;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }
}
