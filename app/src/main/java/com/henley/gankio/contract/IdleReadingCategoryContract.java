package com.henley.gankio.contract;

import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.CategoryEntity;
import com.henley.gankio.mvp.BasePresenter;
import com.henley.gankio.mvp.IMVPModel;
import com.henley.gankio.mvp.IMVPView;

import java.util.List;

import io.reactivex.Observable;

/**
 * 闲读主分类Contract
 *
 * @author Henley
 * @date 2018/7/9 14:21
 */
public interface IdleReadingCategoryContract {

    interface View extends IMVPView {

        void handleIdleReadingCategoryResult(BaseGank<List<CategoryEntity>> gank);

    }

    interface Model extends IMVPModel {

        Observable<BaseGank<List<CategoryEntity>>> getIdleReadingCategory();
    }

    abstract class Presenter<Model> extends BasePresenter<Model, View> {

        public abstract void getIdleReadingCategory();

    }

}
