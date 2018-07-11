package com.liyunlong.gankio.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.liyunlong.gankio.R;
import com.liyunlong.gankio.entity.SubCategoryEntity;
import com.liyunlong.gankio.utils.ViewHolder;

import java.util.Collection;

/**
 * 闲读子分类筛选适配器
 *
 * @author liyunlong
 * @date 2018/7/9 17:05
 */
public class IdleReadingFilterAdapter extends CommonAdapter<SubCategoryEntity> {

    private String selectedCategory;

    public IdleReadingFilterAdapter(Collection<SubCategoryEntity> datas) {
        super(datas);
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    @Override
    public int getItemLayoutID() {
        return R.layout.layout_item_idle_reading_filter;
    }

    @Override
    public void convert(ViewHolder holder, SubCategoryEntity data, int position) {
        holder.setText(R.id.filter_name, data.getTitle());
        holder.setChecked(R.id.filter_check, TextUtils.equals(selectedCategory, data.getCategoryId()));
        final ImageView ivIcon = holder.getView(R.id.filter_icon);
        Glide.with(getContext())
                .asBitmap()
                .load(data.getIcon())
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
