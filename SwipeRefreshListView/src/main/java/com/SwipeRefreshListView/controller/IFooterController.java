package com.SwipeRefreshListView.controller;

import android.view.View;

/**
 * SwipeRefreshListView
 *          Footer加载更多控制器基类
 *
 * Created by janedler on 16/4/2.
 */
public interface IFooterController {

    /**
     * 创建FooterView
     */
    View setFooterView();

    /**
     * 设置是否运行上拉或者下拉
     *
     * @param mode 0允许上下拉 MODE.BOTH
     *             1只允许下拉 MODE.ONLY_DOWN
     */
    void setMode(int mode);

    /**
     * 下拉刷新完成
     */
    void pullDownComplete();

    /**
     * 上拉刷新完成
     */
    void pullUpSuccess();

    /**
     * 上拉刷新失败
     */
    void pullUpError();


    /**
     * 用于判断是否正在加载更多的数据
     *
     * @return true  加载完毕
     *         false 正在加载中
     */
    boolean isPullUpLoading();

    /**
     * 不允许上拉刷新
     */
    void footerStylePullUpOnlyDown();

}
