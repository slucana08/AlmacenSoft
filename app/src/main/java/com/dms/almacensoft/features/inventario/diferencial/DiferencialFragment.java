package com.dms.almacensoft.features.inventario.diferencial;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.models.inventario.BodyRegistrarInventario;
import com.dms.almacensoft.features.inventario.registrar.RegistrarInventarioFragment;
import com.dms.almacensoft.features.principal.PrincipalActivity;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.features.shared.BaseFragment;
import com.dms.almacensoft.utils.SimpleDividerItemDecoration;
import com.dms.almacensoft.utils.dialogs.CustomDialog;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * {@link DiferencialFragment} es la pantalla que muestra los productos que tienen difereciales en sus
 * lecturas luego de que un conteo del inventario fuera cerrado.
 * Recibe la data enviada por {@link ChildDiferencialViewHolder.EnviarData} para ser procesada en
 * {@link DiferencialPresenter} y así determinar como se realizará el registro de esa lectura.
 */
public class DiferencialFragment extends BaseFragment implements DifeferencialContract.View, View.OnTouchListener {

    @BindView(R.id.seleccion_switch)
    SwitchCompat seleccionSwitch;
    @BindView(R.id.diferencial_inventario_recycler_view)
    RecyclerView diferencialRecyclerView;
    @BindView(R.id.diferencial_inventario_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.empty_diferencial_inv_text_view)
    TextView emptyTextView;
    @BindView(R.id.retry_faltantes_inv_button)
    Button retryButton;
    @BindView(R.id.error_diferencial_inv_image_view)
    ImageView errorImageView;

    private Unbinder unbinder;

    private Handler handler = new Handler();

    private HeaderDiferencialAdapter adapter;

    private boolean isTouch = false;

    @Inject
    DiferencialPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inventario_diferencial, container, false);

        unbinder = ButterKnife.bind(this, view);

        emptyTextView.setVisibility(View.GONE);

        // Setea el ícono de retroceso en RegistrarInventarioFragment
        ((PrincipalActivity) getActivity()).changeToogleIcon(1,
                () -> ((PrincipalActivity) getActivity()).replaceFragment(RegistrarInventarioFragment.newInstance(-1,
                        "", "",0, "", ""), getActivity().getString(R.string.inventario)));

        // Se obtiene la información relacionada al inventario
        presenter.getConteo();

        seleccionSwitch.setOnTouchListener(this);
        seleccionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isTouch){
                progressBar.setVisibility(View.VISIBLE);
                diferencialRecyclerView.setVisibility(View.GONE);
                retryButton.setVisibility(View.GONE);
                errorImageView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                // Obtiene los diferenciales que serán organizados en grupos:
                // 1 - por código de producto
                // 0 - por código de ubicación
                presenter.getDiferencial(isChecked ? 1 : 0);
                isTouch = false;
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }


    @Override
    public void onBackPressed() {
        ProgressDialog.show(getActivity(), getActivity().getString(R.string.recopilando));
        ((PrincipalActivity) getActivity()).replaceFragment(RegistrarInventarioFragment.newInstance(-1,
                "", "",0, "", ""), getActivity().getString(R.string.inventario));
    }

    @Override
    public void setUpDiferencial(List<HeaderDiferencial> diferencialList) {
        if (diferencialList.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            diferencialRecyclerView.setVisibility(View.GONE);
            retryButton.setVisibility(View.GONE);
            errorImageView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(getActivity().getString(R.string.no_diferenciales));
        } else {
            progressBar.setVisibility(View.GONE);
            retryButton.setVisibility(View.GONE);
            errorImageView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.GONE);
            adapter = new HeaderDiferencialAdapter(diferencialList);
            adapter.setEnviarData((idProducto, codProducto, idUbicacion, codUbicacion, loteProducto, dscProducto) -> {
                // Captura la data del producto seleccionado
                BodyRegistrarInventario body = new BodyRegistrarInventario();
                body.setIdProducto(idProducto);
                body.setIdUbicacion(idUbicacion);
                body.setLoteProducto(loteProducto);
                body.setCodProducto(codProducto);
                body.setDscProducto(dscProducto);
                body.setCodUbicacion(codUbicacion);
                progressBar.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.GONE);
                errorImageView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                diferencialRecyclerView.setVisibility(View.GONE);
                // Envia esta data para ser procesada y determinar el tipo de registro
                presenter.determinarRegistro(body);
            });
            diferencialRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            diferencialRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext(), R.drawable.line_white_divider));
            diferencialRecyclerView.setAdapter(adapter);
            diferencialRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Se ejecuta cuando ocurre un error
     * @param event es el procedimiento a seguir
     * @param message es el mensaje a mostrar
     * @param type es el tipo de error ocurrido
     *             0: El móvil no tiene conexión a una red
     *             1: Error de conexión al WS con reintento
     *             2: No se pudo resgitrar una lectura de diferencial
     *             3: Limite de intentos para registro de una lectura
     */

    @Override
    public void onError(CustomDialog.IButton event, String message, int type) {
        handler.postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            diferencialRecyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(type == 3 ? getActivity().getString(R.string.limite_intentos) : message);
            errorImageView.setVisibility(View.VISIBLE);
            if (type != 0) errorImageView.setImageResource(R.drawable.ic_warning);
            retryButton.setVisibility(View.VISIBLE);
            retryButton.setText(getActivity().getString(type == 3 ? R.string.label_ok : R.string.reintentar));
        }, 200);
        retryButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            diferencialRecyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.GONE);
            errorImageView.setVisibility(View.GONE);
            retryButton.setVisibility(View.GONE);
            event.onButtonClick();
        });
    }

    /**
     * Muestra la pantalla de registro de lectura de inventario {@link RegistrarInventarioFragment}
      * @param body contiene toda la data necesaria que se enviará a la pantalla de registro
     */
    @Override
    public void showRegistroScreen(BodyRegistrarInventario body) {
        ProgressDialog.show(getContext(), getActivity().getString(R.string.recopilando));
        ((PrincipalActivity) getActivity()).replaceFragment(RegistrarInventarioFragment.newInstance(body.getIdProducto(),
                body.getCodProducto(), body.getDscProducto(),body.getIdUbicacion(), body.getCodUbicacion(), body.getLoteProducto()), getActivity().getString(R.string.producto_diferencial));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        isTouch = true;
        return false;
    }
}
