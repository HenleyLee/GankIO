package com.liyunlong.gankio.model;

import com.liyunlong.gankio.contract.TimeReadCategoryContract;
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
public class TimeReadCategoryModel implements TimeReadCategoryContract.Model {

    @Override
    public Observable<BaseGank<List<CategoryEntity>>> getTimeReadCategory() {
        return HttpManager.getInstance()
                .getGankApiService()
                .getTimeReadCategory();
    }

}
