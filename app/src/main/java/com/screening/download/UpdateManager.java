package com.screening.download;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.R;
import com.screening.activity.UpdateActivity;
import com.screening.uitls.FileUtils;
import com.logger.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import io.reactivex.schedulers.Schedulers;

/**
 */

public class UpdateManager {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 保存解析的XML信息 */
    HashMap<String, String> mHashMap;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;
    private String version = "";
    private String downloadurl = "";
    private String downName = "";
    private int localVersion = -1;
    private String localVersionName = "-1";
    private long localFileSize = 0;//本地已有文件的大小
    private long ftpFileSize = 0;//服务器文件的大小
    private TextView tv_show;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
//                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpdateManager(Context context, TextView tv_show) {
        this.mContext = context;
        this.tv_show = tv_show;
    }

    //解析数据
    public void initJson(String msg) {
        LogHelper.i("软件更新 msg ="+msg);
        try {
            JSONObject jsonObject = new JSONObject(msg);
            version = jsonObject.getString("version");
            downloadurl = jsonObject.getString("url");
            downName = jsonObject.getString("name");
            ftpFileSize = jsonObject.getLong("size");
            if (version != null && !version.equals("")) {
                localVersionName = getVersionName();
                Log.e("version", version + localVersionName);
                if (Double.parseDouble(version) > Double.parseDouble(localVersionName)) {
                    showNoticeDialog(downName);
                } else {
                    tv_show.setText(mContext.getString(R.string.soft_update_no));
                    Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    //获取版本名称

    private String getVersionName() {
        return getPackageInfo(mContext).versionName;
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo("com.screening", 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    public void showNoticeDialog(String apkname) {
        // 构造对话框
        Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(R.string.soft_update_info);
        builder.setCancelable(false);
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
//                showDownloadDialog();
                getApk(apkname);
            }
        });
        // 稍后更新
        builder.setNegativeButton(R.string.soft_update_later, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.setCanceledOnTouchOutside(false);
        noticeDialog.show();
    }

    /**
     * 更新前先判断本地有无APK，如果有，就不下载，直接安装
     */
    private void getApk(String apkName){
        String apkPath = Environment.getExternalStorageDirectory() + "/ScreenAppDownload/" + apkName;
        File file = new File(apkPath);
        if(file.exists()){

            localFileSize = FileUtils.getFileSize(file);
            Log.e("update11",localFileSize + ",,," + ftpFileSize);
            //对比本地和服务器文件大小
            if(localFileSize != ftpFileSize){
                //先下载
                showDownloadDialog();
            }else {
                //直接安装
            if(updateProgress != null){
                    updateProgress.startInstallApk(Environment.getExternalStorageDirectory() + "/ScreenAppDownload/",apkName);
                }
            }

        }else {
            //先下载
            showDownloadDialog();
        }
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_updating);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.setCanceledOnTouchOutside(false);
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }


    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "ScreenAppDownload";
                    URL url = null;

                    url = new URL(downloadurl);

                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = null;
                    Log.e("version", downName);
                    apkFile = new File(mSavePath, downName);

                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
//                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            if(updateProgress != null){
                                updateProgress.getUpdateResult(true,mSavePath,downName);
                            }
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if(updateProgress != null){
                    updateProgress.getUpdateResult(false,"","");
                }
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }

    /**
     * 打印机插件安装的通用方法
     */
    public void initInstallAPK(File apkFile){
        Intent install = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri apkUri =
                FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", apkFile);
//                content://com.qcam.fileprovider/external_files/Download/update.apk
//        Log.e(TAG, "android 7.0 : apkUri " + apkUri);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        install.setDataAndType(apkUri, "application/vnd.android.package-archive");
        mContext.startActivity(install);
    }

    public interface UpdateProgress{
        void getUpdateResult(boolean result, String mSavePath, String downName);
        void startInstallApk( String mSavePath, String downName);
    }
    static UpdateProgress updateProgress;
    public static void setUpdateProgresslisteneter(UpdateProgress updateProgresslisteneter){
        updateProgress = updateProgresslisteneter;
    }
}
