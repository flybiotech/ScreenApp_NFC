package com.screening.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.activity.BaseActivity;
import com.activity.R;
import com.logger.LogHelper;
import com.screening.api.LoginContractApi;
import com.screening.impl.ILoginPresenterImpl;
import com.screening.manager.UserManager;
import com.screening.model.Bean;
import com.screening.model.Item;
import com.screening.uitls.DateUtils;
import com.screening.uitls.SPUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.util.Constss;
import com.util.SouthUtil;
import com.view.LoadingDialog;

import java.io.File;


public class LoginActivity extends BaseActivity implements LoginContractApi.ILoginView, View.OnClickListener {
    private Button loginBtn;
    private EditText loginNameEdit, loginPassEdit;
    private CheckBox remenberPassBox;
    private ILoginPresenterImpl mPresenter;
    private UserManager userManager;
    private RxPermissions mRxPermissions;
    private LinearLayout linearLayout;
    //再按一次退出程序
    private long exitTime = 0;
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        LogHelper.v("");
        initView();

    }

    private void initView() {
        loginNameEdit = (EditText) findViewById(R.id.edit_loginName);
        loginPassEdit = (EditText) findViewById(R.id.edit_loginPass);
        remenberPassBox = (CheckBox) findViewById(R.id.checkBox_login); //记住密码
        linearLayout = (LinearLayout) findViewById(R.id.linear_login);
        loginBtn = (Button) findViewById(R.id.login);
        mPresenter = new ILoginPresenterImpl(this);
        mRxPermissions = new RxPermissions(this);
        loginBtn.setOnClickListener(this);
        linearLayout.requestFocus();//获取焦点
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogHelper.v("");
        if (userManager == null) {
            userManager = new UserManager();
        }
        //初始化医生编号
//        userManager.InitializationDoctorId();
        mPresenter.setActivity(this);
        //输入admin 账户
//        userManager.saveUserData("Admin", "admin123", "");
//        userManager.saveUserData("admin", "admin123", "");
//        userManager.saveUserData("shanghaifaluyuan", "123456", "");
//        //普通账户 账户
//        userManager.saveUserData("fly", "123456", "");
        //设置账户和密码
        setNameAndPassWord();
    }

    //设置账号和密码
    @Override
    public void setNameAndPassWord() {

        String userName = (String) SPUtils.get(this, Constss.LOGIN_SP_NAME, "");
        String userPass = (String) SPUtils.get(this, Constss.LOGIN_SP_PASS, "");
        remenberPassBox.setChecked((boolean) SPUtils.get(this, Constss.LOGIN_SP_CHECKBOX, false));
        loginNameEdit.setText(userName);
        loginPassEdit.setText(userPass);
    }

    @Override
    public void loginSuccess(String type) {
        createPackageFile();
        Intent intent;
        LogHelper.d("login type=" + type);

        if (type.equals(Constss.LOGIN_DELETEALL_TYPE)) {//恢复出厂设置
            intent = new Intent(LoginActivity.this, DeleteAllActivity.class);
        } else if (type.equals(Constss.LOGIN_DELETESINGLE_TYPE)) { //单个账号功能删除未上传的受检者信息
            intent = new Intent(LoginActivity.this, DeleteSingleActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, MainActivity.class);

        }

        startActivity(intent);
    }

    @Override
    public void loginFailed(String loginFailedMsg) {

        SouthUtil.showToast(this, loginFailedMsg);
    }

    //创建以包名命名的文件夹
    private void createPackageFile() {
        File file = new File(Bean.sdZipPath + "Android/data/" + getPackageInfo(this).packageName + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                attemptPermissions();
                break;
            default:
                break;
        }
    }

    private void attemptPermissions() {

        mRxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {

                        String loginTime = new DateUtils().getDate();
                        Item.getOneItem(LoginActivity.this).setLoginTime(loginTime);//记录登录时间，用以和扫码时间对比
//                        adminVerification();
                        login();
                    } else {
                        SouthUtil.showToast(LoginActivity.this, getString(R.string.login_permission));
                    }
                });
    }

    private void login() {
        if (mPresenter != null) {
            mPresenter.login();
        }
    }

    @Override
    public String getUserName() {
        return loginNameEdit.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return loginPassEdit.getText().toString().trim();
    }


    //设置记住密码框  是否被选择
    @Override
    public boolean isCheckBox() {
        return remenberPassBox.isChecked();
    }

    @Override
    public Context getContext() {
        return this;
    }

    //展示进度条 展示登录过程中的其他消息
    @Override
    public void showDialogLoading(String msg) {
        showDiolog(msg);
    }

    //隐藏进度条
    @Override
    public void dismissDialog() {
        dismissdialog();
    }


    private void showDiolog(String msg) {
        if (mDialog != null && mDialog.isShow()) {
            mDialog.setMessage(msg);
        } else {
            if (mDialog == null) {
                mDialog = new LoadingDialog(this, true);
            }
            mDialog.setMessage(msg);
            mDialog.dialogShow();
        }
    }

    private void dismissdialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//            LogUtils.e("TAG","软键盘执行 ");
            /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
            attemptPermissions();

            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    @Override //用于 确定是否退出程序
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 1000) {
                SouthUtil.showToast(this, getString(R.string.quit_program));
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onStop() {
        super.onStop();
        LogHelper.v("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogHelper.d();
    }

}
