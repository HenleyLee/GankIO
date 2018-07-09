package com.liyunlong.gankio.model;

import com.liyunlong.gankio.contract.GankDataContract;
import com.liyunlong.gankio.entity.GankData;
import com.liyunlong.gankio.http.HttpManager;

import io.reactivex.Observable;

/**
 * 干货分类数据Model
 *
 * @author liyunlong
 * @date 2018/7/3 19:27
 */
public class GankDataModel implements GankDataContract.Model {

    @Override
    public Observable<GankData> getGankData(String type, int size, int page) {
        return HttpManager.getInstance()
                .getGankApiService()
                .getGankData(type, size, page);
    }

}
