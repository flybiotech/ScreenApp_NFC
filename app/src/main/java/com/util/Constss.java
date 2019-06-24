package com.util;

import android.Manifest;

import com.huashi.bluetooth.HSBlueApi;
import com.model.DevModel;

/**
 * Created by dell on 2018/4/23.
 */

public class Constss {

    public static int  videoType =0; //0： 表示默认状态，1 :表示 从视珍宝界面里面进行播放的视频 2 ：表示从病例管理里面进行的播放
    public static boolean wifiRepeat = false;//wifi当前的wifi是否是重复连接
    public static int picYuanTuCount = 0; // 原图的数量
    public static int picCuSuanBaiCount = 0; //醋酸白的数量
    public static int picCuSuanBaiAutoCount = 0; //醋酸白自动拍照的数量
    public static int picCuSuanBaiAutoCountSize = 1; //选择醋酸白自动拍照的次数
    public static int picDianYouCount = 0; //碘油的数量
    public static int picDianYouAutoCount = 0; //碘油自动拍照的数量
    public static int vedioCount = 0; //视频的数量
    //    fileName = /storage/emulated/0/ScreenApp/安康里/admin1562654/
    public static String fileBasePath = "";//筛查人员的文件夹地址
    public static String SZB_WIFI_SSID_KEY = "szbName";//视珍宝wiif
    public static String SZB_WIFI_PASS_KEY = "szbPass";//视珍宝密码
    public static String SZB_WIFI_MODIFY_KEY = "szbModify";//视珍宝账号和密码是否有修改
    public static String SZB_WIFI_FILTER_KEY = "szbFilter";//视珍宝WiFi过滤的关键字
    public static String LAN_WIFI_SSID_KEY = "LANName";//局域网wifi名称
    public static String LAN_WIFI_PASS_KEY = "LANPass";//局域网WiFi密码
    public static String LAN_WIFI_MODIFY_KEY = "LANModify";//局域网WiFi账号和密码是否有修改
    public static String WIFI_TYPE_KEY = "WIFI_TYPE_KEY";//区分WIFI的类型,是局域网还是视诊宝的WiFi
    public static String WIFI_TYPE_SZB = "WIFI_TYPE_SZB";//区分WIFI的类型,是局域网还是视诊宝的WiFi
    public static String WIFI_TYPE_LAN = "WIFI_TYPE_LAN";//区分WIFI的类型,是局域网还是视诊宝的WiFi
    public static String IMAGE_TYPE_YT = "TYPE_YT"; //原图
    public static String IMAGE_TYPE_CSB = "TYPE_CSB"; //原图
    public static String IMAGE_TYPE_DY = "TYPE_DY"; //原图

    //设置拍照录像的计时用的名称
    public static String SNAP_TOTALTIME="snapTotalTime";//定时 设置醋酸白状态下，连续拍照的总时间
    public static String SNAP_INTERVAL="snapInterval";// 设置醋酸白状态下，连续拍照的间隔时间

    public static String SNAP_TOTALTIME_dy="snapTotalTime_dy";//定时 设置醋酸白状态下，连续拍照的总时间
    public static String SNAP_INTERVAL_dy="snapInterval_dy";// 设置醋酸白状态下，连续拍照的间隔时间

    public static String RECORD_TOTALTIME="recordTotalTime";// 设置录制视频的时间
    //
    public static String SP_TOTALTIME_INDEX ="snap_total_index";// 记录选择的设置拍照的时长
    public static String SP_INTERVAL_INDEX ="snap_inteval_index";// 记录选择的拍照的间隔时间
    public static String SP_TOTALTIME_INDEX_dy ="snap_total_index_dy";// 记录选择的设置拍照的时长
    public static String SP_INTERVAL_INDEX_dy ="snap_inteval_index_dy";// 记录选择的拍照的间隔时间
    public static String SP_RECORD_INDEX ="snap_record_index";// 记录选择了第几个 摄像的时间
    public static int wifiConnectTime = 1000 * 3; //10秒
    public static int temp=-1;//用于判断点击自定义对话框MyDialog的按钮执行什么操作,0代表ftp,1代表wifi设置
    public static String Bluetooth_Path="blue_path";//本地缓存的蓝牙地址的key值
    public static String Bluetooth_Key="blue_key";//本地缓存的蓝牙名称的key值
    public static boolean isConnBlue=false;//蓝牙是否连接
    public static HSBlueApi hsBlueApi;//
    //loginActivity
    public static final String LOGIN_USER_TYPE = "NOMAL";
    public static final String LOGIN_DELETEALL_TYPE = "DELETEALL";//恢复出厂设置
    public static final String LOGIN_DELETEALL_NAME = "shanghaifaluyuan";//恢复出厂设置的账号
    public static final String LOGIN_DELETEALL_PASS = "123456";//恢复出厂设置的密码
    public static final String LOGIN_DELETESINGLE_TYPE = "DELETESINGLE"; //单个账号功能删除未上传的受检者信息
    public static final String LOGIN_DELETESINGLE_PASS = "admin123"; //单个账号功能删除未上传的受检者信息的密码

    public static final String LOGIN_USERINFO = "LOGIN_USER_INFO"; //SP 用于保存用户信息
    public static String LOGIN_FAILED_NAMEERROR = "账户不能为空";
    public static String LOGIN_FAILED_PASSERROR = "密码不能为空";
    public static String LOGIN_FAILED_ERROR = "账户或密码错误";

    public static final String LOGIN_SP_NAME = "userName_login";//记录登录的账号
    public static final String LOGIN_SP_PASS = "userPass_login";
    public static final String LOGIN_SP_CHECKBOX = "boxState_login";

    public static String UPDATE_CODE_SUC_00="SUCCESS_00"; //表示用户还没有获得 jobid
    public static String UPDATE_CODE_SUC_0="SUCCESS_0"; //表示用户获得了jobId，但是还没有获取任务编号
    public static String UPDATE_CODE_SUC_1="SUCCESS_1"; //表示用户获得了任务编号，但是还没有进行文件重命名
    public static String UPDATE_CODE_SUC_2="SUCCESS_2"; //表示用户的 文件拷贝成功
    public static String UPDATE_CODE_SUC_3="SUCCESS_3"; //

    public static String IS_SD_STATE = "SD_STATE";//判断是否有SD 卡
    public static String OldJobId = "oldJobId"; //保存获取到的jobid


    public static String ERROR01 = "";
    public static String ERROR02 = "";
    public static String ERROR03 = "该HPV试管码已被绑定过";
    public static String ERROR04 = "该TCT试管码已被绑定过";
    public static String ERROR05 = "该基因检测试管码已被绑定过";
    public static String ERROR06 = "该DNA倍体试管码已被绑定过";
    public static String ERROR07 = "";
    public static String ERROR08 = "文件重命名失败";
    public static String ERROR09 = "";
    public static String ERROR010 = "";
    public static String ERROR011 = "";




    /**
     * 权限常量相关
     */
    public static final int WRITE_READ_EXTERNAL_CODE = 0x01;
    public static final String[] WRITE_READ_EXTERNAL_PERMISSION = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final int HARDWEAR_CAMERA_CODE = 0x02;
    public static final String[] HARDWEAR_CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};

    public static final int ASK_CALL_BLUE_CODE= 0x03;//权限申请的返回值
    public static final String[] ASK_CALL_BLUE_PERMISSION = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};







}
