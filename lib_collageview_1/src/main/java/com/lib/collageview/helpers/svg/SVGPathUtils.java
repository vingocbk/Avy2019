package com.lib.collageview.helpers.svg;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Xml;

import com.lib.collageview.helpers.Flog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by thuck on 3/1/2017.
 */

public class SVGPathUtils {
    private static final String SHAPE_PATH = "path";
    private static final String HEADER = "vector";
    private static final String PATH_DATA = "pathData";
    private static final String VIEWPORT_WIDTH = "viewportWidth";
    private static final String VIEWPORT_HEIGHT = "viewportHeight";


    public static SVGItem getSVGItem(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
//        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        parser.setInput(inputStream, null);
        SVGItem item = new SVGItem();


        final String namespace = parser.getNamespace();
        int eventType = parser.getEventType();
        while(eventType != XmlPullParser.END_DOCUMENT){
            if(eventType == XmlPullParser.START_TAG){

                final String tagName = parser.getName();

                switch (tagName) {
                    case SHAPE_PATH:

//                        Flog.d("getAttributeCount: " + parser.getAttributeCount());//2//
//                        Flog.d("getAttributeCount: " + parser.getAttributeNamespace(0));//http://schemas.android.com/apk/res/android
//                        Flog.d("getAttributeCount: " + parser.getAttributeName(0));//pathData
//                        Flog.d("getAttributeCount: " + parser.getAttributePrefix(0));//android
//                        Flog.d("getAttributeCount: " + parser.getAttributeValue(0));//android
//                        Flog.d("getAttributeCount: " + parser.getAttributeValue(namespace, PATH_DATA));//android

                        String pathDataVal = parser.getAttributeValue(namespace, PATH_DATA);
                        item.pathData.add(pathDataVal);
                        item.pathDataList.add(SVGParser.parsePath(pathDataVal));
                        break;
                    case HEADER:
                        String viewportWidthVal = parser.getAttributeValue(namespace, VIEWPORT_WIDTH);
                        String viewportHeightVal = parser.getAttributeValue(namespace, VIEWPORT_HEIGHT);

                        item.viewportWidth = Float.parseFloat(viewportWidthVal);
                        item.viewportHeight = Float.parseFloat(viewportHeightVal);

                        break;
                }
            }

            eventType = parser.next();
        }


        item.numImgs = item.pathData.size();

        return item;
    }

    public static SVGItem getSVGItem(Resources res, int resId) throws XmlPullParserException, IOException {
        XmlResourceParser parser = res.getXml(resId);
        int type;
        while ((type = parser.next()) != XmlPullParser.START_TAG &&
                type != XmlPullParser.END_DOCUMENT) {
            // Empty loop
        }

        SVGItem item = new SVGItem();
        // Parse everything until the end of the vector element.
        int eventType = parser.getEventType();
        final int innerDepth = parser.getDepth() + 1;
        String namespace = parser.getAttributeNamespace(0);
        while (eventType != XmlPullParser.END_DOCUMENT
                && (parser.getDepth() >= innerDepth || eventType != XmlPullParser.END_TAG)) {
            if (eventType == XmlPullParser.START_TAG) {
                final String tagName = parser.getName();
                switch (tagName) {
                    case SHAPE_PATH:
                        item.pathData.add(parser.getAttributeValue(namespace, PATH_DATA));
                        break;
                    case HEADER:
                        item.viewportWidth = parser.getAttributeFloatValue(namespace, VIEWPORT_WIDTH, -1);
                        item.viewportHeight = parser.getAttributeFloatValue(namespace, VIEWPORT_HEIGHT, -1);
                        break;
                }
            }
            eventType = parser.next();
        }
        item.numImgs = item.pathData.size();

        return item;
    }

    public static Path zoomPath(Path srcPath, float scaleValue) {
        Matrix scaleMatrix = new Matrix();
        RectF rectF = new RectF();
        srcPath.computeBounds(rectF, true);
        scaleMatrix.setScale(scaleValue, scaleValue, rectF.centerX(), rectF.centerY());
        srcPath.transform(scaleMatrix);
        return srcPath;
    }
}
