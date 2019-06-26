package com.screening.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.Manager.DataManager;
import com.activity.BaseActivity;
import com.activity.LiveVidActivity;
import com.activity.R;
import com.cw.cwsdk.cw;
import com.cw.cwsdk.u8API.barcode.BarCodeAPI;
import com.logger.LogHelper;
import com.model.DevModel;
import com.screening.model.BarcodeBean;
import com.screening.model.Item;
import com.screening.model.ListMessage;
import com.screening.rotatedialog.dialog.MyDialog;
import com.screening.ui.WifiLoadingAnim;
import com.screening.uitls.BardcodeSettingUtils;
import com.screening.uitls.Constant;
import com.screening.uitls.ModifyORAdd;
import com.screening.uitls.SPUtils;
import com.screening.uitls.SearchMessage;
import com.screening.uitls.VerificationUtils;
import com.screening.wifi.WifiAutoConnectManager;
import com.screening.wifi.WifiConnectUtil;
import com.southtech.thSDK.lib;
import com.util.AlignedTextUtils;
import com.util.Constss;
import com.util.FileUtil;
import com.util.SouthUtil;
import com.util.ToastUtils;
import com.view.LoadingDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.lang.ref.WeakReference;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 这个界面为从列表信息界面跳转过来，查询详细信息
 *
 * @param
 */
public class ShowActivity extends BaseActivity implements View.OnClickListener, MyDialog.OnDialogButtonClickListener, VerificationUtils.VerificationResult, WifiConnectUtil.WifiConnectResultListener , BarCodeAPI.IBarCodeData {
    private TextView tv01, tv02, tv03, tv04, tv05, tv06, tv07, tv08, tv09, tv10, tv11, tv12, tv13, tv14, tv15, tv16, tv17, tv18, tv19, tv20, tv21, tv22, title_text;
    private String[] tvName;//标识字段的名称集合
    private EditText et_pId, et_pName, et_pAge, et_pSex, et_pPhone, et_pLocalPhone, et_pDate, et_pOccupation, et_smoking, et_pSexualpartners, et_pGestationaltimes,
            pAbortion, et_pRequiredID, et_pRequiredHPV, et_pRequiredCytology, et_pDetailedaddress, et_pRemarks, et_pRequiredGene, et_pRequiredDNA, et_pRequiredOther;
    private Spinner sp_marray, sp_contraception;
    //bt_getImage 获取图像
    private Button btn_left, btn_right, bt_clear, bt_save, bt_getImage, bt_add;
    private String[] marryDatas;//婚否
    private String[] bhdDatas;//避孕方式
    private int id;//存储传过来的值
    private String msgIntent;//存储传递过来的详细信息
    private ModifyORAdd modifyORAdd;
    private List<ListMessage> listMessages;
    private String marry = "无", birthControlMode = "无";
    private ImageView iv_scanhpv, iv_scanxbx, iv_scanjyjc, iv_scandna, iv_scanther;
    private ImageView iv_screen;
    private WifiAutoConnectManager mManagerConnect;
    private WifiManager wifiManager;
    private SearchMessage searchMessage;
    private WifiLoadingAnim wifiLoadingAnim;
    private Handler mHandler = new ShowHandler(this);
    private MyDialog myDialog;
    private String searchMsg;
    private DataManager dataManager = DataManager.getInstance();
    private LoadingDialog mDialog;
    private Item mConst;
    private boolean isFront = false;//判断当前页面是否在前台
    private int style = 0;//1代表hpv,2代表细胞学，3代表基因，4代表dna,5代表其他

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    };


    @Override
    public void okButtonClick() {

    }


    private Disposable mDisposable = null;

    private void dispose() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private Observable observable = null;
    private Observable observable1 = null;

    private void searchDevss() {
        showDiolog(getString(R.string.search_szb));
        dispose();


        observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

//                Thread.sleep(1000);
                searchMsg = lib.jthNet_SearchDev_old();
                String msgType = analyzeDevMsg(searchMsg);
                LogHelper.e("msgType= " + msgType);
                if (msgType.equals("error1")) { //当前平板没有搜索到主机设备
                    Thread.sleep(5000);
                    emitter.onError(new Throwable("error1"));
                } else if (msgType.equals("error2")) {

                } else if (msgType.equals("error3")) {

                } else if (msgType.equals("error4")) {

                } else if (msgType.equals("success")) {
                    emitter.onNext("success");
                }
            }
        }).retry(5);

        observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Thread.sleep(1000);
                searchMsg = lib.jthNet_SearchDev_old();
                String msgType = analyzeDevMsg(searchMsg);
                LogHelper.e("msgType= " + msgType + " ,isFront= " + isFront);
                if (msgType.equals("error1")) { //当前平板没有搜索到主机设备
                    Thread.sleep(5000);
                    emitter.onError(new Throwable("error1"));
                } else if (msgType.equals("error2")) {

                } else if (msgType.equals("error3")) {

                } else if (msgType.equals("error4")) {

                } else if (msgType.equals("success")) {
                    emitter.onNext("success");
                }
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> apply(Throwable throwable) throws Exception {
                return observable;
            }
        });

        observable1.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("success") && isFront) {
                            Intent intent = new Intent(ShowActivity.this, LiveVidActivity.class);
                            intent.putExtra("user_id", et_pRequiredID.getText().toString().trim());// id
                            intent.putExtra("dev", mDevModel);
                            Log.e("TAG_", "handleMessage: id =" + et_pRequiredID.getText().toString().trim());
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogHelper.e("throwable e=" + e.getMessage());
                        Toasty.normal(ShowActivity.this, getString(R.string.image_setting_STA_wifi)).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private DevModel mDevModel = null;

    //分析从主机设备上的到的消息
    private String analyzeDevMsg(String msg) {
        //当前平板没有搜索到主机设备
        if ("".equals(msg)) return "error1";

        String[] devArray = msg.split("@");
        if (devArray.length == 0) return "error2";
        //add 0407 搜索设备全部删除
        if (!dataManager.deleteAllDev()) {
            return "error3";
        }

        for (int i = 0; i < devArray.length; i++) {
            String[] array = devArray[i].split(",");
            if (array.length < 6) {
                continue;
            }
            mDevModel = new DevModel();
            mDevModel.ip = array[0];
            mDevModel.sn = array[1];
            mDevModel.online = DevModel.EnumOnlineState.Online;
            mDevModel.dataport = array[2];
            mDevModel.httpport = array[3];
            mDevModel.name = array[5];

            if (!dataManager.addDev(mDevModel)) {
                return "error4";
            }
        }
        return "success";
    }

    //wifi连接成功
    @Override
    public void wifiConnectSuccess(String type) {
        stopAnim();
        if (type.equals(Constss.WIFI_TYPE_SZB)) {
            FileUtil.createFileFolder(ShowActivity.this, et_pId.getText().toString().trim(), et_pRequiredID.getText().toString());
            //设置默认值
            Constss.picYuanTuCount = 0;
            Constss.picCuSuanBaiCount = 0;
            Constss.picCuSuanBaiAutoCount = 0;
            Constss.picDianYouCount = 0;
            Constss.picDianYouAutoCount = 0;
            Constss.vedioCount = 0;
            searchDevss();
        } else {
            dismissDiolog();
        }
    }

    @Override
    public void wifiConnectScaning(@NotNull String type) {
        showDiolog(getString(R.string.wifiProcessMsg));
    }

    @Override
    public void wifiConnectFalid(@NotNull String type, @NotNull String errorMsg) {
        dismissDiolog();
        stopAnim();
        Toasty.normal(this, errorMsg).show();
    }

    @Override
    public void sendScan() {

    }

    @Override
    public void onBarCodeData(String s) {
        mhandler.post(new Runnable() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void run() {
                //按键扫描
                Log.e("code", s);
//                tvShow.setText(s);
                switch (style){
                    case 0:
                        Toasty.normal(ShowActivity.this,"请重新点击扫描按钮");
                        break;
                    case 1:
                        et_pRequiredHPV.setText(s);
                        break;
                    case 2:
                        et_pRequiredCytology.setText(s);
                        break;
                    case 3:
                        et_pRequiredGene.setText(s);
                        break;
                    case 4:
                        et_pRequiredDNA.setText(s);
                        break;
                    case 5:
                        et_pRequiredOther.setText(s);
                        break;
                }

            }
        });
    }

    @Override
    public void getSettings(int i, int i1, int i2, String s, String s1, int i3, int i4) {

    }

    @Override
    public void setSettingsSuccess() {

    }


    private class ShowHandler extends Handler {

        private WeakReference<ShowActivity> activity;

        public ShowHandler(ShowActivity activity) {
            this.activity = new WeakReference<ShowActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ShowActivity act = activity.get();
            if (act != null) {

                switch (msg.what) {
                    case 1:
                        et_pId.setText(Integer.parseInt(listMessages.get(listMessages.size() - 1).getpId()) + 1 + "");
                        initJson();
                        break;

                    case 2:
                        et_pId.setText("1");
                        initJson();
                        break;

                    case 3:
                        SouthUtil.showToast(ShowActivity.this, getString(R.string.saveMsgSuccess));
                        break;

                    case 4:
                        SouthUtil.showToast(ShowActivity.this, getString(R.string.saveMsgFaild));
                        break;

                    case 5: //WiFi 已经找到了，开始连接

                        break;

                    case 6: //W没有找到wifi ，循环查找

                        break;
                    default:
                        break;

                }
            }
        }

    }

    private class ShowThread extends Thread {
        private WeakReference<ShowActivity> activity;

        public ShowThread(ShowActivity activity) {
            this.activity = new WeakReference<ShowActivity>(activity);
        }

        public void run() {
            ShowActivity act = activity.get();
            if (act != null) {
                Message message = mHandler.obtainMessage();
                listMessages = LitePal.findAll(ListMessage.class);
                if (listMessages.size() > 0) {
                    message.what = 1;
                } else {
                    message.what = 2;
                }
                mHandler.sendMessage(message);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        id = getIntent().getIntExtra("id", -1);
        msgIntent = getIntent().getStringExtra("msg");
        initView();
        initTextView();
        initData();//spinner初始化数据
        initJudge();//展示已有用户的详细信息
        initClick();
        initSelectItemClick();
        initEditTextClick();
        WifiConnectUtil.Companion.getInstance().setWifiConnectListener(this, null);
        cw.BarCodeAPI(this).setOnBarCodeDataListener(this);
    }

    private void initEditTextClick() {
        et_pRequiredID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String idCard = et_pRequiredID.getText().toString().trim();
                et_pAge.setText(VerificationUtils.getAge(idCard));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (id == -1 && msgIntent != null) {//扫码传递过来的值
            new ShowThread(this).start();
        } else if (id != -1 && msgIntent == null) {//点击listview查看信息传递过来的值
            screenId = searchMessage.getScreenId(String.valueOf(id));
        } else if (id == -1 && msgIntent == null) {
            initCopy();
        }
        BardcodeSettingUtils.getScreenBarcode(1, Constant.hpv_key, et_pRequiredHPV, iv_scanhpv, ShowActivity.this);
        BardcodeSettingUtils.getScreenBarcode(2, Constant.cytology_key, et_pRequiredCytology, iv_scanxbx, ShowActivity.this);
        BardcodeSettingUtils.getScreenBarcode(3, Constant.gene_key, et_pRequiredGene, iv_scanjyjc, ShowActivity.this);
        BardcodeSettingUtils.getScreenBarcode(4, Constant.dna_key, et_pRequiredDNA, iv_scandna, ShowActivity.this);
        BardcodeSettingUtils.getScreenBarcode(5, Constant.other_key, et_pRequiredOther, iv_scanther, ShowActivity.this);
    }

    //获取粘贴板上的值，并对其进行解析，存放到数据库中
    private void initCopy() {

        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = cm.getPrimaryClip();
        if (clipData != null) {
            ClipData.Item item = clipData.getItemAt(0);
            msgIntent = item.getText().toString();

            if (msgIntent != null && !msgIntent.equals("")) {
                new ShowThread(this).start();
            }

        }

    }

    //    //根据条件，判断是否启动WiFi加载动画，或者停止动画
    private void startAnim() {
        if (wifiLoadingAnim != null) {
            wifiLoadingAnim.setVisibility(View.VISIBLE);
            wifiLoadingAnim.startAnim();
        }
    }

    private void stopAnim() {
        if (wifiLoadingAnim != null) {
            wifiLoadingAnim.stopAnim();
            wifiLoadingAnim.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;
        cw.BarCodeAPI(ShowActivity.this).openBarCodeReceiver();
    }


    String name = "", age = "", idCard = "", screenId = "",
            mobile = "", HPV = "", TCT = "", DNA = "", gene = "", other = "",
            career = "", smokingYears = "", pregnancyNumber = "", abortionNumber = "",
            sexualPartnerNumber = "", contraception = "无", marriage = "无";

    //解析扫码传过来的值
    private void initJson() {
        try {
            Log.e("msgIntent", msgIntent);
            JSONObject object = new JSONObject(msgIntent);
            if (object.has("name")) {
                name = object.getString("name");
            }
            if (object.has("age")) {
                age = object.getString("age");
            }
            if (object.has("idCard")) {
                idCard = object.getString("idCard");

            }
            if (object.has("wechatOpenId")) {
                screenId = object.getString("wechatOpenId");
            }
            if (object.has("mobile")) {
                mobile = object.getString("mobile");
            }

            if (object.has("career")) {
                career = object.getString("career");
            }
            if (object.has("smokingYears")) {
                smokingYears = object.getString("smokingYears");
            }
            if (object.has("pregnancyNumber")) {
                pregnancyNumber = object.getString("pregnancyNumber");
            }
            if (object.has("abortionNumber")) {
                abortionNumber = object.getString("abortionNumber");
            }
            if (object.has("sexualPartnerNumber")) {
                sexualPartnerNumber = object.getString("sexualPartnerNumber");
            }
            if (object.has("contraception")) {
                contraception = object.getString("contraception");
            }
            if (object.has("marriage")) {
                marriage = object.getString("marriage");
            }
            et_pName.setText(name);
            et_pPhone.setText(mobile);
            et_pRequiredID.setText(idCard);
            et_pAge.setText(age);

            et_pOccupation.setText(career);
            et_smoking.setText(smokingYears);
            et_pGestationaltimes.setText(pregnancyNumber);
            pAbortion.setText(abortionNumber);
            et_pSexualpartners.setText(sexualPartnerNumber);
            initShowMarray(marriage);
            initShowContraception(contraception);
            VerificationUtils.verificationInput(ShowActivity.this, et_pPhone, et_pRequiredID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断修改还是新增
     */
    boolean isSave = false;//是否已保存

    private void initdialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ShowActivity.this);
        builder.setTitle(getString(R.string.tip));
        builder.setMessage(getString(R.string.havemsg));
        builder.setNegativeButton(getString(R.string.image_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.title_sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                initConectWIfi();
            }
        });
        builder.show();
    }

    private void initClick() {
        bt_clear.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        bt_save.setVisibility(View.GONE);
        bt_getImage.setOnClickListener(this);
        iv_screen.setOnClickListener(this);
        iv_scanxbx.setOnClickListener(this);
        iv_scanther.setOnClickListener(this);
        iv_scanjyjc.setOnClickListener(this);
        iv_scanhpv.setOnClickListener(this);
        iv_scandna.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_clearM:
                modifyORAdd.againAsk(getString(R.string.clearTitle), getString(R.string.clearMessage));

                break;
            case R.id.bt_save:
                break;
            case R.id.bt_getImage://获取图像
                VerificationUtils.getDuplicateChecking(et_pRequiredHPV, et_pRequiredCytology, et_pRequiredGene, et_pRequiredDNA, et_pRequiredOther, et_pRequiredID);
                break;
            case R.id.iv_screen:
                break;
            case R.id.iv_scanhpv:
//                startActivityForResult(new Intent(this, ScanningActivity.class), 1);
                style = 1;
                cw.BarCodeAPI(ShowActivity.this).scan();
                break;
            case R.id.iv_scandna:
                style = 4;
                cw.BarCodeAPI(ShowActivity.this).scan();
//                startActivityForResult(new Intent(this, ScanningActivity.class), 4);
                break;
            case R.id.iv_scanxbx:
                style = 2;
                cw.BarCodeAPI(ShowActivity.this).scan();
//                startActivityForResult(new Intent(this, ScanningActivity.class), 2);
                break;
            case R.id.iv_scanjyjc:
                style = 3;
                cw.BarCodeAPI(ShowActivity.this).scan();
//                startActivityForResult(new Intent(this, ScanningActivity.class), 3);
                break;
            case R.id.iv_scanother:
                style = 5;
                cw.BarCodeAPI(ShowActivity.this).scan();
//                startActivityForResult(new Intent(this, ScanningActivity.class), 5);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra("text");
            if (!TextUtils.isEmpty(result)) {
                if (requestCode == 1) {
                    if (result.contains(Constant.hpv_id) && Integer.parseInt(Constant.hpv_size) == result.length()) {
                        et_pRequiredHPV.setText(result);
                    } else {
                        ToastUtils.showToast(ShowActivity.this, getString(R.string.barcode_error));
                        startActivityForResult(new Intent(this, ScanningActivity.class), 1);
                    }
                } else if (requestCode == 2) {
                    if (result.contains(Constant.cytology_id) && result.length() == Integer.parseInt(Constant.cytology_size)) {
                        et_pRequiredCytology.setText(result);
                    } else {
                        ToastUtils.showToast(ShowActivity.this, getString(R.string.barcode_error));
                        startActivityForResult(new Intent(this, ScanningActivity.class), 2);
                    }

                } else if (requestCode == 3) {
                    if (result.contains(Constant.gene_id) && result.length() == Integer.parseInt(Constant.gene_size)) {
                        et_pRequiredGene.setText(result);
                    } else {
                        ToastUtils.showToast(ShowActivity.this, getString(R.string.barcode_error));
                        startActivityForResult(new Intent(this, ScanningActivity.class), 3);
                    }

                } else if (requestCode == 4) {
                    if (result.contains(Constant.dna_id) && result.length() == Integer.parseInt(Constant.dna_size)) {
                        et_pRequiredDNA.setText(result);
                    } else {
                        ToastUtils.showToast(ShowActivity.this, getString(R.string.barcode_error));
                        startActivityForResult(new Intent(this, ScanningActivity.class), 4);
                    }

                } else if (requestCode == 5) {
                    if (result.contains(Constant.other_id) && result.length() == Integer.parseInt(Constant.other_size)) {
                        et_pRequiredOther.setText(result);
                    } else {
                        ToastUtils.showToast(ShowActivity.this, getString(R.string.barcode_error));
                        startActivityForResult(new Intent(this, ScanningActivity.class), 5);
                    }

                }
            }
        }
    }

    private void initConectWIfi() {

        showDiolog(getString(R.string.wifi_start_connect_szb));
        startAnim();
        WifiConnectUtil.Companion.getInstance().startConnectWifi(Constss.WIFI_TYPE_SZB);

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""));
    }


    //判断当前患者是否已采集图片
    private boolean isGetImage() {
        return searchMessage.getImage(et_pRequiredID.getText().toString().trim());

    }

    /**
     * spinner的点击事件响应
     */
    public void initSelectItemClick() {
        sp_marray.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Item.getOneItem(ShowActivity.this).setMarry(getString(R.string.patient_nothing));
                        break;
                    case 1:
                        mConst.setMarry(getString(R.string.patient_married));
                        break;
                    case 2:
                        mConst.setMarry(getString(R.string.patient_unmarried));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_contraception.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0://病人避孕方式
                        mConst.setBirthControlMode(getString(R.string.patient_nothing));
                        break;
                    case 1://病人避孕方式
                        mConst.setBirthControlMode(getString(R.string.patient_condom));
                        break;
                    case 2://
                        mConst.setBirthControlMode(getString(R.string.patient_acyterion));
                        break;
                    case 3://
                        mConst.setBirthControlMode(getString(R.string.patient_Contraceptive_needle));
                        break;
                    case 4://
                        mConst.setBirthControlMode(getString(R.string.patient_female_condom));
                        break;
                    case 5://
                        mConst.setBirthControlMode(getString(R.string.patient_Ligation));
                        break;
                    case 6://
                        mConst.setBirthControlMode(getString(R.string.patient_Women__IUD));
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initShowContraception(String category) {
        switch (category) {
            case "无"://病人避孕方式
                sp_contraception.setSelection(0, true);//设为true时显示
                mConst.setBirthControlMode(getString(R.string.patient_nothing));
                break;
            case "避孕套"://病人避孕方式
                sp_contraception.setSelection(1, true);//设为true时显示为安全套
                mConst.setBirthControlMode(getString(R.string.patient_condom));
                break;
            case "避孕药"://
                sp_contraception.setSelection(2, true);
                mConst.setBirthControlMode(getString(R.string.patient_acyterion));
                break;
            case "避孕针":
                sp_contraception.setSelection(3, true);
                mConst.setBirthControlMode(getString(R.string.patient_Contraceptive_needle));
                break;
            case "女性安全套":
                sp_contraception.setSelection(4, true);
                mConst.setBirthControlMode(getString(R.string.patient_female_condom));
                break;
            case "男女结扎"://
                sp_contraception.setSelection(5, true);
                mConst.setBirthControlMode(getString(R.string.patient_Ligation));
                break;
            case "女性节育环"://
                sp_contraception.setSelection(6, true);
                mConst.setBirthControlMode(getString(R.string.patient_Women__IUD));
                break;
            default:
                break;
        }
    }


    /**
     * 展示详细信息
     */
    private void initJudge() {
        if (id != -1) {
            initShow();
        }
    }

    private void initTextView() {
        tvName = new String[]{getString(R.string.pId), getString(R.string.pName), getString(R.string.pAge), getString(R.string.pSex), getString(R.string.RequiredPhone),
                getString(R.string.localPhone), getString(R.string.birthData), getString(R.string.Occupation), getString(R.string.smoking), getString(R.string.sexualpartners), getString(R.string.Gestationaltimes), getString(R.string.abortion),
                getString(R.string.RequiredID), getString(R.string.RequiredHPV),
                getString(R.string.RequiredCytology), getString(R.string.RequiredGene), getString(R.string.dna), getString(R.string.other), getString(R.string.Detailedaddress),
                getString(R.string.Remarks), getString(R.string.marry),
                getString(R.string.contraception)};
        tv01 = (TextView) findViewById(R.id.tv_1);
        tv02 = (TextView) findViewById(R.id.tv_2);
        tv03 = (TextView) findViewById(R.id.tv_3);
        tv04 = (TextView) findViewById(R.id.tv_4);
        tv05 = (TextView) findViewById(R.id.tv_5);
        tv06 = (TextView) findViewById(R.id.tv_6);
        tv07 = (TextView) findViewById(R.id.tv_7);
        tv08 = (TextView) findViewById(R.id.tv_8);
        tv09 = (TextView) findViewById(R.id.tv_9);
        tv10 = (TextView) findViewById(R.id.tv_10);
        tv11 = (TextView) findViewById(R.id.tv_11);
        tv12 = (TextView) findViewById(R.id.tv_12);
        tv13 = (TextView) findViewById(R.id.tv_13);
        tv14 = (TextView) findViewById(R.id.tv_14);
        tv15 = (TextView) findViewById(R.id.tv_15);
        tv16 = (TextView) findViewById(R.id.tv_16);
        tv17 = (TextView) findViewById(R.id.tv_17);
        tv18 = (TextView) findViewById(R.id.tv_18);
        tv19 = (TextView) findViewById(R.id.tv_19);
        tv20 = (TextView) findViewById(R.id.tv_20);
        tv21 = findViewById(R.id.tv_21);
        tv22 = findViewById(R.id.tv_22);
        TextView[] tvData = {tv01, tv02, tv03, tv04, tv05, tv06, tv07, tv08, tv09, tv10, tv11, tv12, tv13, tv14, tv15, tv16, tv17, tv18, tv19, tv20, tv21, tv22};
        for (int i = 0; i < tvName.length; i++) {
            if (tvData[i] != null) {
                tvData[i].setText(AlignedTextUtils.justifyString(tvName[i], 5));
            }
        }
    }

    private void initData() {
        marryDatas = new String[]{getString(R.string.patient_nothing), getString(R.string.patient_married), getString(R.string.patient_unmarried)};
        bhdDatas = new String[]{getString(R.string.patient_nothing), getString(R.string.patient_condom), getString(R.string.patient_acyterion), getString(R.string.patient_Contraceptive_needle),
                getString(R.string.patient_female_condom), getString(R.string.patient_Ligation), getString(R.string.patient_Women__IUD)};
        ArrayAdapter<String> adapterMarry = new ArrayAdapter<String>(ShowActivity.this, R.layout.adapter_item, R.id.sp_textview, marryDatas);
        ArrayAdapter<String> adapterSource = new ArrayAdapter<String>(ShowActivity.this, R.layout.adapter_item, R.id.sp_textview, bhdDatas);
        sp_marray.setAdapter(adapterMarry);
        sp_contraception.setAdapter(adapterSource);
    }

    private void initView() {
        searchMessage = new SearchMessage();
        mConst = Item.getOneItem(ShowActivity.this);
        et_pRequiredOther = findViewById(R.id.et_pRequiredOther);
        et_pRequiredDNA = findViewById(R.id.et_pRequiredDNA);
        et_pRequiredGene = findViewById(R.id.et_pRequiredGene);
        iv_screen = findViewById(R.id.iv_screen);
        iv_screen.setVisibility(View.GONE);
        bt_add = findViewById(R.id.bt_add);
        bt_clear = findViewById(R.id.bt_clearM);
        bt_save = findViewById(R.id.bt_save);
        bt_getImage = findViewById(R.id.bt_getImage);
        et_pId = findViewById(R.id.et_pId);
        et_pName = findViewById(R.id.et_pName);
        et_pAge = findViewById(R.id.et_pAge);
        et_pSex = findViewById(R.id.et_pSex);
        et_pPhone = findViewById(R.id.et_pPhone);
        et_pLocalPhone = findViewById(R.id.et_pLocalPhone);
        et_pDate = findViewById(R.id.et_pDate);
        et_pOccupation = findViewById(R.id.et_pOccupation);
        et_smoking = findViewById(R.id.et_smoking);
        et_pSexualpartners = findViewById(R.id.et_pSexualpartners);
        et_pGestationaltimes = findViewById(R.id.et_pGestationaltimes);
        pAbortion = findViewById(R.id.pAbortion);
        et_pRequiredID = findViewById(R.id.et_pRequiredID);
        et_pRequiredID.setRawInputType(Configuration.KEYBOARD_QWERTY);
        et_pRequiredHPV = findViewById(R.id.et_pRequiredHPV);
        et_pRequiredCytology = findViewById(R.id.et_pRequiredCytology);
        et_pDetailedaddress = findViewById(R.id.et_pDetailedaddress);
        et_pRemarks = findViewById(R.id.et_pRemarks);
        sp_marray = findViewById(R.id.sp_marray);
        sp_contraception = findViewById(R.id.sp_contraception);
        title_text = findViewById(R.id.title_text);
        title_text.setText(getString(R.string.patients_information));
        btn_left = findViewById(R.id.btn_left);
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setText(getString(R.string.patient_return));
        iv_scandna = findViewById(R.id.iv_scandna);
        iv_scanhpv = findViewById(R.id.iv_scanhpv);
        iv_scanjyjc = findViewById(R.id.iv_scanjyjc);
        iv_scanther = findViewById(R.id.iv_scanther);
        iv_scanxbx = findViewById(R.id.iv_scanxbx);
        mConst.setMarry(getString(R.string.patient_nothing));
        mConst.setBirthControlMode(getString(R.string.patient_nothing));
        wifiLoadingAnim = (WifiLoadingAnim) findViewById(R.id.wifiLoadingAnim);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""));
                Intent intent = new Intent(ShowActivity.this, MainActivity.class);
                intent.putExtra("canshu", 0);
                startActivity(intent);
            }
        });

        modifyORAdd = new ModifyORAdd(ShowActivity.this, et_pId, et_pName, et_pAge, et_pSex, et_pPhone, et_pLocalPhone, et_pDate, et_pOccupation, et_smoking, et_pSexualpartners, et_pGestationaltimes,
                pAbortion, et_pRequiredID, et_pRequiredHPV, et_pRequiredCytology, et_pDetailedaddress, et_pRemarks, marry, birthControlMode, et_pRequiredGene, screenId, et_pRequiredDNA, et_pRequiredOther);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);//wifi管理器
        mManagerConnect = new WifiAutoConnectManager(wifiManager, this);
        myDialog = new MyDialog(getApplicationContext());
        myDialog.setOnButtonClickListener(this);
        VerificationUtils.setGetVerificationResultListener(this);
    }

    /**
     * 将该被筛查人员的信息展示出来
     */
    private void initShow() {
        listMessages = LitePal.where("pId=?", String.valueOf(id)).find(ListMessage.class);
        for (int i = 0; i < listMessages.size(); i++) {
            et_pId.setText(listMessages.get(0).getpId());
            et_pName.setText(listMessages.get(0).getName());
            et_pAge.setText(listMessages.get(0).getAge());
            et_pPhone.setText(listMessages.get(0).getPhone());
            et_pLocalPhone.setText(listMessages.get(0).getFixedtelephone());
            et_pDate.setText(listMessages.get(0).getDataofbirth());
            et_pOccupation.setText(listMessages.get(0).getOccupation());
            et_smoking.setText(listMessages.get(0).getSmoking());
            et_pSexualpartners.setText(listMessages.get(0).getSexualpartners());
            et_pGestationaltimes.setText(listMessages.get(0).getGestationaltimes());
            pAbortion.setText(listMessages.get(0).getAbortion());
            et_pRequiredID.setText(listMessages.get(0).getIdCard());
            et_pRequiredHPV.setText(listMessages.get(0).getHpv());
            et_pRequiredCytology.setText(listMessages.get(0).getCytology());
            et_pDetailedaddress.setText(listMessages.get(0).getDetailedaddress());
            et_pRemarks.setText(listMessages.get(0).getRemarks());
            marry = listMessages.get(i).getMarry();//显示要修改的病人的婚否
            et_pRequiredGene.setText(listMessages.get(0).getGene());
            et_pRequiredDNA.setText(listMessages.get(0).getDna());
            et_pRequiredOther.setText(listMessages.get(0).getOther());
//            PCommit=listMessages.get(0).getCommunity();
            mConst.setScreeningId(listMessages.get(0).getScreeningId());
//            Item.getOneItem().setOther(listMessages.get(0).getOther());
//            Item.getOneItem().setDNA(listMessages.get(0).getDNA());
            initShowMarray(marry);

            birthControlMode = listMessages.get(i).getContraception();//避孕方式
            initShowContraception(birthControlMode);

        }
    }

    private void initShowMarray(String category) {
        switch (category) {
            case "无":
                sp_marray.setSelection(0, true);//设为true时显示
                mConst.setMarry(getString(R.string.patient_nothing));
                break;
            case "是":
                sp_marray.setSelection(1, true);//设为true时显示为已婚
                mConst.setMarry(getString(R.string.patient_married));
                break;
            case "否":
                sp_marray.setSelection(2, true);//设为true时显示为未婚
                mConst.setMarry(getString(R.string.patient_unmarried));
                break;
            default:
                break;
        }
    }

    @Override
    public void getResult(int code) {

    }

    @Override
    public void getVerifitionResult(int temp) {
        switch (temp) {
            case 0:
                bt_getImage.setEnabled(false);
                ToastUtils.showToast(ShowActivity.this, getString(R.string.patient_had));
                break;
            case 1:
                mConst.setScreeningId(screenId);
                modifyORAdd.addMessage();
                FileUtil.createFileFolder(ShowActivity.this, et_pId.getText().toString().trim(), screenId);
                break;
            case 2:
                et_pRequiredHPV.requestFocus();
                ToastUtils.showToast(ShowActivity.this, getString(R.string.hpvhad));
                break;
            case 3:
                et_pRequiredCytology.requestFocus();
                ToastUtils.showToast(ShowActivity.this, getString(R.string.RequiredCytology_had));
                break;
            case 4:
                et_pRequiredGene.requestFocus();
                ToastUtils.showToast(ShowActivity.this, getString(R.string.RequiredGene_had));
                break;
            case 5:
                et_pRequiredDNA.requestFocus();
                ToastUtils.showToast(ShowActivity.this, getString(R.string.Requireddna));
                break;
            case 6:
                et_pRequiredOther.requestFocus();
                ToastUtils.showToast(ShowActivity.this, getString(R.string.Requiredother_had));
                break;
            case 7:
                mConst.setScreeningId(screenId);

                boolean isAdd = modifyORAdd.modify();
                if (isAdd) {
                    if (isGetImage()) {//已經保存過圖片
                        initdialog();
                    } else {
                        initConectWIfi();
                    }
                }
                break;
        }
    }

    @Override
    public void getVerifitionBarcodeResult(int temp) {
        switch (temp) {
            case 1:
                ToastUtils.showToast(ShowActivity.this, getString(R.string.RequiredHPV_error));
                break;
            case 2:
                ToastUtils.showToast(ShowActivity.this, getString(R.string.RequiredCytology_error));
                break;
            case 3:
                ToastUtils.showToast(ShowActivity.this, getString(R.string.Requiredgene_error));
                break;
            case 4:
                ToastUtils.showToast(ShowActivity.this, getString(R.string.Requireddna_error));
                break;
            case 5:
                ToastUtils.showToast(ShowActivity.this, getString(R.string.Requiredother_error));
                break;
        }
    }


    private void showDiolog(String msg) {
        if (!isFront) return;
        if (mDialog != null && mDialog.isShow()) {
            mDialog.setMessage(msg);
        } else {
            if (mDialog == null) {
                mDialog = new LoadingDialog(ShowActivity.this, true);
            }
            mDialog.setMessage(msg);
            mDialog.dialogShow();
        }


    }


    private void dismissDiolog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""));
            Intent intent = new Intent(ShowActivity.this, MainActivity.class);
            intent.putExtra("canshu", 0);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onPause() {
        super.onPause();
        msgIntent = null;
        id = -1;
        cw.BarCodeAPI(ShowActivity.this).CloseScanning();
        //建议在onPause里或者监听屏幕息屏里放，息屏后可以省电
        cw.BarCodeAPI(ShowActivity.this).closeBarCodeReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isFront = false;
        msgIntent = null;
        id = -1;
        dismissDiolog();
        stopAnim();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDiolog();
        dispose();
    }
}
