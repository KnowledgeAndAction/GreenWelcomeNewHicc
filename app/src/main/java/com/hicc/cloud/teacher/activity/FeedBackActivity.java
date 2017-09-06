package com.hicc.cloud.teacher.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.utils.ConstantValue;
import com.hicc.cloud.teacher.utils.Logs;
import com.hicc.cloud.teacher.utils.SpUtils;
import com.hicc.cloud.teacher.utils.ToastUtli;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/4/004.
 * 反馈   ——崔国钊
 */
public class FeedBackActivity extends AppCompatActivity {
    private static final String URL = "http://suguan.hicc.cn/feedback1/suggest.do";
    private ImageView iv_back;
    private Button bt_send;
    private EditText ed_sendtext;
    private RadioGroup rg_root;
    private String mTag = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initUI();
    }

    private void initUI() {
        ed_sendtext = (EditText) findViewById(R.id.sendtext);
        bt_send = (Button) findViewById(R.id.bt_send);
        rg_root = (RadioGroup) findViewById(R.id.rg_root);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rg_root.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_ui:
                        mTag = "1";
                        break;
                    case R.id.rb_function:
                        mTag = "2";
                        break;
                }
            }
        });

        // 发送按钮的点击事件
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String massage = ed_sendtext.getText().toString().trim();
                // 如果输入不为空
                if(!massage.equals("")){
                    OkHttpUtils
                            .get()
                            .url(URL)
                            .addParams("userId", SpUtils.getStringSp(getApplicationContext(), ConstantValue.USER_NAME, ""))
                            .addParams("appId", "1")
                            .addParams("appVersion", String.valueOf(getVersionCode()))
                            .addParams("tag", mTag)
                            .addParams("content", massage)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Logs.i("发送反馈信息失败："+e.toString());
                                    ToastUtli.show(getApplicationContext(), "服务器繁忙，请重新发送");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean falg = jsonObject.getBoolean("falg");
                                        if(falg){
                                            ToastUtli.show(getApplicationContext(), "发送成功");
                                        } else {
                                            Logs.i("服务器错误,发送失败："+jsonObject.getString("data"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Logs.i("解析反馈信息结果失败："+e.toString());
                                    }
                                }
                            });
                } else {
                    ToastUtli.show(getApplicationContext(),"请输入您的反馈信息");
                }
            }
        });
    }

    // 获取本应用版本号
    private int getVersionCode() {
        // 拿到包管理者
        PackageManager pm = getPackageManager();
        // 获取包的基本信息
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            // 返回应用的版本号
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
