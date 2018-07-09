package com.liyunlong.gankio.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.liyunlong.gankio.R;
import com.liyunlong.gankio.base.BaseActivity;
import com.liyunlong.gankio.entity.GankType;
import com.liyunlong.gankio.fragment.GankDataFragment;
import com.liyunlong.gankio.gank.GankConfig;

import java.util.HashMap;

/**
 * 主界面
 *
 * @author liyunlong
 * @date 2018/7/3 15:07
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolBar;
    private DrawerLayout drawer;
    private HashMap<GankType, GankDataFragment> fragments = new HashMap<>();

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
    }

    @Override
    protected int getMenuRes() {
        return R.menu.menu_main;
    }

    @Override
    protected boolean onMenuItemSelected(MenuItem menuItem, int itemId) {
        if (itemId == R.id.main_history) {
            GankHistoryActivity.startActivity(getContext());
        } else if (itemId == R.id.main_gankio) {
            WebActivity.startActivity(getContext(), GankConfig.GANK_HOME_NAME, GankConfig.GANK_HOME_URL);
        } else if (itemId == R.id.main_github) {
            WebActivity.startActivity(getContext(), GankConfig.GIHUB_TRENDING_NAME, GankConfig.GIHUB_TRENDING_URL);
        }
        return super.onMenuItemSelected(menuItem, itemId);
    }

    @Override
    public void onBackClick() {
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackClick();
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
