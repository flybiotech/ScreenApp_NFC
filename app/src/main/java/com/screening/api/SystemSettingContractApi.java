package com.screening.api;

import android.app.Activity;
import android.content.Context;

import java.util.List;

/**
 * Created by dell on 2018/4/25.
 */

public class SystemSettingContractApi {

    public interface ISettingView {

        void toActivity(Activity act);

        void setListData(List<String> list);

        Context getContext();

    }


    public interface ISettingPresenter {

        void selectItem(int position);

        void listData();


    }


    public interface ISettingModel {

        void selectItemActivtiy(int position, OnSettingSelectListener listener);

        void listData(Context context, OnSettingSelectListener listener);

        interface OnSettingSelectListener {

            void selectActivity(Activity act);

            void setListData(List<String> list);

        }
    }

}
