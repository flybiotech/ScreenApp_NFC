package com.screening.uitls;

import android.util.Log;

import com.screening.model.Addmessage;
import com.screening.model.ListMessage;
import com.util.Constss;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangbin on 2018/4/25.
 * 这个类用来模糊查询符合条件的信息
 */

public class SearchMessage {
    private List<ListMessage> listMessages;//数据库集合
    private List<Addmessage> addmessageList;//listview数据源
    private List<Addmessage> noScreen;//未筛查的信息集合
    private List<Addmessage> haveScreen;//已筛查的信息集合

    /**
     * 查询数据库数据，并将指定字段查询、添加的List_message对象中，作为listview 的数据源
     */
    public List addData() {

        addmessageList = new ArrayList<>();
        if (addmessageList != null && addmessageList.size() > 0) {
            addmessageList.clear();
        }
        listMessages = LitePal.findAll(ListMessage.class);

        noScreen = new ArrayList<>();
        haveScreen = new ArrayList<>();
        for (int i = 0; i < listMessages.size(); i++) {
            Addmessage addmessage = new Addmessage();
            addmessage.setpId(listMessages.get(i).getpId());
            addmessage.setName(listMessages.get(i).getName());
            addmessage.setPhone(listMessages.get(i).getPhone());
            addmessage.setIdCard(listMessages.get(i).getIdCard());
            if (listMessages.get(i).getScreenState() < 3) {
                noScreen.add(addmessage);

            } else if (listMessages.get(i).getScreenState() >= 3) {
                haveScreen.add(addmessage);
            }
        }
        addmessageList.addAll(noScreen);
//                addmessageList.addAll(haveScreen);
        return addmessageList;
    }

    /**
     * 根据输入的查询条件进行模糊查询
     */
    public List vagueSelect(final String condition) {
        addmessageList = new ArrayList<>();
        listMessages = LitePal.findAll(ListMessage.class);
        addmessageList.clear();
        for (int i = 0; i < listMessages.size(); i++) {
            if (listMessages.get(i).getIdCard().contains(condition) && listMessages.get(i).getScreenState() == 1) {
                Addmessage addmessage = new Addmessage();
                addmessage.setpId(listMessages.get(i).getpId());
                addmessage.setName(listMessages.get(i).getName());
                addmessage.setPhone(listMessages.get(i).getPhone());
                addmessage.setIdCard(listMessages.get(i).getIdCard());
                addmessageList.add(addmessage);
            }
        }
        Log.e("search",addmessageList.size()+"");
        return addmessageList;
    }

    /**
     * 根据身份证判断是否已存在
     */
    public static boolean getIdCard(String idCard) {
        List<ListMessage> messages = LitePal.where("idCard = ?", idCard).find(ListMessage.class);
        return messages.size() > 0;
    }


    /**
     * 根据输入的条形码查找是否已存在,temp=1表示HPV,temp=2表示TCT
     */
    public List isExist(final String bar,int temp){
        if(temp==1){
            listMessages=LitePal.where("hpv=?",bar).find(ListMessage.class);
        }else if(temp==2){
            listMessages=LitePal.where("cytology=?",bar).find(ListMessage.class);
        }
        return listMessages;
    }

    /**
     * 判断患者信息是否被修改,被修改后自动保存
     */
    public boolean isModify(String pid, String ID, String name, String age, String sex, String phone, String Fixedtelephone, String dataofbirth, String Occupation, String smoking
            , String sexualpartners, String Gestationaltimes, String abortion, String HPV, String cytology, String gene, String Detailedaddress, String Remarks, String marry, String contraception) {
        listMessages = LitePal.where("pId=?", pid).find(ListMessage.class);
        if (listMessages.size() > 0) {
            if (name.equals(listMessages.get(0).getName()) && age.equals(listMessages.get(0).getAge()) && sex.equals(listMessages.get(0).getSex()) && phone.equals(listMessages.get(0).getPhone()
            ) && Fixedtelephone.equals(listMessages.get(0).getFixedtelephone()) && dataofbirth.equals(listMessages.get(0).getDataofbirth()) && Occupation.equals(listMessages.get(0).getOccupation()) &&
                    smoking.equals(listMessages.get(0).getSmoking()) && sexualpartners.equals(listMessages.get(0).getSexualpartners()) && Gestationaltimes.equals(listMessages.get(0).getGestationaltimes()) &&
                    abortion.equals(listMessages.get(0).getAbortion()) && HPV.equals(listMessages.get(0).getHpv()) && cytology.equals(listMessages.get(0).getCytology()) && gene.equals(listMessages.get(0).getGene()) &&
                    Detailedaddress.equals(listMessages.get(0).getDetailedaddress()) && Remarks.equals(listMessages.get(0).getRemarks()) && marry.equals(listMessages.get(0).getMarry()) &&
                    contraception.equals(listMessages.get(0).getContraception())) {
                return false;//不需要点击保存
            } else {
                return true;//需要点击保存
            }
        }
        return true;
    }

    /**
     * 根据id得到该患者的screenid
     */

    public String getScreenId(String pid) {
        listMessages = LitePal.where("pId=?", pid).find(ListMessage.class);
        if (listMessages.size() > 0) {
            return listMessages.get(0).getScreeningId();
        } else {
            return "";
        }
    }

    //根据id判断该患者是否已采集图片
    public boolean getImage(String idCard){
        listMessages=LitePal.where("idCard=?",idCard).find(ListMessage.class);
        if(listMessages.size()>0){
            String Identification=listMessages.get(0).getIdentification();
            if(Identification.equals("1")){
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
