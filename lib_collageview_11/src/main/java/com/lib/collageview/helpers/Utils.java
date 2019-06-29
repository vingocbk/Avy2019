package com.lib.collageview.helpers;

public class Utils {
    public static final Integer[] arr = {15, 16, 19, 20, 21, 23, 24, 28};


    public static int mapIndex(int index) {
        switch (index) {
            case 15:
                return 6;
            case 16:
                return 7;
            case 19:
                return 4;
            case 20:
                return 5;
            case 21:
                return 3;
            case 23:
                return 1;
            case 24:
                return 2;
            case 28:
                return 8;
            default:
                return 0;
        }

    }
}
