package com.example.downtoearth.toget.activity;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
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
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.adapter.ViewPagerAdapter;
import com.example.downtoearth.toget.bean.AppVersion;
import com.example.downtoearth.toget.bean.UserInfo;
import com.example.downtoearth.toget.fragment.BaseFragment;
import com.example.downtoearth.toget.fragment.ChatFragment;
import com.example.downtoearth.toget.fragment.HomeFragment;
import com.example.downtoearth.toget.fragment.ServiceFragment;
import com.example.downtoearth.toget.utils.ActivityCollector;
import com.example.downtoearth.toget.utils.DownloadIntentService;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.PermissionHelper;
import com.example.downtoearth.toget.utils.PermissionInterface;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements PermissionInterface {

    private DrawerLayout mDrawerLayout;

    private List<BaseFragment> mFragmentList;
    private CircleImageView mHeadPic;
    private TextView mUserName;
    private TextView tvHeartWord;

    private PermissionHelper mPermissionHelper;
    private PromptDialog promptDialog;


    @Override
    public void onBackPressed() {
        PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton button) {
                finish();
            }
        });
        promptDialog.showWarnAlert("你确定要退出登录？", new PromptButton("取消", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton button) {
                promptDialog.dismiss();
            }
        }), confirm);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.finishOther(this);

        //初始化并发起权限申请
        mPermissionHelper = new PermissionHelper(this, this);
        mPermissionHelper.requestPermissions();

        promptDialog = new PromptDialog(this);
        mDrawerLayout = findViewById(R.id.drawerlayout);
        NavigationView navView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.login_out:
                        ToolUtils.putString("token", "");
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.user_info:
                        Intent intent1 = new Intent(MainActivity.this, EditInfoActivity.class);

                        startActivityForResult(intent1, EditInfoActivity.EDIT_INFO);
                        break;
                }

                return true;
            }
        });


        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ServiceFragment());

        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new ChatFragment());

        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList));
        viewPager.setCurrentItem(0);
        BottomNavigationView btnNavView = findViewById(R.id.bot_nav_view);

        btnNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.find:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.news:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });

        View headView = navView.getHeaderView(0);
        mHeadPic = headView.findViewById(R.id.image_head);
        mUserName = headView.findViewById(R.id.username);
        tvHeartWord = headView.findViewById(R.id.heart_word);

        loadData();
        checkUpdate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EditInfoActivity.EDIT_INFO:
                getUserInfo();
                break;
        }
    }

    public void loadData() {
        String userInfo = getIntent().getStringExtra("user_info");
        if (userInfo != null && !userInfo.isEmpty()) {//从登录/注册页面过来
            updateUI(userInfo);

        } else {
            getUserInfo();
        }
    }

    public void updateUI(String dataStr) {
        UserInfo.DataBean.UserInfoBean uib = (new Gson()).fromJson(dataStr, UserInfo.DataBean.UserInfoBean.class);
        //UserInfo.DataBean.UserInfoBean uib=lb.getData().getUserInfo();
        if (uib.getHeadPic() != null) {
            Glide.with(this).load(HttpUtils.DOWNLOAD_URL + uib.getHeadPic()).into(mHeadPic);
        }
        if (uib.getNickname() != null) {
            mUserName.setText(uib.getNickname());
            tvHeartWord.setText(uib.getHeartWord());
        }
    }

    public void getUserInfo() {
        OkGo.post(HttpUtils.GET_USER_INFO)
                .tag(this)
                .isMultipart(true)
                .params("token", ToolUtils.getString("token"))
                .headers("Content-Type", "application/json")
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e(TAG, s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                updateUI(jsonObject.getString("data"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    public void checkUpdate() {
        OkGo.post(HttpUtils.GET_APP_VERSION)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        log(s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                parseVersionData(s);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void parseVersionData(String s) {
        AppVersion av = new Gson().fromJson(s, AppVersion.class);
        if (av.getData() != null) {
            if (av.getData().size() != 0) {

                final AppVersion.DataBean dataBean = av.getData().get(0);
                if (dataBean.getVersionCode() > ToolUtils.getVersionCode(this)) {

                    promptDialog.getDefaultBuilder().icon(R.mipmap.logo_app);

                    promptDialog.getAlertDefaultBuilder().icon(R.mipmap.logo_app);
                    PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
                        @Override
                        public void onClick(PromptButton button) {
                            Intent intent = new Intent(MainActivity.this, DownloadIntentService.class);
                            intent.putExtra(DownloadIntentService.INTENT_VERSION_NAME, dataBean.getVersionName());
                            intent.putExtra(DownloadIntentService.INTENT_DOWNLOAD_URL, HttpUtils.DOWNLOAD_URL + dataBean.getDownloadUrl());
                            startService(intent);
                        }
                    });

                    promptDialog.showWarnAlert("有更新，是否更新", new PromptButton("取消", new PromptButtonListener() {
                        @Override
                        public void onClick(PromptButton button) {
                            promptDialog.dismiss();
                        }
                    }), confirm);
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.delete:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.setting:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT)
                        .show();
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (mPermissionHelper.requestPermissionsResult(requestCode, permissions, grantResults)) {
            //权限请求结果，并已经处理了该回调
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public int getPermissionsRequestCode() {
        return 0;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
    }

    @Override
    public void requestPermissionsSuccess() {

    }

    @Override
    public void requestPermissionsFail() {

    }

}
