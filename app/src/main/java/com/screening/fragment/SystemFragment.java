package com.screening.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.activity.R;
import com.screening.adapter.SystemSettingAdapter;
import com.screening.api.SystemSettingContractApi;
import com.screening.impl.ISettingPresenterImpl;
import com.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 系统设置界面
 */
public class SystemFragment extends Fragment implements AdapterView.OnItemClickListener, SystemSettingContractApi.ISettingView {

    private ListView mListView;
    private TextView mTextView;
    private List<String> mListData;
    private SystemSettingAdapter mAdapter;
    private SystemSettingContractApi.ISettingPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.list_setting);
        mTextView = (TextView) view.findViewById(R.id.title_bar);
        mTextView.setText(getString(R.string.setting_title));
        mListData = new ArrayList<String>();
        mPresenter = new ISettingPresenterImpl(this);
        mListView.setOnItemClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        mListData.clear();
        mPresenter.listData();
    }

    private void setAdapterData() {

        mAdapter = new SystemSettingAdapter(getActivity(), mListData);
        mListView.setAdapter(mAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        ToastUtils.showToast(getActivity(),"position = "+position);
        if (position == mListData.size() - 1) {
            return;
        }
        mPresenter.selectItem(position);

    }


    @Override
    public void toActivity(Activity act) {
        if (act != null) {
            Intent it = new Intent(getActivity(), act.getClass());
            startActivity(it);
        }

    }

    @Override
    public void setListData(List<String> list) {
        mListData = list;
        setAdapterData();
    }


}
