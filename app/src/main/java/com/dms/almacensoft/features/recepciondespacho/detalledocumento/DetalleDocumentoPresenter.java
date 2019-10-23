package com.dms.almacensoft.features.recepciondespacho.detalledocumento;

import android.content.Context;
import android.text.TextUtils;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumentoOri;
import com.dms.almacensoft.data.entities.dbtransact.DetalleDocumento;
import com.dms.almacensoft.data.entities.dbtransact.LecturaDocumento;
import com.dms.almacensoft.data.entities.dbtransact.MovimientoInterno;
import com.dms.almacensoft.data.models.BodyUbicacion;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.data.models.recepciondespacho.BodyBuscarSerie;
import com.dms.almacensoft.data.models.recepciondespacho.BodyCancelarDocumento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyCerrarDocumento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyConsultarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.data.models.recepciondespacho.BodyRegistrarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.DetalleDocumentoLive;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.features.configuracion.ConfiguracionActivity;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link DetalleDocumentoPresenter} realiza las llamadas al WS, base de datos interna y preferencias del equipo
 * para {@link DetalleDocumentoFragment}
 */

public class DetalleDocumentoPresenter implements DetalleDocumentoContract.Presenter {

    private DetalleDocumentoContract.View view;
    private PreferenceManager preferenceManager;
    private DataSourceRepository dataSourceRepository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Context context;

    // Contador al realizar la verificación de cantidades de detalle en el modo Batch
    private int detalleIndex = 0;
    private double cantidadAtendida = 0.0; // Se usará para obtener la cantidad pendiente
    private double cantidadAtendidaMostrar = 0.0; // se mostrará en el detalle mientras el documento interno esté abierto
    private String numDocInterno;

    // Datos guardados para llamar la consulta de detalles
    private BodyDetalleDocumentoPendiente bodySaved;

    // Contadores para reintento de conexión en error de WS
    private int getDataCount = 0;
    private int registroCount = 0;

    // Registra el módulo en el que se está trabajando actualmente
    private String moduleType;

    // Determina si se trabaja con o sin documento
    private int conSinDoc;

    // Verifica si todos los detalles de un documento se cerraron
    private boolean cerradoCheck = false;
    private boolean firstDetalleDocumentoOri = true;
    private boolean detalleRegistrado = false;

    @Inject
    public DetalleDocumentoPresenter(PreferenceManager preferenceManager, Context context,
                                     DataSourceRepository dataSourceRepository){
        this.preferenceManager = preferenceManager;
        this.context = context;
        this.dataSourceRepository = dataSourceRepository;
    }

    /**
     * Trae los detalles del documento trabajado y también las configuraciones seteadas en {@link ConfiguracionActivity}
     * @param body es la data necesaria para poder traer los detalles
     * @param type determina quien tipo de llamada recibe el método
     *             0 - Carga inicial de detalle con documento
     *             1 - Actualización de detalle luego de registro de lectura
     *             2 - Carga inicial sin documento, no trae detalles
     */
    @Override
    public void getData(BodyDetalleDocumentoPendiente body, int type, int conSinDoc) {
        this.conSinDoc = conSinDoc;
        bodySaved = body;
        boolean lote = preferenceManager.getConfig().getLote();
        boolean serie = preferenceManager.getConfig().getSerie();
        boolean barrido = preferenceManager.getConfig().getModoLectura();
        boolean impresora = preferenceManager.getConfig().getActivarImpresora();
        if (type == 2) {
            // Solo enviamos la configuración
            DetalleDocumentoPresenter.this.getView().setUpViews(lote, serie, barrido, impresora, null);
        } else {
            if (preferenceManager.getConfig().getTipoConexion()){
                // Se ejecuta al trabajar en el modo batch
                if (conSinDoc == 1) {
                    disposable.add(dataSourceRepository.getDetalleListBD(body.getIdDocumento())
                            .subscribeOn(Schedulers.single())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(detalleDocumentoLives -> {
                                if (type == 0 && !preferenceManager.getGuiaIniciada() && TextUtils.equals(Constants.RECEPCION,moduleType)) {
                                    // Mostramos los detalles obtenidos
                                    DetalleDocumentoPresenter.this.getView().setUpViews(lote, serie, barrido, impresora, detalleDocumentoLives);
                                    getDataCount = 0;
                                } else {
                                    getView().setUpViews(lote, serie, barrido, impresora, null);
                                    verificarCantidadDetalles(detalleDocumentoLives);
                                }
                            }, throwable -> {
                            }));
                } else {
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        getView().setUpViews(lote, serie, barrido, impresora, null);
                        verificarCantidadDetalles(new ArrayList<>());
                    });
                }
            } else {
                if (!UtilMethods.checkConnection(context)) {
                    getView().onError(() -> DetalleDocumentoPresenter.this.getData(body, type,conSinDoc),
                            context.getString(R.string.movil_sin_conexion), 0);
                } else {
                    // Se ejecuta en el modo en línea
                    disposable.add(dataSourceRepository.getDetalleDocumento(body)
                            .subscribeOn(Schedulers.single())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(detalleDocumentoLives -> {
                                if (type == 0) {
                                    // Mostramos los detalles obtenidos
                                    DetalleDocumentoPresenter.this.getView().setUpViews(lote, serie, barrido, impresora, detalleDocumentoLives);
                                    getDataCount = 0;
                                } else if (type == 1)
                                    // Actualizamos a lista de detalles
                                    DetalleDocumentoPresenter.this.getView().updateDetalle(detalleDocumentoLives);
                                getDataCount = 0;
                            }, throwable -> {
                                if (getDataCount < 2) {
                                    getDataCount += 1;
                                    if (type == 0)
                                        DetalleDocumentoPresenter.this.getView().setUpViews(lote, serie, barrido, impresora, null);
                                    DetalleDocumentoPresenter.this.getView().onError(() ->
                                            getData(body, type,conSinDoc), context.getString(R.string.error_WS), 2);
                                } else {
                                    getDataCount = 0;
                                    getView().onError(null, context.getString(R.string.limite_intentos), 3);
                                }
                            }));

                }
            }
        }
    }

    @Override
    public void verifyUbicacion(String codUbicacion) {
        BodyUbicacion body = new BodyUbicacion();
        body.setIdAlmacen(preferenceManager.getConfig().getIdAlmacen());
        body.setCodUbicacion(codUbicacion);
        if (preferenceManager.getConfig().getTipoConexion()){
            //Se ejecuta si está en modo Batch
            disposable.add(dataSourceRepository.verifyUbicacionBD(body.getIdAlmacen(),codUbicacion)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(ubicacions -> getView().checkUbicacion(ubicacions), throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> DetalleDocumentoPresenter.this.verifyUbicacion(codUbicacion),
                        null, 1);
            } else {
                if (TextUtils.equals(Constants.RECEPCION, moduleType))
                    body.setTipoResultado(0);
                else if (TextUtils.equals(Constants.DESPACHO, moduleType))
                    body.setTipoResultado(1);
                disposable.add(dataSourceRepository.getUbicacion(body)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(ubicacions -> getView().checkUbicacion(ubicacions),
                                throwable -> UtilMethods.showToast(context, context.getString(R.string.error_WS))));
            }
        }
    }

    /**
     * Ordena la lista de acuerdo a número de línea asignado al detalle
     * @param list es la lista a ordenar
     * @return la lista ordenada por línea de menor a mayor
     */
    @Override
    public List<DetalleDocumentoLive> orderDocument(List<DetalleDocumentoLive> list) {
        Collections.sort(list, (o1, o2) -> Integer.compare(o1.getLinea(), o2.getLinea()));
        return list;
    }

    @Override
    public void attachView(DetalleDocumentoContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        disposable.clear();
        this.view = null;
    }

    @Override
    public DetalleDocumentoContract.View getView() {
        return view;
    }

    /**
     * Realiza el registro de una lectura
     * @param body es la data necesaria para realizar el registro
     */
    @Override
    public void registrarLecturas(BodyRegistrarLecturas body) {
        BodyRegistrarLecturas bodySend = body;
        bodySend.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
        bodySend.setTerminal(preferenceManager.getImei());
        numDocInterno = body.getNumDocInterno();
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.verifyDetalleDocumentoBD(body.getLoteProducto(),body.getNumDocInterno()
                    ,body.getLinea(),body.getIdDocumento(), body.getIdProducto())
                    .subscribeOn(Schedulers.single())
                    .subscribe(count -> {
                        if (count == 0){
                            // Inserta un nuevo detalle en DetalleDocumento
                            DetalleDocumento detalle = new DetalleDocumento();
                            detalle.setIdDocumento(bodySend.getIdDocumento());
                            detalle.setIdProducto(bodySend.getIdProducto());
                            detalle.setLoteProducto(bodySend.getLoteProducto());
                            detalle.setCtdRequerida(bodySend.getCtdRequerida());
                            detalle.setCtdAtendida(bodySend.getCtdConfirmada()); // Para el primer registro
                            detalle.setCtdAtendidaCliente(0.0);
                            detalle.setFlgActivo("1");
                            detalle.setCodUsuarioRegistro(bodySend.getCodUsuario());
                            detalle.setFchRegistro(UtilMethods.formatCurrentDate(new Date()));
                            detalle.setCodUsuarioModificacion(bodySend.getCodUsuario());
                            detalle.setFchModificacion(UtilMethods.formatCurrentDate(new Date()));
                            detalle.setLinea(bodySend.getLinea());
                            detalle.setNumDocInterno(bodySend.getNumDocInterno());
                            detalle.setFlgCierreDoc("0");
                            detalle.setFlgCerrarEnvio("0");
                            detalle.setEtiquetado("0");
                            detalle.setFlgDescargado(0);
                            dataSourceRepository.insertDetalleDocumentoBD(detalle);
                        } else {
                            // Actualizamos el detalle si es que ya existe en DetalleDocumento
                            dataSourceRepository.updateDetalleDocumentoBD(bodySend.getCtdConfirmada(),
                                    bodySend.getLoteProducto(),body.getNumDocInterno(),bodySend.getLinea()
                                    ,bodySend.getIdDocumento(),bodySend.getCodUsuario(),UtilMethods.formatCurrentDate(new Date())
                                    ,bodySend.getIdProducto());
                        }
                        // Inserta la lectura en LecturaDocumento
                        Schedulers.single().scheduleDirect(() -> {
                            dataSourceRepository.updateDocumentoTrabajado(bodySend.getCodUsuario(),
                                    UtilMethods.formatCurrentDate(new Date()),bodySend.getIdDocumento());
                            DetalleDocumentoPresenter.this.verificarDetalleDocumentoBD(bodySend);
                        }, 80, TimeUnit.MILLISECONDS);
                    }, throwable -> {}));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> registrarLecturas(body), context.getString(R.string.movil_sin_conexion), 0);
            } else {
                disposable.add(dataSourceRepository.registrarLecturasOnline(body)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(msgResponse -> {
                            if (msgResponse.getMsgError().contains("registro Creado")) {
                                // Seteamos la guía a iniciada y guardamos el número de guia actual
                                saveGuia(1, body.getNumDocInterno());
                                // Verificamos el estado de la guía para que la pantalla se actualice
                                isGuiaStarted();
                                // Traemos los detalles con type = 1 para especificar actualización
                                getData(bodySaved, 1,conSinDoc);
                            } else if (msgResponse.getCodError() == 1) {
                                getView().onError(null, null, 5);
                            } else {
                                getView().onError(() -> registrarLecturas(body), context.getString(R.string.no_pudo_registrar_lectura), 2);
                            }
                            registroCount = 0;
                        }, throwable -> {
                            if (registroCount < 2) {
                                registroCount += 1;
                                getView().onError(() -> registrarLecturas(body), context.getString(R.string.no_pudo_registrar_lectura), 2);
                            } else {
                                registroCount = 0;
                                getView().onError(null, context.getString(R.string.limite_intentos), 4);
                            }
                        }));
            }
        }
    }

    /**
     * Verifica que un detalle haya sido creado en {@link DetalleDocumento} cuando se trabaja en el modo Batch
     * @param body es la data necesaria para realizar la lectura
     */
    @Override
    public void verificarDetalleDocumentoBD(BodyRegistrarLecturas body) {
        disposable.add(dataSourceRepository.verifyDetalleDocumentoBD(body.getLoteProducto(),body.getNumDocInterno()
                ,body.getLinea(),body.getIdDocumento(), body.getIdProducto())
                .subscribe(count -> {
                    if (count == 0){
                        verificarDetalleDocumentoBD(body);
                    } else {
                        registrarLecturaBD(body);
                    }
                }, throwable -> { }));
    }

    /**
     * Registra una lectura en {@link LecturaDocumento}
     * @param body es la data necesaria para realizar una lectura
     */
    @Override
    public void registrarLecturaBD(BodyRegistrarLecturas body) {
        disposable.add(dataSourceRepository.getIDDetalleDocumentoBD(body.getLoteProducto(), body.getNumDocInterno()
                , body.getLinea(), body.getIdDocumento(), body.getIdProducto())
                .subscribeOn(Schedulers.single())
                .subscribe(idDetalleDocumento -> {
                    LecturaDocumento lectura = new LecturaDocumento();
                    lectura.setIdDetalleDocumento(idDetalleDocumento);
                    lectura.setIdUbicacion(body.getIdUbicacion());
                    lectura.setIdProducto(body.getIdProducto());
                    lectura.setLoteProducto(body.getLoteProducto());
                    lectura.setSerieProducto(body.getSerieProducto());
                    lectura.setCtdAsignada(body.getCtdConfirmada());
                    lectura.setFlgActivo("1");
                    lectura.setCodUsarioRegistro(body.getCodUsuario());
                    lectura.setFchRegistro(UtilMethods.formatCurrentDate(new Date()));
                    lectura.setCodUsuarioModificacion("");
                    lectura.setFchModificacion("");
                    lectura.setIdTerminal(body.getTerminal());
                    lectura.setFlgEnviado("0");
                    lectura.setFchEnviado(UtilMethods.formatCurrentDate(new Date()));
                    lectura.setGuia(body.getNumDocInterno());
                    lectura.setFchProduccion(UtilMethods.formatCurrentDate(new Date()));
                    lectura.setCodProdCaja("");
                    lectura.setNroCaja(0);
                    dataSourceRepository.insertLecturaDocumentoBD(lectura);
                    saveGuia(1, numDocInterno);
                    Schedulers.single().scheduleDirect(() -> {
                                registrarMovimientoInterno(body,idDetalleDocumento);
                                DetalleDocumentoPresenter.this.getData(bodySaved, 1,conSinDoc);
                            },
                            80, TimeUnit.MILLISECONDS);
                }, throwable -> { }));
    }

    /**
     * Organiza la data capturada desde {@link DetalleDocumentoOri} y {@link DetalleDocumento} cuando
     * se registra una lectura en modo batch
     * Si se trabaja en modo sin documento, organiza toda la data capturada de {@link DetalleDocumento}
     * para que pueda ser mostrada en la lista de detalles de {@link DetalleDocumentoFragment}
     * @param detalleDocumentoLiveList es la data capturada de {@link DetalleDocumentoOri}
     */
    @Override
    public void verificarCantidadDetalles(List<DetalleDocumentoLive> detalleDocumentoLiveList) {
        if (detalleDocumentoLiveList.isEmpty()){
            // Cuando la lista esta vacía siginifica que se está trabajando en el modo sin documento
            // La lista será creada basándose en los detalles del documento
            List<DetalleDocumentoLive> detalleDocumentoLiveListSend = new ArrayList<>();
            disposable.add(dataSourceRepository.getDetalleDocumentoByDocumentoBD(bodySaved.getIdDocumento())
                    .subscribeOn(Schedulers.single())
                    .flatMapIterable(detalleDocumentos -> detalleDocumentos)
                    .groupBy(DetalleDocumento::getIdProducto)
                    .flatMapSingle(Observable::toList)
                    .doOnComplete(() -> {
                        getProductoByDetalle(detalleDocumentoLiveListSend);
                    })
                    .subscribe(detalleDocumentos -> {
                        DetalleDocumentoLive detalleDocumentoLive = new DetalleDocumentoLive();
                        detalleDocumentoLive.setLinea(0);
                        detalleDocumentoLive.setIdProducto(detalleDocumentos.get(0).getIdProducto());
                        detalleDocumentoLive.setIdDetalleDocumento(0);
                        detalleDocumentoLive.setIdDocumento(detalleDocumentos.get(0).getIdDocumento());
                        detalleDocumentoLive.setCtdPendiente(0);
                        detalleDocumentoLive.setCtdRequerida(0);
                        for (DetalleDocumento detalleDocumento: detalleDocumentos){
                            cantidadAtendidaMostrar += detalleDocumento.getCtdAtendida();
                        }
                        detalleDocumentoLive.setCtdAtendida(cantidadAtendidaMostrar);
                        cantidadAtendidaMostrar = 0.0;
                        detalleDocumentoLiveListSend.add(detalleDocumentoLive);
                    }, throwable -> {}));
        } else {
            if (detalleIndex < detalleDocumentoLiveList.size()) {
                DetalleDocumentoLive detalle = detalleDocumentoLiveList.get(detalleIndex);
                disposable.add(dataSourceRepository.getDetalleDocumentoByProductoBD(detalle.getIdProducto(),
                        detalle.getIdDocumento())
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMapIterable(detalleDocumentos -> detalleDocumentos)
                        .doOnComplete(() -> {
                            detalleDocumentoLiveList.get(detalleIndex).setCtdAtendida(cantidadAtendidaMostrar);
                            if (cantidadAtendida != 0.0) {
                                double cantidadPendiente = detalleDocumentoLiveList.get(detalleIndex).getCtdPendiente() - cantidadAtendida;
                                detalleDocumentoLiveList.get(detalleIndex).setCtdPendiente(cantidadPendiente);
                            }
                            detalleIndex += 1;
                            cantidadAtendida = 0.0;
                            cantidadAtendidaMostrar = 0.0;
                            verificarCantidadDetalles(detalleDocumentoLiveList);
                        })
                        .subscribe(detalleDocumento -> {
                            if (TextUtils.equals("0", detalleDocumento.getFlgCerrarEnvio())) {
                                cantidadAtendidaMostrar += detalleDocumento.getCtdAtendida();
                            }
                            cantidadAtendida += detalleDocumento.getCtdAtendida();
                        }, throwable -> {
                        }));
            } else {
                detalleIndex = 0;
                isGuiaStarted();
                getView().updateDetalle(detalleDocumentoLiveList);
            }
        }
    }

    /**
     * Completa la data faltante de la lista de detalles
     * @param detalleDocumentoLiveList es la lista de detalles agrupada para el documento activo
     *                                 en el modo sin documento.
     */
    @Override
    public void getProductoByDetalle(List<DetalleDocumentoLive> detalleDocumentoLiveList) {
        if (detalleIndex < detalleDocumentoLiveList.size()){
            disposable.add(dataSourceRepository.getProductoByID(detalleDocumentoLiveList.get(detalleIndex).getIdProducto())
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(productos -> {
                        detalleDocumentoLiveList.get(detalleIndex).setCodProducto(productos.get(0).getCodProducto());
                        detalleDocumentoLiveList.get(detalleIndex).setDscProducto(productos.get(0).getDscProducto());
                        detalleIndex += 1;
                        getProductoByDetalle(detalleDocumentoLiveList);
                    }, throwable -> { }));
        } else {
            detalleIndex = 0;
            isGuiaStarted();
            getView().updateDetalle(detalleDocumentoLiveList);
        }
    }

    /**
     * Registra la data en {@link MovimientoInterno} cuando se trabaja en el modo batch
     * @param body es la data necesaria de registro creada cuando el usuario registra una lectura
     * @param idDetalleDocumento es el identificador único del detalle creado al registrar la lectura
     */
    @Override
    public void registrarMovimientoInterno(BodyRegistrarLecturas body, int idDetalleDocumento) {
        disposable.add(dataSourceRepository.getIDLecturaDocumento(idDetalleDocumento,body.getIdProducto()
                ,body.getLoteProducto(),body.getSerieProducto())
                .subscribeOn(Schedulers.single())
                .subscribe(idList -> {
                    MovimientoInterno movimiento = new MovimientoInterno();
                    movimiento.setIdLecturaDocumento(idList.get(idList.size() - 1));
                    if (TextUtils.equals(Constants.RECEPCION, moduleType)) {
                        movimiento.setIdAlmacenDestino(preferenceManager.getConfig().getIdAlmacen());
                        movimiento.setIdUbicacionDestino(body.getIdUbicacion());
                    } else {
                        movimiento.setIdAlmacenOrigen(preferenceManager.getConfig().getIdAlmacen());
                        movimiento.setIdUbicacionOrigen(body.getIdUbicacion());
                    }
                    movimiento.setIdProducto(body.getIdProducto());
                    movimiento.setLoteProducto(body.getLoteProducto());
                    movimiento.setSerieProducto(body.getSerieProducto());
                    movimiento.setCtdAsignada(body.getCtdConfirmada());
                    movimiento.setCodUsuarioRegistro(body.getCodUsuario());
                    movimiento.setFlgActivo("1");
                    movimiento.setFchRegistro(UtilMethods.formatCurrentDate(new Date()));
                    movimiento.setFlgCerrarEnvio("0");
                    movimiento.setIdTerminal(body.getTerminal());
                    movimiento.setFchRegistroTerminal(UtilMethods.formatCurrentDate(new Date()));
                    dataSourceRepository.insertMovimientoInterno(movimiento);
                }, throwable -> {}));
    }

    /**
     * Obtiene la data faltante desde las preferencias para poder mostrar la consulta de las lecturas
     * @param body es la data incompleta para realizar la consulta de lecturas
     */
    @Override
    public void consultarLecturas(BodyConsultarLecturas body) {
        BodyConsultarLecturas bodyConsulta = body;
        bodyConsulta.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
        getView().consultarLecturas(bodyConsulta);
    }

    /**
     * Verifica si la ubicación fue seteada por defecto en las preferencias del equipo
     * @param type determina el módulo de trabajo
     *             "R" - Recepción
     *             "D" - Despacho
     */
    @Override
    public void isUbicacionSet(String type) {
        moduleType = type;
        if (TextUtils.equals(type,Constants.RECEPCION)){
            getView().setUpUbicacion(preferenceManager.getRecepcionUbi());
        } else if (TextUtils.equals(type,Constants.DESPACHO)){
            getView().setUpUbicacion(preferenceManager.getDespachoUbi());
        }
    }

    /***
     * Verifica que el usuario haya realizado lecturas para poder cerrar un documento
     */
    @Override
    public void getLecturasDespacho(BodyCerrarDocumento bodyCerrar) {
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.getLecturasCountBD(bodySaved.getCodTipoDocumento(),
                    bodySaved.getIdClaseDocumento(),bodySaved.getNumDocumento(),"1","0",
                    preferenceManager.getConfig().getCodUsuario())
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(count -> {
                        if (count != 0){
                            cerrarDocumento(bodyCerrar);
                        } else {
                            ProgressDialog.dismiss();
                            UtilMethods.showToast(context,context.getString(R.string.realizar_una_lectura));
                        }
                    }, throwable -> {}));
        } else {
            BodyConsultarLecturas bodyConsulta = new BodyConsultarLecturas();
            bodyConsulta.setCodTipodocumento(bodyCerrar.getCodClaseDocumento());
            bodyConsulta.setIdClaseDocumento(bodySaved.getIdClaseDocumento());
            bodyConsulta.setNumDocumento(bodyCerrar.getNumDocumento());
            bodyConsulta.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> cerrarDocumento(bodyCerrar),null, 1);
            } else {
                disposable.add(dataSourceRepository.getAllLecturasOnline(bodyConsulta)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(hijoConsultas -> {
                            if (!hijoConsultas.isEmpty()){
                                cerrarDocumento(bodyCerrar);
                            } else {
                                ProgressDialog.dismiss();
                                UtilMethods.showToast(context,context.getString(R.string.realizar_una_lectura));
                            }
                        }, throwable -> {
                            ProgressDialog.dismiss();
                            getView().onError(null,context.getString(R.string.error_WS), 6);
                        }));
            }
        }
    }

    @Override
    public void cerrarDocumento(BodyCerrarDocumento body) {
        BodyCerrarDocumento bodyCerrar = body;
        bodyCerrar.setCodEmpresa(preferenceManager.getConfig().getCodEmpresa());
        bodyCerrar.setCodAlmacen(preferenceManager.getConfig().getCodAlmacen());
        bodyCerrar.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
        if (preferenceManager.getConfig().getTipoConexion()){
            if (TextUtils.equals(Constants.RECEPCION,moduleType)) {
                // Cerramos las lecturas de esta guia
                disposable.add(dataSourceRepository.getLecturaDocumentoCerrar(body.getDocumentoInterno())
                        .subscribeOn(Schedulers.single())
                        .subscribe(lecturaDocumentos -> {
                            for (LecturaDocumento lecturaDocumento : lecturaDocumentos) {
                                dataSourceRepository.cerrarLecturaDocumento(lecturaDocumento.getIdLecturaDocumento(),
                                        bodyCerrar.getCodUsuario(), UtilMethods.formatCurrentDate(new Date()));
                                dataSourceRepository.cerrarMovimiento(lecturaDocumento.getIdLecturaDocumento(),
                                        bodyCerrar.getCodUsuario(), UtilMethods.formatCurrentDate(new Date()));
                            }
                            Schedulers.single().scheduleDirect(() -> cerrarDetalleDocumentoBD(bodyCerrar),
                                    15 * lecturaDocumentos.size(), TimeUnit.MILLISECONDS);
                        }, throwable -> {
                        }));
            } else if (TextUtils.equals(Constants.DESPACHO,moduleType)){
                // Cerramos las lecturas por el id de cada detalle creado para este documento
                disposable.add(dataSourceRepository.getDetalleDocumentoByDocumentoBD(bodyCerrar.getIdDocumento())
                        .subscribeOn(Schedulers.single())
                        .subscribe(detalleDocumentos -> {
                            for (DetalleDocumento detalleDocumento : detalleDocumentos){
                                dataSourceRepository.cerrarLecturaDocumentoByIdDetalle(detalleDocumento.getIdDetalleDocumento()
                                        ,bodyCerrar.getCodUsuario(),UtilMethods.formatCurrentDate(new Date()));
                            }
                            Schedulers.single().scheduleDirect(() -> {
                                cerrarDetalleDocumentoBD(bodyCerrar);
                                cerrarMovimientoDespacho(detalleDocumentos);
                            },15 * detalleDocumentos.size(),TimeUnit.MILLISECONDS);
                        }, throwable -> { }));
            }
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> cerrarDocumento(body),null, 1);
            } else {
                disposable.add(dataSourceRepository.cerrarDocumentoInterno(body)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(msgResponse -> {
                            if (msgResponse.getMsgError().contains("OK")) {
                                getView().cerrarDocumento();
                            }
                        }, throwable -> {
                            ProgressDialog.dismiss();
                            getView().onError(null, context.getString(R.string.error_WS),6);
                        }));
            }
        }
    }

    @Override
    public void cerrarMovimientoDespacho(List<DetalleDocumento> detalleDocumentos) {
        if (detalleIndex < detalleDocumentos.size()){
            disposable.add(dataSourceRepository.getIdLecturaDocumentoByIdDetalle(detalleDocumentos.
                    get(detalleIndex).getIdDetalleDocumento())
                    .subscribe(idLecturaList -> {
                        for (int id : idLecturaList) {
                            dataSourceRepository.cerrarMovimiento(id,bodySaved.getCodUsuario(),
                                    UtilMethods.formatCurrentDate(new Date()));
                        }
                        detalleIndex += 1;
                        cerrarMovimientoDespacho(detalleDocumentos);
                    }, throwable -> { }));
        } else {
            detalleIndex = 0;
        }
    }

    @Override
    public void cerrarDetalleDocumentoBD(BodyCerrarDocumento bodyCerrar) {
        disposable.add(dataSourceRepository.getDetalleDocumentoCerrar(bodyCerrar.getIdDocumento(),
                bodyCerrar.getDocumentoInterno())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(detalleDocumentos -> {
                    for (DetalleDocumento detalleDocumento : detalleDocumentos){
                        dataSourceRepository.cerrarDetalleDocumento(bodyCerrar.getCodUsuario(),
                                UtilMethods.formatCurrentDate(new Date()),detalleDocumento.getIdDetalleDocumento());
                    }
                    if (conSinDoc == 1){
                        Schedulers.single().scheduleDirect(() -> cerrarDetalleDocumentoOri(detalleDocumentos,bodyCerrar),
                                10 * detalleDocumentos.size(),TimeUnit.MILLISECONDS);
                    } else {
                       finalizarDocumento(detalleDocumentos,bodyCerrar);
                    }
                }, throwable -> { }));
    }

    @Override
    public void cerrarDetalleDocumentoOri(List<DetalleDocumento> detalleDocumentos, BodyCerrarDocumento bodyCerrar) {
        List<DetalleDocumentoOri> detalleDocumentoOriInsert = new ArrayList<>();
        disposable.add(dataSourceRepository.getDetalleDocumentoOri(bodyCerrar.getIdDocumento())
                .flatMapIterable(detalleDocumentoOris -> detalleDocumentoOris)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    dataSourceRepository.insertDetalleDocumentoOri(detalleDocumentoOriInsert);
                    if (cerradoCheck) {
                        finalizarDocumento(detalleDocumentos, bodyCerrar);
                    } else {
                        getView().cerrarDocumento();
                    }
                })
                .subscribe(detalleDocumentoOri -> {
                    for (DetalleDocumento detalleDocumento : detalleDocumentos) {
                        if (detalleDocumentoOri.getLinea() == detalleDocumento.getLinea()){
                            cantidadAtendida += detalleDocumento.getCtdAtendida();
                            detalleRegistrado = true;
                        }
                    }
                    if (detalleRegistrado) {
                        detalleDocumentoOri.setCtdAtendidaCliente(detalleDocumentoOri.getCtdAtendidaCliente() + cantidadAtendida);
                        detalleDocumentoOri.setCtdPendiente((int) (detalleDocumentoOri.getCtdRequerida() - detalleDocumentoOri.getCtdAtendidaCliente()));
                        cantidadAtendida = 0.0;
                        detalleRegistrado = false;
                    }
                    detalleDocumentoOri.setCodUsuarioModificacion(bodyCerrar.getCodUsuario());
                    detalleDocumentoOri.setFchModificacion(UtilMethods.formatCurrentDate(new Date()));
                    if (detalleDocumentoOri.getCtdPendiente() == 0) {
                        if (firstDetalleDocumentoOri) {
                            cerradoCheck = true;
                            firstDetalleDocumentoOri = false;
                        } else {
                            if (cerradoCheck) cerradoCheck = true;
                            else cerradoCheck = false;
                        }
                    } else {
                        cerradoCheck = false;
                    }
                    detalleDocumentoOriInsert.add(detalleDocumentoOri);
                }, throwable -> {
                }));
    }

    @Override
    public void finalizarDocumento(List<DetalleDocumento> detalleDocumentos, BodyCerrarDocumento bodyCerrar) {
        dataSourceRepository.cerrarDocumento(bodyCerrar.getCodUsuario(),UtilMethods.formatCurrentDate(new Date()),
                bodyCerrar.getIdDocumento());
        for (DetalleDocumento detalleDocumento : detalleDocumentos){
            dataSourceRepository.finalizarDetalleDocumento(bodyCerrar.getCodUsuario(),
                    UtilMethods.formatCurrentDate(new Date()),detalleDocumento.getIdDetalleDocumento());
        }
        getView().cerrarDocumento();
    }

    @Override
    public boolean isBatchMode() {
        return preferenceManager.getConfig().getTipoConexion();
    }

    @Override
    public void setImprimirBluetooth() {
        Configuracion config = preferenceManager.getConfig();
        config.setTipoImpresora(true);
        preferenceManager.saveConfig(config);
    }

    @Override
    public String getPrinterAddress() {
        return preferenceManager.getPrinterAddress();
    }

    /**
     * Verifica si una guia fue iniciada
     */
    @Override
    public void isGuiaStarted() {
        getView().setUpGuia(preferenceManager.getGuiaIniciada(),preferenceManager.getNroGuia());
    }

    /**
     * Guarda el estado de la guía trabajada
     * @param type es el tipo de registro a realizar
     *             0 - la guia fue cerrada
     *             1 - la guia fue iniciada y está en curso
     * @param nroGuia es el número de la guía trabajada
     */
    @Override
    public void saveGuia(int type, String nroGuia) {
        if (type == 0){
            preferenceManager.setGuiaIniciada(false);
            preferenceManager.setGuiaCerrada(true);
        } else {
            preferenceManager.setGuiaIniciada(true);
            preferenceManager.setGuiaCerrada(false);
        }
        preferenceManager.setNroGuia(nroGuia);
    }

    /**
     * @return true - si guía fue cerrada
     *         false - si guía aún está en curso
     */
    @Override
    public boolean getGuiaCerrada() {
        return preferenceManager.getGuiaCerrada();
    }

    /**
     * Verifica que la serie en uso esté disponible para un determinado producto
     * @param body es la data necesaria para realizar la verificación
     */
    @Override
    public void verifySerie(BodyBuscarSerie body) {
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.verifySerieRecepcionBD(body.getIdProducto(),
                    body.getSerieProducto(),body.getIdDocumento())
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(hijoConsultas -> {
                        if (!hijoConsultas.isEmpty()) {
                            getView().checkSerieResult(1);
                        } else {
                            getView().checkSerieResult(0);
                        }
                    }, throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> verifySerie(body), null, 1);
            } else {
                disposable.add(dataSourceRepository.getSerie(body)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(hijoConsultas -> {
                            if (!hijoConsultas.isEmpty()) {
                                getView().checkSerieResult(1);
                            } else {
                                getView().checkSerieResult(0);
                            }
                        }, throwable -> getView().checkSerieResult(2)));
            }
        }
    }

    /**
     * Verifica que un producto exista en la base de datos
     * @param codProducto es el código del prooducto seleccionado
     */
    @Override
    public void verifyProducto(String codProducto) {
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.verifyProductoDB(codProducto)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(productos -> getView().checkProducto(productos), throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> verifyProducto(codProducto), null, 1);
            } else {
                disposable.add(dataSourceRepository.getProducto(codProducto)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(productos -> getView().checkProducto(productos)
                                , throwable -> {
                                    UtilMethods.showToast(context, "Error de conexión");
                                }));
            }
        }
    }

    /**
     * Setea el estado de trabajo del aplicativo
     * @param value es el tipo de estado
     *              0 - no asignado
     *              1 - con documento
     *              2 - sin documento
     */
    @Override
    public void setConSinDoc(int value) {
        preferenceManager.setConSinDoc(value);
    }

    /**
     * @return el estado de trabajo del aplicativo
     */
    @Override
    public int getConSinDoc() {
        return preferenceManager.getConSinDoc();
    }

    @Override
    public void deleteDataClase() {
        if (!preferenceManager.getConfig().getTipoConexion()) dataSourceRepository.deleteClaseDocumento();
    }

    @Override
    public void cancelarDocumento() {
        BodyCancelarDocumento bodyCancelar = new BodyCancelarDocumento(preferenceManager.getConfig().getCodUsuario());
        if (preferenceManager.getConfig().getTipoConexion()){
            dataSourceRepository.cancelarDocumentoBD(bodyCancelar.getCodUsuario(),UtilMethods.formatCurrentDate(new Date()),
                    bodySaved.getIdDocumento());
            AndroidSchedulers.mainThread().scheduleDirect(() -> getView().cancelarDocumento(),50,TimeUnit.MILLISECONDS);
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(this::cancelarDocumento, null, 1);
            } else {
                disposable.add(dataSourceRepository.cancelarDocumento(bodyCancelar, bodySaved.getIdDocumento())
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(msgResponse -> {
                            if (msgResponse.getCodError() == 0) {
                                getView().cancelarDocumento();
                            }
                        }, throwable -> {
                            ProgressDialog.dismiss();
                            getView().onError(this::cancelarDocumento, context.getString(R.string.no_pudo_cancelar_documento), 7);
                        }));
            }
        }
    }
}
