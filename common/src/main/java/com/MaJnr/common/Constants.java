package com.MaJnr.common;

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
    public static final String REDIRECT_MOBILE_PATH = "/redirect_mobile";
    public static final String REDIRECT_WEAR_PATH = "/redirect_wear";
    public static final String DEVICES_SYNC_PATH = "/sync";
    public static final String STOP_MEASURE_PATH = "/stop";
    public static final String MOBILE_QUIT_APP_PATH = "/mobile_quit";
    public static final String WEAR_QUIT_APP_PATH = "/wear_quit";

    // measures
    public static final String DEVICES_SYNC = "com.example.common.DEVICES_SYNC";
    public static final String HEART_MEASURE_1 = "com.example.common.HEART_MEASURE_1";
    public static final String HEART_MEASURE_2 = "com.example.common.HEART_MEASURE_2";
    public static final String HEART_MEASURE_3 = "com.example.common.HEART_MEASURE_3";
    public static final String STOP_MEASURE = "com.example.common.STOP_MEASURE";
    public static final String QUIT_APP = "com.example.common.QUIT_APP";

    // duration of all heartrate measures (in milisec)
    public static final int MEASURE_DURATION = 10 * 1000;

    // check if measure started
    public static final String START_MEASURE = "com.example.common.START_MEASURE";

    // list of measures done on MEASURE_DURATION time
    public static List<Integer> measuresList1 = new ArrayList<>();
    public static List<Integer> measuresList2 = new ArrayList<>();
    public static List<Integer> measuresList3 = new ArrayList<>();

    // current measure number
    public static int measureNb = 1;

    // check if redirect button has been pushed
    public static final String REDIRECT_MOBILE = "com.example.common.REDIRECT_MOBILE";
    public static final String REDIRECT_WEAR = "com.example.common.REDIRECT_WEAR";

    public static final String POST_TEST_AD = "ca-app-pub-1795579585601289/1266772680";
    public static final String POST_TEST_AD_TEST = "ca-app-pub-3940256099942544/1033173712";

    public static boolean isWaitFragmentRunning = false;
}
