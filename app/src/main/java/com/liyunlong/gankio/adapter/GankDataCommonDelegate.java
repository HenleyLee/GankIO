package com.liyunlong.gankio.adapter;

import com.liyunlong.gankio.R;
import com.liyunlong.gankio.delegate.ItemViewDelegate;
import com.liyunlong.gankio.entity.GankEntity;
import com.liyunlong.gankio.entity.GankType;
import com.liyunlong.gankio.utils.DateHelper;
import com.liyunlong.gankio.utils.ViewHolder;

/**
 * @author liyunlong
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
