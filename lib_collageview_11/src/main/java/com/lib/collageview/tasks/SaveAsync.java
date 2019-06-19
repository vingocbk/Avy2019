package com.lib.collageview.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.DateFormat;

import com.lib.collageview.CollageView;
import com.lib.collageview.R;
import com.lib.collageview.helpers.FileUtils;
import com.lib.collageview.helpers.Flog;
import com.lib.collageview.helpers.Utils;
import com.lib.collageview.helpers.bitmap.BitmapHelper;

import java.io.File;
import java.io.FileOutputStream;

import static com.lib.collageview.CollageView.TAG;

/**
 * Created by vutha on 9/10/2016.
 */
public class SaveAsync extends AsyncTask<Object, Void, Uri> {

    private ProgressDialog mProgressDialog;
    private Context mContext;
    private OnSavedFinish mOnSavedFinish;
    private String pathFile = "";

    public SaveAsync(Context context) {
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
//        if (savedCollageBmp == null || !FileUtils.makeFolder(absolutePathDir))
//            return null;

        FileOutputStream fOut;
        DateFormat df = new DateFormat();
        File saveFile = new File(FileUtils.APP_FOLDER, "photo_" + df.format("yyyy-MM-dd_hh-mm-ss", new java.util.Date()) + ".png");
//        File saveFile = new File(absolutePathDir + Utils.getSimpleDate() + ".png"); //.jpg
        pathFile = saveFile.getAbsolutePath();
        try {
            fOut = new FileOutputStream(saveFile);
            if (fOut == null)
                return null;
            savedCollageBmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.getFD().sync();
            fOut.close();
            Uri uri = FileUtils.addImage(mContext.getContentResolver(), mContext, pathFile);
            if (uri != null)
                FileUtils.scanMediaStore(mContext, uri);
            savedCollageBmp = BitmapHelper.recycle(savedCollageBmp);
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
            mOnSavedFinish.onSavedDone(pathFile);
        }
    }

    public SaveAsync setOnSavedFinish(OnSavedFinish onSavedFinish) {
        mOnSavedFinish = onSavedFinish;
        return this;
    }

    public interface OnSavedFinish {
        void onSavedDone(Uri uri);

        void onSavedDone(String path);
    }
}


