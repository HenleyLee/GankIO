package com.henley.gankio.presenter;

import com.henley.gankio.contract.GankHistoryContract;
import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.GankDaily;
import com.henley.gankio.http.ExceptionEngine;
import com.henley.gankio.http.HttpException;
import com.henley.gankio.http.HttpObserver;
import com.henley.gankio.model.GankHistoryModel;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 每日干货数据Presenter
 *
 * @author Henley
 * @date 2018/7/5 10:34
 */
public class GankHistoryPresenter extends GankHistoryContract.Presenter<GankHistoryModel> {

    @Override
    public void getGankHistory() {
        subscribe(getModel().getGankHistory(), new HttpObserver<BaseGank<List<String>>>(getContext()) {

            @Override
            public void onError(HttpException exception) {
                getView().handleException(exception);
            }

            @Override
            public void onNext(BaseGank<List<String>> gank) {
                getView().handleGankHistoryResult(gank);
            }
        });
    }

    @Override
    public void getHistoryGankDaily(List<String> historyDates) {
        add(getModel().getHistoryGankDaily(historyDates)
                .subscribe(new Consumer<List<BaseGank<GankDaily>>>() {
                    @Override
                    public void accept(List<BaseGank<GankDaily>> ganks) throws Exception {
                        getView().handleHistoryGankDailyResult(ganks);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().handleException(ExceptionEngine.handleException(throwable));
                    }
                }));
    }

}
