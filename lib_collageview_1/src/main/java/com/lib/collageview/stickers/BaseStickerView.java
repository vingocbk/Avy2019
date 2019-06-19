package com.lib.collageview.stickers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import com.lib.collageview.CollageView;
import com.lib.collageview.R;
import com.lib.collageview.customviews.views.BaseView;
import com.lib.collageview.helpers.Flog;
import com.lib.collageview.helpers.bitmap.BitmapHelper;
import com.lib.collageview.interfaces.StickerViewListener;
import com.lib.collageview.stickers.text.TextStickerView;

/**
 * Created by vutha on 3/29/2017.
 */

public abstract class BaseStickerView extends BaseView {

    public static final float STEP_MOVE = 10f;
    public static final float STEP_ROTATE = 5f;
    public static final float STEP_SCALE_IN = 1.05f;
    public static final float STEP_SCALE_OUT = 0.95f;
    public static final int MOVE_UP = 0;
    public static final int MOVE_DOWN = 1;
    public static final int MOVE_LEFT = 2;
    public static final int MOVE_RIGHT = 3;
    public static final int STICKER_ICON = 0;
    public static final int STICKER_TEXT = 1;
    public static final int MIN_OPACITY_VALUE = 20;
    public static final int MAX_OPACITY_VALUE = 255;
    private static final java.lang.String TAG = BaseStickerView.class.getSimpleName();
    public final float MOVE_DISTANCE_MAX = 10f;
    protected final float POINTER_DISTANCE_MAX = 20f;
    protected final float POINTEER_ZOOM_COEFF = 0.09f;
    private final float BITMAP_SCALE = 0.7f;
    protected int mStickerType = -1; // 0: Icon  &&  1: Text
    /**
     * The paint of border-line of stickerview when is selected.
     */
    protected Paint mLinePaint;
    /**
     * The paint used to draw stickerview [icon/text].
     */
    protected Paint mPaint;
    /**
     * Used for options of stickerview.
     */
    protected Bitmap deleteBitmap;
    protected Bitmap resizeBitmap;
    protected Bitmap flipVBitmap;
    protected Bitmap topBitmap;
    protected Rect dst_delete;
    protected Rect dst_resize;
    protected Rect dst_flipV;
    protected Rect dst_top;
    protected int deleteBitmapWidth;
    protected int deleteBitmapHeight;
    protected int resizeBitmapWidth;
    protected int resizeBitmapHeight;
    protected int flipVBitmapWidth;
    protected int flipVBitmapHeight;
    protected int topBitmapWidth;
    protected int topBitmapHeight;
    protected Bitmap mBitmap;
    protected Matrix mMatrix;
    protected double halfDiagonalLength;
    protected float minScale, maxScale;
    protected PointF mid = new PointF();
    protected boolean isInEdit;
    protected boolean isInResize;
    protected float lastRotateDegree;
    protected float lastLength;
    protected boolean isMovable;
    protected boolean isInBitmap;
    protected float lastX, lastY;
    protected float oldDis;
    protected float scaleValue = 1.0f;
    private boolean mNotTranslate;
    private boolean mNotScale;
    private boolean isPointerDown;
    /**
     * Listener of current stickerview. Used for callback in activity.
     */
    private StickerViewListener mStickerViewListener = null;

    protected float[] arrayOfFloat = new float[9];


    protected BaseStickerView(CollageView collageView) {
        super(collageView);
        init();
    }

    private void init() {
        mMatrix = new Matrix();
        initPaints();
        initOptions();
    }

    private void initPaints() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        mLinePaint = new Paint();
        mLinePaint.setFilterBitmap(true);
        mLinePaint.setColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(2.0f);
    }

    /**
     * Initialize options of stickerview.
     * - Decode bitmaps of options.
     * - Get width and height of them.
     * - Initialize rect-bound of options.
     */
    private void initOptions() {
        if (topBitmap == null)
            topBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_edit);
        if (deleteBitmap == null)
            deleteBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_delete);
        if (flipVBitmap == null)
            flipVBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_flip);
        if (resizeBitmap == null)
            resizeBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_resize);

        if (deleteBitmap != null) {
            deleteBitmapWidth = (int) (deleteBitmap.getWidth() * BITMAP_SCALE);
            deleteBitmapHeight = (int) (deleteBitmap.getHeight() * BITMAP_SCALE);
        }

        if (resizeBitmap != null) {
            resizeBitmapWidth = (int) (resizeBitmap.getWidth() * BITMAP_SCALE);
            resizeBitmapHeight = (int) (resizeBitmap.getHeight() * BITMAP_SCALE);
        }

        if (flipVBitmap != null) {
            flipVBitmapWidth = (int) (flipVBitmap.getWidth() * BITMAP_SCALE);
            flipVBitmapHeight = (int) (flipVBitmap.getHeight() * BITMAP_SCALE);
        }

        if (topBitmap != null) {
            topBitmapWidth = (int) (topBitmap.getWidth() * BITMAP_SCALE);
            topBitmapHeight = (int) (topBitmap.getHeight() * BITMAP_SCALE);
        }

        dst_delete = new Rect();
        dst_resize = new Rect();
        dst_flipV = new Rect();
        dst_top = new Rect();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Flog.d(TAG, "onTouchEvent: BaseStickerModel");
        if (mCollageView == null) return false;
        int action = MotionEventCompat.getActionMasked(event);
        boolean handled = true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Flog.d(TAG, "ACTION_DOWN BaseStickerModel");
                if (this instanceof TextStickerView && isInButton(event, dst_top)) {
                    if (mStickerViewListener != null) {
                        mStickerViewListener.onInputTextSticker(mIndex);
                    }
                } else if (isInButton(event, dst_delete)) {
                    Flog.d(TAG, "isInDelete");
                    if (mStickerViewListener != null) {
                        Flog.d(TAG, "idx=" + mIndex + ": this=" + this);
                        mStickerViewListener.onStickerDeleted(mIndex);
                        mCollageView.invalidate();
                    }
                } else if (isInResize(event)) {
                    isInResize = true;
                    lastRotateDegree = rotationToStartPoint(event);
                    updateMiddlePoint(event);
                    lastLength = diagonalLength(event);
                    Flog.d(TAG, "isInResize");
                } else if (isInButton(event, dst_flipV)) {
                    //水平镜像
                    PointF localPointF = new PointF();
                    midDiagonalPoint(localPointF);
                    mMatrix.postScale(-1.0F, 1.0F, localPointF.x, localPointF.y);
                    Flog.d(TAG, "isInFlip");
                } else if (isInBitmap(event)) {
                    Flog.d(TAG, "isInBitmap");
                    isInBitmap = true;
                    lastX = event.getX(0);
                    lastY = event.getY(0);
                    isMovable = false;
                    isPointerDown = false;
                    setInEdit(true);
//                    if (mStickerViewListener != null)
//                        mStickerViewListener.onTextStickerClicked(mIndex);
                } else {
                    Flog.d(TAG, "outside");
                    handled = false;
                    setInEdit(false);
                }
                mCollageView.invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (spacing(event) > POINTER_DISTANCE_MAX) {
                    oldDis = spacing(event);
                    isPointerDown = true;
                    updateMiddlePoint(event);
                } else {
                    isPointerDown = false;
                }
                isInBitmap = false;
                isInResize = false;
                break;
            case MotionEvent.ACTION_MOVE:
                Flog.d(TAG, "ACTION_MOVE BaseStickerModel");
                if (isPointerDown) {
                    float scale;
                    float disNew = spacing(event);
                    if (disNew == 0 || disNew < POINTER_DISTANCE_MAX) {
                        scale = 1;
                    } else {
                        scale = disNew / oldDis;
                        //缩放缓慢
                        scale = (scale - 1) * POINTEER_ZOOM_COEFF + 1;
                    }
                    float scaleTemp = (scale * Math.abs(dst_flipV.left - dst_resize.left)) / mBitmap.getWidth();
                    if (((scaleTemp <= minScale)) && scale < 1 ||
                            (scaleTemp >= maxScale) && scale > 1) {
                        scale = 1;
                    } else {
                        lastLength = diagonalLength(event);
                    }
                    mMatrix.postScale(scale, scale, mid.x, mid.y);
                    mCollageView.invalidate();
                } else if (isInResize) {
                    mMatrix.postRotate((rotationToStartPoint(event) - lastRotateDegree) * 2, mid.x, mid.y);
                    lastRotateDegree = rotationToStartPoint(event);

                    scaleValue = diagonalLength(event) / lastLength;

                    if (((diagonalLength(event) / halfDiagonalLength <= minScale)) && scaleValue < 1 ||
                            (diagonalLength(event) / halfDiagonalLength >= maxScale) && scaleValue > 1) {
                        scaleValue = 1;
                        if (!isInResize(event)) {
                            isInResize = false;
                        }
                    } else {
                        lastLength = diagonalLength(event);
                    }

                    mMatrix.postScale(scaleValue, scaleValue, mid.x, mid.y);
                    mCollageView.invalidate();
                } else if (isInBitmap) {
                    float x = event.getX(0);
                    float y = event.getY(0);
                    //判断手指抖动距离 加上isMove判断 只要移动过 都是true
                    if (!isMovable && Math.abs(x - lastX) < MOVE_DISTANCE_MAX
                            && Math.abs(y - lastY) < MOVE_DISTANCE_MAX) {
                        isMovable = false;
                    } else {
                        if (!exceedsBound(event)) {
                            isMovable = true;
                            mMatrix.postTranslate(x - lastX, y - lastY);
                            lastX = x;
                            lastY = y;
                        } else {
                            isMovable = false;
                        }
                    }
                    mCollageView.invalidate();
//                    if (mStickerViewListener != null)
//                        mStickerViewListener.onStickerMoving(mIndex);
                }
                if (mStickerViewListener != null)
                    mStickerViewListener.onStickerMoving(mIndex);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Flog.d(TAG, "ACTION_UP");
                isInResize = false;
                isInBitmap = false;
                isPointerDown = false;
                isMovable = false;
                if (mStickerViewListener != null)
                    mStickerViewListener.onStickerStoped(mIndex);
                mCollageView.invalidate();
                break;
        }
        setInEdit(handled);
        return handled;
    }

    protected void setDiagonalLength() {
        if (mBitmap == null) return;
        halfDiagonalLength = Math.hypot(mBitmap.getWidth(), mBitmap.getHeight()) / 2;
    }

    protected float diagonalLength(MotionEvent event) {
        float diagonalLength = (float) Math.hypot(event.getX(0) - mid.x, event.getY(0) - mid.y);
        return diagonalLength;
    }

    protected float diagonalLength(float f7, float f8) {
        float diagonalLength = (float) Math.hypot(f7 - mid.x, f8 - mid.y);
        return diagonalLength;
    }

    protected void initScaleLimit() {
        if (mCollageView == null || mBitmap == null) return;
        float minWidth = mCollageView.getWidth() / 8;
        if (mBitmap.getWidth() < minWidth) {
            minScale = 1.0f;
        } else {
            minScale = minWidth / mBitmap.getWidth();
        }

        if (mBitmap.getWidth() > mCollageView.getWidth()) {
            maxScale = 1.0f;
        } else {
            maxScale = mCollageView.getWidth() / mBitmap.getWidth();
        }
        Flog.d(TAG, "initScaleLimit: min=" + minScale + "_max=" + maxScale);
    }

    protected boolean isInButton(MotionEvent event, Rect rect) {
        if (event == null || rect == null) return false;
        int left = rect.left;
        int right = rect.right;
        int top = rect.top;
        int bottom = rect.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    protected boolean isInResize(MotionEvent event) {
        if (event == null || this.dst_resize == null) return false;
        int left = -20 + this.dst_resize.left;
        int top = -20 + this.dst_resize.top;
        int right = 20 + this.dst_resize.right;
        int bottom = 20 + this.dst_resize.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    protected void midDiagonalPoint(PointF pointF) {
        if (pointF == null || mMatrix == null || this.mBitmap == null) return;
        float[] arrayOfFloat = new float[9];
        mMatrix.getValues(arrayOfFloat);
        float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        float f5 = f1 + f3;
        float f6 = f2 + f4;
        pointF.set(f5 / 2.0F, f6 / 2.0F);
    }

    public boolean isInBitmap(MotionEvent event) {
        if (mMatrix == null || mBitmap == null) return false;
        float[] arrayOfFloat1 = new float[9];
        mMatrix.getValues(arrayOfFloat1);
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
        if (xRange.length < 4 || yRange.length < 4) return false;
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

    private boolean exceedsBound(MotionEvent event) {
        if (event == null || mCollageView == null) return false;
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

    protected boolean exceedsBound(int direction, float dx, float dy) {
        if (mCollageView == null) return false;
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
        return false;
    }

    public boolean isInEdit() {
        return isInEdit;
    }

    public void setInEdit(boolean isInEdit) {
        this.isInEdit = isInEdit;
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    public void setMatrix(Matrix matrix) {
        mMatrix = matrix;
    }

    private void postRotate(float angel) {
        if (mMatrix == null || mid == null || mCollageView == null) return;
        initMidPoint();
        mMatrix.postRotate(angel * 1.0f, mid.x, mid.y);
        mCollageView.invalidate();
    }

    public void moveUp() {
        postTranslate(MOVE_UP);
    }

    public void moveDown() {
        postTranslate(MOVE_DOWN);
    }

    public void moveLeft() {
        postTranslate(MOVE_LEFT);
    }

    public void moveRight() {
        postTranslate(MOVE_RIGHT);
    }

    public void zoomIn() {
        postScale(STEP_SCALE_IN);
    }

    public void zoomOut() {
        postScale(STEP_SCALE_OUT);
    }

    public void rotate90() {
        postRotate(90);
    }

    public void mirror90() {
        postRotate(-90);
    }

    public void rotate() {
        postRotate(STEP_ROTATE);
    }

    public void mirror() {
        postRotate(-STEP_ROTATE);
    }

    private void initMidPoint() {
        if (mMatrix == null || mBitmap == null) return;
        float[] arrayOfFloat = new float[9];
        mMatrix.getValues(arrayOfFloat);
        float f7 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f8 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        midPointToStartPoint(f7, f8);
    }

    private void midPointToStartPoint(float f7, float f8) {

        float[] arrayOfFloat = new float[9];
        mMatrix.getValues(arrayOfFloat);
        float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = f1 + f7;
        float f4 = f2 + f8;
        mid.set(f3 / 2, f4 / 2);
    }

    private void postTranslate(int direction) {
        initMidPoint();
        if (mid == null || mMatrix == null || mCollageView == null) return;
        if (exceedsBound(direction, mid.x, mid.y)) {
            mNotTranslate = true;
            return;
        }
        mNotTranslate = false;

        switch (direction) {
            case MOVE_UP:
                mMatrix.postTranslate(0, -STEP_MOVE);
                break;
            case MOVE_DOWN:
                mMatrix.postTranslate(0, STEP_MOVE);
                break;
            case MOVE_LEFT:
                mMatrix.postTranslate(-STEP_MOVE, 0);
                break;
            case MOVE_RIGHT:
                mMatrix.postTranslate(STEP_MOVE, 0);
                break;
            default:
                break;
        }
        mCollageView.invalidate();
    }

    public boolean isNotTranslate() {
        return mNotTranslate;
    }

    public boolean isNotScale() {
        return mNotScale;
    }

    private void postScale(float scale) {
        initMidPoint();

        if (dst_resize == null || mMatrix == null || mCollageView == null) return;
        float f7 = (dst_resize.right + dst_resize.left) / 2;
        float f8 = (dst_resize.top + dst_resize.bottom) / 2;
        if (((diagonalLength(f7, f8) /
                halfDiagonalLength <= minScale)) && scale < 1 ||
                (diagonalLength(f7, f8) / halfDiagonalLength >= maxScale) && scale > 1) {
            mNotScale = true;
            return;
        }
        mNotScale = false;
        mMatrix.postScale(scale, scale, mid.x, mid.y);
        mCollageView.invalidate();
    }

    public void invalidateRatio(float newCollageRatio) {
        if (mCollageView == null || mMatrix == null) return;
        int[] oldSize = mCollageView.getOldSize();
        Flog.d(TAG, "invalidateRatio old: x=" + oldSize[0] + "_y=" + oldSize[1]);
        float ratioWidth = (float) mCollageView.getWidth() / oldSize[0];
        float ratioHeight = (mCollageView.getWidth() * newCollageRatio) / oldSize[1];
        float[] values = new float[9];
        mMatrix.getValues(values);

        values[Matrix.MTRANS_X] *= ratioWidth;
        values[Matrix.MTRANS_Y] *= ratioHeight;
        mMatrix.setValues(values);
//        mCollageView.invalidate();
    }

    protected float rotationToStartPoint(MotionEvent event) {

        if (event == null || mMatrix == null) return 0f;
        float[] arrayOfFloat = new float[9];
        mMatrix.getValues(arrayOfFloat);
        float x = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float y = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        double arc = Math.atan2(event.getY(0) - y, event.getX(0) - x);

        return (float) Math.toDegrees(arc);
    }

    protected void updateMiddlePoint(MotionEvent event) {
        if (mMatrix == null || mid == null) return;
        float[] arrayOfFloat = new float[9];
        mMatrix.getValues(arrayOfFloat);
        float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = f1 + event.getX(0);
        float f4 = f2 + event.getY(0);
        mid.set(f3 / 2, f4 / 2);
    }

    /**
     * Determine the space between the first two fingers
     */
    protected float spacing(MotionEvent event) {
        if (event == null) return 0;
        if (event.getPointerCount() == 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }
    }

    public int getOpacity() {
        return mPaint.getAlpha();
    }

    /**
     * Helper to setColor(), that only assigns the color's alpha value, leaving its r,g,b values unchanged.
     * Results are undefined if the alpha value is outside of the range [20..255]
     *
     * @param opacity int: set the alpha component [20..255] of the paint's color.
     */
    public void setOpacity(int opacity) {
        if (opacity < MIN_OPACITY_VALUE || opacity > MAX_OPACITY_VALUE)
            return;
        mPaint.setAlpha(opacity);
        mCollageView.invalidate();
    }

    public int getStickerType() {
        return mStickerType;
    }

    @Override
    public void release() {

        if (mPaint != null) {
            mPaint.reset();
            mPaint = null;
        }
        if (mLinePaint != null) {
            mLinePaint.reset();
            mLinePaint = null;
        }
        if (deleteBitmap != null) {
            deleteBitmap = BitmapHelper.recycle(deleteBitmap);
        }
        if (resizeBitmap != null) {
            resizeBitmap = BitmapHelper.recycle(resizeBitmap);
        }
        if (flipVBitmap != null) {
            flipVBitmap = BitmapHelper.recycle(flipVBitmap);
        }
        if (topBitmap != null) {
            topBitmap = BitmapHelper.recycle(topBitmap);
        }
        if (dst_delete != null) {
            dst_delete.setEmpty();
            dst_delete = null;
        }
        if (dst_resize != null) {
            dst_resize.setEmpty();
            dst_resize = null;
        }
        if (dst_flipV != null) {
            dst_flipV.setEmpty();
            dst_flipV = null;
        }
        if (dst_top != null) {
            dst_top.setEmpty();
            dst_top = null;
        }
        if (mBitmap != null) {
            mBitmap = BitmapHelper.recycle(mBitmap);
        }
        if (mMatrix != null) {
            mMatrix.reset();
            mMatrix = null;
        }
        if (mid != null) {
            mid = null;
        }
        if (mStickerViewListener != null) {
            mStickerViewListener = null;
        }
    }

    public StickerViewListener getStickerViewListener() {
        return mStickerViewListener;
    }

    public BaseStickerView setStickerViewListener(StickerViewListener stickerViewListener) {
        mStickerViewListener = stickerViewListener;
        return this;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
