package com.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.model.DevModel;
import com.model.RetModel;
import com.network.Network;
import com.screening.manager.UserManager;
import com.screening.model.ListMessage;
import com.screening.rotatedialog.dialog.MyDialog;
import com.screening.uitls.SPUtils;
import com.screening.wifi.WifiConnectUtil;
import com.southtech.thSDK.lib;
import com.util.Constss;
import com.util.FileUtil;
import com.util.SouthUtil;
import com.view.LoadingDialog;
import com.view.MyTitleView;
import com.view.XCArcMenuView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class LiveVidActivity extends BaseActivity implements View.OnClickListener, XCArcMenuView.OnMenuItemClickListener, MyDialog.OnDialogButtonClickListener {

    public GlBufferView mPlayer;
    private XCArcMenuView view_xcarc;
    ImageButton btn_capture, btn_record, btn_photo_manager, btn_setting, btn_back;
    private MyTitleView tx_record, tx_snap_time, tv_huanzhe, bt_claer, bt_start;
    private MyTitleView tx_xcarc;
    private LinearLayout ctl_right;
    private boolean isRecord = false;
    private boolean isSnap_time = false;
    private String TAG = "TAG_1_LiveVidActivity : ";
    private float recordTotalTime = 0;
    private float recordCurrentTime = 0;
    private float snapCurrentTime = 0;//当前计时的时间
    private float snapTotalTime = 0;//总的拍摄照片的时长 ，例如180秒，240秒等等
    private float snapIntervalTime = 0; //拍照的间隔时间
    private int index; //
    private DevModel dev;
    private String imageName = Constss.IMAGE_TYPE_YT; //原图的照片的命名规则
    //定时刷新列表
    public int TIME = 1000;
    public boolean updateDevTimeExec = false;
    private ListMessage userListMessage; //
    private String picFilePath = ""; //根目录地址
    private String vedioFilePath = ""; //根目录地址
    private String userId = null;
    private UserManager userManager;
    private LoadingDialog mDialog;
    private boolean isChecked_lamp = false;
    private boolean isFront = false;//判断activity是否在前台

    private MyDialog myDialog;
    private boolean snapGroundback = false;
    private boolean recordGroundback = false;
    private SouthUtil southUtil;

    //已经在FragGetImage 中，初始化视珍宝拍照和拍摄视频的默认时间
    Handler handler = new Handler();

    Runnable runnable_fresh = new Runnable() {
        @Override
        public void run() {
            snapImage();//拍照
            recordVedio();//录制视频
            handler.postDelayed(runnable_fresh, TIME);

            if (!updateDevTimeExec) {
                updateDevTimeExec = true;
                updateDevTime();
            }

        }
    };


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (southUtil == null) {
                southUtil = new SouthUtil();
            }
            switch (msg.what) {

                case 1:
                    break;
                case 2:

                    showDiolog(getString(R.string.photoing));
                    snapGroundback = true;
                    break;

                case 3:
                    dismissDiolog();
                    southUtil.showToastInfo(context, context.getString(R.string.image_picture_save_success));
                    snapGroundback = false;
                    break;


                case 4:
                    dismissDiolog();
                    southUtil.showToastInfo(context, context.getString(R.string.image_photograph_error));
                    snapGroundback = false;
                    break;

                case 7:

                    southUtil.showToastInfo(context, context.getString(R.string.image_pickup_begin));
                    break;

                case 8:
                    southUtil.showToastInfo(context, context.getString(R.string.image_pickup_error));
                    break;

                case 10:
                    break;

                default:
                    break;

            }


        }
    };

    //拍照
    private void snapImage() {
        if (!isSnap_time) {
            return;
        }
        if (snapTotalTime > snapCurrentTime) {
            snapCurrentTime++;

            if (tx_snap_time != null) {
                tx_snap_time.setText(SouthUtil.getTimeSSMM((int) snapCurrentTime));
            }
            if ((snapCurrentTime % snapIntervalTime) == 0) {
                if (mPlayer == null) return;
                Log.i(TAG, "snapImage: snapCurrentTime = " + snapCurrentTime);
                SouthUtil.setImageNameAtuo(imageName, (snapTotalTime / 60), snapCurrentTime, picFilePath);
                mPlayer.mHandler = mHandler;
                mPlayer.isTakePicture = true;

            }
            isEnableClick(false, false, true, true, false);
            if (snapCurrentTime == snapTotalTime) {

                //获取图像界面醋酸白计时完成后自动切换碘油
                tx_xcarc.setText(getString(R.string.image_Lipiodol));
                setTextViewName(2);
            }

        } else {
            isSnap_time = false;
            isEnableClick(true, true, false, false, true);
            snapCurrentTime = 0;
            if (tx_snap_time != null) {
                tx_snap_time.setText("00:00");
            }
        }

    }

    //录制视频
    private void recordVedio() {
        if (dev == null || !isRecord) {
            return;
        }

        if (recordTotalTime > recordCurrentTime) {
            recordCurrentTime++;
            tx_record.setVisibility(View.VISIBLE);
            tx_record.setText("REC:" + SouthUtil.getTimeSSMM((int) recordCurrentTime));
            tx_record.setTextColor("#FF69B4");
            tx_record.setBackgroundResource(R.color.bg_xcarc);
        } else {

            if (isRecord) {
                lib.jlocal_StopRec(0);//停止录制视频
                southUtil.showToastInfo(LiveVidActivity.this, getString(R.string.image_pickup_end));
            }
            recordCurrentTime = 0;
            isRecord = false;
            if (btn_record != null) {
                btn_record.setSelected(false);
            }
            tx_record.setText("");
            tx_record.setBackgroundResource(R.color.bg_xcarc);
            tx_record.setVisibility(View.GONE);
        }

    }


    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//禁止屏幕休眠
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userId = getIntent().getStringExtra("user_id");
        dev = getIntent().getParcelableExtra("dev");
        Log.e("TAG_1", "onCreate: userId =" + userId + ",dev=" + dev);
        initView();
        onClickListenerImpl();

    }


    private void initView() {
        setContentView(R.layout.activity_live_vid);
        tv_huanzhe = (MyTitleView) findViewById(R.id.tv_huanzhe);
        tv_huanzhe.setEnabled(false);
        tv_huanzhe.setTextColor("#51516F");
        mPlayer = (GlBufferView) findViewById(R.id.player);
        bt_start = (MyTitleView) findViewById(R.id.bt_start);//开始计时
        tx_snap_time = (MyTitleView) findViewById(R.id.snap_time);
        tx_snap_time.setText("00:00");
        tx_xcarc = (MyTitleView) findViewById(R.id.id_button);
        view_xcarc = (XCArcMenuView) findViewById(R.id.arcmenu);
        view_xcarc.setOnMenuItemClickListener(this);
        mPlayer.setLongClickable(true);
        ctl_right = (LinearLayout) findViewById(R.id.ctl_right);
        btn_capture = (ImageButton) findViewById(R.id.btn_capture);//拍照
        btn_record = (ImageButton) findViewById(R.id.btn_record);//录像
        btn_photo_manager = (ImageButton) findViewById(R.id.btn_photo_manager);//照片管理
        btn_setting = (ImageButton) findViewById(R.id.btn_setting);//设置
        tx_record = (MyTitleView) findViewById(R.id.tx_record);//录像
        btn_back = (ImageButton) findViewById(R.id.btn_back);//返回按钮
        bt_claer = (MyTitleView) findViewById(R.id.bt_claer);
        bt_claer.setText(getString(R.string.image_timing_clear));
        southUtil = new SouthUtil();
        userManager = new UserManager();
        //设置 照片 和视频的 初始时间
        myDialog = new MyDialog(getApplicationContext());
        myDialog.setOnButtonClickListener(this);
    }

    private void onClickListenerImpl() {
        bt_claer.setOnClickListener(this);
        bt_start.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_capture.setOnClickListener(this);
        btn_record.setOnClickListener(this);
        btn_photo_manager.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
    }


    /**
     * @param isCuSuanBai    选择 原图或者醋酸白或者碘油
     * @param isStartTime    开始计时
     * @param isIntervalTime 显示连续拍照时的时间
     * @param isPause        暂停计时
     * @param isbottom       底部一排按钮 是否显示
     */
    private void isEnableClick(boolean isCuSuanBai, boolean isStartTime, boolean isIntervalTime, boolean isPause, boolean isbottom) {
        tx_xcarc.setEnabled(isCuSuanBai); //选择 原图或者醋酸白或者碘油
        bt_start.setEnabled(isStartTime); //开始计时
        tx_snap_time.setEnabled(isIntervalTime);//显示连续拍照时的时间
        bt_claer.setEnabled(isPause); //暂停计时
        settingBtnEnableIsClick(isbottom); // 底部 按钮 是否显示

        if (isCuSuanBai) {
            tx_xcarc.setBackgroundResource(R.drawable.text_shape_select);

            tx_xcarc.setTextColor("#ffffffff");
        } else {
            tx_xcarc.setTextColor("#51516F");
            tx_xcarc.setBackgroundResource(R.drawable.text_shape_nor);

        }

        if (isStartTime) {
            bt_start.setTextColor("#ffffffff");
            bt_start.setBackgroundResource(R.drawable.text_shape_select);
        } else {
            bt_start.setTextColor("#51516F");
            bt_start.setBackgroundResource(R.drawable.text_shape_nor);
        }

        if (isIntervalTime) {
            tx_snap_time.setTextColor("#000000");
        } else {
            tx_snap_time.setTextColor("#51516F");
        }

        if (isPause) {
            bt_claer.setTextColor("#ffffffff");
            bt_claer.setBackgroundResource(R.drawable.text_shape_select);
        } else {
            bt_claer.setTextColor("#51516F"); //深灰色
            bt_claer.setBackgroundResource(R.drawable.text_shape_nor);
        }

    }

    //视频右边的按钮
    private void settingBtnEnableIsClick(boolean isclick) {
        if (isclick) {
            btn_capture.setEnabled(true);
            btn_record.setEnabled(true);
            btn_photo_manager.setEnabled(true);
            btn_setting.setEnabled(true);
            btn_back.setEnabled(true);
            btn_capture.setVisibility(View.VISIBLE);
            btn_record.setVisibility(View.VISIBLE);
            btn_photo_manager.setVisibility(View.VISIBLE);
            btn_setting.setVisibility(View.VISIBLE);
            btn_back.setVisibility(View.VISIBLE);
        } else {
            btn_capture.setEnabled(false);
            btn_record.setEnabled(false);
            btn_photo_manager.setEnabled(false);
            btn_setting.setEnabled(false);
            btn_back.setEnabled(false);
            btn_capture.setVisibility(View.GONE);
            btn_record.setVisibility(View.GONE);
            btn_photo_manager.setVisibility(View.GONE);
            btn_setting.setVisibility(View.GONE);
            btn_back.setVisibility(View.GONE);
        }

    }

    String path = null;

    @Override
    protected void onStart() {
        super.onStart();

        setSnapTime(); //初始化拍照录像的时间
        setUp();//
        if (imageName.contains(Constss.IMAGE_TYPE_YT)) {
            isEnableClick(true, false, false, false, true);

        } else {
            isEnableClick(true, true, false, false, true);
        }


        userListMessage = userManager.toIDSearch(userId);
        if (userListMessage == null) {
            finish();
            //数据异常
            SouthUtil.showToast(this, getString(R.string.liveVidact_exception));
            return;
        }

        picFilePath = FileUtil.getRootFilePath();
        if (imageName.equals(Constss.IMAGE_TYPE_YT)) {

            picFilePath = userListMessage.getPicYaunTuPath();

        } else if (imageName.equals(Constss.IMAGE_TYPE_CSB)) {

            picFilePath = userListMessage.getPicCuSuanPath();
            snapTotalTime = (float) SPUtils.get(this, Constss.SNAP_TOTALTIME, 60.0f);
            snapIntervalTime = (float) SPUtils.get(this, Constss.SNAP_INTERVAL, 10.0f);

        } else if (imageName.equals(Constss.IMAGE_TYPE_DY)) {

            picFilePath = userListMessage.getPicDianYouPath();

            snapTotalTime = (float) SPUtils.get(this, Constss.SNAP_TOTALTIME_dy, 30.0f);
            snapIntervalTime = (float) SPUtils.get(this, Constss.SNAP_INTERVAL_dy, 10.0f);
        }

        vedioFilePath = userListMessage.getVedioPath();

        tv_huanzhe.setText(userListMessage.getName()); //设置筛查人员的姓名
        Log.i(TAG, "onStart: picFilePath =" + picFilePath + " ,vedioFilePath=  " + vedioFilePath);

    }


    void setUp() {
        if (dev == null) {
            return;
        }

    }

    //初始化拍照录像的时间      //默认的返回值，单位都是秒
    void setSnapTime() {
        recordTotalTime = (float) SPUtils.get(this, Constss.RECORD_TOTALTIME, 60.0f);
    }


    void initControlButtonViewParam() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        LayoutParams layoutParams = new LayoutParams(SouthUtil.convertDpToPixel(50, this), LayoutParams.MATCH_PARENT);
        if (SouthUtil.getButtonIsAlignRightValue(this)) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            layoutParams.rightMargin = SouthUtil.convertDpToPixel(10, this);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            layoutParams.leftMargin = SouthUtil.convertDpToPixel(10, this);
        }

        ctl_right.setLayoutParams(layoutParams);

    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;////判断activity是否在前台

        if (!TextUtils.isEmpty(dev.ip)) {

            int port = Integer.parseInt(dev.dataport);
            lib.jthNet_PlayLive(0, dev.ip, dev.usr, dev.pwd, port, 0);
        }
        initControlButtonViewParam();

        handler.postDelayed(runnable_fresh, TIME);
    }


    //返回时
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            mPlayer.stop();

            this.finish();

        } //音量键＋的监听事件
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            //保存照片

            SouthUtil.setImageName(imageName, picFilePath);
            if (mPlayer != null) {
                mPlayer.mHandler = mHandler;
                mPlayer.isTakePicture = true;
            }
            return true;
        }

        return false;
    }


    //监听事件
    @Override
    public void onClick(View v) {
        view_xcarc.closeAnim();
        switch (v.getId()) {
            case R.id.btn_capture: { //拍照
                if (dev == null || snapGroundback) {
                    return;
                }
                snapGroundback = true;

                SouthUtil.setImageName(imageName, picFilePath);
//                mHandler.sendEmptyMessage(2);
                if (mPlayer != null) {
                    mPlayer.mHandler = mHandler;
                    mPlayer.isTakePicture = true;
                }


            }
            break;

            case R.id.btn_record: { //录制视频

                if (dev == null || snapGroundback) {
                    return;
                }
                if (!isRecord) {
                    isRecord = !isRecord;
                    btn_record.setSelected(true);
                    lib.jthNet_MediaKeepAlive(0);
                    SouthUtil.getSaveVedio(LiveVidActivity.this, vedioFilePath, mHandler);
                } else {
                    isRecord = !isRecord;
                    int i = lib.jlocal_StopRec(0);
                    southUtil.showToastInfo(LiveVidActivity.this, getString(R.string.image_pickup_end));
                    tx_record.setText("");
                    tx_record.setBackgroundResource(R.color.bg_xcarc);
                    tx_record.setVisibility(View.GONE);
                    recordCurrentTime = 0;
                    btn_record.setSelected(false);
                }
                Log.e(TAG, " 视频总时间 recordTotalTime: " + recordTotalTime + "  , isRecord : " + isRecord);
            }
            break;

            case R.id.btn_photo_manager: { //图片视频管理
                if (snapGroundback || isRecord) {
                    return;
                }

                Intent intent = new Intent(this, SDMediaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("index", index);
                bundle.putParcelable("dev",dev);
                intent.putExtras(bundle);
                startActivity(intent);


            }
            break;

            case R.id.btn_setting: { //视珍宝设置
                if (snapGroundback || isRecord) {
                    return;
                }

                Intent intent = new Intent(this, ConfigActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("index", index);
                bundle.putParcelable("dev",dev);

                intent.putExtras(bundle);
                startActivity(intent);

            }
            break;

            case R.id.btn_back: { //返回
                if (snapGroundback) return;

                finish();
            }
            break;

            case R.id.bt_start: {//开始计时

                //判断 拍照时是否同时记录视频
                isChecked_lamp = (boolean) SPUtils.get(this, "lamp", false);
                isSnap_time = true;//开始拍照
//                snapTotalTime = OneItem.getOneItem().getSpTotalTime();
                snapCurrentTime = 0;
                if (isChecked_lamp && !isRecord) {
                    isRecord = !isRecord;
                    recordTotalTime = snapTotalTime; //拍照和录像同时进行时，让录像的时间跟拍照的时间保持一致
                    recordCurrentTime = 0;
                    btn_record.setSelected(true);

                    SouthUtil.getSaveVedio(LiveVidActivity.this, vedioFilePath, mHandler);
                }

            }
            break;

            case R.id.bt_claer: { //停止拍照
                //停止拍摄
                snapCurrentTime = snapTotalTime;
                if (isChecked_lamp && isRecord) {
                    recordCurrentTime = recordTotalTime;
                }

                isEnableClick(true, true, false, false, true);
            }
            break;


            default:
                break;
        }


    }

    /**
     * 本地有无文件的弹出框，有两种情况
     */
    private void initFileDialog() {
        if (myDialog == null) {
            myDialog = new MyDialog(getApplicationContext());
        }
        Constss.temp = -1;
        myDialog.show(getFragmentManager(), 3);
        myDialog.setMessage(getString(R.string.havennofile));
        myDialog.setRotation(3);
    }

    public void setTextViewName(int position) {
        Log.i(TAG, "setTextViewName:position = " + position);

        switch (position) {
            case 0:
                imageName = Constss.IMAGE_TYPE_YT;
                if (userListMessage != null) {
                    picFilePath = userListMessage.getPicYaunTuPath();
                }

                isEnableClick(true, false, false, false, true);

                break;
            case 1:
                imageName = Constss.IMAGE_TYPE_CSB; //醋酸白的图片的命名规则
                if (userListMessage != null) {
                    picFilePath = userListMessage.getPicCuSuanPath();
                }
                snapTotalTime = (float) SPUtils.get(this, Constss.SNAP_TOTALTIME, 60.0f);
                snapIntervalTime = (float) SPUtils.get(this, Constss.SNAP_INTERVAL, 10.0f);
                isEnableClick(true, true, false, false, true);
                break;
            case 2:
                imageName = Constss.IMAGE_TYPE_DY; //碘油的图片的命名规则
                if (userListMessage != null) {
                    picFilePath = userListMessage.getPicDianYouPath();
                }
                snapTotalTime = (float) SPUtils.get(this, Constss.SNAP_TOTALTIME_dy, 30.0f);
                snapIntervalTime = (float) SPUtils.get(this, Constss.SNAP_INTERVAL_dy, 15.0f);
                isEnableClick(true, true, false, false, true);
                break;
            default:
                break;
        }

    }


    /**
     * 设置设备时间
     */
    void updateDevTime() {
        if (dev == null) {
            return;
        }
        if (!TextUtils.isEmpty(dev.ip)) {

            Network.getCommandApi(dev).updateTime(dev.usr, dev.pwd, 17, SouthUtil.getTimeYyyymmddhhmmss(), SouthUtil.getRandom())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<RetModel>() {
                        @Override
                        public void accept(RetModel retModel) throws Exception {

                        }
                    });
        }
    }


    //改变控件的背景颜色
    @Override
    public void onChangeBackground(boolean isChange) {
        Log.i(TAG, "onChangeBackground: isChange = " + isChange);
        //判断是否处于展开状态
        if (isChange) {
            view_xcarc.setBackgroundColor(00000000);
        } else {

            view_xcarc.setBackgroundResource(R.drawable.text_shape_select);
        }
    }

    //选择原图，醋酸白，碘油 的回调方法
    @Override
    public void onClickMenu(View view, int pos) {
        Log.i(TAG, "onClickMenu: pos = " + pos);
        String tag = (String) view.getTag();
        tx_xcarc.setText(tag);
        //这个pos 是从1 开始的，
        setTextViewName(pos - 1);
    }



    private void showDiolog(String msg) {
        try {
            if (mDialog != null && mDialog.isShow()) {
                mDialog.setMyMessage(msg);
            } else {
                if (mDialog == null) {
                    mDialog = new LoadingDialog(LiveVidActivity.this, "fly");
                }
                mDialog.setMyMessage(msg);
                mDialog.dialogShow();
            }
        } catch (Exception e) {
            Log.e(TAG, "showDiolog: " + e.getMessage());
            e.printStackTrace();
        }

    }


    private void dismissDiolog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }

    }


    @Override
    public void okButtonClick() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        //停止录像
        lib.jlocal_StopRec(0);
        lib.jthNet_StopPlay(0); //停止连接视珍宝
        bt_start.setEnabled(true);

    }


    @Override
    protected void onStop() {
        super.onStop();
        dismissDiolog();

        isFront = false;//判断activity是否在前台
        isRecord = false;


        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        WifiConnectUtil.Companion.getInstance().disposable();
        Log.i(TAG, "onStop: mHandler = " + mHandler + " , handler = " + handler);

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

//        mPlayer.cnx = null;
        mPlayer = null;
        southUtil = null;


    }


}

class GlBufferView extends GLSurfaceView {

    public Bitmap bitmap;
    public boolean isTakePicture = false;
    public Handler mHandler;
    int w;
    int h;
    int bt[];
    int b[];
    byte[] bitmapdata;
    IntBuffer buffer;


    public GlBufferView(Context context) {
        super(context);
//        cnx = context;
        // 设置OpenGl ES的版本为2.0
        setEGLContextClientVersion(2);
        // 设置与当前GLSurfaceView绑定的Renderer
        setRenderer(new MyRenderer());
        // 设置渲染的模式
        setRenderMode(RENDERMODE_WHEN_DIRTY);

        requestFocus();
        setFocusableInTouchMode(true);
//        capture = true;

    }

    public GlBufferView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        cnx = context;
        setRenderer(new MyRenderer());
        requestFocus();
        setFocusableInTouchMode(true);
//        capture = true;
    }


    public void play() {

        lib.jthNet_StopPlay(0);//
    }

    public void stop() {
        lib.jthNet_StopPlay(0);//停止连接视珍宝
    }

    @SuppressWarnings("unused")
    private Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl)
            throws OutOfMemoryError {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }

        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }


    //Renderer的功能就是这里说的画笔
    class MyRenderer implements Renderer {

        int width_surface, height_surface;
        private File dir_image;
        private int count = 0;

        // 绘制图形时调用  每隔16ms调用一次
        public void onDrawFrame(GL10 gl) {
            int ret = lib.jopengl_Render(0);
            saveBitmap();
        }

        //视图大小发生改变  时调用
        public void onSurfaceChanged(GL10 gl, int w, int h) {

// 设置OpenGL场景的大小,(0,0)表示窗口内部视口的左下角，(w,h)指定了视口的大小
            GLES20.glViewport(0, 0, w, h);

            width_surface = w;
            height_surface = h;
            int i = lib.jopengl_Resize(0, w, h);
        }


        //SurfaceView创建
        public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {

        }

        /**
         * 保存方法
         */
        public void saveBitmap() {


            try {

                if (isTakePicture) {
                    isTakePicture = false;

                    //--------------------------------------------------------------------
                    mHandler.sendEmptyMessage(2);
                    Log.i("hari", "printOptionEnable if condition:isTakePicture = " + isTakePicture);
                    w = width_surface;
                    h = height_surface;

                    b = new int[(int) (w * h)];
                    bt = new int[(int) (w * h)];
                    buffer = IntBuffer.wrap(b);
                    buffer.position(0);

//                    Log.e("hari", "saveBitmap:time 1 =  ");
                    GLES20.glReadPixels(0, 0, w, h, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);


//                    Log.e("hari", "saveBitmap:time 2.0 =  ");
                    for (int i = 0; i < h; i++) {
                        //remember, that OpenGL bitmap is incompatible with Android bitmap
                        //and so, some correction need.
                        for (int j = 0; j < w; j++) {
                            int pix = b[i * w + j];
                            int pb = (pix >> 16) & 0xff;
                            int pr = (pix << 16) & 0x00ff0000;
                            int pix1 = (pix & 0xff00ff00) | pr | pb;
                            bt[(h - i - 1) * w + j] = pix1;
                        }
                    }


//                    Log.e("hari", "saveBitmap:time 2 =  ");

                    Bitmap inBitmap = null;
                    if (inBitmap == null || !inBitmap.isMutable()
                            || inBitmap.getWidth() != w || inBitmap.getHeight() != h) {
                        inBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
                    }

                    inBitmap.copyPixelsFromBuffer(buffer);

                    // return Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
                    inBitmap = Bitmap.createBitmap(bt, w, h, Bitmap.Config.RGB_565);

                    bitmap = scaleBitmap(inBitmap);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
                    bitmapdata = bos.toByteArray();
                    ByteArrayInputStream fis = new ByteArrayInputStream(bitmapdata);


//                    String myfile = SouthUtil.saveImageName();
                    String myfile = SouthUtil.getImageName();
                    //Item.saveImageFilePath = /storage/emulated/0/SZB_save/Admin/2019-1-4/17-30-34_1_yey
                    Log.i("TAG_1_LiveVidActivity", "saveBitmap: myfile= " + myfile);
                    dir_image = new File(SouthUtil.getImageRootPath());
                    dir_image.mkdirs();
                    FileOutputStream fos = null;
                    try {
                        File tmpFile = new File(dir_image, myfile);
                        fos = new FileOutputStream(tmpFile);

                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = fis.read(buf)) > 0) {
                            fos.write(buf, 0, len);
                        }


                        mHandler.sendEmptyMessage(3);

                    } catch (FileNotFoundException e) {
                        mHandler.sendEmptyMessage(4);
                        e.printStackTrace();
                    } catch (IOException e) {
                        mHandler.sendEmptyMessage(4);
                        e.printStackTrace();
                    } finally {

                        b = null;
                        bt = null;
                        buffer = null;
                        bitmapdata = null;
                        if (fis != null) {
                            fis.close();
                        }

                        if (fos != null) {
                            fos.close();
                        }

                        if (bos != null) {
                            bos.close();
                        }

                        if (inBitmap != null) {
                            inBitmap.recycle();
                        }

                        if (bitmap != null) {
                            bitmap.recycle();
                        }
                        dir_image = null;
                        mHandler = null;

                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("hari", "onDrawFrame: " + e.getMessage());
            }


        }


        /**
         * 按比例缩放图片
         *
         * @param origin 原图
         *               ratio  比例
         * @return 新的bitmap
         */
        private Bitmap scaleBitmap(Bitmap origin) {
            if (origin == null) {
                return null;
            }
//            float scale = 1;
            int ratate = 90;


            int width = origin.getWidth();
            int height = origin.getHeight();
            float sw = 1920f / width;
            float sh = 1080f / height;
//            float ss = 1 / 2;
//            float ss1 = 1 / 2f;


            Matrix matrix = new Matrix();
            matrix.postScale(sw, sh);
            matrix.postRotate(ratate);
//            matrix.preScale(ratio, ratio);
            Bitmap newBM = null;
            try {
                newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
            } catch (Exception e) {
                Log.i("TAG_11", "scaleBitmap: error " + e.getMessage());
                e.printStackTrace();
            }
            origin.recycle();
            return newBM;
        }


    }


}










