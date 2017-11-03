package com.bilalbaloch.wifidirecttestapp.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ListView;

import com.bilalbaloch.wifidirecttestapp.model.PingData;
import com.bilalbaloch.wifidirecttestapp.model.PingStatus;
import com.bilalbaloch.wifidirecttestapp.utils.TimeUtils;
import com.bilalbaloch.wifidirecttestapp.widget.adapter.PingListAdapter;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bilal on 2017-10-25.
 */

public class PingListView extends ListView {

    private static final String PING_COMMAND = "/system/bin/ping -c 1 8.8.8.8";
    private static final int UPDATE_PING_DATA = 2;
    private static final int PING_PERIOD = 1000;
    private static final int PING_SUCCESS = 0;
    private PingListAdapter mAdapter;
    private Handler mHandler;
    private Timer mTimer;
    private Runtime mRuntime;
    private Process mProcess;

    /**
     * Constructor.
     *
     * @param context application context.
     */
    public PingListView(Context context) {
        super(context);
        init();
    }

    /**
     * Constructor.
     *
     * @param context applicaton context.
     * @param attrs   @see <code>AttributeSet</code>
     */
    public PingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackground(ContextCompat.getDrawable(getContext(), android.R.drawable.alert_light_frame));
        mAdapter = new PingListAdapter(getContext());
        setAdapter(mAdapter);
        mRuntime = Runtime.getRuntime();
        schedulePing();
    }

    private void schedulePing() {
        mTimer = new Timer();
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_PING_DATA:
                        updateList((PingData) msg.obj);
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
                try {
                    sendMessage(new PingData(PingStatus.PINGING, (TimeUtils.getCurrentTime()), "pinging..."));
                    if (doPing()) {
                        sendMessage(new PingData(PingStatus.CONNECTED, (TimeUtils.getCurrentTime()), "connected"));
                    } else {
                        sendMessage(new PingData(PingStatus.DISCONNECTED, (TimeUtils.getCurrentTime()), "disconnected"));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    sendMessage(new PingData(PingStatus.ERROR, (TimeUtils.getCurrentTime()), "error"));
                } catch (InterruptedException e) {
                    sendMessage(new PingData(PingStatus.ERROR, (TimeUtils.getCurrentTime()), "error"));
                }
            }
        }, 0, PING_PERIOD);
    }

    private boolean doPing() throws IOException, InterruptedException {
        mProcess = mRuntime.exec(PING_COMMAND);
        final int result = mProcess.waitFor();
        return (result == PING_SUCCESS);
    }

    private void sendMessage(final PingData pingData) {
        if (mHandler == null) {
            return;
        }

        mHandler.sendMessage(Message.obtain(mHandler, UPDATE_PING_DATA, pingData));
    }

    private void updateList(final PingData data) {
        mAdapter.setPingData(data);
    }

    /**
     * Call it on activity's onDestroy to clear resources.
     */
    public void onDestroy() {
        mHandler = null;
        mTimer.cancel();
        mTimer = null;
        mProcess.destroy();
        mRuntime.gc();
    }
}
