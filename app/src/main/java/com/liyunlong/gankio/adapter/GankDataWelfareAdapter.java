package com.liyunlong.gankio.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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
import com.liyunlong.gankio.entity.GankEntity;
import com.liyunlong.gankio.utils.Utility;
import com.liyunlong.gankio.utils.ViewHolder;

import java.util.Collection;

/**
 * 干货分类数据Adapter(福利)
 *
 * @author liyunlong
 * @date 2018/7/4 10:48
 */
public class GankDataWelfareAdapter extends CommonAdapter<GankEntity> {

    public GankDataWelfareAdapter(Collection<GankEntity> datas) {
        super(datas);
    }

    @Override
    public int getItemLayoutID() {
        return R.layout.layout_item_gank_data_welfare;
    }

    @Override
    public void convert(ViewHolder holder, GankEntity data, int position) {
        if (data != null) {
            final ImageView ivPicture = holder.getView(R.id.gank_data_picture);
            int itemWidth = (Utility.getScreenWidth(getContext()) - 4 * Utility.dp2px(getContext(), 3)) / 2;
            Glide.with(getContext())
                    .asBitmap()
                    .load(data.getUrl())
                    .apply(new RequestOptions()
                            .centerInside()
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_image_placeholder)
                            .priority(Priority.HIGH)
                            .override(itemWidth, Integer.MAX_VALUE)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                    )
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(new BitmapImageViewTarget(ivPicture) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            super.onResourceReady(resource, transition);
                            CardView.LayoutParams layoutParams = (CardView.LayoutParams) ivPicture.getLayoutParams();
                            layoutParams.width = resource.getWidth();
                            layoutParams.height = resource.getHeight();
                            ivPicture.setLayoutParams(layoutParams);
                            if (!ivPicture.isShown()) {
                                ivPicture.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }

}
