package com.dms.almacensoft.features.principal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dms.almacensoft.R;
import com.dms.almacensoft.features.shared.BaseFragment;

/**
 * {@link PrincipalFragment} es la pantalla inicial que se muestra en {@link PrincipalActivity}
 */

public class PrincipalFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_splash, container, false);

        return view;
    }

    @Override
    public void onBackPressed() {

    }
}
