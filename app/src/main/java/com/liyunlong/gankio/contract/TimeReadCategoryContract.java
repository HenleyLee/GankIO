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
public interface TimeReadCategoryContract {

    interface View extends IMVPView {

        void handleTimeReadCategoryResult(BaseGank<List<CategoryEntity>> gank);

    }

    interface Model extends IMVPModel {

        Observable<BaseGank<List<CategoryEntity>>> getTimeReadCategory();
    }

    abstract class Presenter<Model> extends BasePresenter<Model, View> {

        public abstract void getTimeReadCategory();

    }

}
