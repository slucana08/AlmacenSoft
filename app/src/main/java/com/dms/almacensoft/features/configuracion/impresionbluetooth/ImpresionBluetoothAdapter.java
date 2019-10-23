package com.dms.almacensoft.features.configuracion.impresionbluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dms.almacensoft.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link ImpresionBluetoothAdapter} permite el llenado de la lista de dispositivos sincronizados en
 * {@link ImpresionBluetoothActivity}.
 * Además, permite realizar el muestreo de una selección previamente realizada y también el realizar
 * una nueva selección
 */

public class ImpresionBluetoothAdapter extends RecyclerView.Adapter<ImpresionBluetoothAdapter.ImpresionBluetoothAdapteViewHolder>  {

    private Context context;
    private List<BluetoothDevice> devices;
    private String printerAddress;
    private SelectedAddress selectedAddress = null;

    @Inject
    public ImpresionBluetoothAdapter (Context context){
        this.context = context;
    }

    public void setPrinterAddress(String printerAddress) {
        this.printerAddress = printerAddress;
    }

    public void setDevices(List<BluetoothDevice> devices) {
        this.devices = devices;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImpresionBluetoothAdapteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bluetooth,parent,false);
        return new ImpresionBluetoothAdapteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImpresionBluetoothAdapteViewHolder holder, int position) {
        final BluetoothDevice device = devices.get(position);
        if (TextUtils.equals(device.getAddress(),printerAddress)){
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }
        holder.deviceNameTextView.setText(device.getName());
        holder.deviceAddressTextView.setText(device.getAddress());
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    class ImpresionBluetoothAdapteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.device_name_text_view)
        TextView deviceNameTextView;
        @BindView(R.id.device_address_text_view)
        TextView deviceAddressTextView;


        public ImpresionBluetoothAdapteViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            printerAddress = devices.get(getAdapterPosition()).getAddress();
            notifyDataSetChanged();
            selectedAddress.saveAddress(printerAddress);
        }
    }

    /**
     * Interfaz que permite realizar acciones en {@link ImpresionBluetoothActivity}, permite guardar
     * la MAC de la impresora seleccionada en la preferencias del equipo
     */
    public interface SelectedAddress{
        void saveAddress(String printerAdress);
    }

    public void setSelectedAddress(SelectedAddress selectedAddress) {
        this.selectedAddress = selectedAddress;
    }
}
