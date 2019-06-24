package com.screening.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.R;
import com.logger.LogHelper;
import com.network.ScreenNetWork;
import com.screening.download.UpdateManager;
import com.screening.uitls.SPUtils;
import com.screening.wifi.WifiConnectManager;
import com.screening.wifi.WifiConnectUtil;
import com.util.Constss;
import com.util.SouthUtil;
import com.view.LoadingDialog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class UpdateActivity extends AppCompatActivity implements WifiConnectUtil.WifiConnectResultListener, WifiConnectUtil.WifiConnectResultListener.WifiPingResultListener,UpdateManager.UpdateProgress {
    private Button btn_left, btn_right;
    private TextView title_bar, tv_show;
    private UpdateManager updateManager;
    private LoadingDialog mDialog;
    private boolean isFront;
    private ScreenNetWork mNetWork;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
        initClick();
        WifiConnectUtil.Companion.getInstance().setWifiConnectListener(this,this);
    }

    private void initClick() {
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initView() {
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        title_bar = findViewById(R.id.title_bar);
        btn_left.setVisibility(View.VISIBLE);
        btn_right.setVisibility(View.GONE);
        title_bar.setText(getString(R.string.setting_Version_update));
        tv_show = findViewById(R.id.tv_show);
        updateManager = new UpdateManager(this, tv_show);

        UpdateManager.setUpdateProgresslisteneter(this);
        mNetWork = new ScreenNetWork();
    }



    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;
        WifiConnectUtil.Companion.getInstance().startConnectWifi(Constss.WIFI_TYPE_LAN);
        showDiolog(getString(R.string.wifiProcessMsg));
    }

//    private OkHttpClient okHttpClient;

    private void initDownLoad() {
        mNetWork.getScreenApi(3).getUpLoadAPP()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            updateManager.initJson(responseBody.string());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogHelper.i("访问失败 "+e.getMessage()+" , "+e.getCause());
                        Toasty.normal(UpdateActivity.this, "访问失败").show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    /**
     * 安装APK文件
     */
    File apkfile = null;
    public void installApk(String mSavePath, String downName) {
        apkfile = new File(mSavePath, downName);
        if (!apkfile.exists()) {
            return;
        }
        Uri uri = Uri.fromFile(apkfile);
        // 通过Intent安装APK文件
        if (uri != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                updateManager.initInstallAPK(apkfile);
            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//Android 8.0以上，增加了一个未知来源安装的权限
                if(!getPackageManager().canRequestPackageInstalls()){
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent,1);
                }else {
                    updateManager.initInstallAPK(apkfile);
                }
            }  else {
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.setDataAndType(uri, "application/vnd.android.package-archive");
                startActivity(install);
            }
        }

    }
    public static final int REQUEST_CODE_APP_INSTALL = 102;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_APP_INSTALL) {
            if (resultCode == Activity.RESULT_OK) {
                updateManager.initInstallAPK(apkfile);
            }
        }
    }

    //wifi连接成功
    @Override
    public void wifiConnectSuccess(String type) {
        showDiolog(getString(R.string.testWifi));
      WifiConnectUtil.Companion.getInstance().pingNet();
    }


    @Override
    public void wifiConnectScaning(@NotNull String type) {

    }

    @Override
    public void wifiConnectFalid(@NotNull String type, @NotNull String errorMsg) {
        dismissDiolog();
        Toasty.normal(this, errorMsg).show();
    }

    @Override
    public void pingSuccess() {
        dismissDiolog();
        initDownLoad();
    }

    @Override
    public void pingFailed() {
        dismissDiolog();
        Toasty.normal(this, getString(R.string.save_wifi_test_fail)).show();
    }

    @Override
    public void getUpdateResult(boolean result, String mSavePath, String downName) {
        Log.e("updateactivity",result+"");
        if(result){
            installApk(mSavePath,downName);
        }
    }

    @Override
    public void startInstallApk(String mSavePath, String downName) {
        installApk(mSavePath,downName);
    }


    private class MyCient implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(UpdateActivity.this, "访问失败", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            final String res = response.body().string();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateManager.initJson(res);
                }
            });
        }
    }


    private void showDiolog(String msg) {
        if (!isFront) return;
        if (mDialog != null && mDialog.isShow()) {
            mDialog.setMessage(msg);
        } else {
            if (mDialog == null) {
                mDialog = new LoadingDialog(this, true);
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

    @Override
    protected void onPause() {
        super.onPause();
        isFront = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissDiolog();
        if (mDisposable != null) {
            mDisposable.dispose();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WifiConnectUtil.Companion.getInstance().disposable();

    }
}
