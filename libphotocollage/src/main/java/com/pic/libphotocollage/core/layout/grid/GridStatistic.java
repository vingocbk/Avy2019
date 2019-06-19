package com.pic.libphotocollage.core.layout.grid;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Adm on 8/9/2016.
 */
public class GridStatistic {

    private static int mSize;
    private static String mName;

    public static float[][] getGridRectByName(int numberOfPhotos, String nameLayout, float spacing) {
        mSize = numberOfPhotos;
        mName = nameLayout;
        switch (numberOfPhotos) {
            case 2:
                return NewGridLayout2.getGridRectMargined(nameLayout, spacing);
            case 3:
                return NewGridLayout3.getGridRectMargined(nameLayout, spacing);
            case 4:
                return NewGridLayout4.getGridRectMargined(nameLayout, spacing);
            case 5:
                return NewGridLayout5.getGridRectMargined(nameLayout, spacing);
            case 6:
                return NewGridLayout6.getGridRectMargined(nameLayout, spacing);
            default:
                return NewGridLayout2.getGridRectMargined(nameLayout, spacing);
        }
    }

    public static ArrayList<Path> getGridPointPaths(Rect drawRect, float spacing) {
        float[][][] currentPreset = getGridPointsByName(mSize, mName, spacing);
        ArrayList<Path> mDrawPaths = null;
        if (mDrawPaths == null) {
            mDrawPaths = new ArrayList<Path>();
        }

        float viewWidth = drawRect.width(), viewHeight = drawRect.height(),
                leftPadding = drawRect.left, topPadding = drawRect.top;

        for (int idx = 0; idx < currentPreset.length; idx++) {
            Path path = getGridPath(currentPreset[idx], viewWidth, viewHeight, leftPadding, topPadding, idx);
            mDrawPaths.add(path);
            path.close();
        }

        return mDrawPaths;
    }

    public static float[][][] getGridPointsByName(int numberOfPhotos, String nameLayout, float spacing) {
        switch (numberOfPhotos) {
            case 2:
                return NewGridLayout2.getGridPointsMarginedByName(nameLayout, spacing);
            case 3:
                return NewGridLayout3.getGridPointsMarginedByName(nameLayout, spacing);
            case 4:
                return NewGridLayout4.getGridPointsMarginedByName(nameLayout, spacing);
            case 5:
                return NewGridLayout5.getGridPointsMarginedByName(nameLayout, spacing);
            case 6:
                return NewGridLayout6.getGridPointsMarginedByName(nameLayout, spacing);
            default:
                return NewGridLayout2.getGridPointsMarginedByName(nameLayout, spacing);
        }
    }

    public static boolean[][] getGridCurvedCheckByName(int numberOfPhotos, String nameLayout) {
        switch (numberOfPhotos) {
            case 2:
                return NewGridLayout2.getGridCurvedCheck(nameLayout);
            case 3:
                return NewGridLayout3.getGridCurvedCheck(nameLayout);
            case 4:
                return NewGridLayout4.getGridCurvedCheck(nameLayout);
            case 5:
                return NewGridLayout5.getGridCurvedCheck(nameLayout);
            case 6:
                return NewGridLayout6.getGridCurvedCheck(nameLayout);
            default:
                return NewGridLayout2.getGridCurvedCheck(nameLayout);
        }
    }

    public static Path getGridPath(float[][] points, float viewWidth, float viewHeight,
                                   float leftPadding, float topPadding, int idx) {
        int i = 0;
        Path path = new Path();
        path.moveTo(points[0][0] * viewWidth + leftPadding, points[0][1]
                * viewHeight + topPadding);
        do {
            i++;
            int index = i % points.length;
//            Flog.i((index) + "-");
//            path.lineTo(points[index][0] * viewWidth + leftPadding, points[index][1]
//                    * viewHeight + topPadding);
            if (getGridCurvedCheckByName(mSize, mName) != null && getGridCurvedCheckByName(mSize, mName)[idx][index]) {
                path.quadTo(points[index][0] * viewWidth + leftPadding, points[index][1]
                        * viewHeight + topPadding, points[index + 1][0] * viewWidth + leftPadding, points[index + 1][1]
                        * viewHeight + topPadding);
                i++;
            } else {
                path.lineTo(points[index][0] * viewWidth + leftPadding, points[index][1]
                        * viewHeight + topPadding);
            }
        } while (i % points.length != 0);
        return path;
    }
}
