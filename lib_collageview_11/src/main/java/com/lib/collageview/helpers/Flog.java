package com.lib.collageview.helpers;

import android.util.Log;

import com.lib.collageview.BuildConfig;

/**
 * Created by vutha on 3/28/2017.
 */

public class Flog {

    private static String TAG = "CollageAppLib";
    private static boolean show = true;

    public static void d(String tag, String content) {
        if (show)
            Log.d(tag, content);
    }

    public static void d(String content) {
        if (show)
            Log.d(TAG, content);
    }

    public static void i(String tag, String content) {
        if (show)
            Log.i(tag, content);
    }

    public static void i(String content) {
        if (show)
            Log.i(TAG, content);
    }

    public static void e(String tag, String content) {
        if (show)
            Log.e(tag, content);
    }

    public static void e(String content) {
        if (show)
            Log.e(TAG, content);
    }
}
