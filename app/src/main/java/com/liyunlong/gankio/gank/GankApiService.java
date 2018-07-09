package com.liyunlong.gankio.gank;

import com.liyunlong.gankio.entity.GankDaily;
import com.liyunlong.gankio.entity.GankData;
import com.liyunlong.gankio.entity.GankHistory;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 干货API
 *
 * @author liyunlong
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
    Observable<GankDaily> getGankDaily(@Path("year") String year, @Path("month") String month, @Path("day") String day);

    /**
     * 获取分类数据
     *
     * @param type 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * @param size 请求个数： 数字，大于0
     * @param page 第几页：数字，大于0
     * @see GankData
     */
    @GET("data/{type}/{size}/{page}")
    Observable<GankData> getGankData(@Path("type") String type, @Path("size") int size, @Path("page") int page);

    /**
     * 获取发过干货的日期
     *
     * @see GankHistory
     */
    @GET("day/history")
    Observable<GankHistory> getGankHistory();

}
