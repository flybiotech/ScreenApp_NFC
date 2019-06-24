package com.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.model.DevModel;
import com.model.RetModel;
import com.model.WIFIStateModel;
import com.network.Network;

import com.util.Constss;
import com.util.SouthUtil;
import com.view.LoadingDialog;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by gyl1 on 4/1/17.
 */

public class ConfigApActivity extends BaseActivity implements View.OnClickListener {
    int index; //
    DevModel model;
    WIFIStateModel mWifiStateModel;
    TextInputEditText ed_wifi_name;
    TextInputEditText ed_wifi_pwd;
    TextInputLayout layout_wifi_pwd;
    Button btn_ap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//禁止屏幕休眠
        setContentView(R.layout.activity_configap);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            mWifiStateModel = bundle.getParcelable("wifi");
            index = bundle.getInt("index");
            model=bundle.getParcelable("dev");
        }



        btn_ap = (Button) findViewById(R.id.btn_ap);
        ed_wifi_name = (TextInputEditText) findViewById(R.id.ed_wifi_name);
        ed_wifi_pwd = (TextInputEditText) findViewById(R.id.ed_wifi_pwd);
        layout_wifi_pwd = (TextInputLayout) findViewById(R.id.layout_wifi_pwd);
        ed_wifi_pwd.setText(mWifiStateModel.wifi_Password_AP);
        ed_wifi_name.setEnabled(false);
        ed_wifi_name.setText(mWifiStateModel.wifi_SSID_AP);

        btn_ap.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        final String wifi_ssid = ed_wifi_name.getText().toString();
        final String wifi_pwd = ed_wifi_pwd.getText().toString();
        if (wifi_ssid.length() == 0 && wifi_pwd.length() == 0) {
            String msg = getString(R.string.image_setting_AP_dialog);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(msg)
                    .setTitle(msg)
                    .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (lod == null) {
                                lod = new LoadingDialog(ConfigApActivity.this);
                            }
                            lod.dialogShow();
                            Observable subscription = Network.getCommandApi(model)
                                    .STA2AP_keepValue(model.usr, model.pwd, 38, 1, 1, SouthUtil.getRandom())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());
                            subscription.subscribe(observer);

                        }
                    })
                    .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (wifi_ssid.length() == 0) {
            SouthUtil.showToast(context, getString(R.string.image_setting_AP_name));
        } else if (wifi_pwd.length() == 0) {
            SouthUtil.showToast(context, getString(R.string.image_setting_AP_password));
        } else {
            String msg = getString(R.string.image_setting_AP_message1) + "\r\n" + getString(R.string.image_setting_AP_message2) + wifi_ssid + "\r\n" + getString(R.string.image_setting_AP_message3) + wifi_pwd;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(msg)
                    .setTitle(msg)
                    .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //
                            if (lod == null) {
                                lod = new LoadingDialog(ConfigApActivity.this);
                            }
                            lod.dialogShow();
                            Observable subscription = Network.getCommandApi(model)
                                    .STA2AP_changeValue(model.usr, model.pwd, 38, 1, 1, wifi_ssid, wifi_pwd, SouthUtil.getRandom())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());
                            subscription.subscribe(observer);
                        }
                    })
                    .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    void reboot() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定重启？")
                .setTitle("重启后生效")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //
                        if (lod == null) {
                            lod = new LoadingDialog(ConfigApActivity.this);
                        }
                        lod.dialogShow();
                        Observable subscription = Network.getCommandApi(model)
                                .reboot(model.usr, model.pwd, 18, SouthUtil.getRandom())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                        subscription.subscribe(observer_reboot);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    Observer<RetModel> observer_reboot = new Observer<RetModel>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(RetModel retModel) {
            lod.dismiss();
            if (retModel.ret == 1) {
                SouthUtil.showToast(ConfigApActivity.this, "设备已重启");
            } else {
                SouthUtil.showToast(ConfigApActivity.this, "重启失败");
            }
        }

        @Override
        public void onError(Throwable e) {
            lod.dismiss();
        }

        @Override
        public void onComplete() {
            lod.dismiss();
        }
    };

//    Observer<RetModel> observer_reboot = new Observer<RetModel>() {
//        @Override
//        public void onCompleted() {
//            lod.dismiss();
//        }
//        @Override
//        public void onError(Throwable e) {
//            lod.dismiss();
//            Log.e(tag, e.toString());
//        }
//
//        @Override
//        public void onNext(RetModel m) {
//            lod.dismiss();
//            if (m.ret == 1){
//                SouthUtil.showToast(ConfigApActivity.this,"设备已重启");
//            }
//            else{
//                SouthUtil.showToast(ConfigApActivity.this,"重启失败");
//            }
//        }
//    };


    Observer<RetModel> observer = new Observer<RetModel>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(RetModel retModel) {
            lod.dismiss();
            if (retModel.ret == 2) {
                SouthUtil.showToast(ConfigApActivity.this, getString(R.string.image_setting_success));
                reboot();
            } else if (retModel.ret == 1) {
                SouthUtil.showToast(ConfigApActivity.this, getString(R.string.image_setting_success));
            } else {
                SouthUtil.showToast(ConfigApActivity.this, getString(R.string.image_setting_faild));
            }
        }

        @Override
        public void onError(Throwable e) {
            lod.dismiss();
            Log.e(tag, e.toString());
        }

        @Override
        public void onComplete() {
            lod.dismiss();
        }
    };

    //    Observer<RetModel> observer = new Observer<RetModel>() {
//        @Override
//        public void onCompleted() {
//            lod.dismiss();
//        }
//        @Override
//        public void onError(Throwable e) {
//            lod.dismiss();
//            Log.e(tag, e.toString());
//        }
//        @Override
//        public void onNext(RetModel m) {
//            lod.dismiss();
//            if (m.ret == 2){
//                SouthUtil.showToast(ConfigApActivity.this,getString(R.string.image_setting_success));
//                reboot();
//            }
//            else if(m.ret == 1){
//                SouthUtil.showToast(ConfigApActivity.this,getString(R.string.image_setting_success));
//            }
//            else{
//                SouthUtil.showToast(ConfigApActivity.this,getString(R.string.image_setting_faild));
//            }
//        }
//    };
    public void back(View v) {
        this.finish();
    }

    LoadingDialog lod;
    //    protected Subscription subscription;
    static final String tag = "ConfigApActivity";
}