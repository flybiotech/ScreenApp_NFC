package com.screening.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.activity.R;


import java.util.ArrayList;
import java.util.List;

public class ScreeningRelatedActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView lv_scan_relate;
    private TextView tv_title;
    private Button bt_left, bt_right;
    List<String> list;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_related);
        initView();
        initClick();
        initData();
        initItemClick();
    }

    private void initItemClick() {
        lv_scan_relate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
//                        new ScreenJsonUtils(ScreeningRelatedActivity.this).searchListMessage();
                        break;
                    case 1:
//                        new CreateScreeningUtils(ScreeningRelatedActivity.this).selectJobIdResult();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initClick() {
        bt_left.setOnClickListener(this);
    }

    private void initData() {
        list = new ArrayList<>();
//        list.add(getString(R.string.createScanning));
//        list.add(getString(R.string.scanningResult));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        lv_scan_relate.setAdapter(adapter);
    }

    private void initView() {
        lv_scan_relate = findViewById(R.id.lv_scan_relate);
        tv_title = findViewById(R.id.title_bar);
        bt_left = findViewById(R.id.btn_left);
        bt_right = findViewById(R.id.btn_right);
        bt_left.setText(getString(R.string.patient_return));
        bt_left.setVisibility(View.VISIBLE);
        bt_right.setVisibility(View.GONE);
        tv_title.setText(getString(R.string.scanningRelate));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            default:
                break;
        }
    }
}
