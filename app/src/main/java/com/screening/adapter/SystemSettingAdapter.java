package com.screening.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activity.R;

import java.util.List;

/**
 * Created by dell on 2018/4/25.
 */

public class SystemSettingAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mData;
    private LayoutInflater inflater;

    public SystemSettingAdapter(Context context, List<String> mData) {
        mContext = context;
        this.mData = mData;
        inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
//        View view;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_setting_item, parent, false);
            holder.mTextView = (TextView) convertView.findViewById(R.id.list_setting_item_tv);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(mData.get(position));


        return convertView;
    }


    class ViewHolder {

        public TextView mTextView;


    }

    private void setData(List<String> mData) {
        this.mData = mData;
    }


}
