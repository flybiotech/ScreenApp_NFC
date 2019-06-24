package com.screening.fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.activity.R;
import com.screening.activity.BarcodeSettingActivity;
import com.screening.activity.ScanShowActivity;
import com.screening.activity.ScanningActivity;
import com.screening.activity.ShowActivity;
import com.screening.adapter.ListMessageAdapter;
import com.screening.model.Addmessage;
import com.screening.uitls.BardcodeSettingUtils;
import com.screening.uitls.Constant;
import com.screening.uitls.SPUtils;
import com.screening.uitls.SearchMessage;
import com.util.Constss;
import com.util.SouthUtil;
import com.util.ToastUtils;
import com.view.LoadingDialog;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 展示录入信息界面，如果已导入数据，显示该界面
 */
public class ListFragment extends Fragment implements View.OnClickListener {
    private EditText edittext;
    private ImageView imageview, iv_add, iv_scanning;
    private ListMessageAdapter listMessageAdapter;
    private List<Addmessage> list;//展示的数据源
    private SearchMessage searchMessage;
    private ListView lv_show;
    private LoadingDialog loadingDialog;//导出时进度条
    private TextView tv_empty;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                initData();
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initView(view);
        initTextClick();
        initClick();
        initItmClick();
        return view;
    }

    private void initItmClick() {
        lv_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowActivity.class);
                intent.putExtra("id", Integer.parseInt(list.get(i).getpId()));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        initCopy();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getActivity());
        }
        loadingDialog.setMessage(getString(R.string.loadingMEssage));
        loadingDialog.dialogShow();
        //设置当前WiFi连接的类型
        SPUtils.put(getActivity(), Constss.WIFI_TYPE_KEY, Constss.WIFI_TYPE_SZB);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.what = 1;
                list = searchMessage.addData();
                handler.sendMessage(message);
            }
        }).start();

    }

    private void initData() {
        if (list.size() > 0) {
            tv_empty.setText("");
        }
        Collections.reverse(list);
        listMessageAdapter = new ListMessageAdapter(getActivity(), list);
        lv_show.setAdapter(listMessageAdapter);
        listMessageAdapter.notifyDataSetChanged();

    }

    private void initClick() {
        iv_add.setOnClickListener(this);
//        add.setOnClickListener(this);
        imageview.setOnClickListener(this);
        iv_scanning.setOnClickListener(this);
    }

    private void initView(View view) {
        searchMessage = new SearchMessage();
        tv_empty = view.findViewById(R.id.tv_empty);
        edittext = view.findViewById(R.id.edittext);
        edittext.clearFocus();
        imageview = view.findViewById(R.id.imageview);
////        add = view.findViewById(R.id.add);
        iv_add = view.findViewById(R.id.iv_add);
//        iv_serach.setVisibility(View.GONE);
        lv_show = view.findViewById(R.id.lv_show);
        lv_show.requestFocus();
        iv_scanning = view.findViewById(R.id.iv_scanning);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), BarcodeSettingActivity.class);
        switch (view.getId()) {
            case R.id.iv_scanning://扫描二维码信息
                if (BardcodeSettingUtils.isBarcode(getContext(), Constant.barcode_key)) {
                    startActivityForResult(new Intent(getActivity(), ScanningActivity.class), 1);
                } else {
                    ToastUtils.showToast(getContext(), getString(R.string.barcode_setting));
                    getActivity().startActivity(intent);
                }

                break;
            case R.id.iv_add://添加
                if (BardcodeSettingUtils.isBarcode(getContext(), Constant.barcode_key)) {
                    startActivity(new Intent(getActivity(), ScanShowActivity.class).putExtra("id", 1));
                } else {
                    ToastUtils.showToast(getContext(), getString(R.string.barcode_setting));
                    getActivity().startActivity(intent);
                }

                break;
            case R.id.imageview:
                //把EditText内容设置为空
                edittext.setText("");
                break;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == 1) {
                String msg = data.getStringExtra("text");
                Log.e("text", msg);
                if (!msg.equals("") && msg.length() > 15 && msg.contains("wechatOpenId")) {
                    Intent intent = new Intent(getActivity(), ShowActivity.class);
                    intent.putExtra("msg", msg);
                    startActivity(intent);
                } else {
                    SouthUtil.showToast(getContext(), getString(R.string.scanning_faild));
                    startActivityForResult(new Intent(getActivity(), ScanningActivity.class), 1);
                }
            }
        }
    }

    private void initTextClick() {
        //EditText添加监听
        edittext.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }//文本改变之前执行

            @Override
            //文本改变的时候执行
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果长度为0
                if (s.length() == 0) {
                    //隐藏“删除”图片
                    imageview.setVisibility(View.GONE);
                } else {//长度不为0
                    //显示“删除图片”
                    imageview.setVisibility(View.VISIBLE);
                }

            }

            public void afterTextChanged(Editable s) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String msg = edittext.getText().toString().trim();
                        if (msg != null && !msg.equals("")) {//当输入查询条件时，
                            list = searchMessage.vagueSelect(msg);
                        } else {//未输入查询条件，查询全部
                            list = searchMessage.addData();
                        }
                        handler.sendMessage(message);
                    }
                }).start();
            }//文本改变之后执行
        });
    }

    String data;

    //获取粘贴板上的值，并对其进行解析，存放到数据库中
    private void initCopy() {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = cm.getPrimaryClip();
        if (clipData != null) {
            ClipData.Item item = clipData.getItemAt(0);
            data = item.getText().toString();
            if (data != null && !data.equals("") && data.length() > 60) {
//            initJson(data);
                Intent intent = new Intent(getContext(), ShowActivity.class);
                intent.putExtra("msg", data);
                startActivity(intent);
            }
        }

    }
}
