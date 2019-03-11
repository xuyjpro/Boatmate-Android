package com.example.downtoearth.toget.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.adapter.ViewPagerAdapter;
import com.example.downtoearth.toget.fragment.BaseFragment;
import com.example.downtoearth.toget.fragment.DynamicFragment;
import com.example.downtoearth.toget.fragment.SchoolHelpFragment;
import com.example.downtoearth.toget.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

public class SchoolHelpActivity extends BaseActivity {

    private ViewPager viewPager;
    private FloatingActionButton mFloatBtn;
    private RadioGroup radioGroup;
    private List<BaseFragment> fragments;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        initView();
        initData();

    }
    public void initView(){
        viewPager=findViewById(R.id.view_pager);
        mFloatBtn=findViewById(R.id.btn_floating);

        mFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        radioGroup=findViewById(R.id.radio_group);

        for(int i=0;i<radioGroup.getChildCount();i++){
            RadioButton rb= (RadioButton) radioGroup.getChildAt(i);
            Drawable drawable = getResources().getDrawable(
                    R.drawable.home_top_rb);
            drawable.setBounds(0, 0, ToolUtils.dip2px(16),
                    ToolUtils.dip2px(4));
            rb.setCompoundDrawables(null, null, null, drawable);
            if(i==0){
                rb.setText("所有帮帮");
            }else{
                rb.setText("我发布的");
            }
        }
    }
    public void initData(){
        fragments=new ArrayList<>();



        fragments.add(SchoolHelpFragment.newInstance(0));
        fragments.add(SchoolHelpFragment.newInstance(1));


        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),fragments));

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
}
