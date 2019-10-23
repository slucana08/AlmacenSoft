package com.dms.almacensoft.features.configuracion.impresionbluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.features.configuracion.impresion.ImpresionConfigFragment;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link ImpresionBluetoothActivity} pantalla que permite seleccionar una impresora bluetooth y devuelve está
 * respuesta a {@link ImpresionConfigFragment}.
 */

public class ImpresionBluetoothActivity extends BaseActivity implements ImpresionBluetoothContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bluetooth_recycler_view)
    RecyclerView bluetoothRecyclerView;
    @BindView(R.id.selection_linear_layout)
    LinearLayout seleccionLinearLayout;
    @BindView(R.id.no_bluetooth_linear_layout)
    LinearLayout noBluetoothLinearLayout;
    @BindView(R.id.bluetooth_config_linear_layout)
    LinearLayout configLinearLayout;
    @BindView(R.id.enable_button)
    AppCompatButton enableButton;
    @BindView(R.id.bluetooth_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.bluetooth_text_view)
    TextView emptyTextView;
    @BindView(R.id.actualizar_button)
    AppCompatButton actualizarButton;

    @Inject
    ImpresionBluetoothPresenter presenter;

    @Inject
    ImpresionBluetoothAdapter adapter;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        presenter.attachView(this);

        setupNavigation();

        searchBluetoothDevices();
    }

    public void searchBluetoothDevices() {
        if (UtilMethods.getBluetoothAdapter() == null) {
            // Si no hay adaptador bluetooth disponible
            displayNoBluetoothError();
        } else if (UtilMethods.getBluetoothAdapter().isEnabled()) {
            // Si el adaptador está encendido, mostramos la lista de dispositivos sincronizados
            List<BluetoothDevice> pairedDevices = new ArrayList<>();
            pairedDevices.addAll(UtilMethods.getBluetoothAdapter().getBondedDevices());
            displayBluetoothDevices(pairedDevices, presenter.getPrinterAddress());
        } else {
            // Si el adaptador está apagado, mostramos la pantalla que nos permitirá hacer la llamada
            // a la pantalla de activación de bluetooth
            displayEnableScreen();
        }
    }

    public void displayBluetoothDevices(List<BluetoothDevice> pairedDevices, String selected) {
        noBluetoothLinearLayout.setVisibility(View.GONE);
        configLinearLayout.setVisibility(View.GONE);
        seleccionLinearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.GONE);

        if (pairedDevices.isEmpty()){
            emptyTextView.setVisibility(View.VISIBLE);
            bluetoothRecyclerView.setVisibility(View.GONE);
            setResult(RESULT_CANCELED);
        } else {
            setResult(RESULT_OK);
            // Se establece la selección si es que ya se había realizado previamente
            adapter.setPrinterAddress(selected);
            adapter.setDevices(pairedDevices);
            // Se guarda la MAC del dispositivo seleccionado en la preferencias del equipo
            adapter.setSelectedAddress(new ImpresionBluetoothAdapter.SelectedAddress() {
                @Override
                public void saveAddress(String printerAdress) {
                    presenter.savePrinterAddress(printerAdress);
                }
            });
            bluetoothRecyclerView.setLayoutManager(new LinearLayoutManager(ImpresionBluetoothActivity.this));
            bluetoothRecyclerView.setAdapter(adapter);
            bluetoothRecyclerView.setVisibility(View.VISIBLE);
        }

        actualizarButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
            bluetoothRecyclerView.setVisibility(View.GONE);
            handler.postDelayed(this::searchBluetoothDevices,500);
        });
    }

    public void displayNoBluetoothError() {
        noBluetoothLinearLayout.setVisibility(View.VISIBLE);
        configLinearLayout.setVisibility(View.GONE);
        seleccionLinearLayout.setVisibility(View.GONE);
    }

    public void displayEnableScreen() {
        noBluetoothLinearLayout.setVisibility(View.GONE);
        configLinearLayout.setVisibility(View.VISIBLE);
        seleccionLinearLayout.setVisibility(View.GONE);
        // Llama a la pantalla de activación de bluetooth de Android
        enableButton.setOnClickListener(v -> {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, Constants.ENABLE_BLUETOOTH_REQUEST);
        });
    }

    /**
     * Verifica la respuesta de la pantalla de activación de bluetooth de Android
     * @param requestCode código de solicitud
     * @param resultCode código de respuesta
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ENABLE_BLUETOOTH_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                // Si se activo el bluetooth volvemos a evaluar la búsqueda de dispositivos sincronizados
                searchBluetoothDevices();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Si no se activo damos la opción de activarlo una vez más
                new CustomDialog.Builder(ImpresionBluetoothActivity.this)
                        .setMessage(getString(R.string.habilitar_bluetooth_impresoras_disponibles))
                        .setPositiveButtonLabel(getString(R.string.label_ok))
                        .setPositiveButtonlistener(() -> enableButton.performClick())
                        .setTheme(R.style.AppTheme_Dialog_Warning)
                        .setIcon(R.drawable.ic_alert)
                        .build().show();
            }
        }
    }

    private void setupNavigation() {
        setSupportActionBar(toolbar);
        setTitle(R.string.impresora_bluetooth);
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
    public void onBackPressed() {
        super.onBackPressed();
    }
}
