package com.dms.almacensoft.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.dms.almacensoft.data.entities.dbalmacen.Ubicacion;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.injection.annotations.ApplicationScope;
import com.dms.almacensoft.utils.Constants;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

@ApplicationScope
public class PreferenceManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Inject
    public PreferenceManager(Context context){
        prefs = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public boolean getFirstLoad(){
        boolean firstLoad = true;
        if (prefs.contains(Constants.PREF_FIRST_LOAD)) firstLoad = prefs.getBoolean(Constants.PREF_FIRST_LOAD,true);
        return firstLoad;
    }

    public void setFirstLoad(boolean firstLoad){
        editor.putBoolean(Constants.PREF_FIRST_LOAD,firstLoad).commit();
    }

    public long getUltimaLimpiezaBD(){
        long ultimaLimpiezaBD = 0L;
        if (prefs.contains(Constants.PREF_ULTIMA_LIMPIEZA_BD)) ultimaLimpiezaBD = prefs.getLong(Constants.PREF_ULTIMA_LIMPIEZA_BD,0L);
        return ultimaLimpiezaBD;
    }

    public void setUltimaLimpiezaBD(long ultimaLimpiezaBD){
        editor.putLong(Constants.PREF_ULTIMA_LIMPIEZA_BD,ultimaLimpiezaBD).commit();
    }

    public String getBaseURL() {
        String baseURL = "http://172.16.4.4/WSAlmacenSoftAndroid/";
        if (prefs.contains(Constants.PREF_BASE_URL)) baseURL = prefs.getString(Constants.PREF_BASE_URL,"");
        return baseURL;
    }

    public long getTimeOut(){
        return 60000000L;
    }

    public void saveBaseURL(String baseURL){
        editor.putString(Constants.PREF_BASE_URL,baseURL).commit();
    }

    public void saveConfig(Configuracion configuracion) {
        String configString = new Gson().toJson(configuracion, Configuracion.class);
        editor.putString(Constants.PREF_CONFIG, configString).commit();
    }

    public Configuracion getConfig() {
        String configString = prefs.getString(Constants.PREF_CONFIG, "");
        if (TextUtils.isEmpty(configString)) {
            Configuracion config = new Configuracion();
            saveConfig(config);
            return config;
        } else {
            return new Gson().fromJson(configString, Configuracion.class);
        }
    }

    public String getImei(){
        String imei = "";
        if (prefs.contains(Constants.PREF_IMEI)) imei = prefs.getString(Constants.PREF_IMEI, "");
        return imei;
    }

    public void setImei(String imei){
        editor.putString(Constants.PREF_IMEI, imei).commit();
    }

    public void saveRecepcionUbi(Ubicacion ubicacion) {
        String configString = new Gson().toJson(ubicacion, Ubicacion.class);
        editor.putString(Constants.PREF_RECEPCION_UBI, configString).commit();
    }

    public Ubicacion getRecepcionUbi() {
        String ubicacionString = prefs.getString(Constants.PREF_RECEPCION_UBI, "");
        if (TextUtils.isEmpty(ubicacionString)) {
            Ubicacion ubicacion = new Ubicacion();
            saveRecepcionUbi(ubicacion);
            return ubicacion;
        } else {
            return new Gson().fromJson(ubicacionString, Ubicacion.class);
        }
    }

    public void saveDespachoUbi(Ubicacion ubicacion) {
        String configString = new Gson().toJson(ubicacion, Ubicacion.class);
        editor.putString(Constants.PREF_DESPACHO_UBI, configString).commit();
    }

    public Ubicacion getDespachoUbi() {
        String ubicacionString = prefs.getString(Constants.PREF_DESPACHO_UBI, "");
        if (TextUtils.isEmpty(ubicacionString)) {
            Ubicacion ubicacion = new Ubicacion();
            saveDespachoUbi(ubicacion);
            return ubicacion;
        } else {
            return new Gson().fromJson(ubicacionString, Ubicacion.class);
        }
    }
    public void saveInventarioUbi(Ubicacion ubicacion) {
        String configString = new Gson().toJson(ubicacion, Ubicacion.class);
        editor.putString(Constants.PREF_INVENTARIO_UBI, configString).commit();
    }

    public Ubicacion getInventarioUbi() {
        String ubicacionString = prefs.getString(Constants.PREF_INVENTARIO_UBI, "");
        if (TextUtils.isEmpty(ubicacionString)) {
            Ubicacion ubicacion = new Ubicacion();
            saveInventarioUbi(ubicacion);
            return ubicacion;
        } else {
            return new Gson().fromJson(ubicacionString, Ubicacion.class);
        }
    }

    public boolean getGuiaIniciada(){
        boolean guiaIniciada = false;
        if (prefs.contains(Constants.PREF_GUIA_INICIADA)) guiaIniciada = prefs.getBoolean(Constants.PREF_GUIA_INICIADA, false);
        return guiaIniciada;
    }

    public void setGuiaIniciada(boolean guiaIniciada){
        editor.putBoolean(Constants.PREF_GUIA_INICIADA, guiaIniciada).commit();
    }

    public String getNroGuia(){
        String nroGuia = "";
        if (prefs.contains(Constants.PREF_NRO_GUIA)) nroGuia = prefs.getString(Constants.PREF_NRO_GUIA, "");
        return nroGuia;
    }

    public void setNroGuia(String nroGuia){
        editor.putString(Constants.PREF_NRO_GUIA, nroGuia).commit();
    }

    public boolean getGuiaCerrada(){
        boolean guiaCerrada = true;
        if (prefs.contains(Constants.PREF_GUIA_CERRADA)) guiaCerrada = prefs.getBoolean(Constants.PREF_GUIA_CERRADA, true);
        return guiaCerrada;
    }

    public void setGuiaCerrada(boolean guiaCerrada){
        editor.putBoolean(Constants.PREF_GUIA_CERRADA, guiaCerrada).commit();
    }

    public void setConSinDoc(int conSinDoc){
        editor.putInt(Constants.PREF_CON_SIN_DOC, conSinDoc).commit(); // 0 - no asignado // 1 - con Doc // - 2 sin Doc
    }

    public int getConSinDoc(){
        int conSindDoc = 0;
        if (prefs.contains(Constants.PREF_CON_SIN_DOC)) conSindDoc = prefs.getInt(Constants.PREF_CON_SIN_DOC,0);
        return conSindDoc;
    }

    public void setEtiquetaBluetooth(String etiquetaBluetooth){
        editor.putString(Constants.PREF_ETIQUETA_BLUETOOTH, etiquetaBluetooth).commit();
    }

    public String getEtiquetaBluetooth(){
        String etiquetaBluetooth = "";
        if (prefs.contains(Constants.PREF_ETIQUETA_BLUETOOTH)) etiquetaBluetooth = prefs.getString(Constants.PREF_ETIQUETA_BLUETOOTH,"");
        return etiquetaBluetooth;
    }

    public void savePrinterAddress(String printer) {
        editor.putString(Constants.PREF_PRINTER_ADDRESS,printer).commit();
    }

    public String getPrinterAddress() {
        String printerAddress = "";
        if (prefs.contains(Constants.PREF_PRINTER_ADDRESS)) printerAddress = prefs.getString(Constants.PREF_PRINTER_ADDRESS,"");
        return printerAddress;
    }
}
