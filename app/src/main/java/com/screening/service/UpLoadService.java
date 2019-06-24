package com.screening.service;


import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.activity.R;
import com.logger.LogHelper;
import com.screening.activity.AdminSetActivity;
import com.screening.model.Bean;
import com.screening.model.FTPBean;
import com.screening.model.FTPPath;
import com.screening.model.ListMessage;
import com.screening.uitls.CopyUtils;
import com.screening.uitls.Deleteutils;
import com.screening.uitls.FileUtils;
import com.screening.uitls.LogWriteUtils;
import com.util.Constss;
import com.zhy.http.okhttp.callback.Callback;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.litepal.LitePal;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UpLoadService extends Service {
    private final FTPClient ftpClient;//FTP连接

    private String strIp = "118.25.70.83";//ip地址

    private int intPort = 21;//端口号

    private String user;//用户名

    private String password;//用户密码

    private long localAllSize, ftpAllSize;

    Deleteutils deleteutils = new Deleteutils();

    public UpLoadService() {
        ftpClient = new FTPClient();
        ftpClient.setDataTimeout(5000);
        ftpClient.setConnectTimeout(5000);

        List<FTPBean> ftpBeanList = LitePal.findAll(FTPBean.class);
        if (ftpBeanList.size() > 0) {
            user = ftpBeanList.get(0).getFTPName();
            password = ftpBeanList.get(0).getFTPPassword();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogHelper.i("UpLoadService服务开始启动");
        new uploadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        return super.onStartCommand(intent, flags, startId);
    }

    private class uploadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String path = "";
            path = Bean.endPath;
            File file = new File(path);//某次筛查的全部文件，即根目录，如100
            if (file.exists()) {
                int temp = deleteutils.fileIsNull(file);
                if (temp == 1) {
                    boolean isSuccess = ftpLogin();
                    if (isSuccess) {
                        //得到本地文件的大小
                        upLoadDir(path, "/");
                        boolean uploadResult = getFileNumber(path);
                        ftpLogOut();
                        ftpAllSize = 0;
                        localAllSize = 0;
                        stopSelf();
                    } else {
                        stopSelf();
                    }
                } else {
                    if (upLoadFileProcess != null) {
                        upLoadFileProcess.getUpLoadFileProcessPrecent(100);
                    }
                }

            } else {
                LogHelper.e("文件不存在 path=" + path);
                if (upLoadFileProcess != null) {
                    upLoadFileProcess.getUpLoadFileProcessPrecent(100);
                }
            }
            stopSelf();
            return null;
        }

    }

    /**
     * 搜索上传文件夹中的以任务编号命名的文件夹数量，作为每次上传的路径
     */
    private boolean getFileNumber(String path) {
        String remoteDirectoryPath = "/图片和视频/";
        //创建服务器根路径，防止服务器无该路径造成数据上传失败
        initCreateFtpFile(remoteDirectoryPath);
        boolean uploadResult = false;
        File file = new File(path);
        File[] files = file.listFiles();
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                uploadResult = uploadDirectory(files[i].getAbsolutePath(), remoteDirectoryPath);
                if (uploadResult) {
                    Deleteutils.deleteLocal(files[i]);
                    setScreenState(files[i].getName());
                } else {
                    ftpLogOut();
                    stopSelf();
                    if (upLoadFileProcess != null) {
                        upLoadFileProcess.loginOut(false);
                    }
                    break;
                }
            }
        }
        return uploadResult;
    }

    /**
     * 上传成功后开始改变状态
     */
    private void setScreenState(String tasknumber) {
        ContentValues values = new ContentValues();
        values.put("screenState", 5);
        LitePal.updateAll(ListMessage.class, values, "taskNumber = ?", tasknumber);
    }

    /***
     * @上传文件夹
     * @param localDirectory
     *   当地文件夹
     * @param remoteDirectoryPath
     *   Ftp 服务器路径 以目录"/"结束
     * */
    boolean makeDirFlag = false;

    public boolean uploadDirectory(String localDirectory, String remoteDirectoryPath) {
        File src = new File(localDirectory);
        try {
            remoteDirectoryPath = remoteDirectoryPath + src.getName() + "/";
            Log.i("TAG_1_ftp", "uploadDirectory: src.getName() = " + src.getName() + ",localDirectory = " + localDirectory + " remoteDirectoryPath =" + remoteDirectoryPath);
            //创建服务器文件
            initCreateFtpFile(remoteDirectoryPath);
        } catch (Exception e) {
            e.printStackTrace();
            ftpLogOut();
            stopSelf();
            if (upLoadFileProcess != null) {
                upLoadFileProcess.loginOut(false);
            }
            LogHelper.e("出现异常e.msg=" + e.getMessage());
        }
        if (makeDirFlag) {
            //listFiles是获取该目录下所有文件和目录的绝对路径
            File[] allFile = src.listFiles();
            if (allFile == null) {
                return makeDirFlag;
            }
            if (allFile.length > 0)
                for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
                    if (!allFile[currentFile].isDirectory()) {
                        String srcName = allFile[currentFile].getPath().toString();
                        makeDirFlag = uploadFile(new File(srcName), remoteDirectoryPath);
                        if (!makeDirFlag) {
                            return false;
                        }
                    }
                }
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
                if (allFile[currentFile].isDirectory()) {
                    // 递归
                    uploadDirectory(allFile[currentFile].getPath().toString(),
                            remoteDirectoryPath);
                }
            }
            return makeDirFlag;
        } else {
            ftpLogOut();
            return makeDirFlag;
        }
    }

    /***
     * 上传Ftp文件
     * @param localFile 当地文件
     * @param
     * */
    int num = 0;

    public boolean uploadFile(File localFile, String romotUpLoadePath) {

        LogHelper.i("uploadFile: localFile = " + localFile + " , localFile.getName()= " + localFile.getName() + " , romotUpLoadePath =" + romotUpLoadePath);

        BufferedInputStream inStream = null;
        boolean success = false;
        try {
            this.ftpClient.changeWorkingDirectory(romotUpLoadePath);// 改变工作路径
            inStream = new BufferedInputStream(new FileInputStream(localFile));
            LogHelper.e(localFile.getName() + "开始上传....." + inStream + "......" + this.ftpClient);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setSoTimeout(5000);
            success = ftpClient.storeFile(localFile.getName(), inStream);
            //上传FTP文件中...,结果 success=true
            LogHelper.d("上传FTP文件中...,结果 success=" + success);
            //文件上传成功
            if (success == true) {
                ftpClient.sendCommand("pwd");
                num = 0;
                //判断是否上传完整，有时断网后ftpSuccess也为true
                boolean isComplete = initSize(romotUpLoadePath, localFile);
                //如果本地上传的图片和服务器上已上传的文件的大小一致，则说明该文件已完整上传至ftp
                if (isComplete) {
                    double percent = ((double) ftpAllSize / localAllSize) * 100;
                    if (upLoadFileProcess != null) {
                        upLoadFileProcess.getUpLoadFileProcessPrecent(percent);
                    }
                    initDelete(localFile.getAbsolutePath());
                    if (percent >= 100) {
                        Deleteutils.deleteLocal(new File(Bean.endPath));
                    }
                    return success;
                }
                return success;
            } else {
                //文件上传失败后，再次进行上传，最多上传4次
                num++;
                if (num <= 4) {
                    uploadFile(localFile, romotUpLoadePath);
                } else {
                    LogHelper.e("上传失败图片...,图片路径path=" + localFile.getAbsolutePath());
                    ftpLogOut();
                    stopSelf();
                    if (upLoadFileProcess != null) {
                        upLoadFileProcess.loginOut(false);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //先断开连接
            LogHelper.e("上传FTP图片出现异常2 e.msg=" + e.getMessage() + " , 图片路径" + localFile.getAbsolutePath());
            ftpLogOut();
            stopSelf();
            if (upLoadFileProcess != null) {
                upLoadFileProcess.loginOut(false);
            }

        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                }
            }
        }
        return success;
    }

    //文件复制成功后删除原始文件
    private void initDelete(String path) {
        new File(path).delete();
    }

    //创建服务器文件
    private void initCreateFtpFile(String remoteDirectoryPath) {
        //遍历该服务器某级目录下所有文件的集合
        FTPFile[] ftpFiles = new FTPFile[0];
        try {
            ftpClient.enterLocalPassiveMode();
            ftpFiles = ftpClient.listFiles(remoteDirectoryPath);
            if (ftpFiles.length == 0) {
                makeDirFlag = this.ftpClient.makeDirectory(remoteDirectoryPath);//在ftp服务器上创建文件
            } else {
                makeDirFlag = true;
            }
            if (!makeDirFlag) {
                if (num <= 3) {
                    initCreateFtpFile(remoteDirectoryPath);
                }
            }
            //创建服务器文件 remoteDirectoryPath=/图片和视频/ ,结果makeDirFlag=true
            LogHelper.d("创建服务器文件 remoteDirectoryPath=" + remoteDirectoryPath + " ,结果makeDirFlag=" + makeDirFlag);
        } catch (IOException e) {
            e.printStackTrace();
            LogHelper.e("创建服务器文件出现异常 文件夹路径=" + remoteDirectoryPath + " e.msg=" + e.getMessage() + "");
        }

    }

    //得到服务器文件大小，刚刚上传成功的,如果上传成功，但是该文件的大小没有计入总大小，则重复进行计算
    private boolean initSize(String path, File localFile) {
        FTPFile[] ftpFiles = new FTPFile[0];
        try {
            ftpClient.enterLocalPassiveMode();
            ftpFiles = ftpClient.listFiles(path + localFile.getName());
            if (ftpFiles.length > 0) {
                if (ftpFiles[0].getSize() == localFile.length()) {
                    ftpAllSize += ftpFiles[0].getSize();
                    LogHelper.d(localFile.getName() + " 上传成功 文件size=" + ftpAllSize + "|||" + ftpFiles[0].getSize());
                    return true;
                }
            } else {
                if (num <= 3) {
                    initSize(path, localFile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogHelper.e("得到服务器文件小出现异常 e.msg=" + e.getMessage());
        }
        return false;
    }

    /**
     * @退出关闭服务器链接
     */
    public void ftpLogOut() {
        if (null != this.ftpClient && this.ftpClient.isConnected()) {
            try {
                //退出ftp服务器
                boolean outResult = this.ftpClient.logout();
                // 成功退出服务器 outResult=true
                LogHelper.d("成功退出服务器 outResult=" + outResult);
            } catch (IOException e) {
                LogHelper.e("退出FTP服务器异常 error.msg=" + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    this.ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    LogHelper.e("退出FTP服务器异常 error.msg=" + e.getMessage());
                }
            }
        }
    }

    /**
     * @return 判断是否登入成功
     */
    public boolean ftpLogin() {
        boolean isLogin = false;
        FTPClientConfig ftpClientConfig = new FTPClientConfig();
        ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
        ftpClient.setControlEncoding("utf-8");
        ftpClient.configure(ftpClientConfig);
        try {
            if (this.intPort > 0) {
                ftpClient.connect(this.strIp, this.intPort);
            } else {
                ftpClient.connect(this.strIp);
            }
            // FTP服务器连接回答
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                LogHelper.e("登录FTP服务失败");
                return isLogin;
            }
            isLogin = ftpClient.login(this.user, this.password);
            // 设置传输协议
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            //成功登陆FTP服务器 isLogin=true
            LogHelper.d("成功登陆FTP服务器 isLogin=" + isLogin);
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.e("登录FTP服务失败 isLogin=" + isLogin + e.getMessage());
        }
        ftpClient.setBufferSize(1024 * 2);
        ftpClient.setDataTimeout(5 * 1000);
        return isLogin;
    }

    //得到本地文件的大小
    public void upLoadDir(String localDir, String remoteDir) {
        File file = new File(localDir);
        if (file.exists()) {
            localAllSize = getSize(file.listFiles());
        }
    }

    /**
     * 本地文件大小
     */
    public long getSize(File[] localFile) {
        for (int i = 0; i < localFile.length; i++) {
            if (localFile[i].isFile()) {
                localAllSize += localFile[i].length();
            } else {
                getSize(localFile[i].listFiles());
            }
        }
        return localAllSize;
    }

    public interface UpLoadFileProcess {

        void getUpLoadFileProcessPrecent(double percent);

        //        void setRestartService();
        void loginOut(boolean outResult);
    }

    static UpLoadFileProcess upLoadFileProcess;

    public static void setUpLoadFileProcessListener(UpLoadFileProcess upLoadFileProcessListener) {
        upLoadFileProcess = upLoadFileProcessListener;
    }

    public static void setUpLoadFileProcessNull() {
        upLoadFileProcess = null;
    }
}
