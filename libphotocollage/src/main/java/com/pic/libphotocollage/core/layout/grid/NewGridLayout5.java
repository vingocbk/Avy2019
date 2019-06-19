package com.pic.libphotocollage.core.layout.grid;

/**
 * Created by hoavt on 08/10/2016.
 */
public class NewGridLayout5 extends BaseGrid {
    public static float[][] getGridRectMargined(String nameLayout, float spacing) {
        switch (nameLayout) {
            case "grid_05_1":
                return GRID_5L_01T.getGridRectMargined(spacing);
            case "grid_05_2":
                return GRID_5L_02T.getGridRectMargined(spacing);
            case "grid_05_3":
                return GRID_5L_03T.getGridRectMargined(spacing);
            case "grid_05_4":
                return GRID_5L_04T.getGridRectMargined(spacing);
            case "grid_05_5":
                return GRID_5L_05T.getGridRectMargined(spacing);
            case "grid_05_6":
                return GRID_5L_06T.getGridRectMargined(spacing);
            case "grid_05_7":
                return GRID_5L_07T.getGridRectMargined(spacing);
            case "grid_05_8":
                return GRID_5L_08T.getGridRectMargined(spacing);
            case "grid_05_9":
                return GRID_5L_09T.getGridRectMargined(spacing);
            case "grid_05_10":
                return GRID_5L_10T.getGridRectMargined(spacing);
            case "grid_05_11":
                return GRID_5L_11T.getGridRectMargined(spacing);
            case "grid_05_12":
                return GRID_5L_12T.getGridRectMargined(spacing);
            case "grid_05_13":
                return GRID_5L_13T.getGridRectMargined(spacing);
            case "grid_05_14":
                return GRID_5L_14T.getGridRectMargined(spacing);
            case "grid_05_15":
                return GRID_5L_15T.getGridRectMargined(spacing);
            case "grid_05_16":
                return GRID_5L_16T.getGridRectMargined(spacing);
            case "grid_05_17":
                return GRID_5L_17T.getGridRectMargined(spacing);
            case "grid_05_18":
                return GRID_5L_18T.getGridRectMargined(spacing);
            case "grid_05_19":
                return GRID_5L_19T.getGridRectMargined(spacing);
            default:
                return GRID_5L_01T.getGridRectMargined(spacing);
        }
    }

    public static float[][][] getGridPointsMarginedByName(String nameLayout, float spacing) {
        switch (nameLayout) {
            case "grid_05_1":
                return GRID_5L_01T.getGridPointsMargined(spacing);
            case "grid_05_2":
                return GRID_5L_02T.getGridPointsMargined(spacing);
            case "grid_05_3":
                return GRID_5L_03T.getGridPointsMargined(spacing);
            case "grid_05_4":
                return GRID_5L_04T.getGridPointsMargined(spacing);
            case "grid_05_5":
                return GRID_5L_05T.getGridPointsMargined(spacing);
            case "grid_05_6":
                return GRID_5L_06T.getGridPointsMargined(spacing);
            case "grid_05_7":
                return GRID_5L_07T.getGridPointsMargined(spacing);
            case "grid_05_8":
                return GRID_5L_08T.getGridPointsMargined(spacing);
            case "grid_05_9":
                return GRID_5L_09T.getGridPointsMargined(spacing);
            case "grid_05_10":
                return GRID_5L_10T.getGridPointsMargined(spacing);
            case "grid_05_11":
                return GRID_5L_11T.getGridPointsMargined(spacing);
            case "grid_05_12":
                return GRID_5L_12T.getGridPointsMargined(spacing);
            case "grid_05_13":
                return GRID_5L_13T.getGridPointsMargined(spacing);
            case "grid_05_14":
                return GRID_5L_14T.getGridPointsMargined(spacing);
            case "grid_05_15":
                return GRID_5L_15T.getGridPointsMargined(spacing);
            case "grid_05_16":
                return GRID_5L_16T.getGridPointsMargined(spacing);
            case "grid_05_17":
                return GRID_5L_17T.getGridPointsMargined(spacing);
            case "grid_05_18":
                return GRID_5L_18T.getGridPointsMargined(spacing);
            case "grid_05_19":
                return GRID_5L_19T.getGridPointsMargined(spacing);
            default:
                return GRID_5L_01T.getGridPointsMargined(spacing);
        }
    }

    public static final class GRID_5L_01T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.66f - spacing, 0f + spacing},
                            {0.66f - spacing, 0.33f - spacing},
                            {0f + spacing, 0.33f - spacing},
                    },
                    {
                            {0.66f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.66f - spacing},
                            {0.66f + spacing, 0.66f - spacing},
                    },
                    {
                            {0.33f + spacing, 0.66f + spacing},
                            {1f - spacing, 0.66f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.33f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.33f + spacing},
                            {0.33f - spacing, 0.33f + spacing},
                            {0.33f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
                    {
                            {0.33f + spacing, 0.33f + spacing},
                            {0.66f - spacing, 0.33f + spacing},
                            {0.66f - spacing, 0.66f - spacing},
                            {0.33f + spacing, 0.66f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.66f - spacing, 0.33f - spacing},
                    {0.66f + spacing, 0f + spacing, 1f - spacing, 0.66f - spacing},
                    {0.33f + spacing, 0.66f + spacing, 1f - spacing, 1f - spacing},
                    {0f + spacing, 0.33f + spacing, 0.33f - spacing, 1f - spacing},
                    {0.33f + spacing, 0.33f + spacing, 0.66f - spacing, 0.66f - spacing}
            };
        }
    }

    public static final class GRID_5L_02T {
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
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.33f - spacing},
                            {0.33f + spacing, 0.33f - spacing},
                    },
                    {
                            {0.66f + spacing, 0.33f + spacing},
                            {1f - spacing, 0.33f + spacing},
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
                            {0.33f + spacing, 0.33f + spacing},
                            {0.66f - spacing, 0.33f + spacing},
                            {0.66f - spacing, 0.66f - spacing},
                            {0.33f + spacing, 0.66f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.33f - spacing, 0.66f - spacing},
                    {0.33f + spacing, 0f + spacing, 1f - spacing, 0.33f - spacing},
                    {0.66f + spacing, 0.33f + spacing, 1f - spacing, 1f - spacing},
                    {0f + spacing, 0.66f + spacing, 0.66f - spacing, 1f - spacing},
                    {0.33f + spacing, 0.33f + spacing, 0.66f - spacing, 0.66f - spacing}
            };
        }
    }

    public static final class GRID_5L_03T {
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
                            {0f + spacing, 0.33f + spacing},
                            {1f - spacing, 0.33f + spacing},
                            {1f - spacing, 0.66f - spacing},
                            {0f + spacing, 0.66f - spacing},
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
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.5f - spacing, 0.33f - spacing},
                    {0.5f + spacing, 0f + spacing, 1f - spacing, 0.33f - spacing},
                    {0f + spacing, 0.33f + spacing, 1f - spacing, 0.66f - spacing},
                    {0.5f + spacing, 0.66f + spacing, 1f - spacing, 1f - spacing},
                    {0f + spacing, 0.66f + spacing, 0.5f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_5L_04T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.33f - spacing, 0f + spacing},
                            {0.33f - spacing, 0.5f - spacing},
                            {0f + spacing, 0.5f - spacing},
                    },
                    {
                            {0f + spacing, 0.5f + spacing},
                            {0.33f - spacing, 0.5f + spacing},
                            {0.33f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
                    {
                            {0.33f + spacing, 0f + spacing},
                            {0.66f - spacing, 0f + spacing},
                            {0.66f - spacing, 1f - spacing},
                            {0.33f + spacing, 1f - spacing},
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
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.33f - spacing, 0.5f - spacing},
                    {0f + spacing, 0.5f + spacing, 0.33f - spacing, 1f - spacing},
                    {0.33f + spacing, 0f + spacing, 0.66f - spacing, 1f - spacing},
                    {0.66f + spacing, 0f + spacing, 1f - spacing, 0.5f - spacing},
                    {0.66f + spacing, 0.5f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_5L_05T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.2f - spacing, 0f + spacing},
                            {0.2f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
                    {
                            {0.2f + spacing, 0f + spacing},
                            {0.4f - spacing, 0f + spacing},
                            {0.4f - spacing, 1f - spacing},
                            {0.2f + spacing, 1f - spacing},
                    },
                    {
                            {0.4f + spacing, 0f + spacing},
                            {0.6f - spacing, 0f + spacing},
                            {0.6f - spacing, 1f - spacing},
                            {0.4f + spacing, 1f - spacing},
                    },
                    {
                            {0.6f + spacing, 0f + spacing},
                            {0.8f - spacing, 0f + spacing},
                            {0.8f - spacing, 1f - spacing},
                            {0.6f + spacing, 1f - spacing},
                    },
                    {
                            {0.8f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.8f + spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.2f - spacing, 1f - spacing},
                    {0.2f + spacing, 0f + spacing, 0.4f - spacing, 1f - spacing},
                    {0.4f + spacing, 0f + spacing, 0.6f - spacing, 1f - spacing},
                    {0.6f + spacing, 0f + spacing, 0.8f - spacing, 1f - spacing},
                    {0.8f + spacing, 0f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_5L_06T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.2f - spacing},
                            {0f + spacing, 0.2f - spacing},
                    },
                    {
                            {0f + spacing, 0.2f + spacing},
                            {1f - spacing, 0.2f + spacing},
                            {1f - spacing, 0.4f - spacing},
                            {0f + spacing, 0.4f - spacing},
                    },
                    {
                            {0f + spacing, 0.4f + spacing},
                            {1f - spacing, 0.4f + spacing},
                            {1f - spacing, 0.6f - spacing},
                            {0f + spacing, 0.6f - spacing},
                    },
                    {
                            {0f + spacing, 0.6f + spacing},
                            {1f - spacing, 0.6f + spacing},
                            {1f - spacing, 0.8f - spacing},
                            {0f + spacing, 0.8f - spacing},
                    },
                    {
                            {0f + spacing, 0.8f + spacing},
                            {1f - spacing, 0.8f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 1f - spacing, 0.2f - spacing},
                    {0f + spacing, 0.2f + spacing, 1f - spacing, 0.4f - spacing},
                    {0f + spacing, 0.4f + spacing, 1f - spacing, 0.6f - spacing},
                    {0f + spacing, 0.6f + spacing, 1f - spacing, 0.8f - spacing},
                    {0f + spacing, 0.8f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_5L_07T {
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
                            {0.5f + spacing, 0.5f + spacing},
                            {1f - spacing, 0.5f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0.5f + spacing, 1f - spacing},
                    },
                    {
                            {0f + spacing, 0.5f + spacing},
                            {0.5f - spacing, 0.5f + spacing},
                            {0.5f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.33f - spacing, 0.5f - spacing},
                    {0.33f + spacing, 0f + spacing, 0.66f - spacing, 0.5f - spacing},
                    {0.66f + spacing, 0f + spacing, 1f - spacing, 0.5f - spacing},
                    {0.5f + spacing, 0.5f + spacing, 1f - spacing, 1f - spacing},
                    {0f + spacing, 0.5f + spacing, 0.5f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_5L_08T {
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
                            {0f + spacing, 0.5f + spacing},
                            {0.5f - spacing, 0.5f + spacing},
                            {0.5f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.5f - spacing, 0.5f - spacing},
                    {0.5f + spacing, 0f + spacing, 1f - spacing, 0.33f - spacing},
                    {0.5f + spacing, 0.33f + spacing, 1f - spacing, 0.66f - spacing},
                    {0.5f + spacing, 0.66f + spacing, 1f - spacing, 1f - spacing},
                    {0f + spacing, 0.5f + spacing, 0.5f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_5L_09T {
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
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.5f - spacing, 0.5f - spacing},
                    {0.5f + spacing, 0f + spacing, 1f - spacing, 0.5f - spacing},
                    {0.66f + spacing, 0.5f + spacing, 1f - spacing, 1f - spacing},
                    {0.33f + spacing, 0.5f + spacing, 0.66f - spacing, 1f - spacing},
                    {0f + spacing, 0.5f + spacing, 0.33f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_5L_10T {
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
                    {0.5f + spacing, 0f + spacing, 1f - spacing, 0.5f - spacing},
                    {0.5f + spacing, 0.5f + spacing, 1f - spacing, 1f - spacing},
                    {0f + spacing, 0.66f + spacing, 0.5f - spacing, 1f - spacing},
                    {0f + spacing, 0.33f + spacing, 0.5f - spacing, 0.66f - spacing}
            };
        }
    }

    public static final class GRID_5L_11T {
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
                            {0.33f - spacing, 0.33f + spacing},
                            {0.33f - spacing, 0.66f - spacing},
                            {0f + spacing, 0.66f - spacing},
                    },
                    {
                            {0.33f + spacing, 0.33f + spacing},
                            {0.66f - spacing, 0.33f + spacing},
                            {0.66f - spacing, 0.66f - spacing},
                            {0.33f + spacing, 0.66f - spacing},
                    },
                    {
                            {0.66f + spacing, 0.33f + spacing},
                            {1f - spacing, 0.33f + spacing},
                            {1f - spacing, 0.66f - spacing},
                            {0.66f + spacing, 0.66f - spacing},
                    },
                    {
                            {0f + spacing, 0.66f + spacing},
                            {1f - spacing, 0.66f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 1f - spacing, 0.33f - spacing},
                    {0f + spacing, 0.33f + spacing, 0.33f - spacing, 0.66f - spacing},
                    {0.33f + spacing, 0.33f + spacing, 0.66f - spacing, 0.66f - spacing},
                    {0.66f + spacing, 0.33f + spacing, 1f - spacing, 0.66f - spacing},
                    {0f + spacing, 0.66f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_5L_12T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {1f/2-spacing, 0f+spacing},
                            {1f/2-spacing, 1f/4-spacing},
                            {1f/4-spacing, 1f/4-spacing},
                            {1f/4-spacing, 1f/2-spacing},
                            {0f+spacing, 1f/2-spacing},
                    },
                    {
                            {1f/2+spacing, 0f+spacing},
                            {1f-spacing, 0f+spacing},
                            {1f-spacing, 1f/2-spacing},
                            {3f/4+spacing, 1f/2-spacing},
                            {3f/4+spacing, 1f/4-spacing},
                            {1f/2+spacing, 1f/4-spacing},
                    },
                    {
                            {1f-spacing, 1f/2+spacing},
                            {3f/4+spacing, 1f/2+spacing},
                            {3f/4+spacing, 3f/4+spacing},
                            {1f/2+spacing, 3f/4+spacing},
                            {1f/2+spacing, 1f-spacing},
                            {1f-spacing, 1f-spacing},
                    },
                    {
                            {0f+spacing, 1f/2+spacing},
                            {0f+spacing, 1f-spacing},
                            {1f/2-spacing, 1f-spacing},
                            {1f/2-spacing, 3f/4+spacing},
                            {1f/4-spacing, 3f/4+spacing},
                            {1f/4-spacing, 1f/2+spacing},
                    },
                    {
                            {1f/4+spacing, 1f/4+spacing},
                            {3f/4-spacing, 1f/4+spacing},
                            {3f/4-spacing, 3f/4-spacing},
                            {1f/4+spacing, 3f/4-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f / 2-spacing, 1f/2-spacing},
                    {1f/2+spacing, 0f+spacing, 1f-spacing, 1f/2-spacing},
                    {1f/2+spacing, 1f/2+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 1f/2+spacing, 1f/2-spacing, 1f-spacing},
                    {1f/4+spacing, 1f/4+spacing, 3f/4-spacing, 3f/4-spacing}
            };
        }
    }

    public static final class GRID_5L_13T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {1f/2-spacing, 0f+spacing},
                            {1f/2-spacing, 1f/4-spacing},
                            {1f/4-spacing, 1f/2-spacing},
                            {0f+spacing, 1f/2-spacing},
                    },
                    {
                            {1f/2+spacing, 0f+spacing},
                            {1f-spacing, 0f+spacing},
                            {1f+spacing, 1f/2-spacing},
                            {3f/4+spacing, 1f/2-spacing},
                            {1f/2+spacing, 1f/4-spacing},
                    },
                    {
                            {1f-spacing, 1f/2+spacing},
                            {3f/4+spacing, 1f/2+spacing},
                            {1f/2+spacing, 3f/4+spacing},
                            {1f/2+spacing, 1f-spacing},
                            {1f-spacing, 1f-spacing},
                    },
                    {
                            {0f+spacing, 1f/2+spacing},
                            {0f+spacing, 1f-spacing},
                            {1f/2-spacing, 1f-spacing},
                            {1f/2-spacing, 3f/4+spacing},
                            {1f/4-spacing, 1f/2+spacing},
                    },
                    {
                            {1f/2, 1f/4+spacing},
                            {3f/4-spacing, 1f/2},
                            {1f/2, 3f/4-spacing},
                            {1f/4+spacing, 1f/2},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f / 2-spacing, 1f/2-spacing},
                    {1f/2+spacing, 0f+spacing, 1f-spacing, 1f/2-spacing},
                    {1f/2+spacing, 1f/2+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 1f/2+spacing, 1f/2-spacing, 1f-spacing},
                    {1f/4+spacing, 1f/4+spacing, 3f/4-spacing, 3f/4-spacing}
            };
        }
    }

    public static final class GRID_5L_14T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing*2},
                            {1f/4-spacing, 1f/4+spacing},
                            {1f/4-spacing, 3f/4-spacing},
                            {0f+spacing, 1f-spacing*2},
                    },
                    {
                            {0f+spacing*2, 0f+spacing},
                            {1f/4+spacing, 1f/4-spacing},
                            {3f/4-spacing, 1f/4-spacing},
                            {1f-spacing*2, 0f+spacing},
                    },
                    {
                            {1f-spacing, 0f+spacing*2},
                            {3f/4+spacing, 1f/4+spacing},
                            {3f/4+spacing, 3f/4-spacing},
                            {1f-spacing, 1f-spacing*2},
                    },
                    {
                            {1f-spacing*2, 1f-spacing},
                            {3f/4-spacing, 3f/4+spacing},
                            {1f/4+spacing, 3f/4+spacing},
                            {0f+spacing*2, 1f-spacing},
                    },
                    {
                            {1f/4+spacing, 1f/4+spacing},
                            {3f/4-spacing, 1f/4+spacing},
                            {3f/4-spacing, 3f/4-spacing},
                            {1f/4+spacing, 3f/4-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing*2, 1f / 4-spacing, 1f-spacing*2},
                    {0f+spacing*2, 0f+spacing, 1f-spacing*2, 1f/4-spacing},
                    {1f/4+spacing, 0f+spacing*2, 1f-spacing, 1f-spacing*2},
                    {0f+spacing*2, 3f/4+spacing, 1f-spacing*2, 1f-spacing},
                    {1f/4+spacing, 1f/4+spacing, 3f/4-spacing, 3f/4-spacing}
            };
        }
    }

    public static final class GRID_5L_15T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {1f/2-spacing, 0f+spacing},
                            {1f/2-spacing, 6f/19-spacing},
                            {0f+spacing, 1f/3-spacing},
                    },
                    {
                            {1f/2+spacing, 0f+spacing},
                            {1f/2+spacing, 6f/19-spacing},
                            {1f-spacing, 1f/3-spacing},
                            {1f-spacing, 0f+spacing},
                    },
                    {
                            {1f/2+spacing, 13f/19+spacing},
                            {1f-spacing, 2f/3+spacing},
                            {1f-spacing, 1f-spacing},
                            {1f/2+spacing, 1f-spacing},
                    },
                    {
                            {1f/2-spacing, 13f/19+spacing},
                            {1f/2-spacing, 1f-spacing},
                            {0f+spacing, 1f-spacing},
                            {0f+spacing, 2f/3+spacing},
                    },
                    {
                            {0f+spacing, 1f/3+spacing},
                            {1f/2, 6f/19+spacing},
                            {1f-spacing, 1f/3+spacing},
                            {1f-spacing, 2f/3-spacing},
                            {1f/2, 13f/19-spacing},
                            {0f+spacing, 2f/3-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f / 2-spacing, 1f/3-spacing},
                    {1f/2+spacing, 0f+spacing, 1f-spacing, 1f/3-spacing},
                    {1f/2+spacing, 2f/3+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 2f/3+spacing, 1f/2-spacing, 1f-spacing},
                    {0f+spacing, 6f/19+spacing, 1f-spacing, 13f/19-spacing}
            };
        }
    }

    public static final class GRID_5L_16T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {1f/2-spacing, 0f+spacing},
                            {3f/8-spacing, 1f/4-spacing},
                            {0f+spacing, 3f/8-spacing},
                    },
                    {
                            {1f/2+spacing, 0f+spacing},
                            {3f/8+spacing, 1f/4-spacing},
                            {3f/4+spacing, 1f/2-spacing},
                            {1f-spacing, 1f/2-spacing},
                            {1f-spacing, 0f+spacing},
                    },
                    {
                            {1f-spacing, 1f/2+spacing},
                            {3f/4+spacing, 1f/2+spacing},
                            {3f/8+spacing, 3f/4+spacing},
                            {1f/2+spacing, 1f-spacing},
                            {1f-spacing, 1f-spacing}
                    },
                    {
                            {1f/2-spacing, 1f-spacing},
                            {3f/8-spacing, 3f/4+spacing},
                            {0f+spacing, 5f/8+spacing},
                            {0f+spacing, 1f-spacing},
                    },
                    {
                            {0f+spacing, 3f/8+spacing},
                            {3f/8, 1f/4+spacing},
                            {3f/4-spacing, 1f/2},
                            {3f/8, 3f/4-spacing},
                            {0f+spacing, 5f/8-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f / 2-spacing, 3f/8-spacing},
                    {3f/8+spacing, 0f+spacing, 1f-spacing, 1f/2-spacing},
                    {3f/8+spacing, 1f/2+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 5f/8+spacing, 1f/2-spacing, 1f-spacing},
                    {0f+spacing, 1f/4+spacing, 3f/4-spacing, 3f/4-spacing}
            };
        }
    }

    public static final class GRID_5L_17T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 1f-spacing},
                            {0.3889f-spacing, 0.3264f},
                            {0f+spacing, 0f+spacing},
                    },
                    {
                            {0f+spacing, 0f+spacing},
                            {0.6736f, 0.3889f-spacing},
                            {1f-spacing, 0f+spacing},
                    },
                    {
                            {1f-spacing, 0f+spacing},
                            {0.6111f+spacing, 0.6736f},
                            {1f-spacing, 1f-spacing},
                    },
                    {
                            {1f-spacing, 1f-spacing},
                            {0.3264f, 0.6111f+spacing},
                            {0f+spacing, 1f-spacing},
                    },
                    {
                            {0.3889f+spacing, 0.3264f+spacing},
                            {0.6736f-spacing, 0.3889f+spacing},
                            {0.6111f-spacing, 0.6736f-spacing},
                            {0.3264f+spacing, 0.6111f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 0.3889f-spacing, 1f-spacing},
                    {0f+spacing, 0f+spacing, 1f-spacing, 0.3889f-spacing},
                    {0.6111f+spacing, 0f+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 0.6111f+spacing, 1f-spacing, 1f-spacing},
                    {0.3264f+spacing, 0.3264f+spacing, 0.6736f-spacing, 0.6736f-spacing}
            };
        }
    }

    public static final class GRID_5L_18T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {1f/4-spacing, 0f+spacing},
                            {1f/2-spacing, 1f/4+spacing},
                            {0f+spacing, 3f/4-spacing},
                    },
                    {
                            {1f/4+spacing, 0f+spacing},
                            {1f-spacing, 0f+spacing},
                            {1f-spacing, 1f/4-spacing},
                            {3f/4-spacing, 1f/2-spacing},
                    },
                    {
                            {1f-spacing, 1f/4+spacing},
                            {1f-spacing, 1f-spacing},
                            {3f/4+spacing, 1f-spacing},
                            {1f/2+spacing, 3f/4-spacing},
                    },
                    {
                            {3f/4-spacing, 1f-spacing},
                            {0f+spacing, 1f-spacing},
                            {0f+spacing, 3f/4+spacing},
                            {1f/4+spacing, 1f/2+spacing},
                    },
                    {
                            {1f/2, 1f/4+spacing*2},
                            {1f/4+spacing*2, 1f/2},
                            {1f/2, 3f/4-spacing*2},
                            {3f/4-spacing*2, 1f/2},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f/2-spacing, 3f/4-spacing},
                    {1f/4+spacing, 0f+spacing, 1f-spacing, 1f/2-spacing},
                    {1f/2+spacing, 1f/4+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 1f/2+spacing, 3f/4-spacing, 1f-spacing},
                    {1f/4, 1f/4, 3f/4, 3f/4}
            };
        }
    }

    public static final class GRID_5L_19T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f+spacing, 0f+spacing},
                            {1f / 2-spacing, 0f+spacing},
                            {1f / 2-spacing, 1f / 2-spacing},
                            {0f+spacing, 1f / 2-spacing}
                    },
                    {
                            {1f-spacing, 0f+spacing},
                            {1f / 2+spacing, 0f+spacing},
                            {1f / 2+spacing, 1f / 2-spacing},
                            {1f-spacing, 1f / 2-spacing}
                    },
                    {
                            {1f-spacing, 1f-spacing},
                            {1f / 2+spacing, 1f-spacing},
                            {1f / 2+spacing, 1f / 2+spacing},
                            {1f-spacing, 1f / 2+spacing}
                    },
                    {
                            {0f+spacing, 1f-spacing},
                            {1f / 2-spacing, 1f-spacing},
                            {1f / 2-spacing, 1f / 2+spacing},
                            {0f+spacing, 1f / 2+spacing}
                    },
                    {
                            {1f / 2, 0f+spacing*2},
                            {1f / 2-spacing, 1f / 2+spacing},
                            {1f-spacing*2, 1f / 2},
                            {1f / 2-spacing, 1f / 2-spacing},
                            {1f / 2, 1f-spacing*2},
                            {1f / 2+spacing, 1f / 2-spacing},
                            {0f+spacing*2, 1f / 2},
                            {1f / 2+spacing, 1f / 2+spacing},
                            {1f / 2, 0f+spacing*2},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f / 2-spacing, 1f / 2-spacing},
                    {1f / 2+spacing, 0f+spacing, 1f-spacing, 1f / 2-spacing},
                    {1f / 2+spacing, 1f / 2+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 1f / 2+spacing, 1f / 2-spacing, 1f-spacing},
                    {0f+spacing*2, 0f+spacing*2, 1f-spacing*2, 1f-spacing*2}
            };
        }

        public static final boolean[][] TEMP_CURVED = {
                {
                        false,
                        false,
                        true,
                        false
                },
                {
                        false,
                        false,
                        true,
                        false
                },
                {
                        false,
                        false,
                        true,
                        false
                },
                {
                        false,
                        false,
                        true,
                        false
                },
                {
                        false,
                        true,
                        false,
                        true,
                        false,
                        true,
                        false,
                        true,
                        false
                }
        };
    }

    public static boolean[][] getGridCurvedCheck(String nameLayout) {
        switch (nameLayout) {
            case "grid_05_1":
            case "grid_05_2":
            case "grid_05_3":
            case "grid_05_4":
            case "grid_05_5":
            case "grid_05_6":
            case "grid_05_7":
            case "grid_05_8":
            case "grid_05_9":
            case "grid_05_10":
            case "grid_05_11":
            case "grid_05_12":
            case "grid_05_13":
            case "grid_05_14":
            case "grid_05_15":
            case "grid_05_16":
            case "grid_05_17":
            case "grid_05_18":
                return null;
            case "grid_05_19":
                return GRID_5L_19T.TEMP_CURVED;
            default:
                return null;
        }
    }
}
