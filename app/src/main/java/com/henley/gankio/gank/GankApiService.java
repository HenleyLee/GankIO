package com.henley.gankio.gank;

import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.CategoryEntity;
import com.henley.gankio.entity.GankDaily;
import com.henley.gankio.entity.GankEntity;
import com.henley.gankio.entity.SubCategoryEntity;
import com.henley.gankio.entity.IdleReadingEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 干货API
 *
 * @author Henley
 * @date 2018/7/3 16:11
 */
public interface GankApiService {

    /**
     * 获取每日数据
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @see GankDaily
     */
    @GET("day/{year}/{month}/{day}")
    Observable<BaseGank<GankDaily>> getGankDaily(@Path("year") String year, @Path("month") String month, @Path("day") String day);

    /**
     * 获取分类数据
     *
     * @param type 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * @param size 请求个数： 数字，大于0
     * @param page 第几页：数字，大于0
     * @see GankEntity
     */
    @GET("data/{type}/{size}/{page}")
    Observable<BaseGank<List<GankEntity>>> getGankData(@Path("type") String type, @Path("size") int size, @Path("page") int page);

    /**
     * 获取发过干货的日期
     */
    @GET("day/history")
    Observable<BaseGank<List<String>>> getGankHistory();

    /**
     * 获取闲读的主分类
     *
     * @see CategoryEntity
     */
    @GET("xiandu/categories")
    Observable<BaseGank<List<CategoryEntity>>> getIdleReadingCategory();

    /**
     * 获取闲读的子分类
     *
     * @param category 主分类名称(英文)
     * @see SubCategoryEntity
     */
    @GET("xiandu/category/{category}")
    Observable<BaseGank<List<SubCategoryEntity>>> getIdleReadingSubCategory(@Path("category") String category);

    /**
     * 获取闲读数据
     *
     * @param category 子分类ID
     * @param size     请求个数： 数字，大于0
     * @param page     第几页：数字，大于0
     * @see IdleReadingEntity
     */
    @GET("xiandu/data/id/{category}/count/{size}/page/{page}")
    Observable<BaseGank<List<IdleReadingEntity>>> getIdleReadingData(@Path("category") String category, @Path("size") int size, @Path("page") int page);

}
