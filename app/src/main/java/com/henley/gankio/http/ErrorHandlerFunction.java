package com.henley.gankio.http;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 异常处理
 *
 * @author Henley
 * @date 2018/7/9 11:36
 */
public class ErrorHandlerFunction<T> implements Function<Throwable, Observable<T>> {

    @Override
    public Observable<T> apply(@NonNull Throwable throwable) throws Exception {
        return Observable.error(ExceptionEngine.handleException(throwable));
    }

}
