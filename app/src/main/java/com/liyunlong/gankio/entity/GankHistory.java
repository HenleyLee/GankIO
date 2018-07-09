package com.liyunlong.gankio.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 干货历史数据
 *
 * @author liyunlong
 * @date 2018/7/3 16:12
 */
public class GankHistory extends BaseGank {

    @SerializedName("results")
    private List<String> results;

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

}
