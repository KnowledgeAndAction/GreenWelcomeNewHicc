package com.hicc.cloud.teacher.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hicc.cloud.R;
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
 * Created by cs on 2017/08/23/025.
 * 宿舍列表
 */

public class DormitoryListActivity extends AppCompatActivity {
    private ImageView iv_back;
    private List<Dormitory> dormitoryInfoList = new ArrayList<>();
    private List<String> dormitoryNameList = new ArrayList<>();
    private ListView lv_dormitory;
    private ProgressDialog progressDialog;
    private MyAdapter myAdapter;
    private String classCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormitory_list);

        classCode = getIntent().getStringExtra("ClassCode");

        initUI();

        initData();
    }


    private void initData() {
        showProgressDialog();

        OkHttpUtils
                .get()
                .url(URLs.GetDormitory)
                .addParams("ClassCode", classCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeProgressDialog();
                        Logs.i(e.toString());
                        ToastUtli.show(getApplicationContext(), "服务器繁忙，请重新选择");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i(response);
                        // 解析宿舍信息json
                        Logs.i("解析json");
                        getJsonInfo(response);
                    }
                });
    }

    // 解析json数据
    private void getJsonInfo(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean sucessed = jsonObject.getBoolean("sucessed");
            if (sucessed) {
                // 宿舍信息
                JSONArray dormitoryInfo = jsonObject.getJSONArray("data");

                for (int i = 0; i < dormitoryInfo.length(); i++) {
                    JSONObject info = dormitoryInfo.getJSONObject(i);
                    String dormitoryBuildingDescription = info.getString("DormitoryBuildingDescription");
                    int dormitoryNo = info.getInt("DormitoryNo");
                    String dor = dormitoryBuildingDescription + dormitoryNo;
                    // 过滤相同的数据
                    if (!dormitoryNameList.contains(dor)) {
                        Dormitory dormitory = new Dormitory();
                        dormitory.setDormitoryBuildingDescription(dormitoryBuildingDescription);
                        dormitory.setDormitoryNo(dormitoryNo);
                        dormitory.setDormitoryBuildingCode(info.getInt("DormitoryBuildingCode"));
                        dormitoryInfoList.add(dormitory);
                        dormitoryNameList.add(dor);
                    }
                }

                myAdapter.notifyDataSetChanged();
                closeProgressDialog();
            } else {
                closeProgressDialog();
                ToastUtli.show(getApplicationContext(), "选择失败，请重新选择");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            closeProgressDialog();
            ToastUtli.show(getApplicationContext(), "选择失败");
        }

    }

    private void initUI() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_dormitory = (ListView) findViewById(R.id.lv_dormitory);
        lv_dormitory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DormitoryScoreActivity.class);
                intent.putExtra("DormitoryBuildingCode", dormitoryInfoList.get(position).getDormitoryBuildingCode()+"");
                intent.putExtra("DormitoryNo", dormitoryInfoList.get(position).getDormitoryNo()+"");
                String title = dormitoryInfoList.get(position).getDormitoryBuildingDescription() + dormitoryInfoList.get(position).getDormitoryNo() + "号";
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });

        myAdapter = new MyAdapter();
        lv_dormitory.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dormitoryInfoList.size();
        }

        @Override
        public Dormitory getItem(int position) {
            return dormitoryInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoulder viewHoulder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_class, null);
                viewHoulder = new ViewHoulder();
                viewHoulder.lv_dormitory = (TextView) convertView.findViewById(R.id.tv_classdes);
                convertView.setTag(viewHoulder);
            }
            viewHoulder = (ViewHoulder) convertView.getTag();
            viewHoulder.lv_dormitory.setText(getItem(position).getDormitoryBuildingDescription() + getItem(position).getDormitoryNo() + "号");

            return convertView;
        }
    }

    static class ViewHoulder {
        TextView lv_dormitory;
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