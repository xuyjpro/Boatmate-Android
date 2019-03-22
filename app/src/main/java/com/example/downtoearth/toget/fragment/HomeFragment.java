package com.example.downtoearth.toget.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.activity.PublishDynamicActivity;
import com.example.downtoearth.toget.adapter.ViewPagerAdapter;
import com.example.downtoearth.toget.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DownToEarth on 2018/10/18.
 */

public class HomeFragment extends BaseFragment {
    private ViewPager viewPager;
    private FloatingActionButton mFloatBtn;
    private RadioGroup radioGroup;
    private  List<BaseFragment> fragments;

    private static final int PUBLISH_DYNAMIC=999;
    @Override
    public View initView() {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_home,null);


        viewPager=view.findViewById(R.id.view_pager);
        mFloatBtn=view.findViewById(R.id.btn_floating);

        mFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),PublishDynamicActivity.class);

                startActivityForResult(intent,PUBLISH_DYNAMIC);
            }
        });
        radioGroup=view.findViewById(R.id.radio_group);

        for(int i=0;i<radioGroup.getChildCount();i++){
            RadioButton rb= (RadioButton) radioGroup.getChildAt(i);
            Drawable drawable = getResources().getDrawable(
                    R.drawable.home_top_rb);
            // / 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, ToolUtils.dip2px(getContext(),16),
                    ToolUtils.dip2px(getContext(),4));
            rb.setCompoundDrawables(null, null, null, drawable);
        }
        initData();
        return view;
    }

    @Override
    public void initData() {



        fragments=new ArrayList<>();



        fragments.add(DynamicFragment.newInstance(0));
        fragments.add(DynamicFragment.newInstance(1));


        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(),fragments));
        viewPager.setCurrentItem(0);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb1:

                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.rb2:
                        viewPager.setCurrentItem(1);
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case PUBLISH_DYNAMIC:
                if(resultCode==PublishDynamicActivity.ADD_SUCCESS){

                    ((DynamicFragment)fragments.get(0)).autoRefresh();
                    ((DynamicFragment)fragments.get(1)).autoRefresh();


                }
            case 1000://点击详情
                    if(viewPager.getCurrentItem()==0){
                        ((DynamicFragment)fragments.get(1)).autoRefresh();
                    }else{
                        ((DynamicFragment)fragments.get(0)).autoRefresh();

                    }

                break;

        }
    }


}
