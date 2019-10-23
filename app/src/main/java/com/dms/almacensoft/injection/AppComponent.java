package com.dms.almacensoft.injection;

import android.content.Context;

import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.data.source.local.DbAlmacen;
import com.dms.almacensoft.data.source.remote.WebService;
import com.dms.almacensoft.injection.annotations.ApplicationScope;
import com.dms.almacensoft.injection.modules.AppModule;
import com.dms.almacensoft.injection.modules.SQLiteModule;
import com.dms.almacensoft.injection.modules.WebServiceModule;

import dagger.Component;

@ApplicationScope
@Component(modules = {AppModule.class, SQLiteModule.class, WebServiceModule.class})
public interface AppComponent {

    Context context();

    DbAlmacen dbAlmacen();

    PreferenceManager preferenceManager();

    DataSourceRepository dataSourceRepository();

    WebService webService();

}
