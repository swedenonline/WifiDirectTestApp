package com.bilalbaloch.wifidirecttestapp.model;

/**
 * Created by bilal on 2017-11-02.
 */

public class PingData {

    private PingStatus mPingStatus;
    private String mPingTime;
    private String mPingText;

    /**
     * Constructor.
     *
     * @param pingStatus
     * @param pingTime
     * @param pingText
     */
    public PingData(final PingStatus pingStatus, final String pingTime, final String pingText) {
        mPingStatus = pingStatus;
        mPingTime = pingTime;
        mPingText = pingText;
    }

    /**
     * Returns ping status.
     */
    public PingStatus getPingStatus() {
        return mPingStatus;
    }

    /**
     * Returns ping time.
     */
    public String getPingTime() {
        return mPingTime;
    }

    /**
     * Returns ping text.
     */
    public String getPingText() {
        return mPingText;
    }
}
