package com.liyunlong.gankio.statuslayout;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liyunlong.gankio.R;

/**
 * 多状态布局辅助类
 *
 * @author liyunlong
 * @date 2018/7/5 17:29
 */
public class StatusLayoutManager {

    private final Context context;
    private final View emptyLayout;
    private final View errorLayout;
    private final View loadingLayout;
    private final View netWorkErrorLayout;
    private final View netWorkPoorLayout;
    private View contentLayout;
    private View currentLayout;
    private OnRetryActionListener onRetryActionListener;

    public static StatusLayoutManager create(Context context) {
        return new StatusLayoutManager(context);
    }

    private StatusLayoutManager(Context context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        emptyLayout = inflater.inflate(R.layout.layout_status_empty, null);
        errorLayout = inflater.inflate(R.layout.layout_status_empty, null);
        loadingLayout = inflater.inflate(R.layout.layout_status_loading, null);
        netWorkErrorLayout = inflater.inflate(R.layout.layout_status_network_error, null);
        netWorkPoorLayout = inflater.inflate(R.layout.layout_status_network_error, null);
    }

    public Context getContext() {
        return context;
    }

    public View getEmptyLayout() {
        return emptyLayout;
    }

    public View getErrorLayout() {
        return errorLayout;
    }

    public View getLoadingLayout() {
        return loadingLayout;
    }

    public View getNetWorkErrorLayout() {
        return netWorkErrorLayout;
    }

    public View getNetWorkPoorLayout() {
        return netWorkPoorLayout;
    }

    public View getContentLayout() {
        return contentLayout;
    }

    public View getCurrentLayout() {
        return currentLayout;
    }

    public void setCurrentLayout(View currentLayout) {
        this.currentLayout = currentLayout;
    }

    /**
     * 设置内容布局
     */
    public StatusLayoutManager setContentLayout(View contentLayout) {
        this.contentLayout = contentLayout;
        this.currentLayout = contentLayout;
        return this;
    }

    /**
     * 设置重试操作监听
     */
    public StatusLayoutManager setOnRetryActionListener(OnRetryActionListener onRetryActionListener) {
        this.onRetryActionListener = onRetryActionListener;
        return this;
    }

    /**
     * 设置无数据样式的图标是否显示
     */
    public StatusLayoutManager setEmptyDrawable(boolean hide) {
        ImageView ivIcon = (ImageView) emptyLayout.findViewById(R.id.status_state_icon);
        ivIcon.setVisibility(hide ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置无数据样式的图标
     */
    public StatusLayoutManager setEmptyDrawable(@DrawableRes int resId) {
        ImageView ivIcon = (ImageView) emptyLayout.findViewById(R.id.status_state_icon);
        ivIcon.setImageResource(resId);
        ivIcon.setVisibility(resId == 0 ? View.GONE : View.VISIBLE);
        return this;
    }


    /**
     * 设置无数据样式的提示信息
     */
    public StatusLayoutManager setEmptyMessage(boolean hide) {
        TextView tvMessage = (TextView) emptyLayout.findViewById(R.id.status_state_desc);
        tvMessage.setVisibility(hide ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置无数据样式的提示信息
     */
    public StatusLayoutManager setEmptyMessage(@StringRes int resId) {
        return setEmptyMessage(context.getText(resId));
    }

    /**
     * 设置无数据样式的提示信息
     */
    public StatusLayoutManager setEmptyMessage(CharSequence message) {
        TextView tvMessage = (TextView) emptyLayout.findViewById(R.id.status_state_desc);
        tvMessage.setText(message);
        tvMessage.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置加载异常样式的图标是否显示
     */
    public StatusLayoutManager setErrorDrawable(boolean hide) {
        ImageView ivIcon = (ImageView) errorLayout.findViewById(R.id.status_state_icon);
        ivIcon.setVisibility(hide ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置加载异常样式的图标
     */
    public StatusLayoutManager setErrorDrawable(@DrawableRes int resId) {
        ImageView ivIcon = (ImageView) errorLayout.findViewById(R.id.status_state_icon);
        ivIcon.setImageResource(resId);
        ivIcon.setVisibility(resId == 0 ? View.GONE : View.VISIBLE);
        return this;
    }


    /**
     * 设置加载异常样式的提示信息
     */
    public StatusLayoutManager setErrorMessage(boolean hide) {
        TextView tvMessage = (TextView) errorLayout.findViewById(R.id.status_state_desc);
        tvMessage.setVisibility(hide ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置加载异常样式的提示信息
     */
    public StatusLayoutManager setErrorMessage(@StringRes int resId) {
        return setErrorMessage(context.getText(resId));
    }

    /**
     * 设置加载异常样式的提示信息
     */
    public StatusLayoutManager setErrorMessage(CharSequence message) {
        TextView tvMessage = (TextView) errorLayout.findViewById(R.id.status_state_desc);
        tvMessage.setText(message);
        tvMessage.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置网络异常样式的图标是否显示
     */
    public StatusLayoutManager setNetworkErrorDrawable(boolean hide) {
        ImageView ivIcon = (ImageView) netWorkErrorLayout.findViewById(R.id.status_state_icon);
        ivIcon.setVisibility(hide ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置网络异常样式的图标
     */
    public StatusLayoutManager setNetworkErrorDrawable(@DrawableRes int resId) {
        ImageView ivIcon = (ImageView) netWorkErrorLayout.findViewById(R.id.status_state_icon);
        ivIcon.setImageResource(resId);
        ivIcon.setVisibility(resId == 0 ? View.GONE : View.VISIBLE);
        return this;
    }


    /**
     * 设置网络异常样式的提示信息
     */
    public StatusLayoutManager setNetworkErrorMessage(boolean hide) {
        TextView tvMessage = (TextView) netWorkErrorLayout.findViewById(R.id.status_state_desc);
        tvMessage.setVisibility(hide ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置网络异常样式的提示信息
     */
    public StatusLayoutManager setNetworkErrorMessage(@StringRes int resId) {
        return setNetworkErrorMessage(context.getText(resId));
    }

    /**
     * 设置网络异常样式的提示信息
     */
    public StatusLayoutManager setNetworkErrorMessage(CharSequence message) {
        TextView tvMessage = (TextView) netWorkErrorLayout.findViewById(R.id.status_state_desc);
        tvMessage.setText(message);
        tvMessage.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置网络不佳样式的图标是否显示
     */
    public StatusLayoutManager setNetworkPoorDrawable(boolean hide) {
        ImageView ivIcon = (ImageView) netWorkPoorLayout.findViewById(R.id.status_state_icon);
        ivIcon.setVisibility(hide ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置网络不佳样式的图标
     */
    public StatusLayoutManager setNetworkPoorDrawable(@DrawableRes int resId) {
        ImageView ivIcon = (ImageView) netWorkPoorLayout.findViewById(R.id.status_state_icon);
        ivIcon.setImageResource(resId);
        ivIcon.setVisibility(resId == 0 ? View.GONE : View.VISIBLE);
        return this;
    }


    /**
     * 设置网络不佳样式的提示信息
     */
    public StatusLayoutManager setNetworkPoorMessage(boolean hide) {
        TextView tvMessage = (TextView) netWorkPoorLayout.findViewById(R.id.status_state_desc);
        tvMessage.setVisibility(hide ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 设置网络不佳样式的提示信息
     */
    public StatusLayoutManager setNetworkPoorMessage(@StringRes int resId) {
        return setNetworkPoorMessage(context.getText(resId));
    }

    /**
     * 设置网络不佳样式的提示信息
     */
    public StatusLayoutManager setNetworkPoorMessage(CharSequence message) {
        TextView tvMessage = (TextView) netWorkPoorLayout.findViewById(R.id.status_state_desc);
        tvMessage.setText(message);
        tvMessage.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
        return this;
    }

}
