package com.henley.gankio.model;

import com.henley.gankio.contract.IdleReadingCategoryContract;
import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.CategoryEntity;
import com.henley.gankio.http.HttpManager;

import java.util.List;

import io.reactivex.Observable;

/**
 * 闲读主分类Model
 *
 * @author Henley
 * @date 2018/7/9 14:21
 */
public class IdleReadingCategoryModel implements IdleReadingCategoryContract.Model {

    @Override
    public Observable<BaseGank<List<CategoryEntity>>> getIdleReadingCategory() {
        return HttpManager.getInstance()
                .getGankApiService()
                .getIdleReadingCategory();
    }

}
