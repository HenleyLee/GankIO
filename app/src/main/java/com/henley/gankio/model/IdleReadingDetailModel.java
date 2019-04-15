package com.henley.gankio.model;

import com.henley.gankio.contract.IdleReadingDetailContract;
import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.SubCategoryEntity;
import com.henley.gankio.entity.IdleReadingEntity;
import com.henley.gankio.http.HttpManager;

import java.util.List;

import io.reactivex.Observable;

/**
 * 闲读分类Model
 *
 * @author Henley
 * @date 2018/7/9 15:48
 */
public class IdleReadingDetailModel implements IdleReadingDetailContract.Model {

    @Override
    public Observable<BaseGank<List<SubCategoryEntity>>> getIdleReadingSubCategory(String category) {
        return HttpManager.getInstance()
                .getGankApiService()
                .getIdleReadingSubCategory(category);
    }

    @Override
    public Observable<BaseGank<List<IdleReadingEntity>>> getIdleReadingData(String category, int size, int page) {
        return HttpManager.getInstance()
                .getGankApiService()
                .getIdleReadingData(category, size, page);
    }

}
