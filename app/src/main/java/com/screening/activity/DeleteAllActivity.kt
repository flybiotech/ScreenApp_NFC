package com.screening.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.activity.R
import com.logger.LogHelper
import com.screening.model.*
import com.screening.uitls.Deleteutils
import com.screening.uitls.SPUtils
import com.util.Constss
import com.util.FileUtil
import com.util.LogUtils
import com.util.SouthUtil
import com.view.LoadingDialog
import com.view.dialog.ConfirmDialog
import es.dmoral.toasty.Toasty
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.litepal.LitePal
import java.io.File

class DeleteAllActivity : AppCompatActivity(), View.OnClickListener {
    private var bt_delete: Button? = null
    private var btn_left: Button? = null
    private var title_bar: TextView? = null
    private var mDialog: LoadingDialog? = null
    private var disposabel: Disposable? = null
    private var screenWith: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delate_all)
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        screenWith = dm.widthPixels
        initView()
        initClick()
    }

    private fun initClick() {

        bt_delete?.setOnClickListener(this)
        btn_left?.setOnClickListener(this)
    }

    private fun initView() {
        bt_delete = findViewById(R.id.bt_delete)
        btn_left = findViewById(R.id.btn_left)
        title_bar = findViewById(R.id.title_bar)
        btn_left?.visibility = View.VISIBLE
        title_bar?.text = getString(R.string.setting_delete_title)
        bt_delete?.text = getString(R.string.setting_delete_all)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_delete -> {

                deleteDataConfirmDialog()
            }
            R.id.btn_left -> finish()
            else -> {
            }
        }
    }

    fun deleteDataConfirmDialog() {
        ConfirmDialog(this)
                .setMessage(getString(R.string.deleteDataConfirm))
                .setNegativeButton(getString(R.string.deleteDataNega), ContextCompat.getColor(this, R.color.gray_dialog), DialogInterface.OnClickListener { dialog, which ->

                    //                    Toasty.normal(this, "取消")
                })
                .setPositiveButton(getString(R.string.deleteDataPos), Color.RED, DialogInterface.OnClickListener { dialog, which ->
                    //FileUtil.getRootFilePath() 获取根目录
                    val file = File(FileUtil.getRootFilePath())
                    deleteAllData(getString(R.string.deletingData), file)

                })
                .setDialogCornersRadius(10.0f)
                .setDialogWidth((screenWith - SouthUtil.dp2px(100.0f)).toInt())
                .create().show()
    }

    //删除所有的数据
    @SuppressLint("CheckResult")
    private fun deleteAllData(msg: String, file: File) {
        showDiolog(msg)
        Single.create(SingleOnSubscribe<Boolean> { emitter ->

            LitePal.deleteAll(ListMessage::class.java)
            LitePal.deleteAll(User::class.java)

            LitePal.deleteAll(FTPPath::class.java)
            LitePal.deleteAll(FTPBean::class.java)
            LitePal.deleteAll(DoctorId::class.java)
            LitePal.deleteAll(ScreenErrorList::class.java)

            SPUtils.remove(this, Constss.LOGIN_SP_CHECKBOX)
            SPUtils.remove(this, Constss.LOGIN_SP_NAME)
            SPUtils.remove(this, Constss.LOGIN_SP_PASS)

            val a = deleteDirectory(Bean.bePath)//删除文件
            val b = deleteDirectory(Bean.endPath)//删除文件
            val c = deleteDirectory(Bean.uploadPath)//删除文件

//            Log.e("page", "恢复出厂设置文件删除结果 a=" + a + ",b=" + b + ",c=" + c)
            LogHelper.d("恢复出厂设置文件删除结果 a=" + a + ",b=" + b + ",c=" + c)
            emitter.onSuccess(a && b && c)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { str ->
                    dismissdialog()
                    Toasty.normal(this@DeleteAllActivity, getString(R.string.deleteDataSuc)).show()
                }
    }

    private fun deleteDirectory(filePath: String): Boolean {
        val file = File(filePath)
        return Deleteutils.deleteLocal(file)

    }


    private fun showDiolog(msg: String) {

        if (mDialog != null && mDialog?.isShow() ?: false) {
            mDialog?.setMessage(msg)
        } else {
            if (mDialog == null) {
                mDialog = LoadingDialog(this, true)
            }
            mDialog?.setMessage(msg)
            mDialog?.dialogShow()
        }

    }

    private fun dismissdialog() {
        mDialog?.dismiss()
    }


    override fun onStop() {
        super.onStop()
        dismissdialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposabel?.dispose()
    }


}

