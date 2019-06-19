package com.lib.collageview.helpers.bitmap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vutha on 3/22/2017.
 */

public class BitmapHelper {
    public static Bitmap recycle(Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = null;
        }
        return null;
    }

    synchronized public static Bitmap loadBitmapFromAssets(Context context, String fullPath) {
        Bitmap frameBitmap = null;

        String imagePath = fullPath;
        AssetManager assetMng = context.getAssets();

        // Create an input stream to read from the asset folder
        InputStream is = null;
        try {
            is = assetMng.open(imagePath);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        //Get the texture from the Android resource directory
        //InputStream is = context.getResources().openRawResource(R.drawable.radiocd5);
        if (frameBitmap != null) {
            frameBitmap.recycle();
            frameBitmap = null;
        }
        try {
            //BitmapFactory is an Android graphics utility for images
            frameBitmap = BitmapFactory.decodeStream(is);

        } finally {
            //Always clear and close
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return frameBitmap;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static Bitmap correctRotateBmp(String path, Bitmap sourceBitmap) {
        try {
            ExifInterface exif = new ExifInterface(path);
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotation != 0f) {
                matrix.preRotate(rotationInDegrees);
            } else {
                return sourceBitmap;
            }
            if (sourceBitmap == null)
                return sourceBitmap;
            return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
