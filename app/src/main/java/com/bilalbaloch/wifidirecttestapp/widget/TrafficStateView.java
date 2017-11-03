package com.bilalbaloch.wifidirecttestapp.widget;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bilal on 2017-11-01.
 */

public class TrafficStateView extends AppCompatTextView {

    private static final int CALL_UPDATE_METHOD = 1;
    private Handler mHandler;
    private Timer mTimer = new Timer();

    /**
     * Constructor.
     *
     * @param context application context.
     */
    public TrafficStateView(Context context) {
        super(context);
        init();
    }

    /**
     * Constructor.
     *
     * @param context application context.
     * @param attrs   attribute set.
     */
    public TrafficStateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.drawable.alert_light_frame));
        setPadding(30, 30, 30, 30);
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CALL_UPDATE_METHOD:
                        update();
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mHandler == null) {
                    return;
                }
                mHandler.sendEmptyMessage(CALL_UPDATE_METHOD);
            }
        }, 1, 1000);
    }

    private int getCurrentAppUid() {
        return Binder.getCallingUid();
    }

    private float getUidTxBytes() {
        float bytes = TrafficStats.getUidTxBytes(getCurrentAppUid());
        float mbs = Math.round(bytes / (Math.pow(1024, 2)));
        return mbs;
    }

    private float getUidRxBytes() {
        float bytes = TrafficStats.getUidRxBytes(getCurrentAppUid());
        float mbs = Math.round(bytes / (Math.pow(1024, 2)));
        return mbs;
    }

    private float getMobileTxBytes() {
        float bytes = TrafficStats.getMobileTxBytes();
        float mbs = Math.round(bytes / (Math.pow(1024, 2)));
        return mbs;
    }

    private float getMobileRxBytes() {
        float bytes = TrafficStats.getMobileRxBytes();
        float mbs = Math.round(bytes / (Math.pow(1024, 2)));
        return mbs;
    }

    public void update() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Mobile Transmitted = " + getUidTxBytes() + " Mbits" + System.getProperty("line.separator"));
        stringBuilder.append("Mobile Received = " + getUidRxBytes() + " Mbits" + System.getProperty("line.separator"));
        stringBuilder.append("Total Mobile Transmitted = " + getMobileTxBytes() + " Mbits" + System.getProperty("line.separator"));
        stringBuilder.append("Total Mobile Received = " + getMobileRxBytes() + " Mbits" + System.getProperty("line.separator"));
        setText(stringBuilder);
    }

    public void destroyView() {
        mTimer.cancel();
        mTimer = null;
        mHandler = null;
    }
}
