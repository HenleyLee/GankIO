package com.henley.gankio.adapter;

import android.support.v7.widget.RecyclerView;

import com.henley.gankio.delegate.IAdapterDelegate;
import com.henley.gankio.delegate.ItemViewDelegate;
import com.henley.gankio.delegate.ItemViewDelegateManager;
import com.henley.gankio.utils.ViewHolder;

import java.util.Collection;

/**
 * {@link RecyclerView}适配器(用于多种ItemView类型)
 *
 * @param <DataType> 数据类型的泛型
 * @author Henley
 * @date 2018/7/12 13:35
 */
public class MultiItemTypeAdapter<DataType> extends CommonAdapter<DataType> implements IAdapterDelegate<DataType> {


    private ItemViewDelegateManager<DataType> mItemViewDelegateManager = new ItemViewDelegateManager<>();

    public MultiItemTypeAdapter(Collection<DataType> datas) {
        super(datas);
    }

    @Override
    public void addItemViewDelegate(ItemViewDelegate<DataType> delegate) {
        mItemViewDelegateManager.addDelegate(delegate);
    }

    @Override
    public void addItemViewDelegate(int viewType, ItemViewDelegate<DataType> delegate) {
        mItemViewDelegateManager.addDelegate(viewType, delegate);
    }

    @Override
    public ItemViewDelegateManager<DataType> removeDelegate(int itemType) {
        return mItemViewDelegateManager.removeDelegate(itemType);
    }

    @Override
    public ItemViewDelegateManager<DataType> removeDelegate(ItemViewDelegate<DataType> delegate) {
        return mItemViewDelegateManager.removeDelegate(delegate);
    }

    @Override
    public boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (useItemViewDelegateManager()) {
            return mItemViewDelegateManager.getItemViewType(getItem(position), position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemLayoutID(int viewType) {
        if (useItemViewDelegateManager()) {
            return mItemViewDelegateManager.getItemViewLayoutId(viewType);
        }
        return getItemLayoutID();
    }

    @Override
    public int getItemLayoutID() {
        return 0;
    }

    @Override
    public void convert(ViewHolder holder, DataType data, int position) {
        mItemViewDelegateManager.convert(holder, data, position);
    }

}
