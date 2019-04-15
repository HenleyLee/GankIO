package com.henley.gankio.mvp;

import android.content.Context;

import com.henley.gankio.http.HttpException;

import io.reactivex.annotations.NonNull;

/**
 * 负责绘制UI元素、与用户进行交互(在Android中体现为Activity)
 * <ul>
 * <li>UI层，包含所有UI相关组件
 * <li>持有对应的Presenter的对象，可通过依赖注入解耦此部分
 * <li>由Presenter来负责更新UI
 * </ul>
 *
 * @author Henley
 * @date 2018/7/3 16:31
 */
public interface IMVPView {

    /**
     * 返回{@link Context}对象
     */
    Context getContext();

    /**
     * 处理网络请求错误
     *
     * @param exception 网络请求错误
     */
    void handleException(@NonNull HttpException exception);

}
