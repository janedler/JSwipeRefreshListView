package com.SwipeRefreshListView.controller;

/**
 * SwipeRefreshListView
 *          下滑到底部自动加载更多控制器
 *
 * Created by janedler on 16/4/2.
 */
public interface IFooterAutoController extends IFooterController {


    /**
     * 滑到了底部
     */
    void footerStyleAutoBottom();


    /**
     * 进行加载中
     */
    void footerStyleAutoLoading();


    /**
     * 加载完毕
     */
    void footerStyleAutoComplete();

    /**
     * 判断是否运行自动滑到底部加载
     * @return
     */
    boolean isAllowAutoLoad();

}
