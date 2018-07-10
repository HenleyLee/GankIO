package com.liyunlong.gankio.model;

import com.liyunlong.gankio.contract.GankDataContract;
import com.liyunlong.gankio.entity.BaseGank;
import com.liyunlong.gankio.entity.GankEntity;
import com.liyunlong.gankio.http.HttpManager;

import java.util.List;

import io.reactivex.Observable;

/**
 * 干货分类数据Model
 *
 * @author liyunlong
 * @date 2018/7/3 19:27
 */
public class GankDataModel implements GankDataContract.Model {

    @Override
    public Observable<BaseGank<List<GankEntity>>> getGankData(String type, int size, int page) {
        return HttpManager.getInstance()
                .getGankApiService()
                .getGankData(type, size, page);
    }

}
