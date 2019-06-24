package com.screening.handler;

import android.os.Handler;
import android.os.Message;

import com.screening.activity.ShowActivity;

import java.lang.ref.WeakReference;

/**
 * Created by zhangbin on 2018/6/25.
 */

public class MyHandler extends Handler {
    //声明一个弱应用对象
    WeakReference<ShowActivity> weakReference;

    //    WeakReference<ShowActivity>showActivityWeakReference;
    public MyHandler(ShowActivity showActivity) {
        //在构造器传入activity,创建弱引用对象
        weakReference = new WeakReference<ShowActivity>(showActivity);
    }

    public void handleMessage(Message message) {
        //判空处理
        if (weakReference != null && weakReference.get() != null) {

        }
    }
}
