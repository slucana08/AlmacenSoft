package com.dms.almacensoft.features.recepciondespacho.documentos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.entities.dbtransact.Documento;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link DocumentosAdapter} permite el llenado de la lista de documentos pendientes en {@link DocumentosFragment}
 * Además permite realizar la selección del documento con el que se trabajará
 */

public class DocumentosAdapter extends RecyclerView.Adapter<DocumentosAdapter.DocumentosAdapterViewHolder> {

    private Context context;
    private List<Documento> list;
    private SelectNroListener selectNroListener;

    @Inject
    public DocumentosAdapter(Context context){
        this.context = context;
    }

    public void setList(List<Documento> list){
        this.list = list;
    }

    @NonNull
    @Override
    public DocumentosAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recepcion,viewGroup,false);
        return new DocumentosAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentosAdapterViewHolder holder, int position) {
        holder.nroDocumentoTextView.setText(list.get(position).getNumDocumento());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DocumentosAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.nro_documento_text_view)
        TextView nroDocumentoTextView;

        public DocumentosAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            nroDocumentoTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == nroDocumentoTextView)
                selectNroListener.selectNro(list.get(getAdapterPosition()).getCodTipoDocumento(),
                        list.get(getAdapterPosition()).getCodClaseDocumento(),
                        list.get(getAdapterPosition()).getNumDocumento(),
                        list.get(getAdapterPosition()).getIdClaseDocumento(),
                        list.get(getAdapterPosition()).getIdDocumento());
        }
    }

    /**
     * Interfaz que permite realizar acciones en {@link DocumentosFragment}
     * Envia la data necesaria para poder traer los detalles del documento seleccionado
     */
    public interface SelectNroListener{
        void selectNro(String codTipoDocumento,String codClaseDocumento,String numDocumento, int idClaseDocumento, int idDocumento);
    }

    public void setSelectNroListener (SelectNroListener selectNroListener){
        this.selectNroListener = selectNroListener;
    }
}
