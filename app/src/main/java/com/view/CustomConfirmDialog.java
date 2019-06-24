package com.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.activity.R;


/**
 * Created by dell on 2017/9/27.
 */

public class CustomConfirmDialog {
    private Button  btn_posi,btn_nega;
//    private TextView tvTitle;
    private AlertDialog.Builder builder;
    private Context context;
    private AlertDialog alertDialog;

    /**
     * 初始化自定义确认框
     *
     * @param context
     * @param
     * @param确认按钮监听
     */
    public CustomConfirmDialog(Context context) {
        this.context = context;
    }

    public void show(String title, String btnPosi,String btnNega,View.OnClickListener positiveListener, View.OnClickListener negativeListener) {
        builder = new AlertDialog.Builder(context);
        View customView=getCustomView(title, btnPosi, btnNega, positiveListener, negativeListener);
        builder.setView(customView);
        alertDialog = builder.create();
        alertDialog.setTitle(title);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void show(String title,View.OnClickListener positiveListener, View.OnClickListener negativeListener) {
        String btnPosi = context.getResources().getString(R.string.image_manage_delete_ok);
        String btnNega = context.getResources().getString(R.string.image_manage_delete_no);
        builder = new AlertDialog.Builder(context);

        View customView=getCustomView(title, btnPosi, btnNega, positiveListener, negativeListener);

        builder.setView(customView);

        alertDialog = builder.create();
        alertDialog.setTitle(title);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void dimessDialog() {
//        Item.btnOptionEnable = false;
        if (alertDialog != null) {
            alertDialog.cancel();
        }
    }

    private View getCustomView(String title, String btnPosi,String btnNega,View.OnClickListener positiveListener, View.OnClickListener negativeListener) {
        View mView = LayoutInflater.from(context).inflate(R.layout.dialog_custom_layout, null);
//        tvTitle = (TextView) mView.findViewById(R.id.tv_constumdialog);//title
//        tvTitle.setText(title);

        btn_posi = (Button) mView.findViewById(R.id.btn_delete);//删除
        btn_posi.setText(btnPosi);
        btn_posi.setOnClickListener(positiveListener);

        btn_nega = (Button) mView.findViewById(R.id.btn_notdelete);//不删除
        btn_nega.setText(btnNega);
        btn_nega.setOnClickListener(negativeListener);

//        btn_yuantu = (Button) mView.findViewById(R.id.btn_yuantu);//原图
//        btn_yuantu.setOnClickListener(regativeListener);

        return mView;
    }

}
