package com.screening.uitls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.R;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.logger.LogHelper;
import com.orhanobut.logger.Logger;
import com.screening.model.ListMessage;
import com.util.AlignedTextUtils;

import org.litepal.LitePal;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zhangbin on 2018/4/27.
 */

public class CaseListUtils {
    private Context context;
    StringBuilder sb1 = new StringBuilder();
    List<Integer> listAllLength1 = new ArrayList<>();
    List<Integer> startList1 = new ArrayList<>();
    List<Integer> endList1 = new ArrayList<>();
    TextView tv_show;//登记信息
    private List<String> imageAll;//所有图片的集合
    private List<String> videoPathlist;//视频地址集合
    private ConvenientBanner convenientBanner;//轮播
    private TextView tv_imagenameshow01;//图片展示

    public CaseListUtils(Context context, ConvenientBanner convenientBanner, TextView tv_imagenameshow01) {
        this.context = context;
        this.convenientBanner = convenientBanner;
        this.tv_imagenameshow01 = tv_imagenameshow01;

    }

    /**
     * 将详细信息归结到一个字符串中
     */
    public void initViewContent(TextView view, ListMessage listMessage) {
        tv_show = view;
        if (listMessage != null) {
            sb1.delete(0, sb1.length());
            listAllLength1.clear();
            startList1.clear();
            startList1.clear();


            sb1.append(context.getString(R.string.pId) + " : " + listMessage.getpId().toString() + "\n\n");
            startList1.add(0);
            endList1.add(context.getString(R.string.pId).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.pName) + " : " + listMessage.getName().toString() + "\n\n");
            startList1.add(listAllLength1.get(0));
            endList1.add(listAllLength1.get(0) + context.getString(R.string.pName).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.pAge) + " : " + listMessage.getAge().toString() + "\n\n");
            startList1.add(listAllLength1.get(1));
            endList1.add(listAllLength1.get(1) + context.getString(R.string.pAge).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.pSex) + " : " + listMessage.getSex().toString() + "\n\n");
            startList1.add(listAllLength1.get(2));
            endList1.add(listAllLength1.get(2) + context.getString(R.string.pSex).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.RequiredPhone) + " : " + listMessage.getPhone().toString() + "\n\n");
            startList1.add(listAllLength1.get(3));
            endList1.add(listAllLength1.get(3) + context.getString(R.string.RequiredPhone).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.localPhone) + " : " + listMessage.getFixedtelephone().toString() + "\n\n");
            startList1.add(listAllLength1.get(4));
            endList1.add(listAllLength1.get(4) + context.getString(R.string.localPhone).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.birthData) + " : " + listMessage.getDataofbirth().toString() + "\n\n");
            startList1.add(listAllLength1.get(5));
            endList1.add(listAllLength1.get(5) + context.getString(R.string.birthData).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.Occupation) + " : " + listMessage.getOccupation().toString() + "\n\n");
            startList1.add(listAllLength1.get(6));
            endList1.add(listAllLength1.get(6) + context.getString(R.string.Occupation).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.smoking) + " : " + listMessage.getSmoking().toString() + "\n\n");
            startList1.add(listAllLength1.get(7));
            endList1.add(listAllLength1.get(7) + context.getString(R.string.smoking).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.sexualpartners) + " : " + listMessage.getSexualpartners().toString() + "\n\n");
            startList1.add(listAllLength1.get(8));
            endList1.add(listAllLength1.get(8) + context.getString(R.string.sexualpartners).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.Gestationaltimes) + " : " + listMessage.getGestationaltimes().toString() + "\n\n");
            startList1.add(listAllLength1.get(9));
            endList1.add(listAllLength1.get(9) + context.getString(R.string.Gestationaltimes).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.abortion) + " : " + listMessage.getAbortion().toString() + "\n\n");
            startList1.add(listAllLength1.get(10));
            endList1.add(listAllLength1.get(10) + context.getString(R.string.abortion).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.RequiredID) + " : " + listMessage.getIdCard().toString() + "\n\n");
            startList1.add(listAllLength1.get(11));
            endList1.add(listAllLength1.get(11) + context.getString(R.string.RequiredID).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.RequiredHPV) + " : " + listMessage.getHpv().toString() + "\n\n");
            startList1.add(listAllLength1.get(12));
            endList1.add(listAllLength1.get(12) + context.getString(R.string.RequiredHPV).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.RequiredCytology) + " : " + listMessage.getCytology().toString() + "\n\n");
            startList1.add(listAllLength1.get(13));
            endList1.add(listAllLength1.get(13) + context.getString(R.string.RequiredCytology).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.RequiredGene) + " : " + listMessage.getGene().toString() + "\n\n");
            startList1.add(listAllLength1.get(14));
            endList1.add(listAllLength1.get(14) + context.getString(R.string.RequiredGene).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.Detailedaddress) + " : " + listMessage.getDetailedaddress().toString() + "\n\n");
            startList1.add(listAllLength1.get(15));
            endList1.add(listAllLength1.get(15) + context.getString(R.string.Detailedaddress).length());
            listAllLength1.add(sb1.toString().length());


            sb1.append(context.getString(R.string.marry) + " : " + listMessage.getMarry().toString() + "\n\n");
            startList1.add(listAllLength1.get(16));
            endList1.add(listAllLength1.get(16) + context.getString(R.string.marry).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.contraception) + " : " + listMessage.getContraception().toString() + "\n\n");
            startList1.add(listAllLength1.get(17));
            endList1.add(listAllLength1.get(17) + context.getString(R.string.contraception).length());
            listAllLength1.add(sb1.toString().length());

            sb1.append(context.getString(R.string.Remarks) + " : " + listMessage.getRemarks().toString() + "\n\n");
            startList1.add(listAllLength1.get(18));
            endList1.add(listAllLength1.get(18) + context.getString(R.string.Remarks).length());
            listAllLength1.add(sb1.toString().length());


            if (listMessage.getScanTime() != null) {
                sb1.append(context.getString(R.string.scantime) + " :  " + listMessage.getScanTime().toString() + "\n\n");
                startList1.add(listAllLength1.get(19));
                endList1.add(listAllLength1.get(19) + context.getString(R.string.scantime).length());
                listAllLength1.add(sb1.toString().length());
            }

            tv_show.setText(AlignedTextUtils.addConbine1(sb1.toString(), startList1, endList1));
            Logger.e("登记信息：sb.toString()" + sb1.toString());
        }
    }

    private List<ListMessage> listMsg = null;

    @SuppressLint("CheckResult")
    public void initView(String id, OnShowContentListener listener) {
        if (listener == null) return;


        if (listMsg == null) {
            listMsg = new ArrayList<>();
        } else {
            listMsg.clear();
        }


        Observable.create(new ObservableOnSubscribe<List<ListMessage>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ListMessage>> emitter) throws Exception {
                listMsg = LitePal.where("pId = ?", id).find(ListMessage.class);
                emitter.onNext(listMsg);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ListMessage>>() {
                    @Override
                    public void accept(List<ListMessage> listMessages) throws Exception {
                        listener.initView(listMessages);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void startImageShow(ListMessage msg, String ytType, String csbType, String dyType, OnShowContentListener listener) {
        if (listener == null || msg == null) return;

        if (imageAll == null) {
            imageAll = new ArrayList<String>();
        } else {
            imageAll.clear();
        }

        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                String imageFilePath = getImageFilePath(msg);

                List<String> ytPath = getAllImageAbsolutePath(ytType, imageFilePath);
                imageAll.addAll(ytPath);

                List<String> csbPath = getAllImageAbsolutePath(csbType, imageFilePath);
                imageAll.addAll(csbPath);

                List<String> dyPath = getAllImageAbsolutePath(dyType, imageFilePath);
                imageAll.addAll(dyPath);
                emitter.onNext(imageAll);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        listener.showImage(strings);
                    }
                });
    }

    private String getImageFilePath(ListMessage msg) {

        String path = Environment.getExternalStorageDirectory() + "/ScreenApp/图片和视频/" + msg.getIdCard();
        File mFile = new File(path);
        LogHelper.i("信息管理-文件夹路径 path1=" + path);
        if (!mFile.exists()) {
            //重命名之后的文件夹名称
            path = Environment.getExternalStorageDirectory() + "/ScreenApp/图片和视频/" + msg.getTaskNumber();

        }
        LogHelper.i("信息管理-文件夹路径 path2=" + path + " , msg.getTaskNumber() =" + msg.getTaskNumber());
        return path;
    }


    List<String> listImagePath = null;

    private List<String> getAllImageAbsolutePath(String imageType, String path1) {
        if (listImagePath == null) {
            listImagePath = new ArrayList<String>();
        } else {
            listImagePath.clear();
        }
        String path = path1 + "/" + imageType;
        Log.i("TAG_1", "getAllImageAbsolutePath: path = " + path);
        File mFile = new File(path);

        if (mFile.exists()) {

            File[] files = new File(path).listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    boolean a = false;
                    if (name.endsWith(".jpg")) {//所有.jpg格式的文件添加到数组中
                        a = true;
                    }
                    return a;
                }
            });
            if (files != null && files.length > 0) {
                for (File file1 : files) {
                    if (file1.getName().endsWith(".jpg")) ;
                    listImagePath.add(file1.getAbsolutePath());
                }
            }

        }
        return listImagePath;
    }

    @SuppressLint("CheckResult")
    public void videoShow(ListMessage listMessage, OnShowContentListener listener) {

        if (listMessage == null) return;

        if (videoPathlist == null) {
            videoPathlist = new ArrayList<>();
        } else {
            videoPathlist.clear();
        }


        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                String videoPath = listMessage.getVedioPath();
                if (new File(videoPath).exists()) {
                    File[] files = new File(videoPath).listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            boolean a = false;
                            if (name.endsWith(".mp4")) {//所有.jpg格式的文件添加到数组中
                                a = true;
                            }
                            return a;
                        }
                    });
                    if (files != null && files.length > 0) {
                        for (File file1 : files) {
                            if (file1.getName().endsWith(".mp4")) ;
//                            videoList.add(file1.getName());
                            videoPathlist.add(file1.getAbsolutePath());
                        }
                    }
                }
                emitter.onNext(videoPathlist);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        listener.showVideo(strings);
                    }
                });

    }


    /**
     * 图片翻页监视
     */
    public void lunbo(List<String> list) {
//        List<String> list = ImageShow();
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
            @Override
            public LocalImageHolderView createHolder() {

                return new LocalImageHolderView();
            }
        }, list)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
        ;
    }

    /**
     * 轮播回调接口
     */
    class LocalImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, final String data) {
//            final List<String> list = ImageShow();
            convenientBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (imageAll != null)
                        tv_imagenameshow01.setText("图片展示 ： " + new File(imageAll.get(position)).getName());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            Glide.with(context).load(data)
                    .into(imageView);
        }

    }


    public interface OnShowContentListener {

        void initView(List<ListMessage> listMsg);

        void showImage(List<String> listImagePath);

        void showVideo(List<String> listVideoPath);

    }


}
