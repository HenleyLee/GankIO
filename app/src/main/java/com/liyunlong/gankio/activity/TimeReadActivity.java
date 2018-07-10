package com.liyunlong.gankio.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.liyunlong.gankio.R;
import com.liyunlong.gankio.adapter.TabPagerAdapter;
import com.liyunlong.gankio.base.BaseActivity;
import com.liyunlong.gankio.contract.TimeReadCategoryContract;
import com.liyunlong.gankio.entity.BaseGank;
import com.liyunlong.gankio.entity.CategoryEntity;
import com.liyunlong.gankio.fragment.TimeReadFragment;
import com.liyunlong.gankio.http.HttpException;
import com.liyunlong.gankio.presenter.TimeReadCategoryPresenter;
import com.liyunlong.gankio.utils.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 闲读页面
 *
 * @author liyunlong
 * @date 2018/7/9 14:07
 */
public class TimeReadActivity extends BaseActivity<TimeReadCategoryPresenter> implements TimeReadCategoryContract.View {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FrameLayout content;
    private int mCurrentPosition;
    private List<TimeReadFragment> mFragments;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, TimeReadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_time_read;
    }

    @Override
    protected int getMenuRes() {
        return R.menu.menu_read;
    }

    @Override
    protected String title() {
        return "闲读";
    }

    @Override
    protected View getContentView() {
        return content;
    }

    @Override
    protected void initViews() {
        content = findViewById(R.id.time_read_content);
        mTabLayout = findViewById(R.id.time_read_tab_layout);
        mViewPager = findViewById(R.id.time_read_view_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentPosition = position;
            }
        });
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (NetworkHelper.isNetworkAvailable(getContext())) {
            getPresenter().getTimeReadCategory();
        } else {
            showNetworkErrorLayout();
        }
    }

    @Override
    protected boolean onMenuItemSelected(MenuItem menuItem, int itemId) {
        if (itemId == R.id.read_filter) {
            mFragments.get(mCurrentPosition).showFilterDialog();
        }
        return super.onMenuItemSelected(menuItem, itemId);
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
    public void handleTimeReadCategoryResult(BaseGank<List<CategoryEntity>> gank) {
        if (gank == null) {
            showEmptyLayout();
            return;
        }
        List<CategoryEntity> categories = gank.getResults();
        if (categories == null || categories.isEmpty()) {
            showEmptyLayout();
            return;
        }
        int size = categories.size();
        mFragments = new ArrayList<>(size);
        List<String> titles = new ArrayList<>(size);
        for (CategoryEntity category : categories) {
            String categoryName = category.getCategoryCN();
            titles.add(categoryName);
            mFragments.add(TimeReadFragment.newInstance(category));
            mTabLayout.addTab(mTabLayout.newTab().setText(categoryName));
        }
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE); // 设置TabLayout模式
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER); // 设置内容的显示模式
        mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), titles, mFragments));// 设置适配器
        mTabLayout.setupWithViewPager(mViewPager); // 将TabLayout与ViewPager关联

        content.setVisibility(View.VISIBLE);
        restoreLayout();
    }

}
