package com.screening.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.activity.R;

public class ScanningSeleActivity extends AppCompatActivity {
    private Button btn_left, btn_right;
    private TextView title_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning_sele);
        initVeiw();
        initClick();
    }

    private void initClick() {
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initVeiw() {
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        title_bar = findViewById(R.id.title_bar);
        btn_left.setVisibility(View.VISIBLE);
        title_bar.setVisibility(View.VISIBLE);
        title_bar.setText("扫描选择");
    }

}
