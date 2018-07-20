package com.liyunlong.gankio.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.liyunlong.gankio.NetworkChangeReceiver;
import com.liyunlong.gankio.R;
import com.liyunlong.gankio.activity.WebActivity;
import com.liyunlong.gankio.adapter.IdleReadingAdapter;
import com.liyunlong.gankio.adapter.IdleReadingFilterAdapter;
import com.liyunlong.gankio.base.BaseFragment;
import com.liyunlong.gankio.contract.IdleReadingDetailContract;
import com.liyunlong.gankio.entity.BaseGank;
import com.liyunlong.gankio.entity.CategoryEntity;
import com.liyunlong.gankio.entity.IdleReadingEntity;
import com.liyunlong.gankio.entity.SubCategoryEntity;
import com.liyunlong.gankio.gank.GankConfig;
import com.liyunlong.gankio.http.HttpException;
import com.liyunlong.gankio.listener.OnIdleReadingCategoryCallback;
import com.liyunlong.gankio.listener.OnItemClickListener;
import com.liyunlong.gankio.listener.OnNetWorkChangeListener;
import com.liyunlong.gankio.presenter.IdleReadingDetailPresenter;
import com.liyunlong.gankio.utils.NetworkHelper;
import com.liyunlong.gankio.utils.NetworkType;
import com.liyunlong.gankio.utils.Utility;
import com.liyunlong.gankio.widget.IdleReadingCategoryView;
import com.liyunlong.gankio.widget.SpaceDividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

/**
 * 闲读分类数据Fragment
 *
 * @author liyunlong
 * @date 2018/7/9 14:57
 */
public class IdleReadingFragment extends BaseFragment<IdleReadingDetailPresenter> implements OnRefreshLoadMoreListener, OnItemClickListener, IdleReadingDetailContract.View, OnNetWorkChangeListener, OnIdleReadingCategoryCallback {

    private String categoryId;
    private CategoryEntity category;
    private List<SubCategoryEntity> categories;
    private int pageIndex = GankConfig.PAGE_INDEX;
    private boolean isFirst = true;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private IdleReadingAdapter mAdapter;
    private IdleReadingFilterAdapter mFilterAdapter;
    private AlertDialog mFilterDialog;
    private AlertDialog mCategoryDialog;
    private IdleReadingCategoryView mIdleReadingCategoryView;

    public static IdleReadingFragment newInstance(CategoryEntity category) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(GankConfig.GANK_CATEGORY, category);
        IdleReadingFragment fragment = new IdleReadingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_idle_reading;
    }

    @Override
    protected View getContentView() {
        return mRecyclerView;
    }

    @Override
    protected void handleBundle(@NonNull Bundle bundle) {
        super.handleBundle(bundle);
        category = bundle.getParcelable(GankConfig.GANK_CATEGORY);
    }

    @Override
    protected void initViews(View rootView) {
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        mRefreshLayout.setEnableLoadMore(false);
        mRecyclerView = findViewById(R.id.idle_reading_list);
        mRecyclerView.addItemDecoration(new SpaceDividerItemDecoration(
                Utility.dp2px(getContext(), 8),
                Utility.dp2px(getContext(), 8)
        ));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new IdleReadingAdapter(null);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnIdleReadingCategoryCallback(this);
        mRecyclerView.setAdapter(mAdapter);
        setRefreshLayout(mRefreshLayout);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        NetworkChangeReceiver.getInstance().addOnNetWorkChangeListener(this);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible && isFirst) {
            if (NetworkHelper.isNetworkAvailable(getContext())) {
                if (category == null || TextUtils.isEmpty(category.getCategoryEN())) {
                    showEmptyLayout();
                } else {
                    restoreLayout();
                    mRefreshLayout.autoRefresh();
                }
                isFirst = false;
            } else {
                showNetworkErrorLayout();
            }
        }
    }

    @Override
    public void onNetWorkChange(boolean isAvailable, NetworkType oldType, NetworkType newType) {
        if (isFragmentVisible() && category != null && isAvailable && isFirst) {
            restoreLayout();
            mRefreshLayout.autoRefresh();
            isFirst = false;
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        getPresenter().getIdleReadingData(categoryId, GankConfig.PAGE_SIZE, pageIndex);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        IdleReadingEntity readEntity = mAdapter.getItem(adapterPosition);
        if (readEntity != null) {
            WebActivity.startActivity(getContext(), readEntity.getTitle(), readEntity.getUrl());
        }
    }

    @Override
    public void onIdleReadingCategoryCallback(IdleReadingEntity.Site site) {
        if (site != null && !TextUtils.isEmpty(site.getUrl())) {
            showCategoryDialog(site);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageIndex++;
        loadData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        restoreLayout();
        if (categories == null || categories.isEmpty()) {
            getPresenter().getIdleReadingSubCategory(category.getCategoryEN());
        } else {
            pageIndex = GankConfig.PAGE_INDEX;
            loadData();
        }
    }

    @Override
    public void handleException(HttpException exception) {
        super.handleException(exception);
        stopRefreshingOrLoading();
        if (mAdapter == null || mAdapter.isEmpty()) {
            if (exception.isNetworkError()) {
                showNetworkErrorLayout();
            } else if (exception.isNetworkPoor()) {
                showNetworkPoorLayout();
            } else {
                showErrorLayout();
            }
        } else {
            restoreLayout();
        }
    }

    @Override
    public void handleIdleReadingSubCategoryResult(BaseGank<List<SubCategoryEntity>> gank) {
        if (gank == null) {
            stopRefreshingOrLoading();
            updateEmptyViewVisibility();
            return;
        }
        this.categories = gank.getResults();
        if (categories == null || categories.isEmpty()) {
            stopRefreshingOrLoading();
            updateEmptyViewVisibility();
            return;
        }
        categoryId = categories.get(0).getCategoryId();
        pageIndex = GankConfig.PAGE_INDEX;
        loadData();
    }

    @Override
    public void handleIdleReadingDataResult(BaseGank<List<IdleReadingEntity>> gank) {
        stopRefreshingOrLoading();
        if (gank == null) {
            mRefreshLayout.setEnableLoadMore(false);
            mRefreshLayout.setNoMoreData(true);
            updateEmptyViewVisibility();
            return;
        }
        List<IdleReadingEntity> results = gank.getResults();
        if (pageIndex == GankConfig.PAGE_INDEX) {
            mAdapter.refresh(results);
        } else {
            mAdapter.addAll(results);
        }
        updateEmptyViewVisibility();
        boolean hasNoMoreData = results == null || results.isEmpty();
        mRefreshLayout.setEnableLoadMore(!hasNoMoreData);
        mRefreshLayout.setNoMoreData(hasNoMoreData);
    }

    public void showFilterDialog() {
        if (categories == null || categories.isEmpty()) {
            Snackbar.make(mRefreshLayout, R.string.idle_reading_category_empty, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (mFilterDialog == null) {
            Context context = requireContext();
            mFilterDialog = new AlertDialog.Builder(context)
                    .setTitle(category.getCategoryCN())
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            categoryId = mFilterAdapter.getSelectedCategory();
                            mAdapter.clear();
                            mRefreshLayout.autoRefresh();
                        }
                    })
                    .create();
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setHasFixedSize(true);
            recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mFilterAdapter = new IdleReadingFilterAdapter(categories);
            mFilterAdapter.setSelectedCategory(categoryId);
            mFilterAdapter.setOnItemClickListener(new OnItemClickListener() {
                private int tempPosition;

                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    SubCategoryEntity entity = mFilterAdapter.getItem(position);
                    mFilterAdapter.setSelectedCategory(entity.getCategoryId());
                    mFilterAdapter.notifyItemChanged(tempPosition);
                    mFilterAdapter.notifyItemChanged(position);
                    this.tempPosition = position;
                }
            });
            recyclerView.setAdapter(mFilterAdapter);
            mFilterDialog.setView(recyclerView);
        }
        mFilterAdapter.setSelectedCategory(categoryId);
        mFilterAdapter.notifyDataSetChanged();
        mFilterDialog.show();
    }

    private void showCategoryDialog(final IdleReadingEntity.Site site) {
        if (mCategoryDialog == null) {
            Context context = requireContext();
            mCategoryDialog = new AlertDialog.Builder(context)
                    .setTitle(category.getCategoryCN())
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            WebActivity.startActivity(getContext(), site.getName(), site.getUrl());
                        }
                    })
                    .create();
            mIdleReadingCategoryView = new IdleReadingCategoryView(context);
            mCategoryDialog.setView(mIdleReadingCategoryView);
        }
        mIdleReadingCategoryView.updateSite(site);
        mCategoryDialog.show();
    }

    private void stopRefreshingOrLoading() {
        if (mRefreshLayout != null) {
            if (pageIndex == GankConfig.PAGE_INDEX) {
                mRefreshLayout.finishRefresh();
            } else {
                mRefreshLayout.finishLoadMore();
            }
        }
    }

    private void updateEmptyViewVisibility() {
        if (mAdapter == null || mAdapter.isEmpty()) {
            showEmptyLayout();
        } else {
            restoreLayout();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetworkChangeReceiver.getInstance().removeOnNetWorkChangeListener(this);
    }

}
