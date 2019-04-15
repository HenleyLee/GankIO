package com.henley.gankio.adapter;

import com.henley.gankio.R;
import com.henley.gankio.delegate.ItemViewDelegate;
import com.henley.gankio.entity.GankEntity;
import com.henley.gankio.entity.GankType;
import com.henley.gankio.utils.DateHelper;
import com.henley.gankio.utils.ViewHolder;

/**
 * @author Henley
 * @date 2018/7/12 13:37
 */
public class GankDataCommonDelegate implements ItemViewDelegate<GankEntity> {

    private GankType gankType;

    public GankDataCommonDelegate(GankType gankType) {
        this.gankType = gankType;
    }

    @Override
    public int getItemLayoutID() {
        return R.layout.layout_item_gank_data_common;
    }

    @Override
    public boolean isForViewType(GankEntity data, int position) {
        return gankType != GankType.All && gankType != GankType.Welfare;
    }

    @Override
    public void convert(ViewHolder holder, GankEntity data, int position) {
        if (data != null) {
            holder.setText(R.id.gank_data_title, data.getTitle());
            holder.setText(R.id.gank_data_author, data.getAuthor());
            holder.setText(R.id.gank_data_date, DateHelper.getTimestampString(data.getPublishedTime()));
        }
    }

}
