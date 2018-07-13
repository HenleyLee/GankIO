package com.liyunlong.gankio.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.liyunlong.gankio.NetworkChangeReceiver;
import com.liyunlong.gankio.R;
import com.liyunlong.gankio.base.BaseActivity;
import com.liyunlong.gankio.entity.GankType;
import com.liyunlong.gankio.fragment.GankDataFragment;
import com.liyunlong.gankio.gank.GankConfig;
import com.liyunlong.gankio.listener.OnNetWorkChangeListener;
import com.liyunlong.gankio.utils.AnimationHelper;
import com.liyunlong.gankio.utils.NetworkHelper;
import com.liyunlong.gankio.utils.NetworkType;

import java.util.HashMap;

/**
 * 主界面
 *
 * @author liyunlong
 * @date 2018/7/3 15:07
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnNetWorkChangeListener {

    private Toolbar toolBar;
    private DrawerLayout drawer;
    private FloatingActionButton fabSubscribe;
    private HashMap<GankType, GankDataFragment> fragments = new HashMap<>();
    private long mExitStartTime = 0;

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
            fabSubscribe.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AnimationHelper.showFloatingActionButton(fabSubscribe);
                }
            }, 1000);
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
    public void onNetWorkChange(boolean isAvailable, NetworkType oldType, NetworkType newType) {
        if (fabSubscribe != null) {
            if (isAvailable) {
                AnimationHelper.showFloatingActionButton(fabSubscribe);
            } else {
                AnimationHelper.hideFloatingActionButton(fabSubscribe);
            }
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        GankType gankType = GankConfig.getGankType(itemId);
        updateCurrentFragment(gankType);
        if (toolBar != null) {
            if (itemId == R.id.navigation_all) {
                toolBar.setTitle(getString(R.string.gank_io));
            } else {
                toolBar.setTitle(item.getTitle());
            }
        }
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    /**
     * 更新当前Fragment
     *
     * @param gankType
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

}
