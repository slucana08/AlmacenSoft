package com.dms.almacensoft.features.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dms.almacensoft.App;
import com.dms.almacensoft.R;
import com.dms.almacensoft.features.configuracion.almacen.AlmacenActivity;
import com.dms.almacensoft.features.configuracion.ip.IpActivity;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.CustomDialog;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link LoginActivity} es la pantalla de login, desde aqui se puede ingresar al menú principal del
 * aplicativo luego de verificar la credenciales ingresadas. También se puede ir a {@link IpActivity}
 * para realizar la configuración de la dirección del WebService.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.config_image_view)
    ImageView configImageView;
    @BindView(R.id.login_text_view)
    TextView loginTextView;
    @BindView(R.id.usuario_text_input)
    TextInputLayout usuarioText;
    @BindView(R.id.password_text_input)
    TextInputLayout passwordText;
    @BindView(R.id.network_state_image_view)
    ImageView networkImageView;

    @Inject
    LoginPresenter presenter;

    private String imei;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        presenter.attachView(this);

        networkImageView.setImageResource(presenter.isModoBatch() ? R.drawable.ic_netwotk_state_gray :
                R.drawable.ic_network_state );

        configImageView.setVisibility(presenter.isModoBatch() ? View.GONE : View.VISIBLE);

        // Capturamos la IMEI del equipo (AndroidID para versiones superiores de Android - 7.0 en adelante)
        getImei();

        // Llama a la pantalla de configuración de dirección de IP del WS - IpActivity
        configImageView.setOnClickListener(v -> startActivityForResult(new Intent(LoginActivity.this,
                IpActivity.class), Constants.INTENT_IP));

        loginTextView.setOnClickListener(v -> {
            ProgressDialog.show(LoginActivity.this, getString(R.string.procesando));
            String codUsuario = usuarioText.getEditText().getText().toString().trim();
            String claveUsuario = passwordText.getEditText().getText().toString().trim();
            if (TextUtils.isEmpty(codUsuario) || TextUtils.isEmpty(claveUsuario)) {
                UtilMethods.showToast(LoginActivity.this, getString(R.string.ingresar_ambos_campos));
                ProgressDialog.dismiss();
            } else {
                presenter.verificarLicencia(imei,codUsuario, claveUsuario);
            }
        });
    }

    /**
     * Al recibir la respuesta de que la dirección del WS fue configurada en {@link IpActivity} realizamos
     * la verificación de licencia del equipo.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.INTENT_IP) {
                ProgressDialog.show(LoginActivity.this, getString(R.string.verificando_licencia));
                // Reconstruimos el mapa de inyección de dependencias para trabajar con la dirección del WS
                // que fue ingresada
                App.buildDependencyInjection();
                buildComponent();
                getActivityComponent().inject(this);

                presenter.attachView(this);

                // Esperamos un tiempo a que el mapa de inyección de dependencias sea construído antes
                // de verificar la licencia
                new Handler().postDelayed(() -> presenter.verificarLicencia(imei), 3000);
            }
        }
    }

    /**
     * Captura el IMEI del equipo para poder realizar el licenciamiento, AndroidID para versiones de Android - 7.0 en adelante
     * Se debe solicitar permiso al usuario en ejecución en versiones superiores a Android 6.0
     */
    @SuppressLint("HardwareIds")
    private void getImei() {
        // Verificamos si permiso ha sido otorgado por el usuario
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) LoginActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                imei = telephonyManager.getImei();
            } else{
                imei = telephonyManager.getDeviceId();
            }
            if (imei == null){
                imei = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Capturamos el ID del equipo si el permiso es otorgado
                    getImei();
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // De no ser otorgado lo solicitamos nuevamente
                   ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                }
            }
        }
    }

    /**
     * Se ejecuta luego que el login fue exitoso y muestra la pantalla de selección de almacen
     * {@link AlmacenActivity}
     */
    @Override
    public void showAlmacenSelección() {
        ProgressDialog.dismiss();
        startActivity(new Intent(LoginActivity.this, AlmacenActivity.class));
        finish();
    }

    /**
     * Se ejecuta al finalizar la verificación de licenciamiento del equipo
     * @param action determina el procedimiento a seguir
     *               0 - Licencia verificafa u otorgada
     *               1 - Equipo no licenciado luego de intentar realizar un Login
     *               2 - No se puede licenciar més equipos
     */
    @Override
    public void showLicenciaResult(int action) {
        ProgressDialog.dismiss();
        switch (action){
            case 0:
                new CustomDialog.Builder(LoginActivity.this)
                        .setMessage(getString(R.string.licenciado))
                        .setPositiveButtonLabel(getString(R.string.label_ok))
                        .setTheme(R.style.AppTheme_Dialog)
                        .setPositiveButtonlistener(() -> {})
                        .setIcon(R.drawable.ic_alert)
                        .build().show();
                break;
            case 1:
                new CustomDialog.Builder(LoginActivity.this)
                        .setMessage(getString(R.string.no_licenciado))
                        .setPositiveButtonLabel(getString(R.string.label_ok))
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setPositiveButtonlistener(() -> {
                            Runtime.getRuntime().exit(0);
                        })
                        .setIcon(R.drawable.ic_alert)
                        .build().show();
                break;
            case 2:
                new CustomDialog.Builder(LoginActivity.this)
                        .setMessage(getString(R.string.no_mas_licencias))
                        .setPositiveButtonLabel(getString(R.string.label_ok))
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setPositiveButtonlistener(() -> {
                            Runtime.getRuntime().exit(0);
                        })
                        .setIcon(R.drawable.ic_alert)
                        .build().show();
                break;
        }
    }

    @Override
    public void clearValues() {
        usuarioText.getEditText().getText().clear();
        passwordText.getEditText().getText().clear();
        usuarioText.getEditText().requestFocus();
    }


    /**
     * Se llama cuando no hay una conexión de red
     * @param iButton procedimiento a seguir
     * @param message mensaje a mostrar
     */
    @Override
    public void showNoInternet(CustomDialog.IButton iButton, String message) {
        ProgressDialog.show(LoginActivity.this, message);
        UtilMethods.getInternetError(LoginActivity.this,iButton).show();
    }

    /**
     * Se llama cuando ocurre algún error de WS
     * @param iButton procedimiento a seguir
     * @param message mensaje a mostrar o mensaje a agregar en {@link ProgressDialog}
     * @param type determina la procedencia del error
     *             0 - Error de conexión al WS con reintento, se muestra en cuadro de diálogo
     *             1 - Limite de intentos de reconexión
     *             2 - Error de conexión de WS al registrar una nueva licencia
     */

    @Override
    public void onError(CustomDialog.IButton iButton, String message, int type) {
        if (type == 0) {
            ProgressDialog.show(LoginActivity.this, message);
            new CustomDialog.Builder(LoginActivity.this)
                    .setMessage(getString(R.string.error_WS))
                    .setIcon(R.drawable.ic_alert)
                    .setCancelable(false)
                    .setPositiveButtonLabel(getString(R.string.reintentar))
                    .setPositiveButtonlistener(iButton)
                    .setTheme(R.style.AppTheme_Dialog_Error)
                    .build().show();
        } else {
            new CustomDialog.Builder(LoginActivity.this)
                    .setMessage(type == 2 ? message : getString(R.string.reintentar))
                    .setIcon(R.drawable.ic_alert)
                    .setCancelable(false)
                    .setPositiveButtonLabel(getString(R.string.label_ok))
                    .setPositiveButtonlistener(() -> Runtime.getRuntime().exit(0))
                    .setTheme(R.style.AppTheme_Dialog_Error)
                    .build().show();
        }
    }

    @Override
    public void onBackPressed() {

    }


}
