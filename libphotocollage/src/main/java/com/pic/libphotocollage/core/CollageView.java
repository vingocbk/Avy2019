package com.pic.libphotocollage.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pic.libphotocollage.core.collection.ListAdaptiveItem;
import com.pic.libphotocollage.core.collection.ListPhotoViews;
import com.pic.libphotocollage.core.model.BaseItem;
import com.pic.libphotocollage.core.model.ItemBubbleTextView;
import com.pic.libphotocollage.core.model.ItemPhotoView;
import com.pic.libphotocollage.core.util.Flog;
import com.pic.libphotocollage.core.util.SystemUtils;
import com.pic.libphotocollage.core.util.zoom.PhotoPinchZoom;

import java.util.List;

/**
 * Created by hoavt on 20/07/2016.
 */
public class CollageView extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener {
    public static final int MIN_PIXEL_WIDTH_SAVED = 960;
    public static final int DRAGGING_ALPHA_PAINT = 155;
    private static final String TAG = CollageView.class.getSimpleName();
    boolean isInEdit = false;
    int[] oldSize = new int[2];
    private boolean mIsDragging = false;
    private int mSaveCanvasWidth;
    private int mSaveCanvasHeight;
    private float mRatioView = 1.0f;
    private boolean isKeyboardShown = false;
    private float lastTranslateY;
    private int keyboardHeight;
    private boolean isTranslated = false;
    private Context mContext;
    //    private ListAdaptiveItem mListStickers;
//    private ListAdaptiveItem mListBubbleTexts;
    private ListAdaptiveItem mListItem = new ListAdaptiveItem();
    private PhotoPinchZoom.OnCollagePinchZoomCallback mTouchZoomCallback;
    private PhotoPinchZoom mPinchZoomListenner;
    private GestureDetector mGestureDetector;
    private Paint mAlphaPaint;
    private ListPhotoViews mListPhotos;
    private OnSwapTwoObjListenner mOnSwapTwoObjListenner = null;
    private TextWatcher curTextWatcher;
    private EditText virtualEditText;
    private Rect r = new Rect();

    public CollageView(Context context) {
        super(context);
        if (!isInEditMode()) {
            initViews(context);
        }
    }

    public CollageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initViews(context);
        }
    }

    public CollageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            initViews(context);
        }
    }

    public boolean isCurrentItemType(BaseItem.ItemType type) {
        return getCurrentItem() != null && getCurrentItem().getItemType() == type;
    }

    private void initViews(Context context) {

        mContext = context;
        initPinchZoomListenner();
        initPaint();
        initGesture();

        virtualEditText = new AppCompatEditText(context);
        virtualEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(0, 0);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(1, 1);
        virtualEditText.setLayoutParams(params);

        virtualEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        addView(virtualEditText);

        virtualEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    dismissKeyboard();
                }
                return false;
            }
        });

        final Window mRootWindow = getActivity().getWindow();
        View mRootView = mRootWindow.getDecorView().findViewById(android.R.id.content);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

//    public void setListStickers(ListAdaptiveItem listStickers) {
//        mListStickers = listStickers;
//    }

    private Activity getActivity() {
        return (Activity) mContext;
    }

    private void initPaint() {
        mAlphaPaint = new Paint();
        mAlphaPaint.setAlpha(DRAGGING_ALPHA_PAINT);
    }

    private void initGesture() {
        GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent event) {
                super.onLongPress(event);
                if (mListPhotos == null || mListItem == null)
                    return;
                int touchedIdx = mListPhotos.getTouchedIndex(event);
                if (touchedIdx != -1 && mListPhotos.get(touchedIdx).isSelected() && (mListItem.isDeleteAll() || mListItem.notTouchAll())) {  // apply only for dragging photo-item
                    // prepare dragging for photo item
                    mIsDragging = true;
                }
            }
        };

        mGestureDetector = new GestureDetector(mContext, gestureListener);
    }

    public void setListPhotos(ListPhotoViews listPhotos) {
        mListPhotos = listPhotos;
    }

    public void setBackgroundRect(int width, int height) {
        mListPhotos.setBackgroundRect(new Rect(0, 0, width, height));
    }

    public void setBackgroundRect(int left, int top, int width, int height) {
        mListPhotos.setBackgroundRect(new Rect(left, top, width, height));
    }

    public void setBackgroundRectSaved() {
        mListPhotos.setBackgroundRect(new Rect(0, 0, mSaveCanvasWidth, mSaveCanvasHeight));
    }

    private void initPinchZoomListenner() {
        mPinchZoomListenner = new PhotoPinchZoom();
        mTouchZoomCallback = new PhotoPinchZoom.OnCollagePinchZoomCallback() {
            @Override
            public void onPinchZoom(MotionEvent ev, int diffDst) {
                if (mListPhotos == null) return;
                int index0 = mListPhotos.getTouchedIndex(ev.getX(0),
                        ev.getY(0));    // first finger
                int index1 = mListPhotos.getTouchedIndex(ev.getX(1),
                        ev.getY(1));    // second finger

                if (index0 == index1) {
                    mListPhotos.pinchZoomItemPhoto(index0, diffDst);
                }
            }

            @Override
            public boolean onSingleTouchEvent(MotionEvent ev) {
                return false;
            }
        };
        mPinchZoomListenner.init(mTouchZoomCallback);
    }

//    private void drawStickers(Canvas canvas) {
//        if (mListStickers != null) {
//            mListStickers.onDraw(canvas);
//        }
//
//    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        mListItem.onDraw(canvas);
        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();  // push to stack storing context, not save shape

        // Touch sticker, not drawGrid Round border of photo-items
        if (!mListItem.isDeleteAll() && !mListItem.notTouchAll()) {
            mListPhotos.setCurPhotoIndex(-1);
        }

        drawPhotoItems(canvas);

        if (mIsDragging) {
            drawDraggingObj(canvas);
        }

        mListItem.onDraw(canvas);

        canvas.restore();   // pop off stack
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        Flog.d("TouchEvent ", "dispatchTouchEvent");
        if (event == null) return true;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isInEdit = mListItem.isInEdit();
        }
        // onTouch of sticker is unique
        if (!mListItem.isDeleteAll() && mListItem.onTouchEvent(event)) {
//                 Flog.d("Duc", "move sticker");
            mIsDragging = false;
            invalidate();   // update onDraw Stickers
            return true;
        }

        if (mPinchZoomListenner != null) {

            mPinchZoomListenner.onTouchEvent(event);
            invalidate();   // update onDraw zooming
        }

        if (mGestureDetector.onTouchEvent(event)) {
            invalidate();   // update onLongClick
            return true;
        }

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mPinchZoomListenner.isZoom()) {
                    invalidate();
                    return true;
                }
                if (event.getPointerCount() > 1 || mListItem == null) return true;
                if (!mListItem.isDeleteAll()) {
                    // update when touch to sticker
                    mListItem.setCurrentItem(event);
                }

                if (!isInEdit && mListItem.getCurrentItem() == null && mListPhotos != null) {
                    Log.d("touched", "event: x=" + event.getX() + "_y=" + event.getY());
//                    Log.d("touched", "cur TOuched: "+mListPhotos.getTouchedIndex(event));
                    boolean touchItemStart = mListPhotos.moveItemStart(mListPhotos.getTouchedIndex(event)
                            , event.getX(), event.getY());

                    if (touchItemStart)
                        mListPhotos.scanOpenGallery();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mListPhotos == null) return true;
                if (!mListPhotos.isIndexInList(mListPhotos.getCurPhotoIndex())) {
                    return true;
                }
                if (mPinchZoomListenner.isZoom()) {
                    invalidate();
                    return true;
                }
                if (mIsDragging) {
                    mIsDragging = dragItemPhoto(event);
                } else {
                    boolean moveItem = mListPhotos.moveItemPhoto(mListPhotos.getTouchedIndex(event),
                            event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mListPhotos == null) return true; // khanh add to fix crash problem, 19/01/2017
                if (!mListPhotos.isIndexInList(mListPhotos.getCurPhotoIndex())) {
                    return true;
                }
                if (mPinchZoomListenner.isZoom()) {
                    invalidate();
                    return true;
                }

                if (isInEdit) return true;
                if (mIsDragging) {
                    if (mListPhotos != null && mListPhotos.checkDraggingCollision(event, mListPhotos.getCurPhotoIndex())) {
                        if (mOnSwapTwoObjListenner != null)
                            mOnSwapTwoObjListenner.onSwapTwoObj(mListPhotos.getCurPhotoIndex(), mListPhotos.getDraggedPhotoIndex());
                        mListPhotos.swapTwoPhotoItems(mListPhotos.getCurPhotoIndex(), mListPhotos.getDraggedPhotoIndex());
                    }
                    mIsDragging = false;
                } else {
                    updateCurIndexPhoto(event);
                }
                break;
        }
        invalidate();   // update onDraw
        return true;

    }


    private void drawDraggingObj(Canvas canvas) {
        ItemPhotoView itemPhotoView = mListPhotos.getCurPhotoItem();
        RectF draggingRectF = mListPhotos.getDraggingRect();
        if (itemPhotoView != null && draggingRectF != null) {
            canvas.drawBitmap(itemPhotoView.getPhotoBmp(), itemPhotoView.getSrcRect(), draggingRectF, mAlphaPaint);
        }
    }

    private boolean dragItemPhoto(MotionEvent event) {

        float xFinger = event.getX();
        float yFinger = event.getY();
        ItemPhotoView itemPhotoView = mListPhotos.getCurPhotoItem();
        if (itemPhotoView == null) return false;
        RectF rectF = itemPhotoView.getDesRect();
        mListPhotos.setDraggingRect(new RectF(xFinger - rectF.width() / 2, yFinger - rectF.height() / 2,
                xFinger + rectF.width() / 2, yFinger + rectF.height() / 2));
        return true;
    }

    private void drawPhotoItems(Canvas canvas) {
        if (mListPhotos != null) {
            mListPhotos.draws(canvas);
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        super.onTouchEvent(event);
//        Flog.d("TouchEvent ", "onTouchEvent");
//        if (event == null) return true;
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            isInEdit = mListItem.isInEdit();
//        }
//        // onTouch of sticker is unique
//        if (!mListItem.isDeleteAll() && mListItem.onTouchEvent(event)) {
//            mIsDragging = false;
//            invalidate();   // update onDraw Stickers
//            return true;
//        }
//
//        if (mPinchZoomListenner != null) {
//            mPinchZoomListenner.onTouchEvent(event);
//            invalidate();   // update onDraw zooming
//        }
//
//        if (mGestureDetector.onTouchEvent(event)) {
//            invalidate();   // update onLongClick
//            return true;
//        }
//
//        int action = event.getAction();
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                if (mPinchZoomListenner.isZoom()) {
//                    invalidate();
//                    return true;
//                }
//                if (event.getPointerCount() > 1 || mListItem == null) return true;
//                if (!mListItem.isDeleteAll()) {
//                    // update when touch to sticker
//                    mListItem.setCurrentItem(event);
//                }
//
//                if (!isInEdit && mListItem.getCurrentItem() == null && mListPhotos != null) {
//                    Log.d("touched", "event: x="+event.getX()+"_y="+event.getY());
////                    Log.d("touched", "cur TOuched: "+mListPhotos.getTouchedIndex(event));
//                    boolean touchItemStart = mListPhotos.moveItemStart(mListPhotos.getTouchedIndex(event)
//                            , event.getX(), event.getY());
//
//                    if (touchItemStart)
//                        mListPhotos.scanOpenGallery();
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (!mListPhotos.isIndexInList(mListPhotos.getCurPhotoIndex())) {
//                    return true;
//                }
//                if (mPinchZoomListenner.isZoom()) {
//                    invalidate();
//                    return true;
//                }
//                if (mIsDragging) {
//                    mIsDragging = dragItemPhoto(event);
//                } else {
//                    boolean moveItem = mListPhotos.moveItemPhoto(mListPhotos.getTouchedIndex(event),
//                            event.getX(), event.getY());
//                }
//                break;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                if (mListPhotos == null) return true; // khanh add to fix crash problem, 19/01/2017
//                if (!mListPhotos.isIndexInList(mListPhotos.getCurPhotoIndex())) {
//                    return true;
//                }
//                if (mPinchZoomListenner.isZoom()) {
//                    invalidate();
//                    return true;
//                }
//
//                if (isInEdit) return true;
//                if (mIsDragging) {
//                    if (mListPhotos != null && mListPhotos.checkDraggingCollision(event, mListPhotos.getCurPhotoIndex())) {
//                        if (mOnSwapTwoObjListenner != null)
//                            mOnSwapTwoObjListenner.onSwapTwoObj(mListPhotos.getCurPhotoIndex(), mListPhotos.getDraggedPhotoIndex());
//                        mListPhotos.swapTwoPhotoItems(mListPhotos.getCurPhotoIndex(), mListPhotos.getDraggedPhotoIndex());
//                    }
//                    mIsDragging = false;
//                } else {
//                    updateCurIndexPhoto(event);
//                }
//                break;
//        }
//        invalidate();   // update onDraw
//        return true;
//    }

    public void registerKeyboardEvent(TextWatcher textWatcher) {
        if (curTextWatcher != null) {
            virtualEditText.removeTextChangedListener(curTextWatcher);
        }
        curTextWatcher = textWatcher;
        virtualEditText.addTextChangedListener(textWatcher);
    }

    public void unregisterKeyboardEvent(TextWatcher textWatcher) {
        virtualEditText.removeTextChangedListener(textWatcher);
    }

    public void requestKeyboard() {
        if (isKeyboardShown) return;
        if (!isCurrentBubbleText()) return;
        Log.d(TAG, "request Keyboard");
        showRealEditText();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        isKeyboardShown = true;
        registerKeyboardEvent((TextWatcher) mListItem.getCurrentItem());
        invalidate();
        //performAnimation();
    }

    private boolean isCurrentBubbleText() {
        return mListItem.getCurrentItem() instanceof ItemBubbleTextView;
    }

    public void showRealEditText() {
        if (mListItem.getCurrentItem() == null) return;
        if (!isCurrentBubbleText()) return;
        ViewGroup.LayoutParams params = virtualEditText.getLayoutParams();
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = 0;
        params.height = 0;

        virtualEditText.setLayoutParams(params);
        virtualEditText.setBackground(getResources().getDrawable(R.drawable.rect));
        virtualEditText.setTextColor(Color.WHITE);
        virtualEditText.setIncludeFontPadding(true);
        virtualEditText.setPadding(SystemUtils.getDpToPixel(getContext(), 5),
                0, SystemUtils.getDpToPixel(getContext(), 15), virtualEditText.getPaddingBottom());
        virtualEditText.removeTextChangedListener(curTextWatcher);
        virtualEditText.getEditableText().clear();

        virtualEditText.append(((ItemBubbleTextView) mListItem.getCurrentItem()).getText());
        virtualEditText.addTextChangedListener(curTextWatcher);
        virtualEditText.invalidate();
        virtualEditText.requestFocus();
        isKeyboardShown = true;
    }

    public void hideRealEditText() {
        ViewGroup.LayoutParams params = virtualEditText.getLayoutParams();
        params.width = 0;
        params.height = 0;
        virtualEditText.setLayoutParams(params);
        virtualEditText.invalidate();
        isKeyboardShown = false;
    }

    public void translateBack() {
        if (true) return;
        if (isTranslated) {
            Log.d("EE", "vao translate back");
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, lastTranslateY);
            translateAnimation.setDuration(250);
            translateAnimation.setFillAfter(true);
            final float oldY = getY();
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            clearAnimation();
                            isTranslated = false;
                            Log.d("EE", "getX = " + getX() + " getY = " + getY());
                            setY(oldY + lastTranslateY);
                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            startAnimation(translateAnimation);
        }
    }

    private void performAnimation() {
        if (true) return;
        if (isTranslated) return;
        ItemBubbleTextView textView = (ItemBubbleTextView) mListItem.getCurrentItem();
        final float[] pos = textView.getCurrentPosition();
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        Log.d(TAG, "actionBarSize = " + actionBarSize);

        lastTranslateY = getMeasuredHeight() - pos[1];
        if (getMeasuredHeight() - pos[1] >= keyboardHeight) return;
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -lastTranslateY);
        translateAnimation.setDuration(250);
        translateAnimation.setFillAfter(true);
        final float oldY = getY();
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "animation start");
                isTranslated = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        clearAnimation();
                        Log.d("EE", "getX = " + getX() + " getY = " + getY());
                        setY(oldY - lastTranslateY);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(translateAnimation);
    }

    public boolean isKeyboardShown() {
        return isKeyboardShown;
    }

    public void unSelectAllComponent() {
        mListItem.setNotTouchAll();
        invalidate();
    }

    public void dismissKeyboard() {
        if (!isKeyboardShown) return;
        Log.d(TAG, "dismiss keyboard");
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(virtualEditText.getWindowToken(), 0);
        hideRealEditText();
        if (curTextWatcher != null) {
            virtualEditText.removeTextChangedListener(curTextWatcher);
        }
        isKeyboardShown = false;
    }

    private void updateCurIndexPhoto(MotionEvent event) {
        // Set item photoview selected currently
        int curIndex = mListPhotos.getTouchedIndex(event);
        mListPhotos.setCurPhotoIndex(curIndex);
        Flog.d("update curindex = " + curIndex);
    }

    public void setRoundSize(float roundSize) {
        mListPhotos.setRoundnessRects(roundSize);
    }

    public void setSaveBitmapSize() {
        int maxSizeIndex = mListPhotos.getMaxPhotoPixelIndex();

        if (maxSizeIndex < 0)
            return;

//        Flog.i("width=" + width
//                + "_getOrgWidth=" + mListPhotoViews.get(maxSizeIndex).getOrgSrcWidth()
//                + "_getOrgHeight=" + mListPhotoViews.get(maxSizeIndex).getOrgSrcHeight());

        mSaveCanvasWidth = Math.max(MIN_PIXEL_WIDTH_SAVED,
                Math.max(mListPhotos.get(maxSizeIndex).getOrgSrcWidth(),
                        mListPhotos.get(maxSizeIndex).getOrgSrcHeight()));

        mSaveCanvasHeight = (int) (mSaveCanvasWidth * mRatioView);
        Flog.d(TAG, "mSaveCanvasWidth=" + mSaveCanvasWidth + "_mSaveCanvasHeight=" + mSaveCanvasHeight);
    }

    public void setSaveBitmapSize(int saveCanvasWidth, int saveCanvasHeight) {
        mSaveCanvasWidth = saveCanvasWidth;
        mSaveCanvasHeight = saveCanvasHeight;
    }

    public int getSaveCanvasHeight() {
        return mSaveCanvasHeight;
    }

    public int getSaveCanvasWidth() {
        return mSaveCanvasWidth;
    }

    public float getRatioSavedView() {
        return mRatioView;
    }

    public void setRatioSavedView(float ratio) {
        mRatioView = ratio;
    }

    public int[] getOldSize() {
        return oldSize;
    }

    public void putOldSize() {
        oldSize[0] = getWidth();
        oldSize[1] = getHeight();
    }

    public void addItem(BaseItem item) {
        mListItem.setNotTouchAll();
        mListItem.add(item);
        mListItem.setCurrentItem(mListItem.size() - 1);
    }

    public int getItemSize() {
        return mListItem.size();
    }

    public BaseItem getCurrentItem() {
        return mListItem.getCurrentItem();
    }

    public ListAdaptiveItem getListItem() {
        return mListItem;
    }

    public void setListItem(ListAdaptiveItem listAdaptiveItem) {
        if (mListItem != null) releaseListItemView(mListItem);
        this.mListItem = listAdaptiveItem;
    }

    private void releaseListItemView(List<? extends BaseItem> list) {
        for (BaseItem item : list) {
            if (item != null) item.release();
        }
    }

    public void release() {
        releaseListItemView(mListItem);
//        releaseListItemView(mListPhotos);
        if (mListPhotos != null) {
            mListPhotos.release();
            mListPhotos = null;
        }
        // Release unused objects:
        mTouchZoomCallback = null;
        mPinchZoomListenner = null;
        mGestureDetector = null;
        mAlphaPaint = null;
        mOnSwapTwoObjListenner = null;
        curTextWatcher = null;
        virtualEditText = null;
        r = null;
    }

    @Override
    public void onGlobalLayout() {
        int oldBottom = r.bottom;
        View view = getActivity().getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(r);
        int height = oldBottom - r.bottom;
        if (height > SystemUtils.dpToPx(getActivity(), 150)) {
            keyboardHeight = height;
            performAnimation();
        }
    }

    public CollageView setOnSwapTwoObj(OnSwapTwoObjListenner onSwapTwoObj) {
        mOnSwapTwoObjListenner = onSwapTwoObj;
        return this;
    }

    public interface OnSwapTwoObjListenner {
        void onSwapTwoObj(int curIndex, int dragIndex);
    }
}