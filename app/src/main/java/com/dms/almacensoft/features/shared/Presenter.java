package com.dms.almacensoft.features.shared;

/**
 * Todos los Presenter extienden de {@link Presenter}
 * @param <V> es la View que extiende {@link BaseContractView}
 */

public interface Presenter <V extends BaseContractView> {

    void attachView(V mvpView);

    void detachView();

    V getView();

}
