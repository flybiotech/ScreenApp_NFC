package com.screening.model;

import org.litepal.crud.LitePalSupport;

/**
 * 保存FTP账号和密码的
 */
public class FTPBean extends LitePalSupport {
    private String FTPName;//ftp服务器账号
    private String FTPPassword;//ftp服务器密码
    /**
     * data : {"ftpUsername":"v2hTLf","ftpPassword":"JTc47VCTtuXR"}
     * code : 1
     * message : 操作成功
     */

    private DataBean data;
    private int code;
    private String message;

    public String getFTPName() {
        return FTPName;
    }

    public void setFTPName(String FTPName) {
        this.FTPName = FTPName;
    }

    public String getFTPPassword() {
        return FTPPassword;
    }

    public void setFTPPassword(String FTPPassword) {
        this.FTPPassword = FTPPassword;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public static class DataBean {
        /**
         * ftpUsername : v2hTLf
         * ftpPassword : JTc47VCTtuXR
         */

        private String ftpUsername;
        private String ftpPassword;

        public String getFtpUsername() {
            return ftpUsername;
        }

        public void setFtpUsername(String ftpUsername) {
            this.ftpUsername = ftpUsername;
        }

        public String getFtpPassword() {
            return ftpPassword;
        }

        public void setFtpPassword(String ftpPassword) {
            this.ftpPassword = ftpPassword;
        }
    }
}
