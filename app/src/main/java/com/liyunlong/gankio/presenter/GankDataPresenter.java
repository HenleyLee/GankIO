package com.liyunlong.gankio.presenter;

import com.liyunlong.gankio.contract.GankDataContract;
import com.liyunlong.gankio.entity.GankData;
import com.liyunlong.gankio.http.HttpException;
import com.liyunlong.gankio.http.HttpObserver;
import com.liyunlong.gankio.model.GankDataModel;

/**
 * 干货分类数据Presenter
 *
 * @author liyunlong
 * @date 2018/7/3 19:27
 */
public class GankDataPresenter extends GankDataContract.Presenter<GankDataModel> {

    @Override
    public void getGankData(String type, int size, int page) {
        subscribe(getModel().getGankData(type, size, page), new HttpObserver<GankData>(getContext()) {

            @Override
            public void onError(HttpException exception) {
                getView().handleException(exception);
            }

            @Override
            public void onNext(GankData gankData) {
                getView().handleGankDataResult(gankData);
            }
        });
    }

}
