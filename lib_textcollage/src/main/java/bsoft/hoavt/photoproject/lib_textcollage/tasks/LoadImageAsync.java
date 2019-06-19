package bsoft.hoavt.photoproject.lib_textcollage.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.File;

import bsoft.hoavt.photoproject.lib_textcollage.helpers.BitmapUtil;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.Flog;

/**
 * Created by vutha on 4/10/2017.
 */

/**
 * Asyntask for loading photo from gallery.
 * The params for doInBackground includes two values:
 * 1. The path of photo from gallery in external storage of device.
 * 2. The size of desired width of photo that you want to decode.
 */
public class LoadImageAsync extends AsyncTask<Object, Void, Bitmap> {

    private static final String TAG = LoadImageAsync.class.getSimpleName();
    private int mId = -1;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private OnLoadPhotoListener mLoadPhotoListener;
    private int mTextureMaxSize;
    private boolean mIsOutOfMemoryError;

    public LoadImageAsync(Context context, int id, int textureMaxSize) {
        mContext = context;
        mId = id;
        mTextureMaxSize = textureMaxSize;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(mContext, "", "Loading...", true);
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
            bitmap = BitmapUtil.sampeMinZoomFromFile(path, reqWidth);
        } catch (OutOfMemoryError er) {
            er.printStackTrace();
            mIsOutOfMemoryError = true;
        }
        return BitmapUtil.correctRotateBmp(path, bitmap);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        mProgressDialog = null;

        if (mLoadPhotoListener != null) {
            Flog.d(TAG, "outofmemory error: " + mIsOutOfMemoryError);
            mLoadPhotoListener.onImageLoaded(mId, bitmap, mIsOutOfMemoryError);
        }
    }

    public LoadImageAsync setLoadPhotoListener(OnLoadPhotoListener loadPhotoListener) {
        mLoadPhotoListener = loadPhotoListener;
        return this;
    }

    public interface OnLoadPhotoListener {
        void onImageLoaded(int idPhoto, Bitmap bmp, boolean outOfMemoryError);
    }
}
