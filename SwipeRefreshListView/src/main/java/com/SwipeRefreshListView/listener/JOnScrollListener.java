package com.SwipeRefreshListView.listener;

import android.widget.AbsListView;

/**
 * 自定义ListView OnScrollListener
 *
 * ps:不要用原生的OnScrollListener
 */
public interface JOnScrollListener {

    void onScroll(AbsListView view, int firstVisiableItem, int visibleItemCount, int totalItemCount);

    void onScrollStateChanged(AbsListView view, int scrollState);
}
