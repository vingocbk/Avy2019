package com.lib.collageview.helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.lib.collageview.interfaces.CollageViewListener;
import com.lib.collageview.interfaces.KeyboardListener;

/**
 * Created by vutha on 3/31/2017.
 */

public class KeyboardUtil {

    /**
     * The flag variable check whether keyboard soft is shown or not.
     */
    public static boolean isKeyboardShown = false;

    /**
     * You can force Android to hide the virtual keyboard using the InputMethodManager,
     * calling hideSoftInputFromWindow, passing in the token of the window containing your focused view.
     *
     * @param context     the context of activity.
     * @param viewFocused the EditText view is being focused to show keyboard.
     */
    public static void hideKeyboard(Context context, View viewFocused) {
        if (viewFocused == null) return;
        if (viewFocused.isFocused()) {
//            viewFocused.setFocusableInTouchMode(false);
            viewFocused.clearFocus();
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewFocused.getWindowToken(), 0);
        isKeyboardShown = false;


//        if (callback && keyboardListener != null) {
//            keyboardListener.onKeyboardDismiss();
//        }
    }

    public static void showKeyboard(Context context, View viewFocused) {
        isKeyboardShown = false;
        if (viewFocused == null) return;
        if (!viewFocused.isFocused()) {
//            viewFocused.setFocusableInTouchMode(true);
            viewFocused.requestFocus();
        }
        Flog.d("StickerViewList showkeyboard = " + viewFocused.isFocused());
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(viewFocused, InputMethodManager.SHOW_IMPLICIT);
        /**
         * Show forced to keyboard display, not depend to edittext view.
         * */
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        isKeyboardShown = true;


//        if (callback && keyboardListener != null) {
//            keyboardListener.onKeyboardDisplay();
//        }
    }

    /**
     * Remove on global layout listener.
     *
     * @param v        the root view, take it from "Activity.getWindow().getDecorView().findViewById(android.R.id.content)"
     * @param listener the OnGlobalLayoutListener of ViewTreeObserver
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < 16) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }
}
