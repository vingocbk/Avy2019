package com.pic.libphotocollage.core.tasks;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;

import com.pic.libphotocollage.core.R;
import com.pic.libphotocollage.core.util.Flog;
import com.pic.libphotocollage.core.util.SystemUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vutha on 9/7/2016.
 */
public class DecodeAsyncTask extends AsyncTask<InputStream, Void, Bitmap> {
    private ProgressDialog mProgressDialog = null;
    private Context mContext = null;
    private ContentResolver mContentResolver = null;
    private OnDecodedFinish mOnDecodedFinish = null;
    private int mIndex = -1;

    public DecodeAsyncTask(Context context) {
        mContext = context;
        mContentResolver = context.getContentResolver();
//        mIndex = index;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getResources().getString(R.string.processing));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    @Override
    protected Bitmap doInBackground(InputStream... params) {
        Bitmap bitmapDecoded = null;

        InputStream stream = params[0];
        BitmapFactory.Options optsResolution = null;
        if (stream != null) {
            optsResolution = new BitmapFactory.Options();
            optsResolution.inJustDecodeBounds = false;
            optsResolution.inPreferredConfig = Bitmap.Config.ARGB_8888;
            optsResolution.inMutable = true;
            int sampleSize = getSampleSize(optsResolution.outWidth
                    * optsResolution.outHeight, SystemUtils.MAX_IMAGE_SIZE);
//            Flog.i("sampleSize: " + sampleSize);
            optsResolution.inSampleSize = sampleSize;

            try {
                bitmapDecoded = BitmapFactory.decodeStream(stream, null,
                        optsResolution);
                if (bitmapDecoded != null) {

                    int sampleSizeDecoded = getSampleSize(bitmapDecoded.getWidth()
                            * bitmapDecoded.getHeight(), SystemUtils.MAX_IMAGE_SIZE);
//                    Flog.i("sampleSizeDecoded: " + sampleSizeDecoded);
                    if (sampleSizeDecoded > 1) {
                        Bitmap tmp = Bitmap.createScaledBitmap(bitmapDecoded,
                                bitmapDecoded.getWidth() / sampleSizeDecoded,
                                bitmapDecoded.getHeight() / sampleSizeDecoded, true);
                        bitmapDecoded.recycle();
                        bitmapDecoded = tmp;
                    }
                } else {
                    Flog.i("bitmapDecoded is null");
                }
            } catch (NullPointerException e) {
                bitmapDecoded = null;
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                bitmapDecoded = null;
                e.printStackTrace();
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        if (bitmapDecoded == null)
//            return bitmapDecoded;
        /** Rotate image to correct the direction */
//        Matrix matrix = new Matrix();
//        boolean isRotated = makeBitmapMatrix(bitmapDecoded, matrix, uri);
//        if (isRotated) {
////            synchronized (bitmapDecoded) {
//            Bitmap b = null;
//            if (!bitmapDecoded.isRecycled()) {
//                try {
//                    b = Bitmap.createBitmap(bitmapDecoded, 0, 0,
//                            bitmapDecoded.getWidth(), bitmapDecoded.getHeight(),
//                            matrix, true);
//                } catch (OutOfMemoryError e) {
//                    e.printStackTrace();
//                    b = null;
//                }
//            }
//            if (b == null) {
//                return null;
//            }
//            ImageUtils.recycleBitmap(bitmapDecoded);
//            bitmapDecoded = b;
////            }
//        }
        return bitmapDecoded;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (bitmap == null) {
            Flog.i("bitmap == null");
            return;
        }
        if (mOnDecodedFinish != null) {
//            Flog.i("mIndex: " + mIndex);
//            mOnDecodedFinish.onDecodedDone(bitmap, mIndex);
            mOnDecodedFinish.onDecodedDone(bitmap);
        } else {
            Flog.i("mOnDecodedFinish is null");
        }
    }

    private int getSampleSize(int bitmapSize, int maxSize) {
        int ret = 1;
        if (bitmapSize > maxSize) {
            for (int i = 1; (bitmapSize / i) > maxSize; i *= 2) {
                ret = i;
            }
            ret++;
        }
        return ret;
    }

    public DecodeAsyncTask setOndecodedFinish(OnDecodedFinish ondecodedFinish) {
        mOnDecodedFinish = ondecodedFinish;
        return this;
    }

    private boolean makeBitmapMatrix(Bitmap bitmap, Matrix matrix, Uri uriImg) {
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

        Flog.i("rotate: " + rotate + "_scale: " + scale);
        if (rotate != 0 || scale != 1.f)
            ret = true;
        return ret;
    }

    public interface OnDecodedFinish {
        public void onDecodedDone(Bitmap bitmapDecoded);
    }
}
