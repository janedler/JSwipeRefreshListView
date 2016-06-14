package com.SwipeRefreshListView.controller;

/**
 * ListView下滑到底部自动刷新控制器
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
