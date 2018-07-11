package com.liyunlong.gankio.presenter;

import com.liyunlong.gankio.contract.IdleReadingDetailContract;
import com.liyunlong.gankio.entity.BaseGank;
import com.liyunlong.gankio.entity.SubCategoryEntity;
import com.liyunlong.gankio.entity.IdleReadingEntity;
import com.liyunlong.gankio.http.HttpException;
import com.liyunlong.gankio.http.HttpObserver;
import com.liyunlong.gankio.model.IdleReadingDetailModel;

import java.util.List;

/**
 * 闲读分类Presenter
 *
 * @author liyunlong
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
