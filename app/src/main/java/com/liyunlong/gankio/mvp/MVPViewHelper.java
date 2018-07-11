package com.liyunlong.gankio.mvp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.liyunlong.gankio.http.HttpException;
import com.liyunlong.gankio.statuslayout.StatusLayoutManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * MVP模式中IMVPView的实现类
 *
 * @author liyunlong
 * @date 2017/8/28 15:54
 */
public class MVPViewHelper implements IMVPViewHelper {

    private Context context;
    private SmartRefreshLayout refreshLayout;
    private StatusLayoutManager statusLayoutManager;

    public MVPViewHelper(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void setRefreshLayout(SmartRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    @Override
    public void handleException(HttpException exception) {

    }

    public StatusLayoutManager getStatusLayoutManager() {
        return statusLayoutManager;
    }

    public void setStatusLayoutManager(StatusLayoutManager statusLayoutManager) {
        this.statusLayoutManager = statusLayoutManager;
    }

    @Override
    public void showContentLayout() {
        if (statusLayoutManager != null && refreshLayout != null) {
            setRefreshContent(statusLayoutManager.getContentLayout());
        }
    }

    @Override
    public void showEmptyLayout() {
        if (statusLayoutManager != null && refreshLayout != null) {
            setRefreshContent(statusLayoutManager.getEmptyLayout());
        }
    }

    @Override
    public void showLoadingLayout() {
        if (statusLayoutManager != null && refreshLayout != null) {
            setRefreshContent(statusLayoutManager.getLoadingLayout());
        }
    }

    @Override
    public void showErrorLayout() {
        if (statusLayoutManager != null && refreshLayout != null) {
            setRefreshContent(statusLayoutManager.getErrorLayout());
        }
    }

    @Override
    public void showNetworkErrorLayout() {
        if (statusLayoutManager != null && refreshLayout != null) {
            setRefreshContent(statusLayoutManager.getNetWorkErrorLayout());
        }
    }

    @Override
    public void showNetworkPoorLayout() {
        if (statusLayoutManager != null && refreshLayout != null) {
            setRefreshContent(statusLayoutManager.getNetWorkPoorLayout());
        }
    }

    @Override
    public void restoreLayout() {
        if (statusLayoutManager != null && refreshLayout != null) {
            setRefreshContent(statusLayoutManager.getContentLayout());
        }
    }

    private void setRefreshContent(@NonNull View content) {
        if (content == statusLayoutManager.getCurrentLayout()) {
            return;
        }
        ViewParent parent = content.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(content);
        }
        refreshLayout.setRefreshContent(content);
        statusLayoutManager.setCurrentLayout(content);
    }

}
