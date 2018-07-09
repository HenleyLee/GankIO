package com.liyunlong.gankio.listener;

import android.view.View;

import com.liyunlong.gankio.entity.GankEntity;

/**
 * 每日干货数据Item点击监听
 *
 * @author liyunlong
 * @date 2018/7/5 10:15
 */
public interface OnGankDailyItemClickListener {

    void onGankDailyItemClick(View view, GankEntity gankEntity);

}
