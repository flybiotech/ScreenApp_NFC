package com.screening.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.activity.R;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.screening.uitls.BardcodeSettingUtils;
import com.screening.uitls.Constant;
import com.suke.widget.SwitchButton;
import com.util.ToastUtils;
import java.util.concurrent.TimeUnit;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BarcodeSettingActivity extends AppCompatActivity implements View.OnClickListener, SwitchButton.OnCheckedChangeListener {
    private EditText et_hpv, et_hpv_size, et_cytology, et_cytology_size, et_gene, et_gene_size, et_dna, et_dna_size, et_other, et_other_size;
    private Button btn_left, btn_right,bt_clearhpv,bt_cleartct,bt_cleargene,bt_cleardna,bt_clearother;
    private SwitchButton switch_hpv, switch_cytology, switch_gene, switch_dna, switch_other;
    private TextView title_bar;
    private boolean hpv_state = false, cytology_state = false, gene_state = false, dna_state = false, other_state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_setting);
        initView();
        initClick();
        getData();
        initEdittext(et_hpv, et_hpv_size, switch_hpv);
        initEdittext(et_cytology, et_cytology_size, switch_cytology);
        initEdittext(et_gene, et_gene_size, switch_gene);
        initEdittext(et_dna, et_dna_size, switch_dna);
        initEdittext(et_other, et_other_size, switch_other);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //只要到过此界面，就将此属性保存在本地，下次新增患者不再自动跳转至本界面
        BardcodeSettingUtils.setBarcodeSetting(this, Constant.barcode_key, true);
    }

    private void initView() {
        et_hpv = findViewById(R.id.et_hpv);
        et_hpv_size = findViewById(R.id.et_hpv_size);
        et_cytology = findViewById(R.id.et_cytology);
        et_cytology_size = findViewById(R.id.et_cytology_size);
        et_gene = findViewById(R.id.et_gene);
        et_gene_size = findViewById(R.id.et_gene_size);
        et_dna = findViewById(R.id.et_dna);
        et_dna_size = findViewById(R.id.et_dna_size);
        et_other = findViewById(R.id.et_other);
        et_other_size = findViewById(R.id.et_other_size);
//        btn_wifiSetting_clear01 = findViewById(R.id.btn_wifiSetting_clear01);
//        btn_wifiSetting_save01 = findViewById(R.id.btn_wifiSetting_save01);
        switch_hpv = findViewById(R.id.switch_hpv);
        switch_cytology = findViewById(R.id.switch_cytology);
        switch_gene = findViewById(R.id.switch_gene);
        switch_dna = findViewById(R.id.switch_dna);
        switch_other = findViewById(R.id.switch_other);
        btn_right = findViewById(R.id.btn_right);
        btn_left = findViewById(R.id.btn_left);
        title_bar = findViewById(R.id.title_bar);
        title_bar.setText(getString(R.string.setting_scan_setting));
        btn_left.setVisibility(View.VISIBLE);
        bt_clearhpv = findViewById(R.id.bt_clearhpv);
        bt_cleartct = findViewById(R.id.bt_cleartct);
        bt_cleargene = findViewById(R.id.bt_cleargene);
        bt_cleardna = findViewById(R.id.bt_cleardna);
        bt_clearother = findViewById(R.id.bt_clearother);
    }

    private void initClick() {
//        btn_wifiSetting_save01.setOnClickListener(this);
//        btn_wifiSetting_clear01.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        switch_hpv.setOnCheckedChangeListener(this);
        switch_cytology.setOnCheckedChangeListener(this);
        switch_gene.setOnCheckedChangeListener(this);
        switch_dna.setOnCheckedChangeListener(this);
        switch_other.setOnCheckedChangeListener(this);
        bt_clearhpv.setOnClickListener(this);
        bt_cleartct.setOnClickListener(this);
        bt_cleargene.setOnClickListener(this);
        bt_cleardna.setOnClickListener(this);
        bt_clearother.setOnClickListener(this);
    }

    /**
     * 得到初始值
     */
    private void getData() {

        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                hpv_state = BardcodeSettingUtils.getBarcodeState(BarcodeSettingActivity.this, et_hpv, et_hpv_size, switch_hpv, Constant.hpv_key);
                setState(hpv_state, switch_hpv);
                getEdittextMsg(et_hpv, et_hpv_size, switch_hpv);
                Log.e("barcode", hpv_state + "");
            }
            if (i == 1) {
                cytology_state = BardcodeSettingUtils.getBarcodeState(BarcodeSettingActivity.this, et_cytology, et_cytology_size, switch_cytology, Constant.cytology_key);
                setState(cytology_state, switch_cytology);
                getEdittextMsg(et_cytology, et_cytology_size, switch_cytology);
                Log.e("barcode1", cytology_state + "");
            }
            if (i == 2) {
                gene_state = BardcodeSettingUtils.getBarcodeState(BarcodeSettingActivity.this, et_gene, et_gene_size, switch_gene, Constant.gene_key);
                setState(gene_state, switch_gene);
                getEdittextMsg(et_gene, et_gene_size, switch_gene);
                Log.e("barcode2", gene_state + "");
            }
            if (i == 3) {
                dna_state = BardcodeSettingUtils.getBarcodeState(BarcodeSettingActivity.this, et_dna, et_dna_size, switch_dna, Constant.dna_key);
                setState(dna_state, switch_dna);
                getEdittextMsg(et_dna, et_dna_size, switch_dna);
                Log.e("barcode3", dna_state + "");
            }
            if (i == 4) {
                other_state = BardcodeSettingUtils.getBarcodeState(BarcodeSettingActivity.this, et_other, et_other_size, switch_other, Constant.other_key);
                setState(other_state, switch_other);
                getEdittextMsg(et_other, et_other_size, switch_other);
                Log.e("barcode4", other_state + "");
            }
        }
    }

    private void setState(boolean state, SwitchButton button) {
        if (state) {
            button.setChecked(true);
        } else {
            button.setChecked(false);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //设置完成
            case R.id.btn_wifiSetting_save01:
                BardcodeSettingUtils.setBarcodeSetting(this, Constant.barcode_key, true);
                finish();
                break;
            case R.id.btn_left:
                finish();
                break;
            case R.id.bt_clearhpv:
                clearBarcode(et_hpv,et_hpv_size,switch_hpv);
                BardcodeSettingUtils.setBarcode(BarcodeSettingActivity.this,Constant.hpv_key,"","",false);
                break;
            case R.id.bt_cleartct:
                clearBarcode(et_cytology,et_cytology_size,switch_cytology);
                BardcodeSettingUtils.setBarcode(BarcodeSettingActivity.this,Constant.cytology_key,"","",false);
                break;
            case R.id.bt_cleargene:
                clearBarcode(et_gene,et_gene_size,switch_gene);
                BardcodeSettingUtils.setBarcode(BarcodeSettingActivity.this,Constant.gene_key,"","",false);
                break;
            case R.id.bt_cleardna:
                clearBarcode(et_dna,et_dna_size,switch_dna);
                BardcodeSettingUtils.setBarcode(BarcodeSettingActivity.this,Constant.dna_key,"","",false);
                break;
            case R.id.bt_clearother:
                clearBarcode(et_other,et_other_size,switch_other);
                BardcodeSettingUtils.setBarcode(BarcodeSettingActivity.this,Constant.other_key,"","",false);
                break;
        }
    }

    private void clearBarcode(EditText et_iden,EditText et_size,SwitchButton sb_clear){
        et_iden.setText("");
        et_size.setText("");
//        sb_clear.setChecked(false);
    }

    /**
     * 开始改变扫码设置的属性
     */
    private void setCode(int temp, EditText et_Identification, EditText et_size, boolean state) {
        String identification = et_Identification.getText().toString().trim();
        String size_cast = et_size.getText().toString().trim();
        switch (temp) {
            case 1:
                BardcodeSettingUtils.setBarcode(this, Constant.hpv_key, identification, size_cast, state);
                break;
            case 2:

                BardcodeSettingUtils.setBarcode(this, Constant.cytology_key, identification, size_cast, state);
                break;
            case 3:

                BardcodeSettingUtils.setBarcode(this, Constant.gene_key, identification, size_cast, state);
                break;
            case 4:

                BardcodeSettingUtils.setBarcode(this, Constant.dna_key, identification, size_cast, state);
                break;
            case 5:

                BardcodeSettingUtils.setBarcode(this, Constant.other_key, identification, size_cast, state);
                break;
        }
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {

        switch (view.getId()) {
            case R.id.switch_hpv:
                setCode(1, et_hpv, et_hpv_size, isChecked);
                break;
            case R.id.switch_cytology:
                setCode(2, et_cytology, et_cytology_size, isChecked);
                break;
            case R.id.switch_gene:
                setCode(3, et_gene, et_gene_size, isChecked);
                break;
            case R.id.switch_dna:
                setCode(4, et_dna, et_dna_size, isChecked);
                break;
            case R.id.switch_other:
                setCode(5, et_other, et_other_size, isChecked);
                break;
        }
    }


    /**
     * 点击切换按钮之前，需要先判断输入框是否为空，为空时不能点击
     */
    private void getEdittextMsg(EditText et_Identification, EditText et_size, SwitchButton button) {
        String identification = et_Identification.getText().toString().trim();
        String size = et_size.getText().toString().trim();
        if (identification.equals("") || size.equals("")) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
    }


    /**
     * 监听edittext的内容
     */
    String identification = "";
    String size = "";
    String before_size = "";

    @SuppressLint("CheckResult")
    private void initEdittext(EditText et_Identification, EditText et_size, SwitchButton button) {

        RxTextView.textChanges(et_Identification)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                       getChange(et_Identification,et_size,button);
                    }
                });
        RxTextView.textChanges(et_size)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        getChange(et_Identification,et_size,button);
                    }
                });
    }

    /**
     * 检测输入框内容变化的通用方法
     */
    private void getChange(EditText et_Identification, EditText et_size,SwitchButton button){
        size = et_size.getText().toString().trim();
        identification = et_Identification.getText().toString().trim();
        //当只输入一位时，判断是否为0，为0时自动设置为空
        if (size.length() == 1 && size.equals("0")) {
            et_size.setText("");
            return;
        }
        //当输入两位时，先判断第一位是否为0，当第一位为0，第二位不为0时，设置为第二位数字；当第二位为0时，设置为空
        if (size.length() == 2) {
            String acronym = size.substring(0, 1);
            String acronym1 = size.substring(1, 2);
            if (acronym.equals("0") && !acronym1.equals("0")) {
                et_size.setText(acronym1);
            } else if (acronym.equals("0") && acronym1.equals("0")) {
                et_size.setText("");
            }
        }
        if (!size.equals("") && !identification.equals("")) {
            //位数限制不能小于标识码长度
            int identSize = identification.length();
            int num = Integer.parseInt(size);
            if (num < identSize) {
                ToastUtils.showToast(BarcodeSettingActivity.this, getString(R.string.wordlimit));
                button.setChecked(false);
                button.setEnabled(false);
                return;
            }
            if (!before_size.equals(size)) {
                button.setChecked(false);
            }
            button.setEnabled(true);
        } else {
            button.setChecked(false);
            button.setEnabled(false);
        }

    }
}
