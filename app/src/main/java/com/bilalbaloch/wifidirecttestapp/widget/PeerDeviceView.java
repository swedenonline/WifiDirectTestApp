package com.bilalbaloch.wifidirecttestapp.widget;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bilalbaloch.wifidirecttestapp.R;

/**
 * Created by bilal on 2017-11-02.
 */

public class PeerDeviceView extends RelativeLayout {

    private ImageView mDeviceStatus;
    private TextView mDeviceName;

    /**
     * Constructor.
     *
     * @param context
     */
    public PeerDeviceView(Context context) {
        super(context);
        init();
    }

    /**
     * Constructor.
     *
     * @param context
     * @param attrs
     */
    public PeerDeviceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackground(ContextCompat.getDrawable(getContext(), android.R.drawable.alert_light_frame));
        inflate(getContext(), R.layout.peer_device_view, this);
        mDeviceStatus = (ImageView) findViewById(R.id.device_status);
        mDeviceName = (TextView) findViewById(R.id.device_name);
    }

    /**
     * Update view.
     *
     * @param device
     */
    public void updateView(final WifiP2pDevice device) {
        final int imageSrc;

        switch (device.status) {
            case WifiP2pDevice.AVAILABLE:
                imageSrc = android.R.drawable.star_on;
                break;
            default:
                imageSrc = android.R.drawable.star_off;
                break;

        }

        mDeviceStatus.setImageDrawable(ContextCompat.getDrawable(getContext(), imageSrc));
        mDeviceName.setText(device.deviceName);
    }

}
