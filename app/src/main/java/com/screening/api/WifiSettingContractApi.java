package com.screening.api;

import android.content.Context;

/**
 * Created by dell on 2018/4/25.
 */

public class WifiSettingContractApi {

    public interface IWifiSettingView {

        String getWifiName();

        String getWifiPassWord();

        void setWifiName(String name);

        void setWifiPassword(String password);

        Context getContext();

        int getWifiType();


    }


    public interface IWifiSettingPresenter {

        void startSearchWifi();

        void connectWifi();

    }


    public interface IWifiSettingModel {
        void startConnWifi(String wifiName, String wifiPassword, int type);

        void startSearchWifi(Context context, OnWifiSearchListener listener);

        interface OnWifiSearchListener {
            void setWifiName(String wifiName);

            void setWifiPassWord(String wifiPassWord);

        }

    }
}
