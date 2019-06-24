package com.screening.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.activity.BaseActivity;
import com.activity.R;
import com.logger.LogHelper;
import com.screening.fragment.ListFragment;
import com.screening.fragment.MessageManagementFragment;
import com.screening.fragment.SystemFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private TextView textView_home_patientInfo, textView_home_casemanagerInfo, textView_home_settingInfo;
    private List<Fragment> fragmentList = new ArrayList<>();
    private int mState = -1;
    private FragmentManager ftManager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Intent intent = getIntent();
        int canshu = intent.getIntExtra("canshu", 0);//默认为登记页面
        if (canshu == 0) {
            switchState(0);//跳到患者信息fragment

        } else if (canshu == 2) {//跳到系统中心界面
            switchState(2);

        } else if (canshu == 1) {//跳到信息管理界面/
            switchState(1);

        }

        LogHelper.v("canshu=" + canshu);

    }

    private void initView() {
        textView_home_casemanagerInfo = findViewById(R.id.textView_home_casemanagerInfo);
        textView_home_patientInfo = findViewById(R.id.textView_home_patientInfo);
        textView_home_settingInfo = findViewById(R.id.textView_home_settingInfo);
        textView_home_settingInfo.setOnClickListener(this);
        textView_home_patientInfo.setOnClickListener(this);
        textView_home_casemanagerInfo.setOnClickListener(this);
        fragmentList.clear();
        ListFragment listFragment = new ListFragment();
        MessageManagementFragment messageManagementFragment = new MessageManagementFragment();
        SystemFragment systemFragment = new SystemFragment();
        fragmentList.add(listFragment);//信息列表
        fragmentList.add(messageManagementFragment);
        fragmentList.add(systemFragment);
        ftManager = getSupportFragmentManager();
        transaction = ftManager.beginTransaction();
        transaction.replace(R.id.fl_container, fragmentList.get(0));
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogHelper.v("");
    }

    @Override
    public void onClick(View view) {
        if (ftManager == null) {
            ftManager = getSupportFragmentManager();
        }
        transaction = ftManager.beginTransaction();

        switch (view.getId()) {
            case R.id.textView_home_patientInfo:
                transaction.replace(R.id.fl_container, fragmentList.get(0));
                switchState(0);

                break;
            case R.id.textView_home_casemanagerInfo:
                switchState(1);
                transaction.replace(R.id.fl_container, fragmentList.get(1));

                break;
            case R.id.textView_home_settingInfo:
                switchState(2);
                transaction.replace(R.id.fl_container, fragmentList.get(2));

                break;
        }
        transaction.commit();

    }

    //判断选中了哪个Fragment
    private void switchState(int state) {
        if (mState == state) {
            return;
        }
        mState = state;
        textView_home_patientInfo.setTextColor(Color.BLACK);
        textView_home_casemanagerInfo.setTextColor(Color.BLACK);
        textView_home_settingInfo.setTextColor(Color.BLACK);

        textView_home_patientInfo.setSelected(false);
        textView_home_casemanagerInfo.setSelected(false);
        textView_home_settingInfo.setSelected(false);


        switch (state) {
            case 0:
                textView_home_patientInfo.setTextColor(Color.RED);
                textView_home_patientInfo.setSelected(true);
                break;
            case 1:
                textView_home_casemanagerInfo.setTextColor(Color.RED);
                textView_home_casemanagerInfo.setSelected(true);
                break;
            case 2:
                textView_home_settingInfo.setTextColor(Color.RED);
                textView_home_settingInfo.setSelected(true);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogHelper.v("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogHelper.v("");
    }


}
