package com.dms.almacensoft.features.shared;

import android.support.v4.app.Fragment;

/**
 * Todos los Fragment extienden de {@link BaseFragment} lo que permite que cada Fragment implemente
 * su propia opción de retroceso
 */

public abstract class BaseFragment extends Fragment {

    public abstract void onBackPressed();

}
