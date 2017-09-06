package com.hicc.cloud.teacher.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.adapter.MarkHistoryAdapter;
import com.hicc.cloud.teacher.bean.Mark;
import com.hicc.cloud.teacher.utils.Logs;
import com.hicc.cloud.teacher.utils.ToastUtli;
import com.hicc.cloud.teacher.utils.URLs;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by 野 on 2016/10/13.
 * 学生成绩
 */

public class StudentMarkActivity extends AppCompatActivity {
    private ImageView iv_back;
    private TextView tv_name;
    private TextView tv_class;
    private TextView tv_stu_number;
    private ProgressDialog progressDialog;
    private List<Mark> markList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private String num;
    private MarkHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentmark);

        Intent intent = getIntent();
        String num = intent.getStringExtra("studentNu");

        initUI();

        // 获取学生成绩
        getStudentMark(num);
    }

    private void getStudentMark(String num) {
        // 显示进度对话框
        showProgressDialog();
        // 发送GET请求
        OkHttpUtils
                .get()
                .url(URLs.GetAllScore)
                .addParams("studentNum", num)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i(e.toString());
                        // 关闭进度对话框
                        closeProgressDialog();
                        ToastUtli.show(getApplicationContext(), "服务器繁忙，请重新查询");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i(response);
                        // 解析json
                        getJsonInfo(response);
                    }
                });
    }

    private void getJsonInfo(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean sucessed = jsonObject.getBoolean("sucessed");

            if (sucessed) {
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    Mark studentmark = new Mark();
                    JSONObject jsonObject1 = data.getJSONObject(i);
                    // 学生姓名
                    String stuName = jsonObject1.getString("StudentName");
                    tv_name.setText("姓名：" + stuName);
                    // 学号00
                    String stuNum = jsonObject1.getString("StudentNu");
                    tv_stu_number.setText("学号：" + stuNum);
                    // 班级
                    String classDes = jsonObject1.getString("BJH");
                    tv_class.setText("班级：" + classDes);

                    //课程名
                    String course = jsonObject1.getString("KCM");
                    studentmark.setCourse(course);
                    //任课老师
                    String teacher = jsonObject1.getString("SKJS");
                    studentmark.setTeacher(teacher);
                    //成绩
                    int mark = jsonObject1.getInt("KCCJ");
                    studentmark.setMark(mark);
                    markList.add(studentmark);
                }

                adapter.notifyDataSetChanged();

                // 解析完成  关闭对话框
                closeProgressDialog();

            } else {
                tv_name.setText("姓名：");
                tv_stu_number.setText("学号：");
                tv_class.setText("班级：");
                closeProgressDialog();
                ToastUtli.show(getApplicationContext(), "查询失败");
            }
        } catch (JSONException e) {
            closeProgressDialog();
            ToastUtli.show(getApplicationContext(), "查询失败");
            e.printStackTrace();
        }
    }

    // 显示进度对话框
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
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

    private void initUI() {
        // 返回
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_stu_number = (TextView) findViewById(R.id.tv_stu_number);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 4;
            }
        });
        mRecyclerView.setLayoutManager(manager);

        adapter = new MarkHistoryAdapter(markList);
        mRecyclerView.setAdapter(adapter);
    }
}