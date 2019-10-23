package com.dms.almacensoft.features.inventario.lecturas;

import com.dms.almacensoft.data.models.inventario.HijoConsultaInventario;
import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.utils.dialogs.CustomDialog;

import java.util.List;

interface LecturaInventarioContract {

    interface View extends BaseContractView{

        void setUpViews(int perfilTipo);

        void showLecturas(List<HeaderConsultaInventario> lecturas);

        void onError(CustomDialog.IButton event, String message, int type);

        void showMainScreen();
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<LecturaInventarioContract.View>{

        void getInventario();

        void getLecturas(int tipoConsulta, String valor);

        void deleteLectura(int idLecturaInventario);

        void deleteDetalle(int idDetaleInventario);

        void deleteTotalLecturas();

        void verifyUbicacionBD(String codUbicacion);

        void verifyProductoBD(String codProducto);

        void organizeLecturas(List<HijoConsultaInventario> hijoConsultaInventarios);
    }
}
