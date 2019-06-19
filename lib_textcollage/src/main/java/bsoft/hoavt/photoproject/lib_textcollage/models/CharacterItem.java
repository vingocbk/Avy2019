package bsoft.hoavt.photoproject.lib_textcollage.models;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import bsoft.hoavt.photoproject.lib_textcollage.helpers.BitmapUtil;

/**
 * Created by vutha on 7/10/2017.
 */

public class CharacterItem {

    private int id;
    private String text;
    private Bitmap bitmap;
    private Matrix matrix;
    private int textHeight;
    private float dis2Vertices;

    public CharacterItem() {
        id = -1;
        text = "";
        matrix = new Matrix();
        textHeight = 0;
        dis2Vertices = 0F;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = BitmapUtil.recycle(this.bitmap);
        this.bitmap = bitmap;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix.set(matrix);
    }

    public float getDis2Vertices() {
        return dis2Vertices;
    }

    public void setDis2Vertices(float dis2Vertices) {
        this.dis2Vertices = dis2Vertices;
    }

    public int getTextHeight() {
        return textHeight;
    }

    public void setTextHeight(int textHeight) {
        this.textHeight = textHeight;
    }
}
