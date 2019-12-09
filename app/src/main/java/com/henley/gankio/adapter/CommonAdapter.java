package com.henley.gankio.adapter;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.henley.gankio.listener.IRecycleViewAdapter;
import com.henley.gankio.listener.OnItemClickListener;
import com.henley.gankio.listener.OnItemLongClickListener;
import com.henley.gankio.utils.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Henley
 * @date 2018/7/3 18:25
 */
public abstract class CommonAdapter<DataType> extends RecyclerView.Adapter<RecyclerViewHolder> implements IRecycleViewAdapter<DataType> {

    /** 上下文 */
    private Context mContext;
    /** 数据源 */
    public List<DataType> mDatas = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public CommonAdapter(Collection<DataType> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        this.mDatas.addAll(datas);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public Resources getResources() {
        return mContext.getResources();
    }

    @Override
    public List<DataType> getDatas() {
        return mDatas;
    }

    @Override
    public void refresh(Collection<DataType> datas) {
        if (datas == null) {
            datas = new ArrayList<>(0);
        }
        this.mDatas.clear();
        this.mDatas.addAll(datas);
        this.notifyDataSetChanged();
    }

    @Override
    public void add(DataType data) {
        this.mDatas.add(data);
        this.notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void add(int position, DataType data) {
        this.mDatas.add(position, data);
        this.notifyItemInserted(position);
    }

    @Override
    public void addAll(Collection<DataType> datas) {
        if (datas == null || datas.isEmpty()) {
            return;
        }
        int itemCount = getItemCount();
        this.mDatas.addAll(datas);
        this.notifyItemRangeInserted(itemCount, datas.size());
    }

    @Override
    public void remove(DataType data) {
        if (mDatas != null && !mDatas.isEmpty()) {
            int indexToRemove = mDatas.indexOf(data);
            remove(indexToRemove);
        }
    }

    @Override
    public void remove(int position) {
        if (mDatas != null && !mDatas.isEmpty()) {
            this.mDatas.remove(position);
            this.notifyItemRemoved(position);
            if (position != getItemCount()) { // 如果是最后一条数据则不再通知刷新
                notifyItemRangeChanged(position, getItemCount());
            }
        }
    }

    @Override
    public void removeAll(Collection<DataType> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        this.mDatas.removeAll(datas);
        this.notifyDataSetChanged();
    }

    @Override
    public void clear() {
        this.mDatas.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    @Override
    public DataType getItem(int position) {
        return isValidity(position) ? mDatas.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        int itemLayoutID = getItemLayoutID(viewType);
        return RecyclerViewHolder.createViewHolder(mContext, parent, itemLayoutID);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        final int finalPosition = holder.getLayoutPosition();
        setItemViewListener(holder, finalPosition);
        convert(holder.getViewHolder(), getItem(finalPosition), finalPosition);
    }

    /**
     * 设置ItemView的点击事件和长按事件监听
     *
     * @param holder   {@link RecyclerViewHolder}对象
     * @param position 点击的Item的索引
     */
    private void setItemViewListener(final RecyclerViewHolder holder, final int position) {
        if (!areAllItemsEnabled() && !isEnabled(position)) {
            return;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, holder, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    return mOnItemLongClickListener.onItemLongClick(v, holder, position);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemLayoutID(int viewType) {
        return getItemLayoutID();
    }

    /**
     * 检查索引是否有效
     */
    private boolean isValidity(int position) {
        return mDatas != null && !mDatas.isEmpty() && position >= 0 && position < mDatas.size();
    }

}
