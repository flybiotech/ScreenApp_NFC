package com.screening.uitls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

public class FileUtils {
    /**
     * 获取文件大小（包括文件夹）
     *
     * @return
     */
    public static String getAutoFilesSize(String filePath) {

        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFile(file);
            } else {
                blockSize = getFileSize(file);
            }

        } catch (Exception e) {

        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取本地单个文件的大小
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }

    /*
 转换文件大小
  */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.0000");
        String fileSizeString = "";
        if (fileS == 0) {
            return "0";
        }
        fileSizeString = df.format((float) fileS / 1073741824);
        return fileSizeString;
    }

    /*
   获取指定文件夹
    */
    private static long getFile(File f) throws Exception {
        long size = 0;
        File filest[] = f.listFiles();
        for (int i = 0; i < filest.length; i++) {
            if (filest[i].isDirectory()) {
                size = size + getFile(filest[i]);
            } else {
                size = size + getFileSize(filest[i]);
            }
        }
        return size;
    }
}
