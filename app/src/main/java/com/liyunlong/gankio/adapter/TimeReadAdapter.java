package com.liyunlong.gankio.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.liyunlong.gankio.R;
import com.liyunlong.gankio.entity.TimeReadEntity;
import com.liyunlong.gankio.utils.DateHelper;
import com.liyunlong.gankio.utils.ViewHolder;

import java.util.Collection;

/**
 * 闲读数据适配器
 *
 * @author liyunlong
 * @date 2018/7/9 15:18
 */
public class TimeReadAdapter extends CommonAdapter<TimeReadEntity> {

    public TimeReadAdapter(Collection<TimeReadEntity> datas) {
        super(datas);
    }

    @Override
    public int getItemLayoutID() {
        return R.layout.layout_item_time_read;
    }

    @Override
    public void convert(ViewHolder holder, TimeReadEntity data, int position) {
        if (data != null) {
            holder.setText(R.id.time_read_title, data.getTitle());
            holder.setText(R.id.time_read_date, DateHelper.getTimestampString(data.getPublishedTime()));
            String iconUrl = null;
            TimeReadEntity.Site site = data.getSite();
            if (site != null) {
                iconUrl = site.getIcon();
            }
            final ImageView ivIcon = holder.getView(R.id.time_read_icon);
            Glide.with(getContext())
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
