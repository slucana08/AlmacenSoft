package com.dms.almacensoft.features.recepciondespacho.lectura;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.models.recepciondespacho.HijoConsulta;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link ChildConsultaViewHolder} es el encargado de llenar los items individuales de cada uno de los grupos
 * mostrados en {@link HeaderConsultaAdapter}
 */
public class ChildConsultaViewHolder extends ChildViewHolder {

    @BindView(R.id.codigo_text_view)
    TextView codigoTextView;
    @BindView(R.id.descripcion_text_view)
    TextView descripcionTextView;
    @BindView(R.id.delete_linear_layout)
    View deleteView;
    @BindView(R.id.cantidad_text_view)
    TextView cantidadTextView;
    @BindView(R.id.ubicacion_text_view)
    TextView ubicacion;
    @BindView(R.id.lote_text_view)
    TextView lote;
    @BindView(R.id.serie_text_view)
    TextView serie;
    @BindView(R.id.ubicacion2_text_view)
    TextView ubicacionTextView;
    @BindView(R.id.lote2_text_view)
    TextView loteTextView;
    @BindView(R.id.serie2_text_view)
    TextView serieTextView;
    private OnRemove listener = null;

    public ChildConsultaViewHolder(View itemView, OnRemove listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    public void onBind(HijoConsulta hijoConsulta) {
        codigoTextView.setText(hijoConsulta.getCodProducto());
        descripcionTextView.setText(hijoConsulta.getDscProducto());

        cantidadTextView.setText(String.valueOf(hijoConsulta.getCtdAsignada()));

        ubicacionTextView.setText(hijoConsulta.getCodUbicacion());

        String lote = hijoConsulta.getLoteProducto();
        if (TextUtils.isEmpty(lote)){
            this.lote.setVisibility(View.GONE);
            loteTextView.setVisibility(View.GONE);
        } else {
            this.lote.setVisibility(View.VISIBLE);
            loteTextView.setVisibility(View.VISIBLE);
        }
        loteTextView.setText(lote);

        String serie = hijoConsulta.getSerieProducto();
        if (TextUtils.isEmpty(serie)){
            this.serie.setVisibility(View.GONE);
            serieTextView.setVisibility(View.GONE);
        } else {
            this.serie.setVisibility(View.VISIBLE);
            serieTextView.setVisibility(View.VISIBLE);
        }
        serieTextView.setText(serie);

        deleteView.setOnClickListener(view -> {
            listener.onRemoveHijo(hijoConsulta);
        });
    }

    /**
     * Interfaz que permite realizar la eliminación de items individuales en la lista mostrada.
     * Envia la información necesaria a {@link LecturaFragment} para realizar la eliminación
     */
    public interface OnRemove {
        void onRemoveHijo(HijoConsulta hijoConsulta);
    }
}
