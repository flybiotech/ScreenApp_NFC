package com.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.fragment.SDImageFragment;
import com.fragment.SDVideoFragment;
import com.model.DevModel;




/**
 * Created by gyl1 on 3/30/17.
 */

public class TabFragmentPagerAdapter  extends FragmentPagerAdapter {
    private int length=2;
//    private DevModel model;
    public TabFragmentPagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }
    public void setModel(DevModel m){
//        if (m != null){
//            model = m;
//        }
    }
    @Override
    public Fragment getItem(int arg0) {
        Fragment ft = null;
        switch (arg0) {

            case 0:
                ft = new SDImageFragment();
//                ((SDImageFragment)ft).setModel(model);

                break;
            case 1:
                ft = new SDVideoFragment();
//                ((SDVideoFragment)ft).setModel(model);
                break;
            default:
                break;

        }
        return ft;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        switch (position) {
            case 0:
                sdImageFragment = (SDImageFragment) createdFragment;
                break;
            case 1:
                sdVideoFragment = (SDVideoFragment) createdFragment;
                break;
        }
        return createdFragment;
    }

    public SDImageFragment getSDImageFragment(){
        return sdImageFragment;
    }

    public SDVideoFragment getSDVideoFragment(){
        return sdVideoFragment;
    }
    @Override
    public int getCount() {
        return length;
    }


    public void setTabTitleSize(int size) {
        length = size;

    }


    private SDImageFragment sdImageFragment;
    private SDVideoFragment sdVideoFragment;


}
