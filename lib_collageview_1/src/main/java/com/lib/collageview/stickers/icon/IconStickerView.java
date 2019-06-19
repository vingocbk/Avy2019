package com.lib.collageview.stickers.icon;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.lib.collageview.CollageView;
import com.lib.collageview.stickers.BaseStickerView;

/**
 * Created by vutha on 3/29/2017.
 */

public class IconStickerView extends BaseStickerView {

    public IconStickerView(CollageView collageView, int stickerIndex) {
        super(collageView);
        mIndex = stickerIndex;
        mStickerType = STICKER_ICON;
        init();
    }

    public IconStickerView(CollageView collageView) {
        super(collageView);
        mStickerType = STICKER_ICON;
        init();
    }

    private void init() {
        maxScale = 1.2f;
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        if (mMatrix == null || bitmap == null || mCollageView == null) return;
        mMatrix.reset();
        mBitmap = bitmap;
        setDiagonalLength();
        initScaleLimit();
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();

        //normalize icon size for fit the screen
        float scaleVal = (float) Math.sqrt(((mCollageView.getWidth() * mCollageView.getHeight()) / 25.0f) / (w * h));

        mMatrix.postScale(scaleVal, scaleVal, w / 2f, h / 2f);
        mMatrix.postTranslate(mCollageView.getWidth() / 4f - w / 4f, (mCollageView.getWidth()) / 2f - h / 2f);
        mCollageView.invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mBitmap == null || mMatrix == null) return;
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        if (isInEdit) {
            if (dst_delete == null || dst_resize == null || dst_flipV == null || dst_top == null
                    || deleteBitmap == null || resizeBitmap == null || flipVBitmap == null || topBitmap == null)
                return;

            mMatrix.getValues(arrayOfFloat);
            float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            float f5 = 0.0F * arrayOfFloat[0] + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            float f6 = 0.0F * arrayOfFloat[3] + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
            float f7 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            float f8 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];


            //删除在右上角
            dst_delete.left = (int) (f3 - (deleteBitmapWidth >> 1));
            dst_delete.right = (int) (f3 + (deleteBitmapWidth >> 1));
            dst_delete.top = (int) (f4 - (deleteBitmapHeight >> 1));
            dst_delete.bottom = (int) (f4 + (deleteBitmapHeight >> 1));
            //拉伸等操作在右下角
            dst_resize.left = (int) (f7 - (resizeBitmapWidth >> 1));
            dst_resize.right = (int) (f7 + (resizeBitmapWidth >> 1));
            dst_resize.top = (int) (f8 - (resizeBitmapHeight >> 1));
            dst_resize.bottom = (int) (f8 + (resizeBitmapHeight >> 1));
            //垂直镜像在左上角
            dst_top.left = (int) (f1 - (flipVBitmapWidth >> 1));
            dst_top.right = (int) (f1 + (flipVBitmapWidth >> 1));
            dst_top.top = (int) (f2 - (flipVBitmapHeight >> 1));
            dst_top.bottom = (int) (f2 + (flipVBitmapHeight >> 1));
            //水平镜像在左下角
            dst_flipV.left = (int) (f5 - (topBitmapWidth >> 1));
            dst_flipV.right = (int) (f5 + (topBitmapWidth >> 1));
            dst_flipV.top = (int) (f6 - (topBitmapHeight >> 1));
            dst_flipV.bottom = (int) (f6 + (topBitmapHeight >> 1));

            canvas.drawLine(f1, f2, f3, f4, mLinePaint);
            canvas.drawLine(f3, f4, f7, f8, mLinePaint);
            canvas.drawLine(f5, f6, f7, f8, mLinePaint);
            canvas.drawLine(f5, f6, f1, f2, mLinePaint);

            canvas.drawBitmap(deleteBitmap, null, dst_delete, null);
            canvas.drawBitmap(resizeBitmap, null, dst_resize, null);
            canvas.drawBitmap(flipVBitmap, null, dst_flipV, null);
//                canvas.drawBitmap(topBitmap, null, dst_top, null);
        }
    }

    @Override
    public void release() {
        super.release();
    }
}
