package com.network

import com.google.gson.Gson
import com.network.api.ScreenApi
import com.screening.uitls.Constant
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ScreenNetWork {

    private var mScreenApi: ScreenApi? = null
    private var mClient: OkHttpClient? = null
    private var mGson: Gson? = null
    private var baseurl="https://wechat.flybiotech.cn/"
//    private var baseurl1="http://flybiotech.com/"

     constructor(){
        mGson= Gson()
        val basic = Credentials.basic(Constant.MD5_USERNAME, Constant.MD5_PASSWORD)

        mClient = OkHttpClient.Builder()
                .authenticator { route: Route, response: Response ->
                    response.request().newBuilder()
                            .header("Authorization", basic)
                            .build()
                }
                .connectTimeout(2, TimeUnit.SECONDS)
                .build()

    }

    var mRetrofit:Retrofit.Builder?=null
     fun getScreenApi(baseurlType:Int) :ScreenApi?{
         if (baseurlType == 1) {//微信端
             baseurl = "https://wechat.flybiotech.cn/"
         } else if (baseurlType == 2){//后台端
             baseurl = "http://flybiotech.com/"
         }else if (baseurlType == 3) {
             baseurl = "http://flybiotech.w231.mc-test.com/fly_user/"
         }
         if (mRetrofit == null) {
              mRetrofit = Retrofit.Builder()
                     .client(mClient)
                     .baseUrl(baseurl)
                     .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                     .addConverterFactory(GsonConverterFactory.create(mGson))
         } else {
             mRetrofit?.baseUrl(baseurl)
         }
         mScreenApi=mRetrofit?.build()?.create(ScreenApi::class.java)
        return mScreenApi
    }



}