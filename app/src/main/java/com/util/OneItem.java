package com.util;

/**
 * Created by dell on 2018/4/23.
 */

public class OneItem {

    private static OneItem oneItem;
    private String adminName;//超级用户名称
    private int spTotalTime;//定时 设置醋酸白状态下，连续拍照的总时间
    private int spInterval;// 设置醋酸白状态下，连续拍照的间隔时间
    private int recordTotalTime;// 设置录制视频的时间

    public static OneItem getOneItem(){
        if(oneItem==null){
            synchronized (OneItem.class){
                if(oneItem==null){
                    oneItem=new OneItem();
                }
            }
        }
        return oneItem;
    }


    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public int getSpTotalTime() {
        return spTotalTime;
    }

    public void setSpTotalTime(int spTotalTime) {
        this.spTotalTime = spTotalTime;
    }

    public int getSpInterval() {
        return spInterval;
    }

    public void setSpInterval(int spInterval) {
        this.spInterval = spInterval;
    }

    public int getRecordTotalTime() {
        return recordTotalTime;
    }

    public void setRecordTotalTime(int recordTotalTime) {
        this.recordTotalTime = recordTotalTime;
    }
}
