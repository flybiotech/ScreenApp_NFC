package com.screening.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.activity.BaseActivity;
import com.activity.R;
import com.screening.adapter.CaseManagerAdapter;

import com.screening.model.Item;
import com.screening.model.ListMessage;
import com.screening.uitls.MessageSelectUtils;
import com.view.LoadingDialog;


import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//信息管理列表
public class CaseListManagerActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView tv_title, tv_empty;
    private Button bt_left, bt_right;
    private ListView list_caselist_01;
    private CaseManagerAdapter caseManagerAdapter;
    private List<ListMessage> listDataMessages = new ArrayList<>();
    private MessageSelectUtils messageSelectUtils;
    private String searchFields;
    private Disposable disposable;
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_list_manager);
        initView();
        initData();
        initClick();
    }


    private void initClick() {
        bt_left.setOnClickListener(this);
        list_caselist_01.setOnItemClickListener(this);
    }

    private void initData() {
        searchFields = getIntent().getStringExtra("fields");
    }

    private void initView() {

        tv_title = findViewById(R.id.title_text);
        tv_title.setText(getString(R.string.listMessage));
        bt_left = findViewById(R.id.btn_left);
        bt_right = findViewById(R.id.btn_right);
        bt_right.setVisibility(View.INVISIBLE);
        bt_left.setVisibility(View.VISIBLE);
        bt_left.setText(getString(R.string.title_back));
        list_caselist_01 = findViewById(R.id.list_caselist_01);
        tv_empty = findViewById(R.id.tv_empty);
        messageSelectUtils = new MessageSelectUtils();

    }

    @Override
    protected void onResume() {
        super.onResume();
        showUserDataList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                Intent intent = new Intent(CaseListManagerActivity.this, MainActivity.class);
                intent.putExtra("canshu", 1);
                startActivity(intent);
                break;
        }
    }


    private void showUserDataList() {
        showDiolog(getString(R.string.patient_select_patients_message));
        Observable.create(new ObservableOnSubscribe<List<ListMessage>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ListMessage>> emitter) throws Exception {
                if (messageSelectUtils != null) {
                    emitter.onNext(messageSelectUtils.searchUser(searchFields));
                } else {
                    emitter.onError(new Throwable());
                }
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ListMessage>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(List<ListMessage> list) {
                        dismissDiolog();
                        listDataMessages = list;
                        if (list.size() >= 0) {
                            tv_empty.setText("");
                        }
                        caseManagerAdapter = new CaseManagerAdapter(CaseListManagerActivity.this, list);
                        list_caselist_01.setAdapter(caseManagerAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissDiolog();
                        Toasty.error(CaseListManagerActivity.this, "数据查询有异常");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void showDiolog(String msg) {
        try {
            if (mDialog != null && mDialog.isShow()) {
                mDialog.setMyMessage(msg);
            } else {
                if (mDialog == null) {
                    mDialog = new LoadingDialog(this, "fly");
                }
                mDialog.setMyMessage(msg);
                mDialog.dialogShow();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void dismissDiolog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dismissDiolog();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long lid) {
        if (listDataMessages != null && listDataMessages.size() > 0) {
            String id = listDataMessages.get(position).getpId();
            Intent intent = new Intent(CaseListManagerActivity.this, CaseListShowActivity.class);
            intent.putExtra("message", id);
            startActivity(intent);
        }
    }
}
