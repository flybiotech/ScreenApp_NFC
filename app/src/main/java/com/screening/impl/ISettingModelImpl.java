package com.screening.impl;

import android.app.Activity;
import android.content.Context;

import com.activity.R;
import com.screening.activity.AboutUsActivity;
import com.screening.activity.AdminSetActivity;
import com.screening.activity.BluetoothActivity;
import com.screening.activity.CommonProblemActivity;
import com.screening.activity.BarcodeSettingActivity;
import com.screening.activity.LoginActivity;
import com.screening.activity.RecoverActivity;
import com.screening.activity.DocNumActivity;
import com.screening.activity.UpdateActivity;
import com.screening.activity.WifiSettingActivity;
import com.screening.api.SystemSettingContractApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2018/4/25.
 */

public class ISettingModelImpl implements SystemSettingContractApi.ISettingModel {


    private List<String> mListData = new ArrayList<String>();

    @Override
    public void selectItemActivtiy(int position, OnSettingSelectListener listener) {
        Activity act = null;

        switch (position) {
            case 0:
                act = new LoginActivity();

                break;

//            case 1:
//                act = new UserManagerActivity();
//
//                break;

            case 1:
                act = new DocNumActivity();

                break;

            case 2:
                act = new WifiSettingActivity();

                break;
            case 3:
                act = new BluetoothActivity();
                break;
//            case 5:
//                act=new FTPSettingActivity();
//                break;
//            case 6:
//
//                act = new BackupActivity();
//
//                break;
            case 4:
                act = new BarcodeSettingActivity();
                break;
            case 5:
                act = new RecoverActivity();
                break;
            case 6:
                act = new AdminSetActivity();
                break;
            case 7:
                act = new AboutUsActivity();

                break;

            case 8:
                act = new CommonProblemActivity();

                break;

            case 9:
                act = new UpdateActivity();
                break;
            case 10:

                break;
            default:
                break;
        }
        listener.selectActivity(act);

    }


    @Override
    public void listData(Context context, OnSettingSelectListener listener) {
        mListData.add(context.getString(R.string.setting_User_switching)); //用户切换
//        mListData.add(context.getString(R.string.setting_area_msg));//地区信息
        mListData.add(context.getString(R.string.doctorid)); //医生编号
        mListData.add(context.getString(R.string.setting_wifi));// WIFI设置
        mListData.add(context.getString(R.string.setting_blue));//蓝牙绑定
        mListData.add(context.getString(R.string.setting_scan_setting));//扫码设置
//        mListData.add(context.getString(R.string.setting_advanced_setting));//高级设置
//        mListData.add(context.getString(R.string.ftpsetting));

//        mListData.add(context.getString(R.string.setting_backup));
        mListData.add(context.getString(R.string.setting_recovery));

        mListData.add(context.getString(R.string.commitData));
        mListData.add(context.getString(R.string.setting_about_us)); //关于我们
        mListData.add(context.getString(R.string.setting_error_query)); //常见问题
        mListData.add(context.getString(R.string.setting_Version_update));//版本更新
        mListData.add("");
        listener.setListData(mListData);
    }

}
