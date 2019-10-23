package com.dms.almacensoft.features.configuracion.impresion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.features.configuracion.ConfiguracionActivity;
import com.dms.almacensoft.features.configuracion.impresionbluetooth.ImpresionBluetoothActivity;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.features.shared.BaseFragment;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.UtilMethods;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * {@link ImpresionConfigFragment} muestra las configuraciones relacionadas al módulo de impresión
 * del aplicativo.
 * Desde aquí se realiza la llamada {@link ImpresionBluetoothActivity} para cuando se solicita la impresión por
 * bluetooth
 * Implementa {@link ConfiguracionActivity.ImpresionListener} que permite enviar la data de configuración a
 * {@link ConfiguracionActivity} para luego ser procesada
 */

public class ImpresionConfigFragment extends BaseFragment implements ImpresionConfigContract.View,
        ConfiguracionActivity.ImpresionListener, View.OnTouchListener {

    private Unbinder unbinder;

    @BindView(R.id.impresora_switch)
    SwitchCompat impresoraSwitch;
    @BindView(R.id.tipo_impresora_switch)
    SwitchCompat tipoImpresoraSwitch;
    @BindView(R.id.impresion_textInputLayout)
    TextInputLayout nombreImpresoraEditText;
    @BindView(R.id.registros_switch)
    SwitchCompat registrosSwitch;
    @BindView(R.id.lote_impresion_switch)
    SwitchCompat loteImpresionSwitch;
    @BindView(R.id.serie_impresion_switch)
    SwitchCompat serieImpresionSwitch;
    @BindView(R.id.serie_label_text_view)
    TextView serieLabelTextView;
    @BindView(R.id.serie_linear_layout)
    LinearLayout serieLinearLayout;

    private String impresoraRed;

    private boolean isTouch = false;

    private Configuracion configGeneralTemp;

    @Inject
    ImpresionConfigPresenter presenter;

    public ImpresionConfigFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ((ConfiguracionActivity) getActivity()).setImpresionListener(this);
        presenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_impresion_config, container, false);

        unbinder = ButterKnife.bind(this,view);

        presenter.getConfig();

        return view;
    }


    @Override
    public void onDetach() {
        presenter.detachView();
        unbinder.unbind();
        super.onDetach();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setUpConfig(Configuracion config) {
        impresoraRed = config.getNombreImpresora();
        impresoraSwitch.setChecked(config.getActivarImpresora());
        tipoImpresoraSwitch.setEnabled(impresoraSwitch.isChecked());
        registrosSwitch.setEnabled(impresoraSwitch.isChecked());
        loteImpresionSwitch.setEnabled(impresoraSwitch.isChecked());
        serieImpresionSwitch.setEnabled(impresoraSwitch.isChecked());

        if (config.getTipoImpresora() && !TextUtils.isEmpty(presenter.getPrinterAddress())){
            tipoImpresoraSwitch.setChecked(true);
        } else {
            tipoImpresoraSwitch.setChecked(false);
        }
        nombreImpresoraEditText.setEnabled(false);
        nombreImpresoraEditText.getEditText().setText(tipoImpresoraSwitch.isChecked() ? presenter.getPrinterAddress() : impresoraRed);

        impresoraSwitch.setOnTouchListener(this);
        impresoraSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            configGeneralTemp = ((ConfiguracionActivity) getActivity()).getGeneralConfig();
            if (isTouch){
                if (isChecked && configGeneralTemp.getTipoConexion()){
                    // Si se activa la impresora y se está en modo batch solo permite la impresión por bluetooth
                    tipoImpresoraSwitch.setEnabled(true);
                    isTouch = false;
                    if (!tipoImpresoraSwitch.isChecked()) {
                        tipoImpresoraSwitch.setChecked(true);
                        Intent intent = new Intent(getActivity(), ImpresionBluetoothActivity.class);
                        startActivityForResult(intent, Constants.INTENT_BLUETOOTH);
                    }
                    registrosSwitch.setEnabled(true);
                    loteImpresionSwitch.setEnabled(true);
                    serieImpresionSwitch.setEnabled(true);
                } else {
                    tipoImpresoraSwitch.setEnabled(isChecked);
                    registrosSwitch.setEnabled(isChecked);
                    loteImpresionSwitch.setEnabled(isChecked);
                    serieImpresionSwitch.setEnabled(isChecked);
                }
                isTouch = false;
            }
        });

        tipoImpresoraSwitch.setOnTouchListener(this);
        tipoImpresoraSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isTouch) {
                configGeneralTemp = ((ConfiguracionActivity) getActivity()).getGeneralConfig();
                if (tipoImpresoraSwitch.isChecked()) {
                    // Si se cambia el tipo de impresora a Bluetooth se llama a la pantalla de configuración
                    // de impresión por bluetooth (ImpresionBluetoothActivity)
                    Intent intent = new Intent(getActivity(), ImpresionBluetoothActivity.class);
                    startActivityForResult(intent, Constants.INTENT_BLUETOOTH);
                } else {
                    if (configGeneralTemp.getTipoConexion()){
                        UtilMethods.showToast(getActivity(),getActivity().getString(R.string.impresion_bluetooth_batch));
                        tipoImpresoraSwitch.setChecked(true);
                    } else {
                        nombreImpresoraEditText.getEditText().setText(impresoraRed);
                    }
                }
                isTouch = false;
            }
        });

        registrosSwitch.setChecked(config.getImpresionRegistros());
        loteImpresionSwitch.setChecked(config.getLoteImpresion());
        serieImpresionSwitch.setChecked(config.getSerieImpresion());
        if (!registrosSwitch.isChecked()) {
            serieLabelTextView.setVisibility(View.VISIBLE);
            serieLinearLayout.setVisibility(View.VISIBLE);
        } else {
            serieLabelTextView.setVisibility(View.GONE);
            serieLinearLayout.setVisibility(View.GONE);
            serieImpresionSwitch.setChecked(false);
        }

        registrosSwitch.setOnTouchListener(this);
        registrosSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isTouch){
                if (!registrosSwitch.isChecked()) {
                    serieLabelTextView.setVisibility(View.VISIBLE);
                    serieLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    serieLabelTextView.setVisibility(View.GONE);
                    serieLinearLayout.setVisibility(View.GONE);
                    serieImpresionSwitch.setChecked(false);
                }
                isTouch = false;
            }
        });
    }

    /**
     * @return la configuracion capturada de esta pantalla para ser procesada por {@link ConfiguracionActivity}
     */
    @Override
    public Configuracion getConfig() {
        Configuracion config = new Configuracion();
        config.setActivarImpresora(impresoraSwitch.isChecked());
        config.setTipoImpresora(tipoImpresoraSwitch.isChecked());
        config.setNombreImpresora(impresoraRed);
        config.setImpresionRegistros(registrosSwitch.isChecked());
        config.setLoteImpresion(loteImpresionSwitch.isChecked());
        config.setSerieImpresion(serieImpresionSwitch.isChecked());
        return config;
    }

    @Override
    public void resetConfig(Configuracion configuracion) {
        setUpConfig(configuracion);
    }

    @Override
    public void onBackPressed() {

    }

    /**
     * Captura el resultado de la selección de la impresora Bluetooth a utilizar en {@link ImpresionBluetoothActivity}
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        configGeneralTemp = ((ConfiguracionActivity) getActivity()).getGeneralConfig();
        if (requestCode == Constants.INTENT_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (TextUtils.isEmpty(presenter.getPrinterAddress())){
                    // Si no se seleccionó una impresora bluetooth se cambia a impresión por red
                    UtilMethods.showToast(getActivity(),getActivity().getString(R.string.seleccionar_impresora));
                    tipoImpresoraSwitch.setChecked(false);
                    if (configGeneralTemp.getTipoConexion()) {
                        impresoraSwitch.setChecked(false);
                        tipoImpresoraSwitch.setEnabled(false);
                        registrosSwitch.setEnabled(false);
                        loteImpresionSwitch.setEnabled(false);
                        serieImpresionSwitch.setEnabled(false);
                    }
                    nombreImpresoraEditText.getEditText().setText(impresoraRed);
                } else {
                    nombreImpresoraEditText.getEditText().setText(presenter.getPrinterAddress());
                }
            } else {
                // Significa que no hay dispositivos pareados con Android
                UtilMethods.showToast(getActivity(),"No hay dispositivos conectados por bluetooth");
                tipoImpresoraSwitch.setChecked(false);
                if (configGeneralTemp.getTipoConexion()) {
                    impresoraSwitch.setChecked(false);
                    tipoImpresoraSwitch.setEnabled(false);
                    registrosSwitch.setEnabled(false);
                    loteImpresionSwitch.setEnabled(false);
                    serieImpresionSwitch.setEnabled(false);
                }
                nombreImpresoraEditText.getEditText().setText(impresoraRed);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        isTouch = true;
        return false;
    }
}
