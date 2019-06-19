package com.lib.collageview.helpers.svg;

import android.graphics.Path;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vutha on 3/22/2017.
 */

public class SVGItem implements Parcelable {
    public int numImgs = 0;
    public ArrayList<String> pathData;
    public ArrayList<Path> pathDataList;
    public float viewportWidth;
    public float viewportHeight;

    public SVGItem() {
        this.pathData = new ArrayList<>();
        this.pathDataList = new ArrayList<>();
    }

    public SVGItem(ArrayList<String> pathData, float viewportWidth, float viewportHeight) {
        this.pathData = pathData;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
    }


    protected SVGItem(Parcel in) {
        numImgs = in.readInt();
        pathData = in.createStringArrayList();
        viewportWidth = in.readFloat();
        viewportHeight = in.readFloat();
    }

    public static final Creator<SVGItem> CREATOR = new Creator<SVGItem>() {
        @Override
        public SVGItem createFromParcel(Parcel in) {
            return new SVGItem(in);
        }

        @Override
        public SVGItem[] newArray(int size) {
            return new SVGItem[size];
        }
    };

    /**
     * Clear content of svg item.
     * */
    public void clear() {

        if (pathDataList != null) {
            pathDataList.clear();
            pathDataList = null;
        }

        if (pathData != null) {
            pathData.clear();
            pathData = null;
        }
        numImgs = 0;
        viewportHeight = 0f;
        viewportWidth = 0f;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(numImgs);
        parcel.writeStringList(pathData);
        parcel.writeFloat(viewportWidth);
        parcel.writeFloat(viewportHeight);
    }
}
