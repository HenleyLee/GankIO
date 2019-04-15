package com.henley.gankio.listener;

import com.henley.gankio.entity.IdleReadingEntity;

/**
 * 闲读分类回调
 *
 * @author Henley
 * @date 2018/7/16 11:18
 */
public interface OnIdleReadingCategoryCallback {

    void onIdleReadingCategoryCallback(IdleReadingEntity.Site site);

}
