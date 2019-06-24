package com.activity;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Manager.DataManager;
import com.model.DevModel;

import com.util.Constss;
import com.util.SouthUtil;

import java.util.List;

/**
 * Created by dell on 2017/8/21.
 */

public class ListDevAdapter extends BaseAdapter{

    public  final  static  String tag =  "ListDevAdapter";
    private LayoutInflater mInflater;
    public List<DevModel> devList = null;
    public Context mContext;
    //    public FragIntentListDev fragIntentListDev=null;
    DataManager manager = DataManager.getInstance();

    public ListDevAdapter(Context con) {
        mContext = con;
        mInflater = LayoutInflater.from(mContext);
    }
    public void setDevArray( List<DevModel> array) {
        devList = array;
    }


    @Override
    public int getCount() {
        if (devList == null){
            return 0;
        }
        else{
            return devList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertViews, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_deva, null);
        TextView dev_name = (TextView) v.findViewById(R.id.dev_name);
        TextView dev_info = (TextView) v.findViewById(R.id.dev_info);
        ImageView dev_image = (ImageView) v.findViewById(R.id.dev_image);
        DevModel model = devList.get(0);
        dev_name.setText(model.name);
//        Constss.devModel = model;
        if (model.online == DevModel.EnumOnlineState.Online){
//            Constss.isItem = true;
//            dev_info.setText(Html.fromHtml(model.sn+"[<font color=blue>Lan</font>]"+model.ip));
//            fragIntentListDev.intentToListDiActivity();
//            if (Constss.isIntent&& Constss.isIntentFrag) {
//                mButtonClickListner.OnlineSuccess();
////                Intent intent = new Intent(mContext, LiveVidActivity.class);
////                Bundle bundle = new Bundle();
////                bundle.putInt("index",0);
////                intent.putExtras(bundle);
////                Constss.isIntent = false;
////                mContext.startActivity(intent);
//            }
        }
        else if (model.online == DevModel.EnumOnlineState.Offline){
//            Constss.isItem = false;
//            dev_info.setText(Html.fromHtml(model.sn+"[<font color=red>"+R.string.image_host_state+"</font>]"+model.ip));
        }
        else{
//            Constss.isItem = false;
//            dev_info.setText(Html.fromHtml(model.sn+"[<font color=#4876FF>正在获取状态</font>]"+model.ip));
//            dev_info.setText(Html.fromHtml(model.sn+"[<font color=#4876FF>Lan</font>]"+model.ip));
        }
        v.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mButtonClickListner != null){
//                    mButtonClickListner.OnDeleteClicked(view, position);
//                }
//                closeItem(position);
            }
        });
        v.findViewById(R.id.hand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mButtonClickListner != null)
//                    mButtonClickListner.OnChangeNameClicked(view, position);
//                closeItem(position);
            }
        });



        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(SouthUtil.convertDpToPixel(50,mContext),SouthUtil.convertDpToPixel(50,mContext));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams.setMargins(SouthUtil.convertDpToPixel(10,mContext),SouthUtil.convertDpToPixel(10,mContext),0,SouthUtil.convertDpToPixel(10,mContext));
        //layoutParams.addRule(RelativeLayout., RelativeLayout.TRUE);
        dev_image.setLayoutParams(layoutParams);
        dev_image.setImageResource(R.drawable.logo);

        return v;
    }


//    public void setOnButtonClickListner(OnButtonClickListner buttonClickListner) {
//        this.mButtonClickListner = buttonClickListner;
//    }


//    public interface OnButtonClickListner {
//        void OnDeleteClicked(View view, int position);
//        void OnChangeNameClicked(View view, int position);
//
//        void OnlineSuccess();
//
//    }
//    OnButtonClickListner mButtonClickListner = null;



}
