package com.screening.model;

import org.litepal.crud.LitePalSupport;

/**
 * 上传数据时出现错误信息
 */
public class ScreenErrorList extends LitePalSupport {
    private String pId;//序号
    private String name;//姓名
    private String screeningId;//患者每次筛查的id,也是存放图像的目录
    private String errorMsg;//错误信息

    private String fromFile;//复制的原始路径
    private String toFile;//目标路径


    public String getFromFile() {
        return fromFile;
    }

    public void setFromFile(String fromFile) {
        this.fromFile = fromFile;
    }

    public String getToFile() {
        return toFile;
    }

    public void setToFile(String toFile) {
        this.toFile = toFile;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(String screeningId) {
        this.screeningId = screeningId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
