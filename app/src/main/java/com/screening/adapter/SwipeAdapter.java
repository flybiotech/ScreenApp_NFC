package com.screening.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activity.R;
import com.application.MyApplication;
import com.screening.model.ListMessage;
import com.util.Constss;

import java.util.List;
import java.util.zip.Inflater;

//展示受检者信息（向左滑动删除）
public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.ViewHolder> {

    private List<ListMessage> mDataList;
    private Context mContext;

    public SwipeAdapter(Context context) {
        mContext = context;
    }

    public void notifyDataSetChanged(List<ListMessage> list) {
        this.mDataList = list;
//        Log.i("page", "notifyDataSetChanged: mDataList.size=" + mDataList.size());
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu_main, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(mDataList.get(i));
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_menu_name;
        TextView tv_menu_idcard;
        TextView tv_menu_tel;
        TextView tv_menu_screen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_menu_name = itemView.findViewById(R.id.tv_menu_name);
            tv_menu_idcard = itemView.findViewById(R.id.tv_menu_idcard);
            tv_menu_tel = itemView.findViewById(R.id.tv_menu_tel);
            tv_menu_screen = itemView.findViewById(R.id.tv_menu_screen);
        }

        @SuppressLint("SetTextI18n")
        public void setData(ListMessage user) {
            tv_menu_name.setText(MyApplication.getContext().getString(R.string.deleteSinglename) + ": " + user.getName());
            tv_menu_idcard.setText(MyApplication.getContext().getString(R.string.deleteSingleidcard) + ": " + user.getIdCard());
            tv_menu_tel.setText(MyApplication.getContext().getString(R.string.deleteSingletel) + ": " + user.getPhone());
            if (user.getScreenState() >=5) {//已上传
                tv_menu_screen.setText(MyApplication.getContext().getString(R.string.deleteSinglescreen)
                        + ": " + MyApplication.getContext().getString(R.string.deleteSinglescreenFin));
                tv_menu_screen.setTextColor(ContextCompat.getColor(MyApplication.getContext(), R.color.appcolor1));
            } else {//未上传
                tv_menu_screen.setText(MyApplication.getContext().getString(R.string.deleteSinglescreen)
                        + ": " + MyApplication.getContext().getString(R.string.deleteSinglescreenUnFin));
                tv_menu_screen.setTextColor(ContextCompat.getColor(MyApplication.getContext(), R.color.blue));
            }

        }
    }
}
