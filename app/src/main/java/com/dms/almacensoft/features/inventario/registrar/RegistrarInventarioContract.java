package com.dms.almacensoft.features.inventario.registrar;

import com.dms.almacensoft.data.entities.dbalmacen.Inventario;
import com.dms.almacensoft.data.entities.dbalmacen.Producto;
import com.dms.almacensoft.data.entities.dbalmacen.Ubicacion;
import com.dms.almacensoft.data.models.inventario.BodyRegistrarInventario;
import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import java.util.List;

interface RegistrarInventarioContract {

    interface View extends BaseContractView {

        void setUpViews(boolean lote, boolean serie, boolean barrido, Ubicacion ubicacion);

        void checkUbicacion(List<Ubicacion> ubicacion);

        void checkProducto(List<Producto> productos);

        void setUpConteo(List<Inventario> inventario);

        void setUpLecturas(String lecturas);

        void showDiferencial(int count);

        void verifyDataToSubmit();

        void updateViews(int type);

        void checkSerieResult(int result);

        void onError(CustomDialog.IButton event, String message, int type);
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<RegistrarInventarioContract.View>{

        void getConfig();

        void verifyUbicacion(String codUbicacion);

        void getConteo();

        void getDiferencial(int idInventario, int conteo);

        void verifyProducto(String codProducto);

        void getLecturas(int idInventario);

        void registrarLectura(BodyRegistrarInventario body);

        void verifySerie(int idInventario, int idProducto, String serieProducto);

        void verifyDetalleInventario(BodyRegistrarInventario body);

        void registrarLecturaBD(BodyRegistrarInventario body);
    }


}
