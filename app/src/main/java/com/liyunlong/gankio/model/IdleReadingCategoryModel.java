package com.liyunlong.gankio.model;

import com.liyunlong.gankio.contract.IdleReadingCategoryContract;
import com.liyunlong.gankio.entity.BaseGank;
import com.liyunlong.gankio.entity.CategoryEntity;
import com.liyunlong.gankio.http.HttpManager;

import java.util.List;

import io.reactivex.Observable;

/**
 * 闲读主分类Model
 *
 * @author liyunlong
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
