package com.dms.almacensoft.features.impresion;

import com.dms.almacensoft.features.shared.BaseContractView;
import com.dms.almacensoft.features.shared.Presenter;

interface ImpresionContract {

    interface View extends BaseContractView {

        void setUpNavigationUp();

        void setUpViews();
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<ImpresionContract.View> {

        void checkType(int type);

        boolean getTipoImpresora();

        void setTipoImpresoraRed();
    }
}
