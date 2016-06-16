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
import com.SwipeRefreshListView.controller.IFooterAutoController;
import com.SwipeRefreshListView.controller.IFooterController;
import com.SwipeRefreshListView.interfaces.IPullUpStates;
import com.SwipeRefreshListView.interfaces.MODE;
import com.SwipeRefreshListView.log.SwipeRefreshLog;
import com.SwipeRefreshListView.util.MeasureUtil;
import com.SwipeRefreshListView.view.base.SwipeRefreshBaseListView;

/**
 * 滑到底部自动进行刷新
 *
 * Created by janedler on 16/4/2.
 */
public class SwipeRefreshAutoListView extends SwipeRefreshBaseListView implements IFooterAutoController {

    private View mFootRootView; //footer view

    private TextView mFooterTipTV;

    private LinearLayout mFooterContentLayout;

    private ProgressBar mFooterbar;

    private TextView mFooterHint;

    private String  mHint = "查看更多";

    private boolean mIsAllowLoad = true;

    private boolean mLoading;// 是否在加载数据

    public SwipeRefreshAutoListView(Context context) {
        super(context);
    }

    public SwipeRefreshAutoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected IFooterController getController() {
        return this;
    }

    @Override
    protected ListView inflateListView() {

        //这里只提供了ListView基本的样式，如需改变请通过调用getListView来获得到ListView再进行设置
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

        mFootRootView = LayoutInflater.from(mContext).inflate(R.layout.ui_footer_auto_layout, null, false);

        mFooterTipTV = (TextView) mFootRootView.findViewById(R.id.footer_tip);

        mFooterContentLayout = (LinearLayout) mFootRootView.findViewById(R.id.footer_content_layout);

        mFooterbar = (ProgressBar) mFootRootView.findViewById(R.id.footer_bar);

        mFooterHint = (TextView) mFootRootView.findViewById(R.id.footer_hint);

        //对FooterView进行测量
        MeasureUtil.measureView(mFootRootView);

        //得到FooterView测量后高度
        mFootContentHeight = mFootRootView.getMeasuredHeight();

        mFootRootView.setVisibility(VISIBLE);

        mFooterTipTV.setVisibility(GONE);

        mFooterContentLayout.setVisibility(GONE);

        mFooterbar.setVisibility(GONE);

        mFooterHint.setVisibility(GONE);

        mFootRootView.setOnClickListener(new FooterViewClickListener());

        mFootRootView.setClickable(false);

        layout.addView(mFootRootView);

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

    @Override
    public void footerStyleAutoBottom() {

        SwipeRefreshLog.e("Auto -- Footer style bottom");

        mLoading = false;

        mFootRootView.setVisibility(VISIBLE);

        mFooterTipTV.setVisibility(GONE);

        mFooterContentLayout.setVisibility(VISIBLE);

        mFooterbar.setVisibility(VISIBLE);

        mFooterHint.setVisibility(VISIBLE);

        mFooterHint.setText("正在加载");

    }

    @Override
    public void footerStyleAutoLoading() {

        SwipeRefreshLog.e("Auto -- Footer style loading");

        mLoading = true;

        mFootRootView.setClickable(false);

        mFootRootView.setVisibility(VISIBLE);

        mFooterTipTV.setVisibility(GONE);

        mFooterContentLayout.setVisibility(VISIBLE);

        mFooterbar.setVisibility(VISIBLE);

        mFooterHint.setVisibility(VISIBLE);

        mFooterHint.setText("正在加载");

    }

    @Override
    public void footerStyleAutoComplete() {

        SwipeRefreshLog.e("Auto -- Footer style complete >> "+mHint);

        mLoading = false;

        mFooterStatus =  IPullUpStates.PULL_UP_NOMAL;

        mFootRootView.setVisibility(VISIBLE);

        mFooterTipTV.setVisibility(VISIBLE);

        mFooterContentLayout.setVisibility(GONE);

        mFooterbar.setVisibility(VISIBLE);

        mFooterHint.setVisibility(GONE);

        mFooterTipTV.setText(mHint);

    }

    /**
     * 不允许上拉刷新
     */
    @Override
    public void footerStylePullUpOnlyDown() {

        SwipeRefreshLog.e("Auto -- Footer style disallow pullup");

        mLoading = false;

        mFooterStatus = IPullUpStates.PULL_UP_ONLY_DOWN;

        mFootRootView.setVisibility(GONE);

        mFooterTipTV.setVisibility(GONE);

        mFooterContentLayout.setVisibility(GONE);

        mFooterbar.setVisibility(GONE);

        mFooterHint.setVisibility(GONE);

    }

    @Override
    public boolean isAllowAutoLoad() {

        return mIsAllowLoad;

    }


    /**
     * 下拉刷新完成
     */
    @Override
    public void pullDownComplete() {

        super.pullDownComplete();

        mIsAllowLoad = true;

    }

    /**
     * 上拉加载更多成功 并且还有上拉数据
     */
    @Override
    public void pullUpSuccess() {

        pullUpSuccess("");

    }

    /**
     * 上拉加载更多成功
     */
    public void pullUpSuccess(String hint) {

        SwipeRefreshLog.e("pullup is success");

        // 修改加载标记
        mLoading = false;

        this.mHint = hint;

        // 通过设置SetMode来只允许下拉
        if (mFooterStatus == IPullUpStates.PULL_UP_ONLY_DOWN){

            footerStylePullUpOnlyDown();

            return ;
        }

        if (TextUtils.isEmpty(hint)){
            // 如果hint为空 则表示若继续上拉有更多的数据
            mIsAllowLoad = true;

            mFootRootView.setClickable(true);

            footerStyleAutoBottom();

        }else{
            // 如果hint不为空 则表示若继续上拉也没有更多数据了
            mIsAllowLoad = false;

            mFootRootView.setClickable(false);

            footerStyleAutoComplete();
        }
    }

    /**
     * 上拉失败提示
     */
    @Override
    public void pullUpError(){

        pullUpError("加载失败，点击重试");

    }

    /**
     * 上拉失败提示
     */
    public void pullUpError(String hint) {

        SwipeRefreshLog.e("pullup is error");

        // 修改加载标记
        mLoading = false;

        mFootRootView.setClickable(true);

        this.mHint = TextUtils.isEmpty(hint)?"加载失败，点击重试":hint;

        if (mFooterStatus == IPullUpStates.PULL_UP_ONLY_DOWN){

            footerStylePullUpOnlyDown();

            return ;
        }
        mIsAllowLoad = false;

        footerStyleAutoComplete();
    }

    private class FooterViewClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {

            SwipeRefreshLog.e("FooterViewClickListener is clicked");

            footerStyleAutoLoading();

            mPullUpListener.onLoad();
        }
    }

}













