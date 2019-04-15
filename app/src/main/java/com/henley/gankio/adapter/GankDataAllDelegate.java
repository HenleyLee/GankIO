package com.henley.gankio.adapter;

import android.text.TextUtils;

import com.henley.gankio.R;
import com.henley.gankio.delegate.ItemViewDelegate;
import com.henley.gankio.entity.GankEntity;
import com.henley.gankio.entity.GankType;
import com.henley.gankio.gank.GankConfig;
import com.henley.gankio.utils.DateHelper;
import com.henley.gankio.utils.ViewHolder;

/**
 * @author Henley
 * @date 2018/7/12 13:38
 */
public class GankDataAllDelegate implements ItemViewDelegate<GankEntity> {

    private GankType gankType;

    public GankDataAllDelegate(GankType gankType) {
        this.gankType = gankType;
    }

    @Override
    public int getItemLayoutID() {
        return R.layout.layout_item_gank_data_all;
    }

    @Override
    public boolean isForViewType(GankEntity data, int position) {
        return gankType == GankType.All;
    }

    @Override
    public void convert(ViewHolder holder, GankEntity data, int position) {
        if (data != null) {
            String title;
            String type = data.getType();
            if (TextUtils.equals(type, GankType.Welfare.getName())) {
                title = DateHelper.date2String(data.getPublishedTime().getTime(), GankConfig.WELFARE_DATE_FORMAT);
            } else {
                title = data.getTitle();
            }
            holder.setText(R.id.gank_data_title, title);
            holder.setImageResource(R.id.gank_data_icon, GankConfig.getTypeIcon(type));
            holder.setText(R.id.gank_data_author, data.getAuthor());
            holder.setText(R.id.gank_data_date, DateHelper.getTimestampString(data.getPublishedTime()));
        }
    }

}
