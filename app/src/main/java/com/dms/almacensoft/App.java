package com.dms.almacensoft;

import android.app.Application;
import android.os.StrictMode;

import com.dms.almacensoft.injection.AppComponent;
import com.dms.almacensoft.injection.DaggerAppComponent;
import com.dms.almacensoft.injection.modules.AppModule;

import timber.log.Timber;

public class App extends Application {

    public static App instance;

    public static App get() {
        return (App) instance;
    }

    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        buildDependencyInjection();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            setStrictMode();
        }
    }

    public static void buildDependencyInjection() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(instance))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }
}
