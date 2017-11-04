package com.bilalbaloch.wifidirecttestapp.connection;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.util.Log;

/**
 * Created by bilal on 2017-10-24.
 */

public class WifiDirectManager extends BroadcastReceiver {

    private static final String TAG = WifiDirectManager.class.getSimpleName();
    private WifiDirectManagerListener mWifiDirectManagerListener;
    private Context mContext;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private volatile boolean mIsStarted = false;


    /**
     * Constructor.
     */
    private WifiDirectManager() {
        throw new RuntimeException("Call constructor with parameter application context");
    }

    /**
     * Constructor.
     *
     * @param context application context.
     */
    public WifiDirectManager(final Context context) {
        mContext = context;
    }

    private void init() {

        mIsStarted = true;

        registerReceiver();

        mManager = (WifiP2pManager) mContext.getSystemService(Service.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(mContext, Looper.getMainLooper(), new WifiP2pManager.ChannelListener() {
            @Override
            public void onChannelDisconnected() {
                mWifiDirectManagerListener.onChannelDisconnected();
            }
        });
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                mWifiDirectManagerListener.onSuccessDiscoverPeers();
            }

            @Override
            public void onFailure(final int i) {
                mWifiDirectManagerListener.onFailureDisconverPeers(i);
            }
        });
    }

    /**
     * Registers receiver.
     *
     * @param listener WifiDirectManagerListener instance.
     */
    public void start(final WifiDirectManagerListener listener) {

        if (mIsStarted) {
            stop();
        }

        mWifiDirectManagerListener = listener;

        init();
    }

    /**
     * Connect to provided device.
     *
     * @param wifiP2pDevice
     */
    public void connect(final WifiP2pDevice wifiP2pDevice) {
        Log.d(TAG, wifiP2pDevice.toString());
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wifiP2pDevice.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                mWifiDirectManagerListener.onConnectSuccess();
            }

            @Override
            public void onFailure(int i) {
                mWifiDirectManagerListener.onConnectFailure(i);
            }
        });
        mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                mWifiDirectManagerListener.onConnectionInfoAvailable(wifiP2pInfo);
            }
        });
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mContext.registerReceiver(this, intentFilter);
    }

    /**
     * Unregisters receiver.
     */
    public void stop() {
        mIsStarted = false;
        try {
            mContext.unregisterReceiver(this);
            mWifiDirectManagerListener = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                setIsWifiP2pEnabled(true);
            } else {
                setIsWifiP2pEnabled(false);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (mManager != null) {
                mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                        mWifiDirectManagerListener.onPeersAvailable(wifiP2pDeviceList);
                    }
                });
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            // Connection state changed!  We should probably do something about
            // that.

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            mWifiDirectManagerListener.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));

        }
    }

    /**
     * Sets state of Wifi P2p.
     *
     * @param isWifiP2pEnabled true on enabled, else false.
     */
    public void setIsWifiP2pEnabled(final boolean isWifiP2pEnabled) {
        mWifiDirectManagerListener.onWifiP2pEnableStateChange(isWifiP2pEnabled);
    }

    /**
     * WifiP2pManager Callbacks.
     */
    public interface WifiDirectManagerListener {

        void onWifiP2pEnableStateChange(boolean state);

        void onChannelDisconnected();

        void onSuccessDiscoverPeers();

        void onFailureDisconverPeers(int errorCode);

        void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList);

        void updateThisDevice(WifiP2pDevice wifiP2pDevice);

        void onConnectFailure(int errorCode);

        void onConnectSuccess();

        void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo);
    }
}
