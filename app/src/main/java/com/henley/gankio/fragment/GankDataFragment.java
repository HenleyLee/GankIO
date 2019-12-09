package com.henley.gankio.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.henley.gankio.NetworkChangeReceiver;
import com.henley.gankio.R;
import com.henley.gankio.activity.PictureActivity;
import com.henley.gankio.activity.WebActivity;
import com.henley.gankio.adapter.GankDataAllDelegate;
import com.henley.gankio.adapter.GankDataCommonDelegate;
import com.henley.gankio.adapter.GankDataWelfareDelegate;
import com.henley.gankio.adapter.MultiItemTypeAdapter;
import com.henley.gankio.base.BaseFragment;
import com.henley.gankio.contract.GankDataContract;
import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.GankEntity;
import com.henley.gankio.entity.GankType;
import com.henley.gankio.gank.GankConfig;
import com.henley.gankio.http.HttpException;
import com.henley.gankio.listener.OnItemClickListener;
import com.henley.gankio.listener.OnNetWorkChangeListener;
import com.henley.gankio.presenter.GankDataPresenter;
import com.henley.gankio.utils.DateHelper;
import com.henley.gankio.utils.NetworkHelper;
import com.henley.gankio.utils.NetworkType;
import com.henley.gankio.utils.Utility;
import com.henley.gankio.widget.SpaceDividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 干货分类数据Fragment
 *
 * @author Henley
 * @date 2018/7/3 16:30
 */
public class GankDataFragment extends BaseFragment<GankDataPresenter> implements OnRefreshLoadMoreListener, OnItemClickListener, GankDataContract.View, OnNetWorkChangeListener {

    private int pageIndex = GankConfig.PAGE_INDEX;
    private GankType gankType;
    private boolean isFirst = true;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private MultiItemTypeAdapter<GankEntity> mAdapter;

    public static GankDataFragment newInstance(String gankType) {
        Bundle bundle = new Bundle();
        bundle.putString(GankConfig.GANK_TYPE, gankType);
        GankDataFragment fragment = new GankDataFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_gank_data;
    }

    @Override
    protected View getContentView() {
        return mRecyclerView;
    }

    @Override
    protected void handleBundle(@NonNull Bundle bundle) {
        super.handleBundle(bundle);
        String gankTypeName = bundle.getString(GankConfig.GANK_TYPE, GankType.All.name());
        gankType = GankType.valueOf(gankTypeName);
    }

    @Override
    protected void initViews(View rootView) {
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        mRefreshLayout.setEnableLoadMore(false);
        mRecyclerView = findViewById(R.id.gank_data_list);
        if (gankType != GankType.Welfare) {
            mRecyclerView.addItemDecoration(new SpaceDividerItemDecoration(
                    Utility.dp2px(getContext(), 8),
                    Utility.dp2px(getContext(), 8)
            ));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mRecyclerView.addItemDecoration(new SpaceDividerItemDecoration(
                    Utility.dp2px(getContext(), 6),
                    Utility.dp2px(getContext(), 3)
            ));
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                    2, StaggeredGridLayoutManager.VERTICAL
            ));
        }
        mAdapter = new MultiItemTypeAdapter<>(null);
        mAdapter.addItemViewDelegate(new GankDataAllDelegate(gankType));
        mAdapter.addItemViewDelegate(new GankDataWelfareDelegate(gankType));
        mAdapter.addItemViewDelegate(new GankDataCommonDelegate(gankType));
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        setRefreshLayout(mRefreshLayout);
    }

    @Override
    public void onResume() {
        super.onResume();
        onFragmentVisibleChange(isViewCreated() && isResumed());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        NetworkChangeReceiver.getInstance().addOnNetWorkChangeListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        NetworkChangeReceiver.getInstance().removeOnNetWorkChangeListener(this);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible && isFirst) {
            if (NetworkHelper.isNetworkAvailable(getContext())) {
                restoreLayout();
                mRefreshLayout.autoRefresh();
                isFirst = false;
            } else {
                showNetworkErrorLayout();
            }
        }
    }

    @Override
    public void onNetWorkChange(boolean isAvailable, NetworkType oldType, NetworkType newType) {
        if (isFragmentVisible() && isAvailable && isFirst) {
            restoreLayout();
            mRefreshLayout.autoRefresh();
            isFirst = false;
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        getPresenter().getGankData(gankType.getName(), GankConfig.PAGE_SIZE, pageIndex);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        GankEntity data = mAdapter.getItem(adapterPosition);
        if (gankType == GankType.Welfare || TextUtils.equals(data.getType(), GankType.Welfare.getName())) {
            String title = DateHelper.date2String(data.getPublishedTime().getTime(), GankConfig.DISPLAY_DATE_FORMAT);
            ImageView ivPicture = view.findViewById(R.id.gank_data_picture);
            PictureActivity.startActivity(getActivity(), title, data.getUrl(), ivPicture);
        } else {
            WebActivity.startActivity(getContext(), data.getTitle(), data.getUrl());
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageIndex = GankConfig.PAGE_INDEX;
        loadData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageIndex++;
        loadData();
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
    public void handleGankDataResult(BaseGank<List<GankEntity>> gank) {
        stopRefreshingOrLoading();
        if (gank == null) {
            mRefreshLayout.setEnableLoadMore(false);
            mRefreshLayout.setNoMoreData(true);
            updateEmptyViewVisibility();
            return;
        }
        List<GankEntity> results = gank.getResults();
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
