package com.pic.libphotocollage.core.layout.pile;

import com.pic.libphotocollage.core.util.BaseStatistic;

/**
 * Created by hoavt on 10/08/2016.
 */
public class PileLayoutB {
    private static final float[][] mPilePreset2_1to1 = {
            {0.4639f, 0.2611f, 0.9917f, 0.6833f,},
            {0.0667f, 0.2083f, 0.5111f, 0.7806f,},
    };

    private static final float[][] mPilePreset2_9to16 = {
            {0.3194f, 0.5203f, 1.0444f, 0.8500f,},
            {0.0028f, 0.1563f, 0.6667f, 0.6203f,},
    };

    private static final float[][] mPilePreset3_1to1 = {
            {0.4806f, 0.0806f, 0.9222f, 0.4361f,},
            {0.3583f, 0.4833f, 0.7139f, 0.9389f,},
            {0.1250f, 0.1611f, 0.5000f, 0.6444f,},
    };

    private static final float[][] mPilePreset3_9to16 = {
            {0.4528f, 0.2922f, 1.0000f, 0.6828f,},
            {0.1806f, 0.5422f, 0.6750f, 0.9016f,},
            {0.0000f, 0.1328f, 0.6750f, 0.4391f,},
    };

    private static final float[][] mPilePreset4_1to1 = {
            {0.5028f, 0.5028f, 0.8389f, 0.9333f,},
            {0.1000f, 0.5306f, 0.5444f, 0.8806f,},
            {0.4833f, 0.0889f, 0.9278f, 0.4417f,},
            {0.1361f, 0.1444f, 0.4972f, 0.6083f,},};

    private static final float[][] mPilePreset4_9to16 = {
            {0.5556f, 0.4297f, 1.0889f, 0.8000f,},
            {0.0639f, 0.6266f, 0.6556f, 0.8906f,},
            {0.3722f, 0.0891f, 1.0167f, 0.3813f,},
            {-0.0111f, 0.2391f, 0.5167f, 0.6188f,},};

    private static final float[][] mPilePreset5_1to1 = {
            {0.1000f, 0.5028f, 0.5028f, 0.8194f,},
            {0.5417f, 0.4889f, 0.8861f, 0.9250f,},
            {-0.0139f, 0.2056f, 0.3694f, 0.5111f,},
            {0.6528f, 0.2417f, 1.0333f, 0.5472f,},
            {0.3278f, 0.1611f, 0.6889f, 0.6250f,},};

    private static final float[][] mPilePreset5_9to16 = {
            {0.0194f, 0.5000f, 0.6056f, 0.7594f,},
            {0.4250f, 0.7109f, 0.9778f, 0.9578f,},
            {0.0389f, 0.0609f, 0.5250f, 0.4125f,},
            {0.4639f, 0.1047f, 1.0194f, 0.3547f,},
            {0.3194f, 0.3000f, 0.8194f, 0.6563f,},};

    private static final float[][] mPilePreset6_1to1 = {
            {-0.0056f, 0.3722f, 0.3639f, 0.8306f,},
            {0.6694f, 0.3806f, 1.0000f, 0.8028f,},
            {0.2667f, 0.5361f, 0.7472f, 0.9111f,},
            {-0.0139f, 0.2056f, 0.3694f, 0.5111f,},
            {0.5861f, 0.2028f, 0.9722f, 0.5139f,},
            {0.3556f, 0.1417f, 0.7167f, 0.6056f,},};

    private static final float[][] mPilePreset6_9to16 = {
            {0.0556f, 0.6781f, 0.4944f, 0.9844f,},
            {0.4250f, 0.7109f, 0.9750f, 0.9578f,},
            {-0.0361f, 0.4078f, 0.5500f, 0.6656f,},
            {0.5778f, 0.4156f, 0.9778f, 0.7000f,},
            {0.0694f, 0.0250f, 0.5944f, 0.4031f,},
            {0.4500f, 0.1516f, 1.0167f, 0.4094f,},};

    private static final float[][][] mPilePath2_1to1 = {
            {{0.5000f, 0.2611f,}, {0.9917f, 0.3083f,},
                    {0.9528f, 0.6833f,}, {0.4639f, 0.6361f,}},
            {{0.0667f, 0.2167f,}, {0.5000f, 0.2083f,},
                    {0.5111f, 0.7722f,}, {0.0778f, 0.7806f,}},};

    private static final float[][][] mPilePath3_1to1 = {
            {{0.5111f, 0.0806f,}, {0.9222f, 0.1194f,},
                    {0.8917f, 0.4361f,}, {0.4806f, 0.3972f,}},
            {{0.3750f, 0.4833f,}, {0.7139f, 0.4972f,},
                    {0.6944f, 0.9389f,}, {0.3583f, 0.9250f,}},
            {{0.1250f, 0.1694f,}, {0.4889f, 0.1611f,},
                    {0.5000f, 0.6389f,}, {0.1333f, 0.6444f,}},};

    private static final float[][][] mPilePath4_1to1 = {
            {{0.5028f, 0.5139f,}, {0.8250f, 0.5028f,},
                    {0.8389f, 0.9222f,}, {0.5167f, 0.9333f,}},
            {{0.1000f, 0.5583f,}, {0.5222f, 0.5306f,},
                    {0.5444f, 0.8556f,}, {0.1194f, 0.8806f,}},
            {{0.5139f, 0.0889f,}, {0.9278f, 0.1278f,},
                    {0.8972f, 0.4417f,}, {0.4833f, 0.4028f,}},
            {{0.1361f, 0.1528f,}, {0.4861f, 0.1444f,},
                    {0.4972f, 0.5972f,}, {0.1472f, 0.6083f,}},};

    private static final float[][][] mPilePath5_1to1 = {
            {{0.1000f, 0.5222f,}, {0.4889f, 0.5028f,},
                    {0.5028f, 0.8028f,}, {0.1139f, 0.8194f,}},
            {{0.5639f, 0.4889f,}, {0.8861f, 0.5056f,},
                    {0.8639f, 0.9250f,}, {0.5417f, 0.9083f,}},
            {{0.0083f, 0.2056f,}, {0.3694f, 0.2333f,},
                    {0.3472f, 0.5111f,}, {-0.0139f, 0.4806f,}},
            {{0.6528f, 0.2694f,}, {1.0139f, 0.2417f,},
                    {1.0333f, 0.5194f,}, {0.6722f, 0.5472f,}},
            {{0.3278f, 0.1694f,}, {0.6778f, 0.1611f,},
                    {0.6889f, 0.6167f,}, {0.3417f, 0.6250f,}},};

    private static final float[][][] mPilePath6_1to1 = {
            {{-0.0056f, 0.4056f,}, {0.3222f, 0.3722f,},
                    {0.3639f, 0.7972f,}, {0.0389f, 0.8306f,}},
            {{0.6889f, 0.3806f,}, {1.0000f, 0.3972f,},
                    {0.9778f, 0.8028f,}, {0.6694f, 0.7861f,}},
            {{0.2667f, 0.5583f,}, {0.7278f, 0.5361f,},
                    {0.7472f, 0.8889f,}, {0.2861f, 0.9111f,}},
            {{0.0083f, 0.2056f,}, {0.3694f, 0.2333f,},
                    {0.3472f, 0.5111f,}, {-0.0139f, 0.4806f,}},
            {{0.5861f, 0.2389f,}, {0.9472f, 0.2028f,},
                    {0.9722f, 0.4806f,}, {0.6111f, 0.5139f,}},
            {{0.3556f, 0.1528f,}, {0.7056f, 0.1417f,},
                    {0.7167f, 0.5972f,}, {0.3694f, 0.6056f,}},};

    private static final float[][][] mPilePath2_9to16 = {
            {{0.3694f, 0.5203f,}, {1.0444f, 0.5563f,},
                    {0.9944f, 0.8500f,}, {0.3194f, 0.8141f,}},
            {{0.0028f, 0.1844f,}, {0.6028f, 0.1563f,},
                    {0.6667f, 0.5922f,}, {0.0667f, 0.6203f,}},};

    private static final float[][][] mPilePath3_9to16 = {
            {{0.4806f, 0.2922f,}, {1.0000f, 0.3047f,},
                    {0.9694f, 0.6828f,}, {0.4528f, 0.6703f,}},
            {{0.1806f, 0.5484f,}, {0.6639f, 0.5422f,},
                    {0.6750f, 0.8969f,}, {0.1917f, 0.9016f,}},
            {{0.0472f, 0.1328f,}, {0.6750f, 0.1656f,},
                    {0.6278f, 0.4391f,}, {0.0000f, 0.4047f,}},};

    private static final float[][][] mPilePath4_9to16 = {
            {{0.5556f, 0.4578f,}, {1.0222f, 0.4297f,},
                    {1.0889f, 0.7719f,}, {0.6194f, 0.8000f,}},
            {{0.0639f, 0.6469f,}, {0.6250f, 0.6266f,},
                    {0.6556f, 0.8703f,}, {0.0917f, 0.8906f,}},
            {{0.4167f, 0.0891f,}, {1.0167f, 0.1203f,},
                    {0.9722f, 0.3813f,}, {0.3722f, 0.3453f,}},
            {{-0.0111f, 0.2469f,}, {0.4972f, 0.2391f,},
                    {0.5167f, 0.6125f,}, {0.0083f, 0.6188f,}},};

    private static final float[][][] mPilePath5_9to16 = {
            {{0.0194f, 0.5156f,}, {0.5833f, 0.5000f,},
                    {0.6056f, 0.7438f,}, {0.0417f, 0.7594f,}},
            {{0.4500f, 0.7109f,}, {0.9778f, 0.7297f,},
                    {0.9500f, 0.9578f,}, {0.4250f, 0.9391f,}},
            {{0.0389f, 0.0688f,}, {0.5083f, 0.0609f,},
                    {0.5250f, 0.4047f,}, {0.0556f, 0.4125f,}},
            {{0.4639f, 0.1266f,}, {0.9889f, 0.1047f,},
                    {1.0194f, 0.3313f,}, {0.4944f, 0.3547f,}},
            {{0.3528f, 0.3000f,}, {0.8194f, 0.3141f,},
                    {0.7861f, 0.6563f,}, {0.3194f, 0.6422f,}},};

    private static final float[][][] mPilePath6_9to16 = {
            {{0.0556f, 0.7000f,}, {0.4417f, 0.6781f,},
                    {0.4944f, 0.9625f,}, {0.1083f, 0.9844f,}},
            {{0.4472f, 0.7109f,}, {0.9750f, 0.7281f,},
                    {0.9528f, 0.9578f,}, {0.4250f, 0.9391f,}},
            {{-0.0194f, 0.4078f,}, {0.5500f, 0.4156f,},
                    {0.5417f, 0.6656f,}, {-0.0361f, 0.6578f,}},
            {{0.6083f, 0.4156f,}, {0.9778f, 0.4281f,},
                    {0.9472f, 0.7000f,}, {0.5778f, 0.6875f,}},
            {{0.0694f, 0.0328f,}, {0.5750f, 0.0250f,},
                    {0.5944f, 0.3953f,}, {0.0889f, 0.4031f,}},
            {{0.4500f, 0.1813f,}, {0.9750f, 0.1516f,},
                    {1.0167f, 0.3797f,}, {0.4889f, 0.4094f,}},};

    public static float[][] getCollageLayoutByName(String nameLayout, int ratio) {
        switch (nameLayout) {
            case "pile_b_3":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePreset3_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePreset3_9to16;
            case "pile_b_4":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePreset4_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePreset4_9to16;
            case "pile_b_5":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePreset5_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePreset5_9to16;
            case "pile_b_6":
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
            case "pile_b_3":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePath3_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePath3_9to16;
            case "pile_b_4":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePath4_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePath4_9to16;
            case "pile_b_5":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePath5_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePath5_9to16;
            case "pile_b_6":
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
