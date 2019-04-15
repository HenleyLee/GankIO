package com.henley.gankio.listener;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;

import com.henley.gankio.utils.ViewHolder;

import java.util.Collection;
import java.util.List;

/**
 * Adapter数据操作接口
 *
 * @author Henley
 * @date 2018/7/3 18:29
 */
public interface IAdapter<DataType> {

    /**
     * 返回{@link Context}对象
     */
    Context getContext();

    /**
     * 返回{@link Resources}对象
     */
    Resources getResources();

    /**
     * 返回数据源
     */
    List<DataType> getDatas();

    /**
     * 刷新数据源
     */
    void refresh(Collection<DataType> datas);

    /**
     * 添加数据
     */
    void add(DataType data);

    /**
     * 添加数据到指定位置
     */
    void add(int position, DataType data);

    /**
     * 添加数据集
     */
    void addAll(Collection<DataType> datas);

    /**
     * 移除指定位置的数据
     */
    void remove(int position);

    /**
     * 移除指定数据
     */
    void remove(DataType data);

    /**
     * 移除指定数据集
     */
    void removeAll(Collection<DataType> datas);

    /**
     * 清空数据源
     */
    void clear();

    /**
     * 返回Item布局资源ID(用于多类型ItemView)
     */
    @LayoutRes
    int getItemLayoutID(int viewType);

    /**
     * 返回Item布局资源ID(用于单类型ItemView)
     */
    @LayoutRes
    int getItemLayoutID();

    /**
     * 数据和事件绑定
     */
    void convert(ViewHolder holder, DataType data, int position);

}
