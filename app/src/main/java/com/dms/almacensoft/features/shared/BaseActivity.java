package com.dms.almacensoft.features.shared;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.dms.almacensoft.App;
import com.dms.almacensoft.injection.ActivityComponent;
import com.dms.almacensoft.injection.DaggerActivityComponent;

/**
 * Todas las activities de la aplicación extienden de {@link BaseActivity} lo cual permite que
 * puedan tener acceso al activityComponent para realizar la inyección de dependencias
 */

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        activityComponent = DaggerActivityComponent.builder()
                .appComponent(App.get().getAppComponent())
                .build();
    }

    protected void buildComponent() {
        activityComponent = DaggerActivityComponent.builder()
                .appComponent(App.get().getAppComponent())
                .build();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
