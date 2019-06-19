package bsoft.hoavt.photoproject.lib_textcollage.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.ViewGroup;

import bsoft.hoavt.photoproject.lib_textcollage.helpers.Flog;

/**
 * Created by vutha on 3/22/2017.
 */

public abstract class BaseView {

    private static final String TAG = BaseView.class.getSimpleName();
    protected ViewGroup mParentView;
    protected Context mContext;
    protected float mWidth, mHeight;
    protected float mPaddingTop, mPaddingLeft;
    /**
     * The index of photoview/stickerview in list of them.
     * Update it when swap with other elements in list.
     */
    protected int mId = -1;

    protected BaseView(ViewGroup collageView) {
        mParentView = collageView;
        mContext = collageView.getContext();
        mWidth = collageView.getWidth();
        mHeight = collageView.getHeight();
        mPaddingTop = collageView.getPaddingTop();
        mPaddingLeft = collageView.getPaddingLeft();
        Flog.d(TAG, "BaseView1: w=" + mWidth + "_h=" + mHeight + "_pTop=" + mPaddingTop + "_mLeft=" + mPaddingLeft);
    }

    protected void setViewSize(float w, float h) {
        mWidth = w;
        mHeight = h;
        mPaddingTop = mParentView.getPaddingTop();
        mPaddingLeft = mParentView.getPaddingLeft();
        Flog.d(TAG, "BaseView2: w=" + mWidth + "_h=" + mHeight + "_pTop=" + mPaddingTop + "_mLeft=" + mPaddingLeft);
    }

    public float getWidth() {
        return mWidth;
    }

    public float getHeight() {
        return mHeight;
    }

    public abstract void onDraw(Canvas canvas);

    public abstract void release();

    public abstract boolean onTouchEvent(MotionEvent event);

    public abstract void setBitmap(Bitmap bmp);

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
