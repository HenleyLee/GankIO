package com.henley.gankio.model;

import com.henley.gankio.contract.GankHistoryContract;
import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.GankDaily;
import com.henley.gankio.entity.HistoryDate;
import com.henley.gankio.http.HttpManager;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 每日干货数据Model
 *
 * @author Henley
 * @date 2018/7/5 10:34
 */
public class GankHistoryModel implements GankHistoryContract.Model {

    @Override
    public Observable<BaseGank<List<String>>> getGankHistory() {
        return HttpManager.getInstance()
                .getGankApiService()
                .getGankHistory();
    }

    @Override
    public Single<List<BaseGank<GankDaily>>> getHistoryGankDaily(List<String> historyDates) {
        return Observable.fromIterable(historyDates)
                .map(new Function<String, HistoryDate>() {
                    @Override
                    public HistoryDate apply(String date) throws Exception {
                        return HistoryDate.parse(date);
                    }
                })
                .filter(new Predicate<HistoryDate>() {
                    @Override
                    public boolean test(HistoryDate historyDate) throws Exception {
                        return historyDate != null;
                    }
                })
                .flatMap(new Function<HistoryDate, ObservableSource<BaseGank<GankDaily>>>() {
                    @Override
                    public ObservableSource<BaseGank<GankDaily>> apply(HistoryDate historyDate) throws Exception {
                        return getGankDaily(historyDate.getYear(), historyDate.getMonth(), historyDate.getDay())
                                .filter(new Predicate<BaseGank<GankDaily>>() {
                                    @Override
                                    public boolean test(BaseGank<GankDaily> gank) throws Exception {
                                        return gank != null
                                                && gank.getResults() != null
                                                && gank.getResults().getAndroidData() != null;
                                    }
                                })
                                .unsubscribeOn(Schedulers.io()) // 取消订阅
                                .subscribeOn(Schedulers.io()) // 指定上游发送事件的线程(只有第一次指定有效)
                                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
                    }
                })
                .toSortedList(new Comparator<BaseGank<GankDaily>>() {
                    @Override
                    public int compare(BaseGank<GankDaily> gank1, BaseGank<GankDaily> gank2) {
                        return gank2.getResults().getAndroidData().get(0).getPublishedTime().compareTo(
                                gank1.getResults().getAndroidData().get(0).getPublishedTime());
                    }
                })
                .unsubscribeOn(Schedulers.newThread()) // 取消订阅
                .subscribeOn(Schedulers.newThread()) // 指定上游发送事件的线程(只有第一次指定有效)
                .observeOn(AndroidSchedulers.mainThread());// 指定下游接收事件的线程(每次指定都有效)
    }

    @Override
    public Observable<BaseGank<GankDaily>> getGankDaily(String year, String month, String day) {
        return HttpManager.getInstance()
                .getGankApiService()
                .getGankDaily(year, month, day);
    }

}
