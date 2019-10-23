package com.dms.almacensoft.features.inventario.diferencial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.entities.dbtransact.ProductoDiferencial;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * {@link HeaderDiferencialAdapter} permite el llenado de la lista de lecturas agrupadas por código de producto
 * o código de ubicación en {@link DiferencialFragment}
 * Permite realizar el registro de lecturas a tráves de {@link ChildDiferencialViewHolder.EnviarData}
 * Se utiliza un tipo especial de librería que permite mostrar listas agrupadas por un criterio en el que la
 * cabecera de las listas es el criterio definido.
 */

public class HeaderDiferencialAdapter extends ExpandableRecyclerViewAdapter<HeaderDiferencialViewHolder,ChildDiferencialViewHolder> {

    private ChildDiferencialViewHolder.EnviarData enviarData = null;

    public HeaderDiferencialAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    /**
     * Setea la interfaz {@link ChildDiferencialViewHolder.EnviarData}
     * @param enviarData es el método implementado en {@link DiferencialFragment}
     */
    public void setEnviarData(ChildDiferencialViewHolder.EnviarData enviarData) {
        this.enviarData = enviarData;
    }

    @Override
    public HeaderDiferencialViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lectura_header,parent,false);
        return new HeaderDiferencialViewHolder(view);
    }

    @Override
    public ChildDiferencialViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lectura_diferencial,parent,false);
        return new ChildDiferencialViewHolder(view, enviarData);
    }

    @Override
    public void onBindChildViewHolder(ChildDiferencialViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final ProductoDiferencial diferencial = (ProductoDiferencial) group.getItems().get(childIndex);
        holder.onBind(diferencial);
    }

    @Override
    public void onBindGroupViewHolder(HeaderDiferencialViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setHeader(group);
    }
}
