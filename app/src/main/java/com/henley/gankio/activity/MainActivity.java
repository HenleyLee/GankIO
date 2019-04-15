package com.henley.gankio.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.henley.gankio.GlideApp;
import com.henley.gankio.NetworkChangeReceiver;
import com.henley.gankio.R;
import com.henley.gankio.base.BaseActivity;
import com.henley.gankio.entity.GankType;
import com.henley.gankio.fragment.GankDataFragment;
import com.henley.gankio.gank.GankConfig;
import com.henley.gankio.listener.OnNetWorkChangeListener;
import com.henley.gankio.utils.AnimationHelper;
import com.henley.gankio.utils.NetworkHelper;
import com.henley.gankio.utils.NetworkType;
import com.henley.gankio.utils.ShareHelper;
import com.henley.gankio.utils.Utility;

import java.util.HashMap;

/**
 * 主界面
 *
 * @author Henley
 * @date 2018/7/3 15:07
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnNetWorkChangeListener {

    private Toolbar toolBar;
    private DrawerLayout drawer;
    private FloatingActionButton fabSubscribe;
    private HashMap<GankType, GankDataFragment> fragments = new HashMap<>();
    private long mExitStartTime = 0;
    private ImageView ivHeaderIcon;
    private boolean hasLoadHeaderIcon;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String title() {
        return getString(R.string.gank_io);
    }

    @Override
    protected boolean hideToolBar() {
        return true;
    }

    @Override
    protected void initViews() {
        toolBar = getToolBar();
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getContext(), drawer, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        ivHeaderIcon = headerView.findViewById(R.id.navigation_header_icon);

        // 默认选中第一个菜单
        navigationView.setCheckedItem(R.id.navigation_all);
        updateCurrentFragment(GankType.All);

        fabSubscribe = findViewById(R.id.main_fab);
        fabSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.startActivity(getContext(), GankConfig.GANK_SUBSCRIBE_NAME, GankConfig.GANK_SUBSCRIBE_URL);
            }
        });
        if (NetworkHelper.isNetworkAvailable(getContext())) {
            loadHeaderIcon(); // 加载侧滑菜单顶部图标
            fabSubscribe.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AnimationHelper.showFloatingActionButton(fabSubscribe);
                }
            }, 1500);
        }
    }

    @Override
    protected int getMenuRes() {
        return R.menu.menu_main;
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        NetworkChangeReceiver.getInstance().addOnNetWorkChangeListener(this);
    }

    @Override
    protected boolean onMenuItemSelected(MenuItem menuItem, int itemId) {
        if (itemId == R.id.main_read) {
            IdleReadingActivity.startActivity(getContext());
        } else if (itemId == R.id.main_history) {
            GankHistoryActivity.startActivity(getContext());
        } else if (itemId == R.id.main_gankio) {
            WebActivity.startActivity(getContext(), GankConfig.GANK_HOME_NAME, GankConfig.GANK_HOME_URL);
        } else if (itemId == R.id.main_github) {
            WebActivity.startActivity(getContext(), GankConfig.GIHUB_TRENDING_NAME, GankConfig.GIHUB_TRENDING_URL);
        }
        return super.onMenuItemSelected(menuItem, itemId);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.navigation_all:           // 全部
            case R.id.navigation_welfare:       // 福利
            case R.id.navigation_android:       // Android
            case R.id.navigation_ios:           // IOS
            case R.id.navigation_js:            // 前端
            case R.id.navigation_video:         // 休息视频
            case R.id.navigation_resource:      // 拓展资源
            case R.id.navigation_app:           // APP
            case R.id.navigation_recommend:     // 瞎推荐
                switchGankType(item, itemId);
                break;
            case R.id.navigation_share:         // 分享
                ShareHelper.shareText(getContext(), getString(R.string.share_message));
                break;
            case R.id.navigation_about:         // 关于
                AboutUsActivity.startActivity(getContext());
                break;
            default:
                break;
        }
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onNetWorkChange(boolean isAvailable, NetworkType oldType, NetworkType newType) {
        if (fabSubscribe != null) {
            if (isAvailable) {
                AnimationHelper.showFloatingActionButton(fabSubscribe);
            } else {
                AnimationHelper.hideFloatingActionButton(fabSubscribe);
            }
        }
        if (isAvailable && !hasLoadHeaderIcon) {
            loadHeaderIcon(); // 加载侧滑菜单顶部图标
        }
    }

    /**
     * 切换干货类型
     */
    private void switchGankType(@NonNull MenuItem item, int itemId) {
        GankType gankType = GankConfig.getGankType(itemId);
        updateCurrentFragment(gankType);
        if (toolBar != null) {
            if (itemId == R.id.navigation_all) {
                toolBar.setTitle(getString(R.string.gank_io));
            } else {
                toolBar.setTitle(item.getTitle());
            }
        }
    }

    /**
     * 根据GankType更新当前Fragment
     */
    private void updateCurrentFragment(GankType gankType) {
        GankDataFragment gankDataFragment = fragments.get(gankType);
        if (gankDataFragment == null) {
            gankDataFragment = GankDataFragment.newInstance(gankType.name());
            fragments.put(gankType, gankDataFragment);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, gankDataFragment, gankType.getName())
                .commitAllowingStateLoss();
    }

    /**
     * 加载侧滑菜单顶部图标
     */
    private void loadHeaderIcon() {
        String imageUrl = Utility.getPreference(getContext()).getString(GankConfig.GANK_SPLASH_IMAGE, null);
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        GlideApp.with(getContext())
                .asBitmap()
                .load(imageUrl)
                .apply(new RequestOptions()
                        .circleCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                )
                .into(new BitmapImageViewTarget(ivHeaderIcon) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);
                        hasLoadHeaderIcon = true;
                    }
                }); // 加载侧滑菜单顶部图标
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkChangeReceiver.getInstance().removeOnNetWorkChangeListener(this);
    }

    @Override
    public void onBackClick() {
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - mExitStartTime > GankConfig.EXIT_WAIT_TIME) {
                mExitStartTime = System.currentTimeMillis();
                Snackbar.make(drawer, R.string.exit_app_hint, Snackbar.LENGTH_SHORT).show();
            } else {
                super.onBackClick();
            }
        }
    }

}
