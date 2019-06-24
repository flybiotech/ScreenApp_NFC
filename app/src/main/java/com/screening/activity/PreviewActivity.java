package com.screening.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.activity.BaseActivity;
import com.activity.R;
import com.bumptech.glide.Glide;
import com.screening.ui.ZoomImageView;

public class PreviewActivity extends BaseActivity {
    Bitmap bp = null;
    ZoomImageView imageview;//自定义imageview，可以随着手势放大或缩小

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//禁止屏幕休眠
        setContentView(R.layout.activity_preview);
        imageview = (ZoomImageView) findViewById(R.id.iv);
        Intent intent = getIntent();
        String pathName = intent.getStringExtra("msg");
        bp = BitmapFactory.decodeFile(pathName);//通过传递过来的路径转化为bitmap
        Glide.with(getApplicationContext())//通过第三方框架展示传递过来的图片
                .load(pathName)
                .crossFade()
                .into(imageview);
        imageview.setImage(bp);
    }
}
