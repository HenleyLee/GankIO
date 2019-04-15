package com.henley.gankio.contract;

import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.GankDaily;
import com.henley.gankio.mvp.BasePresenter;
import com.henley.gankio.mvp.IMVPModel;
import com.henley.gankio.mvp.IMVPView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * 每日干货数据Contract
 *
 * @author Henley
 * @date 2018/7/5 10:34
 */
public interface GankHistoryContract {

    interface View extends IMVPView {

        void handleGankHistoryResult(BaseGank<List<String>> gank);

        void handleHistoryGankDailyResult(List<BaseGank<GankDaily>> gankDailies);
    }

    interface Model extends IMVPModel {

        Observable<BaseGank<List<String>>> getGankHistory();

        Single<List<BaseGank<GankDaily>>> getHistoryGankDaily(List<String> historyDates);

        Observable<BaseGank<GankDaily>> getGankDaily(String year, String month, String day);
    }

    abstract class Presenter<Model> extends BasePresenter<Model, View> {

        public abstract void getGankHistory();

        public abstract void getHistoryGankDaily(List<String> historyDates);

    }

}
