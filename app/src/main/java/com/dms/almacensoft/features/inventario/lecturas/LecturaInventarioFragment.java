package com.dms.almacensoft.features.inventario.lecturas;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.features.inventario.registrar.RegistrarInventarioFragment;
import com.dms.almacensoft.features.principal.PrincipalActivity;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.features.shared.BaseFragment;
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
 * {@link LecturaInventarioFragment} permite realizar consultas sobre las lecturas realizadas en el
 * conteo actual del inventario. Las consultas pueden ser realizadas por diferentes criterios
 * (Ubicación,Código producto,Lote,Serie).
 * Permite eliminar lecturas individuales a través de {@link ChildConsultaInventarioViewHolder.OnRemove} o de un grupo de
 * lecturas a través de {@link HeaderConsultaInventarioViewHolder.OnRemove}
 * Permite también elimnar todas las lecturas realizadas en este conteo
 */
public class LecturaInventarioFragment extends BaseFragment implements LecturaInventarioContract.View {

    @BindView(R.id.search_params_linear_layout)
    LinearLayout searchLinearLayout;
    @BindView(R.id.criterio_spinner)
    Spinner criterioSpinner;
    @BindView(R.id.valor_text_input_layout)
    TextInputLayout valorEditText;
    @BindView(R.id.search_button)
    AppCompatButton searchButton;
    @BindView(R.id.lecturas_inventario_recycler_view)
    RecyclerView lecturaRecyclerView;
    @BindView(R.id.lecturas_inventario_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.empty_lectura_inv_text_view)
    TextView emptyTextView;
    @BindView(R.id.error_lectura_inv_image_view)
    ImageView errorImageView;
    @BindView(R.id.retry_lectura_inv_button)
    Button retryButton;

    private Unbinder unbinder;
    private MenuItem action;
    private MenuItem actionDelete;

    // Determina el criterio por el cual se realizará la búsqueda de lecturas
    // 0 - Ubicación
    // 1 - Código de producto
    // 2 - Lote
    // 3 - Serie
    private int selection;

    private Animation iconAnimation;
    private Animation iconAnimation2;

    private ImageView actionButton; // Se utiliza para retraer la opciones de búsqueda
    private ImageView actionButtonMore; // Se utilza para expandir las opciones de búsqueda

    private HeaderConsultaInventarioAdapter adapter;

    private Handler handler = new Handler();

    @Inject
    LecturaInventarioPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_inventario_lectura, container, false);

        unbinder = ButterKnife.bind(this, view);

        progressBar.setVisibility(View.GONE);

        lecturaRecyclerView.setVisibility(View.GONE);

        // Setea el ícono de retroceso en PrincipalActivity
        ((PrincipalActivity) getActivity()).changeToogleIcon(1,
                () -> ((PrincipalActivity) getActivity()).replaceFragment(RegistrarInventarioFragment.
                        newInstance(-1,"","",0,"",""),
                        getActivity().getString(R.string.inventario)));

        presenter.getInventario();

        return view;
    }

    @Override
    public void onDetach() {
        presenter.detachView();
        unbinder.unbind();
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.lectura_inventario, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        action = menu.findItem(R.id.action);
        LayoutInflater inflate = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        actionButton = (ImageView)inflate.inflate(R.layout.action_view, null);
        actionButton.setScaleX(0.85f);
        actionButton.setScaleY(0.85f);
        actionButtonMore = (ImageView) inflate.inflate(R.layout.action_view_more, null);
        actionButtonMore.setScaleX(0.85f);
        actionButtonMore.setScaleY(0.85f);
        action.setActionView(actionButton);

        // Retrae las opciones de búsqueda
        actionButton.setOnClickListener(v -> {
                UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                searchLinearLayout.setVisibility(View.GONE);
                iconAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_clockwise);
                iconAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        actionButton.setVisibility(View.GONE);
                        action.setActionView(actionButtonMore);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                actionButton.startAnimation(iconAnimation);
        });

        // Expande la opciones de búsqueda
        actionButtonMore.setOnClickListener(v -> {
            searchLinearLayout.setVisibility(View.VISIBLE);
            valorEditText.getEditText().requestFocus();
            iconAnimation2 = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_anticlockwise);
            iconAnimation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
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
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                // Elimina todas las lecturas realizadas en este conteo
                UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
                new CustomDialog.Builder(LecturaInventarioFragment.this.getContext())
                        .setTitle(getActivity().getString(R.string.alerta))
                        .setMessage(getActivity().getString(R.string.eliminar_lecturas_inventario))
                        .setPositiveButtonLabel(LecturaInventarioFragment.this.getString(R.string.label_yes))
                        .setNegativeButtonLabel(LecturaInventarioFragment.this.getString(R.string.label_no))
                        .setIcon(R.drawable.ic_alert)
                        .setTheme(R.style.AppTheme_Dialog_Warning)
                        .setPositiveButtonlistener(() -> {
                            progressBar.setVisibility(View.VISIBLE);
                            lecturaRecyclerView.setVisibility(View.GONE);
                            emptyTextView.setVisibility(View.GONE);
                            presenter.deleteTotalLecturas();
                        })
                        .build().show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        ProgressDialog.show(getActivity(), getActivity().getString(R.string.recopilando));
        ((PrincipalActivity) getActivity()).replaceFragment(RegistrarInventarioFragment.newInstance(-1,
                "","",0,"",""),getActivity().getString(R.string.inventario));
    }

    @Override
    public void setUpViews(int perfilTipo) {
        // Se comentó está sección por solicitud del Sr. Benito, de está forma la opción de eliminación de todas
        // las lecturas no está disponible

        // Solo mostrar la opción de eliminación del total de lecturas a los usuarios con perfil de
        // administrador
        // actionDelete.setVisible(perfilTipo == 0);

        String [] list = new String[]{getActivity().getString(R.string.ubicacion),
                getActivity().getString(R.string.codigo_producto),
                getActivity().getString(R.string.lote),
                getActivity().getString(R.string.serie)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_row, list );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        criterioSpinner.setAdapter(adapter);
        criterioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        searchButton.setOnClickListener(v -> {
            String valor = valorEditText.getEditText().getText().toString().trim();
            if (TextUtils.isEmpty(valor)){
                valorEditText.getEditText().requestFocus();
                UtilMethods.showToast(getActivity(),getActivity().getString(R.string.debe_ingresar_valor));
            } else {
                valorEditText.getEditText().getText().clear();
                lecturaRecyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                // Se realiza una búsqueda con el criterio seleccionado y el valor ingresado
                presenter.getLecturas(selection,valor);
                UtilMethods.hideKeyboard((AppCompatActivity) getActivity());
            }
        });
    }

    @Override
    public void showLecturas(List<HeaderConsultaInventario> lecturas) {
        if (lecturas.isEmpty()){
            searchLinearLayout.setVisibility(View.VISIBLE);
            lecturaRecyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(getString(R.string.no_lecturas_criterio));
            progressBar.setVisibility(View.GONE);
        } else {
            actionButton.performClick();
            adapter = new HeaderConsultaInventarioAdapter(lecturas);
            adapter.onRemoveHijo(idLecturaInventario -> {
                // Realiza la eliminación individual de lecturas
                lecturaRecyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
                presenter.deleteLectura(idLecturaInventario);
            });
            adapter.onRemovePadre(idDetalleInventario ->
                    // Permite al usuario seleccionar si desea eliminar un grupo de lecturas
                    new CustomDialog.Builder(LecturaInventarioFragment.this.getContext())
                    .setTitle(getActivity().getString(R.string.alerta))
                    .setMessage(getActivity().getString(R.string.eliminar_grupo_lectura_pregunta))
                    .setPositiveButtonLabel(LecturaInventarioFragment.this.getString(R.string.label_yes))
                    .setNegativeButtonLabel(LecturaInventarioFragment.this.getString(R.string.label_no))
                    .setIcon(R.drawable.ic_alert)
                    .setTheme(R.style.AppTheme_Dialog_Warning)
                    .setPositiveButtonlistener(() -> {
                        // Realiza la eliminación de un grupo de lecturas
                        progressBar.setVisibility(View.VISIBLE);
                        lecturaRecyclerView.setVisibility(View.GONE);
                        emptyTextView.setVisibility(View.GONE);
                        presenter.deleteDetalle(idDetalleInventario);
                    })
                    .build().show());
            lecturaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            lecturaRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext(), R.drawable.line_white_divider));
            lecturaRecyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            lecturaRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Se ejecuta cuando ocurren errores
     * @param event es el procedimiento a seguir
     * @param message es el mensaje a mostrar
     * @param type es el tipo de error ocurrido
     *             0: El móvil no tiene conexión a una red, muestra en grilla
     *             1: El móvil no tiene conexión a una red, muestra en cuadro de diálogo
     *             2: Limite de intentos de eliminación de lectura
     *             3: Error de eliminación de grupo de lecturas o total de lecturas
     */

    @Override
    public void onError(CustomDialog.IButton event, String message, int type) {
        switch (type){
            case 0:
            case 2:
            case 3:
                handler.postDelayed(() -> {
                    progressBar.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
                    emptyTextView.setText(type == 0 || type == 3 ? message : getActivity().getString(R.string.limite_intentos));
                    errorImageView.setVisibility(View.VISIBLE);
                    retryButton.setVisibility(View.VISIBLE);
                    retryButton.setText(getActivity().getString(type == 2 || type == 3 ? R.string.label_ok : R.string.reintentar));
                },200);
                retryButton.setOnClickListener(v -> {
                    progressBar.setVisibility(View.VISIBLE);
                    emptyTextView.setVisibility(View.GONE);
                    errorImageView.setVisibility(View.GONE);
                    retryButton.setVisibility(View.GONE);
                    if (type == 2) {
                        String [] array = message.split("-");
                        presenter.getLecturas(Integer.parseInt(array[0]),array[1]);
                    } else {
                        event.onButtonClick();
                    }
                });
                break;
            case 1:
                UtilMethods.getInternetError(getActivity(),event).show();
                break;
        }
    }

    /**
     * Es llamado cuando se realiza la eliminación total de las lecturas. Se muestra {@link RegistrarInventarioFragment}
     * luego de culminado el proceso
     */
    @Override
    public void showMainScreen() {
        UtilMethods.showToast(getActivity(),getActivity().getString(R.string.lecturas_eliminadas));
        ((PrincipalActivity) getActivity()).replaceFragment(RegistrarInventarioFragment.newInstance(-1,
                "","",0,"",""),getActivity().getString(R.string.inventario));
    }
}
