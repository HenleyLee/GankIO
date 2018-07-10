package com.liyunlong.gankio.presenter;

import com.liyunlong.gankio.contract.TimeReadDetailContract;
import com.liyunlong.gankio.entity.BaseGank;
import com.liyunlong.gankio.entity.SubCategoryEntity;
import com.liyunlong.gankio.entity.TimeReadEntity;
import com.liyunlong.gankio.http.HttpException;
import com.liyunlong.gankio.http.HttpObserver;
import com.liyunlong.gankio.model.TimeReadDetailModel;

import java.util.List;

/**
 * 闲读分类Presenter
 *
 * @author liyunlong
 * @date 2018/7/9 15:48
 */
public class TimeReadDetailPresenter extends TimeReadDetailContract.Presenter<TimeReadDetailModel> {

    @Override
    public void getTimeReadSubCategory(String category) {
        subscribe(getModel().getTimeReadSubCategory(category), new HttpObserver<BaseGank<List<SubCategoryEntity>>>(getContext()) {

            @Override
            public void onError(HttpException exception) {
                getView().handleException(exception);
            }

            @Override
            public void onNext(BaseGank<List<SubCategoryEntity>> gank) {
                getView().handleTimeReadSubCategoryResult(gank);
            }
        });
    }

    @Override
    public void getTimeReadData(String category, int size, int page) {
        subscribe(getModel().getTimeReadData(category, size, page), new HttpObserver<BaseGank<List<TimeReadEntity>>>(getContext()) {

            @Override
            public void onError(HttpException exception) {
                getView().handleException(exception);
            }

            @Override
            public void onNext(BaseGank<List<TimeReadEntity>> gank) {
                getView().handleTimeReadDataResult(gank);
            }
        });
    }

}
