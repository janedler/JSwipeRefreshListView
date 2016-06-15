package com.SwipeRefreshListView.listener;

import android.widget.AbsListView;

/**
 * SwipeRefreshListView OnScrollListener
 *
 * ps:不要用原生的OnScrollListener
 */
public interface SwipeRefreshOnScrollListener {

    void onScroll(AbsListView view, int firstVisiableItem, int visibleItemCount, int totalItemCount);

    void onScrollStateChanged(AbsListView view, int scrollState);
}
