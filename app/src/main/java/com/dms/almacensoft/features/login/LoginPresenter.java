package com.dms.almacensoft.features.login;

import android.content.Context;
import android.text.TextUtils;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.entities.dbalmacen.Ubicacion;
import com.dms.almacensoft.data.entities.dbalmacen.Usuario;
import com.dms.almacensoft.data.models.BodyLicencia;
import com.dms.almacensoft.data.models.BodyLogin;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.features.configuracion.ip.IpActivity;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link LoginPresenter} controla todas las llamadas a WS realizadas por {@link LoginActivity}
 */

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private Context context;
    private DataSourceRepository dataSourceRepository;
    private PreferenceManager preferenceManager;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Configuracion configuracion;

    // Contadores para control de reintentos
    private int loginCount = 0;
    private int configCount = 0;
    private int verificarCount = 0;
    private int almacenCount = 0;

    @Inject
    public LoginPresenter (DataSourceRepository dataSourceRepository, PreferenceManager preferenceManager, Context context){
        this.dataSourceRepository = dataSourceRepository;
        this.preferenceManager = preferenceManager;
        this.context = context;
    }

    @Override
    public void attachView(LoginContract.View mvpView) {
        this.view = mvpView;
        // dataSourceRepository.limpiarBdAdmin(); // TODO remover cuando el cambio a batch esté listo
    }

    @Override
    public void detachView() {
        disposable.clear();
        this.view = null;
    }

    @Override
    public LoginContract.View getView() {
        return view;
    }

    /**
     * Realiza las verificación de credenciales
     * @param codUsuario es el código de usuario ingreasado
     * @param claveUsuario es la contraseña ingresada
     */
    @Override
    public void processLogin(String codUsuario, String claveUsuario) {
        BodyLogin body = new BodyLogin();
        body.setCodUsuario(codUsuario);
        body.setClaveUsuario(claveUsuario);
        if (preferenceManager.getConfig().getTipoConexion()) {
            configuracion = preferenceManager.getConfig();
            disposable.add(dataSourceRepository.getUsuario(codUsuario)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(usuarios -> {
                        if (usuarios.isEmpty()){
                            UtilMethods.showToast(context, context.getString(R.string.usuario_no_existe));
                            ProgressDialog.dismiss();
                        } else {
                            if (!TextUtils.equals(usuarios.get(0).getClaveUsuario(),claveUsuario)){
                                UtilMethods.showToast(context, context.getString(R.string.contrana_incorrecta));
                                getView().clearValues();
                                ProgressDialog.dismiss();
                            } else if (TextUtils.equals(usuarios.get(0).getFlgHabilitado(),"0")){
                                UtilMethods.showToast(context, context.getString(R.string.usuario_inactivo));
                                ProgressDialog.dismiss();
                            } else {

                                // Verificamos que tipo de perfil tiene el usuario
                                if (TextUtils.equals(Constants.PERFIL_ADM, usuarios.get(0).getPerfilUsuario())) {
                                    configuracion.setPerfilTipo(0);
                                } else if (TextUtils.equals(Constants.PERFIL_OPE, usuarios.get(0).getPerfilUsuario())) {
                                    configuracion.setPerfilTipo(1);
                                }
                                configuracion.setCodUsuario(usuarios.get(0).getCodUsuario());
                                configuracion.setIdUsuario(String.valueOf(usuarios.get(0).getIdUsuario()));

                                //Actualizamos las configuraciones en las preferencias del equipo
                                preferenceManager.saveConfig(configuracion);
                                ProgressDialog.dismiss();
                                getView().showAlmacenSelección();
                            }
                        }
                    }, throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                ProgressDialog.dismiss();
                getView().showNoInternet(() -> processLogin(codUsuario, claveUsuario),
                        context.getString(R.string.procesando_solicitud));
            } else {
                disposable.add(dataSourceRepository.processLogin(body)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(msgResponse -> {
                            if (msgResponse.getMsgError().contains("Usuario Inactivo")) {
                                UtilMethods.showToast(context, context.getString(R.string.usuario_inactivo));
                                ProgressDialog.dismiss();
                            } else if (msgResponse.getMsgError().contains("Usuario no existe")) {
                                UtilMethods.showToast(context,context.getString(R.string.usuario_no_existe));
                                ProgressDialog.dismiss();
                            } else if (msgResponse.getMsgError().contains("Contraseña incorrecto")) {
                                UtilMethods.showToast(context, context.getString(R.string.usuario_no_existe));
                                getView().clearValues();
                                ProgressDialog.dismiss();
                            } else if (msgResponse.getMsgError().contains("Usuario Activo")) {
                                // Si las credenciales son correctas se procede a cargar la configuración del usuario
                                String[] data = msgResponse.getMsgError().split("\\|");
                                getConfig(data[1], codUsuario, data[2]);
                            }
                        }, throwable -> {
                            ProgressDialog.dismiss();
                            if (loginCount < 2) {
                                loginCount += 1;
                                getView().onError(() -> processLogin(codUsuario, claveUsuario),
                                        context.getString(R.string.procesando), 0);
                            } else {
                                loginCount = 0;
                                getView().onError(null, "", 1);
                            }
                        }));
            }
        }
    }

    @Override
    public void getConfig(String idUsuario, String codUsuario, String perfilTipo) {
        if (!UtilMethods.checkConnection(context)) {
            ProgressDialog.dismiss();
            getView().showNoInternet(() -> getConfig(idUsuario, codUsuario, perfilTipo),
                    context.getString(R.string.procesando));
        } else {
            disposable.add(dataSourceRepository.getConfiguracioByUser(codUsuario)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(configuracions -> {
                        if (!configuracions.isEmpty()) {
                            // Si la configuración para este usuario existe guardamos la data
                            configuracion = configuracions.get(0);
                        } else {
                            // La configuración para este usuario no existe en BD principal
                            configuracion = new Configuracion();
                            configuracion.setCodUsuario(codUsuario);
                        }

                        configuracion.setIdUsuario(idUsuario);

                        // Verificamos que tipo de perfil tiene el usuario
                        if (TextUtils.equals(Constants.PERFIL_ADM, perfilTipo)) {
                            configuracion.setPerfilTipo(0);
                        } else if (TextUtils.equals(Constants.PERFIL_OPE, perfilTipo)) {
                            configuracion.setPerfilTipo(1);
                        }

                        // Guardamos las configuraciones en las preferencias del equipo
                        preferenceManager.saveRecepcionUbi(new Ubicacion());
                        preferenceManager.saveDespachoUbi(new Ubicacion());
                        preferenceManager.saveInventarioUbi(new Ubicacion());
                        preferenceManager.saveConfig(configuracion);

                        // Capturamos la lista de almacenes asignados a este usuario
                        listAlmacen(idUsuario);

                    }, throwable -> {
                        ProgressDialog.dismiss();
                        if (configCount < 2) {
                            configCount += 1;
                            getView().onError(() -> getConfig(idUsuario, codUsuario, perfilTipo),
                                    context.getString(R.string.procesando), 0);
                        } else {
                            configCount = 0;
                            getView().onError(null, "", 1);
                        }
                    }));
        }
    }

    @Override
    public void listAlmacen(String idUsuario){
        if (!UtilMethods.checkConnection(context)){
            ProgressDialog.dismiss();
            getView().showNoInternet(() -> listAlmacen(idUsuario),
                    context.getString(R.string.procesando));
        } else {
            disposable.add(dataSourceRepository.getAllAlmacenByUsuario(idUsuario)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(almacens ->
                                // Luego de guardas los almacenes asignados mostramos la pantalla de selección de almacenes (AlmacenActivity)
                                LoginPresenter.this.getView().showAlmacenSelección(),
                            throwable -> {
                                ProgressDialog.dismiss();
                                if (almacenCount < 2) {
                                    almacenCount += 1;
                                    getView().onError(() -> listAlmacen(idUsuario), context.getString(R.string.procesando), 0);
                                } else {
                                    almacenCount = 0;
                                    getView().onError(null, "", 1);
                                }
                            }));
        }
    }

    /**
     * Verifica la licencia al realizar un login
     * @param imei es el ID único del equipo
     * @param codUsuario es el código de usuario ingreasado
     * @param claveUsuario es la contraseña ingresada
     */
    @Override
    public void verificarLicencia(String imei, String codUsuario, String claveUsuario) {
        preferenceManager.setImei(imei);
        if (preferenceManager.getConfig().getTipoConexion()){
            processLogin(codUsuario, claveUsuario);
        } else {
            if (!UtilMethods.checkConnection(context)) {
                ProgressDialog.dismiss();
                getView().showNoInternet(() -> verificarLicencia(imei, codUsuario, claveUsuario),
                        context.getString(R.string.procesando));
            } else {
                disposable.add(dataSourceRepository.verificarLicencia(imei)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(msgResponse -> {
                            if (msgResponse.getMsgError().contains("No licenciado")) {
                                getView().showLicenciaResult(1);
                            } else if (msgResponse.getMsgError().contains("Imei verificado")) {
                                // Si el equipo esta licenciado procesamos las credenciales
                                processLogin(codUsuario, claveUsuario);
                            }
                        }, throwable -> {
                            ProgressDialog.dismiss();
                            if (verificarCount < 2) {
                                verificarCount += 1;
                                getView().onError(() -> verificarLicencia(imei, codUsuario, claveUsuario),
                                        context.getString(R.string.verificando_licencia), 0);
                            } else {
                                verificarCount = 0;
                                getView().onError(null, "", 1);
                            }
                        }));
            }
        }
    }

    /**
     * Verifica la licencia al volver de {@link IpActivity}
     * @param imei es el ID único del equipo
     */

    @Override
    public void verificarLicencia(String imei) {
        if (!UtilMethods.checkConnection(context)){
            ProgressDialog.dismiss();
            getView().showNoInternet(() -> verificarLicencia(imei),context.getString(R.string.verificando_licencia));
        } else {
            disposable.add(dataSourceRepository.verificarLicencia(imei)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(msgResponse -> {
                        if (msgResponse.getMsgError().contains("No licenciado")) {
                            // Si el equipo no está licenciado procedemos a realizar el registro de licencia
                            registrarLicencia(imei);
                        } else if (msgResponse.getMsgError().contains("Imei verificado")) {
                            getView().showLicenciaResult(0);
                        }
                    }, throwable -> {
                        ProgressDialog.dismiss();
                        getView().onError(() -> verificarLicencia(imei),context.getString(R.string.error_WS),2);
                    }));
        }
    }

    @Override
    public void registrarLicencia(String imei) {
        BodyLicencia body = new BodyLicencia();
        body.setCodAsociado(imei);
        if (!UtilMethods.checkConnection(context)){
            ProgressDialog.dismiss();
            getView().showNoInternet(() -> registrarLicencia(imei),context.getString(R.string.verificando_licencia));
        } else {
            disposable.add(dataSourceRepository.registrarLicencia(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(msgResponse -> {
                        if (msgResponse.getMsgError().contains("Registro guardado")) {
                            // El registro fue exitoso
                            getView().showLicenciaResult(0);
                        } else if (msgResponse.getMsgError().contains("Supera el maximo")) {
                            // No se puede registrar más licencias
                            getView().showLicenciaResult(2);
                        }
                    }, throwable -> {
                        ProgressDialog.dismiss();
                        getView().onError(() -> verificarLicencia(imei),context.getString(R.string.error_WS),2);
                    }));
        }
    }

    @Override
    public boolean isModoBatch() {
        return preferenceManager.getConfig().getTipoConexion();
    }


}
