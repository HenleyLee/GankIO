package com.henley.gankio.activity;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.henley.gankio.GlideApp;
import com.henley.gankio.R;
import com.henley.gankio.base.BaseActivity;
import com.henley.gankio.contract.GankDataContract;
import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.GankEntity;
import com.henley.gankio.entity.GankType;
import com.henley.gankio.gank.GankConfig;
import com.henley.gankio.presenter.GankDataPresenter;
import com.henley.gankio.utils.NetworkHelper;
import com.henley.gankio.utils.SafetyHandler;
import com.henley.gankio.utils.Utility;

import java.util.List;

/**
 * 启动页面
 *
 * @author Henley
 * @date 2018/7/13 15:58
 */
public class SplashActivity extends BaseActivity<GankDataPresenter> implements GankDataContract.View, SafetyHandler.Delegate {

    private ImageView ivSplash;
    private SafetyHandler mHandler = SafetyHandler.create(this);

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected String title() {
        return null;
    }

    @Override
    protected boolean hideToolBar() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉Activity上面的状态栏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);// 去掉虚拟按键全屏显示
    }

    @Override
    protected void initViews() {
        ivSplash = findViewById(R.id.splash_image);
        mHandler = SafetyHandler.create(this);
        mHandler.sendEmptyMessageDelayed(GankConfig.MESSAGE_WHAT_SPLASH, GankConfig.MESSAGE_WAIT_SPLASH);
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (NetworkHelper.isNetworkAvailable(getContext())) {
            getPresenter().getGankData(GankType.Welfare.getName(), 1, 1);
        } else {
            String imageUrl = Utility.getPreference(getContext()).getString(GankConfig.GANK_SPLASH_IMAGE, null);
            loadSplashImage(imageUrl);
        }
    }

    @Override
    public void handleGankDataResult(BaseGank<List<GankEntity>> gank) {
        if (gank == null || gank.getResults() == null || gank.getResults().isEmpty()) {
            return;
        }
        GankEntity gankEntity = gank.getResults().get(0);
        loadSplashImage(gankEntity.getUrl());
    }

    private void loadSplashImage(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        GlideApp.with(getContext())
                .asBitmap()
                .load(imageUrl)
                .apply(new RequestOptions()
                        .centerCrop()
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                )
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(ivSplash); // 加载启动图片
        Utility.getPreference(getContext())
                .edit()
                .putString(GankConfig.GANK_SPLASH_IMAGE, imageUrl)
                .apply(); // 保存启动图片链接
    }

    @Override
    public void handleMessage(int what, Message msg) {
        if (what == GankConfig.MESSAGE_WHAT_SPLASH) {
            MainActivity.startActivity(getContext());
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.clear();
            mHandler = null;
        }
    }

}
