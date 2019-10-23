package com.dms.almacensoft.features.recepciondespacho.lectura;

import android.view.View;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.models.GroupConsulta;
import com.dms.almacensoft.data.models.recepciondespacho.HijoConsulta;
import com.google.gson.Gson;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link HeaderConsultaViewHolder} es el encargado de llenar las cabeceras de las listas mostradas por
 * {@link HeaderConsultaAdapter}.
 */
public class HeaderConsultaViewHolder extends GroupViewHolder {

    @BindView(R.id.producto_text_view)
    TextView productoTextView;
    @BindView(R.id.total_text_view)
    TextView totalTextView;

    private List<HijoConsulta> items;
    private OnRemove listener = null;

    public HeaderConsultaViewHolder(View itemView, OnRemove listener) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.listener = listener;
        itemView.setOnLongClickListener(view -> {
            listener.onRemovePadre(items);
            return true;
        });
    }

    public void setHeader(ExpandableGroup group, int position) {
        String data = group.getTitle();
        GroupConsulta groupConsulta = new Gson().fromJson(data, GroupConsulta.class);
        items = group.getItems();
        productoTextView.setText(groupConsulta.getTitulo());
        totalTextView.setText(groupConsulta.getTotal());
    }

    /**
     * Interfaz que permite la eliminación de un grupo de lecturas, envia la data necesaria a {@link LecturaFragment}
     * para realizar esta eliminación
     */
    public interface OnRemove {
        void onRemovePadre(List<HijoConsulta> items);
    }
}
