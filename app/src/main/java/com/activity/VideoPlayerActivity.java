package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.drm.DrmErrorEvent;
import android.drm.DrmManagerClient;
import android.drm.DrmManagerClient.OnErrorListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.util.FileUtil;
import com.util.SouthUtil;



public class VideoPlayerActivity extends Activity implements OnPreparedListener, OnErrorListener{
    private static String tag =  "VideoPlayerActivity";
    private VideoView vv_video;
    private MediaController mController;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//禁止屏幕休眠
        setContentView(R.layout.activity_playvid);
        vv_video=(VideoView) findViewById(R.id.vv_video);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            url  = bundle.getString("videoPath");
        }
//        Logger.e(" videoPath = "+url);

        // 实例化MediaController
        mController=new MediaController(this);

        if (FileUtil.isFileExist(url))
        {
            // vv_video.setVideoPath(file.getAbsolutePath());
            // 为VideoView指定MediaController
            //Uri uri = Uri.parse(url);
            try {
                vv_video.setMediaController(mController);
                // 为MediaController指定控制的VideoView
                mController.setMediaPlayer(vv_video);
                mController.setKeepScreenOn(true);
                vv_video.setOnPreparedListener(this);
                vv_video.setVideoPath(url); // 加载path文件代表的视
            } catch (Exception e) {
                e.printStackTrace();
            }

            vv_video.seekTo(10);
             vv_video.start();
            // 增加监听上一个和下一个的切换事件，默认这两个按钮是不显示的
//            mController.setPrevNextListeners(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // Toast.makeText(VideoPlayerAct.this, "下一个",0).show();
//                }
//            }, new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    //Toast.makeText(VideoPlayerAct.this, "上一个",0).show();
//                }
//            });
        }
        else{
            SouthUtil.showToast(this,getString(R.string.image_manager_pickup_error));
        }
    }

    public void back(View v){
        Intent intent = VideoPlayerActivity.this.getIntent();
        VideoPlayerActivity.this.setResult(0,intent);
        finish();
    }
    @Override
    public void onError(DrmManagerClient arg0, DrmErrorEvent arg1) {
        // TODO Auto-generated method stub
        Log.e(tag,arg1.toString());

    }
    @Override
    public void onPrepared(MediaPlayer arg0) {
        // TODO Auto-generated method stub
        vv_video.start();

    }

    public void save(View v) {
        vv_video.stopPlayback();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vv_video != null) {
            vv_video.stopPlayback();
        }
    }
}
