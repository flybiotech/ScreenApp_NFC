package com.screening.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.activity.R;
import com.screening.model.FTPBean;
import com.screening.rotatedialog.dialog.MyDialog;
import com.util.Constss;
import com.util.SouthUtil;
import com.util.ToastUtils;
import com.view.LoadingDialog;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.litepal.LitePal;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.TimeZone;


public class FTPSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bt_ftp_cancel, bt_ftp_save, btn_left;
    private EditText et_ftp_password, et_ftp_name;
    private TextView title_bar;
    List<FTPBean> ftpBeanList;
    private int isFont = 1;//页面销毁时，不执行子线程的返回结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftpsetting);
        initView();
        initShow();
        initClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            isFont = -1;//不执行
            Log.e("loadingDialog", loadingDialog + "111111" + isFont);
        }
    }

    private void initShow() {
        ftpBeanList = LitePal.findAll(FTPBean.class);
        if (ftpBeanList.size() > 0) {
            et_ftp_name.setText(ftpBeanList.get(0).getFTPName());
            et_ftp_password.setText(ftpBeanList.get(0).getFTPPassword());
        }
    }

    private void initClick() {
        bt_ftp_save.setOnClickListener(this);
        bt_ftp_cancel.setOnClickListener(this);
        btn_left.setOnClickListener(this);
    }

    private void initView() {
        bt_ftp_cancel = findViewById(R.id.bt_ftp_cancel);
        bt_ftp_save = findViewById(R.id.bt_ftp_save);
        et_ftp_name = findViewById(R.id.et_ftp_name);
        et_ftp_password = findViewById(R.id.et_ftp_password);
        btn_left = findViewById(R.id.btn_left);
        title_bar = findViewById(R.id.title_bar);
        title_bar.setText(getString(R.string.ftpsetting));
        btn_left.setVisibility(View.VISIBLE);
        et_ftp_name.setEnabled(false);
        et_ftp_password.setEnabled(false);
        ftpClient = new FTPClient();
        bt_ftp_cancel.setVisibility(View.GONE);
        bt_ftp_save.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_ftp_save:
                initSave();
                break;
            case R.id.bt_ftp_cancel:
                finish();
                break;
            case R.id.btn_left:
                finish();
                break;
            default:
                break;
        }
    }

    private boolean isSave = false;

    //保存FTP服务器账号密码
    private void initSave() {
        if (et_ftp_name.getText().toString().trim() != null && et_ftp_password.getText().toString().trim() != null && !TextUtils.isEmpty(et_ftp_name.getText().toString().trim()) && !TextUtils.isEmpty(et_ftp_password.getText().toString().trim())) {

            initSaveFTP();
            if (isSave) {
                if (isNetworkAvailable()) {
                    getUrl();
                } else {
                    ToastUtils.showToast(this, "请先设置局域网WIFI");
                }
            }
        } else {
            ToastUtils.showToast(this, "请将信息填写完整");
        }

    }

    /**
     * 保存ftp服务器账户密码
     */
    private void initSaveFTP() {
        if (ftpBeanList.size() > 0) {
            ftpBeanList.get(0).setFTPName(et_ftp_name.getText().toString().trim());
            ftpBeanList.get(0).setFTPPassword(et_ftp_password.getText().toString().trim());
            isSave = ftpBeanList.get(0).save();
        } else {
            FTPBean ftpBean = new FTPBean();
            ftpBean.setFTPName(et_ftp_name.getText().toString().trim());
            ftpBean.setFTPPassword(et_ftp_password.getText().toString().trim());
            isSave = ftpBean.save();
        }
        ToastUtils.showToast(FTPSettingActivity.this, "保存成功");
    }

    /**
     * 测试ftp服务器是否登录成功
     */
    private static final String TestUrl = "http://www.baidu.com";
    private URL url;
    private static int state = -1;//网络请求返回值
    private MyDialog myDialog;
    private static HttpURLConnection urlConnection;
    private FTPClient ftpClient;//FTP连接
    private String strIp = "118.25.70.83";//ip地址
    private int intPort = 21;//端口号
    private boolean isLogin = false;
    private int netCount = 0;//测试网络连接的次数
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("loadingDialog1111", loadingDialog + "111111" + isFont);
            if (isFont == 1) {
                stopThread();
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                if (ftpClient.isConnected()) {
                    try {
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (msg.what == 0) {
                    if (myDialog != null) {
                        myDialog.dismiss();
                    }
                    SouthUtil.showToast(FTPSettingActivity.this, getString(R.string.ftpLoginSuccess));
                    ToastUtils.showToast(FTPSettingActivity.this, "设置成功");
                } else if (msg.what == 1) {

                    initMyDialogShow(getString(R.string.ftpLoginFaild));

                } else if (msg.what == -1) {
                    if (netCount < 3) {
                        getUrl();
                    } else {
                        netCount = 0;
                        initMyDialogShow(getString(R.string.errornet));
                    }

                }
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }

            }

        }
    };


    private void initMyDialogShow(String msg) {
        if (myDialog == null) {
            myDialog = new MyDialog(getApplicationContext());
        }
        Constss.temp = -1;
        myDialog.show(getFragmentManager(), 0);
        myDialog.setMessage(msg);
        myDialog.setRotation(0);
    }

    /**
     *  * 检测网络是否连接
     *  *
     *  * @return
     *  
     */
    public boolean isNetworkAvailable() {
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        // 去进行判断网络是否连接
        return (info != null && info.isAvailable());
    }

    private LoadingDialog loadingDialog;
    private Thread thread = null;

    public void getUrl() {//测试网络是否可用与上网

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.setMessage(getString(R.string.ftpLoginTest));
        loadingDialog.dialogShow();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message message = handler.obtainMessage();
                    url = new URL(TestUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(5000);//设置连接超时时间
                    state = urlConnection.getResponseCode();
                    Log.e("netTest", state + "");
                    if (state == 302) {
                        Log.e("netTest2", state + "");
                        FTPClientConfig ftpClientConfig = new FTPClientConfig();
                        ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
                        ftpClient.setControlEncoding("utf-8");
                        ftpClient.configure(ftpClientConfig);

                        if (intPort > 0) {
                            ftpClient.connect(strIp, intPort);
                        } else {
                            ftpClient.connect(strIp);
                        }
                        // FTP服务器连接回答
                        int reply = ftpClient.getReplyCode();
                        if (!FTPReply.isPositiveCompletion(reply)) {
                            ftpClient.disconnect();
                            Log.e("faild", "登录FTP服务失败！");
                        }
                        isLogin = ftpClient.login(et_ftp_name.getText().toString().trim(), et_ftp_password.getText().toString().trim());
                        // 设置传输协议
                        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                        Log.e("netTest1", isLogin + "");
                        if (isLogin) {
                            message.what = 0;
                        } else {
                            message.what = 1;
                        }

                        handler.sendMessage(message);
                        return;
                    } else {
                        message.what = -1;
                        netCount++;
                        handler.sendMessage(message);
                        return;
                    }

                } catch (Exception e) {
                    Log.e("netTest3", state + ",,," + e.getMessage().toString());
                    Message message = handler.obtainMessage();
                    message.what = -1;
                    netCount++;
                    handler.sendMessage(message);
                    return;
                }
            }
        });
        thread.start();
    }

    private void stopThread() {
        if (thread != null) {
            thread.interrupt();
        }
    }
}
