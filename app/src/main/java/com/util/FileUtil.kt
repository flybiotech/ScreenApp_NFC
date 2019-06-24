package com.util

import android.content.Context
import android.os.Environment
import com.activity.R
import com.logger.LogHelper
import com.screening.model.Bean
import com.screening.model.ListMessage
import java.io.*

class FileUtil {

    public constructor() {


    }

    companion object {

        @JvmStatic
        fun getRootFilePath(): String {
            var path: String? = null
//            var mkdirs = false
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                path = Environment.getExternalStorageDirectory().absolutePath
            }
            path = "$path/ScreenApp"
            val file = File(path)
            if (!file.exists()) {
                file.mkdirs()
            }
            return path
        }


        @JvmStatic
        fun isFileExist(path: String?): Boolean {
            if (path == null) {
                return false
            }
            val file = File(path)
            return file.exists()

        }

        @JvmStatic
        fun createFileFolder(context: Context, id: String, idCard: String?): Boolean {
            var str = "Default"
            if (idCard != null && idCard != "") {
                str = idCard
            }
            var path: String? = null
            var mkdirs = false
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                path = Environment.getExternalStorageDirectory().absolutePath
            }
            path = "$path/ScreenApp/图片和视频/$str/"
            Constss.fileBasePath = path
            //fileName = /storage/emulated/0/ScreenApp/18/340826199803162615/原图
            val b0 = getPicFileName(context, 0, path, id)
            val b1 = getPicFileName(context, 1, path, id)
            val b2 = getPicFileName(context, 2, path, id)
            val b3 = getPicFileName(context, 3, path, id)
            // LogUtils.e("tag四个参数","b0  = "+b0+",  b1  = "+b1+",  b2  = "+b2+",  b3  = "+b3+" ,  path = "+path);
            mkdirs = b0 == b1 == b2 == b3

            return mkdirs
        }


        @JvmStatic
        private fun getPicFileName(context: Context, type: Int, path: String, id: String): Boolean {
            val mContext = context.applicationContext
            var path = path
            var mkdirs = true
            var str = mContext.getString(R.string.image_artword)
            val listMessage = ListMessage()

            when (type) {
                0 -> {
                    str = mContext.getString(R.string.image_artword)
                    path = path + str
                    listMessage.picYaunTuPath = path
                }

                1 -> {
                    str = mContext.getString(R.string.image_acetic_acid_white)
                    path = path + str
                    listMessage.picCuSuanPath = path
                }

                2 -> {
                    str = mContext.getString(R.string.image_Lipiodol)
                    path = path + str
                    listMessage.picDianYouPath = path
                }

                3 -> {
                    str = mContext.getString(R.string.image_manage_pickup)
                    path = path + str
                    listMessage.vedioPath = path
                }
                else -> {
                }
            }
            val file = File(path)
            if (!file.exists()) {
                mkdirs = file.mkdirs()
            }
            if (mkdirs && "" != id) {
                val i = listMessage.updateAll("pId = ?", id)
            }

            return mkdirs
        }


    }


    //文件夹重命名
    fun reNameFile(useridCard: String, userTaskNumber: String): String {
        // bePath= Environment.getExternalStorageDirectory()+"/ScreenApp/图片和视频/"
        // endPath=Environment.getExternalStorageDirectory()+"/ScreenAppCopy/图片和视频/";//文件夹重命名后复制到此路径下，上传也是此路径
        var result = "-1"
        var isRename = false
        if (useridCard.isEmpty()) {
            result = "-2"
            return result
        }

        if (userTaskNumber.isEmpty()) {
            result = "-3"
            return result
        }
        //这里是主要代码
        val scrIdFile = File(Bean.bePath + useridCard.trim())//
        val tNumFile = File(Bean.bePath + userTaskNumber.trim())//需要被改成的文件名名称
        //scrIdFile=/storage/emulated/0/ScreenApp/图片和视频/3408261992,tNumFile=/storage/emulated/0/ScreenAppCopy/图片和视频/1027280452
        LogHelper.i("重命名 scrIdFile=${scrIdFile.absolutePath},tNumFile=${tNumFile.absolutePath}, tNumFile=${tNumFile.path}")
        if (scrIdFile.exists() && !tNumFile.exists()) {
            isRename = scrIdFile.renameTo(tNumFile)
        }

        if (isRename) { //表示重命名成功
            result = "1"
            return result
        }

        if (!scrIdFile.exists()) {
            result = "-4"
            return result
        }

        if (tNumFile.exists()) {
            result = "-5"
            return result
        }

        return result
    }


    //文件夹的复制
    fun copyDirectory(sourceDir: String, targetDir: String) {
        //新建目标文件夹
        val targetFileDir = File(targetDir)
        if (!targetFileDir.exists()) {
            targetFileDir.mkdirs()
        }
        //获取源文件夹下的所有文件或文件夹
        val sourceFileList = File(sourceDir).listFiles()

        for (file in sourceFileList) if (file.isFile) {
            copyFile(file.path, targetDir + file.name)
        } else copyDirectory(file.path + File.separator, targetDir + File.separator + file.name + File.separator)
    }


    //文件的复制
    private fun copyFile(sourcePath: String, targertPath: String) {
        //新建文件输入流，并对它进行缓冲
        val input = FileInputStream(sourcePath)
        val inBuffer = BufferedInputStream(input)

        //新建文件夹输出流并对它进行缓冲 ，这种方式速度会更快一些
        val output = FileOutputStream(targertPath)
        val outBuff = BufferedOutputStream(output)

        //缓冲数组
        val b = ByteArray(1024 * 5)
        var len: Int = -1
        inBuffer.use { inBuffers ->
            outBuff.use {
                while (inBuffers.read(b).also { len = it } != -1) {
                    it.write(b, 0, len)
                }
                //刷新此缓冲的输出流
                it.flush()
            }
        }
    }


    //判断文件夹下是否有文件
    fun isExistFile(dirFile: File): Boolean {
        if (dirFile.exists()) {
            if (dirFile.isDirectory()) {
                val files = dirFile.listFiles()
                if (files.size > 0) {
                    return true
                }
            }
        }
        return false
    }

    var fileCount = 0 //文件夹下所有的文件的个数
    fun setFileCount() {
        fileCount = 0
    }

    fun getFileCount(filePath: String): Int {

        //新建目标文件夹
        val targetFileDir = File(filePath)
        if (targetFileDir.exists()) {
            //获取源文件夹下的所有文件或文件夹
            val sourceFileList = File(filePath).listFiles()

            for (file in sourceFileList) if (file.isFile) {
                fileCount++
            } else getFileCount(file.absolutePath)
        }
        return fileCount

    }


}