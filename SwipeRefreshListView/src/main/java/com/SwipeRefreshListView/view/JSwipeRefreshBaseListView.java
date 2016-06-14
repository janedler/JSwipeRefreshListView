package com.SwipeRefreshListView.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.SwipeRefreshListView.controller.IFooterController;
import com.SwipeRefreshListView.exception.SwipeRefreshException;
import com.SwipeRefreshListView.interfaces.IPullUpStates;
import com.SwipeRefreshListView.interfaces.MODE;
import com.SwipeRefreshListView.listener.OnPullDownListener;
import com.SwipeRefreshListView.listener.OnPullUpListener;
import com.SwipeRefreshListView.log.JSwipeRefreshLog;

/**
 * SwipeRefreshLayout扩展基类 支持自定上拉加载更多的样式
 *
 * @author janedler
 */
public abstract class JSwipeRefreshBaseListView extends SwipeRefreshLayout {

    /**
     * 最小滑动距离
     */
    private int mTouchSlop;
    protected Context mContext;  //context

    /**
     * footerView item的高度
     */
    protected int mFootContentHeight;

    /**
     * 此变量用来标识是否允许上拉或者下拉
     */
    protected int mMode = MODE.ONLY_DOWN;

    protected int mFooterStatus = IPullUpStates.PULL_UP_NOMAL; //FooterView 视图状态
    protected IFooterController mController;

    /**
     * ListView View
     */
    protected ListView mListView;

    /**
     * FooterView
     */
    private View mFootRootView;

    /**
     * 滑动的距离
     */
    private int mDistanceY;
    private int mFirstVisibleItem;
    private int mVisibleItemCount;
    private int mTotalItemCount;
    private boolean isBottom = false;
    private int mStartY = 0;
    private int mPaddingBottom = 0;
    private boolean isRecord = false;
    // 加载数据的监听
    protected OnPullUpListener mPullUpListener;
    // 刷新监听
    protected OnPullDownListener mPullDownListener;
    //Listview滚动监听器
    protected JOnScrollListener mJOnScrollListener;
    // 刷新监听标记
    private boolean mRefreshListenerFlag = false;

    public JSwipeRefreshBaseListView(Context context) {
        this(context, null);
    }

    public JSwipeRefreshBaseListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        /**
         * 获得控制器
         * 为空 会抛出异常
         */
        mController = getController();
        if (mController == null) new SwipeRefreshException("子类需要实现getController方法");

        /**
         * 获得ListView 支持用户自定义ListView 通过子类重写inflateListView方法
         * 为空 会抛出异常
         */
        mListView = inflateListView();
        if (mListView == null) new SwipeRefreshException("子类需要实现getListView方法");
        mListView.setOnScrollListener(new JOnScrollListener());
        this.addView(mListView);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        /**
         * 设置默认的头部加载样式
         */
        setHeaderDefaltStyle();

        /**
         * 获得到FooterView
         */
        mFootRootView = mController.setFooterView();
        if (mFootRootView == null) new SwipeRefreshException("子类需要实现setFooterView方法");

        mRefreshListenerFlag = true;
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mController.isPullUpLoading()) {
                    mController.pullDownComplete();
                    return;
                }
                if (mPullDownListener != null) mPullDownListener.onRefresh();
            }
        });
    }


    /**
     * 设置默认的头部加载样式
     */
    private void setHeaderDefaltStyle() {
        // 设置刷新进度颜色变化
        setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED);
        // 设置刷新进度的背景
        setProgressBackgroundColorSchemeColor(Color.argb(105, 22, 55, 66));
        // 设置刷新进度的大小
        setSize(SwipeRefreshLayout.DEFAULT);
    }


    /**
     * 设置FooterPadding 提供子类也可以调用
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param isRecordPadding 是否给mPaddingBottom赋值
     */
    protected void setFooterPadding(int left, int top, int right, int bottom, boolean isRecordPadding) {
        if (isRecordPadding) mPaddingBottom = bottom;
        mFootRootView.setPadding(left, top, right, bottom);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                doActionDown();
                break;
            case MotionEvent.ACTION_MOVE:
                doActionMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                doActionUp();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * ACTION_DOWN
     */
    private void doActionDown() {
        isRecord = false;
        isBottom = false;
    }

    /**
     * ACTION_MOVE
     * @param event
     */
    private void doActionMove(final MotionEvent event) {

        /**
         * 用于判断是否到了底部
         */
        isBottom = isBottom ? true : mFirstVisibleItem + mVisibleItemCount == mTotalItemCount;
        if (!isBottom || mController.isPullUpLoading() || isRefreshing()) {
            return;
        }

        if (!isRecord) {
            mStartY = (int) event.getRawY();
            isRecord = true;
        }

        /**
         * 手动上拉
         */
        if (mController instanceof JSwipeRefreshManualListView) {

            mListView.post(new Runnable() {
                @Override
                public void run() {

                    mDistanceY = (int) (event.getRawY() - mStartY);

                    if (-mDistanceY < mTouchSlop) {
                        return;
                    }

                    /**
                     * 只允许下拉刷新 不支持上拉加载更多
                     */
                    if (mMode == MODE.ONLY_DOWN) {
                        setFooterPadding(0, 0, 0, mFootContentHeight - mDistanceY - mTouchSlop, false);
                        mController.footerStylePullUpOnlyDown();
                        mFooterStatus = IPullUpStates.PULL_UP_ONLY_DOWN;
                        return;
                    }

                    if (mFooterStatus == IPullUpStates.PULL_UP_NOMAL
                            || mFooterStatus == IPullUpStates.PULL_UP
                            || mFooterStatus == IPullUpStates.PULL_UP_RELEASE
                            || mFooterStatus == IPullUpStates.PULL_UP_OVER){
                        setFooterPadding(0, 0, 0, mFootContentHeight - mDistanceY - mTouchSlop, false);
                    }


                    switch (mFooterStatus) {
                        case IPullUpStates.PULL_UP_NOMAL: //初始状态
                            ((JSwipeRefreshManualListView) mController).footerStyleManualPullUp();
                            mFooterStatus = IPullUpStates.PULL_UP;
                            break;

                        case IPullUpStates.PULL_UP: //上拉的状态（此时释放不会触发加载更多）
                            if (-mDistanceY + mPaddingBottom > mFootContentHeight) {
                                ((JSwipeRefreshManualListView) mController).footerStyleManualRelease();
                                mFooterStatus = IPullUpStates.PULL_UP_RELEASE;
                            }
                            break;

                        case IPullUpStates.PULL_UP_RELEASE: //上拉到了可以释放的状态了 （此时释放可以进行加载更多）
                            if (-mDistanceY + mPaddingBottom > mFootContentHeight) {
                                ((JSwipeRefreshManualListView) mController).footerStyleManualRelease();
                                mFooterStatus = IPullUpStates.PULL_UP_RELEASE;
                            } else if (-mDistanceY + mPaddingBottom > 0 && -mDistanceY + mPaddingBottom <= mFootContentHeight) {
                                ((JSwipeRefreshManualListView) mController).footerStyleManualPullUp();
                                mFooterStatus = IPullUpStates.PULL_UP;
                            } else {

                            }
                            break;

                        case IPullUpStates.PULL_UP_OVER: //加载完毕
                            break;
                    }
                }

            });
        }
    }


    /**
     * ACTION_UP
     */
    private void doActionUp() {
        isRecord = false;
        isBottom = false;
        mDistanceY = 0;
        mStartY = 0;
        /**
         * 只允许下拉刷新 不支持上拉加载更多
         */
        if (mController instanceof JSwipeRefreshManualListView) {
            switch (mFooterStatus) {
                case IPullUpStates.PULL_UP_NOMAL:
                    break;

                case IPullUpStates.PULL_UP:
                    setFooterPadding(0, 0, 0, -mFootContentHeight, true);
                    ((JSwipeRefreshManualListView) mController).footerStyleManualNomal();
                    mFooterStatus = IPullUpStates.PULL_UP_NOMAL;
                    break;

                case IPullUpStates.PULL_UP_RELEASE:
                    setFooterPadding(0, 0, 0, 0, true);
                    ((JSwipeRefreshManualListView) mController).footerStyleManualLoading();
                    mFooterStatus = IPullUpStates.PULL_UP_LOADING;
                    break;

                case IPullUpStates.PULL_UP_OVER:
                    setFooterPadding(0, 0, 0, 0, true);
                    break;

                case IPullUpStates.PULL_UP_ONLY_DOWN:
                    setFooterPadding(0, 0, 0, 0, true);
                    break;
            }
        }
    }



    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        if (mRefreshListenerFlag) {
            super.setOnRefreshListener(listener);
            mRefreshListenerFlag = false;
        } else {
            throw new SwipeRefreshException("请使用setOnPullDownListener方法");
        }

    }



    /**
     * ListView 滚动监听器
     */
    private class JOnScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            if (mJOnScrollListener != null) mJOnScrollListener.onScrollStateChanged(view, scrollState);

            if (mController instanceof JSwipeRefreshAutoListView
                    && ((JSwipeRefreshAutoListView) mController).isAllowAutoLoad()
                    && mMode != MODE.ONLY_DOWN
                    && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && (mFirstVisibleItem + mVisibleItemCount == mTotalItemCount)) {

                JSwipeRefreshLog.e("Footer scroll bottom");

                /**
                 * 如果使用的是自动加载更多的ListView 开始自动加载
                 */
                ((JSwipeRefreshAutoListView) mController).footerStyleAutoLoading();

                if (mPullUpListener != null) mPullUpListener.onLoad();

            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            if (mJOnScrollListener != null) mJOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

            mFirstVisibleItem = firstVisibleItem;
            mVisibleItemCount = visibleItemCount;
            mTotalItemCount = totalItemCount;

        }
    }

    //--------------------------------------------------------------------------------------------------

    /**
     * 上拉刷新Listener
     */
    public void setOnPullUpListener(@NonNull OnPullUpListener loadListener) {
        mPullUpListener = loadListener;
    }

    /**
     * 下拉刷新Listener
     */
    public void setOnPullDownListener(@NonNull OnPullDownListener refreshListener) {
        mPullDownListener = refreshListener;
    }

    /**
     * ListView ScrollListener
     *
     * @param jOnScrollListener
     */
    public void setJOnScrollListener(JOnScrollListener jOnScrollListener) {
        this.mJOnScrollListener = jOnScrollListener;
    }


    /**
     * 自动下拉刷新
     */
    public void autoRefreshing() {

        if(isRefreshing() || mController.isPullUpLoading()){
            JSwipeRefreshLog.e("please wait, listview is loading");
            return;
        }

        mListView.post(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(0);
                setRefreshing(true);
                if (mPullDownListener != null) mPullDownListener.onRefresh();
            }
        });
    }


    /**
     * 下拉刷新完成
     */
    public void pullDownComplete() {
        JSwipeRefreshLog.e("pulldown is complete");
        mListView.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(false);
                mFooterStatus = IPullUpStates.PULL_UP_NOMAL;
                if (mMode == MODE.BOTH) {
                    if (mController instanceof JSwipeRefreshManualListView) {
                        ((JSwipeRefreshManualListView) mController).footerStyleManualNomal();
                    }
                    if (mController instanceof JSwipeRefreshAutoListView) {
                        ((JSwipeRefreshAutoListView) mController).footerStyleAutoBottom();
                    }

                }
                if (mMode == MODE.ONLY_DOWN) {
                    mController.footerStylePullUpOnlyDown();
                    mFooterStatus = IPullUpStates.PULL_UP_ONLY_DOWN;
                }
            }
        });
    }


    /**
     * 返回ListView
     * @return
     */
    public ListView getListView(){
        return mListView;
    }

    //--------------------------------------------------------------------------------------------------

    /**
     * 注入FooterView控制器
     * 现在只提供了2中FooterView控制器
     *      IFooterAutoController    自动上拉
     *      IFooterManualController  手动上拉
     * @return
     */
    protected abstract IFooterController getController();


    /**
     * 子类实现此方法 提供ListView
     * @return
     */
    protected abstract ListView inflateListView();



}
