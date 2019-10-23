package com.dms.almacensoft.features.recepciondespacho.detalledocumento;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.models.recepciondespacho.DetalleDocumentoLive;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link DetalleDocumentoAdapter} realiza el llenado de la lista de detalles del documento trabajado
 * Además permite seleccionar un producto para el registro de una lectura
 */

public class DetalleDocumentoAdapter extends RecyclerView.Adapter<DetalleDocumentoAdapter.DetalleDocumentoAdapterViewHolder>  {

    private Context context;
    private List<DetalleDocumentoLive> list;
    private int selectedItem = -1;

    private CodigoProductoSetter codigoProductoSetter;

    @Inject
    public DetalleDocumentoAdapter(Context context){
        this.context = context;
    }

    @Override
    public DetalleDocumentoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detalle_documento,parent,false);
        return new DetalleDocumentoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetalleDocumentoAdapterViewHolder holder, int position) {
        if (position == selectedItem){
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }
        holder.codigoProductoTextView.setText(list.get(position).getCodProducto());
        holder.detalleProductoTextView.setText(list.get(position).getDscProducto());
        holder.cantPendTextView.setText(String.valueOf(list.get(position).getCtdPendiente() < 0 ? 0 : list.get(position).getCtdPendiente()));
        holder.cantReqTextView.setText(String.valueOf(list.get(position).getCtdRequerida()));
        holder.cantAtenTextView.setText(String.valueOf(list.get(position).getCtdAtendida()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DetalleDocumentoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.codigo_producto_text_view)
        TextView codigoProductoTextView;
        @BindView(R.id.detalle_producto_text_view)
        TextView detalleProductoTextView;
        @BindView(R.id.cant_pend_text_view)
        TextView cantPendTextView;
        @BindView(R.id.cant_req_text_view)
        TextView cantReqTextView;
        @BindView(R.id.cant_aten_text_view)
        TextView cantAtenTextView;
        @BindView(R.id.container_linear_layout)
        LinearLayout containerLinearLayour;

        public DetalleDocumentoAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            selectedItem = getAdapterPosition();
            codigoProductoSetter.setCodigo(list.get(getAdapterPosition()).getCodProducto(),selectedItem);
            notifyDataSetChanged();
        }
    }

    public void setList(List<DetalleDocumentoLive> list){
        this.list = list;
    }

    /**
     * Interfaz que permite realizar la selección de un producto y enviar está información a {@link DetalleDocumentoFragment}
     */
    public interface CodigoProductoSetter {
        void setCodigo(String codProducto, int selectedItem);
    }

    public void setCodigoProductoSetter (CodigoProductoSetter codigoProductoSetter){
        this.codigoProductoSetter = codigoProductoSetter;
    }

    /**
     * Define el elemento seleccionado de la lista
     * @param selectedItem es el índice del elemento seleccionado
     */
    public void setSelectedItem(int selectedItem){
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }
}
