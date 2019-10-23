package com.dms.almacensoft.features.configuracion;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dms.almacensoft.features.configuracion.general.GeneralConfigFragment;
import com.dms.almacensoft.features.configuracion.impresion.ImpresionConfigFragment;

public class ConfiguracionAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public ConfiguracionAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new GeneralConfigFragment();
            default: return new ImpresionConfigFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //Array of Strings to be used as tab titles
        String[] tabNames = {"GENERAL","IMPRESION"};
        // Generate title based on item position
        return tabNames[position];
    }
}
