package com.lib.collageview.interfaces;

/**
 * Created by vutha on 3/30/2017.
 */

/**
 * Callbacks of onTouchEvent() on Stickerview.
 */
public interface StickerViewListener {

    /**
     * Callbacked when the clicked stickerview.
     * -> Open editor for stickerview.
     */
    void onTextStickerClicked(int textStickerIndex);

    void onStickerDeleted(int stickerIndex);

    void onStickerMoving(int stickerIndex);

    void onStickerStoped(int stickerIndex);

    void onInputTextSticker(int textStickerIndex);
}
