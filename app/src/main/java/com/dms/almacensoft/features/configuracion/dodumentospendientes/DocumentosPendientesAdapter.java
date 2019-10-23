package com.dms.almacensoft.features.configuracion.dodumentospendientes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dms.almacensoft.features.configuracion.dodumentospendientes.recepciondespacho.RecepcionDespachoPendientesFragment;
import com.dms.almacensoft.utils.Constants;

public class DocumentosPendientesAdapter extends FragmentStatePagerAdapter {

    public DocumentosPendientesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return RecepcionDespachoPendientesFragment.newInstance(Constants.RECEPCION);
            default: return RecepcionDespachoPendientesFragment.newInstance(Constants.DESPACHO);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //Array of Strings to be used as tab titles
        String[] tabNames = {"RECEPCION","DESPACHO"};
        // Generate title based on item position
        return tabNames[position];
    }
}
