package com.lib.collageview.interfaces;

/**
 * Created by vutha on 3/23/2017.
 */

/**
 * Callbacks of onTouchEvent() on Photoview.
 */
public interface PhotoViewListener {

    void onPhotoActionDown(int photoIndex);

    void onPhotoActionMove(int photoIndex);

    void onPhotoActionUp(int photoIndex);
}
