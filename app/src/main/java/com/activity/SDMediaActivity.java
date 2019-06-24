package com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.adapter.TabFragmentPagerAdapter;
import com.fragment.SDImageFragment;

import com.view.CustomConfirmDialog;
import com.view.SyncHorizontalScrollView;



/**
 * Created by gyl1 on 3/30/17.
 * 照片和视频管理
 */

public class SDMediaActivity extends FragmentActivity implements View.OnClickListener, SDImageFragment.OnCheckChangeListner



{
    public static final String TAG = "SDMediaActivity";
    private RelativeLayout rl_nav;
    private SyncHorizontalScrollView mHsv;
    private RadioGroup rg_nav_content;
    private ImageView iv_nav_indicator;
    private ViewPager mViewPager;
    private int indicatorWidth;
    public static String[] tabTitle;//标题
    private LayoutInflater mInflater;
    private TabFragmentPagerAdapter mAdapter;
    private int currentIndicatorLeft = 0;

    private Button btn_delete; //删除
    Button btn_checkall; //全选
//    Button btn_download;

//    private DevModel model = null;

//    private DirectoryChooserFragment mDialog;
    private CustomConfirmDialog customConfirmDialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//禁止屏幕休眠
        setContentView(R.layout.activity_sdmedia);
//        model = Constss.devModel;
        findViewById();
        initView();
        setListener();

    }


    @Override
    protected void onResume() {
        super.onResume();
        setCheckChangeListener();
    }


    private void findViewById() {
        tabTitle = new String[]{this.getString(R.string.image_manage_picture), this.getString(R.string.image_manage_pickup)};//标题
        rl_nav = (RelativeLayout) findViewById(R.id.rl_nav);

        mHsv = (SyncHorizontalScrollView) findViewById(R.id.mHsv);

        rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);

        iv_nav_indicator = (ImageView) findViewById(R.id.iv_nav_indicator);

        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_checkall = (Button) findViewById(R.id.btn_checkall);
//        btn_download = (Button) findViewById(R.id.btn_download);
        btn_delete.setOnClickListener(this);
        btn_checkall.setOnClickListener(this);
//        btn_download.setOnClickListener(this);

        customConfirmDialog = new CustomConfirmDialog(this);

    }
    private void initView() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        indicatorWidth = dm.widthPixels / 2;

        LayoutParams cursor_Params = iv_nav_indicator.getLayoutParams();
        cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
        iv_nav_indicator.setLayoutParams(cursor_Params);


        //获取布局填充器
        mInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);

        initNavigationHSV();

        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
//        mAdapter.setModel(model);
        mAdapter.setTabTitleSize(tabTitle.length);
        mViewPager.setAdapter(mAdapter);
    }

    private void initNavigationHSV() {

        rg_nav_content.removeAllViews();

        for(int i=0;i<tabTitle.length;i++){

            RadioButton rb = (RadioButton) mInflater.inflate(R.layout.nav_radiogroup_item, null);
            rb.setId(i);
            rb.setText(tabTitle[i]);
            rb.setLayoutParams(new LayoutParams(indicatorWidth,
                    LayoutParams.MATCH_PARENT));

            rg_nav_content.addView(rb);
        }
        ((RadioButton)rg_nav_content.getChildAt(0)).performClick();

    }
    private void setListener() {

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG,"onPageSelected:"+position);
                if(rg_nav_content!=null && rg_nav_content.getChildCount()>position){
                    ((RadioButton)rg_nav_content.getChildAt(position)).performClick();
                }
                checkIndex = position;
                setCheckAllBtnText();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                Log.e(TAG,"onPageScrollStateChanged:"+arg0);
            }
        });

        rg_nav_content.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Log.e(TAG,"onCheckedChanged:"+checkedId);

                if(rg_nav_content.getChildAt(checkedId)!=null){

                    TranslateAnimation animation = new TranslateAnimation(
                            currentIndicatorLeft ,
                            ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft(), 0f, 0f);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setDuration(100);
                    animation.setFillAfter(true);

                    //执行位移动画
                    iv_nav_indicator.startAnimation(animation);

                    mViewPager.setCurrentItem(checkedId);   //ViewPager 跟随一起 切换

                    //记录当前 下标的距最左侧的 距离
                    currentIndicatorLeft = ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft();
                    checkIndex = checkedId;
                    setCheckAllBtnText();

                    if (checkIndex == 1){
                        mAdapter.getSDVideoFragment().getVideosInActivity(0);
                    }
                    //  mHsv.smoothScrollTo((checkedId > 1 ? ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rg_nav_content.getChildAt(2)).getLeft(), 0);
                }
            }
        });
    }

    public void back(View v){
        this.finish();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_delete){

          customConfirmDialog.show(getString(R.string.image_manage_delete_title_sd), getString(R.string.image_manage_delete_ok),getString(R.string.image_manage_delete_no),new View.OnClickListener() {
              @Override
              public void onClick(View v) { //确定删除
                  if (0 == checkIndex){
                      mAdapter.getSDImageFragment().deleteImages();
                  }
                  else if(1 == checkIndex){
                      mAdapter.getSDVideoFragment().deleteVideos();
                  }
                  customConfirmDialog.dimessDialog();
              }
          }, new View.OnClickListener() {
              @Override
              public void onClick(View v) {//不删除
                  customConfirmDialog.dimessDialog();
              }
          });

        }
        else if(view.getId() == R.id.btn_checkall){
            if (0 == checkIndex){
                mAdapter.getSDImageFragment().checkAll();

            }
            else if(1 == checkIndex){
                mAdapter.getSDVideoFragment().checkAll();
            }
            setCheckAllBtnText();
        }

    }
    public void setCheckAllBtnText(){
        if (0 == checkIndex){
            btn_checkall.setText(mAdapter.getSDImageFragment().isAnyItemChecked()?getString(R.string.image_manage_all_cancel):getString(R.string.image_manage_all));
        }
        else if(1 == checkIndex){
            btn_checkall.setText(mAdapter.getSDVideoFragment().isAnyItemChecked()?getString(R.string.image_manage_all_cancel):getString(R.string.image_manage_all));
        }
    }

    public void setCheckChangeListener(){
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mAdapter.getSDImageFragment() != null){
                    Log.e(tag,"setCheckChangeListener not null");
                    mAdapter.getSDImageFragment().setOnCheckChangeListner(SDMediaActivity.this);
                }
                else{
                    Log.e(tag,"setCheckChangeListener  null");
                }

                if (mAdapter.getSDVideoFragment() != null){
                    Log.e(tag,"setCheckChangeListener not null");
                    mAdapter.getSDVideoFragment().setOnCheckChangeListner(SDMediaActivity.this);
                }
            }
        };
        handler.postDelayed(runnable,1000);
    }

    private int checkIndex = 0;

    @Override
    public void OnCheckChange() {
        setCheckAllBtnText();
    }
    static  final String tag = "SDMediaActivity";


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //resultCode 1 :fresh image  2:fresh video  0 :do nothing
        //requestCode 1:image browser 2:video player
        Log.e(tag,"request code is "+requestCode+",resultCode code is "+resultCode);
        if (requestCode == 1 && resultCode == 1) {
            mAdapter.getSDImageFragment().getImagesInActivity(1);
        }
        else if (requestCode == 2 && resultCode == 2) {
            mAdapter.getSDVideoFragment().getVideosInActivity(1);
        }
    }

}
