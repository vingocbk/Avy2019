package com.lib.collageview.helpers.bitmap;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;

import com.lib.collageview.helpers.Flog;

/**
 * Created by vutha on 3/23/2017.
 */

public class CanvasUtils {

    private static String TAG = CanvasUtils.class.getSimpleName();

    public static boolean clipPath(Canvas canvas, Path path, Region.Op op) {
        boolean clipped = true;
        try {
            canvas.clipPath(path, op);
        } catch (UnsupportedOperationException e) {
            Flog.e(TAG, "clipPath() not supported");
            clipped = false;
        }
        return clipped;
    }

    public static boolean clipPath(Canvas canvas, Path path) {
        boolean clipped = true;
        try {
            canvas.clipPath(path);
        } catch (UnsupportedOperationException e) {
            Flog.e(TAG, "clipPath() not supported");
            clipped = false;
        }
        return clipped;
    }

    /**
     * Unsupported Drawing Operations: clipRect() with rotation/perspective
     *
     * @param canvas The canvas for drawing action.
     * @param x      The left coordinate
     * @param y      The top coordinate
     * @param w      The right coordinate
     * @param h      The bottom coordinate
     * @return Can clip or not.
     */
    public static boolean clipRect(Canvas canvas, int x, int y, int w, int h) {
        boolean clipped = true;
        try {
            canvas.clipRect(x, y, w, h);
        } catch (UnsupportedOperationException e) {
            Flog.e(TAG, "clipPath() not supported");
            clipped = false;
        }
        return clipped;
    }

    /**
     * Path.rewind => Just empty the contents of the Path, Returns to make .clear() from a list
     * Path.reset => Your Path is reseter. Returns to create one: new Path()
     * */
}
