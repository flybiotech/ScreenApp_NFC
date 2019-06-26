package com.screening.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.R;
//import com.huashi.bluetooth.HSBlueApi;
//import com.huashi.bluetooth.HsInterface;
//import com.huashi.bluetooth.IDCardInfo;
import com.screening.uitls.BluetoothUtils;
import com.util.Constss;
import com.util.SouthUtil;
import com.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener {
    private Button blue_link_break, blue_link, btn_right, btn_left;
    private EditText edit_bluetooth;
    private TextView title_bar;
    private ListView blue_listview;//显示搜索到的蓝牙信息
    String filepath = "";
//    private HSBlueApi api;
//    private IDCardInfo ic;
    private boolean isConn = false;
    int ret;
    private MyAdapter adapter;
    private List<BluetoothDevice> bundDevices;
    private List<BluetoothDevice> notDevices;
    private View diaView;
    private TextView tv_ts;
    private static final int REQUEST_ENABLE = 1;
    private BluetoothUtils bluetoothUtils;
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        initView();
        initClick();
//        initData();

    }

    @Override
    protected void onStart() {
        super.onStart();
        String blue_name = bluetoothUtils.initGetBlue(Constss.Bluetooth_Key);
        edit_bluetooth.setText(blue_name);
    }

    private void initClick() {
        blue_link_break.setOnClickListener(this);
        blue_link.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        btn_left.setOnClickListener(this);
    }

    private void showDiolog(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog != null && mDialog.isShow()) {
                    mDialog.setMessage(msg);
                } else {
                    if (mDialog == null) {
                        mDialog = new LoadingDialog(BluetoothActivity.this, true);
                    }
                    mDialog.setMessage(msg);
                    mDialog.dialogShow();
                }
            }
        });
    }

    private void dismissDiolog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    private void initView() {
        filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wltlib";// 授权目录
        blue_link = findViewById(R.id.blue_link);
        blue_link.setVisibility(View.GONE);
        blue_link_break = findViewById(R.id.blue_link_break);
        edit_bluetooth = findViewById(R.id.edit_bluetooth);
        bundDevices = new ArrayList<>();
        notDevices = new ArrayList<>();
        btn_right = findViewById(R.id.btn_right);
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setText(getString(R.string.wifiSetSearch));
        btn_left = findViewById(R.id.btn_left);
        title_bar = findViewById(R.id.title_bar);
        title_bar.setText(getString(R.string.setting_blue));

        btn_left.setVisibility(View.VISIBLE);
        bluetoothUtils = new BluetoothUtils(this);
        mDialog = new LoadingDialog(BluetoothActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        initBlueBreak();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blue_link:
                break;
            case R.id.blue_link_break:
                if (Constss.isConnBlue) {
//                    initBlueBreak();
                } else {
                    SouthUtil.showToast(this, getString(R.string.blue_no_connect));
                }
                break;
            case R.id.btn_right:
                if (bluetoothUtils.getBluetooth()) {
//                    initSerchBlue();
                } else {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE);
                }
                break;
            case R.id.btn_left:
                finish();
                break;
            default:
                break;
        }
    }
//
///
//    private void initData() {
//        adapter = new MyAdapter();
//        Constss.hsBlueApi.setmInterface(new HsInterface() {
//            @Override
//            public void reslut2Devices(Map<String, List<BluetoothDevice>> map) {
//                bundDevices = map.get("bind");
//                notDevices = map.get("notBind");
//                Log.e("已绑定", bundDevices.size() + ",,," + notDevices.size());
//                adapter.notifyDataSetChanged();
//            }
//        });
//    }

//    //开始搜索蓝牙
//    private void initSerchBlue() {
//        if (Constss.isConnBlue) {
//            SouthUtil.showToast(BluetoothActivity.this, getString(R.string.BlueConnected));
//            Log.e("已绑定1", getString(R.string.BlueConnected));
//            return;
//        }
//        if (bundDevices != null && notDevices != null) {
//            bundDevices.clear();
//            notDevices.clear();
//        }
//        diaView = View.inflate(BluetoothActivity.this, R.layout.test, null);
//        tv_ts = (TextView) diaView.findViewById(R.id.tv_ts);
//        blue_listview = (ListView) diaView.findViewById(R.id.lv);
//        blue_listview.setAdapter(adapter);
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle(getString(R.string.setting_blue_title)).setView(diaView)//在这里把写好的这个listview的布局加载dialog中
//                .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                }).create();
//        dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
//        dialog.show();
//        blue_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0 || position == bundDevices.size() + 1) {
//                    return;
//                }
//                dialog.cancel();
////                showDiolog(getString(R.string.linkingBlue));
//                BluetoothDevice d = null;
//                if (position < bundDevices.size() + 1) {
//                    d = bundDevices.get(position - 1);
//                } else {
//                    d = notDevices.get(position - 2 - bundDevices.size());
//                }
//                int ret = Constss.hsBlueApi.connect(d.getAddress());
//                if (ret == 0) {
//                    Constss.isConnBlue = true;
//                    Log.e("已绑定2", getString(R.string.BlueConnected));
//                    SouthUtil.showToast(BluetoothActivity.this, getString(R.string.BlueConnected));
//                    String sam = d.getName();
//                    edit_bluetooth.setText(sam);
//                    bluetoothUtils.initSaveBlue(Constss.Bluetooth_Path, d.getAddress());//蓝牙mac的地址
//                    bluetoothUtils.initSaveBlue(Constss.Bluetooth_Key, d.getName());//蓝牙名称的地址
//                } else {
//                    Log.e("已绑定3", getString(R.string.BlueConnected));
//                    SouthUtil.showToast(BluetoothActivity.this, getString(R.string.BlueConnected));
//                }
//                dismissDiolog();
//            }
//        });
//        Log.e("Constss.hsBlueApi", Constss.hsBlueApi + "");
//        Constss.hsBlueApi.scanf();
//        tv_ts.setVisibility(View.INVISIBLE);
//        blue_listview.setVisibility(View.VISIBLE);
//
//    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return bundDevices.size() + notDevices.size() + 2;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                TextView tv = new TextView(BluetoothActivity.this);
                tv.setVisibility(View.GONE);
                return tv;
            }
            if (position == bundDevices.size() + 1) {
                TextView tv = new TextView(BluetoothActivity.this);
                tv.setVisibility(View.GONE);
                return tv;
            }
            View v = null;
            ViewHodler hodler;
            BluetoothDevice device = null;
            if (position < bundDevices.size() + 1) {
                if (position > 0) {
                    device = bundDevices.get(position - 1);
                } else {
                    return null;
                }
            } else {
                device = notDevices.get(position - 2 - bundDevices.size());

            }
            if (convertView != null && convertView instanceof LinearLayout) {
                v = convertView;
                hodler = (ViewHodler) convertView.getTag();
            } else {
                v = View.inflate(BluetoothActivity.this, R.layout.dialog, null);
                hodler = new ViewHodler();
                hodler.tv_name = (TextView) v.findViewById(R.id.name);
                hodler.tv_address = (TextView) v.findViewById(R.id.mac);
                convertView = v;
                convertView.setTag(hodler);
            }
            hodler.tv_name.setText(device.getName());
            hodler.tv_address.setText(device.getAddress());
            return v;
        }

        class ViewHodler {
            private TextView tv_name, tv_address;
        }
    }
}
