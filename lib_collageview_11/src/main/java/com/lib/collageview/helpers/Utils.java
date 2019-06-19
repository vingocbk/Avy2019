package com.lib.collageview.helpers;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vutha on 4/12/2017.
 */

public class Utils {
    public static String getSimpleDate() {
        String ret = null;

        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        ret = sdfNow.format(new Date(System.currentTimeMillis()));
        ret = ret.trim();
        return ret;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavigationBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
