package com.model;

/**
 * Created by gyl1 on 3/30/17.
 */

public class SnapShotConfigModel {
    public  int Action;//0 ： 拍一张 1 ：表示连续拍照
    public  int TotalTime;//时长
    public  int IntervalTime;//间隔时间
    public String toMString(){
        return "SnapShotConfigModel:Action:"+Action+",TotalTime:"+TotalTime+",IntervalTime:"+IntervalTime;
    }
}
