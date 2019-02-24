package com.example.downtoearth.toget.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.example.downtoearth.toget.MainActivity;
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.activity.PublishDynamicActivity;
import com.example.downtoearth.toget.adapter.ViewPagerAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DownToEarth on 2018/10/18.
 */

public class HomeFragment extends BaseFragment {
    private ViewPager viewPager;
    private FloatingActionButton mFloatBtn;
    private  List<Fragment> fragments;
    private RefreshLayout smartRefreshLayout;

    private static final int PUBLISH_DYNAMIC=999;
    @Override
    public View initView() {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_home,null);

        smartRefreshLayout = view.findViewById(R.id.smart_refresh);

        viewPager=view.findViewById(R.id.view_pager);
        mFloatBtn=view.findViewById(R.id.btn_floating);

        mFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),PublishDynamicActivity.class);

                startActivityForResult(intent,PUBLISH_DYNAMIC);
            }
        });
        initData();
        return view;
    }

    @Override
    public void initData() {



        fragments=new ArrayList<>();

        fragments.add(DynamicFragment.newInstance(0));
        fragments.add(DynamicFragment.newInstance(1));

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(),fragments));

        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                ((DynamicFragment)fragments.get(0)).getNewData(false);
                refreshlayout.finishLoadmore(1000);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                ((DynamicFragment)fragments.get(0)).getNewData(true);
                refreshlayout.finishRefresh(1000);
            }
        });
        smartRefreshLayout.autoRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case PUBLISH_DYNAMIC:
                if(resultCode==PublishDynamicActivity.ADD_SUCCESS){
                    smartRefreshLayout.autoRefresh();
                }
        }
    }


}
