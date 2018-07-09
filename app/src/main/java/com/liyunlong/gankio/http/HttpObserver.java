package com.liyunlong.gankio.http;

import android.content.Context;

import com.liyunlong.gankio.mvp.BaseObserver;

import io.reactivex.annotations.NonNull;

/**
 * 基于Observable的网络请求观察者
 *
 * @author liyunlong
 * @date 2018/7/9 11:46
 */
public abstract class HttpObserver<T> extends BaseObserver<T> {

    private Context mContext;

    public HttpObserver(Context context) {
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    /**
     * 处理异常(不允许重写改方法)
     */
    @Override
    public final void onError(@NonNull Throwable throwable) {
        onStop();
        HttpException exception;
        if (throwable instanceof HttpException) {
            exception = (HttpException) throwable;
        } else {
            exception = ExceptionEngine.handleException(throwable);
        }
        onError(exception);
    }

    public abstract void onError(@NonNull HttpException exception);

}
