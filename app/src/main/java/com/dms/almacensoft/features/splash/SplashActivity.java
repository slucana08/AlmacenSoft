package com.dms.almacensoft.features.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.dms.almacensoft.R;
import com.dms.almacensoft.features.login.LoginActivity;
import com.dms.almacensoft.features.shared.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * {@link SplashActivity} pantalla de presentación de la aplicación que dirige a la pantalla de login
 * luego de que el tiempo especificado transcurra.
 */

public class SplashActivity extends BaseActivity implements SplashContract.View {

    @Inject
    SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);
        presenter.startTimer();
    }

    @Override
    public void showLogin(long time) {
        new Handler().postDelayed(() -> {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        },time);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy(){
        presenter.detachView();
        super.onDestroy();
    }
}
