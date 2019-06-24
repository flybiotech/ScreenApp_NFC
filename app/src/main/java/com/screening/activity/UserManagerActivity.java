package com.screening.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.activity.BaseActivity;
import com.activity.R;
import com.screening.adapter.UserManagerAdapter;
import com.screening.api.UserManagerContractApi;
import com.screening.impl.IUserManagerPresenterImpl;
import com.screening.model.User;
import com.util.SouthUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class UserManagerActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, UserManagerContractApi.IUserManagerView {

    //    private ListView mListView;
//    private List<User> mListData;
    private UserManagerAdapter mAdapter;
    private Button btn_left, bt_area_save;
    private TextView mTitle;
    //    private UserManagerContractApi.IUserManagerPresenter  mPresenter;
    private EditText et_area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);
        init();
    }

    private void init() {
//        mListView = (ListView) findViewById(R.id.list_usermanager);
        mTitle = (TextView) findViewById(R.id.title_bar);
        mTitle.setText(getString(R.string.setting_area_msg));
        btn_left = (Button) findViewById(R.id.btn_left);
        bt_area_save = findViewById(R.id.bt_area_save);
//        mPresenter = new IUserManagerPresenterImpl(this);
        et_area = findViewById(R.id.et_area);
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);
        bt_area_save.setOnClickListener(this);
//        mListView.setOnItemClickListener(this);
//        mListData = new ArrayList<User>();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        mPresenter.searchUserData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.bt_area_save:

                finish();
                break;


            default:
                break;

        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (position != mListData.size() - 1) {
//            mPresenter.deleteAfterData(mListData.get(position).getName());
//        }


    }


    @Override
    public void setListData(List<User> list) {
//        mListData = list;
        show();


    }

    @Override
    public Context getContext() {
        return this;
    }

    private void show() {
//        if (mAdapter == null) {
//            mAdapter = new UserManagerAdapter(this, mListData);
//            mListView.setAdapter(mAdapter);
//        } else {
//            mAdapter.setList(mListData);
//            mAdapter.notifyDataSetChanged();
//        }


    }


}
