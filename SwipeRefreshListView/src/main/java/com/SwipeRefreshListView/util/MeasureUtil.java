package com.SwipeRefreshListView.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * 测量工具类
 *
 * Created by janedler on 16/4/2.
 */
public class MeasureUtil {

    //计算headView的width以及height
    public static void measureView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null)
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, params.width);
        int lpHeight = params.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }


}
