package com.pic.libphotocollage.core.collection;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.MotionEvent;

import com.pic.libphotocollage.core.model.BaseItem;
import com.pic.libphotocollage.core.util.Flog;

import java.util.ArrayList;

/**
 * Created by thuck on 9/19/2016.
 */
public class ListAdaptiveItem extends ArrayList<BaseItem> {
    private int mCurrentItemIndex = -1;

    public void onDraw(Canvas canvas) {
        for (BaseItem item : this) {
            if (item != null) {
                item.draw(canvas);
            }
        }
    }

    @Override
    public boolean remove(Object object) {
        return super.remove(object);
    }

    public BaseItem getCurrentItem() {
        int index = mCurrentItemIndex;
        if (index < 0 || index >= this.size()) {
            Flog.i("itemsticker current is null at getCurrentSticker");
            return null;
        }
        return this.get(index);
    }

    public void setCurrentItem(int index) {
        if (index < 0 || index >= this.size()) {
            Flog.i("itemsticker current is null");
            return;
        }
        mCurrentItemIndex = index;
        for (int i = 0; i < this.size(); i++) {
            BaseItem item = this.get(i);
            if (i != index && item != null) {
                item.setInEdit(false);
                return;
            }
            if (i == index && item != null) {
                item.setInEdit(true);
                return;
            }
        }
    }

    public boolean isInEdit() {
        for (BaseItem item : this) {
            if (item != null && item.isInEdit()) return true;
        }
        return false;
    }

    public boolean setCurrentItem(MotionEvent event) {
        boolean result = false;

        for (int i = this.size() - 1; i >= 0; i--) {
            final BaseItem item = this.get(i);
            if (item != null && item.isInBitmap(event) && !result) {
                mCurrentItemIndex = i;
                item.setInEdit(true);
                new Handler().post(new Runnable() {       
                    @Override
                    public void run() {
                        item.getListener().onItemClicked(item, true);
                    }
                });
                result = true;
            } else if(item!=null) {
                item.setInEdit(false);
            }
        }
        if (!result) mCurrentItemIndex = -1;
        return result;
    }

    public boolean onTouchEvent(MotionEvent event) {
        BaseItem BaseItem = getCurrentItem();
        if (BaseItem != null) {
            if (BaseItem.onTouchEvent(event)) {
                return true;
            }
        } else {
            Flog.i("ItemSticker == null");
        }
        return false;
    }


    public boolean notTouchAll() {
        for (BaseItem item : this) {
            if (item != null && item.isInEdit())
                return false;
        }
        return true;
    }

    public boolean isDeleteAll() {
        for (BaseItem item : this) {
            if (item != null && !item.isItemDeleted())
                return false;
        }
        return true;
    }

    public void setNotTouchAll() {
        for (BaseItem item : this) {
            if (item != null) {
                item.setInEdit(false);
            }
        }
    }

    public void invalidate() {
        for (BaseItem item : this) {
            if (item != null) {
                item.invalidateRatio();
            }
        }
    }

    public int getCurrentItemIndex() {
        return mCurrentItemIndex;
    }
}
