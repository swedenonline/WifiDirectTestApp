package com.bilalbaloch.wifidirecttestapp.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bilal on 2017-11-02.
 */

public final class TimeUtils {

    private static final String DATE_FORMAT = "HH:mm:ss:SSS";
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_FORMAT);
    private static final String TAG = TimeUtils.class.getSimpleName();

    /**
     * Constructor.
     */
    private TimeUtils() {

    }

    /**
     * Returns current time in milliseconds.
     */
    public static String getCurrentTime() {
        //Log.d(TAG, FORMATTER.format(new Date()));
        return FORMATTER.format(new Date());
    }

    /**
     * Returns milliseconds in H:M:S format
     *
     * @param timeInMillis milliseconds to convert.
     */
    public static String getHumanReadableTimeFormat(final String timeInMillis) {
        //Log.d(TAG, timeInMillis);
        return timeInMillis;
    }
}
