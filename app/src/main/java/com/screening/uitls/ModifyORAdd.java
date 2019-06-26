package com.screening.uitls;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.activity.R;
import com.screening.model.Item;

import com.screening.model.ListMessage;
import com.util.Constss;
import com.util.SouthUtil;

import org.litepal.LitePal;

import java.util.List;

/**
 * Created by zhangbin on 2018/4/25.
 * 这个类用来展示、修改列表信息或者登记新的信息
 */

public class ModifyORAdd {
    List<ListMessage> listMessages;
    private Context context;
    private EditText et_pId, et_pName, et_pAge, et_pSex, et_pPhone, et_pLocalPhone, et_pDate, et_pOccupation, et_smoking, et_pSexualpartners, et_pGestationaltimes,
            pAbortion, et_pRequiredID, et_pRequiredHPV, et_pRequiredCytology, et_pDetailedaddress, et_pRemarks, et_pRequiredGene, et_pRequiredDNA, et_pRequiredOther;
    private String sp_marray, sp_contraception;

    private String pid, name, age, sex, phone, localPhone, data, occupation, smoking, sexualPartners, gestationltimes, abordtion, requiredID, RequiredHPV,
            RequiredCytology, Detailedaddress, Remarks, marry, birthControlMode, gene, screeningId, dna, other;

    private Item mConst;

    private BackupsLitepalUtils backupsLitepalUtils;

    public ModifyORAdd(Context context, EditText et_pId, EditText et_pName, EditText et_pAge, EditText et_pSex, EditText et_pPhone, EditText et_pLocalPhone,
                       EditText et_pDate, EditText et_pOccupation, EditText et_smoking, EditText et_pSexualpartners, EditText et_pGestationaltimes,
                       EditText pAbortion, EditText et_pRequiredID, EditText et_pRequiredHPV, EditText et_pRequiredCytology, EditText et_pDetailedaddress,
                       EditText et_pRemarks, String marry, String contraception, EditText et_pRequiredGene, String screeningId, EditText et_pRequiredDNA, EditText et_pRequiredOther) {
        mConst = Item.getOneItem(context);
        this.context = context;
        this.et_pId = et_pId;
        this.et_pName = et_pName;
        this.et_pAge = et_pAge;
        this.et_pSex = et_pSex;
        this.et_pPhone = et_pPhone;
        this.et_pLocalPhone = et_pLocalPhone;
        this.et_pDate = et_pDate;
        this.et_pOccupation = et_pOccupation;
        this.et_smoking = et_smoking;
        this.et_pSexualpartners = et_pSexualpartners;
        this.et_pGestationaltimes = et_pGestationaltimes;
        this.pAbortion = pAbortion;
        this.et_pRequiredID = et_pRequiredID;
        this.et_pRequiredHPV = et_pRequiredHPV;
        this.et_pRequiredCytology = et_pRequiredCytology;
        this.et_pDetailedaddress = et_pDetailedaddress;
        this.et_pRemarks = et_pRemarks;
        this.sp_marray = marry;
        this.sp_contraception = contraception;
        this.et_pRequiredGene = et_pRequiredGene;
        this.screeningId = screeningId;
        this.et_pRequiredDNA = et_pRequiredDNA;
        this.et_pRequiredOther = et_pRequiredOther;
        backupsLitepalUtils = new BackupsLitepalUtils(context);
    }


    private int Mphone = 0, Mage = 0, MHPV = 0, MCytology = 0, MGene = 0;

    /**
     * 修改信息
     */
    public boolean modify() {
        boolean isTrue = false;//判断是否修改成功
        getEdittextMsg();
        listMessages = LitePal.where("idCard=?", requiredID).find(ListMessage.class);
        if (!requiredID.equals("") && requiredID != null && !name.equals("") && name != null) {
            for (int i = 0; i < listMessages.size(); i++) {
                listMessages.get(0).setName(name);
                listMessages.get(0).setAge(age);
                listMessages.get(0).setSex(sex);
                listMessages.get(0).setPhone(phone);
                listMessages.get(0).setFixedtelephone(localPhone);
                listMessages.get(0).setDataofbirth(data);
                listMessages.get(0).setOccupation(occupation);
                listMessages.get(0).setSmoking(smoking);
                listMessages.get(0).setSexualpartners(sexualPartners);
                listMessages.get(0).setGestationaltimes(gestationltimes);
                listMessages.get(0).setAbortion(abordtion);
                listMessages.get(0).setIdCard(requiredID);
                listMessages.get(0).setHpv(RequiredHPV);
                listMessages.get(0).setCytology(RequiredCytology);
                listMessages.get(0).setDetailedaddress(Detailedaddress);
                listMessages.get(0).setRemarks(Remarks);
                listMessages.get(0).setMarry(mConst.getMarry());
                listMessages.get(0).setContraception(mConst.getBirthControlMode());
                listMessages.get(0).setGene(gene);
                listMessages.get(0).setDna(dna);
                listMessages.get(0).setOther(other);
                listMessages.get(0).setScreeningId(mConst.getScreeningId());
                listMessages.get(0).setUploadScreenid(Constss.UPDATE_CODE_SUC_00);
                listMessages.get(0).setDayTime(System.currentTimeMillis());
//                listMessages.get(0).setScreenState(1);

                isTrue = listMessages.get(0).save();
                Log.e("msgaaab", listMessages.get(0).getPicYaunTuPath());
                if (isTrue) {
                    Toast.makeText(context, context.getString(R.string.modifysuccess) + "", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getString(R.string.modifyfaild) + "", Toast.LENGTH_SHORT).show();
                }
            }

        } else {
            SouthUtil.showToast(context, context.getString(R.string.requestInfo));
        }
        return isTrue;
    }

    /**
     * 添加信息
     */
    public boolean addMessage() {
        boolean isAdd = false;
        ListMessage listMessage = new ListMessage();
        getEdittextMsg();
        if (!name.equals("") && name != null && !requiredID.equals("") && requiredID != null) {
            if (!et_pRequiredID.getText().toString().trim().equals("")) {
                {
                    listMessage.setpId(pid);
                    listMessage.setName(name);
                    listMessage.setAge(age);
                    listMessage.setSex(sex);
                    listMessage.setPhone(phone);
                    listMessage.setFixedtelephone(localPhone);
                    listMessage.setDataofbirth(data);
                    listMessage.setOccupation(occupation);
                    listMessage.setSmoking(smoking);
                    listMessage.setSexualpartners(sexualPartners);
                    listMessage.setGestationaltimes(gestationltimes);
                    listMessage.setAbortion(abordtion);
                    listMessage.setIdCard(requiredID);
                    listMessage.setHpv(RequiredHPV);
                    listMessage.setCytology(RequiredCytology);
                    listMessage.setDetailedaddress(Detailedaddress);
                    listMessage.setRemarks(Remarks);
                    listMessage.setGene(gene);
                    listMessage.setOther(other);
                    listMessage.setDna(dna);
                    listMessage.setMarry(mConst.getMarry());
                    listMessage.setContraception(mConst.getBirthControlMode());
                    listMessage.setIdentification("0");
                    listMessage.setScreeningId(mConst.getScreeningId());
                    listMessage.setUploadScreenid(Constss.UPDATE_CODE_SUC_00);
                    listMessage.setTaskNumber("");
                    listMessage.setDayTime(System.currentTimeMillis());
                    listMessage.setIsCopy(Constant.COPYCODE_0);
                    listMessage.setScreenState(1);
                    listMessage.setPicYaunTuPath("");
                    listMessage.setPicCuSuanPath("");
                    listMessage.setPicDianYouPath("");
                    listMessage.setVedioPath("");
                    isAdd = listMessage.save();
                    if (isAdd) {
                        Toast.makeText(context, context.getString(R.string.addsuccess), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, context.getString(R.string.addfaild), Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                Toast.makeText(context, "您输入的身份证号位数为" + et_pRequiredID.getText().toString().trim().length() + ",请输入正确的身份证号", Toast.LENGTH_SHORT).show();
            }
//            }
        } else {
            SouthUtil.showToast(context, context.getString(R.string.requestInfo));
        }
        return isAdd;
    }

    /**
     * 得到输入框的值
     */
    public void getEdittextMsg() {
        pid = et_pId.getText().toString().trim();
        name = et_pName.getText().toString().trim();
        age = et_pAge.getText().toString().trim();
        sex = et_pSex.getText().toString().trim();
        phone = et_pPhone.getText().toString().trim();
        localPhone = et_pLocalPhone.getText().toString().trim();
        data = et_pDate.getText().toString().trim();
        occupation = et_pOccupation.getText().toString().trim();
        smoking = et_smoking.getText().toString().trim();
        sexualPartners = et_pSexualpartners.getText().toString().trim();
        gestationltimes = et_pGestationaltimes.getText().toString().trim();
        abordtion = pAbortion.getText().toString().trim();
        requiredID = et_pRequiredID.getText().toString().trim();
        RequiredHPV = et_pRequiredHPV.getText().toString().trim();
        RequiredCytology = et_pRequiredCytology.getText().toString().trim();
        Detailedaddress = et_pDetailedaddress.getText().toString().trim();
        Remarks = et_pRemarks.getText().toString().trim();
        gene = et_pRequiredGene.getText().toString().trim();
        dna = et_pRequiredDNA.getText().toString().trim();
        other = et_pRequiredOther.getText().toString().trim();
    }

    /**
     * 询问提示框
     */
    public void againAsk(String title, String message) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)//设置对话框的标题
                .setMessage(message)//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearData();
                        dialog.dismiss();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 清空输入框
     */
    public void clearData() {
        et_pName.setText("");
        et_pAge.setText("");
        et_pPhone.setText("");
        et_pLocalPhone.setText("");
        et_pDate.setText("");
        et_pOccupation.setText("");
        et_smoking.setText("");
        et_pSexualpartners.setText("");
        et_pGestationaltimes.setText("");
        pAbortion.setText("");
        et_pRequiredID.setText("");
        et_pRequiredHPV.setText("");
        et_pRequiredCytology.setText("");
        et_pDetailedaddress.setText("");
        et_pRemarks.setText("");
        et_pRequiredGene.setText("");
        et_pRequiredDNA.setText("");
        et_pRequiredOther.setText("");
        sp_marray = "无";
        sp_contraception = "无";
    }
}
