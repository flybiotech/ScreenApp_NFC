package com.screening.impl;

import com.screening.api.UserManagerContractApi;
import com.screening.model.User;

import java.util.List;

/**
 * Created by dell on 2018/4/25.
 */

public class IUserManagerPresenterImpl implements UserManagerContractApi.IUserManagerPresenter {

    private UserManagerContractApi.IUserManagerView mView;
    private IUserManagerModelImpl mModel;

    public IUserManagerPresenterImpl(UserManagerContractApi.IUserManagerView mView) {
        this.mView = mView;
        mModel = new IUserManagerModelImpl();
    }


    @Override
    public void searchUserData() {
        mModel.searchUserData(new UserManagerContractApi.IUserManagerModel.OnUserManagerListener() {
            @Override
            public void setListData(List<User> list) {
                mView.setListData(list);
            }

            @Override
            public void deleteUserData(List<User> list) {

            }
        });

    }


    @Override
    public void deleteAfterData(String name) {


        mModel.deleteUser(mView.getContext(), name, new UserManagerContractApi.IUserManagerModel.OnUserManagerListener() {
            @Override
            public void setListData(List<User> list) {

            }

            @Override
            public void deleteUserData(List<User> list) {
                mView.setListData(list);
            }
        });

    }


}
