package com.liyunlong.gankio.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 干货分类数据
 *
 * @author liyunlong
 * @date 2018/7/3 15:04
 */
public class GankData extends BaseGank {

    @SerializedName(value = "results")
    private List<GankEntity> results;

    public List<GankEntity> getResults() {
        return results;
    }

    public void setResults(List<GankEntity> results) {
        this.results = results;
    }

}
