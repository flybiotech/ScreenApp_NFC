package com.screening.impl;

import android.content.Context;

import com.screening.api.StaffContractApi;
import com.screening.manager.UserManager;
import com.screening.model.User;
import com.screening.uitls.SPUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by dell on 2018/4/25.
 */

public class IDocNumModelImpl implements StaffContractApi.IStaffModel {

    private UserManager mUserManager;


    @Override
    public void next(final String name, final String passWord, final String state, final OnStaffListener staffListener) {

        if ("".equals(name)) {
            staffListener.saveFaildNameEmpty();
            return;
        }


        if (mUserManager == null) {
            mUserManager = new UserManager();
        }


        Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {
                List<User> userList = mUserManager.staffSearch(state);
                emitter.onNext(userList);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<User> users) {
                        if (users.size() == 0) {
                            mUserManager.saveUserData(name, passWord, state);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        staffListener.nextActivtiySuccess();
                    }
                });

//        Observable.create(new Observable.OnSubscribe<List<User>>() {
//
//            @Override
//            public void call(Subscriber<? super List<User>> subscriber) {
//                List<User> userList = mUserManager.staffSearch(state);
//                subscriber.onNext(userList);
//                subscriber.onCompleted();
//
//            }
//        }).subscribeOn(Schedulers.newThread())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Subscriber<List<User>>() {
//            @Override
//            public void onCompleted() {
//                staffListener.nextActivtiySuccess();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(List<User> users) {
//                if (users.size() == 0) {
//                    mUserManager.saveUserData(name,passWord,state);
//                }
//
//            }
//        });


    }

    @Override
    public void skip(OnStaffListener staffListener) {
        staffListener.skipActivtiySuccess();

    }

    @Override
    public void saveNameAndPassWord(Context context, String name, String passWord, boolean checkBox) {
        SPUtils.put(context, "userName_staff", name);
//        SPUtils.put(context, "userPass_staff",passWord);
        SPUtils.put(context, "checkBox_staff", checkBox);
    }


}
