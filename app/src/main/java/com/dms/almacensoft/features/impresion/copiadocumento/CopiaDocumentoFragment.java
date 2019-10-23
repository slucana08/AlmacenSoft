package com.dms.almacensoft.features.impresion.copiadocumento;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.entities.dbalmacen.ClaseDocumento;
import com.dms.almacensoft.data.models.impresion.DetalleImpresion;
import com.dms.almacensoft.data.models.impresion.DocumentosCerrados;
import com.dms.almacensoft.features.principal.PrincipalActivity;
import com.dms.almacensoft.features.principal.PrincipalFragment;
import com.dms.almacensoft.features.impresion.ImpresionFragment;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.features.shared.BaseFragment;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.PrinterHelper;
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
 * {@link CopiaDocumentoFragment} pantalla que permite realizar la búsqueda de lecturas o detalles
 * dentro de guias que han sido cerradas en un determinado documento. Permite seleccionar los documentos
 * por tipo.
 * Implementa {@link ImpresionFragment.ImprimirCopia} que permite realizar la impresión dentro de esta pantalla
 * y {@link ImpresionFragment.LimpiarCopia} que permite resetear la búsqueda en esta pantalla
 */

public class CopiaDocumentoFragment extends BaseFragment implements CopiaDocumentoContract.View, ImpresionFragment.ImprimirCopia,
        ImpresionFragment.LimpiarCopia, View.OnTouchListener {

    @BindView(R.id.tipo_doc_label_text_view)
    TextView tipoDocLabelTextView;
    @BindView(R.id.tipo_documneto_spinner)
    Spinner tipoDocumentoSpinner;
    @BindView(R.id.nro_documento_edit_text)
    EditText nroDocumentoEditText;
    @BindView(R.id.nro_guia_spinner)
    Spinner nroGuiaSpinner;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.impresion_empty_text_view)
    TextView emptyTextView;
    @BindView(R.id.impresion_error_image_view)
    ImageView errorImageView;
    @BindView(R.id.impresion_retry_button)
    Button retryButton;
    @BindView(R.id.impresion_recycler_view)
    RecyclerView impresionRecyclerView;
    @BindView(R.id.search_params_linear_layout)
    LinearLayout searchLinearLayout;
    @BindView(R.id.marcar_switch)
    SwitchCompat marcarSwitch;
    @BindView(R.id.mas_image_view)
    ImageView masImageView;
    @BindView(R.id.menos_image_view)
    ImageView menosImageView;
    @BindView(R.id.copias_text_view)
    TextView copiasTextView;

    private int type;
    private int idClaseDocumento;
    private String numDocumento;
    private String nroGuia;

    private int copiasCantidad = 1;

    private Unbinder unbinder;

    boolean spinnerTouch = false;

    private Handler handler = new Handler();

    private Fragment fragment;

    private boolean isTouch = false;

    @Inject
    CopiaDocumentoPresenter presenter;

    @Inject
    CopiaDocumentoAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
    }

    /**
     * @param type determina si la llamada fue realizada desde el menú principal o desde el módulo de recepción
     *             0 - llamada desde el módulo recepción
     *             1 - llamada desde el menú principal
     * @param idClaseDocumento es el identificador de la clase a usar
     *              -1 - llamada desde el menú principal, significa que se deben cargar las clases disponibles
     *           valor - llamada deade el módulo de recepción, significa que se utilizará esta clase de documento
     * @param numDocumento es el número de documento del cual se obtendrán las guías cerradas cuando la llamada
     *                     es desde el modúlo de recepción
     * @return una instancia de {@link CopiaDocumentoFragment}
     */
    public static CopiaDocumentoFragment newInstance(int type, int idClaseDocumento, String numDocumento) {
        CopiaDocumentoFragment f = new CopiaDocumentoFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.TYPE, type);
        args.putInt(Constants.ID_CLASE_DOCUMENTO, idClaseDocumento);
        args.putString(Constants.NUM_DOCUMENTO, numDocumento);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        type = getArguments().getInt(Constants.TYPE);
        idClaseDocumento = getArguments().getInt(Constants.ID_CLASE_DOCUMENTO);
        numDocumento = getArguments().getString(Constants.NUM_DOCUMENTO);

        View view = inflater.inflate(R.layout.fragment_impresion_copia, container, false);

        unbinder = ButterKnife.bind(this, view);

        // Si fue llamado desde el menú principal se debe cargar los tipos de documento
        if (idClaseDocumento == -1) {
            ProgressDialog.show(getActivity(), getActivity().getString(R.string.recopilando));
            presenter.getDataClase();
        }

        // Si hay un valor en numDocumento se debe mostrar este número y cargar los documentos cerrados que tiene
        if (!TextUtils.isEmpty(numDocumento)) {
            nroDocumentoEditText.setText(numDocumento);
            presenter.getDocumentoCerrados(idClaseDocumento, numDocumento);
        }

        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT);
        if (fragment != null && fragment instanceof ImpresionFragment) {
            ((ImpresionFragment) fragment).setImprimirCopia(this);
            ((ImpresionFragment) fragment).setLimpiarCopia(this);
        }

        setUpViews();

        return view;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onDetach() {
        presenter.detachView();
        unbinder.unbind();
        super.onDetach();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setUpViews() {
        if (type == 0) {
            tipoDocLabelTextView.setVisibility(View.GONE);
            tipoDocumentoSpinner.setVisibility(View.GONE);
        } else {
            tipoDocLabelTextView.setVisibility(View.VISIBLE);
            tipoDocumentoSpinner.setVisibility(View.VISIBLE);
        }

        marcarSwitch.setEnabled(false);

        nroDocumentoEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                numDocumento = nroDocumentoEditText.getText().toString().trim();
                if (TextUtils.isEmpty(numDocumento)) {
                    UtilMethods.showToast(getActivity(), getActivity().getString(R.string.debe_ingresar_n_documento));
                    return true;
                } else {
                    ProgressDialog.show(getActivity(), getActivity().getString(R.string.procesando));
                    presenter.getDocumentoCerrados(idClaseDocumento, numDocumento);
                    UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                    nroDocumentoEditText.clearFocus();
                    return false;
                }
            }
            return false;
        });

        marcarSwitch.setEnabled(false);
        menosImageView.setImageResource(R.drawable.ic_minus_gray);
        menosImageView.setEnabled(false);
        menosImageView.setOnClickListener(v -> {
            copiasCantidad -= 1;
            copiasTextView.setText(String.valueOf(copiasCantidad));
            if (copiasCantidad == 1) {
                menosImageView.setImageResource(R.drawable.ic_minus_gray);
                menosImageView.setEnabled(false);
            }
        });
        masImageView.setImageResource(R.drawable.ic_add_gray);
        masImageView.setEnabled(false);
        masImageView.setOnClickListener(v -> {
            copiasCantidad += 1;
            copiasTextView.setText(String.valueOf(copiasCantidad));
            if (copiasCantidad == 2) {
                menosImageView.setImageResource(R.drawable.ic_minus);
                menosImageView.setEnabled(true);
            }
        });
        copiasTextView.setText(String.valueOf(copiasCantidad));
        marcarSwitch.setOnTouchListener(this);
        marcarSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isTouch){
                if (isChecked) {
                    adapter.selectAll(true);
                } else {
                    adapter.selectAll(false);
                }
                isTouch = false;
            }
        });

        // Se ejecuta al presionar el boton desplegable de ImpresionFragment
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT);
        if (fragment != null && fragment instanceof ImpresionFragment)
            ((ImpresionFragment) fragment).setChildAction(new ImpresionFragment.ChildAction() {
                @Override
                public void onClick(int action) {
                    if (action == 0) {
                        searchLinearLayout.setVisibility(View.GONE);
                    } else {
                        searchLinearLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
    }

    @Override
    public void setUpDocumentosCerrados(List<DocumentosCerrados> list) {
        ProgressDialog.dismiss();
        if (list.isEmpty()) {
            UtilMethods.showToast(getContext(), getActivity().getString(R.string.documento_no_guias_cerradas));
            nroDocumentoEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(nroDocumentoEditText.getWindowToken(),
                    InputMethodManager.SHOW_FORCED, 0);
        } else {
            List<String> guias = new ArrayList<>();
            // Agrega un item adicional a la lista para que cuando se defina el setOnItemSelectedListener
            // no se seleccione ningún documento por defecto
            guias.add(getActivity().getString(R.string.n_guia_select));
            for (DocumentosCerrados documentosCerrado : list) {
                guias.add(documentosCerrado.getNumDocInterno());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row, guias);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown);
            nroGuiaSpinner.setAdapter(adapter);
            nroGuiaSpinner.setSelection(-1);
            // Se agrega para que la selección solo se ejecute cuando de verdad se haya tocado el Spinner
            nroGuiaSpinner.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    spinnerTouch = true;
                    return false;
                }
            });
            nroGuiaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerTouch) {
                        // Si el spinner se tocó, siginifica que el usuario realizó una selección
                        if (position == 0) {
                            retryButton.setVisibility(View.GONE);
                            emptyTextView.setVisibility(View.VISIBLE);
                            emptyTextView.setText(getActivity().getString(R.string.copia_impresion_message));
                            errorImageView.setVisibility(View.GONE);
                            impresionRecyclerView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            nroGuia = guias.get(position);
                            retryButton.setVisibility(View.GONE);
                            emptyTextView.setVisibility(View.GONE);
                            errorImageView.setVisibility(View.GONE);
                            impresionRecyclerView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            presenter.getDetalle(numDocumento, nroGuia);
                        }
                        spinnerTouch = false;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    @Override
    public void setUpClases(List<ClaseDocumento> list) {
        ProgressDialog.dismiss();
        List<String> clases = new ArrayList<>();
        if (list.isEmpty()) {
            // Si no hay tipos de documento disponible se regresa a la pantalla del menú principal PrincipalFragment
            new CustomDialog.Builder(getActivity())
                    .setTheme(R.style.AppTheme_Dialog_Error)
                    .setPositiveButtonLabel(getActivity().getString(R.string.label_ok))
                    .setPositiveButtonlistener(() -> ((PrincipalActivity) getActivity()).replaceFragment(new PrincipalFragment(), "AlmacenSoft"))
                    .setIcon(R.drawable.ic_alert)
                    .setMessage(getActivity().getString(R.string.no_clases_disponibles))
                    .setCancelable(false)
                    .build().show();
        } else {
            for (ClaseDocumento clase : list) {
                clases.add(clase.getDscClaseDocumento());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row, clases);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown);
            tipoDocumentoSpinner.setAdapter(adapter);
            tipoDocumentoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    idClaseDocumento = list.get(position).getIdClaseDocumento();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    @Override
    public void setUpDetalle(List<DetalleImpresion> list) {
        if (list == null) {
            // Se llama con null porque es un error, solo para poder reciclar el método
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT);
            if (fragment != null && fragment instanceof ImpresionFragment)
                ((ImpresionFragment) fragment).showMenu(1);
        } else {
            if (list.isEmpty()) {
                retryButton.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
                emptyTextView.setText(getActivity().getString(R.string.no_detalle_documento));
                errorImageView.setVisibility(View.GONE);
                impresionRecyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            } else {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT);
                if (fragment != null && fragment instanceof ImpresionFragment)
                    ((ImpresionFragment) fragment).showMenu(1);
                retryButton.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                errorImageView.setVisibility(View.GONE);
                impresionRecyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                adapter.setList(list);
                impresionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                impresionRecyclerView.setAdapter(adapter);
                searchLinearLayout.setVisibility(View.GONE);
                marcarSwitch.setEnabled(true);
                masImageView.setImageResource(R.drawable.ic_add);
                masImageView.setEnabled(true);
                copiasTextView.setText(String.valueOf(copiasCantidad));
            }
        }
    }

    /**
     * Se ejecuta cuando ocurre algún error
     * @param event es el procedimiento a seguir
     * @param message es el mensaje a mostrar en el cuadro de diálogo o en el ProgressDialog
     * @param type determina el tipo de error ocurrido
     *             0 - El móvil no tiene conexión a una red, error en cuadro de diálogo
     *             1 - El móvil no tiene conexión a una red // Error de conexión con WS al traer detalles - error en grilla
     *             2 - Error de conexión con WS al traer las clases disponibles o al intentar actualizar las etiquetas impresas
     *             3 - Límite de intentos de reconexión al traer las clases disponibles
     *             4 - Error de conexión con WS al imprimir las etiquetas
     *             5 - Límite de intentos de reconexión al imprimir las etiquetas
     */
    @Override
    public void onError(CustomDialog.IButton event, String message, int type) {
        switch (type) {
            case 0:
                ProgressDialog.show(getActivity(), message);
                UtilMethods.getInternetError(getActivity(), event).show();
                break;
            case 1:
                setUpDetalle(null);
                handler.postDelayed(() -> {
                    retryButton.setVisibility(View.VISIBLE);
                    emptyTextView.setVisibility(View.VISIBLE);
                    emptyTextView.setText(message);
                    errorImageView.setVisibility(View.VISIBLE);
                    impresionRecyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }, 100);
                retryButton.setOnClickListener(v -> {
                    retryButton.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.GONE);
                    errorImageView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    event.onButtonClick();
                });
                break;
            case 2:
            case 4:
                new CustomDialog.Builder(getActivity())
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setIcon(R.drawable.ic_alert)
                        .setMessage(type == 2 ? getActivity().getString(R.string.error_WS) : getActivity().getString(R.string.no_pudo_imprimir))
                        .setPositiveButtonLabel(getActivity().getString(R.string.reintentar))
                        .setPositiveButtonlistener(() -> {
                            ProgressDialog.show(CopiaDocumentoFragment.this.getActivity(), message);
                            event.onButtonClick();
                        })
                        .setCancelable(false)
                        .build().show();
                break;
            case 3:
            case 5:
                new CustomDialog.Builder(getActivity())
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setIcon(R.drawable.ic_alert)
                        .setMessage(getActivity().getString(R.string.limite_intentos))
                        .setPositiveButtonLabel(getString(R.string.label_ok))
                        .setPositiveButtonlistener(() -> {
                            if (type == 3) {
                                ((PrincipalActivity) getActivity()).replaceFragment(new PrincipalFragment(),
                                        getActivity().getString(R.string.app_name));
                            } else {
                                marcarSwitch.setChecked(false);
                                adapter.selectAll(false);
                                copiasCantidad = 1;
                                menosImageView.setImageResource(R.drawable.ic_minus_gray);
                                menosImageView.setEnabled(false);
                                copiasTextView.setText(String.valueOf(copiasCantidad));
                            }
                        })
                        .setCancelable(false)
                        .build().show();
                break;
        }
    }

    /**
     * Verififa resultado de impresión por bluetooth
     * @param list es la lista de etiquetas
     */
    @Override
    public void checkPrintResult(List<DetalleImpresion> list) {
        handler.postDelayed(() -> {
            if (PrinterHelper.getResult()) {
                // Si conexión fue exitosa se procede a actualizar las etiquetas
                presenter.actualizarImpresion(list,0);
            } else {
                // Si conexión fue fállida se procede a notificar al usuario
                ProgressDialog.dismiss();
                new CustomDialog.Builder(getActivity())
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setIcon(R.drawable.ic_alert)
                        .setMessage(getActivity().getString(R.string.error_impresion) + " \n" +
                                getActivity().getString(R.string.revisar_impresora))
                        .setPositiveButtonLabel(getString(R.string.label_ok))
                        .setCancelable(false)
                        .build().show();
            }
        }, 6000); // se setea el tiempo a 6 segundos para dar tiempo a que se cree la conexión con la impresora
    }

    @Override
    public void imprimir() {
        List<DetalleImpresion> list = adapter.getListSelected();
        if (list.isEmpty()) {
            UtilMethods.showToast(getActivity(), getActivity().getString(R.string.seleccionar_elemento));
        } else {
            ProgressDialog.show(getActivity(), getActivity().getString(R.string.imprimiendo));
            presenter.imprimirEtiquetas(list, copiasCantidad);
        }
    }

    @Override
    public void limpiar() {
        nroDocumentoEditText.getText().clear();
        nroDocumentoEditText.requestFocus();
        nroGuiaSpinner.setAdapter(null);
        retryButton.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
        emptyTextView.setText(getActivity().getString(R.string.copia_impresion_message));
        errorImageView.setVisibility(View.GONE);
        impresionRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        if (fragment != null && fragment instanceof ImpresionFragment)
            ((ImpresionFragment) fragment).resetMenu();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        isTouch = true;
        return false;
    }
}
