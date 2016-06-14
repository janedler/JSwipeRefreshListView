package com.SwipeRefreshListView.controller;

/**
 * ListView下滑到底部上拉操作进行刷新控制器
 *
 * Created by janedler on 16/4/2.
 */
public interface IFooterManualController extends IFooterController {

    /**
     * 初始状态
     */
    void footerStyleManualNomal();

    /**
     * 上拉的状态
     */
    void footerStyleManualPullUp();


    /**
     * 上拉到一定位置开始释放的状态
     */
    void footerStyleManualRelease();

    /**
     * 开始加载更多的状态
     */
    void footerStyleManualLoading();


    /**
     * 加载完毕
     */
    void footerStyleManualOver();

}
