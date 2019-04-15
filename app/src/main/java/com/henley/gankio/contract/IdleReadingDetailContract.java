package com.henley.gankio.contract;

import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.SubCategoryEntity;
import com.henley.gankio.entity.IdleReadingEntity;
import com.henley.gankio.mvp.BasePresenter;
import com.henley.gankio.mvp.IMVPModel;
import com.henley.gankio.mvp.IMVPView;

import java.util.List;

import io.reactivex.Observable;

/**
 * 闲读分类Contract
 *
 * @author Henley
 * @date 2018/7/9 15:48
 */
public interface IdleReadingDetailContract {

    interface View extends IMVPView {

        void handleIdleReadingSubCategoryResult(BaseGank<List<SubCategoryEntity>> gank);

        void handleIdleReadingDataResult(BaseGank<List<IdleReadingEntity>> gank);

    }

    interface Model extends IMVPModel {

        Observable<BaseGank<List<SubCategoryEntity>>> getIdleReadingSubCategory(String category);

        Observable<BaseGank<List<IdleReadingEntity>>> getIdleReadingData(String category, int size, int page);

    }

    abstract class Presenter<Model> extends BasePresenter<Model, View> {

        public abstract void getIdleReadingSubCategory(String category);

        public abstract void getIdleReadingData(String category, int size, int page);

    }

}
