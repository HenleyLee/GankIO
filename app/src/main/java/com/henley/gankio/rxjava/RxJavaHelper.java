package com.henley.gankio.rxjava;

import com.henley.gankio.mvp.BaseObserver;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author Henley
 * @date 2018/7/3 17:10
 */
public class RxJavaHelper {

    /**
     * 基于{@link Observable}的事件订阅
     *
     * @param observable
     * @param observer
     * @param <T>
     * @return
     */
    public static <T> Disposable subscribe(Observable<T> observable, final BaseObserver<T> observer) {
        return observable.subscribe(new Consumer<T>() {
            @Override
            public void accept(@NonNull T t) throws Exception {
                if (observer != null) {
                    observer.onNext(t);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                if (observer != null) {
                    observer.onError(throwable);
                }
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                if (observer != null) {
                    observer.onComplete();
                }
            }
        }, new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                if (observer != null) {
                    observer.onSubscribe(disposable);
                }
            }
        });
    }

}
