package com.lib.collageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.lib.collageview.customviews.listviews.PhotoViewList;
import com.lib.collageview.customviews.views.DragPhotoView;
import com.lib.collageview.customviews.views.PhotoView;
import com.lib.collageview.helpers.ConstValues;
import com.lib.collageview.helpers.FileUtils;
import com.lib.collageview.helpers.Flog;
import com.lib.collageview.helpers.bitmap.BitmapHelper;
import com.lib.collageview.helpers.bitmap.CanvasUtils;
import com.lib.collageview.helpers.svg.SVGItem;
import com.lib.collageview.helpers.svg.SVGParser;
import com.lib.collageview.helpers.svg.SVGPathUtils;
import com.lib.collageview.interfaces.CollageViewListener;
import com.lib.collageview.interfaces.PhotoViewListener;
import com.lib.collageview.interfaces.StickerViewListener;
import com.lib.collageview.stickers.BaseStickerView;
import com.lib.collageview.stickers.icon.IconStickerView;
import com.lib.collageview.stickers.liststickers.StickerViewList;
import com.lib.collageview.stickers.text.TextStickerView;
import com.lib.collageview.tasks.LoadPhotoAsync;
import com.lib.collageview.tasks.SaveAsync;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import bsoft.hoavt.photoproject.lib_textcollage.customviews.WordView;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.CommonVl;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.EGL14Util;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.MathUtil;

/**
 * Custom view that shows a collaged and nice-shape photo.
 * Includes:
 * 1. List of photoviews.
 * 2. The shadow of photoview when dragging it.
 * 3. List of icon stickerviews.
 * 4. List of text stickerviews.
 *
 * @author vuthaihoa.
 * @since 3/20/2017.
 */
public class CollageView extends FrameLayout implements StickerViewListener, PhotoViewListener, LoadPhotoAsync.OnLoadPhotoListener, SaveAsync.OnSavedFinish {

    public static final String TAG = CollageView.class.getSimpleName();
    /**
     * The flag variables check the type of current view that is focused/selected on collageview.
     */
    public static final int FOCUS_ON_ICON_STICKER = 0x00102; //258
    public static final int FOCUS_ON_TEXT_STICKER = 0x00112; //274
    public static final int FOCUS_ON_PHOTO = 0x00122;//290
    public static final int FOCUS_ON_NO_PHOTO = 0x00132;//306
    public static final int FOCUS_ON_NOTHING = -1;
    /**
     * Resolutions for saving collageview.
     */
    public static final int RESOLUTION_EXCELLENT = 2560;
    public static final int RESOLUTION_GOOD = 1920;
    public static final int RESOLUTION_AVERAGE = 1280;
    public static final int RESOLUTION_RECOMMAND = 1024;

    public static final int RESOLUTION_2160p = 2160; // 3840x2160
    public static final int RESOLUTION_1440p = 1440; // 2560x1440
    public static final int RESOLUTION_1080p = 1080; // 1920x1080
    public static final int RESOLUTION_720p = 720; // 1280x720
    public static final int RESOLUTION_480p = 480; // 854x480
    public static final int RESOLUTION_360p = 360; // 640x360
    public static final int RESOLUTION_240p = 240; // 426x240

    /**
     * The number of photos that CollageView contains.
     */
    private int mNumOfPhotos;
    /**
     * Contains information about vectordrawable include:
     * - The number of pathData.
     * - The pathData list.
     * - The viewportWidth.
     * - The viewportHeight.
     */
    private SVGItem mSvgItem;
    /**
     * Custom ArrayList contains PhotoView Models that is easy
     * to manage list.
     */
    private PhotoViewList mPhotoViewList;
    /**
     * Custom ArrayList contains StickerView Models that is easy
     * to manage list.
     */
    private StickerViewList mStickerViewList;
    private Paint mCollageViewPaint;
    private Paint mBackgroundPaint;
    private Paint mFramePaint;
    /**
     * Color background && frame code.
     */
    private int mBackgroundColor = ConstValues.NO_COLOR_VALUE;
    private int mFrameColor = ConstValues.NO_COLOR_VALUE;
    /**
     * RectF Bounds of total collage view
     */
    private Rect mCollageViewRect = new Rect();
    /**
     * The width size of the frame line that is drawed on collageview.
     */
    private int mFrameWidth = -1;
    /**
     * The current ratio value of collageview.
     */
    private float mCollageViewRatio = ConstValues.DEFAULT_RATIO_VALUE;
    /**
     * Catch onLongPress event when to drag and drop a photoview.
     */
    private GestureDetector mGestureDetector;
    /**
     * The flag variable check the dragging state.
     */
    private boolean mIsDragging;
    /**
     * The shadow of photoview when dragging it.
     */
    private DragPhotoView mDragPhotoview;
    /**
     * Collalge view listener to handle which object has picked. Used for callback in activity.
     */
    private CollageViewListener mCollageViewListener = null;
    /**
     * Save old width & old height of collageview.
     * Used to scaleValue stickview when changing ratio layout of collageview.
     * With: [0] is width and [1] is height.
     */
    private int[] mOldDimens = new int[2];
    /**
     * A variable check the type of current view that is focused/selected on collageview.
     */
    private int mFocusedViewtype = FOCUS_ON_NOTHING;
    /**
     * The PhotoViewList for caching the previous photo(s).
     */
    private PhotoViewList mCachedPhotoViewList;
    /**
     * The number of indices of the changed rectangle about photo in collageview.
     * Each changed photo corresponding to a thread.
     */
    private int mCntThreadDone;
    private int mTotalThreads;
    /*
    * The resolution for saving collageview.
    * Default: RESOLUTION_GOOD.
    * */
    private int mSaveResolution = RESOLUTION_GOOD;
    /**
     * The bitmap for drawing frame/background of collageview.
     */
    private Bitmap mFrameBmp, mBgBmp;
    /**
     * The current margin value between photos in collageview.
     */
    private float mBeforeMarginValue = ConstValues.MARGIN_MAX_VALUE;
    /**
     * The current round value of each photo in collageview.
     */
    private float mRoundValue = ConstValues.ROUND_MIN_VALUE;
    private int mTextureMaxSize;
    private CollageView savedCollageView;
    /**
     * The flag used to check type of collageview wheather it is magazine or not.
     */
    private boolean mIsMagazineType;
    private Bitmap mMagazineBmp;
    private ArrayList<WordView> mListWords = new ArrayList<>();
    private int mCurId = -1;
    private int mTypeCollage = -1;
    private Bitmap mBgGalleryBmp, mOriginBgGalleryBmp;
    private int mWidthScreen, mHeightScreen;

    /**
     * Class constructor taking only a context. Use this constructor to create
     * {@link CollageView} objects from your own code.
     *
     * @param context
     */
    public CollageView(Context context) {
        super(context);
        init();
    }

    /**
     * Class constructor taking a context and an attribute set. This constructor
     * is used by the layout engine to construct a {@link CollageView} from a set of
     * XML attributes.
     *
     * @param context
     * @param attrs   An attribute set.
     */
    public CollageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Class constructor taking a context and an attribute set. This constructor
     * is used by the layout engine to construct a {@link CollageView} from a set of
     * XML attributes.
     *
     * @param context
     * @param attrs        An attribute set.
     * @param defStyleAttr An definition style attribute.
     */
    public CollageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Log.d("CALL ONDRAW", "11111aaaa");
        if (mTypeCollage != ConstValues.COLLAGE_TEXT_TYPE) {

            if (mSvgItem == null) return;

            drawBackground(canvas, mBackgroundColor);

            if (mPhotoViewList != null)
                mPhotoViewList.onDraw(canvas);
            if (mIsMagazineType) {
                drawMagazine(canvas);
            }

            if (!mIsMagazineType) {
                drawFrame(canvas, mFrameColor);
            }

            if (mStickerViewList != null) {
                mStickerViewList.onDraw(canvas);
            }

            if (mIsDragging && mStickerViewList.getCurrentIndex() == -1 && mDragPhotoview != null && mCollageViewRect != null) {
                canvas.clipRect(mCollageViewRect, Region.Op.REPLACE);
                mDragPhotoview.onDraw(canvas);
            }


        } else {    // draw text collage

            Flog.d(TAG, "draw text collage");

//            if (false)
            if (mBgGalleryBmp != null) {
//                int remainH = mBgGalleryBmp.getHeight() - this.getHeight();
//                int remainW = mBgGalleryBmp.getWidth() - this.getWidth();
                int remainH = mBgGalleryBmp.getHeight() - mCollageViewRect.height();
                int remainW = mBgGalleryBmp.getWidth() - mCollageViewRect.width();
                Flog.d(TAG, "remain: w=" + remainW + "_h=" + remainH);
                int halfH = (remainH >> 1);
                int halfW = (remainW >> 1);
                int top = mCollageViewRect.top;
                Flog.d(TAG, "half123=" + halfH + "_top=" + top);

                if (true) {
                    Flog.d(TAG, "old rect=" + mCollageViewRect);
                    Rect desRect = new Rect(mCollageViewRect.left - halfW,
                            mCollageViewRect.top - halfH,
                            mCollageViewRect.right + halfW,
                            mCollageViewRect.bottom + halfH);
                    Flog.d(TAG, "new rect=" + desRect);
                    canvas.drawBitmap(mBgGalleryBmp, null, desRect, mCollageViewPaint);
                } else {
                    canvas.drawBitmap(mBgGalleryBmp, 0, top - halfH, mCollageViewPaint);
                }
            } else {
                drawBackground(canvas, mBackgroundColor);
            }

            drawListWords(canvas);

//            if (false)
            drawFrame(canvas, mFrameColor);
        }
    }

    private void drawMagazine(Canvas canvas) {
        canvas.clipRect(mCollageViewRect, Region.Op.REPLACE);
        canvas.drawBitmap(mMagazineBmp, null, mCollageViewRect, mCollageViewPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event == null) return false;
        boolean result = false;

        Flog.d(TAG, "onTouch mTypeCollage=" + mTypeCollage);
        if (mTypeCollage != ConstValues.COLLAGE_TEXT_TYPE) {

            if (mFocusedViewtype != FOCUS_ON_NOTHING) {
                unselectedAllViews();
            }

            if (!mIsDragging)   // when dragging, not handle onTouchEvent of sticker.
                result = mStickerViewList.onTouchEvent(event);

            Flog.d(TAG, "onFocusedView result=" + result);
            if (result && mStickerViewList.getCurrentIndex() != -1) {
                /**
                 * Callbacked when the clicked stickerview.
                 * -> Open editor for stickerview.
                 * */
                int tmp = mStickerViewList.get(mStickerViewList.getCurrentIndex()).getStickerType();

                if (tmp == BaseStickerView.STICKER_ICON)
                    mFocusedViewtype = FOCUS_ON_ICON_STICKER;
                else if (tmp == BaseStickerView.STICKER_TEXT)
                    mFocusedViewtype = FOCUS_ON_TEXT_STICKER;

                Flog.d(TAG, "onFocusedView 111111");
                Flog.d(TAG, "onFocusedView idnx=" + tmp + "_aaa=" + mFocusedViewtype);
                callbackOnTouch(event);
                return true;
            } else {
                if ((mFocusedViewtype == FOCUS_ON_ICON_STICKER) || (mFocusedViewtype == FOCUS_ON_TEXT_STICKER)) {
                    int focusOnType = mStickerViewList.focusOtherSticker(event);
                    if (focusOnType != -1) {
                        if (focusOnType == BaseStickerView.STICKER_TEXT) {
                            mFocusedViewtype = FOCUS_ON_TEXT_STICKER;
                        } else {
                            mFocusedViewtype = FOCUS_ON_ICON_STICKER;
                        }
                        /**
                         * Case: A stickview is being selecte/focuse by user, then click to switch to other stickerview.
                         * */
                        Flog.d(TAG, "switch sticker");
                        Flog.d(TAG, "11  idnx" + "bbb=" + mFocusedViewtype);
                        Flog.d(TAG, "onFocusedView 22222");
                        callbackOnTouch(event);
                        return true;
                    }

                    Flog.d(TAG, "outside sticker");
                    /**
                     * Case: A stickview is being selecte/focuse by user, then click to OUTSIDE of it.
                     * */

                    unselectedAllViews();

                    /**
                     * Callbacked when the all views is unselected.
                     * -> Unfocus and do nothing.
                     * */
                    mFocusedViewtype = FOCUS_ON_NOTHING;
                    Flog.d(TAG, "onFocusedView 3333333333");
                    callbackOnTouch(event);
                    return true;
                }
            }

            // detect press-holding photoview:
            if (mGestureDetector != null)
                mGestureDetector.onTouchEvent(event);

            Flog.d(TAG, "mIsDragging=" + mIsDragging);
            if (mIsDragging && mDragPhotoview != null) {
                result = mDragPhotoview.onTouchEvent(event);
            } else if (mPhotoViewList != null) {
                result = mPhotoViewList.onTouchEvent(event);
                if (result) {
                    /**
                     * Callbacked when the clicked photo contains image.
                     * -> Open editor for the photoview.
                     * */
                    mFocusedViewtype = FOCUS_ON_PHOTO;
                    Bitmap tmpBmp = mPhotoViewList.get(mPhotoViewList.getCurrentIndex()).getBitmap();
                    if (tmpBmp == null) {
                        // tell application layer that there is no photo,
                        // should pick one
                        /**
                         * Callbacked when the clicked photo does NOT contains image.
                         * -> Open gallery.
                         * */
                        mFocusedViewtype = FOCUS_ON_NO_PHOTO;
                    }
                }
            }

            if (!result) {
                // nothing to handle onTouch
                /**
                 * Callbacked when the all views is unselected.
                 * -> Unfocus and do nothing.
                 * */
                mFocusedViewtype = FOCUS_ON_NOTHING;
            }

            Flog.d(TAG, "onFocusedView 4444444444");
            callbackOnTouch(event);

        } else {


            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                takeCurId(event);
                Flog.d(TAG, "curId 1=" + mCurId);
                if (mCurId == -1)
                    return result;

                bringToTop();
                Flog.d(TAG, "curId 2=" + mCurId);
            }

            WordView curWordview = mListWords.get(mCurId);
            if (curWordview != null) {
                result = curWordview.onTouchEvent(event);
            } else {
                Flog.d(TAG, "curWordview is null");
            }

            Flog.d(TAG, "result final=" + result + "_curId=" + mCurId);
            if (!result) {
                mCurId = -1;
            }
            // set selected state of wordview:
            setSelectedViews();

            if (event.getAction() != MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_CANCEL) {
                invalidate();
            }
        }

        Flog.d(TAG, "result=" + result);
        return result;
    }

    private void bringToTop() {
        if (mCurId < 0 || mCurId >= mListWords.size()) return;
        // swap index id of StickerView/
        mListWords.get(mCurId).setId(mListWords.size() - 1);
        mListWords.get(mListWords.size() - 1).setId(mCurId);
        // swap two elements in list.
        Collections.swap(mListWords, mListWords.size() - 1, mCurId);
        mCurId = mListWords.size() - 1;
    }

    private void takeCurId(MotionEvent event) {
        Flog.d(TAG, "takeCurId: a=" + (event == null) + "_b=" + (mListWords == null) + "_c=" + mListWords.isEmpty());
        if ((event == null) || (mListWords == null) || mListWords.isEmpty()) {
            return;
        }

        for (int i = mListWords.size() - 1; i >= 0; i--) {
            WordView wordView = mListWords.get(i);
//            Flog.d(TAG, i+"=="+wordView.isInEdit(event));
//            if (wordView.isInEdit(event)) {
            if (wordView.isInEdit(event)) {
                Flog.d(TAG, "curWordview: selected=" + wordView.isSelected() + "_curId=" + i);
                mCurId = i;
                break;
            }

//            if (wordView.isInBitmap(event)) {
//                Flog.d(TAG, "curWordview: selected="+wordView.isSelected()+"_curId="+i);
//                mCurId = i;
//                break;
//            }
        }
    }

    /**
     * Initialize the instance variables for:
     * - Paint
     * - PhotoViewList.
     * <p>
     * Enable hardware acceleration for API < 18.
     */
    private void init() {

        mTextureMaxSize = EGL14Util.getMaxTextureSize();
        Flog.d(TAG, "init mTextureMaxSize=" + mTextureMaxSize);

        mCollageViewPaint = new Paint();
        mCollageViewPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.WHITE);
        mFramePaint = new Paint();
        mPhotoViewList = new PhotoViewList(this);
        mCachedPhotoViewList = new PhotoViewList(this);
        mStickerViewList = new StickerViewList(this);

        if (mBackgroundColor == ConstValues.NO_COLOR_VALUE)
            mBackgroundColor = Color.WHITE;
        if (mFrameColor == ConstValues.NO_COLOR_VALUE)
            mFrameColor = Color.TRANSPARENT;
        if (mFrameWidth == -1)
            mFrameWidth = (int) getResources().getDimension(R.dimen.frame_width_size);

        /**
         * Used for drag and drop PhotoView to swap between photoviews.
         * */
        initGesture();

        /**
         * Canvas.clipPath() support with hardware acceleration has been reintroduced since API 18.
         * The best way to work around the problem is calling setLayerType(View.LAYER_TYPE_SOFTWARE, null)
         * only when you are running on API from 11 to 17:
         * */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Flog.d(TAG, "hardware acceleration");
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    /**
     * Init the style for layout. This function parse xml of vectordrawable
     * to get the information of vectordrawable. Using SVGItem models to contains
     * this info.
     *
     * @param resID The id resource of vectordrawable xml file.
     */
    public void setLayoutStyle(int resID) throws IOException, XmlPullParserException {
        mSvgItem = SVGPathUtils.getSVGItem(getResources(), resID);
    }

    public void setLayoutStyle(SVGItem svgItem) {
//        mTypeCollage = mIsMagazineType ? ConstValues.TYPE_COLLAGE_MAGAZINE : ConstValues.TYPE_COLLAGE_PHOTO;
        mSvgItem = svgItem;
    }

    public void setTypeCollage(int typeCollage) {
        mTypeCollage = typeCollage;
    }

    /**
     * Initialize the photo items in collageview.
     * Scale vectordrawable to fit collageview.
     * Set bounds rect of collageview for clipRectF onDraw().
     *
     * @param widthView  The width of collageview used to fitting vectordrawable.
     * @param heightView The height of collageview used to fitting vectordrawable.
     */
    private void initItems(int widthView, int heightView) {

        mNumOfPhotos = mSvgItem.numImgs;
        Matrix doubleMatrix = new Matrix();
        doubleMatrix.setScale(widthView / mSvgItem.viewportWidth, heightView / mSvgItem.viewportHeight);

        setCollageViewRect(0, 0, widthView, heightView);
        if (mPhotoViewList == null)
            mPhotoViewList = new PhotoViewList(this);
        mPhotoViewList.clear();

        float maxWidth = 0F;
        for (int i = 0; i < mNumOfPhotos; i++) {
            PhotoView photoView = new PhotoView(this, i).setPhotoViewListener(this);
            Path path = new Path(SVGParser.parsePath(mSvgItem.pathData.get(i)));
            // Save the inital path for changing ratio of collageview
            photoView.setOriginPath(path);
            Path temp = new Path(path);
            temp.transform(doubleMatrix);
            photoView.setPath(temp);
            mPhotoViewList.add(photoView);
            if (maxWidth < photoView.getRectF().width()) {
                maxWidth = photoView.getRectF().width();
            }
        }
        Flog.d(TAG, "maxWidth=" + maxWidth);
        mPhotoViewList.setMaxWidth(maxWidth);

        setOldDimens();
    }

    /**
     * Update layout when changing ratio of collageview. Scale path to fit to the current onMesure()
     * of collageview.
     *
     * @param ratio The ratio value of collageview
     */
    public void setChangedRatioLayout(float ratio) {

        if (Float.compare(mCollageViewRatio, ratio) == 0) {
            Flog.d(TAG, "setChangedRatioLayout method is not fulfilled");
            return;
        }

        setCollageViewRatio(ratio);

        int widthView, heightView;
        if (mCollageViewRatio >= 1) {
            widthView = Math.max(getWidth(), getHeight());
            heightView = (int) (widthView / mCollageViewRatio);
        } else {
            heightView = Math.max(getWidth(), getHeight());
            widthView = (int) (heightView * mCollageViewRatio);
        }
        Flog.d(TAG, "collageview: w=" + widthView + "_h=" + heightView);

        Matrix doubleMatrix = new Matrix();
        if (mSvgItem != null)
            doubleMatrix.setScale(widthView / mSvgItem.viewportWidth, heightView / mSvgItem.viewportHeight);
        setCollageViewRect(0, 0, widthView, heightView);

        Flog.d(TAG, "mNumOfPhotos2=" + mNumOfPhotos);
        for (int i = 0; i < mNumOfPhotos; i++) {
            PhotoView photoView = mPhotoViewList.get(i);
            Path path = new Path(photoView.getOriginPath());
            path.transform(doubleMatrix);
            photoView.setPath(SVGPathUtils.zoomPath(path, mBeforeMarginValue));
            photoView.fitPhotoToLayout();
        }

        /**
         * Invoke onMeasure() method first. After that, call onDraw() method
         * */
        requestLayout();
    }

    /**
     * Change "ratio" and "layout" of collageview.
     * Scale path to fit to the current onMesure() of collageview.
     *
     * @param svgItem The changed id resource of vectordrawable xml file.
     * @param ratio   The changed ratio value of collageview
     */
    public void setChangedRatioLayout(SVGItem svgItem, float ratio) throws IOException, XmlPullParserException {

        if (Float.compare(mCollageViewRatio, ratio) == 0) {
            Flog.d(TAG, "setChangedRatioLayout method is not fulfilled");
            return;
        }

        /**
         * Invoke onMeasure() method first. After that, call onDraw() method
         * */
        requestLayout();

        // Change "layout"
        mSvgItem = svgItem;
        // Change "ratio"
        setCollageViewRatio(ratio);

//        int widthView = getWidth();
//        int heightView = (int) (widthView / mCollageViewRatio);
        int widthView, heightView;
        if (mCollageViewRatio >= 1) {
            widthView = Math.max(getWidth(), getHeight());
            heightView = (int) (widthView / mCollageViewRatio);
        } else {
            heightView = Math.max(getWidth(), getHeight());
            widthView = (int) (heightView * mCollageViewRatio);
        }
        /**
         * Update the new layout based on the new ratio of collageview:
         */
        mNumOfPhotos = mSvgItem.numImgs;
        Matrix doubleMatrix = new Matrix();
        doubleMatrix.setScale(widthView / mSvgItem.viewportWidth, heightView / mSvgItem.viewportHeight);
        setCollageViewRect(0, 0, widthView, heightView);

        for (int i = 0; i < mNumOfPhotos; i++) {
            PhotoView photoView = mPhotoViewList.get(i);
            Path path = new Path(SVGParser.parsePath(mSvgItem.pathData.get(i)));
            // Save the inital path for changing ratio of collageview
            photoView.setOriginPath(path);
            Path temp = new Path(path);
            temp.transform(doubleMatrix);
            photoView.setPath(SVGPathUtils.zoomPath(temp, mBeforeMarginValue));
            photoView.fitPhotoToLayout();
        }

        /**
         * Invoke onMeasure() method first. After that, call onDraw() method
         * */
        requestLayout();
    }

    /**
     * Change "layout" of collageview.
     * Scale path to fit to the current onMesure() of collageview.
     *
     * @param svgItem   The item SVG model.
     * @param widthView The size of width of collageview.
     */
    public void setChangedLayout(SVGItem svgItem, int widthView) {

        // Change "layout"
        mSvgItem = svgItem;
//        Flog.d("setChangedLayout= ratio" + mCollageViewRatio);
        int heightView = (int) (widthView / mCollageViewRatio);
        /**
         * Update the new layout based on the new ratio of collageview:
         */
        mNumOfPhotos = mSvgItem.numImgs;
        Matrix doubleMatrix = new Matrix();
        doubleMatrix.setScale(widthView / mSvgItem.viewportWidth, heightView / mSvgItem.viewportHeight);
        setCollageViewRect(0, 0, widthView, heightView);

        for (int i = 0; i < mPhotoViewList.size(); i++) {
            PhotoView photoView = mPhotoViewList.get(i);
            Path path = new Path(SVGParser.parsePath(mSvgItem.pathData.get(i)));
            // Save the inital path for changing ratio of collageview
            photoView.setOriginPath(path);
            Path temp = new Path(path);
            temp.transform(doubleMatrix);
            photoView.setPath(temp);
//            photoView.fitPhotoToLayout();
        }
        if (mBeforeMarginValue < 1F) {
            mPhotoViewList.setItemMargin(mBeforeMarginValue);
        }

        /**
         * Invoke onMeasure() method first. After that, call onDraw() method
         * */
        requestLayout();
        invalidate();
    }

    /**
     * Change "layout" of collageview.
     * Scale path to fit to the current onMesure() of collageview.
     *
     * @param svgItem The item SVG model.
     */
    public void setChangedLayout(SVGItem svgItem) {

        // Change "layout"
        mSvgItem = svgItem;
//        Flog.d("setChangedLayout= ratio" + mCollageViewRatio);
//        int widthView = this.getWidth();
//        int heightView = (int) (widthView / mCollageViewRatio);
        int widthView, heightView;
        if (mCollageViewRatio >= 1) {
            widthView = Math.max(getWidth(), getHeight());
            heightView = (int) (widthView / mCollageViewRatio);
        } else {
            heightView = Math.max(getWidth(), getHeight());
            widthView = (int) (heightView * mCollageViewRatio);
        }
        /**
         * Update the new layout based on the new ratio of collageview:
         */
        mNumOfPhotos = mSvgItem.numImgs;
        Matrix doubleMatrix = new Matrix();
        doubleMatrix.setScale(widthView / mSvgItem.viewportWidth, heightView / mSvgItem.viewportHeight);
        setCollageViewRect(0, 0, widthView, heightView);

        updatePhotoViewList();
//        Flog.d(TAG, "sppp mNumOfPhotos=" + mNumOfPhotos + "_size=" + mPhotoViewList.size());
//        for (int i = 0; i < mPhotoViewList.size(); i++) {
//            Flog.d(TAG, "sppp " + i + "=" + mPhotoViewList.get(i).getBitmap());
//        }
//        if (true) return;

        for (int i = 0; i < mPhotoViewList.size(); i++) {
            PhotoView photoView = mPhotoViewList.get(i);
            Path path = new Path(SVGParser.parsePath(mSvgItem.pathData.get(i)));
            // Save the inital path for changing ratio of collageview
            photoView.setOriginPath(path);
            Path temp = new Path(path);
            temp.transform(doubleMatrix);
            photoView.setPath(temp);
            photoView.setRoundValue(mRoundValue);
            photoView.fitPhotoToLayout();
        }
        Flog.d(TAG, "mZoomPathValue size of margin value = " + mBeforeMarginValue);
        if (mBeforeMarginValue < 1F) {
            Flog.d(TAG, "marginnnnnnnnnnnnnnn");
            mPhotoViewList.setItemMargin(mBeforeMarginValue);
        }

        /**
         * Invoke onMeasure() method first. After that, call onDraw() method
         * */
        requestLayout();
        invalidate();
    }

    /**
     * Update layout of collageview with the different number of photoviews.
     * WARNING: uncondition to test this method.
     */
    private void updatePhotoViewList() {

        int numOfPhotos = mPhotoViewList.size();
        int numOfRects = mNumOfPhotos;
        if (numOfPhotos > numOfRects) {
            int diff = numOfPhotos - numOfRects;
            int[] noContentIdx = mPhotoViewList.getNoContentIndex();
            if (diff >= noContentIdx.length) {
                // del from reverse array avoiding confuse:
                for (int i = noContentIdx.length - 1; i >= 0; i--) {
                    mPhotoViewList.remove(noContentIdx[i]);
                }
                int odd = diff - noContentIdx.length;
                for (int i = 0; i < odd; i++) {
                    int lastIdx = mPhotoViewList.size() - 1;
                    mCachedPhotoViewList.add(mPhotoViewList.get(lastIdx));
                    mPhotoViewList.remove(lastIdx);
                }
            } else {
                // del from reverse array avoiding confuse:
                for (int i = noContentIdx.length - 1; i > (noContentIdx.length - 1 - diff); i--) {
                    mPhotoViewList.remove(noContentIdx[i]);
                }
            }
        } else if (numOfPhotos < numOfRects) {
            int diff = numOfRects - numOfPhotos;
            if (diff >= mCachedPhotoViewList.size()) {
                for (int i = mCachedPhotoViewList.size() - 1; i >= 0; i--) {
                    int priority = numOfPhotos + ((mCachedPhotoViewList.size() - 1) - i);
                    PhotoView cachedItem = mCachedPhotoViewList.get(i);
                    PhotoView photoModel = new PhotoView(this, priority).setPhotoViewListener(this);
                    photoModel.setPath(cachedItem.getPath());
                    photoModel.setBitmap(cachedItem.getBitmap());
//                    photoModel.setIndex(priority);
//                    photoModel.setRoundSize(mRoundSize);
                    mPhotoViewList.add(photoModel);
                }

                for (int i = (numOfPhotos + mCachedPhotoViewList.size()); i < numOfRects; i++) {
                    PhotoView photoModel = new PhotoView(this, i).setPhotoViewListener(this);
                    photoModel.setPath(SVGParser.parsePath(mSvgItem.pathData.get(i)));
                    photoModel.setIndex(i);
//                    photoModel.setRoundSize(mRoundSize);
                    mPhotoViewList.add(photoModel);
                }
            } else {
                for (int i = mCachedPhotoViewList.size() - 1; i >= mCachedPhotoViewList.size() - diff; i--) {
                    int priority = numOfPhotos + ((mCachedPhotoViewList.size() - 1) - i);
                    PhotoView cachedItem = mCachedPhotoViewList.get(i);
                    PhotoView photoModel = new PhotoView(this, priority).setPhotoViewListener(this);
                    photoModel.setPath(cachedItem.getPath());
                    photoModel.setBitmap(cachedItem.getBitmap());
//                    photoModel.setIndex(priority);
//                    photoModel.setRoundSize(mRoundSize);
                    mPhotoViewList.add(photoModel);
                }
            }
            mCachedPhotoViewList.clear();
        }

        // Update of item-photo
//        for (int i = 0; i < model.imgCount; i++) {
//            PhotoView photoModel = mPhotoViewList.get(i);
//            photoModel.setRegionPath(clipLayoutPaths.get(i));
//            photoModel.fitPhotoToLayout();
//            photoModel.setPriority(i);
//        }
    }

    /**
     * Display collageview on screen.
     */
    public void show() {
        /**
         * Call onDraw() after initialized the photo items.
         * */
        post(new Runnable() {
            @Override
            public void run() {

                if (mTypeCollage != ConstValues.COLLAGE_TEXT_TYPE) {
                    initItems(CollageView.this.getWidth(), CollageView.this.getHeight());
                } else {
                    setCollageViewRect(0, 0, CollageView.this.getWidth(), CollageView.this.getHeight());
                    setOldDimens();
                }

                if (mCollageViewListener != null) mCollageViewListener.showedOnScreen();
                /**
                 * Allow to call onDraw() method from ViewGroup Custom View.
                 */
                setWillNotDraw(false);
            }
        });
    }

    /**
     * Override the onMeasure() method to measure the view more precisely.
     * Change width-height ratio of collageview.
     *
     * @param widthMeasureSpec  Horizontal space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec.
     * @param heightMeasureSpec Vertical space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w, h;
        if (mCollageViewRatio >= 1) {
            w = resolveSizeAndState(minw, widthMeasureSpec, 1);
            h = (int) (w / mCollageViewRatio);
        } else {
            h = resolveSizeAndState(minw, widthMeasureSpec, 1);
            w = (int) (h * mCollageViewRatio);
        }
        Flog.d(TAG, "resolveSizeAndState w=" + w + "_h=" + h);
        setMeasuredDimension(w, h);
    }

    /**
     * Draw background for collageview
     *
     * @param canvas The general canvas of collageview used to drawing.
     * @param color  The color code of background. Equal to -1 if background is pattern,
     *               otherwise background is color.
     */
    private void drawBackground(Canvas canvas, int color) {
        Flog.d(TAG, "-------color=" + color);
        if (color == ConstValues.NO_COLOR_VALUE) {
            Flog.d(TAG, "mBackgroundPaint=" + mBackgroundPaint);
            canvas.drawPaint(mBackgroundPaint);
        } else canvas.drawColor(color);
    }

    /**
     * Create shader for paint of background/frame to draw pattern image repeatedly, avoid using the large image.
     *
     * @param paint  The paint of background/frame that is used for drawing pattern background.
     * @param bitmap The bitmap of pattern image with small size.
     */
    private void setShaderPaint(Paint paint, Bitmap bitmap) {

        Shader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paint.setShader(shader);
    }

    /**
     * Set pattern background.
     *
     * @param bitmap The bitmap of pattern image with small size.
     */
    public void setBackgroundPattern(Bitmap bitmap) {
        mBgGalleryBmp = BitmapHelper.recycle(mBgGalleryBmp);
        mOriginBgGalleryBmp = BitmapHelper.recycle(mOriginBgGalleryBmp);
        mBgBmp = BitmapHelper.recycle(mBgBmp);
        mBgBmp = bitmap;
        setShaderPaint(mBackgroundPaint, bitmap);
        mBackgroundColor = ConstValues.NO_COLOR_VALUE;
    }

    /**
     * Draw frame border for collageview
     *
     * @param canvas The general canvas of collageview used to drawing.
     * @param color  The color code of frame. Equal to -1 if frame is pattern,
     *               otherwise frame is color.
     */
    private void drawFrame(Canvas canvas, @ColorInt int color) {

        Flog.d(TAG, "drawFrame color=" + color);
        if (mCollageViewRect == null || color == Color.TRANSPARENT) return;

        canvas.clipRect(mCollageViewRect, Region.Op.REPLACE);
        drawFrameLine(canvas, 0, 0, mFrameWidth, mCollageViewRect.height(), color);
        drawFrameLine(canvas, 0, 0, mCollageViewRect.width(), mFrameWidth, color);
        drawFrameLine(canvas, mCollageViewRect.width() - mFrameWidth, 0, mCollageViewRect.width(), mCollageViewRect.height(), color);
        drawFrameLine(canvas, 0, mCollageViewRect.height() - mFrameWidth, mCollageViewRect.width(), mCollageViewRect.height(), color);
    }

    /**
     * Draw line: horizontal or vertical.
     * Implementation:
     * - clip region that need to draw
     * - draw color/pattern
     * - clip whole collageview region.
     *
     * @param canvas The general canvas of collageview used to drawing.
     * @param x      The left coordinate
     * @param y      The top coordinate
     * @param w      The right coordinate
     * @param h      The bottom coordinate
     * @param color  The color code of frame. Equal to -1 if frame is pattern,
     *               otherwise frame is color.
     */
    private void drawFrameLine(Canvas canvas, int x, int y, int w, int h, int color) {

        CanvasUtils.clipRect(canvas, x, y, w, h);
        if (color == ConstValues.NO_COLOR_VALUE) canvas.drawPaint(mFramePaint);
        else canvas.drawColor(color);
        canvas.clipRect(mCollageViewRect, Region.Op.REPLACE);
    }

    /**
     * Set color frame.
     *
     * @param color The color code of frame. Equal to -1 if background is pattern,
     *              otherwise frame is color.
     */
    public void setFrameColor(int color) {
        mFrameColor = color;
        mFramePaint.reset();
    }

    /**
     * Set pattern frame.
     *
     * @param bitmap The bitmap of pattern image with small size.
     */
    public void setFramePattern(Bitmap bitmap) {
        mFrameBmp = BitmapHelper.recycle(mFrameBmp);
        mFrameBmp = bitmap;
        setShaderPaint(mFramePaint, bitmap);
        mFrameColor = ConstValues.NO_COLOR_VALUE;
    }

    /**
     * Set margin between photos in collageview.
     *
     * @param progressValue The current value of progress bar.
     *                      The margin value in [0,5..1]. With "1": the initial margin value
     *                      and "0,5": min value of margin size.
     * @param minValue      The minimum value of progress bar.
     * @param maxValue      The maximum value of progress bar.
     */
    public void setPhotoMargin(float progressValue, int minValue, int maxValue) {
        Flog.d(TAG, "setPhotoMargin progressValue=" + progressValue + "_minValue=" + minValue + "_maxValue=" + maxValue);
        if (minValue >= maxValue)
            return;
        if (minValue <= 0)
            minValue = 1;
        if (maxValue > Integer.MAX_VALUE)
            maxValue = Integer.MAX_VALUE;
        if (progressValue <= 0)
            progressValue = 1;
        else if (progressValue > Integer.MAX_VALUE)
            progressValue = Integer.MAX_VALUE;
        if (progressValue < minValue * 1F || progressValue > maxValue * 1F)
            return;
        float curMarginValue = (progressValue - minValue)
                * (ConstValues.MARGIN_MAX_VALUE - ConstValues.MARGIN_MIN_VALUE) / (maxValue - minValue)
                + ConstValues.MARGIN_MIN_VALUE;
        Flog.d(TAG, "setPhotoMargin curMarginValue=" + curMarginValue);
//        if (curMarginValue < ConstValues.MARGIN_MIN_VALUE || curMarginValue > ConstValues.MARGIN_MAX_VALUE)
//            return;
        float zoomPathValue = curMarginValue / mBeforeMarginValue;
        Flog.d(TAG, "setPhotoMargin zoomPathValue=" + zoomPathValue);
        mPhotoViewList.setItemMargin(zoomPathValue);
        mBeforeMarginValue = curMarginValue;
        invalidate();
    }

    /**
     * Set round for each photo in collageview.
     *
     * @param progressValue The current value of progress bar.
     *                      The round value in [0..30]. With "0": the initial margin value
     * @param minValue      The minimum value of progress bar.
     * @param maxValue      The maximum value of progress bar.
     */
    public void setPhotoRound(float progressValue, int minValue, int maxValue) {
        if (getWidth() <= 0) return;
        if (minValue >= maxValue)
            return;
        if (minValue <= 0)
            minValue = 1;
        if (maxValue > Integer.MAX_VALUE)
            maxValue = Integer.MAX_VALUE;
        if (progressValue <= 0)
            progressValue = 1;
        else if (progressValue > Integer.MAX_VALUE)
            progressValue = Integer.MAX_VALUE;
        if (progressValue < minValue * 1F || progressValue > maxValue * 1F)
            return;
        float curMarginValue = (progressValue - minValue)
                * (ConstValues.ROUND_MAX_VALUE - ConstValues.ROUND_MIN_VALUE) / (maxValue - minValue)
                + ConstValues.ROUND_MIN_VALUE;

//        if (curMarginValue < ConstValues.ROUND_MIN_VALUE || curMarginValue > ConstValues.ROUND_MAX_VALUE)
//            return;
//        final float ROUND_VALUE_PER_STEP = 0.015f;
        final float ROUND_VALUE_PER_STEP = (progressValue / maxValue) / maxValue;
        mRoundValue = ROUND_VALUE_PER_STEP * curMarginValue * getWidth();
        Flog.d(TAG, "setPhotoRound mRoundValue=" + mRoundValue + "_progress=" + curMarginValue + "_ROUND_VALUE_PER_STEP=" + ROUND_VALUE_PER_STEP);
        mPhotoViewList.setItemRound(mRoundValue);
        invalidate();
    }

    /**
     * Set bitmap image for each photo in collageview at specific position.
     *
     * @param index  The position that set bitmap changed.
     * @param bitmap The bitmap of picked image from gallery.
     */
    public void setPhotoBitmapAt(int index, Bitmap bitmap) {
        mPhotoViewList.get(index).setBitmap(bitmap);
        mPhotoViewList.get(index).fitPhotoToLayout();
    }

    /**
     * Initialize on long press event of photoview on collageview.
     */
    private void initGesture() {
        GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent event) {
                super.onLongPress(event);
                Flog.d(TAG, "onLongPress onLongPress --");
                if (mPhotoViewList == null)
                    return;
                int touchedIdx = mPhotoViewList.getTouchedIndex(event);
                Flog.d(TAG, "touchedIdx=" + touchedIdx);
                if (touchedIdx == -1) return;
                /**
                 * Save index of photoview that is dragged.
                 * */
                mPhotoViewList.setSrcDraggedIndex(touchedIdx);

                PhotoView curPhotoView = mPhotoViewList.get(touchedIdx);
                if (curPhotoView == null)
                    return;
                Bitmap bitmap = curPhotoView.getBitmap();
                if (bitmap == null) // no image
                    return;

                /**
                 * This photoview is able to drag and drop:
                 * */
                mIsDragging = true;

                // Initialize DragPhotoView() to display on collageview.
                if (mDragPhotoview == null)
                    mDragPhotoview = new DragPhotoView(CollageView.this).setPhotoSwapListener(new DragPhotoView.OnPhotoSwapListener() {
                        @Override
                        public void onSwapDone(MotionEvent event) {
                            mIsDragging = false;
                            Flog.d(TAG, "onSwapDone desIdx = " + mPhotoViewList.getTouchedIndex(event));
                            Flog.d(TAG, "           srcIdx = " + mPhotoViewList.getSrcDraggedIndex());
                            int srcIdx = mPhotoViewList.getSrcDraggedIndex();
                            int dstIdx = mPhotoViewList.getTouchedIndex(event);
                            if (srcIdx == -1 || dstIdx == -1 || srcIdx == dstIdx) {

                            } else {
                                mPhotoViewList.swap(srcIdx, dstIdx);
                            }

                            if (mCollageViewListener != null)
                                mCollageViewListener.onSwapDone(srcIdx, dstIdx);
                            /**
                             * Disappear DragPhotoView. Then swap two photoviews if satisfying.
                             * */
                            CollageView.this.invalidate();
                        }
                    });
                mDragPhotoview.setRectF(curPhotoView.getRectF());
                mDragPhotoview.setBitmap(bitmap, event.getX(0), event.getY(0));
                Flog.d(TAG, "start draw shadow");
                invalidate();
            }
        };

        mGestureDetector = new GestureDetector(getContext(), gestureListener);
    }

    /**
     * Rotate photoview by a angle.
     *
     * @param degree the value of angle that photoview rotate.
     */
    public void postRotate(float degree) {
        if ((mPhotoViewList == null) || (mPhotoViewList.getCurrentIndex() == -1)
                || (mPhotoViewList.get(mPhotoViewList.getCurrentIndex()) == null)) return;
        mPhotoViewList.get(mPhotoViewList.getCurrentIndex()).postRotate(degree);
    }

    /**
     * Translate photoview to new position on collageview.
     *
     * @param x the translated value based-on horizontal axis.
     * @param y the translated value based-on vertical axis.
     */
    public void postTranslate(float x, float y) {
        Flog.d(TAG, "mPhotoViewList has curIdx1=" + mPhotoViewList.getCurrentIndex()
                + "_sizeAll=" + mPhotoViewList.size()
                + "mPhotoViewList has curIdx2=" + mPhotoViewList.getCurrentIndex());
//        + "_value=" + mPhotoViewList.get(mPhotoViewList.getCurrentIndex())
        if ((mPhotoViewList == null) || (mPhotoViewList.getCurrentIndex() == -1)
                || (mPhotoViewList.get(mPhotoViewList.getCurrentIndex()) == null)) return;
        mPhotoViewList.get(mPhotoViewList.getCurrentIndex()).postTranslate(x, y);
    }

    /**
     * Zoom in/out photoview.
     *
     * @param ratio the value of zoomed ratio.
     */
    public void postScale(float ratio) {
        if ((mPhotoViewList == null) || (mPhotoViewList.getCurrentIndex() == -1)
                || (mPhotoViewList.get(mPhotoViewList.getCurrentIndex()) == null)) return;
        mPhotoViewList.get(mPhotoViewList.getCurrentIndex()).postScale(ratio);
    }

    public void rotate90() {
        postRotate(90);
    }

    public void mirror90() {
        postRotate(-90);
    }

    public void zoomIn() {
        postScale(ConstValues.ZOOM_IN_RATIO);
    }

    public void zoomOut() {
        postScale(ConstValues.ZOOM_OUT_RATIO);
    }

    /**
     * Correct photoview to removing skew angle. Make it horizontally/vertically.
     */
    public void correctSkew() {
        if ((mPhotoViewList == null) || (mPhotoViewList.getCurrentIndex() == -1)
                || (mPhotoViewList.get(mPhotoViewList.getCurrentIndex()) == null)) return;
        mPhotoViewList.get(mPhotoViewList.getCurrentIndex()).correctSkew();
    }

    public void moveUp() {
        postTranslate(0, -ConstValues.TRANSLATING_THRESHOLD);
    }

    public void moveDown() {
        postTranslate(0, ConstValues.TRANSLATING_THRESHOLD);
    }

    public void moveLeft() {
        postTranslate(-ConstValues.TRANSLATING_THRESHOLD, 0);
    }

    public void moveRight() {
        postTranslate(ConstValues.TRANSLATING_THRESHOLD, 0);
    }

    /**
     * Callback when action down on collageview.
     * Used to check the type of current view that is focused/selected on collageview.
     *
     * @param event motion event of collageview.
     */
    private void callbackOnTouch(MotionEvent event) {
        Flog.d(TAG, "onFocusedView callbackOnTouch 111111");
        Flog.d(TAG, "onFocusedView callbackOnTouch getAction: " + (event.getAction() == MotionEvent.ACTION_DOWN));
        Flog.d(TAG, "onFocusedView callbackOnTouch mCollageViewListener: " + mCollageViewListener);

        if (event.getAction() == MotionEvent.ACTION_UP && mCollageViewListener != null) {
            Flog.d(TAG, "onFocusedView callbackOnTouch 2222222");
            mCollageViewListener.onFocusedView(mFocusedViewtype);
        }
    }

    /**
     * Disable selected/focused state of the current view on collageview.
     * The actually state has only A focused/selected view.
     *
     * @return if unselected successfully then true. Otherwise return to false.
     */
    public boolean unselectedAllViews() {
        if (mTypeCollage == ConstValues.COLLAGE_TEXT_TYPE) {
            mCurId = -1;
            setSelectedViews();
        }

        if (mPhotoViewList == null || mPhotoViewList.isEmpty()) return false;
        for (int i = 0; i < mPhotoViewList.size(); i++) {
            mPhotoViewList.get(i).setIsSelected(false);
        }

        if (mStickerViewList == null || mStickerViewList.isEmpty()) return false;
        for (int i = 0; i < mStickerViewList.size(); i++) {
            mStickerViewList.get(i).setInEdit(false);
        }

        if (true) return true;
        if (mPhotoViewList != null && !mPhotoViewList.isEmpty() && mPhotoViewList.getCurrentIndex() != -1
                && mPhotoViewList.size() > mPhotoViewList.getCurrentIndex()) {
            mPhotoViewList.get(mPhotoViewList.getCurrentIndex()).setIsSelected(false);
//            mPhotoViewList.setCurIndex(-1);
            return true;
        }
        if (mStickerViewList != null && !mStickerViewList.isEmpty() && mStickerViewList.getCurrentIndex() != -1
                && mStickerViewList.size() > mStickerViewList.getCurrentIndex()) {
            mStickerViewList.get(mStickerViewList.getCurrentIndex()).setInEdit(false);
//            mStickerViewList.setCurrentIndex(-1);
            return true;
        }
        return false;
    }

    /**
     * Called at onDestroy(). Used to release memory,object,... for performance improvement.
     */
    public void release() {
        if (mCollageViewListener != null)
            mCollageViewListener = null;
//        if (mSvgItem != null) {
//            mSvgItem.clear();
//            mSvgItem = null;
//        }
        if (mPhotoViewList != null) {
            mPhotoViewList.release();
            mPhotoViewList.clear();
            mPhotoViewList = null;
        }
        if (mCachedPhotoViewList != null) {
            mCachedPhotoViewList.release();
            mCachedPhotoViewList.clear();
            mCachedPhotoViewList = null;
        }
        if (mStickerViewList != null) {
            mStickerViewList.release();
            mStickerViewList.clear();
            mStickerViewList = null;
        }
        if (mCollageViewPaint != null) {
            mCollageViewPaint.reset();
            mCollageViewPaint = null;
        }
        if (mBackgroundPaint != null) {
            mBackgroundPaint.reset();
            mBackgroundPaint = null;
        }
        if (mFramePaint != null) {
            mFramePaint.reset();
            mFramePaint = null;
        }
        if (mCollageViewRect != null) {
            mCollageViewRect.setEmpty();
            mCollageViewRect = null;
        }
        if (mGestureDetector != null) {
            mGestureDetector = null;
        }
        if (mDragPhotoview != null) {
            mDragPhotoview.release();
            mDragPhotoview = null;
        }
        if (mFrameBmp != null) {
            mFrameBmp = BitmapHelper.recycle(mFrameBmp);
        }
        if (mBgBmp != null) {
            mBgBmp = BitmapHelper.recycle(mBgBmp);
        }
        if (mMagazineBmp != null) {
            mMagazineBmp = BitmapHelper.recycle(mMagazineBmp);
        }
    }

    public void setCollageViewRatio(float collageViewRatio) {
        setOldDimens();
        mCollageViewRatio = collageViewRatio;

        if (!mIsMagazineType && mStickerViewList != null) {
            mStickerViewList.invalidateRatio(mCollageViewRatio);
        }
    }

    private void setCollageViewRect(int left, int top, int width, int height) {
        if (mCollageViewRect == null)
            mCollageViewRect = new Rect();
        mCollageViewRect.set(left, top, width, height);
    }

    public Rect getCollageViewRect() {
        return mCollageViewRect;
    }

    public int getNumOfPhotos() {
        return mNumOfPhotos;
    }

    public int[] getOldSize() {
        return mOldDimens;
    }

    public void setOldDimens() {
        mOldDimens[0] = getWidth();
        mOldDimens[1] = getHeight();
    }

    /**
     * Add sticker view (Icon/Text) to collageview.
     *
     * @param stickerView a stickerview: icon or text.
     */
    public void addStickerView(BaseStickerView stickerView) {
        Flog.d("stickerView: " + stickerView);
        stickerView.setStickerViewListener(this);
        // TODO:
        Flog.d(TAG, "size=" + mStickerViewList.size());
        stickerView.setIndex(mStickerViewList.size());
        mStickerViewList.add(stickerView);
//        if (stickerView instanceof TextStickerView) {
//            mStickerViewList.requestKeyboard((TextStickerView) stickerView, true);
//        }
        mStickerViewList.setCurrentIndex(mStickerViewList.size() - 1);
        mStickerViewList.updateIndices();

        if (stickerView instanceof IconStickerView) mFocusedViewtype = FOCUS_ON_ICON_STICKER;
        else if (stickerView instanceof TextStickerView) mFocusedViewtype = FOCUS_ON_TEXT_STICKER;

        if (mPhotoViewList != null)
            mPhotoViewList.unselected();
    }

    public PhotoViewList getPhotoViewList() {
        return mPhotoViewList;
    }

    public void setPhotoViewList(PhotoViewList photoViewList) {
        mPhotoViewList = photoViewList;
    }

    public StickerViewList getStickerViewList() {
        return mStickerViewList;
    }

    public void setStickerViewList(StickerViewList stickerViewList) {
        mStickerViewList = stickerViewList;
    }

    public BaseStickerView getCurrentSticker() {
        if (mStickerViewList.isEmpty() || mStickerViewList.getCurrentIndex() < 0 || mStickerViewList.getCurrentIndex() >= mStickerViewList.size()) {
            return null;
        }
        return mStickerViewList.get(mStickerViewList.getCurrentIndex());
    }

    @Override
    public void onTextStickerClicked(int stickerIndex) {
        if (mCollageViewListener != null)
            mCollageViewListener.onTextStickerClicked(stickerIndex);
    }

    @Override
    public void onStickerDeleted(int stickerIndex) {
        Flog.d(TAG, "bbb onStickerDeleted: 1=" + this + "_2=" + mStickerViewList + "_3=" + (stickerIndex >= mStickerViewList.size())
                + "_4=" + (stickerIndex < 0));
        Flog.d(TAG, "bbb idx=" + stickerIndex + "_SIZE=" + mStickerViewList.size());
        if (this == null || mStickerViewList == null || (stickerIndex >= mStickerViewList.size() || stickerIndex < 0))
            return;
        BaseStickerView stickerView = mStickerViewList.get(stickerIndex);
        stickerView.release();
        stickerView.setIndex(-1);
        mStickerViewList.setCurrentIndex(-1);
        mStickerViewList.remove(stickerIndex);
        mStickerViewList.updateIndices();
//        Flog.d(TAG, "bbb size=" + mStickerViewList.size());
//        for (int i = 0; i < mStickerViewList.size(); i++) {
//            Flog.d(TAG, i+" bbb="+ mStickerViewList.get(i));
//        }
    }

    @Override
    public void onStickerMoving(int stickerIndex) {
        if (mCollageViewListener != null)
            mCollageViewListener.onStickerMoving(stickerIndex);
    }

    @Override
    public void onStickerStoped(int stickerIndex) {

    }

    @Override
    public void onInputTextSticker(int textStickerIndex) {
        Flog.d(TAG, "onInputTextSticker idx= " + textStickerIndex);
        if (mCollageViewListener != null)
            mCollageViewListener.onInputTextSticker(textStickerIndex);
    }

    @Override
    public void onPhotoActionDown(int photoIndex) {

    }

    @Override
    public void onPhotoActionMove(int photoIndex) {

    }

    @Override
    public void onPhotoActionUp(int photoIndex) {
        if (mCollageViewListener != null)
            mCollageViewListener.onPhotoviewActionUp(photoIndex);
    }

    /**
     * Load photo(s) from gallery.
     * - Handle decoding image.
     * - Update collageview when loading done.
     *
     * @param arrChangedIndices the list of changed indices of rectangle in collageview.
     * @param arrPhotoPaths     the list of path of photo/image in galley , corresponding to index of rectangle.
     */
    public void loadPhotosFromGallery(ArrayList<Integer> arrChangedIndices, ArrayList<String> arrPhotoPaths) {
//        Flog.d(TAG, "arrChangedIndices size=" + arrChangedIndices.size());
//        for (int i = 0; i < arrChangedIndices.size(); i++) {
//            Flog.d(TAG, i + "=" + arrChangedIndices.get(i));
//        }
//        Flog.d(TAG, "---------------------------------------------");
//        Flog.d(TAG, "arrPhotoPaths size=" + arrPhotoPaths.size());
//        for (int i = 0; i < arrPhotoPaths.size(); i++) {
//            Flog.d(TAG, i + "=" + arrPhotoPaths.get(i));
//        }
//        Flog.d(TAG, "---------------------------------------------");

        if (arrChangedIndices.size() != arrPhotoPaths.size() || mPhotoViewList == null
                || mPhotoViewList.size() < arrChangedIndices.size()) return;
        mTotalThreads = arrChangedIndices.size();
        if (mTotalThreads < 1) return;
        mCntThreadDone = 0;

        Flog.d(TAG, "mNumOfPhotos=" + mNumOfPhotos);
        if (mNumOfPhotos != 1) {
            for (int i = 0; i < mTotalThreads; i++) {
                String path = arrPhotoPaths.get(i);
//            int reqWidth = (int) mPhotoViewList.get(arrChangedIndices.get(i)).getRectF().width() * ConstValues.BITMAP_SCALE_VALUE;
                int reqWidth = (int) mPhotoViewList.getMaxWidth() * ConstValues.BITMAP_SCALE_VALUE;
                if (reqWidth > getWidth())
                    reqWidth = getWidth();
                Flog.d(TAG, "1 reqWidth=" + reqWidth);

                new LoadPhotoAsync(getContext(), arrChangedIndices.get(i), mTextureMaxSize).setLoadPhotoListener(this).executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, path, reqWidth);
            }
        } else {
            for (int i = 0; i < mTotalThreads; i++) {
                String path = arrPhotoPaths.get(i);
//            int reqWidth = (int) mPhotoViewList.get(arrChangedIndices.get(i)).getRectF().width() * ConstValues.BITMAP_SCALE_VALUE;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                Flog.d(TAG, "real bitmap decoded: w=" + options.outWidth + "_h=" + options.outHeight);


                int reqWidth;
                if (mPhotoViewList.get(0).isOutOfMemory()) {

                    reqWidth = MathUtil.getFitCenterImgSize(mWidthScreen, mHeightScreen,
                            options.outWidth, options.outHeight)[1];
                } else {

                    reqWidth = (options.outWidth > options.outHeight ? options.outHeight : options.outWidth);
                }
                Flog.d(TAG, "2 reqWidth=" + reqWidth);

                new LoadPhotoAsync(getContext(), arrChangedIndices.get(i), mTextureMaxSize).setLoadPhotoListener(this).executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, path, reqWidth);
            }
        }

    }

    @Override
    public void onLoadDone(int idPhoto, Bitmap bmp, boolean outOfMemoryError) {
        Flog.d(TAG, "onLoadDone outOfMemoryError=" + outOfMemoryError);
        if (outOfMemoryError && mCollageViewListener != null) {
            mCollageViewListener.outOfMemoryError(idPhoto);
        }
        mCntThreadDone++;
        Flog.d(TAG, "mCntThreadDone=" + mCntThreadDone + "_bmp=" + bmp + "_id=" + idPhoto);
        if (bmp != null)
            Flog.d(TAG, "bmp: w=" + bmp.getWidth() + "_h=" + bmp.getHeight());
        if (mPhotoViewList == null || idPhoto >= mPhotoViewList.size()) return;
        PhotoView photoView = mPhotoViewList.get(idPhoto);
        photoView.setBitmap(bmp);
        photoView.fitPhotoToLayout();
        if (mCntThreadDone == mTotalThreads) {
            Flog.d(TAG, "all load done!");
            mCollageViewListener.onPhotosLoadDone();
            // All the changed photos load done. Update the collageview.
            invalidate();
        }
    }

    /**
     * Get the type of current view that is focused/selected on collageview.
     *
     * @return type of current view that is focused/selected on collageview. Includes:
     * 1, FOCUS_ON_ICON_STICKER = 258
     * 2, FOCUS_ON_TEXT_STICKER = 274
     * 3, FOCUS_ON_PHOTO = 290
     * 4, FOCUS_ON_NO_PHOTO = 306
     * 5, FOCUS_ON_NOTHING = -1;
     */
    public int getFocusedViewtype() {
        return mFocusedViewtype;
    }

    public void setFocusedViewtype(int focusedViewtype) {
        mFocusedViewtype = focusedViewtype;
    }

    /**
     * Save the final collaged photo.
     * Can change resolution for saving by calling setResolution(int).
     */
    public void save() {

        int maxTextureSize = EGL14Util.getMaxTextureSize();
        Flog.d(TAG, "maxTextureSize=" + maxTextureSize);
        if (maxTextureSize <= 0) return;
        int widthSave, heightSave;
        Flog.d(TAG, "mSaveResolution=" + mSaveResolution);
        heightSave = mSaveResolution;
        widthSave = (int) (heightSave * mCollageViewRatio);

        Flog.d(TAG, "save: w=" + widthSave + "_h=" + heightSave);
        Flog.d(TAG, "save: maxTextureSize=" + maxTextureSize);

        if (widthSave >= maxTextureSize || heightSave >= maxTextureSize) {
            Flog.d(TAG, "createSaveBitmap 1");
            if (mCollageViewRatio >= 1) {
                createSaveBitmap((int) (maxTextureSize * mCollageViewRatio), maxTextureSize);
            } else {
                createSaveBitmap(maxTextureSize, (int) (maxTextureSize * mCollageViewRatio));
            }
        } else {
            Flog.d(TAG, "createSaveBitmap 2");
            createSaveBitmap(widthSave, heightSave);
        }
    }

    /**
     * Create bitmap for saving image.
     *
     * @param widthSave  the width of this bitmap.
     * @param heightSave the height of this bitmap.
     */
    private void createSaveBitmap(int widthSave, int heightSave) {

        Flog.d(TAG, "0 createSaveBitmap: w=" + widthSave + "_h=" + heightSave);
        /**
         * The scale ratio between collageview and the final saved-collageview.
         * */
        float savedScaleRatio = widthSave * 1.f / this.getWidth();
        Flog.d(TAG, "savedScaleRatio=" + savedScaleRatio);
        /**
         * Update the attributes of child views in collageview.
         * */
        savedCollageView = new CollageView(getContext());
        Flog.d(TAG, "mRatioView=" + mCollageViewRatio);
        Flog.d(TAG, "this.getWidth()=" + this.getWidth() + "_getHeight()=" + this.getHeight());
        savedCollageView.setTypeCollage(mTypeCollage);

        if (mTypeCollage != ConstValues.COLLAGE_TEXT_TYPE) {
            /**
             * Initialize the condition for drawing collageview.
             * */
            savedCollageView.setMagazine(getMagazineBmp());
            savedCollageView.setCollageViewRatio(mCollageViewRatio);
            savedCollageView.setCollageViewRect(0, 0, widthSave, heightSave);
            savedCollageView.setSvgItem(mSvgItem);

            /**
             * Initialize the photo list.
             * */
            PhotoViewList savedPhotoList = new PhotoViewList(savedCollageView);
            Matrix doubleMatrix = new Matrix();
            doubleMatrix.setScale(widthSave / mSvgItem.viewportWidth, heightSave / mSvgItem.viewportHeight);
            for (int i = 0; i < mPhotoViewList.size(); i++) {
                PhotoView photoView = mPhotoViewList.get(i);
                PhotoView savedPhotoView = new PhotoView(savedCollageView, i);
                savedPhotoView.setSavedState(true);
                Path temp = new Path(photoView.getOriginPath());
                temp.transform(doubleMatrix);
                savedPhotoView.setPath(temp);
                savedPhotoView.setWidthSignAddition(photoView.getWidthSignAddition() * savedScaleRatio);
                savedPhotoView.setStrokeWidthLinePaint(photoView.getStrokeWidthLinePaint() * savedScaleRatio);
                savedPhotoView.setIsSelected(false);

                // TODO:
                savedPhotoView.setBitmap(photoView.getBitmap());

                savedPhotoView.setMatrix(fitRatioMatrix(photoView.getMatrix(), savedScaleRatio));
                savedPhotoView.setRoundValue(photoView.getRoundValue() * savedScaleRatio);
                savedPhotoView.setBorderPaint(photoView.getBorderPaint());
                savedPhotoView.setStrokeWidth(photoView.getStrokeWidth() * savedScaleRatio);
                if (photoView.getBorderPaint().getShader() != null) {
                    Flog.d(TAG, "set change pattern at " + i);
                    Bitmap oldBmp = photoView.getPatternPathBorderBmp();
                    Flog.d(TAG, "the value of old bitmap: w=" + oldBmp.getWidth() + "_h=" + oldBmp.getHeight());
                    Bitmap newBmp = Bitmap.createScaledBitmap(
                            oldBmp,
                            (int) (oldBmp.getWidth() * savedScaleRatio),
                            (int) (oldBmp.getHeight() * savedScaleRatio),
                            false);
                    Flog.d(TAG, "the value of new bitmap: w=" + newBmp.getWidth() + "_h=" + newBmp.getHeight());
                    savedPhotoView.setPatternPathBorderBmp(newBmp);
                }
                savedPhotoList.add(savedPhotoView);
            }
            Flog.d(TAG, "saveaaaa  mBeforeMarginValue=" + mBeforeMarginValue);
            if (mBeforeMarginValue < 1F) {
                Flog.d(TAG, "marginnnnnnnnnnnnnnn");
                savedPhotoList.setItemMargin(mBeforeMarginValue);
            }
            savedCollageView.setPhotoViewList(savedPhotoList);

            /**
             * Initialize the sticker list.
             * */
            StickerViewList savedStickerList = new StickerViewList(savedCollageView);
            for (int i = 0; i < mStickerViewList.size(); i++) {
                BaseStickerView stickerView = mStickerViewList.get(i);
                BaseStickerView savedStickerView;
                if (stickerView instanceof IconStickerView) {
                    savedStickerView = new IconStickerView(savedCollageView, i);
                } else {
                    savedStickerView = new TextStickerView(savedCollageView, i);
//                ((TextStickerView) savedStickerView).setText(((TextStickerView) stickerView).getText());
                    TextPaint textPaint = ((TextStickerView) stickerView).getTextFont();
                    ((TextStickerView) savedStickerView).setTextFont(textPaint.getTypeface());
                    if (textPaint.getShader() == null)
                        ((TextStickerView) savedStickerView).getTextFont().setColor(textPaint.getColor());
                    else
                        ((TextStickerView) savedStickerView).getTextFont().setShader(textPaint.getShader());
                    ((TextStickerView) savedStickerView).getTextFont().setTextSize((textPaint.getTextSize()));
                    ((TextStickerView) savedStickerView).setTextPadding((((TextStickerView) stickerView).getTextPadding()));
                }
                savedStickerView.setBitmap(stickerView.getBitmap());
                savedStickerView.setMatrix(fitRatioMatrix(stickerView.getMatrix(), savedScaleRatio));
                savedStickerView.setInEdit(false);
                savedStickerView.setOpacity(stickerView.getOpacity());
                savedStickerList.add(savedStickerView);
            }
            savedCollageView.setStickerViewList(savedStickerList);


            /**
             * Initialize the background.
             * */
            if (mBackgroundColor == ConstValues.NO_COLOR_VALUE) {

                Bitmap backgroundBmp = Bitmap.createScaledBitmap(mBgBmp,
                        (int) (mBgBmp.getWidth() * savedScaleRatio),
                        (int) (mBgBmp.getHeight() * savedScaleRatio),
                        false);

                savedCollageView.setBackgroundPattern(backgroundBmp);
            } else {
                savedCollageView.setBackgroundColor(mBackgroundColor);
            }

        } else {

            // TEXT COLLAGE:
            savedCollageView.setCollageViewRatio(mCollageViewRatio);
            savedCollageView.setCollageViewRect(0, 0, widthSave, heightSave);

            ArrayList<WordView> newListWordViews = new ArrayList<>();
            for (int i = 0; i < mListWords.size(); i++) {
                WordView wordView = mListWords.get(i);

                WordView newWordView = new WordView(savedCollageView);
                newWordView.setWord(wordView.getWord(), CommonVl.WORD_SIZE * savedScaleRatio);
                newWordView.setInit(false);

                newWordView.setWordMatrix(fitRatioWordMatrix(wordView.getWordMatrix(), savedScaleRatio));
                newWordView.setListWordBmps(wordView.getListWordBmps());

                ArrayList<Matrix> matrices = new ArrayList<>();
                matrices.addAll(wordView.getListWordMatrices());
                for (int j = 0; j < matrices.size(); j++) {
                    matrices.set(j, fitRatioMatrix(matrices.get(j), savedScaleRatio));
                }
                newWordView.setListWordMatrices(matrices);

                newListWordViews.add(newWordView);
            }
            savedCollageView.setListWords(newListWordViews);

            if (mBgGalleryBmp != null) {

                Bitmap bgGalleryBmp = Bitmap.createScaledBitmap(mBgGalleryBmp,
                        (int) (mBgGalleryBmp.getWidth() * savedScaleRatio),
                        (int) (mBgGalleryBmp.getHeight() * savedScaleRatio),
                        false);

                savedCollageView.setBgGallery(bgGalleryBmp);
            } else {

                /**
                 * Initialize the background.
                 * */
                if (mBackgroundColor == ConstValues.NO_COLOR_VALUE) {

                    Bitmap backgroundBmp = Bitmap.createScaledBitmap(mBgBmp,
                            (int) (mBgBmp.getWidth() * savedScaleRatio),
                            (int) (mBgBmp.getHeight() * savedScaleRatio),
                            false);

                    savedCollageView.setBackgroundPattern(backgroundBmp);
                } else {
                    savedCollageView.setBackgroundColor(mBackgroundColor);
                }
            }
        }

        /**
         * Initialize the frame.
         * */
        savedCollageView.setFrameWidth((int) (mFrameWidth * savedScaleRatio));
        if (mFrameColor == ConstValues.NO_COLOR_VALUE) {

            Bitmap frameBmp = Bitmap.createScaledBitmap(mFrameBmp,
                    (int) (mFrameBmp.getWidth() * savedScaleRatio),
                    (int) (mFrameBmp.getHeight() * savedScaleRatio),
                    false);

            savedCollageView.setFramePattern(frameBmp);
        } else {
            savedCollageView.setFrameColor(mFrameColor);
        }

        boolean canSave = false;
        Bitmap saveBitmap = null;
        try {
            /**
             * Redraw the saved collageview.
             * */
//            int reqSize = (widthSave >= heightSave) ? widthSave : heightSave;
//            Bitmap newBitmap = BitmapUtil.sampeZoomFromBitmap(getBitmapFromView(this), reqSize);

            saveBitmap = Bitmap.createBitmap(widthSave, heightSave, Bitmap.Config.ARGB_8888);
            Canvas saveCanvas = new Canvas(saveBitmap);

//            Canvas saveCanvas = new Canvas(newBitmap);
            savedCollageView.draw(saveCanvas);
            canSave = true;
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
            canSave = false;
            if (mCollageViewListener != null)
                mCollageViewListener.outOfMemoryErrorSave();
            Flog.d(TAG, "OutOfMemoryError... w=" + widthSave + "_h" + heightSave);
        }

        if (!canSave)
            return;
        /**
         * Create the image file that is redrawn again.
         * */
        String absolutePathSavedPhoto = FileUtils.APP_FOLDER;
        new SaveAsync(getContext()).setOnSavedFinish(this)
                .execute(saveBitmap, absolutePathSavedPhoto);
    }

    private Matrix fitRatioWordMatrix(Matrix matrix, float ratio) {
        Matrix tmp = new Matrix(matrix);
        float values[] = new float[9];
        matrix.getValues(values);
        // get old values of matrix stickers:
//        float scale_X = values[0];
//        float skew_X = values[1];
        float transform_X = values[2];
//        float skew_Y = values[3];
//        float scale_Y = values[4];
        float transform_Y = values[5];

        // set new values for matrix stickers:
        float newTransform_X = transform_X * ratio;
        float newTransform_Y = transform_Y * ratio;
//        tmp.preScale(ratio, ratio);

        Matrix concatMatrix = new Matrix();
        concatMatrix.setTranslate(newTransform_X - transform_X, newTransform_Y - transform_Y);
        tmp.postConcat(concatMatrix);
        return tmp;
    }

    /**
     * Scale fit matrix to collage view that has the new ratio.
     *
     * @param matrix the initial matrix of collageview.
     * @param ratio  the ratio value of the saved-collageview with the collageview.
     * @return the matrix is scale-fitted to the saved-collageview.
     */
    private Matrix fitRatioMatrix(Matrix matrix, float ratio) {
        Matrix tmp = new Matrix(matrix);
        float values[] = new float[9];
        matrix.getValues(values);
        // get old values of matrix stickers:
//        float scale_X = values[0];
//        float skew_X = values[1];
        float transform_X = values[2];
//        float skew_Y = values[3];
//        float scale_Y = values[4];
        float transform_Y = values[5];

        // set new values for matrix stickers:
        float newTransform_X = transform_X * ratio;
        float newTransform_Y = transform_Y * ratio;
        tmp.preScale(ratio, ratio);

        Matrix concatMatrix = new Matrix();
        concatMatrix.setTranslate(newTransform_X - transform_X, newTransform_Y - transform_Y);
        tmp.postConcat(concatMatrix);
        return tmp;
    }

    @Override
    public void onSavedDone(Uri uri) {
        Flog.d(TAG, "onSavedDone = " + uri);
        if (mCollageViewListener != null) {
            if (savedCollageView != null) {
                savedCollageView.release();
                savedCollageView = null;
            }
            mCollageViewListener.onSavedDone(uri);
        }
    }

    public void setSvgItem(SVGItem svgItem) {
        mSvgItem = svgItem;
    }

    public void setFrameWidth(int frameWidth) {
        mFrameWidth = frameWidth;
    }

    public CollageViewListener getCollageViewListener() {
        return mCollageViewListener;
    }

    public CollageView setCollageViewListener(CollageViewListener listener) {
        mCollageViewListener = listener;
        return this;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * Set color background.
     *
     * @param color The color code of background. Equal to -1 if background is pattern,
     *              otherwise background is color.
     */
    @Override
    public void setBackgroundColor(@ColorInt int color) {
        mBgGalleryBmp = BitmapHelper.recycle(mBgGalleryBmp);
        mOriginBgGalleryBmp = BitmapHelper.recycle(mOriginBgGalleryBmp);
        mBackgroundColor = color;
        mBackgroundPaint.reset();
    }

    public int getFrameBorderColor() {
        return mFrameColor;
    }

    public void setSaveResolution(int saveResolution) {
        mSaveResolution = saveResolution;
    }

    public boolean isMagazineType() {
        return mIsMagazineType;
    }

    public void setMagazine(Bitmap magazineBmp) {
        mMagazineBmp = BitmapHelper.recycle(mMagazineBmp);
        mMagazineBmp = magazineBmp;
        mIsMagazineType = (mMagazineBmp != null);
    }

    public Bitmap getMagazineBmp() {
        return mMagazineBmp;
    }

    public void saveAdaptive(int wScreen, int hScreen) {
        Flog.d(TAG, "--------------saveAdaptive----------------");
        Flog.d(TAG, "mCollageViewRatio=" + mCollageViewRatio);
        Flog.d(TAG, "screen: w=" + wScreen + "_h=" + hScreen);
        int wSave = 0, hSave = 0;
        int wImg = wScreen;
        int hImg = (int) (wImg / mCollageViewRatio);
        Flog.d(TAG, "image: w=" + wImg + "_h=" + hImg);
        float rImg = 1 / mCollageViewRatio;
        Flog.d(TAG, "image: r=" + rImg);

        if (wImg < hImg) {
            wSave = wScreen;
            hSave = (int) (wSave * rImg);
            if (hSave > hScreen) {
                hSave = hScreen;
                wSave = (int) (hSave / rImg);
            }
        } else {
            wSave = wScreen;
            hSave = (int) (wSave * rImg);
        }

        Flog.d(TAG, "save: w=" + wSave + "_h=" + hSave);
        Flog.d(TAG, "*********************************");
        createSaveBitmap(wSave, hSave);
    }

    public void addWord(WordView wordView) {
//        mTypeCollage = ConstValues.TYPE_COLLAGE_TEXT;

        mListWords.add(wordView);
        int curId = mListWords.size() - 1;

        Flog.d(TAG, "addWord: curId=" + curId);
        WordView curWordview = mListWords.get(curId);
        curWordview.setId(curId);
        mCurId = curId;
        setSelectedViews();
    }

    private void setSelectedViews() {
        for (int i = 0; i < mListWords.size(); i++) {
            WordView wordView = mListWords.get(i);
            wordView.setIsSelected(i == mCurId);
        }
    }

    private void drawListWords(Canvas canvas) {
        if (canvas == null || mListWords == null || mListWords.isEmpty())
            return;
        Flog.d(TAG, "size=" + mListWords.size());
        for (int i = 0; i < mListWords.size(); i++) {
            WordView wordView = mListWords.get(i);
            wordView.onDraw(canvas);
        }
    }

    public void setListWords(ArrayList<WordView> listWords) {
        mListWords.clear();
        mListWords.addAll(listWords);
    }

    public void removeWord(int id) {
        if (mListWords == null || mListWords.isEmpty() || id < 0 || id >= mListWords.size())
            return;
        WordView curWordview = mListWords.get(id);
        Flog.d(TAG, "onDeleteWordView: curWordview=" + curWordview);
        mListWords.remove(id);
        mCurId = -1;
        setSelectedViews();
    }

    public void setBgGallery(Bitmap bmp) {
        if (bmp == null || bmp.isRecycled())
            return;
        Flog.d(TAG, "setBgGallery bmp: w=" + bmp.getWidth() + "_h=" + bmp.getHeight());
        mBgGalleryBmp = BitmapHelper.recycle(mBgGalleryBmp);
        mBgGalleryBmp = bmp.copy(Bitmap.Config.ARGB_8888, true);

        bmp = BitmapHelper.recycle(bmp);
        invalidate();
    }

    public Bitmap getBgGalleryBmp() {
        return mBgGalleryBmp;
    }

    public Bitmap getOriginBgGalleryBmp() {
        return mOriginBgGalleryBmp;
    }

    public void setOriginBgGalleryBmp(Bitmap bmp) {
        if (bmp == null || bmp.isRecycled())
            return;
        mOriginBgGalleryBmp = BitmapHelper.recycle(mOriginBgGalleryBmp);
        mOriginBgGalleryBmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
    }

    public void setScreenSizes(int widthScreen, int heightScreen) {
        mWidthScreen = widthScreen;
        mHeightScreen = heightScreen;
        Flog.d(TAG, "Screen: w=" + mWidthScreen + "_h=" + mHeightScreen);
    }
}
