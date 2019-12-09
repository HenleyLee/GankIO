package com.henley.gankio.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.henley.gankio.R;
import com.henley.gankio.adapter.GankDailyAdapter;
import com.henley.gankio.base.BaseActivity;
import com.henley.gankio.entity.GankDaily;
import com.henley.gankio.entity.GankEntity;
import com.henley.gankio.entity.GankType;
import com.henley.gankio.gank.GankConfig;
import com.henley.gankio.listener.OnGankDailyItemClickListener;
import com.henley.gankio.mvp.BaseObserver;
import com.henley.gankio.rxjava.transformer.ObservableTransformerAsync;
import com.henley.gankio.utils.DateHelper;
import com.henley.gankio.utils.Utility;
import com.henley.gankio.widget.SpaceDividerItemDecoration;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 每日干货页面
 *
 * @author Henley
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


