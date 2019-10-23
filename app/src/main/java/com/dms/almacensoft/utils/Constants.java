package com.dms.almacensoft.utils;

/**
 * {@link Constants} contiene constantes utilizadas en el aplicativo.
 */

public class Constants {

    private Constants (){

    }

    // Preferencias

    public static final String PREF_BASE_URL = "baseURL";
    public static final String PREF_IS_BATCH = "isBatch";
    public static final String PREF_CONFIG = "configuracion";
    public static final String PREF_IMEI = "imei";
    public static final String PREF_RECEPCION_UBI = "recepcionUbicacion";
    public static final String PREF_DESPACHO_UBI = "despachoUbicacion";
    public static final String PREF_INVENTARIO_UBI = "inventarioUbicacion";
    public static final String PREF_GUIA_INICIADA = "guiaIniciada";
    public static final String PREF_NRO_GUIA = "nroGuia";
    public static final String PREF_GUIA_CERRADA = "guiaCerrada";
    public static final String PREF_CON_SIN_DOC = "conSinDoc";
    public static final String PREF_ETIQUETA_BLUETOOTH = "etiquetaBluetooth";
    public static final String PREF_PRINTER_ADDRESS = "printerAddress";
    public static final String PREF_FIRST_LOAD = "firstLoad";
    public static final String PREF_ULTIMA_LIMPIEZA_BD = "ultimaLimpiezaBD";

    // QR capture

    public static final String EXTRA_CODE_TYPE = "EXTRA_CODE_TYPE";


    // Intents

    public static final int INTENT_SCAN = 6;
    public static final int INTENT_IP = 1;
    public static final int INTENT_BLUETOOTH = 7;
    public static final int ENABLE_BLUETOOTH_REQUEST = 10;
    public static final int INTENT_DOCUMEMTO_PENDIENTE = 2;

    // Nombres Argumento

    public static final String RECEPCION = "R";
    public static final String DESPACHO = "D";
    public static final String PERFIL_ADM = "ADM";
    public static final String PERFIL_OPE = "OPE";


    public static final String TYPE = "type";
    public static final String BODY_DETALLE_LIST = "bodyDetalleList";
    public static final String ID_CLASE_DOCUMENTO = "idClaseDocumento";
    public static final String NUM_DOCUMENTO = "numDocumento";
    public static final String ID_PRODUCTO = "idProducto";
    public static final String DSC_PRODUCTO = "dscProducto";
    public static final String COD_PRODUCTO = "codProducto";
    public static final String ID_UBICACION = "idUbicacion";
    public static final String COD_UBICACION = "codUbicacion";
    public static final String LOTE_PRODUCTO = "loteProducto";
    public static final String BODY_DETALLE = "bodyDetalle";
    public static final String CON_SIN_DOC_SET = "conSinDocSet";
    public static final String CON_LECTURAS = "conLecturas";
    public static final String BODY_CONSULTA = "bodyConsulta";
    public static final String CON_SIN_DOC = "conSinDoc";


    public static final String FRAGMENT = "current";


}
