package com.dms.almacensoft.features.configuracion.dodumentospendientes.recepciondespacho;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.dms.almacensoft.features.configuracion.dodumentospendientes.DocumentosPendientesActivity;
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
 * {@link RecepcionDespachoPendientesFragment} permite descargar los documentos pendientes para los módulos
 * Recepción y Despacho para el modo de trabajo en Batch.
 */

public class RecepcionDespachoPendientesFragment extends BaseFragment implements RecepcionDespachoPendientesContract.View,
        DocumentosPendientesActivity.DataRecepcion, DocumentosPendientesActivity.DataDespacho {

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
    @BindView(R.id.empty_text_view)
    TextView emptyTextView;

    private String claseDocumentoSelected;

    private String type;

    private Unbinder unbinder;

    private Handler handler = new Handler();

    private List<BodyDetalleDocumentoPendiente> detalleBodyList = new ArrayList<>();

    @Inject
    RecepcionDespachoPendientesAdapter adapterRecycler;

    @Inject
    RecepcionDespachoPendientesPresenter presenter;

    public static RecepcionDespachoPendientesFragment newInstance(String type){
        RecepcionDespachoPendientesFragment f = new RecepcionDespachoPendientesFragment();
        Bundle args = new Bundle();
        args.putString(Constants.TYPE,type);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        type = getArguments().getString(Constants.TYPE);

        View view = inflater.inflate(R.layout.fragment_recepcion_documentos_pendientes, container, false);

        unbinder = ButterKnife.bind(this, view);

        // Traemos los tipos de clase de documento desde el WS
        presenter.getDataClaseBD(type);

        if (TextUtils.equals(Constants.RECEPCION,type)){
            ((DocumentosPendientesActivity) getActivity()).setDataRecepcion(this);
        } else if (TextUtils.equals(Constants.DESPACHO,type)) {
            ((DocumentosPendientesActivity) getActivity()).setDataDespacho(this);
        }

        emptyTextView.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }

    @Override
    public void onBackPressed() {
        getActivity().onBackPressed();
    }

    @Override
    public void setUpClasesSpinner(String almacen, List<String> clases) {
        if (clases.isEmpty()){
            new CustomDialog.Builder(getActivity())
                    .setTheme(R.style.AppTheme_Dialog_Error)
                    .setPositiveButtonLabel(getActivity().getString(R.string.label_ok))
                    .setPositiveButtonlistener(this::onBackPressed)
                    .setIcon(R.drawable.ic_alert)
                    .setMessage(getActivity().getString(R.string.no_clases_disponibles))
                    .setCancelable(false)
                    .build().show();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.spinner_row, clases);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown);
            documentosSpinner.setAdapter(adapter);
            documentosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    claseDocumentoSelected = clases.get(position);
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

            // Realiza la búsqueda de documentos pendientes
            searchRecepcionImageView.setOnClickListener(v -> {
                nroDocumentoTextView.clearFocus();
                ProgressDialog.show(getActivity(),getActivity().getString(R.string.procesando));
                String nroDocumento = null;
                if (!TextUtils.isEmpty(nroDocumentoTextView.getText().toString().trim())){
                    // Si se ingresó algo a nroDocumentoTextView se utiliza esta data para realizar la búsqueda
                    nroDocumento = nroDocumentoTextView.getText().toString().trim();
                }
                UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                presenter.getDocumentoPendienteOnline(claseDocumentoSelected, nroDocumento);
            });
        }
    }

    @Override
    public void setUpDocumentosPendientes(List<Documento> documentos) {
        handler.postDelayed(() -> {
            if (documentos.isEmpty()){
                emptyTextView.setVisibility(View.VISIBLE);
            } else {
                emptyTextView.setVisibility(View.GONE);
            }
            adapterRecycler.setList(documentos);
            recepcionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recepcionRecyclerView.setAdapter(adapterRecycler);
            ProgressDialog.dismiss();
        },1000);
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
     *             0 - Error al traer documentos pendientes
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
        }
    }

    @Override
    public List<BodyDetalleDocumentoPendiente> guardar() {
        List<Documento> selectedList = adapterRecycler.getSelectedList();
        presenter.insertSelectedDocumentos(selectedList); // Insertamos los documentos seleccionados en la base de datos interno
        presenter.bloquearSelectedDocumentos(selectedList); // Bloqueamos los documentos seleccionados
        for (Documento documento : selectedList){
            detalleBodyList.add(presenter.completeData(documento.getCodTipoDocumento(),
                    documento.getCodClaseDocumento(),documento.getNumDocumento(),
                    documento.getIdDocumento(),documento.getIdClaseDocumento()));
        }
        return detalleBodyList;
    }
}
