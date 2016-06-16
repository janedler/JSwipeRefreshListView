package com.SwipeRefreshListView.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.SwipeRefreshListView.R;
import com.SwipeRefreshListView.controller.IFooterController;
import com.SwipeRefreshListView.controller.IFooterManualController;
import com.SwipeRefreshListView.interfaces.IPullUpStates;
import com.SwipeRefreshListView.interfaces.MODE;
import com.SwipeRefreshListView.log.SwipeRefreshLog;
import com.SwipeRefreshListView.util.MeasureUtil;
import com.SwipeRefreshListView.view.base.SwipeRefreshBaseListView;

/**
 * 滑到底部进行上拉后进行刷新
 *
 * Created by janedler on 16/4/2.
 */
public class SwipeRefreshManualListView extends SwipeRefreshBaseListView implements IFooterManualController {

    private View mFootRootView; //footer view

    private TextView mFooterTipTV;

    private LinearLayout mFooterContentLayout;

    private ProgressBar mFooterbar;

    private TextView mFooterHint;

    private boolean mLoading;// 是否在加载数据

    private String mHint;

    private String mDefaultHint;

    public SwipeRefreshManualListView(Context context) {
        super(context);
    }

    public SwipeRefreshManualListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected IFooterController getController() {
        return this;
    }

    @Override
    protected ListView inflateListView() {

        ListView listView = (ListView) LayoutInflater.from(mContext).inflate(R.layout.ui_swipe_default_listview, null, false);

        listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        return listView;
    }

    /**
     * 设置FooterView
     */
    @Override
    public View setFooterView() {

        LinearLayout layout = new LinearLayout(mContext);

        layout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));

        layout.setGravity(Gravity.CENTER);

        mFootRootView = LayoutInflater.from(mContext).inflate(R.layout.ui_footer_manual_layout, null, false);

        mFooterTipTV = (TextView) mFootRootView.findViewById(R.id.footer_tip);

        mFooterContentLayout = (LinearLayout) mFootRootView.findViewById(R.id.footer_content_layout);

        mFooterbar = (ProgressBar) mFootRootView.findViewById(R.id.footer_bar);

        mFooterHint = (TextView) mFootRootView.findViewById(R.id.footer_hint);

        mDefaultHint = mFooterTipTV.getText().toString();

        MeasureUtil.measureView(mFootRootView);//对FooterView进行测量

        mFootContentHeight = mFootRootView.getMeasuredHeight(); //得到FooterView测量后高度

        layout.addView(mFootRootView);

        mFootRootView.setVisibility(VISIBLE);

        mFooterTipTV.setVisibility(GONE);

        mFooterContentLayout.setVisibility(GONE);

        mFooterbar.setVisibility(GONE);

        mFooterHint.setVisibility(GONE);

        if (mListView.getFooterViewsCount() == 0) mListView.addFooterView(layout);

        return mFootRootView;
    }


    /**
     * 设置是否运行上拉或者下拉
     *
     * @param mode 0允许上下拉 MODE.BOTH
     *             1只允许下拉  MODE.ONLY_DOWN
     */
    @Override
    public void setMode(int mode) {

        mMode = mode;

        if (mode == MODE.BOTH) {

            mFootRootView.setVisibility(VISIBLE);

        } else if (mode == MODE.ONLY_DOWN) {

            mFootRootView.setVisibility(VISIBLE);

            mFooterTipTV.setVisibility(GONE);

            mFooterContentLayout.setVisibility(GONE);

            mFooterbar.setVisibility(GONE);

            mFooterHint.setVisibility(GONE);

            footerStylePullUpOnlyDown();
        }
    }

    /**
     * 用于判断是否正在加载更多的数据
     * @return
     */
    @Override
    public boolean isPullUpLoading() {
        return mLoading;
    }

    /**
     * 初始状态
     */
    @Override
    public void footerStyleManualNomal() {

        SwipeRefreshLog.e("Manual -- Footer style nomal");

        mLoading = false;

        mFooterStatus = IPullUpStates.PULL_UP_NOMAL;

        mFootRootView.setVisibility(VISIBLE);

        mFooterTipTV.setVisibility(VISIBLE);

        mFooterContentLayout.setVisibility(GONE);

        mFooterTipTV.setText(mDefaultHint);
    }


    public void footerStyleManualNomal(String hint) {

        SwipeRefreshLog.e("Manual -- Footer style nomal");

        mLoading = false;

        mFooterStatus = IPullUpStates.PULL_UP_NOMAL;

        mFootRootView.setVisibility(VISIBLE);

        mFooterTipTV.setVisibility(VISIBLE);

        mFooterContentLayout.setVisibility(GONE);

        mFooterTipTV.setText(hint);
    }


    /**
     * 上拉的状态
     */
    @Override
    public void footerStyleManualPullUp() {

        SwipeRefreshLog.e("Manual -- Footer style pull up");

        mFootRootView.setVisibility(VISIBLE);

        mFooterTipTV.setVisibility(GONE);

        mFooterContentLayout.setVisibility(VISIBLE);

        mFooterbar.setVisibility(VISIBLE);

        mFooterHint.setVisibility(VISIBLE);

        mFooterHint.setText("上拉进行加载");

    }


    /**
     * 上拉到一定位置开始释放的状态
     */
    @Override
    public void footerStyleManualRelease() {

        SwipeRefreshLog.e("Manual -- Footer style release");

        mFootRootView.setVisibility(VISIBLE);

        mFooterTipTV.setVisibility(GONE);

        mFooterContentLayout.setVisibility(VISIBLE);

        mFooterbar.setVisibility(VISIBLE);

        mFooterHint.setVisibility(VISIBLE);

        mFooterHint.setText("松开进行加载");
    }

    /**
     * 开始加载更多的状态
     */
    @Override
    public void footerStyleManualLoading() {

        SwipeRefreshLog.e("Manual -- Footer style loading");

        if (mPullUpListener != null) mPullUpListener.onLoad();

        mLoading = true;

        mFootRootView.setVisibility(VISIBLE);

        mFooterTipTV.setVisibility(GONE);

        mFooterContentLayout.setVisibility(VISIBLE);

        mFooterbar.setVisibility(VISIBLE);

        mFooterHint.setVisibility(VISIBLE);

        mFooterHint.setText("正在加载");
    }


    /**
     * 加载完毕
     */
    @Override
    public void footerStyleManualOver() {

        SwipeRefreshLog.e("Manual -- Footer style over");

        mLoading = false;

        mFooterStatus = IPullUpStates.PULL_UP_OVER;

        mFootRootView.setVisibility(VISIBLE);

        mFooterTipTV.setVisibility(VISIBLE);

        mFooterContentLayout.setVisibility(GONE);

        mFooterbar.setVisibility(GONE);

        mFooterHint.setVisibility(GONE);

        mFooterTipTV.setText(mHint);
    }



    /**
     * 不允许上拉刷新
     */
    @Override
    public void footerStylePullUpOnlyDown() {

        mLoading = false;

        mFootRootView.setVisibility(VISIBLE);

        mFooterTipTV.setVisibility(GONE);

        mFooterContentLayout.setVisibility(GONE);

        mFooterbar.setVisibility(GONE);

        mFooterHint.setVisibility(GONE);

    }


    /**
     * 上拉刷新完成了
     */
    @Override
    public void pullUpSuccess() {

        dealWithMessage("");

    }

    public void pullUpSuccess(String hint) {

        dealWithMessage(hint);

    }

    /**
     * 上拉刷新失败了
     */
    public void pullUpError(){

        dealWithMessage("");

    }

    public void pullUpError(String hint){

        dealWithMessage(hint);

    }


    public void dealWithMessage(String hint) {
        mLoading = false; // 修改加载标记
        this.mHint = hint;

        if (mFooterStatus == IPullUpStates.PULL_UP_ONLY_DOWN){
            footerStylePullUpOnlyDown();
            return ;
        }

        if (TextUtils.isEmpty(hint)){
            setFooterPadding(0, 0, 0, -mFootContentHeight,true);
            footerStylePullUpHint();

        } else {
            setFooterPadding(0, 0, 0, 0,true);
            footerStyleManualNomal(hint);
        }
    }


    /**
     * 隐藏状态
     */
    private void footerStylePullUpHint(){
        mLoading = false;
        mFooterStatus = IPullUpStates.PULL_UP_NOMAL;
        mFootRootView.setVisibility(VISIBLE);
        mFooterTipTV.setVisibility(GONE);
        mFooterContentLayout.setVisibility(GONE);
    }

}













