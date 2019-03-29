package com.example.downtoearth.toget.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toolbar;

import com.example.downtoearth.toget.R;

public class ModifyPasswordActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);

        Toolbar toolbar=findViewById(R.id.toolbar);
//        Drawable drawable=toolbar.getNavigationIcon();
    }
}
