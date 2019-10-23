package com.dms.almacensoft.features.configuracion.documentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.features.shared.BaseActivity;
import com.dms.almacensoft.features.shared.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DocumentosConfigFragment extends BaseFragment implements DocumentosConfigContract.View{

    private Unbinder unbinder;

    @BindView(R.id.zona_recepcion_textInputLayout)
    TextInputLayout zonaRecepcionEditText;
    @BindView(R.id.zona_despacho_textInputLayout)
    TextInputLayout zonaDespachoEditText;
    @BindView(R.id.zona_inventario_textInputLayout)
    TextInputLayout zonaInventarioEditText;
    @BindView(R.id.todo_documento_switch)
    SwitchCompat todoDocumentoSwitch;
    @BindView(R.id.solo_numero_switch)
    SwitchCompat soloNumeroSwitch;

    @Inject
    DocumentosConfigPresenter presenter;

    public DocumentosConfigFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
    }

    @Override
    public void onDetach() {
        presenter.detachView();
        unbinder.unbind();
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recepcion_sin_documento_config, container, false);
        unbinder = ButterKnife.bind(this,view);

        presenter.getConfig();

        return view;
    }

    @Override
    public void setUpConfig(Configuracion config) {
        todoDocumentoSwitch.setChecked(config.getTodoDocumento());
        soloNumeroSwitch.setChecked(config.getSoloNumero());
    }

    @Override
    public void onBackPressed() {

    }
}
