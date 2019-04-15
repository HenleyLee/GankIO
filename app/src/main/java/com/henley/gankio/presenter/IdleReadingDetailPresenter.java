package com.henley.gankio.presenter;

import com.henley.gankio.contract.IdleReadingDetailContract;
import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.SubCategoryEntity;
import com.henley.gankio.entity.IdleReadingEntity;
import com.henley.gankio.http.HttpException;
import com.henley.gankio.http.HttpObserver;
import com.henley.gankio.model.IdleReadingDetailModel;

import java.util.List;

/**
 * 闲读分类Presenter
 *
 * @author Henley
 * @date 2018/7/9 15:48
 */
public class IdleReadingDetailPresenter extends IdleReadingDetailContract.Presenter<IdleReadingDetailModel> {

    @Override
    public void getIdleReadingSubCategory(String category) {
        subscribe(getModel().getIdleReadingSubCategory(category), new HttpObserver<BaseGank<List<SubCategoryEntity>>>(getContext()) {

            @Override
            public void onError(HttpException exception) {
                getView().handleException(exception);
            }

            @Override
            public void onNext(BaseGank<List<SubCategoryEntity>> gank) {
                getView().handleIdleReadingSubCategoryResult(gank);
            }
        });
    }

    @Override
    public void getIdleReadingData(String category, int size, int page) {
        subscribe(getModel().getIdleReadingData(category, size, page), new HttpObserver<BaseGank<List<IdleReadingEntity>>>(getContext()) {

            @Override
            public void onError(HttpException exception) {
                getView().handleException(exception);
            }

            @Override
            public void onNext(BaseGank<List<IdleReadingEntity>> gank) {
                getView().handleIdleReadingDataResult(gank);
            }
        });
    }

}
