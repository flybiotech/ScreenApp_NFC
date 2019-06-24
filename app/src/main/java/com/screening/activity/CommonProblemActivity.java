package com.screening.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.activity.BaseActivity;
import com.activity.R;
import com.screening.adapter.ScreenFaildAdapter;
import com.screening.model.ListMessage;
import com.screening.model.ScreenErrorList;
import com.util.Constss;
import com.util.SouthUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class CommonProblemActivity extends BaseActivity implements View.OnClickListener {

    private Button btnBack;
    private TextView title;
    private List<ScreenErrorList> screenListSuccessList;
    private ScreenFaildAdapter listMessageAdapter;
    private ListView lv_show;
    private TextView tv_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_problem);
        init();

    }

    private void init() {
        btnBack = (Button) findViewById(R.id.btn_left);
        title = (TextView) findViewById(R.id.title_bar);
        lv_show = findViewById(R.id.lv_show);
        tv_empty = findViewById(R.id.tv_empty);
        title.setText(getString(R.string.setting_error_query));
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        showErrorMsg();
    }

    /**
     * 展示信息
     */
//    private void showFaild msg(){
//        screenListSuccessList = LitePal.findAll(ScreenErrorList.class);
//        if(screenListSuccessList.size()>0){
//            tv_empty.setText("");
//            listMessageAdapter=new ScreenFaildAdapter(this,screenListSuccessList);
//            lv_show.setAdapter(listMessageAdapter);
//            listMessageAdapter.notifyDataSetChanged();
//        }else {
//            SouthUtil.showToast(this,getString(R.string.patient_data_no));
//        }
//    }
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


    @SuppressLint("CheckResult")
    private void showErrorMsg() {

        Observable.create(new ObservableOnSubscribe<List<ScreenErrorList>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ScreenErrorList>> emitter) throws Exception {
                if (screenListSuccessList == null) {
                    screenListSuccessList = new ArrayList<>();
                } else {
                    screenListSuccessList.clear();
                }
                screenListSuccessList = getScreenDB();
                emitter.onNext(screenListSuccessList);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ScreenErrorList>>() {
                    @Override
                    public void accept(List<ScreenErrorList> screenErrorLists) throws Exception {
                        showTextInfo(screenErrorLists);
                    }
                });

    }


    private List<ScreenErrorList> getScreenDB() {
        List<ScreenErrorList> list = new ArrayList<>();
        //记录 创建筛查时的异常
        List<ListMessage> listMsg = LitePal.where("errorMsg != ? ", "").find(ListMessage.class);
        if (listMsg != null && listMsg.size() > 0) {
            for (int i = 0; i < listMsg.size(); i++) {
                ScreenErrorList sel = new ScreenErrorList();
                sel.setpId(listMsg.get(i).getpId());
                sel.setName(listMsg.get(i).getName());
                sel.setScreeningId(listMsg.get(i).getScreeningId());
                sel.setErrorMsg(listMsg.get(i).getErrorMsg());
                list.add(sel);
            }
        }

        //记录备份时，出现的异常
        list.addAll(LitePal.findAll(ScreenErrorList.class));

        return list;
    }


    private void showTextInfo(List<ScreenErrorList> screenList) {

        if (screenList.size() > 0) {
            tv_empty.setText("");
            listMessageAdapter = new ScreenFaildAdapter(CommonProblemActivity.this, screenList);
            lv_show.setAdapter(listMessageAdapter);
            listMessageAdapter.notifyDataSetChanged();
        } else {
            SouthUtil.showToast(this, getString(R.string.patient_data_no));
        }
    }


}
