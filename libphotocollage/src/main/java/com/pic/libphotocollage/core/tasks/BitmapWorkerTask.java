//package com.pic.libphotocollage.core.tasks;
//
//import android.graphics.Bitmap;
//import android.os.AsyncTask;
//
//import com.pic.libphotocollage.core.model.ItemPhotoView;
//import com.pic.libphotocollage.core.util.displaybmp.BitmapDecoder;
//
//import java.lang.ref.WeakReference;
//
///**
// * Created by vutha on 9/18/2016.
// */
//public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
//    private final WeakReference<ItemPhotoView> itemPhotoReference;
//    private int mWidthImage = 0;
//    private int mHeightImage = 0;
//    private OnBitmapLoaded mOnBimapLoadedListener;
//
//    public BitmapWorkerTask(ItemPhotoView photoView, int w, int h) {
//        // Use a WeakReference to ensure the ImageView can be garbage collected
//        itemPhotoReference = new WeakReference<ItemPhotoView>(photoView);
//        mWidthImage = w;
//        mHeightImage = h;
//    }
//
//    // Decode image in background.
//    @Override
//    protected Bitmap doInBackground(String... params) {
//        String pathFile = params[0];
////        Flog.d("doInBackground: w="+mWidthImage+"_h="+mHeightImage);
//        return BitmapDecoder.decodeSampledBitmapFromFile(pathFile, mWidthImage, mHeightImage);
//    }
//
//    // Once complete, see if ImageView is still around and set bitmap.
//    @Override
//    protected void onPostExecute(Bitmap bitmap) {
//        if (itemPhotoReference != null && bitmap != null) {
//            final ItemPhotoView photoView = itemPhotoReference.get();
//            if (photoView != null) {
//                photoView.setBitmapRect(bitmap);
//                photoView.setDefaultSrcRect();
//                if (mOnBimapLoadedListener != null) {
//                    mOnBimapLoadedListener.onBitmapLoaded(photoView.getPriority(), bitmap);
//                }
//            }
//        }
//    }
//
//    public BitmapWorkerTask setOnBimapLoadedListener(OnBitmapLoaded onBimapLoadedListener) {
//        mOnBimapLoadedListener = onBimapLoadedListener;
//        return this;
//    }
//
//    public interface OnBitmapLoaded {
//        public void onBitmapLoaded(int curPhotoIndex, Bitmap bitmap);
//    }
//}
