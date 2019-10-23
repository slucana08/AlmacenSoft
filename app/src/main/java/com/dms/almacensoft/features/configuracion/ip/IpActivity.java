package com.dms.almacensoft.features.configuracion.ip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dms.almacensoft.App;
import com.dms.almacensoft.R;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.TextWatcher;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.scanner.CodeCaptureActivity;
import com.google.android.gms.vision.barcode.Barcode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * {@link IpActivity} pantalla en la que se realiza la configuración de la dirección del WebService
 */

public class IpActivity extends BaseActivity implements IpContract.view {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ip_edit_text)
    EditText ipEditText;
    @BindView(R.id.scanning_image_view)
    ImageView scanningImageView;

    @Inject
    IpPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        ButterKnife.bind(this);

        setupNavigation();
        setTitle(R.string.config_ip);

        setupViews();
    }

    private void setupNavigation() {
        setSupportActionBar(toolbar);
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
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void displayIp(String ip) {
        ipEditText.setText(ip);
    }

    private void setupViews() {
        presenter.onViewCreated();

        Glide.with(App.get())
                .load(R.drawable.scanning)
                .into(scanningImageView);

        // Al cambiar la data de ipEditText se realiza el guardado de la dirección del WS
        ipEditText.addTextChangedListener((TextWatcher) s -> presenter.updateIp(s));

        ipEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ipEditText.setEnabled(false);
                UtilMethods.hideKeyboard(IpActivity.this);
                return true;
            }
            return false;
        });
    }

    /**
     * Llama a {@link CodeCaptureActivity} que permite obtener la dirección del WS mediante un código QR
     */
    @OnClick(R.id.scan_button)
    public void onScanClicked() {
        Intent intent = CodeCaptureActivity.newInstanceQr(IpActivity.this);
        startActivityForResult(intent, Constants.INTENT_SCAN);
    }

    @OnClick(R.id.manual_button)
    public void onManualClicked() {
        ipEditText.setEnabled(true);
        ipEditText.requestFocus();
    }

    /**
     * Verifica el resultado de la lectura de el código QR
     * @param requestCode código de solicitud
     * @param resultCode resultado de solicitud
     * @param data información obtenida
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.INTENT_SCAN && data != null) {
                Barcode barcode = data.getParcelableExtra(CodeCaptureActivity.BarcodeObject);
                String ipString = barcode.displayValue;
                // Mostramos la data capturada en la lectura
                displayIp(ipString);
            }
        }
    }

    @Override
    protected void onDestroy(){
        presenter.detachView();
        super.onDestroy();
    }
}
