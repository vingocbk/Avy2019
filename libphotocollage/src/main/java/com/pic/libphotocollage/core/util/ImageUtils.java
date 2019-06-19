package com.pic.libphotocollage.core.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <b>Class</b> : ImageUtils
 * <p/>
 * <b>Description : </b>It is base ImageUtils class of mobile imaging part
 */
public class ImageUtils {

    private static boolean mIsPng = false; // br.kim

    /**
     * add image to ImageUtils ContentProvider
     */
    static Uri addImage(ContentResolver cr, String title, long dateTaken,
                        Location location, int orientation, String directory,
                        String filename, int width, int height) {

        ContentValues values = new ContentValues();

        if (directory != null)
            if (directory.endsWith("/"))
                directory = directory.substring(0, directory.length() - 1);

        File ftemp = null;
        long filesize = 0;
        if (directory != null && filename != null) {

            ftemp = new File(directory + "/" + filename);
            filesize = ftemp.length();
            ftemp = null;
        }

        if (title == null)
            values.put(Images.Media.TITLE, filename);
        else
            values.put(Images.Media.TITLE, title);
        values.put(Images.Media.DISPLAY_NAME, filename);
        values.put(Images.Media.DATE_TAKEN, dateTaken);
        values.put(Images.Media.DATE_MODIFIED, dateTaken);
        values.put(Images.Media.DATE_ADDED, dateTaken);
        values.put(Images.Media.DESCRIPTION, BaseStatistic.ALBUM);

        if (filename.endsWith(".png")) {
            values.put(Images.Media.MIME_TYPE, "image/png");
        } else {
            values.put(Images.Media.MIME_TYPE, "image/jpeg");
        }

        values.put(Images.Media.SIZE, filesize);

        values.put(Images.Media.ORIENTATION, orientation);

        if (location != null) {
            values.put(Images.Media.LATITUDE, location.getLatitude());
            values.put(Images.Media.LONGITUDE, location.getLongitude());
        }

        values.put("width", width);
        values.put("height", height);

        if (directory != null && filename != null) {
            String fullfilename = directory + "/" + filename;
            values.put(Images.Media.DATA, fullfilename);
        }

        return cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    static boolean sync(FileOutputStream stream) {
        try {
            if (stream != null) {
                stream.getFD().sync();
            }
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    static boolean isPng() {
        return mIsPng;
    }

    static void setPng(boolean isPng) {
        mIsPng = isPng;
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

    public static Bitmap initEmptyBmp(float w, float h) {
        if (((int)w) <= 0)
            w += 1;
        if (((int)h) <= 0)
            h += 1;
        Bitmap bitmap;
        bitmap = Bitmap.createBitmap((int) w, (int) h, Bitmap.Config.RGB_565);
        bitmap.eraseColor(Color.GRAY);
        return bitmap;
    }

    public static Bitmap recycleBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = null;
        }
        return null;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle, PointF pivotCenter) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle, pivotCenter.x, pivotCenter.y);
//        matrix.postTranslate(100, 100);
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(source,source.getWidth() - 10,source.getHeight() - 10,true);
//        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static boolean makeBitmapMatrix(Bitmap bitmap, Matrix matrix, Uri uriImg) {
        boolean ret = false;
        int rotate = 0;
        float scale = 1.0f;
        rotate = SystemUtils.getRotateDegree(uriImg.getPath());
        if (rotate == 0) {
            matrix.postRotate(0);
        } else if (rotate == 90) {
            matrix.postRotate(90);
        } else if (rotate == 180) {
            matrix.postRotate(180);
        } else if (rotate == 270) {
            matrix.postRotate(270);
        } else {
            rotate = 0;
        }

        int imageSize = bitmap.getWidth() * bitmap.getHeight();
        if (imageSize > SystemUtils.MAX_MULTIGRID_IMAGE_SIZE) {
            scale = (float) Math
                    .sqrt((SystemUtils.MAX_MULTIGRID_IMAGE_SIZE / (float) (imageSize)));
            if (scale > 0.999f && scale < 1.f)
                scale = 0.999f;
            matrix.postScale(scale, scale);
        }

//        Flog.d("rotate: " + rotate + "_scale: " + scale);
        if (rotate != 0 || scale != 1.f)
            ret = true;
        return ret;
    }

    /**
     * Rotate image to correct the direction
     */
    public static Bitmap correctRotatedBitmap(Uri uriImg, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        boolean isRotated = makeBitmapMatrix(bitmap, matrix, uriImg);
        if (isRotated) {
            Bitmap b = null;
            if (!bitmap.isRecycled()) {
                try {
                    b = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(),
                            matrix, true);
                    bitmap = ImageUtils.recycleBitmap(bitmap);
                    return b;
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    bitmap = ImageUtils.recycleBitmap(bitmap);
                    b = ImageUtils.recycleBitmap(b);
                }
            }
            return null;
        } else
            return bitmap;
    }

    public static final int MAX_IMAGE_SIZE = 3000000;

    public static int getSampleSize(BitmapFactory.Options options, int maxSize) {
        int bitmapSize = options.outWidth * options.outHeight;
        int ret = 1;
        if (bitmapSize > maxSize) {
            for (int i = 1; (bitmapSize / i) > maxSize; i *= 2) {
                ret = i;
            }
            ret++;
        }
        return ret;
    }

    /**
     * Converts a immutable bitmap to a mutable bitmap. This operation doesn't allocates
     * more memory that there is already allocated.
     *
     * @param imgIn - Source image. It will be released, and should not be used more
     * @return a copy of imgIn, but muttable.
     */
    public static Bitmap convertToMutable(Bitmap imgIn) {
        try {
            //this is the file going to use temporally to save the bytes.
            // This file will not be a image, it will store the raw image data.
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

            //Open an RandomAccessFile
            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
            //into AndroidManifest.xml file
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            // get the width and height of the source bitmap.
            int width = imgIn.getWidth();
            int height = imgIn.getHeight();
            Bitmap.Config type = imgIn.getConfig();

            //Copy the byte to the file
            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes()*height);
            imgIn.copyPixelsToBuffer(map);
            //recycle the source bitmap, this will be no longer used.
            imgIn.recycle();
            System.gc();// try to force the bytes from the imgIn to be released

            //Create a new bitmap to load the bitmap again. Probably the memory will be available.
            imgIn = Bitmap.createBitmap(width, height, type);
            map.position(0);
            //load it back from temporary
            imgIn.copyPixelsFromBuffer(map);
            //close the temporary file and channel , then delete that also
            channel.close();
            randomAccessFile.close();

            // delete the temp file
            file.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgIn;
    }

    public static Drawable getBgClicked(Context context) {
        // Create an array of the attributes we want to resolve
        // using values from a theme
        // android.R.attr.selectableItemBackground requires API LEVEL 11
        int[] attrs = new int[] { android.R.attr.selectableItemBackground /* index 0 */};

        // Obtain the styled attributes. 'themedContext' is a context with a
        // theme, typically the current Activity (i.e. 'this')
        TypedArray ta = context.obtainStyledAttributes(attrs);

        // Now get the value of the 'listItemBackground' attribute that was
        // set in the theme used in 'themedContext'. The parameter is the index
        // of the attribute in the 'attrs' array. The returned Drawable
        // is what you are after
        Drawable drawableFromTheme = ta.getDrawable(0 /* index */);

        // Finally free resources used by TypedArray
        ta.recycle();

        // setBackground(Drawable) requires API LEVEL 16,
        // otherwise you have to use deprecated setBackgroundDrawable(Drawable) method
        return drawableFromTheme;
    }
}
