package com.bilalbaloch.wifidirecttestapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by bilal on 2017-11-02.
 */

public final class TimeUtils {

    private static final String DATE_FORMAT = "hh:mm:ss";
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_FORMAT);

    /**
     * Constructor.
     */
    private TimeUtils() {

    }

    /**
     * Returns current time in milliseconds.
     */
    public static String getCurrentTime() {
        final Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.MILLISECOND));
    }

    /**
     * Returns milliseconds in H:M:S format
     *
     * @param timeInMillis milliseconds to convert.
     */
    public static String getHumanReadableTimeFormat(final String timeInMillis) {
        final Long millis = Long.parseLong(timeInMillis);
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return FORMATTER.format(c.getTime());
    }
}
