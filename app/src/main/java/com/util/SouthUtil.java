package com.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.activity.R;
import com.application.MyApplication;
import com.screening.model.ListMessage;
import com.screening.uitls.SPUtils;
import com.southtech.thSDK.lib;
import com.view.MyTitleView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by gyl1 on 12/22/16.
 */

public class SouthUtil {

    public static String ButtonIsAlignRightValue = "ButtonIsAlignRightValue";//默认按钮靠右
    public static String APP = "SouthUtil";
    private static Toast toast;
    private  MyTitleView tvMessage;

    public static void showDialog(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void setButtonIsAlignRightValue(Context context, boolean type) {
        SharedPreferences agPreferences = context.getSharedPreferences(SouthUtil.APP, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = agPreferences.edit();
        editor.putBoolean(SouthUtil.ButtonIsAlignRightValue, type);
        editor.commit();
    }

    public static boolean getButtonIsAlignRightValue(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SouthUtil.APP, Activity.MODE_PRIVATE);
        boolean ring_tpe = preferences.getBoolean(SouthUtil.ButtonIsAlignRightValue, true);
        return ring_tpe;

    }

    //toast
    public static void showToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();

    }


    /**
     * 创建自定义Toast
     */
    private static Toast mToast;

    public  void showToastInfo(Context context, String msg) {
        if (mToast == null) {
            mToast = new Toast(context.getApplicationContext());
        }
        //设置Toast显示位置，居中，向 X、Y轴偏移量均为0
        mToast.setGravity(Gravity.CENTER, 0, 0);
        //获取自定义视图
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.toast_view, null);
        tvMessage = (MyTitleView) view.findViewById(R.id.toast_viewss);
        //设置文本颜色
        tvMessage.setTextColor("#F8F8FF");
        //设置视图
        mToast.setView(view);
        //设置显示时长
        mToast.setDuration(Toast.LENGTH_SHORT);

        //设置文本
        tvMessage.setText(msg);
        //显示
        mToast.show();


    }


    public static int convertDpToPixel(float dp, Context context) {
        Resources resources;
        resources = context.getApplicationContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }


    public static float sp2px( float spVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal,  Resources.getSystem().getDisplayMetrics());
    }

    public static float dp2px(float dpVal) {
        return  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, Resources.getSystem().getDisplayMetrics());
    }



    //日期转换
    public static String getTimeSSMM(int total) {

        return String.format("%02d:%02d", total / 60, total % 60);
    }

    public static String getTimeYyyymmddhhmmss() {

        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String ctime = formatter.format(new Date());
        return ctime;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            // versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            //  Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public static int getRandom() {
        int max = 100000;
        int min = 1;
        Random random = new Random();

        int s = random.nextInt(max) % (max - min + 1) + min;
        Log.e(tag, "random is " + s);
        return s;
    }

    static final String tag = "SouthUtil";
    static  String imageName="fly1.jpg";
    static  String imagerootPath="";


    public static  void setImageName(String type,String imagerootPath1) {
//        String str = "";
        String time = getTimeYyyymmddhhmmss();
        imagerootPath = imagerootPath1;
//        imageName=imageName1+"_"+time+".jpg";
        if (type.equals(Constss.IMAGE_TYPE_YT)) {
            imageName = "原图"+"_"+time+".jpg";


        } else if (type.equals(Constss.IMAGE_TYPE_CSB)) {

            imageName = "醋酸白" + "_" + time + ".jpg";


        } else if (type.equals(Constss.IMAGE_TYPE_DY)) {

            imageName ="碘油"+"_"+time+".jpg";

        }

//        return str;

    }


    public static String getImageName() {


        return imageName;
    }





    public static void setImageNameAtuo(String type,float minute,float second,String imagerootPath1) {
        String str = getTimeYyyymmddhhmmss();
        imagerootPath = imagerootPath1;
        Log.i("TAG_Live", "setImageNameAtuo: minute = "+minute +" , second = "+second);
        if (type.equals(Constss.IMAGE_TYPE_CSB)) {

            str = "醋酸白"+"_"+second+"秒_"+minute+"分_"+str+".jpg";


        } else if (type.equals(Constss.IMAGE_TYPE_DY)) {

            str = "碘油"+"_"+second+"秒_"+minute+"分_"+str+".jpg";;

        }

        imageName = str;

    }


    public static String getImageRootPath() {

        return imagerootPath;
    }


















    /**
     * @param picCatogary 表示图片的类型
     * @param fileName    //图片的保存地址  fileName = /storage/emulated/0/ScreenApp/安康里/admin1562654/原图
     *  @param isAuto :表示当前保存的照片，是否是醋酸白自动拍照的图片
     * @return
     */
    public void getSaveImage(Context mContext,String picCatogary, String fileName, final Handler mHandler, final String IDCard,boolean isAuto) {
        String myfile = null;
        if (fileName == null) {
            Message msg = new Message();
            msg.what = 2;
            mHandler.sendMessage(msg);
            return;
        }

        myfile = fileName + "/" + changeInfo(mContext,picCatogary,isAuto) + ".jpg";
        Log.i("TAG_file", "getSaveImage: fileName = "+fileName +"  , myfile = "+myfile);
        File file = new File(fileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(myfile);
        if (file1.exists()) {
            file1.delete();
        }
        try {
            file1.createNewFile();
        } catch (IOException e) {
            return;
        }

        //拍摄图片
        final String picPath = file1.getAbsolutePath();
        int i = lib.jlocal_SnapShot(0, picPath);
        if (i < 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        //判断是否筛查，0标识为筛查，1标识以筛查
                        ListMessage listMessage = new ListMessage();
                        listMessage.setIdentification("1");
                        listMessage.updateAll("idCard = ? ", IDCard);

                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        boolean result = rotatePicture(picPath, 90);
                        Message msg = new Message();
                        msg.obj = result;
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }
                }
            }).start();
        }

    }

    private static String changeInfo(Context mContext,String filePath,boolean isAuto) {
        int count = 0;
        String imageCatagory = MyApplication.getInstance().getString(R.string.image_artword);
//        String imageCatagory = "yuantu";
        switch (filePath) {
            case "yuantu":
                count = Constss.picYuanTuCount++;
//                imageCatagory = MyApplication.getInstance().getString(R.string.image_artword) + count;
                imageCatagory = MyApplication.getInstance().getString(R.string.image_artword) + "_"+getTimeYyyymmddhhmmss();
//                imageCatagory = "yuantu" + count;
                break;
            case "cusuanbai":

                if (isAuto) {
                    count = Constss.picCuSuanBaiAutoCount++;
                    int intervalTime = (int)SPUtils.get(mContext, Constss.SNAP_INTERVAL, 15);
                    int totalTime = (int)SPUtils.get(mContext, Constss.SNAP_TOTALTIME, 180);
//                    imageCatagory = MyApplication.getInstance().getString(R.string.image_acetic_acid_white) + "_" + (count) * (OneItem.getOneItem().getSpInterval()) + "秒_" + OneItem.getOneItem().getSpTotalTime() / 60+"分钟";
                    imageCatagory = MyApplication.getInstance().getString(R.string.image_acetic_acid_white) + "_" + (count) * intervalTime + "秒_" + totalTime / 60+"分钟_"+"_"+getTimeYyyymmddhhmmss();
                } else {
                    count = Constss.picCuSuanBaiCount++;
//                    imageCatagory = MyApplication.getInstance().getString(R.string.image_acetic_acid_white) + count;
                    imageCatagory = MyApplication.getInstance().getString(R.string.image_acetic_acid_white) + "_"+getTimeYyyymmddhhmmss();
                }


                break;
            case "dianyou":
//                count = Constss.picDianYouCount++;
//                imageCatagory = MyApplication.getInstance().getString(R.string.image_Lipiodol) + "_"+getTimeYyyymmddhhmmss();

                if (isAuto) {
                    count = Constss.picDianYouAutoCount++;
                    int intervalTime = (int)SPUtils.get(mContext, Constss.SNAP_INTERVAL, 15);
                    int totalTime = (int)SPUtils.get(mContext, Constss.SNAP_TOTALTIME, 180);
//                    imageCatagory = MyApplication.getInstance().getString(R.string.image_acetic_acid_white) + "_" + (count) * (OneItem.getOneItem().getSpInterval()) + "秒_" + OneItem.getOneItem().getSpTotalTime() / 60+"分钟";
                    imageCatagory = MyApplication.getInstance().getString(R.string.image_Lipiodol) + "_" + (count) * intervalTime + "秒_" + totalTime / 60+"分钟_"+"_"+getTimeYyyymmddhhmmss();
                } else {
                    count = Constss.picDianYouCount++;
                    imageCatagory = MyApplication.getInstance().getString(R.string.image_Lipiodol) + "_"+getTimeYyyymmddhhmmss();
                }

                break;
            default:
                break;

        }
        return imageCatagory;
    }

    /**
     * 旋转照片
     *
     * @param path   照片的地址
     * @param degree 要旋转的角度
     * @return
     */
    public boolean rotatePicture(String path, int degree) {
        boolean result = false;
        if (path == null) {
            return result;
        }
        //获取照片  android  根据路径获取Bitmap 失败，一直返回null
//        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null) {
            return result;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap resizeBitmap = null;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
//        LogUtils.e("TAG", "height = " + height + " , width = " + width + "  , resizeBitmap = " + resizeBitmap);
        result = saveBitmapToPath(resizeBitmap, path);

        return result;
    }


    /**
     * 把新图片保存到原来的路径，即覆盖原来的图片保存
     *
     * @param bitmap   要保存的图片
     * @param filePath 目标路径
     * @return 是否成功
     */
    public boolean saveBitmapToPath(Bitmap bitmap, String filePath) {
        if (bitmap == null || filePath == null) {
            return false;
        }
        boolean result = false;//默认结果
        File file = new File(filePath);
        OutputStream outputStream = null;//文件输出流
        try {
            outputStream = new FileOutputStream(file);
            //将图片压缩成JPEG
            result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (FileNotFoundException e) {
            result = false;
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 保存视频
     *
     * @param context
     * @param fileName
     * @return
     */
    public static  boolean getSaveVedio(Context context, String fileName,Handler mHandler) {
        String myfile = null;
        boolean b = false;
//        myfile = fileName + "/" + "视频" + Constss.vedioCount + ".mp4";
        myfile = fileName + "/" + "视频" + "_"+getTimeYyyymmddhhmmss() + ".mp4";
        Constss.vedioCount++;
        File file = new File(fileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(myfile);
        if (file1.exists()) {
            file1.delete();
        }
        try {
            file1.createNewFile();
        } catch (IOException e) {
            return false;
        }

        int i = lib.jlocal_StartRec(0, file1.getAbsolutePath());
        if (i == 1) {



            //正在保存视频
            mHandler.sendEmptyMessage(7);
//            showToastInfo(context, context.getString(R.string.image_pickup_begin));
            return true;
        } else {
            //视频保存失败
            mHandler.sendEmptyMessage(8);
//            showToastInfo(context, context.getString(R.string.image_pickup_error));
        }
        return b;
    }






}
