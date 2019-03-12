package com.example.downtoearth.toget.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.activity.SchoolHelpActivity;
import com.example.downtoearth.toget.activity.SchoolHelpDetailActivity;
import com.example.downtoearth.toget.activity.StuffLossActivity;
import com.example.downtoearth.toget.adapter.SchoolHelpAdapter;
import com.example.downtoearth.toget.bean.SchoolHelp;
import com.example.downtoearth.toget.impl.OnItemClickListener;
import com.example.downtoearth.toget.utils.GlideImageLoader;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ServiceFragment extends BaseFragment implements View.OnClickListener {

    private Banner banner;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private ViewGroup layout_help;
    private List<String> images;
    private List<String> titles;
    private List helpList;
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
        layout_help=view.findViewById(R.id.layout_help);

        ViewGroup layout_tvs=view.findViewById(R.id.layout_tvs);
        for(int i=0;i<layout_tvs.getChildCount();i++){
            TextView tv= (TextView) layout_tvs.getChildAt(i);
            Drawable[] drawables=tv.getCompoundDrawables();
            drawables[1].setBounds(0,0,ToolUtils.dip2px(54),ToolUtils.dip2px(54));
            tv.setCompoundDrawables(null,drawables[1],null,null);
        }


//        LinearLayout.LayoutParams rvParams= (LinearLayout.LayoutParams) rv_helps.getLayoutParams();
//        rvParams.leftMargin=ToolUtils.dip2px(10);
//        rvParams.rightMargin=ToolUtils.dip2px(10);
//        rvParams.topMargin=ToolUtils.dip2px(10);
//        rv_helps.setLayoutParams(rvParams);
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
        helpList=new ArrayList();
        rv_helps.setLayoutManager(new LinearLayoutManager(getContext()));
     //   rv_helps.setAdapter(helpAdapter=new SchoolHelpAdapter(helpList));
        rv_helps.setNestedScrollingEnabled(false);

        initListener();
        getHelpList();
    }

    public void getHelpList(){
        OkGo.post(HttpUtils.GET_SCHOOL_HELPS)
                .tag(this)
                .isMultipart(true)
                .params("currentPage",1)
                .params("pageSize",6)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        parseHelpList(s);

                    }
                });
    }
    public void parseHelpList(String s){

        SchoolHelp  sh=new Gson().fromJson(s,SchoolHelp.class);
        if(sh.getCode()==200){
            if(sh.getData()!=null&&sh.getData()
                    .size()!=0) {
                helpList=sh.getData();
                helpAdapter=new SchoolHelpAdapter(helpList);
                rv_helps.setAdapter(helpAdapter);
                helpAdapter.setOnItemListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        SchoolHelp.DataBean d=(SchoolHelp.DataBean)helpList.get(position);
                        Intent intent=new Intent(getContext(),SchoolHelpDetailActivity.class);
                        intent.putExtra("id",d.getId());
                        intent.putExtra("position",position);

                        startActivityForResult(intent,1000);
                    }
                });
                //helpAdapter.notifyDataSetChanged();
            }
        }
    }
    public void initListener(){
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);

        layout_help.setOnClickListener(this);
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
            case R.id.layout_help:
                Intent intent=new Intent(getContext(),SchoolHelpActivity.class);
                startActivityForResult(intent,1001);
                break;
            case R.id.tv2:
                Intent intent1=new Intent(getContext(),StuffLossActivity.class);
                startActivity(intent1);
                break;
            default:
                showToast("暂未开发");

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
//            if(data!=null){
//                int position=data.getIntExtra("position",0);
//                if(position!=0){
//
//                    helpList.remove(position);
//                    helpAdapter.notifyItemRemoved(position);
//                }
//            }
            getHelpList();
        }else{
            getHelpList();
        }
    }
}
