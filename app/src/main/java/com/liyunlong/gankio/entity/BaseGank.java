package com.liyunlong.gankio.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 干货数据基类
 *
 * @author liyunlong
 * @date 2018/7/3 15:09
 */
public class BaseGank<T> {

    @SerializedName(value = "error")
    private Boolean error;
    @SerializedName("results")
    private T results;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

}
