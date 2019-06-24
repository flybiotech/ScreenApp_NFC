package com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.fragment.ImageBrowserFragment;
import com.model.DevModel;
import com.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by gyl1 on 3/24/16.
 * 显示放大之后的图片
 */
public class ImageBrowserActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    private static String tag =  "ImageBrowserActivity";
    private List<String> urls = null;
//    private List<String> names = null;
    private int index;
    private List<ImageBrowserFragment> mContents = new ArrayList<ImageBrowserFragment>();
    private FragmentPagerAdapter mAdapter;
//    private ViewPager mViewPager;
    private ViewPagerFixed mViewPager;
    private TextView tx_index;
//    private ImageButton btn_trash;
    // private Button btn_save;
    private DevModel model;
    boolean isAnyTrashed = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//禁止屏幕休眠
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_imagebrowser);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            urls = bundle.getStringArrayList("urls");
//            names = bundle.getStringArrayList("names");
            index = bundle.getInt("channel", 0);
//            model = bundle.getParcelable("devmodel");
        }

        initViews();
        initDatas();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            if (isAnyTrashed){
                this.setResult(1,intent);
            }
            else{
                this.setResult(0,intent);
            }
            finish();
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        }
        return false;
    }

    public void getBrowserFragments(){
        mContents.clear();
        for (int i = 0; i < urls.size(); ++i) {
            ImageBrowserFragment fragment = ImageBrowserFragment.newInstance(urls.get(i));
            mContents.add(fragment);
        }
    }
    private void initDatas() {
        isAnyTrashed = false;
        if (urls == null) return;
        getBrowserFragments();
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mContents.get(i);
            }

            @Override
            public int getCount() {
                return mContents.size();
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(index);
        setCurIndex(index);
    }

    private void initViews() {
        mViewPager = (ViewPagerFixed) findViewById(R.id.id_viewpager);
        mViewPager.setOnPageChangeListener(this);
        tx_index = (TextView) findViewById(R.id.id_index);
//        btn_trash = (ImageButton) findViewById(R.id.trash);
        //  btn_save = (Button) findViewById(R.id.save);

    }

    void setCurIndex(int index) {
        if (urls.size() == 0){
            tx_index.setText(getString(R.string.image_manager_picture_nothing));
        }
        else{
            tx_index.setText((index + 1) + "/" + urls.size());
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        setCurIndex(position);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    public void save(View v) {

//        final String path_root = Environment.getExternalStorageDirectory()
//                .getAbsolutePath() + "/"+ OneItem.getOneItem().getGather_path()+"/";
//        final String path = Environment.getExternalStorageDirectory()
//                .getAbsolutePath() +"/"+ OneItem.getOneItem().getGather_path()+"/"+ "image/";
//        if(!FileUtil.makeDir(path_root)) {
//            return;
//        }
//        if(!FileUtil.makeDir(path)) {
//            return;
//        }
//        SimpleDateFormat sDateFormat1   =   new   SimpleDateFormat("yyyyMMddHHmmss");
//        Date d = new   java.util.Date();
//        String   date1   =   sDateFormat1.format(d);
//        final String name = date1+".jpg";
//        OkHttpUtils//
//                .get()//
//                .url(urls.get(index))//
//                .build()//
//                .execute(new FileCallBack(path, name)//
//                {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void onResponse(File response, int id) {
//                        SouthUtil.showToast(ImageBrowserActivity.this,"已保存至根目录下的south_save下的image文件夹");
//
//                        try {
//                            MediaStore.Images.Media.insertImage(ImageBrowserActivity.this.getContentResolver(),
//                                    path, name, null);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        // 最后通知图库更新
//                        ImageBrowserActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path+name))));
//
//                    }
//                });
    }
    //删除
    public void trash(View v) {

//        if (lod == null){
//            lod = new LoadingDialog(this);
//        }
//        lod.dialogShow();
//        Log.e(tag,"file name is "+names.get(index));
//        subscription = Network.getCommandApi(model)
//                .deleteFile(model.usr,model.pwd,77,names.get(index),SouthUtil.getRandom())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
        //先删除，删除完毕后再执行界面操作


    }
//    Observer<List<RetModel>> observer = new Observer<List<RetModel>>() {
//        @Override
//        public void onCompleted() {
//            lod.dismiss();
//        }
//        @Override
//        public void onError(Throwable e) {
//            lod.dismiss();
//            Log.e(tag, e.toString());
//        }
//
//        @Override
//        public void onNext(List<RetModel> rets) {
//            lod.dismiss();
//
//            if (rets.get(0).ret == 1){
//                isAnyTrashed = true;
//                mContents.remove(index);
//                urls.remove(index);
//                names.remove(index);
//                mAdapter.notifyDataSetChanged();
//                if (index == urls.size()){
//                    index = index-1>=0?index-1:0;
//                }
//                setCurIndex(index);
//                if (urls.size() == 0){
//                    mViewPager.setVisibility(View.INVISIBLE);
//                    btn_trash.setVisibility(View.INVISIBLE);
//                    //  btn_save.setVisibility(View.INVISIBLE);
//                }
//                else{
//                    mViewPager.setVisibility(View.VISIBLE);
//                    btn_trash.setVisibility(View.VISIBLE);
//                    //   btn_save.setVisibility(View.VISIBLE);
//
//                }
//            }
//        }
//    };

//    protected Subscription subscription;
    LoadingDialog lod;
}


