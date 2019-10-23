package com.dms.almacensoft.features.impresion.etiquetalibre;

import android.content.Context;
import android.text.TextUtils;

import com.dms.almacensoft.data.PreferenceManager;
import com.dms.almacensoft.data.entities.dbalmacen.Producto;
import com.dms.almacensoft.data.models.impresion.BodyImprimirEtiqueta;
import com.dms.almacensoft.data.source.DataSourceRepository;
import com.dms.almacensoft.utils.PrinterHelper;
import com.dms.almacensoft.utils.UtilMethods;
import com.dms.almacensoft.utils.dialogs.ProgressDialog;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * {@link EtiquetaLibrePresenter} realiza las llamadas al WS para {@link EtiquetaLibreFragment}
 */

public class EtiquetaLibrePresenter implements EtiquetaLibreContract.Presenter {

    private EtiquetaLibreContract.View view;

    private Context context;
    private DataSourceRepository dataSourceRepository;
    private PreferenceManager preferenceManager;
    private CompositeDisposable disposable = new CompositeDisposable();

    private int imprimirCount = 0;

    @Inject
    public EtiquetaLibrePresenter (Context context, DataSourceRepository dataSourceRepository,
                                   PreferenceManager preferenceManager) {
        this.context = context;
        this.dataSourceRepository = dataSourceRepository;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void attachView(EtiquetaLibreContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        disposable.clear();
        this.view = null;
    }

    @Override
    public EtiquetaLibreContract.View getView() {
        return view;
    }

    @Override
    public void getProducto(String codProducto) {
        if (preferenceManager.getConfig().getTipoConexion()){
            disposable.add(dataSourceRepository.verifyProductoDB(codProducto)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(productos -> getView().setUpProducto(productos), throwable -> { }));
        } else {
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> getProducto(codProducto), "Buscando...", 0);
            } else {
                disposable.add(dataSourceRepository.getProducto(codProducto)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(productos -> EtiquetaLibrePresenter.this.getView().setUpProducto(productos),
                                throwable -> UtilMethods.showToast(context, "Error de conexión")));
            }
        }
    }

    @Override
    public void imprimirEtiquetas(BodyImprimirEtiqueta etiqueta) {
        if (preferenceManager.getConfig().getTipoImpresora()){
            // Realiza la impresión si se seleccionó la impresión por bluetooth
            if (!TextUtils.isEmpty(preferenceManager.getPrinterAddress())){
                // Genera la etiqueta basándose en el formato obtenido desde el WS
                String label = preferenceManager.getEtiquetaBluetooth();
                label = label.replace("[CODBARRA]", etiqueta.getCodigoProducto());
                label = label.replace("[DESCRIPCION]", etiqueta.getDescripcionProducto());
                label = label.replace("[LOTE]", etiqueta.getLote());
                label = label.replace("[SERIE]", etiqueta.getSerie());

                PrinterHelper.printBluetooth(preferenceManager.getPrinterAddress(),label,etiqueta.getNumCopias());
                getView().checkPrintResult();

            } else {
                ProgressDialog.dismiss();
                UtilMethods.showToast(context,"Debe seleccionar la impresora desde el menú de configuración");
            }
        } else {
            // Realiza la impresión si se seleccionó la impresión por red
            if (!UtilMethods.checkConnection(context)) {
                getView().onError(() -> imprimirEtiquetas(etiqueta),
                        "Imprimiendo...", 0);
            } else {
                disposable.add(dataSourceRepository.imprimirEtiqueta(etiqueta)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            if (result) {
                                ProgressDialog.dismiss();
                                UtilMethods.showToast(context, "Impresión realizada");
                            }
                        }, throwable -> {
                            ProgressDialog.dismiss();
                            if (imprimirCount < 2) {
                                imprimirCount += 1;
                                getView().onError(() -> imprimirEtiquetas(etiqueta), "Imprimiendo...", 1);
                            } else {
                                imprimirCount = 0;
                                getView().onError(null, null, 2);
                            }
                        }));
            }
        }
    }
}
