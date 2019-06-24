package com.screening.activity;


import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.activity.BaseActivity;
import com.activity.R;
import com.logger.LogHelper;
import com.screening.manager.UserManager;
import com.screening.model.Item;
import com.screening.service.UpLoadService;
import com.screening.uitls.BackupsLitepalUtils;
import com.screening.uitls.CreateScrUtil;
import com.screening.uitls.VerificationUtils;
import com.screening.wifi.NetBroadcastReceiver;
import com.screening.wifi.NetEvent;
import com.screening.wifi.WifiConnectUtil;
import com.util.Constss;
import com.util.SouthUtil;
import com.view.LoadingDialog;
import com.white.progressview.CircleProgressView;

import org.jetbrains.annotations.NotNull;

import es.dmoral.toasty.Toasty;



public class AdminSetActivity extends BaseActivity implements View.OnClickListener, UpLoadService.UpLoadFileProcess,
        NetEvent, VerificationUtils.VerificationResult
        , BackupsLitepalUtils.BackupsResult, CreateScrUtil.OnScreeningProcessListener, WifiConnectUtil.WifiConnectResultListener, WifiConnectUtil.WifiConnectResultListener.WifiPingResultListener {


    private Button bt_commit, btn_left, btn_right;
    private TextView title_text, tv_docId;
    private CircleProgressView progressBar;
    private LoadingDialog mDialog;
    private NetBroadcastReceiver netBroadcastReceiver;
    private UserManager userManager;
    private String docId = "";
    private CreateScrUtil createScrUtil;
    private boolean isClickConn;
    private TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//禁止屏幕休眠
        setContentView(R.layout.activity_admin_set);
        LogHelper.v("");
        initView();
        initClick();
        registerReceiver();

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogHelper.v("");
        isFront = true;

    }


    @Override
    protected void onResume() {
        super.onResume();
        isClickConn = false;
        WifiConnectUtil.Companion.getInstance().startConnectWifi(Constss.WIFI_TYPE_LAN);
        docId = userManager.searchDoctorId();
        tv_docId.setText(getString(R.string.doctorid) + " : " + docId);
    }

    private void initClick() {
        bt_commit.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        UpLoadService.setUpLoadFileProcessListener(this);
        VerificationUtils.setGetVerificationResultListener(this);
        BackupsLitepalUtils.setBackupsResultLisnester(this);
    }

    private void initView() {
        tv_show = findViewById(R.id.tv_show);
        backupsLitepalUtils = new BackupsLitepalUtils(this);
        progressBar = findViewById(R.id.circle_progress_normal);
        bt_commit = findViewById(R.id.bt_commit);
        tv_docId = findViewById(R.id.tv_docId);//医生编号
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setText(getString(R.string.title_back));
        btn_left.setTextSize(15);
        btn_right.setVisibility(View.GONE);
        title_text = findViewById(R.id.title_text);
        title_text.setText(getString(R.string.commitData));
        Item.getOneItem(this).setBt_return(btn_left);
        userManager = new UserManager();
        createScrUtil = new CreateScrUtil(this);
        createScrUtil.setOnScreeningProcessListener(this);
        WifiConnectUtil.Companion.getInstance().setWifiConnectListener(this, this);
    }


    private BackupsLitepalUtils backupsLitepalUtils;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_commit://上传数据
                if (!docId.equals("")) {
                    isClickConn = true;
                    showDiolog(getString(R.string.creatingScreen));
                    WifiConnectUtil.Companion.getInstance().startConnectWifi(Constss.WIFI_TYPE_LAN);
                } else {
                    SouthUtil.showToast(AdminSetActivity.this, getString(R.string.doctorid_isnull));
                    Intent intent = new Intent(AdminSetActivity.this, DocNumActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_left:
                finish();
                break;
            default:
                break;
        }
    }

    private void showDiolog(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog != null && mDialog.isShow()) {
                    mDialog.setMessage(msg);
                } else {
                    if (mDialog == null) {
                        mDialog = new LoadingDialog(AdminSetActivity.this, true);
                    }
                    mDialog.setMessage(msg);
                    mDialog.dialogShow();
                }
            }
        });

    }

    private void dismissDiolog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    private boolean isFront = false;//判断activity是否在前台

    @Override
    public void getUpLoadFileProcessPrecent(final double percent) {
        LogHelper.d("activity是否在前台isFront=" + isFront);
        if (!isFront) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress((int) percent);
                float dpercent = (float) percent;
                bt_commit.setEnabled(false);
                tv_show.setText("上传进度 ： " + dpercent + "%");
                if (percent >= 100) {
                    dismissDiolog();
                    Toasty.normal(AdminSetActivity.this, getString(R.string.import_Success)).show();
                    bt_commit.setEnabled(true);
                } else if (percent >= 1) {
                    dismissDiolog();
                }
            }
        });
    }

    @Override
    public void loginOut(boolean outResult) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!outResult) {
                    bt_commit.setEnabled(true);
                    dismissDiolog();
                    showDiolog(getString(R.string.setting_ftp_break));
                    SystemClock.sleep(1000);
                    Intent intent = new Intent(AdminSetActivity.this, UpLoadService.class);
                    startService(intent);
                }
            }
        });
    }

    //注册广播
    private void registerReceiver() {
        if (netBroadcastReceiver == null) {
            netBroadcastReceiver = new NetBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(netBroadcastReceiver, filter);
            //设置监听
            netBroadcastReceiver.setNetEvent(this);
        }
    }

    //注销广播
    private void unRegisterReceiver() {
        if (netBroadcastReceiver != null) {
            unregisterReceiver(netBroadcastReceiver);
        }
    }

    //wifi发生改变
    @Override
    public void onNetChange(int netMobile) {
//        if (netMobile == -1) { //wifi已断开
//            Toasty.normal(this, getString(R.string.wifidiconnect)).show();
//        } else {//wifi已连接
//            Toasty.normal(this, getString(R.string.wificonnect)).show();
//        }
        LogHelper.i("wifi连接情况 netMobile= " + netMobile);
    }

    //验证医生是否存在 code=1 医生是存在的
    @Override
    public void getResult(int code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogHelper.d("code=-3表示网络异常，code=1表示正常 code=" + code);
                if (code == 1) {
                    createScrUtil.startCreateScreenings();
                } else if (code == -3) {
                    dismissDiolog();
                    Toasty.normal(AdminSetActivity.this, getString(R.string.verifyFailed)).show();
                } else {
                    Toasty.normal(AdminSetActivity.this, getString(R.string.doctorNO)).show();
                    Intent intent = new Intent(AdminSetActivity.this, DocNumActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void getVerifitionResult(int temp) {

    }

    @Override
    public void getVerifitionBarcodeResult(int temp) {

    }


    //备份结果
    @Override
    public void getBackupsResult(int type, boolean result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type) {
                    case 1:
                        if (result) {
                            new VerificationUtils().getVerification(userManager.searchDoctorId());
                            Toasty.normal(AdminSetActivity.this, getString(R.string.setting_backp_success)).show();
                        } else {
                            Toasty.normal(AdminSetActivity.this, getString(R.string.setting_backp_faild)).show();
                        }
                        break;
                    case 2://图片复制动作结束
                        if (result) {
                            backupsLitepalUtils.backUplitepalAdmin();
                        }
                        break;
                    case 5:
                        dismissDiolog();
                        Toasty.normal(AdminSetActivity.this, getString(R.string.setting_back_insufficient_memory)).show();
                        break;
                    case 6:
                        new VerificationUtils().getVerification(userManager.searchDoctorId());
                        break;
                    case 7:
                        dismissDiolog();
                        Toasty.normal(AdminSetActivity.this, getString(R.string.setting_SD_create_faild)).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void screeningProcessFile(@NotNull String type, @NotNull String errorMsg) {
        LogHelper.e("错误类型：" + type + " ,错误信息：" + errorMsg);
        dismissDiolog();
        Toasty.normal(this, errorMsg).show();
    }

    @Override
    public void screeningProcessSuccess(@NotNull String type, @NotNull String processMsg) {
        LogHelper.i(processMsg);
//        dismissDiolog();
//        Toasty.normal(this, "成功类型：" + type + " ,成功信息：" + processMsg).show();
        Toasty.normal(this, processMsg).show();
    }

    @Override
    public void wifiConnectSuccess(String type) {
        LogHelper.i("wifi连接成功 wifi的类型type" + type);
        if (isClickConn) {
//            showDiolog(getString(R.string.testWifi));
            WifiConnectUtil.Companion.getInstance().pingNet();
        }
    }

    @Override
    public void wifiConnectScaning(@NotNull String type) {
        LogHelper.i("WiFi正在后台搜索中...type=" + type);

    }

    @Override
    public void wifiConnectFalid(@NotNull String type, @NotNull String errorMsg) {
        dismissDiolog();
        Toasty.normal(this, errorMsg).show();
        LogHelper.i("wifi连接失败...type=" + type + " ,错误信息 errorMsg=" + errorMsg);
    }


    @Override
    public void pingSuccess() {
        LogHelper.d("ping成功,开始备份");
//        showDiolog(getString(R.string.startbackup_adminSetAct));
        String SDMemoryAvailable = backupsLitepalUtils.getSDMessage();
        if (SDMemoryAvailable == null || "".equals(SDMemoryAvailable)) {
            dismissDiolog();
            Toasty.normal(this, getString(R.string.setting_SD_no)).show();
        } else {
            backupsLitepalUtils.copyToSd(SDMemoryAvailable);
        }
    }

    @Override
    public void pingFailed() {
        LogHelper.d("ping失败，wifi不能上网");
        dismissDiolog();
        Toasty.normal(this, getString(R.string.save_wifi_test_fail)).show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        LogHelper.v("");
    }


    @Override
    protected void onStop() {
        super.onStop();
        dismissDiolog();
        isFront = false;
        LogHelper.v("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogHelper.v("");
        unRegisterReceiver();
        dismissDiolog();
        UpLoadService.setUpLoadFileProcessNull();
        VerificationUtils.setGetVerificationResultNull();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
