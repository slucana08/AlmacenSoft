package com.dms.almacensoft.features.recepciondespacho.lectura;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.models.recepciondespacho.HijoConsulta;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * {@link HeaderConsultaAdapter} permite el llenado de la lista de lecturas agrupadas por código de producto en
 * {@link LecturaFragment}.
 * Permite eliminar lecturas individuales a través de {@link ChildConsultaViewHolder.OnRemove} o de un grupo de
 * lecturas a través de {@link HeaderConsultaViewHolder.OnRemove}
 * Se utiliza un tipo especial de librería que permite mostrar listas agrupadas por un criterio en el que la
 * cabecera de las listas es el criterio definido.
 */
public class HeaderConsultaAdapter extends ExpandableRecyclerViewAdapter<HeaderConsultaViewHolder,ChildConsultaViewHolder> {

    private ChildConsultaViewHolder.OnRemove hijoRemover = null;
    private HeaderConsultaViewHolder.OnRemove padreRemover = null;

    public HeaderConsultaAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    /**
     * Setea la interfaz {@link ChildConsultaViewHolder.OnRemove}
     * @param listener es el método implementado en {@link LecturaFragment}
     */
    public void onRemoveHijo(ChildConsultaViewHolder.OnRemove listener) {
        this.hijoRemover = listener;
    }

    /**
     * Setea la interfaz {@link HeaderConsultaViewHolder.OnRemove}
     * @param listener es el método implementado en {@link LecturaFragment}
     */
    public void onRemovePadre(HeaderConsultaViewHolder.OnRemove listener) {
        this.padreRemover = listener;
    }

    @Override
    public HeaderConsultaViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lectura_header,parent,false);
        return new HeaderConsultaViewHolder(view, padreRemover);
    }

    @Override
    public ChildConsultaViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lectura_swipe_consultar,parent,false);
        return new ChildConsultaViewHolder(view, hijoRemover);
    }

    @Override
    public void onBindChildViewHolder(ChildConsultaViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final HijoConsulta lectura = (HijoConsulta) group.getItems().get(childIndex);
        lectura.setPosition(flatPosition);
        holder.onBind(lectura);
    }

    @Override
    public void onBindGroupViewHolder(HeaderConsultaViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setHeader(group,flatPosition);
    }
}
