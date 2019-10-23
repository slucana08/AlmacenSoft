package com.dms.almacensoft.features.configuracion;

import android.content.Context;
import android.text.TextUtils;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.entities.dbalmacen.Ubicacion;
import com.dms.almacensoft.data.models.BodyUbicacion;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.features.configuracion.general.GeneralConfigFragment;
import com.dms.almacensoft.features.configuracion.impresion.ImpresionConfigFragment;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link ConfiguracionPresenter} realizar las llamadas el WS y el registro en preferencias para
 * {@link ConfiguracionActivity}
 */

public class ConfiguracionPresenter implements ConfiguracionContract.Presenter {

    private ConfiguracionContract.View view;
    private DataSourceRepository dataSourceRepository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private PreferenceManager preferenceManager;
    private Context context;

    // Contador de reintentos en error de conexión al WS
    private int configCount = 0;

    @Inject
    public ConfiguracionPresenter(DataSourceRepository dataSourceRepository, PreferenceManager preferenceManager,
                                  Context context){
        this.dataSourceRepository = dataSourceRepository;
        this.preferenceManager = preferenceManager;
        this.context = context;
    }


    @Override
    public void attachView(ConfiguracionContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
        disposable.clear();
    }

    @Override
    public ConfiguracionContract.View getView() {
        return view;
    }

    /**
     * Realiza el guardado de la configuración establecida en las preferencias del equipo y el WS
     * @param generalConfig configuraciones capturadas de {@link GeneralConfigFragment}
     * @param impresionConfig configuraciones capturadas de {@link ImpresionConfigFragment}
     */
    @Override
    public void saveConfig(Configuracion generalConfig, Configuracion impresionConfig) {
        Configuracion config = new Configuracion();
        config.setIdAlmacen(generalConfig.getIdAlmacen());
        config.setDscAlmacen(generalConfig.getDscAlmacen());
        config.setCodAlmacen(generalConfig.getCodAlmacen());
        config.setCodEmpresa(generalConfig.getCodEmpresa());
        config.setIdEmpresa(generalConfig.getIdEmpresa());
        config.setModoLectura(generalConfig.getModoLectura());
        config.setLote(generalConfig.getLote());
        config.setSerie(generalConfig.getSerie());
        config.setTipoConexion(generalConfig.getTipoConexion()); // Se envía falso para que siempre que el aplicativo reinicie, lo haga en ModoLínea
        config.setHabilitarScanner(generalConfig.getHabilitarScanner());
        config.setActivarImpresora(impresionConfig.getActivarImpresora());
        config.setTipoImpresora(impresionConfig.getTipoImpresora());
        config.setNombreImpresora(impresionConfig.getNombreImpresora());
        config.setImpresionRegistros(impresionConfig.getImpresionRegistros());
        config.setCodigoProductoImpresion(impresionConfig.getCodigoProductoImpresion());
        config.setLoteImpresion(impresionConfig.getLoteImpresion());
        config.setSerieImpresion(impresionConfig.getSerieImpresion());
        config.setZonaRecepcion(generalConfig.getZonaRecepcion());
        config.setZonaDespacho(generalConfig.getZonaDespacho());
        config.setZonaInventario(generalConfig.getZonaInventario());
        String codUsuario = preferenceManager.getConfig().getCodUsuario();
        config.setCodUsuario(codUsuario);
        config.setPerfilTipo(preferenceManager.getConfig().getPerfilTipo());
        config.setIdUsuario(preferenceManager.getConfig().getIdUsuario());
        if (generalConfig.getTipoConexion()){
            preferenceManager.saveConfig(config);
            getView().showMainMenu();
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> saveConfig(generalConfig, impresionConfig), null, 0);
            } else {
                disposable.add(dataSourceRepository.getConfiguracioByUser(preferenceManager.getConfig().getCodUsuario())
                        .subscribeOn(Schedulers.single())
                        .subscribe(configuracions -> {
                            if (configuracions.isEmpty()){
                                // Si no hay configuración para este ususario se realiza la creación del registro
                                crearConfig(config);
                            } else {
                                // Si la configuración para este usuario existe en la BD principal se realiza una actualización
                                // de registro
                                updateConfig(config,codUsuario);
                            }
                        }, throwable -> {
                            if (configCount < 2) {
                                configCount += 1;
                                getView().onError(() -> saveConfig(generalConfig, impresionConfig), context.getString(R.string.error_WS), 1);
                            } else {
                                configCount = 0;
                                getView().onError(null, null, 2);
                            }
                        }));
            }
        }
    }

    @Override
    public boolean checkImpresoraBluetooth() {
        if (TextUtils.isEmpty(preferenceManager.getPrinterAddress())){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getPrinterAddress() {
        return preferenceManager.getPrinterAddress();
    }

    @Override
    public void crearConfig(Configuracion configuracion) {
        disposable.add(dataSourceRepository.sendConfiguracion(configuracion)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msgResponse -> {
                    if (msgResponse.getMsgError().contains(context.getString(R.string.creado))) {
                        preferenceManager.saveConfig(configuracion);
                        getView().showMainMenu();
                        configCount = 0;
                    }
                }, throwable -> {
                    if (configCount < 2) {
                        configCount += 1;
                        getView().onError(() -> crearConfig(configuracion), context.getString(R.string.error_WS), 1);
                    } else {
                        configCount = 0;
                        getView().onError(null, null, 2);
                    }
                }));
    }

    @Override
    public void updateConfig(Configuracion configuracion, String codUsuario) {
        disposable.add(dataSourceRepository.sendConfiguracionByUser(configuracion, codUsuario)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msgResponse -> {
                    if (msgResponse.getMsgError().contains(context.getString(R.string.modificado))) {
                        preferenceManager.saveConfig(configuracion);
                        getView().showMainMenu();
                        configCount = 0;
                    }
                }, throwable -> {
                    if (configCount < 2) {
                        configCount += 1;
                        getView().onError(() -> updateConfig(configuracion,codUsuario), context.getString(R.string.error_WS), 1);
                    } else {
                        configCount = 0;
                        getView().onError(null, null, 2);
                    }
                }));
    }

    @Override
    public void verifyUbicacion(String codUbicacion, int type) {
        if (TextUtils.isEmpty(codUbicacion)){
            switch (type){
                case 0:
                    preferenceManager.saveRecepcionUbi(new Ubicacion());
                    break;
                case 1:
                    preferenceManager.saveDespachoUbi(new Ubicacion());
                    break;
                case 2:
                    preferenceManager.saveInventarioUbi(new Ubicacion());
                    break;
            }
            getView().checkUbicacion(true,type);
        } else {
            BodyUbicacion body = new BodyUbicacion();
            body.setIdAlmacen(preferenceManager.getConfig().getIdAlmacen());
            body.setCodProducto("");
            body.setCodUbicacion(codUbicacion);
            if (type == 0){
                body.setTipoResultado(type);
            } else {
                body.setTipoResultado(1); // se envia 1 para despacho e inventario
            }
            if (preferenceManager.getConfig().getTipoConexion()){
                // Se ejecuta si esta en modo Batch
                disposable.add(dataSourceRepository.verifyUbicacionBD(body.getIdAlmacen(),codUbicacion)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(ubicacions -> {
                            if (ubicacions.isEmpty()) {
                                getView().checkUbicacion(false,type);
                            } else {
                                getView().checkUbicacion(true, type);
                                switch (type) {
                                    case 0:
                                        preferenceManager.saveRecepcionUbi(ubicacions.get(0));
                                        break;
                                    case 1:
                                        preferenceManager.saveDespachoUbi(ubicacions.get(0));
                                        break;
                                    case 2:
                                        preferenceManager.saveInventarioUbi(ubicacions.get(0));
                                        break;
                                }
                            }
                        }, throwable -> { }));
            } else {
                if (!UtilMethods.checkConnection(context)) {
                    getView().onError(() -> verifyUbicacion(codUbicacion, type), null, 0);
                } else {
                    disposable.add(dataSourceRepository.getUbicacion(body)
                            .subscribeOn(Schedulers.single())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(ubicacions -> {
                                if (ubicacions.isEmpty()) {
                                    getView().checkUbicacion(false, type);
                                } else {
                                    getView().checkUbicacion(true, type);
                                    switch (type) {
                                        case 0:
                                            preferenceManager.saveRecepcionUbi(ubicacions.get(0));
                                            break;
                                        case 1:
                                            preferenceManager.saveDespachoUbi(ubicacions.get(0));
                                            break;
                                        case 2:
                                            preferenceManager.saveInventarioUbi(ubicacions.get(0));
                                            break;
                                    }
                                }
                            }, throwable -> getView().onError(new CustomDialog.IButton() {
                                @Override
                                public void onButtonClick() {
                                    ConfiguracionPresenter.this.verifyUbicacion(codUbicacion, type);
                                }
                            }, context.getString(R.string.error_WS), 1)));
                }
            }
        }
    }
}
