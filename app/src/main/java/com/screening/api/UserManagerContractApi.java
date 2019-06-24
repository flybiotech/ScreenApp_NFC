package com.screening.api;

import android.content.Context;

import com.screening.model.User;

import java.util.List;

/**
 * Created by dell on 2018/4/25.
 */

public class UserManagerContractApi {

    public interface IUserManagerView {

        void setListData(List<User> list);

        Context getContext();

    }

    public interface IUserManagerPresenter {
        void searchUserData();

        void deleteAfterData(String name);


    }


    public interface IUserManagerModel {

        void searchUserData(OnUserManagerListener listener);

        void deleteUser(Context context, String name, OnUserManagerListener listener);

        interface OnUserManagerListener {
            void setListData(List<User> list);

            void deleteUserData(List<User> list);

        }


    }


}
