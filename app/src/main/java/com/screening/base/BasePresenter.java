package com.screening.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by dell on 2018/4/24.
 */

public abstract class BasePresenter<T> {

    //View 接口类型的弱引用
    protected Reference<T> mViewRef;

    //建立连接
    public void attachView(T view) {

        mViewRef = new WeakReference<T>(view);
    }

    //获取view
    protected T getView() {
        return mViewRef.get();
    }

    //判断是否与view 建立了关联
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    //解除关联
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }


}
