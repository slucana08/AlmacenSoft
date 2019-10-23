package com.dms.almacensoft.features.impresion;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.dms.almacensoft.R;
import com.dms.almacensoft.features.impresion.copiadocumento.CopiaDocumentoFragment;
import com.dms.almacensoft.features.impresion.etiquetalibre.EtiquetaLibreFragment;

/**
 * {@link ImpresionAdapter} muestra las pantallas del módulo impresión
 */

public class ImpresionAdapter extends FragmentStatePagerAdapter {

    private Context context;

    private int type;
    private int idClaseDocumento;
    private String numDocumento;

    private SlideChange slideChange = null;

    public ImpresionAdapter(FragmentManager fm, Context context, int type, int idClaseDocumento,
                            String numDocumento) {
        super(fm);
        this.context = context;
        this.type = type;
        this.idClaseDocumento = idClaseDocumento;
        this.numDocumento = numDocumento;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return CopiaDocumentoFragment.newInstance(type,idClaseDocumento,numDocumento);
            default: return new EtiquetaLibreFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //Array of Strings to be used as tab titles
        String[] tabNames = {context.getString(R.string.copia_documento),
                context.getString(R.string.etiqueta_libre)};
        // Generate title based on item position
        return tabNames[position];
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        slideChange.onSlide(position);
    }

    /**
     * Interfaz que permite informar a {@link ImpresionFragment} cuando el adapter transiciona entre
     * {@link CopiaDocumentoFragment} y {@link EtiquetaLibreFragment}
     */
    public interface SlideChange {
        void onSlide(int childFragment);
    }

    public void setSlideChange(SlideChange slideChange) {
        this.slideChange = slideChange;
    }


}

