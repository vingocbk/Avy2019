package bsoft.hoavt.photoproject.lib_textcollage.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

import bsoft.hoavt.photoproject.lib_textcollage.helpers.Flog;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.blur.Blur;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.blur.BlurFactor;
import bsoft.hoavt.photoproject.lib_textcollage.listeners.OnBlurTaskListener;

/**
 * Created by vutha on 7/20/2017.
 */

public class BlueTask extends AsyncTask<Bitmap, Void, Bitmap> {

    private static final java.lang.String TAG = BlueTask.class.getSimpleName();
    private Context mContext;
    private int mRadius;
    private int mColor;
    private OnBlurTaskListener mListener;
    private ProgressDialog mDialog;
    private int mInSampleSize;

    public BlueTask(Context context) {
        mContext = context;
        mRadius = BlurFactor.DEFAULT_RADIUS;
        mColor = Color.TRANSPARENT;
        mInSampleSize = BlurFactor.DEFAULT_SAMPLING;
    }

    public BlueTask color(int color) {
        mColor = color;
        return this;
    }

    public BlueTask sampling(int inSampleSize) {
        mInSampleSize = inSampleSize;
        return this;
    }

    public BlueTask radius(int radius) {
        mRadius = radius;
        return this;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mDialog == null)
            mDialog = ProgressDialog.show(mContext, "Blur background", "Bluring...");
        if (mDialog != null && !mDialog.isShowing())
            mDialog.show();
    }

    @Override
    protected Bitmap doInBackground(Bitmap... bitmaps) {
        if (bitmaps[0] == null || bitmaps[0].isRecycled())
            return null;
        Flog.d(TAG, "radius=" + mRadius);

        BlurFactor blurFactor = new BlurFactor();
        blurFactor.width = bitmaps[0].getWidth();
        blurFactor.height = bitmaps[0].getHeight();
        blurFactor.radius = mRadius;
        blurFactor.color = mColor;
        blurFactor.sampling = mInSampleSize;
        return Blur.of(mContext, bitmaps[0], blurFactor);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
        if (bitmap == null || bitmap.isRecycled())
            return;
        if (mListener != null)
            mListener.onBlured(bitmap);
    }

    public BlueTask callback(OnBlurTaskListener listener) {
        mListener = listener;
        return this;
    }
}
