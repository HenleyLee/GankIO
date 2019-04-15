package com.henley.gankio.presenter;

import com.henley.gankio.contract.GankDataContract;
import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.GankEntity;
import com.henley.gankio.http.HttpException;
import com.henley.gankio.http.HttpObserver;
import com.henley.gankio.model.GankDataModel;

import java.util.List;

/**
 * 干货分类数据Presenter
 *
 * @author Henley
 * @date 2018/7/3 19:27
 */
public class GankDataPresenter extends GankDataContract.Presenter<GankDataModel> {

    @Override
    public void getGankData(String type, int size, int page) {
        subscribe(getModel().getGankData(type, size, page), new HttpObserver<BaseGank<List<GankEntity>>>(getContext()) {

            @Override
            public void onError(HttpException exception) {
                getView().handleException(exception);
            }

            @Override
            public void onNext(BaseGank<List<GankEntity>> gank) {
                getView().handleGankDataResult(gank);
            }
        });
    }

}
