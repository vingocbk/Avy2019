package cn.finalteam.galleryfinal.model;

import android.media.ExifInterface;

import java.io.IOException;
import java.io.Serializable;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2016/1/20 0020 20:22
 */
public class PhotoTempModel implements Serializable{
    private int orientation;
    private String sourcePath;

    public PhotoTempModel(String path) {
        sourcePath = path;
        orientation = getOrientation(path);
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private int getOrientation(String path) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            return rotationInDegrees;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }
}
