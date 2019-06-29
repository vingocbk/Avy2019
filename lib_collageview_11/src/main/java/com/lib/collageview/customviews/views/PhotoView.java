package com.lib.collageview.customviews.views;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import com.lib.collageview.CollageView;
import com.lib.collageview.R;
import com.lib.collageview.helpers.ConstValues;
import com.lib.collageview.helpers.Flog;
import com.lib.collageview.helpers.Utils;
import com.lib.collageview.helpers.bitmap.BitmapHelper;
import com.lib.collageview.helpers.bitmap.CanvasUtils;
import com.lib.collageview.interfaces.PhotoViewListener;

import java.util.Arrays;

/**
 * Created by vutha on 3/22/2017.
 */
public class PhotoView extends BaseView {

    private static final String TAG = PhotoView.class.getSimpleName();
    private static final boolean USE_OLD_CODE_SCALE = false;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    public final float MIN_ZOOM_VALUE = 0.08F;
    public final float MAX_ZOOM_VALUE = 8F;
    private final float BITMAP_SCALE = 0.7f;
    private float mWidthSignAddition = 25;

    /**
     * Initialize local variable for check conditions and save values.
     * Used for rotating and moving photoview.
     */
    private float oldSpacing = 0f;
    private boolean isPointerDown = false, isTranslating = false;
    private PointF midPoint = new PointF();
    private float lastRotateDegree = 0f;
    private float pointerZoomCoeff = 0.05f;
    private float oldScale = 0f;
    private float mLastX, mLastY;
    private boolean isFocus = false;
    /**
     * The current path of photoview.
     */
    private Path mPath;
    /**
     * The initial path that received when parseSVG vectordrawable.
     * Used to keep margin value between photos in collageview when changing ratio of view.
     */
    private Path mOriginPath;
    private Bitmap mBitmap;
    private Matrix mMatrix;
    /**
     * The region of photoview. Used to catch on event click.
     */
    private Region mRegion;
    private Paint mPaint;
    private Paint mAntiAliasPaint;
    private Paint mBorderPaint;
    private Paint mLinePaint;
    private Paint mHighLightPaint;
    /**
     * mRect is the rect integer of mRectF. Used to invalidateRatio rect.
     */
    private Rect mRect;
    private RectF mRectF;
    private RectF mRectFItem;
    private Path mRoundPath;
    private float mRoundValue = 0;
    /**
     * Scale-value bound [minScale, maxScale].
     */
    private float minScale, maxScale;
    /**
     * The flag variable check whether photoview is selected or not.
     */
    private boolean mIsSelected;
    /**
     * Listener of current photoview. Used for callback in activity.
     */
    private PhotoViewListener mPhotoViewListener = null;
    private float mStrokeWidth = ConstValues.NO_VALUE;
    private Bitmap mBorderBmp;
    private boolean mIsFiltered = false;
    private PointF mScaleCenter = new PointF();
    private PointF mLastMovePoint = new PointF();
    private float total = 0;
    private float[] preCoords = new float[4];
    private float[] curCoords = new float[4];
    // these matrices will be used to move and zoom image
    private Matrix savedMatrix = new Matrix();
    private int mode = NONE;
    // remember some things for zooming
    private PointF startP = new PointF();
    private PointF midP = new PointF();
    private float oldDist = 1f;
    private float oldAngle = 0f;
    private float newAngle = 0f;
    private int colorBorderDefault = -1;
    private boolean mIsOutOfMemory = false;

    public PhotoView(CollageView collageView, int photoIndex) {
        super(collageView);
        mIndex = photoIndex;
        init();
    }

    private void init() {
        mPath = new Path();
        mOriginPath = new Path();
        mMatrix = new Matrix();
        mRegion = new Region();
        mRect = new Rect();
        mRectF = new RectF();
        mRoundPath = new Path();
        if (mStrokeWidth == ConstValues.NO_VALUE) {
            mStrokeWidth = mContext.getResources().getDimension(R.dimen.collage_select_line_width);
        }
        initPaints();

        mScaleCenter.set(0, 0);
        mLastMovePoint.set(0, 0);
    }

    private void initPaints() {
        /**
         * Initialize paint for photoview.
         * */
        mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        /**
         * Initialize paint for anti-alias.
         * */
        mAntiAliasPaint = new Paint();
//        mAntiAliasPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mAntiAliasPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        /**
         * Initialize paint for border-line when photoview is selected.
         * */
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        colorBorderDefault = Color.WHITE;
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(colorBorderDefault);
        setStrokeStylePaint(mBorderPaint, mStrokeWidth);

        mHighLightPaint = new Paint();
        mHighLightPaint.setColor(ContextCompat.getColor(mContext, R.color.colorSecondaryDark));
        setStrokeStylePaint(mHighLightPaint, mStrokeWidth);
        /**
         * Initialize paint for line of addition-sign used to suggest that user select photo from gallery.
         * */
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL);

        mLinePaint.setColor(Color.RED);
        mLinePaint.setTextSize(14);

    }

    private void setStrokeStylePaint(Paint paint, float strokeWidth) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);
        paint.setAntiAlias(true);
    }


    /**
     * ANTI-ALIAS on clipPath:
     * Draw the Path on top of the bitmap as a blurred transparent line some pixels wide
     * with paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN)).
     *//*
//        canvas.drawPath(mPath, mAntiAliasPaint);
    }*/
    @Override
    public void onDraw(Canvas canvas, int index) {

        // drawRound(canvas);
        CanvasUtils.clipPath(canvas, mPath);
        if (mRectFItem != null) {
            if (isFocus)
                mPaint.setColor(Color.DKGRAY);
            else
                mPaint.setColor(Color.TRANSPARENT);
            canvas.drawRect(mRectFItem, mPaint);
        }

        if (Arrays.asList(Utils.arr).contains(index)) {
            drawAddPhotoviewSign(canvas, index);

        }

        drawBorderLine(canvas);

        if (mBitmap != null && mIsSelected) {
            drawHighLightLine(canvas);
        }

        /**
         * ANTI-ALIAS on clipPath:
         * Draw the Path on top of the bitmap as a blurred transparent line some pixels wide
         * with paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN)).
         * */
//        canvas.drawPath(mPath, mAntiAliasPaint);
    }

    private void drawHighLightLine(Canvas canvas) {
        canvas.drawPath(mPath, mHighLightPaint);

        if (mRoundValue <= 0) return;
        canvas.drawPath(mRoundPath, mHighLightPaint);
    }

    /**
     * Draw the additional sign. Used to suggest that user select photo from gallery.
     *
     * @param canvas The Canvas class holds the "draw" calls.
     *               To draw something, you need 4 basic components: A Bitmap to hold the pixels,
     *               a Canvas to host the draw calls (writing into the bitmap),
     *               a drawing primitive (e.g. Rect, Path, text, Bitmap),
     *               and a paint (to describe the colors and styles for the drawing).
     */
    private void drawAddPhotoviewSign(Canvas canvas, int index) {
        canvas.drawText(String.valueOf(Utils.mapIndex(index)), mRectF.centerX() / 2, mRectF.centerY() / 2, mLinePaint);
    }

    @Override
    public void release() {

        if (mScaleCenter != null) {
            mScaleCenter = null;
        }

        if (mLastMovePoint != null) {
            mLastMovePoint = null;
        }

        if (preCoords != null) {
            preCoords = null;
        }

        if (curCoords != null) {
            curCoords = null;
        }

        if (savedMatrix != null) {
            savedMatrix.reset();
            savedMatrix = null;
        }

        if (startP != null)
            startP = null;

        if (midP != null)
            midP = null;

        if (mPath != null) {
            mPath.reset();
            mPath = null;
        }
        if (mOriginPath != null) {
            mOriginPath.reset();
            mOriginPath = null;
        }
        if (mBitmap != null) {
            mBitmap = BitmapHelper.recycle(mBitmap);
        }
        if (mMatrix != null) {
            mMatrix.reset();
            mMatrix = null;
        }
        if (mRegion != null) {
            mRegion.setEmpty();
            mRegion = null;
        }
        if (mPaint != null) {
            mPaint.reset();
            mPaint = null;
        }
        if (mAntiAliasPaint != null) {
            mAntiAliasPaint.reset();
            mAntiAliasPaint = null;
        }
        if (mBorderPaint != null) {
            mBorderPaint.reset();
            mBorderPaint = null;
        }
        if (mLinePaint != null) {
            mLinePaint.reset();
            mLinePaint = null;
        }
        if (mHighLightPaint != null) {
            mHighLightPaint.reset();
            mHighLightPaint = null;
        }
        if (mRectF != null) {
            mRectF.setEmpty();
            mRectF = null;
        }
        if (mRect != null) {
            mRect.setEmpty();
            mRect = null;
        }
        if (mRoundPath != null) {
            mRoundPath.reset();
            mRoundPath = null;
        }
        if (midPoint != null) {
            midPoint = null;
        }
        if (mPhotoViewListener != null) {
            mPhotoViewListener = null;
        }
        if (mBorderBmp != null) {
            mBorderBmp = BitmapHelper.recycle(mBorderBmp);
            mBorderBmp = null;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (USE_OLD_CODE_SCALE) {
            int action = MotionEventCompat.getActionMasked(event);
            boolean handled = true;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    Flog.d(TAG, "photo ACTION_DOWN");
                    if (mRegion.contains((int) event.getX(0), (int) event.getY(0))) {
                        isTranslating = true;
                        mLastX = event.getX(0);
                        mLastY = event.getY(0);
                        Flog.d(TAG, "No image=" + (mBitmap == null));
                        if (mPhotoViewListener != null)
                            mPhotoViewListener.onPhotoActionDown(mIndex);
                        if (mBitmap != null) {
                            // Photoview has contained image.
                        }
                        mIsSelected = true;
                        mCollageView.invalidate();
                    } else {
                        handled = false;
                    }

                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    Flog.d(TAG, "photo ACTION_POINTER_DOWN");
                    if (spacing(event) > ConstValues.MAX_2_FINGERS_SPACING) {
                        oldSpacing = spacing(event);
                        isPointerDown = true;
                        float[] tmp = getMidPoint(event);
                        midPoint.set(tmp[0], tmp[1]);
                        lastRotateDegree = getRotateDegree(event);
                    } else {
                        isPointerDown = false;
                    }
                    isTranslating = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    Flog.d(TAG, "photo ACTION_MOVE");
                    //Two-finger scaling
                    if (isPointerDown && event.getPointerCount() >= 2) {
                        Flog.d(TAG, "photo ACTION_MOVE 2 fingers===");
                        float scale;
                        float newDis = spacing(event);
                        float angle = (getRotateDegree(event) - lastRotateDegree);
                        Flog.d(TAG, "1 angle=" + angle);
                        // Rotating
                        mMatrix.postRotate(angle, midPoint.x, midPoint.y);
                        lastRotateDegree = getRotateDegree(event);

                        // Scaling
                        if (newDis == 0 || newDis < ConstValues.MAX_2_FINGERS_SPACING) {
                            scale = 1;
                        } else {
                            scale = newDis / oldSpacing;
                            //The zoom is slow
                            scale = (scale - 1) * pointerZoomCoeff + 1;
                            if (false || oldScale == scale)
                                return false;
                        }

                        scale = getLimitedScale(scale);
                        Flog.d(TAG, "getLimitedScale=" + scale);

                        mMatrix.postScale(scale, scale, midPoint.x, midPoint.y);

                        oldScale = scale;
//                        }
                        float[] tmp = getMidPoint(event);
                        midPoint.set(tmp[0], tmp[1]);
                        mCollageView.invalidate();
                    } else if (isTranslating && event.getPointerCount() < 2) {
                        Flog.d(TAG, "photo ACTION_MOVE isInBitmap");
                        float x = event.getX(0);
                        float y = event.getY(0);
//                    Flog.oldAngle(TAG, "photo ACTION_MOVE isInBitmap");
                        //TODO: The movement area judgment can not exceed the screen
                        mMatrix.postTranslate(x - mLastX, y - mLastY);
                        mLastX = x;
                        mLastY = y;
                        if (mPhotoViewListener != null)
                            mPhotoViewListener.onPhotoActionMove(mIndex);
                        mCollageView.invalidate(mRect);
                    } else {
                        Flog.d(TAG, "no action");
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    isTranslating = false;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    Flog.d(TAG, "photo ACTION_UP");
                    isTranslating = false;
                    isPointerDown = false;
                    if (mPhotoViewListener != null)
                        mPhotoViewListener.onPhotoActionUp(mIndex);
                    break;
            }
            return handled;
        } else {
            boolean handled = true;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    if (mRegion.contains((int) event.getX(0), (int) event.getY(0))) {

                        if (mPhotoViewListener != null)
                            mPhotoViewListener.onPhotoActionDown(mIndex);

                        savedMatrix.set(mMatrix);
                        startP.set(event.getX(), event.getY());
                        mode = DRAG;
                        mCollageView.invalidate();
                    } else {
                        handled = false;
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(mMatrix);
                        midPoint(midP, event);
                        mode = ZOOM;
                    }
                    oldAngle = rotation(event);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    if (mPhotoViewListener != null)
                        mPhotoViewListener.onPhotoActionUp(mIndex);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        mMatrix.set(savedMatrix);
                        float dx = event.getX() - startP.x;
                        float dy = event.getY() - startP.y;
                        mMatrix.postTranslate(dx, dy);
                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        // mode == ZOOM
                        if (newDist > 10f) {
                            mMatrix.set(savedMatrix);
                            float scale = (newDist / oldDist);
                            mMatrix.postScale(scale, scale, midP.x, midP.y);
                        }
                        // mode == ROTATION
                        if (mBitmap != null && event.getPointerCount() == 2) {
                            newAngle = rotation(event);
                            float r = newAngle - oldAngle;
                            Flog.d(TAG, "rotation value=" + r);
                            float[] values = new float[9];
                            mMatrix.getValues(values);
                            float tx = values[2];
                            float ty = values[5];
                            float sx = values[0];
                            Flog.d(TAG, "bitmap: w=" + mBitmap.getWidth() + "_h=" + mBitmap.getHeight());
                            float xc = (mBitmap.getWidth() / 2) * sx;
                            float yc = (mBitmap.getHeight() / 2) * sx;
                            mMatrix.postRotate(r, tx + xc, ty + yc);
                        }
                    }
                    mCollageView.invalidate();
                    break;
            }
            return handled;
        }
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

    private void rotate(float angle) {
        total += angle;
        // calculate the degree of rotation

        mMatrix.postRotate(total, midPoint.x, midPoint.y);
    }

    private float[] getCoordsFromEvent(MotionEvent event) {
        return new float[]{event.getX(0), event.getY(0), event.getX(1), event.getY(1)};
    }

    private double skew(MotionEvent event) {
        Flog.d(TAG, "skew calculation!");
        curCoords = getCoordsFromEvent(event);
        printCoords("preCoords", preCoords);
        printCoords("curCoords", curCoords);
        float x1 = preCoords[0];
        float y1 = preCoords[1];
        float x2 = preCoords[2];
        float y2 = preCoords[3];
        float x3 = curCoords[0];
        float y3 = curCoords[1];
        float x4 = curCoords[2];
        float y4 = curCoords[3];

        float p1x = y2 - y1;
        float p1y = x1 - x2;
        float p2x = y4 - y3;
        float p2y = x3 - x4;

        double cosVal = (Math.abs(p1x * p2x + p1y * p2y) / (Math.hypot(p1x, p1y) * Math.hypot(p2x, p2y)));
        double angle = Math.acos(cosVal);
        Flog.d(TAG, "acos angle=" + angle);
        return angle;
    }

    private void printCoords(String tag, float[] coords) {
        Flog.d(TAG, "printCoords++++ of " + tag);
        Flog.d(TAG, "x1=" + coords[0]);
        Flog.d(TAG, "y1=" + coords[1]);
        Flog.d(TAG, "x2=" + coords[2]);
        Flog.d(TAG, "y2=" + coords[3]);
        Flog.d(TAG, "print done-------------------");
    }

    private void test() {
        Flog.d(TAG, "function test");
        float[] v = new float[9];
        mMatrix.getValues(v);
        // translation is simple
        float tX = v[Matrix.MTRANS_X];
        float tY = v[Matrix.MTRANS_Y];
        Flog.d(TAG, "trans: x=" + tX + "_y=" + tY);

        // calculate real scale
        float scaleX = v[Matrix.MSCALE_X];
        float skewY = v[Matrix.MSKEW_Y];
        float realScale = (float) Math.sqrt(scaleX * scaleX + skewY * skewY);
        Flog.d(TAG, "realScale=" + realScale);

        // calculate the degree of rotation
        float skewX = v[Matrix.MSKEW_X];
        float realAngle = Math.round(Math.atan2(skewX, scaleX) * (180 / Math.PI));
        Flog.d(TAG, "realAngle=" + realAngle);
    }

    /**
     * Get the limited scaleValue value of photoview.
     *
     * @param scale the scaleValue value of photoview.
     * @return the limited scaleValue value.
     */
    private float getLimitedScale(float scale) {
        if (mBitmap == null) return scale;
        float[] coordinates = getCoordinates();
        float scaleTempHorizontal = (scale * spacing(coordinates[0], coordinates[1], coordinates[2], coordinates[3]));
        float scaleTempVertical = (scale * spacing(coordinates[0], coordinates[1], coordinates[6], coordinates[7]));
        if (scaleTempHorizontal > scaleTempVertical) {
            scaleTempHorizontal = scaleTempVertical;
        }
        int mOringinWidth = mBitmap.getWidth();
        int mOringinHeight = mBitmap.getHeight();
        if (mOringinWidth > mOringinHeight)
            mOringinWidth = mOringinHeight;

        scaleTempHorizontal /= mOringinWidth;

        if (((scaleTempHorizontal <= minScale) && (scale < 1)) || ((scaleTempHorizontal >= maxScale) && (scale > 1))) {
            scale = 1;
        }
        return scale;
    }

    /**
     * Initialize the limit of scaleValue value, based-on dimension of bitmap and screen.
     */
    private void initScaleLimit() {
        if (mBitmap == null) return;
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int screenwidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        //When the width of the picture width is calculated according to the width of the zoom size
        // according to the size of the picture to change the smallest picture of the 1/4 maximum screen width
        if (mBitmap.getWidth() >= mBitmap.getHeight()) {
            float minWidth = screenwidth / 4;
            if (mBitmap.getWidth() < minWidth) {
                minScale = 1f;
            } else {
                minScale = 1.0f * minWidth / mBitmap.getWidth();
            }

            if (mBitmap.getWidth() > screenwidth) {
                maxScale = 1;
            } else {
                maxScale = 1.0f * screenwidth / mBitmap.getWidth();
            }
        } else {
            //When the picture is higher than the large, according to the high picture calculation
            float minHeight = screenHeight / 4;
            if (mBitmap.getHeight() < minHeight) {
                minScale = 1f;
            } else {
                minScale = 1.0f * minHeight / mBitmap.getHeight();
            }

            if (mBitmap.getHeight() > screenHeight) {
                maxScale = 1;
            } else {
                maxScale = 1.0f * screenHeight / mBitmap.getHeight();
            }
        }
        maxScale += 1; // increase MAX_SCALE to 1 units.
    }

    /**
     * Get four vertices of the lastest matrix:
     * - Lower left
     * - Lower right
     * - Upper right
     * - Upper left
     *
     * @return arrays of float variables: Two sequential elements is two coordinate (x, y) of one vertex.
     */
    private float[] getCoordinates() {
        float[] vertices = new float[8];
        float[] arrayOfFloat = new float[9];
        if (mMatrix == null || mBitmap == null) return vertices;
        mMatrix.getValues(arrayOfFloat);
        float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
        float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
        float f5 = 0.0F * arrayOfFloat[0] + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f6 = 0.0F * arrayOfFloat[3] + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        float f7 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f8 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        // in the lower left corner: (f5, f6)
        vertices[0] = f5;
        vertices[1] = f6;
        // in the lower right corner: (f7, f8)
        vertices[2] = f7;
        vertices[3] = f8;
        // in the upper right corner: (f3, f4)
        vertices[4] = f3;
        vertices[5] = f4;
        // in the upper left corner: (f1, f2)
        vertices[6] = f1;
        vertices[7] = f2;
        return vertices;
    }

    /**
     * Determine the space between the two fingers
     *
     * @param event motion event of photoview.
     * @return the spacing between two points.
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

    /**
     * Determine the space between the two points
     *
     * @param xBegin the x coordinate of begin point.
     * @param yBegin the y coordinate of begin point.
     * @param xEnd   the x coordinate of end point.
     * @param yEnd   the y coordinate of end point.
     * @return the spacing between two points.
     */
    protected float spacing(float xBegin, float yBegin, float xEnd, float yEnd) {
        float x = xBegin - xEnd;
        float y = yBegin - yEnd;
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * The location of the touch and the mMidPoint point of the top left corner of the image
     *
     * @param event motion event of photoview.
     */
    private float[] getMidPoint(MotionEvent event) {
        float[] arrayOfFloat = new float[9];
        mMatrix.getValues(arrayOfFloat);
        float f1 = event.getX(0);
        float f2 = event.getY(0);
        float f3 = event.getX(1);
        float f4 = event.getY(1);
//        Flog.oldAngle(TAG, "f1="+f1+"_f2="+f2+"_f3="+f3+"_f4="+f4);
        return new float[]{(f1 + f3) / 2, (f2 + f4) / 2};
    }

    /**
     * Get the middle point of current matrix of photoview.
     *
     * @return a array includes [0] is x and [1] is y. With (x, y) is coordinate of middle point.
     */
    private float[] getMidPointOfCurrentMatrix() {
        float coords[] = getCoordinates();
        return new float[]{(coords[6] + coords[2]) / 2, (coords[7] + coords[3]) / 2};
    }

    /**
     * Return the skew angle of photoview when rotate it using two fingers.
     *
     * @param event motion event of photoview.
     * @return the angle degree of current photoview.
     */
    public float getRotateDegree(MotionEvent event) {
        if (event.getPointerCount() >= 2) {
            double arc = Math.atan2(event.getY(1) - event.getY(0), event.getX(1) - event.getX(0));
            return (float) Math.toDegrees(arc);
        }
        return 0;
    }

    /**
     * Return the skew angle of line with the horizontal axis.
     *
     * @param xBegin the x coordinate of begin point.
     * @param yBegin the y coordinate of begin point.
     * @param xEnd   the x coordinate of end point.
     * @param yEnd   the y coordinate of end point.
     * @return the angle degree of line with the horizontal axis.
     */
    public float getRotateDegree(float xBegin, float yBegin, float xEnd, float yEnd) {
        double arc = Math.atan2(yEnd - yBegin, xEnd - xBegin);
        return (float) Math.toDegrees(arc);
    }

    public void fitPhotoToLayout() {

        if (mRectF.isEmpty() || mBitmap == null) return;

        float centerX = mRectF.centerX();
        float centerY = mRectF.centerY();
        float leftTranslateX = (centerX - mBitmap.getWidth() / 2);
        float topTranslateY = (centerY - mBitmap.getHeight() / 2);

        float scaleW, scaleH;
        scaleW = mRectF.width() / mBitmap.getWidth();
        scaleH = mRectF.height() / mBitmap.getHeight();
        if (scaleW < scaleH)
            scaleW = scaleH;
        /*
         * Translate to center of rect:
         * */
        mMatrix.setTranslate(leftTranslateX, topTranslateY);
        /*
         * Center crop photo to rect:
         * */
        mMatrix.postScale(scaleW, scaleW, centerX, centerY);
    }

    /**
     * Draw round of photoview.
     *
     * @param canvas The Canvas class holds the "draw" calls.
     *               To draw something, you need 4 basic components: A Bitmap to hold the pixels,
     *               a Canvas to host the draw calls (writing into the bitmap),
     *               a drawing primitive (e.g. Rect, Path, text, Bitmap),
     *               and a paint (to describe the colors and styles for the drawing).
     */
    private void drawRound(Canvas canvas) {
        if (mRoundValue <= 0) return;
        mRoundPath.reset();
        //  mRoundPath.addRoundRect(mRectF, mRoundValue, mRoundValue, Path.Direction.CW);
        // CanvasUtils.clipPath(canvas, mRoundPath, Region.Op.REPLACE);
    }

    /**
     * Draw border-line of photoview when it's selected.
     *
     * @param canvas The Canvas class holds the "draw" calls.
     *               To draw something, you need 4 basic components: A Bitmap to hold the pixels,
     *               a Canvas to host the draw calls (writing into the bitmap),
     *               a drawing primitive (e.g. Rect, Path, text, Bitmap),
     *               and a paint (to describe the colors and styles for the drawing).
     */
    public void drawBorderLine(Canvas canvas) {
        Flog.d(TAG, "drawBorderLine" + mRoundPath);
        canvas.drawPath(mPath, mBorderPaint);

        if (mRoundValue <= 0) return;
        canvas.drawPath(mRoundPath, mBorderPaint);
        //  canvas.drawRoundRect(mRectF, mRoundValue, mRoundValue, mBorderPaint);
    }

    public Path getPath() {
        return mPath;
    }

    /**
     * Update path, rect_bound, region of photoview.
     * Includes:
     * - Set path of collageview
     * - Create rect from path. Used to invalidateRatio Rect on canvas.
     * - Translate matrix to left-top of rect.
     * - Make region from rect. Used for catch on event click.
     *
     * @param path the lastest path of collageview.
     */
    public void setPath(Path path) {

        mPath = new Path(path);
        mPath.computeBounds(mRectF, true);
        mRectF.round(mRect);
        mMatrix.setTranslate(mRectF.left, mRectF.top);
        setPathRegion();
    }

    public void setPathAfterMargin(Path path) {

        mPath = new Path(path);
        mPath.computeBounds(mRectF, true);
        mRectF.round(mRect);
        setPathRegion();
    }

    private void setPathRegion() {
        mRegion.setPath(mPath, new Region((int) mRectF.left, (int) mRectF.top,
                (int) mRectF.right, (int) mRectF.bottom));
        /**
         * Set the region to the specified region. Not specified equal to setPath().
         * */
//        mRegion.set(new Region((int) mRectF.left, (int) mRectF.top,
//                (int) mRectF.right, (int) mRectF.bottom));
    }

    /**
     * Rotate photoview by a angle.
     *
     * @param degree the value of angle that photoview rotate.
     */
    public void postRotate(float degree) {
        mMatrix.postRotate(degree, getMidPointOfCurrentMatrix()[0], getMidPointOfCurrentMatrix()[1]);
        mCollageView.invalidate(mRect);
    }

    /**
     * Translate photoview to new position on collageview.
     *
     * @param x the translated value based-on horizontal axis.
     * @param y the translated value based-on vertical axis.
     */
    public void postTranslate(float x, float y) {
        mMatrix.postTranslate(x, y);
        mCollageView.invalidate(mRect);
    }

    /**
     * Zoom in/out photoview.
     *
     * @param ratio the value of zoomed ratio.
     */
    public void postScale(float ratio) {
        ratio = getLimitedScale(ratio);
        mMatrix.postScale(ratio, ratio, getCoordinates()[0], getCoordinates()[1]);
        mCollageView.invalidate(mRect);
    }

    /**
     * Correct photoview to removing skew angle. Make it horizontally/vertically.
     */
    public void correctSkew() {
        if (mBitmap == null || mMatrix == null || mRect == null || mCollageView == null)
            return;
        float skewAngle;
        float coords[] = getCoordinates();
        boolean cmp = mBitmap.getWidth() > mBitmap.getHeight();
        float dis1 = spacing(coords[6], coords[7], coords[4], coords[5]);
        float dis2 = spacing(coords[6], coords[7], coords[0], coords[1]);
        if ((cmp && (dis1 > dis2)) || (!cmp && (dis1 < dis2))) {
            skewAngle = getRotateDegree(coords[6], coords[7], coords[4], coords[5]);
        } else {
            skewAngle = getRotateDegree(coords[6], coords[7], coords[0], coords[1]);
        }

        if (isPerpendiculared(skewAngle)) return;

        float angle;
        if (skewAngle < 0) {
            if (Math.abs(skewAngle) > 90) angle = -(180 - Math.abs(skewAngle));
            else angle = Math.abs(skewAngle);
        } else {
            if (skewAngle > 90) angle = (180 - skewAngle);
            else angle = -skewAngle;
        }
        mMatrix.postRotate(angle, getMidPointOfCurrentMatrix()[0], getMidPointOfCurrentMatrix()[1]);
        mCollageView.invalidate(mRect);
    }

    private boolean isPerpendiculared(float degree) {
        float cmper = Math.abs(Math.round(degree));
        return (cmper == 0 || cmper == 90 || cmper == 180 || cmper == 270 || cmper == 360);
    }

    public Path getOriginPath() {
        return mOriginPath;
    }

    public void setOriginPath(Path path) {
        mOriginPath = new Path(path);
    }

    public Region getRegion() {
        return mRegion;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        Flog.d(TAG, "setBitmap=" + mBitmap);
        initScaleLimit();
    }

    public void setRectF(RectF rectF) {
        mRectFItem = new RectF();
        mRectFItem = rectF;
    }

    public RectF getRectF() {
        return mRectF;
    }

    public void setIsSelected(boolean isSelected) {
        mIsSelected = isSelected;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public PhotoView setPhotoViewListener(PhotoViewListener listener) {
        mPhotoViewListener = listener;
        return this;
    }

    public float getWidthSignAddition() {
        return mWidthSignAddition;
    }

    public void setWidthSignAddition(float widthSignAddition) {
        mWidthSignAddition = widthSignAddition;
    }

    public float getStrokeWidthLinePaint() {
        if (mLinePaint == null) return 0f;
        return mLinePaint.getStrokeWidth();
    }

    public void setStrokeWidthLinePaint(float widthStroke) {
        mLinePaint.setStrokeWidth(widthStroke);
    }

    public float getRoundValue() {
        return mRoundValue;
    }

    public void setRoundValue(float roundValue) {
        mRoundValue = roundValue;
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    public void setMatrix(Matrix matrix) {
        mMatrix = matrix;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }

    public boolean getFocus() {
        return isFocus;
    }

    public void setColorPathBorder(int color) {

        Flog.d(TAG, "setColorPathBorder=" + color);
        if (mBorderPaint.getShader() != null) {
            mBorderPaint.reset();
            mBorderPaint.setShader(null);
            setStrokeStylePaint(mBorderPaint, mStrokeWidth);
        }
        mBorderPaint.setColor(color);

        mIsSelected = false;
    }

    public Bitmap getPatternPathBorderBmp() {
        return mBorderBmp;
    }

    public void setPatternPathBorderBmp(Bitmap bmp) {

        mBorderBmp = BitmapHelper.recycle(mBorderBmp);
        mBorderBmp = bmp;
        Shader shader = new BitmapShader(mBorderBmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        mBorderPaint.reset();
        setStrokeStylePaint(mBorderPaint, mStrokeWidth);
        mBorderPaint.setShader(shader);

        mIsSelected = false;
    }

    public Paint getBorderPaint() {
        return mBorderPaint;
    }

    public void setBorderPaint(Paint paint) {
        mBorderPaint = new Paint(paint);
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        mStrokeWidth = strokeWidth;
        mBorderPaint.setStrokeWidth(strokeWidth);
    }

    public void setFilter(boolean isFiltered) {
        mIsFiltered = isFiltered;
    }

    public boolean isFiltered() {
        return mIsFiltered;
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public void setOutOfMemory() {
        mIsOutOfMemory = true;
    }

    public boolean isOutOfMemory() {
        return mIsOutOfMemory;
    }
}
