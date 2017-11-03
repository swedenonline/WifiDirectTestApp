package com.bilalbaloch.wifidirecttestapp.widget.adapter;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bilalbaloch.wifidirecttestapp.widget.PeerDeviceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bilal on 2017-10-25.
 */

public class PeerDevicesListAdapter extends BaseAdapter {

    private final Context mContext;
    private List<WifiP2pDevice> mPeerDevices = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param context application context.
     */
    public PeerDevicesListAdapter(final Context context) {
        super();
        mContext = context;
    }

    @Override
    public int getCount() {
        return mPeerDevices.size();
    }

    @Override
    public WifiP2pDevice getItem(int i) {
        return mPeerDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final WifiP2pDevice device = getItem(i);

        if (view == null) {
            view = new PeerDeviceView(mContext);
        }

        ((PeerDeviceView) view).updateView(device);
        return view;
    }

    /**
     * update list of peer devices.
     *
     * @param peerDevices list of peer devices to update.
     */
    public void update(final List<WifiP2pDevice> peerDevices) {
        if (peerDevices.equals(mPeerDevices)) {
            return;
        }

        mPeerDevices.clear();

        if (peerDevices.size() > 0) {
            mPeerDevices.addAll(peerDevices);
            notifyDataSetChanged();
        } else {
            notifyDataSetInvalidated();
        }

    }
}
