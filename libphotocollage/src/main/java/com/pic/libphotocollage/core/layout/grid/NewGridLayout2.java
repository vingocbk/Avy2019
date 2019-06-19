package com.pic.libphotocollage.core.layout.grid;

/**
 * Created by hoavt on 08/10/2016.
 */
public class NewGridLayout2 extends BaseGrid {
    public static float[][] getGridRectMargined(String nameLayout, float spacing) {
        switch (nameLayout) {
            case "grid_02_1":
                return GRID_2L_01T.getGridRectMargined(spacing);
            case "grid_02_2":
                return GRID_2L_02T.getGridRectMargined(spacing);
            case "grid_02_3":
                return GRID_2L_03T.getGridRectMargined(spacing);
            case "grid_02_4":
                return GRID_2L_04T.getGridRectMargined(spacing);
            case "grid_02_5":
                return GRID_2L_05T.getGridRectMargined(spacing);
            case "grid_02_6":
                return GRID_2L_06T.getGridRectMargined(spacing);
            case "grid_02_7":
                return GRID_2L_07T.getGridRectMargined(spacing);
            case "grid_02_8":
                return GRID_2L_08T.getGridRectMargined(spacing);
            case "grid_02_9":
                return GRID_2L_09T.getGridRectMargined(spacing);
            case "grid_02_10":
                return GRID_2L_10T.getGridRectMargined(spacing);
            case "grid_02_11":
                return GRID_2L_11T.getGridRectMargined(spacing);
            case "grid_02_12":
                return GRID_2L_12T.getGridRectMargined(spacing);
            case "grid_02_13":
                return GRID_2L_13T.getGridRectMargined(spacing);
            case "grid_02_14":
                return GRID_2L_14T.getGridRectMargined(spacing);
            default:
                return GRID_2L_01T.getGridRectMargined(spacing);
        }
    }

    public static boolean[][] getGridCurvedCheck(String nameLayout) {
        switch (nameLayout) {
            case "grid_02_1":
            case "grid_02_2":
            case "grid_02_3":
            case "grid_02_4":
            case "grid_02_5":
            case "grid_02_6":
            case "grid_02_7":
            case "grid_02_8":
            case "grid_02_9":
            case "grid_02_10":
            case "grid_02_11":
                return null;
            case "grid_02_12":
                return GRID_2L_12T.TEMP_CURVED;
            case "grid_02_13":
                return GRID_2L_13T.TEMP_CURVED;
            case "grid_02_14":
                return GRID_2L_14T.TEMP_CURVED;
            default:
                return null;
        }
    }

    public static float[][][] getGridPointsMarginedByName(String nameLayout, float spacing) {
        switch (nameLayout) {
            case "grid_02_1":
                return GRID_2L_01T.getGridPointsMargined(spacing);
            case "grid_02_2":
                return GRID_2L_02T.getGridPointsMargined(spacing);
            case "grid_02_3":
                return GRID_2L_03T.getGridPointsMargined(spacing);
            case "grid_02_4":
                return GRID_2L_04T.getGridPointsMargined(spacing);
            case "grid_02_5":
                return GRID_2L_05T.getGridPointsMargined(spacing);
            case "grid_02_6":
                return GRID_2L_06T.getGridPointsMargined(spacing);
            case "grid_02_7":
                return GRID_2L_07T.getGridPointsMargined(spacing);
            case "grid_02_8":
                return GRID_2L_08T.getGridPointsMargined(spacing);
            case "grid_02_9":
                return GRID_2L_09T.getGridPointsMargined(spacing);
            case "grid_02_10":
                return GRID_2L_10T.getGridPointsMargined(spacing);
            case "grid_02_11":
                return GRID_2L_11T.getGridPointsMargined(spacing);
            case "grid_02_12":
                return GRID_2L_12T.getGridPointsMargined(spacing);
            case "grid_02_13":
                return GRID_2L_13T.getGridPointsMargined(spacing);
            case "grid_02_14":
                return GRID_2L_14T.getGridPointsMargined(spacing);
            default:
                return GRID_2L_01T.getGridPointsMargined(spacing);
        }
    }

    public static final class GRID_2L_01T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.5f - spacing, 0f + spacing},
                            {0.5f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
                    {
                            {1f - spacing, 0f + spacing},
                            {0.5f + spacing, 0f + spacing},
                            {0.5f + spacing, 1f - spacing},
                            {1f - spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.5f - spacing, 1f - spacing},
                    {0.5f + spacing, 0f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_2L_02T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.5f - spacing},
                            {0f + spacing, 0.5f - spacing},
                    },
                    {
                            {0f + spacing, 0.5f + spacing},
                            {1f - spacing, 0.5f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 1f - spacing, 0.5f - spacing},
                    {0f + spacing, 0.5f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_2L_03T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.375f - spacing, 0f + spacing},
                            {0.375f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
                    {
                            {0.375f + spacing, 0f + spacing},
                            {0.375f + spacing, 1f - spacing},
                            {1f - spacing, 1f - spacing},
                            {1f - spacing, 0f + spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.375f - spacing, 1f - spacing},
                    {0.375f + spacing, 0f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_2L_04T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.375f - spacing},
                            {0f + spacing, 0.375f - spacing},
                    },
                    {
                            {0f + spacing, 0.375f + spacing},
                            {1f - spacing, 0.375f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 1f - spacing, 0.375f - spacing},
                    {0f + spacing, 0.375f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_2L_05T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {0.625f - spacing, 0f + spacing},
                            {0.625f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    },
                    {
                            {1f - spacing, 0f + spacing},
                            {0.625f + spacing, 0f + spacing},
                            {0.625f + spacing, 1f - spacing},
                            {1f - spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.625f - spacing, 1f - spacing},
                    {0.625f + spacing, 0f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_2L_06T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][]{
                    {
                            {0f + spacing, 0f + spacing},
                            {1f - spacing, 0f + spacing},
                            {1f - spacing, 0.625f - spacing},
                            {0f + spacing, 0.625f - spacing},
                    },
                    {
                            {0f + spacing, 0.625f + spacing},
                            {1f - spacing, 0.625f + spacing},
                            {1f - spacing, 1f - spacing},
                            {0f + spacing, 1f - spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 1f - spacing, 0.625f - spacing},
                    {0f + spacing, 0.625f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_2L_07T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][] {
                    {
                            {0f+spacing, 0f+spacing},
                            {0.6f-spacing, 0f+spacing},
                            {0.4f-spacing, 1f / 6},
                            {0.6f-spacing, 2f / 6},
                            {0.4f-spacing, 3f / 6},
                            {0.6f-spacing, 4f / 6},
                            {0.4f-spacing, 5f / 6},
                            {0.6f-spacing, 1f-spacing},
                            {0f+spacing, 1f-spacing},
                    },
                    {
                            {0.6f+spacing, 0f+spacing},
                            {0.4f+spacing, 1f / 6},
                            {0.6f+spacing, 2f / 6},
                            {0.4f+spacing, 3f / 6},
                            {0.6f+spacing, 4f / 6},
                            {0.4f+spacing, 5f / 6},
                            {0.6f+spacing, 1f-spacing},
                            {1f-spacing, 1f-spacing},
                            {1f-spacing, 0f+spacing}
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f + spacing, 0f + spacing, 0.6f - spacing, 1f - spacing},
                    {0.4f + spacing, 0f + spacing, 1f - spacing, 1f - spacing}
            };
        }
    }

    public static final class GRID_2L_08T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][] {
                    {
                            {0f+spacing, 0f+spacing},
                            {0f+spacing, 0.4f-spacing},
                            {1f / 6, 0.6f-spacing},
                            {2f / 6, 0.4f-spacing},
                            {3f / 6, 0.6f-spacing},
                            {4f / 6, 0.4f-spacing},
                            {5f / 6, 0.6f-spacing},
                            {1f-spacing, 0.4f-spacing},
                            {1f-spacing, 0f+spacing},
                    },
                    {
                            {0f+spacing, 1f-spacing},
                            {0f+spacing, 0.4f+spacing},
                            {1f / 6, 0.6f+spacing},
                            {2f / 6, 0.4f+spacing},
                            {3f / 6, 0.6f+spacing},
                            {4f / 6, 0.4f+spacing},
                            {5f / 6, 0.6f+spacing},
                            {1f-spacing, 0.4f+spacing},
                            {1f-spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f-spacing, 0.6f-spacing},
                    {0f+spacing, 0.4f+spacing, 1f-spacing, 1f-spacing}
            };
        }
    }

    public static final class GRID_2L_09T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][] {
                    {
                            {0f+spacing*2, 0f+spacing},
                            {1f-spacing, 0f+spacing},
                            {1f-spacing, 1f-spacing*2},
                    },
                    {
                            {0f+spacing, 0f+spacing*2},
                            {0f+spacing, 1f-spacing},
                            {1f-spacing*2, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 0f+spacing, 1f-spacing, 1f-spacing}
            };
        }
    }

    public static final class GRID_2L_10T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][] {
                    {
                            {0f+spacing, 0f+spacing},
                            {1f-spacing*2, 0f+spacing},
                            {0f+spacing, 1f-spacing*2},
                    },
                    {
                            {1f-spacing, 0f+spacing*2},
                            {0f+spacing*2, 1f-spacing},
                            {1f-spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f-spacing, 1f-spacing},
                    {0f+spacing, 0f+spacing, 1f-spacing, 1f-spacing}
            };
        }
    }

    public static final class GRID_2L_11T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][] {
                    {
                            {0f+spacing, 0f+spacing},
                            {1f-spacing, 0f+spacing},
                            {1f-spacing, 1f / 3-spacing},
                            {5f / 6, 1f / 2-spacing},
                            {1f / 6, 1f / 2-spacing},
                            {0f+spacing, 2f / 3-spacing},
                    },
                    {
                            {0f+spacing, 1f-spacing},
                            {1f-spacing, 1f-spacing},
                            {1f-spacing, 1f / 3+spacing},
                            {5f / 6, 1f / 2+spacing},
                            {1f / 6, 1f / 2+spacing},
                            {0f+spacing, 2f / 3+spacing},
                    },
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f-spacing, 2f / 3-spacing},
                    {0f+spacing, 1f / 3+spacing, 1f-spacing, 1f-spacing}
            };
        }
    }

    public static final class GRID_2L_13T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][] {
                    {
                            {0f+spacing, 0f+spacing},
                            {0f+spacing, 0.5f-spacing},
                            {1f / 8, 0.6f-spacing},
                            {2f / 8, 0.5f-spacing},
                            {3f / 8, 0.4f-spacing},
                            {4f / 8, 0.5f-spacing},
                            {5f / 8, 0.6f-spacing},
                            {6f / 8, 0.5f-spacing},
                            {7f / 8, 0.4f-spacing},
                            {1f-spacing, 0.5f-spacing},
                            {1f-spacing, 0f+spacing}
                    },
                    {
                            {0f+spacing, 1f-spacing},
                            {0f+spacing, 0.5f+spacing},
                            {1f / 8, 0.6f+spacing},
                            {2f / 8, 0.5f+spacing},
                            {3f / 8, 0.4f+spacing},
                            {4f / 8, 0.5f+spacing},
                            {5f / 8, 0.6f+spacing},
                            {6f / 8, 0.5f+spacing},
                            {7f / 8, 0.4f+spacing},
                            {1f-spacing, 0.5f+spacing},
                            {1f-spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 1f-spacing, 0.6f-spacing},
                    {0f+spacing, 0.4f+spacing, 1f-spacing, 1f-spacing}
            };
        }

        public static final boolean[][] TEMP_CURVED = {
                {
                        false,
                        false,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        false,
                        false
                },
                {
                        false,
                        false,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        false,
                        false
                }
        };
    }

    public static final class GRID_2L_12T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][] {
                    {
                            {0f+spacing, 0f+spacing},
                            {0.5f-spacing, 0f+spacing},
                            {0.6f-spacing, 1f / 8},
                            {0.5f-spacing, 2f / 8},
                            {0.4f-spacing, 3f / 8},
                            {0.5f-spacing, 4f / 8},
                            {0.6f-spacing, 5f / 8},
                            {0.5f-spacing, 6f / 8},
                            {0.4f-spacing, 7f / 8},
                            {0.5f-spacing, 1f-spacing},
                            {0f+spacing, 1f-spacing}
                    },
                    {
                            {1f-spacing, 0f+spacing},
                            {0.5f+spacing, 0f+spacing},
                            {0.6f+spacing, 1f / 8},
                            {0.5f+spacing, 2f / 8},
                            {0.4f+spacing, 3f / 8},
                            {0.5f+spacing, 4f / 8},
                            {0.6f+spacing, 5f / 8},
                            {0.5f+spacing, 6f / 8},
                            {0.4f+spacing, 7f / 8},
                            {0.5f+spacing, 1f-spacing},
                            {1f-spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 0.6f-spacing, 1f-spacing},
                    {0.4f+spacing, 0f+spacing, 1f-spacing, 1f-spacing}
            };
        }

        public static final boolean[][] TEMP_CURVED = {
                {
                        false,
                        false,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        false,
                        false
                },
                {
                        false,
                        false,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        false,
                        false
                }
        };
    }

    public static final class GRID_2L_14T {
        public static float[][][] getGridPointsMargined(float spacing) {
            return new float[][][] {
                    {
                            {0f+spacing, 0f+spacing},
                            {2f / 3-spacing, 0f+spacing},
                            {2f / 3-spacing, 2f / 3-spacing},
                            {0f+spacing, 2f / 3-spacing},
                            {0f+spacing, 0f+spacing}
                    },
                    {
                            {1f-spacing, 1f-spacing},
                            {1f-spacing, 1f / 3+spacing},
                            {1f / 3+spacing, 1f / 3+spacing},
                            {1f / 3+spacing, 1f-spacing},
                            {1f-spacing, 1f-spacing},
                    }
            };
        }

        public static float[][] getGridRectMargined(float spacing) {
            return new float[][]{
                    {0f+spacing, 0f+spacing, 2f / 3-spacing, 2f / 3-spacing},
                    {1f / 3+spacing, 1f / 3+spacing, 1f-spacing, 1f-spacing}
            };
        }

        public static final boolean[][] TEMP_CURVED = {
                {
                        false,
                        false,
                        true,
                        false,
                        false
                },
                {
                        false,
                        false,
                        true,
                        false,
                        false
                }
        };
    }
}
