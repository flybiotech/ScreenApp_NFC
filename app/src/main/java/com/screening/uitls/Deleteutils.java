package com.screening.uitls;

import android.util.Log;

import com.logger.LogHelper;
import com.screening.model.FTPPath;
import com.screening.model.ListMessage;
import com.screening.model.User;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangbin on 2018/5/22.
 */

public class Deleteutils {
    /**
     * 删除本地文件
     */
    public static boolean deleteLocal(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file1 : files) {
                    deleteLocal(file1);
                }
            }
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * 删除数据库数据
     */
    public boolean deleteLitepal() {
        LitePal.deleteAll(ListMessage.class);
        LitePal.deleteAll(FTPPath.class);
        LitePal.deleteAll(User.class);

        List<ListMessage> listMessages = LitePal.findAll(ListMessage.class);
        List<FTPPath> ftpPaths = LitePal.findAll(FTPPath.class);
        List<User> users = LitePal.findAll(User.class);
        if (listMessages.size() == 0 && ftpPaths.size() == 0 && users.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断默认路径下是否有文件存在
     */
    public int fileIsNull(File file) {
        int temp = -1;
        if (file.exists()) {
            Log.e("cancelTimer4", temp + "");
            if (file.isDirectory()) {
                Log.e("cancelTimer5", temp + "");
                File[] files = file.listFiles();
                Log.e("size", getSize(files) + "");
                if (getSize(files) > 0) {

                    temp = 1;
                    Log.e("cancelTimer6", temp + "");
                } else {

                    temp = -1;
                    Log.e("cancelTimer7", temp + "");
                }
            }

            LogHelper.d("判断默认路径下是否有文件存在，temp=1表示有文件 temp=" + temp + " , file.path=" + file.getAbsolutePath());
            return temp;
        } else {
            LogHelper.d("默认路径下，文件夹为空 temp=" + temp);

            return temp;
        }
    }

    /**
     * 本地文件大小
     */
    private long LocalAllSize;

    public long getSize(File[] localFile) {

        for (int i = 0; i < localFile.length; i++) {
            if (localFile[i].isFile()) {
                LocalAllSize += localFile[i].length();
            } else {
                getSize(localFile[i].listFiles());
            }
        }
        return LocalAllSize;
    }

}
