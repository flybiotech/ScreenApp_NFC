package com.screening.impl;

import android.app.Activity;
import android.content.Context;

import com.screening.api.StaffContractApi;
import com.screening.api.SystemSettingContractApi;

import java.util.List;

/**
 * Created by dell on 2018/4/25.
 */

public class ISettingPresenterImpl implements SystemSettingContractApi.ISettingPresenter {

    private SystemSettingContractApi.ISettingView mView;
    private SystemSettingContractApi.ISettingModel mModel;

    public ISettingPresenterImpl(SystemSettingContractApi.ISettingView mView) {
        this.mView = mView;
        mModel = new ISettingModelImpl();
    }


    @Override
    public void selectItem(int position) {
        mModel.selectItemActivtiy(position, new SystemSettingContractApi.ISettingModel.OnSettingSelectListener() {
            @Override
            public void selectActivity(Activity act) {
                mView.toActivity(act);
            }

            @Override
            public void setListData(List<String> list) {

            }
        });
    }

    @Override
    public void listData() {
        mModel.listData(mView.getContext(), new SystemSettingContractApi.ISettingModel.OnSettingSelectListener() {
            @Override
            public void selectActivity(Activity act) {

            }

            @Override
            public void setListData(List<String> list) {
                mView.setListData(list);
            }
        });

    }


}
