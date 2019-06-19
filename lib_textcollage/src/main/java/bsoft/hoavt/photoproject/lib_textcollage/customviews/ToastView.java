package bsoft.hoavt.photoproject.lib_textcollage.customviews;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import bsoft.hoavt.photoproject.lib_textcollage.R;

/**
 * Created by nam on 4/20/2017.
 */

public class ToastView extends Toast {
    public ToastView(Context context, Activity activity, String content) {
        super(context);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.customview_toast, null);
        TextView tv = (TextView) view.findViewById(R.id.tvToast);
        tv.setText(content);
        setDuration(Toast.LENGTH_SHORT);
        setView(view);
    }

    public static void showContent(Context context, Activity activity, String content) {
        new ToastView(context, activity, content).show();
    }
}
