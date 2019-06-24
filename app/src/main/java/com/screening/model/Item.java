package com.screening.model;

import android.content.Context;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.List;

/**
 * Created by zhangbin on 2018/4/27.
 */

public class Item {
    private List<ListMessage> listMessages;
    private static Item oneItem;
    private String province, city, area, marry, birthControlMode, ScanTime, LoginTime;
    private int hpv, tct, gene;//判断需要扫描几次和扫描内容，0不需要，1需要
    private String screeningId;//暂时保存患者的筛查ID
    private String DNA, other;
    private int progress;//上传进度
    private ProgressBar progressBar;
    private TextView tv_progress;
    private TextView tv_progress1;
    private Button bt_return;
    private Context mContext;

    private String sdRootPath;//SD卡根路径
    private String sdBackPath;//SD卡每次备份的路径

    public String getSdRootPath() {
        return sdRootPath;
    }

    public void setSdRootPath(String sdRootPath) {
        this.sdRootPath = sdRootPath;
    }

    public String getSdBackPath() {
        return sdBackPath;
    }

    public void setSdBackPath(String sdBackPath) {
        this.sdBackPath = sdBackPath;
    }

    private Item(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public TextView getTv_progress1() {
        return tv_progress1;
    }

    public void setTv_progress1(TextView tv_progress1) {
        this.tv_progress1 = tv_progress1;
    }

    public Button getBt_return() {
        return bt_return;
    }

    public void setBt_return(Button bt_return) {
        this.bt_return = bt_return;
    }

    public TextView getTv_progress() {
        return tv_progress;
    }

    public void setTv_progress(TextView tv_progress) {
        this.tv_progress = tv_progress;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getDNA() {
        return DNA;
    }

    public void setDNA(String DNA) {
        this.DNA = DNA;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(String screeningId) {
        this.screeningId = screeningId;
    }

    public String getLoginTime() {
        return LoginTime;
    }

    public void setLoginTime(String loginTime) {
        LoginTime = loginTime;
    }

    public String getScanTime() {
        return ScanTime;
    }

    public void setScanTime(String scanTime) {
        ScanTime = scanTime;
    }

    public int getHpv() {
        return hpv;
    }

    public void setHpv(int hpv) {
        this.hpv = hpv;
    }

    public int getTct() {
        return tct;
    }

    public void setTct(int tct) {
        this.tct = tct;
    }

    public int getGene() {
        return gene;
    }

    public void setGene(int gene) {
        this.gene = gene;
    }

    public String getMarry() {
        return marry;
    }

    public void setMarry(String marry) {
        this.marry = marry;
    }

    public String getBirthControlMode() {
        return birthControlMode;
    }

    public void setBirthControlMode(String birthControlMode) {
        this.birthControlMode = birthControlMode;
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<ListMessage> getListMessages() {
        return listMessages;
    }

    public void setListMessages(List<ListMessage> listMessages) {
        this.listMessages = listMessages;
    }

    public static Item getOneItem(Context context) {
        if (oneItem == null) {
            synchronized (Item.class) {
                if (oneItem == null) {
                    oneItem = new Item(context.getApplicationContext());
                }
            }
        }
        return oneItem;
    }
}
