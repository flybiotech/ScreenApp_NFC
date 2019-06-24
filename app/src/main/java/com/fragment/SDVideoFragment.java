package com.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activity.R;
import com.activity.VideoPlayerActivity;
import com.adapter.RecycleViewDivider;
import com.adapter.SDVideoRecyclerAdapter;
import com.model.DevModel;
import com.model.SDVideoModel;
import com.util.Constss;
import com.util.SouthUtil;
import com.view.LoadingDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
//import rx.functions.Func1;


/**
 * Created by gyl1 on 3/30/17.
 */

public class SDVideoFragment extends Fragment implements SDVideoRecyclerAdapter.OnTextClickListner {


    SDImageFragment.OnCheckChangeListner mOnCheckChangeListner;
    LoadingDialog lod;
    static final String tag = "SDVideoFragment";
    public RecyclerView mRecyclerView;

    public LinearLayoutManager mLinearLayoutManager;
    ;
    public SDVideoRecyclerAdapter adapter;
    public List<SDVideoModel> mVideos = new ArrayList<>();
    public List<SDVideoModel> deleteFileVideoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
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
        Log.d(this.getTag(), "onResume---------");
        super.onResume();
        Constss.videoType = 1;
    }


    @Override
    public void onPause() {
// TODO Auto-generated method stub
        Log.d(this.getTag(), "onPause--------");
        super.onPause();
    }


    public void setModel(DevModel m) {
    }

    private void initView(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(
                this.getContext(), LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.divider)));
    }

    private void initRecylerView() {
        adapter = new SDVideoRecyclerAdapter(this.getActivity(), mVideos);
        adapter.setOnTextClickListner(this);
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        getVideoData();
    }


    @SuppressLint("CheckResult")
    private void getVideoData() {

        Single.create(new SingleOnSubscribe<File[]>() {
            @Override
            public void subscribe(SingleEmitter<File[]> emitter) throws Exception {
                Log.i("tag_11", "视频 getFiles: basePath = " + Constss.fileBasePath);
                String videoPath = Constss.fileBasePath + "/视频";
                File[] files = new File(videoPath).listFiles(); //获取当前文件夹下的所有文件和文件夹
                emitter.onSuccess(files);
            }
        }).flatMap(new Function<File[], SingleSource<ArrayList<SDVideoModel>>>() {
            @Override
            public SingleSource<ArrayList<SDVideoModel>> apply(File[] files) throws Exception {
                return getShowListObservable(files);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<SDVideoModel>>() {
                    @Override
                    public void accept(ArrayList<SDVideoModel> sdVideoModels) throws Exception {
                        mVideos.clear();
                        mVideos = sdVideoModels;
                        adapter.setList(mVideos);
                        adapter.notifyDataSetChanged();
                        mRecyclerView.invalidate();
//
                    }
                });

    }


    //    public List<SDVideoModel> mVideos = new ArrayList<>();
    private Single<ArrayList<SDVideoModel>> getShowListObservable(File[] files) {

        return Observable.fromArray(files).map(new Function<File, String>() {
            @Override
            public String apply(File file) throws Exception {

                return file.getAbsolutePath();
            }
        }).collect(new Callable<ArrayList<SDVideoModel>>() {
            @Override
            public ArrayList<SDVideoModel> call() throws Exception {
                return new ArrayList<>();
            }
        }, new BiConsumer<ArrayList<SDVideoModel>, String>() {
            @Override
            public void accept(ArrayList<SDVideoModel> list, String s2) throws Exception {
                SDVideoModel sdVideoModel = new SDVideoModel();
                sdVideoModel.sdVideo = s2;
                list.add(sdVideoModel);
            }
        });


    }

    public boolean isAnyItemChecked() {
        boolean isAnySelected = false;
        for (int i = mVideos.size() - 1; i >= 0; i--) {
            SDVideoModel model = mVideos.get(i);
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
        for (int i = mVideos.size() - 1; i >= 0; i--) {
            SDVideoModel model = mVideos.get(i);
            if (model.checked) {
                isAnySelected = true;
                break;
            }
        }

        if (isAnySelected) {
            for (int i = mVideos.size() - 1; i >= 0; i--) {
                SDVideoModel model = mVideos.get(i);
                model.checked = false;
            }
            adapter.notifyDataSetChanged();
            return false;
        } else {
            for (int i = mVideos.size() - 1; i >= 0; i--) {
                SDVideoModel model = mVideos.get(i);
                model.checked = true;
            }
            adapter.notifyDataSetChanged();
            return true;
        }

    }


    @SuppressLint("CheckResult")
    public void deleteVideos() {


        boolean isAnySelected = false;
        for (int i = mVideos.size() - 1; i >= 0; i--) {
            SDVideoModel model = mVideos.get(i);
            if (model.checked) {
                isAnySelected = true;
                break;
            }
        }
        if (!isAnySelected) {
            SouthUtil.showToast(this.getContext(), getString(R.string.chooseVideoForDelete));
            return;
        }


        if (lod == null) {
            lod = new LoadingDialog(this.getContext());
        }
        lod.setMessage("");
        lod.dialogShow();
//        Log.e(tag, "deleteVideos");
//        String fileName = "";
        for (SDVideoModel model : mVideos) {
            if (model.checked) {
                deleteFileVideoList.add(model);
//                Log.e(tag, "checked:" + model.sdVideo);
//                fileName += model.sdVideo + ",";
            }
        }

        Observable.fromIterable(deleteFileVideoList)
                .flatMap(new Function<SDVideoModel, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(SDVideoModel sdVideoModel) throws Exception {
                        mVideos.remove(sdVideoModel);
                        File file = new File(sdVideoModel.sdVideo);
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
                        if (mOnCheckChangeListner != null) {
                            mOnCheckChangeListner.OnCheckChange();
                        }

                        adapter.setList(mVideos);
                        adapter.notifyDataSetChanged();
                        mRecyclerView.invalidate();
                    }
                });

    }

    /**
     * @param type 0 if no data ,data request 1:date request
     */
    public void getVideosInActivity(int type) {
        if (mVideos == null) {
            mVideos = new ArrayList<>();
        }
        if (mVideos.size() == 0 || type == 1) {
//            handler.postDelayed(runnable, 50);
        }

    }


    @Override
    public void OnTextClicked(View view, String videoPath, int type) {
//        SDVideoModel sdVideoModel = mVideos.get(position);
        if (type == 0 || type == 2) {
            return;
        }
        Intent intent = new Intent(this.getActivity(), VideoPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("videoPath", videoPath);
        intent.putExtras(bundle);
        this.getActivity().startActivityForResult(intent, 2);
    }

    @Override
    public void OnCheckClicked(int type) {
        if (mOnCheckChangeListner != null) {
            mOnCheckChangeListner.OnCheckChange();
        }
    }

    public interface OnCheckChangeListner {

        void OnCheckChange();//如果有点击，则通知
    }

    public void setOnCheckChangeListner(SDImageFragment.OnCheckChangeListner onCheckChangeListner) {
        mOnCheckChangeListner = onCheckChangeListner;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Constss.videoType = 0;
    }
}
