package com.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

//import com.huashi.bluetooth.HSBlueApi;
import com.screening.uitls.BluetoothUtils;
import com.util.Constss;

/**
 * Created by gyl1 on 12/21/16.
 */

public class BaseActivity extends AppCompatActivity {
    Context context;
    private boolean isRegistered = false;
//    private HSBlueApi api;
    String filepath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = this;
        //注册网络状态监听广播
//        netWorkChangReceiver = new NetWorkReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(netWorkChangReceiver, filter);
        isRegistered = true;
//        notificationUtils=new NotificationUtils(this);
//        Item.getOneItem().setNotificationUtils(notificationUtils);
//        initView();
    }

    private void initView() {
        BluetoothUtils bluetoothUtils=new BluetoothUtils(this);
        filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wltlib";// 授权目录
//        api = new HSBlueApi(this, filepath);
//        api.init();
//        int ret = api.connect(bluetoothUtils.initGetBlue(Constss.Bluetooth_Path));
//        if (ret == 0){
//            Constss.isConnBlue=true;
//            Toast.makeText(BaseActivity.this,"连接成功",Toast.LENGTH_LONG).show();
//        }else {
//            Constss.isConnBlue=true;
//        }
    }


    /**
     * 申请指定的权限.
     */
    public void requestPermission(int code, String... permissions) {

        ActivityCompat.requestPermissions(this, permissions, code);
    }

    /**
     * 判断是否有指定的权限
     */
    public boolean hasPermission(String... permissions) {

        for (String permisson : permissions) {
            if (ContextCompat.checkSelfPermission(this, permisson)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constss.WRITE_READ_EXTERNAL_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doSDCardPermission();
                }
                break;
            case Constss.ASK_CALL_BLUE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGpsPermission();
                }
                break;
            default:
                break;
        }
    }


    //处理整个应用中SD卡业务权限
    public void doSDCardPermission() {

    }

    public void openGpsPermission() {

    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
//        if (isRegistered) {
//            unregisterReceiver(netWorkChangReceiver);
//        }
    }



}
