package com.screening.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.R;
import com.orhanobut.logger.Logger;
import com.screening.model.ListMessage;
import com.util.Constss;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/9/12.
 */

public class CaseManagerAdapter extends BaseAdapter {
    private Context context;
    private List<ListMessage> mList;//查询的患者的集合

    public CaseManagerAdapter(Context context, List<ListMessage> list) {
        this.context = context;
        this.mList = list;
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
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
//        View view;
        if (convertView == null) {
            holder = new ViewHolder();
            // 如果convertView为空，则表示第一次显示该条目，需要创建一个view
            convertView = LayoutInflater.from(context).inflate(R.layout.item_casesearchlist, null);
            //将findviewbyID的结果赋值给holder对应的成员变量
            holder.tvBianHao = convertView.findViewById(R.id.tv_caselist_bianhao);
            holder.tvAge = convertView.findViewById(R.id.tv_caselist_age);
            holder.tvName = convertView.findViewById(R.id.tv_caselist_name);
            holder.tvTel = convertView.findViewById(R.id.tv_caselist_phone);
            holder.isSceen = convertView.findViewById(R.id.tv_caselist_isSceen);
            holder.IDCard = convertView.findViewById(R.id.tv_caselist_idcard);
            // 将holder与view进行绑定
            convertView.setTag(holder);
        } else {
            // 否则表示可以复用convertView
            holder = (ViewHolder) convertView.getTag();
        }

        // 直接操作holder中的成员变量即可，不需要每次都findViewById
        holder.tvBianHao.setText(":" + mList.get(position).getpId());
        holder.tvAge.setText(":" + mList.get(position).getAge());
        holder.tvName.setText(":" + mList.get(position).getName());
        holder.tvTel.setText(":" + mList.get(position).getPhone());
        holder.IDCard.setText(":" + mList.get(position).getIdCard());
        if (mList.get(position).getScreenState() < 5) {
            holder.isSceen.setText(":未上传");
            holder.isSceen.setTextColor(context.getResources().getColor(R.color.blue));

        } else {
            holder.isSceen.setText(":已上传");
            holder.isSceen.setTextColor(context.getResources().getColor(R.color.appcolor1));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvBianHao;
        TextView tvAge;
        TextView tvName;
        TextView tvTel;
        TextView isSceen;
        TextView IDCard;
    }
}
