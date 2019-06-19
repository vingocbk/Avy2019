package com.pic.libphotocollage.core.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.pic.libphotocollage.core.CollageView;
import com.pic.libphotocollage.core.util.Flog;

/**
 * Created by thuck on 8/18/2016.
 */
public abstract class BaseItem {

    public static final int MOVE_UP = 0;
    public static final int MOVE_DOWN = 1;
    public static final int MOVE_LEFT = 2;
    public static final int MOVE_RIGHT = 3;
    public static final int MOVE_THRESHOLD = 4;
    public static final float STEP_MOVE = 10f;
    public static final float STEP_ROTATE = 5f;
    public static final float STEP_SCALE_IN = 1.1f;
    public static final float STEP_SCALE_OUT = 0.9f;
    protected static final float BITMAP_SCALE = 0.7f;
    protected static final float pointerLimitDis = 20f;
    protected static final float pointerZoomCoeff = 0.09f;
    protected static final float BOUNDS_SIZE_SCREEN = 50.0f;
    protected static final float moveLimitDis = 10f;
    private static final String TAG = "Base_Item";
    protected ItemType itemType;
    protected CollageView mCollageView;
    protected Context mContext;
    protected Bitmap deleteBitmap;
    protected Bitmap flipVBitmap;
    protected Bitmap topBitmap;
    protected Bitmap resizeBitmap;
    protected Bitmap mBitmap;
    protected Rect dst_delete;
    protected Rect dst_resize;
    protected Rect dst_flipV;
    protected Rect dst_top;
    protected int deleteBitmapWidth;
    protected int deleteBitmapHeight;
    protected int resizeBitmapWidth;
    protected int resizeBitmapHeight;
    //水平镜像
    protected int flipVBitmapWidth;
    protected int flipVBitmapHeight;
    //置顶
    protected int topBitmapWidth;
    protected int topBitmapHeight;
    protected Paint localPaint;
    //    protected int mScreenWidth, mScreenHeight;
    protected PointF mid = new PointF();
    //    protected OperationListener operationListener;
    protected float lastRotateDegree;
    //是否是第二根手指放下
    protected boolean isPointerDown = false;
    /**
     * 对角线的长度
     */
    protected float lastLength;
    protected boolean isInResize = false;
    protected Matrix matrix = new Matrix();
    /**
     * 是否在四条线内部
     */
    protected boolean isInSide;
    protected float lastX, lastY;
    protected boolean isMove = false;
    protected boolean isDown = false;
    //是否移动
    //是否抬起手
    protected boolean isUp = false;
    //是否在顶部
    protected boolean isTop = true;
    protected boolean isInBitmap;
    /**
     * 是否在编辑模式
     */
    protected boolean isInEdit = true;
    protected float MIN_SCALE = 0.6f;
    protected float MAX_SCALE = 1.5f;
    protected double halfDiagonalLength;
    protected float originalWidth = 0;
    protected boolean isDeleted = false;
    protected DisplayMetrics dm;
    protected float oldDis;
    protected boolean isHorizonMirror = false;
    protected OnItemInteractListener listener;
    private boolean mNotTranslate = false;
    private boolean mNotScale = false;

    protected BaseItem(CollageView collageView) {
        this.mCollageView = collageView;
        this.mContext = collageView.getContext();
    }



    public ItemType getItemType() {
        return itemType;
    }

    public boolean isInEdit() {
        return isInEdit;
    }

    public void setInEdit(boolean isInEdit) {
        this.isInEdit = isInEdit;
    }

    public boolean isItemDeleted() {
        return isDeleted;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void draw(Canvas canvas) {

    }

    public void invalidateRatio() {
        int[] oldSize = mCollageView.getOldSize();
        ViewGroup.LayoutParams params = mCollageView.getLayoutParams();
//        Log.d(TAG, "CollageWidth = " + mCollageView.getWidth() + " CollageHeight = " + mCollageView.getHeight());
        float ratioWidth = (float) params.width / oldSize[0];
        float ratioHeight = (float) params.height / oldSize[1];
        float[] values = new float[9];
        matrix.getValues(values);
        float X_TRANS = values[Matrix.MTRANS_X];
        float Y_TRANS = values[Matrix.MTRANS_Y];
//        Log.d(TAG, "X_TRANS =" + X_TRANS + " Y_TRANS = " + Y_TRANS);

        values[Matrix.MTRANS_X] *= ratioWidth;
        values[Matrix.MTRANS_Y] *= ratioHeight;
        matrix.setValues(values);
        X_TRANS = values[Matrix.MTRANS_X];
        Y_TRANS = values[Matrix.MTRANS_Y];
//        Log.d(TAG, "Change:  X_TRANS =" + X_TRANS + " Y_TRANS = " + Y_TRANS);
        mCollageView.invalidate();

    }

    public boolean isInBitmap(MotionEvent event) {
        float[] arrayOfFloat1 = new float[9];
        this.matrix.getValues(arrayOfFloat1);
        //左上角
        float f1 = 0.0F * arrayOfFloat1[0] + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
        float f2 = 0.0F * arrayOfFloat1[3] + 0.0F * arrayOfFloat1[4] + arrayOfFloat1[5];
        //右上角
        float f3 = arrayOfFloat1[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
        float f4 = arrayOfFloat1[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat1[4] + arrayOfFloat1[5];
        //左下角
        float f5 = 0.0F * arrayOfFloat1[0] + arrayOfFloat1[1] * this.mBitmap.getHeight() + arrayOfFloat1[2];
        float f6 = 0.0F * arrayOfFloat1[3] + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];
        //右下角
        float f7 = arrayOfFloat1[0] * this.mBitmap.getWidth() + arrayOfFloat1[1] * this.mBitmap.getHeight() + arrayOfFloat1[2];
        float f8 = arrayOfFloat1[3] * this.mBitmap.getWidth() + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];

        float[] arrayOfFloat2 = new float[4];
        float[] arrayOfFloat3 = new float[4];
        //确定X方向的范围
        arrayOfFloat2[0] = f1;//左上的左
        arrayOfFloat2[1] = f3;//右上的右
        arrayOfFloat2[2] = f7;//右下的右
        arrayOfFloat2[3] = f5;//左下的左
        //确定Y方向的范围
        arrayOfFloat3[0] = f2;//左上的上
        arrayOfFloat3[1] = f4;//右上的上
        arrayOfFloat3[2] = f8;
        arrayOfFloat3[3] = f6;
        return pointInRect(arrayOfFloat2, arrayOfFloat3, event.getX(0), event.getY(0));
    }

    private boolean pointInRect(float[] xRange, float[] yRange, float x, float y) {
        //四条边的长度
        double a1 = Math.hypot(xRange[0] - xRange[1], yRange[0] - yRange[1]);
        double a2 = Math.hypot(xRange[1] - xRange[2], yRange[1] - yRange[2]);
        double a3 = Math.hypot(xRange[3] - xRange[2], yRange[3] - yRange[2]);
        double a4 = Math.hypot(xRange[0] - xRange[3], yRange[0] - yRange[3]);
        //待检测点到四个点的距离
        double b1 = Math.hypot(x - xRange[0], y - yRange[0]);
        double b2 = Math.hypot(x - xRange[1], y - yRange[1]);
        double b3 = Math.hypot(x - xRange[2], y - yRange[2]);
        double b4 = Math.hypot(x - xRange[3], y - yRange[3]);

        double u1 = (a1 + b1 + b2) / 2;
        double u2 = (a2 + b2 + b3) / 2;
        double u3 = (a3 + b3 + b4) / 2;
        double u4 = (a4 + b4 + b1) / 2;

        //矩形的面积
        double s = a1 * a2;
        double ss = Math.sqrt(u1 * (u1 - a1) * (u1 - b1) * (u1 - b2))
                + Math.sqrt(u2 * (u2 - a2) * (u2 - b2) * (u2 - b3))
                + Math.sqrt(u3 * (u3 - a3) * (u3 - b3) * (u3 - b4))
                + Math.sqrt(u4 * (u4 - a4) * (u4 - b4) * (u4 - b1));
        return Math.abs(s - ss) < 0.5;
    }

    protected boolean isInButton(MotionEvent event, Rect rect) {
        int left = rect.left;
        int right = rect.right;
        int top = rect.top;
        int bottom = rect.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    protected boolean isInResize(MotionEvent event) {
        int left = -20 + this.dst_resize.left;
        int top = -20 + this.dst_resize.top;
        int right = 20 + this.dst_resize.right;
        int bottom = 20 + this.dst_resize.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    protected void midPointToStartPoint(MotionEvent event) {
//        Log.d(TAG, "midPointToStartPoint: event: x="+event.getX(0)+"_y="+event.getY(0));
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = f1 + event.getX(0);
        float f4 = f2 + event.getY(0);
        mid.set(f3 / 2, f4 / 2);
    }

    protected void midPointToStartPoint(float f7, float f8) {
//        Log.d(TAG, "midPointToStartPoint: event: x="+event.getX(0)+"_y="+event.getY(0));
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = f1 + f7;
        float f4 = f2 + f8;
        mid.set(f3 / 2, f4 / 2);
    }

    protected float rotationToStartPoint(float f7, float f8) {

        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float x = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float y = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        double arc = Math.atan2(f8 - y, f7 - x);

        return (float) Math.toDegrees(arc);
    }

    protected void midDiagonalPoint(PointF paramPointF) {
        float[] arrayOfFloat = new float[9];
        this.matrix.getValues(arrayOfFloat);
        float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        float f5 = f1 + f3;
        float f6 = f2 + f4;
        paramPointF.set(f5 / 2.0F, f6 / 2.0F);
    }

    protected float rotationToStartPoint(MotionEvent event) {

        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float x = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float y = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        double arc = Math.atan2(event.getY(0) - y, event.getX(0) - x);

        return (float) Math.toDegrees(arc);
    }

    protected float diagonalLength(MotionEvent event) {
        float diagonalLength = (float) Math.hypot(event.getX(0) - mid.x, event.getY(0) - mid.y);
        return diagonalLength;
    }

    protected float diagonalLength(float f7, float f8) {
        float diagonalLength = (float) Math.hypot(f7 - mid.x, f8 - mid.y);
        return diagonalLength;
    }

    /**
     * Determine the space between the first two fingers
     */
    protected float spacing(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isInEdit || isDeleted) {
            return false;
        }
        int action = MotionEventCompat.getActionMasked(event);
        boolean handled = true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isInButton(event, dst_delete)) {
                    isDeleted = true;
                    //dismiss because of press delete btn"); 4136:bvcv  4091:aaa
                    mCollageView.post(new Runnable() {
                        @Override
                        public void run() {
                            int index = mCollageView.getListItem().indexOf(BaseItem.this);
                            mCollageView.getListItem().remove(index);
                            release();
                        }
                    });

                    if (listener != null) {
                        listener.onItemDeleted(this);
                    }
                    return false;
                } else if (isInResize(event)) {
                    isInResize = true;
                    lastRotateDegree = rotationToStartPoint(event);
                    midPointToStartPoint(event);
                    lastLength = diagonalLength(event);

                } else if (isInButton(event, dst_flipV)) {
                    Flog.i("dst_flipV");
                    //水平镜像
                    PointF localPointF = new PointF();
                    midDiagonalPoint(localPointF);
                    matrix.postScale(-1.0F, 1.0F, localPointF.x, localPointF.y);
                    isHorizonMirror = !isHorizonMirror;
                } else if (isInBitmap(event)) {
                    Flog.i("InSide");
                    isInSide = true;
                    lastX = event.getX(0);
                    lastY = event.getY(0);
                    isMove = false;
                    isPointerDown = false;
                    return true;
                } else {
                    handled = false;
                    setInEdit(false);
                    //TODO: hide keyboard
                    //dismiss because of click outside textview
                    mCollageView.invalidate();
                    if (listener != null) {
                        listener.onItemUnselected(this);
                    }
                    return handled;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //TODO: hide keyboard
//                mCollageView.dismissKeyboard();

                if (spacing(event) > pointerLimitDis) {
                    oldDis = spacing(event);
                    isPointerDown = true;
                    midPointToStartPoint(event);
                } else {
                    isPointerDown = false;
                }
                isInSide = false;
                isInResize = false;
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.d(TAG, "ACTION_MOVE bubble text");

                if (isPointerDown) {
                    float scale;
                    float disNew = spacing(event);
                    if (disNew == 0 || disNew < pointerLimitDis) {
                        scale = 1;
                    } else {
                        scale = disNew / oldDis;
                        //缩放缓慢
                        scale = (scale - 1) * pointerZoomCoeff + 1;
                    }
                    float scaleTemp = (scale * Math.abs(dst_flipV.left - dst_resize.left)) / originalWidth;
                    if (((scaleTemp <= MIN_SCALE)) && scale < 1 ||
                            (scaleTemp >= MAX_SCALE) && scale > 1) {
                        scale = 1;
                    } else {
                        lastLength = diagonalLength(event);
                    }
                    matrix.postScale(scale, scale, mid.x, mid.y);

                } else if (isInResize) {
                    matrix.postRotate((rotationToStartPoint(event) - lastRotateDegree) * 2, mid.x, mid.y);
                    lastRotateDegree = rotationToStartPoint(event);

                    float scale = diagonalLength(event) / lastLength;

                    if (((diagonalLength(event) / halfDiagonalLength <= MIN_SCALE)) && scale < 1 ||
                            (diagonalLength(event) / halfDiagonalLength >= MAX_SCALE) && scale > 1) {
                        scale = 1;
                        if (!isInResize(event)) {
                            isInResize = false;
                        }
                    } else {
                        lastLength = diagonalLength(event);
                    }
                    matrix.postScale(scale, scale, mid.x, mid.y);

//                    invalidate();
                } else if (isInSide) {
                    float x = event.getX(0);
                    float y = event.getY(0);
                    //判断手指抖动距离 加上isMove判断 只要移动过 都是true
                    if (!isMove && Math.abs(x - lastX) < moveLimitDis
                            && Math.abs(y - lastY) < moveLimitDis) {
                        isMove = false;
                    } else {
                        if (!checkOverBounds(event)) {
                            isMove = true;
                            matrix.postTranslate(x - lastX, y - lastY);
                            lastX = x;
                            lastY = y;
                        } else {
                            isMove = false;
                        }
                    }
                    if (listener != null && isMove) {
                        listener.onMovingItem(this);
                    }

                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (listener != null && isInSide && !isMove && isInEdit) {
//                    firstClick = false;
                    listener.onItemClicked(this, true);
                }
                if (listener != null && isMove) {
                    listener.onStopMovingItem(this);
                }
                isInResize = false;
                isInSide = false;
                isPointerDown = false;
                isUp = true;
                isMove = false;
                break;

        }
        if (handled) {
            this.isInEdit = true;
        }

        return handled;
    }

    protected boolean checkOverBounds(MotionEvent event) {
        float rx = event.getRawX();
        float ry = event.getRawY();
        int[] l = new int[2];
        mCollageView.getLocationOnScreen(l);
        int x = l[0];
        int y = l[1];
        int w = mCollageView.getWidth();
        int h = mCollageView.getHeight();

        if (rx < x || rx > x + w || ry < y || ry > y + h) {
            return true;
        }
        return false;
    }

    protected boolean checkOverBounds(int direction, float dx, float dy) {
        float rx = dx;
        float ry = dy;
        int[] l = new int[2];
        mCollageView.getLocationOnScreen(l);
        int x = 0;
        int y = 0;
        int w = mCollageView.getWidth();
        int h = mCollageView.getHeight();

        switch (direction) {
            case MOVE_UP:
                if (ry < y)
                    return true;
                break;
            case MOVE_DOWN:
                if (ry > y + h)
                    return true;
                break;
            case MOVE_LEFT:
                if (rx < x)
                    return true;
                break;
            case MOVE_RIGHT:
                if (rx > x + w)
                    return true;
                break;
            default:
                break;
        }
//        if (rx < x || rx > x + w || ry < y || ry > y + h) {
//            return true;
//        }
        return false;
    }

    public abstract void release();

    public abstract float[] getCurrentPosition();

    public void setOnItemInteractListener(OnItemInteractListener listener) {
        this.listener = listener;
    }

    public OnItemInteractListener getListener() {
        return listener;
    }

    public void postMatrixRotated(float angel) {
//        float[] values = new float[9];
//        matrix.getValues(values);
//        float X_TRANS = values[Matrix.MTRANS_X];
//        float Y_TRANS = values[Matrix.MTRANS_Y];
//        float X_SCALE = values[Matrix.MSCALE_X];
//        float Y_SCALE = values[Matrix.MSCALE_Y];
//        float X_SKNEW = values[Matrix.MSKEW_X];
//        float Y_SKNEW = values[Matrix.MSKEW_Y];
//        Log.d(TAG, "X_TRANS =" + X_TRANS + " Y_TRANS = " + Y_TRANS);
//        Log.d(TAG, "X_SCALE =" + X_SCALE + " Y_SCALE = " + Y_SCALE);
//        Log.d(TAG, "X_SKNEW =" + X_SKNEW + " Y_SKNEW = " + Y_SKNEW);
//        Log.d(TAG, "mBitmap.getWidth()/2="+mBitmap.getWidth()/2);
//        Log.d(TAG, "mBitmap.getHeight()/2="+mBitmap.getHeight()/2);
//        Log.d(TAG, "dst_resize=" + dst_resize);
//        float leftMost = (dst_top.right + dst_top.left)*1.0f/2;
//        float topMost = (dst_top.bottom + dst_top.top)*1.0f/2f;
//        float rightMost = (dst_resize.right + dst_resize.left)*1.0f/2f;
//        float bottomMost = (dst_resize.bottom + dst_resize.top)*1.0f/2f;
//        float centerX = Math.abs(rightMost-leftMost) / 2;
//        float centerY = Math.abs(bottomMost-topMost)/2;
//        Log.d(TAG, "center: x="+centerX+"_y="+centerY);
//        Log.d(TAG, "most: l="+leftMost+"_t="+topMost+"_r="+rightMost+"_b="+bottomMost);
//        Log.d(TAG, "------------------");

//        matrix.preRotate(angel-lastRotateDegree, Math.abs(rightMost-leftMost) / 2, Math.abs(bottomMost-topMost)/2);
//        matrix.preRotate(angel);
//        matrix.postScale(1f, 1f);
//        matrix.postTranslate(trans_x, trans_y);

        initMidPoint();
        matrix.postRotate(angel * 1.0f, mid.x, mid.y);
    }

    private void initMidPoint() {
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
//        float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
//        float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
//        float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
//        float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
//        float f5 = 0.0F * arrayOfFloat[0] + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
//        float f6 = 0.0F * arrayOfFloat[3] + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        float f7 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f8 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
//        Log.d(TAG, "f7: "+f7+"_f8: "+f8);
//        Log.d(TAG, "angel: " + angel + "_lastRotateDegree: " + lastRotateDegree);
        midPointToStartPoint(f7, f8);
    }

    public void postMatrixTranslated(int direction) {
        initMidPoint();
        if (checkOverBounds(direction, mid.x, mid.y)) {
            mNotTranslate = true;
            return;
        }
        mNotTranslate = false;

        switch (direction) {
            case MOVE_UP:
                matrix.postTranslate(0, -STEP_MOVE);
                break;
            case MOVE_DOWN:
                matrix.postTranslate(0, STEP_MOVE);
                break;
            case MOVE_LEFT:
                matrix.postTranslate(-STEP_MOVE, 0);
                break;
            case MOVE_RIGHT:
                matrix.postTranslate(STEP_MOVE, 0);
                break;
            default:
                break;
        }
    }

    public void postMatrixScaled(float scale) {
        initMidPoint();

        float f7 = (dst_resize.right + dst_resize.left) / 2;
        float f8 = (dst_resize.top + dst_resize.bottom) / 2;
        if (((diagonalLength(f7, f8) / halfDiagonalLength <= MIN_SCALE)) && scale < 1 ||
                (diagonalLength(f7, f8) / halfDiagonalLength >= MAX_SCALE) && scale > 1) {
            mNotScale = true;
            return;
        }
        mNotScale = false;
        matrix.postScale(scale, scale, mid.x, mid.y);
    }

    public enum ItemType {
        TEXT, STICKER
    }

    public interface OnItemInteractListener {
        void onMovingItem(BaseItem item);

        void onStopMovingItem(BaseItem item);

        void onItemDeleted(BaseItem item);

        void onItemClicked(BaseItem item, boolean isFirstClick);

        void onItemUnselected(BaseItem item);
    }

    public boolean isNotTranslate() {
        return mNotTranslate;
    }

    public boolean isNotScale() {
        return mNotScale;
    }
}
