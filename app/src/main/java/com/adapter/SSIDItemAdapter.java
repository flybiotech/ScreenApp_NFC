package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activity.R;
import com.application.MyApplication;
import com.model.SSIDModel;

import java.util.List;

public class SSIDItemAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;



    List<SSIDModel> mSSIDs;
    public SSIDItemAdapter(Context context) {
        mLayoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    public SSIDItemAdapter(Context context, List<SSIDModel> its) {  //暂时这个里面的type 都为1
        mLayoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        mSSIDs=its;
    }
    public void setItems( List<SSIDModel> array){
        mSSIDs = array;
    }

    @Override
    public int getCount() {
        if(mSSIDs != null)
            return mSSIDs.size();
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
            firstItemView = mLayoutInflater.inflate(R.layout.list_ssid_item,null);
            firstItemViewHolder=new ViewHolder();
            firstItemViewHolder.ssid=(TextView) firstItemView.findViewById(R.id.ssid);
            firstItemViewHolder.signal=(TextView) firstItemView.findViewById(R.id.signal);
            firstItemView.setTag(firstItemViewHolder);

        } else {
            firstItemViewHolder=(ViewHolder) firstItemView.getTag();
        }
        SSIDModel model = mSSIDs.get(position);
        if (firstItemViewHolder.ssid!=null) {
            firstItemViewHolder.ssid.setText(model.SSID);
        }
        if (firstItemViewHolder.signal!=null) {
            firstItemViewHolder.signal.setText(MyApplication.getInstance().getContext().getString(R.string.image_setting_STA_SignalStrength)+"("+model.Siganl+")");
        }
        convertView=firstItemView;
        return convertView;
    }

    //第一个Item的ViewHolder
    private class ViewHolder{

        TextView ssid;
        TextView signal;
    }


}
