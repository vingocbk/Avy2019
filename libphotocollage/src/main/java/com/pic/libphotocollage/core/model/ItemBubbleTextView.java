package com.pic.libphotocollage.core.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;

import com.pic.libphotocollage.core.CollageView;
import com.pic.libphotocollage.core.R;
import com.pic.libphotocollage.core.util.Flog;

/**
 * Created by Abner on 15/6/7.
 * QQ 230877476
 * Email nimengbo@gmail.com
 */
public class ItemBubbleTextView extends BaseItem implements TextWatcher {

    //最大最小字号
    public static final int MAX_FONT_SIZE = 64;
    public static final int MIN_FONT_SIZE = 14;
    //字号默认18sp
    public static final int M_DEFULT_SIZE = 22;
    private static final String TAG = "ItemBubbleTextView";

    /**
     * 文字部分
     */
    private static final String defaultStr = "Enter text";
    //字离旁边的距离
    private final float mDefaultMargin = 20;
    private final int fontColor = Color.WHITE;
    private final long doubleClickTimeLimit = 200;
    //    private Typeface fontType = null;
    boolean isInit = false;
    private Bitmap originBitmap;

    //显示的字符串
    private String mStr = "";
    private float mFontSize = 16;
    private float mMargin = 20;
    //绘制文字的画笔
    private TextPaint mFontPaint;
    private Canvas canvasText;

    private float realTextHeight;

    private boolean isEdited = false;
    private String s;
    //    private OnBubbleTextViewListener listener = null;
    private int mIdResRootView = -1;
    private float mPaddingRect = 0f;
    private float leftMargin;
    private float topMargin;
    private int mPaddingText = 0;



    public ItemBubbleTextView(CollageView collageView) {
        super(collageView);
//        defaultStr = getContext().getString(R.string.double_click_input_text);
        init();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

//    public OnBubbleTextViewListener getListener() {
//        return listener;
//    }

    public void setPatternType(Shader shader) {
        mFontPaint.setShader(shader);
        initSize();
    }

    public void setFontType(Typeface fontType) {
//        this.fontType = fontType;
        mFontPaint.setTypeface(fontType);
        initSize();
    }

//    public ItemBubbleTextView setBubbleListener(OnBubbleTextViewListener listener) {
//        this.listener = listener;
//        return this;
//    }

    private void init() {
        itemType = ItemType.TEXT;
        dm = mContext.getResources().getDisplayMetrics();
        dst_delete = new Rect();
        dst_resize = new Rect();
        dst_flipV = new Rect();
        dst_top = new Rect();
        localPaint = new Paint();
        localPaint.setColor(mContext.getResources().getColor(R.color.red_e73a3d));
        localPaint.setAntiAlias(true);
        localPaint.setDither(true);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(2.0f);
//        mScreenWidth = dm.widthPixels;
//        mScreenHeight = dm.heightPixels;
        mFontSize = M_DEFULT_SIZE;
        mFontPaint = new TextPaint();
        mFontPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mFontSize, dm));
        mFontPaint.setColor(fontColor);
        mFontPaint.setTextAlign(Paint.Align.CENTER);
        mFontPaint.setAntiAlias(true);
        mFontPaint.setTextAlign(Paint.Align.LEFT);
//        mFontPaint.setLinearText(true);
        mCollageView.registerKeyboardEvent(this);
        allocateBitmaps();
        leftMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, dm);
        topMargin = leftMargin;
        initSize();
        isInit = true;
    }

    private void initSize() {
        if (mBitmap != null) mBitmap.recycle();
        Paint.FontMetrics fm = mFontPaint.getFontMetrics();
        realTextHeight = Math.abs(fm.top - fm.bottom);
        String curText = TextUtils.isEmpty(this.getText()) ? defaultStr : this.getText();
        float measureTextWidth = mFontPaint.measureText(curText);
        measureTextWidth = Math.min(measureTextWidth, 14.0f * mCollageView.getWidth() / 16);
        int numLines = autoSplit(curText, mFontPaint, measureTextWidth).length;
        int height = (int) (numLines * realTextHeight + (numLines - 1) * (Math.abs(mFontPaint.getFontMetrics().leading) + mPaddingText) + 2 * topMargin);
        Bitmap bitmap = Bitmap.createBitmap((int) (measureTextWidth + leftMargin + deleteBitmapWidth), height, Bitmap.Config.ARGB_4444);
        setBitmap(bitmap);
    }

    public void draw(Canvas canvas) {
        if (mBitmap != null && !isDeleted) {

            float[] arrayOfFloat = new float[9];
            matrix.getValues(arrayOfFloat);
            float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            float f5 = 0.0F * arrayOfFloat[0] + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            float f6 = 0.0F * arrayOfFloat[3] + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
            float f7 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            float f8 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];


            canvas.save();

            //先往文字上绘图
            mBitmap = originBitmap.copy(Bitmap.Config.ARGB_8888, true);
            canvasText.setBitmap(mBitmap);
            canvasText.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            // khanh comment due to Hoa's fixxing
//            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            float scalex = arrayOfFloat[Matrix.MSCALE_X];
            float skewy = arrayOfFloat[Matrix.MSKEW_Y];
            float skewx = arrayOfFloat[Matrix.MSKEW_X];
            float rScale = (float) Math.sqrt(scalex * scalex + skewy * skewy);

            mFontPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mFontSize, dm));
            String[] texts = autoSplit(TextUtils.isEmpty(mStr) ? defaultStr : mStr, mFontPaint, mBitmap.getWidth() - leftMargin - deleteBitmapWidth);
            Paint.FontMetrics fm = mFontPaint.getFontMetrics();
            float top = Math.abs(fm.top) + topMargin;
            //基于底线开始画的
            for (String text : texts) {
                if (TextUtils.isEmpty(text)) {
                    continue;
                }
                canvasText.drawText(text, leftMargin, top, mFontPaint);
                top += realTextHeight + mFontPaint.getFontMetrics().leading + mPaddingText; //添加字体行间距
            }
            canvas.drawBitmap(mBitmap, matrix, null);

            //删除在右上角w
            dst_delete.left = (int) (f3 + mPaddingRect - deleteBitmapWidth / 2);
            dst_delete.right = (int) (f3 + mPaddingRect + deleteBitmapWidth / 2);
            dst_delete.top = (int) (f4 - mPaddingRect - deleteBitmapHeight / 2);
            dst_delete.bottom = (int) (f4 - mPaddingRect + deleteBitmapHeight / 2);
            //拉伸等操作在右下角
            dst_resize.left = (int) (f7 + mPaddingRect - resizeBitmapWidth / 2);
            dst_resize.right = (int) (f7 + mPaddingRect + resizeBitmapWidth / 2);
            dst_resize.top = (int) (f8 + mPaddingRect - resizeBitmapHeight / 2);
            dst_resize.bottom = (int) (f8 + mPaddingRect + resizeBitmapHeight / 2);

            if (isInEdit) {
                canvas.drawLine(f1 - mPaddingRect, f2 - mPaddingRect, f3 + mPaddingRect, f4 - mPaddingRect, localPaint);
                canvas.drawLine(f3 + mPaddingRect, f4 - mPaddingRect, f7 + mPaddingRect, f8 + mPaddingRect, localPaint);
                canvas.drawLine(f5 - mPaddingRect, f6 + mPaddingRect, f7 + mPaddingRect, f8 + mPaddingRect, localPaint);
                canvas.drawLine(f5 - mPaddingRect, f6 + mPaddingRect, f1 - mPaddingRect, f2 - mPaddingRect, localPaint);

                canvas.drawBitmap(deleteBitmap, null, dst_delete, null);
                canvas.drawBitmap(resizeBitmap, null, dst_resize, null);
            }

            canvas.restore();
        }
    }

    public void setImageResource(int resId) {
        matrix.reset();
        //使用拷贝 不然会对资源文件进行引用而修改
        setBitmap(BitmapFactory.decodeResource(mContext.getResources(), resId));
    }

    public void setBitmap(Bitmap bitmap) {
//        mFontSize = M_DEFULT_SIZE;
        originBitmap = bitmap;
        mBitmap = originBitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvasText = new Canvas(mBitmap);

        setDiagonalLength();
        initBitmaps();
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        originalWidth = w;
        if (!isInit) {
            matrix.postTranslate(5, (mCollageView.getWidth()) / 3 - h);
        } else {
//            matrix.setTranslate(mScreenWidth / 2 - w / 2, 0);
        }
        mCollageView.invalidate();
    }

    private void setDiagonalLength() {
        halfDiagonalLength = Math.hypot(mBitmap.getWidth(), mBitmap.getHeight()) / 2;
    }

    private void allocateBitmaps() {
        if (topBitmap == null)
            topBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_top_enable);
        if (deleteBitmap == null)
            deleteBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_delete);
        if (flipVBitmap == null)
            flipVBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_flip);
        if (resizeBitmap == null)
            resizeBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_resize);

        deleteBitmapWidth = (int) (deleteBitmap.getWidth() * BITMAP_SCALE);
        deleteBitmapHeight = (int) (deleteBitmap.getHeight() * BITMAP_SCALE);

        resizeBitmapWidth = (int) (resizeBitmap.getWidth() * BITMAP_SCALE);
        resizeBitmapHeight = (int) (resizeBitmap.getHeight() * BITMAP_SCALE);

        flipVBitmapWidth = (int) (flipVBitmap.getWidth() * BITMAP_SCALE);
        flipVBitmapHeight = (int) (flipVBitmap.getHeight() * BITMAP_SCALE);

        topBitmapWidth = (int) (topBitmap.getWidth() * BITMAP_SCALE);
        topBitmapHeight = (int) (topBitmap.getHeight() * BITMAP_SCALE);
    }

    private void initBitmaps() {
        float minWidth = mCollageView.getWidth() / 8;
        if (mBitmap.getWidth() < minWidth) {
            MIN_SCALE = 1f;
        } else {
            MIN_SCALE = 1.0f * minWidth / mBitmap.getWidth();
        }

        if (mBitmap.getWidth() > mCollageView.getWidth()) {
            MAX_SCALE = 1;
        } else {
            MAX_SCALE = 1.0f * mCollageView.getWidth() / mBitmap.getWidth();
        }
    }

    public float getTextSize() {
        return mFontSize;
    }

    public void setTextSize(int unit) {
        mFontSize = unit;
        mFontPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mFontSize, dm));
        initSize();
    }

    public TextPaint getTextFont() {
        return mFontPaint;
    }


    public float getPadding() {
        return mPaddingText;
    }

    private String[] autoSplit(String content, Paint p, float width) {
        int length = content.length();
        float textWidth = p.measureText(content);
        if (textWidth <= width) {
            return new String[]{content};
        }

        int start = 0, end = 1, i = 0;
        int lines = (int) Math.ceil(textWidth / width); //计算行数
        String[] lineTexts = new String[lines];
        while (start < length) {

            if (i >= lines)
                break;
            if (p.measureText(content, start, end) > width) { //文本宽度超出控件宽度时
                lineTexts[i++] = (String) content.subSequence(start, end);
                start = end;
            }
            if (start <= end && end == length) { //不足一行的文本
                lineTexts[i] = (String) content.subSequence(start, end);
                break;
            }
            end += 1;

        }
        return lineTexts;
    }

    public String getText() {
        return mStr;
    }

    public void setText(String text) {
        Log.d(TAG, "setText = " + text);
        mStr = text;
        initSize();
    }

    public void setTextColor(int color) {
        mFontPaint.setColor(color);
    }

    public void setIdResRootView(int idResRootView) {
        mIdResRootView = idResRootView;
    }

    public void setPaddingText(int padding) {
        Flog.i("mPaddingRect: " + padding);
//        mPaddingRect = padding;
        mPaddingText = padding;
        initSize();
    }


    @Override
    public void release() {
        Bitmap[] list = new Bitmap[]{mBitmap, deleteBitmap, flipVBitmap, topBitmap, resizeBitmap,
                originBitmap};
        for (Bitmap item : list) {
            if (item != null) item.recycle();
        }
    }

    @Override
    public float[] getCurrentPosition() {
        float[] arrayOfFloat1 = new float[9];
        this.matrix.getValues(arrayOfFloat1);

        //左上角
        float f1 = 0.0F * arrayOfFloat1[0] + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
        float f6 = 0.0F * arrayOfFloat1[3] + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];

        return new float[]{f1, f6};
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setInEdit(true);
        setText(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        mCollageView.invalidate();
    }

//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        setText(s.toString());
//        setInEdit(true);
//    }

}
