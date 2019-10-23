package com.dms.almacensoft.features.inventario.diferencial;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.entities.dbtransact.ProductoDiferencial;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link ChildDiferencialViewHolder es el encargado de llenar los items individuales de cada uno de los grupos
 * mostrados en {@link HeaderDiferencialAdapter }
 */
public class ChildDiferencialViewHolder extends ChildViewHolder {

    @BindView(R.id.descripcion_text_view)
    TextView descripcionTextView;
    @BindView(R.id.codigo_text_view)
    TextView codigoTextView;
    @BindView(R.id.cantidad_text_view)
    TextView cantidadTextView;
    @BindView(R.id.lote_text_view)
    TextView loteLabelTextView;
    @BindView(R.id.lote2_text_view)
    TextView loteTextView;
    @BindView(R.id.ubicacion2_text_view)
    TextView ubicacionTextView;

    private EnviarData enviarData;

    private ProductoDiferencial productoDiferencial;

    public ChildDiferencialViewHolder(View itemView, EnviarData enviarData) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.enviarData = enviarData;
        itemView.setOnClickListener(v -> enviarData.enviar(productoDiferencial.getIdProducto(),
                productoDiferencial.getCodProducto(),productoDiferencial.getIdUbicacion(),
                productoDiferencial.getCodUbicacion(),productoDiferencial.getLoteProducto(),
                productoDiferencial.getDscProducto()));
    }

    public void onBind(ProductoDiferencial hijoConsulta) {
        this.productoDiferencial = hijoConsulta;
        codigoTextView.setText(hijoConsulta.getCodProducto());
        descripcionTextView.setText(hijoConsulta.getDscProducto());

        cantidadTextView.setText(String.valueOf(hijoConsulta.getStockAnterior()));

        ubicacionTextView.setText(hijoConsulta.getCodUbicacion());

        String lote = hijoConsulta.getLoteProducto();
        if (TextUtils.isEmpty(lote)){
            loteLabelTextView.setVisibility(View.GONE);
            loteTextView.setVisibility(View.GONE);
        } else {
            loteLabelTextView.setVisibility(View.VISIBLE);
            loteTextView.setVisibility(View.VISIBLE);
        }
        loteTextView.setText(lote);
    }

    /**
     * Interfaz que permite obtener toda la información necesaria para realizar el registro de una lectura.
     * La información es enviada a {@link DiferencialFragment} para determinar como realizar el registro
     */
    public interface EnviarData {
        void enviar(int idProducto, String codProducto, int idUbicacion, String codUbicacion, String loteProducto, String dscProducto);
    }



}
