package com.screening.impl;

import android.content.Context;

import com.screening.api.UserManagerContractApi;
import com.screening.manager.UserManager;
import com.screening.model.User;
import com.view.AlertDialogUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by dell on 2018/4/25.
 */

public class IUserManagerModelImpl implements UserManagerContractApi.IUserManagerModel {

    private UserManager mUserManager;

    public IUserManagerModelImpl() {
        if (mUserManager == null) {
            mUserManager = new UserManager();
        }
    }

    @Override
    public void searchUserData(final OnUserManagerListener listener) {

        Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {
                List<User> userList = mUserManager.allUserInfo();
                emitter.onNext(userList);
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        users.add(new User.Builder().build());
                        listener.setListData(users);
                    }
                });


//        Observable.create(new Observable.OnSubscribe<List<User>>() {
//            @Override
//            public void call(Subscriber<? super List<User>> subscriber) {
//
//                List<User> userList = mUserManager.allUserInfo();
//                subscriber.onNext(userList);
//
//
//            }
//        }).subscribeOn(Schedulers.newThread())
//         .observeOn(AndroidSchedulers.mainThread())
//         .subscribe(new Subscriber<List<User>>() {
//             @Override
//             public void onCompleted() {
//
//             }
//
//             @Override
//             public void onError(Throwable e) {
//
//             }
//
//             @Override
//             public void onNext(List<User> users) {
//                 users.add(new User.Builder().build()  );
//                 listener.setListData(users);
//
//             }
//         });
    }

    @Override
    public void deleteUser(Context context, String name, OnUserManagerListener listener) {
        AlertDialogUtils dialogUtils = new AlertDialogUtils();
        dialogUtils.showDialog1(context, name, mUserManager, listener);

    }


}
