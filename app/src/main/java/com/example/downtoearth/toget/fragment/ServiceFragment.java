package com.example.downtoearth.toget.fragment;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.adapter.SchoolHelpAdapter;
import com.example.downtoearth.toget.impl.OnItemClickListener;
import com.example.downtoearth.toget.utils.GlideImageLoader;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

public class ServiceFragment extends BaseFragment implements View.OnClickListener {

    private Banner banner;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    private List<String> images;
    private List<String> titles;
    private RecyclerView rv_helps;
    private SchoolHelpAdapter helpAdapter;
    @Override
    public View initView() {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_service,null);

        banner=view.findViewById(R.id.banner);
        rv_helps=view.findViewById(R.id.rv_helps);
        tv1=view.findViewById(R.id.tv1);
        tv2=view.findViewById(R.id.tv2);
        tv3=view.findViewById(R.id.tv3);
        tv4=view.findViewById(R.id.tv4);

        ViewGroup layout_tvs=view.findViewById(R.id.layout_tvs);
        for(int i=0;i<layout_tvs.getChildCount();i++){
            TextView tv= (TextView) layout_tvs.getChildAt(i);
            Drawable[] drawables=tv.getCompoundDrawables();
            drawables[1].setBounds(0,0,ToolUtils.dip2px(54),ToolUtils.dip2px(54));
            tv.setCompoundDrawables(null,drawables[1],null,null);
        }
        return view;

    }

    @Override
    public void initData() {
        images=new ArrayList<>();
        images.add(HttpUtils.DOWNLOAD_URL+"banner_image1.jpg");
        images.add(HttpUtils.DOWNLOAD_URL+"banner_image2.jpeg");
        images.add(HttpUtils.DOWNLOAD_URL+"banner_image3.jpg");

        titles=new ArrayList<>();
        titles.add("代码使我很快乐");
        titles.add("我快勒马了皮");
        titles.add("爱丽丝是我好闺女");
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        banner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        banner.setImages(images);
        banner.setDelayTime(1500);

        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();

        //
        rv_helps.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_helps.setAdapter(helpAdapter=new SchoolHelpAdapter());
        rv_helps.setNestedScrollingEnabled(false);
        helpAdapter.setOnItemListener(new OnItemClickListener() {
            @Override
            public void onItemClick() {
                showToast("暂未开发");
            }
        });
        initListener();
    }

    public void initListener(){
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);

    }
    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            default:
                showToast("暂未开发");

        }
    }
}
