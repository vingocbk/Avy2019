package com.lib.collageview.stickers.text;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import com.lib.collageview.CollageView;
import com.lib.collageview.R;
import com.lib.collageview.helpers.Flog;
import com.lib.collageview.helpers.bitmap.BitmapHelper;
import com.lib.collageview.stickers.BaseStickerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by vutha on 3/29/2017.
 */

public class TextStickerView extends BaseStickerView {

    public static final int MAX_TEXT_SIZE = 50;
    public static final int MIN_TEXT_SIZE = 12;
    public static final int MAX_TEXT_PADDING = 100;
    public static final int MIN_TEXT_PADDING = 0;
    public static final int TEXT_ALIGN_LEFT = 0x11;
    public static final int TEXT_ALIGN_CENTER = 0x12;
    public static final int TEXT_ALIGN_RIGHT = 0x13;
    private static final String TAG = TextStickerView.class.getSimpleName();
    public static int DEFAULT_PROGRESS_TEXT_SIZE;
    private String mDefaultText;
    private String mText;
    private int mTextColor;
    private TextPaint mTextFont;
    private Canvas mTextCanvas;
    private boolean mIsInit;
    private int mScreenwidth, mScreenHeight;
    /**
     * Margin both: from left and from right.
     */
    private int mTextMargin;
    private int mTextMaxWidth;
    private String[] mTextLines;
    private ArrayList<String> mLines = new ArrayList<>();
    private Bitmap mPatternBmp;
    private DisplayMetrics mDisplayMetrics;
    /**
     * The progress value of seekbar for size and padding of the text sticker.
     */
    private int mProgressSize = 0;
    private int mTextPadding = 0;
    /**
     * The type of align of text.
     */
    private int mTextAlign = TEXT_ALIGN_LEFT;

    public TextStickerView(CollageView collageView, int stickerIndex) {
        super(collageView);
        mIndex = stickerIndex;
        mStickerType = STICKER_TEXT;
        inits();
    }

    public TextStickerView(CollageView collageView) {
        super(collageView);
        mStickerType = STICKER_TEXT;
        inits();
    }

    private void inits() {

        DEFAULT_PROGRESS_TEXT_SIZE = (int) mContext.getResources().getDimension(R.dimen.default_sticker_text_size);
        mProgressSize = DEFAULT_PROGRESS_TEXT_SIZE;
        mDefaultText = mContext.getString(R.string.input_text);
        mTextColor = Color.BLACK;
        mText = "";

        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        mScreenwidth = mDisplayMetrics.widthPixels;
        mScreenHeight = mDisplayMetrics.heightPixels;

        mTextFont = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        mTextFont.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_PROGRESS_TEXT_SIZE, mDisplayMetrics));
        mTextFont.setColor(mTextColor);

        mTextMargin = 20;
        mTextMaxWidth = mScreenwidth * 8 / 9;
        mTextLines = new String[]{};

        mIsInit = true;

//        resizeTextCanvas();
    }

    /**
     * When size of text is changed, resizing textCanvas for drawing text on it.
     */
    private void resizeTextCanvas() {

//        autoSplit(mText, mTextFont, mTextMaxWidth - mTextMargin);

        mTextLines = separateText(mText);
        mLines.clear();
        for (int i = 0; i < mTextLines.length; i++) {
            String[] pLines = autoSplit(mTextLines[i], mTextFont, mTextMaxWidth - mTextMargin);
            if (pLines.length <= 0) {
                continue;
            } else if (pLines.length == 1) {
                mLines.add(mTextLines[i]);
            } else {
                for (int j = 0; j < pLines.length; j++) {
                    mLines.add(pLines[j]);
                }
            }
        }

        Flog.d(TAG, "text size=" + mLines.size());
        for (int i = 0; i < mLines.size(); i++) {
            Flog.d(TAG, "text at " + i + " : " + mLines.get(i));
        }

        String curText = TextUtils.isEmpty(mText) ? mDefaultText : mText;

        /**
         * Get dimension(s) of text. There are two ways:
         * 1- Using Font.getTextBounds(str, 0, str.length(), rect) -> get width & height of text.
         * 2- Using Font.measureText(str) -> get width of text.
         * */
//        Rect boundRect = new Rect();
//        mTextFont.getTextBounds(curText, 0, curText.length(), boundRect);
//        Flog.d(TAG, "mTextLines len=" + mTextLines.length);
//        Flog.d(TAG, "height text = " + (mTextPadding * mTextLines.length));
        Paint.FontMetrics fontMetrics = mTextFont.getFontMetrics();
//        float additionalHeight = (mTextLines.length - 1) * (Math.abs(mTextFont.getFontMetrics().leading + mTextPadding));
        float additionalHeight = (mLines.size() - 1) * (Math.abs(mTextFont.getFontMetrics().leading + mTextPadding));
//        Flog.d(TAG, "additionalHeight=" + additionalHeight);
//        float heightText = (fontMetrics.descent - fontMetrics.ascent) * mTextLines.length + additionalHeight;
        float heightText = (fontMetrics.descent - fontMetrics.ascent) * mLines.size() + additionalHeight;
//        Flog.d(TAG, "width 1="+mTextFont.measureText(curText)+"_2="+boundRect.width());
        int widthText = (int) mTextFont.measureText(curText);
//        Flog.d(TAG, "size text: w=" + widthText + "_h=" + heightText + "_mTextMaxWidth=" + mTextMaxWidth);
        if (widthText >= mTextMaxWidth)
            widthText = mTextMaxWidth + mTextMargin;
//        Flog.d(TAG, "mCollageView.getHeight()=" + mCollageView.getHeight());
        if (heightText > mCollageView.getHeight()) {
//            heightText = mCollageView.getHeight();
        }

        if (widthText <= 0 || heightText <= 0) return;
        try {
            Bitmap bmpText = Bitmap.createBitmap(widthText, Math.round(heightText), Bitmap.Config.ARGB_4444);
            Flog.d(TAG, "bmp w=" + bmpText.getWidth() + "_h=" + bmpText.getHeight());
            setBitmap(bmpText);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
    }

    private String[] separateText(String text) {
        return text.split("\n");
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        Flog.d(TAG, "setText text=" + text + "\nlen=" + text.length());
        mText = text;
        resizeTextCanvas();
    }

    private String[] autoSplit(String content, Paint p, float width) {
        Flog.d(TAG, "autoSplit: content=" + content);
        int length = content.length();
        float textWidth = p.measureText(content);
        Flog.d(TAG, "autoSplit: textWidth=" + textWidth + "_width=" + width);
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
            if (start < end && end == length) { //不足一行的文本
                lineTexts[i] = (String) content.subSequence(start, end);
                break;
            }
            end += 1;
        }
        return lineTexts;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mBitmap == null || mBitmap.isRecycled() || mMatrix == null) return;

        if (mTextFont != null && mTextFont.getShader() != null) {
            mTextFont.getShader().setLocalMatrix(mMatrix);
        }

        Paint.FontMetrics fontMetrics = mTextFont.getFontMetrics();
        /* ref: http://wikiwiki.jp/android/?%A5%C6%A5%AD%A5%B9%A5%C8%A4%CE%C9%C1%B2%E8(FontMetrics) */
//        Flog.d(TAG, "fontMetrics: ascent=" + fontMetrics.ascent + "_descent=" + fontMetrics.descent);
//        Flog.d(TAG, "mTextLines=" + mTextLines.length + "_leading=" + mTextFont.getFontMetrics().leading);

        float distance = (fontMetrics.descent - fontMetrics.ascent) + mTextPadding;
//        Flog.d(TAG, "distance="+distance+"_x="+mTextFont.measureText(mText)/2);
        float paddingTop = 0;
        for (int i = 0; i < mLines.size(); i++) {
            String text = mLines.get(i);
            if (text == null)
                text = "";
//            Flog.d(TAG, "text=" + text);
            if (TextUtils.isEmpty(text)) {
//                continue;
            }
//            Flog.d(TAG, "paddingTop=" + paddingTop);
//            float centerX = mTextFont.measureText(text) / 2;
            float centerY = (fontMetrics.descent + fontMetrics.ascent) / 2 + distance * (mLines.size() - 1) / 2;
//                mTextCanvas.drawText(text, (mBitmap.getWidth() >> 1) - centerX, (mBitmap.getHeight() >> 1) - centerY + paddingTop, mTextFont);
            float startX = getStartXAlign(text);
            Flog.d(TAG, "startX=" + startX);
            if (startX < 0) {
                startX = 0;
            }
            mTextCanvas.drawText(text, startX, (mBitmap.getHeight() >> 1) - centerY + paddingTop, mTextFont);
            paddingTop += distance;
        }

        canvas.drawBitmap(mBitmap, mMatrix, mPaint);

        if (isInEdit) {

            mMatrix.getValues(arrayOfFloat);
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

            canvas.drawLine(f1, f2, f3, f4, mLinePaint);
            canvas.drawLine(f3, f4, f7, f8, mLinePaint);
            canvas.drawLine(f5, f6, f7, f8, mLinePaint);
            canvas.drawLine(f5, f6, f1, f2, mLinePaint);

            canvas.drawBitmap(topBitmap, null, dst_top, null);
            canvas.drawBitmap(deleteBitmap, null, dst_delete, null);
            canvas.drawBitmap(resizeBitmap, null, dst_resize, null);
            canvas.drawBitmap(flipVBitmap, null, dst_flipV, null);
        }
    }

    private float getStartXAlign(String text) {
        if (text == null || text.isEmpty())
            return 0;
        switch (mTextAlign) {
            case TEXT_ALIGN_CENTER:
                return (mBitmap.getWidth() >> 1) - mTextFont.measureText(text) / 2;
            case TEXT_ALIGN_RIGHT:
                return mBitmap.getWidth() - mTextFont.measureText(text);
            default:
                return 0;
        }
    }

    @Override
    public void setBitmap(Bitmap bitmap) {

        mBitmap = BitmapHelper.recycle(mBitmap);
        mBitmap = bitmap;
        Flog.d(TAG, "bmp: w=" + mBitmap.getWidth() + "_h=" + mBitmap.getHeight());
        mTextCanvas = new Canvas(mBitmap);
        mTextCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        setDiagonalLength();
        initScaleLimit();

//        if (mIsInit) {
//            Flog.d(TAG, "heightScreen="+mScreenwidth+"_height bmp="+mBitmap.getHeight());
//            Flog.d(TAG, "mInitalTransY="+mInitalTransY);
//            mMatrix.postTranslate(mTextMargin, mInitalTransY);
//            mIsInit = false;
//        }
        mCollageView.invalidate();
    }

    public void setTextAlign(int textAlign) {
        mTextAlign = textAlign;
        resizeTextCanvas();
    }

    /**
     * Translate text sticker at position above tablayout.
     *
     * @param heightTabLayout the height of tablayout that is below text sticker.
     */
    public void setTranslateInit(float heightTabLayout) {
        if (mIsInit) {
            float[] arrayOfFloat = new float[9];
            mMatrix.getValues(arrayOfFloat);
            float f4 = arrayOfFloat[3] * mBitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            float f8 = arrayOfFloat[3] * mBitmap.getWidth() + arrayOfFloat[4] * mBitmap.getHeight() + arrayOfFloat[5];

            float heightTextSticker = (f8 + resizeBitmapHeight / 2) - (f4 - deleteBitmapHeight / 2);
            mMatrix.setTranslate(mTextMargin, mScreenHeight - heightTabLayout - heightTextSticker - (mScreenHeight / 2 - mCollageView.getHeight() / 2));
            Log.d(TAG, "heightTextSticker=" + heightTextSticker + "_heightTabLayout=" + heightTabLayout + "_mScreenHeight=" + mScreenHeight);
            mCollageView.invalidate();
            mIsInit = false;
        }
    }

    public TextPaint getTextFont() {
        return mTextFont;
    }

    public void setTextFont(Typeface fontType) {
        mTextFont.setTypeface(fontType);
        resizeTextCanvas();
    }

    public int getTextPadding() {
        return mTextPadding;
    }

    public void setTextPadding(int textPadding) {
        if (textPadding < MIN_TEXT_PADDING || textPadding > MAX_TEXT_PADDING) {
            return;
        }
        mTextPadding = textPadding;
        resizeTextCanvas();
    }

    public void setTextColor(@ColorInt int color) {
        if (mTextFont.getShader() != null)
            mTextFont.setShader(null);
        mTextFont.setColor(color);
        mCollageView.invalidate();
    }

    public void setTextPattern(Context context, String bgName) {
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open("bg/" + bgName);
            mPatternBmp = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Shader shader = new BitmapShader(mPatternBmp,
                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        shader.setLocalMatrix(mMatrix);
        mTextFont.setShader(shader);
        mCollageView.invalidate();
    }

    public void setTextPattern(Bitmap bmp) {
        Shader shader = new BitmapShader(bmp,
                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        shader.setLocalMatrix(mMatrix);
        mTextFont.setShader(shader);
        mCollageView.invalidate();
    }

    public float getTextSize() {
        return mTextFont.getTextSize();
    }

    public void setTextSize(int progressSize) {
        mProgressSize = progressSize;
        Flog.d(TAG, "setTextSize size=" + progressSize);

//        int widthText = ((int) mTextFont.measureText(mText) + mTextMargin);
//        if ((overlaps(dst_resize, dst_delete)&&size<mTextFont.getTextSize()) || (widthText >= mTextMaxWidth)&&(size>mTextFont.getTextSize())) {
//            Flog.d(TAG, "the text size exceeds limit.");
//            return;
//        }
        mTextFont.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, progressSize, mDisplayMetrics));
        resizeTextCanvas();
    }

    @Override
    public void release() {
        super.release();
        if (mTextFont != null) {
            mTextFont.reset();
            mTextFont = null;
        }
        if (mTextCanvas != null) {
            mTextCanvas = null;
        }
        if (mPatternBmp != null) {
            mPatternBmp = BitmapHelper.recycle(mPatternBmp);
        }
        if (mDisplayMetrics != null) {
            mDisplayMetrics = null;
        }
        if (mLines != null) {
            mLines.clear();
            mLines = null;
        }
    }

    public boolean overlaps(Rect r1, Rect r2) {
        return r1.left < r2.left + r2.width() && r1.left + r1.width() > r2.left
                && r1.top < r2.top + r2.height() && r1.top + r1.height() > r2.top;
    }

    public int getProgressSize() {
        return mProgressSize;
    }
}
