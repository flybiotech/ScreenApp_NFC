package com.screening.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.logger.LogHelper;
import com.screening.model.Bean;
import com.screening.model.FTPBean;
import com.screening.model.ListMessage;
import com.screening.uitls.Constant;
import com.screening.uitls.Deleteutils;
import com.screening.uitls.FileUtils;
import com.screening.uitls.LogWriteUtils;
import com.screening.uitls.SPUtils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.litepal.LitePal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

import static com.screening.service.UpLoadService.upLoadFileProcess;

public class Upload1Service extends Service {
    private FTPClient ftpClient;
    //ip地址
    private String ftpIp = "118.25.70.83";
    //端口号
    private int ftpPort = 21;
    //用户名
    private String ftpUser;
    //用户密码
    private String ftpPassword;
    //本地文件大小
    private long localFileSize;
    //已上传到ftp的文件大小
    private long ftpFileSize;
    Deleteutils deleteutils = new Deleteutils();
    //创建服务器文件的结果
    boolean makeDirFlag = false;
    //上传文件的次数
    int num = 0;


    /**
     * 初始化参数，包括连接超时时间和FTP账号密码
     */
    public Upload1Service() {
        ftpClient = new FTPClient();
        //数据传输超时时间
        ftpClient.setDataTimeout(5000);
        //连接超时时间
        ftpClient.setConnectTimeout(5000);
        //数据库查询得到账户密码
        List<FTPBean> ftpBeans = LitePal.findAll(FTPBean.class);
        if (ftpBeans.size() > 0) {
            ftpUser = ftpBeans.get(0).getFTPName();
            ftpPassword = ftpBeans.get(0).getFTPPassword();
        }
//        //得到本地缓存的文件大小
//        getFileSize();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 开始启动服务
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new UploadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 自定义异步类
     */
    private class UploadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            //得到上传的目录
            String localPath = Bean.endPath;
            File localFile = new File(localPath);
            //判断文件是否存在
            if (localFile.exists()) {
                //判断文件是否为空
                int empty = deleteutils.fileIsNull(localFile);
                //文件不为空时
                if (empty == 1) {
                    //开始登陆ftp
                    boolean loginResult = ftpLogin();
                    //登陆成功
                    if (loginResult) {
                        getFileSize();
                        getLocalFileSize(localPath);
                        //上传结果
                        boolean result = getFileNumber(localPath);
                        ftpSignOut();
//                        ftpFileSize = 0;
//                        localFileSize = 0;
                        if (result) {
                            clearPreservation();
                        } else {
                            preservationSize(localFileSize, ftpFileSize);
                        }
                        stopSelf();
                    } else {
                        stopSelf();
                    }
                } else {
                    //当本地文件大小为0时，直接显示100%
                    if (upLoadFileProcess != null) {
                        upLoadFileProcess.getUpLoadFileProcessPrecent(100);
                    }
                }
            } else {
                //当本地文件不存在时，直接显示100%
                if (upLoadFileProcess != null) {
                    upLoadFileProcess.getUpLoadFileProcessPrecent(100);
                }
            }
            return null;
        }
    }

    /**
     * 搜索上传文件夹中的以任务编号命名的文件夹数量，作为每次上传的路径
     */
    private boolean getFileNumber(String path) {
        String remoteDirectoryPath = "/图片和视频/";
        //创建服务器根路径，防止服务器无该路径造成数据上传失败
        createFtpFile(remoteDirectoryPath);
        boolean uploadResult = false;
        File file = new File(path);
        File[] files = file.listFiles();
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                //上传文件夹
                uploadResult = uploadDirectory(files[i].getAbsolutePath(), remoteDirectoryPath);
                if (uploadResult) {
                    //改变该患者的筛查状态
                    setScreenState(files[i].getName());
                } else {
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


    /**
     * 上传本地文件夹到ftp服务器上
     */
    private boolean uploadDirectory(String localDirectory, String remoteDirectoryPath) {
        try {
            File localFile = new File(localDirectory);
            //拼接路径，作为ftp服务器上的路径
            remoteDirectoryPath = remoteDirectoryPath + localFile.getName() + "/";
            //创建服务器文件
            createFtpFile(remoteDirectoryPath);
            if (makeDirFlag) {
                //获取该目录下所有文件和目录的绝对路径
                File[] allFiles = localFile.listFiles();
                if (allFiles == null) {
                    return makeDirFlag;
                }
                if (allFiles.length > 0) {
                    for (int currentFile = 0; currentFile < allFiles.length; currentFile++) {
                        if (!allFiles[currentFile].isDirectory()) {
                            String srcName = allFiles[currentFile].getPath().toString();
                            makeDirFlag = uploadFile(new File(srcName), remoteDirectoryPath);
                            if (!makeDirFlag) {
                                return false;
                            }
                        }
                    }
                    for (int currentFile = 0; currentFile < allFiles.length; currentFile++) {
                        if (allFiles[currentFile].isDirectory()) {
                            // 递归
                            uploadDirectory(allFiles[currentFile].getPath().toString(),
                                    remoteDirectoryPath);
                        }
                    }
                }
                return makeDirFlag;
            } else {
                ftpSignOut();
                return makeDirFlag;
            }

        } catch (Exception e) {
            e.printStackTrace();
            ftpSignOut();
            stopSelf();
            if (upLoadFileProcess != null) {
                upLoadFileProcess.loginOut(false);
            }
            LogHelper.e("出现异常e.msg=" + e.getMessage());
        }
        return makeDirFlag;
    }

    /**
     * 上传单个文件
     */
    private boolean uploadFile(File localFile, String remoteUploadPath) {
        LogHelper.i("uploadFile: localFile = " + localFile + " , localFile.getName()= " + localFile.getName() + " , romotUpLoadePath =" + remoteUploadPath);
        BufferedInputStream inputStream = null;
        boolean result = false;
        try {
            //改变工作路径
            ftpClient.changeWorkingDirectory(remoteUploadPath);
            inputStream = new BufferedInputStream(new FileInputStream(localFile));
            LogHelper.e(localFile.getName() + "开始上传....." + inputStream + "......" + this.ftpClient);
            //设置为被动模式，由客户端发起请求
            ftpClient.enterLocalPassiveMode();
            ftpClient.setSoTimeout(5000);
            result = ftpClient.storeFile(localFile.getName(), inputStream);
            LogHelper.d("上传FTP文件中...,结果 success=" + result);
            //文件上传成功
            if (result) {
                //向服务器发送命令，保持长连接，此方法暂时不知道是否起作用
                ftpClient.sendCommand("pwd");
                num = 0;
                //判断是否上传完整，有时断网后result也为true
                boolean isComplete = getSize(remoteUploadPath, localFile);
                //如果本地上传的图片和服务器上已上传的文件的大小一致，则说明该文件已完整上传至ftp
                if (isComplete) {
                    double percent = ((double) ftpFileSize / localFileSize) * 100;
                    if (upLoadFileProcess != null) {
                        upLoadFileProcess.getUpLoadFileProcessPrecent(percent);
                    }
//                    initDelete(localFile.getAbsolutePath());
//                    if(percent >= 100){
//                        clearPreservation();
//                        Deleteutils.deleteLocal(new File(Bean.endPath));
//                    }
                }
                return result;
            } else {
                //文件上传失败后，再次进行上传，最多上传4次
                num++;
                if (num <= 4) {
                    uploadFile(localFile, remoteUploadPath);
                } else {
                    LogWriteUtils.e("上传失败图片", localFile.getAbsolutePath());
                    LogHelper.e("上传失败图片...,图片路径path=" + localFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            //程序出现假死状态，需要先退出FTP，在尝试重新连接
            num++;
            if (num <= 4) {
                ftpSignOut();
                ;
                ftpLogin();
                uploadFile(localFile, remoteUploadPath);
                LogHelper.e("上传FTP图片出现异常2 e.msg=" + e.getMessage() + " , 图片路径" + localFile.getAbsolutePath());
            } else {
                ftpSignOut();
                stopSelf();
                preservationSize(localFileSize, ftpFileSize);
                //此时不再尝试连接，直接返回结果
                if (upLoadFileProcess != null) {
                    upLoadFileProcess.loginOut(false);
                }
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private String TAG = "service";

    /**
     * 假如程序处于假死状态时，将本地文件大小和已上传的文件大小保存到本地，作为重新上传时的基础
     */
    private void preservationSize(long localFileSize, long ftpFileSize) {
        Log.e(TAG + "_save", "本地文件大小 ： " + localFileSize + " , 服务器文件大小 ： " + ftpFileSize);
        SPUtils.put(this, Constant.localFile_key, localFileSize);
        SPUtils.put(this, Constant.ftpFile_key, ftpFileSize);
    }

    /**
     * 文件上传完成后，清除本地缓存的文件大小
     */
    private void clearPreservation() {
        SPUtils.put(this, Constant.localFile_key, 0);
        SPUtils.put(this, Constant.ftpFile_key, 0);
        Log.e(TAG + "_clear", "本地文件大小 ： " + localFileSize + " , 服务器文件大小 ： " + ftpFileSize);
    }

    /**
     * 得到缓存的文件的大小
     */
    private void getFileSize() {
        boolean local_key = SPUtils.contains(this, Constant.localFile_key);
        boolean ftp_key = SPUtils.contains(this, Constant.ftpFile_key);
        if (local_key && ftp_key) {
            localFileSize = (long) SPUtils.get(this, Constant.localFile_key, 0L);
            ftpFileSize = (long) SPUtils.get(this, Constant.ftpFile_key, 0L);
        }
        Log.e(TAG + "_get", "本地文件大小 ： " + localFileSize + " , 服务器文件大小 ： " + ftpFileSize);
    }

    /**
     * 文件复制成功后删除原始文件
     */
    private void initDelete(String path) {
        new File(path).delete();
    }

    /**
     * 得到服务器文件大小，刚刚上传成功的,如果上传成功，但是该文件的大小没有计入总大小，则重复进行计算
     */

    private boolean getSize(String path, File localFile) {
        FTPFile[] ftpFiles = new FTPFile[0];
        try {
            ftpClient.enterLocalPassiveMode();
            ftpFiles = ftpClient.listFiles(path + localFile.getName());
            if (ftpFiles.length > 0) {

                if (ftpFiles[0].getSize() == localFile.length()) {
                    ftpFileSize += ftpFiles[0].getSize();
                    //原图_20190612164855.jpg 上传成功 文件size=182309|||182309
                    LogHelper.d(localFile.getName() + " 上传成功 文件size=" + ftpFileSize + "|||" + ftpFiles[0].getSize());
//                    Log.e("ftpsize", localFile.getName() + "上传成功" + ftpAllSize+"|||"+ftpFiles[0].getSize()+"|||"+localFile.length());
                    return true;
                }
//                Log.e("loadSuccess", localFile.getName() + "上传成功" + FTPAllSize);
            } else {
                if (num <= 3) {
                    getSize(path, localFile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogHelper.e("得到服务器文件小出现异常 e.msg=" + e.getMessage());
        }
        return false;
    }

    //创建服务器文件
    private void createFtpFile(String remoteDirectoryPath) {
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
                    createFtpFile(remoteDirectoryPath);
                }
            }

            //创建服务器文件 remoteDirectoryPath=/图片和视频/ ,结果makeDirFlag=true
            LogHelper.d("创建服务器文件 remoteDirectoryPath=" + remoteDirectoryPath + " ,结果makeDirFlag=" + makeDirFlag);
        } catch (IOException e) {
            e.printStackTrace();
            LogHelper.e("创建服务器文件出现异常 文件夹路径=" + remoteDirectoryPath + " e.msg=" + e.getMessage() + "");
        }

    }

    //得到本地文件大小
    private void getLocalFileSize(String localPath) {
        File file = new File(localPath);
        if (file.exists()) {
            localFileSize = getSize(file.listFiles());
        }
    }

    /**
     * 本地文件大小
     */
    public long getSize(File[] localFile) {
        for (int i = 0; i < localFile.length; i++) {
            if (localFile[i].isFile()) {
                localFileSize += localFile[i].length();
            } else {
                getSize(localFile[i].listFiles());
            }
        }
        return localFileSize;
    }

    /**
     * 登录ftp服务器
     */
    public boolean ftpLogin() {
        boolean isLogin = false;
        try {
            FTPClientConfig ftpClientConfig = new FTPClientConfig();
            ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
            //设置编码格式
            ftpClient.setControlEncoding("utf-8");
            ftpClient.configure(ftpClientConfig);
            //开始连接服务器
            if (this.ftpPort > 0) {
                ftpClient.connect(this.ftpIp, this.ftpPort);
            } else {
                ftpClient.connect(this.ftpIp);
            }
            //ftp服务器连接回答
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                LogHelper.e("服务器登陆失败");
                return isLogin;
            }
            isLogin = ftpClient.login(this.ftpUser, this.ftpPassword);
            //设置传输协议
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            LogHelper.d("成功登陆FTP服务器 isLogin=" + isLogin);
            ftpClient.setBufferSize(1024 * 2);
            ftpClient.setDataTimeout(10 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.e("登录FTP服务失败 isLogin=" + isLogin + " ，异常 " + e.getMessage().toString());
        }
        return isLogin;
    }

    /**
     * 退出ftp服务器，断开连接
     */
    private void ftpSignOut() {
        try {
            if (null != ftpClient && ftpClient.isConnected()) {
                boolean outResult = ftpClient.logout();
                LogHelper.d("成功退出服务器 outResult=" + outResult);
            }
        } catch (Exception e) {
            LogHelper.e("退出FTP服务器异常 error.msg=" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface UploadFileProcess {
        void getUpLoadFileProcessPrecent(double percent);

        void loginOut(boolean outResult);
    }

    static UploadFileProcess upLoadFileProcess;

    public static void setUpLoadFileProcessListener(UploadFileProcess upLoadFileProcessListener) {
        upLoadFileProcess = upLoadFileProcessListener;
    }
}
