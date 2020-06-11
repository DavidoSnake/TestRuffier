package com.example.common;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    // messages and values
    public static final String MEASURE_1_MSG = "m1";
    public static final String MEASURE_2_MSG = "m2";
    public static final String MEASURE_3_MSG = "m3";
    public static final String START_MEASURE_MSG = "start measure";

    // pathes
    public static final String HEART_RATE_COUNT_PATH="/count";
    public static final String START_MEASURE_PATH="/start";

    // measures
    public static final String HEART_MEASURE_1 = "com.example.common.HEART_MEASURE_1";
    public static final String HEART_MEASURE_2 = "com.example.common.HEART_MEASURE_2";
    public static final String HEART_MEASURE_3 = "com.example.common.HEART_MEASURE_3";

    // duration of all heartrate measures (in milisec)
    public static final int MEASURE_DURATION = 10 * 1000;

    // check if measure started
    public static final String START_MEASURE = "com.example.common.START_MEASURE";

    // used to send different signal in order to trigger ondatachange on MainActivityWear class
    public static boolean changeValue = false;

    // list of measures done on MEASURE_DURATION time
    public static List<Integer> measuresList1 = new ArrayList<>();
    public static List<Integer> measuresList2 = new ArrayList<>();
    public static List<Integer> measuresList3 = new ArrayList<>();

    // current measure number
    public static int measureNb = 1;
}
