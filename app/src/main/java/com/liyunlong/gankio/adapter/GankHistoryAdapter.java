package com.liyunlong.gankio.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.liyunlong.gankio.R;
import com.liyunlong.gankio.entity.BaseGank;
import com.liyunlong.gankio.entity.GankDaily;
import com.liyunlong.gankio.entity.GankEntity;
import com.liyunlong.gankio.gank.GankConfig;
import com.liyunlong.gankio.utils.DateHelper;
import com.liyunlong.gankio.utils.ViewHolder;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author liyunlong
 * @date 2018/7/5 13:32
 */
public class GankHistoryAdapter extends CommonAdapter<BaseGank<GankDaily>> {

    public GankHistoryAdapter(Collection<BaseGank<GankDaily>> datas) {
        super(datas);
    }

    @Override
    public int getItemLayoutID() {
        return R.layout.layout_item_gank_history;
    }

    @Override
    public void convert(ViewHolder holder, BaseGank<GankDaily> data, int position) {
        if (data != null && data.getResults() != null) {
            GankDaily gankDaily = data.getResults();
            ArrayList<GankEntity> welfareData = gankDaily.getWelfareData();
            String welfareUrl = null;
            if (welfareData != null && !welfareData.isEmpty()) {
                welfareUrl = welfareData.get(0).getUrl();
            }
            final ImageView ivPicture = holder.getView(R.id.gank_history_picture);
            Glide.with(getContext())
                    .asBitmap()
                    .load(welfareUrl)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_image_placeholder)
                            .priority(Priority.HIGH)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                    )
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(new BitmapImageViewTarget(ivPicture) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            super.onResourceReady(resource, transition);
                            if (!ivPicture.isShown()) {
                                ivPicture.setVisibility(View.VISIBLE);
                            }
                        }
                    });

            ArrayList<GankEntity> androidData = gankDaily.getAndroidData();
            if (androidData != null && !androidData.isEmpty()) {
                GankEntity gankEntity = androidData.get(0);
                holder.setText(R.id.gank_history_title, gankEntity.getTitle());
                holder.setText(R.id.gank_history_date, DateHelper.date2String(gankEntity.getPublishedTime().getTime(), GankConfig.DISPLAY_DATE_FORMAT));
            }
        }
    }

}
