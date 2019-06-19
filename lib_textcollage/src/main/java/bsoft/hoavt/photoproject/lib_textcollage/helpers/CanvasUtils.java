package bsoft.hoavt.photoproject.lib_textcollage.helpers;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import static android.view.View.LAYER_TYPE_SOFTWARE;

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
     * Canvas.clipPath() support with hardware acceleration has been reintroduced since API 18.
     * The best way to work around the problem is calling setLayerType(View.LAYER_TYPE_SOFTWARE, null)
     * only when you are running on API from 11 to 17:
     * */
    public static boolean supportClipMethod(ViewGroup viewGroup) {
        /**
         * Canvas.clipPath() support with hardware acceleration has been reintroduced since API 18: JELLY_BEAN_MR2
         * The best way to work around the problem is calling setLayerType(View.LAYER_TYPE_SOFTWARE, null)
         * only when you are running on API from 11 to 17:
         * */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Flog.d(TAG, "hardware acceleration");
            viewGroup.setLayerType(LAYER_TYPE_SOFTWARE, null);
            return true;
        }
        return false;
    }

    /**
     * Canvas.clipPath() support with hardware acceleration has been reintroduced since API 18.
     * The best way to work around the problem is calling setLayerType(View.LAYER_TYPE_SOFTWARE, null)
     * only when you are running on API from 11 to 17:
     * */
    public static boolean supportClipMethod(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Flog.d(TAG, "hardware acceleration");
            view.setLayerType(LAYER_TYPE_SOFTWARE, null);
            return true;
        }
        return false;
    }

    /**
     * Path.rewind => Just empty the contents of the Path, Returns to make .clear() from a list
     * Path.reset => Your Path is reseter. Returns to create one: new Path()
     * */
}
