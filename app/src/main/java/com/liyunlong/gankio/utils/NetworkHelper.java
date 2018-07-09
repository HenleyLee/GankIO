package com.liyunlong.gankio.utils;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresPermission;

/**
 * 判断网络状态的工具类
 *
 * @author liyunlong
 * @date 2018/7/5 17:43
 */
public class NetworkHelper {

    /**
     * 判断网络是否可以使用
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();// 后去可用的网络信息
                if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) { // 判断网络是否可用
                    return true;
                }
            }
        }
        return false;
    }

}
