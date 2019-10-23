package com.dms.almacensoft.features.configuracion.almacen;

import android.content.Context;

import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.data.source.DataSourceRepository;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link AlmacenPresenter} realiza la llamadas a la BD y las preferencias del equipo para {@link AlmacenActivity}
 */

public class AlmacenPresenter implements AlmacenContract.Presenter {

    private AlmacenContract.View view;

    private Context context;
    private DataSourceRepository dataSourceRepository;
    private PreferenceManager preferenceManager;

    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public AlmacenPresenter (Context context, DataSourceRepository dataSourceRepository, PreferenceManager preferenceManager){
        this.context = context;
        this.dataSourceRepository = dataSourceRepository;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void attachView(AlmacenContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        disposable.clear();
        this.view = null;
    }

    @Override
    public AlmacenContract.View getView() {
        return view;
    }

    @Override
    public void getData() {
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.getAllAlmacenByUsuarioBD(Integer.parseInt(preferenceManager.getConfig().getIdUsuario()))
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(almacens -> getView().setAlmacen(almacens), throwable -> { }));
        } else {
            disposable.add(dataSourceRepository.getListAlmacen()
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(almacens ->
                            // Listamos los almacenes disponibles
                            getView().setAlmacen(almacens), throwable -> {
                    }));
        }
    }

    /**
     * Guarda la data necesaria en las preferencias del equipo
     * @param config es data capturada al seleccionar el almacén
     */
    @Override
    public void saveAlmacenConfig(Configuracion config) {
        Configuracion configuracion = preferenceManager.getConfig();
        configuracion.setIdAlmacen(config.getIdAlmacen());
        configuracion.setDscAlmacen(config.getDscAlmacen());
        configuracion.setCodAlmacen(config.getCodAlmacen());
        configuracion.setCodEmpresa(config.getCodEmpresa());
        configuracion.setIdEmpresa(config.getIdEmpresa());
        preferenceManager.saveConfig(configuracion);
        getView().showMainScreen();
    }
}
