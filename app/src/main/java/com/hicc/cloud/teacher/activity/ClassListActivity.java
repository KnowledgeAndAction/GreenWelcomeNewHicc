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
import com.hicc.cloud.teacher.bean.TeacherClassInfo;
import com.hicc.cloud.teacher.utils.ConstantValue;
import com.hicc.cloud.teacher.utils.Logs;
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

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/25/025.
 */

public class ClassListActivity extends AppCompatActivity {
    private ImageView iv_back;
    private List<TeacherClassInfo> classInfoList = new ArrayList<>();;
    private ListView lv_class;
    private ProgressDialog progressDialog;
    private int type;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classlist);
        type = getIntent().getIntExtra("type",0);

        initUI();

        switch (type) {
            case ConstantValue.PAY_STATISTICS:
                initTemData();
                break;
            default:
                initData();
                break;
        }
    }

    // 临时数据
    private void initTemData() {
        showProgressDialog();

        OkHttpUtils
                .get()
                .url("http://home.hicc.cn/PhoneInterface/LoginService.asmx/ClassInfoByuserNo")
                .addParams("userNo", SpUtils.getIntSp(this, ConstantValue.USER_NO,0)+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeProgressDialog();
                        Logs.i(e.toString());
                        ToastUtli.show(getApplicationContext(),"服务器繁忙，请重新选择");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i(response);
                        // 解析json
                        Logs.i("解析json");
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject info = jsonArray.getJSONObject(i);

                                TeacherClassInfo teacherClassInfo = new TeacherClassInfo();
                                teacherClassInfo.setClassDescription(info.getString("ClassDescription"));
                                teacherClassInfo.setDivisionCode(info.getInt("DivisionCode"));
                                teacherClassInfo.setDivisionDescription(info.getString("DivisionDescription"));
                                teacherClassInfo.setGradeCode(info.getInt("TimeCode"));
                                teacherClassInfo.setNid(Integer.valueOf(info.getString("ClassCode")));
                                teacherClassInfo.setProfessionalDescription(info.getString("ProfessionalDescription"));
                                teacherClassInfo.setProfessionalId(info.getInt("ProfessionalCode"));

                                classInfoList.add(teacherClassInfo);
                            }
                            myAdapter.notifyDataSetChanged();
                            closeProgressDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            closeProgressDialog();
                            ToastUtli.show(getApplicationContext(),"选择失败");
                        }
                    }
                });
    }

    private void initData() {
        showProgressDialog();

        OkHttpUtils
                .get()
                .url(URLs.GetClassByUserNo)
                .addParams("teachercode", SpUtils.getIntSp(this, ConstantValue.USER_NO,0)+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeProgressDialog();
                        Logs.i(e.toString());
                        ToastUtli.show(getApplicationContext(),"服务器繁忙，请重新选择");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i(response);
                        // 解析json
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
            if(sucessed) {
                Logs.i("开始解析");
                // 带班信息
                JSONArray classInfo = jsonObject.getJSONArray("data");

                TeacherClassInfo teacherClassInfo;
                for (int i=0; i < classInfo.length(); i++){
                    JSONObject info = classInfo.getJSONObject(i);

                    teacherClassInfo = new TeacherClassInfo();
                    teacherClassInfo.setClassDescription(info.getString("ClassDescription"));
                    teacherClassInfo.setDivisionCode(info.getInt("DivisionCode"));
                    teacherClassInfo.setDivisionDescription(info.getString("DivisionDescription"));
                    teacherClassInfo.setUserNo(info.getString("UserNo"));
                    teacherClassInfo.setGradeCode(info.getInt("GradeCode"));
                    teacherClassInfo.setNid(info.getInt("Nid"));
                    teacherClassInfo.setProfessionalDescription(info.getString("ProfessionalDescription"));
                    teacherClassInfo.setProfessionalId(info.getInt("ProfessionalId"));
                    teacherClassInfo.setClassQQGroup(info.getString("ClassQQGroup"));

                    classInfoList.add(teacherClassInfo);
                }

                myAdapter.notifyDataSetChanged();
                closeProgressDialog();
            }else{
                closeProgressDialog();
                ToastUtli.show(getApplicationContext(),"选择失败，请重新选择");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            closeProgressDialog();
            ToastUtli.show(getApplicationContext(),"选择失败");
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

        lv_class = (ListView) findViewById(R.id.lv_class);
        lv_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type) {
                    // 交费
                    case ConstantValue.PAY_STATISTICS:
                        Intent intentPay = new Intent(ClassListActivity.this,PayStatisticsActivity.class);
                        intentPay.putExtra("num",classInfoList.get(position).getNid()+"");
                        intentPay.putExtra("timeCode",classInfoList.get(position).getGradeCode()+"");
                        startActivity(intentPay);
                        break;
                    // 宿舍成绩
                    case ConstantValue.DORMITORY_SCORE:
                        Intent intentDor = new Intent(ClassListActivity.this,DormitoryListActivity.class);
                        intentDor.putExtra("ClassCode",classInfoList.get(position).getNid()+"");
                        startActivity(intentDor);
                        break;
                    // 档案，成绩
                    default:
                        Intent intent = new Intent(getApplicationContext(),StudentListActivity.class);
                        intent.putExtra("classcode",classInfoList.get(position).getNid());
                        intent.putExtra("timescode",classInfoList.get(position).getGradeCode());
                        intent.putExtra("divisionCode",classInfoList.get(position).getDivisionCode());
                        intent.putExtra("professionalCode",classInfoList.get(position).getProfessionalId());
                        String title = classInfoList.get(position).getGradeCode()+"级"+classInfoList.get(position).getClassDescription();
                        intent.putExtra("title",title);
                        intent.putExtra("type",type);
                        startActivity(intent);
                        break;
                }
            }
        });

        myAdapter = new MyAdapter();
        lv_class.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return classInfoList.size();
        }

        @Override
        public TeacherClassInfo getItem(int position) {
            return classInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoulder viewHoulder;
            if(convertView == null){
                convertView = View.inflate(getApplicationContext(),R.layout.item_class,null);
                viewHoulder = new ViewHoulder();
                viewHoulder.tv_classdes = (TextView) convertView.findViewById(R.id.tv_classdes);
                convertView.setTag(viewHoulder);
            }
            viewHoulder = (ViewHoulder) convertView.getTag();
            viewHoulder.tv_classdes.setText(getItem(position).getDivisionDescription()+getItem(position).getGradeCode()+"级"+getItem(position).getClassDescription());

            return convertView;
        }
    }

    static class ViewHoulder {
        TextView tv_classdes;
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