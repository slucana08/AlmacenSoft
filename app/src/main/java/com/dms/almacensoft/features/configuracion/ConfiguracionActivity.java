package com.dms.almacensoft.features.configuracion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.features.configuracion.general.GeneralConfigFragment;
import com.dms.almacensoft.features.configuracion.impresion.ImpresionConfigFragment;
import com.dms.almacensoft.features.configuracion.impresionbluetooth.ImpresionBluetoothActivity;
import com.dms.almacensoft.features.principal.PrincipalActivity;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link ConfiguracionActivity} pantalla donde se definen las configuraciones que se utilizaran en los
 * módulos de la aplicación
 */

public class ConfiguracionActivity extends BaseActivity implements ConfiguracionContract.View{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.configuracion_view_pager)
    ViewPager viewPager;
    @BindView(R.id.configuracion_tablayout)
    TabLayout tabLayout;

    private GeneralListener generalListener;
    private ImpresionListener impresionListener;

    private UbicacionError ubicacionError;

    private Configuracion generalConfig;
    private Configuracion impresionConfig;

    private ConfiguracionAdapter adapter;

    private CustomDialog customDialog;

    private boolean recepcionUbiOk,despachoUbiOk,inventarioUbiOk;

    @Inject
    ConfiguracionPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        setupNavigation();

        presenter.attachView(this);

        adapter = new ConfiguracionAdapter(getSupportFragmentManager(),ConfiguracionActivity.this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupNavigation() {
        setSupportActionBar(toolbar);
        setTitle(R.string.config);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void checkUbicacion(boolean isDefined, int type) {
        if (isDefined) {
            switch (type) {
                case 0:
                    recepcionUbiOk = true;
                    break;
                case 1:
                    despachoUbiOk = true;
                    break;
                case 2:
                    inventarioUbiOk = true;
                    break;
            }
            onBackPressed();
        } else {
            ubicacionError.setError(type);
        }
    }


    /**
     * Al presionar el boton de retroceso se pregunta al usuario si el almacén seleccionado es con el
     * que desea trabajar
     */
    @Override
    public void onBackPressed() {
        generalConfig = generalListener.getConfig();
        impresionConfig = impresionListener.getConfig();

        if (!recepcionUbiOk){
            presenter.verifyUbicacion(generalConfig.getZonaRecepcion(),0);
            return;
        }

        if (!despachoUbiOk){
            presenter.verifyUbicacion(generalConfig.getZonaDespacho(),1);
            return;
        }

        if (!inventarioUbiOk){
            presenter.verifyUbicacion(generalConfig.getZonaInventario(),2);
            return;
        }

        customDialog = new CustomDialog.Builder(ConfiguracionActivity.this)
                .setMessage(getString(R.string.seguro_almacen) + "\n\n" + generalConfig.getDscAlmacen())
                .setPositiveButtonLabel(getString(R.string.label_ok))
                .setNegativeButtonLabel(getString(R.string.label_no))
                .setTheme(R.style.AppTheme_Dialog_Warning)
                .setNegativeButtonlistener(() -> {
                    customDialog.dismiss();
                })
                // Si la respuesta es positiva se procede a guardar las configuraciones
                .setPositiveButtonlistener(() -> {
                    presenter.saveConfig(generalConfig,impresionConfig);
                    customDialog.dismiss();
                })
                .setIcon(R.drawable.ic_alert)
                .build();
        customDialog.show();
    }

    /**
     * Verifica que la impresora bluetooth esté configurada cuando se trabaja en modo batch
     */
    public void checkImpresora(){
        generalConfig = generalListener.getConfig();
        impresionConfig = impresionListener.getConfig();

        if (generalConfig.getTipoConexion() && impresionConfig.getActivarImpresora()){
            impresionConfig.setTipoImpresora(true);
            if (presenter.checkImpresoraBluetooth()){
                impresionListener.resetConfig(impresionConfig);
                onBackPressed();
            } else {
                UtilMethods.showToast(ConfiguracionActivity.this,
                        getString(R.string.debe_seleccionar_impresora));
                startActivityForResult(new Intent(ConfiguracionActivity.this,
                        ImpresionBluetoothActivity.class), Constants.INTENT_BLUETOOTH);
            }
        } else {
            onBackPressed();
        }
    }

    /**
     * Captura la respuesta de la selección de una impresora bluetooth en {@link ImpresionBluetoothActivity}
     * @param requestCode es el código de la solicitud
     * @param resultCode es el resultado de la solicitud
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (TextUtils.isEmpty(presenter.getPrinterAddress())){
                    // Si no se seleccionó se notifica al usuario
                    customDialog = new CustomDialog.Builder(ConfiguracionActivity.this)
                            .setMessage(getString(R.string.no_selecciona_impresora) + "\n\n" +
                                    getString(R.string.seleccionar_impresora_pregunta))
                            .setPositiveButtonLabel(getString(R.string.label_ok))
                            .setNegativeButtonLabel(getString(R.string.label_no))
                            .setTheme(R.style.AppTheme_Dialog_Warning)
                            // Si la respuesta es negativa se bloquea la impresión
                            .setNegativeButtonlistener(() -> {
                                impresionConfig.setActivarImpresora(false);
                                impresionListener.resetConfig(impresionConfig);
                                onBackPressed();
                                customDialog.dismiss();
                            })
                            // Si la respuesta es positiva se da la opción de seleccionar la impresora
                            .setPositiveButtonlistener(() -> {
                                startActivityForResult(new Intent(ConfiguracionActivity.this,
                                        ImpresionBluetoothActivity.class), Constants.INTENT_BLUETOOTH);
                                customDialog.dismiss();
                            })
                            .setIcon(R.drawable.ic_alert)
                            .build();
                    customDialog.show();
                } else {
                    impresionListener.resetConfig(impresionConfig);
                    onBackPressed();
                }
            } else {
                customDialog = new CustomDialog.Builder(ConfiguracionActivity.this)
                        .setMessage("No hay dispositivos conectados por bluetooth")
                        .setPositiveButtonLabel(getString(R.string.label_ok))
                        .setTheme(R.style.AppTheme_Dialog_Warning)
                        // Si la respuesta es positiva se da la opción de seleccionar la impresora
                        .setPositiveButtonlistener(() -> {
                            impresionConfig.setActivarImpresora(false);
                            impresionListener.resetConfig(impresionConfig);
                            onBackPressed();
                        })
                        .setIcon(R.drawable.ic_alert)
                        .build();
                customDialog.show();
            }
        }
    }

    @Override
    public void showMainMenu() {
        startActivity(new Intent(ConfiguracionActivity.this, PrincipalActivity.class));
        finish();
    }

    /**
     * Se ejecuta cuando ocurre un error
     * @param event es el procedimiento a seguir
     * @param message es el mensaje a mostrar
     * @param type determina la procedencia del error
     *             0 - El móvil no tiene conexión a una red
     *             1 - Error de conexión con WS
     *             2 - Limite de reintentos de reconexión
     */

    @Override
    public void onError(CustomDialog.IButton event, String message, int type) {
        switch (type){
            case 0:
                UtilMethods.getInternetError(ConfiguracionActivity.this,event).show();
                break;
            case 1:
            case 2:
                new CustomDialog.Builder(ConfiguracionActivity.this)
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setIcon(R.drawable.ic_alert)
                        .setMessage(type == 1 ? message : getString(R.string.limite_intentos))
                        .setCancelable(false)
                        .setPositiveButtonLabel(getString(type == 1 ? R.string.reintentar : R.string.label_ok))
                        .setPositiveButtonlistener(type == 1 ? event :
                                (CustomDialog.IButton) () -> Runtime.getRuntime().exit(0))
                        .build();
                break;
        }
    }

    /**
     * Interfaz que permite capturar la configuración establecida en {@link GeneralConfigFragment}
     */
    public interface GeneralListener {
        Configuracion getConfig();
    }

    public void setGeneralListener (GeneralListener generalListener){
        this.generalListener = generalListener;
    }

    /**
     * Interfaz que permite capturar la configuración establecida en {@link ImpresionConfigFragment}
     */
    public interface ImpresionListener {
        Configuracion getConfig();
        void resetConfig(Configuracion configuracion);
    }

    public void setImpresionListener(ImpresionListener impresionListener){
        this.impresionListener = impresionListener;
    }

    public Configuracion getGeneralConfig() {
        return generalListener.getConfig();
    }

    public interface UbicacionError {
        void setError(int type);
    }

    public void setUbicacionError(UbicacionError ubicacionError) {
        this.ubicacionError = ubicacionError;
    }

    @Override
    protected void onDestroy(){
        presenter.detachView();
        super.onDestroy();
    }
}
