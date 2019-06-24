package com.screening.uitls;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.activity.R;
import com.screening.adapter.Showadapter;
import com.screening.model.Bean;
import com.util.SouthUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManagerUtils {
    private ListView lv_show;//文件展示
    List<String> stringList;//文件路径集合
    private Showadapter arrayAdapter;
    private String getPath;//下一级目录路径
    private Context mContext;
    private String path = "";

    public FileManagerUtils(Context context, ListView lv_show) {
        this.mContext = context;
        this.lv_show = lv_show;
//        openFile=new OpenFile(context);
    }

    /**
     * 递归遍历出目录下面所有文件
     *
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @throws IOException
     */
    public List<String> filelist;
    private List<Bean> constList;
    private List<String> memoryList = new ArrayList<>();//记录上一次的所有文件路径，当下一级目录有数据时，再赋值给它，如果没有，保留上一次数据，防止为空时，路径异常

    public List<String> List(String pathName) throws IOException {
        filelist = new ArrayList<>();
        constList = new ArrayList<>();
        if (pathName.startsWith("/") && pathName.endsWith("/")) {
            final String directory = pathName;
            //更换目录到当前目录

            File[] files = new File(directory).listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    filelist.add(directory + file.getName());
                    Bean bean = new Bean();
                    bean.setImg(R.drawable.wenjian);
                    bean.setFileName(file.getName());
                    bean.setFilePath(file.getAbsolutePath());
                    bean.setCheckBox(new CheckBox(mContext));
                    constList.add(bean);
                } else if (file.isDirectory()) {
                    filelist.add(directory + file.getName() + "/");
                    Bean bean = new Bean();
                    bean.setImg(R.drawable.wenjianja);
                    bean.setCheckBox(new CheckBox(mContext));
                    bean.setFileName(file.getName());
                    bean.setFilePath(file.getAbsolutePath());
                    constList.add(bean);
                }
            }

        }

        if (filelist.size() > 0) {
            memoryList.clear();
            memoryList.addAll(filelist);
        }
        return memoryList;
    }


    public void lvItemClick() {
        lv_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    CheckBox checkBox = constList.get(i).getCheckBox();
                    stringList = new ArrayList<>();
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        stringList.add(constList.get(i).getFilePath());
                    } else {
                        checkBox.setChecked(true);
                        stringList.remove(constList.get(i).getFilePath());
                    }
                } catch (Exception e) {
                    Log.e("异常：", e.getMessage().toString());
                }
            }
        });
    }

    public void returnBefore() {
//        if(OnItem.getOneItem().getTemp()==2){
        if (path != null && !path.equals("") && !path.equals(Environment.getExternalStorageDirectory() + "/")) {
            String[] s = path.split("/");
            path = "";
            if (s.length > 1) {
                for (int i = 0; i < s.length - 1; i++) {
                    path += s[i] + "/";
                }
                try {
                    stringList = List(path);
                    arrayAdapter = new Showadapter(mContext, constList);
                    lv_show.setAdapter(arrayAdapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            SouthUtil.showToast(mContext, mContext.getString(R.string.cannotReturn));
        }

    }

    public void select(String Npath) {
        try {
            path = Npath;
            stringList = List(path);
            arrayAdapter = new Showadapter(mContext, constList);
            lv_show.setAdapter(arrayAdapter);
        } catch (IOException e) {
            Log.e("异常：", e.getMessage().toString());
        }
    }
}
