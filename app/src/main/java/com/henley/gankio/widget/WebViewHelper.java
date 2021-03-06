package com.henley.gankio.widget;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.orhanobut.logger.Logger;

import java.net.URISyntaxException;

/**
 * WebView辅助类
 *
 * @author Henley
 * @date 2018/7/4 17:58
 */
public class WebViewHelper {

    /**
     * 初始化WebSettings
     */
    @SuppressLint("SetJavaScriptEnabled")
    public static void initWebViewSettings(WebView webView) {
        Context context = webView.getContext();
        WebSettings settings = webView.getSettings();
        settings.setAllowContentAccess(true);// 是否使用WebView内置的变焦机制
        settings.setAllowFileAccess(true);// 是否允许访问文件
        settings.setJavaScriptEnabled(true);// 是否可以执行JavaScript脚本程序
//        settings.setSupportMultipleWindows(true);// 设置是否支持多窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true); // 是否支持通过js打开新窗口
        settings.setAppCacheEnabled(true); // 是否允许缓存，默认false
        settings.setAppCachePath(context.getCacheDir() + "/web");
        settings.setCacheMode(WebSettings.LOAD_DEFAULT); // 设置WebView的缓存模式
        settings.setBuiltInZoomControls(false);// 是否使用再带的缩放机制
        settings.setDisplayZoomControls(false);// 是否显示显示缩放工具
        settings.setSupportZoom(true);// 是否支持缩放
        settings.setDatabaseEnabled(true);// 是否启用数据库
        settings.setDomStorageEnabled(true);// 是否打开localStorage
        settings.setGeolocationEnabled(true);// 是否启用地理定位
        settings.setLoadsImagesAutomatically(true);// 是否支持自动加载图片
        settings.setLoadWithOverviewMode(true); // 是否设置WebView加载的页面模式
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 适应屏幕，内容将自动缩放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);// HTTP/HTTPS混合加载
        }
    }

    /**
     * 根据Url打开第三方APP
     */
    public static boolean openThirdPartyApp(Context context, String url) {
        Logger.i("InterceptUrl = " + url);
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))); // 打开APP
        } catch (NullPointerException | ActivityNotFoundException e) { // 打开APP失败
            try {
                context.startActivity(Intent.parseUri(url, 0)); // 打开APP
            } catch (URISyntaxException | ActivityNotFoundException e1) {
                return false;
            }
        }
        return true;
    }


    /**
     * 销毁WebView
     */
    public static void destroyWebView(WebView webView) {
        if (webView != null) { // 销毁WebView
            ViewParent parent = webView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.loadUrl("about:blank");
            webView.stopLoading();          // 停止加载
            webView.clearMatches();         // 清除创建的高亮显示文本匹配
            webView.clearHistory();         // 清除历史记录
            webView.clearFormData();        // 清除表单数据
            webView.clearAnimation();       // 取消视图动画
            webView.removeAllViews();       // 移除子视图
            webView.destroy();
        }
    }

}
