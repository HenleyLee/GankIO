package com.henley.gankio.mvp;

import androidx.annotation.UiThread;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * MVPViewHelper的接口
 *
 * @author Henley
 * @date 2018/7/3 16:45
 */
public interface IMVPViewHelper extends IMVPView {

    /**
     * 设置下拉刷新布局
     *
     * @param refreshLayout 下拉刷新布局
     */
    void setRefreshLayout(SmartRefreshLayout refreshLayout);

    /**
     * 显示内容View
     */
    @UiThread
    void showContentLayout();

    /**
     * 显示无数据View
     */
    @UiThread
    void showEmptyLayout();

    /**
     * 显示加载中View
     */
    @UiThread
    void showLoadingLayout();

    /**
     * 显示加载失败View
     */
    @UiThread
    void showErrorLayout();

    /**
     * 显示无网络View
     */
    @UiThread
    void showNetworkErrorLayout();

    /**
     * 显示网络不佳View
     */
    @UiThread
    void showNetworkPoorLayout();

    /**
     * 恢复显示有数据View
     */
    @UiThread
    void restoreLayout();

}
