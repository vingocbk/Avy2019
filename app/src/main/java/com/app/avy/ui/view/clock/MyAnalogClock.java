package com.app.avy.ui.view.clock;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;
import com.turki.vectoranalogclockview.VectorAnalogClock;

public class MyAnalogClock extends VectorAnalogClock {

    private void init() {
        //use this for the default Analog Clock (recommended)
        initializeSimple();
    }

    //mandatory constructor
    public MyAnalogClock(Context ctx) {
        super(ctx);
        init();
    }

    // the other constructors are in case you want to add the view in XML

    public MyAnalogClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyAnalogClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyAnalogClock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
}
