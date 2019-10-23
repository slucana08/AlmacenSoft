package com.dms.almacensoft.features.recepciondespacho.lectura;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.models.recepciondespacho.BodyConsultarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.features.principal.PrincipalActivity;
import com.dms.almacensoft.features.recepciondespacho.detalledocumento.DetalleDocumentoFragment;
import com.dms.almacensoft.features.recepciondespacho.documentos.DocumentosFragment;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.features.shared.BaseFragment;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.SimpleDividerItemDecoration;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.CustomDialog;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * {@link LecturaFragment} es la pantalla que muestra las lecturas realizadas para el documento seleccionado en
 * {@link DocumentosFragment}.
 * Permite eliminar lecturas individuales a través de {@link ChildConsultaViewHolder.OnRemove} o de un grupo de
 * lecturas a través de {@link HeaderConsultaViewHolder.OnRemove}
 */

public class LecturaFragment extends BaseFragment implements LecturaContract.View {

    @BindView(R.id.lectura_recycler_view)
    RecyclerView lecturaRecyclerView;
    @BindView(R.id.lectura_progress_bar)
    ProgressBar lecturaProgressBar;
    @BindView(R.id.documento_lectura_text_view)
    TextView documentoTextView;
    @BindView(R.id.empty_lectura_text_view)
    TextView emptyTextView;
    @BindView(R.id.error_lectura_image_view)
    ImageView errorImageView;
    @BindView(R.id.retry_lectura_button)
    Button retryButton;

    public HeaderConsultaAdapter adapter;
    private String type;
    private Unbinder unbinder;
    private BodyDetalleDocumentoPendiente bodyDetalle;
    private BodyConsultarLecturas bodyConsulta;
    private int conSinDoc;

    private boolean conLecturas;

    private Handler handler = new Handler();

    @Inject
    LecturaPresenter presenter;

    public LecturaFragment (){

    }

    /**
     * @param type es el módulo en el que se trabaja
     *             "R" - Recepción
     *             "D" - Despacho
     * @param bodyDetalle es la data necesaria para poder mostrar los detalles de un documento, se utliliza
     *                    para poder volver a mostrar data al volver a {@link DetalleDocumentoFragment}
     * @param bodyConsulta es la data necesaria para poder realizar la consulta de lecturas
     * @param conSinDoc determina si se está trabajando con o sin documento
     *                  0 - sin documento
     *                  1 - con documento
     * @return una instancia de {@link LecturaFragment}
     */
    public static LecturaFragment newInstance(String type, BodyDetalleDocumentoPendiente bodyDetalle, BodyConsultarLecturas bodyConsulta,
                                              int conSinDoc){
        LecturaFragment f =  new LecturaFragment();
        Bundle args = new Bundle();
        args.putString(Constants.TYPE,type);
        args.putParcelable(Constants.BODY_DETALLE,bodyDetalle);
        args.putParcelable(Constants.BODY_CONSULTA,bodyConsulta);
        args.putInt(Constants.CON_SIN_DOC,conSinDoc);
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
        bodyDetalle = getArguments().getParcelable(Constants.BODY_DETALLE);
        bodyConsulta = getArguments().getParcelable(Constants.BODY_CONSULTA);
        conSinDoc = getArguments().getInt(Constants.CON_SIN_DOC);

        View view = inflater.inflate(R.layout.fragment_recepcion_lectura, container, false);

        unbinder = ButterKnife.bind(this,view);

        documentoTextView.setText(bodyDetalle.getNumDocumento());

        lecturaRecyclerView.setVisibility(View.GONE);

        emptyTextView.setVisibility(View.GONE);

        ((PrincipalActivity) getActivity()).changeToogleIcon(1, () -> LecturaFragment.this.onBackPressed());

        // Se realiza la consulta de lecturas
        presenter.getLecturas(bodyConsulta);

        return view;
    }

    /**
     * Muestra las lecturas obtenidas
     * @param headerConsultaList es la lista de lecturas agrupadas por código de producto que contiene
     *                           las lecturas individuales
     */
    @Override
    public void showLecturas(List<HeaderConsulta> headerConsultaList) {
        if (headerConsultaList.isEmpty()){
            lecturaProgressBar.setVisibility(View.GONE);
            lecturaRecyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            conLecturas = false;
        } else {
            conLecturas = true;
            adapter = new HeaderConsultaAdapter(headerConsultaList);
            // Permite realizar la eliminación de una lectura individual
            adapter.onRemoveHijo(hijoConsulta -> {
                lecturaProgressBar.setVisibility(View.VISIBLE);
                lecturaRecyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                presenter.deleteLectura(hijoConsulta);
            });
            // Permite realizar la eliminación de un grupo de lecturas
            adapter.onRemovePadre(items -> new CustomDialog.Builder(LecturaFragment.this.getContext())
                    .setTitle(getActivity().getString(R.string.alerta))
                    .setMessage(getActivity().getString(R.string.eliminar_grupo_lectura_pregunta))
                    .setPositiveButtonLabel(LecturaFragment.this.getString(R.string.label_yes))
                    .setNegativeButtonLabel(LecturaFragment.this.getString(R.string.label_no))
                    .setIcon(R.drawable.ic_alert)
                    .setTheme(R.style.AppTheme_Dialog_Warning)
                    .setPositiveButtonlistener(() -> {
                        lecturaProgressBar.setVisibility(View.VISIBLE);
                        lecturaRecyclerView.setVisibility(View.GONE);
                        emptyTextView.setVisibility(View.GONE);
                        presenter.deleteLecturaGroup(items);
                    })
                    .build().show());
            lecturaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            lecturaRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext(), R.drawable.line_white_divider));
            lecturaRecyclerView.setAdapter(adapter);
            lecturaProgressBar.setVisibility(View.GONE);
            lecturaRecyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed() {
        if (TextUtils.equals(Constants.RECEPCION,type))
            ((PrincipalActivity) getActivity()).replaceFragment(DetalleDocumentoFragment.newInstance(bodyDetalle,type,conSinDoc,conLecturas),getActivity().getString(R.string.recepcion_tilde));
        else if (TextUtils.equals(Constants.DESPACHO,type))
            ((PrincipalActivity) getActivity()).replaceFragment(DetalleDocumentoFragment.newInstance(bodyDetalle,type,conSinDoc,conLecturas),getActivity().getString(R.string.despacho));
    }

    /**
     * Se ejecuta en caso de que ocurran errores
     * @param event es el evento a ejecutar cuando se presione el botón reiniciar
     * @param message es el mensaje a mostrar en grilla o ProgressDialog
     * @param type determina la acción a realizar
     *             0 - El móvil no tiene internet mensaje en grilla
     *             1 - El móvil no tiene interner mensaje en cuadro de diálogo
     *             2 - Error de al conseguir lecturas / cancelar documento
     *             3 - Límite de intentos en eliminacion individual
     *             4 - Error de eliminación grupal
     */
    @Override
    public void onError(CustomDialog.IButton event, String message, int type) {
        switch (type){
            case 0:
            case 2:
            case 3:
            case 4:
                handler.postDelayed(() -> {
                    lecturaRecyclerView.setVisibility(View.GONE);
                    lecturaProgressBar.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
                    emptyTextView.setText(message);
                    errorImageView.setVisibility(View.VISIBLE);
                    if (type == 2 || type == 3 || type == 4) errorImageView.setImageResource(R.drawable.ic_warning);
                    retryButton.setVisibility(View.VISIBLE);
                    retryButton.setText(getActivity().getString(type == 3 || type == 4 ? R.string.label_ok :R.string.reintentar));
                },200);
                retryButton.setOnClickListener(v -> {
                    if (type == 3){
                        if (TextUtils.equals(Constants.RECEPCION,LecturaFragment.this.type))
                            ((PrincipalActivity) getActivity()).replaceFragment(DetalleDocumentoFragment.newInstance(bodyDetalle,LecturaFragment.this.type,conSinDoc,conLecturas),
                                    getActivity().getString(R.string.recepcion_tilde));
                        else if (TextUtils.equals(Constants.DESPACHO,LecturaFragment.this.type))
                            ((PrincipalActivity) getActivity()).replaceFragment(DetalleDocumentoFragment.newInstance(bodyDetalle,LecturaFragment.this.type,conSinDoc,conLecturas),
                                    getActivity().getString(R.string.despacho));
                    } else {
                        lecturaProgressBar.setVisibility(View.VISIBLE);
                        errorImageView.setVisibility(View.GONE);
                        retryButton.setVisibility(View.GONE);
                        emptyTextView.setVisibility(View.GONE);
                        event.onButtonClick();
                    }
                });
                break;
            case 1:
                UtilMethods.getInternetError(getActivity(), () -> {
                    ProgressDialog.show(getActivity(),message);
                    event.onButtonClick();
                }).show();
                break;
        }
    }

}
