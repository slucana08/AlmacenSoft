package com.dms.almacensoft.features.recepciondespacho.sindocumento;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
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
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.entities.dbalmacen.ClaseDocumento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyCrearDocumento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.features.principal.PrincipalActivity;
import com.dms.almacensoft.features.principal.PrincipalFragment;
import com.dms.almacensoft.features.recepciondespacho.detalledocumento.DetalleDocumentoFragment;
import com.dms.almacensoft.features.recepciondespacho.documentos.DocumentosFragment;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.features.shared.BaseFragment;
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
 * {@link SinDocumentoConfigFragment} pantalla que se muestra cuando el usuario desea trabajar en el modo
 * sin documento. Permite la creación de un nuevo documento, da la opción de seleccionar el tipo de documento
 * a crear. También permite decidir si se ingresará el número del nuevo documento o si el sistema creará un número.
 */

public class SinDocumentoConfigFragment extends BaseFragment implements SinDocumentoConfigContract.View, View.OnTouchListener {

    @BindView(R.id.todo_documento_switch)
    SwitchCompat todoDocumentoSwitch;
    @BindView(R.id.solo_numero_switch)
    SwitchCompat soloNumeroSwitch;
    @BindView(R.id.sin_documento_spinner)
    AppCompatSpinner sinDocumentoSpinner;
    @BindView(R.id.message_text_view)
    TextView messageTextView;
    @BindView(R.id.crear_image_view)
    ImageView crearImageView;
    @BindView(R.id.nro_documento_text_input_layout)
    TextInputLayout nroDocumentoEditText;

    private Unbinder unbinder;

    // Lista que contiene la data de las clases de documento
    private List<ClaseDocumento> claseDocumentos;

    // Es el índice de la clase de documento seleccionada
    private int claseSelected;

    private String type;

    private boolean isTouch = false;

    @Inject
    SinDocumentoConfigPresenter presenter;

    /**
     * @param type es el módulo en el que se está trabajando
     *             "R" - Recepción
     *             "D" - Despacho
     * @return una istancia de {@link SinDocumentoConfigFragment}
     */
    public static SinDocumentoConfigFragment newInstance(String type) {
        SinDocumentoConfigFragment f = new SinDocumentoConfigFragment();
        Bundle args = new Bundle();
        args.putString(Constants.TYPE, type);
        f.setArguments(args);
        return f;
    }

    public SinDocumentoConfigFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        type = getArguments().getString(Constants.TYPE);

        View view = inflater.inflate(R.layout.fragment_recepcion_sin_documento_config, container, false);

        unbinder = ButterKnife.bind(this,view);

        // Obtenemos las clases de documento disponibles
        presenter.getDataClase(type);

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
    public void setUpViews(List<ClaseDocumento> list) {
        List<String> clases = new ArrayList<>();
        if (list.isEmpty()) {
            // Si no hay clases disponibles se notifica al usuario y se regresa a la pantalla princiapal (PrincipalFragment)
            new CustomDialog.Builder(getActivity())
                    .setTheme(R.style.AppTheme_Dialog_Error)
                    .setPositiveButtonLabel(getActivity().getString(R.string.label_ok))
                    .setPositiveButtonlistener(() -> ((PrincipalActivity) getActivity()).replaceFragment(new PrincipalFragment(), "AlmacenSoft"))
                    .setIcon(R.drawable.ic_alert)
                    .setMessage(getActivity().getString(R.string.no_clases_disponibles))
                    .setCancelable(false)
                    .build().show();
        } else {
            claseDocumentos = list;
            messageTextView.setVisibility(View.GONE);
            crearImageView.setEnabled(true);
            for (ClaseDocumento clase : list) {
                clases.add(clase.getDscClaseDocumento());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.spinner_row, clases);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown);
            sinDocumentoSpinner.setAdapter(adapter);
            sinDocumentoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    claseSelected = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    claseSelected = 0;
                }
            });
        }

        todoDocumentoSwitch.setChecked(true);
        soloNumeroSwitch.setChecked(false);
        nroDocumentoEditText.setVisibility(View.GONE);

        todoDocumentoSwitch.setOnTouchListener(this);
        todoDocumentoSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isTouch){
                if (todoDocumentoSwitch.isChecked()){
                    soloNumeroSwitch.setChecked(false);
                    nroDocumentoEditText.setVisibility(View.GONE);
                } else {
                    soloNumeroSwitch.setChecked(true);
                    nroDocumentoEditText.setVisibility(View.VISIBLE);
                    nroDocumentoEditText.getEditText().clearFocus();
                }
                isTouch = false;
            }
        });

        soloNumeroSwitch.setOnTouchListener(this);
        soloNumeroSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isTouch){
                if (soloNumeroSwitch.isChecked()){
                    todoDocumentoSwitch.setChecked(false);
                    nroDocumentoEditText.setVisibility(View.VISIBLE);
                    nroDocumentoEditText.getEditText().clearFocus();
                    nroDocumentoEditText.setErrorEnabled(false);
                    nroDocumentoEditText.getEditText().getText().clear();
                } else {
                    todoDocumentoSwitch.setChecked(true);
                    nroDocumentoEditText.setVisibility(View.GONE);
                }
                isTouch = false;
            }
        });

        // Realiza la creación de documento
        crearImageView.setOnClickListener(v -> {
            if (soloNumeroSwitch.isChecked() &&
                    TextUtils.isEmpty(nroDocumentoEditText.getEditText().getText().toString().trim())){
                nroDocumentoEditText.setErrorEnabled(true);
                nroDocumentoEditText.setError(getActivity().getString(R.string.debe_ingresar_n_documento));
                nroDocumentoEditText.getEditText().requestFocus();
            } else {
                String nroDocumento = nroDocumentoEditText.getEditText().getText().toString().trim();
                ProgressDialog.show(getActivity(), getString(R.string.creando_documento));

                // Se captura la data necesaria de la clase seleccionada para poder crear el documento
                BodyCrearDocumento bodyCrear = new BodyCrearDocumento();
                bodyCrear.setCodClaseDocumento(claseDocumentos.get(claseSelected).getCodClaseDocumento());
                bodyCrear.setCodTipoDocumento(claseDocumentos.get(claseSelected).getCodTipoDocumento());
                bodyCrear.setIdClaseDocumento(claseDocumentos.get(claseSelected).getIdClaseDocumento());
                bodyCrear.setTipoDocDestino(claseDocumentos.get(claseSelected).getCodClaseDocumento());
                // Si la opcion de ingreso de número de documento es manual se envía el número ingresado
                bodyCrear.setNumDocumento(todoDocumentoSwitch.isChecked() ? "" : nroDocumento);
                presenter.crearDocumento(bodyCrear);
            }
        });

        nroDocumentoEditText.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                nroDocumentoEditText.getEditText().clearFocus();
                nroDocumentoEditText.setErrorEnabled(false);
            }
            return false;
        });
    }

    /**
     * Luego de que se crea el documento, se muetra la pantall de detalle {@link DetalleDocumentoFragment}
     * para que se inicialicen las lecturas de este documento
     * @param body es la información necesaria para obtener los detalles del documento creado luego que se
     *             inicialicen sus lecturas
     */
    @Override
    public void showDetalle(BodyDetalleDocumentoPendiente body) {
        ProgressDialog.dismiss();
        presenter.setConSinDoc(2);
        if (TextUtils.equals(Constants.RECEPCION,type))
            ((PrincipalActivity) getActivity()).replaceFragment(DetalleDocumentoFragment.newInstance(body,type,0,false),
                    getActivity().getString(R.string.recepcion_tilde));
        else if (TextUtils.equals(Constants.DESPACHO,type))
            ((PrincipalActivity) getActivity()).replaceFragment(DetalleDocumentoFragment.newInstance(body,type,0,false),
                    getActivity().getString(R.string.despacho));
    }

    /**
     * Se ejecuta cuando ocurre un error
     * @param event es el método a ejecutar
     * @param message es el mensaje a mostrar
     * @param type determina el tipo de error
     *             0 - No internet mensaje en cuadro de diálogo
     *             1 - Error obteniendo las clases de documento, con reintento
     *             2 - Limite de intentos para obtener clases
     *             3 - Error de creación de documento, ya existe
     *             4 - Error de creación de documento
     */
    @Override
    public void onError(CustomDialog.IButton event, String message, int type) {
        switch (type){
            case 0:
                UtilMethods.getInternetError(getActivity(),event).show();
                break;
            case 1:
            case 2:
            case 4:
                new CustomDialog.Builder(getActivity())
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setMessage(message)
                        .setPositiveButtonLabel(getActivity().getString(type == 1 ? R.string.reintentar : R.string.label_ok))
                        .setPositiveButtonlistener(type == 1 ? event : (CustomDialog.IButton) () -> {
                            if (TextUtils.equals(SinDocumentoConfigFragment.this.type,Constants.RECEPCION))
                                ((PrincipalActivity) getActivity()).replaceFragment(DocumentosFragment.newInstance(SinDocumentoConfigFragment.this.type),
                                        getActivity().getString(R.string.recepcion_tilde));
                            else if (TextUtils.equals(SinDocumentoConfigFragment.this.type,Constants.DESPACHO))
                                ((PrincipalActivity) getActivity()).replaceFragment(DocumentosFragment.newInstance(SinDocumentoConfigFragment.this.type),
                                        getActivity().getString(R.string.despacho));
                        })
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_alert)
                        .build().show();
                break;
            case 3:
                nroDocumentoEditText.setErrorEnabled(true);
                nroDocumentoEditText.setError(getActivity().getString(R.string.documento_ya_existe));
                nroDocumentoEditText.getEditText().requestFocus();
                break;
        }
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
