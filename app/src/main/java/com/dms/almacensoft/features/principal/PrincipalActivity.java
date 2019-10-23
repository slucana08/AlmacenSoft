package com.dms.almacensoft.features.principal;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.entities.dbalmacen.CajaProducto;
import com.dms.almacensoft.data.entities.dbalmacen.Inventario;
import com.dms.almacensoft.features.configuracion.ConfiguracionActivity;
import com.dms.almacensoft.features.recepciondespacho.detalledocumento.DetalleDocumentoFragment;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.features.shared.BaseFragment;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.CustomDialog;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link PrincipalActivity} es la pantalla principal del aplicativo, desde aquí se pueden seleccionar
 * todos los módulos y acceder a la configuración de trabajo que se va a realizar
 */

public class PrincipalActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, PrincipalContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.network_state_image_view)
    ImageView networkImageView;

    Menu menu;

    @Inject
    PrincipalPresenter presenter;

    private Handler handler = new Handler();

    private ActionBarDrawerToggle toggle;

    // determina si se muestra el ícono de menú de navegación o el ícono de retroceso de pantalla
    // - false - ícono de menú de navegación
    // - true - ícono de retroceso de pantalla
    private boolean isHomeAsUp = false;

    private CustomDialog.IButton event;

    private int typeSelected; // 0 - Recepción // 1 - Despacho //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        getActivityComponent().inject(this);

        ButterKnife.bind(this);

        presenter.attachView(this);

        if(presenter.isModoBatch()){
            networkImageView.setImageResource(R.drawable.ic_netwotk_state_gray);
        } else {
            networkImageView.setImageResource(R.drawable.ic_network_state);
            presenter.getEtiquetaBluetooth();
        }

        setSupportActionBar(toolbar);
        replaceFragment(new PrincipalFragment(), getString(R.string.app_name));

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                UtilMethods.hideKeyboard(PrincipalActivity.this);
                boolean check = presenter.getGuiaCerrada();
                int conSinDoc = presenter.getConSinDoc();
                if (!check && typeSelected == 0 && conSinDoc == 1) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    // No permite que el menú de navegación se muestre si es que hay una guía abierta y en proceso
                    new CustomDialog.Builder(PrincipalActivity.this)
                            .setCancelable(false)
                            .setTheme(R.style.AppTheme_Dialog_Error)
                            .setPositiveButtonLabel(getString(R.string.label_ok))
                            .setMessage(getString(R.string.cerra_guia_cambio_modulo))
                            .setIcon(R.drawable.ic_alert)
                            .setPositiveButtonlistener(() -> {
                                drawerLayout.closeDrawer(GravityCompat.START);
                            })
                            .build().show();
                } else if (conSinDoc == 2) {
                    // No se permite que el menú de navegación se muestre si es que hay un nuevo documento creado
                    // desde la opcion sin documento.
                    // Se da la opcion de cancelar el documento desde el cuadro de diálogo
                    drawerLayout.closeDrawer(GravityCompat.START);
                    new CustomDialog.Builder(PrincipalActivity.this)
                            .setCancelable(false)
                            .setTheme(R.style.AppTheme_Dialog_Warning)
                            .setPositiveButtonLabel(getString(R.string.label_yes))
                            .setNegativeButtonLabel(getString(R.string.label_no))
                            .setMessage(getString(R.string.cancelar_documento_cambio_modulo))
                            .setIcon(R.drawable.ic_alert)
                            .setPositiveButtonlistener(() -> {
                                // Si el usuario lo decide se puede cancelar el documento desde el diálogo
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT);
                                if (fragment instanceof DetalleDocumentoFragment) {
                                    ((DetalleDocumentoFragment) fragment).cancelarDocumentoMenu();
                                }
                            })
                            .setNegativeButtonlistener(() -> {
                            })
                            .build().show();
                } else {
                    super.onDrawerOpened(drawerView);
                }
            }
        };

        // Se agrega para controlar que se acción se ejecuta al usar el ícono de menú de navegación
        toolbar.setNavigationOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (isHomeAsUp){
                event.onButtonClick();
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        /*
            Código necesario para remover elementos del Menú en el navigation drawer
         */
        // MenuItem despacho = navigationView.getMenu().getItem(1).setVisible(false);

        if (presenter.getTiempoUltimaLimpiezaBD().contains(getString(R.string.dias_limpieza))){
            new CustomDialog.Builder(PrincipalActivity.this)
                    .setTheme(R.style.AppTheme_Dialog_Error)
                    .setIcon(R.drawable.ic_alert)
                    .setMessage(getString(R.string.limpieza_bd_warning))
                    .setPositiveButtonLabel(getString(R.string.label_ok))
                    .setPositiveButtonlistener(() -> {})
                    .setCancelable(false)
                    .build().show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // Ejecuta el onBackPressed que haya sido implemetado en el fragment actual
            BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT);
            currentFragment.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_recepción) {
            callRecepcionDespacho(0);
            typeSelected = 0;
        } else if (id == R.id.nav_despacho) {
            callRecepcionDespacho(1);
            typeSelected = 1;
        } else if (id == R.id.nav_inventario) {
            // Se trae la información del inventario a utilizar en la aplicación desde el WS
            presenter.getInventario();
        } else if (id == R.id.nav_impresion) {
            if (presenter.getImpresionActiva()){
                presenter.selectFragment(2, getString(R.string.impresion), "IMP");
            } else {
                // Si la impresión de etiquetas está desactivada se muestra un diálogo notificando al usuario
                new CustomDialog.Builder(PrincipalActivity.this)
                        .setMessage(getString(R.string.activar_impresion_configuracion))
                        .setPositiveButtonLabel(getString(R.string.label_ok))
                        .setCancelable(false)
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setIcon(R.drawable.ic_alert)
                        .build().show();
            }
        } else if (id == R.id.nav_configuracion) {
            startActivity(new Intent(PrincipalActivity.this, ConfiguracionActivity.class));
            finish();
        } else if (id == R.id.nav_cerrar_sesion) {
            Runtime.getRuntime().exit(0);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callRecepcionDespacho(int type) {
        handler.postDelayed(() -> {
            if (type == 0) {
                presenter.selectFragment(0, getString(R.string.recepcion_tilde), Constants.RECEPCION);
            } else {
                presenter.selectFragment(0, getString(R.string.despacho), Constants.DESPACHO);
            }
        }, 1000);
    }


    /**
     * Determina el formulario que se mostrará en la pantalla principal
     * @param fragment es el fragmento a mostrar
     * @param message es el título a setear en la barra de acción
     */
    @Override
    public void replaceFragment(Fragment fragment, String message) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment1 = fm.findFragmentByTag(Constants.FRAGMENT);
        if (fragment1 != null) fm.beginTransaction().remove(fragment).commit();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.general_container, fragment, Constants.FRAGMENT);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(message);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public void showInventario(List<Inventario> inventarios) {
        if (inventarios.isEmpty()) {
            // Si no hay un inventario se notifica al usuario de que no se puede acceder a este módulo
            new CustomDialog.Builder(PrincipalActivity.this)
                    .setMessage(getString(R.string.no_inventario_abierto))
                    .setPositiveButtonLabel(getString(R.string.label_ok))
                    .setPositiveButtonlistener(() -> replaceFragment(new PrincipalFragment(),getString(R.string.app_name)))
                    .setCancelable(false)
                    .setTheme(R.style.AppTheme_Dialog_Error)
                    .setIcon(R.drawable.ic_alert)
                    .build().show();
        } else {
            ProgressDialog.show(PrincipalActivity.this, getString(R.string.recopilando));
            presenter.selectFragment(1, getString(R.string.inventario), "INV");
        }
    }

    /**
     * Se ejecuta en cualquier caso de error.
     * @param event   es el método a ejecutar al presionar el botón del cuadro de diálogo
     * @param message es el mensaje a mostrar
     * @param type    determina el error ocurrido
     *                0 - El móvil no tiene conexión a una red
     *                1 - Error de conexión de WS
     *                2 - Límite de intentos de reconexión
     *                3 - Error de conexión de WS al traer la etiqueta del formate de impresión bluetooth
     */
    @Override
    public void onError(CustomDialog.IButton event, String message, int type) {
        switch (type) {
            case 0:
                UtilMethods.getInternetError(PrincipalActivity.this, event).show();
                break;
            case 1:
            case 2:
                new CustomDialog.Builder(PrincipalActivity.this)
                        .setMessage(message)
                        .setPositiveButtonLabel(getString(type == 1 ? R.string.reintentar : R.string.label_ok))
                        .setCancelable(false)
                        .setPositiveButtonlistener(type == 1 ? event :
                                (CustomDialog.IButton) () -> Runtime.getRuntime().exit(0))
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setIcon(R.drawable.ic_alert)
                        .build().show();
                break;
            case 3:
                new CustomDialog.Builder(PrincipalActivity.this)
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setIcon(R.drawable.ic_alert)
                        .setCancelable(false)
                        .setMessage(message)
                        .setNegativeButtonLabel(getString(R.string.label_ok))
                        .setNegativeButtonlistener(() -> {})
                        .setPositiveButtonLabel(getString(R.string.reintentar))
                        .setPositiveButtonlistener(event)
                        .build().show();
        }
    }

    /**
     * Realiza una transición entre el ícono de menú de navegación y el ícono de retroceso de pantallla
     * @param action determina que cambio a realizar
     * @param event proporciona la acción que va a realizar el ícono de retroceso
     */
    public void changeToogleIcon(int action, CustomDialog.IButton event) {
        if (action == 1) {
            setHomeAsUp(true);
            this.event = event;
        } else {
            setHomeAsUp(false);
        }
    }

    /**
     * Realiza una animación circular al realizar el cambio entre íconos
     * @param isHomeAsUp determina que transición se va a realizar
     */
    private void setHomeAsUp(boolean isHomeAsUp){
        if (this.isHomeAsUp != isHomeAsUp) {
            this.isHomeAsUp = isHomeAsUp;
            ValueAnimator anim = isHomeAsUp ? ValueAnimator.ofFloat(0, 1) : ValueAnimator.ofFloat(1, 0);
            anim.addUpdateListener(valueAnimator -> {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                toggle.onDrawerSlide(drawerLayout, slideOffset);
            });
            drawerLayout.setDrawerLockMode(isHomeAsUp ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED);
            anim.setInterpolator(new DecelerateInterpolator());
            // Se puede cambiar la duración de la animación de acuerdo a los requerimientos
            anim.setDuration(500);
            anim.start();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
