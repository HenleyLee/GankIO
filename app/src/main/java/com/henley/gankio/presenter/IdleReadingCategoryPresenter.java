package com.henley.gankio.presenter;

import com.henley.gankio.contract.IdleReadingCategoryContract;
import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.CategoryEntity;
import com.henley.gankio.http.HttpException;
import com.henley.gankio.http.HttpObserver;
import com.henley.gankio.model.IdleReadingCategoryModel;

import java.util.List;

/**
 * 闲读主分类Presenter
 *
 * @author Henley
 * @date 2018/7/9 14:21
 */
public class IdleReadingCategoryPresenter extends IdleReadingCategoryContract.Presenter<IdleReadingCategoryModel> {

    @Override
    public void getIdleReadingCategory() {
        subscribe(getModel().getIdleReadingCategory(), new HttpObserver<BaseGank<List<CategoryEntity>>>(getContext()) {

            @Override
            public void onError(HttpException exception) {
                getView().handleException(exception);
            }

            @Override
            public void onNext(BaseGank<List<CategoryEntity>> gank) {
                getView().handleIdleReadingCategoryResult(gank);
            }
        });
    }

}
