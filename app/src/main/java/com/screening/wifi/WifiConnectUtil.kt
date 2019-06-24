package com.screening.wifi

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import com.activity.R
import com.application.MyApplication
import com.logger.LogHelper
import com.orhanobut.logger.Logger
import com.screening.uitls.SPUtils
import com.thanosfisherman.wifiutils.WifiConnectorBuilder
import com.thanosfisherman.wifiutils.WifiUtils
import com.util.Constss.*
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class WifiConnectUtil private constructor() {
    private val TAG = "TAG_1_WifiConnectUtil"
    private var WIFITYPE = WIFI_TYPE_LAN
    private var WIFISSIDKEY = SZB_WIFI_SSID_KEY
    private var WIFIPWKEY = SZB_WIFI_PASS_KEY
    private var dstWifiSSID = ""
    private var dstWifiPW = "12345678"
    private var mContext: Context? = null
    private val tiemOut = 15000L
    private var count = 0
    private var mDispose: Disposable? = null
    private var mWifiManager: WifiManager? = null
    private var mConnManager: ConnectivityManager? = null
    private var builder: WifiConnectorBuilder.WifiUtilsBuilder? = null
    private var mConnListener: WifiConnectResultListener? = null
    private var mPingListener: WifiConnectResultListener.WifiPingResultListener? = null


    //双重校验模式
    companion object {
        val instance: WifiConnectUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            WifiConnectUtil()
        }
    }

    init {
        mContext = MyApplication.getContext()
        mWifiManager = mContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        mConnManager = mContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    }


    @SuppressLint("CheckResult")
    fun startConnectWifi(type: String) {
        disposable()
        count = 0
        Observable.create(ObservableOnSubscribe<String> {

            WIFITYPE = type
            setWifiNameAndPWKey(type)
            dstWifiSSID = getWifiSSID()
            dstWifiPW = getWifiPW()

            if (dstWifiSSID.isEmpty() || dstWifiPW.isEmpty()) {
                it.onNext("empty")
            } else if (wifiStated()) {
                //当前手机连接的WiFi就是指定的WiFi，不需要重新连接
                it.onComplete()
            } else {
                builder?.cancelAutoConnect()//让wifi停止自动连接
                val isResult = getScanWifiResult()
                if (isResult) {
                    //已经搜索到指定的WiFi
                    it.onNext("success")
                } else {
                    Log.i("tag_11", "正在搜索信息")
                    //没有搜索到指定的WiFi
                    it.onNext("continueSearch")
                    Thread.sleep(1500)
                    it.onError(Throwable(mContext?.getString(R.string.wifi_conn_error)))
                }

            }

        })
                .retry(200) //进行重试主要是搜索WiFi列表
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onComplete() {
                        checkReslut(true)
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(t: String) {

                        if (t == "empty") {
                            var emptyMsg=""
                            if (WIFITYPE.equals(WIFI_TYPE_LAN)) {
                                emptyMsg = mContext?.getString(R.string.wifi_LANname_empty)?:""//局域网WiFi未设置
                            } else {
                                emptyMsg = mContext?.getString(R.string.wifi_SZBname_empty)?:""//主机WiFi未设置
                            }
                            mConnListener?.wifiConnectFalid(WIFITYPE, emptyMsg)
                        } else if (t == "success") {
                            //找到WiFi，开始连接WiFi
                            conncetWithWpa(dstWifiSSID, dstWifiPW)
                        } else if (t == "continueSearch") {
                            if (count < 3) {  //没有搜索到指定的WiFi
                                //通知activity，正在搜索WiFi列表
                                mConnListener?.wifiConnectScaning(mContext?.getString(R.string.wifisearching)
                                        ?: "searching")
                                count++
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        LogHelper.e(TAG, "LogHelper e=${e.message}")
                    }


                })

    }

    //设置获取 sp 数据的key
    private fun setWifiNameAndPWKey(wifiType: String) = if (wifiType == WIFI_TYPE_SZB) {

        WIFISSIDKEY = SZB_WIFI_SSID_KEY
        WIFIPWKEY = SZB_WIFI_PASS_KEY
    } else {
        WIFISSIDKEY = LAN_WIFI_SSID_KEY
        WIFIPWKEY = LAN_WIFI_PASS_KEY
    }

    //获取WiFi的SSID
    private fun getWifiSSID(): String {
        return SPUtils.get(mContext, WIFISSIDKEY, "") as String
    }

    //获取WIFI的密码
    private fun getWifiPW(): String {
        return SPUtils.get(mContext, WIFIPWKEY, "12345678") as String
    }

    //搜索WiFi列表
    private fun getScanWifiResult(): Boolean {
        if (mWifiManager?.isWifiEnabled != false) {
            mWifiManager?.setWifiEnabled(true)
        }
        //27 是android8.1 28是android9.0 9.0就不再搜索WiFi列表了，直接连接
        if (Build.VERSION.SDK_INT > 27) {
            return true
        }
        mWifiManager?.startScan()
        val scanListResult = mWifiManager?.scanResults
        //如果循环中不要最后一个范围区间的值可以使用 until 函数:
        if (scanListResult?.size ?: 0 > 0) for (index in 0 until (scanListResult?.size ?: 0 - 1)) {

            if (scanListResult?.get(index)?.SSID?.equals(dstWifiSSID) == true) {
                return true
            }
        }
        return false
    }

    //检测当前手机WiFi的连接状态
    private fun wifiStated(): Boolean {
        val wifiInfo = mWifiManager?.getConnectionInfo()
        val networkInfo = mConnManager?.activeNetworkInfo
        val ssid = wifiInfo?.ssid

        if (ssid.equals("\"" + dstWifiSSID + "\"") && networkInfo?.isConnected ?: false) {
            return true
        }
        return false
    }


    //开始调用连接WiFi的接口
    private fun conncetWithWpa(wifiSSID: String, wifiPw: String) {

        mContext?.let {
            WifiUtils.withContext(it).enableWifi()
            builder = WifiUtils.withContext(it)
            builder?.let {
                it.connectWith(wifiSSID, wifiPw)
                        .setTimeout(tiemOut)
                        .onConnectionResult {
                            checkReslut(it)
                        }
                        .start()
            }

        }
    }

    //检查WiFi连接结果
    private fun checkReslut(isSuccess: Boolean) {
        if (isSuccess) {
            mConnListener?.wifiConnectSuccess(WIFITYPE)
        } else {
            mConnListener?.wifiConnectFalid(WIFITYPE, "Wifi连接失败")
        }

    }

    //测试网络是否能上网
    fun pingNet() {
        Observable.create(ObservableOnSubscribe<Int> {
            // M 23 Android6.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = mConnManager?.getNetworkCapabilities(mConnManager?.getActiveNetwork())
                val netResult = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                if (netResult == true) {
                    it.onNext(1)
                } else {
                    Thread.sleep(500)
                    it.onError(Throwable("-1"))
                }
            } else {//表示在Android6.0版本以下的手机上运行此程序
                it.onNext(2)
            }

        })
                .retry(4)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Int> {

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: Int) {
                        mPingListener?.pingSuccess()
                    }

                    override fun onError(e: Throwable) {
                        mPingListener?.pingFailed()
                    }

                })
    }

    fun disposable() {
        mDispose?.dispose()
    }


    //设置监听
    fun setWifiConnectListener(connListener: WifiConnectResultListener?, pingListener: WifiConnectResultListener.WifiPingResultListener?) {
        mConnListener = connListener
        mPingListener = pingListener
    }


    interface WifiConnectResultListener {

    fun wifiConnectSuccess(type: String)  //wifi 连接成功

    fun wifiConnectScaning(type: String)  //wifi 正在扫描

    fun wifiConnectFalid(type: String, errorMsg: String)  //wifi连接失败

    interface WifiPingResultListener {

        fun pingSuccess() //网络能上网

        fun pingFailed()   //网络不能上网

    }
}


}