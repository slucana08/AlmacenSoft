package com.dms.almacensoft.features.configuracion.general;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.entities.dbalmacen.Almacen;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.features.configuracion.ConfiguracionActivity;
import com.dms.almacensoft.features.configuracion.dodumentospendientes.DocumentosPendientesActivity;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.features.shared.BaseFragment;
import com.dms.almacensoft.features.sync.SyncDialog;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.CustomDialog;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * {@link GeneralConfigFragment} muestra las configuraciones generales del aplicativo para realizar
 * acciones en los módulos disponibles.
 * Implementa {@link ConfiguracionActivity.GeneralListener} que permite enviar la data de configuración a
 * {@link ConfiguracionActivity} para luego ser procesada
 */

public class GeneralConfigFragment extends BaseFragment implements GeneralConfigContract.View,
        ConfiguracionActivity.GeneralListener, View.OnTouchListener {

    private Unbinder unbinder;
    private int idAlmacenSelected;
    private String dscAlmacenSelected;
    private String codAlmacenSelected;
    private String codEmpresaSelected;
    private int idEmpresaSelected;

    private boolean isTouch = false;

    private SyncDialog syncDialog;

    @BindView(R.id.conexion_switch)
    SwitchCompat conexionSwitch;
    @BindView(R.id.conteo_switch)
    SwitchCompat conteoSwitch;
    @BindView(R.id.lote_switch)
    SwitchCompat loteSwitch;
    @BindView(R.id.serie_switch)
    SwitchCompat serieSwitch;
    @BindView(R.id.camara_switch)
    SwitchCompat camaraSwitch;
    @BindView(R.id.almacen_spinner)
    Spinner almacenSpinner;
    @BindView(R.id.zona_recepcion_textInputLayout)
    TextInputLayout zonaRecepcionEditText;
    @BindView(R.id.zona_despacho_textInputLayout)
    TextInputLayout zonaDespachoEditText;
    @BindView(R.id.zona_inventario_textInputLayout)
    TextInputLayout zonaInventarioEditText;
    @BindView(R.id.limpieza_bd_linear_layout)
    LinearLayout limpiezaBDLinearLayout;
    @BindView(R.id.limpiar_bd_button)
    ImageView limpiarBdButton;
    @BindView(R.id.tiempo_text_view)
    TextView tiempoTextView;

    @Inject
    GeneralConfigPresenter presenter;

    public GeneralConfigFragment(){

    }

    @Override
    public void onDetach() {
        presenter.detachView();
        unbinder.unbind();
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ((ConfiguracionActivity) getActivity()).setGeneralListener(this);
        presenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_config, container, false);
        unbinder = ButterKnife.bind(this,view);

        // Traemos los almacenes disponibles y la configuración registrada en las preferencias del usuario
        presenter.listAlmacen();
        presenter.getConfig();

        ((ConfiguracionActivity) getActivity()).setUbicacionError(new ConfiguracionActivity.UbicacionError() {
            @Override
            public void setError(int type) {
                switch (type){
                    case 0:
                        zonaRecepcionEditText.setError("Ubicación equivocada");
                        break;
                    case 1:
                        zonaDespachoEditText.setError("Ubicación equivocada");
                        break;
                    case 2:
                        zonaInventarioEditText.setError("Ubicación equivocada");
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void setUpSpinner(List<Almacen> almacenList, int idAlmacen) {
        int almacenSelected = 0;
        List<String> spinnerList = new ArrayList<>();
        for (int i = 0; i < almacenList.size(); i++) {
            spinnerList.add(almacenList.get(i).getDscAlmacen());
            if (idAlmacen == almacenList.get(i).getIdAlmacen()){
                almacenSelected = i;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_row, spinnerList );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        almacenSpinner.setAdapter(adapter);
        almacenSpinner.setSelection(almacenSelected);
        almacenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idAlmacenSelected = almacenList.get(position).getIdAlmacen();
                dscAlmacenSelected = almacenList.get(position).getDscAlmacen();
                codAlmacenSelected = almacenList.get(position).getCodAlmacen();
                codEmpresaSelected = almacenList.get(position).getCodEmpresa();
                idEmpresaSelected = almacenList.get(position).getIdEmpresa();
                presenter.saveAlmacenData(idAlmacenSelected,dscAlmacenSelected,codAlmacenSelected,
                        codEmpresaSelected, idEmpresaSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setUpConfig(Configuracion config) {
        conexionSwitch.setChecked(config.getTipoConexion());
        conteoSwitch.setChecked(config.getModoLectura());
        loteSwitch.setChecked(config.getLote());
        if (!config.getModoLectura()){
            serieSwitch.setChecked(false);
            serieSwitch.setEnabled(false);
        } else {
            serieSwitch.setChecked(config.getSerie());
        }
        camaraSwitch.setChecked(config.getHabilitarScanner());
        zonaRecepcionEditText.getEditText().setText(config.getZonaRecepcion());
        zonaDespachoEditText.getEditText().setText(config.getZonaDespacho());
        zonaInventarioEditText.getEditText().setText(config.getZonaInventario());

        //----------------------------------------------------------------------------------------//
        // Si se desea setear zonas (ubicaciones) de trabajo por defecto para Recepción, despacho o inventario
        // se debe realizar la configuración desde esta pantalla. Para ello se debe verificar que la ubicación
        // esté disponible en el almacen seleccionado

        zonaRecepcionEditText.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (!TextUtils.isEmpty(zonaRecepcionEditText.getEditText().getText().toString().trim())) {
                    ProgressDialog.show(getActivity(),getActivity().getString(R.string.verificando_ubicacion));
                }
                presenter.verifyUbicacion(zonaRecepcionEditText.getEditText().getText().toString().trim(), 0,idAlmacenSelected);
                zonaRecepcionEditText.getEditText().clearFocus();
                UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
            }
            return false;
        });

        zonaDespachoEditText.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (!TextUtils.isEmpty(zonaDespachoEditText.getEditText().getText().toString().trim())) {
                    ProgressDialog.show(getActivity(),getActivity().getString(R.string.verificando_ubicacion));
                }
                presenter.verifyUbicacion(zonaDespachoEditText.getEditText().getText().toString().trim(), 1, idAlmacenSelected);
                zonaDespachoEditText.getEditText().clearFocus();
                UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
            }
            return false;
        });

        zonaInventarioEditText.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (!TextUtils.isEmpty(zonaInventarioEditText.getEditText().getText().toString().trim())) {
                    ProgressDialog.show(getActivity(),getActivity().getString(R.string.verificando_ubicacion));
                }
                presenter.verifyUbicacion(zonaInventarioEditText.getEditText().getText().toString().trim(), 2, idAlmacenSelected);
                zonaInventarioEditText.getEditText().clearFocus();
                UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
            }
            return false;
        });
        //-----------------------------------------------------------------------------------------//

        conteoSwitch.setOnTouchListener(this);

        conteoSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isTouch) {
                if (!isChecked) {
                    serieSwitch.setChecked(false);
                    serieSwitch.setEnabled(false);
                } else {
                    serieSwitch.setEnabled(true);
                }
                isTouch = false;
            }
        });

        conexionSwitch.setOnTouchListener(this);

        conexionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isTouch) {
                // Guardamos el valor seleccionado
                presenter.setBatchMode(isChecked);
                if (isChecked) {
                    // Se ejecuta cuando se va a trabajar en batch
                    startActivityForResult(new Intent(getActivity(), DocumentosPendientesActivity.class), Constants.INTENT_DOCUMEMTO_PENDIENTE);
                } else {
                    // Solo se llama cuando se vuelve a trabajar en línea
                    ProgressDialog.show(getActivity(),getActivity().getString(R.string.cerrando_despachos));
                    presenter.countDespachoCerrar(isChecked,0);
                }
                isTouch = false;
            }
        });

        if (config.getPerfilTipo() == 0){
            limpiezaBDLinearLayout.setVisibility(View.VISIBLE);
            limpiarBdButton.setOnClickListener(v -> {
                procesarLimpiezaBD();
            });
            tiempoTextView.setText(presenter.getTiempoUltimaLimpiezaBD());
            if (presenter.getTiempoUltimaLimpiezaBD().contains(getActivity().getString(R.string.dias_limpieza))){
                tiempoTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.red));
            }
        }
    }

    @Override
    public void enviarDatos(boolean isBatch, int type) {
        syncDialog = new SyncDialog.Builder(GeneralConfigFragment.this.getActivity())
                .setTheme(R.style.AppTheme_Dialog)
                .setButtonlistener(success -> {
                    if (!success) {
                        if (type == 0) {
                            // Si la transacción falla, debemos regresar las parámetros a su valor anterior
                            conexionSwitch.setChecked(!isBatch);
                            presenter.setBatchMode(!isBatch);
                        } else {
                            presenter.setBatchMode(isBatch);
                            presenter.getConfig();
                        }
                    } else {
                        if (type == 1) {
                            ProgressDialog.show(GeneralConfigFragment.this.getActivity(),
                                    getActivity().getString(R.string.limpiando_bd));
                            presenter.limpiarBD();
                        }
                    }
                    syncDialog.dismiss();
                })
                .build();
        syncDialog.show();
    }

    /**
     * Pregunta al usuario si desea que los datos trabajdos en la base de datos interna sean cargados
     * al servidor principal. Dependiendo de la respuesta procesa la limpieza de la BD.
     */
    @Override
    public void procesarLimpiezaBD() {
        boolean isBatch = presenter.getBatchMode();
        presenter.setBatchMode(false);
        new CustomDialog.Builder(GeneralConfigFragment.this.getActivity())
                .setTheme(R.style.AppTheme_Dialog_Warning)
                .setIcon(R.drawable.ic_alert)
                .setMessage(getActivity().getString(R.string.cargar_datos_limpiar_BD))
                .setPositiveButtonLabel(GeneralConfigFragment.this.getActivity().getString(R.string.label_yes))
                .setPositiveButtonlistener(() -> {
                    ProgressDialog.show(getActivity(),getActivity().getString(R.string.cerrando_despachos));
                    presenter.countDespachoCerrar(isBatch,1);
                })
                .setNegativeButtonLabel(GeneralConfigFragment.this.getActivity().getString(R.string.label_no))
                .setNegativeButtonlistener(() -> {
                    ProgressDialog.show(GeneralConfigFragment.this.getActivity(), getActivity().getString(R.string.limpiando_bd));
                    presenter.limpiarBD();
                })
                .setCancelable(false)
                .build().show();
    }

    /**
     * Captura la data de los documentos seleccionados en {@link DocumentosPendientesActivity}
     * @param requestCode es el código de la solicitud
     * @param resultCode es el resultado de la solicitud
     * @param data es la data necesaria de los documentos seleccionados
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.INTENT_DOCUMEMTO_PENDIENTE) {
            if (resultCode == Activity.RESULT_OK) {
                List<BodyDetalleDocumentoPendiente> bodyDetalleList = data.getParcelableArrayListExtra(Constants.BODY_DETALLE_LIST);
                syncDialog = new SyncDialog.Builder(GeneralConfigFragment.this.getActivity())
                        .setTheme(R.style.AppTheme_Dialog)
                        .setButtonlistener(success -> {
                            if (!success) {
                                // Si la transacción falla, debemos regresar las parámetros a su valor anterior
                                conexionSwitch.setChecked(!conexionSwitch.isChecked());
                                presenter.setBatchMode(!conexionSwitch.isChecked());
                            } else {
                                ((ConfiguracionActivity) getActivity()).checkImpresora();
                            }
                            syncDialog.dismiss();
                        })
                        .setBodyDetalleList(bodyDetalleList)
                        .build();
                syncDialog.show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                conexionSwitch.setChecked(false);
                presenter.setBatchMode(false);
            }
        }
    }

    /**
     * Se ejecuta luego de realizar la verificació de la ubicación ingresada
     * @param result es el resultado: true - verificación exitosa / false - verificación fállida
     * @param type es el tipo de ubicación a verificar
     *             0 - Recepción
     *             1 - Despacho
     *             2 - Inventario
     */
    @Override
    public void updateUbicacion(boolean result, int type) {
        switch (type){
            case 0:
                if (result){
                    zonaRecepcionEditText.setError(null);
                    UtilMethods.showToast(getActivity(),getActivity().getString(R.string.ubicacion_verificada));
                } else {
                    zonaRecepcionEditText.getEditText().getText().clear();
                    UtilMethods.showToast(getActivity(),getActivity().getString(R.string.ubicacion_invalida));
                }
                break;
            case 1:
                if (result) {
                    zonaDespachoEditText.setError(null);
                    UtilMethods.showToast(getActivity(),getActivity().getString(R.string.ubicacion_verificada));
                } else {
                    zonaDespachoEditText.getEditText().getText().clear();
                    UtilMethods.showToast(getActivity(),getActivity().getString(R.string.ubicacion_invalida));
                }
                break;
            case 2:
                if (result) {
                    zonaInventarioEditText.setError(null);
                    UtilMethods.showToast(getActivity(),getActivity().getString(R.string.ubicacion_verificada));
                } else {
                    zonaInventarioEditText.getEditText().getText().clear();
                    UtilMethods.showToast(getActivity(),getActivity().getString(R.string.ubicacion_invalida));
                }
                break;
        }
        ProgressDialog.dismiss();
    }

    /**
     * Se ejecuta al ocurrir un error
     * @param event procedimiento a seguir
     * @param message mensaje a mostrar
     * @param type determina la procedencia del error
     *             0 - El móvil no tiene conexión a internet
     *             1 - Error de conexión con el WS
     */
    @Override
    public void onError(CustomDialog.IButton event, String message, int type) {
        if (type == 0){
            UtilMethods.getInternetError(getActivity(),event).show();
        } else {
            new CustomDialog.Builder(getActivity())
                    .setTheme(R.style.AppTheme_Dialog_Error)
                    .setIcon(R.drawable.ic_alert)
                    .setMessage(message)
                    .setPositiveButtonLabel(getActivity().getString(R.string.reintentar))
                    .setPositiveButtonlistener(event)
                    .setCancelable(false)
                    .build().show();
        }
    }

    /**
     * @return la configuracion capturada de esta pantalla para ser procesada por {@link ConfiguracionActivity}
     */
    @Override
    public Configuracion getConfig() {
        Configuracion config = new Configuracion();
        config.setIdAlmacen(idAlmacenSelected);
        config.setDscAlmacen(dscAlmacenSelected);
        config.setCodAlmacen(codAlmacenSelected);
        config.setCodEmpresa(codEmpresaSelected);
        config.setIdEmpresa(idEmpresaSelected);
        config.setTipoConexion(conexionSwitch.isChecked());
        config.setModoLectura(conteoSwitch.isChecked());
        config.setLote(loteSwitch.isChecked());
        config.setSerie(serieSwitch.isChecked());
        config.setHabilitarScanner(camaraSwitch.isChecked());
        config.setZonaRecepcion(TextUtils.isEmpty(zonaRecepcionEditText.getEditText().getText().toString().trim()) ?
                "" : zonaRecepcionEditText.getEditText().getText().toString().trim());
        config.setZonaDespacho(TextUtils.isEmpty(zonaDespachoEditText.getEditText().getText().toString().trim()) ?
                "" : zonaDespachoEditText.getEditText().getText().toString().trim());
        config.setZonaInventario(TextUtils.isEmpty(zonaInventarioEditText.getEditText().getText().toString().trim()) ?
                "" : zonaInventarioEditText.getEditText().getText().toString().trim());
        return config;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        isTouch = true;
        return false;
    }
}
