package bsoft.hoavt.photoproject.lib_textcollage.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import bsoft.hoavt.photoproject.lib_textcollage.R;
import bsoft.hoavt.photoproject.lib_textcollage.customviews.CharacterTextView;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.Flog;
import bsoft.hoavt.photoproject.lib_textcollage.listeners.OnCharacterTextViewListener;

/**
 * Created by vutha on 7/10/2017.
 */

public class TextCollageAdapter extends PagerAdapter {

    private static final java.lang.String TAG = TextCollageAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Character> mCharacters = new ArrayList<>();
    private OnCharacterTextViewListener mListener;

    public TextCollageAdapter(Context context, String word, OnCharacterTextViewListener listener) {
        mContext = context;
        mListener = listener;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (word == null || word.isEmpty())
            return;

//        String trimWord = word.replace(" ", "").replace("\n", "");
        String trimWord = word;
        Flog.d(TAG, "word trimed="+trimWord);

        mCharacters.clear();
        for (int i = 0; i < trimWord.length(); i++) {
            mCharacters.add(trimWord.charAt(i));
        }
    }

    @Override
    public int getCount() {
        return mCharacters.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_text_collage, container, false);
//        itemView.setTranslationX(-1 * itemView.getWidth() * position);  // DepthPageTransformer

//            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
//            imageView.setImageResource(mResources[position]);

        Flog.d(TAG, "position="+position);

        CharacterTextView characterTextView = (CharacterTextView) itemView.findViewById(R.id.item_character_textview);
        characterTextView.setListener(mListener);
        characterTextView.setId(position);
        characterTextView.setText(mCharacters.get(position).toString());

        characterTextView.setTag("extra_" + position);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}