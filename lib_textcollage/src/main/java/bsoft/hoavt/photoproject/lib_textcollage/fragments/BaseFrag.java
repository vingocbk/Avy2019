package bsoft.hoavt.photoproject.lib_textcollage.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Adm on 3/30/2017.
 */
public abstract class BaseFrag extends Fragment {

    private static final String TAG = BaseFrag.class.getSimpleName();
    protected FragmentActivity mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
