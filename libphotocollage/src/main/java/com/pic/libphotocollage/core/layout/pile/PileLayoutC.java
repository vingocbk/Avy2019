package com.pic.libphotocollage.core.layout.pile;

import com.pic.libphotocollage.core.util.BaseStatistic;

/**
 * Created by hoavt on 10/08/2016.
 */
public class PileLayoutC {

    private static final float[][] mPilePreset2_1to1 = {
            {0.0417f, 0.2556f, 0.4694f, 0.7917f,},
            {0.4889f, 0.2639f, 0.9667f, 0.6417f,},
    };

    private static final float[][] mPilePreset2_9to16 = {
            {0.0139f, 0.2953f, 0.5444f, 0.6734f,},
            {0.4944f, 0.2875f, 0.9611f, 0.6266f,},
    };

    private static final float[][] mPilePreset3_1to1 = {
            {0.6583f, 0.2889f, 0.9694f, 0.6833f,},
            {0.3194f, 0.3222f, 0.7361f, 0.6472f,},
            {0.0194f, 0.2806f, 0.3833f, 0.7389f,},
    };

    private static final float[][] mPilePreset3_9to16 = {
            {0.2361f, 0.5969f, 0.7111f, 0.8078f,},
            {0.5222f, 0.1938f, 0.9556f, 0.5000f,},
            {0.0556f, 0.1953f, 0.4778f, 0.5000f,},
    };

    private static final float[][] mPilePreset4_1to1 = {
            {0.1028f, 0.5444f, 0.4972f, 0.8528f,},
            {0.5444f, 0.5306f, 0.8889f, 0.9694f,},
            {0.1250f, 0.1000f, 0.4389f, 0.5056f,},
            {0.5167f, 0.1028f, 0.9194f, 0.4139f,},};

    private static final float[][] mPilePreset4_9to16 = {
            {0.0167f, 0.5672f, 0.5194f, 0.7891f,},
            {0.5778f, 0.5656f, 0.9500f, 0.8313f,},
            {0.0306f, 0.1859f, 0.4444f, 0.4813f,},
            {0.4694f, 0.2047f, 0.9917f, 0.4328f,},};

    private static final float[][] mPilePreset5_1to1 = {
            {0.6778f, 0.5806f, 0.9417f, 0.9111f,},
            {0.3722f, 0.5694f, 0.6500f, 0.9194f,},
            {0.0389f, 0.5889f, 0.3361f, 0.8167f,},
            {0.1194f, 0.1333f, 0.4361f, 0.5306f,},
            {0.5250f, 0.1528f, 0.9056f, 0.4472f,},};

    private static final float[][] mPilePreset5_9to16 = {
            {0.6722f, 0.1828f, 1.2056f, 0.4219f,},
            {0.2833f, 0.1859f, 0.7611f, 0.5172f,},
            {-0.0722f, 0.1859f, 0.3389f, 0.4813f,},
            {0.0167f, 0.5672f, 0.5194f, 0.7891f,},
            {0.5778f, 0.5656f, 0.9500f, 0.8313f,},};

    private static final float[][] mPilePreset6_1to1 = {
            {0.6750f, 0.1333f, 0.9583f, 0.4917f,},
            {0.3278f, 0.1639f, 0.6806f, 0.4361f,},
            {0.0194f, 0.1083f, 0.3389f, 0.5056f,},
            {0.6778f, 0.5806f, 0.9417f, 0.9111f,},
            {0.3722f, 0.5694f, 0.6500f, 0.9194f,},
            {0.0389f, 0.5889f, 0.3361f, 0.8167f,},};

    private static final float[][] mPilePreset6_9to16 = {
            {0.6722f, 0.1828f, 1.2056f, 0.4219f,},
            {0.2833f, 0.1859f, 0.7611f, 0.5172f,},
            {-0.0722f, 0.1828f, 0.3389f, 0.4813f,},
            {0.7250f, 0.5672f, 1.0972f, 0.8313f,},
            {0.3333f, 0.5766f, 0.7056f, 0.8484f,},
            {-0.1083f, 0.5719f, 0.3778f, 0.7938f,},};

    private static final float[][][] mPilePath2_1to1 = {
            {{0.0417f, 0.2833f,}, {0.4333f, 0.2556f,},
                    {0.4694f, 0.7667f,}, {0.0778f, 0.7917f,}},
            {{0.5028f, 0.2639f,}, {0.9667f, 0.2833f,},
                    {0.9528f, 0.6417f,}, {0.4889f, 0.6222f,}},};

    private static final float[][][] mPilePath3_1to1 = {
            {{0.6861f, 0.2889f,}, {0.9694f, 0.3111f,},
                    {0.9389f, 0.6833f,}, {0.6583f, 0.6611f,}},
            {{0.3333f, 0.3222f,}, {0.7361f, 0.3389f,},
                    {0.7222f, 0.6472f,}, {0.3194f, 0.6306f,}},
            {{0.0194f, 0.3028f,}, {0.3528f, 0.2806f,},
                    {0.3833f, 0.7167f,}, {0.0500f, 0.7389f,}},};

    private static final float[][][] mPilePath4_1to1 = {
            {{0.1028f, 0.5667f,}, {0.4778f, 0.5444f,},
                    {0.4972f, 0.8306f,}, {0.1222f, 0.8528f,}},
            {{0.5639f, 0.5306f,}, {0.8889f, 0.5444f,},
                    {0.8667f, 0.9694f,}, {0.5444f, 0.9528f,}},
            {{0.1250f, 0.1083f,}, {0.4278f, 0.1000f,},
                    {0.4389f, 0.4972f,}, {0.1361f, 0.5056f,}},
            {{0.5222f, 0.1028f,}, {0.9194f, 0.1111f,},
                    {0.9139f, 0.4139f,}, {0.5167f, 0.4056f,}},};

    private static final float[][][] mPilePath5_1to1 = {
            {{0.6778f, 0.6056f,}, {0.9111f, 0.5806f,},
                    {0.9417f, 0.8889f,}, {0.7083f, 0.9111f,}},
            {{0.4000f, 0.5694f,}, {0.6500f, 0.5889f,},
                    {0.6222f, 0.9194f,}, {0.3722f, 0.9000f,}},
            {{0.0444f, 0.5889f,}, {0.3361f, 0.5944f,},
                    {0.3306f, 0.8167f,}, {0.0389f, 0.8111f,}},
            {{0.1194f, 0.1556f,}, {0.4056f, 0.1333f,},
                    {0.4361f, 0.5083f,}, {0.1500f, 0.5306f,}},
            {{0.5306f, 0.1528f,}, {0.9056f, 0.1611f,},
                    {0.9000f, 0.4472f,}, {0.5250f, 0.4417f,}},};

    private static final float[][][] mPilePath6_1to1 = {
            {{0.7000f, 0.1333f,}, {0.9583f, 0.1556f,},
                    {0.9306f, 0.4917f,}, {0.6750f, 0.4722f,}},
            {{0.3333f, 0.1639f,}, {0.6806f, 0.1694f,},
                    {0.6750f, 0.4361f,}, {0.3278f, 0.4278f,}},
            {{0.0194f, 0.1361f,}, {0.3028f, 0.1083f,},
                    {0.3389f, 0.4750f,}, {0.0583f, 0.5056f,}},
            {{0.6778f, 0.6056f,}, {0.9111f, 0.5806f,},
                    {0.9417f, 0.8889f,}, {0.7083f, 0.9111f,}},
            {{0.4000f, 0.5694f,}, {0.6500f, 0.5889f,},
                    {0.6222f, 0.9194f,}, {0.3722f, 0.9000f,}},
            {{0.0444f, 0.5889f,}, {0.3361f, 0.5944f,},
                    {0.3306f, 0.8167f,}, {0.0389f, 0.8111f,}},};

    private static final float[][][] mPilePath2_9to16 = {
            {{0.0139f, 0.3094f,}, {0.5111f, 0.2953f,},
                    {0.5444f, 0.6594f,}, {0.0472f, 0.6734f,}},
            {{0.5139f, 0.2875f,}, {0.9611f, 0.2969f,},
                    {0.9472f, 0.6266f,}, {0.4944f, 0.6172f,}},};

    private static final float[][][] mPilePath3_9to16 = {
            {{0.2500f, 0.5969f,}, {0.7111f, 0.6094f,},
                    {0.6972f, 0.8078f,}, {0.2361f, 0.7969f,}},
            {{0.5639f, 0.1938f,}, {0.9556f, 0.2125f,},
                    {0.9167f, 0.5000f,}, {0.5222f, 0.4828f,}},
            {{0.0556f, 0.2031f,}, {0.4611f, 0.1953f,},
                    {0.4778f, 0.4922f,}, {0.0722f, 0.5000f,}},
    };

    private static final float[][][] mPilePath4_9to16 = {
            {{0.0333f, 0.5672f,}, {0.5194f, 0.5781f,},
                    {0.5056f, 0.7891f,}, {0.0167f, 0.7781f,}},
            {{0.5778f, 0.5781f,}, {0.9250f, 0.5656f,},
                    {0.9500f, 0.8203f,}, {0.6028f, 0.8313f,}},
            {{0.0306f, 0.1922f,}, {0.4278f, 0.1859f,},
                    {0.4444f, 0.4766f,}, {0.0444f, 0.4813f,}},
            {{0.4750f, 0.2047f,}, {0.9917f, 0.2094f,},
                    {0.9833f, 0.4328f,}, {0.4694f, 0.4281f,}},};

    private static final float[][][] mPilePath5_9to16 = {

            {{0.6722f, 0.1984f,}, {1.1861f, 0.1828f,},
                    {1.2056f, 0.4063f,}, {0.6917f, 0.4219f,}},
            {{0.3417f, 0.1859f,}, {0.7611f, 0.2125f,},
                    {0.7000f, 0.5172f,}, {0.2833f, 0.4906f,}},
            {{-0.0722f, 0.1922f,}, {0.3250f, 0.1859f,},
                    {0.3389f, 0.4750f,}, {-0.0583f, 0.4813f,}},
            {{0.0444f, 0.5672f,}, {0.5194f, 0.5781f,},
                    {0.5056f, 0.7891f,}, {0.0167f, 0.7781f,}},
            {{0.5778f, 0.5781f,}, {0.9222f, 0.5656f,},
                    {0.9500f, 0.8203f,}, {0.6028f, 0.8313f,}},};

    private static final float[][][] mPilePath6_9to16 = {

            {{0.6722f, 0.1984f,}, {1.1861f, 0.1828f,},
                    {1.2056f, 0.4063f,}, {0.6917f, 0.4219f,}},
            {{0.3417f, 0.1859f,}, {0.7611f, 0.2125f,},
                    {0.7000f, 0.5172f,}, {0.2833f, 0.4906f,}},
            {{-0.0722f, 0.1922f,}, {0.3250f, 0.1828f,},
                    {0.3389f, 0.4750f,}, {-0.0583f, 0.4813f,}},
            {{0.7250f, 0.5781f,}, {1.0722f, 0.5672f,},
                    {1.0972f, 0.8203f,}, {0.7500f, 0.8313f,}},
            {{0.3333f, 0.5797f,}, {0.7000f, 0.5766f,},
                    {0.7056f, 0.8453f,}, {0.3389f, 0.8484f,}},
            {{-0.0944f, 0.5719f,}, {0.3778f, 0.5828f,},
                    {0.3778f, 0.7938f,}, {-0.1083f, 0.7828f,}},};

    public static float[][] getCollageLayoutByName(String nameLayout, int ratio) {
        switch (nameLayout) {
            case "pile_c_3":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePreset3_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePreset3_9to16;
            case "pile_c_4":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePreset4_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePreset4_9to16;
            case "pile_c_5":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePreset5_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePreset5_9to16;
            case "pile_c_6":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePreset6_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePreset6_9to16;
            default:
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePreset2_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePreset2_9to16;
        }
        return mPilePreset2_1to1;
    }

    public static float[][][] getPathDateByName(String nameLayout, int ratio) {
        switch (nameLayout) {
            case "pile_c_3":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePath3_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePath3_9to16;
            case "pile_c_4":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePath4_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePath4_9to16;
            case "pile_c_5":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePath5_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePath5_9to16;
            case "pile_c_6":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePath6_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePath6_9to16;
            default:
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePath2_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePath2_9to16;
        }
        return mPilePath2_1to1;
    }
}
