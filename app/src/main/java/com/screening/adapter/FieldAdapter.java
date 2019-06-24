package com.screening.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.activity.R;
import com.screening.model.FieldObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhangbin on 2018/4/23.
 */

public class FieldAdapter extends BaseAdapter {
    private List<FieldObject> list;//展示的字段集合
    private Context context;
    private static List<String> list1 = new ArrayList<>();//选择的必填字段的集合
    private int position;//字段选择时的position
    static ViewHolder viewHolder = null;
    private TextView tv_haveField;

    public FieldAdapter(Context context, List<FieldObject> list, TextView tv_haveField) {
        this.context = context;
        this.list = list;
        this.tv_haveField = tv_haveField;
    }

    @Override
    public int getCount() {
        return list.size();
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

        this.position = i;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.field_list, null);
            viewHolder.checkBox = view.findViewById(R.id.cb);
            viewHolder.textView = view.findViewById(R.id.textview_field);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(list.get(i).getTextView());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    list1.add(list.get(i).getTextView());
                    tv_haveField.setText("");
                    for (int i = 0; i < list1.size(); i++) {
                        tv_haveField.setText(tv_haveField.getText().toString().trim() + "  " + list1.get(i));
                    }

                } else {
                    list1.remove(list.get(i).getTextView());
                    tv_haveField.setText("");
                    for (int i = 0; i < list1.size(); i++) {
                        tv_haveField.setText(tv_haveField.getText().toString().trim() + "  " + list1.get(i));
                    }
                }
            }
        });
        return view;
    }

    class ViewHolder {
        CheckBox checkBox;
        TextView textView;
    }

    public static void setList() {//清空上次残留的数据
        if (list1 != null) {
            list1.clear();
        }
    }

    public static List returnList() {//得到选择的数据集合
        return list1;
    }
}
