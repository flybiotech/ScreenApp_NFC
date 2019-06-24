package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activity.R;

import java.util.List;

public class ResoluteAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;



    List<String> items;
    public ResoluteAdapter(Context context) {
        mLayoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    public ResoluteAdapter(Context context, List<String> its) {  //暂时这个里面的type 都为1
        mLayoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        items=its;
    }
    public void setItems( List<String> array){
        items = array;
    }

    @Override
    public int getCount() {
        if(items != null)
            return items.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    ///////////////////////////////////////////////////////
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }
    ///////////////////////////////////////////////////////

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View firstItemView = null;

        //获取到当前位置所对应的Type

        firstItemView = convertView;
        ViewHolder firstItemViewHolder=null;
        if (firstItemView==null) {
            System.out.println("firstItemView==null ");
            firstItemView = mLayoutInflater.inflate(R.layout.list_resolute_sp,null);
            firstItemViewHolder=new ViewHolder();
            firstItemViewHolder.textView=(TextView) firstItemView.findViewById(R.id.item);
            firstItemView.setTag(firstItemViewHolder);

        } else {
            firstItemViewHolder=(ViewHolder) firstItemView.getTag();
        }
        if (firstItemViewHolder.textView!=null) {
            firstItemViewHolder.textView.setText(items.get(position));
        }
        convertView=firstItemView;
        return convertView;
    }

    //第一个Item的ViewHolder
    private class ViewHolder{

        TextView textView;
    }


}
