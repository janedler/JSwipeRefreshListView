package com.SwipeRefreshListView.interfaces;

/**
 * ListView 状态
 *
 * Created by dell on 2016/3/31.
 */
public interface MODE {

    int BOTH = 0;                        //  运行上拉下拉

    int ONLY_DOWN = 1;                   //  只允许下拉

    int NONE = 2;                        //  不允许上拉与下拉
}
