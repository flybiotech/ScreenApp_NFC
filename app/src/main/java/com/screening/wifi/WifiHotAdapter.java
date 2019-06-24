package com.screening.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activity.R;

import java.util.List;


public class WifiHotAdapter extends BaseAdapter {

    public List<ScanResult> mResults;

    private Context mContext;

    public WifiHotAdapter(List<ScanResult> results, Context mContext) {

        this.mResults = results;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public Object getItem(int position) {
        return mResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView nameTxt = null;
        TextView levelTxt = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.wifihot_layout, null);
        }
        nameTxt = (TextView) convertView.findViewById(R.id.hotName);
        levelTxt = (TextView) convertView.findViewById(R.id.hotLevel);
        nameTxt.setText(mResults.get(position).SSID);
        levelTxt.setText("Level :" + mResults.get(position).level);
        return convertView;
    }

    public void refreshData(List<ScanResult> results) {
        this.mResults = results;
        this.notifyDataSetChanged();
    }

    public void clearData() {

        if (mResults != null && mResults.size() > 0) {
            mResults.clear();
            mResults = null;
        }
    }
}
