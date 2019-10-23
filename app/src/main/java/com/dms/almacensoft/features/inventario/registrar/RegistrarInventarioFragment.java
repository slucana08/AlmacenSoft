package com.dms.almacensoft.features.inventario.registrar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.entities.dbalmacen.Inventario;
import com.dms.almacensoft.data.entities.dbalmacen.Producto;
import com.dms.almacensoft.data.entities.dbalmacen.Ubicacion;
import com.dms.almacensoft.data.models.inventario.BodyRegistrarInventario;
import com.dms.almacensoft.features.configuracion.general.GeneralConfigFragment;
import com.dms.almacensoft.features.principal.PrincipalActivity;
import com.dms.almacensoft.features.inventario.diferencial.DiferencialFragment;
import com.dms.almacensoft.features.inventario.lecturas.LecturaInventarioFragment;
import com.dms.almacensoft.features.principal.PrincipalFragment;
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
 * {@link RegistrarInventarioFragment} pantalla que permite realizar lecturas dentro del módulo de inventario.
 * Las lecturas pueden ser normales o llamadas desde la pantalla de diferenciales {@link DiferencialFragment}
 */

public class RegistrarInventarioFragment extends BaseFragment implements RegistrarInventarioContract.View {

    @BindView(R.id.lecturas_linear_layout)
    LinearLayout lecturasLinearLayout;
    @BindView(R.id.diferencial_linear_layout)
    LinearLayout diferencialLinearLayout;
    @BindView(R.id.ubicacion_text_input_layout)
    TextInputLayout ubicacionEditText;
    @BindView(R.id.producto_text_input_layout)
    TextInputLayout productoEditText;
    @BindView(R.id.lote_text_input_layout)
    TextInputLayout loteEditText;
    @BindView(R.id.serie_text_input_layout)
    TextInputLayout serieEditText;
    @BindView(R.id.cantidad_text_input_layout)
    TextInputLayout cantidadEditText;
    @BindView(R.id.conteo_text_view)
    TextView conteoTextView;
    @BindView(R.id.lectura_text_view)
    TextView lecturaTextView;
    @BindView(R.id.lecturas_label_text_view)
    TextView lecturasLabelTextView;
    @BindView(R.id.diferencial_label_text_view)
    TextView diferencialLabelTextView;
    @BindView(R.id.diferencial_text_view)
    TextView diferencialTextView;

    private Unbinder unbinder;

    private Handler handler = new Handler();

    private boolean loteSet;
    private boolean serieSet;
    private boolean barridoSet;

    // Data necesaria de la ubicación seleccionada
    private Ubicacion ubicacionData;
    // Determina si la ubicacion fue seteada por defecto
    private boolean ubicacionSet;
    // Data necesaria del inventario abierto disponible
    private Inventario inventarioData;
    // Data del producto seleccionado cuando la llamada es desde PrincipalActivity
    private Producto productoSelected;

    // Data del producto cuando la llamada es desde DiferencialFragment
    private int idProducto;
    private String codUbicacion;
    private int idUbicacion;
    private String codProducto;
    private String dscProducto;
    private String loteProducto;

    @Inject
    RegistrarInventarioPresenter presenter;

    /**
     * @param idProducto es el identificador único del producto seleccionado
     *                   -1 - indica que es un registro normal, llamada hecha desde {@link PrincipalActivity}
     *                valor - indica que es un registro diferencial, llamada hecha desde {@link DiferencialFragment}
     * @param codProducto es el código de un producto diferencial
     * @param dscProducto es la descripción de un producto diferencial
     * @param idUbicacion es el identificador de la ubicación de un producto diferencial
     * @param codUbicacion es el código de ubicación de un producto diferencial
     * @param loteProducto es el lote de un producto diferencial
     * @return una instancia de {@link RegistrarInventarioFragment}
     */
    public static RegistrarInventarioFragment newInstance(int idProducto, String codProducto, String dscProducto,
                                                          int idUbicacion, String codUbicacion, String loteProducto){
        RegistrarInventarioFragment f = new RegistrarInventarioFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ID_PRODUCTO, idProducto);
        args.putString(Constants.COD_PRODUCTO,codProducto);
        args.putString(Constants.DSC_PRODUCTO,dscProducto);
        args.putInt(Constants.ID_UBICACION,idUbicacion);
        args.putString(Constants.COD_UBICACION,codUbicacion);
        args.putString(Constants.LOTE_PRODUCTO,loteProducto);
        f.setArguments(args);
        return  f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((BaseActivity)getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        idProducto = getArguments().getInt(Constants.ID_PRODUCTO);
        codProducto = getArguments().getString(Constants.COD_PRODUCTO);
        dscProducto = getArguments().getString(Constants.DSC_PRODUCTO);
        idUbicacion = getArguments().getInt(Constants.ID_UBICACION);
        codUbicacion = getArguments().getString(Constants.COD_UBICACION);
        loteProducto = getArguments().getString(Constants.LOTE_PRODUCTO);

        View view = inflater.inflate(R.layout.fragment_inventario_registrar, container, false);

        if (idProducto != -1){
            // Se setea el ícono de retroceso en PrincipalActivity cuando la llamada fue hecha desde DiferencialFragment
            ((PrincipalActivity) getActivity()).changeToogleIcon(1,
                    () -> ((PrincipalActivity) RegistrarInventarioFragment.this.getActivity()).replaceFragment(new DiferencialFragment(),
                            RegistrarInventarioFragment.this.getActivity().getString(R.string.diferencial)));
        } else {
            // Se mantiene el ícono de navegación en PrincipalActivity cuando la llamada fue hecha desde allí
            ((PrincipalActivity) getActivity()).changeToogleIcon(0, null);
        }

        unbinder = ButterKnife.bind(this,view);

        // Obtenemos la información relacionada al inventario
        presenter.getConteo();

        return view;
    }


    @Override
    public void onBackPressed() {

    }

    /**
     * Muestra data y setea vistas de acuerdo a las configuraciones ingresadas en {@link GeneralConfigFragment}
     * @param ubicacion muestra la data de una ubicación seteada por defecto
     */
    @Override
    public void setUpViews(boolean lote, boolean serie, boolean barrido, Ubicacion ubicacion) {
        loteSet = lote;
        serieSet = serie;
        barridoSet = barrido;

        if (idProducto == -1) {
            // Si la llamada fue desde PrincipalActivity
            if (!TextUtils.isEmpty(ubicacion.getCodUbicacion())){
                ubicacionSet = true;
                ubicacionData = ubicacion;
                // Se bloquea el ingreso de una ubicación si es que fue seteada por defecto
                ubicacionEditText.setEnabled(false);
                ubicacionEditText.getEditText().setText(ubicacion.getCodUbicacion());
                nextfocus(1);
            } else {
                // Se permite el ingreso de la ubicación si es que no fue seteada por defecto
                ubicacionEditText.setEnabled(true);
                nextfocus(0);
            }

            // Se habilita el ingreso a la pantalla de consulta de lecturas del inventario LecturaInventarioFragment
            lecturasLinearLayout.setOnClickListener(v -> ((PrincipalActivity) RegistrarInventarioFragment.this.getActivity()).replaceFragment(new LecturaInventarioFragment(),
                    getActivity().getString(R.string.lecturas)));

            if (inventarioData.getNroConteo() > 1 && inventarioData.getDiferenciado() == 1)
                // Si el inventario no está en el primer conteo y es un inventario diferenciado se habilita
                // el acceso a la pantalla de diferenciales
                diferencialLinearLayout.setOnClickListener(v -> ((PrincipalActivity) getActivity()).replaceFragment(new DiferencialFragment(),
                        getActivity().getString(R.string.diferencial)));
            else {
                // Si no el ingreso a la pantalla de diferenciales es deshabilitado
                diferencialLinearLayout.setEnabled(false);
                diferencialLabelTextView.setTextColor(Color.LTGRAY);
                diferencialTextView.setTextColor(Color.LTGRAY);
            }
        } else {
            // Si llamada fue hecha desdeDiferencialFragment
            // Se muestra la ubicación y código que se trajó desde la pantalla de diferenciales,
            productoEditText.getEditText().setText(codProducto);
            productoEditText.setEnabled(false);
            ubicacionEditText.getEditText().setText(codUbicacion);
            ubicacionEditText.setEnabled(false);

            // Se bloquea el ingreso a la pantalla de consulta de lecturas de inventario LecturaInventarioFragment
            lecturasLinearLayout.setEnabled(false);
            lecturasLabelTextView.setTextColor(Color.LTGRAY);
            lecturaTextView.setTextColor(Color.LTGRAY);
            // Se bloquea el ingreso a la pantalla de diferenciales DiferencialFragment
            diferencialLinearLayout.setEnabled(false);
            diferencialLabelTextView.setTextColor(Color.LTGRAY);
            diferencialTextView.setTextColor(Color.LTGRAY);
        }

        if (barrido){
            // Si el modo de lectura es barrido
            cantidadEditText.getEditText().setText("1");
            cantidadEditText.setEnabled(false);
            loteEditText.setVisibility(View.GONE);
            serieEditText.setVisibility(View.GONE);
            if (lote && !serie){
                loteEditText.setVisibility(View.VISIBLE);
                serieEditText.setVisibility(View.GONE);
                loteEditText.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);

                // Si la llamada fue hecha desde DiferencialFragment se procede al ingreso de lote
                if(idProducto != -1) nextfocus(2);
            } else if (serie && !lote) {
                if (idProducto != -1 && !TextUtils.isEmpty(loteProducto)) {
                    // Si el registro de lote no está activado pero la llamada fue hecha desde
                    // DiferencialFragment y el producto seleccionado tiene lote, se muestra el lote
                    loteEditText.setVisibility(View.VISIBLE);
                    loteEditText.setEnabled(false);
                } else loteEditText.setVisibility(View.GONE);
                serieEditText.setVisibility(View.VISIBLE);

                // Si la llamada fue hecha desde DiferencialFragment se procede al ingreso de serie
                if(idProducto != -1) nextfocus(3);
            } else if (lote && serie){
                loteEditText.setVisibility(View.VISIBLE);
                serieEditText.setVisibility(View.VISIBLE);
                if (idProducto != -1 && !TextUtils.isEmpty(loteProducto)) {
                    // Si el registro de lote y serie está activado pero la llamada fue hecha desde
                    // DiferencialFragment y el producto seleccionado tiene lote, se muestra el lote
                    // pero no se permite la modificación del mismo
                    loteEditText.setEnabled(false);
                    // Se solicita el ingreso de serie
                    nextfocus(3);
                } else if (idProducto != -1) {
                    // Si el registro de lote y serie está activado pero la llamada fue hecha desde
                    // DiferencialFragment y el producto seleccionado no tiene lote, se solicita el ingreso
                    // del mismo
                    nextfocus(2);
                }
            }
        } else {
            // Si el modo de lectura es manual
            if (lote){
                loteEditText.setVisibility(View.VISIBLE);
                if (idProducto != -1 && !TextUtils.isEmpty(loteProducto)){
                    // Si el ingreso de lote está activado pero la llamada fue hecha desde
                    // DiferencialFragment, se debe bloquear la modificación de este parámetro
                    // y solicitar el ingreso de la cantidad
                    nextfocus(4);
                    loteEditText.setEnabled(false);
                }
            } else {
                if (idProducto != -1 && !TextUtils.isEmpty(loteProducto)){
                    // Si el ingreso de lote no está activado pero la llamada fue hecha desde
                    // DiferencialFragment y el producto seleccionado tiene lote, se muestra el lote
                    loteEditText.setVisibility(View.VISIBLE);
                    loteEditText.setEnabled(false);
                } else {
                    loteEditText.setVisibility(View.GONE);
                }

                // Si la llamada fue hecha desde DiferencialFragment se solicita el ingreso de la cantidad
                if(idProducto != -1) nextfocus(4);
            }
            serieEditText.setVisibility(View.GONE);
        }
        loteEditText.getEditText().setText(loteProducto);

        ubicacionEditText.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)){
                if (TextUtils.isEmpty(ubicacionEditText.getEditText().getText().toString().trim())) {
                    ubicacionEditText.setErrorEnabled(true);
                    ubicacionEditText.setError(getActivity().getString(R.string.campo_obligatorio));
                    nextfocus(0);
                    return true;
                } else {
                    ubicacionEditText.setErrorEnabled(false);
                    ProgressDialog.show(getActivity(),getActivity().getString(R.string.procesando));
                    // Se verifica que la ubicación este disponible para este módulo
                    presenter.verifyUbicacion(ubicacionEditText.getEditText().getText().toString().trim());
                    UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                    return true;
                }
            }
            return false;
        });

        productoEditText.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE ||
                    (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)){
                if (TextUtils.isEmpty(productoEditText.getEditText().getText().toString().trim())) {
                    productoEditText.setErrorEnabled(true);
                    productoEditText.setError(getActivity().getString(R.string.campo_obligatorio));
                    nextfocus(1);
                    return true;
                } else {
                    if (idProducto == -1){
                        ProgressDialog.show(getActivity(),getActivity().getString(R.string.procesando));
                        // Si la llamada fue hecha desde PrincipalFragment se verifica que el producto exista en la
                        // base de datos
                        String producto = productoEditText.getEditText().getText().toString().trim();
                        presenter.verifyProducto(producto);
                    }
                    productoEditText.setErrorEnabled(false);
                    return true;
                }
            }
            return false;
        });

        loteEditText.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE ||
                    (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)){
                if (TextUtils.isEmpty(loteEditText.getEditText().getText().toString().trim())) {
                    loteEditText.setErrorEnabled(true);
                    loteEditText.setError(getActivity().getString(R.string.campo_obligatorio));
                    nextfocus(2);
                    return true;
                } else {
                    loteEditText.setErrorEnabled(false);
                    if(serie) {
                        // Si el ingreso de serie está activado se solicita está información
                        nextfocus(3);
                    } else {
                        // Si el ingreso de serie no está activado
                        if (barrido) {
                            // Si el modo de lectura es barrido se procede a la verificación de datos
                            verifyDataToSubmit();
                            loteEditText.clearFocus();
                        } else {
                            // Si el modo de lectura es manual se procede al ingreso de cantidad
                            nextfocus(4);
                        }
                        UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                    }
                    return false;
                }
            }
            return false;
        });

        serieEditText.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)){
                if (TextUtils.isEmpty(serieEditText.getEditText().getText().toString().trim())) {
                    serieEditText.setErrorEnabled(true);
                    serieEditText.setError(getActivity().getString(R.string.campo_obligatorio));
                    nextfocus(3);
                    return true;
                } else {
                    ProgressDialog.show(getActivity(),getActivity().getString(R.string.procesando));
                    // Se verifica que la serie no esté en uso para el producto seleccionado
                    presenter.verifySerie(inventarioData.getIdInventario()
                            ,idProducto == -1 ? productoSelected.getIdProducto() : idProducto,
                            serieEditText.getEditText().getText().toString().trim());
                    return false;
                }
            }
            return false;
        });

        cantidadEditText.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)){
                if (TextUtils.isEmpty(cantidadEditText.getEditText().getText().toString().trim())) {
                    cantidadEditText.setErrorEnabled(true);
                    cantidadEditText.setError(getActivity().getString(R.string.debe_ingresar_cantidad));
                    nextfocus(5);
                    return false;
                } else {
                    // Solo se llega aquí cuando el modo de lectura es manual, se procede a la verificación de datos
                    verifyDataToSubmit();
                    cantidadEditText.clearFocus();
                    UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                    return false;
                }
            }
            return false;
        });
    }

    @Override
    public void checkUbicacion(List<Ubicacion> ubicacion) {
        ProgressDialog.dismiss();
        if (ubicacion.isEmpty()){
            ubicacionEditText.setError(getActivity().getString(R.string.ubicacion_no_existe));
            ubicacionEditText.getEditText().getText().clear();
            nextfocus(0);
        } else {
            // Si se encontró la ubicación disponible para este módulo se guarda la data necesaria
            ubicacionSet = false;
            ubicacionData = ubicacion.get(0);
            if (barridoSet && !loteSet && !serieSet)
                productoEditText.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            // Se solicita el ingreso del código de producto
            nextfocus(1);
        }
    }

    @Override
    public void checkProducto(List<Producto> productos) {
        ProgressDialog.dismiss();
        if (!productos.isEmpty()){
            // Si el producto fue encontrado en la base de datos se guarda la información necesaria
            productoSelected = productos.get(0);
            // Se verifica cual es el siguiente paso
            checkBarrido();
        } else {
            nextfocus(1);
            productoEditText.setError(getActivity().getString(R.string.cod_producto_no_existe));
            productoEditText.getEditText().getText().clear();
        }
    }

    /**
     * Determina el siguiente paso de acuerdo a las configuraciones
     */
    private void checkBarrido() {
        if (barridoSet) {
            // Si el modo de lectura es barrido
            if (loteSet) {
                // Si el ingreso de lote esta activado se solicita que se ingrese
                nextfocus(2);
            } else if (serieSet) {
                // Si el ingreso de serie esta activado se solicita que se ingrese
                nextfocus(3);
            } else {
                // Si el ingreso de lote y serie están desactivados se procede a la verificación de
                // datos
                verifyDataToSubmit();
                productoEditText.getEditText().clearFocus();
                UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
            }
        } else {
            // Si el modo de lectura es manual
            if (loteSet) {
                // Si el ingreso de lote esta activado se solicita que se ingrese
                nextfocus(2);
            } else {
                // Si el ingreso de lote no está activado se solicita el ingreso de cantidad
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInputFromWindow(cantidadEditText.getWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
                nextfocus(4);
            }
        }
    }

    @Override
    public void setUpConteo(List<Inventario> inventario) {
         inventarioData = inventario.get(0);
         conteoTextView.setText(String.valueOf(inventarioData.getNroConteo()));
         // Se obtienen las configuraciones del aplicativo
         presenter.getConfig();
         // Se obtiene la cantidad de lecturas que tiene el inventario
         presenter.getLecturas(inventarioData.getIdInventario());
    }

    @Override
    public void setUpLecturas(String lecturas) {
        lecturaTextView.setText(lecturas);
        if (inventarioData.getNroConteo() > 1 && inventarioData.getDiferenciado() == 1) {
            // Si el inventario no está en el primer conteo y es un inventario diferenciado se obtiene
            // la cantidad de producto con diferencial
            presenter.getDiferencial(inventarioData.getIdInventario(), inventarioData.getNroConteo());
        } else {
            ProgressDialog.dismiss();
        }
    }

    @Override
    public void showDiferencial(int count) {
        ProgressDialog.dismiss();
        diferencialTextView.setText(String.valueOf(count));
    }

    /**
     * Realiza la verificación de datos antes de proceder con el registro de una lectura
     */
    @Override
    public void verifyDataToSubmit() {
        String ubicacion = ubicacionEditText.getEditText().getText().toString().trim();
        String producto = productoEditText.getEditText().getText().toString().trim();
        String lote = loteEditText.getEditText().getText().toString().trim();
        String serie = serieEditText.getEditText().getText().toString().trim();
        String cantidad = cantidadEditText.getEditText().getText().toString().trim();

        if (ubicacionEditText.getEditText().isEnabled()) {
            if (TextUtils.isEmpty(ubicacion)) {
                ubicacionEditText.setErrorEnabled(true);
                ubicacionEditText.setError(getActivity().getString(R.string.campo_obligatorio));
                nextfocus(0);
                return;
            }
        }
        if (TextUtils.isEmpty(producto)){
            productoEditText.setErrorEnabled(true);
            productoEditText.setError(getActivity().getString(R.string.campo_obligatorio));
            nextfocus(1);
            return;
        }

        if (loteSet){
            if (TextUtils.isEmpty(lote)){
                loteEditText.setErrorEnabled(true);
                loteEditText.setError(getActivity().getString(R.string.campo_obligatorio));
                nextfocus(2);
                return;
            }
        } else {
            // Si el ingreso de lote está desactivado pero la llamada fue hecha desde DiferencialFragment
            // y el producto seleccionado tiene lote se envía esa información.
            // Si no se envía el dato ingresado en loteEditText
            lote = idProducto != -1 ? loteProducto : "";
        }

        if (serieSet){
            if (TextUtils.isEmpty(serie)){
                serieEditText.setErrorEnabled(true);
                serieEditText.setError(getActivity().getString(R.string.campo_obligatorio));
                nextfocus(3);
            }
        } else {
            serie = "";
        }

        if (!barridoSet){
            // Verifica que la cantidad ingresada en manual cumpla los siguientes requisitos
            if (TextUtils.isEmpty(cantidad) || TextUtils.equals("0",cantidad)){
                cantidadEditText.setErrorEnabled(true);
                cantidadEditText.setError(getActivity().getString(R.string.debe_ingresar_cantidad));
                nextfocus(5);
                return;
            }
            if (cantidad.contains(".")){
                String [] values = cantidad.split("\\.");
                    if (values[0].length() > 3) {
                        cantidadEditText.setErrorEnabled(true);
                        cantidadEditText.setError(getActivity().getString(R.string.formato_maximo));
                        nextfocus(5);
                        return;
                    }
                    if (values[1].length() > 2) {
                        cantidadEditText.setErrorEnabled(true);
                        cantidadEditText.setError(getActivity().getString(R.string.formato_maximo));
                        nextfocus(5);
                        return;
                    }
            } else {
               if (cantidad.length() > 3) {
                    cantidadEditText.setErrorEnabled(true);
                    cantidadEditText.setError(getActivity().getString(R.string.maximo_cifras));
                    nextfocus(5);
                    return;
               }
            }
            cantidadEditText.setErrorEnabled(false);
            cantidadEditText.clearFocus();
        } else {
            cantidad = "1";
        }

        if (idProducto == -1 && productoSelected == null){
            nextfocus(1);
            productoEditText.setError(getString(R.string.debe_seleccionar_producto));
            return;
        }

        BodyRegistrarInventario bodyRegistro = new BodyRegistrarInventario();
        bodyRegistro.setIdInventario(inventarioData.getIdInventario());
        // Si la llamada se realizo desde PrincipalActivity se utiliza la ubicacion seteada por defecto
        // o ingresada por el usuario.
        // Si la llamada se realizó desde DiferencialFragment se utiliza la data de la ubicación asignada
        // al producto diferencial seleccionado
        bodyRegistro.setIdUbicacion(idProducto == -1 ? ubicacionData.getIdUbicacion() : idUbicacion);
        // Si la llamada se realizo desde PrincipalActivity se utiliza el producto ingresado por el usuario.
        // Si la llamada se realizó desde DiferencialFragment se utiliza la data del producto diferencial seleccionado
        bodyRegistro.setIdProducto(idProducto == -1 ? productoSelected.getIdProducto() : idProducto);
        bodyRegistro.setCodProducto(idProducto == -1 ? productoSelected.getCodProducto() : codProducto);
        bodyRegistro.setDscProducto(idProducto == -1 ? productoSelected.getDscProducto() : dscProducto);
        bodyRegistro.setLoteProducto(lote);
        bodyRegistro.setSerieProducto(serie);
        bodyRegistro.setCantidadLeida(Double.parseDouble(cantidad));
        bodyRegistro.setEspacioLleno(0);
        bodyRegistro.setCodProdCaja("");
        bodyRegistro.setNumCaja(0);
        bodyRegistro.setCodPresentacion("");
        bodyRegistro.setCtdPresentacion("0");
        ProgressDialog.show(getActivity(),getActivity().getString(R.string.registrando_lectura));
        // Se realiza el registro de la lectura
        presenter.registrarLectura(bodyRegistro);
    }

    /**
     * Actualiza la pantalla luego del registro de una lectura
     * @param type determina el resultado del registro
     *             0 - exitoso
     *             1 - fállido
     */
    @Override
    public void updateViews(int type) {
        if (type == 0) {
            ProgressDialog.dismiss();
            UtilMethods.showToast(getActivity(), getActivity().getString(R.string.lectura_registrada));
        }

        if (idProducto == -1) {
            ProgressDialog.show(getActivity(),getActivity().getString(R.string.recopilando));

            // Actualizamos el conteo de lecturas para este inventario
            presenter.getLecturas(inventarioData.getIdInventario());

            // Si la llamada fue hecha desde PrincipalFragment
            if (!ubicacionSet) {
                // Si la ubicacion no fue seteada por defecto
                if (type == 1) {
                    // Si el registro de la lectura fue fállido se solicita el ingreso de la ubicación
                    ubicacionEditText.getEditText().getText().clear();
                    nextfocus(0);
                } else {
                    // Si el registro de la lectura fue exitoso se se mantiene la ubicación y se solicita el ingreso de
                    // código de producto
                    nextfocus(1);
                }
            } else {
                // Si la ubicación fue seteado por defecto solicitamos ingreso de código de producto
                nextfocus(1);
            }
            productoEditText.getEditText().getText().clear();
            loteEditText.getEditText().getText().clear();
            serieEditText.getEditText().getText().clear();
            if (!barridoSet) {
                // Si el modo de lectura es manual se resetea la cantidad
                cantidadEditText.getEditText().getText().clear();
            }
        } else {
            ((PrincipalActivity) getActivity()).replaceFragment(new DiferencialFragment(),
                    getActivity().getString(R.string.diferencial));

            // Si la llamada fue hecha desde DiferencialFragment
            // Las siguientes validaciones se realizaban si luego del registro se mantenía esta pantalla
            // activa para permitir el registro de más lecturas para le mismo producto diferencial
//            ubicacionEditText.setEnabled(false);
//            productoEditText.setEnabled(false);
//            if (barridoSet){
//                // Si el modo de lectura es barrido
//                if (loteSet){
//                    // Si el ingreso de lote está activado
//                    if (serieSet){
//                        // Si el ingreso de serie está activado limpiamos la data ingresada en
//                        // serieEditText
//                        serieEditText.getEditText().getText().clear();
//                        if (TextUtils.isEmpty(loteProducto)) {
//                            // Si el producto diferencial no tenía lote, limpiamos la data ingresada
//                            // en loteEditText y solicitamos el ingreso del nuevo lote
//                            loteEditText.getEditText().getText().clear();
//                            nextfocus(2);
//                        } else {
//                            // Si no conservamos la data del lote y solicitamos el ingreso de la serie
//                            loteEditText.setEnabled(false);
//                            nextfocus(3);
//                        }
//                    } else {
//                        // Si el ingreso de serie está desactivado, limpiamos la data ingresada en
//                        // loteEditText y solicitamos el ingreso de el nuevo lote
//                        loteEditText.getEditText().getText().clear();
//                        nextfocus(2);
//                    }
//                } else {
//                    // Si el ingreso de lote no está activado
//                    if (serieSet){
//                        // Si el ingreso de serie está activado limpiamos la data ingresada en
//                        // serieEditText y solicitamos el ingreso de la nueva serie
//                        serieEditText.getEditText().getText().clear();
//                        nextfocus(3);
//                    }
//                }
//            } else {
//                // Si el modo de lectura es manual
//                if (loteSet){
//                    // Si el ingreso de lote está activado
//                    if (TextUtils.isEmpty(loteProducto)) {
//                        // Si el producto diferencial no tenía lote, limpiamos la data ingresada
//                        // en loteEditText y solicitamos el ingreso del nuevo lote
//                        loteEditText.getEditText().getText().clear();
//                        nextfocus(2);
//                    } else {
//                        // Si no conservamos la data del lote, limpiamos la data ingresada en
//                        // cantidadEditText y solicitamos el ingreso de la nueva cantidad
//                        loteEditText.setEnabled(false);
//                        cantidadEditText.getEditText().getText().clear();
//                        nextfocus(4);
//                    }
//                } else {
//                    // Si el ingreso de lote está desactivado limpiamos la data ingresada en
//                    // cantidadEditText y solicitamos el ingreso de la nueva cantidad
//                    cantidadEditText.getEditText().getText().clear();
//                    nextfocus(4);
//                }
//            }
        }

    }

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
        ProgressDialog.dismiss();
        if (result == 0){
            // Se verifica los datos
            verifyDataToSubmit();
            serieEditText.setErrorEnabled(false);
            serieEditText.clearFocus();
        } else if (result == 1){
            handler.postDelayed(() -> {
                serieEditText.setErrorEnabled(true);
                serieEditText.setError(getActivity().getString(R.string.serie_ya_en_uso));
                serieEditText.requestFocus();
            }, 100);
        } else {
            UtilMethods.showToast(getActivity(),getActivity().getString(R.string.error_WS));
        }
    }

    /**
     * Se ejecutan cuando ocurren errores
     * @param event es el procedimiento a seguir
     * @param message el mensaje de error a mostrar
     * @param type el tipo de error ocurrido
     *             0: El móvil no tiene conexión a una red, se llama un ProgressDialog en reintento
     *             1: El móvil no tiene conexión a una red
     *             2: Error de conexión con WS con reintento
     *             3: Error de registro de lectura, con reintento
     *             4: Límite de reintentos
     */

    @Override
    public void onError(CustomDialog.IButton event, String message, int type) {
        switch (type){
            case 0:
            case 1:
                UtilMethods.getInternetError(getActivity(), type == 1 ? event : (CustomDialog.IButton) () -> {
                    ProgressDialog.show(getActivity(),message);
                    event.onButtonClick();
                }).show();
                break;
            case 2:
            case 3:
                new CustomDialog.Builder(getActivity())
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setIcon(R.drawable.ic_alert)
                        .setMessage(getActivity().getString(type == 2 ? R.string.error_WS : R.string.no_pudo_registro))
                        .setPositiveButtonLabel(getActivity().getString(R.string.reintentar))
                        .setPositiveButtonlistener(() -> {
                            ProgressDialog.show(RegistrarInventarioFragment.this.getActivity(), message);
                            event.onButtonClick();
                        })
                        .setCancelable(false)
                        .build().show();
                break;
            case 4:
                new CustomDialog.Builder(getActivity())
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setIcon(R.drawable.ic_alert)
                        .setMessage(getActivity().getString(R.string.limite_intentos))
                        .setPositiveButtonLabel("OK")
                        .setPositiveButtonlistener(() -> updateViews(1))
                        .setCancelable(false)
                        .build().show();
                break;
            case 5:
                new CustomDialog.Builder(getActivity())
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setIcon(R.drawable.ic_alert)
                        .setMessage(getActivity().getString(R.string.conteo_cerrado))
                        .setPositiveButtonLabel("OK")
                        .setPositiveButtonlistener(() -> ((PrincipalActivity) getActivity()).replaceFragment(new PrincipalFragment(),"AlmacenSoft"))
                        .setCancelable(false)
                        .build().show();
                break;
        }
    }

    private void nextfocus(int id){
        switch (id) {
            case 0:
                handler.postDelayed(() -> {
                    ubicacionEditText.getEditText().requestFocus();
                    UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                },0);
                break;
            case 1:
                handler.postDelayed(() -> {
                    productoEditText.getEditText().requestFocus();
                    UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                },0);
                break;
            case 2:
                handler.postDelayed(() -> {
                    loteEditText.getEditText().requestFocus();
                    UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                },0);
                break;
            case 3:
                handler.postDelayed(() -> {
                    serieEditText.getEditText().requestFocus();
                    UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                },0);
                break;
            case 4:
                handler.postDelayed(() -> {
                    cantidadEditText.getEditText().requestFocus();
                    InputMethodManager imm = (InputMethodManager)   getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInputFromWindow(cantidadEditText.getWindowToken(),
                            InputMethodManager.SHOW_FORCED,0);
                },0);
                break;
            case 5:
                handler.postDelayed(() -> {
                    cantidadEditText.getEditText().requestFocus();
                },0);
        }
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }
}
