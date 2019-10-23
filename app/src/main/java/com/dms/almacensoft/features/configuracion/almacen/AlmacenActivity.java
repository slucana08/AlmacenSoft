package com.dms.almacensoft.features.configuracion.almacen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.entities.dbalmacen.Almacen;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.features.principal.PrincipalActivity;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.utils.SimpleDividerItemDecoration;
import com.dms.almacensoft.utils.TextWatcher;
import com.dms.almacensoft.utils.UtilMethods;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link AlmacenActivity} pantalla de selección del almacen con el cual se trabajará, luego de seleccionar
 * un almacén se mostrará {@link PrincipalActivity}
 * Implementa {@link AlmacenAdapter.Selection} que permite ejecutar acciones al seleccionar un almacen de la lista
 */

public class AlmacenActivity extends BaseActivity implements AlmacenContract.View, AlmacenAdapter.Selection {

    @Inject
    AlmacenPresenter presenter;

    @Inject
    AlmacenAdapter adapter;

    @BindView(R.id.almacen_empty_text_view)
    TextView emptyTextView;
    @BindView(R.id.almacen_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.almacen_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.filter_edit_text)
    EditText filterEditText;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacen);

        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        presenter.attachView(this);

        progressBar.setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        // Capturamos la data de la BD
        handler.postDelayed(() -> presenter.getData(),200);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void setAlmacen(List<Almacen> list) {
        progressBar.setVisibility(View.GONE);
        if (list.isEmpty()){
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            adapter.setSelection(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this, R.drawable.line_black_divider));
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);

            adapter.setList(list, "");

            filterEditText.addTextChangedListener((TextWatcher) filter -> adapter.setList(list, filter));

            filterEditText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    UtilMethods.hideKeyboard(AlmacenActivity.this);
                    filterEditText.clearFocus();
                }
                return false;
            });
        }
    }

    @Override
    public void showMainScreen() {
       startActivity(new Intent(AlmacenActivity.this, PrincipalActivity.class));
       finish();
    }

    /**
     * Se ejecuta al selecionar un almacén de la lista y captura la data necesaria para la ejecución
     * de procedimientos en la aplicación
     */
    @Override
    public void onSelect(int idAlmacen, String dscAlmacen, String codAlmacen, String codEmpresa, int idEmpresa) {
        Configuracion config = new Configuracion();
        config.setIdAlmacen(idAlmacen);
        config.setDscAlmacen(dscAlmacen);
        config.setCodAlmacen(codAlmacen);
        config.setCodEmpresa(codEmpresa);
        config.setIdEmpresa(idEmpresa);
        presenter.saveAlmacenConfig(config);
    }

    @Override
    public void onBackPressed() {
        Runtime.getRuntime().exit(0);
    }
}
