package bsoft.hoavt.photoproject.lib_textcollage.helpers.gallery;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import bsoft.hoavt.photoproject.lib_textcollage.R;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.gallery.listener.GlidePauseOnScrollListener;
import bsoft.hoavt.photoproject.lib_textcollage.helpers.gallery.loader.GlideImageLoader;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.GalleryFinal.OnHanlderResultCallback;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by vutha on 7/12/2017.
 */

public class MyGalleryLib {

    private static final int REQUEST_CODE_GALLERY = 1001;

    public static void display(Activity activity, ThemeConfig themeConfigParams, boolean multiChoiceMode,
                               int maxChoice, boolean animation, OnHanlderResultCallback onHanlderResultCallback) {
        ThemeConfig themeConfig = themeConfigParams;
//        ThemeConfig themeConfig = null;
//
//        if (mRbThemeDefault.isChecked()) {
//            themeConfig = ThemeConfig.DEFAULT;
//        } else if (mRbThemeDark.isChecked()) {
//            themeConfig = ThemeConfig.DARK;
//        } else if (mRbThemeCyan.isChecked()) {
//            themeConfig = ThemeConfig.CYAN;
//        } else if (mRbThemeOrange.isChecked()) {
//            themeConfig = ThemeConfig.ORANGE;
//        } else if (mRbThemeGreen.isChecked()) {
//            themeConfig = ThemeConfig.GREEN;
//        } else if (mRbThemeTeal.isChecked()) {
//            themeConfig = ThemeConfig.TEAL;
//        } else if (mRbThemeCustom.isChecked()) {
//            ThemeConfig theme = new ThemeConfig.Builder()
//                    .setTitleBarBgColor(Color.rgb(0xFF, 0x57, 0x22))
//                    .setTitleBarTextColor(Color.BLACK)
//                    .setTitleBarIconColor(Color.BLACK)
//                    .setFabNornalColor(Color.RED)
//                    .setFabPressedColor(Color.BLUE)
//                    .setCheckNornalColor(Color.WHITE)
//                    .setCheckSelectedColor(Color.BLACK)
//                    .setIconBack(R.mipmap.ic_action_previous_item)
//                    .setIconRotate(R.mipmap.ic_action_repeat)
//                    .setIconCrop(R.mipmap.ic_action_crop)
//                    .setIconCamera(R.mipmap.ic_action_camera)
//                    .build();
//            themeConfig = theme;
//        }

        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        cn.finalteam.galleryfinal.ImageLoader imageLoader;
        PauseOnScrollListener pauseOnScrollListener = null;

        imageLoader = new GlideImageLoader();
        pauseOnScrollListener = new GlidePauseOnScrollListener(false, true);

        boolean muti = false;
        if (!multiChoiceMode) {
//        if (mRbSingleSelect.isChecked()) {
            muti = false;
        } else {
            muti = true;
//            if (TextUtils.isEmpty(mEtMaxSize.getText().toString())) {
//                Toast.makeText(getApplicationContext(), "请输入MaxSize", Toast.LENGTH_SHORT).show();
//                return;
//            }
            int maxSize = maxChoice;
//            int maxSize = Integer.parseInt(mEtMaxSize.getText().toString());
            functionConfigBuilder.setMutiSelectMaxSize(maxSize);
        }
        final boolean mutiSelect = muti;

//        if (mCbEdit.isChecked()) {
        functionConfigBuilder.setEnableEdit(true);
//        }

//        if (mCbRotate.isChecked()) {
        functionConfigBuilder.setEnableRotate(true);
//            if (mCbRotateReplaceSource.isChecked()) {
//        functionConfigBuilder.setRotateReplaceSource(true);
//            }
//        }

//        if (mCbCrop.isChecked()) {
            functionConfigBuilder.setEnableCrop(true);
//            if (!TextUtils.isEmpty(mEtCropWidth.getText().toString())) {
//                int width = Integer.parseInt(mEtCropWidth.getText().toString());
//                functionConfigBuilder.setCropWidth(width);
//            }

//            if (!TextUtils.isEmpty(mEtCropHeight.getText().toString())) {
//                int height = Integer.parseInt(mEtCropHeight.getText().toString());
//                functionConfigBuilder.setCropHeight(height);
//            }

//            if (mCbCropSquare.isChecked()) {
        functionConfigBuilder.setCropSquare(true);
//            }
//            if (mCbCropReplaceSource.isChecked()) {
//        functionConfigBuilder.setCropReplaceSource(true);
//            }
//            if (mCbOpenForceCrop.isChecked() && mRbSingleSelect.isChecked()) {
//                functionConfigBuilder.setForceCrop(true);
//                if (mCbOpenForceCropEdit.isChecked()) {
//                    functionConfigBuilder.setForceCropEdit(true);
//                }
//            }
//        }

//        if (mCbShowCamera.isChecked()) {
        functionConfigBuilder.setEnableCamera(true);
//        }
//        if (mCbPreview.isChecked()) {
        functionConfigBuilder.setEnablePreview(true);
//        }

//        functionConfigBuilder.setSelected(mPhotoList);//添加过滤集合
        final FunctionConfig functionConfig = functionConfigBuilder.build();


        CoreConfig coreConfig = new CoreConfig.Builder(activity, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(animation)
                .build();
        cn.finalteam.galleryfinal.GalleryFinal.init(coreConfig);

        if (mutiSelect) {
            cn.finalteam.galleryfinal.GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, onHanlderResultCallback);
        } else {
            cn.finalteam.galleryfinal.GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, onHanlderResultCallback);
        }
    }

    public static void display(Activity activity, boolean multiChoiceMode, OnHanlderResultCallback onHanlderResultCallback) {
        ThemeConfig theme = new ThemeConfig.Builder()
                    .setTitleBarBgColor(ContextCompat.getColor(activity, R.color.color_primary_dark))
                    .setTitleBarTextColor(Color.BLACK)
                    .setTitleBarIconColor(Color.BLACK)
                    .setFabNornalColor(ContextCompat.getColor(activity, R.color.color_primary_dark))
                    .setFabPressedColor(ContextCompat.getColor(activity, R.color.color_accent))
                    .setIconFab(R.drawable.ic_ok)
                    .setCheckNornalColor(Color.WHITE)
                    .setCheckSelectedColor(Color.BLACK)
                    .build();
        ThemeConfig themeConfig = theme;

        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        cn.finalteam.galleryfinal.ImageLoader imageLoader;
        PauseOnScrollListener pauseOnScrollListener = null;

        imageLoader = new GlideImageLoader();
        pauseOnScrollListener = new GlidePauseOnScrollListener(false, true);

        boolean muti = false;
        if (!multiChoiceMode) {
            muti = false;
        } else {
            muti = true;
        }
        final boolean mutiSelect = muti;

        functionConfigBuilder.setEnableEdit(true);

        functionConfigBuilder.setEnableRotate(true);
//        functionConfigBuilder.setRotateReplaceSource(true);
        functionConfigBuilder.setEnableCrop(true);
        functionConfigBuilder.setCropSquare(true);
//        functionConfigBuilder.setCropReplaceSource(true);
        functionConfigBuilder.setEnableCamera(true);
        functionConfigBuilder.setEnablePreview(true);
        final FunctionConfig functionConfig = functionConfigBuilder.build();

        CoreConfig coreConfig = new CoreConfig.Builder(activity, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(false)
                .build();
        cn.finalteam.galleryfinal.GalleryFinal.init(coreConfig);

        if (mutiSelect) {
            cn.finalteam.galleryfinal.GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, onHanlderResultCallback);
        } else {
            cn.finalteam.galleryfinal.GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, onHanlderResultCallback);
        }
    }

    public static void cleanCacheFile() {
        GalleryFinal.cleanCacheFile();
    }
}
