package com.screening.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.util.Constss;

import java.util.List;


/**
 * Created by 用户 on 2017/5/3.
 */

public class WifiAutoConnectManager {
    private static final String TAG = "TAG_WifiAutoCon";

    private WifiManager wifiManager;
    private Context mContext;
    NetworkInfo.State wifiState = null;
    private boolean modify = false; //表示WiFi是否有修改 ,true 表示已经修改过了
    private ConnectivityManager connectManager;
    int connectCount = 0;

    public boolean scanResult = false; //判断扫描的结果


    // 构造函数
    public WifiAutoConnectManager(WifiManager wifiManager, Context context) {
        mContext = context;
        this.wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);//wifi管理器

        connectManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);


    }


    // 提供一个外部接口，传入要连接的无线网
    public void connect(final String ssid, String password, int connectCount) {

        this.connectCount = connectCount;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        wifiState = connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();

        final String ssid1 = wifiInfo.getSSID();
//        LogUtils.e("TAG_WIFI","modify="+modify+ " , ssid1 = "+ssid1 + " , ssid = "+ssid);
        if (ssid == null || ssid1.equals("\"" + ssid + "\"") && NetworkInfo.State.CONNECTED == wifiState) {
            Constss.wifiRepeat = true;
        } else {

            Constss.wifiRepeat = false;
            final ConnectRunnable connectRunnable = new ConnectRunnable(ssid, password, getCipherType(mContext, ssid));
            final Thread thread = new Thread(connectRunnable);
            thread.start();

        }

    }


    int mPriority = 10;

    // 查看以前是否也配置过这个网络
    @Nullable //这个表示可以传入空值
    private WifiConfiguration isExsits(String SSID) {

        Log.e(TAG, "isExsits: modify = " + modify + "  ,SSID = " + SSID);

//        检查是否已经配置过该wifi，通过WifiManager获取已经配置的wifi列表
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        if (existingConfigs == null) {
            return null;
        }
        wifiManager.disconnect();
        for (int i = 0; i < existingConfigs.size(); i++) {
//            禁用这个network（即使Wifi扫描到这个热点也不会主动去连接），参数netId就是WifiInfo中的networkId值
            wifiManager.disableNetwork(existingConfigs.get(i).networkId);
//            Log.e(TAG, " wifiState  的 id  networkId =" + existingConfigs.get(i).networkId);
            wifiManager.saveConfiguration();

        }

        for (WifiConfiguration existingConfig : existingConfigs) {
//            Log.e(TAG+"_2", "isExsits: existingConfig.SSID = "+existingConfig.SSID );
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
//                //则清除旧有配置
//                boolean b = wifiManager.removeNetwork(existingConfig.networkId);
//                wifiManager.saveConfiguration();
                Log.e(TAG + "_1", "ssid 已经在WiFi列表中配置过了 ");

                return existingConfig;
            }
        }
        return null;
    }


    private boolean removeWifi(String ssid1) {
        WifiConfiguration tempConfig = isExsits(ssid1);
        if (tempConfig != null) {
            //则清除旧有配置
            boolean b = wifiManager.removeNetwork(tempConfig.networkId);
            Log.e(TAG + "_1", "SSID = " + ssid1 + " 从系统WiFi配置文件中移除的结果 reslut = " + b);
            return b;
        } else {
            return false;
        }

    }


    private WifiConfiguration createWifiInfo(String SSID, String Password,
                                             int Type) {
//        mPriority++;
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.priority = 0;
        config.SSID = "\"" + SSID + "\"";
//        Logger.e(" createWifiInfo   Type = " + Type);
//则清除旧有配置


        removeWifi(SSID);
//        WifiConfiguration tempConfig = isExsits(SSID);
//        if (tempConfig != null) {
//            //则清除旧有配置
//            boolean b = wifiManager.removeNetwork(tempConfig.networkId);
//            wifiManager.saveConfiguration();
//            Log.e(TAG+"_1", "createWifiInfo: 移除是否成功 = "+b );
//        }

        if (Type == 1) { //没有密码的情况
//            config.wepKeys[0] = "\"" + "\"";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//
        }
        // wep
        if (Type == 2) {
            if (!TextUtils.isEmpty(Password)) {
                if (isHexWepKey(Password)) {
                    config.wepKeys[0] = Password;
                } else {
                    config.wepKeys[0] = "\"" + Password + "\"";
                }
            }
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
//            config.priority = priority+20;
        }


        // wpa
        if (Type == 3) {  //WIFICIPHER_WPA WPA加密的情况
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            // 此处需要修改否则不能自动重联
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
//            config.priority = priority+20;

        }
        return config;
    }

    // 打开wifi功能
    private boolean openWifi() {
        boolean bRet = true;
        if (!wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    // 关闭WIFI
    private void closeWifi() {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    class ConnectRunnable implements Runnable {
        private String ssid;

        private String password;

        private int type;

        private boolean isStopThread = true;//控制子线程的终止

        public ConnectRunnable(String ssid, String password, int type) {
            this.ssid = ssid;
            this.password = password;
            this.type = type;
        }

        @Override
        public void run() {
//            while (isStopThread){
            // 打开wifi
            openWifi();
            // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
            // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句

            while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                try {
                    // 为了避免程序一直while循环，让它睡个100毫秒检测……
                    Thread.sleep(200);
                } catch (InterruptedException ie) {
                }
            }

            // 搜索 WiFi 列表
            boolean result = scanResultWifi(ssid);
            Log.e(TAG, "run:wifi 列表的数量 result = " + result);
//            if (!result) {
//                EventBusUtils.sendEvent(new EventBusEvent(EventBusUtils.EventCode.WIFI_LIST_NULL,"zero"));
//                return;
//            }


            int netId = -1;
            if (connectCount > 2) {
//                EventBusUtils.sendEvent(new EventBusEvent(EventBusUtils.EventCode.WIFI_PASS_ERROR,"ERROR"));
            } else if (connectCount == 1 && removeWifi(ssid)) { //移除成功，新建config
                WifiConfiguration wifiConfig = createWifiInfo(ssid, password, type);
                netId = wifiManager.addNetwork(wifiConfig);
            } else if (isExsits(ssid) != null) { //移除失败，

//                if (isExsits(ssid) != null) {
                Log.e("TAG_sin", "这个wifi是连接过 connectCount = " + connectCount);
                netId = isExsits(ssid).networkId;
//                } else {

//                netId = wifiManager.addNetwork(createWifiInfo(ssid, password, type));
//                Log.e("TAG_sin", "没连接过的，新建一个wifi配置 netId =" + netId);
//                }
            } else {
                netId = wifiManager.addNetwork(createWifiInfo(ssid, password, type));
                Log.e("TAG_sin", "没连接过的，新建一个wifi配置 netId =" + netId + " , connectCount = " + connectCount);
            }

            Log.e("TAG_WifiReceiver_1", "添加WiFi时，返回的id netID " + netId + " , connectCount = " + connectCount);
            wifiManager.disconnect();
            boolean b = wifiManager.enableNetwork(netId, true);
            wifiManager.saveConfiguration();
            wifiManager.reconnect();
//            if (netId == -1) {
//                EventBusUtils.sendEvent(new EventBusEvent(EventBusUtils.EventCode.WIFI_netId,"-1"));
//            }

        }
    }

    private static boolean isHexWepKey(String wepKey) {
        final int len = wepKey.length();

        // WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }

        return isHex(wepKey);
    }

    private static boolean isHex(String key) {
        for (int i = key.length() - 1; i >= 0; i--) {
            final char c = key.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a'
                    && c <= 'f')) {
                return false;
            }
        }

        return true;
    }

    // 获取ssid的加密方式

    public static int getCipherType(Context context, String ssid) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);

        List<ScanResult> list = wifiManager.getScanResults();

        for (ScanResult scResult : list) {

            if (!TextUtils.isEmpty(scResult.SSID) && scResult.SSID.equals(ssid)) {
                String capabilities = scResult.capabilities;

                if (!TextUtils.isEmpty(capabilities)) {

                    if (capabilities.contains("WPA")
                            || capabilities.contains("wpa")) {
                        return 3;//WifiCipherType.WIFICIPHER_WPA;
                    } else if (capabilities.contains("WEP")
                            || capabilities.contains("wep")) {
                        return 2;//WifiCipherType.WIFICIPHER_WEP;
                    } else {
                        return 1;//WifiCipherType.WIFICIPHER_NOPASS;
                    }
                }
            }
        }
        return 3;// WifiCipherType.WIFICIPHER_INVALID;
    }


    private boolean scanResultWifi(String ssid) {
//        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
        List<ScanResult> scan = wifiManager.getScanResults();
        //如果没有搜索到wifi，就开启GPS
        if (scan.size() == 0) {
            return false;
//            openGPSSettings();
        } else if (scan.size() > 0) {
            for (int i = 0; i < scan.size(); i++) {
                Log.e("TAG_2", "scanResultWifi: " + scan.get(i).SSID + " , scanResult =" + scanResult + " , ssid= " + ssid);
                if (scan.get(i).SSID.equals(ssid)) {
                    return true;
                }
            }
        }
        return false;
    }


}
