package com.bilalbaloch.wifidirecttestapp.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bilalbaloch.wifidirecttestapp.R;
import com.bilalbaloch.wifidirecttestapp.model.PingData;
import com.bilalbaloch.wifidirecttestapp.model.PingStatus;
import com.bilalbaloch.wifidirecttestapp.utils.TimeUtils;

/**
 * Created by bilal on 2017-11-02.
 */

public class PingStateView extends RelativeLayout {

    private TextView mPingTextTextView;
    private TextView mPingTimeTextView;

    /**
     * Constructor.
     *
     * @param context
     */
    public PingStateView(Context context) {
        super(context);
        init();
    }

    /**
     * Constructor.
     *
     * @param context
     * @param attrs
     */
    public PingStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.ping_state_view, this);
        mPingTextTextView = (TextView) findViewById(R.id.ping_text_textview);
        mPingTimeTextView = (TextView) findViewById(R.id.ping_time_textview);
    }

    /**
     * Update view.
     *
     * @param pingData
     */
    public void updateView(final PingData pingData) {
        mPingTextTextView.setTextColor(ContextCompat.getColor(getContext(), getTextColor(pingData.getPingStatus())));
        mPingTimeTextView.setTextColor(ContextCompat.getColor(getContext(), getTextColor(pingData.getPingStatus())));
        mPingTextTextView.setText(pingData.getPingText());
        mPingTimeTextView.setText(TimeUtils.getHumanReadableTimeFormat(pingData.getPingTime()));
    }

    private int getTextColor(PingStatus pingStatus) {
        final int textColor;
        switch (pingStatus) {
            case CONNECTED:
                textColor = android.R.color.holo_green_dark;
                break;
            case DISCONNECTED:
                textColor = android.R.color.black;
                break;
            case ERROR:
                textColor = android.R.color.holo_red_dark;
                break;
            case PINGING:
                default:
                    textColor = android.R.color.darker_gray;
                break;
        }
        return textColor;
    }

}
