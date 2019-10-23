package com.dms.almacensoft.features.inventario.diferencial;

import android.view.View;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.models.GroupConsulta;
import com.google.gson.Gson;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link HeaderDiferencialViewHolder} es el encargado de llenar las cabeceras de las listas mostradas por
 * {@link HeaderDiferencialAdapter}.
 */

public class HeaderDiferencialViewHolder extends GroupViewHolder {

    @BindView(R.id.producto_text_view)
    TextView productoTextView;
    @BindView(R.id.total_text_view)
    TextView totalTextView;

    public HeaderDiferencialViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setHeader(ExpandableGroup group) {
        String data = group.getTitle();
        GroupConsulta groupConsulta = new Gson().fromJson(data, GroupConsulta.class);
        productoTextView.setText(groupConsulta.getTitulo());
        totalTextView.setText(groupConsulta.getTotal());
    }
}
