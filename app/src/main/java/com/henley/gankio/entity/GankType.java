package com.henley.gankio.entity;

/**
 * 干货类型
 *
 * @author Henley
 * @date 2018/7/3 14:34
 */
public enum GankType {

    Daily("今日力推"),
    All("all"),
    Welfare("福利"),
    Android("Android"),
    IOS("iOS"),
    JS("前端"),
    Video("休息视频"),
    Resources("拓展资源"),
    App("App"),
    Recommend("瞎推荐");

    private String name;

    GankType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
