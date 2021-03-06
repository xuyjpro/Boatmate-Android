package com.example.downtoearth.toget.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.adapter.ViewPagerAdapter;
import com.example.downtoearth.toget.fragment.BaseFragment;
import com.example.downtoearth.toget.fragment.CollegeMarketFragment;
import com.example.downtoearth.toget.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

public class CollegeMarketActivity extends BaseActivity {

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
                Intent intent=new Intent(CollegeMarketActivity.this,PublishStuffActivity.class);
                intent.putExtra("category",2);
                startActivityForResult(intent,1000);
            }
        });
        radioGroup=findViewById(R.id.radio_group);

        for(int i=0;i<radioGroup.getChildCount();i++){
            RadioButton rb= (RadioButton) radioGroup.getChildAt(i);
            Drawable drawable = getResources().getDrawable(
                    R.drawable.home_top_rb);
            // / 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, ToolUtils.dip2px(this,16),
                    ToolUtils.dip2px(this,4));
            rb.setCompoundDrawables(null, null, null, drawable);
            if(i==0){
                rb.setText("校园跳蚤");
            }else{
                rb.setText("我发布的");

            }
        }
    }
    public void initData() {
        fragments=new ArrayList<>();

        fragments.add(CollegeMarketFragment.newInstance(2,false));
        fragments.add(CollegeMarketFragment.newInstance(2,true));


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode==RESULT_OK){
                ((CollegeMarketFragment)fragments.get(0)).getNewData(true);
               ((CollegeMarketFragment)fragments.get(1)).getNewData(true);

            }
        }
    }
}
