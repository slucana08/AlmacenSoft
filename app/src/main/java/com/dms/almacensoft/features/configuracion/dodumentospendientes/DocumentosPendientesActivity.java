package com.dms.almacensoft.features.configuracion.dodumentospendientes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.CustomDialog;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link DocumentosPendientesActivity} pantalla de muestreo de los módulos para los cuales se pueden
 * descargar documentos pendientes
 */

public class DocumentosPendientesActivity extends BaseActivity implements DocumentosPendientesContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.doc_pendientes_tablayout)
    TabLayout tabLayout;
    @BindView(R.id.doc_pendientes_view_pager)
    ViewPager viewPager;
    @BindView(R.id.save_image_view)
    ImageView saveImageView;

    @Inject
    DocumentosPendientesPresenter presenter;

    private DocumentosPendientesAdapter adapter;

    private DataRecepcion dataRecepcion = null;
    private DataDespacho dataDespacho = null;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentos_pendientes);

        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        setupNavigation();

        presenter.attachView(this);

        ProgressDialog.show(DocumentosPendientesActivity.this,getString(R.string.recopilando));

        // Elimina la lista actual de Documentos y DetalleDocumentoOri
        presenter.deleteDocumentos();

        // Treamos las clases hacía la base de datos interna
        presenter.getDataClasesOnline();
    }

    private void setupNavigation() {
        setSupportActionBar(toolbar);
        setTitle(R.string.documentos_pendientes);
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
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy(){
        presenter.detachView();
        super.onDestroy();
    }

    /**
     * Se ejecuta cuando ocurre algún error
     * @param event es el procedimiento a seguir
     * @param message es el mensaje a mostrar en el ProgressDialog
     * @param type es el tipo de error ocurrido
     *             0 - EL móvil no tiene conexión a internet
     *             1 - Error de conexión con WS
     *             2 - Límite de intentos al traer las clases
     */
    @Override
    public void onError(CustomDialog.IButton event, String message, int type) {
        switch (type){
            case 0:
                ProgressDialog.show(DocumentosPendientesActivity.this,message);
                UtilMethods.getInternetError(DocumentosPendientesActivity.this,event).show();
                break;
            case 1:
            case 2:
                new CustomDialog.Builder(DocumentosPendientesActivity.this)
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setIcon(R.drawable.ic_alert)
                        .setMessage(getString(type == 1 ? R.string.error_WS : R.string.limite_intentos))
                        .setPositiveButtonLabel(getString(type == 1 ? R.string.reintentar : R.string.label_ok))
                        .setPositiveButtonlistener(() -> {
                            if (type == 1){
                                ProgressDialog.show(DocumentosPendientesActivity.this,message);
                                event.onButtonClick();
                            } else {
                                onBackPressed();
                            }
                        })
                        .setCancelable(false)
                        .build().show();
                break;
        }
    }

    @Override
    public void setUpViews() {
        adapter = new DocumentosPendientesAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);

        saveImageView.setOnClickListener(v -> {
            ArrayList<BodyDetalleDocumentoPendiente> list = new ArrayList<>();
            list.addAll(dataRecepcion.guardar());
            list.addAll(dataDespacho.guardar());
            if (list.isEmpty()) {
                new CustomDialog.Builder(DocumentosPendientesActivity.this)
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setIcon(R.drawable.ic_alert)
                        .setMessage(getString(R.string.seleccionar_documento_modo_batch) + "\n" +
                                getString(R.string.retornar_seleccion))
                        .setPositiveButtonLabel(getString(R.string.label_yes))
                        .setPositiveButtonlistener(() -> { })
                        .setNegativeButtonLabel(getString(R.string.label_no))
                        .setNegativeButtonlistener(this::onBackPressed)
                        .setCancelable(false)
                        .build().show();
            } else {
                Intent data = new Intent();
                data.putParcelableArrayListExtra(Constants.BODY_DETALLE_LIST, list);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
    }

    public interface DataRecepcion{
        List<BodyDetalleDocumentoPendiente> guardar();
    }

    public void setDataRecepcion(DataRecepcion dataRecepcion) {
        this.dataRecepcion = dataRecepcion;
    }

    public interface DataDespacho{
        List<BodyDetalleDocumentoPendiente> guardar();
    }

    public void setDataDespacho(DataDespacho dataDespacho) {
        this.dataDespacho = dataDespacho;
    }
}
