package com.pic.libphotocollage.core.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.pic.libphotocollage.core.CollageView;
import com.pic.libphotocollage.core.R;
import com.pic.libphotocollage.core.util.Flog;


/**
 * 表情贴纸
 */
public class ItemStickerView extends BaseItem {
    public static final float INIT_SCALE = 1f;
    private static final float FIT_X_TRANSLATE = 100;
    private static final float FIT_Y_TRANSLATE = 95;
    private static final String TAG = ItemStickerView.class.getSimpleName();


    //手指移动距离必须超过这个数值

    private final long stickerId;

    //水平镜像

    private float initScale = INIT_SCALE;
    private float mScreenLayoutWidth = 720.f;
    private Paint mStickerPaint;
//    private OnDeleteStickerListenner mDeleteStickerListener;

    public ItemStickerView(CollageView collageView) {
        super(collageView);
        stickerId = 0;
        init();
    }


    @Override
    public void release() {
        Bitmap[] list = new Bitmap[]{deleteBitmap, flipVBitmap, topBitmap, resizeBitmap, mBitmap,};
        for (Bitmap item : list) {
            if (item != null) item.recycle();
        }
    }

    @Override
    public float[] getCurrentPosition() {
        return null;
    }

    private void init() {
        itemType = ItemType.STICKER;
        MAX_SCALE = 1.2f;
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
        mStickerPaint = new Paint();
        dm = mContext.getResources().getDisplayMetrics();
//        mScreenWidth = dm.widthPixels;
//        mScreenHeight = dm.heightPixels;
    }

    public void draw(Canvas canvas) {
        if (canvas == null || isDeleted) return;
        if (mBitmap != null) {

            Flog.i("drawGrid bitmap sticker");
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
            canvas.drawBitmap(mBitmap, matrix, mStickerPaint);

            //删除在右上角
            dst_delete.left = (int) (f3 - deleteBitmapWidth / 2);
            dst_delete.right = (int) (f3 + deleteBitmapWidth / 2);
            dst_delete.top = (int) (f4 - deleteBitmapHeight / 2);
            dst_delete.bottom = (int) (f4 + deleteBitmapHeight / 2);
            //拉伸等操作在右下角
            dst_resize.left = (int) (f7 - resizeBitmapWidth / 2);
            dst_resize.right = (int) (f7 + resizeBitmapWidth / 2);
            dst_resize.top = (int) (f8 - resizeBitmapHeight / 2);
            dst_resize.bottom = (int) (f8 + resizeBitmapHeight / 2);
            //垂直镜像在左上角
            dst_top.left = (int) (f1 - flipVBitmapWidth / 2);
            dst_top.right = (int) (f1 + flipVBitmapWidth / 2);
            dst_top.top = (int) (f2 - flipVBitmapHeight / 2);
            dst_top.bottom = (int) (f2 + flipVBitmapHeight / 2);
            //水平镜像在左下角
            dst_flipV.left = (int) (f5 - topBitmapWidth / 2);
            dst_flipV.right = (int) (f5 + topBitmapWidth / 2);
            dst_flipV.top = (int) (f6 - topBitmapHeight / 2);
            dst_flipV.bottom = (int) (f6 + topBitmapHeight / 2);
            if (isInEdit) {

                canvas.drawLine(f1, f2, f3, f4, localPaint);
                canvas.drawLine(f3, f4, f7, f8, localPaint);
                canvas.drawLine(f5, f6, f7, f8, localPaint);
                canvas.drawLine(f5, f6, f1, f2, localPaint);

                canvas.drawBitmap(deleteBitmap, null, dst_delete, null);
                canvas.drawBitmap(resizeBitmap, null, dst_resize, null);
                canvas.drawBitmap(flipVBitmap, null, dst_flipV, null);
//                canvas.drawBitmap(topBitmap, null, dst_top, null);
            }

            canvas.restore();
        }
    }


    public void setImageResource(int resId) {
        setBitmap(BitmapFactory.decodeResource(mContext.getResources(), resId));
    }

    public void setScreenwidth(int screenwidth, float ratio) {
//        mScreenWidth = screenwidth;
//        mScreenHeight = (int) (screenwidth * ratio);
    }

//    public int getScreenWidth() {
//        return mScreenWidth;
////    }

    public void setSavedMatrix() {
        float raio = mCollageView.getWidth() / mScreenLayoutWidth;
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        matrix.postScale(initScale * raio, initScale * raio, w / 2f, h / 2f);
        //Y坐标为 （顶部操作栏+正方形图）/2
        Matrix concatMatrix = new Matrix();
        concatMatrix.setTranslate(FIT_X_TRANSLATE, FIT_Y_TRANSLATE);
        matrix.postConcat(concatMatrix);
    }

    public void setInitScale(float initScale) {
        this.initScale = initScale;
    }

    private void setDiagonalLength() {
        halfDiagonalLength = Math.hypot(mBitmap.getWidth(), mBitmap.getHeight()) / 2;
    }

    private void initBitmaps() {
        //当图片的宽比高大时 按照宽计算 缩放大小根据图片的大小而改变 最小为图片的1/8 最大为屏幕宽
        if (mBitmap.getWidth() >= mBitmap.getHeight()) {
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
        } else {
            //当图片高比宽大时，按照图片的高计算
            float minHeight = mCollageView.getWidth() / 8;
            if (mBitmap.getHeight() < minHeight) {
                MIN_SCALE = 1f;
            } else {
                MIN_SCALE = 1.0f * minHeight / mBitmap.getHeight();
            }

            if (mBitmap.getHeight() > mCollageView.getWidth()) {
                MAX_SCALE = 1;
            } else {
                MAX_SCALE = 1.0f * mCollageView.getWidth() / mBitmap.getHeight();
            }
        }

        topBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_top_enable);
        deleteBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_delete);
        flipVBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_flip);
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

    public void setBitmap(Bitmap bitmap) {
        Flog.d("setBitmap " + bitmap);
        matrix.reset();
        mBitmap = bitmap;
        setDiagonalLength();
        initBitmaps();
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        originalWidth = w;

        //normalize icon size for fit the screen
        initScale = (float) Math.sqrt(((mCollageView.getWidth() * mCollageView.getHeight()) / 25.0f) / (w * h));

        matrix.postScale(initScale, initScale, w / 2f, h / 2f);
        matrix.postTranslate(mCollageView.getWidth() / 4f - w / 4f, (mCollageView.getWidth()) / 2f - h / 2f);
    }


    public void printTest() {
        float[] arrayOfFloat1 = new float[9];
        this.matrix.getValues(arrayOfFloat1);
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
//        Flog.i("f1=" + f1 + "_f2=" + f2 + "_f3=" + f3 + "_f4=" + f4 + "_f5=" + f5 + "_f6=" + f6 + "_f7=" + f7 + "_f8=" + f8);
    }

    public void setScreenLayoutWidth(float layoutWidth) {
        mScreenLayoutWidth = layoutWidth;
    }

    public void setStickerPaint(int opacity) {
        mStickerPaint.setAlpha(opacity);
    }

    public int getOpacity() {
        return mStickerPaint.getAlpha();
    }


}
