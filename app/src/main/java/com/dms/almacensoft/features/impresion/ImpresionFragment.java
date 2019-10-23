package com.dms.almacensoft.features.impresion;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.features.impresion.copiadocumento.CopiaDocumentoFragment;
import com.dms.almacensoft.features.impresion.etiquetalibre.EtiquetaLibreFragment;
import com.dms.almacensoft.features.principal.PrincipalActivity;
import com.dms.almacensoft.features.principal.PrincipalFragment;
import com.dms.almacensoft.features.recepciondespacho.documentos.DocumentosFragment;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.features.shared.BaseFragment;
import com.dms.almacensoft.utils.Constants;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * {@link ImpresionFragment} pantalla principal del modulo de impresión.
 * También realiza una verifica si el adaptador bluetooth está encendido antes de imprimir si es que
 * la impresión por bluetooth ha sido seleccionada
 */

public class ImpresionFragment extends BaseFragment implements ImpresionContract.View{

    @BindView(R.id.impresion_view_pager)
    ViewPager impresionViewPager;
    @BindView(R.id.impresion_tablayout)
    TabLayout impresionTabLayout;
    @BindView(R.id.print_image_view)
    ImageView imprimirImageView;
    @BindView(R.id.erase_image_view)
    ImageView limpiarImageView;

    private Unbinder unbinder;

    private ImpresionAdapter adapter;

    private int idClaseDocumento;
    private String numDocumento;

    private int type; // 0 - llamada desde módulo recepción // 1 - llamada desde menu principal

    private int menuState = 1; // 1 - expandido  // 0 - contraído

    private int impresion; // 0 - copia // 1 - libre;

    private MenuItem action;
    private MenuItem actionDelete;

    private ImageView actionButton; // Se utiliza para retraer las opciones de búsqueda en CopiaDocumentoFragment
    private ImageView actionButtonMore; // Se utiliza para expandir las opciones de búsqueda en CopiaDocumentoFragment

    private ChildAction childAction = null;
    private ImprimirEtiquetaLibre imprimirEtiquetaLibre = null;
    private ImprimirCopia imprimirCopia = null;
    private LimpiarCopia limpiarCopia = null;
    private LimpiarEtiqueta limpiarEtiqueta = null;

    private Animation iconAnimation;
    private Animation iconAnimation2;

    @Inject
    ImpresionPresenter presenter;

    /**
     * @param type determina si la llamada fue realizada desde el menú principal o desde el módulo de recepción
     *             0 - llamada desde el módulo recepción
     *             1 - llamada desde el menú principal
     * @param idClaseDocumento es el identificador de la clase a usar
     *              -1 - llamada desde el menú principal, significa que se deben cargar las clases disponibles
     *           valor - llamada deade el módulo de recepción, significa que se utilizará esta clase de documento
     * @param numDocumento es el número de documento del cual se obtendrán las guías cerradas cuando la llamada
     *                     es desde el modúlo de recepción
     * @return una instancia de {@link ImpresionFragment}
     */
    public static ImpresionFragment newInstance(int type, int idClaseDocumento, String numDocumento){
        ImpresionFragment f = new ImpresionFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.TYPE,type);
        args.putInt(Constants.ID_CLASE_DOCUMENTO,idClaseDocumento);
        args.putString(Constants.NUM_DOCUMENTO,numDocumento);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        type = getArguments().getInt(Constants.TYPE);
        idClaseDocumento = getArguments().getInt(Constants.ID_CLASE_DOCUMENTO);
        numDocumento = getArguments().getString(Constants.NUM_DOCUMENTO);

        View view = inflater.inflate(R.layout.fragment_impresion_main, container, false);

        unbinder = ButterKnife.bind(this,view);

        // Para determinar si el desplegable con los tipos de documento debe mostrarse
        presenter.checkType(type);

        return view;
    }

    @Override
    public void onDetach() {
        presenter.detachView();
        unbinder.unbind();
        super.onDetach();
    }

    /**
     * Controlado por el botón de retroceso de Android a pedido del Sr. Benito
     */
    @Override
    public void onBackPressed() {
        if (type == 0){
            ((PrincipalActivity) getActivity()).replaceFragment(DocumentosFragment.newInstance(Constants.RECEPCION),getActivity().getString(R.string.recepcion_tilde));
        } else {
            ((PrincipalActivity) getActivity()).replaceFragment(new PrincipalFragment(),
                    getActivity().getString(R.string.app_name));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.lectura_inventario, menu);
        action = menu.findItem(R.id.action);
        LayoutInflater inflate = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        actionButton = (ImageView)inflate.inflate(R.layout.action_view, null);
        actionButton.setScaleX(0.85f);
        actionButton.setScaleY(0.85f);
        actionButtonMore = (ImageView) inflate.inflate(R.layout.action_view_more, null);
        actionButtonMore.setScaleX(0.85f);
        actionButtonMore.setScaleY(0.85f);
        action.setActionView(actionButton);

        // Retrae las opciones de selección en CopiaDocumentoFragment
        actionButton.setOnClickListener(v -> {
            UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
            childAction.onClick(0);
            iconAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_clockwise);
            iconAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    menuState = 0;
                    actionButton.setVisibility(View.GONE);
                    action.setActionView(actionButtonMore);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            actionButton.startAnimation(iconAnimation);
        });

        // Expande las opciones de selección en CopiaDocumentoFragment
        actionButtonMore.setOnClickListener(v -> {
            childAction.onClick(1);
            iconAnimation2 = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_anticlockwise);
            iconAnimation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    menuState = 1;
                    actionButtonMore.setVisibility(View.GONE);
                    action.setActionView(actionButton);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            actionButtonMore.startAnimation(iconAnimation2);
        });
        actionDelete = menu.findItem(R.id.action_delete);
        actionDelete.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Llama el cambio del icono del menú de navegación en {@link PrincipalActivity}
     */
    @Override
    public void setUpNavigationUp() {
        ((PrincipalActivity) getActivity()).changeToogleIcon(1,
                () -> ((PrincipalActivity) getActivity()).replaceFragment(DocumentosFragment.newInstance(Constants.RECEPCION),
                        getActivity().getString(R.string.recepcion_tilde)));
    }

    @Override
    public void setUpViews() {
        adapter = new ImpresionAdapter(getActivity().getSupportFragmentManager(), getActivity(), type,idClaseDocumento,numDocumento);
        // Determina en que pantalla está el adapter
        // 0 - CopiaDocumentoFragment
        // 1 - EtiquetaLibreFragment
        adapter.setSlideChange(childFragment -> {
            switch (childFragment){
                case 0:
                    showMenu(0);
                    impresion = 0;
                    break;
                case 1:
                    impresion = 1;
                    hideMenu();
                    break;
            }
        });

        // Realiza la impresión de etiquetas de ambas pantallas (CopiaDocumentoFragment / EtiquetaLibreFragment)
        // Se la impresión de bluetooth está seleccionada, primero verifica que el adaptador este encencido
        imprimirImageView.setOnClickListener(v -> {
            if (presenter.getTipoImpresora()){
                if (!UtilMethods.getBluetoothAdapter().isEnabled()){
                    // Da la opción de encender el adaptor de bluetooth
                    new CustomDialog.Builder(getActivity())
                            .setTheme(R.style.AppTheme_Dialog_Warning)
                            .setIcon(R.drawable.ic_alert)
                            .setCancelable(false)
                            .setMessage(getActivity().getString(R.string.encender_adaptador_bluetooth)
                                    + "\n" + getActivity().getString(R.string.encender_adaptador_pregunta))
                            .setPositiveButtonLabel(getActivity().getString(R.string.label_yes))
                            .setPositiveButtonlistener(() -> {
                                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableIntent, Constants.ENABLE_BLUETOOTH_REQUEST);
                            })
                            .setNegativeButtonLabel(getString(R.string.label_no))
                            .setNegativeButtonlistener(() -> {
                                // Si no se desea encender el bluetooth se procede a imprimir en red
                                presenter.setTipoImpresoraRed();
                                enviarImpresion();
                            })
                            .build().show();
                } else {
                    enviarImpresion();
                }
            } else {
                enviarImpresion();
            }
        });

        limpiarImageView.setOnClickListener(v -> {
            if (impresion == 0) {
                limpiarCopia.limpiar();
            } else {
                limpiarEtiqueta.limpiar();
            }
        });

        impresionViewPager.setAdapter(adapter);
        impresionViewPager.setOffscreenPageLimit(2);
        impresionTabLayout.setupWithViewPager(impresionViewPager);
    }

    /**
     * Verifica el resultado de la solicitud de activacion de bluetooth
     * @param requestCode código de solicitud
     * @param resultCode código de resultado de solicitud
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ENABLE_BLUETOOTH_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                // Si se activo se envia la impresión
                enviarImpresion();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Si no se activo se vuelve a dar opción de activarlo
                imprimirImageView.performClick();
            }
        }
    }

    private void enviarImpresion() {
        if (impresion == 0) {
            imprimirCopia.imprimir();
        } else {
            imprimirEtiquetaLibre.imprimir();
        }
    }

    public void showMenu (int type){ // 0 el fragment esta visible // 1 se actualizo la lista de detalles
        if (type == 1){
            actionButton.performClick(); // ejecuta la acción que retrae las opciones de búsqueda en CopiaDocumentoFragment
        }
        // Mantiene el estado del desplegable al transicionar dentro del módulo de impresión
        if (menuState == 1){
            actionButton.setVisibility(View.VISIBLE);
        } else {
            actionButtonMore.setVisibility(View.VISIBLE);
        }
    }

    public void hideMenu(){
        // Esconde el desplegable al cambiar de pantalla a EtiquetaLibreFragment
        if (menuState == 1){
            actionButton.setVisibility(View.GONE);
        } else {
            actionButtonMore.setVisibility(View.GONE);
        }
    }

    public void resetMenu(){
        // Regresa el desplegable a su estado inicial
        if (menuState == 0){
            actionButtonMore.performClick();
        }
    }

    /**
     * Interfaz que permite ocultar o mostrar las opciones de búsqueda en {@link CopiaDocumentoFragment}
     */
    public interface ChildAction{
        void onClick(int action);
    }

    public void setChildAction(ChildAction childAction) {
        this.childAction = childAction;
    }

    /**
     * Interfaz que permite realizar la impresión cuando {@link EtiquetaLibreFragment} está visible
     */
    public interface ImprimirEtiquetaLibre {
        void imprimir();
    }

    public void setImprimirEtiquetaLibre(ImprimirEtiquetaLibre imprimirEtiquetaLibre) {
        this.imprimirEtiquetaLibre = imprimirEtiquetaLibre;
    }

    /**
     * Interfaz que permite realizar la impresión cuando {@link CopiaDocumentoFragment} está visible
     */
    public interface ImprimirCopia {
        void imprimir();
    }

    public void setImprimirCopia(ImprimirCopia imprimirCopia) {
        this.imprimirCopia = imprimirCopia;
    }

    /**
     * Interfaz que permite resetear {@link CopiaDocumentoFragment}
     */
    public interface LimpiarCopia{
        void limpiar();
    }

    public void setLimpiarCopia(LimpiarCopia limpiarCopia) {
        this.limpiarCopia = limpiarCopia;
    }

    /**
     * Interfaz que permite resetear {@link EtiquetaLibreFragment}
     */
    public interface LimpiarEtiqueta{
        void limpiar();
    }

    public void setLimpiarEtiqueta(LimpiarEtiqueta limpiarEtiqueta) {
        this.limpiarEtiqueta = limpiarEtiqueta;
    }
}
