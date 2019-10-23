package com.dms.almacensoft.features.configuracion.ip;

import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.features.shared.Presenter;

interface IpContract {

    interface view extends BaseContractView {

        void displayIp(String ip);
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<IpContract.view>{

        void onViewCreated();

        void updateIp(String ip);
    }
}
