package bsoft.hoavt.photoproject.lib_textcollage.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import bsoft.hoavt.photoproject.lib_textcollage.R;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.BitmapUtil;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.CanvasUtils;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.CommonVl;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.EGL14Util;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.Flog;
import bsoft.hoavt.photoproject.lib_textcollage.listeners.OnCharacterTextViewListener;
import bsoft.hoavt.photoproject.lib_textcollage.models.CharacterItem;

/**
 * Created by vutha on 3/22/2017.
 */
public class CharacterTextView extends FrameLayout {

    private static final String TAG = CharacterTextView.class.getSimpleName();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private static final String[] SPECIAL_TEXTS = {",", "_", "'", "π", "<", ">", "ư", "ơ", "Ư"};
    private int mode = NONE;
    /**
     * The flag variable to check whether path of text had got yet.
     */
    private boolean mIsGetTextPath = true;
    /**
     * The current path of photoview.
     */
    private Path mTextPath;
    /**
     * The initial path that received when parseSVG vectordrawable.
     * Used to keep margin value between photos in collageview when changing ratio of view.
     */
    private Bitmap mBitmap;
    private Matrix mMatrix;
    /**
     * The region of photoview. Used to catch on event click.
     */
    private Region mRegion;

    /**
     * mRect is the rect integer of mRectF. Used to invalidateRatio rect.
     */
    private RectF mRectF;
    // these matrices will be used to move and zoom image
    private Matrix savedMatrix = new Matrix();
    // remember some things for zooming
    private PointF startP = new PointF();
    private PointF midP = new PointF();
    private float oldDist = 1f;
    private float oldAngle = 0f;
    private float newAngle = 0f;
    private TextPaint mTextPaint;
    private float mBaseline;
    private String mText = "";
    private int mTextHeight = -1;
    private Paint mLinePaint;
    private float mWidthSignAddition = 25;
    private Rect mBoundsRect;
    private int mId = -1;
    private OnCharacterTextViewListener listener;
    private CharacterItem mSaver;
    private float SCALE_DOWN_TEXT_SIZE = 82.0f / 100;
    private float xAdded, yAdded;

    public CharacterTextView(Context context) {
        super(context);
        init();
    }

    public CharacterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CharacterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Sets the text size for a Paint object so a given string of text will be a
     * given width.
     *
     * @param paint        the Paint to set the text size for
     * @param desiredWidth the desired width
     * @param text         the text that should be that width
     */
    private static void setTextSizeForWidth(Paint paint, float desiredWidth,
                                            String text) {

        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        final float testTextSize = 48f;

        // Get the mBoundsRect of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = testTextSize * desiredWidth / bounds.width();

        // Set the paint for that size.
        paint.setTextSize(desiredTextSize);
    }

    private void init() {

        mBoundsRect = new Rect();

        mTextPath = new Path();
        mMatrix = new Matrix();
        mRegion = new Region();
        mRectF = new RectF();

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(150);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setStrokeWidth(5);
        mTextPaint.setColor(Color.BLACK);

        mLinePaint = new Paint();
        mLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.color_accent));
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(getResources().getDimension(R.dimen.collage_select_line_width));

//        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);

        mSaver = new CharacterItem();

        boolean isSupported = CanvasUtils.supportClipMethod(this);
        Flog.d(TAG, "isSupported=" + isSupported);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
        mSaver.setId(id);
    }

    public void setText(String text) {
        mText = text;
        mSaver.setText(mText);

        mIsGetTextPath = true;
        invalidate();
    }



    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        Flog.d(TAG, "isHardwareAccelerated="+canvas.isHardwareAccelerated());

        canvas.drawColor(Color.WHITE);

        String text = mText;

        Flog.d(TAG, "height=" + getHeight() + "_width=" + getWidth());
        if (false) {
            setTextSizeForWidth(mTextPaint, getHeight() / 2f, text);
        } else {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                Log.d(TAG, "hardware acceleration");
//            float textSize = getHeight()*10f/7;
                setLayerType(LAYER_TYPE_SOFTWARE, null);
                float textSize = getWidth();
                mTextPaint.setTextSize(textSize);
            } else
                fitCenterText(mText, getWidth());
        }
        Flog.d(TAG, "size real=" + mTextPaint.getTextSize());

        float widthText = mTextPaint.measureText(text);
        float paddingLeft = (getWidth() - widthText) / 2;
        float paddingTop = 0;
        float startX = paddingLeft;
        float startY = paddingTop;
        mBoundsRect.setEmpty();
        mTextPaint.getTextBounds(text, 0, text.length(), mBoundsRect);

        if (mIsGetTextPath) {
            /*DRAW ONCE: to get baseline coordinates of character*/
            // center the text baseline vertically
            int verticalAdjustment = (this.getHeight() >> 1);

            // translate to the new text-baseline vertically
            canvas.translate(0, verticalAdjustment);


            float dx = paddingLeft;
            float dy = paddingTop;
            Flog.d(TAG, "1  left=" + (mBoundsRect.left + dx) + "_top=" + mBoundsRect.top + "_bottom=" + mBoundsRect.bottom);

            mTextHeight = mBoundsRect.height();
            mSaver.setTextHeight(mTextHeight);

            /*DRAW TWICE: draw exactly*/
            // back to top the text baseline vertically
            canvas.translate(0, -verticalAdjustment);

            float remainDis = getHeight() - mBoundsRect.height();
            float halfRemain = remainDis / 2F;
            float baseline = halfRemain + Math.abs(mBoundsRect.top + dy);
            Flog.d(TAG, "baseline=" + baseline);

            canvas.translate(0, baseline);
            mBaseline = baseline;

            if (false) {

                canvas.drawText(text, startX, startY, mTextPaint); // x=0, y=0

            }

            // GET PATH OF TEXT
            Path path = new Path();
            mTextPaint.getTextPath(text, 0, text.length(), paddingLeft, 0, path);
            /**
             *  Paint style needs to have fill set, and it seems getTextPath() returns a Path that isn't closed,
             *  so need to close it with path.close() before drawing
             * */
            path.close();

            setPath(path);

            if (true) {
                canvas.drawPath(path, mTextPaint);
            }

            Flog.d(TAG, "mRectF: w=" + mRectF.width() + "_h=" + mRectF.height());

            // CLIP PATH TEXT
//            canvas.clipPath(path, Region.Op.REPLACE);
            CanvasUtils.clipPath(canvas, path, Region.Op.REPLACE);
            canvas.translate(0, -baseline);

            mIsGetTextPath = false;
        } else {

            Flog.d(TAG, "mBaseline=" + mBaseline);
            Flog.d(TAG, "Canvas: 1=" + canvas.getClipBounds());
            canvas.translate(0, mBaseline);
            Flog.d(TAG, "Canvas: 2=" + canvas.getClipBounds());

            if (true) {
                canvas.drawPath(mTextPath, mTextPaint);
//                canvas.drawText(text, startX, startY, mTextPaint); // x=0, y=0
            }

//            canvas.clipPath(mTextPath, Region.Op.REPLACE);
            CanvasUtils.clipPath(canvas, mTextPath, Region.Op.REPLACE);
//            canvas.translate(0, -mBaseline);
            Flog.d(TAG, "padding left=" + (mBoundsRect.left + paddingLeft) + "_top=" + (mBoundsRect.top + paddingTop));
            Flog.d(TAG, "Canvas: 3=" + canvas.getClipBounds());
//            canvas.translate(mBoundsRect.left + paddingLeft, mBoundsRect.top + paddingTop);
//            Flog.d(TAG, "Canvas: 4=" + canvas.getClipBounds());
//            canvas.translate(-(mBoundsRect.left + paddingLeft), -(mBaseline + mBoundsRect.top + paddingTop));

            xAdded = mBoundsRect.left + paddingLeft;
            yAdded = mBoundsRect.top + paddingTop;
//            Flog.d(TAG, "Added 1: x="+xAdded+"_y="+yAdded);

            canvas.translate(0, -mBaseline);
            Flog.d(TAG, "Canvas: 5=" + canvas.getClipBounds());
        }

        Flog.d(TAG, "mBitmap=" + mBitmap + "_matrix=" + mMatrix);
        if (mBitmap == null) {
            canvas.restore();
            drawAddPhotoviewSign(canvas);
        } else {
            canvas.drawBitmap(mBitmap, mMatrix, null);
            canvas.restore();
        }
    }

    public void printMatrix(Matrix matrix) {
        float values[] = new float[9];
        matrix.getValues(values);
        Log.d(TAG, "aa x=" + values[2] + "_y=" + values[5]);
    }

    private void fitCenterCropImg() {
        Flog.d(TAG, "fitCenterCropImg");
        if (mRectF == null || mRectF.isEmpty() || mBitmap == null || mBitmap.isRecycled() || mMatrix == null)
            return;

        Flog.d(TAG, "mRectF 2=" + mRectF);
//        float xCenterChar = mRectF.width() / 2;
//        float yCenterChar = mRectF.height() / 2;
        float xCenterChar = (mRectF.left + mRectF.right) / 2;;
        float yCenterChar = (mRectF.top + mRectF.bottom) / 2;
        int wImg = 0;
        int hImg = 0;
        if (mBitmap != null && !mBitmap.isRecycled()) {
            wImg = mBitmap.getWidth();
            hImg = mBitmap.getHeight();
        }
        Flog.d(TAG, "image: w=" + wImg + "_h=" + hImg);
        mMatrix.setTranslate(xCenterChar - (wImg >> 1), yCenterChar - (hImg >> 1));
        float sx = mRectF.width() / wImg;
        float sy = mRectF.height() / hImg;

        Flog.d(TAG, "mRectF:  w=" + mRectF.width() + "_h=" + mRectF.height());
        float scale = (Float.compare(sx, sy) >= 0) ? sx : sy;
        Flog.d(TAG, "scale image: " + scale);
        mMatrix.postScale(scale, scale, xCenterChar, yCenterChar);
        if (mSaver != null) {
            Matrix matrix = new Matrix(mMatrix);
            matrix.postTranslate(-xAdded, -(mBaseline+yAdded));
            mSaver.setMatrix(matrix);
//            mSaver.setMatrix(mMatrix);
        }
    }

    private void fitCenterText(String text, int minWidth) {

        float standardFactor = 1f;

        Rect bounds = new Rect();

        int firstSampleSize = 10;
        mTextPaint.setTextSize(firstSampleSize);
        bounds.setEmpty();
        mTextPaint.getTextBounds(text, 0, text.length(), bounds);
//        int firstSize = bounds.width() > bounds.height() ? bounds.width() : bounds.height();
        int firstSize = mTextPaint.measureText(text, 0, text.length()) > bounds.height() ? (int) mTextPaint.measureText(text, 0, text.length()) : bounds.height();

        int secondSampleSize = 20;
        mTextPaint.setTextSize(secondSampleSize);
        bounds.setEmpty();
        mTextPaint.getTextBounds(text, 0, text.length(), bounds);
        int secondSize = mTextPaint.measureText(text, 0, text.length()) > bounds.height() ? (int) mTextPaint.measureText(text, 0, text.length()) : bounds.height();
//        int secondSize = bounds.width() > bounds.height() ? bounds.width() : bounds.height();

        standardFactor = secondSize - firstSize;
        if (standardFactor <= 0) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
            mTextPaint.setTextSize(minWidth);
            return;
        }

        Flog.d(TAG, "height: first=" + firstSize + "_second=" + secondSize);
        Flog.d(TAG, "standardFactor=" + standardFactor);

        mTextPaint.getTextBounds(text, 0, text.length(), bounds);
//        int standEdge = bounds.width() > bounds.height() ? getWidth() : getHeight();
        int standEdge = mTextPaint.measureText(text, 0, text.length()) > bounds.height() ? getWidth() : getHeight();

        int stepSampleSize = secondSampleSize - firstSampleSize - 1;
        float textSize = standEdge * stepSampleSize / standardFactor;

//        Flog.d(TAG, "textSize=" + textSize + "_maxTextureSize=" + EGL14Util.getMaxTextureSize() * 100 / 70);

        if (isSpecialText(text) || textSize > EGL14Util.getMaxTextureSize())
            textSize = textSize * SCALE_DOWN_TEXT_SIZE;

        Flog.d(TAG, "123textSize=" + textSize);
        if (textSize < minWidth) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
            textSize = minWidth;
        }
        mTextPaint.setTextSize(textSize);
    }

    private boolean isSpecialText(String text) {
        for (String specialText : SPECIAL_TEXTS) {
            if (text.equals(specialText))
                return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Flog.d(TAG, "onTouchEvent");
        boolean handled = true;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                Flog.d(TAG, "rect mBoundsRect=" + mRectF);
                Flog.d(TAG, "touch event: x=" + event.getX(0) + "_y=" + event.getY(0));
                Flog.d(TAG, "contains=" + mRegion.contains((int) event.getX(0), (int) event.getY(0)));

                if (mRegion.contains((int) event.getX(0), (int) event.getY(0))) {

//                    if (mPhotoViewListener != null)
//                        mPhotoViewListener.onPhotoActionDown(mId);
                    if (listener != null && mBitmap == null) {
                        float w = mRectF.width();
                        float h = mRectF.height();
                        Flog.d(TAG, "1234 w=" + w + "_h=" + h);
                        listener.onCharacterViewDown(mId, w, h);
                    }

                    savedMatrix.set(mMatrix);
                    startP.set(event.getX(), event.getY());
                    mode = DRAG;
//                    invalidate();
                } else {
                    handled = false;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > CommonVl.ZOOM_DISTANCE_PIVOT) {
                    savedMatrix.set(mMatrix);
                    midPoint(midP, event);
                    mode = ZOOM;
                }
                oldAngle = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
//                Flog.d(TAG, "Added 1: x="+xAdded+"_y="+yAdded);
                Matrix matrix = new Matrix(mMatrix);
                matrix.postTranslate(-xAdded, -(mBaseline+yAdded));
                mSaver.setMatrix(matrix);
//                mSaver.setMatrix(mMatrix);
                if (listener != null) {
                    listener.onCharacterViewUp(mId);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    mMatrix.set(savedMatrix);
                    float dx = event.getX() - startP.x;
                    float dy = event.getY() - startP.y;
                    mMatrix.postTranslate(dx, dy);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > CommonVl.ZOOM_DISTANCE_PIVOT) {
                        mMatrix.set(savedMatrix);
                        float scale = (newDist / oldDist);
                        mMatrix.postScale(scale, scale, midP.x, midP.y);
                    }
                    if (event.getPointerCount() == 2) {
                        newAngle = rotation(event);
                        float r = newAngle - oldAngle;
                        mMatrix.postRotate(r, midP.x, midP.y);
                    }
                }
                invalidate();
                break;
        }
        return handled;
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

    protected float spacing(float xBegin, float yBegin, float xEnd, float yEnd) {
        float x = xBegin - xEnd;
        float y = yBegin - yEnd;
        return (float) Math.sqrt(x * x + y * y);
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

        Flog.d(TAG, "touch on 1");
        Flog.d(TAG, "setPath");
        mTextPath = new Path(path);
        mRectF.setEmpty();
        mTextPath.computeBounds(mRectF, true);

        Flog.d(TAG, "mRectF 1=" + mRectF);
        mRectF.set(mRectF.left, mRectF.top + mBaseline, mRectF.right, mRectF.top + mBaseline + mRectF.height());

//        mMatrix.setTranslate(mRectF.left, mRectF.top);

        setPathRegion();
    }

    private void setPathRegion() {
//        mRegion.setPath(mTextPath, new Region((int) mRectF.left, (int) mRectF.top,
//                (int) mRectF.right, (int) mRectF.bottom));
        /**
         * Set the region to the specified region. Not specified equal to setPath().
         * */
        mRegion.set(new Region((int) mRectF.left, (int) mRectF.top,
                (int) mRectF.right, (int) mRectF.bottom));
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = 200;
        int height = 200;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthRequirement = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthRequirement;
        } else if (widthMode == MeasureSpec.AT_MOST && width > widthRequirement) {
            width = widthRequirement;
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightRequirement = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightRequirement;
        } else if (heightMode == MeasureSpec.AT_MOST && width > heightRequirement) {
            height = heightRequirement;
        }

        setMeasuredDimension(width, height);
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = BitmapUtil.recycle(mBitmap);
        mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        mSaver.setBitmap(mBitmap);

        fitCenterCropImg();

        invalidate();
        Flog.d(TAG, "setBitmap with w=" + mBitmap.getWidth() + "_h=" + mBitmap.getHeight());
    }

    public int getTextHeight() {
        return mTextHeight;
    }

    public float getDistanceTwoVertices() {

        float values[] = new float[9];
//            mMatrix.reset();
        mMatrix.getValues(values);
        // get old values of matrix stickers:
//        float scale_X = values[0];
//        float skew_X = values[1];
        float transform_X = values[2];
//        float skew_Y = values[3];
//        float scale_Y = values[4];
        float transform_Y = values[5];
        Flog.d(TAG, "trans: x=" + transform_X + "_y=" + transform_Y);
        float distance = spacing(0, 0, transform_X, transform_Y);
        Flog.d(TAG, "distance=" + distance);
        return distance;
    }

    private void drawAddPhotoviewSign(Canvas canvas) {

        float centerX = getWidth() >> 1;
        float centerY = getHeight() >> 1;
        Flog.d(TAG, "center: x=" + centerX + "_y=" + centerY);
        canvas.drawLine(centerX - mWidthSignAddition, centerY,
                centerX + mWidthSignAddition, centerY, mLinePaint);
        canvas.drawLine(centerX, centerY - mWidthSignAddition,
                centerX, centerY + mWidthSignAddition, mLinePaint);
    }

    public CharacterTextView setListener(OnCharacterTextViewListener listener) {
        this.listener = listener;
        return this;
    }

    public CharacterItem getSaver() {
        return mSaver;
    }
}
