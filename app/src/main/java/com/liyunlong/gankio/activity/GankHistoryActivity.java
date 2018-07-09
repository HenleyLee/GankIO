package com.liyunlong.gankio.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.liyunlong.gankio.R;
import com.liyunlong.gankio.adapter.GankHistoryAdapter;
import com.liyunlong.gankio.base.BaseActivity;
import com.liyunlong.gankio.contract.GankHistoryContract;
import com.liyunlong.gankio.entity.GankDaily;
import com.liyunlong.gankio.entity.GankHistory;
import com.liyunlong.gankio.gank.GankConfig;
import com.liyunlong.gankio.http.HttpException;
import com.liyunlong.gankio.listener.OnItemClickListener;
import com.liyunlong.gankio.presenter.GankHistoryPresenter;
import com.liyunlong.gankio.utils.NetworkHelper;
import com.liyunlong.gankio.utils.Utility;
import com.liyunlong.gankio.widget.SpaceDividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

/**
 * 每日干货页面
 *
 * @author liyunlong
 * @date 2018/7/4 18:07
 */
public class GankHistoryActivity extends BaseActivity<GankHistoryPresenter> implements OnRefreshLoadMoreListener, OnItemClickListener, GankHistoryContract.View {

    private int pageIndex = GankConfig.PAGE_INDEX;
    private int pageSize;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private GankHistoryAdapter mAdapter;
    private List<String> historyDates;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, GankHistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_gank_history;
    }

    @Override
    protected String title() {
        return "每日力推";
    }

    @Override
    protected View getContentView() {
        return mRecyclerView;
    }

    @Override
    protected void initViews() {
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        mRefreshLayout.setEnableLoadMore(false);
        mRecyclerView = findViewById(R.id.gank_history_list);
        mRecyclerView.addItemDecoration(new SpaceDividerItemDecoration(
                Utility.dp2px(getContext(), 6),
                Utility.dp2px(getContext(), 3)
        ));
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL
        ));
        mAdapter = new GankHistoryAdapter(null);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        setRefreshLayout(mRefreshLayout);

    }

    @Override
    protected void loadData() {
        super.loadData();
        if (NetworkHelper.isNetworkAvailable(getContext())) {
            mRefreshLayout.autoRefresh();
        } else {
            showNetworkErrorLayout();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (historyDates == null || historyDates.isEmpty()) {
            getPresenter().getGankHistory();
        } else {
            pageIndex = GankConfig.PAGE_INDEX;
            loadHistoryGankDailyDatas();
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageIndex++;
        loadHistoryGankDailyDatas();
    }

    private void loadHistoryGankDailyDatas() {
        int fromIndex = (pageIndex - 1) * GankConfig.PAGE_SIZE;
        int toIndex;
        if (pageIndex < pageSize) {
            toIndex = pageIndex * GankConfig.PAGE_SIZE;
        } else {
            toIndex = historyDates.size() - 1;
        }
        getPresenter().getHistoryGankDaily(historyDates.subList(fromIndex, toIndex));
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        GankDaily gankDaily = mAdapter.getItem(adapterPosition);
        GankDailyActivity.startActivity(getContext(), gankDaily);
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
    public void handleGankHistoryResult(GankHistory gankHistory) {
        if (gankHistory == null) {
            return;
        }
        historyDates = gankHistory.getResults();
        if (historyDates != null && !historyDates.isEmpty()) {
            pageSize = (int) Math.ceil(historyDates.size() * 1.0f / GankConfig.PAGE_SIZE);
        }
        pageIndex = GankConfig.PAGE_INDEX;
        loadHistoryGankDailyDatas();
    }

    @Override
    public void handleHistoryGankDailyResult(List<GankDaily> gankDailies) {
        stopRefreshingOrLoading();
        if (gankDailies == null || gankDailies.isEmpty()) {
            mRefreshLayout.setEnableLoadMore(false);
            mRefreshLayout.setNoMoreData(true);
            updateEmptyViewVisibility();
            return;
        }
        if (pageIndex == GankConfig.PAGE_INDEX) {
            mAdapter.refresh(gankDailies);
        } else {
            mAdapter.addAll(gankDailies);
        }
        updateEmptyViewVisibility();
        boolean hasNoMoreData = pageIndex >= pageSize;
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

    private void updateEmptyViewVisibility(){
        if (mAdapter == null || mAdapter.isEmpty()) {
            showEmptyLayout();
        } else {
            restoreLayout();
        }
    }

}


