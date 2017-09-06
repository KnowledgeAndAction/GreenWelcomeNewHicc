package com.hicc.cloud.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.bean.ExitEvent;
import com.hicc.cloud.teacher.utils.ConstantValue;
import com.hicc.cloud.teacher.utils.SpUtils;
import com.hicc.cloud.teacher.utils.ToastUtli;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/11/4/004.
 * 设置   ——崔国钊
 */
public class SettingActivity extends AppCompatActivity {

    private ImageView iv_back;
    private RelativeLayout rl_massage;
    private RelativeLayout rl_password;
    private Button btEsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUI();
    }

    private void initUI() {
        rl_massage= (RelativeLayout) findViewById(R.id.rl_massage);
        rl_password= (RelativeLayout) findViewById(R.id.rl_password);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        // 注销按钮
        btEsc = (Button) findViewById(R.id.esc);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_massage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtli.show(getApplicationContext(),"清理缓存成功");
            }
        });
        rl_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtli.show(getApplicationContext(),"努力开发中");
            }
        });
        btEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ExitEvent());
                startActivity(new Intent(SettingActivity.this, LogInActivity.class));
                SpUtils.remove(SettingActivity.this, ConstantValue.USER_NAME);
                SpUtils.remove(SettingActivity.this,ConstantValue.PASS_WORD);
                SpUtils.remove(SettingActivity.this,ConstantValue.TEACHER_NAME);
                SpUtils.remove(SettingActivity.this,ConstantValue.TEACHER_LEVEL);
                SpUtils.remove(SettingActivity.this,ConstantValue.TEACHER_PHONE);
                SpUtils.putBoolSp(SettingActivity.this,ConstantValue.IS_REMBER_PWD,false);
                finish();
            }
        });

    }
}
