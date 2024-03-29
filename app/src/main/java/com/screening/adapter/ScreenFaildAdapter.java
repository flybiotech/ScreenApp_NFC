package com.screening.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activity.R;
import com.screening.model.ScreenErrorList;

import java.util.List;

public class ScreenFaildAdapter extends BaseAdapter {

    private Context context;
    private List<ScreenErrorList> addmessageList;

    public ScreenFaildAdapter(Context context, List<ScreenErrorList> addmessageList) {
        this.context = context;
        this.addmessageList = addmessageList;
    }

    @Override
    public int getCount() {
        return addmessageList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.list_message, null);
            viewHolder.tv_id = view.findViewById(R.id.tv_id);
            viewHolder.tv_name = view.findViewById(R.id.tv_name);
            viewHolder.tv_phone = view.findViewById(R.id.tv_phone);
            viewHolder.tv_idCard = view.findViewById(R.id.tv_idCard);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_id.setText(addmessageList.get(i).getpId());
        viewHolder.tv_name.setText(addmessageList.get(i).getName());
        viewHolder.tv_phone.setText(addmessageList.get(i).getScreeningId());
        viewHolder.tv_idCard.setText(addmessageList.get(i).getErrorMsg());
        return view;
    }

    class ViewHolder {
        TextView tv_id, tv_name, tv_phone, tv_idCard;
    }
}
