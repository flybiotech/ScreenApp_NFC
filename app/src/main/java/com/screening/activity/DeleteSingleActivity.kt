package com.screening.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.activity.R
import com.logger.LogHelper
import com.screening.adapter.SwipeAdapter
import com.screening.manager.UserManager
import com.screening.model.ListMessage
import com.util.Constss
import com.util.SouthUtil
import com.view.LoadingDialog
import com.view.dialog.ChoiceDialog
import com.view.dialog.ConfirmDialog
import com.yanzhenjie.recyclerview.*
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class DeleteSingleActivity : AppCompatActivity(), OnClickListener, OnItemClickListener {


    private var bt_right: Button? = null
    private var btn_left: Button? = null
    private var title_bar: TextView? = null
    private var swipe_content: SwipeRecyclerView? = null
    private var mDataList = ArrayList<ListMessage>()
    private var mAdapter: SwipeAdapter? = null
    private var screenWith: Int = 0
    private var choiseItem: Array<String>? = null
    private var userManager: UserManager? = null
    private var mDialog: LoadingDialog? = null
    private var mShowType=0  //查询受检者信息，并展示
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deletesingle_layout)
        initView()
        initData()
    }

    private fun initView() {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        screenWith = dm.widthPixels

        bt_right = findViewById(R.id.btn_right)
        btn_left = findViewById(R.id.btn_left)
        title_bar = findViewById(R.id.title_bar)
        swipe_content = findViewById(R.id.swipe_content)
        title_bar?.text = getString(R.string.deletesingle_title)

        bt_right?.visibility = View.VISIBLE
        bt_right?.text = getString(R.string.deletesingle_rightbtn)
        bt_right?.setOnClickListener(this)
        btn_left?.visibility = View.VISIBLE
        btn_left?.setOnClickListener(this)

        userManager = UserManager()

        swipe_content?.layoutManager = LinearLayoutManager(this)
        swipe_content?.setOnItemClickListener(this)
        mAdapter = SwipeAdapter(this)
        swipe_content?.setSwipeMenuCreator(mSwipeMenuCreator)//菜单创建器
        swipe_content?.setOnItemMenuClickListener(mItemMenuClickListener)//Item的点击事件


    }

    private fun initData() {
        // 默认构造，传入颜色即可。
        val itemDecoration: RecyclerView.ItemDecoration = DefaultItemDecoration(ContextCompat.getColor(this,R.color.gray_dialog))
        // 或者：颜色，宽，高，最后一个参数是不画分割线的ViewType，可以传入多个。
        // val itemDecoration =  DefaultDecoration(color, width, height, excludeViewType);
        //  swipe_content.setDecoration(itemDecoration);
        swipe_content?.addItemDecoration(itemDecoration)

        swipe_content?.adapter = mAdapter
        mAdapter?.notifyDataSetChanged(mDataList)
    }



    override fun onResume() {
        super.onResume()
        choiseItem = arrayOf(getString(R.string.deleteSingleOperate1), getString(R.string.deleteSingleOperate2), getString(R.string.deleteSingleOperate3))
        //查询受检者信息，并展示
        searchUserInfo(mShowType)
    }
    
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_left -> {
                finish()
            }

            R.id.btn_right -> {
                infoChoise()

            }
        }
    }

    /**
     *创建菜单器
     */
    private val mSwipeMenuCreator: SwipeMenuCreator = SwipeMenuCreator { swipeLeftMenu: SwipeMenu, swipeRightMenu: SwipeMenu, position: Int ->
        val width = resources.getDimensionPixelSize(R.dimen.dp_70)

        // 1. MATCH_PARENT 自适应高度，保持和Item一样高;

        // 2. 指定具体的高，比如80;
        // 3. WRAP_CONTENT，自身高度，不推荐;
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        // 添加右侧的，如果不添加，则右侧不会出现菜单。
        val deleteItem = SwipeMenuItem(this@DeleteSingleActivity)
                .setBackground(R.drawable.selector_red)
                .setImage(R.drawable.ic_action_delete)
                .setText(getString(R.string.deleteSinglebutton))
                .setTextColor(Color.WHITE)
                .setWidth(width)
                .setHeight(height)
        swipeRightMenu.addMenuItem(deleteItem)


    }

    //右侧菜单的点击事件
    private val mItemMenuClickListener = OnItemMenuClickListener { menuBridge: SwipeMenuBridge, position: Int ->
        menuBridge.closeMenu()

        val direction = menuBridge.direction // 左侧还是右侧菜单。
        val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。

        if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
//            deleteSingleUser(position)
            deleteSingleDataConfirmDialog(position)
        }

    }


    // recyclerView 的监听事件
    override fun onItemClick(view: View?, adapterPosition: Int) {
//        Toasty.normal(this@DeleteSingleActivity, "1第" + adapterPosition + "个").show()
        jumpActivity(adapterPosition)
    }


    private fun infoChoise() {
        ChoiceDialog(this)
                .setTtitle(getString(R.string.deleteSingleOperate))
                .setTitleSize(resources.getDimension(R.dimen.dp_8))
                .setTtitlePadding(20, 20, 20, 20)
                .setItems(choiseItem)
                .setItemText(16.0f, Color.BLACK)
                .setItemTextPaddingLeft(20)
                .setOnItemClickListener { textView: TextView, i: Int ->

                    if (i == 2) {//最后一项是删除功能
                        deleteDataConfirmDialog()
                    } else {
                        searchUserInfo(i)
                    }

                }
                .setDialogWidth(screenWith * 3 / 4)
                .setDialogCornersRadius(10.0f)//设置四个圆角
                .setIsFromBottom(true)
                .create().show();
    }

    //确认删除所有的的弹出框
    fun deleteDataConfirmDialog() {
        ConfirmDialog(this)
                .setMessage(getString(R.string.deleteSingleDataConfirm))
                .setTitleSize(resources.getDimension(R.dimen.dp_8))
                .setNegativeButton(getString(R.string.deleteDataNega), ContextCompat.getColor(this, R.color.gray_dialog), DialogInterface.OnClickListener { dialog, which ->

                    //                    Toasty.normal(this, "取消").show()
                })
                .setPositiveButton(getString(R.string.deleteDataPos), Color.RED, DialogInterface.OnClickListener { dialog, which ->
                    deleteAllUser() //删除所有的用户
//                    Toasty.normal(this, "确定").show()

                })
                .setDialogCornersRadius(10.0f)
                .setDialogWidth((screenWith - SouthUtil.dp2px(100.0f)).toInt())
                .create().show()
    }

    //确认单个删除的弹出框
    fun deleteSingleDataConfirmDialog(delSinglePos: Int) {
        ConfirmDialog(this)
                .setMessage(getString(R.string.deleteSingleDataConfirm1))
                .setTitleSize(resources.getDimension(R.dimen.dp_8))
                .setNegativeButton(getString(R.string.deleteDataNega), ContextCompat.getColor(this, R.color.gray_dialog), DialogInterface.OnClickListener { dialog, which ->

                    //                    Toasty.normal(this, "取消").show()
                })
                .setPositiveButton(getString(R.string.deleteDataPos), Color.RED, DialogInterface.OnClickListener { dialog, which ->
                    deleteSingleUser(delSinglePos) //删除单个受检者的信息

                })
                .setDialogCornersRadius(10.0f)
                .setDialogWidth((screenWith - SouthUtil.dp2px(100.0f)).toInt())
                .create().show()
    }


    @SuppressLint("CheckResult")
    private fun searchUserInfo(type: Int) {
        showDiolog(getString(R.string.deleteSingledialog))
        Observable.create(ObservableOnSubscribe<MutableList<ListMessage>> { emitter ->
            this.mDataList.clear()

            if (type == 0) {//未完成的受检者
                mShowType = 0
                mDataList = userManager?.searchUserUnfinished() as ArrayList<ListMessage>
            } else if (type == 1) {//已完成的受检者
                mShowType = 1
                mDataList = userManager?.searchUserFinished() as ArrayList<ListMessage>
            }
            mDataList.let { emitter.onNext(it) }

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { listdata ->
                    dismissdialog()
                    mAdapter?.notifyDataSetChanged(mDataList)
                })
    }

    //删除单个受检者信息
    @SuppressLint("CheckResult")
    private fun deleteSingleUser(position: Int) {
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            val idcard = mDataList.get(position).idCard
            emitter.onNext(idcard)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { idcard ->
                    val index = userManager?.deleteSingleUser(idcard)
                    mDataList.removeAt(position)
                    mAdapter?.notifyItemRemoved(position)
                })
    }

    //删除所有的受检者信息
    @SuppressLint("CheckResult")
    private fun deleteAllUser() {

        Observable.create(ObservableOnSubscribe<Int> { emitter ->
            emitter.onNext(0)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { type ->
                    var index = 0
                    if (mShowType == 0) {
                        index = userManager?.deleteUserUnfinished() as Int
                    } else if (mShowType == 1) {
                        index = userManager?.deleteUserFinished() as Int
                    }

                    mDataList?.clear()
                    mAdapter?.notifyDataSetChanged(mDataList)
                })
    }


    private fun jumpActivity(position: Int) {
        val id = mDataList.get(position).getpId()
        val intent = Intent(this@DeleteSingleActivity, CaseListShowActivity::class.java).also {
            it.putExtra("message", id)
            startActivity(it)
        }

    }

    private fun showDiolog(msg: String) {

        val unit = if (mDialog != null && mDialog?.isShow() ?: false) {
            mDialog?.setMessage(msg)
        } else {
            if (mDialog == null) {
                mDialog = LoadingDialog(this, true)
            }
            mDialog?.setMessage(msg)
            val dialogShow = mDialog?.dialogShow()

        }

    }

    private fun dismissdialog() {
        mDialog?.dismiss()
    }

    override fun onStop() {
        super.onStop()
        dismissdialog()
    }

}
