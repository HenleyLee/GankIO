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

import com.liyunlong.gankio.R;
import com.liyunlong.gankio.activity.WebActivity;
import com.liyunlong.gankio.adapter.TimeReadAdapter;
import com.liyunlong.gankio.adapter.TimeReadFilterAdapter;
import com.liyunlong.gankio.base.BaseFragment;
import com.liyunlong.gankio.contract.TimeReadDetailContract;
import com.liyunlong.gankio.entity.BaseGank;
import com.liyunlong.gankio.entity.CategoryEntity;
import com.liyunlong.gankio.entity.SubCategoryEntity;
import com.liyunlong.gankio.entity.TimeReadEntity;
import com.liyunlong.gankio.gank.GankConfig;
import com.liyunlong.gankio.http.HttpException;
import com.liyunlong.gankio.listener.OnItemClickListener;
import com.liyunlong.gankio.presenter.TimeReadDetailPresenter;
import com.liyunlong.gankio.utils.NetworkHelper;
import com.liyunlong.gankio.utils.Utility;
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
public class TimeReadFragment extends BaseFragment<TimeReadDetailPresenter> implements OnRefreshLoadMoreListener, OnItemClickListener, TimeReadDetailContract.View {

    private String categoryId;
    private CategoryEntity category;
    private List<SubCategoryEntity> categories;
    private int pageIndex = GankConfig.PAGE_INDEX;
    private boolean isFirst = true;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private TimeReadAdapter mAdapter;
    private TimeReadFilterAdapter mFilterAdapter;
    private AlertDialog mFilterDialog;

    public static TimeReadFragment newInstance(CategoryEntity category) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(GankConfig.GANK_CATEGORY, category);
        TimeReadFragment fragment = new TimeReadFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_time_read;
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
        mRecyclerView = findViewById(R.id.time_read_list);
        mRecyclerView.addItemDecoration(new SpaceDividerItemDecoration(
                Utility.dp2px(getContext(), 8),
                Utility.dp2px(getContext(), 8)
        ));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TimeReadAdapter(null);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        setRefreshLayout(mRefreshLayout);
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
    protected void loadData() {
        super.loadData();
        getPresenter().getTimeReadData(categoryId, GankConfig.PAGE_SIZE, pageIndex);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        TimeReadEntity readEntity = mAdapter.getItem(adapterPosition);
        if (readEntity != null) {
            WebActivity.startActivity(getContext(), readEntity.getTitle(), readEntity.getUrl());
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageIndex++;
        loadData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (categories == null || categories.isEmpty()) {
            getPresenter().getTimeReadSubCategory(category.getCategoryEN());
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
    public void handleTimeReadSubCategoryResult(BaseGank<List<SubCategoryEntity>> gank) {
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
    public void handleTimeReadDataResult(BaseGank<List<TimeReadEntity>> gank) {
        stopRefreshingOrLoading();
        if (gank == null) {
            mRefreshLayout.setEnableLoadMore(false);
            mRefreshLayout.setNoMoreData(true);
            updateEmptyViewVisibility();
            return;
        }
        List<TimeReadEntity> results = gank.getResults();
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
            Snackbar.make(mRecyclerView, R.string.time_read_category_empty, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (mFilterDialog == null) {
            Context context = requireContext();
            mFilterDialog = new AlertDialog.Builder(context)
                    .setTitle("请选择")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
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
            mFilterAdapter = new TimeReadFilterAdapter(categories);
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


}
