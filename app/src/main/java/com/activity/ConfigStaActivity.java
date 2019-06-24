package com.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.adapter.SSIDItemAdapter;
import com.logger.LogHelper;
import com.model.DevModel;
import com.model.RetModel;
import com.model.SSIDModel;
import com.model.SSIDSavedModel;
import com.model.WIFIStateModel;
import com.network.Network;
import com.util.Constss;
import com.util.SouthUtil;
import com.util.ToastUtils;
import com.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;



/**
 * Created by gyl1 on 4/1/17.
 */

public class ConfigStaActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    int index; //
    DevModel model;
    Button btn_refresh;
    Spinner sp_wifi_ssid;
    SSIDItemAdapter ssidAdapter;
    WIFIStateModel mWifiStateModel;
    TextInputEditText ed_wifi_pwd;
    TextInputLayout layout_wifi_pwd;
    Button btn_sta;
    List<SSIDModel> mSSIDs = new ArrayList<>();
    List<SSIDSavedModel> mSavedSSIDs;
    int channel;//选中的第几个ssid 默认是0

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//禁止屏幕休眠
        setContentView(R.layout.activity_configsta);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            mWifiStateModel = bundle.getParcelable("wifi");
            index = bundle.getInt("index");
            model=bundle.getParcelable("dev");

        }


        sp_wifi_ssid = (Spinner) findViewById(R.id.sp_wifi_ssid);
        ed_wifi_pwd = (TextInputEditText) findViewById(R.id.ed_wifi_pwd);
        layout_wifi_pwd = (TextInputLayout) findViewById(R.id.layout_wifi_pwd);
        btn_sta = (Button) findViewById(R.id.btn_sta);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
        btn_sta.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);
        layout_wifi_pwd.setPasswordVisibilityToggleEnabled(true);
        initSpinner();
        initData();

    }

    public void back(View v) {
        this.finish();
    }

    public void initSpinner() {

        ssidAdapter = new SSIDItemAdapter(this);
        ssidAdapter.setItems(mSSIDs);
        sp_wifi_ssid.setAdapter(ssidAdapter);
        sp_wifi_ssid.setOnItemSelectedListener(this);


    }

    void initData() {
        if (model == null) {
            return;
        }

        if (lod == null) {
            lod = new LoadingDialog(ConfigStaActivity.this);
        }
        lod.dialogShow();

        Observable<List<SSIDModel>> observable1 = Network.getCommandApi(model).getSSIDList(model.usr, model.pwd, 36, SouthUtil.getRandom());
        Observable<List<SSIDSavedModel>> observable2 = Network.getCommandApi(model).getSavedSSIDInf(model.usr, model.pwd, 70, SouthUtil.getRandom());

        Observable.combineLatest(observable1, observable2, new BiFunction<List<SSIDModel>, List<SSIDSavedModel>, Boolean>() {
            @Override
            public Boolean apply(List<SSIDModel> ssidModels, List<SSIDSavedModel> ssidSavedModels) throws Exception {
                LogHelper.i("ssidModels.size="+ssidModels.size());
                mSSIDs = ssidModels;
                mSavedSSIDs = ssidSavedModels;
                return true;
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                lod.dismiss();
                ssidAdapter.setItems(mSSIDs);
                ssidAdapter.notifyDataSetChanged();
                for (int i = 0; i < mSSIDs.size(); i++) {
                    if (mWifiStateModel.wifi_SSID_STA.equals(mSSIDs.get(i).SSID)) {
                        sp_wifi_ssid.setSelection(i);
                        break;
                    }
                }
                setText4EditPwdText();
            }
        })
        ;

//                Observable.combineLatest(, ,
//                new Func2<List<SSIDModel>, List<SSIDSavedModel>, Boolean>() {
//                    @Override
//                    public Boolean call(List<SSIDModel> list1, List<SSIDSavedModel> list2) {
//                        mSSIDs = list1;
//                        mSavedSSIDs = list2;
//                        return true;
//                    }
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Boolean>() {
//                    @Override
//                    public void onCompleted() {
//                        lod.dismiss();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        lod.dismiss();
//                    }
//
//                    @Override
//                    public void onNext(Boolean vailed) {
//                        lod.dismiss();
//                        ssidAdapter.setItems(mSSIDs);
//                        ssidAdapter.notifyDataSetChanged();
//                        for (int i = 0; i < mSSIDs.size(); i++) {
//                            if (mWifiStateModel.wifi_SSID_STA.equals(mSSIDs.get(i).SSID)) {
//                                sp_wifi_ssid.setSelection(i);
//                                break;
//                            }
//
//                        }
//                        setText4EditPwdText();
//                    }
//                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        channel = i;
        setText4EditPwdText();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_sta) {
            if (mSSIDs.size() == 0) {
                SouthUtil.showToast(this, getString(R.string.image_setting_STA_wifi));
                return;
            }
            final String wifi_ssid = mSSIDs.get(channel).SSID;
            final String wifi_pwd = ed_wifi_pwd.getText().toString();
            if (wifi_pwd.length() == 0) {
                SouthUtil.showToast(context, getString(R.string.image_setting_STA_wifi_password));
            } else {
                String msg = getString(R.string.image_setting_STA_message) + "\r\n" + getString(R.string.image_setting_AP_message2) + wifi_ssid + "\r\n" + getString(R.string.image_setting_AP_message3) + wifi_pwd;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(msg)
                        .setTitle(getString(R.string.image_setting_STA_dialog))
                        .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (model == null) {
                                    ToastUtils.showToast(ConfigStaActivity.this, getString(R.string.image_setting_faild));
                                } else {
                                    if (lod == null) {
                                        lod = new LoadingDialog(ConfigStaActivity.this);
                                    }
                                    lod.dialogShow();
                                    Observable subscription = Network.getCommandApi(model)
                                            .AP2STA_changeValue(model.usr, model.pwd, 38, 1, 0, wifi_ssid, wifi_pwd, SouthUtil.getRandom())
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread());
                                    subscription.subscribe(observer);
                                }

                            }
                        })
                        .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        } else if (view.getId() == R.id.btn_refresh) {
            initData();
        }

    }

    void setText4EditPwdText() {
        boolean compare = false;
        for (SSIDSavedModel mode : mSavedSSIDs) {
            String SSID = mSSIDs.get(channel).SSID;
            if (mode.SSID.equals(SSID)) {
                compare = true;
                ed_wifi_pwd.setText(mode.Password);
                break;
            }
        }
        if (!compare) {
            ed_wifi_pwd.setText("");
        }
    }

    void reboot() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getString(R.string.image_setting_restart))
                .setTitle(getString(R.string.image_setting_restart_message))
                .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //
                        if (model == null) {
                            ToastUtils.showToast(ConfigStaActivity.this, getString(R.string.image_setting_faild));
                        } else {

                            if (lod == null) {
                                lod = new LoadingDialog(ConfigStaActivity.this);
                            }
                            lod.dialogShow();
                           Observable observable = Network.getCommandApi(model)
                                    .reboot(model.usr, model.pwd, 18, SouthUtil.getRandom())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());

                            observable.subscribe(observer_reboot);
                        }


                    }
                })
                .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
    Observer<RetModel> observer =new Observer<RetModel>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(RetModel retModel) {
            if (lod != null) {
                lod.dismiss();
            }
            if (retModel.ret == 2) {
                SouthUtil.showToast(ConfigStaActivity.this, getString(R.string.image_setting_success_message));
                reboot();
            } else if (retModel.ret == 1) {
                SouthUtil.showToast(ConfigStaActivity.this, getString(R.string.image_setting_success));
            } else {
                SouthUtil.showToast(ConfigStaActivity.this, getString(R.string.image_setting_faild));
            }
        }

        @Override
        public void onError(Throwable e) {
            if (lod != null) {
                lod.dismiss();
            }
        }

        @Override
        public void onComplete() {
            if (lod != null) {
                lod.dismiss();
            }
        }
    };
//    Observer<RetModel> observer = new Observer<RetModel>() {
//        @Override
//        public void onCompleted() {
//            if (lod != null) {
//                lod.dismiss();
//            }
//
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            if (lod != null) {
//                lod.dismiss();
//            }
//        }
//
//        @Override
//        public void onNext(RetModel m) {
//            if (lod != null) {
//                lod.dismiss();
//            }
//            if (m.ret == 2) {
//                SouthUtil.showToast(ConfigStaActivity.this, getString(R.string.image_setting_success_message));
//                reboot();
//            } else if (m.ret == 1) {
//                SouthUtil.showToast(ConfigStaActivity.this, getString(R.string.image_setting_success));
//            } else {
//                SouthUtil.showToast(ConfigStaActivity.this, getString(R.string.image_setting_faild));
//            }
//        }
//    };
    Observer<RetModel> observer_reboot=new Observer<RetModel>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(RetModel retModel) {
            if (lod != null) {
                lod.dismiss();
            }
            if (retModel.ret == 1) {
                SouthUtil.showToast(ConfigStaActivity.this, getString(R.string.image_setting_restart_successs));
            } else {
                SouthUtil.showToast(ConfigStaActivity.this, getString(R.string.image_setting_restart_faild));
            }
        }

        @Override
        public void onError(Throwable e) {
            if (lod != null) {
                lod.dismiss();
            }
        }

        @Override
        public void onComplete() {
            if (lod != null) {
                lod.dismiss();
            }
        }
    };
//    Observer<RetModel> observer_reboot = new Observer<RetModel>() {
//        @Override
//        public void onCompleted() {
//            if (lod != null) {
//                lod.dismiss();
//            }
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            if (lod != null) {
//                lod.dismiss();
//            }
//        }
//
//        @Override
//        public void onNext(RetModel m) {
//            if (lod != null) {
//                lod.dismiss();
//            }
//            if (m.ret == 1) {
//                SouthUtil.showToast(ConfigStaActivity.this, getString(R.string.image_setting_restart_successs));
//            } else {
//                SouthUtil.showToast(ConfigStaActivity.this, getString(R.string.image_setting_restart_faild));
//            }
//        }
//    };


    LoadingDialog lod;
//    protected Subscription subscription;
    static final String tag = "ConfigStaActivity";

}
