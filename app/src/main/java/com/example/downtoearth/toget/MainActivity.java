package com.example.downtoearth.toget;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.downtoearth.toget.activity.BaseActivity;
import com.example.downtoearth.toget.activity.EditInfoActivity;
import com.example.downtoearth.toget.activity.LoginActivity;
import com.example.downtoearth.toget.adapter.ViewPagerAdapter;
import com.example.downtoearth.toget.bean.UserInfo;
import com.example.downtoearth.toget.fragment.BaseFragment;
import com.example.downtoearth.toget.fragment.HomeFragment;
import com.example.downtoearth.toget.fragment.NewsFrament;
import com.example.downtoearth.toget.utils.ActivityCollector;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;

    private List<BaseFragment> mFragmentList;
    private CircleImageView mHeadPic;
    private TextView mUserName;
    private TextView tvHeartWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.finishOther(this);

        mDrawerLayout=findViewById(R.id.drawerlayout);
        NavigationView navView=findViewById(R.id.nav_view);
        ActionBar actionBar=getSupportActionBar();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.login_out:
                        ToolUtils.putString("token","");
                        Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.user_info:
                        Intent intent1=new Intent(MainActivity.this,EditInfoActivity.class);

                        startActivityForResult(intent1,EditInfoActivity.EDIT_INFO);
                        break;
                }

                return true;
            }
        });


        mFragmentList=new ArrayList<>();
        mFragmentList.add(new HomeFragment());

        final ViewPager viewPager=findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),mFragmentList));
        viewPager.setCurrentItem(0);
        BottomNavigationView btnNavView=findViewById(R.id.bot_nav_view);

        btnNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.find:
                        viewPager.setCurrentItem(1);
                        break;
                  /*  case R.id.news:
                        viewPager.setCurrentItem(2);
                        break;*/
                }
                return true;
            }
        });

        View headView=navView.getHeaderView(0);
        mHeadPic=headView.findViewById(R.id.image_head);
        mUserName=headView.findViewById(R.id.username);
        tvHeartWord=headView.findViewById(R.id.heart_word);

        loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case EditInfoActivity.EDIT_INFO:
                getUserInfo();
                break;
        }
    }

    public void loadData(){
        String userInfo=getIntent().getStringExtra("user_info");
        if(userInfo!=null&&!userInfo.isEmpty()){//从登录/注册页面过来
            updateUI(userInfo);

        }else{
            getUserInfo();
        }
    }
    public void updateUI(String dataStr){
        UserInfo.DataBean.UserInfoBean uib=(new Gson()).fromJson(dataStr,UserInfo.DataBean.UserInfoBean.class);
        //UserInfo.DataBean.UserInfoBean uib=lb.getData().getUserInfo();
        if(uib.getHeadPic()!=null){
            Glide.with(this).load(HttpUtils.DOWNLOAD_URL+uib.getHeadPic()).into(mHeadPic);
        }
        if(uib.getNickname()!=null){
            mUserName.setText(uib.getNickname());
            tvHeartWord.setText(uib.getHeartWord());
        }
    }
    public void getUserInfo(){
        OkGo.post(HttpUtils.GET_USER_INFO)
                .tag(this)
                .isMultipart(true)
                .params("token",ToolUtils.getString("token"))
                .headers("Content-Type","application/json")
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e(TAG,s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                                updateUI(jsonObject.getString("data"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.backup:
                Toast.makeText(this,item.getTitle(),Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.delete:
                Toast.makeText(this,item.getTitle(),Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.setting:
                Toast.makeText(this,item.getTitle(),Toast.LENGTH_SHORT)
                        .show();
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }

        return true;
    }

}
