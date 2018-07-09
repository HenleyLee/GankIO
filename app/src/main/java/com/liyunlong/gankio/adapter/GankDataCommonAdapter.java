package com.liyunlong.gankio.adapter;

import android.text.TextUtils;
import android.view.View;

import com.liyunlong.gankio.R;
import com.liyunlong.gankio.entity.GankEntity;
import com.liyunlong.gankio.entity.GankType;
import com.liyunlong.gankio.gank.GankConfig;
import com.liyunlong.gankio.utils.DateHelper;
import com.liyunlong.gankio.utils.ViewHolder;

import java.util.Collection;

/**
 * 干货分类数据Adapter(公用)
 *
 * @author liyunlong
 * @date 2018/7/3 18:33
 */
public class GankDataCommonAdapter extends CommonAdapter<GankEntity> {

    private GankType gankType;

    public GankDataCommonAdapter(Collection<GankEntity> datas, GankType gankType) {
        super(datas);
        this.gankType = gankType;
    }

    @Override
    public int getItemLayoutID() {
        return R.layout.layout_item_gank_data_common;
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
            if (gankType == GankType.All) {
                holder.setVisibility(R.id.gank_data_icon, View.VISIBLE);
                holder.setImageResource(R.id.gank_data_icon, GankConfig.getTypeIcon(type));
            } else {
                holder.setVisibility(R.id.gank_data_icon, View.GONE);
            }
            holder.setText(R.id.gank_data_title, title);
            holder.setText(R.id.gank_data_author, data.getAuthor());
            holder.setText(R.id.gank_data_date, DateHelper.getTimestampString(data.getPublishedTime()));
        }
    }

}
