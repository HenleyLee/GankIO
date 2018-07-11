package com.liyunlong.gankio.utils;

/**
 * 网络类型
 *
 * @author liyunlong
 * @date 2018/7/11 14:10
 */
public enum NetworkType {

    TYPE_NONE("NONE"),
    TYPE_UNKNOWN("UNKNOWN"),
    TYPE_WIFI("WIFI"),
    TYPE_2G("2G"),
    TYPE_3G("3G"),
    TYPE_4G("4G");

    private String name;

    NetworkType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
