package com.pic.libphotocollage.core.util.zoom;

import android.view.MotionEvent;

public class PhotoPinchZoom {

    private final float TOUCH_TOLERANCE = 2;
    private boolean isZoom = false;
    private MultiTouchInfo mCurr = null;
    private MultiTouchInfo mPrev = null;
    private boolean mIsMultiTouchStart;
    private boolean mIsMultiTouchEnd;
    private boolean mWithSingleTouch = true;
    private OnCollagePinchZoomCallback mListener = null;

    @SuppressWarnings("deprecation")
    public boolean onTouchEvent(MotionEvent ev) {
        boolean ret = true;
        final int action = ev.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
        }
        if (mPrev != null && mCurr != null) {
            if (!mIsMultiTouchStart) {
                if ((action == MotionEvent.ACTION_POINTER_1_DOWN || action == MotionEvent.ACTION_POINTER_2_DOWN)
                        && ev.getPointerCount() >= 2) {
                    reset();
                    mPrev.event = MotionEvent.obtain(ev);
                    set(ev);
                    mIsMultiTouchStart = true;
                    mIsMultiTouchEnd = false;
                } else {
                    if (!mIsMultiTouchEnd) {
                        if (action == MotionEvent.ACTION_UP
                                && ev.getPointerCount() == 1) {
                            mIsMultiTouchEnd = true;
                        }
                    } else {
                        if (mWithSingleTouch) {
                            if (mListener != null) {
                                if (isZoom)
                                    isZoom = false;
                                mListener.onSingleTouchEvent(ev);
                            }
                        }
                    }
                }
            } else {
                switch (action) {
                    case MotionEvent.ACTION_POINTER_1_UP:
                    case MotionEvent.ACTION_POINTER_2_UP:
                        set(ev);
                        mIsMultiTouchStart = false;
                        reset();
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        mIsMultiTouchStart = false;
                        reset();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (ev.getPointerCount() < 2)
                            break;
                        set(ev);
                        double prevDstX = mPrev.event.getX(0) - mPrev.event.getX(1);
                        double prevDstY = mPrev.event.getY(0) - mPrev.event.getY(1);
                        double currDstX = mCurr.event.getX(0) - mCurr.event.getX(1);
                        double currDstY = mCurr.event.getY(0) - mCurr.event.getY(1);
                        int prevDst = (int) Math.sqrt((prevDstY * prevDstY) + (prevDstX * prevDstX));
                        int currDst = (int) Math.sqrt((currDstY * currDstY) + (currDstX * currDstX));
                        if (Math.abs(currDst - prevDst) > TOUCH_TOLERANCE) {
                            isZoom = true;
                            mListener.onPinchZoom(ev, (currDst - prevDst));
                        }
                        mPrev.event.recycle();
                        mPrev.event = MotionEvent.obtain(ev);
                        break;
                }
            }
        }
        return ret;
    }

    public boolean isZoom() {
        return isZoom;
    }

    private void set(MotionEvent curr) {
        if (mCurr.event != null) {
            mCurr.event.recycle();
        }
        mCurr.event = MotionEvent.obtain(curr);
    }

    private void reset() {
        if (mPrev != null) {
            if (mPrev.event != null) {
                mPrev.event.recycle();
                mPrev.event = null;
            }
        }
        if (mCurr != null) {
            if (mCurr.event != null) {
                mCurr.event.recycle();
                mCurr.event = null;
            }
        }
    }

    public void init(OnCollagePinchZoomCallback touch) {
        mListener = touch;
        mIsMultiTouchStart = false;
        mIsMultiTouchEnd = false;
        mPrev = new MultiTouchInfo();
        mCurr = new MultiTouchInfo();
    }

    public interface OnCollagePinchZoomCallback {
        void onPinchZoom(MotionEvent ev, int diffDst);

        boolean onSingleTouchEvent(MotionEvent ev);
    }

    private class MultiTouchInfo {
        public MotionEvent event;
    }
}
