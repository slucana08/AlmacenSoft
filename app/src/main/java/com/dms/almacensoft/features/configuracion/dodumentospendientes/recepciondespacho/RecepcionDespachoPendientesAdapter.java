package com.dms.almacensoft.features.configuracion.dodumentospendientes.recepciondespacho;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.entities.dbtransact.Documento;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecepcionDespachoPendientesAdapter} permite realizar el llenado de la lista de documentos pendientes
 * en {@link RecepcionDespachoPendientesFragment}.
 * También permite colectar la data necesaria y enviarla a {@link RecepcionDespachoPendientesFragment} para
 * poder descargar toda la información sobre los documentos seleccionados
 */

public class RecepcionDespachoPendientesAdapter extends
        RecyclerView.Adapter<RecepcionDespachoPendientesAdapter.RecepcionDespachoPendientesAdapterViewHolder>  {

    private Context context;

    private List<Documento> list;
    private List<Boolean> selectedItems = new ArrayList<>();

    @Inject
    public RecepcionDespachoPendientesAdapter(Context context){
        this.context = context;
    }

    public void setList(List<Documento> list) {
        this.list = list;
        selectedItems.clear();
        for (int i = 0; i < list.size(); i++){
            selectedItems.add(false);
        }
        notifyDataSetChanged();
    }

    public List<Documento> getSelectedList(){
        List<Documento> selectedList = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++){
            if (selectedItems.get(i)){
                selectedList.add(list.get(i));
            }
        }
        return selectedList;
    }

    @NonNull
    @Override
    public RecepcionDespachoPendientesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_documento_pendiente,parent,false);
        return new RecepcionDespachoPendientesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecepcionDespachoPendientesAdapterViewHolder holder, int position) {
        holder.itemView.setSelected(selectedItems.get(position));
        holder.nroDocumentoTextView.setText(list.get(position).getNumDocumento());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecepcionDespachoPendientesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.nro_documento_text_view)
        TextView nroDocumentoTextView;

        public RecepcionDespachoPendientesAdapterViewHolder(@NonNull View itemView) {
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
