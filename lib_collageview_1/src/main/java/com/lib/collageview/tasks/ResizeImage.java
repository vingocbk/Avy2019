package com.lib.collageview.tasks;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.lib.collageview.helpers.Flog;

import java.io.FileInputStream;
import java.io.IOException;


/**
 * Created by Duc on 9/7/2016.
 */
public class ResizeImage {
    private static final String TAG = ResizeImage.class.getSimpleName();
    float orientation = 0.0f;
    private Context context;
    private int imageHeight;
    private int imageWidth;
    private int mTextureMaxSize;
    private boolean mOutOfMemoryError = false;

    public ResizeImage(Context applicationContext, int textureMaxSize) {
        this.context = applicationContext;
        mTextureMaxSize = textureMaxSize;
    }

    public Bitmap getBitmap(String imagePath, int widthPixels) {
        this.orientation = getImageOrientation(imagePath);
        getAspectRatio(imagePath, widthPixels);
        return getResizedOriginalBitmap(imagePath, this.imageWidth, this.imageHeight);
    }

    public Bitmap getBitmap(Bitmap orgBitmap, int desiredWidth) {
        int desiredHeight = orgBitmap.getHeight() * desiredWidth / orgBitmap.getWidth();
        int orgWidth = orgBitmap.getWidth();
        int orgHeight = orgBitmap.getHeight();
        float desiredWidthScale = ((float) desiredWidth) / ((float) orgWidth);
        float desiredHeightScale = ((float) desiredHeight) / ((float) orgHeight);
        Matrix matrix = new Matrix();
        matrix.postScale(desiredWidthScale, desiredHeightScale);
        Flog.d(TAG, "ori: w=" + orgWidth + "_h=" + orgHeight);
        return Bitmap.createBitmap(orgBitmap, 0, 0, orgWidth, orgHeight, matrix, true);
    }

    private void getAspectRatio(String selectedImagePath, int widthPixels) {
        float scaleWidth;
        float scaleHeight;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        Flog.d(TAG, "decodeStream 0: w=" + options.outWidth + "_h=" + options.outHeight);
        float scaleFactor = ((float) options.outWidth) / ((float) options.outHeight);

        scaleHeight = (float) widthPixels;
        scaleWidth = scaleHeight * scaleFactor;
        this.imageWidth = (int) scaleWidth;
        this.imageHeight = (int) scaleHeight;
    }

    private Bitmap getResizedOriginalBitmap(String imagePath, int imagwidth, int imageheight) {
        try {
            mOutOfMemoryError = false;

            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(imagePath), null, options);
            int srcWidth = options.outWidth;
            int srcHeight = options.outHeight;
            Flog.d(TAG, "decodeStream 1: w=" + options.outWidth + "_h=" + options.outHeight);

            int desiredWidth = imagwidth;
            int desiredHeight = imageheight;
            Flog.d(TAG, "decodeStream 11: w=" + desiredWidth + "_h=" + desiredHeight);
            desiredWidth = (desiredWidth > mTextureMaxSize ? mTextureMaxSize : desiredWidth);
            desiredHeight = (desiredHeight > mTextureMaxSize ? mTextureMaxSize : desiredHeight);
            Flog.d(TAG, "decodeStream 12: w=" + desiredWidth + "_h=" + desiredHeight);

            int inSampleSize = 1;
            while (srcWidth / 2 > desiredWidth) {
                srcWidth /= 2;
                srcHeight /= 2;
                inSampleSize *= 2;
            }
            Flog.d(TAG, "decodeStream 13: w=" + desiredWidth + "_h=" + desiredHeight + "_inSampleSize=" + inSampleSize);
            float desiredWidthScale = ((float) desiredWidth) / ((float) srcWidth);
            float desiredHeightScale = ((float) desiredHeight) / ((float) srcHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inSampleSize = inSampleSize;
            options.inScaled = false;
            options.inPreferredConfig = Config.ARGB_8888;
            Bitmap sampledSrcBitmap = BitmapFactory.decodeStream(new FileInputStream(imagePath), null, options);
            Flog.d(TAG, "decodeStream 2: w=" + options.outWidth + "_h=" + options.outHeight);
            if (sampledSrcBitmap == null) {
                return null;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(desiredWidthScale, desiredHeightScale);
            matrix.postRotate(this.orientation);
            int widthBmp = sampledSrcBitmap.getWidth();
            int heightBmp = sampledSrcBitmap.getHeight();
            Flog.d(TAG, "sampled: w=" + widthBmp + "_h=" + heightBmp + "_MAX_SIZE=" + mTextureMaxSize);
            widthBmp = (widthBmp > mTextureMaxSize ? mTextureMaxSize : widthBmp);
            heightBmp = (heightBmp > mTextureMaxSize ? mTextureMaxSize : heightBmp);
            Flog.d(TAG, "sampled: w=" + widthBmp + "_h=" + heightBmp + "___");
            Bitmap rslt = Bitmap.createBitmap(sampledSrcBitmap, 0, 0, widthBmp, heightBmp, matrix, true);
            return rslt;
        } catch (Exception e) {
            e.printStackTrace();
            Flog.d(TAG, "Exception 1");
            return null;
        } catch (OutOfMemoryError ex) {
            Flog.d(TAG, "OutOfMemoryError 2");
            mOutOfMemoryError = true;
            return null;
        }
    }

    private float getImageOrientation(String static_image) {
        try {
            int orientation = new ExifInterface(static_image).getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return 90.0f;
            }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                return 180.0f;
            }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                return 270.0f;
            }
            return 0.0f;
        } catch (IOException e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public boolean isOutOfMemoryError() {
        return mOutOfMemoryError;
    }
}