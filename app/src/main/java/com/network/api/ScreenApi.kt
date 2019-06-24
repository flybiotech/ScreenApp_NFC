package com.network.api

import com.screening.model.FTPBean
import com.screening.model.JobsResultModel
import com.screening.model.ListMessageResultModel
import com.screening.model.ResultScreenModel
import io.reactivex.Observable
import okhttp3.RequestBody
import com.screening.model.User
import okhttp3.ResponseBody
import retrofit2.http.*


interface ScreenApi {
    //创建筛查，获取jobid
    @POST("lfy/api/lfys-hospital-screenings/batch-create")
    fun createScreen(@Body requestBody: RequestBody): Observable<ResultScreenModel>


    //获取任务编号
    @GET("lfy/api/batch-jobs/{jobid}")
    fun getTaskNumber(@Path("jobid")jobid:Int): Observable<JobsResultModel>

    //验证医生的id
    @GET("lfy/api/lfys-hospital-doctors/{docId}")
    fun verifyDocId(@Path("docId") docId:String):Observable<FTPBean>
//    http://flybiotech.com/fly_registration/public/index.php/api/upuser/updata
    //上传个人信息到后台  http://flybiotech.com/fly_registration/public/index.php/api/upuser/updata
    @POST("fly_registration/public/index.php/api/upuser/updata")
    fun uploadPatientData(@Body requestBody: RequestBody): Observable<ListMessageResultModel>


    //软件更新
    @GET("version.txt")
    fun getUpLoadAPP(): Observable<ResponseBody>

}