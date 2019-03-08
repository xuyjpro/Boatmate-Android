package com.example.downtoearth.toget.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.downtoearth.toget.fragment.BaseFragment;

import java.util.List;

/**
 * Created by DownToEarth on 2018/10/18.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mFragmentList;

    public ViewPagerAdapter(FragmentManager fm,List<BaseFragment> list) {
        super(fm);
        this.mFragmentList=list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
