package com.henley.gankio.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.henley.gankio.GlideApp;
import com.henley.gankio.R;
import com.henley.gankio.entity.IdleReadingEntity;
import com.henley.gankio.listener.OnIdleReadingCategoryCallback;
import com.henley.gankio.utils.DateHelper;
import com.henley.gankio.utils.ViewHolder;

import java.util.Collection;

/**
 * 闲读数据适配器
 *
 * @author Henley
 * @date 2018/7/9 15:18
 */
public class IdleReadingAdapter extends CommonAdapter<IdleReadingEntity> {

    private OnIdleReadingCategoryCallback mCallback;

    public IdleReadingAdapter(Collection<IdleReadingEntity> datas) {
        super(datas);
    }

    public void setOnIdleReadingCategoryCallback(OnIdleReadingCategoryCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public int getItemLayoutID() {
        return R.layout.layout_item_idle_reading;
    }

    @Override
    public void convert(ViewHolder holder, IdleReadingEntity data, int position) {
        if (data != null) {
            holder.setText(R.id.idle_reading_title, data.getTitle());
            holder.setText(R.id.idle_reading_date, DateHelper.getTimestampString(data.getPublishedTime()));
            String iconUrl = null;
            final IdleReadingEntity.Site site = data.getSite();
            if (site != null) {
                iconUrl = site.getIcon();
            }
            final ImageView ivIcon = holder.getView(R.id.idle_reading_icon);
            ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onIdleReadingCategoryCallback(site);
                    }
                }
            });
            GlideApp.with(getContext())
                    .asBitmap()
                    .load(iconUrl)
                    .apply(new RequestOptions()
                            .circleCrop()
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_image_placeholder)
                            .priority(Priority.HIGH)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                    )
                    .into(new BitmapImageViewTarget(ivIcon));
        }
    }

}
