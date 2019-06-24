package com.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.R;

import org.w3c.dom.Text;

/**
 * Created by dell on 2018/4/24.
 */

public class ToastUtils {

    private static Toast mToast;

    public static void showToast(Context mContext, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);

        }
        mToast.show();
    }


    public static void showToastCustom(Context mContext, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
//        else {
//            mToast.setText(msg);
//        }

        View view = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.toast_custom, null);
        ((TextView)view.findViewById(R.id.tv_toast_custom)).setText(msg);
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }

}
