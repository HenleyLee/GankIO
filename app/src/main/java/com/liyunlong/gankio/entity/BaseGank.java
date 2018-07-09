package com.liyunlong.gankio.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 干货数据基类
 *
 * @author liyunlong
 * @date 2018/7/3 15:09
 */
public class BaseGank {

    @SerializedName(value = "error")
    private Boolean error;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

}
