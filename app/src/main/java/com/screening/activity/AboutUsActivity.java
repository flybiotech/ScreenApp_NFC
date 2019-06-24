package com.screening.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.activity.BaseActivity;
import com.activity.R;
import com.util.AlignedTextUtils;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_banben_name, tv_banben_model, tv_banben_version, tv_banben_all, tv_banben_model_ancer, tv_SN;
    private TextView tv_banben_name_ancer, tv_banben_version_ancer, tv_banben_all_ancer, tv_app_version_hint;
    private String[] tv_banben;
    private TextView tv_title;
    private Button bt_left;
    private EditText et_SN;
    String versionName;//版本号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        init();


    }

    private void init() {
//        et_SN = findViewById(R.id.et_SN);
//        tv_SN = findViewById(R.id.tv_SN);
        tv_title = (TextView) findViewById(R.id.title_bar);
        bt_left = (Button) findViewById(R.id.btn_left);
        bt_left.setVisibility(View.VISIBLE);
        tv_banben_name = (TextView) findViewById(R.id.tv_banben_name);
        tv_app_version_hint = (TextView) findViewById(R.id.tv_app_version_hint);
        TextPaint tp = tv_app_version_hint.getPaint();
        tp.setFakeBoldText(true);
        tv_banben_model = (TextView) findViewById(R.id.tv_banben_model);
        tv_banben_version = (TextView) findViewById(R.id.tv_banben_version);
        tv_banben_all = (TextView) findViewById(R.id.tv_banben_all);
        tv_banben_name_ancer = (TextView) findViewById(R.id.tv_banben_name_ancer);
        tv_banben_model_ancer = (TextView) findViewById(R.id.tv_banben_model_ancer);
        tv_banben_version_ancer = (TextView) findViewById(R.id.tv_banben_version_ancer);
        tv_banben_all_ancer = (TextView) findViewById(R.id.tv_banben_all_ancer);
    }

    private void initData() {
        versionName = getVersionName(this);
        bt_left.setOnClickListener(this);
        tv_title.setText(getString(R.string.setting_about_us));
        tv_banben_name_ancer.setText(getString(R.string.setting_app_name1));
        tv_banben_model_ancer.setText(getString(R.string.setting_Version_specification_model_ancer));
        tv_banben_version_ancer.setText(getString(R.string.setting_version_ancer) + versionName);
//        tv_banben_all_ancer.setText(getString(R.string.setting_version_all_ancer));
        tv_banben = new String[]{getString(R.string.setting_version_message), getString(R.string.setting_app_name),
                getString(R.string.setting_Version_specification_model), getString(R.string.setting_version)};
        TextView[] tvData = {tv_app_version_hint, tv_banben_name, tv_banben_model, tv_banben_version, tv_banben_all};
        for (int i = 0; i < tv_banben.length; i++) {
            if (tvData[i] != null) {
                tvData[i].setText(AlignedTextUtils.justifyString(tv_banben[i], 6));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_left:
                finish();
                break;
        }

    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }


}
