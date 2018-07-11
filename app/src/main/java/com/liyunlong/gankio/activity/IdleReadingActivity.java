package com.liyunlong.gankio.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.liyunlong.gankio.NetworkChangeReceiver;
import com.liyunlong.gankio.R;
import com.liyunlong.gankio.adapter.TabPagerAdapter;
import com.liyunlong.gankio.base.BaseActivity;
import com.liyunlong.gankio.contract.IdleReadingCategoryContract;
import com.liyunlong.gankio.entity.BaseGank;
import com.liyunlong.gankio.entity.CategoryEntity;
import com.liyunlong.gankio.fragment.IdleReadingFragment;
import com.liyunlong.gankio.gank.GankConfig;
import com.liyunlong.gankio.http.HttpException;
import com.liyunlong.gankio.listener.OnNetWorkChangeListener;
import com.liyunlong.gankio.presenter.IdleReadingCategoryPresenter;
import com.liyunlong.gankio.utils.NetworkHelper;
import com.liyunlong.gankio.utils.NetworkType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 闲读页面
 *
 * @author liyunlong
 * @date 2018/7/9 14:07
 */
public class IdleReadingActivity extends BaseActivity<IdleReadingCategoryPresenter> implements IdleReadingCategoryContract.View, OnNetWorkChangeListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FrameLayout content;
    private int mCurrentPosition;
    private MenuItem menuFilter;
    private FloatingActionButton fabSubmit;
    private List<IdleReadingFragment> mFragments;
    private boolean hasLoadCategory;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, IdleReadingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_idle_reading;
    }

    @Override
    protected int getMenuRes() {
        return R.menu.menu_read;
    }

    @Override
    protected String title() {
        return getString(R.string.page_title_reading);
    }

    @Override
    protected View getContentView() {
        return content;
    }

    @Override
    protected void initViews() {
        SmartRefreshLayout refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);
        setRefreshLayout(refreshLayout);
        content = findViewById(R.id.idle_reading_content);
        mTabLayout = findViewById(R.id.idle_reading_tab_layout);
        mViewPager = findViewById(R.id.idle_reading_view_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentPosition = position;
            }
        });

        fabSubmit = findViewById(R.id.idle_reading_fab);
        fabSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.startActivity(getContext(), GankConfig.GANK_SUBMIT_READ_NAME, GankConfig.GANK_SUBMIT_READ_URL);
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
            getPresenter().getIdleReadingCategory();
            hasLoadCategory = true;
        } else {
            showNetworkErrorLayout();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuFilter = menu.findItem(R.id.read_filter);
        if (menuFilter != null) {
            menuFilter.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected boolean onMenuItemSelected(MenuItem menuItem, int itemId) {
        if (itemId == R.id.read_filter) {
            mFragments.get(mCurrentPosition).showFilterDialog();
        }
        return super.onMenuItemSelected(menuItem, itemId);
    }

    @Override
    public void onNetWorkChange(boolean isAvailable, NetworkType oldType, NetworkType newType) {
        if (isAvailable && !hasLoadCategory) {
            restoreLayout();
            getPresenter().getIdleReadingCategory();
            hasLoadCategory = true;
        }
        if (fabSubmit != null) {
            fabSubmit.setVisibility(isAvailable ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void handleException(HttpException exception) {
        super.handleException(exception);
        if (exception.isNetworkError()) {
            showNetworkErrorLayout();
        } else if (exception.isNetworkPoor()) {
            showNetworkPoorLayout();
        } else {
            showErrorLayout();
        }
    }

    @Override
    public void handleIdleReadingCategoryResult(BaseGank<List<CategoryEntity>> gank) {
        if (gank == null) {
            showEmptyLayout();
            return;
        }
        List<CategoryEntity> categories = gank.getResults();
        if (categories == null || categories.isEmpty()) {
            showEmptyLayout();
            return;
        }
        if (menuFilter != null) {
            menuFilter.setVisible(true);
        }
        int size = categories.size();
        mFragments = new ArrayList<>(size);
        List<String> titles = new ArrayList<>(size);
        for (CategoryEntity category : categories) {
            String categoryName = category.getCategoryCN();
            titles.add(categoryName);
            mFragments.add(IdleReadingFragment.newInstance(category));
            mTabLayout.addTab(mTabLayout.newTab().setText(categoryName));
        }
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE); // 设置TabLayout模式
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER); // 设置内容的显示模式
        mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), titles, mFragments));// 设置适配器
        mTabLayout.setupWithViewPager(mViewPager); // 将TabLayout与ViewPager关联

        content.setVisibility(View.VISIBLE);
        restoreLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkChangeReceiver.getInstance().removeOnNetWorkChangeListener(this);
    }

}
