package com.screening.uitls;


import com.screening.model.ListMessage;
import com.screening.model.ScreenErrorList;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by zhangbin on 2018/5/28.
 */

public class CopyUtils {

    /**
     * 将默认路径下的文件复制到指定的目录下
     */
    public static int copy(String fromFile, String toFile) {
        //要复制的文件目录
        File[] fromList;
        File file = new File(fromFile);
        //判断文件是否存在
        if (!file.exists()) {
            return -1;
        }
        //如果存在则获取当前目录下的所有文件，填充数组
        fromList = file.listFiles();
        //目标目录
        File toList = new File(toFile);
        //创建目录
        if (!toList.exists()) {
            toList.mkdirs();
        }
        //遍历要复制的全部文件
        for (int i = 0; i < fromList.length; i++) {
            if (fromList[i].isDirectory()) {//如果当前项为子目录，进行递归
                copy(fromList[i].getPath() + "/", toFile + "/" + fromList[i].getName() + "/");
            } else {//如果当前项为文件则进行拷贝
                copyFile(fromList[i].getPath(), toFile + fromList[i].getName());
            }
        }
        return 1;
    }

    /**
     * 拷贝文件
     */
    private static List<ScreenErrorList> screenListList;
    static int num = 0;

    public static int copyFile(String fromFile, String toFile) {
        try {
            InputStream inputStream = new FileInputStream(fromFile);
            OutputStream outputStream = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int d;
            while ((d = inputStream.read(bt)) > 0) {
                outputStream.write(bt, 0, d);
            }
            inputStream.close();
            outputStream.close();
            LitePal.deleteAll(ScreenErrorList.class, "fromFile = ? and toFile = ?", fromFile, toFile);
            return 1;
        } catch (Exception e) {
            num++;
            if (num <= 2) {
                copyFile(fromFile, toFile);
            } else {
                num = 0;
                screenListList = LitePal.where("fromFile = ? and toFile = ?", fromFile, toFile).find(ScreenErrorList.class);
                if (screenListList.size() == 0) {
                    if (Constant.errorListMessage != null) {//如果有患者集合信息，将信息全部保留；如果没有值，则只保留错误信息
                        ListMessage listMessage = Constant.errorListMessage;
                        ScreenErrorList screenList = new ScreenErrorList();
                        screenList.setpId(listMessage.getpId());
                        screenList.setName(listMessage.getName());
                        screenList.setScreeningId(listMessage.getScreeningId());
                        screenList.setErrorMsg("原始路径：" + "" + fromFile + ",复制到路径:" + toFile + "失败。");
                        screenList.setFromFile(fromFile);
                        screenList.setToFile(toFile);
                        screenList.save();
                    } else {
                        ScreenErrorList screenList = new ScreenErrorList();
                        screenList.setpId("");
                        screenList.setName("");
                        screenList.setScreeningId("");
                        screenList.setErrorMsg("原始路径：" + "" + fromFile + ",复制到路径:" + toFile + "失败。");
                        screenList.setFromFile(fromFile);
                        screenList.setToFile(toFile);
                        screenList.save();
                    }

                }

            }
            return -1;
        }
    }
}
