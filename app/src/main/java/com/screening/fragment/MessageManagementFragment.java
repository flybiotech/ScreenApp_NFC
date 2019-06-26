package com.screening.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.activity.R;
import com.application.MyApplication;
import com.bigkoo.pickerview.TimePickerView;
import com.logger.LogHelper;
import com.screening.activity.CaseListManagerActivity;
import com.screening.uitls.MessageSelectUtils;
import com.util.AlignedTextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * 信息管理界面
 */
public class MessageManagementFragment extends Fragment implements View.OnClickListener {
    private EditText editName, editTel, edit_casesearch_01, edit_casesearch_02;
    private TextView tv01, tv02, tv03, tv04, tv05, tvStartDate, tvEndDate;
    private String[] nameTv;
    private TextView tv;
    private Button startBtn, bt_left, bt_right;
    private MessageSelectUtils messageSelectUtils;
    private Disposable disposable;
    private long timeStart = 0;
    private long timeStop = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_management, container, false);
        initView(view);
        initTextView(view);
        return view;
    }

    private void initView(View view) {
        nameTv = new String[]{getString(R.string.pName), getString(R.string.RequiredPhone), getString(R.string.localPhone), getString(R.string.RequiredID), getString(R.string.patient_select_data)};
        editName = (EditText) view.findViewById(R.id.edit_casesearch_Name);
        editTel = (EditText) view.findViewById(R.id.edit_casesearch_tel);
        edit_casesearch_01 = (EditText) view.findViewById(R.id.edit_casesearch_01);
        edit_casesearch_02 = view.findViewById(R.id.edit_casesearch_02);
        tvStartDate = view.findViewById(R.id.edit_startdate);//开始日期
        tvEndDate = view.findViewById(R.id.edit_enddate);//结束日期

        startBtn = (Button) view.findViewById(R.id.btn_casesearch_search);
        bt_left = (Button) view.findViewById(R.id.btn_left);//左边按钮
        bt_right = (Button) view.findViewById(R.id.btn_right);//右边的按钮
        startBtn.setOnClickListener(this);
        tv = (TextView) view.findViewById(R.id.title_text);//title
        bt_right.setVisibility(View.GONE);
        tv.setText(getString(R.string.case_title));//改变页面标题3
        messageSelectUtils = new MessageSelectUtils(getActivity(), editName, editTel, edit_casesearch_01, edit_casesearch_02);

        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);


    }

    private void initTextView(View view) {
        tv01 = (TextView) view.findViewById(R.id.tv_casesearch_01);
        tv02 = (TextView) view.findViewById(R.id.tv_casesearch_02);
        tv03 = (TextView) view.findViewById(R.id.tv_casesearch_03);
        tv04 = (TextView) view.findViewById(R.id.tv_casesearch_04);
        tv05 = (TextView) view.findViewById(R.id.tv_casesearch_05);

        TextView[] tvID = {tv01, tv02, tv03, tv04, tv05};
        for (int i = 0; i < nameTv.length; i++) {
            tvID[i].setText(AlignedTextUtils.justifyString(nameTv[i], 4));
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        LogHelper.i("开始时间："+timeStart+"， 结束时间："+timeStop);
        if (timeStart == 0) {
            tvStartDate.setText("");
        }

        if (timeStop == 0) {
            tvEndDate.setText("");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:

                break;

            case R.id.edit_startdate:
                getTime(tvStartDate, 1);
                break;

            case R.id.edit_enddate:
                getTime(tvEndDate, 2);
                break;

            //开始查询
            case R.id.btn_casesearch_search:
                jumpActivity();
                break;
        }
    }


    private void jumpActivity() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                if (messageSelectUtils != null) {
                    emitter.onNext(messageSelectUtils.getFields(editName.getText().toString().trim(),
                            editTel.getText().toString().trim(),
                            edit_casesearch_01.getText().toString().trim(),
                            edit_casesearch_02.getText().toString().trim(),
                            timeStart, timeStop));
                } else {
                    emitter.onError(new Throwable());
                }
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(String fields) {

                        Intent intent = new Intent(getActivity(), CaseListManagerActivity.class);
                        intent.putExtra("fields", fields);
                        getActivity().startActivity(intent);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(getActivity(), "数据查询有异常");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void getTime(TextView view, int type) {
        TimePickerView pVTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (type == 1) {
                    timeStart = date.getTime();
                } else {
                    timeStop = date.getTime();
                }
                LogHelper.i(" 11 timeStart = "+timeStart+" timeStop = "+timeStop);

                if (timeStart > System.currentTimeMillis()) {
                    timeStart = 0;
                    view.setText("");
                    Toasty.normal(MyApplication.getContext(), "开始日期不能大于当前日期").show();
                    return;
                }

                if (timeStop > 0 && timeStop < timeStart) {
                    if (type == 1) {
                        timeStart = 0;

                        Toasty.normal(MyApplication.getContext(), "开始日期不能大于结束日期").show();
                    } else {
                        timeStop = 0;
                        Toasty.normal(MyApplication.getContext(), "结束日期不能小于开始日期").show();
                    }

                    view.setText("");

                } else {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
                    view.setText(sf.format(date));
                }


            }
        })
                .setType(new boolean[]{true, true, true, false, false, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("年", "月", "日", "", "", "")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .setDividerColor(Color.RED)
                .setTextColorCenter(R.color.screen_have)//设置选中项的颜色
                .setTextColorOut(R.color.screen_no)//设置没有被选中项的颜色
                .setContentSize(20)
                .build();

        pVTime.show();
    }


    @Override
    public void onStop() {
        super.onStop();
        timeStart = 0;
        timeStop = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }

    }
}
