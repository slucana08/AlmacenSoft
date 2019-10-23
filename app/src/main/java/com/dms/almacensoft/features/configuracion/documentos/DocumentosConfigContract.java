package com.dms.almacensoft.features.configuracion.documentos;

import com.dms.almacensoft.data.models.Configuracion;
import com.dms.almacensoft.features.shared.BaseContractView;

interface DocumentosConfigContract {

    interface View extends BaseContractView {

        void setUpConfig(Configuracion config);
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<DocumentosConfigContract.View>{

        void getConfig();
    }
}
