package com.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activity.R;
import com.application.MyApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.model.DevModel;
import com.model.SDImageModel;
import com.util.SouthUtil;

import java.util.List;

/**
 * Created by zq on 2016/6/6.
 */

public class SDImageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final static String tag = "SDImageRecyclerAdapter";
    private Context context;
    private Activity mActivity;
    private List<SDImageModel> list;
    //    private DevModel devModel;
//    private static final int TYPE_ITEM = 0;
//    private static final int TYPE_FOOTER = 1;
//    //上拉加载更多
//    public static final int PULLUP_LOAD_MORE = 0;
//    //正在加载中
//    public static final int LOADING_MORE     = 1;
//    //没有加载更多 隐藏
//    public static final int NO_LOAD_MORE     = 2;

    //    //上拉加载更多状态-默认为0
//    private int mLoadMoreStatus = 0;
    public SDImageRecyclerAdapter(Context context, List<SDImageModel> list) {
        this.context = context;
        this.list = list;
        mActivity = (Activity) context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType==TYPE_ITEM)
//        {   //显示图片
        View view = LayoutInflater.from(context).inflate(R.layout.grid_image_item, parent, false);

        return new ViewHolder(view);
//        }else if(viewType==TYPE_FOOTER){
//            //显示正在加载图片的框框
//            View view= LayoutInflater.from(context).inflate(R.layout.grid_loadmore_progess,parent,false);
//            return new FooterViewHolder(view);
//        }else {
//            return null;
//        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//        if(holder instanceof ViewHolder)
//        {

        //((ViewHolder) holder).imageView.setTag(list.get(position).getSdImageUrl(devModel));
        //Log.e(tag,"reload data position is "+position);


        String url = list.get(position).sdImage;
//            Picasso.with(context)
//                    .load(url)
//                    .resize(250, 150)
//                    .centerCrop()
//                    .into(((ViewHolder) holder).imageView);

        Glide.with(context)//图片加载框架
                .load(url)//图片的路径
                .skipMemoryCache(true)
                //禁止使用磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(200,200)
//                .crossFade()
                .into(((ViewHolder) holder).imageView);
        final CheckBox checkBox = ((ViewHolder) holder).checkBox;
        checkBox.setTag("" + position);
        SDImageModel model = list.get(position);
        checkBox.setChecked(model.checked);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SDImageModel model = list.get(position);
                model.checked = checkBox.isChecked();
                if (onImageClickListner != null) {
                    onImageClickListner.OnCheckClicked();
                }
            }
        });
//            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    SDImageModel model = list.get(position);
//                    model.checked = b;
////                    Log.e(tag,"OnCheckedChangeListener index is "+position +",check is "+b);
//                }
//            });
        ((ViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onImageClickListner != null) {
                    onImageClickListner.OnImageClicked(v, position);
                }
            }
        });

        ((ViewHolder) holder).tvImageType.setText(getPathSplitName(list.get(position).sdImage));

        // ((ViewHolder) holder).mTextView.setText(list.get(position));
//        }else if(holder instanceof FooterViewHolder)
//        {
//            switch (mLoadMoreStatus){
//                case PULLUP_LOAD_MORE:
//                    ((FooterViewHolder) holder).mRelativelayout.setVisibility(View.GONE);
//                    break;
//                case LOADING_MORE:
//                    ((FooterViewHolder) holder).mRelativelayout.setVisibility(View.VISIBLE);
//                    ((FooterViewHolder) holder).mTextView.setText("加载中...");
//                    break;
//                case NO_LOAD_MORE:
//                    ((FooterViewHolder) holder).mRelativelayout.setVisibility(View.GONE);
//                    break;
//            }
//        }
    }

    @Override
    public int getItemCount() {
        //Log.e(tag,"reload data ,cnt is "+ list.size());
//        return list.size()>9?list.size()+1:list.size();
        return list.size();
    }


    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.get(context).clearMemory();
    }

    //    @Override
//    public int getItemViewType(int position) {
//        if (position + 1 == getItemCount() && list.size()>9) {
//            //最后一个item设置为footerView
//            return TYPE_FOOTER;
//        } else {
//            return TYPE_ITEM;
//        }
//
//    }
//    public void setDevModel(DevModel model) {
//        if (model != null) {
////            devModel = model;
//        }
//    }

    public void setList(List<SDImageModel> images) {
        if (images != null) {
            list = images;
            notifyDataSetChanged();
        }

    }

    /**
     * 更新加载更多状态
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
//        mLoadMoreStatus=status;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        //        ImageViewRotation imageView;
        ImageView imageView;
        CheckBox checkBox;
        public TextView tvImageType;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            tvImageType = (TextView) view.findViewById(R.id.tv_imageType);
            WindowManager manage = mActivity.getWindowManager();
            Display display = manage.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int margin = SouthUtil.convertDpToPixel(2, context);

            RelativeLayout.LayoutParams pam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, width / 5 - 2 * margin);
            pam.bottomMargin = margin;
            pam.leftMargin = margin;
            pam.rightMargin = margin;
            pam.topMargin = margin;
            imageView.setLayoutParams(pam);
        }
    }
//    public class FooterViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
//
//        //        CheckBox checkBox;
//        public FooterViewHolder(View view) {
//            super(view);
//            imageView = (ImageView) view.findViewById(R.id.image);
////            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
//
//            WindowManager manage = mActivity.getWindowManager();
//            Display display = manage.getDefaultDisplay();
//            Point size = new Point();
//            display.getSize(size);
//            int width = size.x;
//            int margin = SouthUtil.convertDpToPixel(2, context);
//
//            RelativeLayout.LayoutParams pam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, width / 5 - 2 * margin);
//            pam.bottomMargin = margin;
//            pam.leftMargin = margin;
//            pam.rightMargin = margin;
//            pam.topMargin = margin;
//            imageView.setLayoutParams(pam);
//
//        }
//    }


    private String getPathSplitName(String filePath) {
//        Logger.e(" 文件名称v：filePath "+filePath);
        ///storage/emulated/0/ScreenApp/DCIM/admin111111/醋酸白/醋酸白11.jpg
        String imageCatagory = null;
        try {
            String[] split = filePath.split("[/]");
//split[2]emulatedsplit[3]0split[4]ScreenAppsplit[5]DCIMsplit[6]admin111111split[7]醋酸白split[8]醋酸白11.jpg split.length = 9
            imageCatagory = split[split.length - 1];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageCatagory;
    }

    private String changeInfo(String filePath) {

        String imageCatagory = MyApplication.getContext().getString(R.string.image_artword);
        switch (filePath) {
            case "yuantu":
                imageCatagory = MyApplication.getContext().getString(R.string.image_artword);
                break;
            case "cusuanbai":
                imageCatagory = MyApplication.getContext().getString(R.string.image_acetic_acid_white);
                break;
            case "dianyou":
                imageCatagory = MyApplication.getContext().getString(R.string.image_Lipiodol);
                break;
            default:
                break;

        }
        return imageCatagory;
    }

    public void setOnImageClickListner(OnImageClickListner onCardClickListner) {
        this.onImageClickListner = onCardClickListner;
    }

    public interface OnImageClickListner {
        void OnImageClicked(View view, int position);

        void OnCheckClicked();//如果有点击，则通知
    }

    OnImageClickListner onImageClickListner;


}
