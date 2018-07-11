package com.liyunlong.gankio.utils;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

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

    /**
     * 根据网络信息判断网络类型
     */
    public static NetworkType getNetworkType(NetworkInfo networkInfo) {
        NetworkType networkType = NetworkType.TYPE_UNKNOWN;
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            networkType = NetworkType.TYPE_WIFI;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            String subTypeName = networkInfo.getSubtypeName();
            int subType = networkInfo.getSubtype();
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2G
                case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2G
                case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2G
                case TelephonyManager.NETWORK_TYPE_1xRTT:// 电信2G
                case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by 11
                    networkType = NetworkType.TYPE_2G;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:// 电信3G
                case TelephonyManager.NETWORK_TYPE_EVDO_A:// 电信3G
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // 电信3G
                case TelephonyManager.NETWORK_TYPE_UMTS: // 联通3G
                case TelephonyManager.NETWORK_TYPE_HSDPA: // 联通3G
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by 15
                    networkType = NetworkType.TYPE_3G;
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:  //3G到4G的一个过渡，称为准4G
                    networkType = NetworkType.TYPE_4G;
                    break;
                default:
                    //中国移动 联通 电信 三种3G制式
                    if (subTypeName.equalsIgnoreCase("TD-SCDMA") || subTypeName.equalsIgnoreCase("WCDMA") || subTypeName.equalsIgnoreCase("CDMA2000")) {
                        networkType = NetworkType.TYPE_3G;
                    } else {
                        networkType = NetworkType.TYPE_UNKNOWN;
                    }
                    break;
            }
        }
        return networkType;
    }

}
