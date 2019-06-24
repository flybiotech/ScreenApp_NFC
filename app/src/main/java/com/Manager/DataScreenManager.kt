package com.Manager

import android.app.LauncherActivity
import android.content.ContentValues
import com.logger.LogHelper
import com.screening.model.DoctorId
import com.screening.model.FTPBean
import com.screening.model.ListMessage
import com.screening.model.Rets
import com.util.Constss.ERROR03
import org.litepal.LitePal

class DataScreenManager {


    //设置集合中的受检者状态
    fun insertListUserState(listMsgs: List<ListMessage>, origState: Int) {
        LogHelper.i("设置集合中的受检者状态 1 listMsgs=${listMsgs.size} ,origState=$origState")
        if (listMsgs != null && listMsgs.size > 0) {
            LogHelper.i("设置集合中的受检者状态 2 listMsgs=${listMsgs.size} ,origState=$origState")
            for ((index, msg) in listMsgs.withIndex()) {
                LogHelper.i("设置集合中的受检者状态 index=$index ,msg = $msg")
                msg.screenState = origState
                msg.upLoadIndex = index
                msg.save()
            }
        }
    }

    fun insertListUserJobId(origState: Int, jobId: Int) {

//        val listMsgs = searchUserByState(origState)
//        if (listMsgs!=null&&listMsgs.size>0) {
        //把ListMessage表中screenState为“origState”的所有jobId改成“jobId”
        val value = ContentValues()
        value.put("jobId", jobId)
        LitePal.updateAll(ListMessage::class.java, value, "screenState =? ", origState.toString())
//            for (msg in listMsgs) {
//                insertUserJobId(msg, jobId)
//            }
//        }
    }

    //从后台得到jobid之后，保存到数据库中
    fun insertListUserJobId(listMsgs: List<ListMessage>, jobId: Int) {
        if (listMsgs != null && listMsgs.size > 0 && jobId != -1) {
            for (msg in listMsgs) {
                insertUserJobId(msg, jobId)
            }
        }
    }



    //                    {
//                        "code": 1,
//                        "data": {
//                        "errCount": 1,
//                        "rets": [
//                        {
//                            "code": 1,
//                            "message": "操作成功",
//                            "screeningId": 1027280530
//                        },
//                        {
//                            "code": -1,
//                            "message": "该HPV试管码已被绑定过",
//                            "screeningId": 0
//                        },
//                        {
//                            "code": 1,
//                            "message": "操作成功",
//                            "screeningId": 1027280531
//                        }
//                        ],
//                        "status": 3,
//                        "sucCount": 2,
//                        "total": 3
//                    },
//                        "message": "操作成功"
//                    }
    //插入任务编号
    fun insertListUserTaskNum(origState: Int, targetState: Int, listRets: List<Rets>?) {

        val listMsgs = searchUserByState(origState)
        if (listMsgs != null && listMsgs.size > 0 && listRets != null) {
            val length = listRets.size
            for ((index, msg) in listMsgs.withIndex()) {

                if (index < length) {

                    if (listRets[msg.upLoadIndex].code == 1) {
                        listMsgs.get(index).taskNumber = listRets[msg.upLoadIndex].screeningId.toString()
                        listMsgs.get(index).screenState = targetState
                    } else { //任务编号赋值成-1
                        listMsgs.get(index).taskNumber ="-1"
                        listMsgs.get(index).errorMsg =  listRets[msg.upLoadIndex].message
                        listMsgs.get(index).screenState = 0
                    }


                }

            }

            LogHelper.i(" listMsgs.get(index).errorMsg=${ listMsgs.get(0).errorMsg}listMsgs.screeningId1=${listMsgs.get(0).screeningId},listRets.screeningId2=${listRets?.get(0)?.screeningId.toString()}")
            LitePal.saveAll(listMsgs)
        }

    }



    //设置受检者的状态
     fun insertUserJobId(msg: ListMessage, jobId: Int) {
        msg.jobId = jobId
        msg.save()
    }


    fun searchUserByState(screenState: Int): List<ListMessage> {
        val listMsgs = LitePal.where("screenState = ? ", screenState.toString()).find(ListMessage::class.java)
        return listMsgs
    }

     fun searchUserByState(screenState1: Int, screenState2: Int): List<ListMessage> {
        val listMsgs = LitePal.where("screenState = ? or screenState = ? ", screenState1.toString(), screenState2.toString()).find(ListMessage::class.java)
        return listMsgs
    }

     fun searchUserByCopy(screenState: Int, isCopy: String): List<ListMessage> {
        val listMsgs = LitePal.where("screenState = ? and isCopy = ?  ", screenState.toString(), isCopy).find(ListMessage::class.java)
        return listMsgs
    }

     fun searchUserByJobId(jobId: Int): List<ListMessage> {
        val listMsgs = LitePal.where("jobId = ? ", jobId.toString()).find(ListMessage::class.java)
        return listMsgs
    }


    fun searchUserByErrorTaskNumber(): List<ListMessage> {
        val listMsgs = LitePal.where("taskNumber = ? ", "-1").find(ListMessage::class.java)
        return listMsgs
    }



    /**
     * 查询doctorid
     */
    fun searchDoctorId(): String {
        val doctorIdList = LitePal.findAll(DoctorId::class.java)
        return if (doctorIdList.size > 0) {
            doctorIdList[0].doctorId
        } else {
            //            return "123";
            ""
        }
    }


    fun searchUserDataList(): List<ListMessage>{
         val listMsgs = LitePal.where("screenState  <6 ").find(ListMessage::class.java)
        return listMsgs
    }

}