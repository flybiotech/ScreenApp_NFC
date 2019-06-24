package com.view;

import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.activity.R;
import com.screening.api.UserManagerContractApi;
import com.screening.api.WifiSettingContractApi;
import com.screening.manager.UserManager;
import com.screening.model.User;
import com.screening.wifi.WifiHotAdapter;

import java.util.List;

/**
 * Created by dell on 2018/4/25.
 */

public class AlertDialogUtils {


    public void showDialog1(Context context, final String name, final UserManager userManager, final UserManagerContractApi.IUserManagerModel.OnUserManagerListener listener ) {
        String[] items = {context.getString(R.string.setting_Restore_initial_password),context.getString(R.string.setting_manager_delete)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0://恢复初始密码
                        User user = new User();
                        user.setPassWord("123456");
                        user.updateAll("name = ?", name);

                        break;

                    case 1: //删除
                        List<User> users = userManager.deleteUserInfo(name);
                        listener.deleteUserData(users);
                        break;

                        default:
                            break;
                }


            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void  showDialogWIFIScan(Context mContext, final List<ScanResult> results, WifiHotAdapter adapter, final WifiSettingContractApi.IWifiSettingModel.OnWifiSearchListener listener){
        LinearLayout linearLayoutMain = new LinearLayout(mContext);//自定义布局
        linearLayoutMain.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        ListView listView = new ListView(mContext);
        listView.setFadingEdgeLength(0);
        listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setAdapter(adapter);
        linearLayoutMain.addView(listView);

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.setting_wifi_name)).setView(linearLayoutMain)//在这里把写好的这个listview的布局加载dialog中
                .setNegativeButton(mContext.getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
        dialog.show();
        listView.setFocusable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//响应listview中的item的点击事件
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                listener.setWifiName( results.get(arg2).SSID);
                dialog.cancel();
            }
        });

    }


















}
