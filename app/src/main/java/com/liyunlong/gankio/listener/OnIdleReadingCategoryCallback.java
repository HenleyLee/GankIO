package com.liyunlong.gankio.listener;

import com.liyunlong.gankio.entity.IdleReadingEntity;

/**
 * 闲读分类回调
 *
 * @author liyunlong
 * @date 2018/7/16 11:18
 */
public interface OnIdleReadingCategoryCallback {

    void onIdleReadingCategoryCallback(IdleReadingEntity.Site site);

}
