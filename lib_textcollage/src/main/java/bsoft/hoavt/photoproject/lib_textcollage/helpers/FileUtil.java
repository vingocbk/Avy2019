package bsoft.hoavt.photoproject.lib_textcollage.helpers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vutha on 7/12/2017.
 */

public class FileUtil {

    public static final int SUFFICIENT_MEMORY_VALUE = 2;    // 2 MB
    private static final long MEGABYTE_VALUE = 0x100000L;

    public static int[] getImgSize(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        return new int[]{imageWidth, imageHeight};
    }

    public static String getExtension(String fileName) {
        String extension = null;

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    public static boolean isSufficientMemory() {
        int availableMegs = (int) Math.floor(getAvailableMemorySize() / MEGABYTE_VALUE);
        Flog.d("megAvailable=" + availableMegs);
        return availableMegs < SUFFICIENT_MEMORY_VALUE;
    }

    private static long getAvailableMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = 0, availableBlocks = 0;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.getBlockSize();
                availableBlocks = stat.getAvailableBlocks();
            } else {
                blockSize = stat.getBlockSizeLong();
                availableBlocks = stat.getAvailableBlocksLong();
            }
            return (availableBlocks * blockSize);
        } else {
            return -1;
        }
    }

    private static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return formatSize(availableBlocks * blockSize);
        } else {
            return "ERROR";
        }
    }

    private static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    private static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

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

    public static String getSimpleDate() {
        String ret = null;

        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        ret = sdfNow.format(new Date(System.currentTimeMillis()));
        ret = ret.trim();
        return ret;
    }

    public static String TIME_STAMP = "HH:mm:ss";
    public static String convertLongToDate(long timestamp) {
//       MM dd yyyy
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
        return formatter.format(timestamp);
    }

    public static String getDate(long milliSeconds, String dateFormat){
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
