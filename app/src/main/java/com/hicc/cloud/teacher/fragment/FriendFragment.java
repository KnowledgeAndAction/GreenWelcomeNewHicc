package com.hicc.cloud.teacher.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.activity.FriendStudentListActivity;
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
 * Created by Administrator on 2016/9/24/024.
 */

public class FriendFragment extends BaseFragment {
    private List<TeacherClassInfo> classInfoList = new ArrayList<TeacherClassInfo>();
    private ListView listView;
    private LinearLayout ll_progress;
    private SwipeRefreshLayout sw_refresh; // 下拉刷新控件

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                // 成功
                case 0:
                    sw_refresh.setRefreshing(false);
                    MyAdapter adapter = new MyAdapter();
                    listView.setAdapter(adapter);
                    if(ll_progress.VISIBLE == View.VISIBLE){
                        ll_progress.setVisibility(View.GONE);
                    }
                    // 成功时设置不可下拉刷新
                    sw_refresh.setEnabled(false);
                    break;
                // 请求失败
                case 1:
                    sw_refresh.setEnabled(true);
                    sw_refresh.setRefreshing(false);
                    if(ll_progress.VISIBLE == View.VISIBLE){
                        ll_progress.setVisibility(View.GONE);
                    }
                    ToastUtli.show(getContext(), "加载失败，请重新加载");
                    break;
                // 解析错误
                case 2:
                    sw_refresh.setEnabled(true);
                    sw_refresh.setRefreshing(false);
                    if(ll_progress.VISIBLE == View.VISIBLE){
                        ll_progress.setVisibility(View.GONE);
                    }
                    ToastUtli.show(getContext(), "加载失败");
                    break;
            }

        }
    };


    @Override
    public void fetchData() {
        intiData();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_firend, container, false);

        listView = (ListView) view.findViewById(R.id.list_view);
        sw_refresh = (SwipeRefreshLayout) view.findViewById(R.id.sw_refresh);
        ll_progress = (LinearLayout) view.findViewById(R.id.ll_progress);
        ll_progress.setVisibility(View.VISIBLE);
        // 初始设置不可下拉刷新
        sw_refresh.setEnabled(false);
        sw_refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.holo_orange_light);

        // 设置条目的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),FriendStudentListActivity.class);
                intent.putExtra("classcode",classInfoList.get(position).getNid());
                intent.putExtra("timescode",classInfoList.get(position).getGradeCode());
                intent.putExtra("divisionCode",classInfoList.get(position).getDivisionCode());
                intent.putExtra("professionalCode",classInfoList.get(position).getProfessionalId());
                String title = classInfoList.get(position).getGradeCode()+"级"+classInfoList.get(position).getClassDescription();
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });

        //下拉加载
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                intiData();
            }
        });


        return view;
    }

    // 网络请求
    private void intiData() {
        OkHttpUtils
                .get()
                .url(URLs.GetClassByUserNo)
                .addParams("teachercode", SpUtils.getIntSp(getContext(), ConstantValue.USER_NO, 0)+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // 错误时可以下拉刷新
                        sw_refresh.setEnabled(true);
                        sw_refresh.setRefreshing(false);
                        if(ll_progress.VISIBLE == View.VISIBLE){
                            ll_progress.setVisibility(View.GONE);
                        }
                        Logs.i(e.toString());
                        ToastUtli.show(getContext(), "服务器繁忙，请重新加载");
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
    private void getJsonInfo(final String response) {
        new Thread() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean sucessed = jsonObject.getBoolean("sucessed");
                    if (sucessed) {
                        Logs.i("开始解析");
                        // 带班信息
                        JSONArray classInfo = jsonObject.getJSONArray("data");
                        TeacherClassInfo teacherClassInfo;
                        for (int i = 0; i < classInfo.length(); i++) {
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

                        mHandler.sendEmptyMessage(0);
                    } else {
                        mHandler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    mHandler.sendEmptyMessage(2);
                    e.printStackTrace();
                }
            }
        }.start();
    }


    // 显示对话框
    protected void showDialog(final String phone) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        //设置对话框标题
        builder.setTitle("打电话给朋友");
        //设置对话框内容
        builder.setMessage("是否跳转到拨号界面");
        //设置积极的按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });
        //设置消极的按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);

        builder.show();
    }

    class MyAdapter extends BaseAdapter {

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
                convertView = View.inflate(getContext(),R.layout.item_class,null);
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
}
