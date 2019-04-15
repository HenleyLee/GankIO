package com.henley.gankio.mvp;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * {@link Observer}管理接口
 *
 * @author Henley
 * @date 2018/7/3 16:31
 */
public interface IRxjava {

    /**
     * 注册{@link Observer}到{@link CompositeDisposable}中统一管理
     */
    boolean add(@NonNull Disposable disposable);

    /**
     * 取消订阅指定的{@link Observer}
     */
    boolean remove(@NonNull Disposable disposable);

    /**
     * 取消订阅所有的{@link Observer}
     */
    void unsubscribe();

}
