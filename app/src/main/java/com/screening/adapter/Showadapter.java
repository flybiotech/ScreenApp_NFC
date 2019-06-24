package com.screening.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.R;
import com.screening.model.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangbin on 2018/5/10.
 */

public class Showadapter extends BaseAdapter {
    private Context context;
    private List<Bean> constList;
    private static List<String> fileList = new ArrayList<>();//选择的图片的路径的集合

    public Showadapter(Context context, List<Bean> constList) {
        this.context = context;
        this.constList = constList;
    }

    @Override
    public int getCount() {
        return constList.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.listmanager, null);
            viewHolder.imageView = view.findViewById(R.id.iv_show);
            viewHolder.textView = view.findViewById(R.id.tv_show);
            viewHolder.cb = view.findViewById(R.id.cb);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.imageView.setImageResource(constList.get(i).getImg());
        viewHolder.textView.setText(constList.get(i).getFileName());
        viewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//如果chekbox被选中，与其对应的文件路径会被加入到list集合里
                    fileList.add(constList.get(i).getFilePath());
                } else {
                    fileList.remove(constList.get(i).getFilePath());
                }
            }
        });
        return view;
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
        CheckBox cb;
    }

    public static void setList() {
        if (fileList != null) {
            fileList.clear();
        }

    }

    public static List getList() {//返回被选中图片的list集合
        return fileList;
    }
}
