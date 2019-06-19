package com.lib.collageview.interfaces;

/**
 * Created by vutha on 4/13/2017.
 */

public interface KeyboardListener {
    void onKeyboardShown(int kbHeight);

    void onKeyboardHide();

    void onKeyboardDismiss();

    void onKeyboardDisplay();
}
