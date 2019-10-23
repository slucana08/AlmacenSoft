package com.dms.almacensoft.features.configuracion.almacen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.entities.dbalmacen.Almacen;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link AlmacenAdapter} permite mostrar los almacenes disponibles en {@link AlmacenActivity}
 * A su vez permite realizar la selección del almacén con el que se trabajara
 */

public class AlmacenAdapter extends RecyclerView.Adapter<AlmacenAdapter.AlmacenAdapterViewHolder>  {

    private Context context;
    private List<Almacen> list;
    private Selection selection;

    @Inject
    public AlmacenAdapter (Context context) {
        this.context = context;
    }

    /**
     * Actualiza la lista de acuerdo a los caracteres ingresados en filterEditText de {@link AlmacenActivity}
     * @param list es la lista de almacenes
     * @param filter es el filtro de búsqueda ingresado
     */
    public void setList(List<Almacen> list, String filter){
        filter = filter.toUpperCase();

        if (TextUtils.isEmpty(filter)){
            this.list = list;
            notifyDataSetChanged();
        } else {
            List<Almacen> temp = new ArrayList<>();
            for (Almacen almacen : list) {
                if (almacen.getCodAlmacen().toUpperCase().contains(filter) ||
                        almacen.getDscAlmacen().toUpperCase().contains(filter)) {
                    temp.add(almacen);
                }
            }
            this.list = temp;
            notifyDataSetChanged();
        }
    }

    public void setSelection(Selection selection){
        this.selection = selection;
    }

    @NonNull
    @Override
    public AlmacenAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_almacen_search,parent,false);
        return new AlmacenAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlmacenAdapterViewHolder holder, int position) {
        holder.codigoTextView.setText(list.get(position).getCodAlmacen());
        holder.descripcionTextView.setText(list.get(position).getDscAlmacen());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AlmacenAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.codigo_almacen_text_view)
        TextView codigoTextView;
        @BindView(R.id.descripcion_almacen_text_view)
        TextView descripcionTextView;

        public AlmacenAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            selection.onSelect(list.get(getAdapterPosition()).getIdAlmacen(),
                    list.get(getAdapterPosition()).getDscAlmacen(),
                    list.get(getAdapterPosition()).getCodAlmacen(),
                    list.get(getAdapterPosition()).getCodEmpresa(),
                    list.get(getAdapterPosition()).getIdEmpresa());
        }
    }

    /**
     * Interfaz que permite ejecutar acciones en {@link AlmacenActivity}
     */
    public interface Selection {
        void onSelect(int idAlmacen, String dscAlmacen, String codAlmacen,String codEmpresa, int idEmpresa);
    }
}
