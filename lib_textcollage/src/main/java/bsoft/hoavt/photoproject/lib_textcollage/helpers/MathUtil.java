package bsoft.hoavt.photoproject.lib_textcollage.helpers;

/**
 * Created by vutha on 7/12/2017.
 */

public class MathUtil {
    private static final String TAG = MathUtil.class.getSimpleName();

    public static int[] getFitCenterImgSize(int wHolder, int hHolder, int wImg, int hImg) {
        int[] ans = new int[2]; // ans[0]: width and ans[1]: height
        if (wHolder >= wImg && hHolder >= hImg) {
            ans[0] = wImg;
            ans[1] = hImg;
        } else if (wHolder >= wImg && hHolder < hImg) {
            ans[1] = hHolder;
            ans[0] = wImg * hHolder / hImg;
        } else if (wHolder < wImg && hHolder >= hImg) {
            ans[0] = wHolder;
            ans[1] = hImg * wHolder / wImg;
        } else if (wHolder < wImg && hHolder < hImg) {
            float rW = wImg * 1F / wHolder;
            float rH = hImg * 1F / hHolder;
            Flog.d(TAG, "ratio: w=" + rW + "_h=" + rH + "_compare=" + Float.compare(rW, rH));
            if (Float.compare(rW, rH) >= 0) {
                ans[0] = wHolder;
                ans[1] = hImg * wHolder / wImg;
            } else {
                ans[1] = hHolder;
                ans[0] = wImg * hHolder / hImg;
            }
        } else {
            Flog.d(TAG, "Causion!! wH=" + wHolder + "_hH=" + hHolder + "_wI=" + wImg + "_hI=" + hImg + "   :This case not captured!");
        }
        return ans;
    }

    public static int[] getCenterCropImgSize(int wHolder, int hHolder, int wImg, int hImg) {
        int[] ans = new int[2]; // ans[0]: width and ans[1]: height

        float rW = wHolder * 1F / wImg;
        float rH = hHolder * 1F / hImg;

        if ((wHolder >= wImg && hHolder >= hImg) || (wHolder < wImg && hHolder < hImg)) {
            if (Float.compare(rW, rH) >= 0) {
                ans[0] = wHolder;
                ans[1] = wHolder * hImg / wImg;
            } else {
                ans[1] = hHolder;
                ans[0] = hHolder * wImg / hImg;
            }
        } else {
            if (wHolder >= wImg) {
                ans[0] = wHolder;
                ans[1] = wHolder * hImg / wImg;
            } else {    // hHolder >= hImg
                ans[1] = hHolder;
                ans[0] = hHolder * wImg / hImg;
            }
        }

        return ans;
    }
}
