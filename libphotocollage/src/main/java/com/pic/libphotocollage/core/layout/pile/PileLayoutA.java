package com.pic.libphotocollage.core.layout.pile;

import com.pic.libphotocollage.core.util.BaseStatistic;

/**
 * Created by hoavt on 10/08/2016.
 */
public class PileLayoutA {

    private static final float[][] mPilePreset2_1to1 = {
            {0.3806f, 0.0500f, 1.0833f, 0.8556f},
            {-0.0694f, 0.1472f, 0.6028f, 0.9333f,},
    };

    private static final float[][] mPilePreset2_9to16 = {
            {0.3111f, 0.3234f, 1.2028f, 0.9047f,},
            {-0.1222f, 0.0594f, 0.7139f, 0.6203f,},
    };

    private static final float[][] mPilePreset3_1to1 = {
            {0.4278f, -0.0250f, 0.9083f, 0.5722f},
            {-0.0056f, 0.0556f, 0.5611f, 0.7111f},
            {0.2444f, 0.5194f, 0.8583f, 1.0194f}
    };

    private static final float[][] mPilePreset3_9to16 = {
            {0.3806f, 0.2594f, 1.0222f, 0.6984f,},
            {-0.0944f, 0.0328f, 0.6306f, 0.5031f,},
            {0.1333f, 0.6422f, 0.8194f, 0.9578f,},
    };

    private static final float[][] mPilePreset4_1to1 = {
            {0.1111f, -0.1028f, 0.5861f, 0.4583f,},
            {0.4111f, 0.1250f, 1.0278f, 0.8389f,},
            {-0.0250f, 0.2500f, 0.5333f, 0.8944f,},
            {0.3944f, 0.6111f, 0.9361f, 1.0472f,},};

    private static final float[][] mPilePreset4_9to16 = {
            {-0.0444f, -0.0047f, 0.5861f, 0.4141f,},
            {0.4278f, 0.1781f, 1.1417f, 0.6453f,},
            {-0.0583f, 0.3609f, 0.6194f, 0.7969f,},
            {0.3278f, 0.6781f, 0.9694f, 0.9734f,},};

    private static final float[][] mPilePreset5_1to1 = {
            {0.1139f, -0.0500f, 0.6417f, 0.5278f,},
            {0.5111f, 0.0333f, 0.9861f, 0.6056f,},
            {0.0472f, 0.4750f, 0.5444f, 1.0500f,},
            {0.5167f, 0.5778f, 0.9972f, 0.9667f,},
            {0.2750f, 0.1861f, 0.8028f, 0.6167f,},};

    private static final float[][] mPilePreset5_9to16 = {
            {0.4528f, 0.1563f, 1.0639f, 0.5719f,},
            {0.0333f, 0.6469f, 0.6528f, 1.0594f,},
            {-0.0917f, 0.2766f, 0.5833f, 0.7063f,},
            {0.0194f, 0.0078f, 0.6750f, 0.3109f,},
            {0.4083f, 0.5703f, 1.0778f, 0.8969f,},};

    private static final float[][] mPilePreset6_1to1 = {
            {-0.0833f, 0.0000f, 0.4222f, 0.5944f,},
            {0.6889f, 0.1056f, 1.1528f, 0.6500f,},
            {0.3278f, -0.0472f, 0.8417f, 0.3639f,},
            {-0.0667f, 0.4917f, 0.5278f, 1.1222f,},
            {0.5278f, 0.5944f, 1.0472f, 1.0306f,},
            {0.2250f, 0.3417f, 0.7472f, 0.7611f,},};

    private static final float[][] mPilePreset6_9to16 = {
            {-0.0194f, 0.5453f, 0.5583f, 0.9344f,},
            {0.4611f, 0.0141f, 1.1139f, 0.4453f,},
            {0.4028f, 0.4125f, 1.0917f, 0.7375f,},
            {-0.0806f, 0.0063f, 0.5806f, 0.3188f,},
            {-0.0139f, 0.2266f, 0.6500f, 0.6359f,},
            {0.3667f, 0.6797f, 1.0306f, 0.9922f,},};

    private static final float[][][] mPilePath2_1to1 = {
            {
                    {0.5806f, 0.0500f,},
                    {1.0833f, 0.2028f,},
                    {0.8833f, 0.8556f,},
                    {0.3806f, 0.7028f,}
            },
            {
                    {-0.0694f, 0.2694f,},
                    {0.4417f, 0.1472f,},
                    {0.6028f, 0.8083f,},
                    {0.0917f, 0.9333f,}
            },
    };
    private static final float[][][] mPilePath3_1to1 = {
            {
                    {0.4806f, -0.0250f,},
                    {0.9083f, 0.0139f,},
                    {0.8556f, 0.5722f,},
                    {0.4278f, 0.5333f,}
            },
            {
                    {0.1472f, 0.0556f,},
                    {0.5611f, 0.1722f,},
                    {0.4083f, 0.7111f,},
                    {-0.0056f, 0.5944f,}
            },
            {
                    {0.2444f, 0.5944f,},
                    {0.8000f, 0.5194f,},
                    {0.8583f, 0.9444f,},
                    {0.3083f, 1.0194f,}
            },
    };
    private static final float[][][] mPilePath4_1to1 = {
            {
                    {0.1111f, -0.0222f,},
                    {0.4778f, -0.1028f,},
                    {0.5861f, 0.3778f,},
                    {0.2167f, 0.4583f,}
            },
            {
                    {0.5750f, 0.1250f,},
                    {1.0278f, 0.2472f,},
                    {0.8667f, 0.8389f,},
                    {0.4111f, 0.7139f,}
            },
            {
                    {-0.0250f, 0.3667f,},
                    {0.3833f, 0.2500f,},
                    {0.5333f, 0.7778f,},
                    {0.1278f, 0.8944f,}
            },
            {
                    {0.4306f, 0.6111f,},
                    {0.9361f, 0.6583f,},
                    {0.9000f, 1.0472f,},
                    {0.3944f, 1.0028f,}
            },
    };
    private static final float[][][] mPilePath5_1to1 = {
            {
                    {0.1139f, 0.1056f,},
                    {0.4389f, -0.0500f,},
                    {0.6417f, 0.3694f,},
                    {0.3194f, 0.5278f,}
            },
            {
                    {0.5944f, 0.0333f,},
                    {0.9861f, 0.1000f,},
                    {0.9000f, 0.6056f,},
                    {0.5111f, 0.5417f,}
            },
            {
                    {0.0472f, 0.5750f,},
                    {0.4139f, 0.4750f,},
                    {0.5444f, 0.9500f,},
                    {0.1861f, 1.0500f,}
            },
            {
                    {0.5556f, 0.5778f,},
                    {0.9972f, 0.6250f,},
                    {0.9611f, 0.9667f,},
                    {0.5167f, 0.9167f,}
            },
            {
                    {0.2750f, 0.2556f,},
                    {0.7472f, 0.1861f,},
                    {0.8028f, 0.5500f,},
                    {0.3250f, 0.6167f,}
            },
    };
    private static final float[][][] mPilePath6_1to1 = {
            {
                    {0.0389f, 0.0000f,},
                    {0.4222f, 0.0944f,},
                    {0.3000f, 0.5944f,},
                    {-0.0833f, 0.4944f,}
            },
            {
                    {0.6889f, 0.1944f,},
                    {1.0361f, 0.1056f,},
                    {1.1528f, 0.5611f,},
                    {0.8056f, 0.6500f,}
            },
            {
                    {0.3278f, -0.0028f,},
                    {0.8056f, -0.0472f,},
                    {0.8417f, 0.3194f,},
                    {0.3639f, 0.3639f,}
            },
            {
                    {-0.0667f, 0.6806f,},
                    {0.2750f, 0.4917f,},
                    {0.5278f, 0.9361f,},
                    {0.1889f, 1.1222f,}
            },
            {
                    {0.5278f, 0.6833f,},
                    {0.9778f, 0.5944f,},
                    {1.0472f, 0.9417f,},
                    {0.5944f, 1.0306f,}
            },
            {
                    {0.2611f, 0.3417f},
                    {0.7472f, 0.3889f,},
                    {0.7111f, 0.7611f,},
                    {0.2250f, 0.7139f,}
            },
    };

    private static final float[][][] mPilePath2_9to16 = {
            {
                    {0.5389f, 0.3234f,},
                    {1.2028f, 0.4203f,},
                    {0.9750f, 0.9047f,},
                    {0.3111f, 0.8063f,}
            },
            {
                    {-0.1222f, 0.1281f,},
                    {0.5528f, 0.0594f,},
                    {0.7139f, 0.5516f,},
                    {0.0389f, 0.6203f,}
            },
    };

    private static final float[][][] mPilePath3_9to16 = {
            {
                    {0.3806f, 0.3000f,},
                    {0.9278f, 0.2594f,},
                    {1.0222f, 0.6563f,},
                    {0.4750f, 0.6984f,}
            },
            {
                    {0.1000f, 0.0328f,},
                    {0.6306f, 0.1156f,},
                    {0.4361f, 0.5031f,},
                    {-0.0944f, 0.4188f,}
            },
            {
                    {0.1333f, 0.6875f,},
                    {0.7583f, 0.6422f,},
                    {0.8194f, 0.9125f,},
                    {0.1944f, 0.9578f,}
            },
    };

    private static final float[][][] mPilePath4_9to16 = {
            {
                    {-0.0444f, 0.0563f,},
                    {0.4417f, -0.0047f,},
                    {0.5861f, 0.3516f,},
                    {0.0972f, 0.4141f,}
            },
            {
                    {0.6167f, 0.1781f,},
                    {1.1417f, 0.2594f,},
                    {0.9528f, 0.6453f,},
                    {0.4278f, 0.5609f,}
            },
            {
                    {-0.0583f, 0.4406f,},
                    {0.4250f, 0.3609f,},
                    {0.6194f, 0.7141f,},
                    {0.1333f, 0.7969f,}
            },
            {
                    {0.3750f, 0.6781f,},
                    {0.9694f, 0.7125f,},
                    {0.9306f, 0.9734f,},
                    {0.3278f, 0.9391f,}
            },
    };

    private static final float[][][] mPilePath5_9to16 = {
            {
                    {0.5639f, 0.1563f,},
                    {1.0639f, 0.2047f,},
                    {0.9528f, 0.5719f,},
                    {0.4528f, 0.5234f,}
            },
            {
                    {0.0333f, 0.7063f,},
                    {0.5139f, 0.6469f,},
                    {0.6528f, 1.0000f,},
                    {0.1694f, 1.0594f,}
            },
            {
                    {-0.0917f, 0.3688f,},
                    {0.3667f, 0.2766f,},
                    {0.5833f, 0.6125f,},
                    {0.1250f, 0.7063f,}
            },
            {
                    {0.0194f, 0.0547f,},
                    {0.6083f, 0.0078f,},
                    {0.6750f, 0.2641f,},
                    {0.0833f, 0.3109f,}
            },
            {
                    {0.5250f, 0.5703f,},
                    {1.0778f, 0.6547f,},
                    {0.9639f, 0.8969f,},
                    {0.4083f, 0.8109f,}
            },
    };

    private static final float[][][] mPilePath6_9to16 = {
            {
                    {-0.0194f, 0.5938f,},
                    {0.4444f, 0.5453f,},
                    {0.5583f, 0.8859f,},
                    {0.0944f, 0.9344f,}
            },
            {
                    {0.4611f, 0.0844f,},
                    {0.9528f, 0.0141f,},
                    {1.1139f, 0.3734f,},
                    {0.6222f, 0.4453f,}
            },
            {
                    {0.4889f, 0.4125f,},
                    {1.0917f, 0.4766f,},
                    {1.0056f, 0.7375f,},
                    {0.4028f, 0.6734f,}
            },
            {
                    {-0.0806f, 0.0688f,},
                    {0.4972f, 0.0063f,},
                    {0.5806f, 0.2563f,},
                    {0.0056f, 0.3188f,}
            },
            {
                    {-0.0139f, 0.3344f,},
                    {0.4000f, 0.2266f,},
                    {0.6500f, 0.5297f,},
                    {0.2361f, 0.6359f,}
            },
            {
                    {0.3667f, 0.7391f,},
                    {0.9500f, 0.6797f,},
                    {1.0306f, 0.9328f,},
                    {0.4472f, 0.9922f,}
            },
    };

    public static float[][] getCollageLayoutByName(String nameLayout, int ratio) {
        switch (nameLayout) {
            case "pile_a_3":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePreset3_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePreset3_9to16;
            case "pile_a_4":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePreset4_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePreset4_9to16;
            case "pile_a_5":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePreset5_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePreset5_9to16;
            case "pile_a_6":
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
            case "pile_a_3":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePath3_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePath3_9to16;
            case "pile_a_4":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePath4_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePath4_9to16;
            case "pile_a_5":
                if (ratio == BaseStatistic.COLLAGE_RATIO_11)
                    return mPilePath5_1to1;
                else if (ratio == BaseStatistic.COLLAGE_RATIO_916)
                    return mPilePath5_9to16;
            case "pile_a_6":
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
