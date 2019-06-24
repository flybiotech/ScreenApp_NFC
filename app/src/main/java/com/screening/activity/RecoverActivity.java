package com.screening.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.activity.R;
import com.screening.adapter.Showadapter;
import com.screening.model.DoctorId;
import com.screening.model.Item;
import com.screening.model.ListMessage;
import com.screening.uitls.Constant;
import com.screening.uitls.FileManagerUtils;
import com.screening.uitls.RecoveryUtils;
import com.util.SouthUtil;
import com.view.CustomConfirmDialog;
import com.view.LoadingDialog;

import org.litepal.LitePal;

import java.util.List;

public class RecoverActivity extends AppCompatActivity implements RecoveryUtils.RecoverResult, View.OnClickListener {
    private Button btn_left, btn_right;
    RecoveryUtils recoveryUtils;
    private LoadingDialog mDialog;
    private List<ListMessage> listMessages;
    private CustomConfirmDialog customConfirmDialog = null;
    private TextView title_bar;
    private List<DoctorId> doctorIds;
    private FileManagerUtils fileManagerUtils;
    private ListView lv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);
        initView();
        initClick();
    }

    private void initView() {
        recoveryUtils = new RecoveryUtils(this);
        title_bar = findViewById(R.id.title_bar);
        title_bar.setText(R.string.setting_recovery);
        btn_left = findViewById(R.id.btn_left);
        btn_left.setVisibility(View.VISIBLE);
        lv_show = findViewById(R.id.lv_show);
        btn_right = findViewById(R.id.btn_right);
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setText(R.string.setting_recover);
        RecoveryUtils.setRecoverResultLisneter(this);
        customConfirmDialog = new CustomConfirmDialog(RecoverActivity.this);
        fileManagerUtils = new FileManagerUtils(this, lv_show);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getDoctorId();
    }

    /**
     * 判断是否已有医生编号
     */
    private void getDoctorId() {
        doctorIds = LitePal.findAll(DoctorId.class);
        if (doctorIds.size() > 0) {
            recoveryUtils.isSDExistence();
        } else {
            dismissDiolog();
            SouthUtil.showToast(RecoverActivity.this, getString(R.string.doctorid_isnull));
            Intent intent = new Intent(RecoverActivity.this, DocNumActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void initClick() {
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
    }

    //先判断是否有SD卡
    private void showDeleteDialog() {

        listMessages = LitePal.findAll(ListMessage.class);
        if (listMessages.size() > 0) {
            dismissDiolog();
            customConfirmDialog.show(getString(R.string.image_manage_delete_title), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (customConfirmDialog != null) {
                        customConfirmDialog.dimessDialog();
                    }
                    showDeleteDialogAgain();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (customConfirmDialog != null) {
                        customConfirmDialog.dimessDialog();
                    }
                }
            });
        } else {
            showDiolog(getString(R.string.setting_file_begin_recover));
            recoveryUtils.recoverData(fileList);
        }
    }

    private void showDeleteDialogAgain() {
        customConfirmDialog.show(getString(R.string.image_manage_delete_title_again), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customConfirmDialog != null) {
                    customConfirmDialog.dimessDialog();
                }
                showDiolog(getString(R.string.setting_file_begin_recover));
                LitePal.deleteAll(ListMessage.class);
                recoveryUtils.recoverData(fileList);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customConfirmDialog != null) {
                    customConfirmDialog.dimessDialog();
                }
                dismissDiolog();
            }
        });
    }


    private void showDiolog(String msg) {

        if (mDialog != null && mDialog.isShow()) {
            mDialog.setMessage(msg);
        } else {
            if (mDialog == null) {
                mDialog = new LoadingDialog(RecoverActivity.this, true);
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


    private List<String> fileList;//病人原图路径集合

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                if (fileList != null) {
                    fileList.clear();
                }
                Showadapter.setList();
                finish();
                break;
            case R.id.btn_right:
                recoverDate();
                break;
            default:
                break;
        }
    }

    //选择后开始恢复数据
    private void recoverDate() {
        fileList = Showadapter.getList();
        if (fileList.size() > 0) {
            showDeleteDialog();
        } else {
            SouthUtil.showToast(this, getString(R.string.recover_no_choise));
        }
    }

    @Override
    public void getRecoverResult(int type, boolean result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type) {
                    case 0:
                        if (!result) {
                            dismissDiolog();
                            SouthUtil.showToast(RecoverActivity.this, getString(R.string.setting_no_sd));
                            Log.e("recoverAc", "SD卡不存在");
                        }
                        break;
                    case 1:
                        if (result) {//Sd卡中有备份文件
                            fileManagerUtils.select(Item.getOneItem(RecoverActivity.this).getSdRootPath() + Constant.sdPath);
                        } else {
                            dismissDiolog();
                            SouthUtil.showToast(RecoverActivity.this, getString(R.string.setting_sd_no_file));
                            Log.e("recoverAc1", "SD卡文件不存在");
                        }
                        break;
                    case 2:
                        if (result) {
                            recoveryUtils.recoverData(fileList);
                            Showadapter.setList();
                            if (fileList != null) {
                                fileList.clear();
                            }
                        } else {
                            dismissDiolog();
                            SouthUtil.showToast(RecoverActivity.this, getString(R.string.setting_sdcopy_faild));
                            Log.e("recoverAc2", "SD卡复制失败");
                        }
                        break;
                    case 3:
                        if (result) {
                            Showadapter.setList();
                            if (fileList != null) {
                                fileList.clear();
                            }
                            SouthUtil.showToast(RecoverActivity.this, getString(R.string.setting_recover_success));
                            Log.e("recoverAc3", "数据恢复成功");
                            finish();
                        } else {
                            SouthUtil.showToast(RecoverActivity.this, getString(R.string.setting_recover_faild));
                            Log.e("recoverAc4", "数据恢复失败");
                        }
                        dismissDiolog();
                        break;
                    case 4:
                        if(!result){
                            SouthUtil.showToast(RecoverActivity.this, getString(R.string.setting_recover_file_error));
                            dismissDiolog();
                        }
                        break;
                }
            }
        });

    }

}
