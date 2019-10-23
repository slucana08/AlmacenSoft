package com.dms.almacensoft.features.configuracion.cajas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dms.almacensoft.R;

public class CajasFragment extends Fragment {

    public CajasFragment (){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cajas, container, false);

        return view;
    }
}
