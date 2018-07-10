package com.liyunlong.gankio.presenter;

import com.liyunlong.gankio.contract.TimeReadCategoryContract;
import com.liyunlong.gankio.entity.BaseGank;
import com.liyunlong.gankio.entity.CategoryEntity;
import com.liyunlong.gankio.http.HttpException;
import com.liyunlong.gankio.http.HttpObserver;
import com.liyunlong.gankio.model.TimeReadCategoryModel;

import java.util.List;

/**
 * 闲读主分类Presenter
 *
 * @author liyunlong
 * @date 2018/7/9 14:21
 */
public class TimeReadCategoryPresenter extends TimeReadCategoryContract.Presenter<TimeReadCategoryModel> {

    @Override
    public void getTimeReadCategory() {
        subscribe(getModel().getTimeReadCategory(), new HttpObserver<BaseGank<List<CategoryEntity>>>(getContext()) {

            @Override
            public void onError(HttpException exception) {
                getView().handleException(exception);
            }

            @Override
            public void onNext(BaseGank<List<CategoryEntity>> gank) {
                getView().handleTimeReadCategoryResult(gank);
            }
        });
    }

}
