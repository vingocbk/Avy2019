package com.lib.collageview.stickers.liststickers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Region;
import android.view.MotionEvent;

import com.lib.collageview.CollageView;
import com.lib.collageview.helpers.Flog;
import com.lib.collageview.interfaces.StickerViewListener;
import com.lib.collageview.stickers.BaseStickerView;
import com.lib.collageview.stickers.text.TextStickerView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by hoavt on 3/29/2017.
 */
public class StickerViewList extends ArrayList<BaseStickerView> {

    private static final String TAG = StickerViewList.class.getSimpleName();
    private Context mContext;
    private CollageView mCollageView;
    /**
     * The index of current stickerview that is selected.
     * The value equals "-1" ,when allviews is unselected/unfocused.
     */
    private int mCurrentIndex = -1;

    public StickerViewList(CollageView collageView) {
        mCollageView = collageView;
        mContext = collageView.getContext();
    }

    public void onDraw(Canvas canvas) {
        if (mCollageView != null && mCollageView.getCollageViewRect() != null)
        Flog.d(TAG, "size arr=" + this.size());
        for (BaseStickerView item : this) {
            if (item != null) {
                Flog.d(TAG, "check=" + item.isInEdit());
                item.onDraw(canvas);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        Flog.d(TAG, "onTouchEvent stickerview");
        if (this.isEmpty()) return false;

        if (mCurrentIndex == -1) {
            /**
             * Actually, all stickerviews are unselected/unfocused.
             * */
            int curIdx = getSelectedIndex(event);
            Flog.d(TAG, "1 curIdx=" + curIdx + "_vs_=" + mCurrentIndex);
            if (curIdx == -1)
                return false;
            bringToFront();
            BaseStickerView curSticker = this.get(mCurrentIndex);
            if (curSticker != null) {

//                if (!setSelectedStickerview(mCurrentIndex)) {
//                }
                setSelectedStickerview(mCurrentIndex);
                if (curSticker.onTouchEvent(event)) {
                    return true;
                }
            } else {
                Flog.d(TAG, "StickerView " + mCurrentIndex + " is null");
            }

            mCurrentIndex = -1;
            return false;
        } else {
            /**
             * Actually, a stickerview is being selected/focused.
             * */
            Flog.d(TAG, " Actually, a stickerview is being selected/focused.");
            BaseStickerView stickerView = this.get(mCurrentIndex);
            if (stickerView == null) return false;
            boolean handled = stickerView.onTouchEvent(event);
            if (!handled)
                mCurrentIndex = -1;
            return handled;
        }
    }

    private void handleClickedSticker() {
        /**
         * Catch onTouchEvent() of TextStickerView to show keyboard for inputing text.
         * */
        if (mCurrentIndex != -1 && mCurrentIndex < size()) {
            BaseStickerView baseStickerView = get(mCurrentIndex);
            if ((baseStickerView != null) && (baseStickerView instanceof TextStickerView)) {
                Flog.d("KEYBOARDDDD", "1 isEdit=" + baseStickerView.isInEdit());
                StickerViewListener listener = baseStickerView.getStickerViewListener();
                if (listener != null)
                    listener.onTextStickerClicked(mCurrentIndex);
            }
        }
    }

    /**
     * Bring the current selected/focused stickerview to front of all stickers in list.
     */
    private void bringToFront() {
        if (mCurrentIndex < 0 || mCurrentIndex >= this.size()) return;
        // swap index id of StickerView/
        this.get(mCurrentIndex).setIndex(this.size() - 1);
        this.get(this.size() - 1).setIndex(mCurrentIndex);
        // swap two elements in list.
        Collections.swap(this, this.size() - 1, mCurrentIndex);
        mCurrentIndex = this.size() - 1;
    }

    public int getSelectedIndex(MotionEvent event) {
        int index = -1;
        for (int i = this.size() - 1; i >= 0; i--) {
            if (this.get(i).isInBitmap(event)) {
                index = i;
                break;
            }
        }
        mCurrentIndex = index;

//        if (KeyboardUtil.isKeyboardShown)
//            unrequestKeyboard();

//        /**
//         * Catch onTouchEvent() of TextStickerView to show keyboard for inputing text.
//         * */
//        if (mCurrentIndex != -1 && mCurrentIndex < size()) {
//            BaseStickerView baseStickerView = get(mCurrentIndex);
//            if ((baseStickerView != null) && (baseStickerView instanceof TextStickerView)) {
//                Flog.d("KEYBOARDDDD", "1 isEdit=" + baseStickerView.isInEdit());
//                StickerViewListener listener = baseStickerView.getStickerViewListener();
//                if (listener != null)
//                    listener.onTextStickerClicked(mCurrentIndex);
//            }
//        }

        return index;
    }

    /**
     * Set flag selected of current stickerview to true. Otherwise, remains in list that is assigned to false.
     *
     * @param curIdx the index of current stickerview that is selected.
     */
    public void setSelectedStickerview(int curIdx) {
        if (curIdx == -1)
            return;
        for (int i = 0; i < this.size(); i++) {
            if (i != curIdx) {
                if (this.get(i).isInEdit()) {
                    this.get(i).setInEdit(false);
                    break;
                }
            }
        }
        this.get(curIdx).setInEdit(true);
        handleClickedSticker();
    }

    public void release() {
        mCurrentIndex = -1;
        if (mCollageView != null)
            mCollageView = null;
        for (BaseStickerView item : this) {
            if (item == null) item.release();
        }
    }

    public void invalidateRatio(float newCollageRatio) {
        for (BaseStickerView item : this) {
            if (item != null) {
                item.invalidateRatio(newCollageRatio);
            }
        }
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        mCurrentIndex = currentIndex;
        setSelectedStickerview(mCurrentIndex);
    }

    /**
     * Determine the outside area of current focused/selected stickerview,
     * whether belong to the area of a other stickerview in this list.
     *
     * @param event motion event of the current stickerview focused/selected.
     * @return true if focusing to other stickerview. Otherwise, false if focusing nothing.
     */
    public int focusOtherSticker(MotionEvent event) {
        if (this.isEmpty()) return -1;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).isInBitmap(event)) {
                return this.get(i).getStickerType();
            }
        }
        return -1;
    }

    public void updateIndices() {
        for (int i = 0; i < this.size(); i++) {
            this.get(i).setIndex(i);
        }
    }
}
