package com.liyunlong.gankio.model;

import com.liyunlong.gankio.contract.TimeReadDetailContract;
import com.liyunlong.gankio.entity.BaseGank;
import com.liyunlong.gankio.entity.SubCategoryEntity;
import com.liyunlong.gankio.entity.TimeReadEntity;
import com.liyunlong.gankio.http.HttpManager;

import java.util.List;

import io.reactivex.Observable;

/**
 * 闲读分类Model
 *
 * @author liyunlong
 * @date 2018/7/9 15:48
 */
public class TimeReadDetailModel implements TimeReadDetailContract.Model {

    @Override
    public Observable<BaseGank<List<SubCategoryEntity>>> getTimeReadSubCategory(String category) {
        return HttpManager.getInstance()
                .getGankApiService()
                .getTimeReadSubCategory(category);
    }

    @Override
    public Observable<BaseGank<List<TimeReadEntity>>> getTimeReadData(String category, int size, int page) {
        return HttpManager.getInstance()
                .getGankApiService()
                .getTimeReadData(category, size, page);
    }

}
