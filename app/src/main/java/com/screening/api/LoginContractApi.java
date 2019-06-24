package com.screening.api;

import android.app.Activity;
import android.content.Context;

import com.screening.model.User;

/**
 * Created by dell on 2018/4/24.
 */

public class LoginContractApi {


    public interface ILoginView {

        String getUserName();

        String getPassword();

        void loginSuccess(String type);

        //登陆失败
        void loginFailed(String loginFailedMsg);

        //登录过程中，出现的各种信息
        void showDialogLoading(String msg);

        void dismissDialog();

        void setNameAndPassWord();

        boolean isCheckBox();

        Context getContext();

    }


    public interface ILoginPresenter {

        void login();

        void clear();


    }


    public interface ILoginModel {

        interface OnLoginListener {

            void loginSuccess(String loginType);

            void loginFaild(String failedMsg);

        }

        void login(String name, String passWord, OnLoginListener loginListener);

        void saveNameAndPassWord(Context context, String name, String passWord, boolean checkBox);

    }


}
