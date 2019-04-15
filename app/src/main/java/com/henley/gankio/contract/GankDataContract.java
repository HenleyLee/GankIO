package com.henley.gankio.contract;

import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.GankEntity;
import com.henley.gankio.mvp.BasePresenter;
import com.henley.gankio.mvp.IMVPModel;
import com.henley.gankio.mvp.IMVPView;

import java.util.List;

import io.reactivex.Observable;

/**
 * 干货分类数据Contract
 *
 * @author Henley
 * @date 2018/7/3 19:27
 */
public interface GankDataContract {

    interface View extends IMVPView {

        void handleGankDataResult(BaseGank<List<GankEntity>> gank);
    }

    interface Model extends IMVPModel {

        Observable<BaseGank<List<GankEntity>>> getGankData(String type, int size, int page);
    }

    abstract class Presenter<Model> extends BasePresenter<Model, View> {

        public abstract void getGankData(String type, int size, int page);

    }

}
