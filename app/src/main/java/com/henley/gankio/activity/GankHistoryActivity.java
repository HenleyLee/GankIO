package com.henley.gankio.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.henley.gankio.R;
import com.henley.gankio.adapter.GankHistoryAdapter;
import com.henley.gankio.base.BaseActivity;
import com.henley.gankio.contract.GankHistoryContract;
import com.henley.gankio.entity.BaseGank;
import com.henley.gankio.entity.GankDaily;
import com.henley.gankio.gank.GankConfig;
import com.henley.gankio.http.HttpException;
import com.henley.gankio.listener.OnItemClickListener;
import com.henley.gankio.presenter.GankHistoryPresenter;
import com.henley.gankio.utils.NetworkHelper;
import com.henley.gankio.utils.Utility;
import com.henley.gankio.widget.SpaceDividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 每日干货页面
 *
 * @author Henley
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
        return getString(R.string.page_title_hsitory);
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
        BaseGank<GankDaily> gank = mAdapter.getItem(adapterPosition);
        GankDailyActivity.startActivity(getContext(), gank.getResults());
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
    public void handleGankHistoryResult(BaseGank<List<String>> gank) {
        if (gank == null) {
            return;
        }
        historyDates = gank.getResults();
        if (historyDates != null && !historyDates.isEmpty()) {
            pageSize = (int) Math.ceil(historyDates.size() * 1.0f / GankConfig.PAGE_SIZE);
        }
        pageIndex = GankConfig.PAGE_INDEX;
        loadHistoryGankDailyDatas();
    }

    @Override
    public void handleHistoryGankDailyResult(List<BaseGank<GankDaily>> ganks) {
        stopRefreshingOrLoading();
        if (ganks == null || ganks.isEmpty()) {
            mRefreshLayout.setEnableLoadMore(false);
            mRefreshLayout.setNoMoreData(true);
            updateEmptyViewVisibility();
            return;
        }
        if (pageIndex == GankConfig.PAGE_INDEX) {
            mAdapter.refresh(ganks);
        } else {
            mAdapter.addAll(ganks);
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


