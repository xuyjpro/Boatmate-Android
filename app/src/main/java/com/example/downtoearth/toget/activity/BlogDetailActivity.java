package com.example.downtoearth.toget.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.downtoearth.toget.R;

import me.leefeng.promptlibrary.PromptDialog;

public class BlogDetailActivity extends BaseActivity {


    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_blog_detail);

        webView = findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        PromptDialog promptDialog=new PromptDialog(this);
        promptDialog.showLoading("加载中");
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getStringExtra("url"));
        promptDialog.showSuccessDelay("加载成功",500);
    }

}
