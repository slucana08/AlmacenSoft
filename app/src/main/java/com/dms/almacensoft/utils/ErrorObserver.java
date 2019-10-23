package com.dms.almacensoft.utils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public interface ErrorObserver<T> extends Observer<T> {

    @Override
    default void onSubscribe(Disposable d) {
//        Timber.d("onSubscribe");
    }

//    @Override
//    default void onError(Throwable e) {
//    }

    @Override
    default void onComplete() {
//        Timber.d("onComplete");
    }

    @Override
    default void onNext(T t) {
//        Timber.d("onNext");

    }
}