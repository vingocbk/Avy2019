package com.pic.libphotocollage.core.layout.pile;

import com.pic.libphotocollage.core.util.BaseStatistic;

/**
 * Created by hoavt on 10/08/2016.
 */
public class PileLayoutD {

    private static final float[][] mPilePreset2_1to1 = {
            {0.1250f, 0.3250f, 0.3722f, 0.6528f,},
            {0.6111f, 0.3250f, 0.8611f, 0.6528f,},
    };

    private static final float[][] mPilePreset2_9to16 = {
            {0.2667f, 0.1813f, 0.7333f, 0.3813f,},
            {0.2667f, 0.6125f, 0.7333f, 0.8125f,},
    };

    private static final float[][] mPilePreset3_1to1 = {
            {0.1361f, 0.1611f, 0.3861f, 0.4889f,},
            {0.6056f, 0.3944f, 0.8861f, 0.5639f,},
            {0.3194f, 0.7361f, 0.5167f, 0.8667f,},
    };

    private static final float[][] mPilePreset3_9to16 = {
            {0.1556f, 0.1313f, 0.4528f, 0.3516f,},
            {0.5139f, 0.5422f, 0.8500f, 0.6578f,},
            {0.1889f, 0.7813f, 0.4250f, 0.8688f,},
    };

    private static final float[][] mPilePreset4_1to1 = {
            {0.1083f, 0.3361f, 0.3917f, 0.5056f,},
            {0.6000f, 0.2444f, 0.8500f, 0.5722f,},
            {0.2056f, 0.6694f, 0.3833f, 0.8222f,},
            {0.5444f, 0.8000f, 0.6889f, 0.9139f,},};

    private static final float[][] mPilePreset4_9to16 = {
            {0.1833f, 0.0625f, 0.4778f, 0.1734f,},
            {0.3361f, 0.3656f, 0.6528f, 0.6000f,},
            {0.1389f, 0.7984f, 0.3639f, 0.9078f,},
            {0.6333f, 0.7984f, 0.8583f, 0.9078f,},};

    private static final float[][] mPilePreset5_1to1 = {
            {0.1083f, 0.2472f, 0.3889f, 0.4194f,},
            {0.6083f, 0.1500f, 0.8583f, 0.4056f,},
            {0.1222f, 0.5861f, 0.3028f, 0.7389f,},
            {0.4611f, 0.5833f, 0.6000f, 0.8139f,},
            {0.7389f, 0.5778f, 0.8750f, 0.6972f,},};

    private static final float[][] mPilePreset5_9to16 = {
            {0.1556f, 0.1750f, 0.3556f, 0.3063f,},
            {0.5944f, 0.0922f, 0.8194f, 0.3000f,},
            {0.1778f, 0.4359f, 0.4528f, 0.5406f,},
            {0.7056f, 0.4422f, 0.8444f, 0.5297f,},
            {0.3194f, 0.6797f, 0.6472f, 0.8734f,},};

    private static final float[][] mPilePreset6_1to1 = {
            {0.1361f, 0.1833f, 0.3833f, 0.5056f,},
            {0.5694f, 0.1333f, 0.6917f, 0.2500f,},
            {0.6028f, 0.4056f, 0.8806f, 0.5778f,},
            {0.1194f, 0.7333f, 0.2556f, 0.8528f,},
            {0.4000f, 0.7389f, 0.5778f, 0.8889f,},
            {0.7278f, 0.7306f, 0.8694f, 0.8444f,},};

    private static final float[][] mPilePreset6_9to16 = {
            {0.2111f, 0.0813f, 0.7750f, 0.2813f,},
            {0.1472f, 0.4313f, 0.4278f, 0.6078f,},
            {0.6861f, 0.4172f, 0.8194f, 0.4875f,},
            {0.6861f, 0.5938f, 0.8194f, 0.6641f,},
            {0.2000f, 0.7891f, 0.3444f, 0.8797f,},
            {0.5583f, 0.7781f, 0.8806f, 0.8875f,},};

    private static final float[][][] mPilePath2_1to1 = {
            {{0.1250f, 0.3250f,}, {0.3722f, 0.3250f,},
                    {0.3722f, 0.6528f,}, {0.1250f, 0.6528f,}},
            {{0.6111f, 0.3250f,}, {0.8611f, 0.3250f,},
                    {0.8611f, 0.6528f,}, {0.6111f, 0.6528f,}},};

    private static final float[][][] mPilePath3_1to1 = {
            {{0.1361f, 0.1611f,}, {0.3861f, 0.1611f,},
                    {0.3861f, 0.4889f,}, {0.1361f, 0.4889f,}},
            {{0.6056f, 0.3944f,}, {0.8861f, 0.3944f,},
                    {0.8861f, 0.5639f,}, {0.6056f, 0.5639f,}},
            {{0.3194f, 0.7361f,}, {0.5167f, 0.7361f,},
                    {0.5167f, 0.8667f,}, {0.3194f, 0.8667f,}},};

    private static final float[][][] mPilePath4_1to1 = {
            {{0.1083f, 0.3361f,}, {0.3917f, 0.3361f,},
                    {0.3917f, 0.5056f,}, {0.1083f, 0.5056f,}},
            {{0.6000f, 0.2444f,}, {0.8500f, 0.2444f,},
                    {0.8500f, 0.5722f,}, {0.6000f, 0.5722f,}},
            {{0.2056f, 0.6694f,}, {0.3833f, 0.6694f,},
                    {0.3833f, 0.8222f,}, {0.2056f, 0.8222f,}},
            {{0.5444f, 0.8000f,}, {0.6889f, 0.8000f,},
                    {0.6889f, 0.9139f,}, {0.5444f, 0.9139f,}},};

    private static final float[][][] mPilePath5_1to1 = {
            {{0.1083f, 0.2472f,}, {0.3889f, 0.2472f,},
                    {0.3889f, 0.4194f,}, {0.1083f, 0.4194f,}},
            {{0.6083f, 0.1500f,}, {0.8583f, 0.1500f,},
                    {0.8583f, 0.4056f,}, {0.6083f, 0.4056f,}},
            {{0.1222f, 0.5861f,}, {0.3028f, 0.5861f,},
                    {0.3028f, 0.7389f,}, {0.1222f, 0.7389f,}},
            {{0.4611f, 0.5833f,}, {0.6000f, 0.5833f,},
                    {0.6000f, 0.8139f,}, {0.4611f, 0.8139f,}},
            {{0.7389f, 0.5778f,}, {0.8750f, 0.5778f,},
                    {0.8750f, 0.6972f,}, {0.7389f, 0.6972f,}},};

    private static final float[][][] mPilePath6_1to1 = {
            {{0.1361f, 0.1833f,}, {0.3833f, 0.1833f,},
                    {0.3833f, 0.5056f,}, {0.1361f, 0.5056f,}},
            {{0.5694f, 0.1333f,}, {0.6917f, 0.1333f,},
                    {0.6917f, 0.2500f,}, {0.5694f, 0.2500f,}},
            {{0.6028f, 0.4056f,}, {0.8806f, 0.4056f,},
                    {0.8806f, 0.5778f,}, {0.6028f, 0.5778f,}},
            {{0.1194f, 0.7333f,}, {0.2556f, 0.7333f,},
                    {0.2556f, 0.8528f,}, {0.1194f, 0.8528f,}},
            {{0.4000f, 0.7389f,}, {0.5778f, 0.7389f,},
                    {0.5778f, 0.8889f,}, {0.4000f, 0.8889f,}},
            {{0.7278f, 0.7306f,}, {0.8694f, 0.7306f,},
                    {0.8694f, 0.8444f,}, {0.7278f, 0.8444f,}},};

    private static final float[][][] mPilePath2_9to16 = {
            {{0.2667f, 0.1813f,}, {0.7333f, 0.1813f,},
                    {0.7333f, 0.3813f,}, {0.2667f, 0.3813f,}},
            {{0.2667f, 0.6125f,}, {0.7333f, 0.6125f,},
                    {0.7333f, 0.8125f,}, {0.2667f, 0.8125f,}},};

    private static final float[][][] mPilePath3_9to16 = {
            {{0.1556f, 0.1313f,}, {0.4528f, 0.1313f,},
                    {0.4528f, 0.3516f,}, {0.1556f, 0.3516f,}},
            {{0.5139f, 0.5422f,}, {0.8500f, 0.5422f,},
                    {0.8500f, 0.6578f,}, {0.5139f, 0.6578f,}},
            {{0.1889f, 0.7813f,}, {0.4250f, 0.7813f,},
                    {0.4250f, 0.8688f,}, {0.1889f, 0.8688f,}},};

    private static final float[][][] mPilePath4_9to16 = {
            {{0.1833f, 0.0625f,}, {0.4778f, 0.0625f,},
                    {0.4778f, 0.1734f,}, {0.1833f, 0.1734f,}},
            {{0.3361f, 0.3656f,}, {0.6528f, 0.3656f,},
                    {0.6528f, 0.6000f,}, {0.3361f, 0.6000f,}},
            {{0.1389f, 0.7984f,}, {0.3639f, 0.7984f,},
                    {0.3639f, 0.9078f,}, {0.1389f, 0.9078f,}},
            {{0.6333f, 0.7984f,}, {0.8583f, 0.7984f,},
                    {0.8583f, 0.9078f,}, {0.6333f, 0.9078f,}},};

    private static final float[][][] mPilePath5_9to16 = {
            {{0.1556f, 0.1750f,}, {0.3556f, 0.1750f,},
                    {0.3556f, 0.3063f,}, {0.1556f, 0.3063f,}},
            {{0.5944f, 0.0922f,}, {0.8194f, 0.0922f,},
                    {0.8194f, 0.3000f,}, {0.5944f, 0.3000f,}},
            {{0.1806f, 0.4359f,}, {0.4528f, 0.4359f,},
                    {0.4528f, 0.5406f,}, {0.1778f, 0.5406f,}},
            {{0.7056f, 0.4422f,}, {0.8444f, 0.4422f,},
                    {0.8444f, 0.5297f,}, {0.7056f, 0.5297f,}},
            {{0.3194f, 0.6797f,}, {0.6472f, 0.6797f,},
                    {0.6472f, 0.8734f,}, {0.3194f, 0.8734f,}},};

    private static final float[][][] mPilePath6_9to16 = {
            {{0.2111f, 0.0813f,}, {0.7750f, 0.0813f,},
                    {0.7750f, 0.2813f,}, {0.2111f, 0.2813f,}},
            {{0.1472f, 0.4313f,}, {0.4278f, 0.4313f,},
                    {0.4278f, 0.6078f,}, {0.1472f, 0.6078f,}},
            {{0.6861f, 0.4172f,}, {0.8194f, 0.4172f,},
                    {0.8194f, 0.4875f,}, {0.6861f, 0.4875f,}},
            {{0.6861f, 0.5938f,}, {0.8194f, 0.5938f,},
                    {0.8194f, 0.6641f,}, {0.6861f, 0.6641f,}},
            {{0.2000f, 0.7891f,}, {0.3444f, 0.7891f,},
                    {0.3444f, 0.8797f,}, {0.2000f, 0.8797f,}},
            {{0.5583f, 0.7781f,}, {0.8806f, 0.7781f,},
                    {0.8806f, 0.8875f,}, {0.5583f, 0.8875f,}},};

    public static float[][] getCollageLayoutByName(String nameLayout, int ratio) {
        switch (nameLayout) {
            case "pile_d_3":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePreset3_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePreset3_9to16;
            case "pile_d_4":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePreset4_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePreset4_9to16;
            case "pile_d_5":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePreset5_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePreset5_9to16;
            case "pile_d_6":
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
            case "pile_d_3":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePath3_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePath3_9to16;
            case "pile_d_4":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePath4_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePath4_9to16;
            case "pile_d_5":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePath5_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePath5_9to16;
            case "pile_d_6":
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
