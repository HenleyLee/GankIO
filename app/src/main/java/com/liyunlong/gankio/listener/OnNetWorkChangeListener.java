package com.liyunlong.gankio.listener;

import com.liyunlong.gankio.utils.NetworkType;

/**
 * 网络状态改变监听
 *
 * @author liyunlong
 * @date 2018/7/11 14:12
 */
public interface OnNetWorkChangeListener {

    /**
     * 当网络状态发送改变时回调该方法
     *
     * @param isAvailable 当前网络是否可用
     * @param oldType     改变之前的网络类型
     * @param newType     改变之后的网络类型
     */
    void onNetWorkChange(boolean isAvailable, NetworkType oldType, NetworkType newType);

}
