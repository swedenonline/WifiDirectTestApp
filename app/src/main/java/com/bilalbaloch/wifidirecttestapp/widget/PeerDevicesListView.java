package com.bilalbaloch.wifidirecttestapp.widget;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.bilalbaloch.wifidirecttestapp.connection.WifiDirectManager;
import com.bilalbaloch.wifidirecttestapp.widget.adapter.PeerDevicesListAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.net.wifi.p2p.WifiP2pManager.BUSY;
import static android.net.wifi.p2p.WifiP2pManager.ERROR;

/**
 * Created by bilal on 2017-10-25.
 */

public class PeerDevicesListView extends GridView
        implements AdapterView.OnItemClickListener,
        WifiDirectManager.WifiDirectManagerListener {

    private static final String TAG = PeerDevicesListView.class.getSimpleName();
    private PeerDevicesListAdapter mAdapter;
    private OnDeviceClickListener mOnDeviceClickListener;
    private WifiDirectManager mWifiDirectManager;
    private List<WifiP2pDevice> mPeerDevices = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param context application context.
     */
    public PeerDevicesListView(Context context) {
        super(context);
        init();
    }

    /**
     * Constructor.
     *
     * @param context applicaton context.
     * @param attrs   @see <code>AttributeSet</code>
     */
    public PeerDevicesListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mWifiDirectManager = new WifiDirectManager(getContext());
        mAdapter = new PeerDevicesListAdapter(getContext());
        setAdapter(mAdapter);
        setOnItemClickListener(this);
    }

    /**
     * sets device click listener.
     *
     * @param listener callback listener.
     */
    public void setOnDeviceClickListener(final OnDeviceClickListener listener) {
        mOnDeviceClickListener = listener;
    }

    /**
     * updates device list.
     *
     * @param mPeerDevices list to update.
     */
    public void updateList(final List<WifiP2pDevice> mPeerDevices) {
        mAdapter.update(mPeerDevices);
    }

    private void resetToggleConnect() {
        mPeerDevices.clear();
        updateList(mPeerDevices);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mOnDeviceClickListener.onDeviceClicked((WifiP2pDevice) getAdapter().getItem(i));
    }

    /**
     * Device click callbacks.
     */
    public interface OnDeviceClickListener {
        void onDeviceClicked(WifiP2pDevice wifiP2pDevice);
    }

    @Override
    public void onWifiP2pEnableStateChange(boolean state) {
        Log.d(TAG, "onWifiP2pEnableStateChange");
    }

    @Override
    public void onChannelDisconnected() {
        Log.d(TAG, "onChannelDisconnected");
        resetToggleConnect();
    }

    @Override
    public void onSuccessDiscoverPeers() {
        Log.d(TAG, "onSuccessDiscoverPeers");
    }

    @Override
    public void onFailureDisconverPeers(int errorCode) {
        switch (errorCode) {
            case BUSY:
                Log.d(TAG, "busy");
                break;
            case ERROR:
                Log.d(TAG, "error");
                break;
            default:
                break;
        }
        Log.d(TAG, "onFailureDisconverPeers");
        resetToggleConnect();
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {

        final List<WifiP2pDevice> tempList;

        if (wifiP2pDeviceList.getDeviceList() instanceof List) {
            tempList = (List) wifiP2pDeviceList.getDeviceList();
        } else {
            tempList = new ArrayList<>(wifiP2pDeviceList.getDeviceList());
        }

        if (mPeerDevices.equals(tempList)) {
            return;
        }

        mPeerDevices = tempList;

        if (mPeerDevices.size() > 0) {
            Log.d(TAG, "onPeersAvailable");
            updateList(mPeerDevices);
        } else {
            Log.d(TAG, "no Peer devices Available");
            resetToggleConnect();
        }
    }

    @Override
    public void updateThisDevice(WifiP2pDevice wifiP2pDevice) {
        Log.d(TAG, "updateThisDevice");
        //Log.d(TAG, wifiP2pDevice.toString());
    }

    @Override
    public void onConnectFailure(int errorCode) {
        switch (errorCode) {
            case BUSY:
                Log.d(TAG, "busy");
                break;
            case ERROR:
                Log.d(TAG, "error");
                break;
            default:
                break;
        }
        Log.d(TAG, "onConnectFailure");
        resetToggleConnect();
    }

    @Override
    public void onConnectSuccess() {
        Log.d(TAG, "onConnectSuccess");
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
        Log.d(TAG, "onConnectionInfoAvailable");
        Log.d(TAG, wifiP2pInfo.toString());
    }

    /**
     * Discover devices using wifidirect.
     */
    public void discover() {
        mWifiDirectManager.start(this);
    }

    public void stop() {
        mWifiDirectManager.stop();
    }
}
