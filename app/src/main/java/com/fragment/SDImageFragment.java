package com.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activity.ImageBrowserActivity;
import com.activity.R;
import com.adapter.SDImageRecyclerAdapter;
import com.model.DevModel;
import com.model.SDImageModel;
import com.util.Constss;
import com.util.SouthUtil;
import com.view.LoadingDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by gyl1 on 3/30/17.
 */

public class SDImageFragment extends Fragment implements SDImageRecyclerAdapter.OnImageClickListner {
//    private DevModel model;
//    private int page = 0;
//    private int length = 10;


    public RecyclerView mRecyclerView;
    //    public SwipeRefreshLayout mSwipeRefreshLayout;
    public GridLayoutManager mGridLayoutManager;
    public SDImageRecyclerAdapter adapter;
    OnCheckChangeListner mOnCheckChangeListner;

    LoadingDialog lod;

    public List<SDImageModel> mImages = new ArrayList<>();
    public ArrayList<String> urls = new ArrayList<>(); //保存图片的路径
    //保存准备删除的图片的 SDImageModel
    public ArrayList<SDImageModel> deleteFileImageList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        initView(rootView);
        initRecylerView();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
// TODO Auto-generated method stub
//        Log.d(this.getTag(), "onResume---------");
        super.onResume();
        getFiles(Constss.fileBasePath);
        Constss.videoType = 1;

    }

    @Override
    public void onPause() {
// TODO Auto-generated method stub
        Log.d(this.getTag(), "onPause--------");
        super.onPause();
    }


    public void setModel(DevModel m) {
//        if (m != null) {
//            model = m;
//            page = 0;
//        }
    }

    private void initView(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initRecylerView() {
        adapter = new SDImageRecyclerAdapter(this.getActivity(), mImages);
        adapter.setOnImageClickListner(this);
        mGridLayoutManager = new GridLayoutManager(this.getActivity(), 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(adapter);


    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void OnImageClicked(View view, int position) {


//        显示放大之后的图片
        Intent intent = new Intent(this.getActivity(), ImageBrowserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("urls", urls);
        bundle.putInt("channel", position);
        intent.putExtras(bundle);

        this.getActivity().startActivityForResult(intent, 1);
        this.getActivity().overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    @Override
    public void OnCheckClicked() {
//通知有变化
        if (mOnCheckChangeListner != null) {
            mOnCheckChangeListner.OnCheckChange();
        }
    }

    public boolean isAnyItemChecked() {
        boolean isAnySelected = false;
        for (int i = mImages.size() - 1; i >= 0; i--) {
            SDImageModel model = mImages.get(i);
            if (model.checked) {
                isAnySelected = true;
                break;
            }
        }
        return isAnySelected;
    }

    public boolean checkAll() {

//首先判断是否已经有选择
        boolean isAnySelected = false;
        for (int i = mImages.size() - 1; i >= 0; i--) {
            SDImageModel model = mImages.get(i);
            if (model.checked) {
                isAnySelected = true;
                break;
            }
        }

        if (isAnySelected) {
            for (int i = mImages.size() - 1; i >= 0; i--) {
                SDImageModel model = mImages.get(i);
                model.checked = false;
            }
            adapter.notifyDataSetChanged();
            return false;
        } else {
            for (int i = mImages.size() - 1; i >= 0; i--) {
                SDImageModel model = mImages.get(i);
                model.checked = true;
            }
            adapter.notifyDataSetChanged();
            return true;
        }

    }


    @SuppressLint("CheckResult")
    public void deleteImages() {

        boolean isAnySelected = false;
        for (int i = mImages.size() - 1; i >= 0; i--) {
            SDImageModel model = mImages.get(i);
            if (model.checked) {
                isAnySelected = true;
                break;
            }
        }
        if (!isAnySelected) {
            SouthUtil.showToast(this.getContext(), getString(R.string.chooseImageForDelete));
            return;
        }

        if (lod == null) {
            lod = new LoadingDialog(this.getContext());
        }
        lod.dialogShow();
//        String fileName = "";
        for (SDImageModel model : mImages) {
            if (model.checked) {
//                Log.e(tag, "checked:" + model.sdImage);
                deleteFileImageList.add(model);
//                fileName += model.sdImage + ",";
            }
        }

        Observable.fromIterable(deleteFileImageList)
                .flatMap(new Function<SDImageModel, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(SDImageModel sdImageModel) throws Exception {
                        mImages.remove(sdImageModel);
                        File file = new File(sdImageModel.sdImage);
                        file.delete();
                        return Observable.just(1);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer aBoolean) throws Exception {
                        if (lod != null) {
                            lod.dismiss();
                        }

                        urls.clear();

                        for (SDImageModel sdImageModel : mImages) {
                            urls.add(sdImageModel.sdImage);

                        }
                        if (mOnCheckChangeListner != null) {
                            mOnCheckChangeListner.OnCheckChange();
                        }

                        adapter.setList(mImages);
                        adapter.notifyDataSetChanged();
                        mRecyclerView.invalidate();
                    }
                });


    }

    /**
     * @param type 0 if no data ,data request 1:date request
     */
    public void getImagesInActivity(int type) {
        if (mImages == null) {
            mImages = new ArrayList<>();
        }
        if (mImages.size() == 0 || type == 1) {

//            handler.postDelayed(runnable, 50);
        }

    }

    public interface OnCheckChangeListner {

        void OnCheckChange();//如果有点击，则通知
    }

    public void setOnCheckChangeListner(OnCheckChangeListner onCheckChangeListner) {
        mOnCheckChangeListner = onCheckChangeListner;
    }


    @SuppressLint("CheckResult")
    private void getFiles(String basePath) {
        mImages.clear();
        urls.clear();
        File rootFile = new File(basePath);
        Observable.just(rootFile)
                .flatMap(new Function<File, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(File file) throws Exception {
                        return listFiles(file);
                    }
                })
                .filter(new Predicate<File>() {
                    @Override
                    public boolean test(File file) throws Exception {
                        //false的话就不会发送事件
                        return file.getName().endsWith(".jpg");
                    }
                })
                .map(new Function<File, SDImageModel>() {
                    @Override
                    public SDImageModel apply(File file) throws Exception {
                        String path = file.getAbsolutePath();
                        SDImageModel sdImageModel = new SDImageModel();
                        sdImageModel.sdImage =path ;
//                        mImages.add(sdImageModel);
                        urls.add(path);
                        return sdImageModel;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SDImageModel>>() {
                    @Override
                    public void accept(List<SDImageModel> strings) throws Exception {
                        mImages = strings;
                        adapter.setList(strings);
                        adapter.notifyDataSetChanged();
                        mRecyclerView.invalidate();
                    }
                });


    }

    // 遍历文件夹
    private Observable<File> listFiles(File file) {
        if (file.isDirectory()) {
            return Observable.fromArray(file.listFiles()).flatMap(new Function<File, ObservableSource<File>>() {
                @Override
                public ObservableSource<File> apply(File file) throws Exception {
                    return listFiles(file);
                }
            });

        } else {
            return Observable.just(file);
        }
    }


}
