package com.liyunlong.gankio.presenter;

import com.liyunlong.gankio.contract.GankHistoryContract;
import com.liyunlong.gankio.entity.GankDaily;
import com.liyunlong.gankio.entity.GankHistory;
import com.liyunlong.gankio.http.ExceptionEngine;
import com.liyunlong.gankio.http.HttpException;
import com.liyunlong.gankio.http.HttpObserver;
import com.liyunlong.gankio.model.GankHistoryModel;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 每日干货数据Presenter
 *
 * @author liyunlong
 * @date 2018/7/5 10:34
 */
public class GankHistoryPresenter extends GankHistoryContract.Presenter<GankHistoryModel> {

    @Override
    public void getGankHistory() {
        subscribe(getModel().getGankHistory(), new HttpObserver<GankHistory>(getContext()) {

            @Override
            public void onError(HttpException exception) {
                getView().handleException(exception);
            }

            @Override
            public void onNext(GankHistory gankHistory) {
                getView().handleGankHistoryResult(gankHistory);
            }
        });
    }

    @Override
    public void getHistoryGankDaily(List<String> historyDates) {
        add(getModel().getHistoryGankDaily(historyDates)
                .subscribe(new Consumer<List<GankDaily>>() {
                    @Override
                    public void accept(List<GankDaily> gankDailies) throws Exception {
                        getView().handleHistoryGankDailyResult(gankDailies);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().handleException(ExceptionEngine.handleException(throwable));
                    }
                }));
    }

}
