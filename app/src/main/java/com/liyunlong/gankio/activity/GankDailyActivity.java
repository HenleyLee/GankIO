package com.liyunlong.gankio.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.liyunlong.gankio.R;
import com.liyunlong.gankio.adapter.GankDailyAdapter;
import com.liyunlong.gankio.base.BaseActivity;
import com.liyunlong.gankio.entity.GankDaily;
import com.liyunlong.gankio.entity.GankEntity;
import com.liyunlong.gankio.entity.GankType;
import com.liyunlong.gankio.gank.GankConfig;
import com.liyunlong.gankio.listener.OnGankDailyItemClickListener;
import com.liyunlong.gankio.mvp.BaseObserver;
import com.liyunlong.gankio.rxjava.transformer.ObservableTransformerAsync;
import com.liyunlong.gankio.utils.DateHelper;
import com.liyunlong.gankio.utils.Utility;
import com.liyunlong.gankio.widget.SpaceDividerItemDecoration;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 每日干货页面
 *
 * @author liyunlong
 * @date 2018/7/5 14:07
 */
public class GankDailyActivity extends BaseActivity implements OnGankDailyItemClickListener {

    private GankDaily gankDaily;
    private RecyclerView mRecyclerView;
    private GankDailyAdapter mAdapter;

    public static void startActivity(Context context, GankDaily gankDaily) {
        Intent intent = new Intent(context, GankDailyActivity.class);
        intent.putExtra(GankConfig.GANK_DAILY, gankDaily);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_gank_daily;
    }

    @Override
    protected String title() {
        return null;
    }

    @Override
    protected void handleIntent(@NonNull Intent intent) {
        super.handleIntent(intent);
        gankDaily = intent.getParcelableExtra(GankConfig.GANK_DAILY);
    }

    @Override
    protected View getContentView() {
        return mRecyclerView;
    }

    @Override
    protected void initViews() {
        mRecyclerView = findViewById(R.id.gank_daily_list);
        mRecyclerView.addItemDecoration(new SpaceDividerItemDecoration(
                Utility.dp2px(getContext(), 8),
                Utility.dp2px(getContext(), 8)
        ));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new GankDailyAdapter(null);
        mAdapter.setOnGankDailyItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void loadData() {
        super.loadData();
        handleGankDailyResult();
    }

    public void handleGankDailyResult() {
        if (gankDaily == null) {
            return;
        }
        Observable.just(gankDaily)
                .map(new Function<GankDaily, List<List<GankEntity>>>() {
                    @Override
                    public List<List<GankEntity>> apply(GankDaily dailyResults) throws Exception {
                        return dailyResults.getDailyResults();
                    }
                })
                .compose(new ObservableTransformerAsync<List<List<GankEntity>>>())
                .subscribe(new BaseObserver<List<List<GankEntity>>>() {
                    @Override
                    public void onNext(List<List<GankEntity>> results) {
                        GankEntity gankEntity = results.get(0).get(0);
                        String title = DateHelper.date2String(gankEntity.getPublishedTime().getTime(), GankConfig.DISPLAY_DATE_FORMAT);
                        getToolBar().setTitle(title);
                        mAdapter.refresh(results);
                    }
                });
    }


    @Override
    public void onGankDailyItemClick(View view, GankEntity gankEntity) {
        if (gankEntity == null) {
            return;
        }
        String type = gankEntity.getType();
        if (TextUtils.equals(type, GankType.Welfare.getName())) {
            String title = DateHelper.date2String(gankEntity.getPublishedTime().getTime(), GankConfig.DISPLAY_DATE_FORMAT);
            ImageView ivPicture = view.findViewById(R.id.gank_daily_picture);
            PictureActivity.startActivity(getContext(), title, gankEntity.getUrl(), ivPicture);
        } else {
            WebActivity.startActivity(getContext(), gankEntity.getTitle(), gankEntity.getUrl());
        }
    }

}


