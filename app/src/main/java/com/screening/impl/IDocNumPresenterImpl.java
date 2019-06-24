package com.screening.impl;

import com.screening.api.StaffContractApi;

/**
 * Created by dell on 2018/4/25.
 */

public class IDocNumPresenterImpl implements StaffContractApi.IStaffPresenter, StaffContractApi.IStaffModel.OnStaffListener {

    private StaffContractApi.IStaffView mView;
    private StaffContractApi.IStaffModel mModel;

    public IDocNumPresenterImpl(StaffContractApi.IStaffView mView) {
        this.mView = mView;
        mModel = new IDocNumModelImpl();
    }


    @Override
    public void next() {

        mModel.next(mView.getUserName(), mView.getPassword(), mView.getState(), this);

    }

    @Override
    public void skip() {
        mModel.skip(this);

    }


    @Override
    public void nextActivtiySuccess() {
        mModel.saveNameAndPassWord(mView.getContext(), mView.getUserName(), mView.getPassword(), mView.isCheckBox());
        mView.toNextActivity();

    }

    @Override
    public void skipActivtiySuccess() {
        mModel.saveNameAndPassWord(mView.getContext(), mView.getUserName(), mView.getPassword(), mView.isCheckBox());
        mView.toSkipActivity();

    }


    @Override
    public void saveFaildNameEmpty() {
        mView.userNameEmpty();

    }

    @Override
    public void saveFaildPassEmpty() {
        mView.userPasswordEmpty();

    }

}
