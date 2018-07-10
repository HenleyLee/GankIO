package com.liyunlong.gankio.contract;

import com.liyunlong.gankio.entity.BaseGank;
import com.liyunlong.gankio.entity.SubCategoryEntity;
import com.liyunlong.gankio.entity.TimeReadEntity;
import com.liyunlong.gankio.mvp.BasePresenter;
import com.liyunlong.gankio.mvp.IMVPModel;
import com.liyunlong.gankio.mvp.IMVPView;

import java.util.List;

import io.reactivex.Observable;

/**
 * 闲读分类Contract
 *
 * @author liyunlong
 * @date 2018/7/9 15:48
 */
public interface TimeReadDetailContract {

    interface View extends IMVPView {

        void handleTimeReadSubCategoryResult(BaseGank<List<SubCategoryEntity>> gank);

        void handleTimeReadDataResult(BaseGank<List<TimeReadEntity>> gank);

    }

    interface Model extends IMVPModel {

        Observable<BaseGank<List<SubCategoryEntity>>> getTimeReadSubCategory(String category);

        Observable<BaseGank<List<TimeReadEntity>>> getTimeReadData(String category, int size, int page);

    }

    abstract class Presenter<Model> extends BasePresenter<Model, View> {

        public abstract void getTimeReadSubCategory(String category);

        public abstract void getTimeReadData(String category, int size, int page);

    }

}
