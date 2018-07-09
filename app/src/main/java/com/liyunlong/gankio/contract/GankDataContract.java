package com.liyunlong.gankio.contract;

import com.liyunlong.gankio.entity.GankData;
import com.liyunlong.gankio.mvp.BasePresenter;
import com.liyunlong.gankio.mvp.IMVPModel;
import com.liyunlong.gankio.mvp.IMVPView;

import io.reactivex.Observable;

/**
 * 干货分类数据Contract
 *
 * @author liyunlong
 * @date 2018/7/3 19:27
 */
public interface GankDataContract {

    interface View extends IMVPView {

        void handleGankDataResult(GankData gankData);
    }

    interface Model extends IMVPModel {

        Observable<GankData> getGankData(String type, int size, int page);
    }

    abstract class Presenter<Model> extends BasePresenter<Model, View> {

        public abstract void getGankData(String type, int size, int page);

    }

}
