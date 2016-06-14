package com.SwipeRefreshListView.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * Log管理类
 *
 * Created byjanedler on 2016/4/2.
 * <p/>
 * A wrapper for android.util.Log
 */
public class JSwipeRefreshLog {


    private static final String TAG = "JSwipeRefreshTAG";

    /**
     * Turn on/off for Log
     */
    private static final boolean IS_LOG_ON = false;

    private JSwipeRefreshLog() {
    }

    /**
     * @param msg The message you would like logged.
     */
    public static int e(String msg) {
        if (IS_LOG_ON) {
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
        if (IS_LOG_ON) {
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
