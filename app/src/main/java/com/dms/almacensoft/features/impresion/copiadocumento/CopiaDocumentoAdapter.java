package com.dms.almacensoft.features.impresion.copiadocumento;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.models.impresion.DetalleImpresion;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link CopiaDocumentoAdapter} permite el llenado de la lista de detalle en {@link CopiaDocumentoFragment}
 * También realiza la selección de item a imprimir.
 */

public class CopiaDocumentoAdapter extends RecyclerView.Adapter<CopiaDocumentoAdapter.CopiaDocumentoAdapterViewHolder> {

    private List<DetalleImpresion> list;

    private Context context;

    private List<Boolean> selectedItems = new ArrayList<>();

    @Inject
    public CopiaDocumentoAdapter (Context context){
        this.context = context;
    }

    /**
     * Llena la lista de detalles para la selección
     * @param list
     */
    public void setList(List<DetalleImpresion> list) {
        this.list = list;
        // Resetea la lista de selecciones
        selectedItems.clear();
        for (int i = 0; i < list.size(); i++){
            selectedItems.add(false);
        }
        notifyDataSetChanged();
    }

    /**
     * Selecciona o deselecciona los elementos de la lista
     * @param select true - seleccionar
     *               falsa - deseleccionar
     */
    public void selectAll(boolean select){
        for (int i = 0; i < selectedItems.size(); i++){
            selectedItems.set(i,select);
        }
        notifyDataSetChanged();
    }

    /**
     * @return la lista de elementos seleccionados para impresión
     */
    public List<DetalleImpresion> getListSelected(){
        List<DetalleImpresion> selected = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++){
            if (selectedItems.get(i)){
                selected.add(list.get(i));
            }
        }
        return selected;
    }

    @NonNull
    @Override
    public CopiaDocumentoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detalle_impresion,parent,false);
        return new CopiaDocumentoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CopiaDocumentoAdapterViewHolder holder, int position) {
        if (selectedItems.get(position)){
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }
        holder.codigoProductoTextView.setText(list.get(position).getCodProducto());
        holder.loteProductoTextView.setText(list.get(position).getLoteProducto());
        if (list.get(position).getIdLecturaDocumento() != 0) {
            holder.serieDataLinearLayout.setVisibility(View.VISIBLE);
            holder.serieProductoTextView.setText(list.get(position).getSerieProducto());
        } else{
            holder.serieDataLinearLayout.setVisibility(View.GONE);
        }
        holder.descripcionProductoTextView.setText(list.get(position).getDscProducto());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CopiaDocumentoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.codigo_producto_text_view)
        TextView codigoProductoTextView;
        @BindView(R.id.lote_producto_text_view)
        TextView loteProductoTextView;
        @BindView(R.id.descripcion_producto_text_view)
        TextView descripcionProductoTextView;
        @BindView(R.id.serie_data_linear_layout)
        LinearLayout serieDataLinearLayout;
        @BindView(R.id.serie_producto_text_view)
        TextView serieProductoTextView;

        public CopiaDocumentoAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (selectedItems.get(getAdapterPosition())){
                selectedItems.set(getAdapterPosition(),false);
            } else {
                selectedItems.set(getAdapterPosition(),true);
            }
            notifyDataSetChanged();
        }
    }
}
