package com.dms.almacensoft.features.recepciondespacho.documentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.entities.dbtransact.Documento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.features.principal.PrincipalActivity;
import com.dms.almacensoft.features.principal.PrincipalFragment;
import com.dms.almacensoft.features.recepciondespacho.detalledocumento.DetalleDocumentoFragment;
import com.dms.almacensoft.features.recepciondespacho.sindocumento.SinDocumentoConfigFragment;
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
import butterknife.Unbinder;

/**
 * {@link DocumentosFragment} es la pantalla inicial de los módulos de recepción y despacho.
 * Desde esta pantalla se puede seleccionar la clase de documento con la que se trabajará.
 * Si se decide trabajar con documento se puede seleccionar un documento pendiente desde la lista mostrada
 * y se mostrará la pantalla {@link DetalleDocumentoFragment}
 * Si se decide trabajar sin documento se mostrará la pantalla de creación de documento {@link SinDocumentoConfigFragment}
 * Implementa {@link DocumentosAdapter.SelectNroListener} que permite capturar la data necesaria de la
 * selección realizada por el usuaio en {@link DocumentosAdapter}
 */

public class DocumentosFragment extends BaseFragment implements DocumentosContract.View,
        DocumentosAdapter.SelectNroListener {

    @BindView(R.id.documento_spinner)
    Spinner documentosSpinner;
    @BindView(R.id.recepcion_recycler_view)
    RecyclerView recepcionRecyclerView;
    @BindView(R.id.almacen_recepcion_text_view)
    TextView almacenTextView;
    @BindView(R.id.nro_documento_recepcion_edit_text)
    EditText nroDocumentoTextView;
    @BindView(R.id.search_recepcion_image_view)
    ImageView searchRecepcionImageView;
    @BindView(R.id.new_documento_image_view)
    ImageView newDocumentoImageView;
    @BindView(R.id.empty_text_view)
    TextView emptyTextView;

    @Inject
    DocumentosPresenter presenter;

    @Inject
    DocumentosAdapter adapterRecycler;

    private Unbinder unbinder;

    private String claseDocumentoSelected;
    private String type;

    /**
     * @param type es el módulo con el cual se esta trabajando
     *             "R" - Recepción
     *             "D" - Despacho
     * @return una instancia de {@link DocumentosFragment}
     */
    public static DocumentosFragment newInstance(String type){
        DocumentosFragment f =  new DocumentosFragment();
        Bundle args = new Bundle();
        args.putString(Constants.TYPE,type);
        f.setArguments(args);
        return f;
    }

    public DocumentosFragment(){

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

        View view = inflater.inflate(R.layout.fragment_recepcion_documentos, container, false);

        unbinder = ButterKnife.bind(this,view);

        ProgressDialog.show(getActivity(), getActivity().getString(R.string.recopilando));

        // Traemos los tipos de clase de documento desde el WS
        presenter.getDataClase(type);

        // Realiza la búsqueda de documentos pendientes
        searchRecepcionImageView.setOnClickListener(v -> {
            nroDocumentoTextView.clearFocus();
            ProgressDialog.show(getActivity(),getActivity().getString(R.string.procesando));
            String nroDocumento = null;
            if (!TextUtils.isEmpty(nroDocumentoTextView.getText().toString().trim())){
                // Si se ingreso algo a nroDocumentoTextView se utiliza esta data para realizar la búsqueda
                nroDocumento = nroDocumentoTextView.getText().toString().trim();
            }
            UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
            presenter.getDocumentoPendienteOnline(claseDocumentoSelected, nroDocumento);
        });

        // Llama a SinDocumentoConfigFragment para realizar la creación de un nuevo documento
        newDocumentoImageView.setOnClickListener(v -> {
            if (TextUtils.equals(Constants.RECEPCION,type))
                ((PrincipalActivity) getActivity()).replaceFragment(SinDocumentoConfigFragment.newInstance(type),
                        getActivity().getString(R.string.recepcion_tilde));
            else if (TextUtils.equals(Constants.DESPACHO,type))
                ((PrincipalActivity) getActivity()).replaceFragment(SinDocumentoConfigFragment.newInstance(type),
                        getActivity().getString(R.string.despacho));
        });
        emptyTextView.setVisibility(View.GONE);

        ((PrincipalActivity) getActivity()).changeToogleIcon(0,null);
        return view;
    }

    @Override
    public void onDetach() {
        presenter.detachView();
        unbinder.unbind();
        super.onDetach();
    }

    @Override
    public void setUpClasesSpinner(String almacen, List<String> documentos) {
        if (documentos.isEmpty()){
            new CustomDialog.Builder(getActivity())
                    .setTheme(R.style.AppTheme_Dialog_Error)
                    .setPositiveButtonLabel(getActivity().getString(R.string.label_ok))
                    .setPositiveButtonlistener(() -> ((PrincipalActivity) getActivity()).replaceFragment(new PrincipalFragment(),"AlmacenSoft"))
                    .setIcon(R.drawable.ic_alert)
                    .setMessage(getActivity().getString(R.string.no_clases_disponibles))
                    .setCancelable(false)
                    .build().show();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.spinner_row, documentos);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown);
            documentosSpinner.setAdapter(adapter);
            documentosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    claseDocumentoSelected = documentos.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            almacenTextView.setText(almacen);

            nroDocumentoTextView.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    nroDocumentoTextView.clearFocus();
                    UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                }
                return false;
            });
        }
        ProgressDialog.dismiss();
    }

    @Override
    public void setUpDocumentosPendientes(List<Documento> documentos) {
        if (documentos.isEmpty()){
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
        }
        adapterRecycler.setList(documentos);
        adapterRecycler.setSelectNroListener(this);
        recepcionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recepcionRecyclerView.setAdapter(adapterRecycler);
    }

    /**
     * Se ejecuta cuando el usuario realiza una selección de la lista de documentos pendientes
     * @param codTipoDocumento es el código del tipo de documento seleccionado
     * @param codClaseDocumento es el código de la clase de documento seleccionada
     * @param numDocumento es el número de documento seleccionado
     * @param idClaseDocumento es el identificador único de la clase de documento seleccionado
     * @param idDocumento es el identificador único del documento seleccionado
     */
    @Override
    public void selectNro(String codTipoDocumento,String codClaseDocumento,String numDocumento, int idClaseDocumento, int idDocumento) {
        BodyDetalleDocumentoPendiente body = presenter.getDataDetalleDocumento();
        body.setCodTipoDocumento(codTipoDocumento);
        body.setCodClaseDocumento(codClaseDocumento);
        body.setIdClaseDocumento(idClaseDocumento);
        body.setNumDocumento(numDocumento);
        body.setIdDocumento(idDocumento);
        presenter.setConSinDoc(1);
        // Se carga la pantalla de detalle de documento (DetalleDocumentoFragment) pasándole los parámetros necesarios
        if (TextUtils.equals(Constants.RECEPCION,type))
        ((PrincipalActivity) getActivity()).replaceFragment(DetalleDocumentoFragment.newInstance(body,type,1,true),
                getActivity().getString(R.string.recepcion_tilde));
        else if (TextUtils.equals(Constants.DESPACHO,type))
        ((PrincipalActivity) getActivity()).replaceFragment(DetalleDocumentoFragment.newInstance(body,type,1,true),
                getActivity().getString(R.string.despacho));
    }

    @Override
    public void onBackPressed() {

    }

    /**
     * Se ejecuta cuando el móvil no tiene una conexión a la red
     * @param iButton es el procedimiento a seguir
     * @param message es el mensaje a mostrar en el ProgressDialog
     */
    @Override
    public void showNoInternet(CustomDialog.IButton iButton, String message) {
        ProgressDialog.show(getActivity(),message);
        UtilMethods.getInternetError(getActivity(),iButton).show();
    }

    /**
     * Se ejecuta cuando ocurre un error de conexión con el WS
     * @param iButton es el procedimiento a seguir
     * @param message es el mensaje a mostrar en el cuadro de diálogo
     * @param type determina el tipo de error ocurrido
     *             0 - Error al traer clases de documentos o al traer documentos pendientes
     *             1 - Limite de intentos al traer clases de documentos
     */
    @Override
    public void onError(CustomDialog.IButton iButton, String message, int type) {
        if (type == 0) {
            ProgressDialog.show(getActivity(), message);
            new CustomDialog.Builder(getActivity())
                    .setMessage(getActivity().getString(R.string.error_WS))
                    .setIcon(R.drawable.ic_alert)
                    .setCancelable(false)
                    .setPositiveButtonLabel(getActivity().getString(R.string.reintentar))
                    .setPositiveButtonlistener(iButton)
                    .setTheme(R.style.AppTheme_Dialog_Error)
                    .build().show();
        } else {
            new CustomDialog.Builder(getActivity())
                    .setMessage(getActivity().getString(R.string.limite_intentos))
                    .setIcon(R.drawable.ic_alert)
                    .setCancelable(false)
                    .setPositiveButtonLabel(getActivity().getString(R.string.label_ok))
                    .setPositiveButtonlistener(() -> ((PrincipalActivity) getActivity())
                            .replaceFragment(new PrincipalFragment(),getActivity().getString(R.string.app_name)))
                    .setTheme(R.style.AppTheme_Dialog_Error)
                    .build().show();
        }
    }
}
