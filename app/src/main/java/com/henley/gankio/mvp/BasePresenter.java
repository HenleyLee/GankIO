package com.henley.gankio.mvp;

import android.content.Context;

import com.henley.gankio.http.HttpObserver;
import com.henley.gankio.rxjava.RxJavaHelper;
import com.henley.gankio.rxjava.transformer.ObservableTransformerAsync;
import com.henley.gankio.rxjava.transformer.ObservableTransformerError;
import com.henley.gankio.rxjava.transformer.ObservableTransformerSync;
import com.henley.gankio.utils.ReflexHelper;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 负责完成View与Model间的交互(作为View与Model交互的中间纽带，处理与用户的交互)
 *
 * @author Henley
 * @date 2018/7/3 16:40
 */
public class BasePresenter<Model, View extends IMVPView> implements IPresenter<Model, View> {

    private View mMVPView;
    private Model mMVPModel;
    private Context mContext;
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void attachView(View mvpView) {
        this.mMVPView = mvpView;
    }

    @Override
    public void detachView() {
        this.mMVPView = null;
        this.mMVPModel = null;
        unsubscribe();
    }

    @Override
    public boolean add(@NonNull Disposable disposable) {
        return getCompositeDisposable().add(disposable);
    }

    @Override
    public boolean remove(@NonNull Disposable disposable) {
        return getCompositeDisposable().remove(disposable);
    }

    @Override
    public void unsubscribe() {
        getCompositeDisposable().dispose();
        mCompositeDisposable = null;
    }


    protected Context getContext() {
        if (mContext == null) {
            mContext = getView().getContext();
        }
        return mContext;
    }

    protected Model getModel() {
        if (mMVPModel == null) {
            mMVPModel = ReflexHelper.getTypeInstance(this, 0);
        }
        return mMVPModel;
    }

    protected View getView() {
        if (!isViewAttached()) {
            throw new IllegalArgumentException("Please call Presenter.attachView(mvpView) before requesting data to the Presenter.");
        }
        return mMVPView;
    }

    protected CompositeDisposable getCompositeDisposable() {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        return mCompositeDisposable;
    }

    /**
     * 订阅(异步)
     *
     * @param <T>        观察项目类型
     * @param observable 被观察者
     * @param observer   观察者
     */
    protected <T> Disposable subscribe(Observable<T> observable, final HttpObserver<T> observer) {
        observable = observable
                .compose(new ObservableTransformerAsync<T>())
                .compose(new ObservableTransformerError<T>());
        Disposable disposable = RxJavaHelper.subscribe(observable, observer);// 建立订阅关系
        add(disposable);
        return disposable;
    }

    /**
     * 订阅(同步)
     *
     * @param <T>        观察项目类型
     * @param observable 被观察者
     * @param observer   观察者
     */
    protected <T> Disposable subscribeSync(Observable<T> observable, final HttpObserver<T> observer) {
        observable = observable
                .compose(new ObservableTransformerSync<T>());
        Disposable disposable = RxJavaHelper.subscribe(observable, observer);// 建立订阅关系
        add(disposable);
        return disposable;
    }

    private boolean isViewAttached() {
        return mMVPView != null;
    }

}
