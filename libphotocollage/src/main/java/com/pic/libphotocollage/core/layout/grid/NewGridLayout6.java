package com.pic.libphotocollage.core.layout.grid;

/**
 * Created by hoavt on 08/10/2016.
 */
public class NewGridLayout6 extends BaseGrid {
    public static float[][] getGridRectMargined(String nameLayout, float spacing) {
        switch (nameLayout) {
            case "grid_06_1":
                return GRID_6L_01T.getGridRectMargined(spacing);
            case "grid_06_2":
                return GRID_6L_02T.getGridRectMargined(spacing);
            case "grid_06_3":
                return GRID_6L_03T.getGridRectMargined(spacing);
            case "grid_06_4":
                return GRID_6L_04T.getGridRectMargined(spacing);
            case "grid_06_5":
                return GRID_6L_05T.getGridRectMargined(spacing);
            case "grid_06_6":
                return GRID_6L_06T.getGridRectMargined(spacing);
            case "grid_06_7":
                return GRID_6L_07T.getGridRectMargined(spacing);
            case "grid_06_8":
                return GRID_6L_08T.getGridRectMargined(spacing);
            case "grid_06_9":
                return GRID_6L_09T.getGridRectMargined(spacing);
            case "grid_06_10":
                return GRID_6L_10T.getGridRectMargined(spacing);
            case "grid_06_11":
                return GRID_6L_11T.getGridRectMargined(spacing);
            case "grid_06_12":
                return GRID_6L_12T.getGridRectMargined(spacing);
            default:
                return GRID_6L_01T.getGridRectMargined(spacing);
        }
    }

    public static float[][][] getGridPointsMarginedByName(String nameLayout, float spacing) {
        switch (nameLayout) {
            case "grid_06_1":
                return GRID_6L_01T.getGridPointsMargined(spacing);
            case "grid_06_2":
                return GRID_6L_02T.getGridPointsMargined(spacing);
            case "grid_06_3":
                return GRID_6L_03T.getGridPointsMargined(spacing);
            case "grid_06_4":
                return GRID_6L_04T.getGridPointsMargined(spacing);
            case "grid_06_5":
                return GRID_6L_05T.getGridPointsMargined(spacing);
            case "grid_06_6":
                return GRID_6L_06T.getGridPointsMargined(spacing);
            case "grid_06_7":
                return GRID_6L_07T.getGridPointsMargined(spacing);
            case "grid_06_8":
                return GRID_6L_08T.getGridPointsMargined(spacing);
            case "grid_06_9":
                return GRID_6L_09T.getGridPointsMargined(spacing);
            case "grid_06_10":
                return GRID_6L_10T.getGridPointsMargined(spacing);
            case "grid_06_11":
                return GRID_6L_11T.getGridPointsMargined(spacing);
            case "grid_06_12":
                return GRID_6L_12T.getGridPointsMargined(spacing);
            default:
                return GRID_6L_01T.getGridPointsMargined(spacing);
        }
    }

    public static final class GRID_6L_01T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.5f - spacing, 0f + spacing},
                            {0.5f - spacing, 0.33f - spacing},
                            {0f + spacing, 0.33f - spacing},
                    },
                    {
                            {0.5f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.33f - spacing},
                            {0.5f + spacing, 0.33f - spacing},
                    },
                    {
                            {0.5f + spacing, 0.33f + spacing},
                            {1f - spacing, 0.33f + spacing},
                            {1f - spacing, 0.66f - spacing},
                            {0.5f + spacing, 0.66f - spacing},
                    },
                    {
                            {0.5f + spacing, 0.66f + spacing},
                            {1f - spacing, 0.66f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.5f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.66f + spacing},
                            {0.5f - spacing, 0.66f + spacing},
                            {0.5f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.33f + spacing},
                            {0.5f - spacing, 0.33f + spacing},
                            {0.5f - spacing, 0.66f - spacing},
                            {0f + spacing, 0.66f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.5f - spacing, 0.33f - spacing},
                    {0.5f + spacing, 0f + spacing, 1f - spacing, 0.33f - spacing},
                    {0.5f + spacing, 0.33f + spacing, 1f - spacing, 0.66f - spacing},
                    {0.5f + spacing, 0.66f + spacing, 1f - spacing, 1f - spacing},
                    {0f + spacing, 0.66f + spacing, 0.5f - spacing, 1f - spacing},
                    {0f + spacing, 0.33f + spacing, 0.5f - spacing, 0.66f - spacing}
            };
        }
    }

    public static final class GRID_6L_02T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.33f - spacing, 0f + spacing},
                            {0.33f - spacing, 0.5f - spacing},
                            {0f + spacing, 0.5f - spacing},
                    },
                    {
                            {0.33f + spacing, 0f + spacing},
                            {0.66f - spacing, 0f + spacing},
                            {0.66f - spacing, 0.5f - spacing},
                            {0.33f + spacing, 0.5f - spacing},
                    },
                    {
                            {0.66f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.5f - spacing},
                            {0.66f + spacing, 0.5f - spacing},
                    },
                    {
                            {0.66f + spacing, 0.5f + spacing},
                            {1f - spacing, 0.5f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.66f + spacing, 1f - spacing},
                    },
                    {
                            {0.33f + spacing, 0.5f + spacing},
                            {0.66f - spacing, 0.5f + spacing},
                            {0.66f - spacing, 1f - spacing},
                            {0.33f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.5f + spacing},
                            {0.33f - spacing, 0.5f + spacing},
                            {0.33f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.33f - spacing, 0.5f - spacing},
                    {0.33f + spacing, 0f + spacing, 0.66f - spacing, 0.5f - spacing},
                    {0.66f + spacing, 0f + spacing, 1f - spacing, 0.5f - spacing},
                    {0.66f + spacing, 0.5f + spacing, 1f - spacing, 1f - spacing},
                    {0.33f + spacing, 0.5f + spacing, 0.66f - spacing, 1f - spacing},
                    {0f + spacing, 0.5f + spacing, 0.33f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_6L_03T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.66f - spacing, 0f + spacing},
                            {0.66f - spacing, 0.33f - spacing},
                            {0f + spacing, 0.33f - spacing},
                    },
                    {
                            {0.33f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.33f - spacing},
                            {0.33f + spacing, 0.33f - spacing},
                    },
                    {
                            {0.33f + spacing, 0.33f + spacing},
                            {1f - spacing, 0.33f + spacing},
                            {1f - spacing, 0.66f - spacing},
                            {0.33f + spacing, 0.66f - spacing},
                    },
                    {
                            {0.66f + spacing, 0.66f + spacing},
                            {1f - spacing, 0.66f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.66f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.66f + spacing},
                            {0.66f - spacing, 0.66f + spacing},
                            {0.66f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.33f + spacing},
                            {0.33f - spacing, 0.33f + spacing},
                            {0.33f - spacing, 0.66f - spacing},
                            {0f + spacing, 0.66f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.66f - spacing, 0.33f - spacing},
                    {0.66f + spacing, 0f + spacing, 1f - spacing, 0.33f - spacing},
                    {0.33f + spacing, 0.33f + spacing, 1f - spacing, 0.66f - spacing},
                    {0.66f + spacing, 0.66f + spacing, 1f - spacing, 1f - spacing},
                    {0f + spacing, 0.66f + spacing, 0.66f - spacing, 1f - spacing},
                    {0f + spacing, 0.33f + spacing, 0.33f - spacing, 0.66f - spacing}
            };
        }
    }

    public static final class GRID_6L_04T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.33f - spacing, 0f + spacing},
                            {0.33f - spacing, 0.66f - spacing},
                            {0f + spacing, 0.66f - spacing},
                    },
                    {
                            {0.33f + spacing, 0f + spacing},
                            {0.66f - spacing, 0f + spacing},
                            {0.66f - spacing, 0.33f - spacing},
                            {0.33f + spacing, 0.33f - spacing},
                    },
                    {
                            {0.66f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.66f - spacing},
                            {0.66f + spacing, 0.66f - spacing},
                    },
                    {
                            {0.66f + spacing, 0.66f + spacing},
                            {1f - spacing, 0.66f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.66f + spacing, 1f - spacing},
                    },
                    {
                            {0.33f + spacing, 0.33f + spacing},
                            {0.66f - spacing, 0.33f + spacing},
                            {0.66f - spacing, 1f - spacing},
                            {0.33f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.66f + spacing},
                            {0.33f - spacing, 0.66f + spacing},
                            {0.33f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.33f - spacing, 0.66f - spacing},
                    {0.33f + spacing, 0f + spacing, 0.66f - spacing, 0.33f - spacing},
                    {0.66f + spacing, 0f + spacing, 1f - spacing, 0.66f - spacing},
                    {0.66f + spacing, 0.66f + spacing, 1f - spacing, 1f - spacing},
                    {0.33f + spacing, 0.33f + spacing, 0.66f - spacing, 1f - spacing},
                    {0f + spacing, 0.66f + spacing, 0.33f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_6L_05T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.66f - spacing, 0f + spacing},
                            {0.66f - spacing, 0.66f - spacing},
                            {0f + spacing, 0.66f - spacing},
                    },
                    {
                            {0.66f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.33f - spacing},
                            {0.66f + spacing, 0.33f - spacing},
                    },
                    {
                            {0.66f + spacing, 0.33f + spacing},
                            {1f - spacing, 0.33f + spacing},
                            {1f - spacing, 0.66f - spacing},
                            {0.66f + spacing, 0.66f - spacing},
                    },
                    {
                            {0.66f + spacing, 0.66f + spacing},
                            {1f - spacing, 0.66f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.66f + spacing, 1f - spacing},
                    },
                    {
                            {0.33f + spacing, 0.66f + spacing},
                            {0.66f - spacing, 0.66f + spacing},
                            {0.66f - spacing, 1f - spacing},
                            {0.33f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.66f + spacing},
                            {0.33f - spacing, 0.66f + spacing},
                            {0.33f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.66f - spacing, 0.66f - spacing},
                    {0.66f + spacing, 0f + spacing, 1f - spacing, 0.33f - spacing},
                    {0.66f + spacing, 0.33f + spacing, 1f - spacing, 0.66f - spacing},
                    {0.66f + spacing, 0.66f + spacing, 1f - spacing, 1f - spacing},
                    {0.33f + spacing, 0.66f + spacing, 0.66f - spacing, 1f - spacing},
                    {0f + spacing, 0.66f + spacing, 0.33f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_6L_06T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.33f - spacing, 0f + spacing},
                            {0.33f - spacing, 0.33f - spacing},
                            {0f + spacing, 0.33f - spacing},
                    },
                    {
                            {0.33f + spacing, 0f + spacing},
                            {0.66f - spacing, 0f + spacing},
                            {0.66f - spacing, 0.33f - spacing},
                            {0.33f + spacing, 0.33f - spacing},
                    },
                    {
                            {0.66f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.33f - spacing},
                            {0.66f + spacing, 0.33f - spacing},
                    },
                    {
                            {0.33f + spacing, 0.33f + spacing},
                            {1f - spacing, 0.33f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.33f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.66f + spacing},
                            {0.33f - spacing, 0.66f + spacing},
                            {0.33f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.33f + spacing},
                            {0.33f - spacing, 0.33f + spacing},
                            {0.33f - spacing, 0.66f - spacing},
                            {0f + spacing, 0.66f - spacing},
                    },
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.33f - spacing, 0.33f - spacing},
                    {0.33f + spacing, 0f + spacing, 0.66f - spacing, 0.33f - spacing},
                    {0.66f + spacing, 0f + spacing, 1f - spacing, 0.33f - spacing},
                    {0.33f + spacing, 0.33f + spacing, 1f - spacing, 1f - spacing},
                    {0f + spacing, 0.66f + spacing, 0.33f - spacing, 1f - spacing},
                    {0f + spacing, 0.33f + spacing, 0.33f - spacing, 0.66f - spacing}
            };
        }
    }

    public static final class GRID_6L_07T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.33f - spacing, 0f + spacing},
                            {0.33f - spacing, 0.33f - spacing},
                            {0f + spacing, 0.33f - spacing},
                    },
                    {
                            {0.33f + spacing, 0f + spacing},
                            {0.66f - spacing, 0f + spacing},
                            {0.66f - spacing, 0.33f - spacing},
                            {0.33f + spacing, 0.33f - spacing},
                    },
                    {
                            {0.66f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.33f - spacing},
                            {0.66f + spacing, 0.33f - spacing},
                    },
                    {
                            {0.5f + spacing, 0.33f + spacing},
                            {1f - spacing, 0.33f + spacing},
                            {1f - spacing, 0.66f - spacing},
                            {0.5f + spacing, 0.66f - spacing},
                    },
                    {
                            {0f + spacing, 0.66f + spacing},
                            {1f - spacing, 0.66f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.33f + spacing},
                            {0.5f - spacing, 0.33f + spacing},
                            {0.5f - spacing, 0.66f - spacing},
                            {0f + spacing, 0.66f - spacing},
                    },
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.33f - spacing, 0.33f - spacing},
                    {0.33f + spacing, 0f + spacing, 0.66f - spacing, 0.33f - spacing},
                    {0.66f + spacing, 0f + spacing, 1f - spacing, 0.33f - spacing},
                    {0.5f + spacing, 0.33f + spacing, 1f - spacing, 0.66f - spacing},
                    {0f + spacing, 0.66f + spacing, 1f - spacing, 1f - spacing},
                    {0f + spacing, 0.33f + spacing, 0.5f - spacing, 0.66f - spacing}
            };
        }
    }

    public static final class GRID_6L_08T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.33f - spacing},
                            {0f + spacing, 0.33f - spacing},
                    },
                    {
                            {0f + spacing, 0.33f + spacing},
                            {0.5f - spacing, 0.33f + spacing},
                            {0.5f - spacing, 0.66f - spacing},
                            {0f + spacing, 0.66f - spacing},
                    },
                    {
                            {0.5f + spacing, 0.33f + spacing},
                            {1f - spacing, 0.33f + spacing},
                            {1f - spacing, 0.66f - spacing},
                            {0.5f + spacing, 0.66f - spacing},
                    },
                    {
                            {0.66f + spacing, 0.66f + spacing},
                            {1f - spacing, 0.66f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.66f + spacing, 1f - spacing},
                    },
                    {
                            {0.33f + spacing, 0.66f + spacing},
                            {0.66f - spacing, 0.66f + spacing},
                            {0.66f - spacing, 1f - spacing},
                            {0.33f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.66f + spacing},
                            {0.33f - spacing, 0.66f + spacing},
                            {0.33f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 1f - spacing, 0.33f - spacing},
                    {0f + spacing, 0.33f + spacing, 0.5f - spacing, 0.66f - spacing},
                    {0.5f + spacing, 0.33f + spacing, 1f - spacing, 0.66f - spacing},
                    {0.66f + spacing, 0.66f + spacing, 1f - spacing, 1f - spacing},
                    {0.33f + spacing, 0.66f + spacing, 0.66f - spacing, 1f - spacing},
                    {0f + spacing, 0.66f + spacing, 0.33f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_6L_09T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][] {
                    {
                            {0f+spacing, 0f+spacing},
                            {2f/3-spacing, 0f+spacing},
                            {2f/3-spacing, 1f/3-spacing},
                            {0f+spacing, 1f/3-spacing},
                    },
                    {
                            {2f/3+spacing, 0f+spacing},
                            {2f/3+spacing, 1f/3-spacing},
                            {1f-spacing, 1f/3-spacing},
                            {1f-spacing, 0f+spacing},
                    },
                    {
                            {1f-spacing, 1f/3+spacing},
                            {1f-spacing, 2f/3-spacing},
                            {1f/2+spacing, 2f/3-spacing},
                            {1f/2+spacing, 1f/3+spacing},
                    },
                    {
                            {1f-spacing, 2f/3+spacing},
                            {1f-spacing, 1f-spacing},
                            {1f/3+spacing, 1f-spacing},
                            {1f/3+spacing, 2f/3+spacing},
                    },
                    {
                            {1f/3-spacing, 1f-spacing},
                            {1f/3-spacing, 2f/3+spacing},
                            {0f+spacing, 2f/3+spacing},
                            {0f+spacing, 1f-spacing},
                    },
                    {
                            {0f+spacing, 2f/3-spacing},
                            {1f/2-spacing, 2f/3-spacing},
                            {1f/2-spacing, 1f/3+spacing},
                            {0f+spacing, 1f/3+spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 2f/3-spacing, 1f/3-spacing},
                    {2f/3+spacing, 0f+spacing, 1f-spacing, 1f/3-spacing},
                    {1f/2+spacing, 1f/3+spacing, 1f-spacing, 2f/3-spacing},
                    {1f/3+spacing, 2f/3+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 2f/3+spacing, 1f/3-spacing, 1f-spacing},
                    {0f+spacing, 1f/3+spacing, 1f/2-spacing, 2f/3-spacing}
            };
        }
    }

    public static final class GRID_6L_10T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][] {
                    {
                            {0f+spacing, 0f+spacing},
                            {1f/3-spacing, 0f+spacing},
                            {1f/3-spacing, 1f/3-spacing},
                            {0f+spacing, 1f/3-spacing},
                    },
                    {
                            {1f/3+spacing, 0f+spacing},
                            {2f/3-spacing, 0f+spacing},
                            {2f/3-spacing, 1f/2-spacing},
                            {1f/3+spacing, 1f/2-spacing},
                    },
                    {
                            {2f/3+spacing, 0f+spacing},
                            {1f-spacing, 0f+spacing},
                            {1f-spacing, 2f/3-spacing},
                            {2f/3+spacing, 2f/3-spacing},
                    },
                    {
                            {2f/3+spacing, 2f/3+spacing},
                            {1f-spacing, 2f/3+spacing},
                            {1f-spacing, 1f-spacing},
                            {2f/3+spacing, 1f-spacing},
                    },
                    {
                            {1f/3+spacing, 1f/2+spacing},
                            {2f/3-spacing, 1f/2+spacing},
                            {2f/3-spacing, 1f-spacing},
                            {1f/3+spacing, 1f-spacing},
                    },
                    {
                            {0f+spacing, 1f/3+spacing},
                            {1f/3-spacing, 1f/3+spacing},
                            {1f/3-spacing, 1f-spacing},
                            {0f+spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f/3-spacing, 1f/3-spacing},
                    {1f/3+spacing, 0f+spacing, 2f/3-spacing, 1f/2-spacing},
                    {2f/3+spacing, 0f+spacing, 1f-spacing, 2f/3-spacing},
                    {2f/3+spacing, 2f/3+spacing, 1f-spacing, 1f-spacing},
                    {1f/3+spacing, 1f/2+spacing, 2f/3-spacing, 1f-spacing},
                    {0f+spacing, 1f/3+spacing, 1f/3-spacing, 1f-spacing}
            };
        }
    }

    public static final class GRID_6L_11T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][] {
                    {
                            {0f+spacing, 0f+spacing},
                            {2f/3-spacing, 0f+spacing},
                            {5f/9-spacing, 1f/3-spacing},
                            {0f+spacing, 1f/3-spacing},
                    },
                    {
                            {2f/3+spacing, 0f+spacing},
                            {1f-spacing, 0f+spacing},
                            {1f-spacing, 1f/3-spacing},
                            {5f/9+spacing, 1f/3-spacing},
                    },
                    {
                            {1f-spacing, 1f/3+spacing},
                            {5f/9+spacing, 1f/3+spacing},
                            {4f/9+spacing, 2f/3-spacing},
                            {1f-spacing, 2f/3-spacing},
                    },
                    {
                            {4f/9+spacing, 2f/3+spacing},
                            {1f-spacing, 2f/3+spacing},
                            {1f-spacing, 1f-spacing},
                            {1f/3+spacing, 1f-spacing},
                    },
                    {
                            {4f/9-spacing, 2f/3+spacing},
                            {1f/3-spacing, 1f-spacing},
                            {0f+spacing, 1f-spacing},
                            {0f+spacing, 2f/3+spacing},
                    },
                    {
                            {4f/9-spacing, 2f/3-spacing},
                            {0f+spacing, 2f/3-spacing},
                            {0f+spacing, 1f/3+spacing},
                            {5f/9-spacing, 1f/3+spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 2f/3-spacing, 1f/3-spacing},
                    {5f/9+spacing, 0f+spacing, 1f-spacing, 1f/3-spacing},
                    {4f/9+spacing, 1f/3+spacing, 1f-spacing, 2f/3-spacing},
                    {1f/3+spacing, 2f/3+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 2f/3+spacing, 4f/9-spacing, 1f-spacing},
                    {0f+spacing, 1f/3+spacing, 5f/9-spacing, 2f/3-spacing}
            };
        }
    }

    public static final class GRID_6L_12T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][] {
                    {
                            {0f+spacing, 0f+spacing},
                            {1f/3-spacing, 0f+spacing},
                            {1f/3-spacing, 4f/9-spacing},
                            {0f+spacing, 1f/3-spacing},
                    },
                    {
                            {1f/3+spacing, 0f+spacing},
                            {2f/3-spacing, 0f+spacing},
                            {2f/3-spacing, 5f/9-spacing},
                            {1f/3+spacing, 4f/9-spacing},
                    },
                    {
                            {2f/3+spacing, 0f+spacing},
                            {1f-spacing, 0f+spacing},
                            {1f-spacing, 2f/3-spacing},
                            {2f/3+spacing, 5f/9-spacing},
                    },
                    {
                            {2f/3+spacing, 5f/9+spacing},
                            {1f-spacing, 2f/3+spacing},
                            {1f-spacing, 1f-spacing},
                            {2f/3+spacing, 1f-spacing},
                    },
                    {
                            {1f/3+spacing, 4f/9+spacing},
                            {2f/3-spacing, 5f/9+spacing},
                            {2f/3-spacing, 1f-spacing},
                            {1f/3+spacing, 1f-spacing},
                    },
                    {
                            {0f+spacing, 1f/3+spacing},
                            {1f/3-spacing, 4f/9+spacing},
                            {1f/3-spacing, 1f-spacing},
                            {0f+spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f/3-spacing, 4f/9-spacing},
                    {1f/3+spacing, 0f+spacing, 2f/3-spacing, 5f/9-spacing},
                    {2f/3+spacing, 0f+spacing, 1f-spacing, 2f/3-spacing},
                    {2f/3+spacing, 5f/9+spacing, 1f-spacing, 1f-spacing},
                    {1f/3+spacing, 4f/9+spacing, 2f/3-spacing, 1f-spacing},
                    {0f+spacing, 1f/3+spacing, 1f/3-spacing, 1f-spacing}
            };
        }
    }

    public static boolean[][] getGridCurvedCheck(String nameLayout) {
        switch (nameLayout) {
            case "grid_06_1":
            case "grid_06_2":
            case "grid_06_3":
            case "grid_06_4":
            case "grid_06_5":
            case "grid_06_6":
            case "grid_06_7":
            case "grid_06_8":
            case "grid_06_9":
            case "grid_06_10":
            case "grid_06_11":
            case "grid_06_12":
                return null;
            default:
                return null;
        }
    }
}
