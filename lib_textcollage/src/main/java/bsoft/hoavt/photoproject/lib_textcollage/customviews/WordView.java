package bsoft.hoavt.photoproject.lib_textcollage.customviews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bsoft.hoavt.photoproject.lib_textcollage.R;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.BitmapUtil;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.CanvasUtils;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.CommonVl;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.EGL14Util;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.Flog;
import bsoft.hoavt.photoproject.lib_textcollage.listeners.OnWordViewListener;
import bsoft.hoavt.photoproject.lib_textcollage.models.CharacterItem;
import bsoft.hoavt.photoproject.lib_textcollage.models.WordItem;

/**
 * Created by vutha on 7/5/2017.
 */

public class WordView extends BaseView {

    private static final String TAG = WordView.class.getSimpleName();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM_2F = 2;
    private static final int ZOOM_1F = 3;
    private final float BITMAP_SCALE = 0.7f;
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
    protected float lastRotateDegree;
    protected float lastLength;
    protected boolean mIsSelected = false;
    protected boolean isInEdit;
    protected PointF mid = new PointF();
    protected float scaleValue = 1.0f;
    //    private ArrayList<CharacterView> list = new ArrayList<>();
    private Paint mBorderPaint;
    private Paint mTextPaint;
    private ArrayList<Bitmap> mListWordBmps = new ArrayList<>();
    private ArrayList<Matrix> mListWordMatrices = new ArrayList<>();
    private Paint paint;
    private String mWord = "";
    private Bitmap mBitmap;
    private Canvas mTextCanvas;
    private Matrix mWordMatrix = new Matrix();
    private boolean mInit = true;
    private int mode = NONE;
    // these matrices will be used to move and zoom image
    private Matrix savedMatrix = new Matrix();
    // remember some things for zooming
    private PointF startP = new PointF();
    private PointF midP = new PointF();
    private float oldDist = 1f;
    private float oldAngle = 0f;
    private float newAngle = 0f;
    private OnWordViewListener listener;
    private String[] mWordLines;
    private int descent = 0, ascent = 0;
    private float wordHeight;
    private Path path = new Path();
    /**
     * Only draw once for shadowing.
     */
    private boolean mIsShadow = true;
    private Paint mAntiShadowPaint;
    private int[] mIndices;

    public WordView(ViewGroup collageView) {
        super(collageView);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        Flog.d(TAG, "onDraw");

        if (mTextCanvas == null || mBitmap == null || mBitmap.isRecycled())
            return;

        float remainDis = mHeight - wordHeight;
        float halfRemain = remainDis / 2F;
        float baseline = halfRemain + ascent;
        Flog.d(TAG, "1 2 baseline=" + baseline);

//        for (int i = 0; i < mListWordBmps.size(); i++) {
//            Flog.d(TAG, "bitmap at " + i + "=" + mListWordBmps.get(i));
//        }

        float left = 0f;

        Rect bounds = new Rect();
        mTextPaint.getTextBounds(mWord, 0, mWord.length(), bounds);

        int idx = 0;
        for (int j = 0; j < mWordLines.length; j++) {

            left = 0;

            for (int i = 0; i < mWordLines[j].length(); i++) {

                String character = mWordLines[j].charAt(i) + "";

                bounds.setEmpty();
                mTextPaint.getTextBounds(character, 0, character.length(), bounds);

                mTextCanvas.save();

//                Flog.d(TAG, "w=" + mWidth + "_y=" + mHeight);
//                Flog.d(TAG, "baseline at " + idx + "=" + baseline);
                mTextCanvas.translate(0 - bounds.left, baseline - halfRemain);

                float charWidth = mTextPaint.measureText(character);
//                Flog.d(TAG, "char at " + idx + "=" + character + "_width=" + charWidth);

                // GET PATH OF TEXT
                path.reset();
                mTextPaint.getTextPath(character, 0, character.length(), left, 0, path);
                /**
                 *  Paint style needs to have fill set, and it seems getTextPath() returns a Path that isn't closed,
                 *  so need to close it with path.close() before drawing
                 * */
                path.close();

                if (mIsShadow) {
                    mTextCanvas.drawText(character, left, 0, mTextPaint);
                }

                // CLIP PATH TEXT
                CanvasUtils.clipPath(mTextCanvas, path, Region.Op.REPLACE);

//                Flog.d(TAG, "bounds.top + mPaddingTop=" + (bounds.top + mPaddingTop));
//                Flog.d(TAG, "bounds.left + mPaddingLeft=" + (bounds.left + mPaddingLeft));
                int leftChar = Math.abs(bounds.left);
                int topChar = Math.abs(bounds.top);
                Flog.d(TAG, "Character: left=" + leftChar + "_top=" + topChar);

                Flog.d(TAG, "Canvas AAA : 3=" + mTextCanvas.getClipBounds());
                mTextCanvas.translate(leftChar + mPaddingLeft + left, -topChar + mPaddingTop);  // substraction
                Flog.d(TAG, "Canvas AAA : 4=" + mTextCanvas.getClipBounds());
                Flog.d(TAG, "leftChar=" + leftChar + "_mPaddingLeft=" + mPaddingLeft + "_left=" + left + "_topChar=" + topChar + "_mPaddingTop=" + mPaddingTop);

                Flog.d(TAG, "idx=" + idx);
                if (!mListWordBmps.isEmpty() && !mListWordMatrices.isEmpty() && mIndices[idx] < mListWordBmps.size()) {
                    if (mListWordBmps.get(mIndices[idx]) != null)
                        mTextCanvas.drawBitmap(mListWordBmps.get(mIndices[idx]), mListWordMatrices.get(mIndices[idx]), null);
                    else
                        mTextCanvas.drawColor(Color.WHITE);
                } else {
                    mTextCanvas.drawColor(Color.WHITE);
                }


                // draw border for text overlay border of image.
                if (true) {
                    mTextCanvas.translate(-(leftChar + mPaddingLeft + left), -(-topChar + mPaddingTop));

                    mAntiShadowPaint.set(mTextPaint);
                    float shadow = 0f;
                    mAntiShadowPaint.setShadowLayer(shadow, shadow, shadow, 0xFF000000);
                    mTextCanvas.drawText(character, left, 0, mAntiShadowPaint);
                }

                left += charWidth;
                idx++;
                mTextCanvas.restore();
            }

            baseline += wordHeight;
        }

        mIsShadow = false;

        float marginX = 0;
        float marginY = ascent + mPaddingTop + halfRemain;
        if (mInit) {
            Flog.d(TAG, "init word matrix");
            mWordMatrix.setTranslate(bounds.left + marginX, bounds.top + marginY);
            mInit = false;
        }

        if (mBitmap != null)
            canvas.drawBitmap(mBitmap, mWordMatrix, paint);

        if (mIsSelected) {
            // DRAW SELECTED STATE:
            float[] arrayOfFloat = new float[9];
            mWordMatrix.getValues(arrayOfFloat);
            float f1 = arrayOfFloat[2];
            float f2 = arrayOfFloat[5];
            float f3 = arrayOfFloat[0] * mBitmap.getWidth() + arrayOfFloat[2];
            float f4 = arrayOfFloat[3] * mBitmap.getWidth() + arrayOfFloat[5];
            float f5 = arrayOfFloat[1] * mBitmap.getHeight() + arrayOfFloat[2];
            float f6 = arrayOfFloat[4] * mBitmap.getHeight() + arrayOfFloat[5];
            float f7 = arrayOfFloat[0] * mBitmap.getWidth() + arrayOfFloat[1] * mBitmap.getHeight() + arrayOfFloat[2];
            float f8 = arrayOfFloat[3] * mBitmap.getWidth() + arrayOfFloat[4] * mBitmap.getHeight() + arrayOfFloat[5];

            //删除在右上角
            dst_delete.left = (int) (f3 - (deleteBitmapWidth >> 1));
            dst_delete.right = (int) (f3 + (deleteBitmapWidth >> 1));
            dst_delete.top = (int) (f4 - (deleteBitmapHeight >> 1));
            dst_delete.bottom = (int) (f4 + (deleteBitmapHeight >> 1));
            //垂直镜像在左上角
            dst_top.left = (int) (f1 - (flipVBitmapWidth >> 1));
            dst_top.right = (int) (f1 + (flipVBitmapWidth >> 1));
            dst_top.top = (int) (f2 - (flipVBitmapHeight >> 1));
            dst_top.bottom = (int) (f2 + (flipVBitmapHeight >> 1));
            //拉伸等操作在右下角
            dst_resize.left = (int) (f7 - (resizeBitmapWidth >> 1));
            dst_resize.right = (int) (f7 + (resizeBitmapWidth >> 1));
            dst_resize.top = (int) (f8 - (resizeBitmapHeight >> 1));
            dst_resize.bottom = (int) (f8 + (resizeBitmapHeight >> 1));
            //水平镜像在左下角
            dst_flipV.left = (int) (f5 - (topBitmapWidth >> 1));
            dst_flipV.right = (int) (f5 + (topBitmapWidth >> 1));
            dst_flipV.top = (int) (f6 - (topBitmapHeight >> 1));
            dst_flipV.bottom = (int) (f6 + (topBitmapHeight >> 1));

            canvas.drawLine(f1, f2, f3, f4, mBorderPaint);
            canvas.drawLine(f3, f4, f7, f8, mBorderPaint);
            canvas.drawLine(f5, f6, f7, f8, mBorderPaint);
            canvas.drawLine(f5, f6, f1, f2, mBorderPaint);

//            canvas.drawBitmap(topBitmap, null, dst_top, null);
            canvas.drawBitmap(deleteBitmap, null, dst_delete, null);
            canvas.drawBitmap(resizeBitmap, null, dst_resize, null);
            canvas.drawBitmap(flipVBitmap, null, dst_flipV, null);
        }
    }

    public Region getBorderOfWord() {
        Region region = new Region();
        if (mWordMatrix == null || mBitmap == null || mBitmap.isRecycled()) {
            return region;
        }

        // DRAW SELECTED STATE:
        float[] arrayOfFloat = new float[9];
        mWordMatrix.getValues(arrayOfFloat);
        float f1 = arrayOfFloat[2];
        float f2 = arrayOfFloat[5];
        float f3 = arrayOfFloat[0] * mBitmap.getWidth() + arrayOfFloat[2];
        float f4 = arrayOfFloat[3] * mBitmap.getWidth() + arrayOfFloat[5];
        float f5 = arrayOfFloat[1] * mBitmap.getHeight() + arrayOfFloat[2];
        float f6 = arrayOfFloat[4] * mBitmap.getHeight() + arrayOfFloat[5];
        float f7 = arrayOfFloat[0] * mBitmap.getWidth() + arrayOfFloat[1] * mBitmap.getHeight() + arrayOfFloat[2];
        float f8 = arrayOfFloat[3] * mBitmap.getWidth() + arrayOfFloat[4] * mBitmap.getHeight() + arrayOfFloat[5];

        Path path = new Path();
        path.moveTo(f1, f2);
        path.lineTo(f3, f4);
        path.lineTo(f7, f8);
        path.lineTo(f5, f6);
        path.close();

        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        Flog.d(TAG, "computeBounds=" + bounds);
        region.setPath(path, new Region((int) bounds.left, (int) bounds.top,
                (int) bounds.right, (int) bounds.bottom));
        return region;
    }

    public boolean isInWord(MotionEvent event) {
        Flog.d(TAG, "event: x=" + event.getX() + "_y=" + event.getY());
        boolean inside = getBorderOfWord().contains((int) event.getX(), (int) event.getY());
        Flog.d(TAG, "inside=" + inside);
        return inside;
    }

    private int getWordHeight(String word) {
        String wordNoSpace = word.replace(" ", "");
//      int  ascent = 0, descent = 0;
        Rect boundsWord = new Rect();
        for (int i = 0; i < wordNoSpace.length(); i++) {
            String character = wordNoSpace.charAt(i) + "";
            boundsWord.setEmpty();
            mTextPaint.getTextBounds(character, 0, character.length(), boundsWord);

            Flog.d(TAG, "char at " + i + "=" + character + "_top=" + Math.abs(boundsWord.top) + "_bottom=" + Math.abs(boundsWord.bottom));
            if (ascent < Math.abs(boundsWord.top)) {
                ascent = Math.abs(boundsWord.top);
            }
            if (descent < Math.abs(boundsWord.bottom)) {
                descent = Math.abs(boundsWord.bottom);
            }
        }
        Flog.d(TAG, "---------rslt: ascent=" + ascent + "_descent=" + descent);
        int wordHeight = ascent + descent;
        return wordHeight;
    }

    @Override
    public void release() {

    }

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

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        int width = 200;
//        int height = 200;
//
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthRequirement = MeasureSpec.getSize(widthMeasureSpec);
//        if (widthMode == MeasureSpec.EXACTLY) {
//            width = widthRequirement;
//        } else if (widthMode == MeasureSpec.AT_MOST && width > widthRequirement) {
//            width = widthRequirement;
//        }
//
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightRequirement = MeasureSpec.getSize(heightMeasureSpec);
//        if (heightMode == MeasureSpec.EXACTLY) {
//            height = heightRequirement;
//        } else if (heightMode == MeasureSpec.AT_MOST && width > heightRequirement) {
//            height = heightRequirement;
//        }
//
//        setMeasuredDimension(width, height);
//    }

    private void alignTextCenter(Canvas canvas, String text, Paint mTextPaint) {
        // DRAW TEXT WITH POSITION EXACTLY:
        float widthText = mTextPaint.measureText(text);
        float paddingLeft = (mWidth - widthText) / 2;
        float paddingTop = 0;
        float startX = paddingLeft;
        float startY = paddingTop;

            /*DRAW ONCE: to get baseline coordinates of character*/
        // center the text baseline vertically
//        int verticalAdjustment = (int) (this.mHeight / 2F);
//        canvas.translate(0, verticalAdjustment);

        Rect boundsWord = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), boundsWord);
        float dx = paddingLeft;
        float dy = paddingTop;
        Flog.d(TAG, "left=" + (boundsWord.left + dx) + "_top=" + boundsWord.top + "_bottom=" + boundsWord.bottom);

            /*DRAW TWICE: draw exactly*/
        // back to top the text baseline vertically
//        canvas.translate(0, -verticalAdjustment);

        float remainDis = mHeight - boundsWord.height();
        float halfRemain = remainDis / 2F;
        float baseline = halfRemain + Math.abs(boundsWord.top + dy);
        Flog.d(TAG, "baseline=" + baseline);

        // translate to the new text-baseline vertically
        canvas.translate(0, baseline);

        if (false) {
            // draw text
            mTextPaint.setColor(Color.BLACK);
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

        // CLIP PATH TEXT
//        canvas.clipPath(path, Region.Op.REPLACE);
        CanvasUtils.clipPath(canvas, path, Region.Op.REPLACE);
        canvas.translate(0, -baseline);
    }

    private void init() {

        initOptions();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setColor(Color.BLACK);
        mBorderPaint.setStrokeWidth(2f);
        mBorderPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(150);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setStrokeWidth(CommonVl.STROKE_WIDTH_BORDER_TEXT);
        mTextPaint.setColor(CommonVl.COLOR_BORDER_TEXT);

        mAntiShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        if (mIsShadow) {
            float shadow = 2f;
            mTextPaint.setShadowLayer(shadow, shadow, shadow, 0xFF000000);
        }

//        list.clear();

//        mListWordBmps.clear();
//        mListWordBmps.add(BitmapFactory.decodeResource(getResources(), R.drawable.a));
//        mListWordBmps.add(BitmapFactory.decodeResource(getResources(), R.drawable.b));
//        mListWordBmps.add(BitmapFactory.decodeResource(getResources(), R.drawable.a));
//        mListWordBmps.add(BitmapFactory.decodeResource(getResources(), R.drawable.b));

//        Matrix matrix1 = new Matrix();
//        mListWordMatrices.add(matrix1);
//        mListWordMatrices.add(matrix1);
//        mListWordMatrices.add(matrix1);
//        mListWordMatrices.add(matrix1);
    }

    public float getDistanceTwoVertices(Matrix matrix) {

        float values[] = new float[9];
//            mWordMatrix.reset();
        matrix.getValues(values);
        // get old values of matrix stickers:
//        float scale_X = values[0];
//        float skew_X = values[1];
        float transform_X = values[2];
//        float skew_Y = values[3];
//        float scale_Y = values[4];
        float transform_Y = values[5];
        Flog.d(TAG, "trans1: x=" + transform_X + "_y=" + transform_Y);
        float distance = spacing(0, 0, transform_X, transform_Y);
        Flog.d(TAG, "distance1=" + distance);
        return distance;
    }

    protected float spacing(float xBegin, float yBegin, float xEnd, float yEnd) {
        float x = xBegin - xEnd;
        float y = yBegin - yEnd;
        return (float) Math.sqrt(x * x + y * y);
    }

    public void setWord(String word, float wordSize) {

        if (true) {


            Flog.d(TAG, "setWord getHeight=" + getHeight());
            mWord = word;

            Rect boundsWord = new Rect();
            mTextPaint.getTextBounds(mWord, 0, mWord.length(), boundsWord);
            int wBmp = (int) (mTextPaint.measureText(mWord, 0, mWord.length()));
//            int wBmp = boundsWord.width();
            int hBmp = boundsWord.height();
            Flog.d(TAG, "Bitmap: w=" + wBmp + "_h=" + hBmp);


            int maxTextureSize = EGL14Util.getMaxTextureSize();
            Flog.d(TAG, "maxTextureSize=" + maxTextureSize);


            Bitmap holderBmp = Bitmap.createBitmap(wBmp, hBmp, Bitmap.Config.ARGB_8888);


            mTextPaint.setTextSize(wordSize);

            wordHeight = getWordHeight(word);

            boundsWord.setEmpty();
            mTextPaint.getTextBounds(mWord, 0, mWord.length(), boundsWord);
//            wBmp = boundsWord.width();
//            wBmp = (int)Math.ceil(mTextPaint.measureText(mWord, 0, mWord.length()));
            /**
             * Calculating width of word using measureText is exact more than getTextBounds.
             * */
            wBmp = (int) (mTextPaint.measureText(mWord, 0, mWord.length()));
            hBmp = boundsWord.height();

            if (word == null || word.isEmpty())
                return;

            mWordLines = word.split("\n");
            ArrayList<String> listLines = new ArrayList<>();
            listLines.clear();
            for (int i = 0; i < mWordLines.length; i++) {
                String[] pLines = autoSplit(mWordLines[i], mTextPaint, maxTextureSize);
                if (pLines.length <= 0) {
                    continue;
                } else if (pLines.length == 1) {
                    listLines.add(mWordLines[i]);
                } else {
                    for (int j = 0; j < pLines.length; j++) {
                        listLines.add(pLines[j]);
                    }
                }
            }

            mWordLines = listLines.toArray(new String[listLines.size()]);


            // make array of index of word that is no contains white-space.
            int lenWords = 0;
            for (int i = 0; i < mWordLines.length; i++) {
                lenWords += mWordLines[i].length();
            }
            Flog.d(TAG, "lenWords=" + lenWords);

            mIndices = new int[lenWords];
            int idx = 0;
            for (int i = 0; i < mWord.length(); i++) {
                if (mWord.charAt(i) != '\n') {
                    mIndices[idx] = i;
                    Flog.d(TAG, "indices at " + idx + "=" + mIndices[idx]);
                    idx++;
                }
            }


            int numLines = mWordLines.length;
            Flog.d(TAG, "autoSplit: numLines=" + mWordLines.length);

            int maxOfLines = 0;
            for (int i = 0; i < mWordLines.length; i++) {
                int widthLine = (int) Math.ceil(mTextPaint.measureText(mWordLines[i], 0, mWordLines[i].length()));
                Flog.d(TAG, i + "=" + mWordLines[i] + "_width=" + widthLine);
                if (widthLine > maxOfLines) {
                    maxOfLines = widthLine;
                }
            }
            Flog.d(TAG, "autoSplit-----------------------maxLines=" + maxOfLines);

            if (numLines <= 0) return;

            Bitmap scaledBmp;
            if (numLines == 1) {

//                boolean isRepetition  = (mWord.replace("j", "").length()==0);
//                if (word.endsWith("j")&&!isRepetition) {
//                    wBmp += mTextPaint.measureText("j", 0, 1)/2;
//                    scaledBmp = Bitmap.createScaledBitmap(holderBmp, wBmp, hBmp, true);
//                } else {
                scaledBmp = Bitmap.createScaledBitmap(holderBmp, wBmp, hBmp, true);

//                }
            } else {

                if (((hBmp * numLines) > maxTextureSize) || (maxOfLines > maxTextureSize)) {
                    if (listener != null) {
                        listener.onOutOfLength(maxOfLines, hBmp * numLines);
                    }
                    return;
                }

                scaledBmp = Bitmap.createScaledBitmap(holderBmp, maxOfLines, hBmp * numLines, true);
            }


            if (scaledBmp == null || scaledBmp.isRecycled())
                return;
            setBitmap(scaledBmp);

        } else {

//        mTextPaint.setTextSize(mHeight / 1.2f);
            mTextPaint.setTextSize(wordSize);
            Flog.d(TAG, "setWord getHeight=" + mHeight);
            mWord = word;

            Rect boundsWord = new Rect();
            mTextPaint.getTextBounds(mWord, 0, mWord.length(), boundsWord);
            int wBmp = boundsWord.width();
            int hBmp = boundsWord.height();
            Flog.d(TAG, "Bitmap: w=" + wBmp + "_h=" + hBmp);

            Bitmap holderBmp = Bitmap.createBitmap(wBmp, hBmp, Bitmap.Config.ARGB_8888);
            setBitmap(holderBmp);
        }
    }

    public void setBitmap(Bitmap holderBmp) {
        mBitmap = BitmapUtil.recycle(mBitmap);
        mBitmap = holderBmp;
        Flog.d(TAG, "bmp: w=" + mBitmap.getWidth() + "_h=" + mBitmap.getHeight());
        mTextCanvas = new Canvas(mBitmap);
        mTextCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Flog.d(TAG, "onTouchEvent: BaseStickerModel");

        boolean handled = true;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

//                    Flog.d(TAG, "rect boundsWord=" + mRectF);
//                    Flog.d(TAG, "touch event: x=" + event.getX(0) + "_y=" + event.getY(0));
//                    Flog.d(TAG, "contains=" + mRegion.contains((int) event.getX(0), (int) event.getY(0)));

                if (isInButton(event, dst_delete)) {
                    Flog.d(TAG, "isInDelete");
                    if (listener != null) {
                        Flog.d(TAG, "idx=" + mId + ": this=" + this);
                        listener.onDeleteWordView(mId);
//                        mParentView.invalidate();
                        return false;
                    }
                } else if (isInButton(event, dst_flipV)) {
                    //水平镜像
                    PointF localPointF = new PointF();
                    midDiagonalPoint(localPointF);
                    mWordMatrix.postScale(-1.0F, 1.0F, localPointF.x, localPointF.y);
                    Flog.d(TAG, "isInFlip");
//                    mParentView.invalidate();
                    return true;
                } else if (isInResize(event)) {
                    mode = ZOOM_1F;
                    lastRotateDegree = rotationToStartPoint(event);
                    updateMiddlePoint(event);
                    lastLength = diagonalLength(event);
                    Flog.d(TAG, "isInResize");
                } else if (isInWord(event)) {

//                    if (mPhotoViewListener != null)
//                        mPhotoViewListener.onPhotoActionDown(mId);

                    savedMatrix.set(mWordMatrix);
                    startP.set(event.getX(), event.getY());
                    mode = DRAG;

                } else {
                    handled = false;
                }
//                mParentView.invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Flog.d(TAG, "test zoom: init");
                if (oldDist > 10f) {
//                if (oldDist > 10f && event.getPointerCount() == 2 && isInBitmap2(event)) {
                    savedMatrix.set(mWordMatrix);
                    midPoint(midP, event);
                    mode = ZOOM_2F;
                    Flog.d(TAG, "test zoom: start");
                }
                oldAngle = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
//                if (mPhotoViewListener != null)
//                    mPhotoViewListener.onPhotoActionUp(mId);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    mWordMatrix.set(savedMatrix);
                    float dx = event.getX() - startP.x;
                    float dy = event.getY() - startP.y;
                    mWordMatrix.postTranslate(dx, dy);
                } else if (mode == ZOOM_2F) {
                    Flog.d(TAG, "test zoom: jumped");
                    float newDist = spacing(event);
                    Flog.d(TAG, "test zoom: newDist=" + newDist);
                    if (newDist > 10f) {
                        mWordMatrix.set(savedMatrix);
                        float scale = (newDist / oldDist);
                        Flog.d(TAG, "test zoom: scale=" + scale);
                        mWordMatrix.postScale(scale, scale, midP.x, midP.y);
                    }
                    if (mBitmap != null && event.getPointerCount() == 2) {
                        newAngle = rotation(event);
                        float r = newAngle - oldAngle;
                        Flog.d(TAG, "test zoom: r=" + r);
                        mWordMatrix.postRotate(r, midP.x, midP.y);
                    }
                } else if (mode == ZOOM_1F) {

                    mWordMatrix.postRotate((rotationToStartPoint(event) - lastRotateDegree) * 2, mid.x, mid.y);
                    lastRotateDegree = rotationToStartPoint(event);

                    scaleValue = diagonalLength(event) / lastLength;

                    lastLength = diagonalLength(event);

                    mWordMatrix.postScale(scaleValue, scaleValue, mid.x, mid.y);
                }
//                mParentView.invalidate();
                break;
        }
        return handled; // true: is focusing && false: unfocus.
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
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

    protected float rotationToStartPoint(MotionEvent event) {

        if (event == null || mWordMatrix == null) return 0f;
        float[] arrayOfFloat = new float[9];
        mWordMatrix.getValues(arrayOfFloat);
        float x = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float y = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        double arc = Math.atan2(event.getY(0) - y, event.getX(0) - x);

        return (float) Math.toDegrees(arc);
    }

    protected void updateMiddlePoint(MotionEvent event) {
        if (mWordMatrix == null || mid == null) return;
        float[] arrayOfFloat = new float[9];
        mWordMatrix.getValues(arrayOfFloat);
        float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = f1 + event.getX(0);
        float f4 = f2 + event.getY(0);
        mid.set(f3 / 2, f4 / 2);
    }

    protected float diagonalLength(MotionEvent event) {
        float diagonalLength = (float) Math.hypot(event.getX(0) - mid.x, event.getY(0) - mid.y);
        return diagonalLength;
    }

    protected void midDiagonalPoint(PointF pointF) {
        if (pointF == null || mWordMatrix == null || this.mBitmap == null) return;
        float[] arrayOfFloat = new float[9];
        mWordMatrix.getValues(arrayOfFloat);
        float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        float f5 = f1 + f3;
        float f6 = f2 + f4;
        pointF.set(f5 / 2.0F, f6 / 2.0F);
    }

    public boolean isInBitmap(MotionEvent event) {
        Flog.d(TAG, "isInBitmap: a=" + (mWordMatrix == null) + "_b=" + (mBitmap == null));
        if (mWordMatrix == null || mBitmap == null) return false;
        float[] arrayOfFloat1 = new float[9];
        mWordMatrix.getValues(arrayOfFloat1);
        Flog.d(TAG, "wordmatrix=" + mWordMatrix + "_+_bitmap: w=" + mBitmap.getWidth() + "_h=" + mBitmap.getHeight());
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

    public boolean isInBitmap2(MotionEvent event) {
        if (mWordMatrix == null || mBitmap == null) return false;
        float[] arrayOfFloat1 = new float[9];
        mWordMatrix.getValues(arrayOfFloat1);
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
        return pointInRect(arrayOfFloat2, arrayOfFloat3, event.getX(1), event.getY(1));
    }

    public void setInEdit(boolean isInEdit) {
        this.isInEdit = isInEdit;
    }

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

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public void setWordItem(WordItem wordItem) {

        setWord(wordItem.getWord(), CommonVl.WORD_SIZE);
        int len = wordItem.size();
        Flog.d(TAG, "2 len=" + len);
        // set bitmaps and matrices of Character Text View
        mListWordBmps.clear();
        mListWordMatrices.clear();
        for (int i = 0; i < len; i++) {
            CharacterItem item = wordItem.get(i);
            mListWordBmps.add(item.getBitmap());
            Flog.d(TAG, "1 textHeight=" + item.getTextHeight());
            Flog.d(TAG, "dis=" + item.getDis2Vertices());
            int textHeight = item.getTextHeight();
            float dis = item.getDis2Vertices();
            mListWordMatrices.add(fitMatrix(item.getText(), item.getMatrix(), textHeight, dis));
        }
    }

    public ArrayList<Bitmap> getListWordBmps() {
        return mListWordBmps;
    }

    public void setListWordBmps(ArrayList<Bitmap> ListWordBmps) {
        mListWordBmps.clear();
        mListWordBmps.addAll(ListWordBmps);
    }

    public ArrayList<Matrix> getListWordMatrices() {
        return mListWordMatrices;
    }

    public void setListWordMatrices(ArrayList<Matrix> listWordMatrices) {
        mListWordMatrices.clear();
        mListWordMatrices.addAll(listWordMatrices);
    }

    public ArrayList<Matrix> getWordMatrixList() {
        return mListWordMatrices;
    }

    public Matrix fitMatrix(String text, Matrix matrix, int textHeight, float dis2Vertices) {
        Flog.d(TAG, "setMatrices=" + matrix);
        Flog.d(TAG, "textHeight=" + textHeight);
        Rect boundsWord = new Rect();
        mTextPaint.getTextBounds(text, 0, 1, boundsWord);
        int height = boundsWord.height();
        Flog.d(TAG, "curHeight=" + height);
        float scale = height * 1F / textHeight;
        Flog.d(TAG, "scale=" + scale);


        float newDistance = dis2Vertices * scale;
        Flog.d(TAG, "oldDis=" + dis2Vertices + "_newDis=" + newDistance);


        Matrix newMatrix = new Matrix(matrix);

        float values[] = new float[9];
        newMatrix.getValues(values);
        float transform_X = values[2];
        float transform_Y = values[5];

        // set new values for matrix stickers:
        float newTransform_X = transform_X * scale;
        float newTransform_Y = transform_Y * scale;

        newMatrix.preScale(scale, scale);
        Matrix concatMatrix = new Matrix();
        concatMatrix.setTranslate(newTransform_X - transform_X, newTransform_Y - transform_Y);
        newMatrix.postConcat(concatMatrix);
        return newMatrix;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setIsSelected(boolean isSelected) {
        mIsSelected = isSelected;
    }

    public boolean isInEdit(MotionEvent event) {
//        Flog.d(TAG, "isInEdit: a=" + isInWord(event) + "_b=" + isInResize(event) + "_c=" + isInButton(event, dst_flipV));
        return isInWord(event) || isInResize(event) || isInButton(event, dst_flipV);
    }

    public WordView setListener(OnWordViewListener listener) {
        this.listener = listener;
        return this;
    }

    public String getWord() {
        return mWord;
    }

    public Matrix getWordMatrix() {
        return mWordMatrix;
    }

    public void setWordMatrix(Matrix wordMatrix) {
        mWordMatrix = new Matrix(wordMatrix);
    }

    public void setInit(boolean init) {
        mInit = init;
    }

    private String[] autoSplit(String content, Paint p, float widthALine) {
        Flog.d(TAG, "autoSplit: content=" + content);
        int length = content.length();
        float textWidth = p.measureText(content);
        Flog.d(TAG, "autoSplit: textWidth=" + textWidth + "_width=" + widthALine);
        if (textWidth <= widthALine) {
            return new String[]{content};
        }

        int start = 0, end = 1, i = 0;
        int lines = (int) Math.ceil(textWidth / widthALine); //计算行数
        String[] lineTexts = new String[lines];
        while (start < length) {
            if (i >= lines)
                break;
//            Rect rect = new Rect();
//            p.getTextBounds(content, start, end, rect);
//            float widthText = rect.width();

//            if (Float.compare(widthText, widthALine) > 0) {

            if (Float.compare(p.measureText(content, start, end), widthALine) > 0) {
//            if (p.measureText(content, start, end) > widthALine) { //文本宽度超出控件宽度时
                end -= 1;
                lineTexts[i++] = (String) content.subSequence(start, end);
                start = end;
            }
            if (start < end && end == length) { //不足一行的文本
                lineTexts[i] = (String) content.subSequence(start, end);
                break;
            }
            end += 1;
        }

        lineTexts = filterNull(lineTexts);

        return lineTexts;
    }

    private String[] filterNull(String[] lineTexts) {
        List<String> list = new ArrayList<String>();

        for (String s : lineTexts) {
            if (s != null && s.length() > 0) {
                list.add(s);
            }
        }

        return list.toArray(new String[list.size()]);
    }
}
