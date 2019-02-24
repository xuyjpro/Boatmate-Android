package com.example.downtoearth.toget.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.downtoearth.toget.R;

import me.leefeng.promptlibrary.PromptDialog;

public class MadeItemActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 1000;
    public static final int REQUEST_SUCCESS = 1001;
    public static final int REQUEST_FAILED = 1002;

    private TextView tv_cancel;
    private TextView tv_commit;
    private TextView tv_title;
    private EditText et_content;

    public static TextView tv_static = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_item);

        tv_cancel = findViewById(R.id.tv_cancel);
        tv_commit = findViewById(R.id.tv_commit);
        tv_title = findViewById(R.id.tv_title);
        et_content = findViewById(R.id.et_content);
        tv_cancel.setOnClickListener(this);
        tv_commit.setOnClickListener(this);

        tv_title.setText(getIntent().getStringExtra("title"));
        et_content.setText(getIntent().getStringExtra("content"));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                tv_static = null;
                finish();
                break;
            case R.id.tv_commit:
                String content = et_content.getText().toString();
                if (content != null && !content.isEmpty()) {
//                    Intent intent=new Intent();
//                    intent.putExtra("content",content);
//                    setResult(REQUEST_SUCCESS,intent);
                    tv_static.setText(content);
                    tv_static = null;
                    finish();

                } else {
                    PromptDialog promptDialog = new PromptDialog(this);
                    promptDialog.showError("内容不能为空");
                }
                break;

        }
    }

    public static void startAction(Context context, String title, String content) {
        Intent intent = new Intent(context, MadeItemActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);

        context.startActivity(intent);

    }
}
