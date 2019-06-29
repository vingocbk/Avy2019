package com.lib.collageview.customviews.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.lib.collageview.CollageView;


/**
 * Created by vutha on 3/22/2017.
 */

public abstract class BaseView {

   protected CollageView mCollageView;
    protected Context mContext;
    /**
     * The index of photoview/stickerview in list of them.
     * Update it when swap with other elements in list.
     */
    protected int mIndex = -1;

    protected BaseView(CollageView collageView) {
        mCollageView = collageView;
        mContext = collageView.getContext();
    }



    public abstract void onDraw(Canvas canvas,int index);

    public abstract void release();

    public abstract boolean onTouchEvent(MotionEvent event);

    public abstract void setBitmap(Bitmap bmp);

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }
}
