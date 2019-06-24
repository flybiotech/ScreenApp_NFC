package com.screening.uitls;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.screening.model.Bean;
import com.screening.model.DoctorId;
import com.screening.model.Item;
import com.screening.model.ListMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

//恢复的工具类，此功能默认为新安装的软件；如果软件原先就有数据，会把原有数据给覆盖
public class RecoveryUtils {

    public Context mContext;
    CopyUtils copyUtils;
    String sdBackPath = "";//sd上的备份路径
    private Thread thread;
    private List<DoctorId> doctorIds;

    public RecoveryUtils(Context mContext) {
        this.mContext = mContext;
        copyUtils = new CopyUtils();
    }

    //1.先判断平板是否安装SD卡,如果安装了，再判断是否有备份文件
    public void isSDExistence() {
        //获取本机所有可用的存储空间
        String[] AllRoutePath = BackupsLitepalUtils.getAllSdPaths(mContext);
        //小于等于1，说明只有内部存储
        if (AllRoutePath.length <= 1) {
            if (recoverResult != null) {
                recoverResult.getRecoverResult(0, false);
            }
            return;
        }
        //获取SD卡的根目录
        String SDPath = AllRoutePath[1];
        Item.getOneItem(mContext).setSdRootPath(SDPath);
        File SDfile = new File(SDPath);
        doctorIds = LitePal.findAll(DoctorId.class);
        //sd上的备份路径
        sdBackPath = SDfile.getAbsolutePath() + "/Android/data/com.flybiotech.screening/" + "BACKUPS" + "/";
        File sdFile = new File(sdBackPath);
        //备份路径下是否有该医生编号下的备份信息
        File[] sdFiles = sdFile.listFiles();
        if (sdFiles != null && sdFiles.length > 0) {
            for (int i = 0; i < sdFiles.length; i++) {
                if (sdFiles[i].getName().contains(doctorIds.get(0).getDoctorId())) {
                    if (recoverResult != null) {
                        recoverResult.getRecoverResult(1, true);
                    }
                    return;
                }
            }
        } else {
            if (recoverResult != null) {
                recoverResult.getRecoverResult(1, false);
            }
        }
    }

    //3.复制成功后开始恢复数据
    public void recoverData(List<String> fileList) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                copyUtils = new CopyUtils();
                boolean fileText = false;
                int result1 = 0;
                boolean result = false;
                for (int i = 0; i < fileList.size(); i++) {
                    File[] files = new File(fileList.get(i)).listFiles();
                    if(files == null || files.length == 0){
                        if(recoverResult != null){
                            recoverResult.getRecoverResult(4,false);
                        }
                        return;
                    }
                    for (int j = 0; j < files.length; j++) {
                        fileText = files[j].getName().contains(".txt");//json文件
                        if (fileText) {
                            result = recoverJson(files[j].getAbsolutePath());
                        } else {
                            result1 = copyUtils.copy(files[j].getAbsolutePath(), Bean.bePath + files[j].getName() + "/");
                        }
                    }
                }
                if (result && result1 == 1) {
                    if (recoverResult != null) {
                        recoverResult.getRecoverResult(3, true);
                    }
                } else {
                    if (recoverResult != null) {
                        recoverResult.getRecoverResult(3, false);
                    }
                }
            }
        });
        thread.start();
    }

    //4.恢复数据库数据
    List<ListMessage> userList;

    private boolean recoverLitepal(String userData) {
        String state = "";
        try {
//            List<ListMessage> userList= LitePal.findAll(ListMessage.class);
            JSONObject jsonObject = new JSONObject(userData);
            JSONArray array = jsonObject.getJSONArray("screenData");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String pID = obj.getString("pId");
                String id = "";
                userList = LitePal.where("pId = ?", pID).find(ListMessage.class);
                if (userList.size() > 0) {
                    userList = LitePal.findAll(ListMessage.class);
                    id = userList.get(userList.size() - 1).getpId();
                    pID = (Integer.parseInt(id) + 1) + "";
                }

                ListMessage listMessage = new ListMessage();
                listMessage.setpId(pID);
                listMessage.setName(obj.getString("name"));
                listMessage.setAge(obj.getString("age"));
                listMessage.setIdCard(obj.getString("IDCard"));
                listMessage.setPhone(obj.getString("phone"));
                listMessage.setFixedtelephone(obj.getString("Fixedtelephone"));
                listMessage.setSex(obj.getString("sex"));
                listMessage.setDataofbirth(obj.getString("dataofbirth"));
                listMessage.setRemarks(obj.getString("Remarks"));
                listMessage.setOccupation(obj.getString("Occupation"));
                listMessage.setSmoking(obj.getString("smoking"));
                listMessage.setAbortion(obj.getString("abortion"));
                listMessage.setSexualpartners(obj.getString("sexualpartners"));
                listMessage.setGestationaltimes(obj.getString("Gestationaltimes"));
                listMessage.setContraception(obj.getString("contraception"));
                listMessage.setMarry(obj.getString("marry"));
                listMessage.setPicYaunTuPath(obj.getString("picYaunTuPath"));
                listMessage.setPicCuSuanPath(obj.getString("picCuSuanPath"));
                listMessage.setPicDianYouPath(obj.getString("picDianYouPath"));
                listMessage.setVedioPath(obj.getString("vedioPath"));
                listMessage.setUploadScreenid(obj.getString("uploadScreenid"));
                listMessage.setHpv(obj.getString(state + "HPV"));
                listMessage.setCytology(obj.getString(state + "cytology"));
                listMessage.setGene(obj.getString(state + "gene"));
                listMessage.setDna(obj.getString(state + "DNA"));
                listMessage.setOther(obj.getString(state + "other"));
                listMessage.setDetailedaddress(obj.getString("Detailedaddress"));
                listMessage.setScreeningId(obj.getString("screeningId"));
                listMessage.setIdentification(obj.getString("Identification"));
                listMessage.setIsCopy(obj.getString("isCopy"));
                if (obj.has("screenState")) {
                    listMessage.setScreenState(obj.getInt("screenState"));
                } else {
                    listMessage.setScreenState(1);
                }
                if (obj.has("dayTime")) {
                    listMessage.setDayTime(obj.getLong("dayTime"));
                } else {
                    listMessage.setDayTime(System.currentTimeMillis());
                }
                listMessage.save();
            }
        } catch (JSONException e) {

            return false;
        }
        return true;
    }

    public boolean recoverJson(String path) {
        boolean recoverLitepal = false;
        String Data = "";
        StringBuffer result = new StringBuffer();
        FileInputStream fis = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(path);
            br = new BufferedReader(new InputStreamReader(fis));
            String line = "";
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                return false;
            }
        }
        Data = result.toString();
        recoverLitepal = recoverLitepal(Data);
        return recoverLitepal;
    }

    public interface RecoverResult {
        void getRecoverResult(int type, boolean result);//0代表sd卡是否不存在;1代表备份文件是否存在;2代表复制是否成功;3代表数据是否恢复成功;4代表文件选择错误
    }

    static RecoverResult recoverResult;

    public static void setRecoverResultLisneter(RecoverResult recoverResultLisneter) {
        recoverResult = recoverResultLisneter;
    }
}
