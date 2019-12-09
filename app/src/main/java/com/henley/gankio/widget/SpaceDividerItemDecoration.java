package com.henley.gankio.widget;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView分割线
 *
 * @author Henley
 * @date 2018/7/4 10:36
 */
public class SpaceDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpacing;
    private final int horizontalSpacing;

    public SpaceDividerItemDecoration(int verticalSpacing, int horizontalSpacing) {
        this.verticalSpacing = verticalSpacing;
        this.horizontalSpacing = horizontalSpacing;
    }

    /**
     * set border
     *
     * @param outRect outRect
     * @param view    view
     * @param parent  parent
     * @param state   state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int itemPosition = layoutParams.getViewLayoutPosition();
        int childCount = parent.getAdapter().getItemCount();
        int left = this.horizontalSpacing;
        int right = this.horizontalSpacing;
        int top = getItemTopSpacing(itemPosition);
        int bottom = getItemBottomSpacing(itemPosition, childCount);
        outRect.set(left, top, right, bottom);
    }


    /**
     * 获得Item底部间距
     *
     * @param itemPosition itemPosition
     * @param childCount   childCount
     * @return int
     */
    private int getItemBottomSpacing(int itemPosition, int childCount) {
        if (isLastItem(itemPosition, childCount)) {
            return this.verticalSpacing;
        }
        return this.verticalSpacing / 2;
    }


    /**
     * get the item top spacing
     *
     * @param itemPosition itemPosition
     * @return int
     */
    private int getItemTopSpacing(int itemPosition) {
        if (isFirstItem(itemPosition)) {
            return this.verticalSpacing;
        }
        return this.verticalSpacing / 2;
    }


    /**
     * 判断是否为第一个Item
     *
     * @param itemPosition itemPosition
     * @return boolean
     */
    private boolean isFirstItem(int itemPosition) {
        return itemPosition == 0;
    }


    /**
     * 判断是否为最后一个Item
     *
     * @param itemPosition itemPosition
     * @param childCount   childCount
     * @return boolean
     */
    private boolean isLastItem(int itemPosition, int childCount) {
        return itemPosition == childCount - 1;
    }

}
