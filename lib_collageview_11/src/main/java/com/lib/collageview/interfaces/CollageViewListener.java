package com.lib.collageview.interfaces;

/**
 * Created by vutha on 3/23/2017.
 */

public interface CollageViewListener {
    /**
     * The listener when CollageView initialized done.
     * Using interface to handle at other activity/fragment.
     */
    void showedOnScreen();

    /**
     * Determine the view type is focused.
     * [Photoview contains image, Photoview no contains image, IconStickerView, TextStickerView].
     *
     * @param focusType the type of view that is focused.
     */
    void onFocusedView(int focusType);

    /**
     * Callback when loading all photos is done from gallery.
     */
    void onPhotosLoadDone();


    void onPhotoviewActionUp(int idx);
}
