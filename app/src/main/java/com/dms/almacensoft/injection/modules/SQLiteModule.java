package com.dms.almacensoft.injection.modules;

import android.content.Context;

import com.dms.almacensoft.data.source.local.DbAlmacen;
import com.dms.almacensoft.injection.annotations.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class SQLiteModule {

    @Provides
    @ApplicationScope
    public DbAlmacen provideDbAlmacen(Context context){
        return DbAlmacen.getDatabase(context);
    }
}
