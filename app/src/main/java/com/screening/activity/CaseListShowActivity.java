package com.screening.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.activity.BaseActivity;
import com.activity.R;
import com.activity.VideoPlayerActivity;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.screening.model.ListMessage;
import com.screening.uitls.CaseListUtils;
import com.screening.uitls.ReListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//展示受检者的详细信息
public class CaseListShowActivity extends BaseActivity implements CaseListUtils.OnShowContentListener, OnItemClickListener, AdapterView.OnItemClickListener {
    private TextView tv_dengji_clm, tv_title, tv_imagenameshow01;
    private Button bt_left, bt_right;
    private CaseListUtils caseListUtils;
    private ConvenientBanner banner;
    private ListView recycleVideo;
    private String id;
    private ListMessage msg = null;
    private List<String> imageAll = null;
    private List<String> videoPathlist = null;
    private Adapter adapter;//视频展示的适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_list_show);
        initView();
        initClick();
    }

    private void initClick() {
        bt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {

        Intent intent = getIntent();
        id = intent.getStringExtra("message");

        tv_dengji_clm = findViewById(R.id.tv_dengji_clm);
        banner = findViewById(R.id.cb_show);
        tv_imagenameshow01 = findViewById(R.id.tv_imagenameshow01);
        tv_title = findViewById(R.id.title_text);
        recycleVideo = findViewById(R.id.case_recycler_vdieo);
        tv_title.setText(getString(R.string.detialMessage));
        bt_left = findViewById(R.id.btn_left);
        bt_left.setText(getString(R.string.patient_return));
        bt_left.setVisibility(View.VISIBLE);
        bt_right = findViewById(R.id.btn_right);
        bt_right.setVisibility(View.INVISIBLE);
        recycleVideo.setOnItemClickListener(this);
        banner.setOnItemClickListener(this);

        caseListUtils = new CaseListUtils(CaseListShowActivity.this, banner, tv_imagenameshow01);

        caseListUtils.initView(id, CaseListShowActivity.this);

    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void initView(List<ListMessage> listMsg) {
        if (listMsg != null && listMsg.size() > 0) {
            msg = listMsg.get(0);
            caseListUtils.initViewContent(tv_dengji_clm, msg);

            caseListUtils.startImageShow(msg, getString(R.string.image_artword), getString(R.string.image_acetic_acid_white),
                    getString(R.string.image_Lipiodol), CaseListShowActivity.this);
        }


    }

    @Override
    public void showImage(List<String> listImagePath) {
        Log.i("TAG_1", "showImage: listImagePath = " + listImagePath.size());
        imageAll = listImagePath;
        if (imageAll != null && imageAll.size() > 0) {
            tv_imagenameshow01.setText("图片展示 ： " + new File(imageAll.get(0)).getName());
        }

        caseListUtils.lunbo(imageAll);
        caseListUtils.videoShow(msg, this);
    }

    private List<String> videoNameList = null;

    @Override
    public void showVideo(List<String> listVideoPath) {
        if (listVideoPath != null && listVideoPath.size() > 0) {
            videoPathlist = listVideoPath;
            showVideoNameList();
        }


    }

    //展示视频列表时，只展示视频的名字
    private void showVideoNameList() {

        if (videoNameList == null) {
            videoNameList = new ArrayList<>();
        } else {
            videoNameList.clear();
        }
        File file = null;
        for (int i = 0; i < videoPathlist.size(); i++) {
            file = new File(videoPathlist.get(i));
            videoNameList.add(file.getName());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, videoNameList);
        recycleVideo.setAdapter((ListAdapter) adapter);
        new ReListView().setListViewHeightBasedOnChildren(recycleVideo);
    }


    // 轮播图的点击事件
    @Override
    public void onItemClick(int position) {
        if (imageAll != null) {
            Intent intent = new Intent(this, PreviewActivity.class);
            intent.putExtra("msg", imageAll.get(position));
            this.startActivity(intent);
        }

    }


    //adapter 的点击事件，在这里是视频列表的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (videoPathlist != null && videoPathlist.size() > 0) {
            Intent intent = new Intent(this, VideoPlayerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videoPath", videoPathlist.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }


}
