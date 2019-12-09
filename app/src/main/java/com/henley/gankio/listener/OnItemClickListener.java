package com.henley.gankio.listener;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * {@link RecyclerView}的Item点击事件监听
 *
 * @author Henley
 * @date 2018/7/3 18:26
 */
public interface OnItemClickListener {

    void onItemClick(View view, RecyclerView.ViewHolder holder, int position);
}
