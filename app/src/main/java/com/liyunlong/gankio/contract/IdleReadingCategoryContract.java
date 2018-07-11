package com.liyunlong.gankio.contract;

import com.liyunlong.gankio.entity.BaseGank;
import com.liyunlong.gankio.entity.CategoryEntity;
import com.liyunlong.gankio.mvp.BasePresenter;
import com.liyunlong.gankio.mvp.IMVPModel;
import com.liyunlong.gankio.mvp.IMVPView;

import java.util.List;

import io.reactivex.Observable;

/**
 * 闲读主分类Contract
 *
 * @author liyunlong
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
