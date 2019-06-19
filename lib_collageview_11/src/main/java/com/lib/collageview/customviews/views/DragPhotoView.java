package com.lib.collageview.customviews.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import androidx.core.view.MotionEventCompat;
import com.lib.collageview.CollageView;
import com.lib.collageview.helpers.Flog;
import com.lib.collageview.helpers.bitmap.BitmapHelper;

/**
 * Created by vutha on 3/27/2017.
 */
public class DragPhotoView extends BaseView {

    private static final java.lang.String TAG = DragPhotoView.class.getSimpleName();
    private static final int DRAG_ALPHA_PAINT = 125;
    private Paint mDragPaint;
    private float mMoveToCenterX, mMoveToCenterY;
    private OnPhotoSwapListener mOnSwapListener;
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private RectF mRectF;
    private float mLastX, mLastY;

    public DragPhotoView(CollageView collageView) {
        super(collageView);
        init();
    }

    private void init() {
        mDragPaint = new Paint();
        mDragPaint.setAlpha(DRAG_ALPHA_PAINT);

        mMatrix = new Matrix();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, mMatrix, mDragPaint);
        }
    }

    @Override
    public void release() {
        if (mDragPaint != null) {
            mDragPaint.reset();
            mDragPaint = null;
        }
        if (mOnSwapListener != null)
            mOnSwapListener = null;
        if (mBitmap != null) {
            mBitmap = BitmapHelper.recycle(mBitmap);
        }
        if (mMatrix != null) {
            mMatrix.reset();
            mMatrix = null;
        }
        if (mRectF != null) {
            mRectF.setEmpty();
            mRectF = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX(0) - mMoveToCenterX;
                float y = event.getY(0) - mMoveToCenterY;
                mMatrix.postTranslate(x - mLastX, y - mLastY);
                mLastX = x;
                mLastY = y;
                mCollageView.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mBitmap = null;
                if (mOnSwapListener != null) {
                    mOnSwapListener.onSwapDone(event);
                }
                break;
        }
        return true;
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        mMatrix.reset();
        mBitmap = bitmap;
    }

    public void setBitmap(Bitmap bitmap, float touchedX, float touchedY) {
        mMatrix.reset();
        mBitmap = bitmap;
        Flog.d(TAG, "rect=" + mRectF);
        float scaleDown = mRectF.width() / mBitmap.getWidth();
        if (scaleDown > mRectF.height() / mBitmap.getHeight()) {
            scaleDown = mRectF.height() / mBitmap.getHeight();
        }

        mMoveToCenterX = mBitmap.getWidth() * scaleDown / 2;
        mMoveToCenterY = mBitmap.getHeight() * scaleDown / 2;

        mLastX = touchedX - mMoveToCenterX;
        mLastY = touchedY - mMoveToCenterY;

        mMatrix.setScale(scaleDown, scaleDown);
        mMatrix.postTranslate(mLastX, mLastY);
    }

    public void setRectF(RectF rectF) {
        mRectF = new RectF(rectF);
    }

    public DragPhotoView setPhotoSwapListener(OnPhotoSwapListener swapPhotosListener) {
        mOnSwapListener = swapPhotosListener;
        return this;
    }

    /**
     * Interface to interact between DragPhotoView ACTION_UP and CollageView.
     */
    public interface OnPhotoSwapListener {
        /**
         * Called when droping photoview-shadow at the specific region on collageview.
         *
         * @param event event of DragPhotoView.
         */
        void onSwapDone(MotionEvent event);
    }
}
