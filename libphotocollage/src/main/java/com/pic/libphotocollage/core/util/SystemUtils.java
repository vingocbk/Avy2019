package com.pic.libphotocollage.core.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.pic.libphotocollage.core.R;
import com.pic.libphotocollage.core.model.ItemData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by hoavt on 02/08/2016.
 */
public class SystemUtils {
    public static final int MAX_MULTIGRID_IMAGE_SIZE = 3000000;
    public static final int MAX_IMAGE_SIZE = 8000000;
    public static final int MAX_DECODE_TIME = 600;
    public static final int MAX_THUMBNAILS_SIZE = 100000;
    private static final String EXTERNAL_STORAGE = Environment.getExternalStorageDirectory()
            + File.separator;
    public static final String SAVE_DIR = EXTERNAL_STORAGE + Environment.DIRECTORY_PICTURES + File.separator + "PhotoCollage";
    public static ArrayList<ItemData> mediaStores = new ArrayList<>();

    public static String getSimpleDate() {
        String ret = null;

        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        ret = sdfNow.format(new Date(System.currentTimeMillis()));
        ret = ret.trim();
        return ret;
    }

    public static int getDpToPixel(Context context, int dpVal) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpVal * density + 0.5);
    }

    public static Uri saveToImage(Context context,
                                  String directory, String filename, Bitmap bitmap,
                                  Bitmap.CompressFormat format) {
        boolean result = false;

        Uri uri = null;
        Uri fail = null;

        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            FileOutputStream fOut = null;

            File saveFile = new File(directory, filename);
            File nf = new File(directory);
            if (!nf.exists()) {
                try {
                    if (!nf.mkdirs()) {
                        return fail; // false
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result = false;
                    return fail;
                }
            }

            try {
                fOut = new FileOutputStream(saveFile);
                bitmap.compress(format, 100, fOut);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                return fail;
            }

            ImageUtils.sync(fOut);

            try {
                fOut.close();
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
                return fail;
            }

            if (ImageUtils.isPng()) {
                ImageUtils.setPng(true);
            } else {
                ImageUtils.setPng(false);
            }

            String title = null;
            Location location = null;
            int orientation = 0;

            try {

                uri = ImageUtils.addImage(context.getContentResolver(), title,
                        System.currentTimeMillis(), location, orientation,
                        directory, filename, width, height);
            } catch (Exception e) {
                e.printStackTrace();
                return fail;
            }

            try {

                context.sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            } catch (Exception e) {
                e.printStackTrace();
                return fail;
            }

            if (uri == null && result == true) {
                uri = Uri.fromFile(saveFile);
            }
        }
        return uri;
    }

    public static void showKeyboardSoft(Context context, View viewFocused) {
//        if (true) return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(viewFocused, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void showKeyboardSoft(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void checkVisibilityKeyboardSoft(final Activity activity, int idResRootView) {
        final View activityRootView = activity.findViewById(idResRootView);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                activityRootView.getWindowVisibleDisplayFrame(r);

                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                    Flog.i("showing");
                } else {
                    Flog.i("hiding");
                }
            }
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

//    private void getHeightKeyboardSoft(Activity activity, int idRootView) {
//        final View root = ac.findViewById(idRootView);
//        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            public void onGlobalLayout() {
//                int heightDiff = root.getRootView().getHeight() - root.getHeight();
//                Flog.i("heightDiff=" + heightDiff);
//                // IF height diff is more then 150, consider keyboard as visible.
//            }
//        });
//    }

    static public int getRotateDegree(String path) {
        int rotation = 0;
        ExifInterface exif;

        try {
            exif = new ExifInterface(path);

            if (exif != null) {

                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
                    rotation = 90;
                else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
                    rotation = 180;
                else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
                    rotation = 270;

            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return rotation;
    }

    static public int getRotateDegree(ContentResolver cr, Uri uri) {
        if (cr != null && uri != null) {
            ExifInterface exif;
            int exifOrientation = -1;
            String path;
            if (uri.toString().contains("/mnt/sdcard")) {
                path = uri.getPath();
            } else {
                String[] proj = {
                        MediaStore.Images.Media.DATA
                };
                Cursor cursor = null;

                try {
                    cursor = cr.query(uri, proj, null, null, null);
                } catch (SQLiteException e) {
                    e.printStackTrace();
                    return -1;
                }
                if (cursor == null) {
                    path = null;
                    return -1;
                }
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                if (cursor.getCount() == 0) {
                    if (cursor != null)
                        cursor.close();
                    cursor = null;
                    return -1;
                }

                if (cursor.isNull(column_index)) {
                    if (cursor != null)
                        cursor.close();
                    cursor = null;

                    String temp = "Unknown Jpeg";
                    path = temp;
                } else {
                    cursor.moveToFirst();
                    String ret = cursor.getString(column_index);
                    if (cursor != null)
                        cursor.close();
                    cursor = null;
                    path = ret;
                }
            }
            if (path == null) {
                return -1;
            }
            try {
                exif = new ExifInterface(path);
                if (exif != null) {
                    exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
                    if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        return 90;
                    } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        return 180;
                    } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        return 270;
                    }
                    return 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public static void initSetAsOrShareViaLayout(Context context, final Intent intent,
                                                 boolean isShareVia) {

        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        Intent shareChooser = null;
        if (isShareVia) {
            shareChooser = Intent.createChooser(intent,
                    context.getString(R.string.share_via));
            LabeledIntent[] extraIntents = intentList
                    .toArray(new LabeledIntent[intentList.size()]);
            shareChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        } else {
            shareChooser = makeSetAsIntent(context, intent);
        }

        shareChooser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(shareChooser);
    }

    public static Intent makeSetAsIntent(Context mContext, Intent intent) {
        Intent shareChooser = null;
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();

        shareChooser = Intent.createChooser(intent,
                mContext.getString(R.string.setas));
        LabeledIntent[] extraIntents = intentList
                .toArray(new LabeledIntent[intentList.size()]);
        shareChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);

        return shareChooser;
    }

    public static Intent makeShareViaIntent(Uri uri) {
        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    public static Intent makeShareViaIntent(ArrayList<Uri> uris) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        return intent;
    }

    public static void show(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static String getString(Context context, int idRes) {
        return context.getResources().getString(idRes);
    }

    public static int getColor(Context context, int idRes) {
        return context.getResources().getColor(idRes);
    }

    public static File checkOrMakeFileDirectory(String directory) {
        File nf = new File(directory);
        if (!nf.exists()) {
            try {
                if (!nf.mkdirs()) {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return nf;
    }

    public static void openAppOnStore(Context context, String pck) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + pck));
        context.startActivity(intent);
    }

    public static void rateApp(Context context) {
        openAppOnStore(context, context.getPackageName());
    }

    public static String getDateFormat(long date) {
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String formatedDate = newFormat.format(date);
        return formatedDate;
    }

    public static void showFileGallery(Context context, File file) {
//        Intent i = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        context.startActivity(intent);
    }

    public static Uri addToLibrary(ContentResolver contentResolver, Context mContext, String imagePath) {
        ContentValues values = new ContentValues(6);
        values.put(MediaStore.Images.Media.TITLE, new File(imagePath).getName());
        values.put(MediaStore.Images.Media.SIZE, (new File(imagePath).length()));
        Flog.d("addToLibrary " + System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DESCRIPTION, BaseStatistic.DES);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
        values.put(MediaStore.Images.Media.DATA, imagePath);
        Uri base = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = null;
        try {
            newUri = contentResolver.insert(base, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        return newUri;
    }

    public static void delImage(ContentResolver contentResolver, String filePath) {
        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{filePath});
    }

    public static void loadImage(ContentResolver contentResolver) {
        Flog.d("loadImage");
        List<String> tempList = new ArrayList<>();
        SimpleDateFormat sdfDay = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
        mediaStores = new ArrayList<>();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE
                        , MediaStore.Images.Media.DATE_ADDED}
                , MediaStore.Images.Media.DESCRIPTION + " = 'collage_img' ", null, MediaStore.Images.Media.DATE_ADDED + " DESC");
        if (cursor == null) {
            Flog.d("loadImage", "222222");
            mediaStores.add(null);
            return;
        }
        Flog.d("loadImage", "33333");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Flog.d("loadImage", "444444");
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
            String data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            int size = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
            long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
            ItemData itemModel = new ItemData(title, data, size, date);
            itemModel.dateString = sdfDay.format(new Date(date * 1000));
            if (!tempList.contains(itemModel.dateString)) {
                itemModel.isHeader = true;
                tempList.add(itemModel.dateString);
            }

            mediaStores.add(itemModel);
            cursor.moveToNext();
        }


    }

    public static void shareMultiple(List<File> files, Context context) {

        ArrayList<Uri> uris = new ArrayList<>();
        for (File file : files) {
            uris.add(Uri.fromFile(file));
        }
        final Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_via)));
    }

}
