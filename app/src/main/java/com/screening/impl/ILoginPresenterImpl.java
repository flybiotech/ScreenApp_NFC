package com.screening.impl;

import android.app.Activity;

import com.activity.R;
import com.screening.api.LoginContractApi;
import com.screening.base.BasePresenter;
import com.screening.model.User;

/**
 * Created by dell on 2018/4/24.
 */

public class ILoginPresenterImpl implements LoginContractApi.ILoginPresenter {

    private LoginContractApi.ILoginModel model;
    private LoginContractApi.ILoginView mView;
    private Activity mActivity;

    public ILoginPresenterImpl(LoginContractApi.ILoginView view) {
//        attachView(view);
        mView = view;
        model = new ILoginModelImpl();
    }


    @Override
    public void login() {
        if (getActivity() == null)
            return;
        mView.showDialogLoading(getActivity().getString(R.string.login_msg));//开启进度条
        model.login(mView.getUserName(), mView.getPassword(), new LoginContractApi.ILoginModel.OnLoginListener() {

            @Override
            public void loginSuccess(String loginType) {
                boolean checkBox = mView.isCheckBox();
                model.saveNameAndPassWord(mView.getContext(), mView.getUserName(), mView.getPassword(), checkBox);
                mView.dismissDialog();
                mView.loginSuccess(loginType);
            }

            @Override//登陆失败
            public void loginFaild(String failedMsg) {
                mView.dismissDialog();
                mView.loginFailed(failedMsg);

            }

        });

    }


    //清除登陆框的数据
    @Override
    public void clear() {


    }

    private Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity act) {
        this.mActivity = act;
    }


}
