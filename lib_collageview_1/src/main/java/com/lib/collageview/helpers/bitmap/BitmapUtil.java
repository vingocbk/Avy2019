package com.lib.collageview.helpers.bitmap;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;

import com.lib.collageview.helpers.Flog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class BitmapUtil {
    private static final String TAG = BitmapUtil.class.getSimpleName();

    public static Bitmap getImage(String path, int reqWidth) {
        String filePath = path;
        if (filePath != null) {
            Bitmap bitmap = BitmapUtil.sampeZoomFromFile(filePath, reqWidth);
            int angle = (int) getImageOrientation(filePath);
            if (angle == 0) {
                return bitmap;
            }
            Matrix m = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            m.setRotate((float) angle);
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
            if (bitmap == newBitmap || bitmap == null || bitmap.isRecycled()) {
                return newBitmap;
            }
            bitmap.recycle();
            return newBitmap;
        }
        return null;
    }

    public static float getImageOrientation(String static_image) {
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

    public static Bitmap readDrawableWithStream(Context context, int resId) {
        InputStream is = context.getResources().openRawResource(resId);
        Bitmap btp = BitmapFactory.decodeStream(is, null, null);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return btp;
    }

    public static Bitmap sampeMinZoomFromFile(String fileName, int minSize) {

        Bitmap bmp;
        if (true) {
            bmp = sampledBitmapFromFile(fileName, minSize, minSize);
        } else {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);
            Flog.d(TAG, "real bitmap: w=" + options.outWidth + "_h=" + options.outHeight);
            minSize = options.outWidth;
            options.inSampleSize = calculateInSampleSize(options, minSize, minSize);
            options.inJustDecodeBounds = false;
            /**
             * Enable the bitmap to share a reference to the input data (inputstream, array, etc.)
             * or if it must make a deep copy.
             * */
            options.inPurgeable = true;
            options.inInputShareable = true;
            bmp = BitmapFactory.decodeFile(fileName, options);
        }

        if (bmp == null) {
            return null;
        }

        Bitmap newBitmap = sampeMinZoomFromBitmap(bmp, minSize);
        if (bmp != newBitmap) {
            ourBitmapRecycle(bmp, true);
        }
        return newBitmap;
    }

    public static Bitmap sampeMinZoomFromBitmap(Bitmap bmp, int minSize) {
        if (bmp == null) {
            return null;
        }
        float rate;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float rateH = ((float) minSize) / ((float) width);
        float rateW = ((float) minSize) / ((float) height);
        if (rateH < rateW) {
            rate = rateW;
        } else {
            rate = rateH;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(rate, rate);
        return Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
    }

    public static Bitmap sampeZoomFromFile(String fileName, int maxSize) {
        Bitmap bmp = sampledBitmapFromFile(fileName, maxSize, maxSize);
        if (bmp == null) {
            return null;
        }
        Bitmap newBitmap = sampeZoomFromBitmap(bmp, maxSize);
        if (bmp != newBitmap) {
            ourBitmapRecycle(bmp, false);
        }
        return newBitmap;
    }

    public static Bitmap sampeZoomFromResource(Resources res, int resId, int maxSize) {
        Bitmap bmp = sampledBitmapFromResource(res, resId, maxSize);
        Bitmap newBitmap = sampeZoomFromBitmap(bmp, maxSize);
        if (bmp != newBitmap) {
            ourBitmapRecycle(bmp, false);
        }
        return newBitmap;
    }

    public static Bitmap sampeZoomFromBitmap(Bitmap bmp, int maxSize) {
        float rate;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float rateH = ((float) maxSize) / ((float) width);
        float rateW = ((float) maxSize) / ((float) height);
        if (rateH < rateW) {
            rate = rateH;
        } else {
            rate = rateW;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(rate, rate);
        return Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight, boolean rotate, int degree) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / ((float) width);
        float scaleHeight = ((float) newHeight) / ((float) height);
        Matrix matrix = new Matrix();
        if (rotate) {
            matrix.postScale(-scaleWidth, scaleHeight);
        } else {
            matrix.postScale(scaleWidth, scaleHeight);
        }
        matrix.postRotate((float) degree);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        if (height <= reqHeight && width <= reqWidth) {
            return 1;
        }
//        float r1 = ((float) height) / ((float) reqHeight);
//        float r2 = ((float) width) / ((float) reqWidth);
        int rateH = Math.round(((float) height) / ((float) reqHeight));
        int rateW = Math.round(((float) width) / ((float) reqWidth));
        if (rateH > rateW) {
            return rateH;
        }
        return rateW;
    }

    public static Options bitmapOptionFromResource(Resources res, int resId) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeResource(res, resId, options);
        if (!(bm == null || bm.isRecycled())) {
            bm.recycle();
        }
        return options;
    }

    public static Options bitmapOptionFromFilename(String filename) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(filename, options);
        if (!(bm == null || bm.isRecycled())) {
            bm.recycle();
        }
        return options;
    }

    public static Options bitmapOptionFromStream(InputStream is) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeStream(is, null, options);
        if (!(bm == null || bm.isRecycled())) {
            bm.recycle();
        }
        return options;
    }

    public static Options bitmapOptionFromUri(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                return null;
            }
            Options option = optionOfBitmapFromStream(inputStream);
            inputStream.close();
            return option;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap sampledBitmapFromResource(Resources res, int resId, int maxSize) {
        Options options = bitmapOptionFromResource(res, resId);
        options.inSampleSize = calculateInSampleSize(options, maxSize, maxSize);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap sampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);
        Flog.d(TAG, "real bitmap: w=" + options.outWidth + "_h=" + options.outHeight);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        /**
         * Enable the bitmap to share a reference to the input data (inputstream, array, etc.)
         * or if it must make a deep copy.
         * */
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeFile(filename, options);
    }

    public static Options optionOfBitmapFromStream(InputStream queryIs) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(queryIs, null, options);
        return options;
    }

    public static Bitmap sampledBitmapFromStream(InputStream queryIs, Options options, int reqWidth, int reqHeight) {
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeStream(queryIs, null, options);
    }

    public static Bitmap sampledBitmapFromStream(InputStream queryIs, InputStream is, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(queryIs, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeStream(is, null, options);
    }

    public static void ourBitmapRecycle(Bitmap bmp, boolean force) {
        if (!(bmp == null || bmp.isRecycled())) {
            bmp.recycle();
        }
    }

    public static int degreesToExifOrientation(float normalizedAngle) {
        if (normalizedAngle == 0.0f) {
            return 1;
        }
        if (normalizedAngle == 90.0f) {
            return 6;
        }
        if (normalizedAngle == 180.0f) {
            return 3;
        }
        if (normalizedAngle == 270.0f) {
            return 8;
        }
        return 1;
    }

    public static float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == 6) {
            return 90.0f;
        }
        if (exifOrientation == 3) {
            return 180.0f;
        }
        if (exifOrientation == 8) {
            return 270.0f;
        }
        return 0.0f;
    }

    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(photoUri, new String[]{"orientation"}, null, null, null);
            if (cursor == null) {
                if (cursor != null) {
                    cursor.close();
                }
                return -1;
            }
            try {
                if (!cursor.moveToFirst()) {
                    return -1;
                }
                int i = cursor.getInt(0);
                if (cursor == null) {
                    return i;
                }
                cursor.close();
                return i;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (SQLiteException e) {
            if (cursor != null) {
                cursor.close();
            }
            return -1;
        }
    }

    public static Bitmap readBitMap(Context context, int resId) {
        Options opt = new Options();
        opt.inPreferredConfig = Config.ARGB_8888;
        return BitmapFactory.decodeStream(context.getResources().openRawResource(resId), null, opt);
    }

    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        int be;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false;
        int beWidth = options.outWidth / width;
        int beHeight = options.outHeight / height;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath, options), width, height, 2);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.DKGRAY);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap getImageFromResourceFile(Resources res, int fileID) {
        Bitmap image = null;
        try {
            InputStream is = res.openRawResource(fileID);
            image = BitmapFactory.decodeStream(is);
            is.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return image;
        }
    }

    public static Bitmap getImageFromAssetsFile(Resources res, String fileName) {
        Bitmap image = null;
        try {
            InputStream is = res.getAssets().open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return image;
        }
    }

    public static Bitmap getImageFromAssetsFile(Resources res, String fileName, Options options) {
        Bitmap image = null;
        try {
            InputStream is = res.getAssets().open(fileName);
            image = BitmapFactory.decodeStream(is, null, options);
            is.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return image;
        }
    }

    public static Bitmap getImageFromAssetsFile(Resources res, String fileName, int sampleSize) {
        Bitmap image = null;
        try {
            InputStream is = res.getAssets().open(fileName);
            Options options = new Options();
            options.inSampleSize = sampleSize;
            options.inPurgeable = true;
            options.inInputShareable = true;
            image = BitmapFactory.decodeStream(is, null, options);
            is.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return image;
        }
    }

    public static InputStream getSDFileInputStream(Context context, String fileName) {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return null;
        }
        String SDPATH = Environment.getExternalStorageDirectory().getPath();
        if (fileName.contains(SDPATH)) {
            fileName = fileName.replace(SDPATH, "");
        }
        try {
            return new FileInputStream(new File(new StringBuilder(String.valueOf(SDPATH)).append("//").append(fileName).toString()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static Bitmap getImageFromSDFile(Context context, String fileName, int sampleSize) {
        try {
            InputStream is = getSDFileInputStream(context, fileName);
            if (is == null) {
                return null;
            }
            Options options = new Options();
            options.inSampleSize = sampleSize;
            options.inPurgeable = true;
            options.inInputShareable = true;
            Bitmap image = BitmapFactory.decodeStream(is, null, options);
            is.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getImageFromSDFile(Context context, String fileName) {
        try {
            InputStream is = getSDFileInputStream(context, fileName);
            if (is == null) {
                return null;
            }
            Bitmap image = BitmapFactory.decodeStream(is);
            is.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String imagelPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = context.getContentResolver().query(contentUri, new String[]{"_data"}, null, null, null);
        String path = "";
        if (cursor != null && cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
        }
        if (cursor != null) {
            cursor.close();
        }
        return path;
    }

    public static Bitmap createCircleImage(Bitmap source, int min) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle((float) (min / 2), (float) (min / 2), (float) (min / 2), paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(source, 0.0f, 0.0f, paint);
        return target;
    }

    /**
     * https://developer.android.com/topic/performance/graphics/load-bitmap.html
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSizeTemplate(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromAsset(Resources src, String pathAssets, int reqWidth, int reqHeight) {

        try {
            InputStream inputStream = src.getAssets().open(pathAssets);
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSizeTemplate(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(inputStream, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * https://developer.android.com/topic/performance/graphics/load-bitmap.html
     */
    public static int calculateInSampleSizeTemplate(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap getImage(Context context, Uri imageCaptureUri, int reqWidth) {
        Cursor cursor = context.getContentResolver().query(imageCaptureUri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String filePath = cursor.getString(cursor.getColumnIndex("_data"));
            String orientation = cursor.getString(cursor.getColumnIndex("orientation"));
            cursor.close();
            if (filePath != null) {
                Bitmap bitmap = BitmapUtil.sampeZoomFromFile(filePath, reqWidth);
                int angle = 0;
                if (!(orientation == null || "".equals(orientation))) {
                    angle = Integer.parseInt(orientation);
                }
                if (angle == 0) {
                    return bitmap;
                }
                Matrix m = new Matrix();
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                m.setRotate((float) angle);
                Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
                if (bitmap == bitmap1 || bitmap == null || bitmap.isRecycled()) {
                    return bitmap1;
                }
                bitmap.recycle();
                return bitmap1;
            }
        }
        return null;
    }
}