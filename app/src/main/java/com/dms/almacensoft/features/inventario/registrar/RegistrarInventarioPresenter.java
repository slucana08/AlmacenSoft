package com.dms.almacensoft.features.inventario.registrar;

import android.content.Context;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.entities.dbalmacen.Ubicacion;
import com.dms.almacensoft.data.entities.dbtransact.DetalleInventario;
import com.dms.almacensoft.data.entities.dbtransact.LecturaInventario;
import com.dms.almacensoft.data.models.BodyUbicacion;
import com.dms.almacensoft.data.models.inventario.BodyRegistrarInventario;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.features.configuracion.general.GeneralConfigFragment;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link RegistrarInventarioPresenter} realiza las llamadas al WS y preferencias del equipo para
 * {@link RegistrarInventarioFragment}
 */
public class RegistrarInventarioPresenter implements RegistrarInventarioContract.Presenter {

    private RegistrarInventarioContract.View view;
    private Context context;
    private DataSourceRepository dataSourceRepository;
    private PreferenceManager preferenceManager;
    private CompositeDisposable disposable = new CompositeDisposable();

    // Contador de intento de reconexión para el registro de lecturas
    private int registroCount = 0;

    @Inject
    public RegistrarInventarioPresenter (Context context, DataSourceRepository dataSourceRepository,
                                         PreferenceManager preferenceManager){
        this.context = context;
        this.dataSourceRepository = dataSourceRepository;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void attachView(RegistrarInventarioContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        disposable.clear();
        this.view = null;
    }

    @Override
    public RegistrarInventarioContract.View getView() {
        return view;
    }

    /**
     * Trae las configuraciones seteadas en {@link GeneralConfigFragment}
     */
    @Override
    public void getConfig() {
        boolean lote = preferenceManager.getConfig().getLote();
        boolean serie = preferenceManager.getConfig().getSerie();
        boolean barrido = preferenceManager.getConfig().getModoLectura();
        Ubicacion ubicacion = preferenceManager.getInventarioUbi();
        getView().setUpViews(lote,serie,barrido,ubicacion);
    }

    @Override
    public void verifyUbicacion(String codUbicacion) {
        BodyUbicacion body = new BodyUbicacion();
        body.setIdAlmacen(preferenceManager.getConfig().getIdAlmacen());
        body.setCodUbicacion(codUbicacion);
        body.setTipoResultado(1);
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.verifyUbicacionBD(body.getIdAlmacen(),codUbicacion)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(ubicacions -> getView().checkUbicacion(ubicacions), throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> verifyUbicacion(codUbicacion), context.getString(R.string.procesando), 1);
            } else {
                disposable.add(dataSourceRepository.getUbicacion(body)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(ubicacions ->
                                        RegistrarInventarioPresenter.this.getView().checkUbicacion(ubicacions),
                                throwable -> {
                                    ProgressDialog.dismiss();
                                    UtilMethods.showToast(context, context.getString(R.string.error_WS));
                                }));
            }
        }
    }

    /**
     * Obtiene la data sobre inventario desde la base de datos interna
     */
    @Override
    public void getConteo() {
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.getAllInventarioByAlmacen(preferenceManager.getConfig().getIdAlmacen())
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(inventarios -> getView().setUpConteo(inventarios), throwable -> { }));
        } else {
            disposable.add(dataSourceRepository.getListInventarioDB()
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(inventarios -> RegistrarInventarioPresenter.this.getView().setUpConteo(inventarios)
                            , throwable -> {
                                ProgressDialog.dismiss();
                                UtilMethods.showToast(context, "Error");
                            }));
        }
    }

    /**
     * Obtiene los diferenciales del inventario
     * @param idInventario identificador único del inventario
     * @param conteo el conteo en que se encuentra el inventario
     */
    @Override
    public void getDiferencial(int idInventario, int conteo) {
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.getProductoDiferencialCount()
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(count -> getView().showDiferencial(count), throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> getDiferencial(idInventario, conteo), context.getString(R.string.recopilando), 0);
            } else {
                disposable.add(dataSourceRepository.getDiferencial(idInventario, conteo)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(productoDiferencials -> getView().showDiferencial(productoDiferencials.size()),
                                throwable -> {
                                    ProgressDialog.dismiss();
                                    getView().onError(() -> getLecturas(idInventario), context.getString(R.string.recopilando), 2);
                                }));
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
                    .subscribe(productos -> RegistrarInventarioPresenter.this.getView().checkProducto(productos), throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> verifyProducto(codProducto), context.getString(R.string.procesando), 1);
            } else {
                disposable.add(dataSourceRepository.getProducto(codProducto)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(productos -> getView().checkProducto(productos)
                                , throwable -> {
                                    UtilMethods.showToast(context, context.getString(R.string.error_WS));
                                    ProgressDialog.dismiss();
                                }));
            }
        }
    }

    /**
     * Trae la cantidad de lecturas realizadas para el conteo en el que se encuentra el inventario
     * @param idInventario identificador único del inventario
     */
    @Override
    public void getLecturas(int idInventario) {
        if (preferenceManager.getConfig().getTipoConexion()) {
            disposable.add(dataSourceRepository.getLecturasInventarioCountBD(idInventario,preferenceManager.getConfig().getCodUsuario())
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(count -> getView().setUpLecturas(String.valueOf(count)), throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                ProgressDialog.dismiss();
                getView().onError(() -> getLecturas(idInventario), context.getString(R.string.recopilando), 0);
            } else {
                disposable.add(dataSourceRepository.getLecturasInventarioCount(idInventario, preferenceManager.getConfig().getCodUsuario())
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(lecturas -> {
                            if (lecturas.getMsgError().contains("El conteo está cerrado")) {
                                ProgressDialog.dismiss();
                                RegistrarInventarioPresenter.this.getView().onError(null, null, 5);
                            } else {
                                String[] data = lecturas.getMsgError().split("\\|");
                                RegistrarInventarioPresenter.this.getView().setUpLecturas(data[1]);
                            }
                        }, throwable -> {
                            ProgressDialog.dismiss();
                            RegistrarInventarioPresenter.this.getView().onError(() -> RegistrarInventarioPresenter.this.getLecturas(idInventario), context.getString(R.string.recopilando), 2);
                        }));
            }
        }
    }

    /**
     * Realiza el registro de una lectura
     * @param body es la data necesaria para reaizar el registro
     */
    @Override
    public void registrarLectura(BodyRegistrarInventario body) {
        body.setIdTerminal(preferenceManager.getImei());
        body.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
        body.setIdAlmacen(preferenceManager.getConfig().getIdAlmacen());
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
                ProgressDialog.dismiss();
                getView().onError(() -> registrarLectura(body), context.getString(R.string.registrando_lectura), 0);
            } else {
                disposable.add(dataSourceRepository.registrarLecturaInventario(body)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(msgResponse -> {
                            if (msgResponse.getMsgError().contains("Operacion correcta")) {
                                registroCount = 0;
                                getView().updateViews(0);
                            }
                        }, throwable -> {
                            ProgressDialog.dismiss();
                            if (registroCount < 2) {
                                registroCount += 1;
                                getView().onError(() -> registrarLectura(body),
                                        context.getString(R.string.registrando_lectura), 3);
                            } else {
                                registroCount = 0;
                                getView().onError(null, null, 4);
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
                    AndroidSchedulers.mainThread().scheduleDirect(() -> getView().updateViews(0), 100, TimeUnit.MILLISECONDS);
                }, throwable -> {}));
    }

    /**
     * Verifica que la serie ingresada no esté en uso para el producto seleccionado
     * @param idInventario identificador único del inventario
     * @param idProducto identificacor único del producto seleccionado
     * @param serieProducto es la serie ingresada por el usuario
     */
    @Override
    public void verifySerie(int idInventario, int idProducto, String serieProducto){
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.obtenerSerieInventarioBD(idInventario, idProducto, serieProducto)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(serieResponseInventarios -> {
                        if (serieResponseInventarios.isEmpty()) {
                            RegistrarInventarioPresenter.this.getView().checkSerieResult(0);
                        } else {
                            RegistrarInventarioPresenter.this.getView().checkSerieResult(1);
                        }
                    }, throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> verifySerie(idInventario,idProducto,serieProducto), context.getString(R.string.procesando), 0);
            } else {
                disposable.add(dataSourceRepository.getSerieInventario(idInventario, idProducto, serieProducto)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(serieResponseInventarios -> {
                            if (serieResponseInventarios.isEmpty()) {
                                RegistrarInventarioPresenter.this.getView().checkSerieResult(0);
                            } else {
                                RegistrarInventarioPresenter.this.getView().checkSerieResult(1);
                            }
                        }, throwable -> getView().checkSerieResult(2)));
            }
        }
    }
}
