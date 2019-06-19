package bsoft.hoavt.photoproject.lib_textcollage.fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.text.Html;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import bsoft.hoavt.photoproject.lib_textcollage.R;
import bsoft.hoavt.photoproject.lib_textcollage.adapters.TextCollageAdapter;
import bsoft.hoavt.photoproject.lib_textcollage.customviews.AutoResizeTextView;
import bsoft.hoavt.photoproject.lib_textcollage.customviews.CharacterTextView;
import bsoft.hoavt.photoproject.lib_textcollage.customviews.NonSwipeableViewPager;
import bsoft.hoavt.photoproject.lib_textcollage.customviews.ZoomOutPageTransformer;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.CommonVl;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.EGL14Util;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.FileUtil;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.Flog;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.MathUtil;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.gallery.MyGalleryLib;
import bsoft.hoavt.photoproject.lib_textcollage.listeners.OnCharacterTextViewListener;
import bsoft.hoavt.photoproject.lib_textcollage.listeners.OnTextCollageFragListener;
import bsoft.hoavt.photoproject.lib_textcollage.models.WordItem;
import bsoft.hoavt.photoproject.lib_textcollage.tasks.LoadImageAsync;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by vutha on 7/7/2017.
 */

public class TextCollageFrag extends BaseFrag implements OnCharacterTextViewListener, LoadImageAsync.OnLoadPhotoListener {

    private static final String TAG = TextCollageFrag.class.getSimpleName();
    private TextCollageAdapter mTextAdapter;
    private AppCompatImageView mIvNextText, mIvBackText, mIvBack, mIvNext;
    /**
     * Reference:   https://github.com/lsjwzh/RecyclerViewPager
     */
    private String word;
    private ViewPager mViewPager;
    private AutoResizeTextView mTvPreview;
    private int mCurId = -1;
    private OnTextCollageFragListener listener;
    private int mHighlightIdx = 0;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.btn_next_text) {
                changeItem(1);
            } else if (id == R.id.btn_back_text) {
                changeItem(-1);
            } else if (id == R.id.btn_back) {
                if (mContext != null)
                    mContext.onBackPressed();
                else
                    Flog.d(TAG, "mContext is null");
            } else if (id == R.id.btn_next) {

                WordItem saver = new WordItem();
                int len = word.length();
                for (int i = 0; i < len; i++) {
                    CharacterTextView characterTv = (CharacterTextView) mViewPager.findViewWithTag("extra_" + i);
                    Flog.d(TAG, "bmp at " + i + "=" + characterTv.getBitmap());
                    characterTv.getSaver().setDis2Vertices(characterTv.getDistanceTwoVertices());
                    saver.add(characterTv.getSaver());
                }
                Flog.d(TAG, "1 word=" + saver.getWord());
//                    ((CollageFrag) collageFrag).addWord(saver);

                if (listener != null)
                    listener.onNextBtnClicked(saver);
            }
        }
    };
    private float mCharWidth, mCharHeight;
    private GalleryFinal.OnHanlderResultCallback mOnHandlerResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {
            Flog.d(TAG, "resultList=" + resultList);
            if (resultList != null) {
                Flog.d(TAG, "resultList size=" + resultList.size());
                String pathImg = resultList.get(0).getPhotoPath();
                Flog.d(TAG, "resultList path=" + pathImg);

                String path = pathImg;
//                int reqWidth = (int) mPhotoViewList.getMaxWidth() * ConstValues.BITMAP_SCALE_VALUE;
                int wHolder = getResources().getDimensionPixelSize(R.dimen.width_charactetv);
                int hHolder = getResources().getDimensionPixelSize(R.dimen.height_charactertv);
                float ratioHolder = wHolder * 1F / hHolder;

                int imgSize[] = FileUtil.getImgSize(path);
                int wImg = imgSize[0];
                int hImg = imgSize[1];
                float ratioImg = wImg * 1F / hImg;

                Flog.d(TAG, "resultList CharacterTextview: w=" + wHolder + "_h=" + hHolder + "_ratio=" + ratioHolder);
                Flog.d(TAG, "resultList Image: w=" + wImg + "_h=" + hImg + "_ratio=" + ratioImg);
                Flog.d(TAG, "resultList Image: char  W=" + (int) mCharWidth + "_H=" + (int) mCharHeight);
//                int reqWidth = FileUtil.getImgSize(path)[0];
                int reqWidth = MathUtil.getFitCenterImgSize((int) mCharWidth, (int) mCharHeight, wImg, hImg)[0];
                Flog.d(TAG, "resultList reqWidth=" + reqWidth + "_textureSize=" + EGL14Util.getMaxTextureSize());
//                if (reqWidth > getWidth())
//                    reqWidth = getWidth();
//                Flog.d(TAG, "reqWidth=" + reqWidth);

                new LoadImageAsync(getContext(), mCurId, EGL14Util.getMaxTextureSize())
                        .setLoadPhotoListener(TextCollageFrag.this)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, path, reqWidth);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(mContext, errorMsg + "", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeItem(int diff) {
        if (mViewPager == null || word == null || mTvPreview == null) {
            return;
        }
        int curPos = mViewPager.getCurrentItem();
        Flog.d(TAG, "curPos=" + curPos);
        if ((curPos <= 0 && diff < 0) || (curPos >= word.length() && diff > 0)) {

            return;
        }
//        int newPos = curPos + diff;
//        Flog.d(TAG, "newPos=" + newPos + "_len=" + mViewPager.getChildCount());
//        mViewPager.setCurrentItem(newPos);
//        highlightCharInStr(mTvPreview, newPos, CommonVl.PREVIEW_HIGHLIGHT_COLOR);

//        Flog.d(TAG, "char at " + newPos + " is: " + word.charAt(newPos));

//        mHighlightIdx = newPos;
        mHighlightIdx += diff;

        if (mHighlightIdx < 0 || mHighlightIdx >= word.length()) {
            if (mHighlightIdx < 0) {
                mHighlightIdx = 0;
            }
            if (mHighlightIdx >= word.length()) {
                mHighlightIdx = word.length() - 1;
            }
            return;
        }

        while ((word.charAt(mHighlightIdx) == ' ') || (word.charAt(mHighlightIdx) == '\n')) {
            mHighlightIdx += diff;
        }
        highlightTextAt(mHighlightIdx);
        mViewPager.setCurrentItem(mHighlightIdx, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_text_collage, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null)
            return;
        getRslt();
        initViews();
    }

    private void getRslt() {
        word = getArguments().getString(CommonVl.EXTRA_WORD_INPUT);
        Flog.d(TAG, "word=" + word);
    }

    private void initViews() {

//        word = word.replace(" ", "").replace("\n", "");

        mTvPreview = (AutoResizeTextView) getView().findViewById(R.id.tv_preview);
        mTvPreview.setText(word);
        Flog.d(TAG, "numOfLines=" + mTvPreview.getLineCount());
        Flog.d(TAG, "len=" + mTvPreview.length());


        highlightTextAt(0);
        highlightCharInStr(mTvPreview, 0, CommonVl.PREVIEW_HIGHLIGHT_COLOR);

        LinearLayout parentView = (LinearLayout) getView().findViewById(R.id.viewparent_fragment_text_collage);
        parentView.setOnClickListener(null);

        mTextAdapter = new TextCollageAdapter(mContext, word, this);

//        mViewPager = (ViewPager) getView().findViewById(R.id.pager);
        mViewPager = (NonSwipeableViewPager) getView().findViewById(R.id.pager);
        mViewPager.setAdapter(mTextAdapter);
        mViewPager.setOffscreenPageLimit(word.length());
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                Flog.d(TAG, "1 onPageSelected= OnPageChanged: newPos=" + position + "_positionOffset=" + positionOffset + "_positionOffsetPixels=" + positionOffsetPixels);
                if (positionOffset != 0 && positionOffsetPixels != 0)
                    return;
                if (mIvBackText == null || mIvNextText == null)
                    return;
                if (mIvBackText.getVisibility() == View.GONE)
                    mIvBackText.setVisibility(View.VISIBLE);
                if (mIvNextText.getVisibility() == View.GONE && word.length() != 1)
                    mIvNextText.setVisibility(View.VISIBLE);
                if (position <= 0) {
                    mIvBackText.setVisibility(View.GONE);
                    return;
                }
                Flog.d(TAG, "child count=" + mViewPager.getChildCount());
                if (position >= mViewPager.getChildCount() - 1) {
                    mIvNextText.setVisibility(View.GONE);
                    return;
                }
            }

            @Override
            public void onPageSelected(int position) {
                Flog.d(TAG, "2 onPageSelected="+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Flog.d(TAG, "3 onPageSelected= onPageScrollStateChanged="+state);
            }
        });

        mIvNextText = (AppCompatImageView) getView().findViewById(R.id.btn_next_text);
        mIvNextText.setOnClickListener(mOnClickListener);
        Flog.d(TAG, "len of word=" + word.length());
        if (word.length() == 1) {
            mIvNextText.setVisibility(View.GONE);
        }

        mIvBackText = (AppCompatImageView) getView().findViewById(R.id.btn_back_text);
        mIvBackText.setOnClickListener(mOnClickListener);
        mIvBackText.setVisibility(View.GONE);

        mIvBack = (AppCompatImageView) getView().findViewById(R.id.btn_back);
        mIvBack.setOnClickListener(mOnClickListener);

        mIvNext = (AppCompatImageView) getView().findViewById(R.id.btn_next);
        mIvNext.setOnClickListener(mOnClickListener);
    }

    private void highlightTextAt(int idx) {
        mTvPreview.setText(null);
        try {
            Spannable spanText = Spannable.Factory.getInstance().newSpannable(word);
            spanText.setSpan(new BackgroundColorSpan(Color.parseColor(CommonVl.PREVIEW_HIGHLIGHT_COLOR)), idx, idx + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvPreview.setText(spanText);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            mTvPreview.setText(word);
        }
    }

    private void highlightCharInStr(TextView tvPreview, int charPos, String highlightColor) {
        if (true)
            return;
        String text = tvPreview.getText().toString();
        if (charPos < 0 || charPos >= text.length()) {
            tvPreview.setTextColor(Color.BLACK);
            return;
        }
        if (text.charAt(charPos) == ' ') {
            tvPreview.setTextColor(Color.BLACK);
            return;
        }
        String highlight = "<font color='" + highlightColor + "'>" + text.charAt(charPos) + "</font>";
        String preText = text.substring(0, charPos);
        String postText = text.substring(charPos + 1);
        Flog.d(TAG, "preText=" + preText);
        Flog.d(TAG, "charAt=" + text.charAt(charPos));
        Flog.d(TAG, "postText=" + postText);
        String newText = preText + highlight + postText;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvPreview.setText(Html.fromHtml(newText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvPreview.setText(Html.fromHtml(newText));
        }
    }

    @Override
    public void onCharacterViewDown(int id, float w, float h) {
        Flog.d(TAG, "touch on 2");
        mCurId = id;
        mCharWidth = w;
        mCharHeight = h;
        MyGalleryLib.display(mContext, false, mOnHandlerResultCallback);
    }

    @Override
    public void onCharacterViewUp(int id) {

    }

    @Override
    public void onImageLoaded(int id, Bitmap bmp, boolean outOfMemoryError) {
        if (bmp == null)
            return;
        Flog.d(TAG, "onImageLoaded Bitmap: w=" + bmp.getWidth() + "_h=" + bmp.getHeight() + "_oom=" + outOfMemoryError + "_id=" + id);
        CharacterTextView characterTextView = (CharacterTextView) mViewPager.findViewWithTag("extra_" + id);
        Flog.d(TAG, "onImageLoaded characterTextView=" + characterTextView);
        characterTextView.setBitmap(bmp);
//        mTextAdapter.notifyDataSetChanged();
    }

    public void setListener(OnTextCollageFragListener listener) {
        this.listener = listener;
    }
}
