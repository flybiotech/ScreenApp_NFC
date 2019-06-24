package com.screening.uitls

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import com.Manager.DataScreenManager
import com.activity.R
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.logger.LogHelper
import com.network.ScreenNetWork
import com.screening.model.Bean
import com.screening.model.JobsResultModel
import com.screening.model.ListMessage
import com.screening.model.ListMessageResultModel
import com.screening.service.UpLoadService
import com.screening.service.Upload1Service
import com.util.Constss
import com.util.Constss.*
import com.util.FileUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import org.json.JSONObject

class CreateScrUtil {

    private var mContext: Context? = null
    //    private val oldJobId = "oldJobId"
    private var mNetWork: ScreenNetWork? = null
    private var mScreeingProcessListener: OnScreeningProcessListener? = null
    private var mDataManager: DataScreenManager? = null
    private var mFileUtil: FileUtil? = null
    private var doctorId = ""
    private var dispose: Disposable? = null
    private val gson = Gson()

    constructor(context: Context) {
        mContext = context
        mNetWork = ScreenNetWork()
        mDataManager = DataScreenManager()
        mFileUtil = FileUtil()
    }


    //开始创建筛查
    @SuppressLint("CheckResult")
    fun startCreateScreenings() {
        doctorId = mDataManager?.searchDoctorId() ?: "";
        if (doctorId.equals("")) {
            mScreeingProcessListener?.screeningProcessFile("doctorId", "doctorId 为空")
            return
        }

        var observable: Observable<Int>? = null
        val oldjobid = getOldJobId()
        LogHelper.i("oldjobid 1  =$oldjobid")
        if (oldjobid > 0) {//表示上一次筛查没有完成
            // state=2 表示已经准备获取jobid，但是没有获得任务编号的受检者
            LogHelper.i("oldjobid 2 =$oldjobid")
            mDataManager?.insertListUserJobId(2, oldjobid)

            observable = jobIdOldObservable(oldjobid)?.flatMap {
                taskNumberObservable(oldjobid)
            }?.flatMap {
                getHPVRepeatData()
            }?.flatMap {
                jobIdObservable()
            }?.flatMap {
                taskNumberObservable(it)
            }

        } else {
            LogHelper.i("oldjobid 3 =-1")
            observable = getHPVRepeatData()?.flatMap {
                jobIdObservable()
            }?.flatMap {
                taskNumberObservable(it)
            }
        }

        //订阅
        observable?.flatMap {
            reNameObservable(it)
        }?.flatMap {
            LogHelper.i("开始上传数据到京九的后台 it=$it")
            upLoadUserData()

        }?.flatMap {
            LogHelper.i("文件夹重命名和复制结束,接下来开始上传文件： it=$it")
            upLoadFileObservable()

        }?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(object : Observer<String> {
            override fun onComplete() {
                LogHelper.i("已经完成了")
            }

            override fun onSubscribe(d: Disposable) {
                dispose = d
            }

            override fun onNext(t: String) {
                mScreeingProcessListener?.screeningProcessSuccess("updataFile", "正在上传文件")
                LogHelper.i("正在上传文件 t=$t")
            }

            override fun onError(e: Throwable) {
                LogHelper.i("上传数据出现异常情况e = ${e.message},e.stackTrace=${e.stackTrace.iterator()}")
                mScreeingProcessListener?.screeningProcessFile("${e.message} ", "${e.cause?.message}")

            }
        })
    }

    //搜索数据库中HPV重复的受检者信息
    @SuppressLint("CheckResult")
    private fun getHPVRepeatData(): Observable<String> {
        return Observable.create(ObservableOnSubscribe<String> {
            val listMsgs = mDataManager?.searchUserByErrorTaskNumber()
            if (listMsgs != null) {
                LogHelper.i("hpv重复的人数= ${listMsgs.size}")
                for (temp in listMsgs) {
                    if (temp.errorMsg.contains("HPV")) {
                        temp.hpv = modifyHPVContext(temp.hpv, "admin")
                    } else if (temp.errorMsg.contains("TCT")) {
                        temp.cytology = modifyHPVContext(temp.cytology, "admin")
                    } else if (temp.errorMsg.contains("基因检测")) {
                        temp.gene = modifyHPVContext(temp.gene, "admin")
                    } else if (temp.errorMsg.contains("DNA倍体")) {
                        temp.dna = modifyHPVContext(temp.dna, "admin")
                    }
                    temp.jobId = 0
                    temp.taskNumber = ""
                    temp.screenState = 1
                    temp.errorMsg = ""
                    temp.save()
                }
            }

            it.onNext("")
        })
    }

    /**
     * ruleContent :修改规则
     */
    private fun modifyHPVContext(origContent: String, ruleContent: String): String {
        val targetContent = ruleContent + origContent
        return targetContent
    }


    //查看本地有没有保存上次创建筛查时的oldJobId
    private fun getOldJobId(): Int {
        return SPUtils.get(mContext, OldJobId, -1) as? Int ?: -1
    }


    private fun jobIdOldObservable(jobId: Int): Observable<Int>? {

        return Observable.create(ObservableOnSubscribe<Int> {
            mDataManager?.insertListUserJobId(2, jobId)
            LogHelper.i("得到jobid的内容 jobId=$jobId")
            it.onNext(jobId)
        })
    }

    private fun jobIdObservable(): Observable<Int>? {
        var observable: Observable<Int>? = null
        observable = Observable.create(ObservableOnSubscribe<List<ListMessage>?> {
            val listMsgs = mDataManager?.searchUserByState(1, 2)
            LogHelper.i("搜索受检者，准备进行获取jobid listMsgs =${listMsgs?.size}")
            mDataManager?.insertListUserState(listMsgs ?: ArrayList<ListMessage>(), 2)
            it.onNext(listMsgs ?: ArrayList<ListMessage>())


        }).flatMap { its ->
            if (its.size == 0) {
                LogHelper.i("需要获取jobid的受检者数量为0，直接进行文件的重命名与复制")
                Observable.just(-1)
            } else {
                val jsonObj = getListMessageJson(its)
                mNetWork?.getScreenApi(1)?.createScreen(getRequestBody(jsonObj.toString()))?.retry(4)?.flatMap { it ->
                    SPUtils.put(mContext, OldJobId, it.jobId)//将jobid保存到本地
                    mDataManager?.insertListUserJobId(2, it.jobId)
//                    mDataManager?.insertListUserJobId(its, it.jobId)
                    LogHelper.i("得到jobid的内容 it.code=${it.code} ,it.message=${it.message} ,it.jobid=${it.jobId}")
                    Observable.just(it.jobId)
                }
            }
        }
        return observable
    }

    //将有openid的和无openid的受检者按照顺序放在一起
    private fun getListMessageJson(listMsgs: List<ListMessage>): JsonObject {
        val jsonArr = JsonArray()
        val allJsonObj = JsonObject()
        for (temp in listMsgs) {
            val jsonObj = JsonObject()
            if (!temp.idCard.equals(temp.screeningId)) {//有openid 的
                jsonObj.addProperty("wechatOpenId", temp.screeningId)
            } else {
                jsonObj.addProperty("name", temp.name)
                jsonObj.addProperty("mobile", temp.phone)
                jsonObj.addProperty("age", temp.age)
                jsonObj.addProperty("idCard", temp.idCard)
            }
            jsonObj.addProperty("doctorId", doctorId)
            jsonObj.addProperty("hpvNumber", temp.hpv)
            jsonObj.addProperty("tctNumber", temp.cytology)
            jsonObj.addProperty("dnaNumber", temp.dna)
            jsonObj.addProperty("geneNumber", temp.gene)
            jsonObj.addProperty("otherNumber", temp.other)
            jsonArr.add(jsonObj)
        }
        allJsonObj.add("data", jsonArr)
        return allJsonObj
    }


    //创建RequestBody
    private fun  getRequestBody(json: String): RequestBody {
        val mediaType = MediaType.parse("application/json;charset=utf-8")
        return RequestBody.create(mediaType, json)
    }

    //获取任务编号 的Observable
    @SuppressLint("CheckResult")
    private fun taskNumberObservable(jobId: Int): Observable<Int>? {
        if (jobId == -1) {
            return Observable.just(3)
        } else {
            return mNetWork?.getScreenApi(1)?.getTaskNumber(jobId)?.flatMap {

                val status = it.data?.status

                if (status == 3) {

                    val listRets = it.data?.rets
                    //将任务编号保存导数据库，并且设置受检者的状态为3
                    mDataManager?.insertListUserTaskNum(2, 3, listRets)
                    SPUtils.put(mContext, OldJobId, -1)//将本地的jobid清空
                    LogHelper.i("任务编号的内容 JobsResultModel =${gson.toJson(it)}")
                    Observable.just(3)
                } else {
                    LogHelper.i("得到任务编号 status正在创建 status=$status")
                    Thread.sleep(1000)
                    Observable.error<Int>(Throwable("taskNmuber", Throwable("")))
                }
            }?.retry(50)
        }

    }


    @SuppressLint("CheckResult")
    fun reNameObservable(state: Int): Observable<String>? {
        return Observable.create {
            //state=3
            val listMsgs = mDataManager?.searchUserByState(state)
            LogHelper.i("搜索重命名的受检者listMsgs.size=${listMsgs?.size ?: -1},state=$state")
            if (listMsgs != null && listMsgs.size > 0) {
                for (msg in listMsgs) {

                    if (msg.taskNumber.equals("-1") || msg.taskNumber.equals("0")) {
                        continue
                    }

                    //文件重命名
                    val result = mFileUtil?.reNameFile(msg.idCard, msg.taskNumber)
                    LogHelper.i("文件重命名结果result=$result,msg.screeningId=${msg.idCard} ,msg.taskNumber=${msg.taskNumber}")

                    if (result.equals("1")) {
                        msg.screenState = 4
                        msg.errorMsg = ""
                        msg.save()
                        //文件夹复制
                        mFileUtil?.copyDirectory(Bean.bePath + msg.taskNumber, Bean.endPath + msg.taskNumber)
                        picCopy(Bean.endPath + msg.taskNumber)

                        LogHelper.i("文件重命名结果result=${picCopy(Bean.endPath + msg.taskNumber)}")
                    } else {
                        //文件夹重命名失败
                        msg.screenState = 3
                        msg.errorMsg = getReNameErrorMsg(result)
                        msg.save()

                        //将失败的图片复制到特定路径下：ScreenAppError
                        var copyResult = CopyUtils.copy(Bean.bePath + msg.idCard , Bean.errorPath + msg.idCard)
                        LogHelper.i("重命名失败文件复制result=$copyResult")

                    }
                }
                //文件及文件夹重命名成功并且复制也成功了
                it.onNext("Folder named successfully")
            } else {
                it.onNext("Folder reNamed is empty")
            }

        }
    }


    private fun getReNameErrorMsg(errorCode: String?): String {
        var error = ERROR08
        when (errorCode) {
            "-2" -> error = "$ERROR08:身份证为空 "
            "-3" -> error = "$ERROR08:任务编号为空 "
            "-4" -> error = "$ERROR08:原始图片文件夹不存在"
            "-5" -> error = "$ERROR08:图片文件夹已被重命名"
        }

        return error
    }

    /**
     * 如果本地文件夹为空，则添加一张空白的图片
     */
    private fun picCopy(path: String): Boolean {
        Log.e("delete0614", ",,任务路径 : $path")
        val res = mContext?.getResources()
        val blankPath = "$path/原图/blank.png"
        val file = File(path)
        var fileSize = ""
        if (file.exists()) {
            fileSize = FileUtils.getAutoFilesSize(path)
        }
        if (fileSize == "") {
            fileSize = "0"
        }
        val size = fileSize.toFloat()
        if (size == 0f) {
            val blank = res?.getDrawable(R.drawable.blank) as BitmapDrawable//app中自带的空白的图片

            return initCopyCurrency(blank, blankPath)
        } else {
            return true
        }
    }

    private fun initCopyCurrency(bitmapDrawable: BitmapDrawable, Path: String): Boolean {
        val bitmap = bitmapDrawable.bitmap
        try {
            val os = FileOutputStream(Path)//输出流
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)//设置图片的大小
            os.close()//关闭流
            return true
        } catch (e: Exception) {
            return false
        }

    }

    //上传数据到自己的后台
    @SuppressLint("CheckResult")
    fun upLoadUserData(): Observable<String>? {
        return Observable.create(ObservableOnSubscribe<String> {
            val strJosnMsgs = generateData()
            LogHelper.i("上传到京九的数据strJosnMsgs=${strJosnMsgs} ")
            it.onNext(strJosnMsgs)
        }).flatMap {
            mNetWork?.getScreenApi(2)?.uploadPatientData(getRequestBody(it))
        }?.flatMap {
            LogHelper.i("从后台的返回值京九 = ${gson.toJson(it)}")
            Observable.just("")
        }
    }


    //上传文件
    @SuppressLint("CheckResult")
    fun upLoadFileObservable(): Observable<String> {
        return Observable.create {
            //            UploadpatientUtils.generateData();
            val intent = Intent(mContext, UpLoadService::class.java)
            mContext?.startService(intent)
            it.onNext("开始上传文件")

        }
    }


    /**
     * 生成上传的json格式的数据
     */
    private fun generateData(): String {
        val openUser = mDataManager?.searchUserDataList()

        var jsonObject = JsonObject();
        var array = JsonArray();
        openUser?.forEach {
            var arr_1 = JsonObject();
            arr_1.addProperty("pId", it.getpId());
            arr_1.addProperty("name", it.getName());
            arr_1.addProperty("sex", it.getSex());
            arr_1.addProperty("age", it.getAge());
            arr_1.addProperty("phone", it.getPhone());
            arr_1.addProperty("fixedtelephone", it.getFixedtelephone());
            arr_1.addProperty("dataofbirth", it.getDataofbirth());
            var time = DateUtils.ms2DateOnlyDay(it.getDayTime());
            arr_1.addProperty("dayTime", time);
            arr_1.addProperty("screeningId", it.getScreeningId());
            arr_1.addProperty("taskNumber", it.getTaskNumber());
            arr_1.addProperty("occupation", it.getOccupation());
            arr_1.addProperty("smoking", it.getSmoking());
            arr_1.addProperty("sexualpartners", it.getSexualpartners());
            arr_1.addProperty("gestationaltimes", it.getGestationaltimes());
            arr_1.addProperty("abortion", it.getAbortion());
            arr_1.addProperty("idCard", it.getIdCard());
            arr_1.addProperty("hpv", it.getHpv());
            arr_1.addProperty("cytology", it.getCytology());
            arr_1.addProperty("gene", it.getGene());
            arr_1.addProperty("dna", it.getDna());
            arr_1.addProperty("other", it.getOther());
            arr_1.addProperty("detailedaddress", it.getDetailedaddress());
            arr_1.addProperty("remarks", it.getRemarks());
            arr_1.addProperty("marry", it.getMarry());
            arr_1.addProperty("contraception", it.getContraception());
            arr_1.addProperty("jobId", it.getJobId());
            arr_1.addProperty("screenState", it.getScreenState());
            array.add(arr_1);
        }
        jsonObject.add("data", array);
        return jsonObject.toString()
    }


    fun setOnScreeningProcessListener(listener: OnScreeningProcessListener) {
        mScreeingProcessListener = listener
    }

    interface OnScreeningProcessListener {

        fun screeningProcessFile(type: String, errorMsg: String)

        fun screeningProcessSuccess(type: String, processMsg: String)

    }

}