package com.henley.gankio;

import android.app.Application;
import android.content.Context;

import com.henley.gankio.gank.GankConfig;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Application
 *
 * @author Henley
 * @date 2018/7/4 9:53
 */
public class GankApplication extends Application {

    private static final String TAG = "BaseApplication";

    static {
        // 设置启用矢量图片资源向后兼容
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // 设置全局异常处理(处理无法传递的异常)
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Logger.e(TAG, throwable);
            }
        });

        // 设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                MaterialHeader header = new MaterialHeader(context);
                header.setColorSchemeColors(GankConfig.MATERIAL_COLORS);
                return header; // 指定为MaterialHeader
            }
        });
        // 设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setEnableLoadMoreWhenContentNotFull(true);//内容不满一页时候启用加载更多
                BallPulseFooter footer = new BallPulseFooter(context);
                footer.setSpinnerStyle(SpinnerStyle.Translate);//设置为拉伸模式
                footer.setNormalColor(ContextCompat.getColor(context, R.color.colorBallPulse));
                footer.setAnimatingColor(ContextCompat.getColor(context, R.color.colorBallPulse));
                return footer; // 指定为BallPulseFooter
            }
        });
    }

    private static Application sInstance;

    public static Application getApplication() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initLogger();
        registerReceiver();
    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)      // 是否显示线程信息(默认为true)
                .methodCount(1)             // 要显示的方法行数(默认为2)
                .methodOffset(0)            // 隐藏内部方法调用到偏移量(默认为5)
                .tag(GankConfig.TAG)        // 每个日志的全局标记(默认为PRETTY_LOGGER)
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }

    private void registerReceiver() {
        NetworkChangeReceiver.registerReceiver(this);
    }

}
