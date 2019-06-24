package com.view;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.activity.R;
import com.util.SouthUtil;

import java.io.File;


public class LoadingDialog {
    private Dialog loadingDialog;
    private TextView textView;
    private MyTitleView mt_tv_show;
    public LoadingDialog instance = null;

    public LoadingDialog(Context context) {
        loadingDialog = new Dialog(context, R.style.myDialogTheme);
        loadingDialog.setContentView(R.layout.view_loadingdlg);
        loadingDialog.setCanceledOnTouchOutside(false);
        //用于平板的返回键是否起作用
//        loadingDialog.setCancelable(false);
        textView = (TextView) loadingDialog.findViewById(R.id.loading_message);
        textView.setVisibility(View.VISIBLE);
        mt_tv_show = loadingDialog.findViewById(R.id.mt_tv_show);
        mt_tv_show.setVisibility(View.GONE);
    }

    public LoadingDialog(Context context, String myTextview) {
        loadingDialog = new Dialog(context, R.style.myDialogTheme);
        loadingDialog.setContentView(R.layout.toast_view);
        loadingDialog.setCanceledOnTouchOutside(false);
        mt_tv_show = loadingDialog.findViewById(R.id.toast_viewss);
        mt_tv_show.setTextColor("#F8F8FF");

    }

    public LoadingDialog(Context context, boolean istextview) {
        loadingDialog = new Dialog(context, R.style.myDialogTheme);
        loadingDialog.setContentView(R.layout.view_loadingdlg);
//        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCanceledOnTouchOutside(istextview);
        //用于平板的返回键是否起作用
        loadingDialog.setCancelable(istextview);
        textView = (TextView) loadingDialog.findViewById(R.id.loading_message);
//        Item.getOneItem(context).setTv_progress1(textView);
        textView.setTextSize(SouthUtil.dp2px(6));
    }

    public void setMyMessage(String message) {
        mt_tv_show.setText(message);
    }

    /**
     * @param message
     */
    public void setMessage(String message) {
        textView.setText(message);
    }

    public LoadingDialog() {
        super();
    }

    /**
     *
     */
    public void dismiss() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }

    }

    /**
     *
     */
    public void dialogShow() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }


    }

    public boolean isShow() {
        return loadingDialog.isShowing();
    }


}

