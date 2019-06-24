package com.screening.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.activity.BaseActivity;
import com.activity.R;
import com.jakewharton.rxbinding3.view.RxView;
import com.orhanobut.logger.Logger;
import com.screening.uitls.SPUtils;
import com.screening.wifi.WifiConnectManager;
import com.screening.wifi.WifiConnectUtil;
import com.screening.wifi.WifiHotAdapter;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.util.Constss;
import com.util.SouthUtil;
import com.view.LoadingDialog;
import com.view.dialog.ChoiceDialog;
import com.view.dialog.ConfirmDialog;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;


//wifi设置
public class WifiSettingActivity extends BaseActivity implements View.OnClickListener,
        WifiConnectUtil.WifiConnectResultListener, WifiConnectUtil.WifiConnectResultListener.WifiPingResultListener {

    private String TAG = "TAG_1_WifiSettingActivity";
    private Button clear, save, btn_right, btn_left, selectBtnLAN, selectBtnGetImageSoft;
    private EditText editName, editPass, editFilter;
    private TextView title_text;
    private WifiHotAdapter adapter;
    private String wifiName = "";
    private int wifiType = 0;// 判断是视珍宝的wifi type=0  还是局域网的wifi type=1
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_setting);

        init();
        WifiConnectUtil.Companion.getInstance().setWifiConnectListener(this, this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!hasPermission(Constss.ASK_CALL_BLUE_PERMISSION)) {
            requestPermission(Constss.ASK_CALL_BLUE_CODE, Constss.ASK_CALL_BLUE_PERMISSION);
        } else {
            selectBtn(0);
        }
        rxClick();


    }

    private void init() {
        clear = (Button) findViewById(R.id.btn_wifiSetting_clear01);
        save = (Button) findViewById(R.id.btn_wifiSetting_save01);
        selectBtnLAN = (Button) findViewById(R.id.btn_wifisetting_lan);
        selectBtnGetImageSoft = (Button) findViewById(R.id.btn_wifisetting_getImageSoft);
        selectBtnLAN.setOnClickListener(this);
        selectBtnGetImageSoft.setOnClickListener(this);
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_left = (Button) findViewById(R.id.btn_left);//菜单项左边的按钮
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);
        editName = (EditText) findViewById(R.id.edit_wifiSetting_name01);
        editPass = (EditText) findViewById(R.id.edit_wifiSetting_pass01);
        editFilter = (EditText) findViewById(R.id.edit_filters);//过滤的WiFi
        title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText(R.string.wifiSet);
        btn_right.setVisibility(View.INVISIBLE);
        clear.setOnClickListener(this);
        save.setOnClickListener(this);
        WifiUtils.enableLog(true);
    }


    @SuppressLint("CheckResult")
    private void rxClick() {
        RxView.clicks(save).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        if (wifiType == 0) { //保存视珍宝WiFi的密码
                            getWifiNameAndPass(Constss.SZB_WIFI_SSID_KEY, Constss.SZB_WIFI_PASS_KEY);
                            Toasty.normal(WifiSettingActivity.this, getString(R.string.wifiPass_save_success)).show();
                        } else {//保存局域网wifi的密码
                            getWifiNameAndPass(Constss.LAN_WIFI_SSID_KEY, Constss.LAN_WIFI_PASS_KEY);
                            //开始连接局域网WiFi
                            showDiolog(getString(R.string.wifiProcessMsg));
                            WifiConnectUtil.Companion.getInstance().startConnectWifi(Constss.WIFI_TYPE_LAN);
                        }
                    }
                });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wifiSetting_clear01:// 清除相关信息
                editName.setText("");
                editPass.setText("");
                break;

            case R.id.btn_wifisetting_getImageSoft: //图像获取软件wifi设置

                selectBtn(0);
                refreshWifiList(this);

                break;
            case R.id.btn_wifisetting_lan://局域网wifi设置
                selectBtn(1);
                refreshWifiList(this);

                break;
            case R.id.btn_left:
                finish();
                break;
            default:
                break;
        }
    }


    //如果输入的账号和密码都没有变，则不需要修改保存的内容，如果有变化，就需要修改保存的内容了
    private void getWifiNameAndPass(String key_Name, String key_Pass) {
        SPUtils.put(this, key_Name, editName.getText().toString().trim());
        SPUtils.put(this, key_Pass, editPass.getText().toString().trim());
    }


    private void showDiolog(String msg) {
        if (mDialog != null && mDialog.isShow()) {
            mDialog.setMessage(msg);
        } else {
            if (mDialog == null) {
                mDialog = new LoadingDialog(WifiSettingActivity.this, true);
            }
            mDialog.setMessage(msg);
            mDialog.dialogShow();
        }
    }

    private void dismissDiolog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void selectBtn(int mState) {
        String wifiName = "";
        String wifiPass = "";
        selectBtnLAN.setSelected(false);
        selectBtnGetImageSoft.setSelected(false);
        switch (mState) {
            case 0: //主机
                selectBtnGetImageSoft.setSelected(true);
                wifiType = 0;
                wifiName = (String) SPUtils.get(this, Constss.SZB_WIFI_SSID_KEY, "");
                wifiPass = (String) SPUtils.get(this, Constss.SZB_WIFI_PASS_KEY, "12345678");

                break;
            case 1://局域网
                selectBtnLAN.setSelected(true);
                wifiType = 1;
                wifiName = (String) SPUtils.get(this, Constss.LAN_WIFI_SSID_KEY, "");
                wifiPass = (String) SPUtils.get(this, Constss.LAN_WIFI_PASS_KEY, "");
                break;
            default:
                break;
        }

        editName.setText(wifiName);
        editPass.setText(wifiPass);

    }

    @Override
    public void openGpsPermission() {
        super.openGpsPermission();
        refreshWifiList(this);

    }


    //给listview 列表填充数据 ,将搜索到的wifi显示在listview上
    @SuppressLint("CheckResult")
    private void refreshWifiList(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
        List<ScanResult> scan = wifiManager.getScanResults();
        //如果没有搜索到wifi，就开启GPS
        if (scan.size() == 0) {
            openGPSSettings();
        } else {


            Observable.fromIterable(scan)
                    .filter(new Predicate<ScanResult>() {
                        @Override
                        public boolean test(ScanResult scanResult) throws Exception {
                            return scanResult.SSID.contains(editFilter.getText().toString().trim());
                        }
                    })
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<ScanResult>>() {
                        @Override
                        public void accept(List<ScanResult> scanResults) throws Exception {
                            if (null == adapter) {
                                adapter = new WifiHotAdapter(scanResults, WifiSettingActivity.this);
                            } else {
                                adapter.refreshData(scanResults);
                            }
                            lv_add(scanResults);
                        }
                    });

        }
    }


    private void lv_add(final List<ScanResult> results) {
        LinearLayout linearLayoutMain = new LinearLayout(this);//自定义一个布局文件
        linearLayoutMain.setLayoutParams(new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        ListView listView = new ListView(this);//this为获取当前的上下文
        listView.setFadingEdgeLength(0);
        listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        listView.setAdapter(adapter);
        linearLayoutMain.addView(listView);//往这个布局中加入listview
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.setting_wifi_name)).setView(linearLayoutMain)//在这里把写好的这个listview的布局加载dialog中
                .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
        dialog.show();
        listView.setFocusable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//响应listview中的item的点击事件
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                wifiName = results.get(arg2).SSID;
                editName.setText(wifiName);
                dialog.cancel();
            }
        });
    }


    //跳到GPS设置界面
    private int GPS_REQUEST_CODE = 10;

    private boolean checkGPSIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    /**
     * 跳转GPS设置
     */
    private void openGPSSettings() {
        if (!checkGPSIsOpen()) {
            new ConfirmDialog(this)
                    .setTtitle(getString(R.string.setting_gps), 20.f, Color.BLACK)
                    .setMessage(getString(R.string.setting_gps_list))
                    .setPositiveButton(getString(R.string.setting_wifi_title), Color.RED, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //跳转GPS设置界面
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, GPS_REQUEST_CODE);
                        }
                    }).setNegativeButton(getString(R.string.button_cancel), Color.GRAY, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
        }
    }


    @Override
    public void wifiConnectSuccess(String type) {

        if (type.equals(Constss.WIFI_TYPE_LAN)) {
            showDiolog(getString(R.string.wifi_test_net));
            WifiConnectUtil.Companion.getInstance().pingNet();
        } else {
            dismissDiolog();
        }

    }

    @Override
    public void wifiConnectFalid(String type, String errorMsg) {
        Logger.i(TAG, "wifiConnectFalid type = " + type);
        dismissDiolog();
        Toasty.normal(this, errorMsg).show();

    }

    @Override
    public void wifiConnectScaning(@NotNull String type) {
        showDiolog(getString(R.string.wifiLANFailMsg));
        Logger.i(TAG, "wifiConnectScaning type = " + type);
    }


    @Override
    public void pingSuccess() {
        Toasty.normal(this, getString(R.string.save_wifi_test_success)).show();

        dismissDiolog();
    }

    @SuppressLint("CheckResult")
    @Override
    public void pingFailed() {
        Toasty.normal(this, getString(R.string.save_wifi_test_fail)).show();

        dismissDiolog();
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDiolog();
        WifiConnectUtil.Companion.getInstance().disposable();
    }
}







