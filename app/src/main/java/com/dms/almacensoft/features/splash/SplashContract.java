package com.dms.almacensoft.features.splash;

import com.dms.almacensoft.features.shared.BaseContractView;

interface SplashContract {

    interface View extends BaseContractView {

        void showLogin(long time);
    }

    interface Presenter extends com.dms.almacensoft.features.shared.Presenter<SplashContract.View>{

        void startTimer();
    }
}
