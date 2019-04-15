package com.henley.gankio.rxjava.transformer;

import com.henley.gankio.http.ErrorHandlerFunction;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;

/**
 * 异常处理器
 *
 * @author Henley
 * @date 2018/7/9 11:35
 */
public class ObservableTransformerError<T> implements ObservableTransformer<T, T> {

    @Override
    public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
        return upstream.onErrorResumeNext(new ErrorHandlerFunction<T>());
    }

}
