package com.screening.model;


import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * Created by zhangbin on 2018/5/21.
 * FTP的路径
 */

public class FTPPath extends LitePalSupport {
    private String ftpPath;

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }
}
