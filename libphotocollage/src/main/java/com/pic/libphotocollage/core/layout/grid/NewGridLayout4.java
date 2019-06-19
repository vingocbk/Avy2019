package com.pic.libphotocollage.core.layout.grid;

/**
 * Created by hoavt on 08/10/2016.
 */
public class NewGridLayout4 extends BaseGrid {
    public static float[][] getGridRectMargined(String nameLayout, float spacing) {
        switch (nameLayout) {
            case "grid_04_1":
                return GRID_4L_01T.getGridRectMargined(spacing);
            case "grid_04_2":
                return GRID_4L_02T.getGridRectMargined(spacing);
            case "grid_04_3":
                return GRID_4L_03T.getGridRectMargined(spacing);
            case "grid_04_4":
                return GRID_4L_04T.getGridRectMargined(spacing);
            case "grid_04_5":
                return GRID_4L_05T.getGridRectMargined(spacing);
            case "grid_04_6":
                return GRID_4L_06T.getGridRectMargined(spacing);
            case "grid_04_7":
                return GRID_4L_07T.getGridRectMargined(spacing);
            case "grid_04_8":
                return GRID_4L_08T.getGridRectMargined(spacing);
            case "grid_04_9":
                return GRID_4L_09T.getGridRectMargined(spacing);
            case "grid_04_10":
                return GRID_4L_10T.getGridRectMargined(spacing);
            case "grid_04_11":
                return GRID_4L_11T.getGridRectMargined(spacing);
            case "grid_04_12":
                return GRID_4L_12T.getGridRectMargined(spacing);
            case "grid_04_13":
                return GRID_4L_13T.getGridRectMargined(spacing);
            case "grid_04_14":
                return GRID_4L_14T.getGridRectMargined(spacing);
            case "grid_04_15":
                return GRID_4L_15T.getGridRectMargined(spacing);
            case "grid_04_16":
                return GRID_4L_16T.getGridRectMargined(spacing);
            case "grid_04_17":
                return GRID_4L_17T.getGridRectMargined(spacing);
            case "grid_04_18":
                return GRID_4L_18T.getGridRectMargined(spacing);
            case "grid_04_19":
                return GRID_4L_19T.getGridRectMargined(spacing);
            case "grid_04_20":
                return GRID_4L_20T.getGridRectMargined(spacing);
            case "grid_04_21":
                return GRID_4L_21T.getGridRectMargined(spacing);
            case "grid_04_22":
                return GRID_4L_22T.getGridRectMargined(spacing);
            case "grid_04_23":
                return GRID_4L_23T.getGridRectMargined(spacing);
            case "grid_04_24":
                return GRID_4L_24T.getGridRectMargined(spacing);
            case "grid_04_25":
                return GRID_4L_25T.getGridRectMargined(spacing);
            case "grid_04_26":
                return GRID_4L_26T.getGridRectMargined(spacing);
            default:
                return GRID_4L_01T.getGridRectMargined(spacing);
        }
    }

    public static float[][][] getGridPointsMarginedByName(String nameLayout, float spacing) {
        switch (nameLayout) {
            case "grid_04_1":
                return GRID_4L_01T.getGridPointsMargined(spacing);
            case "grid_04_2":
                return GRID_4L_02T.getGridPointsMargined(spacing);
            case "grid_04_3":
                return GRID_4L_03T.getGridPointsMargined(spacing);
            case "grid_04_4":
                return GRID_4L_04T.getGridPointsMargined(spacing);
            case "grid_04_5":
                return GRID_4L_05T.getGridPointsMargined(spacing);
            case "grid_04_6":
                return GRID_4L_06T.getGridPointsMargined(spacing);
            case "grid_04_7":
                return GRID_4L_07T.getGridPointsMargined(spacing);
            case "grid_04_8":
                return GRID_4L_08T.getGridPointsMargined(spacing);
            case "grid_04_9":
                return GRID_4L_09T.getGridPointsMargined(spacing);
            case "grid_04_10":
                return GRID_4L_10T.getGridPointsMargined(spacing);
            case "grid_04_11":
                return GRID_4L_11T.getGridPointsMargined(spacing);
            case "grid_04_12":
                return GRID_4L_12T.getGridPointsMargined(spacing);
            case "grid_04_13":
                return GRID_4L_13T.getGridPointsMargined(spacing);
            case "grid_04_14":
                return GRID_4L_14T.getGridPointsMargined(spacing);
            case "grid_04_15":
                return GRID_4L_15T.getGridPointsMargined(spacing);
            case "grid_04_16":
                return GRID_4L_16T.getGridPointsMargined(spacing);
            case "grid_04_17":
                return GRID_4L_17T.getGridPointsMargined(spacing);
            case "grid_04_18":
                return GRID_4L_18T.getGridPointsMargined(spacing);
            case "grid_04_19":
                return GRID_4L_19T.getGridPointsMargined(spacing);
            case "grid_04_20":
                return GRID_4L_20T.getGridPointsMargined(spacing);
            case "grid_04_21":
                return GRID_4L_21T.getGridPointsMargined(spacing);
            case "grid_04_22":
                return GRID_4L_22T.getGridPointsMargined(spacing);
            case "grid_04_23":
                return GRID_4L_23T.getGridPointsMargined(spacing);
            case "grid_04_24":
                return GRID_4L_24T.getGridPointsMargined(spacing);
            case "grid_04_25":
                return GRID_4L_25T.getGridPointsMargined(spacing);
            case "grid_04_26":
                return GRID_4L_26T.getGridPointsMargined(spacing);
            default:
                return GRID_4L_01T.getGridPointsMargined(spacing);
        }
    }

    public static final class GRID_4L_01T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.5f - spacing, 0f + spacing},
                            {0.5f - spacing, 0.5f - spacing},
                            {0f + spacing, 0.5f - spacing},
                    },
                    {
                            {0.5f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.5f - spacing},
                            {0.5f + spacing, 0.5f - spacing},
                    },
                    {
                            {0.5f + spacing, 0.5f + spacing},
                            {1f - spacing, 0.5f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.5f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.5f + spacing},
                            {0f + spacing, 1f - spacing},
                            {0.5f - spacing, 1f - spacing},
                            {0.5f - spacing, 0.5f + spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.5f - spacing, 0.5f - spacing},
                    {0.5f + spacing, 0f + spacing, 1f - spacing, 0.5f - spacing},
                    {0.5f + spacing, 0.5f + spacing, 1f - spacing, 1f - spacing},
                    {0f + spacing, 0.5f + spacing, 0.5f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_4L_02T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.5f - spacing, 0f + spacing},
                            {0.5f - spacing, 0.625f - spacing},
                            {0f + spacing, 0.625f - spacing},
                    },
                    {
                            {0.5f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.375f - spacing},
                            {0.5f + spacing, 0.375f - spacing},
                    },
                    {
                            {0.5f + spacing, 0.375f + spacing},
                            {1f - spacing, 0.375f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.5f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.625f + spacing},
                            {0f + spacing, 1f - spacing},
                            {0.5f - spacing, 1f - spacing},
                            {0.5f - spacing, 0.625f + spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.5f - spacing, 0.625f - spacing},
                    {0.5f + spacing, 0f + spacing, 1f - spacing, 0.375f - spacing},
                    {0.5f + spacing, 0.375f + spacing, 1f - spacing, 1f - spacing},
                    {0f + spacing, 0.625f + spacing, 0.5f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_4L_03T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.375f - spacing, 0f + spacing},
                            {0.375f - spacing, 0.5f - spacing},
                            {0f + spacing, 0.5f - spacing},
                    },
                    {
                            {0.375f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.5f - spacing},
                            {0.375f + spacing, 0.5f - spacing},
                    },
                    {
                            {0.625f + spacing, 0.5f + spacing},
                            {1f - spacing, 0.5f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.625f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.5f + spacing},
                            {0f + spacing, 1f - spacing},
                            {0.625f - spacing, 1f - spacing},
                            {0.625f - spacing, 0.5f + spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.375f - spacing, 0.5f - spacing},
                    {0.375f + spacing, 0f + spacing, 1f - spacing, 0.5f - spacing},
                    {0.625f + spacing, 0.5f + spacing, 1f - spacing, 1f - spacing},
                    {0f + spacing, 0.5f + spacing, 0.625f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_4L_04T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.25f - spacing, 0f + spacing},
                            {0.25f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
                    {
                            {0.25f + spacing, 0f + spacing},
                            {0.5f - spacing, 0f + spacing},
                            {0.5f - spacing, 1f - spacing},
                            {0.25f + spacing, 1f - spacing},
                    },
                    {
                            {0.5f + spacing, 0f + spacing},
                            {0.75f - spacing, 0f + spacing},
                            {0.75f - spacing, 1f - spacing},
                            {0.5f + spacing, 1f - spacing},
                    },
                    {
                            {0.75f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.75f + spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.25f - spacing, 1f - spacing},
                    {0.25f + spacing, 0f + spacing, 0.5f - spacing, 1f - spacing},
                    {0.5f + spacing, 0f + spacing, 0.75f - spacing, 1f - spacing},
                    {0.75f + spacing, 0f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_4L_05T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.25f - spacing},
                            {0f + spacing, 0.25f - spacing},
                    },
                    {
                            {0f + spacing, 0.25f + spacing},
                            {1f - spacing, 0.25f + spacing},
                            {1f - spacing, 0.5f - spacing},
                            {0f + spacing, 0.5f - spacing},
                    },
                    {
                            {0f + spacing, 0.5f + spacing},
                            {1f - spacing, 0.5f + spacing},
                            {1f - spacing, 0.75f - spacing},
                            {0f + spacing, 0.75f - spacing},
                    },
                    {
                            {0f + spacing, 0.75f + spacing},
                            {1f - spacing, 0.75f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 1f - spacing, 0.25f - spacing},
                    {0f + spacing, 0.25f + spacing, 1f - spacing, 0.5f - spacing},
                    {0f + spacing, 0.5f + spacing, 1f - spacing, 0.75f - spacing},
                    {0f + spacing, 0.75f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_4L_06T {
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
                            {1f - spacing, 0f - spacing},
                            {1f - spacing, 0.33f - spacing},
                            {0.66f + spacing, 0.33f - spacing},
                    },
                    {
                            {0f + spacing, 0.33f + spacing},
                            {1f - spacing, 0.33f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.33f - spacing, 0.33f - spacing},
                    {0.33f + spacing, 0f + spacing, 0.66f - spacing, 0.33f - spacing},
                    {0.66f + spacing, 0f + spacing, 1f - spacing, 0.33f - spacing},
                    {0f + spacing, 0.33f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_4L_07T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.66f - spacing, 0f + spacing},
                            {0.66f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
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
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.66f - spacing, 1f - spacing},
                    {0.66f + spacing, 0f + spacing, 1f - spacing, 0.33f - spacing},
                    {0.66f + spacing, 0.33f + spacing, 1f - spacing, 0.66f - spacing},
                    {0.66f + spacing, 0.66f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_4L_08T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.66f - spacing},
                            {0f + spacing, 0.66f - spacing},
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
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 1f - spacing, 0.66f - spacing},
                    {0.66f + spacing, 0.66f + spacing, 1f - spacing, 1f - spacing},
                    {0.33f + spacing, 0.66f + spacing, 0.66f - spacing, 1f - spacing},
                    {0f + spacing, 0.66f + spacing, 0.33f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_4L_09T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.33f - spacing, 0f + spacing},
                            {0.33f - spacing, 0.33f - spacing},
                            {0f + spacing, 0.33f - spacing},
                    },
                    {
                            {0f + spacing, 0.33f + spacing},
                            {0.33f - spacing, 0.33f + spacing},
                            {0.33f - spacing, 0.66f - spacing},
                            {0f + spacing, 0.66f - spacing},
                    },
                    {
                            {0f + spacing, 0.66f + spacing},
                            {0.33f - spacing, 0.66f + spacing},
                            {0.33f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
                    {
                            {0.33f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.33f + spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.33f - spacing, 0.33f - spacing},
                    {0f + spacing, 0.33f + spacing, 0.33f - spacing, 0.66f - spacing},
                    {0f + spacing, 0.66f + spacing, 0.33f - spacing, 1f - spacing},
                    {0.33f + spacing, 0f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_4L_10T {
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
                            {0f + spacing, 0.66f + spacing},
                            {1f - spacing, 0.66f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
                    {
                            {0.5f + spacing, 0.33f + spacing},
                            {1f - spacing, 0.33f + spacing},
                            {1f - spacing, 0.66f - spacing},
                            {0.5f + spacing, 0.66f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 1f - spacing, 0.33f - spacing},
                    {0f + spacing, 0.33f + spacing, 0.5f - spacing, 0.66f - spacing},
                    {0f + spacing, 0.66f + spacing, 1f - spacing, 1f - spacing},
                    {0.5f + spacing, 0.33f + spacing, 1f - spacing, 0.66f - spacing}
            };
        }
    }

    public static final class GRID_4L_11T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.33f - spacing, 0f + spacing},
                            {0.33f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
                    {
                            {0.33f + spacing, 0f + spacing},
                            {0.66f - spacing, 0f + spacing},
                            {0.66f - spacing, 0.5f - spacing},
                            {0.33f + spacing, 0.5f - spacing},
                    },
                    {
                            {0.33f + spacing, 0.5f + spacing},
                            {0.66f - spacing, 0.5f + spacing},
                            {0.66f - spacing, 1f - spacing},
                            {0.33f + spacing, 1f - spacing},
                    },
                    {
                            {0.66f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.66f + spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.33f - spacing, 1f - spacing},
                    {0.33f + spacing, 0f + spacing, 0.66f - spacing, 0.5f - spacing},
                    {0.33f + spacing, 0.5f + spacing, 0.66f - spacing, 1f - spacing},
                    {0.66f + spacing, 0f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_4L_12T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {3f / 8-spacing, 0f+spacing},
                            {3f / 8-spacing, 3f / 8-spacing},
                            {0f+spacing, 3f / 8-spacing},
                    },
                    {
                            {3f / 8+spacing, 0f+spacing},
                            {3f / 8+spacing, 3f / 8-spacing},
                            {5f / 8+spacing, 5f / 8-spacing},
                            {1f-spacing, 5f / 8-spacing},
                            {1f-spacing, 0f+spacing},
                    },
                    {
                            {5f / 8+spacing, 5f / 8+spacing},
                            {1f-spacing, 5f / 8+spacing},
                            {1f-spacing, 1f-spacing},
                            {5f / 8+spacing, 1f-spacing},
                    },
                    {
                            {0f+spacing, 3f / 8+spacing},
                            {3f / 8-spacing, 3f / 8+spacing},
                            {5f / 8-spacing, 5f / 8+spacing},
                            {5f / 8-spacing, 1f-spacing},
                            {0f+spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 3f/8-spacing, 3f / 8-spacing},
                    {3f/8+spacing, 0f+spacing, 1f-spacing, 5f/8-spacing},
                    {5f/8+spacing, 5f/8+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 3f / 8+spacing, 5f/8-spacing, 1f-spacing}
            };
        }
    }

    public static final class GRID_4L_13T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {5f / 8-spacing, 0f+spacing},
                            {5f / 8-spacing, 3f / 8-spacing},
                            {3f/8-spacing, 5f / 8-spacing},
                            {0f+spacing, 5f/8-spacing},
                    },
                    {
                            {5f / 8+spacing, 0f+spacing},
                            {1f-spacing, 0f+spacing},
                            {1f-spacing, 3f / 8-spacing},
                            {5f/8+spacing, 3f / 8-spacing},
                    },
                    {
                            {1f-spacing, 3f / 8+spacing},
                            {5f / 8+spacing, 3f / 8+spacing},
                            {3f / 8+spacing, 5f / 8+spacing},
                            {3f / 8+spacing, 1f-spacing},
                            {1f-spacing, 1f-spacing},
                    },
                    {
                            {3f / 8-spacing, 5f / 8+spacing},
                            {0f+spacing, 5f / 8+spacing},
                            {0f+spacing, 1f-spacing},
                            {3f / 8-spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 5f/8-spacing, 5f / 8-spacing},
                    {5f/8+spacing, 0f+spacing, 1f-spacing, 3f/8-spacing},
                    {3f/8+spacing, 3f/8+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 5f / 8+spacing, 3f/8-spacing, 1f-spacing}
            };
        }
    }

    public static final class GRID_4L_14T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing*2},
                            {0f+spacing, 1f/2-spacing},
                            {1f/2-spacing*2, 1f/2-spacing},
                    },
                    {
                            {0f+spacing*2, 0f+spacing},
                            {1f/2, 1f/2-spacing},
                            {1f-spacing*2, 0f+spacing},
                    },
                    {
                            {1f-spacing, 0f+spacing*2},
                            {1f/2+spacing*2, 1f/2-spacing},
                            {1f-spacing, 1f/2-spacing},
                    },
                    {
                            {0f+spacing, 1f/2+spacing},
                            {1f-spacing, 1f/2+spacing},
                            {1f-spacing, 1f-spacing},
                            {0f+spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing*2, 1f/2-spacing, 1f / 2-spacing},
                    {0f+spacing*2, 0f+spacing, 1f-spacing, 1f / 2-spacing},
                    {1f/2+spacing, 0f+spacing, 1f-spacing, 1f / 2-spacing},
                    {0f+spacing, 1f/2+spacing, 1f-spacing, 1f-spacing}
            };
        }
    }

    public static final class GRID_4L_15T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {1f-spacing, 0f+spacing},
                            {1f-spacing, 1f/2-spacing},
                            {0f+spacing, 1f/2-spacing},
                    },
                    {
                            {1f/2+spacing*2, 1f/2+spacing},
                            {1f-spacing, 1f/2+spacing},
                            {1f-spacing, 1f-spacing*2},
                    },
                    {
                            {1f-spacing*2, 1f-spacing},
                            {1f/2, 1f/2+spacing},
                            {0f+spacing*2, 1f-spacing},
                    },
                    {
                            {1f/2-spacing*2, 1f/2+spacing},
                            {0f+spacing, 1f-spacing*2},
                            {0f+spacing, 1f/2+spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f-spacing, 1f / 2-spacing},
                    {1f/2+spacing*2, 1f / 2+spacing, 1f-spacing, 1f-spacing*2},
                    {0f+spacing, 1f/2+spacing, 1f-spacing*2, 1f+spacing},
                    {0f+spacing, 1f/2+spacing, 1f/2-spacing*2, 1f-spacing}
            };
        }
    }

    public static final class GRID_4L_16T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {3f/4-spacing, 0f+spacing},
                            {1f/2-spacing, 1f/2-spacing},
                            {0f+spacing, 1f/4-spacing},
                    },
                    {
                            {1f/2+spacing, 1f/2-spacing},
                            {3f/4+spacing, 0f+spacing},
                            {1f-spacing, 0f+spacing},
                            {1f-spacing, 3f/4-spacing},
                    },
                    {
                            {1f/2+spacing, 1f/2+spacing},
                            {1f-spacing, 3f/4+spacing},
                            {1f-spacing, 1f-spacing},
                            {1f/4+spacing, 1f-spacing},
                    },
                    {
                            {0f+spacing, 1f/4+spacing},
                            {1f/2-spacing, 1f/2+spacing},
                            {1f/4-spacing, 1f-spacing},
                            {0f+spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 3f/4-spacing, 1f / 2-spacing},
                    {1f/2+spacing, 0f+spacing, 1f-spacing, 3f/4-spacing},
                    {1f/4+spacing, 1f/2+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 1f/4+spacing, 1f/2-spacing, 1f-spacing}
            };
        }
    }

    public static final class GRID_4L_17T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {1f/4-spacing, 0f+spacing},
                            {1f/2-spacing, 1f/2-spacing},
                            {0f+spacing, 3f/4-spacing},
                    },
                    {
                            {1f/4+spacing, 0f+spacing},
                            {1f-spacing, 0f+spacing},
                            {1f-spacing, 1f/4-spacing},
                            {1f/2+spacing, 1f/2-spacing},
                    },
                    {
                            {1f/2+spacing, 1f/2+spacing},
                            {1f-spacing, 1f/4+spacing},
                            {1f-spacing, 1f-spacing},
                            {3f/4+spacing, 1f-spacing},
                    },
                    {
                            {0f+spacing, 3f/4+spacing},
                            {1f/2-spacing, 1f/2+spacing},
                            {3f/4-spacing, 1f-spacing},
                            {0f+spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f / 2-spacing, 3f/4-spacing},
                    {1f/4+spacing, 0f+spacing, 1f-spacing, 1f/2-spacing},
                    {1f/2+spacing, 1f/4+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 1f/2+spacing, 3f/4-spacing, 1f-spacing}
            };
        }
    }

    public static final class GRID_4L_18T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing*2},
                            {1f/2-spacing, 1f/2},
                            {0f+spacing, 1f-spacing*2},
                    },
                    {
                            {0f+spacing*2, 0f+spacing},
                            {1f/2, 1f/2-spacing},
                            {1f-spacing*2, 0f+spacing},
                    },
                    {
                            {1f-spacing, 0f+spacing*2},
                            {1f/2+spacing, 1f/2},
                            {1f-spacing, 1f-spacing*2},
                    },
                    {
                            {1f-spacing*2, 1f-spacing},
                            {1f/2, 1f/2+spacing},
                            {0f+spacing*2, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing*2, 1f / 2-spacing, 1f-spacing*2},
                    {0f+spacing*2, 0f+spacing, 1f-spacing*2, 1f/2-spacing},
                    {1f/2+spacing, 0f+spacing*2, 1f-spacing, 1f-spacing*2},
                    {0f+spacing*2, 1f/2+spacing, 1f-spacing*2, 1f-spacing}
            };
        }
    }

    public static final class GRID_4L_19T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {5f/9-spacing, 0f+spacing},
                            {14f/27-spacing, 1f/3-spacing},
                            {0f+spacing, 6f/19-spacing},
                    },
                    {
                            {5f/9+spacing, 0f+spacing},
                            {1f-spacing, 0f+spacing},
                            {1f-spacing, 13f/19-spacing},
                            {13f/27+spacing, 2f/3-spacing},
                    },
                    {
                            {13f/27+spacing, 2f/3+spacing},
                            {1f-spacing, 13f/19+spacing},
                            {1f-spacing, 1f-spacing},
                            {4f/9+spacing, 1f-spacing},
                    },
                    {
                            {0f+spacing, 6f/19+spacing},
                            {14f/27-spacing, 1f/3+spacing},
                            {4f/9-spacing, 1f-spacing},
                            {0f+spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 5f / 9-spacing, 1f/3-spacing},
                    {4f/9+spacing, 0f+spacing, 1f-spacing, 13f/19-spacing},
                    {4f/9+spacing, 2f/3+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 6f/19+spacing, 14f/27-spacing, 1f-spacing}
            };
        }
    }

    public static final class GRID_4L_20T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {1f/2-spacing, 0f+spacing},
                            {0f+spacing, 1f/2-spacing},
                    },
                    {
                            {0f+spacing, 1f/2+spacing},
                            {1f/2+spacing, 0f+spacing},
                            {1f-spacing*2, 0f+spacing},
                            {0f+spacing, 1f-spacing*2},
                    },
                    {
                            {0f+spacing*2, 1f-spacing},
                            {1f-spacing, 0f+spacing*2},
                            {1f-spacing, 1f/2-spacing},
                            {1f/2-spacing, 1f-spacing},
                    },
                    {
                            {1f/2+spacing, 1f-spacing},
                            {1f-spacing, 1f/2+spacing},
                            {1f-spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f / 2-spacing, 1f/2-spacing},
                    {0f+spacing, 0f+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 0f+spacing, 1f-spacing, 1f-spacing},
                    {1f/2+spacing, 1f/2+spacing, 1f-spacing, 1f-spacing}
            };
        }
    }

    public static final class GRID_4L_21T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {1f/2-spacing, 0f+spacing},
                            {0f+spacing, 1f-spacing*2},
                    },
                    {
                            {1f/2+spacing, 0f+spacing},
                            {1f-spacing*2, 0f+spacing},
                            {3f/4-spacing, 1f/2-spacing},
                            {1f/4+spacing*2, 1f/2-spacing},
                    },
                    {
                            {1f-spacing, 0f+spacing*2},
                            {1f/2+spacing, 1f-spacing},
                            {1f-spacing, 1f-spacing},
                    },
                    {
                            {1f/4+spacing, 1f/2+spacing},
                            {3f/4-spacing*2, 1f/2+spacing},
                            {1f/2-spacing, 1f-spacing},
                            {0f+spacing*2, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f / 2-spacing, 1f-spacing},
                    {1f/4+spacing, 0f+spacing, 1f-spacing, 1f/2-spacing},
                    {1f/2+spacing, 0f+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 1f/2+spacing, 3f/4-spacing, 1f-spacing}
            };
        }
    }

    public static final class GRID_4L_22T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing*2},
                            {0f+spacing, 1f/2-spacing},
                            {1f/2-spacing, 3f/4-spacing*2},
                            {1f/2-spacing, 1f/4+spacing},
                    },
                    {
                            {0f+spacing*2, 0f+spacing},
                            {1f-spacing, 0f+spacing},
                            {1f-spacing, 1f/2-spacing},
                    },
                    {
                            {1f-spacing, 1f/2+spacing},
                            {1f/2+spacing, 1f/4+spacing*2},
                            {1f/2+spacing, 3f/4-spacing},
                            {1f-spacing, 1f-spacing*2},
                    },
                    {
                            {1f-spacing*2, 1f-spacing},
                            {0f+spacing, 1f/2+spacing},
                            {0f+spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f / 2-spacing, 3f/4-spacing},
                    {0f+spacing*2, 0f+spacing, 1f-spacing, 1f/2-spacing},
                    {1f/2+spacing, 1f/4+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 1f/2+spacing, 1f-spacing, 1f-spacing}
            };
        }
    }

    public static final class GRID_4L_23T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {1f / 2-spacing, 0f+spacing},
                            {1f / 4-spacing, 1f / 4-spacing}, // true
                            {1f / 2-spacing, 1f / 2},
                            {1f / 4-spacing, 3f / 4-spacing*2}, // true
                            {0f+spacing, 1f / 2-spacing},
                    },
                    {
                            {1f-spacing, 0f+spacing},
                            {1f / 2+spacing, 0f+spacing},
                            {1f / 4+spacing*2, 1f / 4-spacing}, // true
                            {1f / 2, 1f / 2-spacing},
                            {3f / 4+spacing, 1f / 4-spacing}, // true
                            {1f-spacing, 1f / 2-spacing},
                    },
                    {
                            {1f-spacing, 1f-spacing},
                            {1f / 2+spacing, 1f-spacing},
                            {3f / 4+spacing, 3f / 4+spacing}, // true
                            {1f / 2+spacing, 1f / 2},
                            {3f / 4+spacing, 1f / 4+spacing*2}, // true
                            {1f-spacing, 1f / 2+spacing},
                    },
                    {
                            {0f+spacing, 1f-spacing},
                            {1f / 2-spacing, 1f-spacing},
                            {3f / 4-spacing*2, 3f / 4+spacing}, // true
                            {1f / 2, 1f / 2+spacing},
                            {1f / 4-spacing, 3f / 4+spacing}, // true
                            {0f+spacing, 1f / 2+spacing},
                    },
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f / 2-spacing, 0.63f-spacing},
                    {0.37f+spacing, 0f+spacing, 1f-spacing, 1f / 2-spacing},
                    {1f / 2+spacing, 0.37f+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 1f / 2+spacing, 0.63f-spacing, 1f-spacing},
            };
        }

        public static final boolean[][] TEMP_CURVED = {
                {
                        false,
                        false,
                        true,
                        false,
                        true,
                        false,
                },
                {
                        false,
                        false,
                        true,
                        false,
                        true,
                        false,
                },
                {
                        false,
                        false,
                        true,
                        false,
                        true,
                        false,
                },
                {
                        false,
                        false,
                        true,
                        false,
                        true,
                        false,
                },
        };
    }

    public static final class GRID_4L_24T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {1f-spacing*2, 0f+spacing},
                            {1f / 2-spacing*2, 0f+spacing}, // true
                            {1f / 2-spacing*2, 1f / 2-spacing},
                            {0f+spacing, 1f / 2-spacing}, // true
                            {0f+spacing*2, 0f+spacing},
                    },
                    {
                            {1f-spacing, 0f+spacing*2},
                            {1f / 2+spacing, 0f+spacing}, // true
                            {1f / 2+spacing, 1f / 2-spacing*2},
                            {1f-spacing, 1f / 2-spacing*2}, // true
                            {1f-spacing, 1f-spacing*2},
                    },
                    {
                            {0f+spacing*2, 1f-spacing},
                            {1f / 2+spacing*2, 1f-spacing}, // true
                            {1f / 2+spacing*2, 1f / 2+spacing},
                            {1f-spacing, 1f / 2+spacing}, // true
                            {1f-spacing*2, 1f-spacing},
                    },
                    {
                            {0f+spacing, 0f+spacing*2},
                            {0f+spacing, 1f / 2+spacing*2}, // true
                            {1f / 2-spacing, 1f / 2+spacing*2},
                            {1f / 2-spacing, 1f-spacing}, // true
                            {0f+spacing, 1f-spacing*2},
                    },
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing*2, 0f+spacing, 1f-spacing*2, 1f / 2-spacing},
                    {1f / 2+spacing, 0f+spacing*2, 1f-spacing, 1f-spacing*2},
                    {0f+spacing*2, 1f / 2+spacing, 1f-spacing*2, 1f-spacing},
                    {0f+spacing, 0f+spacing*2, 1f / 2-spacing, 1f-spacing*2},
            };
        }

        public static final boolean[][] TEMP_CURVED = {
                {
                        false,
                        true,
                        false,
                        true,
                        false,
                },
                {
                        false,
                        true,
                        false,
                        true,
                        false,
                },
                {
                        false,
                        true,
                        false,
                        true,
                        false,
                },
                {
                        false,
                        true,
                        false,
                        true,
                        false,
                },
        };
    }

    public static final class GRID_4L_25T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {3f / 5-spacing, 0f+spacing},
                            {0.63125f-spacing, 0.375f}, // true
                            {1f / 2, 1f / 2-spacing},
                            {0f+spacing, 1f / 2-spacing},
                    },
                    {
                            {1f-spacing, 0f+spacing},
                            {3f / 5+spacing, 0f+spacing},
                            {0.63125f+spacing, 0.375f}, // true
                            {1f / 2+spacing*2, 1f / 2-spacing},
                            {1f-spacing, 1f / 2-spacing},
                    },
                    {
                            {1f-spacing, 1f-spacing},
                            {2f / 5+spacing, 1f-spacing},
                            {0.36875f+spacing, 0.625f}, // true
                            {1f / 2, 1f / 2+spacing},
                            {1f-spacing, 1f / 2+spacing},
                    },
                    {
                            {0f+spacing, 1f-spacing},
                            {2f / 5-spacing, 1f-spacing},
                            {0.36875f-spacing, 0.625f}, // true
                            {1f / 2-spacing*2, 1f / 2+spacing},
                            {0f+spacing, 1f / 2+spacing},
                    },
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 0.615625f-spacing, 1f / 2-spacing},
                    {1f / 2+spacing, 0f+spacing, 1f-spacing, 1f / 2-spacing},
                    {0.384375f+spacing, 1f / 2+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 1f / 2+spacing, 1f / 2-spacing, 1f-spacing},
            };
        }

        public static final boolean[][] TEMP_CURVED = {
                {
                        false,
                        false,
                        true,
                        false,
                        false,
                },
                {
                        false,
                        false,
                        true,
                        false,
                        false,
                },
                {
                        false,
                        false,
                        true,
                        false,
                        false,
                },
                {
                        false,
                        false,
                        true,
                        false,
                        false,
                },
        };
    }

    public static final class GRID_4L_26T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {1f / 2-spacing, 0f+spacing}, // true
                            {1f / 2-spacing, 1f / 2-spacing},
                            {0f+spacing, 1f / 2-spacing}, // true
                            {0f+spacing, 0f+spacing},
                    },
                    {
                            {1f-spacing, 0f+spacing},
                            {1f / 2+spacing, 0f+spacing}, // true
                            {1f / 2+spacing, 1f / 2-spacing},
                            {1f-spacing, 1f / 2-spacing}, // true
                            {1f-spacing, 0f+spacing},
                    },
                    {
                            {1f-spacing, 1f-spacing},
                            {1f / 2+spacing, 1f-spacing}, // true
                            {1f / 2+spacing, 1f / 2+spacing},
                            {1f-spacing, 1f / 2+spacing}, // true
                            {1f-spacing, 1f-spacing},
                    },
                    {
                            {0f+spacing, 1f-spacing},
                            {1f / 2-spacing, 1f-spacing}, // true
                            {1f / 2-spacing, 1f / 2+spacing},
                            {0f+spacing, 1f / 2+spacing}, // true
                            {0f+spacing, 1f-spacing},
                    },
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f/2-spacing, 1f / 2-spacing},
                    {1f / 2+spacing, 0f+spacing, 1f-spacing, 1f / 2-spacing},
                    {1f/2+spacing, 1f / 2+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 1f / 2+spacing, 1f / 2-spacing, 1f-spacing},
            };
        }

        public static final boolean[][] TEMP_CURVED = {
                {
                        false,
                        true, // true
                        false,
                        true, // true
                        false,
                },
                {
                        false,
                        true, // true
                        false,
                        true, // true
                        false,
                },
                {
                        false,
                        true, // true
                        false,
                        true, // true
                        false,
                },
                {
                        false,
                        true, // true
                        false,
                        true, // true
                        false,
                },
        };
    }

    public static boolean[][] getGridCurvedCheck(String nameLayout) {
        switch (nameLayout) {
            case "grid_04_1":
            case "grid_04_2":
            case "grid_04_3":
            case "grid_04_4":
            case "grid_04_5":
            case "grid_04_6":
            case "grid_04_7":
            case "grid_04_8":
            case "grid_04_9":
            case "grid_04_10":
            case "grid_04_11":
            case "grid_04_12":
            case "grid_04_13":
            case "grid_04_14":
            case "grid_04_15":
            case "grid_04_16":
            case "grid_04_17":
            case "grid_04_18":
            case "grid_04_19":
            case "grid_04_20":
            case "grid_04_21":
            case "grid_04_22":
                return null;
            case "grid_04_23":
                return GRID_4L_23T.TEMP_CURVED;
            case "grid_04_24":
                return GRID_4L_24T.TEMP_CURVED;
            case "grid_04_25":
                return GRID_4L_25T.TEMP_CURVED;
            case "grid_04_26":
                return GRID_4L_26T.TEMP_CURVED;
            default:
                return null;
        }
    }
}
