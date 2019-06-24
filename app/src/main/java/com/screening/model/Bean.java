package com.screening.model;

import android.os.Environment;
import android.widget.CheckBox;

public class Bean {
    public static final String bePath = Environment.getExternalStorageDirectory() + "/ScreenApp/图片和视频/";

    public static final String bePathBack = Environment.getExternalStorageDirectory() + "/ScreenApp/";

    public static final String endPath = Environment.getExternalStorageDirectory() + "/ScreenAppCopy/图片和视频/";//文件夹重命名后复制到此路径下，上传也是此路径

    public static final String uploadPath = Environment.getExternalStorageDirectory() + "/ScreenAppCopyUpload/";//上传后的图片路径

    public static final String bePathZip = Environment.getExternalStorageDirectory() + "/ScreenAppOriginal/";

    public static final String sdZipPath = Environment.getExternalStorageDirectory() + "/";

    public static final String errorPath = Environment.getExternalStorageDirectory() + "/ScreenAppError/图片和视频/";//重命名失败文件的保存路径

    private Integer img;
    private String fileName;//文件名称
    private CheckBox checkBox;
    private String filePath;//文件路径

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

}
