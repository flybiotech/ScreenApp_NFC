package com.screening.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.activity.BaseActivity;
import com.activity.R;
import com.screening.api.StaffContractApi;
import com.screening.impl.IDocNumPresenterImpl;
import com.screening.manager.UserManager;
import com.screening.model.User;
import com.util.Constss;
import com.util.ToastUtils;

public class DocNumActivity extends BaseActivity implements View.OnClickListener, StaffContractApi.IStaffView {
    private Button bt_ftp_cancel, bt_ftp_save, btn_left, btn_right;

    private EditText et_docId;

    private TextView title;

    private CheckBox checkBox;

    private StaffContractApi.IStaffPresenter mPresenter;

    private User user;

    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        user = getIntent().getParcelableExtra(Constss.LOGIN_USERINFO);
        initView();
    }

    private void initView() {
        et_docId = (EditText) findViewById(R.id.et_docId);
        bt_ftp_save = (Button) findViewById(R.id.bt_ftp_save);
        btn_right = (Button) findViewById(R.id.btn_right);//title 右边的按钮
        title = (TextView) findViewById(R.id.title_bar);
        bt_ftp_cancel = findViewById(R.id.bt_ftp_cancel);
        btn_left = findViewById(R.id.btn_left);
        btn_right.setVisibility(View.GONE);
        bt_ftp_cancel.setOnClickListener(this);
        bt_ftp_save.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        mPresenter = new IDocNumPresenterImpl(this);
        userManager = new UserManager();
    }

    //设置基本信息
    private void setViewData() {
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setText(getString(R.string.patient_return));
        title.setText(getString(R.string.doctorid));

    }

    /**
     * 初始化编号信息
     */

    @Override
    protected void onStart() {
        super.onStart();
        String docId = userManager.searchDoctorId();
        et_docId.setText(docId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setViewData();
//        setNameAndPassWord();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //跳转的按钮
            case R.id.btn_left:
                finish();
                break;
            //保存
            case R.id.bt_ftp_save:
                String docid = et_docId.getText().toString().trim();
                initSave(docid);
                finish();
                break;
            case R.id.bt_ftp_cancel:
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * 保存输入的医生编号
     */
    private void initSave(String docid) {
        userManager.modifyDoctorId(docid);
    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getState() {
        return "staff";
    }

    @Override
    public void toNextActivity() {
        Intent intent = new Intent(DocNumActivity.this, MainActivity.class);
        intent.putExtra(Constss.LOGIN_USERINFO, user);
        startActivity(intent);
    }

    @Override
    public void toSkipActivity() {
        Intent intent = new Intent(DocNumActivity.this, MainActivity.class);
        intent.putExtra(Constss.LOGIN_USERINFO, user);
        startActivity(intent);

    }

    @Override
    public void userNameEmpty() {
        ToastUtils.showToast(this, getString(R.string.nameNotEmpty));
    }

    @Override
    public void userPasswordEmpty() {
        ToastUtils.showToast(this, getString(R.string.passNotEmpty));
    }

//    //设置 保存的账号和密码
//    public void setNameAndPassWord() {
//        String userName = (String) SPUtils.get(this, "userName_staff", "");
////        String userPass = (String) SPUtils.get(this, "userPass_staff", "");
//        checkBox.setChecked((boolean) SPUtils.get(this, "checkBox_staff", false));
////        loginNameEdit.setText(userName);
////        loginPassEdit.setText(userPass);
//    }

    @Override
    public boolean isCheckBox() {
        return checkBox.isChecked();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

//            LogUtils.e("TAG","软键盘执行 ");
            /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(DocNumActivity.this.getCurrentFocus().getWindowToken(), 0);
            }

            mPresenter.next();

        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
