package com.lib.collageview.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.lib.collageview.R;
import com.lib.collageview.helpers.Flog;
import com.lib.collageview.helpers.bitmap.BitmapHelper;
import com.lib.collageview.helpers.bitmap.BitmapUtil;

import java.io.File;

/**
 * Created by vutha on 4/10/2017.
 */

/**
 * Asyntask for loading photo from gallery.
 * The params for doInBackground includes two values:
 * 1. The path of photo from gallery in external storage of device.
 * 2. The size of desired width of photo that you want to decode.
 */
public class LoadPhotoAsync extends AsyncTask<Object, Void, Bitmap> {

    private static final java.lang.String TAG = LoadPhotoAsync.class.getSimpleName();
    private int mId = -1;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private OnLoadPhotoListener mLoadPhotoListener;
    private int mTextureMaxSize;
    private boolean mIsOutOfMemoryError;

    public LoadPhotoAsync(Context context, int id, int textureMaxSize) {
        mContext = context;
        mId = id;
        mTextureMaxSize = textureMaxSize;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(mContext, "", mContext.getString(R.string.loading), true);
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected Bitmap doInBackground(Object... objects) {
        String path = (String) objects[0];
        Flog.d(TAG, "path=" + path);
        if (path == null || !new File(path).exists()) return null;
        int reqWidth = (int) objects[1];
        Bitmap bitmap = null;
        mIsOutOfMemoryError = false;
        try {
            bitmap = BitmapUtil.sampeMinZoomFromFile(path, reqWidth); // params: reqWidth  -> no used
        } catch (OutOfMemoryError er) {
            er.printStackTrace();
            mIsOutOfMemoryError = true;
        }
//        return BitmapHelper.correctRotateBmp(path, bitmap);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        mProgressDialog = null;

        if (mLoadPhotoListener != null) {
            Flog.d(TAG, "outofmemory error: " + mIsOutOfMemoryError);
            mLoadPhotoListener.onLoadDone(mId, bitmap, mIsOutOfMemoryError);
        }
    }

    public LoadPhotoAsync setLoadPhotoListener(OnLoadPhotoListener loadPhotoListener) {
        mLoadPhotoListener = loadPhotoListener;
        return this;
    }

    public interface OnLoadPhotoListener {
        void onLoadDone(int idPhoto, Bitmap bmp, boolean outOfMemoryError);
    }
}
