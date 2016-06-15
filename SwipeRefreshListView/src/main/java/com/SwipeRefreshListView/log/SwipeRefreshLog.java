package com.SwipeRefreshListView.log;

import android.support.v7.appcompat.BuildConfig;
import android.util.Config;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * Log管理类
 *    DEBUG环境下会打印出相应的log日志
 * Created byjanedler on 2016/4/2.
 * <p/>
 * A wrapper for android.util.Log
 */
public class SwipeRefreshLog {


    private static final String TAG = "SwipeRefreshTAG";

    private SwipeRefreshLog() {
    }

    /**
     * @param msg The message you would like logged.
     */
    public static int e(String msg) {
        if (BuildConfig.DEBUG) {
            return android.util.Log.e(TAG, msg);
        } else {
            return 0;
        }
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    @SuppressWarnings("unused")
    public static String getStackTraceString(Throwable tr) {
        if (BuildConfig.DEBUG) {
            if (tr == null) {
                return "";
            }

            // This is to reduce the amount of log spew that apps do in the non-error
            // condition of the network being unavailable.
            Throwable t = tr;
            while (t != null) {
                if (t instanceof UnknownHostException) {
                    return "";
                }
                t = t.getCause();
            }

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            tr.printStackTrace(pw);
            pw.flush();
            return sw.toString();
        } else {
            return null;
        }
    }

}
