package com.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.adapter.ResoluteAdapter;
import com.model.DevModel;
import com.model.LampStateModel;
import com.model.RecordConfigModel;
import com.model.RetModel;
import com.model.SnapShotConfigModel;
import com.model.WIFIStateModel;
import com.network.Network;
import com.screening.uitls.SPUtils;
import com.suke.widget.SwitchButton;
import com.util.Constss;
import com.util.SouthUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by gyl1 on 12/22/16.
 */

public class ConfigActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    static final String tag = "ConfigActivity";
    int index; //
    DevModel model;
    Button btn_ap;
    Button btn_set_snapshot;//保存拍照设置的按钮
    Button btn_set_record;
    //    SwitchButton switchButton ;
    Button btn_sta;
    //    Spinner sp_snap0;//只拍一张或者连续拍照
    Spinner sp_snap1;//时长 3分钟或者5分钟或者6分钟或者8分钟
    Spinner sp_snap2;//时间间隔10秒或者15秒或者20秒或者30秒
    Spinner sp_record;

    Spinner sp_snap1_dy;//时长 3分钟或者5分钟或者6分钟或者8分钟
    Spinner sp_snap2_dy;//时间间隔10秒或者15秒或者20秒或者30秒


    private int snap1_index = 0; //记录选择的设置拍照的时长
    private int snap2_index = 0; //记录选择的拍照的间隔时间
    private int snap1_index_dy = 0; //记录选择的设置拍照的时长
    private int snap2_index_dy = 0; //记录选择的拍照的间隔时间

    private int record_index = 0; //记录选择了第几个 摄像的时间
    ResoluteAdapter snap0Adapter;
    ResoluteAdapter snap1Adapter;
    ResoluteAdapter snap2Adapter;
    ResoluteAdapter snap1Adapter_dy;
    ResoluteAdapter snap2Adapter_dy;
    ResoluteAdapter recordAdapter;
    RelativeLayout lay_set_apsta;
    RelativeLayout lay_snap_set;
    RelativeLayout lay_record_set;
    RelativeLayout lay_lamp_set;
    SwitchButton switchLamp0;
    SwitchButton switchLamp1;
    SwitchButton switchLamp2;
    //设置图片是否水平镜像 0 1 ，是否垂直反转 0 1
//    Button switch_buttonflip00;
//    Button switch_buttonflip01;
//    Button switch_buttonflip10;
//    Button switch_buttonflip11;
//    Button switch_buttonflip12;
    LinkedHashMap<String, Float> map_snap0Items = new LinkedHashMap<String, Float>();
    LinkedHashMap<String, Float> map_snap1Items = new LinkedHashMap<String, Float>();
    LinkedHashMap<String, Float> map_snap2Items = new LinkedHashMap<String, Float>();
    LinkedHashMap<String, Float> map_snap1Items_dy = new LinkedHashMap<String, Float>();
    LinkedHashMap<String, Float> map_snap2Items_dy = new LinkedHashMap<String, Float>();

    LinkedHashMap<String, Float> map_recordItems = new LinkedHashMap<String, Float>();
    List<String> sp_snap0Items = new ArrayList<>();
    List<String> sp_snap1Items = new ArrayList<>();
    List<String> sp_snap2Items = new ArrayList<>();
    List<String> sp_snap1Items_dy = new ArrayList<>();
    List<String> sp_snap2Items_dy = new ArrayList<>();
    List<String> sp_recordItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//禁止屏幕休眠
        setContentView(R.layout.activity_config);
        Bundle bundle = this.getIntent().getExtras();
        index = bundle.getInt("index");
        model=bundle.getParcelable("dev");
//        model = Constss.model;
        sp_snap1 = (Spinner) findViewById(R.id.sp_snap1);
        sp_snap2 = (Spinner) findViewById(R.id.sp_snap2);
        sp_snap1_dy = (Spinner) findViewById(R.id.sp_snap1_dy);
        sp_snap2_dy = (Spinner) findViewById(R.id.sp_snap2_dy);


        sp_record = (Spinner) findViewById(R.id.sp_record);
//        model= FragGetImage.listnode.get(index);
        handler.postDelayed(runnable, 500);
        handler.postDelayed(runnable1, 800);


    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            initView();
        }
    };
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {

            getConfig();
        }
    };

    public void initView() {
        btn_ap = (Button) findViewById(R.id.btn_ap);
        btn_sta = (Button) findViewById(R.id.btn_sta);

//        switchButton = (SwitchButton)findViewById(R.id.switch_button); //按键是否靠右
//        switch_buttonflip00 = (Button)findViewById(R.id.switch_buttonflip00);//是否水平镜像
//        switch_buttonflip01 = (Button)findViewById(R.id.switch_buttonflip01);//是否水平镜像
//        switch_buttonflip10 = (Button)findViewById(R.id.switch_buttonflip10);//是否水平镜像
//        switch_buttonflip11 = (Button)findViewById(R.id.switch_buttonflip11);//是否水平镜像
//        switch_buttonflip12 = (Button)findViewById(R.id.switch_buttonflip12);//是否水平镜像


//        sp_snap0 = (Spinner)findViewById(R.id.sp_snap0);
//        sp_snap1 = (Spinner)findViewById(R.id.sp_snap1);
//        sp_snap2 = (Spinner)findViewById(R.id.sp_snap2);
//        sp_record = (Spinner)findViewById(R.id.sp_record);
        btn_set_snapshot = (Button) findViewById(R.id.btn_set_snapshot);//拍照设置
        btn_set_record = (Button) findViewById(R.id.btn_set_record);
        switchLamp0 = (SwitchButton) findViewById(R.id.switch_lamp0);//白光
        switchLamp1 = (SwitchButton) findViewById(R.id.switch_lamp1);//绿光
        switchLamp2 = (SwitchButton) findViewById(R.id.switch_lamp2);//控制 拍照和录像 是否同时拍摄
        lay_set_apsta = (RelativeLayout) findViewById(R.id.lay_set_apsta);
        lay_snap_set = (RelativeLayout) findViewById(R.id.lay_snap_set);
        lay_record_set = (RelativeLayout) findViewById(R.id.lay_record_set);
        lay_lamp_set = (RelativeLayout) findViewById(R.id.lay_lamp_set);


        btn_ap.setOnClickListener(this);
        btn_sta.setOnClickListener(this);
        btn_set_snapshot.setOnClickListener(this);
        btn_set_record.setOnClickListener(this);
        switchLamp2.setChecked((boolean) SPUtils.get(this, "lamp", false));
//      sp_snap1

        switchLamp0.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                controlLamp(0, isChecked);
            }
        });
        switchLamp1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                controlLamp(1, isChecked);
            }
        });

        switchLamp2.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                SPUtils.put(ConfigActivity.this, "lamp", isChecked);

            }
        });


        initSpinner();
        resetLayoutParamters();
        setSpannerPosition();
    }


//    //设置图片是否镜像和反转
//    private  void getImageFlip(final int isMirror, final int isFlip) {
//        Logger.e("model.usr = "+model.usr+"    model.pwd = "+model.pwd);
//        Network.getCommandApi(model).getImageFlip(model.usr,model.pwd,40,isMirror,isFlip,SouthUtil.getRandom())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<RetModel>() {
//                    @Override
//                    public void onCompleted() {
//                        lod.dismiss();
//                    }
//                    @Override
//                    public void onError(Throwable e) {
//                        lod.dismiss();
//                        Logger.e("设置图片反转 RetModel 异常 : "+e);
////                                Log.e(tag,"设置录像时间 set record error:"+e.toString());
//                    }
//                    @Override
//                    public void onNext(RetModel retModel) {
//
//                        lod.dismiss();
//                        Logger.e("设置图片反转 RetModel : "+retModel.ret +"  isMirror = "+isMirror+"  isFlip = "+isFlip);
////                                Log.e(tag,"设置录像时间 set record ret:"+retModel.ret);
//
//                    }
//                });
//    }


    void resetLayoutParamters() {
        int totalExistHeight = SouthUtil.convertDpToPixel(50 + 40 + 40 + 40 + 40, this);
        WindowManager manage = this.getWindowManager();
        Display display = manage.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;

        int span = (screenHeight - totalExistHeight) / 6;
        RelativeLayout.LayoutParams pam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pam.topMargin = span;
        lay_set_apsta.setLayoutParams(pam);

        RelativeLayout.LayoutParams pam1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pam1.topMargin = span;
        pam1.addRule(RelativeLayout.BELOW, R.id.lay_set_apsta);
        lay_snap_set.setLayoutParams(pam1);


        RelativeLayout.LayoutParams pam2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pam2.topMargin = span;
        pam2.addRule(RelativeLayout.BELOW, R.id.lay_snap_set);
        lay_record_set.setLayoutParams(pam2);

        RelativeLayout.LayoutParams pam3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pam3.topMargin = span;
        pam3.addRule(RelativeLayout.BELOW, R.id.lay_record_set);
        lay_lamp_set.setLayoutParams(pam3);


    }

    public void initSpinner() {
//        map_snap0Items.put("只拍一张",Integer.valueOf(0));
//        map_snap0Items.put("连续拍照",Integer.valueOf(1));

//        map_snap1Items.put(getString(R.string.image_duration_one),Integer.valueOf(1));
//        map_snap1Items.put(getString(R.string.image_duration_two),Integer.valueOf(2));
//        map_snap1Items.put(getString(R.string.image_duration_three),Integer.valueOf(3));
//        map_snap1Items.put(getString(R.string.image_duration_five),Integer.valueOf(5));
//        map_snap1Items.put(getString(R.string.image_duration_six),Integer.valueOf(6));
//        map_snap1Items.put(getString(R.string.image_duration_eight),Integer.valueOf(8));
//        map_snap1Items.put(getString(R.string.image_duration_ten),Integer.valueOf(10));


        map_snap1Items.put(getString(R.string.image_duration_30seconds), Float.valueOf(0.5f));
        map_snap1Items.put(getString(R.string.image_duration_one), Float.valueOf(1.0f));
        map_snap1Items.put(getString(R.string.image_duration_minite), Float.valueOf(1.5f));
        map_snap1Items.put(getString(R.string.image_duration_two), Float.valueOf(2.0f));


        map_snap2Items.put(getString(R.string.image_interval_ten), Float.valueOf(10));
        map_snap2Items.put(getString(R.string.image_interval_fifteen), Float.valueOf(15));
        map_snap2Items.put(getString(R.string.image_interval_twenty), Float.valueOf(20));
        map_snap2Items.put(getString(R.string.image_interval_thirty), Float.valueOf(30));


//        map_snap1Items_dy.put(getString(R.string.image_duration_one),Float.valueOf(1));
//        map_snap1Items_dy.put(getString(R.string.image_duration_two),Float.valueOf(2));
//        map_snap1Items_dy.put(getString(R.string.image_duration_three),Float.valueOf(3));
//        map_snap1Items_dy.put(getString(R.string.image_duration_five),Float.valueOf(5));
//        map_snap1Items_dy.put(getString(R.string.image_duration_six),Float.valueOf(6));
//        map_snap1Items_dy.put(getString(R.string.image_duration_eight),Float.valueOf(8));
//        map_snap1Items_dy.put(getString(R.string.image_duration_ten),Float.valueOf(10));


        map_snap1Items_dy.put(getString(R.string.image_duration_30seconds), Float.valueOf(0.5f));
        map_snap1Items_dy.put(getString(R.string.image_duration_one), Float.valueOf(1.0f));
        map_snap1Items_dy.put(getString(R.string.image_duration_minite), Float.valueOf(1.5f));
        map_snap1Items_dy.put(getString(R.string.image_duration_two), Float.valueOf(2.0f));

        map_snap2Items_dy.put(getString(R.string.image_interval_ten), Float.valueOf(10));
        map_snap2Items_dy.put(getString(R.string.image_interval_fifteen), Float.valueOf(15));
        map_snap2Items_dy.put(getString(R.string.image_interval_twenty), Float.valueOf(20));
        map_snap2Items_dy.put(getString(R.string.image_interval_thirty), Float.valueOf(30));


        //录像的时间设置
        map_recordItems.put(getString(R.string.image_pickup_one), Float.valueOf(1));
        map_recordItems.put(getString(R.string.image_pickup_three), Float.valueOf(3));
        map_recordItems.put(getString(R.string.image_pickup_five), Float.valueOf(4));
        map_recordItems.put(getString(R.string.image_pickup_six), Float.valueOf(5));
        map_recordItems.put(getString(R.string.image_pickup_ten), Float.valueOf(10));

//
//        Iterator iter0 = map_snap0Items.entrySet().iterator();
//        while (iter0.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter0.next();
//            Object key = entry.getKey();
//            Object val = entry.getValue();
//            sp_snap0Items.add((String) key);
//        }

        Iterator iter1 = map_snap1Items.entrySet().iterator();
        while (iter1.hasNext()) {
            Map.Entry entry = (Map.Entry) iter1.next();
            Object key = entry.getKey();
            sp_snap1Items.add((String) key);
        }

        Iterator iter2 = map_snap2Items.entrySet().iterator();
        while (iter2.hasNext()) {
            Map.Entry entry = (Map.Entry) iter2.next();
            Object key = entry.getKey();
            sp_snap2Items.add((String) key);
        }


        Iterator iter1_dy = map_snap1Items_dy.entrySet().iterator();
        while (iter1_dy.hasNext()) {
            Map.Entry entry = (Map.Entry) iter1_dy.next();
            Object key = entry.getKey();
            sp_snap1Items_dy.add((String) key);
        }

        Iterator iter2_dy = map_snap2Items_dy.entrySet().iterator();
        while (iter2_dy.hasNext()) {
            Map.Entry entry = (Map.Entry) iter2_dy.next();
            Object key = entry.getKey();
            sp_snap2Items_dy.add((String) key);
        }


        Iterator iter3 = map_recordItems.entrySet().iterator();
        while (iter3.hasNext()) {
            Map.Entry entry = (Map.Entry) iter3.next();
            Object key = entry.getKey();
            sp_recordItems.add((String) key);
        }


        snap1Adapter = new ResoluteAdapter(this);
        snap1Adapter.setItems(sp_snap1Items);
        sp_snap1.setAdapter(snap1Adapter);


        snap2Adapter = new ResoluteAdapter(this);
        snap2Adapter.setItems(sp_snap2Items);
        sp_snap2.setAdapter(snap2Adapter);


        snap1Adapter_dy = new ResoluteAdapter(this);
        snap1Adapter_dy.setItems(sp_snap1Items_dy);
        sp_snap1_dy.setAdapter(snap1Adapter_dy);


        snap2Adapter_dy = new ResoluteAdapter(this);
        snap2Adapter_dy.setItems(sp_snap2Items_dy);
        sp_snap2_dy.setAdapter(snap2Adapter_dy);


        recordAdapter = new ResoluteAdapter(this);
        recordAdapter.setItems(sp_recordItems);
        sp_record.setAdapter(recordAdapter);


        sp_snap1.setOnItemSelectedListener(this);
        sp_snap2.setOnItemSelectedListener(this);
        sp_snap1_dy.setOnItemSelectedListener(this);
        sp_snap2_dy.setOnItemSelectedListener(this);
        sp_record.setOnItemSelectedListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
//        setSpannerPosition();


    }

    //设置Spanner的位置
    private void setSpannerPosition() {
        //自动拍照的默认时间
        int index1 = (int) SPUtils.get(this, Constss.SP_TOTALTIME_INDEX, 1);
        int index2 = (int) SPUtils.get(this, Constss.SP_INTERVAL_INDEX, 0);

        int index1_dy = (int) SPUtils.get(this, Constss.SP_TOTALTIME_INDEX_dy, 0);
        int index2_dy = (int) SPUtils.get(this, Constss.SP_INTERVAL_INDEX_dy, 1);

        int index3 = (int) SPUtils.get(this, Constss.SP_RECORD_INDEX, 0);
//        Log.e("TAG_", "setSpannerPosition: index1 ="+index1+" ,index2="+index2+" ,index3= "+index3 );
        sp_snap1.setSelection(index1);
        sp_snap2.setSelection(index2);
        sp_snap1_dy.setSelection(index1_dy);
        sp_snap2_dy.setSelection(index2_dy);
        sp_record.setSelection(index3);


    }

    public void back(View v) {
        this.finish();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_ap) {


            Intent intent = new Intent(context, ConfigApActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            bundle.putParcelable("wifi", mWifiStateModel);
            bundle.putParcelable("dev", model);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_sta) {

            Intent intent = new Intent(context, ConfigStaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            bundle.putParcelable("wifi", mWifiStateModel);
            bundle.putParcelable("dev", model);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_set_record) {

//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setMessage(getString(R.string.image_setting_pickup_time)+mRecordConfigModel.TotalTime+getString(R.string.image_setting_pickup_minute))
//                    .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
////                            SPUtils.put(ConfigActivity.this,Constss.RECORD_TOTALTIME,mRecordConfigModel.TotalTime*60);
////                            SPUtils.put(ConfigActivity.this, Constss.SP_RECORD_INDEX, record_index);
//
////                            OneItem.getOneItem().setRecordTotalTime(mRecordConfigModel.TotalTime*60);
//                            SouthUtil.showToast(ConfigActivity.this,getString(R.string.setting_picture_save_success));
//
//                        }
//                    })
//                    .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                        }
//                    });
//            AlertDialog alert = builder.create();
//            alert.setCanceledOnTouchOutside(false);
//            alert.show();
        } else if (v.getId() == R.id.btn_set_snapshot) {//拍照设置的监听事件


//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setMessage(getString(R.string.image_photograph_type)+(mSnapShotConfigModel.TotalTime == 0?getString(R.string.image_photograph_one):getString(R.string.image_photograph_in)
//                    +" "+mSnapShotConfigModel.TotalTime +" "+getString(R.string.image_photograph_interval)+" "+mSnapShotConfigModel.IntervalTime+" "+getString(R.string.image_photograph_continuition)))
//                    .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//
////                            SPUtils.put(ConfigActivity.this,Constss.SNAP_TOTALTIME,mSnapShotConfigModel.TotalTime*60);
////                            SPUtils.put(ConfigActivity.this,Constss.SNAP_INTERVAL,mSnapShotConfigModel.IntervalTime);
////                            SPUtils.put(ConfigActivity.this, Constss.SP_TOTALTIME_INDEX, snap1_index);
////                            SPUtils.put(ConfigActivity.this, Constss.SP_INTERVAL_INDEX, snap2_index);
////                            OneItem.getOneItem().setSpTotalTime( mSnapShotConfigModel.TotalTime*60);
////                            OneItem.getOneItem().setSpInterval( mSnapShotConfigModel.IntervalTime);
//                            SouthUtil.showToast(ConfigActivity.this,getString(R.string.setting_picture_save_success));
//                        }
//                    })
//                    .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                        }
//                    });
//            AlertDialog alert = builder.create();
//            alert.setCanceledOnTouchOutside(false);
//            alert.show();

        }
    }


    @Override //属于拍照设置
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.sp_snap1:
                snap1_index = position;
//                mSnapShotConfigModel.TotalTime =map_snap1Items.get(sp_snap1Items.get(position));
                SPUtils.put(ConfigActivity.this, Constss.SNAP_TOTALTIME, map_snap1Items.get(sp_snap1Items.get(position)) * 60);
                SPUtils.put(ConfigActivity.this, Constss.SP_TOTALTIME_INDEX, snap1_index);


                break;


            case R.id.sp_snap2:
                snap2_index = position;
//                mSnapShotConfigModel.IntervalTime = map_snap2Items.get(sp_snap2Items.get(position));
                SPUtils.put(ConfigActivity.this, Constss.SNAP_INTERVAL, map_snap2Items.get(sp_snap2Items.get(position)));
                SPUtils.put(ConfigActivity.this, Constss.SP_INTERVAL_INDEX, snap2_index);
//                Log.i("TAG_Live", "onItemSelected: "+map_snap2Items.get(sp_snap2Items.get(position)));


                break;

            case R.id.sp_snap1_dy:
                snap1_index_dy = position;
//                mSnapShotConfigModel.TotalTime =map_snap1Items.get(sp_snap1Items.get(position));
                SPUtils.put(ConfigActivity.this, Constss.SNAP_TOTALTIME_dy, map_snap1Items_dy.get(sp_snap1Items_dy.get(position)) * 60);
                SPUtils.put(ConfigActivity.this, Constss.SP_TOTALTIME_INDEX_dy, snap1_index_dy);
                break;


            case R.id.sp_snap2_dy:
                snap2_index_dy = position;
//                mSnapShotConfigModel.IntervalTime = map_snap2Items.get(sp_snap2Items.get(position));
                SPUtils.put(ConfigActivity.this, Constss.SNAP_INTERVAL_dy, map_snap2Items_dy.get(sp_snap2Items_dy.get(position)));
                SPUtils.put(ConfigActivity.this, Constss.SP_INTERVAL_INDEX_dy, snap2_index_dy);

//                Log.i("TAG_Live", "onItemSelected: "+map_snap2Items_dy.get(sp_snap2Items_dy.get(position)));
                break;


            case R.id.sp_record:
                record_index = position;
                mRecordConfigModel.TotalTime = map_recordItems.get(sp_recordItems.get(position));
                SPUtils.put(ConfigActivity.this, Constss.RECORD_TOTALTIME, mRecordConfigModel.TotalTime * 60);
                SPUtils.put(ConfigActivity.this, Constss.SP_RECORD_INDEX, record_index);

                break;

            default:
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("CheckResult")
    void getConfig() {

        if (model== null) {
            return;
        }

        Observable<RecordConfigModel> observable1 = Network.getCommandApi(model).getRecordConfig(model.usr, model.pwd, 89, SouthUtil.getRandom());
        Observable<SnapShotConfigModel> observable2 = Network.getCommandApi(model).getSnapShotConfig(model.usr, model.pwd, 87, SouthUtil.getRandom());
        Observable<WIFIStateModel> observable3 = Network.getCommandApi(model).getWifiState(model.usr, model.pwd, 37, SouthUtil.getRandom());
        Observable<LampStateModel> observable4 = Network.getCommandApi(model).getLampState(model.usr, model.pwd, 74, SouthUtil.getRandom());

        Observable.combineLatest(observable1, observable2, observable3, observable4,

                new Function4<RecordConfigModel, SnapShotConfigModel, WIFIStateModel, LampStateModel, Boolean>() {
                    @Override
                    public Boolean apply(RecordConfigModel recordConfigModel, SnapShotConfigModel snapShotConfigModel, WIFIStateModel wifiStateModel, LampStateModel lampStateModel) throws Exception {
                        mWifiStateModel = wifiStateModel;
                        mLampStateModel = lampStateModel;
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        switchLamp0.setChecked(mLampStateModel.Light0 == 1);
                        switchLamp1.setChecked(mLampStateModel.Light1 == 1);
                    }
                });


    }


    void controlLamp(int index, boolean open) {
        if (model == null) {
            return;
        }

        Observable observable=Network.getCommandApi(model)
                .setLampState(model.usr, model.pwd, 73, index, open ? 1 : 0, SouthUtil.getRandom())
                .flatMap(new Function<RetModel, ObservableSource<LampStateModel>>() {
                    @Override
                    public ObservableSource<LampStateModel> apply(RetModel retModel) throws Exception {
                        return Network.getCommandApi(model).getLampState(model.usr, model.pwd, 74, SouthUtil.getRandom());

                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

                observable.subscribe(observer_lamp);
    }

    Observer<LampStateModel> observer_lamp = new Observer<LampStateModel>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(LampStateModel lampStateModel) {
            mLampStateModel = lampStateModel;
            Log.e(tag, "observer_lamp : " + lampStateModel.toString());
            switchLamp0.setChecked(lampStateModel.Light0 == 1);
            switchLamp1.setChecked(lampStateModel.Light1 == 1);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };







    LampStateModel mLampStateModel = new LampStateModel();
    RecordConfigModel mRecordConfigModel = new RecordConfigModel();
    WIFIStateModel mWifiStateModel = new WIFIStateModel();

    @Override
    protected void onPause() {
        super.onPause();
    }


}
