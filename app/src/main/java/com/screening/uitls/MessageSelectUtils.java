package com.screening.uitls;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.screening.model.ListMessage;
import com.util.Constss;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhangbin on 2018/4/27.
 * 这个类为信息管理界面查询的方法的工具类
 */

public class MessageSelectUtils {
    private Context context;
    //    private List<String> list2;//查询条件集合
    private EditText editName, editTel, edit_casesearch_01, edit_casesearch_02;
    private List<ListMessage> userCaseList = new ArrayList<>();//查询出符合条件的人员信息集合
    private List<ListMessage> haveCaseList;//已筛查人员
    private List<ListMessage> noCaseList;//未筛查人员
//    boolean f1, f2, f3, f4;

    public MessageSelectUtils() {

    }

    public MessageSelectUtils(Context context, EditText editName, EditText editTel, EditText edit_casesearch_01, EditText edit_casesearch_02) {
        this.context = context;
        this.editName = editName;
        this.editTel = editTel;
        this.edit_casesearch_01 = edit_casesearch_01;
        this.edit_casesearch_02 = edit_casesearch_02;
//        list2 = new ArrayList<>();
    }


    private StringBuffer selectFileds = new StringBuffer();//搜索的字段属性
    private String fields = "";//查询的字段

    //根据条件，获取受检者列表
//    public List<ListMessage> getUserInfo(EditText editName, EditText editTel, EditText editFixTel,
//                                         EditText editIdCard, EditText editStartDate) {
//        selectFileds.setLength(0);
//        fields = "";
//        appendConditAndFileds(editName, "name");
//        appendConditAndFileds(editTel, "phone");//手机
//        appendConditAndFileds(editFixTel, "Fixedtelephone");//固定电话
//        appendConditAndFileds(editIdCard, "IDCard");//身份证
//        appendConditAndFileds(editStartDate, "dayTime");//时间
//
//        if (selectFileds.length() > 0) {
//            fields = selectFileds.substring(0, selectFileds.length() - 5);
//        }
//        return searchUser(fields);
//    }


    public String getFields(String editName, String editTel, String editFixTel,
                            String editIdCard, long editStartDate, long editEndDate) {
        selectFileds.setLength(0);
        fields = "";
        appendStrAndFileds(editName, "name");
        appendStrAndFileds(editTel, "phone");//手机
        appendStrAndFileds(editFixTel, "fixedtelephone");//固定电话
        appendStrAndFileds(editIdCard, "idCard");//身份证
        appendIntAndFileds(editStartDate, editEndDate, "dayTime");//时间

        if (selectFileds.length() > 0) {
            fields = selectFileds.substring(0, selectFileds.length() - 5);
        }
        return fields;
    }

    private void appendStrAndFileds(String content, String strFiled) {
        if (!content.equals("")) {
            selectFileds.append(strFiled + "  like " + "'%" + content + "%'" + "  and  ");
        }
    }

    private void appendIntAndFileds(long startIime, long endTime, String strFiled) {

        if (startIime > 0 && endTime > 0) {
            selectFileds.append(strFiled + "  > " + startIime + "  and  " + strFiled + " < " + (endTime + 24 * 60 * 60 * 1000) + "  and  ");
        } else if (startIime > 0 && endTime == 0) {
            selectFileds.append(strFiled + "  > " + startIime + "  and  ");

        } else if (startIime == 0 && endTime > 0) {
            selectFileds.append(strFiled + "  < " + (endTime + 24 * 60 * 60 * 1000) + "  and  ");

        } else if (startIime == 0 && endTime == 0) {//默认查找当前的信息
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000;
            int min = Calendar.getInstance().get(Calendar.MINUTE) * 60 * 1000;
            int sec = Calendar.getInstance().get(Calendar.SECOND) * 1000;
            long seleceDay = System.currentTimeMillis() - (hour + min + sec);
            Log.i("page", "当前时间 " + System.currentTimeMillis() + " (hour+min+sec) = " + (hour + min + sec));
            selectFileds.append(strFiled + "  > " + seleceDay + "  and  ");
        }
    }


    //查找所有的受检者信息 , String condit1, String condit2, String condit3, String condit4, String condit5
    public List<ListMessage> searchUser(String filed) {
        if (haveCaseList == null) {
            haveCaseList = new ArrayList<>();
        } else {
            haveCaseList.clear();
        }
        if (noCaseList == null) {
            noCaseList = new ArrayList<>();
        } else {
            noCaseList.clear();
        }
        userCaseList.clear();
        try {

            Log.i("page", "查询的字段: " + filed);
            if (filed.equals("")) {
                userCaseList = LitePal.findAll(ListMessage.class);
            } else {
                userCaseList = LitePal.where(filed).find(ListMessage.class);
            }
            if (userCaseList.size() > 0) {
                for (int i = 0; i < userCaseList.size(); i++) {
                    if (userCaseList.get(i).getScreenState() == 3) {
                        haveCaseList.add(userCaseList.get(i));
                    } else {
                        noCaseList.add(userCaseList.get(i));
                    }
                }
            }
            userCaseList.clear();
            userCaseList.addAll(haveCaseList);
            userCaseList.addAll(noCaseList);
//            Item.getOneItem(context).setListMessages(userCaseList);
            return userCaseList;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("page", "查询出现异常 ：" + e.getMessage());
            return null;
        }

    }
}
