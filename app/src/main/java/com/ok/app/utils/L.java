package com.ok.app.utils;


import com.orhanobut.logger.Logger;

/**
 * Created by yzd on 2017/8/18 0018.
 */

public class L {
    private static boolean debug = true;
    private static final String TAG = "OkUtil";

    {
        Logger.t(TAG);
    }

    public static void i(String msg) {
        if (debug) {
            Logger.i(msg);
        }
    }

    public static void i(String tag, String msg) {
        setTag(tag);
        i(msg);
    }

    public static void d(String msg) {
        if (debug) {
            Logger.d(msg);
        }
    }

    public static void d(String tag, String msg) {
        setTag(tag);
        d(msg);
    }

    public static void e(String msg) {
        if (debug) {
            Logger.e(msg);
        }
    }

    public static void e(String tag, String msg) {
        setTag(tag);
        e(msg);
    }

    public static void setTag(String tag) {
        Logger.t(tag);
    }

}
