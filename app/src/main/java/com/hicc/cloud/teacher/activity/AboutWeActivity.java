package com.hicc.cloud.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hicc.cloud.R;

/**
 * Created by Administrator on 2016/11/4/004.
 * 关于   ——崔国钊
 */
public class AboutWeActivity extends AppCompatActivity {
    private Button bt_internet;
    private Button bt_software;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutwe);

        initUI();
    }

    private void initUI() {
        iv_back =(ImageView) findViewById(R.id.iv_back);
        bt_internet = (Button) findViewById(R.id.bt_internet);
        bt_software = (Button) findViewById(R.id.bt_software);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutWeActivity.this, InternetActivity.class);
                startActivity(intent);
            }
        });
        bt_software.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutWeActivity.this, SoftwareActivity.class);
                startActivity(intent);
            }
        });
    }
}
