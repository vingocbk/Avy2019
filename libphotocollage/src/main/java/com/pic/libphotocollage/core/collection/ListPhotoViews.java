package com.pic.libphotocollage.core.collection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.MotionEvent;

import com.pic.libphotocollage.core.layout.grid.GridStatistic;
import com.pic.libphotocollage.core.layout.pile.PileStatistic;
import com.pic.libphotocollage.core.model.ItemPhotoView;
import com.pic.libphotocollage.core.util.Flog;
import com.pic.libphotocollage.core.util.ImageUtils;

import java.util.ArrayList;

/**
 * Created by hoavt on 26/07/2016.
 */
public class ListPhotoViews extends ArrayList<ItemPhotoView> {

    private static final String TAG = ListPhotoViews.class.getSimpleName();
    private final int MOVE_TOLLERENCE = 4;
    private int mCurPhotoIndex = -1;
    private int mDraggedPhotoIndex = -1;
    private float mPrevX = 0.0f;
    private float mPrevY = 0.0f;
    private boolean mIsSavePhoto;
    private float mChangedRatio = 1.0f;
    private float mRatioView = 1.0f;
    private int mBgColor = Color.WHITE;

    private boolean mIsDrawGrid = true;
    private Context mContext;
    private RectF mDraggingRect;
    private Bitmap mPileFrameImg;
    private Bitmap mBgBitmap = null;
    private Bitmap mFrameBitmap;
    private String frameName = null;
    private Rect mBackgroundRect;
    private Path mCanvasPath;
    private ArrayList<Path> mGridClipPaths;
    private ArrayList<Path> mPileClipPaths;
    private OnActionDownPhotoItemListenner mOnScanGalleryListenner = null;
    private Paint mPileFramePaint;
    private int[] mDrawOrders;
    private ArrayList<Region> mPathRegions = new ArrayList<>();
    private float mMargin;
    private Path mBgPath;

    public ListPhotoViews(Context context) {
        mContext = context;
        initPaint();
        setBgCanvas();
    }

    private void setBgCanvas() {
        mBgPath = new Path();
    }

    private void initPaint() {
        mPileFramePaint = new Paint();
        mPileFramePaint.setAntiAlias(true);
        mPileFramePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    public void setDrawOrders(int[] drawOrders) {
        mDrawOrders = drawOrders;
    }

    public int getTouchedIndex(MotionEvent event) {
        if (event == null)
            return -1;

        int index = getTouchedIndex(event.getX(), event.getY());
        return index;
    }

    public int getTouchedIndex(float x, float y) {
        int index = -1;

        if (mPathRegions.size() != this.size()) {
            return index;
        }
        for (int i = this.size() - 1; i >= 0; i--) {
            if (mPathRegions.get(i).contains((int) x, (int) y)) {
                index = i;
                break;
            }
        }
//            for (int i = this.size() - 1; i >= 0; i--) {
//                if (this.get(i).getDesRect().contains(x, y) == true) {
//                    index = i;
//                    break;  // return rect on the top-most
//                }
//            }
        return index;
    }

    public ItemPhotoView getCurPhotoItem() {
        if (mCurPhotoIndex == -1) return null;
        return this.get(mCurPhotoIndex);
    }

    public int getCurPhotoIndex() {
        return mCurPhotoIndex;
    }

    public void setCurPhotoIndex(int curPhotoIndex) {
        mCurPhotoIndex = curPhotoIndex;
        if (mCurPhotoIndex == -1) {
            setNotTouchAll();
        } else {
            setCurPhotoTouched();
        }
    }

    private void setNotTouchAll() {
        for (int i = 0; i < this.size(); i++) {
            get(i).setIsSelected(false);
        }
    }

    private void setCurPhotoTouched() {
        for (int i = 0; i < this.size(); i++) {
            if (i == mCurPhotoIndex)
                get(i).setIsSelected(true);
            else
                get(i).setIsSelected(false);
        }
    }

    public boolean moveItemStart(int index, float pointX, float pointY) {
        if (isIndexInList(index)) {
            mCurPhotoIndex = index;
            mPrevX = pointX;
            mPrevY = pointY;

            return true;
        } else {
            return false;
        }
    }

    public boolean isIndexInList(int index) {
        if (index < 0 || index >= this.size()) {
            return false;
        }
        return true;
    }

    public boolean moveItemPhoto(int index, float pointX, float pointY) {
        Flog.i("moveItemPhoto : mCurPhotoIndex=" + mCurPhotoIndex);
        if (!isIndexInList(mCurPhotoIndex)) {
            Flog.i("Not isIndexInList");
            return false;
        }

        if (mCurPhotoIndex != index) {
            mCurPhotoIndex = -1;
            Flog.i("mCurPhotoIndex = -1");
            return false;
        }

        int diffRight = (int) (pointX - mPrevX);
        int diffBottom = (int) (pointY - mPrevY);

        mPrevX = pointX;
        mPrevY = pointY;

        if (Math.abs(diffRight) > MOVE_TOLLERENCE || Math.abs(diffBottom) > MOVE_TOLLERENCE) {
            this.get(mCurPhotoIndex).moveSrcRect(diffRight, diffBottom);
            return true;
        }

        return false;
    }

    public void pinchZoomItemPhoto(int index, int diffDst) {
        if (isIndexInList(index)) {
            this.get(index).pinchZoom(diffDst);
        }
    }

    public void setPileFrameImg(Bitmap pileFrameImg) {
        mPileFrameImg = ImageUtils.recycleBitmap(mPileFrameImg);
        mPileFrameImg = pileFrameImg;
    }

    public void release() {
        mDraggingRect = null;
        frameName = null;
        mBackgroundRect = null;
        mCanvasPath = null;
        if (mPileClipPaths != null) {
            mPileClipPaths.clear();
            mPileClipPaths = null;
        }
        if (mGridClipPaths != null) {
            mGridClipPaths.clear();
            mGridClipPaths = null;
        }
        mPathRegions.clear();
        mOnScanGalleryListenner = null;
        mPileFramePaint = null;
        mPileFrameImg = ImageUtils.recycleBitmap(mPileFrameImg);
        mBgBitmap = ImageUtils.recycleBitmap(mBgBitmap);
        mFrameBitmap = ImageUtils.recycleBitmap(mFrameBitmap);
        for (ItemPhotoView item : this) {
            item.release();
        }
        this.clear();
    }

    public void draws(Canvas canvas) {
        drawBackground(canvas, mBgBitmap, mBgColor);

        if (mIsDrawGrid) {
            drawGridImage(canvas);
        } else {
            setPilePaths();
            drawPileImage(canvas);

            if (mCanvasPath != null) {
                try {
                    canvas.clipPath(mCanvasPath, Region.Op.REPLACE);
                } catch (UnsupportedOperationException e) {
                    Log.e(TAG, "clipPath() not supported");
                }
            }
            drawPileFrame(canvas); // frame image
        }

        canvas.clipRect(mBackgroundRect, Region.Op.REPLACE);
        drawFrame(canvas, mFrameBitmap);
    }

    private void drawPileImage(Canvas canvas) {
        int orderDraw[] = getDrawOrder();
        for (int i = 0; i < orderDraw.length; i++) {
            ItemPhotoView itemPhotoView = this.get(orderDraw[i]);
            itemPhotoView.drawPile(canvas, mCanvasPath);
        }
    }

    private void drawGridImage(Canvas canvas) {

        if (mGridClipPaths != null) {
            mGridClipPaths.clear();
        }

        mGridClipPaths = GridStatistic.getGridPointPaths(mBackgroundRect, mMargin);

        if (mGridClipPaths == null) {
            return;
        }

        mPathRegions.clear();
        for (int i = 0; i < mGridClipPaths.size(); i++) {
            canvas.clipPath(mBgPath, Region.Op.REPLACE);
            ItemPhotoView srcItem = this.get(i);
            if (srcItem == null) {
                Flog.i(i + "_srcItem == null");
                continue;
            }
            srcItem.drawGrid(canvas, mGridClipPaths.get(i));
            setPathBounds(mGridClipPaths.get(i));
        }

        canvas.clipPath(mBgPath, Region.Op.REPLACE);
    }

    private void setPathBounds(Path p) {
        RectF rectF = new RectF();
        p.computeBounds(rectF, true);
        Region r = new Region();
        r.setPath(p, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        mPathRegions.add(r);
    }

    private int[] getDrawOrder() {
        return mDrawOrders;
    }

    private void setPilePaths() {
        if (mPileClipPaths != null) {
            mPileClipPaths.clear();
        }

        mPileClipPaths = PileStatistic.getPilePaths(mBackgroundRect);

        if (mPileClipPaths == null) {
            Flog.i("mPileClipPaths == null");
            return;
        }

        if (mPileClipPaths.size() != this.size()) {
            Flog.i("mPileClipPaths.size() != this.size()");
            return;
        }

        mPathRegions.clear();
        for (int i = 0; i < mPileClipPaths.size(); i++) {
            ItemPhotoView itemPhotoView = this.get(i);
            itemPhotoView.setPathPile(mPileClipPaths.get(i));
            setPathBounds(mPileClipPaths.get(i));
        }
    }

    private void drawPileFrame(Canvas canvas) {
        if (mPileFrameImg == null || mPileFrameImg.isRecycled()) {
            return;
        }
//        Log.d("Sony", "drawPileFrame");
        try {
            canvas.clipRect(mBackgroundRect, Region.Op.REPLACE);
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, "clipPath() not supported");
        }
//        Log.d("Sony", "mBackgroundRect: " + mBackgroundRect.width() + " height = " + mBackgroundRect.height());
        Bitmap temp = Bitmap.createScaledBitmap(mPileFrameImg, mBackgroundRect.width(), mBackgroundRect.height(),
                true);
//        canvas.drawBitmap(temp, new Rect(0, 0, temp.getWidth(),
//                temp.getHeight()), mBackgroundRect, mPileFramePaint);
        if (mIsSavePhoto) {
            canvas.drawBitmap(temp, new Rect(0, 0, temp.getWidth(),
                    temp.getHeight()), mBackgroundRect, mPileFramePaint);
        } else {
            // anti-alias
            canvas.drawBitmap(temp, new Rect(0, 0, temp.getWidth(),
                    temp.getHeight()), mBackgroundRect, null);
        }
        ImageUtils.recycleBitmap(temp);
    }

    private void drawFrame(Canvas canvas) {
        if (frameName != null) {
            Bitmap frameImg = ImageUtils.loadBitmapFromAssets(mContext, frameName);
            if (frameImg != null) {
                if (frameImg == null || frameImg.isRecycled()) {
                    return;
                }

                int paddingStart = mBackgroundRect.left;
                int paddingTop = mBackgroundRect.top;
                Bitmap temp = Bitmap.createScaledBitmap(frameImg,
                        mBackgroundRect.width(), mBackgroundRect.height(), true);
                canvas.drawBitmap(temp, paddingStart, paddingTop, null);
                temp.recycle();
                temp = null;
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
//        bm.recycle();
        return resizedBitmap;
    }

    private void drawFrame(Canvas canvas, Bitmap frImag) {

        if (frImag == null || frImag.isRecycled()) {
            Flog.d("frImag == null");
            return;
        }

        if (mIsSavePhoto) {
            frImag = Bitmap.createScaledBitmap(frImag,
                    (int) (frImag.getWidth() * mChangedRatio),
                    (int) (frImag.getHeight() * mChangedRatio),
                    false);
        }

        int patternImgWidth = frImag.getWidth();
        int patternImgHeight = frImag.getHeight();
        Bitmap topFrame = cutFrame(frImag, 0, patternImgHeight / 6, patternImgWidth, patternImgHeight);
        Bitmap leftFrame = cutFrame(frImag, patternImgWidth / 6, 0, patternImgWidth, patternImgHeight);
        Bitmap bottomFrame = cutFrame(frImag, 0, 0, patternImgWidth, patternImgHeight * 5 / 6);
        Bitmap rightFrame = cutFrame(frImag, 0, 0, patternImgWidth * 5 / 6, patternImgHeight);

        int widthMax = mBackgroundRect.width() / patternImgWidth;
        int heightMax = mBackgroundRect.height() / patternImgHeight;

        int paddingStart = mBackgroundRect.left;
        int paddingTop = mBackgroundRect.top;
        Log.d("frame", "mBackgroundRect= width: " + mBackgroundRect.width() + "_height: " + mBackgroundRect.height());
        Log.d("frame", "pattern= width: " + patternImgWidth + "_height: " + patternImgHeight);
        Log.d("frame", "max= width: " + widthMax + "_height: " + heightMax);

        if (mBackgroundRect.width() > patternImgWidth
                && mBackgroundRect.height() > patternImgHeight) {

//            if (mBackgroundRect.width() % patternImgWidth > 0) {
//                widthMax++;
//            }
//
//            if (mBackgroundRect.height() % patternImgHeight > 0) {
//                heightMax++;
//            }

//            boolean tabletSize = mContext.getResources().getBoolean(R.bool.isTablet);
//            if (tabletSize) {
//                // Tablet for 1:1 aspect ratio:
//                if (mRatioView == 1.0f || mRatioView == 3 / 4f) {
//                    for (int i = 0; i < widthMax; i++) {
//                        for (int j = 0; j < heightMax; j++) {
//                            if (i == 0) {   // left frame
//                                canvas.drawBitmap(leftFrame
//                                        , paddingStart + leftFrame.getWidth() * i
//                                        , paddingTop + leftFrame.getHeight() * j
//                                        , null);
//                            }
//                            if (j == 0) {   // top frame
//                                canvas.drawBitmap(topFrame
//                                        , paddingStart + topFrame.getWidth() * i
//                                        , paddingTop + topFrame.getHeight() * j
//                                        , null);
//                            }
//                            if ((i == widthMax - 1)) {     // right frame
//                                canvas.drawBitmap(rightFrame
//                                        , paddingStart + rightFrame.getWidth() * i + (mBackgroundRect.width() % rightFrame.getWidth())
//                                        , paddingTop + rightFrame.getHeight() * j
//                                        , null);
//                            }
//                            if ((j == heightMax - 1)) {   // bottom frame
//                                canvas.drawBitmap(bottomFrame
//                                        , paddingStart + bottomFrame.getWidth() * i
//                                        , paddingTop + bottomFrame.getHeight() * j + (mBackgroundRect.height() % bottomFrame.getHeight())
//                                        , null);
//                            }
//                        }
//                    }
//                } else if (mRatioView == 3 / 2f || mRatioView == 2 / 3f || mRatioView == 4 / 3f || mRatioView == 5 / 4f || mRatioView == 16 / 9f) {
//                    for (int i = 0; i < widthMax; i++) {
//                        for (int j = 0; j < heightMax; j++) {
//                            if (i == 0) {   // left frame
//                                canvas.drawBitmap(leftFrame
//                                        , paddingStart + leftFrame.getWidth() * i
//                                        , paddingTop + leftFrame.getHeight() * j
//                                        , null);
//                            }
//                            if (j == 0) {   // top frame
//                                canvas.drawBitmap(topFrame
//                                        , paddingStart + topFrame.getWidth() * i
//                                        , paddingTop + topFrame.getHeight() * j
//                                        , null);
//                            }
//                            if ((i == widthMax - 2)) {     // right frame
//                                canvas.drawBitmap(rightFrame
//                                        , paddingStart + rightFrame.getWidth() * i + (mBackgroundRect.width() % rightFrame.getWidth())
//                                        , paddingTop + rightFrame.getHeight() * j
//                                        , null);
//                            }
//                            if ((j == heightMax - 2)) {   // bottom frame
//                                canvas.drawBitmap(bottomFrame
//                                        , paddingStart + bottomFrame.getWidth() * i
//                                        , paddingTop + bottomFrame.getHeight() * j + (mBackgroundRect.height() % bottomFrame.getHeight())
//                                        , null);
//                            }
//                        }
//                    }
//                } else if (mRatioView == 4 / 5f || mRatioView == 9 / 16f) {
//                    for (int i = 0; i < widthMax; i++) {
//                        for (int j = 0; j < heightMax; j++) {
//                            if (i == 0) {   // left frame
//                                canvas.drawBitmap(leftFrame
//                                        , paddingStart + leftFrame.getWidth() * i
//                                        , paddingTop + leftFrame.getHeight() * j
//                                        , null);
//                            }
//                            if (j == 0) {   // top frame
//                                canvas.drawBitmap(topFrame
//                                        , paddingStart + topFrame.getWidth() * i
//                                        , paddingTop + topFrame.getHeight() * j
//                                        , null);
//                            }
//                            if ((i == widthMax - 1)) {     // right frame
//                                canvas.drawBitmap(rightFrame
//                                        , paddingStart + rightFrame.getWidth() * i + (mBackgroundRect.width() % rightFrame.getWidth())
//                                        , paddingTop + rightFrame.getHeight() * j
//                                        , null);
//                            }
//                            if ((j == heightMax - 2)) {   // bottom frame
//                                canvas.drawBitmap(bottomFrame
//                                        , paddingStart + bottomFrame.getWidth() * i
//                                        , paddingTop + bottomFrame.getHeight() * j + (mBackgroundRect.height() % bottomFrame.getHeight())
//                                        , null);
//                            }
//                        }
//                    }
//                }
//            } else {
//                Log.d("frame", "mBackgroundRect.width() % rightFrame.getWidth(): "+mBackgroundRect.width() % rightFrame.getWidth());
//                Log.d("frame", "mBackgroundRect.height() % bottomFrame.getHeight(): "+mBackgroundRect.height() % bottomFrame.getHeight());
//                // Smartphone:
//                for (int i = 0; i < widthMax; i++) {
//                    for (int j = 0; j < heightMax; j++) {
//                        if (i == 0) {   // left frame
//                            canvas.drawBitmap(leftFrame
//                                    , paddingStart + leftFrame.getWidth() * i
//                                    , paddingTop + leftFrame.getHeight() * j
//                                    , null);
//                        }
//                        if (j == 0) {   // top frame
//                            canvas.drawBitmap(topFrame
//                                    , paddingStart + topFrame.getWidth() * i
//                                    , paddingTop + topFrame.getHeight() * j
//                                    , null);
//                        }
//                        if ((i == widthMax - 2)) {     // right frame
//                            canvas.drawBitmap(rightFrame
//                                    , paddingStart + rightFrame.getWidth() * i + (mBackgroundRect.width() % rightFrame.getWidth())
//                                    , paddingTop + rightFrame.getHeight() * j
//                                    , null);
//                        }
//                        if ((j == heightMax - 2)) {   // bottom frame
//                            canvas.drawBitmap(bottomFrame
//                                    , paddingStart + bottomFrame.getWidth() * i
//                                    , paddingTop + bottomFrame.getHeight() * j + (mBackgroundRect.height() % bottomFrame.getHeight())
//                                    , null);
//                        }
//                    }
//                }
//            }
            int fitBottom = (mBackgroundRect.height() % bottomFrame.getHeight());
            int fitRight = (mBackgroundRect.width() % rightFrame.getWidth());
            Log.d("frame", "fitBottom: " + fitBottom);
            Log.d("frame", "fitRight: " + fitRight);
            // Smartphone:
            for (int i = 0; i < widthMax; i++) {
                for (int j = 0; j < heightMax; j++) {
                    if (i == 0) {   // left frame
                        canvas.drawBitmap(leftFrame
                                , paddingStart + leftFrame.getWidth() * i
                                , paddingTop + leftFrame.getHeight() * j
                                , null);
                    }
                    if (j == 0) {   // top frame
                        canvas.drawBitmap(topFrame
                                , paddingStart + topFrame.getWidth() * i
                                , paddingTop + topFrame.getHeight() * j
                                , null);
                    }
                    if ((i == widthMax - 1)) {     // right frame
                        canvas.drawBitmap(rightFrame
                                , paddingStart + rightFrame.getWidth() * i + fitRight
                                , paddingTop + rightFrame.getHeight() * j
                                , null);
                    }
                    if ((j == heightMax - 1)) {   // bottom frame
                        canvas.drawBitmap(bottomFrame
                                , paddingStart + bottomFrame.getWidth() * i
                                , paddingTop + bottomFrame.getHeight() * j + fitBottom
                                , null);
                    }
                }
            }
            if (fitBottom > 0) {
                // draw addition to left and right:
                canvas.drawBitmap(leftFrame
                        , paddingStart
                        , paddingTop + leftFrame.getHeight() * (heightMax - 1) + fitBottom
                        , null);
                canvas.drawBitmap(rightFrame
                        , paddingStart + rightFrame.getWidth() * (widthMax - 1) + fitRight
                        , paddingTop + rightFrame.getHeight() * (heightMax - 1) + fitBottom
                        , null);
            }
            if (fitRight > 0) {
                // draw addition to top and bottom:
                canvas.drawBitmap(topFrame
                        , paddingStart + topFrame.getWidth() * (widthMax - 1) + fitRight
                        , paddingTop
                        , null);
                canvas.drawBitmap(bottomFrame
                        , paddingStart + bottomFrame.getWidth() * (widthMax - 1) + fitRight
                        , paddingTop + bottomFrame.getHeight() * (heightMax - 1) + fitBottom
                        , null);
            }

            // recycle bitmaps:
            topFrame = ImageUtils.recycleBitmap(topFrame);
            leftFrame = ImageUtils.recycleBitmap(leftFrame);
            rightFrame = ImageUtils.recycleBitmap(rightFrame);
            bottomFrame = ImageUtils.recycleBitmap(bottomFrame);
        } else if (mBackgroundRect.width() == patternImgWidth
                && mBackgroundRect.height() == patternImgHeight) {
            canvas.drawBitmap(frImag, paddingStart, paddingTop, null);
        } else {
            Bitmap temp = Bitmap.createScaledBitmap(frImag, mBackgroundRect.width(),
                    mBackgroundRect.height(), true);
            canvas.drawBitmap(temp, paddingStart, paddingTop, null);
            temp.recycle();
            temp = null;
        }
    }

    private Bitmap cutFrame(Bitmap originImag, int top, int left, int right, int bottom) {
//        Bitmap bmOverlay = Bitmap.createScaledBitmap(originImag, originImag.getWidth(), originImag.getHeight(), false);
        Bitmap bmOverlay = Bitmap.createBitmap(originImag.getWidth(), originImag.getHeight(), Bitmap.Config.ARGB_4444);

        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        float shadowSize = 1;
//        float deltaX = 0;
//        float deltaY = 0;
//        int shadowColor = Color.YELLOW;
//        p.setShadowLayer(shadowSize, deltaX, deltaY, shadowColor);
        Canvas c = new Canvas(bmOverlay);
        c.drawBitmap(originImag, 0, 0, null);
        c.drawRect(top, left, right, bottom, p);
        return bmOverlay;
    }

    private void drawBackground(Canvas canvas, Bitmap bgBitmap, @ColorInt int color) {
//        canvas.clipRect(mBackgroundRect, Region.Op.REPLACE);

        if (bgBitmap != null) {
            drawBackgroundPattern(canvas, bgBitmap);
        } else {
            canvas.drawColor(color);
        }
    }

    public void setIsSavePhoto(boolean isSavePhoto) {
        mIsSavePhoto = isSavePhoto;
    }

    private void drawBackgroundPattern(Canvas canvas, Bitmap bgImag) {
        if (bgImag == null || bgImag.isRecycled()) {
            return;
        }

        if (mIsSavePhoto) {
            bgImag = Bitmap.createScaledBitmap(bgImag,
                    (int) (bgImag.getWidth() * mChangedRatio),
                    (int) (bgImag.getHeight() * mChangedRatio),
                    false);
        }

        if (bgImag == null || (int) bgImag.getWidth() == 0 || (int) bgImag.getHeight() == 0)
            return;

        int patternImgWidth = (int) (bgImag.getWidth());
        int patternImgHeight = (int) (bgImag.getHeight());

        int widthMax = mBackgroundRect.width() / patternImgWidth;
        int heightMax = mBackgroundRect.height() / patternImgHeight;

        int paddingStart = mBackgroundRect.left;
        int paddingTop = mBackgroundRect.top;

        if (mBackgroundRect.width() > patternImgWidth
                && mBackgroundRect.height() > patternImgHeight) {

            if (mBackgroundRect.width() % patternImgWidth > 0) {
                widthMax++;
            }

            if (mBackgroundRect.height() % patternImgHeight > 0) {
                heightMax++;
            }

            // drawGrid looping image
            for (int i = 0; i < widthMax; i++) {
                for (int j = 0; j < heightMax; j++) {
                    canvas.drawBitmap(bgImag
                            , paddingStart + patternImgWidth * i
                            , paddingTop + patternImgHeight * j
                            , null);
                }
            }

        } else if (mBackgroundRect.width() == patternImgWidth
                && mBackgroundRect.height() == patternImgHeight) {
            canvas.drawBitmap(bgImag, paddingStart, paddingTop, null);

        } else {
            Bitmap temp = Bitmap.createScaledBitmap(bgImag, mBackgroundRect.width(),
                    mBackgroundRect.height(), true);
            canvas.drawBitmap(temp, paddingStart, paddingTop, null);
            temp.recycle();
            temp = null;
        }
    }

    public void setRoundnessRects(float roundNess) {
        for (ItemPhotoView itemPhotoView : this) {
            itemPhotoView.setRoundSize(roundNess);
        }
    }

    public Rect getBackgroundRect() {
        return mBackgroundRect;
    }

    public void setBackgroundRect(Rect backgroundRect) {
        mBackgroundRect = backgroundRect;
        mBgPath.addRect(new RectF(mBackgroundRect), Path.Direction.CW);
    }

    public RectF getBackgroundRectF() {
        return new RectF(mBackgroundRect);
    }

    public boolean checkDraggingCollision(MotionEvent event, int curPhotoIndex) {
        for (int i = this.size() - 1; i >= 0; i--) {
            if (i != curPhotoIndex) {
                ItemPhotoView itemPhotoView = this.get(i);
                if (itemPhotoView.getDesRect().contains(event.getX(), event.getY())) {
                    setDraggedPhotoIndex(i);
                    return true;
                }
            }
        }
        return false;
    }

    public int getDraggedPhotoIndex() {
        return mDraggedPhotoIndex;
    }

    public void setDraggedPhotoIndex(int draggedPhotoIndex) {
        mDraggedPhotoIndex = draggedPhotoIndex;
    }

    public void swapTwoPhotoItems(int curPhotoIndex, int draggedPhotoIndex) {
        if (curPhotoIndex == -1)
            curPhotoIndex = 0;

        ItemPhotoView curItem = this.get(curPhotoIndex);
        ItemPhotoView draggedItem = this.get(draggedPhotoIndex);
        Log.d("swap", "curItem: " + curItem.getPriority() + "_draggedItem: " + draggedItem.getPriority());
        Log.d("swap", "curItem a: " + curPhotoIndex + "_draggedItem: " + draggedPhotoIndex);
        Bitmap temp = curItem.getPhotoBmp();
        /* Drag and drop between 2 object that have contains image*/
//        curItem.setPhotoBmp(draggedItem.getPhotoBmp());
//        curItem.setDefaultSrcRect();
        curItem.swapBitmap(draggedItem);
//        draggedItem.setPhotoBmp(temp);
//        draggedItem.setDefaultSrcRect();
        Flog.d(Flog.CTAG, "Bitmap recycler: curItem=" + curItem.getPhotoBmp().isRecycled()
                + "_dragItem=" + draggedItem.getPhotoBmp().isRecycled());
        /* Drag and drop between 2 object that one of each have not contains image*/
        boolean tempBool = curItem.isContainsContent();
        curItem.containsContent(draggedItem.isContainsContent());
        draggedItem.containsContent(tempBool);

        setCurPhotoIndex(draggedPhotoIndex);
    }

    public RectF getDraggingRect() {
        return mDraggingRect;
    }

    public void setDraggingRect(RectF draggingRect) {
        mDraggingRect = draggingRect;
    }

    public int getMaxPhotoPixelIndex() {
        int index = -1;
        int maxSize = 0;

        for (int i = 0; i < this.size(); i++) {
            if (maxSize < this.get(i).getOrgPixelSize()) {
                index = i;
                maxSize = this.get(i).getOrgPixelSize();
            }
        }

        Flog.i("indexMax=" + index);
        return index;
    }

    public void setFrameBmp(Bitmap bmpFrame) {
        mFrameBitmap = ImageUtils.recycleBitmap(mFrameBitmap);
        mFrameBitmap = bmpFrame;
    }

    public void setBgBitmap(Bitmap bgBitmap) {
        mBgBitmap = ImageUtils.recycleBitmap(mBgBitmap);
        mBgBitmap = bgBitmap;
    }

    public void setBgColor(int mBgColor) {
        this.mBgColor = mBgColor;
    }

    public void setIsDrawGrid(boolean isGrid) {
        mIsDrawGrid = isGrid;
    }

    public ItemPhotoView getItemNoImage() {
        for (int i = 0; i < this.size(); i++) {
            ItemPhotoView itemPhotoView = this.get(i);
            if (!itemPhotoView.isContainsContent())
                return itemPhotoView;
        }
        return null;
    }

    public int[] getNoContentIndex() {
        //  count number of items that no content:
        int len = 0;
        for (int i = 0; i < this.size(); i++) {
            ItemPhotoView itemPhotoView = this.get(i);
            if (!itemPhotoView.isContainsContent())
                len++;
        }
        // init array of integer that save index of items that no content:
        int ans[] = new int[len];
        if (len <= 0) {
            return ans;
        }
        int index = 0;
        for (int i = 0; i < this.size(); i++) {
            ItemPhotoView itemPhotoView = this.get(i);
            if (!itemPhotoView.isContainsContent()) {
                ans[index] = i;
                Flog.d(Flog.CTAG, "no image: " + i);
                index++;
            }
        }
        return ans;
    }

    public boolean changeImage(int index, Bitmap bmp) {
        try {
            if (isIndexInList(index) == false) {
                Flog.i("isIndexInList(index) == false");
                return false;
            }

            ItemPhotoView multiItem = this.get(index);

            multiItem.setPhotoBmp(bmp);
            if (multiItem.getPhotoBmp() != null & multiItem.getPhotoBmp().isRecycled() == false) {
                return true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void setChangedRatio(float changedRatio) {
        mChangedRatio = changedRatio;
    }

    public void scanOpenGallery() {
        if (mOnScanGalleryListenner == null)
            return;
        ItemPhotoView curPhotoItem = this.getCurPhotoItem();
        if (!curPhotoItem.isOpenGallery()) {
            mOnScanGalleryListenner.onOpenGallery(this.getCurPhotoIndex());
        } else {
            mOnScanGalleryListenner.onShowEditor(this.getCurPhotoIndex());
        }
    }

    public ListPhotoViews setOnScanOpenGallery(OnActionDownPhotoItemListenner onScanOpenGallery) {
        mOnScanGalleryListenner = onScanOpenGallery;
        return this;
    }

    public void setPileDrawHelper() {
        if (mBackgroundRect == null)
            return;
        mCanvasPath = new Path();
        mCanvasPath.addRect(new RectF(mBackgroundRect), Path.Direction.CW);
    }

    public boolean isSavePhoto() {
        return mIsSavePhoto;
    }

    public boolean isDrawGrid() {
        return mIsDrawGrid;
    }

    public void setRatioView(float ratioView) {
        mRatioView = ratioView;
    }

    public void setMargin(float spacing) {
        mMargin = spacing;
    }

    public interface OnActionDownPhotoItemListenner {
        public void onOpenGallery(int curPhotoIndex);

        public void onShowEditor(int curPhotoIndex);
    }
}
