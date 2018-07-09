package com.liyunlong.gankio.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * {@link RecyclerView}的Item长点击事件监听
 *
 * @author liyunlong
 * @date 2018/7/3 18:26
 */
public interface OnItemLongClickListener {

    boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);

}
