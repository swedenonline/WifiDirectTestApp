package com.bilalbaloch.wifidirecttestapp.activity;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bilalbaloch.wifidirecttestapp.R;
import com.bilalbaloch.wifidirecttestapp.connection.WifiDirectManager;
import com.bilalbaloch.wifidirecttestapp.widget.PeerDevicesListView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, PeerDevicesListView.OnDeviceClickListener {

    private FloatingActionButton mDiscoverButton;
    private WifiDirectManager mWifiDirectManager;
    private PeerDevicesListView mPeerDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWifiDirectManager = new WifiDirectManager(this);

        initView();
    }

    private void initView() {
        mPeerDevices = (PeerDevicesListView) findViewById(R.id.list_view);
        mDiscoverButton = (FloatingActionButton) findViewById(R.id.discover);
        mDiscoverButton.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        final int id = view.getId();
        switch (id) {
            case R.id.discover:
                mPeerDevices.discover(mWifiDirectManager, this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDeviceClicked(WifiP2pDevice wifiP2pDevice) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPeerDevices.stop();
    }

}
