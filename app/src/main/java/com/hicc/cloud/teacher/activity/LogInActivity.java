package com.hicc.cloud.teacher.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.utils.ConstantValue;
import com.hicc.cloud.teacher.utils.Logs;
import com.hicc.cloud.teacher.utils.PhoneInfoUtil;
import com.hicc.cloud.teacher.utils.SpUtils;
import com.hicc.cloud.teacher.utils.ToastUtli;
import com.hicc.cloud.teacher.utils.URLs;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/17/017.
 * 登录页面
 */

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    private static Boolean isExit = false;
    private CheckBox cb_rember;
    private TextInputLayout et_username;
    private TextInputLayout et_pwd;
    private ProgressDialog progressDialog;
    private String userName;
    private String mPwd;
    private boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        // 初始化控件
        initUI();

        //检测是否记住了密码 如果是就填充
        checkUpDown();

        // 检测权限
        checkPermission();

        //检查是否已经登陆
        checkEnter();
    }

    // 检查权限
    private void checkPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    // 权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            ToastUtli.show(getApplicationContext(), "我们需要一些权限，来提供更好的服务");
                            return;
                        }
                    }
                } else {
                    ToastUtli.show(getApplicationContext(), "有个小错误");
                    finish();
                }
                break;
        }
    }

    // 点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 登录按钮
            case R.id.bt_login:
                // 登录
                isLogin = true;
                loginButton();
                break;
            // 注册按钮
            case R.id.tv_register:
                ToastUtli.show(getApplicationContext(), "正在开发中");
                break;
            // 找回密码按钮
            case R.id.tv_find_pwd:
                ToastUtli.show(getApplicationContext(), "正在开发中");
                break;
        }
    }

    // 登录
    private void loginButton() {
        userName = et_username.getEditText().getText().toString().trim();
        mPwd = et_pwd.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            et_username.setError("用户名为空");
            isLogin = false;
        }
        if (TextUtils.isEmpty(mPwd)) {
            et_pwd.setError("密码为空");
            isLogin = false;
        }

        if (isLogin){
            // 检查是否登录成功
            checkLoginForService(userName, mPwd);
        }

        // 给输入框设置文字改变监听事件，如果输入框文字改变，并且不为空，就将错误信息置空
        et_username.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    et_username.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // 给输入框设置文字改变监听事件，如果输入框文字改变，并且不为空，就将错误信息置空
        et_pwd.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    et_pwd.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void checkLoginForService(String userName, String mPwd) {
        // 接口不能用时的假数据
        if (userName.equals("学院") && mPwd.equals("学院")) {
            checkUp(userName, mPwd);
            SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_NAME, "模拟领导");
            SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_LEVEL, "测试人员");
            SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_PHONE, "1024");
            SpUtils.putIntSp(getApplicationContext(), ConstantValue.USER_NO, 1001);
            SpUtils.putIntSp(getApplicationContext(), ConstantValue.RECORD_CODE, 1);
            SpUtils.putIntSp(getApplicationContext(), ConstantValue.NID, 1);
            SpUtils.putIntSp(getApplicationContext(), ConstantValue.USER_LEVEL_CODE, 11);
            enterHome();
        } else if (userName.equals("导员") && mPwd.equals("导员")) {
            checkUp(userName, mPwd);
            SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_NAME, "模拟导员");
            SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_LEVEL, "测试人员");
            SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_PHONE, "1024");
            SpUtils.putIntSp(getApplicationContext(), ConstantValue.USER_NO, 3143);
            SpUtils.putIntSp(getApplicationContext(), ConstantValue.RECORD_CODE, 1);
            SpUtils.putIntSp(getApplicationContext(), ConstantValue.NID, 1);
            SpUtils.putIntSp(getApplicationContext(), ConstantValue.USER_LEVEL_CODE, 13);
            enterHome();
        } else if (userName.equals("学部") && mPwd.equals("学部")) {
            checkUp(userName, mPwd);
            SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_NAME, "模拟学部");
            SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_LEVEL, "测试人员");
            SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_PHONE, "1024");
            SpUtils.putIntSp(getApplicationContext(), ConstantValue.USER_NO, 2001);
            SpUtils.putIntSp(getApplicationContext(), ConstantValue.RECORD_CODE, 1);
            SpUtils.putIntSp(getApplicationContext(), ConstantValue.NID, 1);
            SpUtils.putIntSp(getApplicationContext(), ConstantValue.USER_LEVEL_CODE, 12);
            enterHome();
        } else {
            // 显示进度对话框
            showProgressDialog();
            // 对密码加密
            //mPwd = MD5Util.str2md5(mPwd);
            // 向服务器请求数据 发送GET请求
            getFromServer(userName, mPwd);
        }
    }

    // 向服务器请求数据
    private void getFromServer(final String userName, final String mPwd) {
        OkHttpUtils
                .get()
                .url(URLs.Login)
                .addParams("account", userName)
                .addParams("pas", mPwd)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeProgressDialog();
                        Logs.i(e.toString());
                        ToastUtli.show(getApplicationContext(), "服务器繁忙，请重新登录");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i(response);
                        // 解析json
                        Logs.i("解析json");
                        //getJsonInfo(response);
                        // TODO 临时登录
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            checkUp(userName, mPwd);
                            int userNo = jsonObject.getInt("UserNo");
                            int userLevelCode = jsonObject.getInt("UserLevelCode");
                            String userName1 = jsonObject.getString("UserName");
                            String contactWay = jsonObject.getString("ContactWay");
                            String userLevel = jsonObject.getString("UserLevel");

                            // 姓名
                            SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_NAME,userName1);
                            // 职位
                            SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_LEVEL, userLevel);
                            // 联系方式
                            SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_PHONE,contactWay);

                            SpUtils.putIntSp(getApplicationContext(), ConstantValue.USER_NO, userNo);
                            SpUtils.putIntSp(getApplicationContext(), ConstantValue.USER_LEVEL_CODE, userLevelCode);

                            // 关闭对话框进入主界面
                            ToastUtli.show(getApplicationContext(), "登录成功");
                            closeProgressDialog();
                            enterHome();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            closeProgressDialog();
                            ToastUtli.show(getApplicationContext(), "账号或密码错误");
                        }
                    }
                });
    }

    // 解析json数据
    private void getJsonInfo(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean sucessed = jsonObject.getBoolean("sucessed");
            if (sucessed) {
                Logs.i("登录成功，开始解析");
                // 登录成功后检查是否记住密码
                checkUp(userName, mPwd);
                Logs.i("账号：" + userName + "  密码：" + mPwd);

                // 获取用户信息
                getUserInfo();
            } else {
                closeProgressDialog();
                ToastUtli.show(getApplicationContext(), "账号或密码错误");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            closeProgressDialog();
            ToastUtli.show(getApplicationContext(), "登录失败");
        }
    }

    // 获取用户信息
    private void getUserInfo() {
        OkHttpUtils
                .get()
                .url(URLs.GetUserInfo)
                .addParams("Account", SpUtils.getStringSp(this, ConstantValue.USER_NAME, ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.e("获取用户信息失败:" + e.toString());
                        // 关闭对话框进入主界面
                        ToastUtli.show(getApplicationContext(), "登录成功");
                        closeProgressDialog();
                        enterHome();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("sucessed")) {
                                // 保存用户信息
                                JSONObject data = jsonObject.getJSONObject("data");
                                // 姓名
                                SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_NAME, data.getString("UserName"));
                                // 职位
                                SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_LEVEL, data.getString("UserLevel"));
                                // 联系方式
                                SpUtils.putStringSp(getApplicationContext(), ConstantValue.TEACHER_PHONE, data.getString("ContactWay"));

                                SpUtils.putIntSp(getApplicationContext(), ConstantValue.USER_NO, data.getInt("UserNo"));
                                SpUtils.putIntSp(getApplicationContext(), ConstantValue.RECORD_CODE, data.getInt("RecordCode"));
                                SpUtils.putIntSp(getApplicationContext(), ConstantValue.NID, data.getInt("Nid"));
                                SpUtils.putIntSp(getApplicationContext(), ConstantValue.USER_LEVEL_CODE, data.getInt("UserLevelCode"));

                                // 关闭对话框进入主界面
                                ToastUtli.show(getApplicationContext(), "登录成功");
                                closeProgressDialog();
                                enterHome();
                            } else {
                                Logs.e("获取用户信息失败:" + jsonObject.getString("Msg"));
                                // 关闭对话框进入主界面
                                ToastUtli.show(getApplicationContext(), "登录成功");
                                closeProgressDialog();
                                enterHome();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Logs.e("解析用户信息失败:" + e.toString());
                            // 关闭对话框进入主界面
                            ToastUtli.show(getApplicationContext(), "登录成功");
                            closeProgressDialog();
                            enterHome();
                        }
                    }
                });
    }

    //检查是否已经登陆
    private void checkEnter() {
        if (SpUtils.getBoolSp(this, ConstantValue.IS_REMBER_PWD, false)) {
            // 向服务器发送请求登录  获取用户数据
            enterHome();
        }
    }

    //检测是否记住了密码 如果是就填充
    private void checkUpDown() {
        if (SpUtils.getBoolSp(this, ConstantValue.IS_REMBER_PWD, false)) {
            et_username.getEditText().setText(SpUtils.getStringSp(this, ConstantValue.USER_NAME, ""));
            et_pwd.getEditText().setText(SpUtils.getStringSp(this, ConstantValue.PASS_WORD, ""));
            cb_rember.setChecked(SpUtils.getBoolSp(this, ConstantValue.IS_REMBER_PWD, false));
        } else {
            et_username.getEditText().setText(SpUtils.getStringSp(this, ConstantValue.USER_NAME, ""));
        }
    }

    //检查是否勾选记住密码
    private void checkUp(String userName, String mPwd) {
        if (cb_rember.isChecked()) {
            SpUtils.putStringSp(this, ConstantValue.USER_NAME, userName);
            SpUtils.putStringSp(this, ConstantValue.PASS_WORD, mPwd);
            SpUtils.putBoolSp(this, ConstantValue.IS_REMBER_PWD, true);
        } else {
            SpUtils.putStringSp(this, ConstantValue.USER_NAME, userName);
            SpUtils.putStringSp(this, ConstantValue.PASS_WORD, mPwd);
            SpUtils.putBoolSp(this, ConstantValue.IS_REMBER_PWD, true);
        }
    }

    //进入主界面
    private void enterHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // 初始化控件
    private void initUI() {
        et_username = (TextInputLayout) findViewById(R.id.et_username);
        et_pwd = (TextInputLayout) findViewById(R.id.et_pwd);
        cb_rember = (CheckBox) findViewById(R.id.cb_rember);
        Button bt_login = (Button) findViewById(R.id.bt_login);
        TextView tv_register = (TextView) findViewById(R.id.tv_register);
        TextView tv_find_pwd = (TextView) findViewById(R.id.tv_find_pwd);
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("— 云上工商 v" + PhoneInfoUtil.getPackageInfo(this).versionName);

        // 设置点击事件
        bt_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_find_pwd.setOnClickListener(this);
    }

    //监听返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    //双击退出程序
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
            System.exit(0);
        }
    }

    // 显示进度对话框
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("登陆中...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    return;
                }
            });
        }
        progressDialog.show();
    }

    // 关闭进度对话框
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
