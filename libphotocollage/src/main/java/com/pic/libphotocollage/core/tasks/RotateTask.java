package com.pic.libphotocollage.core.tasks;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;

import com.pic.libphotocollage.core.model.ItemPhotoView;

import java.lang.ref.WeakReference;

/**
 * Created by vutha on 9/18/2016.
 */
public class RotateTask extends AsyncTask<Bitmap, Void, Bitmap> {
    private WeakReference<ItemPhotoView> itemPhotoView;
    private WeakReference<Bitmap> rotateBitmap;
    private OnRotatedDone mOnRotatedFinish = null;
    private float degree = 0.0f;

    public RotateTask(ItemPhotoView itemPhotoView, float degree) {
        this.itemPhotoView = new WeakReference<ItemPhotoView>(itemPhotoView);
        this.degree = degree;
    }

    @Override
    protected void onPreExecute() {
        //if you want to show progress dialog
    }

    @Override
    protected Bitmap doInBackground(Bitmap... params) {
        Bitmap rotated = params[0];
        if (rotated == null || rotated.isRecycled())
            return null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        rotateBitmap = new WeakReference<Bitmap>
                (Bitmap.createBitmap(rotated, 0, 0, rotated.getWidth(), rotated.getHeight(), matrix, true));
        return rotateBitmap.get();
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        //dismiss progress dialog
        if (result != null && mOnRotatedFinish != null) {
            itemPhotoView.get().setBitmapRect(result);
            mOnRotatedFinish.onRotatedDone();
        }
        if (!this.isCancelled()) {
            this.cancel(true);
        }
    }

    public RotateTask setOnRotatedFinish(OnRotatedDone onSavedFinish) {
        mOnRotatedFinish = onSavedFinish;
        return this;
    }

    public interface OnRotatedDone {
        public void onRotatedDone();
    }
}