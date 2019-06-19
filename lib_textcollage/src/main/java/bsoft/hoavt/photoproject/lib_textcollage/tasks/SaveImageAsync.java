package bsoft.hoavt.photoproject.lib_textcollage.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;

import bsoft.hoavt.photoproject.lib_textcollage.R;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.BitmapUtil;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.FileUtil;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.Flog;

/**
 * Created by vutha on 9/10/2016.
 */
public class SaveImageAsync extends AsyncTask<Object, Void, Uri> {

    private ProgressDialog mProgressDialog;
    private Context mContext;
    private OnSavedFinish mOnSavedFinish;

    /**
     * Asynctask for saving collaged-photo.
     * The input includes two params:
     * params[0]: the whole bitmap of collaged-photo
     * & params[1]: the absolute path of directory that used to save final collaged-photo.
     *
     * @param context the context of asynctask.
     */
    public SaveImageAsync(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(mContext, null,
                mContext.getResources().getString(R.string.saving), true);
        mProgressDialog.setMessage(mContext.getResources().getString(R.string.saving));
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected Uri doInBackground(Object... params) {

        Bitmap savedCollageBmp = (Bitmap) params[0];
        String absolutePathDir = (String) params[1];
        if (savedCollageBmp == null || !FileUtil.makeFolder(absolutePathDir))
            return null;

        FileOutputStream fOut;
        File saveFile = new File(absolutePathDir + FileUtil.getSimpleDate() + ".jpg");
        try {
            fOut = new FileOutputStream(saveFile);
            if (fOut == null)
                return null;
            savedCollageBmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.getFD().sync();
            fOut.close();

            Uri uri = FileUtil.addImage(mContext.getContentResolver(),
                    null,
                    System.currentTimeMillis(),
                    null,
                    0,
                    saveFile.getParent(),
                    saveFile.getName(),
                    savedCollageBmp.getWidth(),
                    savedCollageBmp.getHeight());
            if (uri != null)
                FileUtil.scanMediaStore(mContext, uri);
            savedCollageBmp = BitmapUtil.recycle(savedCollageBmp);
            return uri;
        } catch (Exception e) {
            Flog.d("Exception saving");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Uri uri) {
        super.onPostExecute(uri);
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (mOnSavedFinish != null && uri != null) {
            mOnSavedFinish.onSavedDone(uri);
        }
    }

    public SaveImageAsync setOnSavedFinish(OnSavedFinish onSavedFinish) {
        mOnSavedFinish = onSavedFinish;
        return this;
    }

    public interface OnSavedFinish {
        void onSavedDone(Uri uri);
    }
}


