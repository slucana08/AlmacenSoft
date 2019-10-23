package com.dms.almacensoft.features.impresion.etiquetalibre;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.entities.dbalmacen.Producto;
import com.dms.almacensoft.data.models.impresion.BodyImprimirEtiqueta;
import com.dms.almacensoft.features.impresion.ImpresionFragment;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.features.shared.BaseFragment;
import com.dms.almacensoft.utils.PrinterHelper;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.CustomDialog;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * {@link EtiquetaLibreFragment} pantalla desde la cual se puede realizar la impresión de una etiqueta libre,
 * permite determinar si el producto será buscado en los maestros
 * Implementa {@link ImpresionFragment.ImprimirEtiquetaLibre} que permite realizar la impresión dentro de esta pantalla
 * y {@link ImpresionFragment.LimpiarEtiqueta} que permite resetear la búsqueda en esta pantalla
 */

public class EtiquetaLibreFragment extends BaseFragment implements EtiquetaLibreContract.View,
        ImpresionFragment.ImprimirEtiquetaLibre, ImpresionFragment.LimpiarEtiqueta, View.OnTouchListener {

    @BindView(R.id.validar_switch)
    SwitchCompat validarSwitch;
    @BindView(R.id.codigo_text_input_layout)
    TextInputLayout codigoEditText;
    @BindView(R.id.descripcion_text_input_layout)
    TextInputLayout descripcionEditText;
    @BindView(R.id.lote_text_input_layout)
    TextInputLayout loteEditText;
    @BindView(R.id.serie_text_input_layout)
    TextInputLayout serieEditText;
    @BindView(R.id.mas_image_view)
    ImageView masImageView;
    @BindView(R.id.copias_text_view)
    TextView copiasTextView;
    @BindView(R.id.menos_image_view)
    ImageView menosImageView;

    private Unbinder unbinder;

    private Handler handler = new Handler();

    private boolean validar; // determina si se debe buscar producto en maestros
    private int copiasCantidad = 1;

    private Producto productoSelected;

    private boolean isTouch = false;

    @Inject
    EtiquetaLibrePresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_impresion_etiqueta, container, false);

        unbinder = ButterKnife.bind(this,view);

        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("current");
        if(fragment != null && fragment instanceof ImpresionFragment) {
            ((ImpresionFragment) fragment).setImprimirEtiquetaLibre(this);
            ((ImpresionFragment) fragment).setLimpiarEtiqueta(this);
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
        validarSwitch.setOnTouchListener(this);
        validarSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isTouch){
                nextfocus(0);
                validar = validarSwitch.isChecked();
                isTouch = false;
            }
        });

        codigoEditText.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE
                    || (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                if (validar) {
                    // Busca el código de producto en la base de datos
                    if (TextUtils.isEmpty(codigoEditText.getEditText().getText().toString().trim())) {
                        codigoEditText.setErrorEnabled(true);
                        codigoEditText.setError("Debe ingresar el código de producto");
                        nextfocus(0);
                        return true;
                    } else {
                        codigoEditText.setErrorEnabled(false);
                        ProgressDialog.show(EtiquetaLibreFragment.this.getActivity(), "Buscando...");
                        presenter.getProducto(codigoEditText.getEditText().getText().toString().trim());
                        return true;
                    }
                } else {
                    // Permite realizar un ingreso libre de datos
                    if (TextUtils.isEmpty(codigoEditText.getEditText().getText().toString().trim())) {
                        codigoEditText.setErrorEnabled(true);
                        codigoEditText.setError("Debe ingresar el código de producto");
                        nextfocus(0);
                        return true;
                    } else {
                        codigoEditText.setErrorEnabled(false);
                        nextfocus(1);
                        masImageView.setImageResource(R.drawable.ic_add);
                        masImageView.setEnabled(true);
                        return false;
                    }
                }
            }
            return false;
        });

        serieEditText.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE
                || (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                serieEditText.getEditText().clearFocus();
            }
            return false;
        });

        descripcionEditText.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE
                    || (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                if (TextUtils.isEmpty(descripcionEditText.getEditText().getText().toString().trim())) {
                    descripcionEditText.setErrorEnabled(true);
                    descripcionEditText.setError("Debe ingresar la descripción de producto");
                    nextfocus(1);
                    return true;
                } else {
                    descripcionEditText.setErrorEnabled(false);
                    UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                    nextfocus(2);
                    return false;
                }
            }
            return false;
        });

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
            if(copiasCantidad == 2){
                menosImageView.setImageResource(R.drawable.ic_minus);
                menosImageView.setEnabled(true);
            }
        });
        copiasTextView.setText(String.valueOf(copiasCantidad));
    }

    @Override
    public void setUpProducto(List<Producto> list) {
        ProgressDialog.dismiss();
        if (list.isEmpty()){
            codigoEditText.setErrorEnabled(true);
            codigoEditText.setError("Producto no existe en base de datos");
        } else {
            codigoEditText.setErrorEnabled(false);
            productoSelected = list.get(0);
            descripcionEditText.getEditText().setText(productoSelected.getDscProducto());
            masImageView.setImageResource(R.drawable.ic_add);
            masImageView.setEnabled(true);
            nextfocus(2);
        }
    }

    /**
     * Se ejecuta cuando ocurre un error
     * @param event es el procedimiento a seguir
     * @param message es el mensaje a mostrar en cuado de diálogo o en el ProgressDialos
     * @param type determina el tipo de error ocurrido
     *             0 - El móvil no tiene internet - cuadro de diálogo
     *             1 - Error de WS al imprimir la etiqueta
     *             2 - Limite de reintentos de impresión
     */
    @Override
    public void onError(CustomDialog.IButton event, String message, int type) {
        switch (type){
            case 0:
                ProgressDialog.show(getActivity(),message);
                UtilMethods.getInternetError(getActivity(),event).show();
                break;
            case 1:
            case 2:
                new CustomDialog.Builder(getActivity())
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setIcon(R.drawable.ic_alert)
                        .setMessage(type == 2 ? getActivity().getString(R.string.limite_intentos) : getString(R.string.no_pudo_imprimir))
                        .setPositiveButtonLabel(type == 2 ? getActivity().getString(R.string.label_ok) : getActivity().getString(R.string.reintentar))
                        .setPositiveButtonlistener(() -> {
                            if (type == 1){
                                ProgressDialog.show(getActivity(), message);
                                event.onButtonClick();
                            } else {
                                limpiar();
                            }
                        })
                        .setCancelable(false)
                        .build().show();
                break;
        }
    }

    /**
     * Verifica resultado de impresión por bluetooth
     */
    @Override
    public void checkPrintResult() {
        handler.postDelayed(() -> {
            ProgressDialog.dismiss();
            if (PrinterHelper.getResult()){
                // Si conexión fue exitosa se procede a actualizar las etiquetas
                UtilMethods.showToast(getActivity(), "Impresión realizada");
            } else {
                // Si conexión fue fállida se procede a notificar al usuario
                ProgressDialog.dismiss();
                new CustomDialog.Builder(getActivity())
                        .setTheme(R.style.AppTheme_Dialog_Error)
                        .setIcon(R.drawable.ic_alert)
                        .setMessage("Error de impresión: " +
                                "\nRevise que la impresora esté prendida y que su adaptador bluetooth esté activado")
                        .setPositiveButtonLabel(getString(R.string.label_ok))
                        .setCancelable(false)
                        .build().show();
            }
        },6000); // se setea el tiempo a 6 segundos para dar tiempo a que se cree la conexión con la impresora
    }

    private void nextfocus(int id){
        switch (id) {
            case 0:
                handler.postDelayed(() -> codigoEditText.getEditText().requestFocus(),0);
                break;
            case 1:
                handler.postDelayed(() -> {
                    descripcionEditText.getEditText().requestFocus();
                },0);
                break;
            case 2:
                handler.postDelayed(() -> {
                    loteEditText.getEditText().requestFocus();
                },0);
                break;
            case 3:
                handler.postDelayed(() -> {
                    serieEditText.getEditText().requestFocus();
                },0);
                break;
        }
    }

    @Override
    public void imprimir() {
        String codigo = codigoEditText.getEditText().getText().toString().trim();
        String descripcion = descripcionEditText.getEditText().getText().toString().trim();
        String lote = loteEditText.getEditText().getText().toString().trim();
        String serie = serieEditText.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(codigo)) {
            codigoEditText.setErrorEnabled(true);
            codigoEditText.setError("Debe ingresar el código de producto");
            nextfocus(0);
            return;
        }

        if (TextUtils.isEmpty(descripcion)) {
            descripcionEditText.setErrorEnabled(true);
            descripcionEditText.setError("Debe ingresar la descripción del producto");
            nextfocus(1);
            return;
        }

        BodyImprimirEtiqueta etiqueta = new BodyImprimirEtiqueta();
        etiqueta.setNumCopias(copiasCantidad);
        etiqueta.setCodigoProducto(codigo);
        etiqueta.setDescripcionProducto(descripcion);
        etiqueta.setLote(lote);
        etiqueta.setSerie(serie);

        ProgressDialog.show(getActivity(),"Imprimiendo...");
        presenter.imprimirEtiquetas(etiqueta);
    }

    @Override
    public void limpiar() {
        validarSwitch.setChecked(false);
        validar = false;
        codigoEditText.getEditText().getText().clear();
        codigoEditText.getEditText().requestFocus();
        descripcionEditText.getEditText().getText().clear();
        loteEditText.getEditText().getText().clear();
        serieEditText.getEditText().getText().clear();
        menosImageView.setImageResource(R.drawable.ic_minus_gray);
        menosImageView.setEnabled(false);
        masImageView.setImageResource(R.drawable.ic_add_gray);
        masImageView.setEnabled(false);
        copiasCantidad = 1;
        copiasTextView.setText(String.valueOf(copiasCantidad));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        isTouch = true;
        return false;
    }
}
