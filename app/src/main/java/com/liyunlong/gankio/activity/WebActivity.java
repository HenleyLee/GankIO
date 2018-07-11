package com.liyunlong.gankio.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.liyunlong.gankio.NetworkChangeReceiver;
import com.liyunlong.gankio.R;
import com.liyunlong.gankio.base.BaseActivity;
import com.liyunlong.gankio.gank.GankConfig;
import com.liyunlong.gankio.listener.OnNetWorkChangeListener;
import com.liyunlong.gankio.mvp.MVPViewHelper;
import com.liyunlong.gankio.utils.NetworkHelper;
import com.liyunlong.gankio.utils.NetworkType;
import com.liyunlong.gankio.utils.ShareHelper;
import com.liyunlong.gankio.utils.Utility;
import com.liyunlong.gankio.widget.WebViewHelper;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * Web页面
 *
 * @author liyunlong
 * @date 2018/7/4 13:05
 */
public class WebActivity extends BaseActivity implements OnNetWorkChangeListener {

    private String mUrl;
    private String mTitle;
    private boolean hasLoad;
    private WebView mWebView;
    private FrameLayout content;
    private MenuItem menuSwitchScreenMode;
    private SmartRefreshLayout mRefreshLayout;

    public static void startActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(GankConfig.GANK_PAGR_TITLE, title);
        intent.putExtra(GankConfig.GANK_PAGE_URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected int getMenuRes() {
        return R.menu.menu_web;
    }

    @Override
    protected String title() {
        return mTitle;
    }

    @Override
    protected int navigationIcon() {
        return R.drawable.ic_menu_cancle;
    }

    @Override
    protected View getContentView() {
        return content;
    }

    @Override
    protected void handleIntent(@NonNull Intent intent) {
        super.handleIntent(intent);
        mUrl = intent.getStringExtra(GankConfig.GANK_PAGE_URL);
        mTitle = intent.getStringExtra(GankConfig.GANK_PAGR_TITLE);
        Logger.i("Url = " + mUrl);
    }

    @Override
    protected void initViews() {
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
        setRefreshLayout(mRefreshLayout);
        ProgressBar progressBar = findViewById(R.id.web_progressbar);
        content = findViewById(R.id.web_content);
        mWebView = findViewById(R.id.web_webview);
        WebViewHelper.initWebViewSettings(mWebView);
        mWebView.setWebViewClient(new CustomWebViewClient(getMvpViewHelper()));
        mWebView.setWebChromeClient(new CustomWebChromeClient(progressBar));
        mWebView.setDownloadListener(new DownloadListener() { // 初始化下载
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Utility.openWebKit(getContext(), url);
            }
        });

    }

    @Override
    protected void initComponents() {
        super.initComponents();
        NetworkChangeReceiver.getInstance().addOnNetWorkChangeListener(this);
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (NetworkHelper.isNetworkAvailable(getContext())) {
            mWebView.loadUrl(mUrl);
            hasLoad = true;
        } else {
            showNetworkErrorLayout();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null && hasLoad) {
            mWebView.onResume();
            mWebView.resumeTimers();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null && hasLoad) {
            mWebView.onPause();
            mWebView.pauseTimers();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuSwitchScreenMode = menu.findItem(R.id.web_switch_screen_mode);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean onMenuItemSelected(MenuItem menuItem, int itemId) {
        if (itemId == R.id.web_refresh) {
            mWebView.reload();
        } else if (itemId == R.id.web_copy) {
            if (Utility.copy(getContext(), mUrl)) {
                Snackbar.make(mRefreshLayout, R.string.copy_success, Snackbar.LENGTH_SHORT).show();
            }
        } else if (itemId == R.id.menu_share) {
            ShareHelper.shareText(getContext(), mUrl);
        } else if (itemId == R.id.web_switch_screen_mode) {
            switchScreenConfiguration();
        } else if (itemId == R.id.web_open_with_browser) {
            Utility.openWebKit(getContext(), mUrl);
        }
        return super.onMenuItemSelected(menuItem, itemId);
    }

    @Override
    public void onNetWorkChange(boolean isAvailable, NetworkType oldType, NetworkType newType) {
        if (isAvailable && !hasLoad) {
            restoreLayout();
            mWebView.loadUrl(mUrl);
            hasLoad = true;
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            WebViewHelper.destroyWebView(mWebView);
        }
        NetworkChangeReceiver.getInstance().removeOnNetWorkChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onBackClick() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            switchScreenConfiguration();
            return;
        }
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackClick();
    }

    public void switchScreenConfiguration() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            if (menuSwitchScreenMode != null) {
                menuSwitchScreenMode.setTitle(R.string.menu_web_vertical);
            }
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            if (menuSwitchScreenMode != null) {
                menuSwitchScreenMode.setTitle(R.string.menu_web_horizontal);
            }
        }
    }

    private static final class CustomWebChromeClient extends WebChromeClient {

        private ProgressBar progressBar;

        CustomWebChromeClient(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (progressBar != null) {
                progressBar.setProgress(newProgress);
                if (newProgress >= progressBar.getMax()) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

    }

    private static final class CustomWebViewClient extends WebViewClient {

        private MVPViewHelper mvpViewHelper;

        CustomWebViewClient(MVPViewHelper mvpViewHelper) {
            this.mvpViewHelper = mvpViewHelper;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (errorCode == ERROR_HOST_LOOKUP
                    || errorCode == ERROR_PROXY_AUTHENTICATION
                    || errorCode == ERROR_CONNECT) {
                mvpViewHelper.showNetworkErrorLayout();
            } else if (errorCode == ERROR_TIMEOUT) {
                mvpViewHelper.showNetworkPoorLayout();
            } else if (errorCode == ERROR_UNKNOWN
                    || errorCode == ERROR_FAILED_SSL_HANDSHAKE
                    || errorCode == ERROR_FILE_NOT_FOUND
                    || errorCode == ERROR_BAD_URL
                    || errorCode == ERROR_FILE) {
                mvpViewHelper.showErrorLayout();
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (request.isForMainFrame()) {
                    int statusCode = errorResponse.getStatusCode();
                    if (statusCode == 404) {
                        mvpViewHelper.showErrorLayout();
                    }
                } else {
                    if (!request.getRequestHeaders().containsKey("Referer")) {
                        mvpViewHelper.showErrorLayout();
                    }
                }
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // 接受所有网站的证书，忽略SSL错误，执行访问网页
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (WebViewHelper.handleInterceptUrl(view.getContext(), url)) {
                return true;
            }
            view.loadUrl(url);
            return true;
        }

    }

}
