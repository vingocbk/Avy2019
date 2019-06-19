package com.lib.collageview.interfaces;

import android.graphics.Path;
import android.net.Uri;

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
     * This method is called when saving the collaged photo to storage of device.
     *
     * @param uri the uri of the final collaged photo.
     * */
    void onSavedDone(String uri);

    void onStickerMoving(int stickerIndex);

    void onTextStickerClicked(int stickerIndex);

    /**
     * Callback when swaping between two photos doned.
     *
     * @param srcIdx the index of source photo.
     * @param dstIdx the index of destination photo.
     * */
    void onSwapDone(int srcIdx, int dstIdx);

    /**
     * Callback when pressing edit-button of TextStickerView.
     *
     * @param textStickerIndex the index of TextStickerView in the sticker-list.
     * */
    void onInputTextSticker(int textStickerIndex);

    /**
     * Callback when the decoding-photo process occurs out of memory error.
     *
     * @param idPhoto the index of photo that is decoded.
     * */
    void outOfMemoryError(int idPhoto);

    /**
     * Callback when loading all photos is done from gallery.
     * */
    void onPhotosLoadDone();

    /**
     * Callback when the saving-photo process occurs out of memory error.
     * */
    void outOfMemoryErrorSave();

    void onPhotoviewActionUp(int idx);
}
