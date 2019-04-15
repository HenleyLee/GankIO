package com.henley.gankio.mvp;

/**
 * 负责业务逻辑和实体模型(主要职责是存储、检索、操纵数据，有时也实现一个Model interface用来降低耦合)
 * <ul>
 * <li>对外提供业务数据API
 * <li>内部实现本地数据, 网络数据的存取等
 * <li>只有Presenter可以访问，与View隔离
 * </ul>
 *
 * @author Henley
 * @date 2018/7/3 16:30
 */
public interface IMVPModel {
}
