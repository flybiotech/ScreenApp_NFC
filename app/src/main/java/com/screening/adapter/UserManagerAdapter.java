package com.screening.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activity.R;
import com.screening.manager.UserManager;
import com.screening.model.User;

import java.util.List;

/**
 * Created by dell on 2018/4/25.
 */

public class UserManagerAdapter extends BaseAdapter {

    private Context mContext;
    private List<User> mList;
    private LayoutInflater inflater;

    public UserManagerAdapter(Context mContext, List<User> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = (View) inflater.inflate(R.layout.list_usermanager_item, parent, false);
            holder.mTextView = (TextView) convertView.findViewById(R.id.list_usermanager_item_tv);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(mList.get(position).getName());

        return convertView;
    }


    class ViewHolder {
        TextView mTextView;

    }

    public void setList(List<User> mUser) {
        mList = mUser;
    }


}
