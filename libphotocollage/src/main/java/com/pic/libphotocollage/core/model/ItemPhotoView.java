package com.pic.libphotocollage.core.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.pic.libphotocollage.core.CollageView;
import com.pic.libphotocollage.core.R;
import com.pic.libphotocollage.core.util.Flog;
import com.pic.libphotocollage.core.util.ImageUtils;

/**
 * Created by hoavt on 25/07/2016.
 */
public class ItemPhotoView extends BaseItem {
    private static final java.lang.String TAG = ItemPhotoView.class.getSimpleName();
    public static final int STEP_ZOOM = 10;
    //    private static final float BITMAP_SCALE = 0.7f;
    private final float DEFAULT_ZOOM_RATIO = 0.5f;
    private final float PINCHZOOM_RATIO = 1.0f;
    private final float MIN_ZOOM_RATIO = 1.0f, MAX_ZOOM_RATIO = 8.0f;
    private float mRoundSize;
    private boolean mIsSelected = false;

    private Bitmap mPhotoBmp;
    private Rect mSrcRect;
    private Rect mOriginRect;
    private RectF mDesRect;
    private Paint mDrawLinePaint;
    private Paint mPhotoPaint;
    private Path mPathRoundRect;
    private Path mPathPile;
    private int mOrgSrcWidth;
    private int mOrgSrcHeight;
    private int resizeBitmapWidth;
    private int resizeBitmapHeight;
    //    private Bitmap resizeBitmapRightBottom;
//    private Bitmap resizeBitmapTop;
//    private Bitmap resizeBitmapBottom;
//    private Bitmap resizeBitmapLeftTop;
//    private Bitmap resizeBitmapLeft;
//    private Bitmap resizeBitmapLeftBottom;
//    private Bitmap resizeBitmapRightTop;
//    private Bitmap resizeBitmapRight;
    private Rect dst_resize_left;
    private Rect dst_resize_right;
    private Rect dst_resize_top;
    private Rect dst_resize_bottom;
    private Rect dst_resize_lefttop;
    private Rect dst_resize_leftbottom;
    private Rect dst_resize_righttop;
    private Rect dst_resize_rightbottom;
    private int mPriority = -1;
    private boolean mContainsContent = false;
    private Paint mXferPaint;
    private boolean mNotZoom = false;
    private boolean mNotMove = false;

    public ItemPhotoView(CollageView collageView) {
        super(collageView);
        setCustomPaint();
        setRoundHelper();
    }

    @Override
    public void invalidateRatio() {
        // Nothing:
    }

    private void setRoundHelper() {
        /* Canvas.clipPath() support with hardware acceleration has been reintroduced since API 18: */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mCollageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mPathRoundRect = new Path();
    }

    public void setBitmapRect(Bitmap bitmapPhoto) {
        if (mPhotoBmp != null) {
            mPhotoBmp.recycle();
        }
        mPhotoBmp = bitmapPhoto;
        if (mPhotoBmp != null) {
            mOrgSrcWidth = mPhotoBmp.getWidth();
            mOrgSrcHeight = mPhotoBmp.getHeight();
            mOriginRect = new Rect();
            mOriginRect.set(0, 0, mPhotoBmp.getWidth(), mPhotoBmp.getHeight());
        }
    }

    public void swapBitmap(ItemPhotoView other) {
        Bitmap temp = this.mPhotoBmp;
        this.setPhotoBmp(other.getPhotoBmp());
        ;
        other.setPhotoBmp(temp);
    }

    private void initResizeRects() {
        dst_resize_left = new Rect();
        dst_resize_bottom = new Rect();
        dst_resize_leftbottom = new Rect();
        dst_resize_lefttop = new Rect();
        dst_resize_right = new Rect();
        dst_resize_righttop = new Rect();
        dst_resize_top = new Rect();
        dst_resize_rightbottom = new Rect();
    }

    private void initResizeBmps() {
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(resizeBitmapLeftBottom, resizeBitmapWidth, resizeBitmapHeight, true);
//        Matrix matrix = new Matrix();
//        matrix.postRotate(45);
//        resizeBitmapLeft = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
//        matrix.postRotate(45);
//        resizeBitmapLeftTop = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
//        matrix.postRotate(45);
//        resizeBitmapTop = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
//        matrix.postRotate(45);
//        resizeBitmapRightTop = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
//        matrix.postRotate(45);
//        resizeBitmapRight = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
//        matrix.postRotate(45);
//        resizeBitmapRightBottom = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
//        matrix.postRotate(45);
//        resizeBitmapBottom = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    public Bitmap getPhotoBmp() {
        return mPhotoBmp;
    }

    public void setPhotoBmp(Bitmap bitmapPhoto) {
        Bitmap old = this.mPhotoBmp;
        mPhotoBmp = bitmapPhoto;

        if (mPhotoBmp != old) {
            mOrgSrcWidth = mPhotoBmp.getWidth();
            mOrgSrcHeight = mPhotoBmp.getHeight();
            mOriginRect = new Rect();
            mOriginRect.set(0, 0, mPhotoBmp.getWidth(), mPhotoBmp.getHeight());
            setDefaultSrcRect();
        }

    }

    public Rect getSrcRect() {
        return mSrcRect;
    }

    public void setSrcRect(Rect srcRect) {
        mSrcRect = checkRectInOrg(srcRect);
    }

    public void setDefaultSrcRect() {
        int defaultWidth = (int) (mOrgSrcWidth * DEFAULT_ZOOM_RATIO),
                defaultHeight = (int) (mOrgSrcHeight * DEFAULT_ZOOM_RATIO),
                leftPadding = (mOrgSrcWidth - defaultWidth) / 2,
                topPadding = (mOrgSrcHeight - defaultHeight) / 2;
//        Flog.d("defaultWidth: " + defaultWidth);
//        Flog.d("defaultHeight: " + defaultHeight);
//        Flog.d("leftPadding: " + leftPadding);
//        Flog.d("topPadding: " + topPadding);

        if (mSrcRect == null) {
            mSrcRect = new Rect();
        }

        mSrcRect.set(leftPadding, topPadding, leftPadding + defaultWidth,
                topPadding + defaultHeight);

        fitRectSizeRatio();
    }

    public RectF getDesRect() {
        return mDesRect;
    }

    public void setDesRect(RectF desRect) {
        mDesRect = desRect;
    }

    public void moveSrcRect(int diffRight, int diffBottom) {
//        Flog.i("moveSrcRect");
        if (mSrcRect == null) {
            Flog.i("mSrcRect == null");
            return;
        }

        int drawLeft = mSrcRect.left - diffRight;
        int drawRight = mSrcRect.right - diffRight;
        int drawTop = mSrcRect.top - diffBottom;
        int drawBottom = mSrcRect.bottom - diffBottom;

//        Flog.i("srcRect before: "+mSrcRect);
        mSrcRect = checkRectInOrg(drawLeft, drawTop, drawRight, drawBottom);
//        Flog.i("srcRect after: "+mSrcRect);
    }

    private Rect checkRectInOrg(int left, int top, int right, int bottom) {
        int diff = 0;
        mNotMove = false;
        if (left < 0) {
            diff = 0 - left;
            left = 0;
            right += diff;
            mNotMove = true;
        }

        if (top < 0) {
            diff = 0 - top;
            top = 0;
            bottom += diff;
            mNotMove = true;
        }

        if (right > mOriginRect.right) {
            diff = mOriginRect.right - right;
            left += diff;
            right += diff;
            mNotMove = true;
        }

        if (bottom > mOriginRect.bottom) {
            diff = mOriginRect.bottom - bottom;
            top += diff;
            bottom += diff;
            mNotMove = true;
        }

//        Flog.i("AHHHHH", "left: " + left + "_right: " + right + "_top: " + top + "_bottom: " + bottom);
        return new Rect(left, top, right, bottom);
    }

    public void pinchZoom(int diffDst) {
//        Log.d(TAG, "diffDst: "+diffDst);
        if (mSrcRect != null) {
            int diffY = (diffDst * mSrcRect.height() / mSrcRect.width());
//            Log.d(TAG, "diffY: "+diffY);
            float srcLeft = mSrcRect.left + diffDst * PINCHZOOM_RATIO;
            float srcRight = mSrcRect.right - diffDst * PINCHZOOM_RATIO;
            float srcTop = mSrcRect.top + diffY * PINCHZOOM_RATIO;
            float srcBottom = mSrcRect.bottom - diffY * PINCHZOOM_RATIO;
//            Log.d(TAG, "srcLeft:"+srcLeft+"_srcRight:"+srcRight+"_srcTop:"+srcTop+"_srcBottom:"+srcBottom);

            Rect prevRect = new Rect(this.mSrcRect);

            mSrcRect = checkRectInOrg(new Rect((int) srcLeft, (int) srcTop,
                    (int) srcRight, (int) srcBottom));

//            Log.d(TAG, "getZoomRatio():"+getZoomRatio());
            mNotZoom = false;
            if (getZoomRatio() > MAX_ZOOM_RATIO || getZoomRatio() <= MIN_ZOOM_RATIO) {
                mNotZoom = true;
                this.mSrcRect = prevRect;
            }
//            Log.d(TAG, "mSrcRect:"+mSrcRect);
        } else {
            Log.d(TAG, "mSrcRect is null");
        }
    }

    private Rect checkRectSizeInOrg(Rect drawRect) {
        float srcWidth = drawRect.width(), srcHeight = drawRect.height();

        float newWidth = 0.0f, newHeight = 0.0f;

        if (drawRect.width() > mOrgSrcWidth) {
            newWidth = mOrgSrcWidth;
            newHeight = mOrgSrcWidth * srcHeight / srcWidth;

            drawRect.set(drawRect.left, drawRect.top,
                    (int) (drawRect.left + newWidth),
                    (int) (drawRect.top + newHeight));
        }

        if (drawRect.height() > mOrgSrcHeight) {
            newHeight = mOrgSrcHeight;
            newWidth = mOrgSrcHeight * srcWidth / srcHeight;

            drawRect.set(drawRect.left, drawRect.top,
                    (int) (drawRect.left + newWidth),
                    (int) (drawRect.top + newHeight));
        }
        return drawRect;
    }

    private Rect checkRectInOrg(Rect tempRect) {
        tempRect = checkRectSizeInOrg(tempRect);
        int diff = 0;

        if (mOriginRect != null && mOriginRect.contains(tempRect) == false) {
            if (tempRect.left < 0) {
                tempRect.right = tempRect.right - tempRect.left;
                tempRect.left = 0;
            }

            if (tempRect.right > mOriginRect.right) {
                diff = mOriginRect.right - tempRect.right;
                tempRect.left = tempRect.left + diff;
                tempRect.right = tempRect.right + diff;
            }

            if (tempRect.top < 0) {
                tempRect.bottom = tempRect.bottom - tempRect.top;
                tempRect.top = 0;
            }

            if (tempRect.bottom > mOriginRect.bottom) {
                diff = mOriginRect.bottom - tempRect.bottom;
                tempRect.top = tempRect.top + diff;
                tempRect.bottom = tempRect.bottom + diff;
            }
        }

        return tempRect;
    }

    private float getZoomRatio() {
        float widthRatio = (float) mOriginRect.width() / (float) mSrcRect.width();
        float heightRatio = (float) mOriginRect.height() / (float) mSrcRect.height();
        float zoomRatio = Math.min(widthRatio, heightRatio);
        return zoomRatio;
    }

    public void drawGrid(Canvas canvas, Path path) {
        /* drawGrid grid */
        // drawGrid roundness
        drawRoundnessRect(canvas, path);
        // drawGrid images
        drawGridImage(canvas, path);
        // drawGrid touched image
        if (mIsSelected) {
            drawSelectedRect(canvas, path);
            // drawResizeRect(canvas);
        }
    }

    public void drawPile(Canvas canvas, Path canvasPath) {
        /* drawGrid pile */
        drawPileImage(canvas, canvasPath);
        // drawGrid touched image
        if (mIsSelected) {
            drawSelectedPath(canvas);
        }
    }

//    public void scanOnClick() {
//        Flog.d("scanOnClick");
//        Flog.d("!mIsOpenGallery: "+!mIsOpenGallery);
//        Flog.d("mPhotoBmp == mOriginBmp: "+(mPhotoBmp == mOriginBmp));
//        Flog.d("listener != null: "+(listener != null));
//
//        if (!mIsOpenGallery && mPhotoBmp == mOriginBmp && listener != null) {
//            mIsOpenGallery = true;
//            listener.onChoosePhotoClick(this);
//        }
//    }

    private void drawPileImage(Canvas canvas, Path canvasPath) {
        if (canvas == null || canvasPath == null)
            return;
        try {
            canvas.clipPath(canvasPath, Region.Op.REPLACE);
            canvas.clipPath(mPathPile, Region.Op.INTERSECT);
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, "clipPath() not supported");
        }
        canvas.drawBitmap(mPhotoBmp, mSrcRect, mDesRect, mPhotoPaint);
    }

    private void drawResizeRect(Canvas canvas) {
//        // drawGrid left
//        drawResizeRect(canvas, mDesRect.left, (mDesRect.top + mDesRect.bottom) / 2, dst_resize_left, resizeBitmapLeft);
//        // drawGrid left-top
//        drawResizeRect(canvas, mDesRect.left, mDesRect.top, dst_resize_lefttop, resizeBitmapLeftTop);
//        // drawGrid top
//        drawResizeRect(canvas, (mDesRect.left + mDesRect.right) / 2, mDesRect.top, dst_resize_top, resizeBitmapTop);
//        // drawGrid right-top
//        drawResizeRect(canvas, mDesRect.right, mDesRect.top, dst_resize_righttop, resizeBitmapRightTop);
//        // drawGrid right
//        drawResizeRect(canvas, mDesRect.right, (mDesRect.top + mDesRect.bottom) / 2, dst_resize_right, resizeBitmapRight);
//        // drawGrid right-bottom
//        drawResizeRect(canvas, mDesRect.right, mDesRect.bottom, dst_resize_rightbottom, resizeBitmapRightBottom);
//        // drawGrid bottom
//        drawResizeRect(canvas, (mDesRect.left + mDesRect.right) / 2, mDesRect.bottom, dst_resize_bottom, resizeBitmapBottom);
//        // drawGrid left-bottom
//        drawResizeRect(canvas, mDesRect.left, mDesRect.bottom, dst_resize_leftbottom, resizeBitmapLeftBottom);
    }

    private void drawResizeRect(Canvas canvas, float x, float y, Rect dst_resize, Bitmap resizeBitmap) {
        if (dst_resize == null) return;
        dst_resize.left = (int) (x - resizeBitmapWidth / 2);
        dst_resize.right = (int) (x + resizeBitmapWidth / 2);
        dst_resize.top = (int) (y - resizeBitmapHeight / 2);
        dst_resize.bottom = (int) (y + resizeBitmapHeight / 2);

        canvas.clipRect(dst_resize, Region.Op.REPLACE);
        canvas.drawBitmap(resizeBitmap, null, dst_resize, null);
    }

    private void drawGridImage(Canvas canvas, Path path) {

        if (mPhotoBmp == null || mPhotoBmp.isRecycled()) {
            Flog.d("Disappear", "mPhotoBmp: " + mPhotoBmp + "_mPhotoBmp.isRecycled() :" + mPhotoBmp.isRecycled());
            return;
        }
        try {
            canvas.drawPath(path, mXferPaint);  // anti-alias
            canvas.clipPath(path, Region.Op.INTERSECT);
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, "clipPath() not supported");
        }
        canvas.drawBitmap(mPhotoBmp, mSrcRect, mDesRect, mPhotoPaint);
    }

    private void drawRoundnessRect(Canvas canvas, Path path) {
        mPathRoundRect.reset();
        mPathRoundRect.addRoundRect(mDesRect, mRoundSize, mRoundSize, Path.Direction.CW);
        try {
            canvas.clipPath(mPathRoundRect, Region.Op.REPLACE);
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, "clipPath() not supported");
        }
    }

    public void drawSelectedRect(Canvas canvas, Path path) {
        canvas.drawPath(path, mDrawLinePaint);
        canvas.drawPath(mPathRoundRect, mDrawLinePaint);
//        canvas.drawRoundRect(mDesRect, mRoundSize, mRoundSize, mDrawLinePaint);
    }

    public float getRoundSize() {
        return mRoundSize;
    }

    public void setRoundSize(float roundSize) {
        mRoundSize = roundSize;
    }

    private void setCustomPaint() {
        // set Photo paint
        mPhotoPaint = new Paint();
        mPhotoPaint.setFilterBitmap(true);
        mPhotoPaint.setAntiAlias(true);
        mPhotoPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
//        if (android.os.Build.VERSION.SDK_INT >= 14) {
//            // config AndroidManifest : android:hardwareAccelerated="false"
//            mPhotoPaint.setShadowLayer(10.0f, 0.0f, 2.0f, Color.BLACK);
//        }

        // set Line Round paint
        mDrawLinePaint = new Paint();
        mDrawLinePaint.setColor(Color.rgb(0, 174, 207));    // light blue color
        mDrawLinePaint.setStrokeWidth((int) mContext.getResources()
                .getDimension(R.dimen.collage_select_line_width));
        mDrawLinePaint.setStyle(Paint.Style.STROKE);
        mDrawLinePaint.setAntiAlias(true);

        // set anti-alias when clip path for paint
        mXferPaint = new Paint();
//        mXferPaint.setStyle(Paint.Style.FILL);
//        mXferPaint.setColor(Color.RED);
//        mXferPaint.setStrokeWidth(.5f);
        mXferPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mXferPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
    }

    public void setIsSelected(boolean isSelected) {
        mIsSelected = isSelected;
    }

    public void setDesRect(float left, float top, float right, float bottom) {
        mDesRect.set(left, top, right, bottom);
    }

    public int getOrgPixelSize() {
        return (mOrgSrcWidth * mOrgSrcHeight);
    }

    public int getOrgSrcWidth() {
        return mOrgSrcWidth;
    }

    public int getOrgSrcHeight() {
        return mOrgSrcHeight;
    }

    public boolean isInResize(MotionEvent event) {
        int left = -20 + this.dst_resize_left.left;
        int top = -20 + this.dst_resize_left.top;
        int right = 20 + this.dst_resize_left.right;
        int bottom = 20 + this.dst_resize_left.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int pos) {
        mPriority = pos;
    }

    @Override
    public void release() {
        mPhotoBmp = ImageUtils.recycleBitmap(mPhotoBmp);
        mSrcRect = null;
        mOriginRect = null;
        mDesRect = null;
        mDrawLinePaint = null;
        mXferPaint = null;
        mPhotoPaint = null;
        mPathRoundRect = null;
        mPathPile = null;
    }

    @Override
    public float[] getCurrentPosition() {
        return null;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    private void drawSelectedPath(Canvas canvas) {
        canvas.drawPath(mPathPile, mDrawLinePaint);
    }

    public void setPathPile(Path pathPile) {
        mPathPile = pathPile;
    }

    public void fitRectSizeRatio() {
//        Flog.d("fitRectSizeRatio");
//        Flog.d("src: " + mSrcRect + "_des: " + mDesRect);
        Rect newRect = calculateRectSize(mSrcRect, mDesRect);

//        Flog.d("newRect: " + newRect);
        if (newRect != null) {
            setSrcRect(newRect);
        }
    }

    public Rect calculateRectSize(Rect srcRect, RectF drawRect) {
        if (srcRect == null || drawRect == null) {
            return null;
        }

        Rect returnRect = null;

        float drawWidth = drawRect.width(), drawHeight = drawRect.height();

        float srcWidth = srcRect.width(), srcHeight = srcRect.height();

        if (srcWidth < srcHeight) {
            srcWidth = srcHeight * drawWidth / drawHeight;
        } else {
            srcHeight = srcWidth * drawHeight / drawWidth;
        }

        returnRect = new Rect((int) (srcRect.left), (int) (srcRect.top),
                (int) (srcRect.left + srcWidth),
                (int) (srcRect.top + srcHeight));

        return returnRect;
    }

    public boolean isOpenGallery() {
        return mContainsContent;
    }

    public void containsContent(boolean con) {
        mContainsContent = con;
    }

    public boolean isContainsContent() {
        return mContainsContent;
    }

    public PointF getMidPointOfRect() {
        PointF midPoint = new PointF();
        float x = (mDesRect.right + mDesRect.left)/2;
        float y = (mDesRect.top + mDesRect.bottom)/2;
        Log.d("getMidPointOfRect", "x="+x+"_y="+y);
        midPoint.set(x, y);
        return midPoint;
    }

    public boolean isNotZoom() {
        return mNotZoom;
    }

    public boolean isNotMove() {
        return mNotMove;
    }
}
