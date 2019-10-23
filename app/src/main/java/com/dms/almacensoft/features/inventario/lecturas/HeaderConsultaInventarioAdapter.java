package com.dms.almacensoft.features.inventario.lecturas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.models.inventario.HijoConsultaInventario;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * {@link HeaderConsultaInventarioAdapter} permite el llenado de la lista de lecturas agrupadas por código de producto en
 * {@link LecturaInventarioFragment}.
 * Permite eliminar lecturas individuales a través de {@link ChildConsultaInventarioViewHolder.OnRemove} o de un grupo de
 * lecturas a través de {@link HeaderConsultaInventarioViewHolder.OnRemove}
 * Se utiliza un tipo especial de librería que permite mostrar listas agrupadas por un criterio en el que la
 * cabecera de las listas es el criterio definido.
 */
public class HeaderConsultaInventarioAdapter extends ExpandableRecyclerViewAdapter<HeaderConsultaInventarioViewHolder, ChildConsultaInventarioViewHolder> {

    private ChildConsultaInventarioViewHolder.OnRemove hijoRemover = null;
    private HeaderConsultaInventarioViewHolder.OnRemove padreRemover = null;

    public HeaderConsultaInventarioAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    /**
     * Setea la interfaz {@link ChildConsultaInventarioViewHolder.OnRemove}
     * @param listener es el método implementado en {@link LecturaInventarioFragment}
     */
    public void onRemoveHijo(ChildConsultaInventarioViewHolder.OnRemove listener) {
        this.hijoRemover = listener;
    }

    /**
     * Setea la interfaz {@link HeaderConsultaInventarioViewHolder.OnRemove}
     * @param listener es el método implementado en {@link LecturaInventarioFragment}
     */
    public void onRemovePadre(HeaderConsultaInventarioViewHolder.OnRemove listener) {
        this.padreRemover = listener;
    }

    @Override
    public HeaderConsultaInventarioViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lectura_header,parent,false);
        return new HeaderConsultaInventarioViewHolder(view, padreRemover);
    }

    @Override
    public ChildConsultaInventarioViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lectura_swipe_consultar,parent,false);
        return new ChildConsultaInventarioViewHolder(view, hijoRemover);
    }

    @Override
    public void onBindChildViewHolder(ChildConsultaInventarioViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final HijoConsultaInventario lectura = (HijoConsultaInventario) group.getItems().get(childIndex);
        holder.onBind(lectura);
    }

    @Override
    public void onBindGroupViewHolder(HeaderConsultaInventarioViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setHeader(group);
    }
}
