package com.dms.almacensoft.features.principal;

import android.content.Context;
import android.text.TextUtils;

import com.dms.almacensoft.R;
import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.features.impresion.ImpresionFragment;
import com.dms.almacensoft.features.inventario.registrar.RegistrarInventarioFragment;
import com.dms.almacensoft.features.recepciondespacho.documentos.DocumentosFragment;
import com.dms.almacensoft.utils.UtilMethods;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link PrincipalPresenter} realiza las llamadas al WS para {@link PrincipalActivity}
 */

public class PrincipalPresenter implements PrincipalContract.Presenter {

    private PrincipalContract.View view;
    private Context context;
    private DataSourceRepository dataSourceRepository;
    private PreferenceManager preferenceManager;
    private CompositeDisposable disposable = new CompositeDisposable();

    // Contador de intentos de reconexión con WS al traer el inventario
    private int getInventarioCount = 0;

    @Inject
    public PrincipalPresenter(Context context, DataSourceRepository dataSourceRepository,
                              PreferenceManager preferenceManager){
        this.context = context;
        this.dataSourceRepository = dataSourceRepository;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void attachView(PrincipalContract.View mvpView){
        this.view = mvpView;
        preferenceManager.setConSinDoc(0);
    }

    @Override
    public void detachView() {
        disposable.clear();
        this.view = null;
    }

    @Override
    public PrincipalContract.View getView() {
        return view;
    }

    @Override
    public void selectFragment(int selection, String title, String type) {
        if (selection == 0){
            getView().replaceFragment(DocumentosFragment.newInstance(type),title);
        } else if (selection == 1){
            // Se llama RegistrarInventarioFragment idProducto = -1 para indicar que la llamada
            // viene del menú principal
            getView().replaceFragment(RegistrarInventarioFragment.newInstance(-1,
                    "","",0,"",""),title);
        } else if (selection == 2){
            // Se llama ImpresionFragment con type = 1 para indicar que la llamada viene desde el
            // menú principal
            getView().replaceFragment(ImpresionFragment.newInstance(1,-1,""),title);
        }
    }

    @Override
    public boolean getGuiaCerrada() {
        return preferenceManager.getGuiaCerrada();
    }

    @Override
    public int getConSinDoc() {
        return preferenceManager.getConSinDoc();
    }

    @Override
    public void getInventario() {
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.getAllInventarioByAlmacen(preferenceManager.getConfig().getIdAlmacen())
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(inventarios -> getView().showInventario(inventarios), throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> getInventario(), null, 0);
            } else {
                disposable.add(dataSourceRepository.getInventarioAbierto(preferenceManager.getConfig().getIdAlmacen())
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(inventarios -> {
                            getView().showInventario(inventarios);
                            getInventarioCount = 0;
                        }, throwable -> {
                            if (getInventarioCount < 2){
                                getInventarioCount +=1;
                                getView().onError(this::getInventario, context.getString(R.string.error_WS), 1);
                            } else {
                                getView().onError(null,context.getString(R.string.limite_intentos),2);
                            }
                        }));
            }
        }
    }

    @Override
    public void getEtiquetaBluetooth() {
        if (!UtilMethods.checkConnection(context)) {
            getView().onError(this::getEtiquetaBluetooth, null, 0);
        } else {
            disposable.add(dataSourceRepository.getEtiquetaBluetooth()
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(etiquetaBluetooth -> {
                        if (etiquetaBluetooth.isStatus() && !TextUtils.isEmpty(etiquetaBluetooth.getValor())) {
                            // Guarda el formato de etiqueta obtenido del WS en las preferencias del equipo
                            preferenceManager.setEtiquetaBluetooth(etiquetaBluetooth.getValor());
                        } else if (!etiquetaBluetooth.isStatus() || TextUtils.isEmpty(etiquetaBluetooth.getValor())) {
                            getView().onError(this::getEtiquetaBluetooth, context.getString(R.string.no_etiqueta_revisar_pc), 3);
                        }
                    }, throwable -> getView().onError(this::getEtiquetaBluetooth,
                            context.getString(R.string.error_carga_etiqueta), 3)));
        }
    }

    @Override
    public boolean getImpresionActiva() {
        return preferenceManager.getConfig().getActivarImpresora();
    }

    @Override
    public boolean isModoBatch() {
        return preferenceManager.getConfig().getTipoConexion();
    }

    @Override
    public String getTiempoUltimaLimpiezaBD() {
        String tiempo;
        long tiempoUltimaEliminacion = new Date().getTime() - preferenceManager.getUltimaLimpiezaBD();
        int minutos = (int) ((tiempoUltimaEliminacion / 1000) / 60);
        if (minutos < 60) {
            tiempo = "0 días 0 horas";
        } else {
            int horas = minutos / 60;
            if (horas < 24) {
                tiempo = "0 días " + horas + " horas";
            } else {
                int dias = horas / 24;
                int res = horas % 24;
                tiempo = "" + dias + " días " + res + " horas";
            }
        }
        return tiempo;
    }
}
