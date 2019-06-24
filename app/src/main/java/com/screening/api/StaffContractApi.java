package com.screening.api;

import android.content.Context;

import com.screening.model.User;

/**
 * Created by dell on 2018/4/25.
 */

public class StaffContractApi {


    public interface IStaffView {
        String getUserName();

        String getPassword();

        String getState();

        //保存员工信息，进入下一个activity
        void toNextActivity();

        //直接跳过，进入下一个activity
        void toSkipActivity();

        //账户不能为空
        void userNameEmpty();

        //密码不能为空
        void userPasswordEmpty();


        //设置 保存的账号和密码
//        void setNameAndPassWord();

        boolean isCheckBox();

        Context getContext();


        void showLoading();

        void hideLoading();

    }

    public interface IStaffPresenter {
        void next();

        void skip();


    }


    public interface IStaffModel {

        interface OnStaffListener {
            void nextActivtiySuccess();

            void skipActivtiySuccess();

            void saveFaildNameEmpty();

            void saveFaildPassEmpty();


        }

        void next(String name, String passWord, String state, OnStaffListener staffListener);

        void skip(OnStaffListener staffListener);

        void saveNameAndPassWord(Context context, String name, String passWord, boolean checkBox);

    }


}
