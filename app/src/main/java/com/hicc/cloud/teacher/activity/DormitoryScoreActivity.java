package com.hicc.cloud.teacher.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.adapter.DormitoryScoreAdapter;
import com.hicc.cloud.teacher.bean.Dormitory;
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
 * Created by cs on 2016/10/11/011.
 * 宿舍成绩
 */
public class DormitoryScoreActivity extends AppCompatActivity {

    private ImageView iv_back;
    private String title;
    private String DormitoryBuildingCode;
    private String DormitoryNo;
    private LinearLayout ll_member;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private List<Dormitory> dormitoryList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();
    private DormitoryScoreAdapter adapter;
    private boolean memberYes = false;
    private boolean scoreYes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormitoryscore);

        title = getIntent().getStringExtra("title");
        DormitoryBuildingCode = getIntent().getStringExtra("DormitoryBuildingCode");
        DormitoryNo = getIntent().getStringExtra("DormitoryNo");

        initUI();

        initData();
    }

    private void initData() {
        showProgressDialog();
        // 获取宿舍成员
        OkHttpUtils
                .get()
                .url(URLs.GetDormitoryMember)
                .addParams("DormitoryBuildingCode",DormitoryBuildingCode)
                .addParams("DormitoryNo",DormitoryNo)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.e("获取宿舍成员失败"+e.toString());
                        closeProgressDialog();
                        ToastUtli.show(getApplicationContext(),"获取宿舍成员失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("sucessed")) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                for (int i=0; i<data.length(); i++) {
                                    JSONObject info = data.getJSONObject(i);
                                    // 创建view
                                    View view = LayoutInflater.from(DormitoryScoreActivity.this).inflate(R.layout.item_dormitory_member, ll_member, false);
                                    // 初始化控件
                                    TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                                    TextView tv_gender = (TextView) view.findViewById(R.id.tv_gender);
                                    TextView tv_bed = (TextView) view.findViewById(R.id.tv_bed);
                                    // 展示信息
                                    tv_name.setText(info.getString("StudentName"));
                                    tv_gender.setText(info.getString("Gender"));
                                    tv_bed.setText(info.getInt("BedNumber") + "号床铺");

                                    // 添加到父布局中
                                    ll_member.addView(view);
                                }
                                memberYes = true;
                                if (memberYes && scoreYes) {
                                    closeProgressDialog();
                                }
                            } else {
                                closeProgressDialog();
                                ToastUtli.show(getApplicationContext(),jsonObject.getString("Msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Logs.e("获取宿舍成员失败"+e.toString());
                            closeProgressDialog();
                            ToastUtli.show(getApplicationContext(),"获取宿舍成员失败");
                        }
                    }
                });

        // 获取宿舍成绩
        OkHttpUtils
                .get()
                .url(URLs.GetDormitoryScore)
                .addParams("DormitoryBuildingCode",DormitoryBuildingCode)
                .addParams("DormitoryNo",DormitoryNo)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.e("获取宿舍成绩失败"+e.toString());
                        closeProgressDialog();
                        ToastUtli.show(getApplicationContext(),"获取宿舍成绩失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("sucessed")) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                for (int i=0; i<data.length(); i++) {
                                    JSONObject info = data.getJSONObject(i);
                                    // 解析宿舍成绩
                                    int scoreTimeYear = info.getInt("ScoreTimeYear");
                                    int scoreTimeMonth = info.getInt("ScoreTimeMonth");
                                    int weekCode = info.getInt("WeekCode");
                                    String s = scoreTimeYear+"."+scoreTimeMonth+"."+weekCode;
                                    if (!nameList.contains(s)) {
                                        Dormitory dormitory = new Dormitory();
                                        dormitory.setScoreTimeYear(scoreTimeYear);
                                        dormitory.setScoreTimeMonth(scoreTimeMonth);
                                        dormitory.setWeekCode(weekCode);
                                        dormitory.setTotalScore(info.getInt("TotalScore"));
                                        dormitory.setCheckTypeDescription(info.getString("CheckTypeDescription"));

                                        dormitoryList.add(dormitory);
                                        nameList.add(s);
                                    }
                                }

                                adapter.notifyDataSetChanged();
                                scoreYes = true;
                                if (scoreYes && memberYes) {
                                    closeProgressDialog();
                                }
                            } else {
                                closeProgressDialog();
                                ToastUtli.show(getApplicationContext(),jsonObject.getString("Msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Logs.e("获取宿舍成绩失败"+e.toString());
                            closeProgressDialog();
                            ToastUtli.show(getApplicationContext(),"获取宿舍成绩失败");
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
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);

        ll_member = (LinearLayout) findViewById(R.id.ll_member);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 4;
            }
        });
        recyclerView.setLayoutManager(manager);

        adapter = new DormitoryScoreAdapter(dormitoryList);
        recyclerView.setAdapter(adapter);
    }

    // 显示进度对话框
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    // 关闭进度对话框
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
