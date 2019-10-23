package com.dms.almacensoft.injection;

import com.dms.almacensoft.features.configuracion.ConfiguracionActivity;
import com.dms.almacensoft.features.configuracion.almacen.AlmacenActivity;
import com.dms.almacensoft.features.configuracion.documentos.DocumentosConfigFragment;
import com.dms.almacensoft.features.configuracion.dodumentospendientes.DocumentosPendientesActivity;
import com.dms.almacensoft.features.configuracion.dodumentospendientes.recepciondespacho.RecepcionDespachoPendientesFragment;
import com.dms.almacensoft.features.configuracion.dodumentospendientes.recepciondespacho.RecepcionDespachoPendientesPresenter;
import com.dms.almacensoft.features.configuracion.general.GeneralConfigFragment;
import com.dms.almacensoft.features.configuracion.impresion.ImpresionConfigFragment;
import com.dms.almacensoft.features.configuracion.impresionbluetooth.ImpresionBluetoothActivity;
import com.dms.almacensoft.features.configuracion.ip.IpActivity;
import com.dms.almacensoft.features.principal.PrincipalActivity;
import com.dms.almacensoft.features.principal.GeneralActivityOld;
import com.dms.almacensoft.features.impresion.ImpresionFragment;
import com.dms.almacensoft.features.impresion.copiadocumento.CopiaDocumentoFragment;
import com.dms.almacensoft.features.impresion.etiquetalibre.EtiquetaLibreFragment;
import com.dms.almacensoft.features.inventario.diferencial.DiferencialFragment;
import com.dms.almacensoft.features.inventario.lecturas.LecturaInventarioFragment;
import com.dms.almacensoft.features.inventario.registrar.RegistrarInventarioFragment;
import com.dms.almacensoft.features.login.LoginActivity;
import com.dms.almacensoft.features.recepciondespacho.detalledocumento.DetalleDocumentoFragment;
import com.dms.almacensoft.features.recepciondespacho.documentos.DocumentosFragment;
import com.dms.almacensoft.features.recepciondespacho.lectura.LecturaFragment;
import com.dms.almacensoft.features.recepciondespacho.sindocumento.SinDocumentoConfigFragment;
import com.dms.almacensoft.features.splash.SplashActivity;
import com.dms.almacensoft.features.sync.SyncDialog;
import com.dms.almacensoft.injection.annotations.ActivityScope;

import dagger.Component;

@ActivityScope
@Component (dependencies = AppComponent.class)
public interface ActivityComponent {

    // Activities

    void inject(LoginActivity loginActivity);

    void inject(IpActivity ipActivity);

    void inject(GeneralActivityOld generalActivityOld);

    void inject(PrincipalActivity principalActivity);

    void inject(ConfiguracionActivity configuracionActivity);

    void inject(SplashActivity activity);

    void inject(ImpresionBluetoothActivity activity);

    void inject(DocumentosPendientesActivity activity);

    // SyncDialog

    void inject(SyncDialog syncDialog);

    // Fragments

    void inject(GeneralConfigFragment fragment);

    void inject(ImpresionConfigFragment fragment);

    void inject(DocumentosConfigFragment fragment);

    void inject(DocumentosFragment fragment);

    void inject(DetalleDocumentoFragment fragment);

    void inject(SinDocumentoConfigFragment fragment);

    void inject(LecturaFragment fragment);

    void inject(RegistrarInventarioFragment fragment);

    void inject(LecturaInventarioFragment fragment);

    void inject(AlmacenActivity activity);

    void inject(DiferencialFragment fragment);

    void inject(ImpresionFragment fragment);

    void inject(CopiaDocumentoFragment fragment);

    void inject(EtiquetaLibreFragment fragment);

    void inject(RecepcionDespachoPendientesFragment fragment);
}
