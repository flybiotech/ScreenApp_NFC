package com.screening.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.activity.R;
import com.screening.activity.ShowActivity;
import com.screening.model.ListMessage;
import com.screening.uitls.ModifyORAdd;
import com.util.AlignedTextUtils;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * 新增信息界面，如果未录入信息，展示新增信息界面
 */
public class EnregisterFragment extends Fragment implements View.OnClickListener {
    private TextView tv01, tv02, tv03, tv04, tv05, tv06, tv07, tv08, tv09, tv10, tv11, tv12, tv13, tv14, tv15, tv16, tv17, tv18, tv19, title_text;
    private String[] tvName;//标识字段的名称集合
    private EditText et_pId, et_pName, et_pAge, et_pSex, et_pPhone, et_pLocalPhone, et_pDate, et_pOccupation, et_smoking, et_pSexualpartners, et_pGestationaltimes,
            pAbortion, et_pRequiredID, et_pRequiredHPV, et_pRequiredCytology, et_pDetailedaddress, et_pRemarks;
    private Spinner sp_marray, sp_contraception;
    private Button btn_left, btn_right, bt_clear, bt_save, bt_getImage;
    private String[] marryDatas;//婚否
    private String[] bhdDatas;//避孕方式
    ModifyORAdd modifyORAdd;
    List<ListMessage> listMessages;
    private String marry, birthControlMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enregister, container, false);
        initView(view);
        initTextView(view);
        initData();//spinner初始化数据
        initClick();
//        modifyORAdd.initSelectItemClick();
        return view;
    }

    private void initClick() {
        bt_clear.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        bt_getImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_clearM:
                modifyORAdd.clearData();
                break;
            case R.id.bt_save:

                modifyORAdd.addMessage();

            case R.id.bt_getImage:
                break;
        }
    }

    private void initTextView(View view) {
        tvName = new String[]{getString(R.string.pId), getString(R.string.pName), getString(R.string.pAge), getString(R.string.pSex), getString(R.string.RequiredPhone),
                getString(R.string.localPhone), getString(R.string.birthData), getString(R.string.Occupation), getString(R.string.smoking), getString(R.string.sexualpartners), getString(R.string.Gestationaltimes), getString(R.string.abortion),
                getString(R.string.RequiredID), getString(R.string.RequiredHPV),
                getString(R.string.RequiredCytology), getString(R.string.Detailedaddress),
                getString(R.string.Remarks), getString(R.string.marry),
                getString(R.string.contraception)};
        tv01 = (TextView) view.findViewById(R.id.tv_1);
        tv02 = (TextView) view.findViewById(R.id.tv_2);
        tv03 = (TextView) view.findViewById(R.id.tv_3);
        tv04 = (TextView) view.findViewById(R.id.tv_4);
        tv05 = (TextView) view.findViewById(R.id.tv_5);
        tv06 = (TextView) view.findViewById(R.id.tv_6);
        tv07 = (TextView) view.findViewById(R.id.tv_7);
        tv08 = (TextView) view.findViewById(R.id.tv_8);
        tv09 = (TextView) view.findViewById(R.id.tv_9);
        tv10 = (TextView) view.findViewById(R.id.tv_10);
        tv11 = (TextView) view.findViewById(R.id.tv_11);
        tv12 = (TextView) view.findViewById(R.id.tv_12);
        tv13 = (TextView) view.findViewById(R.id.tv_13);
        tv14 = (TextView) view.findViewById(R.id.tv_14);
        tv15 = (TextView) view.findViewById(R.id.tv_15);
        tv16 = (TextView) view.findViewById(R.id.tv_16);
        tv17 = (TextView) view.findViewById(R.id.tv_17);
        tv18 = (TextView) view.findViewById(R.id.tv_18);
        tv19 = (TextView) view.findViewById(R.id.tv_19);
        TextView[] tvData = {tv01, tv02, tv03, tv04, tv05, tv06, tv07, tv08, tv09, tv10, tv11, tv12, tv13, tv14, tv15, tv16, tv17, tv18, tv19};
        for (int i = 0; i < tvName.length; i++) {
            if (tvData[i] != null) {
                tvData[i].setText(AlignedTextUtils.justifyString(tvName[i], 5));
            }
        }
    }

    private void initData() {
        marryDatas = new String[]{getString(R.string.patient_nothing), getString(R.string.patient_married), getString(R.string.patient_unmarried)};
        bhdDatas = new String[]{getString(R.string.patient_nothing), getString(R.string.patient_condom), getString(R.string.patient_acyterion), getString(R.string.patient_Contraceptive_needle),
                getString(R.string.patient_female_condom), getString(R.string.patient_Ligation), getString(R.string.patient_Women__IUD)};
        ArrayAdapter<String> adapterMarry = new ArrayAdapter<String>(getActivity(), R.layout.adapter_item, R.id.sp_textview, marryDatas);
        ArrayAdapter<String> adapterSource = new ArrayAdapter<String>(getActivity(), R.layout.adapter_item, R.id.sp_textview, bhdDatas);
        sp_marray.setAdapter(adapterMarry);
        sp_contraception.setAdapter(adapterSource);
    }

    private void initView(View view) {
        bt_clear = view.findViewById(R.id.bt_clearM);
        bt_save = view.findViewById(R.id.bt_save);
        bt_getImage = view.findViewById(R.id.bt_getImage);
        et_pId = view.findViewById(R.id.et_pId);
        et_pName = view.findViewById(R.id.et_pName);
        et_pAge = view.findViewById(R.id.et_pAge);
        et_pSex = view.findViewById(R.id.et_pSex);
        et_pPhone = view.findViewById(R.id.et_pPhone);
        et_pLocalPhone = view.findViewById(R.id.et_pLocalPhone);
        et_pDate = view.findViewById(R.id.et_pDate);
        et_pOccupation = view.findViewById(R.id.et_pOccupation);
        et_smoking = view.findViewById(R.id.et_smoking);
        et_pSexualpartners = view.findViewById(R.id.et_pSexualpartners);
        et_pGestationaltimes = view.findViewById(R.id.et_pGestationaltimes);
        pAbortion = view.findViewById(R.id.pAbortion);
        et_pRequiredID = view.findViewById(R.id.et_pRequiredID);
        et_pRequiredHPV = view.findViewById(R.id.et_pRequiredHPV);
        et_pRequiredCytology = view.findViewById(R.id.et_pRequiredCytology);
        et_pDetailedaddress = view.findViewById(R.id.et_pDetailedaddress);
        et_pRemarks = view.findViewById(R.id.et_pRemarks);
        sp_marray = view.findViewById(R.id.sp_marray);
        sp_contraception = view.findViewById(R.id.sp_contraception);
        title_text = view.findViewById(R.id.title_text);
        title_text.setText(getString(R.string.patients_information));
        btn_left = view.findViewById(R.id.btn_left);
        btn_right = view.findViewById(R.id.btn_right);
        btn_right.setVisibility(View.VISIBLE);
        btn_left.setVisibility(View.INVISIBLE);
        btn_left.setText(getString(R.string.patient_return));
//
//        modifyORAdd=new ModifyORAdd(getActivity(),et_pId,et_pName,et_pAge,et_pSex,et_pPhone,et_pLocalPhone,et_pDate,et_pOccupation,et_smoking,et_pSexualpartners,et_pGestationaltimes,
//                pAbortion,et_pRequiredID,et_pRequiredHPV,et_pRequiredCytology,et_pDetailedaddress,et_pRemarks,sp_marray,sp_contraception);
//        modifyORAdd.initField();
    }

}
