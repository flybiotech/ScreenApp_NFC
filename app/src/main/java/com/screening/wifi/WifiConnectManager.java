package com.screening.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.application.MyApplication;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.util.Constss;

import java.util.List;

public class WifiConnectManager {


    private String wifiType = Constss.WIFI_TYPE_LAN;
    private String dstWifiSSID = ""; //
    private String dstWifiPass = "";
    private Thread thread1 = null;
    private int connectCount = 0;
    private ConnectivityManager connectManager;
    private NetworkInfo.State wifiState = null;
    private WifiManager wifiManager;
    private WifiConnectListener wifiConnectListener;
    private WifiConnectListener.WifiPingListener mPingListener;
    private static WifiConnectManager instance;
    public boolean repeatConnect = false; //登录时需要

    private Context mContext;

    private WifiConnectManager(Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    public static WifiConnectManager getInstance(Context mContext) {
        if (instance == null) {
            synchronized (WifiConnectManager.class) {
                if (instance == null) {
                    instance = new WifiConnectManager(mContext);
                }
            }
        }
        return instance;
    }


    public void connectWifi(String dstSSID, String dstPass, String wifiType, WifiConnectListener mListener) {
        this.dstWifiSSID = dstSSID;
        this.dstWifiPass = dstPass;
        this.wifiType = wifiType;
        this.wifiConnectListener = mListener;
        WifiUtils.withContext(MyApplication.getInstance()).enableWifi();
        startConnectWifi(dstWifiSSID);
    }

    public void connectWifiPing(String dstSSID, String wifiType, WifiConnectListener mListener) {
        this.dstWifiSSID = dstSSID;
        this.wifiType = wifiType;
        this.wifiConnectListener = mListener;
        WifiUtils.withContext(MyApplication.getInstance()).enableWifi();
        startConnectWifi(dstWifiSSID);
    }


    private String oldSSID = "";
    private String newSSID = "";
    private long exitTime = 0;

    //开始连接WiFi
    private void startConnectWifi(String dstSSID) {
        oldSSID = newSSID;
        newSSID = dstSSID;
        connectCount = 0;
        stopThreadConnectWifi();
        Log.e("TAG_WifiManager", "startConnectWifi: " + wifiType + " 的wifi开始连接....... oldSSID =" + oldSSID + " , newSSID = " + newSSID + " , wifiType = " + wifiType);
        if (dstSSID.equals("")) {
            wifiConnectListener.wifiInputNameEmpty(wifiType);
            return;
        }
        if (wifiStated()) {
            checkResult(true);
        } else {
            wifiConnectListener.startWifiConnecting(wifiType);
            taskThreadConnect(dstSSID);
        }
    }


    //获取系统当前连接的wifi 的ssid
    private String getSysConnectedSSID() {
        initWifiManager();
        initConnectivityManager();
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        wifiState = connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();

        return wifiInfo == null ? "" : wifiInfo.getSSID();
    }

    // return true ：表示wifi已经连接好了，，false 表示wifi没有正确连接
    private boolean wifiStated() {
        String srcSSID = getSysConnectedSSID();
        Log.e("TAG_WifiManager", "wifiStated: srcSSID = " + srcSSID + " , newSSID = " + newSSID);
        if (oldSSID.equals(newSSID) && srcSSID.equals("\"" + newSSID + "\"") && NetworkInfo.State.CONNECTED == wifiState) {
            //false 表示没有切换wifi login 是作为判断用的
            setWifiRepeatConnect(false);
            return true;
        }
        setWifiRepeatConnect(true);
        return false;
    }

    //判断wifi是否重复连接
    private boolean isWifiRepeat() {
        String srcSSID = getSysConnectedSSID();
        if (srcSSID.equals("\"" + newSSID + "\"") && NetworkInfo.State.CONNECTED == wifiState) {
            return true;
        }
        return false;
    }


    private void taskThreadConnect(final String dstSSID) {
        initWifiManager();
        initConnectivityManager();

        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        //  reslut =true 表示搜到了指定 的wifi
                        boolean result = scanResultWifi(dstSSID);
                        Log.e("TAG_WifiManager", "taskThreadConnect: ---------------" + "---------------wifiType = " + wifiType + " ,result = " + result + ",  connectCount= " + connectCount);
                        if (result) { //开始连接wifi
                            connectCount = 0;
                            wifiConnectListener.wifiCycleSearch(wifiType, true, connectCount);
                            stopThreadConnectWifi();
                        } else { //主机没有打开

                            connectCount++;
                            wifiConnectListener.wifiCycleSearch(wifiType, false, connectCount);
                        }

                        Thread.sleep(Constss.wifiConnectTime);

                    } catch (InterruptedException e) {
                        thread1 = null;
                        Log.e("TAG_WifiManager", "run: 子线程停止了  当前wifi类型是 wifiType = " + wifiType + "  , error = " + e.getMessage());
                        return;
                    }

                }
            }
        });
        thread1.start();

    }


    private void initWifiManager() {
        if (wifiManager == null) {
            wifiManager = (WifiManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }
    }

    private void initConnectivityManager() {
        if (connectManager == null) {
            connectManager = (ConnectivityManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        }
    }


    public void connectWithWpa(String wifiName, String wifiPass) {
//        setWifiRepeatConnect(false);
        Log.e("TAG_WifiManager", "connectWithWpa: wifi连接中 ....... SSID = " + wifiName);
        WifiUtils.withContext(MyApplication.getContext()).enableWifi();
        String ote = wifiName;
        String otePass = wifiPass;

        WifiUtils.withContext(MyApplication.getContext())
                .connectWith(ote, otePass)
                .setTimeout(40000)
                .onConnectionResult(this::checkResult)
                .start();
    }


    private void checkResult(boolean isSuccess) {
        Log.e("TAG_", "checkResult: 发送wifi连接的结果 " + isSuccess + " , wifiType = " + wifiType + " ,repeatConnect = " + repeatConnect);
        if (isSuccess) { //wifi 连接成功
            if (wifiConnectListener != null) {
                wifiConnectListener.wifiConnectSuccess(wifiType);
                wifiConnectListener = null;
            }


        } else if (!isWifiRepeat()) {
            if (wifiConnectListener != null) {
                wifiConnectListener.wifiConnectFalid(wifiType);
            }

        }
    }


    private boolean scanResultWifi(String ssid) {
        initWifiManager();
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
        List<ScanResult> scan = wifiManager.getScanResults();
        //如果没有搜索到wifi，就开启GPS
        if (scan.size() == 0) {
            return false;
        } else if (scan.size() > 0) {
            for (int i = 0; i < scan.size(); i++) {
                if (scan.get(i).SSID.equals(ssid)) {
                    return true;
                }
            }
        }
        return false;
    }

    //停止WiFi重复连接的子线程
    public void stopThreadConnectWifi() {
        if (thread1 != null) {
            thread1.interrupt();
        }
    }

    //设置wifi 是否切换了 true  表示切换了，false 表示没有切换
    public void setWifiRepeatConnect(boolean repeatConnect) {
        this.repeatConnect = repeatConnect;
    }

    // false  表示没有切换wifi ,就不需要等待wifi稳定
    public boolean getWifiConnectResult() {
        return repeatConnect;
    }

    //测试 是否能上网
    public synchronized void ping(WifiConnectListener.WifiPingListener mPingListener) {
        this.mPingListener = mPingListener;
        initConnectivityManager();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(1000);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        try {
                            NetworkCapabilities networkCapabilities = connectManager.getNetworkCapabilities(connectManager.getActiveNetwork());
                            /**
                             * NOT_METERED&INTERNET&NOT_RESTRICTED&TRUSTED&NOT_VPN
                             *
                             */
                            Log.i("TAG_Avalible", "NetworkCapalbilities:" + networkCapabilities.toString());

                            boolean valudated = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                            if (valudated) {
                                if (mPingListener != null) {
                                    mPingListener.pingSuccess();
                                }
//                                mHandler.sendEmptyMessage(0); //链接成功
                            } else {
                                if (mPingListener != null) {
                                    mPingListener.pingFailed();
                                }
//                                mHandler.sendEmptyMessage(1);
                            }

                        } catch (Exception e) {
                            if (mPingListener != null) {
                                mPingListener.pingFailed();
                            }
//                            mHandler.sendEmptyMessage(1);
                        }
                    }

                } catch (Exception e) {
                    Log.e("TAG_EROOR", "run: 错误I " + e.getMessage());
                    if (mPingListener != null) {
                        mPingListener.pingFailed();
                    }
                }
            }
        }).start();
    }


    public interface WifiConnectListener {

        void startWifiConnecting(String type);  //开始连接wifi

        void wifiConnectSuccess(String type); //wifi 连接成功

        void wifiConnectFalid(String type); //wifi连接失败

        //isSSID =true 表示已经搜索到了wifi， isSSID=false 没有搜到到指定的wifi ,然后循环搜索 isSSID :
        void wifiCycleSearch(String type, boolean isSSID, int count); //

        void wifiInputNameEmpty(String type); //输入的wifi名称为空

        interface WifiPingListener {

            //网络能上网
            void pingSuccess();

            //网络不能上网
            void pingFailed();

        }


    }
}
