package com.bilalbaloch.wifidirecttestapp.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bilalbaloch.wifidirecttestapp.model.PingData;
import com.bilalbaloch.wifidirecttestapp.widget.PingStateView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bilal on 2017-10-25.
 */

public class PingListAdapter extends BaseAdapter {

    private final Context mContext;
    private List<PingData> mRows = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param context application context.
     */
    public PingListAdapter(final Context context) {
        super();
        mContext = context;
    }

    @Override
    public int getCount() {
        return mRows.size();
    }

    @Override
    public PingData getItem(int i) {
        return mRows.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final PingData pingData = getItem(i);

        if (view == null) {
            view = new PingStateView(mContext);
        }

        ((PingStateView) view).updateView(pingData);
        return view;
    }

    /**
     * Sets ping data.
     *
     * @param data ping data to set.
     */
    public void setPingData(final PingData data) {
        mRows.add(0, data);
        notifyDataSetChanged();
    }
}
