package com.liyunlong.gankio.gank;

import com.liyunlong.gankio.R;
import com.liyunlong.gankio.entity.GankType;

import java.util.HashMap;

/**
 * 常量类
 *
 * @author liyunlong
 * @date 2018/7/3 16:01
 */
public class GankConfig {

    public static final String TAG = "Gank.IO";

    public static final String BASE_URL = "https://gank.io/api/";

    public static final String GANK_HOME_NAME = "干货集中营";
    public static final String GANK_HOME_URL = "https://gank.io/";

    public static final String GANK_SUBSCRIBE_NAME = "订阅干货集中营";
    public static final String GANK_SUBSCRIBE_URL = "https://gank.io/subscribe";

    public static final String GANK_SUBMIT_READ_NAME = "推荐优质内容";
    public static final String GANK_SUBMIT_READ_URL = "https://gank.io/xiandu/submit";

    public static final String GIHUB_TRENDING_NAME = "Trending Java repositories on GitHub today";
    public static final String GIHUB_TRENDING_URL = "https://github.com/trending/java?since=daily";

    public static final String GANK_DATA_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DISPLAY_DATE_FORMAT = "yyyy/MM/dd";
    public static final String WELFARE_DATE_FORMAT = "yyyy-MM-dd";

    public static final int EXIT_WAIT_TIME = 2000;

    public static final int PAGE_INDEX = 1;
    public static final int PAGE_SIZE = 20;

    public static final String GANK_TYPE = "gank_type";
    public static final String GANK_DAILY = "gank_daily";
    public static final String GANK_CATEGORY = "gank_category";
    public static final String GANK_PAGE_URL = "gank_page_url";
    public static final String GANK_PAGR_TITLE = "gank_pagr_title";
    public static final String IMAGE_TRANSITION_NAME = "imageTransitionName";

    public static final int[] MATERIAL_COLORS = {0XFF0086FF, 0XFF0086FF, 0XFF0086FF, 0XFF0086FF};

    private static final HashMap<Integer, GankType> MENUID_GANKTYPE_MAP = new HashMap<Integer, GankType>() {
        {
            put(R.id.navigation_all, GankType.All); // 全部
            put(R.id.navigation_welfare, GankType.Welfare); // 福利
            put(R.id.navigation_android, GankType.Android); // Android
            put(R.id.navigation_ios, GankType.IOS); // IOS
            put(R.id.navigation_js, GankType.JS); // 前端
            put(R.id.navigation_video, GankType.Video); // 休息视频
            put(R.id.navigation_resource, GankType.Resources); // 拓展资源
            put(R.id.navigation_app, GankType.App); // APP
            put(R.id.navigation_recommend, GankType.Recommend); // 瞎推荐
        }
    };

    private static final HashMap<String, Integer> TYPE_COLOR_MAP = new HashMap<String, Integer>() {
        {
            put(GankType.All.getName(), 0X00000000); // 全部
            put(GankType.Welfare.getName(), 0XFFFF4081); // 福利
            put(GankType.Android.getName(), 0XFF388E3C); // Android
            put(GankType.IOS.getName(), 0XFF0377BC); // IOS
            put(GankType.JS.getName(), 0XFFEF6C02); // 前端
            put(GankType.Video.getName(), 0xff039588); // 休息视频
            put(GankType.Resources.getName(), 0XFF546E7A); // 拓展资源
            put(GankType.App.getName(), 0XFFC02E34); // APP
            put(GankType.Recommend.getName(), 0XFF4527A0); // 瞎推荐
        }
    };

    private static final HashMap<String, Integer> TYPE_ICON_MAP = new HashMap<String, Integer>() {
        {
            put(GankType.All.getName(), R.drawable.ic_menu_all); // 全部
            put(GankType.Welfare.getName(), R.drawable.ic_menu_welfare); // 福利
            put(GankType.Android.getName(), R.drawable.ic_menu_android); // Android
            put(GankType.IOS.getName(), R.drawable.ic_menu_ios); // IOS
            put(GankType.JS.getName(), R.drawable.ic_menu_js); // 前端
            put(GankType.Video.getName(), R.drawable.ic_menu_video); // 休息视频
            put(GankType.Resources.getName(), R.drawable.ic_menu_resource); // 拓展资源
            put(GankType.App.getName(), R.drawable.ic_menu_app); // APP
            put(GankType.Recommend.getName(), R.drawable.ic_menu_recommend); // 瞎推荐
        }
    };

    public static GankType getGankType(int itemId) {
        GankType gankType = MENUID_GANKTYPE_MAP.get(itemId);
        if (gankType == null) {
            gankType = GankType.All;
        }
        return gankType;
    }

    public static int getTypeColor(String gankType) {
        Integer typeColor = TYPE_COLOR_MAP.get(gankType);
        if (typeColor == null) {
            typeColor = TYPE_COLOR_MAP.get(GankType.All.getName());
        }
        return typeColor;
    }

    public static int getTypeIcon(String gankType) {
        Integer typeIcon = TYPE_ICON_MAP.get(gankType);
        if (typeIcon == null) {
            typeIcon = TYPE_ICON_MAP.get(GankType.All.getName());
        }
        return typeIcon;
    }

}
