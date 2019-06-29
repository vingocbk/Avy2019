package com.lib.collageview.customviews.listviews;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;

import com.lib.collageview.CollageView;
import com.lib.collageview.customviews.views.BaseView;
import com.lib.collageview.customviews.views.PhotoView;
import com.lib.collageview.helpers.Flog;
import com.lib.collageview.helpers.svg.SVGPathUtils;

import java.util.ArrayList;

/**
 * Created by vutha on 3/22/2017.
 */

public class PhotoViewList extends ArrayList<PhotoView> {

    private static final String TAG = PhotoViewList.class.getSimpleName();
    private Context mContext;
    private CollageView mCollageView;
    /**
     * The index of the touched photoview on collageview.
     */
    private int mCurIndex = -1;
    /**
     * Storage index of photoview that is dragged at the start.
     */
    private int mSrcDraggedIndex = -1;
    private float mMaxWidth;

    public PhotoViewList(CollageView collageView) {
        mCollageView = collageView;
        mContext = collageView.getContext();
    }

    public void onDraw(Canvas canvas) {
        // draw photos:
        for (int i = 0; i < this.size(); i++) {
            BaseView item = this.get(i);
            if (item != null) {
                /**
                 * Clip 2 times:
                 * First: Clip rect entire layout parent
                 * Second: Clip path for each photo
                 * */
                canvas.save();
                canvas.clipRect( mCollageView.getCollageViewRect(), Region.Op.INTERSECT);
                item.onDraw(canvas, i);
                canvas.restore();
            }
        }
    }


    public boolean onTouchEvent(MotionEvent event) {
        int curIdx = getTouchedIndex(event);
        if (curIdx == -1)
            return false;
        Flog.d(TAG, "**curIdx=" + curIdx);
        PhotoView curPhoto = this.get(curIdx);
        if (curPhoto != null) {
            setSelectedPhotoview(curIdx);
            if (curPhoto.onTouchEvent(event)) {
                return true;
            }
        } else {
            Flog.d(TAG, "PhotoViewModel " + curIdx + " is null");
        }
        return false;
    }

    /**
     * Set flag selected of current photoview to true. Otherwise, remains set to false.
     *
     * @param curIdx the index of current photoview that is selected.
     */
    private void setSelectedPhotoview(int curIdx) {
        this.get(curIdx).setIsSelected(true);
        for (int i = 0; i < this.size(); i++) {
            if (i != curIdx) {
                if (this.get(i).isSelected()) {
                    this.get(i).setIsSelected(false);
                    break;
                }
            }
        }
    }

    public int getTouchedIndex(MotionEvent event) {
        int index = -1;
        for (int i = this.size() - 1; i >= 0; i--) {
            if (this.get(i).getRegion().contains((int) event.getX(), (int) event.getY())) {
                index = i;
                break;
            }
        }
        mCurIndex = index;
        return index;
    }

    public int getCurrentIndex() {
        return mCurIndex;
    }

    public int getSrcDraggedIndex() {
        return mSrcDraggedIndex;
    }

    public void setSrcDraggedIndex(int touchedIdx) {
        mSrcDraggedIndex = touchedIdx;
    }

    /**
     * Swap two photoviews.
     *
     * @param srcIdx index of draged photoview.
     * @param dstIdx index of droped photoview.
     */
    public void swap(int srcIdx, int dstIdx) {
        PhotoView srcPhotoview = this.get(srcIdx);
        PhotoView dstPhotoview = this.get(dstIdx);

        // swap images between two elements in list.
        Bitmap tmp = dstPhotoview.getBitmap();
        dstPhotoview.setBitmap(srcPhotoview.getBitmap());
        srcPhotoview.setBitmap(tmp);

        srcPhotoview.fitPhotoToLayout();
        dstPhotoview.fitPhotoToLayout();

        srcPhotoview.setIsSelected(false);
        dstPhotoview.setIsSelected(true);


    }

    public void release() {
        for (PhotoView item : this) {
            item.release();
        }
    }

    public int[] getNoContentIndex() {
        //  count number of items that no content:
        int len = 0;
        for (int i = 0; i < this.size(); i++) {
            PhotoView itemPhotoView = this.get(i);
            if (itemPhotoView.getBitmap() == null)
                len++;
        }
        // init array of integer that save index of items that no content:
        int ans[] = new int[len];
        if (len <= 0) {
            return ans;
        }
        int index = 0;
        for (int i = 0; i < this.size(); i++) {
            PhotoView itemPhotoView = this.get(i);
            if (itemPhotoView.getBitmap() == null) {
                ans[index] = i;
                Flog.d(TAG, "no image: " + i);
                index++;
            }
        }
        return ans;
    }


    public void setMaxWidth(float maxWidth) {
        mMaxWidth = maxWidth;
    }

    public float getMaxWidth() {
        return mMaxWidth;
    }
}
