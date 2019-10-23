package com.dms.almacensoft.features.inventario.diferencial;

import android.content.Context;
import android.text.TextUtils;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.entities.dbtransact.DetalleInventario;
import com.dms.almacensoft.data.entities.dbalmacen.Inventario;
import com.dms.almacensoft.data.entities.dbtransact.LecturaInventario;
import com.dms.almacensoft.data.models.GroupConsulta;
import com.dms.almacensoft.data.models.inventario.BodyRegistrarInventario;
import com.dms.almacensoft.data.entities.dbtransact.ProductoDiferencial;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.features.inventario.registrar.RegistrarInventarioFragment;
import com.dms.almacensoft.utils.UtilMethods;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link DiferencialPresenter} realiza las llamadas al WS y preferencias del equipo para
 * {@link DiferencialFragment}
 */
public class DiferencialPresenter implements DifeferencialContract.Presenter {

    private DifeferencialContract.View view;
    private Context context;
    private DataSourceRepository dataSourceRepository;
    private PreferenceManager preferenceManager;
    private CompositeDisposable disposable = new CompositeDisposable();

    // Es la data necesaria del inventario
    private Inventario inventario;

    // Determina si los diferenciales se organizan por:
    // 0 - código de ubicación
    // 1 - código de producto
    private int state = 0;

    // Contador de reintentos de conexión al registrar una lectura
    private int registroCount = 0;

    @Inject
    public DiferencialPresenter(Context context, DataSourceRepository dataSourceRepository, PreferenceManager preferenceManager){
        this.context = context;
        this.dataSourceRepository = dataSourceRepository;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void attachView(DifeferencialContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        disposable.clear();
        this.view = null;
    }

    @Override
    public DifeferencialContract.View getView() {
        return view;
    }

    /**
     * Obtiene la data necesaria del inventario abierto desde la base de datos
     */
    @Override
    public void getConteo() {
        disposable.add(dataSourceRepository.getListInventarioDB()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(inventarios -> {
                    inventario = inventarios.get(0);
                    getDiferencial(0);
                }, throwable -> {}));
    }

    /**
     * Obtiene los diferenciales del conteo actual
     * @param type determina como se organizarán los diferenciales
     *             0 - código de ubicación
     *             1 - código de producto
     */
    @Override
    public void getDiferencial(int type) {
        List<HeaderDiferencial> list = new ArrayList<>();
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.getAllProductoDiferencial()
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    // Divide la lista obtenida para que el Observable devuelva item individuales
                    .flatMapIterable(productoDiferencials -> productoDiferencials)
                    // Agrupa los item individuales por código de ubicación (codUbicacio) o por
                    // código de producto (codProducto)
                    .groupBy(type == 0 ? ProductoDiferencial::getCodUbicacion : ProductoDiferencial::getCodProducto)
                    // Crea listas de los grupos
                    .flatMapSingle(Observable::toList)
                    // Realiza el muestreo de la lista de HeaderDiferencial(list) que contiene las listas
                    // diferenciales con sus respectivas cabeceras
                    .doOnComplete(() -> {
                        // mantiene una referencia del tipo de consulta
                        state = type;
                        DiferencialPresenter.this.getView().setUpDiferencial(list);
                    })
                    // Agrega las listas creadas a la lista de HeaderDiferencial(list)
                    .subscribe(productoDiferencials -> {
                        GroupConsulta groupConsulta = new GroupConsulta();
                        groupConsulta.setTitulo(type == 0 ? productoDiferencials.get(0).getCodUbicacion()
                                : productoDiferencials.get(0).getCodProducto());
                        groupConsulta.setTotal(type == 0 ? String.valueOf(productoDiferencials.size()) : "");
                        list.add(new HeaderDiferencial(new Gson().toJson(groupConsulta, GroupConsulta.class), productoDiferencials));
                    }, throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> getDiferencial(type), context.getString(R.string.movil_sin_conexion), 0);
            } else {
                disposable.add(dataSourceRepository.getDiferencial(inventario.getIdInventario(), inventario.getNroConteo())
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        // Divide la lista obtenida para que el Observable devuelva item individuales
                        .flatMapIterable(productoDiferencials -> productoDiferencials)
                        // Agrupa los item individuales por código de ubicación (codUbicacio) o por
                        // código de producto (codProducto)
                        .groupBy(type == 0 ? ProductoDiferencial::getCodUbicacion : ProductoDiferencial::getCodProducto)
                        // Crea listas de los grupos
                        .flatMapSingle(Observable::toList)
                        // Realiza el muestreo de la lista de HeaderDiferencial(list) que contiene las listas
                        // diferenciales con sus respectivas cabeceras
                        .doOnComplete(() -> {
                            // mantiene una referencia del tipo de consulta
                            state = type;
                            DiferencialPresenter.this.getView().setUpDiferencial(list);
                        })
                        // Agrega las listas creadas a la lista de HeaderDiferencial(list)
                        .subscribe(productoDiferencials -> {
                            GroupConsulta groupConsulta = new GroupConsulta();
                            groupConsulta.setTitulo(type == 0 ? productoDiferencials.get(0).getCodUbicacion()
                                    : productoDiferencials.get(0).getCodProducto());
                            groupConsulta.setTotal(type == 0 ? String.valueOf(productoDiferencials.size()) : "");
                            list.add(new HeaderDiferencial(new Gson().toJson(groupConsulta, GroupConsulta.class), productoDiferencials));
                        }, throwable ->
                                getView().onError(() -> getDiferencial(type), context.getString(R.string.error_WS), 1)));
            }
        }
    }

    /**
     * Determina si es que el registro se realizará en esta pantalla o si se realizará en
     * {@link RegistrarInventarioFragment}
     * @param body contiene la data del prodcuto seleccionado necesaria para realizar el registro
     */
    @Override
    public void determinarRegistro(BodyRegistrarInventario body) {
        boolean barrido = preferenceManager.getConfig().getModoLectura();
        boolean lote = preferenceManager.getConfig().getLote();
        boolean serie = preferenceManager.getConfig().getSerie();
        String loteProducto = body.getLoteProducto();
        if (barrido){
            // Si el modo de lectura es barrido
            if (lote){
                // Si el registro de lote está activado
                if (!TextUtils.isEmpty(loteProducto)){
                    // Si el producto seleccionado tiene lote
                    if (serie){
                        // Si el registro de serie está activado se llama a la pantala
                        // de registro (RegistrarInventarioFragment)
                        getView().showRegistroScreen(body);
                    } else {
                        // Si no se registra la lectura en esta pantalla
                        registrarDiferencial(body);
                    }
                } else {
                    // Si el producto seleccionado no tiene lote se llama a la pantala
                    // de registro (RegistrarInventarioFragment)
                    getView().showRegistroScreen(body);
                }
            } else {
                // Si el registro de lote está desactivado
                    if (serie){
                        // Si el registro de serie está activado se llama a la pantala
                        // de registro (RegistrarInventarioFragment)
                        getView().showRegistroScreen(body);
                    } else {
                        // Si no se registra la lectura en esta pantalla
                        registrarDiferencial(body);
                    }
            }
        } else {
            // Si el modo de lectura es barrido siempre se llama a la pantala de registro (RegistrarInventarioFragment)
            getView().showRegistroScreen(body);
        }
    }

    /**
     * Realiza el registro de la lectura
     * @param body contiene la data del producto seleccionado necesaria para realizar el registro
     */
    @Override
    public void registrarDiferencial(BodyRegistrarInventario body) {
        body.setIdInventario(inventario.getIdInventario());
        body.setSerieProducto("");
        body.setCantidadLeida(1.0);
        body.setIdTerminal(preferenceManager.getImei());
        body.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
        body.setIdAlmacen(preferenceManager.getConfig().getIdAlmacen());
        body.setEspacioLleno(0);
        body.setCodProdCaja("");
        body.setNumCaja(0);
        body.setCodPresentacion("");
        body.setCtdPresentacion("0");
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.verifyDetalleInventarioBD(body.getIdInventario(),body.getIdUbicacion(),
                    body.getIdProducto(),body.getLoteProducto())
                    .subscribeOn(Schedulers.single())
                    .subscribe(count -> {
                        // Registramos el detalle de inventario
                        if (count != 0){
                            dataSourceRepository.updateDetalleInventarioBD(body.getCantidadLeida(),body.getCodUsuario(),
                                    UtilMethods.formatCurrentDate(new Date()),body.getIdInventario(),body.getIdUbicacion(),
                                    body.getIdProducto(),body.getLoteProducto());
                        } else {
                            DetalleInventario detalleInventario = new DetalleInventario();
                            detalleInventario.setIdInventario(body.getIdInventario());
                            detalleInventario.setIdUbicacion(body.getIdUbicacion());
                            detalleInventario.setIdProducto(body.getIdProducto());
                            detalleInventario.setLoteProducto(body.getLoteProducto());
                            detalleInventario.setStockInicial(0.0);
                            detalleInventario.setStockFinal(body.getCantidadLeida());
                            detalleInventario.setFlgEsnuevo("1");
                            detalleInventario.setCodUsuarioRegistro(body.getCodUsuario());
                            detalleInventario.setFchRegistro(UtilMethods.formatCurrentDate(new Date()));
                            detalleInventario.setCodUsuarioModificacion(body.getCodUsuario());
                            detalleInventario.setFchModificacion(UtilMethods.formatCurrentDate(new Date()));
                            dataSourceRepository.insertDetalleInventario(detalleInventario);
                        }
                        Schedulers.single().scheduleDirect(() -> verifyDetalleInventario(body),80,TimeUnit.MILLISECONDS);
                    }, throwable -> {}));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> registrarDiferencial(body), context.getString(R.string.movil_sin_conexion), 0);
            } else {
                disposable.add(dataSourceRepository.registrarLecturaInventario(body)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(msgResponse -> {
                            if (msgResponse.getMsgError().contains(context.getString(R.string.operacion_realizada))) {
                                UtilMethods.showToast(context, context.getString(R.string.lectura_registrada));
                                // Luego de registrar la lectura, actualizamos la lista de diferenciales
                                getDiferencial(state);
                            }
                            registroCount = 0;
                        }, throwable -> {
                            if (registroCount < 2) {
                                registroCount += 1;
                                getView().onError(() -> registrarDiferencial(body),
                                        context.getString(R.string.no_pudo_registro), 2);
                            } else {
                                registroCount = 0;
                                getView().onError(() -> getDiferencial(state), null, 3);
                            }
                        }));
            }
        }
    }

    /**
     * Verifica que el detalle haya sido insertado
     * @param body es la data necesaria para registrar un lectura
     */
    @Override
    public void verifyDetalleInventario(BodyRegistrarInventario body) {
        disposable.add(dataSourceRepository.verifyDetalleInventarioBD(body.getIdInventario(),body.getIdUbicacion()
                ,body.getIdProducto(),body.getLoteProducto())
                .subscribe(count -> {
                    if (count != 0){
                        registrarLecturaBD(body);
                    } else {
                        verifyDetalleInventario(body);
                    }
                }, throwable -> { }));
    }

    @Override
    public void registrarLecturaBD(BodyRegistrarInventario body) {
        disposable.add(dataSourceRepository.getIdDetalleInventarioBD(body.getIdInventario(),body.getIdUbicacion(),
                body.getIdProducto(),body.getLoteProducto())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(idDetalle -> {
                    LecturaInventario lecturaInventario = new LecturaInventario();
                    lecturaInventario.setIdInventario(body.getIdInventario());
                    lecturaInventario.setIdDetalleInventario(idDetalle);
                    lecturaInventario.setIdEmpresa(preferenceManager.getConfig().getIdEmpresa());
                    lecturaInventario.setIdAlmacen(body.getIdAlmacen());
                    lecturaInventario.setIdUbicacion(body.getIdUbicacion());
                    lecturaInventario.setIdProducto(body.getIdProducto());
                    lecturaInventario.setCodProducto(body.getCodProducto());
                    lecturaInventario.setDscProducto(body.getDscProducto());
                    lecturaInventario.setLoteProducto(body.getLoteProducto());
                    lecturaInventario.setSerieProducto(body.getSerieProducto());
                    lecturaInventario.setStockInventariado(body.getCantidadLeida());
                    lecturaInventario.setCodUsuarioRegistro(body.getCodUsuario());
                    lecturaInventario.setFchRegistro(UtilMethods.formatCurrentDate(new Date()));
                    lecturaInventario.setCodUsuarioModificacion(body.getCodUsuario());
                    lecturaInventario.setFchModificacion(UtilMethods.formatCurrentDate(new Date()));
                    lecturaInventario.setIdTerminal(body.getIdTerminal());
                    lecturaInventario.setFlgActivo("1");
                    lecturaInventario.setFlgDescargado("0");
                    lecturaInventario.setFchDescargado("");
                    lecturaInventario.setCodProdCaja(body.getCodProdCaja());
                    lecturaInventario.setNroCaja(body.getNumCaja());
                    dataSourceRepository.insertLecturaInventario(lecturaInventario);
                    AndroidSchedulers.mainThread().scheduleDirect(() -> getDiferencial(state), 100, TimeUnit.MILLISECONDS);
                }, throwable -> {}));
    }
}
