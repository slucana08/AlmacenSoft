package com.dms.almacensoft.features.inventario.lecturas;

import android.content.Context;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.entities.dbalmacen.Inventario;
import com.dms.almacensoft.data.entities.dbalmacen.Producto;
import com.dms.almacensoft.data.entities.dbalmacen.Ubicacion;
import com.dms.almacensoft.data.entities.dbtransact.LecturaInventario;
import com.dms.almacensoft.data.models.GroupConsulta;
import com.dms.almacensoft.data.models.inventario.BodyConsultaLecturaInventario;
import com.dms.almacensoft.data.models.inventario.BodyEliminarDetalle;
import com.dms.almacensoft.data.models.inventario.HijoConsultaInventario;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link LecturaInventarioPresenter} realiza las llamadas al WS y preferencias del equipo para
 * {@link LecturaInventarioFragment}
 */

public class LecturaInventarioPresenter implements LecturaInventarioContract.Presenter {

    private LecturaInventarioContract.View view;
    private Context context;
    private DataSourceRepository dataSourceRepository;
    private PreferenceManager preferenceManager;
    private CompositeDisposable disposable = new CompositeDisposable();

    // Es la data del inventario abierto disponible
    private Inventario inventario;

    // Se usa para guardar datos de la ubicación utilizada en la búsqueda
    private List<Ubicacion> ubicacionList = new ArrayList<>();

    // Se usa para guardar los datos del producto utilizado en la búsqueda
    private List<Producto> productoList =  new ArrayList<>();

    // Valores de consulta que se utilizan para actualizar la pantalla luego de realizar una eliminación
    private int tipoConsulta;
    private String valor;

    // Contador de reintentos de reconexión al eliminar una lectura individual
    private int deleteLecturaCount = 0;

    @Inject
    public LecturaInventarioPresenter (Context context, DataSourceRepository dataSourceRepository,
                                       PreferenceManager preferenceManager){
        this.context = context;
        this.dataSourceRepository = dataSourceRepository;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void attachView(LecturaInventarioContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        disposable.clear();
        this.view = null;
    }

    @Override
    public LecturaInventarioContract.View getView() {
        return view;
    }

    /**
     * Obtiene la data necesaria del inventario abierto desde la base de datos
     */
    @Override
    public void getInventario() {
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.getAllInventarioByAlmacen(preferenceManager.getConfig().getIdAlmacen())
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(inventarios -> {
                        inventario = inventarios.get(0);
                        getView().setUpViews(preferenceManager.getConfig().getPerfilTipo());
                    }, throwable -> { }));
        } else {
            disposable.add(dataSourceRepository.getListInventarioDB()
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(inventarios -> {
                        inventario = inventarios.get(0);
                        getView().setUpViews(preferenceManager.getConfig().getPerfilTipo());
                    }, throwable -> UtilMethods.showToast(context, context.getString(R.string.error_get_inventario))));
        }
    }

    /**
     * Obtiene las lecturas de acuerdo a la data provista
     * @param tipoConsulta es el criterio de búsqueda seleccionado
     *                     0 - ubicación
     *                     1 - código de producto
     *                     2 - lote
     *                     3 - serie
     * @param valor el dato ingresado por el usuario
     */
    @Override
    public void getLecturas(int tipoConsulta, String valor){
        this.tipoConsulta = tipoConsulta;
        this.valor = valor;
        BodyConsultaLecturaInventario bodyConsulta = new BodyConsultaLecturaInventario();
        bodyConsulta.setIdInventario(inventario.getIdInventario());
        bodyConsulta.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
        bodyConsulta.setTipoConsulta(tipoConsulta);
        bodyConsulta.setDato(valor);
        List<HeaderConsultaInventario> list = new ArrayList<>();
        if (preferenceManager.getConfig().getTipoConexion()){
            switch (tipoConsulta){
                case 0:
                    verifyUbicacionBD(valor);
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (ubicacionList.isEmpty()){
                            getView().showLecturas(new ArrayList<>());
                        } else {
                            disposable.add(dataSourceRepository.getLecturaInventarioByUbicacion(inventario.getIdInventario(),
                                    ubicacionList.get(0).getIdUbicacion(),preferenceManager.getConfig().getCodUsuario())
                                    .subscribeOn(Schedulers.single())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(this::organizeLecturas, throwable -> {}));
                        }
                    }, 100, TimeUnit.MILLISECONDS);
                    break;
                case 1:
                    verifyProductoBD(valor);
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (productoList.isEmpty()){
                            getView().showLecturas(new ArrayList<>());
                        } else {
                            disposable.add(dataSourceRepository.getLecturaInventarioByProducto(inventario.getIdInventario(),
                                    productoList.get(0).getIdProducto(),preferenceManager.getConfig().getCodUsuario())
                                    .subscribeOn(Schedulers.single())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(this::organizeLecturas, throwable -> {}));
                        }
                    }, 100,TimeUnit.MILLISECONDS);
                    break;
                case 2:
                    disposable.add(dataSourceRepository.getLecturaInventarioByLote(inventario.getIdInventario(),
                            valor,preferenceManager.getConfig().getCodUsuario())
                            .subscribeOn(Schedulers.single())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::organizeLecturas, throwable -> {}));
                    break;
                case 3:
                    disposable.add(dataSourceRepository.getLecturaInventarioBySerie(inventario.getIdInventario(),
                            valor,preferenceManager.getConfig().getCodUsuario())
                            .subscribeOn(Schedulers.single())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::organizeLecturas, throwable -> {}));
                    break;
            }

        } else {
            if (!UtilMethods.checkConnection(context)) {
                ProgressDialog.dismiss();
                getView().onError(() -> getLecturas(tipoConsulta, valor), context.getString(R.string.movil_sin_conexion), 0);
            } else {
                disposable.add(dataSourceRepository.consultarLecturaInventario(bodyConsulta)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::organizeLecturas,
                                throwable -> LecturaInventarioPresenter.this.getView().onError(() -> LecturaInventarioPresenter.this.getLecturas(tipoConsulta, valor),context.getString(R.string.error_WS), 0)));
            }
        }
    }

    @Override
    public void verifyUbicacionBD(String codUbicacion) {
        disposable.add(dataSourceRepository.verifyUbicacionBD(preferenceManager.getConfig().getIdAlmacen(),codUbicacion)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ubicacions -> ubicacionList = ubicacions, throwable -> {}));
    }

    @Override
    public void verifyProductoBD(String codProducto) {
        disposable.add(dataSourceRepository.verifyProductoDB(codProducto)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productos -> productoList = productos, throwable -> { }));
    }

    @Override
    public void organizeLecturas(List<HijoConsultaInventario> hijoConsultaInventarioList) {
        List<HeaderConsultaInventario> list = new ArrayList<>();
        disposable.add(Observable.fromIterable(hijoConsultaInventarioList)
                .observeOn(AndroidSchedulers.mainThread())
                // Agrupa los item por el código de producto (codProducto)
                .groupBy(HijoConsultaInventario::getIdDetalleInventario)
                // Crea lista de los grupos
                .flatMapSingle(Observable::toList)
                // Muestra la lista de HeaderConsultaInventario(list) que contiene los grupos de lecturas
                // individuales con sus respectivas cabeceras
                .doOnComplete(() -> getView().showLecturas(list))
                // Agrega las listas ya organizadas a la lista de HeaderConsultaInventario(list)
                .subscribe(hijoConsultaInventarios -> {
                    double total = 0;
                    for (HijoConsultaInventario hijo : hijoConsultaInventarios) {
                        total += hijo.getStockInventariado();
                    }
                    GroupConsulta groupConsulta = new GroupConsulta();
                    groupConsulta.setTitulo(hijoConsultaInventarios.get(0).getCodProducto());
                    groupConsulta.setTotal(String.valueOf(total));
                    list.add(new HeaderConsultaInventario(new Gson().toJson(groupConsulta, GroupConsulta.class), hijoConsultaInventarios));
                }, throwable -> { }));
    }

    /**
     * Realiza la eliminación de una lectura individual
     * @param idLecturaInventario identificador único de la lectura
     */
    @Override
    public void deleteLectura(int idLecturaInventario) {
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.getLecturaEliminar(idLecturaInventario,
                    preferenceManager.getConfig().getCodUsuario())
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(lecturaInventario -> {
                        dataSourceRepository.eliminarLectura(idLecturaInventario,preferenceManager.getConfig().getCodUsuario());
                        dataSourceRepository.updateDetalleLecturaEliminar(lecturaInventario.getStockInventariado()
                                ,lecturaInventario.getIdDetalleInventario(), preferenceManager.getConfig().getCodUsuario()
                                ,UtilMethods.formatCurrentDate(new Date()));
                        AndroidSchedulers.mainThread().scheduleDirect(() -> getLecturas(tipoConsulta, valor),50,TimeUnit.MILLISECONDS);
                    }, throwable -> {}));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                ProgressDialog.dismiss();
                getView().onError(() -> deleteLectura(idLecturaInventario), context.getString(R.string.movil_sin_conexion), 0);
            } else {
                disposable.add(dataSourceRepository.deleteLecturaInventario(idLecturaInventario,
                        preferenceManager.getConfig().getCodUsuario())
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(msgResponse -> {
                            if (msgResponse.getMsgError().contains(context.getString(R.string.proceso_ok))) {
                                // Luego de realizar la eliminación de actualiza la lista de lecturas
                                getLecturas(tipoConsulta, valor);
                            }
                            deleteLecturaCount = 0;
                        }, throwable -> {
                            if (deleteLecturaCount < 2) {
                                deleteLecturaCount += 1;
                                getView().onError(() -> deleteLectura(idLecturaInventario),
                                        context.getString(R.string.no_pudo_eliminar_lectura), 0);
                            } else {
                                deleteLecturaCount = 0;
                                getView().onError(null, String.valueOf(tipoConsulta) + "-" + valor, 2);
                            }
                        }));
            }
        }
    }

    /**
     * Realiza la eliminación de un detalle del inventario que contiene un grupo de lecturas individuales
     * @param idDetaleInventario identificador único del detalle
     */
    @Override
    public void deleteDetalle(int idDetaleInventario) {
        BodyEliminarDetalle body = new BodyEliminarDetalle();
        body.setCodUsuario(preferenceManager.getConfig().getCodUsuario());
        body.setFlgTodo(1);
        body.setNumCaja(0);
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.getLecturaEliminarByDetalle(idDetaleInventario,body.getCodUsuario())
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(lecturaInventarios -> {
                        double cantidadEliminar = 0.0;
                        for (LecturaInventario lecturaInventario : lecturaInventarios){
                            dataSourceRepository.eliminarLectura(lecturaInventario.getIdLecturaInventario(),body.getCodUsuario());
                            cantidadEliminar += lecturaInventario.getStockInventariado();
                        }
                        dataSourceRepository.updateDetalleLecturaEliminar(cantidadEliminar,idDetaleInventario,body.getCodUsuario(),
                                UtilMethods.formatCurrentDate(new Date()));
                        AndroidSchedulers.mainThread().scheduleDirect(() -> getLecturas(tipoConsulta, valor),50,TimeUnit.MILLISECONDS);
                    }, throwable -> {}));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                ProgressDialog.dismiss();
                getView().onError(() -> deleteDetalle(idDetaleInventario), context.getString(R.string.movil_sin_conexion), 0);
            } else {
                disposable.add(dataSourceRepository.deleteDetalleInventario(body, idDetaleInventario)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(msgResponse -> {
                            if (msgResponse.getMsgError().contains("ELIMINACION CORRECTA")) {
                                // Se actualiza la lista de lecturas luego de la eliminación
                                getLecturas(tipoConsulta, valor);
                            }
                        }, throwable ->
                                getView().onError(() -> getLecturas(tipoConsulta, valor),
                                        context.getString(R.string.no_pudo_eliminar_grupo_lecturas), 3)));
            }
        }
    }

    /**
     * Realiza la eliminación de todas la lecturas realizadas para el conteo actual
     */
    @Override
    public void deleteTotalLecturas() {
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.countLecturasInventarioBD()
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(count -> {
                        if (count != 0){
                            dataSourceRepository.eliminarDetallesAdmin(preferenceManager.getConfig().getCodUsuario()
                                    , UtilMethods.formatCurrentDate(new Date()));
                            dataSourceRepository.eliminarLecturasAdmin();
                            AndroidSchedulers.mainThread().scheduleDirect(() -> getView().showMainScreen(),10 * count,TimeUnit.MILLISECONDS);
                        } else {
                            getView().showMainScreen();
                        }
                    }, throwable -> {}));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                ProgressDialog.dismiss();
                getView().onError(() -> deleteTotalLecturas(), null, 1);
            } else {
                disposable.add(dataSourceRepository.deleteTotalInventario(inventario.getIdInventario()
                        , preferenceManager.getConfig().getCodUsuario())
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(msgResponse -> {
                            if (msgResponse.getMsgError().contains(context.getString(R.string.proceso_correcto))) {
                                // Luego de la eliminación se muestra la pantalla de registro
                                // de lecturas RegistrarInventarioFragment
                                getView().showMainScreen();
                            }
                        }, throwable ->
                                getView().onError(() -> getLecturas(tipoConsulta, valor), context.getString(R.string.no_pudo_eliminar_total_lecturas), 3)));
            }
        }
    }
}
