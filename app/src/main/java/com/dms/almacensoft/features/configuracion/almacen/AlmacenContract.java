package com.dms.almacensoft.features.configuracion.almacen;

import com.dms.almacensoft.data.entities.dbalmacen.Almacen;
import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.features.shared.BaseContractView;

import java.util.List;

interface AlmacenContract {

    interface View extends BaseContractView {

        void setAlmacen(List<Almacen> list);

        void showMainScreen();
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<AlmacenContract.View>{

        void getData();

        void saveAlmacenConfig(Configuracion config);
    }
}
