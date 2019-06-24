package com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.activity.R;
import com.model.DevModel;
import com.model.SDVideoModel;
import com.util.Constss;

import java.util.List;

/**
 * Created by zq on 2016/6/6.
 */

public class SDVideoRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<SDVideoModel> list;

    public SDVideoRecyclerAdapter(Context context,List<SDVideoModel> list)
    {
        this.context=context;
        this.list=list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view= LayoutInflater.from(context).inflate(R.layout.grid_video_item,parent,false);
            return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (Constss.videoType == 1) {//表示 在视珍宝里的视频

            SDVideoModel model = list.get(position);
            TextView videoNameTextView = ((ViewHolder) holder).videoNameTextView;
            videoNameTextView.setText(list.get(position).getSdVideoName());
            if (model.viewed) {
                videoNameTextView.setTextColor(Color.rgb(0x1e, 0xae, 0xd6));
            } else {
                videoNameTextView.setTextColor(Color.rgb(0x53, 0x53, 0x53));
            }

            videoNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTextClickListner != null) {
                        onTextClickListner.OnTextClicked(v, list.get(position).sdVideo, Constss.videoType);
                    }
                }
            });

            final CheckBox checkBox = ((ViewHolder) holder).checkBox;
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setTag("" + position);

            checkBox.setChecked(model.checked);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SDVideoModel model = list.get(position);
                    model.checked = checkBox.isChecked();
                    if (onTextClickListner != null) {
                        onTextClickListner.OnCheckClicked(Constss.videoType);
                    }
                }
            });
        } else {
            SDVideoModel model = list.get(position);
            TextView videoNameTextView = ((ViewHolder) holder).videoNameTextView;
            videoNameTextView.setText(list.get(position).getSdVideoName());
            if (model.viewed) {
                videoNameTextView.setTextColor(Color.rgb(0x1e, 0xae, 0xd6));
            } else {
                videoNameTextView.setTextColor(Color.rgb(0x53, 0x53, 0x53));
            }

            videoNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTextClickListner != null) {
                        onTextClickListner.OnTextClicked(v, list.get(position).sdVideo, Constss.videoType);
                    }
                }
            });

            final CheckBox checkBox = ((ViewHolder) holder).checkBox;
            checkBox.setVisibility(View.GONE);
        }



    }
    @Override
    public int getItemCount() {
//        return list.size()>14?list.size()+1:list.size();
        return list.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position + 1 == getItemCount() && list.size()>14) {
//            //最后一个item设置为footerView
//            return TYPE_FOOTER;
//        } else {
//            return TYPE_ITEM;
//        }
//    }
    public void setDevModel(DevModel model){
//        if (model != null){
//            devModel = model;
//        }
    }
    public void setList(List<SDVideoModel> videos)
    {
        if (videos != null){
            list = videos;
            notifyDataSetChanged();
        }

    }

    /**
     * 更新加载更多状态
//     * @param status
     */
//    public void changeMoreStatus(int status){
//        mLoadMoreStatus=status;
//        notifyDataSetChanged();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView videoNameTextView;
        CheckBox checkBox;
        public ViewHolder(View view){
            super(view);
            videoNameTextView=(TextView)view.findViewById(R.id.tx_video_name);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        }
    }
//    public class FooterViewHolder extends RecyclerView.ViewHolder{
//
//        TextView mTextView;
//        ProgressBar mProgressBar;
//        RelativeLayout mRelativelayout;
//        public FooterViewHolder(View view)
//        {
//            super(view);
//
//            mTextView=(TextView)view.findViewById(R.id.tvLoadText);
//            mProgressBar=(ProgressBar) view.findViewById(R.id.pbLoad);
//            mRelativelayout=(RelativeLayout) view.findViewById(R.id.loadLayout);
//        }
//
//
//    }
    public void setOnTextClickListner(OnTextClickListner onCardClickListner) {
        this.onTextClickListner = onCardClickListner;
    }
    public interface OnTextClickListner {
        void OnTextClicked(View view, String videoPath, int type);
        void OnCheckClicked(int type);//如果有点击，则通知
    }

    OnTextClickListner onTextClickListner;



}
