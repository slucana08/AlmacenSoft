package com.dms.almacensoft.features.recepciondespacho.detalledocumento;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.entities.dbalmacen.Producto;
import com.dms.almacensoft.data.entities.dbalmacen.Ubicacion;
import com.dms.almacensoft.data.models.recepciondespacho.BodyBuscarSerie;
import com.dms.almacensoft.data.models.recepciondespacho.BodyCerrarDocumento;
import com.dms.almacensoft.data.models.recepciondespacho.BodyConsultarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.data.models.recepciondespacho.BodyRegistrarLecturas;
import com.dms.almacensoft.data.models.recepciondespacho.DetalleDocumentoLive;
import com.dms.almacensoft.features.impresion.ImpresionFragment;
import com.dms.almacensoft.features.principal.PrincipalActivity;
import com.dms.almacensoft.features.recepciondespacho.documentos.DocumentosFragment;
import com.dms.almacensoft.features.recepciondespacho.lectura.LecturaFragment;
import com.dms.almacensoft.features.recepciondespacho.sindocumento.SinDocumentoConfigFragment;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.features.shared.BaseFragment;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.CustomDialog;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * {@link DetalleDocumentoFragment} es la pantalla de muestreo del detalle del documento seleccionado
 * en {@link DocumentosFragment}. Permite además crear una guía, registrar lecturas para esta guía y
 * cerrar la guia una vez el registro de lecturas haya culminado.
 * Permite también ir a la pantalla de consulta de lecturas {@link LecturaFragment}
 * Implementa {@link DetalleDocumentoAdapter.CodigoProductoSetter} que permite mostrar el código
 * de producto cuando se realiza una selección de la lista de detalle llenada por {@link DetalleDocumentoAdapter}
 */

public class DetalleDocumentoFragment extends BaseFragment implements DetalleDocumentoContract.View,
        DetalleDocumentoAdapter.CodigoProductoSetter {

    private Unbinder unbinder;

    @BindView(R.id.ubicacion_edit_text)
    EditText ubicacionEditText;
    @BindView(R.id.nro_guia_edit_text)
    EditText guiaEditText;
    @BindView(R.id.codigo_producto_edit_text)
    EditText codigoProductoEditText;
    @BindView(R.id.documento_detalle_text_view)
    TextView documentoTextView;
    @BindView(R.id.lote_serie_linear_layout)
    LinearLayout loteSerieLinearLayout;
    @BindView(R.id.lote_detalle_doc_edit_text)
    EditText loteDetalleEditText;
    @BindView(R.id.serie_detalle_doc_edit_text)
    EditText serieDetalleEditText;
    @BindView(R.id.second_view)
    View secondView;
    @BindView(R.id.cantidad_edit_text)
    EditText cantidadEditText;
    @BindView(R.id.detalle_doc__recycler_view)
    RecyclerView detalleRecyclerView;
    @BindView(R.id.guia_linear_layout)
    LinearLayout guiaLinearLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.empty_text_view)
    TextView emptyTextView;
    @BindView(R.id.search_lectura_image_view)
    ImageView searchImageView;
    @BindView(R.id.finalizar_check_box)
    CheckBox finalizarCheckBox;
    @BindView(R.id.close_image_view)
    ImageView closeImageView;
    @BindView(R.id.error_image_view)
    ImageView errorImageView;
    @BindView(R.id.retry_button)
    Button reintentarButton;
    @BindView(R.id.imprimir_image_view)
    ImageView imprimirImageView;

    @Inject
    DetalleDocumentoPresenter presenter;

    @Inject
    DetalleDocumentoAdapter adapter;

    private Handler handler = new Handler();

    // Es el primer producto ingresado cuando se trabaja en modo sin documento
    private Producto productoInicial;

    private BodyDetalleDocumentoPendiente bodyDetalle;
    private String type;
    private int conSinDocSet;
    private boolean conLecturas;

    private boolean loteSet;
    private boolean serieSet;
    private boolean barridoSet;
    private boolean impresoraSet;

    // Lista de detalles del documento
    private List<DetalleDocumentoLive> detalleList;

    // Es la data de la ubicación seleccionada para trabajar ya sea que se seteo como ubicación por
    // defecto en GeneralConfigFragment o que se seteo en esta pantalla
    private Ubicacion ubicacionData;

    // Determina el item que fue seleccionado de la lista de detalles de acuerdo a su índice en la lista
    private int selectedItem;

    // Determina donde se encontro el producto con el que se realizará la lectura
    private int productoLocation; // 0 - base de datos // 1 - lista de detalles

    private boolean started = false;

    private String guia;

    /**
     * @param body es la información necesaria para traer los detalles del documento seleccionado
     * @param type es el módulo con el que se está trabajando
     *              "R" - Recepción
     *              "D" - Despacho
     * @param conSinDoc determina si la llamada se realizó pra trabajar con o sin documento
     *                  0 - sin documento
     *                  1 - con documento
     * @param conLecturas determina si se tiene lecturas en el modo sin documento, está variable se utiliza
     *                    cuando se regresa del fragmento de vista de lecturas {@link LecturaFragment}
     * @return una instancia de {@link DetalleDocumentoFragment}
     */
    public static DetalleDocumentoFragment newInstance(BodyDetalleDocumentoPendiente body, String type, int conSinDoc,
                                                       boolean conLecturas){
        DetalleDocumentoFragment f = new DetalleDocumentoFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.BODY_DETALLE,body);
        args.putString(Constants.TYPE,type);
        args.putInt(Constants.CON_SIN_DOC_SET, conSinDoc);
        args.putBoolean(Constants.CON_LECTURAS,conLecturas);
        f.setArguments(args);
        return f;
    }

    public DetalleDocumentoFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        bodyDetalle = getArguments().getParcelable(Constants.BODY_DETALLE);
        type = getArguments().getString(Constants.TYPE);
        conSinDocSet = getArguments().getInt(Constants.CON_SIN_DOC_SET);
        conLecturas = getArguments().getBoolean(Constants.CON_LECTURAS);

        View view = inflater.inflate(R.layout.fragment_recepcion_detalle_documento, container, false);

        unbinder = ButterKnife.bind(this,view);

        detalleRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(conSinDocSet == 1 ? View.VISIBLE : View.GONE);

        ((PrincipalActivity) getActivity()).changeToogleIcon(0,null);

        // Verificamos si es que se configuró una ubicación por defecto en GeneralConfigFragment
        presenter.isUbicacionSet(type);

        // Se realiza la carga de los detalles del documento y configuraciones necesarias para poder registrar las lecturas
        // 0 - determina carga inicial
        // 2 - determina carga incial sin documento, solo trae configuraciones necesarias
        if (conSinDocSet == 0){
            presenter.getData(bodyDetalle,conLecturas ? 0 : 2, conSinDocSet);
            emptyTextView.setVisibility(conLecturas ? View.GONE : View.VISIBLE);
            emptyTextView.setText(getActivity().getString(R.string.realice_lectura));
        } else {
            presenter.getData(bodyDetalle,0,conSinDocSet);
            emptyTextView.setVisibility(View.GONE);
        }

        // Verificamos si es que hay una guía sin cerrar
        presenter.isGuiaStarted();

        return view;
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }

    /**
     * Cancela un documento cuando se da la opción al usuario desde el menu de navegación y el usuario
     * intentó cambiar de módulo sin cancelar un documeno que se creo desde {@link SinDocumentoConfigFragment}
     */
    public void cancelarDocumentoMenu(){
        ProgressDialog.show(getActivity(),getActivity().getString(R.string.cancelando_documento));
        presenter.cancelarDocumento();
    }

    @Override
    public void setUpViews(boolean lote, boolean serie, boolean barrido, boolean impresora,
                           List<DetalleDocumentoLive> documentos) {
        loteSet = lote;
        serieSet = serie;
        barridoSet = barrido;
        impresoraSet = impresora;
        documentoTextView.setText(bodyDetalle.getNumDocumento());

        if(TextUtils.equals(Constants.RECEPCION,type)) {
            guiaLinearLayout.setVisibility(View.VISIBLE);
            if (impresora && conSinDocSet == 1) {
                // Si estamos en el módulo recepción, trabajando con documento y la opción de impresión (ImpresionConfigFragment)
                // está activa, activamos el ícono de impresión
                imprimirImageView.setImageResource(R.drawable.ic_impresion_2);
                imprimirImageView.setOnClickListener(v -> {
                    if (!presenter.getGuiaCerrada()) {
                        // Si hay una guía en proceso que no ha sido cerrada notificamos al usuario
                        // que debe ser cerrada antes de realizar una impresión
                        new CustomDialog.Builder(getActivity())
                                .setCancelable(false)
                                .setTheme(R.style.AppTheme_Dialog_Error)
                                .setPositiveButtonLabel(getString(R.string.label_ok))
                                .setMessage(getActivity().getString(R.string.cerrar_guia_antes_impresion))
                                .setIcon(R.drawable.ic_alert)
                                .setPositiveButtonlistener(() -> {
                                })
                                .build().show();
                    } else {
                        // Si no hay una guía que está siendo trabajada mostramos la pantalla de impresión ImpresionFragment
                        // con type = 0 para indicar que la llamada es desde el módulo de recepción
                        ((PrincipalActivity) DetalleDocumentoFragment.this.getActivity()).
                                replaceFragment(ImpresionFragment.newInstance(0,bodyDetalle.getIdClaseDocumento()
                                        ,bodyDetalle.getNumDocumento()), getActivity().getString(R.string.impresion));
                    }
                });
            } else {
                // Si la opción de impresión esta desactivada o estamos trabajando sin documento
                // desactivamos el ícono de impresión
                imprimirImageView.setImageResource(R.drawable.ic_impresion_2_gray);
            }
        } else if (TextUtils.equals(Constants.DESPACHO,type)) {
            // Si estamos en el módulo de despacho desactivamos el ícono de impresión
            guiaLinearLayout.setVisibility(View.GONE);
            imprimirImageView.setImageResource(R.drawable.ic_impresion_2_gray);
        }

        if (lote && serie){
            loteSerieLinearLayout.setVisibility(View.VISIBLE);
            loteDetalleEditText.setVisibility(View.VISIBLE);
            serieDetalleEditText.setVisibility(View.VISIBLE);
            secondView.setVisibility(View.VISIBLE);
        } else if (lote){
            loteSerieLinearLayout.setVisibility(View.VISIBLE);
            loteDetalleEditText.setVisibility(View.VISIBLE);
            serieDetalleEditText.setVisibility(View.INVISIBLE);
            secondView.setVisibility(View.VISIBLE);
            // Si estamos en modo de lectura en barrido, la opción de teclado de loteDetalleEditText será cambiada
            if (barridoSet) loteDetalleEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        } else if (serie){
            loteSerieLinearLayout.setVisibility(View.VISIBLE);
            loteDetalleEditText.setVisibility(View.INVISIBLE);
            serieDetalleEditText.setVisibility(View.VISIBLE);
            secondView.setVisibility(View.VISIBLE);
        } else {
            loteSerieLinearLayout.setVisibility(View.GONE);
            secondView.setVisibility(View.GONE);
            // Si estamos en modo de lectura en barrido, la opción de teclado de ubicacionEditText será cambiada
            if (barridoSet) ubicacionEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }

        if (documentos != null){
            if (documentos.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
                emptyTextView.setText(getActivity().getString(R.string.no_detalle_documento));
            } else {
                // Seteamos la lista de detalles del documento con el que se está trabajando
                detalleList = presenter.orderDocument(documentos);
                adapter.setList(detalleList);
                adapter.setCodigoProductoSetter(this);
                detalleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                detalleRecyclerView.setAdapter(adapter);
                detalleRecyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }

        // Botón de para consultar las lecturas del documento
        searchImageView.setOnClickListener(v -> {
            BodyConsultarLecturas bodyConsulta = new BodyConsultarLecturas();
            bodyConsulta.setCodTipodocumento(bodyDetalle.getCodTipoDocumento());
            bodyConsulta.setIdClaseDocumento(bodyDetalle.getIdClaseDocumento());
            bodyConsulta.setNumDocumento(bodyDetalle.getNumDocumento());
            presenter.consultarLecturas(bodyConsulta);
        });

        // Botón para cerrar un documento
        closeImageView.setOnClickListener(v -> {
            UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
            BodyCerrarDocumento bodyCerrar = new BodyCerrarDocumento();
            bodyCerrar.setCodTipoDocumento(bodyDetalle.getCodTipoDocumento());
            bodyCerrar.setCodClaseDocumento(bodyDetalle.getCodClaseDocumento());
            bodyCerrar.setNumDocumento(bodyDetalle.getNumDocumento());
            bodyCerrar.setIdDocumento(bodyDetalle.getIdDocumento());
            bodyCerrar.setCerrarDoc(conSinDocSet != 1);
            bodyCerrar.setFlgConSinDoc(conSinDocSet);
            bodyCerrar.setTipoLectImpresion(true);
            if (TextUtils.equals(Constants.RECEPCION, type)) {
                bodyCerrar.setDocumentoInterno(guiaEditText.getText().toString().trim());
                if (!guiaEditText.isEnabled()) {
                    // Si se ha realizado una lectura se setea la información a enviar de acuerdo al
                    // módulo en el que se está trabajando
                    ProgressDialog.show(getActivity(), getActivity().getString(R.string.cerrando_documento));
                    presenter.cerrarDocumento(bodyCerrar);
                } else {
                    // Si no hay lecturas realizadas se notifica al usuario
                    UtilMethods.showToast(getActivity(), getActivity().getString(R.string.realizar_una_lectura));
                }
            } else if (TextUtils.equals(Constants.DESPACHO, type)) {
                bodyCerrar.setDocumentoInterno("");
                presenter.getLecturasDespacho(bodyCerrar);
                ProgressDialog.show(getActivity(), getActivity().getString(R.string.verificando_datos));
            }
        });

        if (barrido) {
            // En modo de lectura en barrido se setea la cantidad por defecto a 1
            cantidadEditText.setText("1");
            cantidadEditText.setEnabled(false);
        } else {
            cantidadEditText.getText().clear();
            cantidadEditText.setEnabled(true);
        }

        guiaEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                if (TextUtils.isEmpty(guiaEditText.getText().toString().trim())) {
                    UtilMethods.showToast(DetalleDocumentoFragment.this.getActivity(),
                            getActivity().getString(R.string.debe_ingresar_guia));
                    DetalleDocumentoFragment.this.nextfocus(0);
                    return true;
                } else {
                    if (!lote && !serie && barrido && !ubicacionEditText.isEnabled())
                        // Si estamos en modo de lectura en barrido, el ingreso de lote y serie esta desactivado,
                        // y la ubicación fue seteada por defecto. El encargado de procesar el registro será
                        // codigoProductoEditText
                        codigoProductoEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    DetalleDocumentoFragment.this.nextfocus(1);
                    return false;
                }
            }
            return false;
        });

        codigoProductoEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE
                        || (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    if (TextUtils.isEmpty(codigoProductoEditText.getText().toString().trim())) {
                        UtilMethods.showToast(DetalleDocumentoFragment.this.getActivity(), DetalleDocumentoFragment.this.getActivity().getString(R.string.debe_ingresar_cod_producto));
                        adapter.setSelectedItem(-1);
                        detalleRecyclerView.smoothScrollToPosition(0);
                        DetalleDocumentoFragment.this.nextfocus(1);
                        return true;
                    } else {
                        UtilMethods.hideKeyboard((AppCompatActivity) DetalleDocumentoFragment.this.getActivity());
                        // Al ingresar el producto se realiza una búsqueda del producto ingresado
                        DetalleDocumentoFragment.this.findProducto();
                        return true;
                    }
                }
                return false;
            }
        });

        ubicacionEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE
                    || (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)){
                if (TextUtils.isEmpty(ubicacionEditText.getText().toString().trim())) {
                    UtilMethods.showToast(getActivity(),getActivity().getString(R.string.debe_ingresar_ubicacion));
                    nextfocus(2);
                    return true;
                } else {
                    UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                    // Se verifica la que la ubicación esté disponible para este módulo
                    presenter.verifyUbicacion(ubicacionEditText.getText().toString().trim());
                    return false;
                }
            }
            return false;
        });

        loteDetalleEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE
                    ||(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                if (TextUtils.isEmpty(loteDetalleEditText.getText().toString().trim())) {
                    UtilMethods.showToast(DetalleDocumentoFragment.this.getActivity(),
                            getActivity().getString(R.string.debe_ingresar_lote));
                    DetalleDocumentoFragment.this.nextfocus(3);
                    return true;
                } else {
                    if (serie) {
                        DetalleDocumentoFragment.this.nextfocus(4);
                    } else {
                        if (barrido) {
                            // Si el registro de serie no está activado y el modo de lectura es barrido se
                            // llama al método de verificación de datos
                            DetalleDocumentoFragment.this.verifyDataToSubmit();
                            loteDetalleEditText.clearFocus();
                        } else {
                            // Si el modo de lectura es manual se solicita al usuario ingresar al cantidad a
                            // resgistrar
                            DetalleDocumentoFragment.this.nextfocus(5);
                        }
                        UtilMethods.hideKeyboard((AppCompatActivity) DetalleDocumentoFragment.this.getActivity());
                    }
                    return false;
                }
            }
            return false;
        });

        serieDetalleEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)){
                if (TextUtils.isEmpty(serieDetalleEditText.getText().toString().trim())) {
                    UtilMethods.showToast(getActivity(),getActivity().getString(R.string.debe_ingresar_serie));
                    nextfocus(4);
                    return true;
                } else {
                    BodyBuscarSerie bodySerie = new BodyBuscarSerie();
                    bodySerie.setIdDocumento(conSinDocSet == 1 ?
                            detalleList.get(selectedItem).getIdDocumento() : bodyDetalle.getIdDocumento());
                    // Si el producto se encontró en la lista de detalles se utilza la información desde esta lista.
                    // Si el producto se encontró en la base de datos se utiliza la data ingresdad en productoInicial
                    // significa que es la primera lectura de un registro sin documento lo cual implica qe detaleList es nulo
                    bodySerie.setIdProducto(productoLocation == 1 ?
                            detalleList.get(selectedItem).getIdProducto() : productoInicial.getIdProducto());
                    bodySerie.setSerieProducto(serieDetalleEditText.getText().toString());
                    // Si se realiza el registro de serie se procede a realizar una verificación de serie
                    presenter.verifySerie(bodySerie);
                    return false;
                }
            }
            return false;
        });

        cantidadEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)){
                if (TextUtils.isEmpty(cantidadEditText.getText().toString().trim())) {
                    UtilMethods.showToast(getActivity(),getActivity().getString(R.string.debe_ingresar_cantidad));
                    nextfocus(5);
                    return false;
                } else {
                    // Si se llega a esta parte significa que el modo de lectura es manual y se debe
                    // llamar la verificación de datos desde aquí
                    verifyDataToSubmit();
                    return false;
                }
            }
            return false;
        });
    }

    /**
     * Actualiza el aspecto visual luego de haber cancelado un documento, regresa a {@link DocumentosFragment}
     */
    @Override
    public void cancelarDocumento() {
        ProgressDialog.show(getActivity(),getActivity().getString(R.string.documento_cancelado));
        presenter.saveGuia(0,"");
        presenter.setConSinDoc(0);
        presenter.deleteDataClase();
        handler.postDelayed(() -> {
            if (TextUtils.equals(Constants.RECEPCION, type)) {
                ((PrincipalActivity) getActivity()).replaceFragment(DocumentosFragment.newInstance(type),
                        getActivity().getString(R.string.recepcion_tilde));
            } else if (TextUtils.equals(Constants.DESPACHO, type)) {
                ((PrincipalActivity) getActivity()).replaceFragment(DocumentosFragment.newInstance(type),
                        getActivity().getString(R.string.despacho));
            }
            ProgressDialog.dismiss();
        },2500);
    }

    /**
     * Realiza una búsqueda de producto para determinar su órigen
     */
    private void findProducto() {
        if (detalleList == null){
            // Significa que no hay lecturas cuando se trabaja desde la opción sin documento
            presenter.verifyProducto(codigoProductoEditText.getText().toString().trim());
        } else {
            // Si hay data en detalleList se procede a buscar el producto ingresado en esta lista
            // Esta búsqueda se realiza en otro thread para contemplar el caso en que la lista
            // es larga y el proceso pueda demorar
            Executors.newSingleThreadScheduledExecutor().execute(() -> {
                for (int i = 0; i < detalleList.size(); i++) {
                    selectedItem = i;
                    if (TextUtils.equals(codigoProductoEditText.getText().toString().trim(),
                            detalleList.get(i).getCodProducto())) {
                        getActivity().runOnUiThread(() -> {
                            // Si se encuentra el producto en la lista se actualiza la selección en
                            // DetalleDocumentoAdapter para que actualice la grilla de muestreo
                            adapter.setSelectedItem(selectedItem);
                            detalleRecyclerView.smoothScrollToPosition(selectedItem);
                            // Se determina que se encontrón el producto en la lista de detalles
                            productoLocation = 1;
                            if (ubicacionEditText.isEnabled()) {
                                // Si la ubicación no fue seteada por defecto pedimos al usuario que
                                // la ingrese
                                nextfocus(2);
                            } else {
                                // Se la ubicación fue seteada por defecto se verifica cual es el
                                // siguiente paso
                                checkBarrido();
                            }
                        });
                        break;
                    } else {
                        if (i == detalleList.size() - 1) {
                            // Si no se encontró el producto luego de recorrer toda la lista de detalles
                            if (conSinDocSet == 0) {
                                // Si se trabaja sin documento se procede a la búsqueda en base de datos
                                presenter.verifyProducto(codigoProductoEditText.getText().toString().trim());
                            } else if (conSinDocSet == 1) {
                                // Si se trabaja con documento se notifica al usuario
                                getActivity().runOnUiThread(() -> {
                                    handler.postDelayed(() -> {
                                        codigoProductoEditText.setError(getActivity().getString(R.string.producto_no_existe_documento));
                                        codigoProductoEditText.requestFocus();
                                    }, 300);
                                    adapter.setSelectedItem(-1);
                                    nextfocus(1);
                                });
                            }
                        }
                    }
                }
            });
        }
    }


    @Override
    public void setUpUbicacion(Ubicacion ubicacion) {
        if (!TextUtils.isEmpty(ubicacion.getCodUbicacion())){
            // Si la ubicación fue seteado por defecto se actualiza la pantalla
            ubicacionEditText.setText(ubicacion.getCodUbicacion());
            ubicacionEditText.setEnabled(false);
            ubicacionData = ubicacion;
        }
    }

    @Override
    public void setUpGuia(boolean started, String nroGuia) {
        if (started){
            // Si la guía fue iniciada se bloquea el ingreso de datos a guiaEditText
            this.started = true;
            guiaEditText.setEnabled(false);
            nextfocus(1);
        } else {
            // Si la guia no está activada se libera el ingreso de datos a guiaEditText
            guiaEditText.setEnabled(true);
        }
        guiaEditText.setText(nroGuia);
    }

    @Override
    public void checkUbicacion(List<Ubicacion> ubicacion) {
        if (ubicacion.isEmpty()){
            UtilMethods.showToast(getActivity(),getActivity().getString(R.string.no_ubicacion));
            ubicacionEditText.getText().clear();
            nextfocus(2);
        } else {
            // Si se encontró la ubicación disponible para este módulo se guarda la data necesaria
            ubicacionData = ubicacion.get(0);
            // Se verifica cual es el siguiente paso
            checkBarrido();
        }
    }

    private void nextfocus(int id){
            switch (id) {
                case 0:
                    handler.postDelayed(() -> guiaEditText.requestFocus(),0);
                    break;
                case 1:
                    handler.postDelayed(() -> {
                        codigoProductoEditText.requestFocus();
                        UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                    },0);
                    break;
                case 2:
                    handler.postDelayed(() -> {
                        ubicacionEditText.requestFocus();
                        UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                    },0);
                    break;
                case 3:
                    handler.postDelayed(() -> {
                        loteDetalleEditText.requestFocus();
                        UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                    },0);
                    break;
                case 4:
                    handler.postDelayed(() -> {
                        serieDetalleEditText.requestFocus();
                        UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                    },0);
                    break;
                case 5:
                    handler.postDelayed(() -> {
                        cantidadEditText.requestFocus();
                        InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInputFromWindow(cantidadEditText.getWindowToken(),
                                InputMethodManager.SHOW_FORCED,0);
                    },0);
                    break;
                case 6:
                    handler.postDelayed(() -> {
                        cantidadEditText.requestFocus();
                        UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                    },0);
                    break;
            }
    }

    /**
     * Se ejecuta al realizar una selección en la lista de detalles mostrada por {@link DetalleDocumentoAdapter}
     * @param codProducto es el código del producto seleccionado
     * @param selectedItem es el índice del producto seleccionado en la lista
     */
    @Override
    public void setCodigo(String codProducto, int selectedItem) {
        codigoProductoEditText.setText(codProducto);
        // Determina que el producto fue seleccionado de la lista de detalles disponible
        productoLocation = 1;
        this.selectedItem = selectedItem;
        if (ubicacionEditText.isEnabled()){
            // Si la ubicación no fue seteada por defecto se le solicita al usuario ingresarla
            nextfocus(2);
        } else {
            // Si la ubicación fue seteada por defecto se verifica cual es el siguiente paso
            checkBarrido();
        }
    }

    /**
     * Determina el siguiente paso de acuerdo a las configuraciones
     */
    private void checkBarrido() {
        if (barridoSet) {
            // Si el modo de lectura es barrido
            if (loteSet) {
                // Si el registro de lote está activado se solicita al usuario que lo ingrese
                nextfocus(3);
            } else if (serieSet && !loteSet) {
                // Si el registro de lote no está activado pero el registro de serie está activad
                // se solicita al usuario que ingrese la serie
                nextfocus(4);
            } else if (!loteSet && !serieSet) {
                // Si el registro de lote y serie están desactivados se procede a verificar los datos
                verifyDataToSubmit();
                ubicacionEditText.clearFocus();
                UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
            }
        } else {
            // Si el modo de lectura es manual
            if (loteSet) {
                // Si el registro de lote está activado se solicita al usuario que lo ingrese
                nextfocus(3);
            } else {
                // Si el registro de lote no está activado se solicita al usuario que ingrese la cantidad
                nextfocus(5);
            }
        }
    }

    /**
     * Verifica los datos antes de procesar un registro de lectura
     */
    @Override
    public void verifyDataToSubmit() {
        guia = guiaEditText.getText().toString().trim();
        String producto = codigoProductoEditText.getText().toString().trim();
        String ubicacion = ubicacionEditText.getText().toString().trim();
        String lote = loteDetalleEditText.getText().toString().trim();
        String serie = serieDetalleEditText.getText().toString().trim();
        String cantidad = cantidadEditText.getText().toString().trim();
        boolean flagConSinDoc = conSinDocSet == 1; /// si esto genera problemas, setear el valor de la manera tradicionals
        /*
        if (conSinDocSet == 1) flagConSinDoc = true;
        else if (conSinDocSet == 0) flagConSinDoc = false;
        */

        // Solo se verifica que haya un número de guía ingresado cuando se trabaja en el módulo de
        // recepción
        if (TextUtils.equals(Constants.RECEPCION,type)){
            if (TextUtils.isEmpty(guia)){
                nextfocus(0);
                UtilMethods.showToast(getActivity(),getActivity().getString(R.string.debe_ingresar_guia));
                return;
            }
        } else {
            guia = "";
        }
        if (TextUtils.isEmpty(producto)){
            nextfocus(1);
            UtilMethods.showToast(getActivity(),getActivity().getString(R.string.debe_ingresar_cod_producto));
            return;
        } else {
            codigoProductoEditText.setError(null);
        }
        if (TextUtils.isEmpty(ubicacion)){
            nextfocus(2);
            UtilMethods.showToast(getActivity(),getActivity().getString(R.string.debe_ingresar_ubicacion));
            return;
        }
        if (loteSet){
            if (TextUtils.isEmpty(lote)){
                nextfocus(3);
                UtilMethods.showToast(getActivity(),getActivity().getString(R.string.debe_ingresar_lote));
                return;
            }
        } else {
            lote = "";
        }
        if (serieSet){
            if (TextUtils.isEmpty(serie)){
                nextfocus(4);
                UtilMethods.showToast(getActivity(),getActivity().getString(R.string.debe_ingresar_serie));
                return;
            } else {
                serieDetalleEditText.setError(null);
            }
        } else {
            serie = "";
        }

        cantidadEditText.clearFocus();
        UtilMethods.hideKeyboard((AppCompatActivity) getActivity());

        if (!barridoSet){
            // Verifica que la cantidad ingresada en manual cumpla los siguientes requisitos
            if (TextUtils.isEmpty(cantidad) || TextUtils.equals("0",cantidad)){
                setCantidadError(getActivity().getString(R.string.debe_ingresar_cantidad));
                return;
            }
            if (cantidad.contains(".")){
                String [] values = cantidad.split("\\.");
                if (values[0].length() > 3) {
                    setCantidadError(getActivity().getString(R.string.formato_maximo));
                    return;
                }
                if (values[1].length() > 2) {
                    setCantidadError(getActivity().getString(R.string.formato_maximo));
                    return;
                }
            } else {
                if (cantidad.length() > 3) {
                    setCantidadError(getActivity().getString(R.string.maximo_cifras));
                    return;
                }
            }
        } else {
            cantidad = "1";
        }
        cantidadEditText.setError(null);
        double cantidadNum = Double.parseDouble(cantidad);

        if (conSinDocSet == 1) {
            if (cantidadNum > detalleList.get(selectedItem).getCtdPendiente()) {
                if (!barridoSet) cantidadEditText.getText().clear();
                // Si se trabaja con documento y la cantidad ingresada sobrepasa a la cantidad pendiente
                // del producto seleccionado
                new CustomDialog.Builder(getActivity())
                        .setMessage(getActivity().getString(R.string.cantidad_mayor_pendiente))
                        .setPositiveButtonLabel(getString(R.string.label_ok))
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setPositiveButtonlistener(() -> {
                            // Se le solicita al usuario corregir la cantidad ingresada
                            nextfocus(5);
                        })
                        .setIcon(R.drawable.ic_alert)
                        .build().show();
                return;
            }
        }

        if (conSinDocSet == 0 && productoInicial == null){
            handler.postDelayed(() -> {
                codigoProductoEditText.setError(getActivity().getString(R.string.debe_seleccionar_producto));
                codigoProductoEditText.requestFocus();
            }, 300);
            return;
        }

        //-----------------------------------------------------------------------------------------//
        // Si se trabaja con documento se utiliza la data disponible en detalleList
        // Si se trabaja sin documento se utiliza la data disponible en bodyDetalle
        // Si se encontró el producto en la lista de detalles se utiliza la data disponible en detalleList
        // Si se encontró el producto en la base de datos se utiliza la data de productoInicial, significa
        // que es el primer momento en que se registra este producto en el documento

        BodyRegistrarLecturas body = new BodyRegistrarLecturas();
        body.setIdDocumento(conSinDocSet == 1 ?
                detalleList.get(selectedItem).getIdDocumento() : bodyDetalle.getIdDocumento());
        body.setIdDetalleDocumento(conSinDocSet == 1 ?
                detalleList.get(selectedItem).getIdDetalleDocumento() : 0);
        body.setIdProducto(productoLocation == 1 ?
                detalleList.get(selectedItem).getIdProducto() : productoInicial.getIdProducto());
        body.setLinea(conSinDocSet == 1 ?
                detalleList.get(selectedItem).getLinea() : 0);
        body.setCtdRequerida(conSinDocSet == 1 ?
                detalleList.get(selectedItem).getCtdRequerida() : 0.0);
        body.setCtdAtendida(productoLocation == 1 ?
                detalleList.get(selectedItem).getCtdAtendida() : 0.0);
        body.setFlgConSinDoc(flagConSinDoc);
        body.setIdUbicacion(ubicacionData.getIdUbicacion());
        body.setLoteProducto(lote);
        body.setSerieProducto(serie);
        body.setCtdConfirmada(cantidadNum);
        body.setNumDocInterno(guia);
        body.setCodUbiDes("");
        body.setCodUbicacion(ubicacionData.getCodUbicacion());
        body.setEspacioLleno("");
        body.setFchProduccion(UtilMethods.formatDateDetalle(new Date()));
        body.setCodProdCaja("");
        body.setNumCaja(0);
        body.setCodPresentacion("");
        body.setCtdPresentacion(0);
        progressBar.setVisibility(View.VISIBLE);
        detalleRecyclerView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.GONE);
        reintentarButton.setVisibility(View.GONE);

        // Se llama el registro de lecturas
        presenter.registrarLecturas(body);
    }

    private void setCantidadError(String s) {
        handler.postDelayed(() -> {
            cantidadEditText.setError(s);
            cantidadEditText.requestFocus();
        }, 300);
    }

    /**
     * Actualiza la pantalla luego del registro de una lectura
     * @param detalleDocumentoLives es la lista de detalle del documento trabajado
     */
    @Override
    public void updateDetalle(List<DetalleDocumentoLive> detalleDocumentoLives) {
        detalleList = presenter.orderDocument(detalleDocumentoLives);
        adapter.setList(detalleList);
        adapter.notifyDataSetChanged();
        adapter.setCodigoProductoSetter(this);
        detalleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        detalleRecyclerView.setAdapter(adapter);
        adapter.setSelectedItem(-1);
        detalleRecyclerView.smoothScrollToPosition(0);
        codigoProductoEditText.getText().clear();
        emptyTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        detalleRecyclerView.setVisibility(View.VISIBLE);
        errorImageView.setVisibility(View.GONE);
        reintentarButton.setVisibility(View.GONE);
        if (ubicacionEditText.isEnabled()) ubicacionEditText.getText().clear();
        if (!barridoSet) cantidadEditText.getText().clear();
        loteDetalleEditText.getText().clear();
        serieDetalleEditText.getText().clear();
        nextfocus(1);
    }

    /**
     * Muestra la pantalla de consulta de lecturas
     * @param bodyConsulta es la información necesaria para procesar una lectura
     */
    @Override
    public void consultarLecturas(BodyConsultarLecturas bodyConsulta) {
        if (TextUtils.equals(Constants.RECEPCION,type))
           ((PrincipalActivity) getActivity()).replaceFragment(LecturaFragment.newInstance(type, bodyDetalle,bodyConsulta, conSinDocSet),
                   getActivity().getString(R.string.consultar_lecturas));
        else if (TextUtils.equals(Constants.DESPACHO,type))
           ((PrincipalActivity) getActivity()).replaceFragment(LecturaFragment.newInstance(type, bodyDetalle,bodyConsulta, conSinDocSet),
                   getActivity().getString(R.string.consultar_lecturas));

    }

    /**
     * Actualiza la pantalla luego de cerrar un documento
     */
    @Override
    public void cerrarDocumento() {
        ProgressDialog.show(getActivity(),getActivity().getString(R.string.documento_cerrado));
        presenter.saveGuia(0,"");
        presenter.setConSinDoc(0);

        // Si se trabaja sin documento se elimina las clases de la base de datos
        if (conSinDocSet == 0) presenter.deleteDataClase();

        handler.postDelayed(() -> {
            ProgressDialog.dismiss();
            if (TextUtils.equals(Constants.RECEPCION,type)){
                if (impresoraSet) {
                    // Si se trabaja en el módulo de recepción y la impresión de etiquetas está activa se llama
                    // a ImpresionFragment con type = 0
                    ((PrincipalActivity) DetalleDocumentoFragment.this.getActivity()).
                            replaceFragment(ImpresionFragment.newInstance(0,bodyDetalle.getIdClaseDocumento()
                                    ,bodyDetalle.getNumDocumento()), getActivity().getString(R.string.impresion));
                } else {
                 /*
                 Se comentó este bloque a solicitud del Sr. Benito. Al comentarse, si la opción de
                 impresión está dehabilitada se procede con el flujo normal sin preguntar al usuario
                 si desea imprimir.
                  */

                     // Si se trabaja en el módulo de recepción y la impresión de etiquetas no está activa
                        // se da la opción al usuario de imprimir si es que así lo desea
//                        new CustomDialog.Builder(getActivity())
//                                .setTheme(R.style.AppTheme_Dialog_Warning)
//                                .setIcon(R.drawable.ic_alert)
//                                .setMessage(getActivity().getString(R.string.imprimir_guia_pregunta))
//                                .setPositiveButtonLabel(getActivity().getString(R.string.label_yes))
//                                .setNegativeButtonLabel(getActivity().getString(R.string.label_no))
//                                // Si desea imprimir se llama a ImpresionFragment con type = 0
//                                .setPositiveButtonlistener(() -> {
//                                    if (presenter.isBatchMode()){
//                                        // Si se está en modo batch se debe setear la impresión por medio
//                                        // del bluetooth
//                                        presenter.setImprimirBluetooth();
//                                        if (TextUtils.isEmpty(presenter.getPrinterAddress())){
//                                            // Si no hay una impresora seteada se da la opción de seleccionar una
//                                            startActivityForResult(new Intent(getActivity(),
//                                                    ImpresionBluetoothActivity.class), Constants.INTENT_BLUETOOTH);
//                                        } else {
//                                            ((PrincipalActivity) DetalleDocumentoFragment.this.getActivity())
//                                                    .replaceFragment(ImpresionFragment.newInstance(0, bodyDetalle.getIdClaseDocumento(),
//                                                            bodyDetalle.getNumDocumento()), getActivity().getString(R.string.impresion));
//                                        }
//                                    } else {
//                                        ((PrincipalActivity) DetalleDocumentoFragment.this.getActivity())
//                                                .replaceFragment(ImpresionFragment.newInstance(0, bodyDetalle.getIdClaseDocumento(),
//                                                        bodyDetalle.getNumDocumento()), getActivity().getString(R.string.impresion));
//                                    }
//                                })
//                                // Si no desea imprimir se regresa a DocumentosFragment para iniciar el flujo nuevamente
//                                .setNegativeButtonlistener(() -> backImageView.performClick())
//                                .build().show();
                    onBackPressed();
                }
            } else if (TextUtils.equals(Constants.DESPACHO,type)) {
                // Si se trabaja en el módulo de despacho se regresa a DocumentosFragment para iniciar el flujo nuevamente
                onBackPressed();;
            }
        },2500);
    }

    /*
        Se comentó esta sección por solicitud del Sr. Benito, si no es necesario preguntar al usuario si desea
        imprimir cuando la impresión de etiquetas está desactivada, tampoco es necesario dar la opción de
        seleccionar una impresora bluetooth
     */

//    /**
//     * Captura la respuesta de la selección de una impresora bluetooth en {@link ImpresionBluetoothActivity}
//     * @param requestCode es el código de la solicitud
//     * @param resultCode es el resultado de la solicitud
//     */
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == Constants.INTENT_BLUETOOTH){
//            if (resultCode == Activity.RESULT_OK){
//                if (TextUtils.isEmpty(presenter.getPrinterAddress())){
//                    // Si no se seleccionó se notifica al usuario
//                    new CustomDialog.Builder(getActivity())
//                            .setMessage(getActivity().getString(R.string.seleccionar_impresora_imprimir) + "\n" +
//                                    getActivity().getString(R.string.seleccionar_impresora_pregunta))
//                            .setPositiveButtonLabel(getString(R.string.label_ok))
//                            .setNegativeButtonLabel(getString(R.string.label_no))
//                            .setTheme(R.style.AppTheme_Dialog_Warning)
//                            // Si la respuesta es negativa se regresa a DocumentosFragment
//                            .setNegativeButtonlistener(() -> {
//                                onBackPressed();
//                            })
//                            // Si la respuesta es positiva se da la opción de seleccionar la impresora
//                            .setPositiveButtonlistener(() -> {
//                                startActivityForResult(new Intent(getActivity(),
//                                        ImpresionBluetoothActivity.class), Constants.INTENT_BLUETOOTH);
//                            })
//                            .setIcon(R.drawable.ic_alert)
//                            .build().show();
//                } else {
//                    ((PrincipalActivity) DetalleDocumentoFragment.this.getActivity())
//                            .replaceFragment(ImpresionFragment.newInstance(0, bodyDetalle.getIdClaseDocumento(),
//                                    bodyDetalle.getNumDocumento()), getActivity().getString(R.string.impresion));
//                }
//            }
//        }
//    }

    /**
     * Coteja el resultado de la verificación de serie
     * @param result es el resultado
     *               0 - la serie no está en uso
     *               1 - la serie ya está en uso
     *               2 - Error de conexión con WS
     */
    @Override
    public void checkSerieResult(int result) {
        UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
        if (result == 0){
            // Se verifica los datos
            verifyDataToSubmit();
            serieDetalleEditText.clearFocus();
            serieDetalleEditText.setError(null);
        } else if (result == 1){
            handler.postDelayed(() -> {
                serieDetalleEditText.setError(getActivity().getString(R.string.serie_ya_en_uso));
                serieDetalleEditText.requestFocus();
            }, 300);
        } else {
            UtilMethods.showToast(getActivity(),getActivity().getString(R.string.error_WS));
        }
    }

    /**
     * Verifica el resultado de la búsqueda de prducto en base de datos
     * @param productos es la lista resultado
     */
    @Override
    public void checkProducto(List<Producto> productos) {
        if (!productos.isEmpty()){
            codigoProductoEditText.setError(null);
            // Si se encontró el producto se guarda esta data
            productoInicial = productos.get(0);
            // Se determina que se encontró el producto en la base de datos
            productoLocation = 0;
            if (ubicacionEditText.isEnabled()) {
                // Si la ubicación no fue seteada por defecto se solicita al usuario que la ingrese
                nextfocus(2);
            } else {
                // Si la ubicación fue seteada por defecto de verifica el siguiente paso
                checkBarrido();
            }
        } else {
            handler.postDelayed(() -> {
                codigoProductoEditText.setError(getString(R.string.cod_producto_no_existe));
                codigoProductoEditText.requestFocus();
            }, 300);
        }
    }

    /**
     * Controlado por el botón de retroceso de Android a pedido del Sr. Benito
     */
    @Override
    public void onBackPressed() {
        if (presenter.getConSinDoc() == 0){
            // Si ejecuta luego de haber cerrado un documento
            if (TextUtils.equals(Constants.RECEPCION, type)) {
                ((PrincipalActivity) getActivity()).replaceFragment(DocumentosFragment.newInstance(type), getActivity().getString(R.string.recepcion_tilde));
            } else if (TextUtils.equals(Constants.DESPACHO, type)) {
                ((PrincipalActivity) getActivity()).replaceFragment(DocumentosFragment.newInstance(type), getActivity().getString(R.string.despacho));
            }
        } else {
            if (conSinDocSet == 1) {
                if (TextUtils.equals(Constants.DESPACHO, type)) {
                    ((PrincipalActivity) getActivity()).replaceFragment(DocumentosFragment.newInstance(type),
                            getActivity().getString(R.string.despacho));
                    presenter.saveGuia(0, "");
                    presenter.setConSinDoc(0);
                } else if (TextUtils.equals(Constants.RECEPCION, type)) {
                    if (!presenter.getGuiaCerrada()) {
                        // Si estamos trabajando en el módulo de recepción, con documento y hay una guía en proceso,
                        // notificamos al usuario que debe cerrar la guía antes de cambiar dejat esta pantalla
                        new CustomDialog.Builder(getActivity())
                                .setCancelable(false)
                                .setTheme(R.style.AppTheme_Dialog_Error)
                                .setPositiveButtonLabel(getString(R.string.label_ok))
                                .setMessage(getActivity().getString(R.string.cerra_guia))
                                .setIcon(R.drawable.ic_alert)
                                .setPositiveButtonlistener(() -> {
                                })
                                .build().show();
                    } else {
                        // Si no hay una guía en proceso regresamos a la pantalla de seleccion de documento
                        // DocumentosFragment
                        ((PrincipalActivity) getActivity()).replaceFragment(DocumentosFragment.newInstance(type),
                                getActivity().getString(R.string.recepcion_tilde));
                    }
                }
            } else if (conSinDocSet == 0) {
                // Si estamos trabajano un documento creado desde SinDocumentoConfigFragment
                // notificamos al usuario que debe cancelar el documento y le damos la opción de
                // hacerlo desde el cuadro de diálogo
                new CustomDialog.Builder(getActivity())
                        .setCancelable(false)
                        .setTheme(R.style.AppTheme_Dialog_Warning)
                        .setPositiveButtonLabel(getString(R.string.label_yes))
                        .setNegativeButtonLabel(getString(R.string.label_no))
                        .setMessage(getActivity().getString(R.string.cancelar_documento_cambio_modulo))
                        .setIcon(R.drawable.ic_alert)
                        .setPositiveButtonlistener(() -> {
                            ProgressDialog.show(getActivity(),getActivity().getString(R.string.cancelando_documento));
                            presenter.cancelarDocumento();
                        })
                        .setNegativeButtonlistener(() -> { })
                        .build().show();
            }
        }
    }

    /**
     * Determina que se muestra al usuario en caso de errores
     * @param event es el evento a ejecutar que se ejecuta en el reinicio
     * @param message mensaje a mostra al usuario
     * @param type es el tipo de error :
     *             0 - No hay internet mensaje en grilla
     *             1 - No hay internet mensaje en cuadro de dialogo
     *             2 - Error de webservice o base de datos mostrado en grilla
     *             3 - Limite de intentos de carga que lleva a la pantalla de búsqueda de documentos {@link DocumentosFragment}
     *             4 - Limite de intentos de registro que limpia las casillas para que el usuario ingrese la información nuevamente
     *             5 - Error de serie ya utilizada
     *             6 - Error al cerrar un documento
     *             7 - Error cancelando documento
     */
    @Override
    public void onError(CustomDialog.IButton event, String message, int type) {
        switch (type) {
            case 0:
            case 2:
            case 4:
            case 3:
                handler.postDelayed(() -> {
                    secondView.setVisibility(View.GONE);
                    loteSerieLinearLayout.setVisibility(loteSerieLinearLayout.getVisibility());
                    secondView.setVisibility(secondView.getVisibility());
                    loteDetalleEditText.setVisibility(loteDetalleEditText.getVisibility());
                    serieDetalleEditText.setVisibility(serieDetalleEditText.getVisibility());
                    cantidadEditText.setEnabled(false);
                    progressBar.setVisibility(View.GONE);
                    detalleRecyclerView.setVisibility(View.GONE);
                    errorImageView.setVisibility(View.VISIBLE);
                    if (type == 2) errorImageView.setImageResource(R.drawable.ic_warning);
                    emptyTextView.setVisibility(View.VISIBLE);
                    emptyTextView.setText(message);
                    reintentarButton.setVisibility(View.VISIBLE);
                    reintentarButton.setText(getActivity().getString(type == 4 || type == 3 ? R.string.label_ok : R.string.reintentar));
                }, 100);
                reintentarButton.setOnClickListener(v -> {
                    reintentarButton.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.GONE);
                    errorImageView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    if (type == 4){
                        guiaEditText.getText().clear();
                        if (detalleList != null) {
                            adapter.setSelectedItem(-1);
                            detalleRecyclerView.smoothScrollToPosition(0);
                            emptyTextView.setVisibility(View.GONE);
                        } else {
                            emptyTextView.setVisibility(View.VISIBLE);
                            emptyTextView.setText(getActivity().getString(R.string.realice_lectura));
                        }
                        progressBar.setVisibility(View.GONE);
                        codigoProductoEditText.getText().clear();
                        if (ubicacionEditText.isEnabled()) ubicacionEditText.getText().clear();
                        if (!barridoSet) cantidadEditText.getText().clear();
                        loteDetalleEditText.getText().clear();
                        serieDetalleEditText.getText().clear();
                        nextfocus(0);
                    } else if (type == 3) {
                        if (TextUtils.equals(Constants.RECEPCION, DetalleDocumentoFragment.this.type))
                            ((PrincipalActivity) getActivity()).replaceFragment(DocumentosFragment.newInstance(DetalleDocumentoFragment.this.type),
                                    getActivity().getString(R.string.recepcion_tilde));
                        else if (TextUtils.equals(Constants.DESPACHO, DetalleDocumentoFragment.this.type))
                            ((PrincipalActivity) getActivity()).replaceFragment(DocumentosFragment.newInstance(DetalleDocumentoFragment.this.type),
                                    getActivity().getString(R.string.despacho));
                    } else {
                        event.onButtonClick();
                    }
                });
                break;
            case 1:
                UtilMethods.getInternetError(getActivity(), event).show();
                break;
            case 5:
                handler.postDelayed(() -> {
                    serieDetalleEditText.setError(getActivity().getString(R.string.serie_ya_en_uso));
                    serieDetalleEditText.requestFocus();
                }, 300);
                break;
            case 6:
                new CustomDialog.Builder(getActivity())
                        .setIcon(R.drawable.ic_alert)
                        .setCancelable(false)
                        .setMessage(message)
                        .setPositiveButtonLabel(getActivity().getString(R.string.label_ok))
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .build().show();
            case 7:
                new CustomDialog.Builder(getActivity())
                        .setIcon(R.drawable.ic_alert)
                        .setCancelable(false)
                        .setMessage(message)
                        .setPositiveButtonLabel(getActivity().getString(R.string.reintentar))
                        .setPositiveButtonlistener(() -> {
                            ProgressDialog.show(getActivity(),getActivity().getString(R.string.cancelando_documento));
                            event.onButtonClick();
                        })
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .build().show();
        }
    }
}
