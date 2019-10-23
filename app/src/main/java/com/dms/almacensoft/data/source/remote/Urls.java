package com.dms.almacensoft.data.source.remote;

public class Urls {

    private Urls() {

    }

    static final String PATH_ID = "id";
    static final String EXTRA_PATH_ID = "/{" + PATH_ID + "}";

    static final String PATH_ID_2 = "id2";
    static final String EXTRA_PATH_ID_2 = "/{" + PATH_ID_2 + "}";

    static final String PATH_ID_3 = "id3";
    static final String EXTRA_PATH_ID_3 = "/{" + PATH_ID_3 + "}";

    public static final String CARGA_MAESTRO = "CargaMaestro/";
    public static final String CARGA_EMPRESA = CARGA_MAESTRO + "Empresa";
    public static final String CARGA_USUARIOS = CARGA_MAESTRO + "Usuarios";
    public static final String CARGA_USUARIOSXALMACEN = CARGA_MAESTRO + "UsuarioxAlmacen";
    public static final String CARGA_ALMACEN = CARGA_MAESTRO + "Almacen";
    public static final String CARGA_UBICACIONES = CARGA_MAESTRO + "Ubicaciones";
    public static final String CARGA_CLASE_DOCUMENTOS = CARGA_MAESTRO + "ClaseDocumentos";
    public static final String CARGA_PRODUCTO = CARGA_MAESTRO + "Producto" +  EXTRA_PATH_ID + EXTRA_PATH_ID_3;
    public static final String CANTIDAD_PRODUCTO = CARGA_MAESTRO + "producto/cantidad";
    public static final String CARGA_ALMACEN_VIRTUAL = CARGA_MAESTRO + "AlmacenVirtual";
    public static final String CARGA_JERARQUIA_CLASIFICACION = CARGA_MAESTRO + "JerarquiaClas";
    public static final String CARGA_CLASIFICACION = CARGA_MAESTRO + "Clasificacion";
    public static final String CARGA_DESTINOS = CARGA_MAESTRO + "Destinos";
    public static final String CARGA_CLIENTES = CARGA_MAESTRO + "Clientes";
    public static final String CARGA_VEHICULOS = CARGA_MAESTRO + "Vehiculos";
    public static final String CARGA_ZONA = CARGA_MAESTRO + "Zona";
    public static final String CARGA_ISLA = CARGA_MAESTRO + "Isla";
    public static final String CARGA_PRODUCTO_UBICACION = CARGA_MAESTRO + "ProductoUbicacion";
    public static final String CARGA_CAJA_PRODUCTO = CARGA_MAESTRO + "CajaProducto";
    public static final String CARGA_UNIDAD_MEDIDA = CARGA_MAESTRO + "UnidadMedida";
    public static final String CARGA_PRESENTACION = CARGA_MAESTRO + "Presentacion";
    public static final String CARGA_STOCK_ALMACEN = CARGA_MAESTRO + "StockAlmacen";

    // Procesos

    public static final String USUARIO_LOGIN = "usuario/validarlogin";

    public static final String CARGA_CONFIGURACIONES = "configuracion/usuario" + EXTRA_PATH_ID;

    public static final String GUARDA_CONFIGURACIONES_EXISTE = "configuracion" + EXTRA_PATH_ID;

    public static final String GUARDA_CONFIGURACIONES = "configuracion";

    public static final String CARGA_ALMACEN_LINEA = "Almacen/usuario" + EXTRA_PATH_ID;

    public static final String VERIFICAR_LICENCIA = "asociado/verificarimei" + EXTRA_PATH_ID;

    public static final String REGISTRAR_LICENCIA = "asociado/asignarterminal";

    public static final String BLOQUEAR_DOCUMENTO = "documento/bloqueardocumento" + EXTRA_PATH_ID;

    // Recepcion - Despacho

    public static final String OBTENER_CLASE_DOCUMENTO = "clasedocumento/obtenerclassdoc";

    public static final String OBTENER_DOCUMENTOS_PENDIENTES = "detalledocumento/obtenerdocpendientes";

    public static final String OBTENER_DETALLE_DOCUMENTO = "detalledocumento/obtenerdetalledocpendiente";

    public static final String OBTENER_UBICACION = "ubicacion/obtenerubicacion";

    public static final String REGISTRAR_LECTURAS = "lecturaDocumento/registrar";

    public static final String CONSULTAR_LECTURAS = "lecturadocumento/consultar/lecturarecepcionacumulado";

    public static final String ELIMINAR_LECTURAS = "lecturadocumento/eliminar/lecturarecepcion";

    public static final String CERRAR_DOCUMENTO_INTERNO = "cerrardocumento";

    public static final String OBTENER_SERIE = "lecturadocumento/buscarserie";

    public static final String CREAR_DOCUMENTO = "documento/crear";

    public static final String CANCELAR_DOCUMENTO = "documento" + EXTRA_PATH_ID + "/actualizar";

    // Inventario

    public static final String OBTENER_INVENTARIO_ABIERTO = "inventario/getinventarioabierto/almacen" + EXTRA_PATH_ID;

    public static final String OBTENER_PRODUCTO = "producto" + EXTRA_PATH_ID;

    public static final String OBTENER_CANTIDAD_LECTURAS = "lecturainventario/count";

    public static final String REGISTRAR_LECTURA_INVENTARIO = "lecturainventario/registrar";

    public static final String CONSULTAR_LECTURA_INVENTARIO = "lecturainventario/consultar";

    public static final String ELIMINAR_LECTURA_INVENTARIO = "lecturainventario" + EXTRA_PATH_ID + "/Eliminar";

    public static final String ELIMINAR_DETALLE_INVENTARIO = "lecturainventario/detalleinventario" + EXTRA_PATH_ID + "/eliminar";

    public static final String ELIMINAR_LECTURAS_TOTAL_INVENTARIO = "lecturainventario/inventario" + EXTRA_PATH_ID + "/eliminartotal";

    public static final String OBTENER_SERIE_INVENTARIO = "inventario" + EXTRA_PATH_ID + "/getserieinventario";

    public static final String OBTENER_DIFERENCIAL = "inventario" + EXTRA_PATH_ID + "/reportediferenciales";

    public static final String CARGA_DETALLE_INVENTARIO = "inventario/getdetalleinventarioabierto";

    // Impresion

    public static final String OBTENER_DOCUMENTOS_CERRADOS = "impresion/consultar/documentoscerrados";

    public static final String OBTENER_DETALLE_IMPRESION = "impresion/documento/" + EXTRA_PATH_ID;

    public static final String IMPRIMIR_ETIQUETA = "impresion/etiquetarecepcion";

    public static final String ACTUALIZAR_IMPRESION = "impresion/actualizar";

    public static final String OBTENER_ETIQUETA_BLUETOOTH = "etiqueta/recepcion";

    // EXPORTACION DE TABLAS

    public static final String EXPORTAR_DOCUMENTO = "exportar/actualizardocumento";
    public static final String EXPORTAR_DETALLE_DOCUMENTO_ORI = "exportar/actualizardetalledocumentoori";
    public static final String EXPORTAR_DETALLE_DOCUMENTO = "exportar/actualizardetalledocumento";
    public static final String EXPORTAR_MOVIMIENTO = "exportar/actualizarmovimientointerno";
    public static final String EXPORTAR_INVENTARIO = "exportar/actualizarinventario";

    public static final String ENVIAR_DATOS_BD_CLIENTE = "exportar/enviardatosbdcliente" + EXTRA_PATH_ID + EXTRA_PATH_ID_2;
}
