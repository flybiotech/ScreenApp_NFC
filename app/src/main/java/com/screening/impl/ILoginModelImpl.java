package com.screening.impl;

import android.content.Context;
import android.util.Log;

import com.screening.api.LoginContractApi;
import com.screening.manager.UserManager;
import com.screening.model.User;
import com.screening.uitls.SPUtils;
import com.util.Constss;

import java.util.List;


/**
 * Created by dell on 2018/4/24.
 */

public class ILoginModelImpl implements LoginContractApi.ILoginModel {

    private UserManager userManager;

    @Override
    public void login(final String name, final String passWord, final OnLoginListener loginListener) {

        //姓名为空
        if ("".equals(name)) {
            loginListener.loginFaild(Constss.LOGIN_FAILED_NAMEERROR);
            return;
        }

        //密码为空
        if ("".equals(passWord)) {
            loginListener.loginFaild(Constss.LOGIN_FAILED_PASSERROR);
            return;
        }


        //登陆成功
        if (Constss.LOGIN_DELETEALL_NAME.equals(name) && Constss.LOGIN_DELETEALL_PASS.equals(passWord)) {
            loginListener.loginSuccess(Constss.LOGIN_DELETEALL_TYPE);//恢复出厂设置

        } else if (("admin".equals(name) || "Admin".equals(name)) && Constss.LOGIN_DELETESINGLE_PASS.equals(passWord)) {
            loginListener.loginSuccess(Constss.LOGIN_DELETESINGLE_TYPE);
        } else if ("fly".equals(name) && "123456".equals(passWord)) {
            loginListener.loginSuccess(Constss.LOGIN_USER_TYPE);//普通用户
        } else {
            //登陆失败
            loginListener.loginFaild(Constss.LOGIN_FAILED_ERROR);
        }


    }

    @Override
    public void saveNameAndPassWord(Context context, String name, String passWord, boolean checkBox) {
        if (checkBox) {
            SPUtils.put(context, Constss.LOGIN_SP_NAME, name);
            SPUtils.put(context, Constss.LOGIN_SP_PASS, passWord);
        } else {
            SPUtils.remove(context, Constss.LOGIN_SP_NAME);
            SPUtils.remove(context, Constss.LOGIN_SP_PASS);
        }
        SPUtils.put(context, Constss.LOGIN_SP_CHECKBOX, checkBox);
    }


    public int add(int a, int b) {
        return a + b;
    }


}
