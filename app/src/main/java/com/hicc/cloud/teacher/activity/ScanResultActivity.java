package com.hicc.cloud.teacher.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hicc.cloud.R;
import com.hicc.cloud.teacher.utils.ConstantValue;
import com.hicc.cloud.teacher.utils.Logs;
import com.hicc.cloud.teacher.utils.SpUtils;
import com.hicc.cloud.teacher.utils.ToastUtli;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/15/015.
 */
public class ScanResultActivity extends AppCompatActivity {

    private ImageView iv_back;
    private ImageView iv_old_pic;
    private ImageView iv_new_pic;
    private TextView tv_num;
    private TextView tv_name;
    private TextView tv_faculty;
    private TextView tv_profession;
    private TextView tv_class;
    private TextView tv_dormitory;
    private TextView tv_pay_status;
    private TextView tv_reported_status;
    private Button bt_reported;
    private String result;
    private ProgressDialog progressDialog;
    private boolean changeReport = false;
    private boolean changePhone = false;
    private AlertDialog dialog;
    private LinearLayout ll_chenggong;
    private String oldImgUrl;
    private String newImgUrl;
    private int reportedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        reportedCount = SpUtils.getIntSp(this, ConstantValue.REPORTED_COUNT, 0);

        initUI();

        Intent intent = getIntent();
        result = intent.getStringExtra("result");
        Logs.i(result);

        initData();
    }

    private void initData() {
        showDialog();
        OkHttpUtils
                .get()
                .url("http://home.hicc.cn/PhoneInterface/OnlineRegister.asmx/getData")
                .addParams("studentNu", result)
                .addParams("userno", SpUtils.getIntSp(getApplicationContext(), ConstantValue.USER_NO, 0) + "")
                .addParams("levelcode", SpUtils.getIntSp(getApplicationContext(), ConstantValue.USER_LEVEL_CODE, 0) + "")
                .addParams("account", SpUtils.getStringSp(getApplicationContext(), ConstantValue.USER_NAME, ""))
                .addParams("pas", SpUtils.getStringSp(getApplicationContext(), ConstantValue.PASS_WORD, ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtli.show(getApplicationContext(), "获取学生信息失败，请稍后重试");
                        // 隐藏按钮
                        bt_reported.setVisibility(View.INVISIBLE);
                        closeDialog();
                        Logs.e("获取学生信息失败:" + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.d(response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONArray data = jsonArray.getJSONArray(1);
                            // 学号
                            String num = data.getString(0);
                            // 姓名
                            String name = data.getString(1);
                            // 学部
                            String faculty = data.getString(2);
                            // 专业
                            String profession = data.getString(3);
                            // 班级
                            String classs = data.getString(4);
                            //String classs = clas.substring(0,clas.indexOf("\\"));
                            // 新照片
                            String newI = data.getString(5);
                            String newImg = newI.substring(newI.indexOf("/") + 1);
                            // 旧照片
                            String old = data.getString(6);
                            String oldImg = old.substring(old.indexOf("/") + 1);
                            // 宿舍楼
                            String dormitoryBuild = data.getString(8);
                            // 宿舍号
                            String dormitoryCode = data.getString(9);
                            // 床号
                            String build = data.getString(10);
                            // 交费状态码
                            int payStatusCode = Integer.valueOf(data.getString(7));
                            // 报道状态码
                            int reportedStatus = Integer.valueOf(data.getString(11));
                            // 交费状态
                            String payStatus = data.getString(12);
                            // 报道状态
                            String reported = data.getString(13);
                            // 是否根据交费情况显示按钮
                            int available = jsonArray.getInt(2);

                            oldImgUrl = "http://home.hicc.cn/OldImage/" + oldImg;
                            newImgUrl = "http://home.hicc.cn/StudentImage/" + newImg;

                            Glide.with(ScanResultActivity.this).load(oldImgUrl).placeholder(R.drawable.icon_pic)
                                    .centerCrop()
                                    .error(R.drawable.icon_pic)
                                    .into(iv_old_pic);
                            Glide.with(ScanResultActivity.this).load(newImgUrl).placeholder(R.drawable.icon_pic)
                                    .centerCrop()
                                    .error(R.drawable.icon_pic)
                                    .into(iv_new_pic);
                            tv_num.setText("学号：" + num);
                            tv_name.setText("姓名：" + name);
                            tv_faculty.setText("学部：" + faculty);
                            tv_profession.setText("专业：" + profession);
                            tv_class.setText("班级：" + classs);
                            tv_dormitory.setText("宿舍信息：" + dormitoryBuild + " " + dormitoryCode + "号 " + build + "号床");
                            tv_pay_status.setText("交费状态：" + payStatus);
                            tv_reported_status.setText("报道状态：" + reported);

                            if (available == 0) {
                                switch (payStatusCode) {
                                    // 缓交
                                    case 10:
                                        bt_reported.setVisibility(View.VISIBLE);
                                        break;
                                    // 贷款
                                    case 11:
                                        bt_reported.setVisibility(View.VISIBLE);
                                        break;
                                    // 未缴费
                                    case 12:
                                        // 隐藏按钮
                                        bt_reported.setVisibility(View.INVISIBLE);
                                        break;
                                    // 已缴费
                                    case 13:
                                        bt_reported.setVisibility(View.VISIBLE);
                                        break;
                                    // 保留学籍
                                    case 14:
                                        bt_reported.setVisibility(View.VISIBLE);
                                        break;
                                    // 待处理
                                    case 15:
                                        // 隐藏按钮
                                        bt_reported.setVisibility(View.INVISIBLE);
                                        break;
                                }
                            } else {
                                // 显示按钮
                                bt_reported.setVisibility(View.VISIBLE);
                            }

                            switch (reportedStatus) {
                                case 1:
                                    break;
                                // 已报到
                                case 0:
                                    // 隐藏按钮
                                    bt_reported.setVisibility(View.GONE);
                                    ll_chenggong.setVisibility(View.VISIBLE);
                                    break;
                            }

                            closeDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            closeDialog();
                            ToastUtli.show(getApplicationContext(), "该学生不在本学部，请该学生到所在学部报到");
                            // 隐藏按钮
                            bt_reported.setVisibility(View.INVISIBLE);
                            Logs.e("解析数据失败:" + e.toString());
                        }
                    }
                });
    }

    private void initUI() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_old_pic = (ImageView) findViewById(R.id.iv_old_pic);
        iv_new_pic = (ImageView) findViewById(R.id.iv_new_pic);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_faculty = (TextView) findViewById(R.id.tv_faculty);
        tv_profession = (TextView) findViewById(R.id.tv_profession);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_dormitory = (TextView) findViewById(R.id.tv_dormitory);
        tv_pay_status = (TextView) findViewById(R.id.tv_pay_status);
        tv_reported_status = (TextView) findViewById(R.id.tv_reported_status);

        ll_chenggong = (LinearLayout) findViewById(R.id.ll_chenggong);

        bt_reported = (Button) findViewById(R.id.bt_reported);
        // 报道按钮点击事件
        bt_reported.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示输入手机号对话框
                showInputPhoneDialog();
            }
        });

        // 设置图片点击事件
        iv_old_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanResultActivity.this, ImageDetailsActivity.class);
                intent.putExtra("image_path", oldImgUrl);
                //android V4包的类,用于两个activity转场时的缩放效果实现
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        ScanResultActivity.this, iv_old_pic, "image");
                try {
                    ActivityCompat.startActivity(ScanResultActivity.this, intent, optionsCompat.toBundle());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    startActivity(intent);
                }
            }
        });
        iv_new_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanResultActivity.this, ImageDetailsActivity.class);
                intent.putExtra("image_path", newImgUrl);
                //android V4包的类,用于两个activity转场时的缩放效果实现
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        ScanResultActivity.this, iv_new_pic, "image");
                try {
                    ActivityCompat.startActivity(ScanResultActivity.this, intent, optionsCompat.toBundle());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    startActivity(intent);
                }
            }
        });
    }

    private void showInputPhoneDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(this, R.layout.dialog_input_phone, null);
        dialog.setView(view, 0, 0, 0, 0);

        final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        Button bt_confirm = (Button) view.findViewById(R.id.bt_confirm);

        // 取消按钮
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 确认按钮
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString().trim();

                if (!phone.equals("")) {
                    // 匹配正则 看是否是手机号
                    if (phone.matches("^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\\d{8}$")) {
                        // 修改手机号
                        changeNewPhone(phone);
                        dialog.dismiss();
                    } else {
                        ToastUtli.show(getApplicationContext(), "请检查手机号是否正确");
                    }
                } else {
                    ToastUtli.show(getApplicationContext(), "手机号不能为空");
                }
            }
        });

        dialog.show();
    }

    // 修改新手机号
    private void changeNewPhone(String phone) {
        showDialog();
        OkHttpUtils
                .get()
                .url("http://home.hicc.cn/PhoneInterface/OnlineRegister.asmx/queding")
                .addParams("studentNu", result)
                .addParams("newphone", phone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        changePhone = false;
                        closeDialog();
                        Logs.e("修改手机失败："+e.toString());
                        ToastUtli.show(getApplicationContext(), "报到失败，请稍后重试");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            changePhone = true;
                            // 修改报到
                            changeReportStatus();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            closeDialog();
                            changePhone = false;
                            ToastUtli.show(getApplicationContext(), "报到失败，请稍后重试");
                            Logs.e("解析失败："+e.toString());
                        }
                    }
                });
    }

    // 修改报道状态
    private void changeReportStatus() {
        showDialog();
        OkHttpUtils
                .get()
                .url("http://home.hicc.cn/PhoneInterface/OnlineRegister.asmx/tijiao")
                .addParams("studentNu", result)
                .addParams("code", "0")
                .addParams("account", SpUtils.getStringSp(getApplicationContext(), ConstantValue.USER_NAME, ""))
                .addParams("pas", SpUtils.getStringSp(getApplicationContext(), ConstantValue.PASS_WORD, ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        changeReport = false;
                        closeDialog();
                        ToastUtli.show(getApplicationContext(), "报到失败，请稍后重试");
                        Logs.e("报到失败:" + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int anInt = jsonArray.getInt(0);
                            changeReport = true;
                            if (changeReport && changePhone) {
                                closeDialog();
                                tv_reported_status.setText("报到状态：已报到");
                                bt_reported.setVisibility(View.GONE);
                                ll_chenggong.setVisibility(View.VISIBLE);

                                reportedCount++;
                                SpUtils.putIntSp(ScanResultActivity.this,ConstantValue.REPORTED_COUNT,reportedCount);

                                ToastUtli.show(getApplicationContext(), "恭喜您报到成功，您是全院第"+anInt+"人");
                            } else {
                                Logs.e("报到失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            changeReport = false;
                            closeDialog();
                            ToastUtli.show(getApplicationContext(), "报到失败，请稍后重试");
                            Logs.e("解析异常:" + e.toString());
                        }
                    }
                });
    }

    private void showDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("加载中...");
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void closeDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
