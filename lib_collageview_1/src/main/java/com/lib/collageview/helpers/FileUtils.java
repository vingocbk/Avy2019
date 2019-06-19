package com.lib.collageview.helpers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by vutha on 4/12/2017.
 */

public class FileUtils {

    public static final String ALBUM_DIR = "Bsoft_photo_collage";
    public static final String APP_FOLDER = Environment.getExternalStorageDirectory() + File.separator
            + Environment.DIRECTORY_PICTURES + File.separator
            + "Photo Collage" + File.separator;

    public static boolean makeFolder(String absolutePath) {
        boolean rslt = true;
        File file = new File(absolutePath);
        if (file.exists()) return rslt;
        try {
            rslt = file.mkdirs();
        } catch (SecurityException ex) {
            ex.printStackTrace();
            rslt = false;
        }
        return rslt;
    }

    /**
     * add image to ImageUtils ContentProvider
     */
    public static Uri addImage(ContentResolver cr, String title, long dateTaken,
                               Location location, int orientation, String directory,
                               String filename, int width, int height) {

        ContentValues values = new ContentValues();

        if (directory != null)
            if (directory.endsWith("/"))
                directory = directory.substring(0, directory.length() - 1);

        File ftemp = null;
        long filesize = 0;
        if (directory != null && filename != null) {

            ftemp = new File(directory + "/" + filename);
            filesize = ftemp.length();
            ftemp = null;
        }
        Flog.d("DATETAEKEN "+filename);
        if (title == null)
            values.put(MediaStore.Images.Media.TITLE, filename);
        else
            values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
        values.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken);
        values.put(MediaStore.Images.Media.DATE_MODIFIED, dateTaken);
        values.put(MediaStore.Images.Media.DATE_ADDED, dateTaken);
        values.put(MediaStore.Images.Media.DESCRIPTION, ALBUM_DIR);

        if (filename.endsWith(".png")) {
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        } else {
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        }

        values.put(MediaStore.Images.Media.SIZE, filesize);

        values.put(MediaStore.Images.Media.ORIENTATION, orientation);

        if (location != null) {
            values.put(MediaStore.Images.Media.LATITUDE, location.getLatitude());
            values.put(MediaStore.Images.Media.LONGITUDE, location.getLongitude());
        }

        values.put("width", width);
        values.put("height", height);

        if (directory != null && filename != null) {
            String fullfilename = directory + "/" + filename;
            values.put(MediaStore.Images.Media.DATA, fullfilename);
        }

        return cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static void scanMediaStore(Context context) {
        try {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE));
        } catch (Exception e) {
            e.printStackTrace();
            Flog.d("scanMediaStore failed");
        }
    }

    public static void scanMediaStore(Context context, Uri uri) {
        try {
            context.sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (Exception e) {
            e.printStackTrace();
            Flog.d("scanMediaStore failed");
        }
    }
}
