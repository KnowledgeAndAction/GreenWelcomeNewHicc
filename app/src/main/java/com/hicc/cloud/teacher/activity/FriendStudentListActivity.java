package com.hicc.cloud.teacher.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.bean.StartStudent;
import com.hicc.cloud.teacher.bean.Student;
import com.hicc.cloud.teacher.db.StudentInfoDB;
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
 * Created by Administrator on 2016/10/26/026.
 * 联系人
 */
public class FriendStudentListActivity extends AppCompatActivity {
    private List<Student> mStudentList = new ArrayList<>();
    private List<Student> mStartStudentList = new ArrayList<>();
    private List<String> mStartNameList = new ArrayList<>();
    private ImageView iv_back;
    private ProgressDialog progressDialog;
    private ListView lv_student;
    private PopupWindow mPopupWindow;
    private Student mStudent;
    private TextView tv_title;
    private long ENCRYPTION_CODE = 1024*1024*1024;
    private StudentInfoDB db;
    private MyAdapter mAdapter;
    private TextView tv_action_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendstudentlist);

        db = StudentInfoDB.getInstance(this);


        int timesCode = getIntent().getIntExtra("timescode", 0);
        int divisionCode = getIntent().getIntExtra("divisionCode", 0);
        int professionalCode = getIntent().getIntExtra("professionalCode", 0);
        int classCode = getIntent().getIntExtra("classcode", 0);
        String title = getIntent().getStringExtra("title");

        initUI();
        tv_action_title.setText(title);

        initData(timesCode, divisionCode, professionalCode, classCode);
    }

    private void initData(int timesCode, int divisionCode, int professionalCode, int classCode) {
        showProgressDialog();
        // 发送GET请求
        OkHttpUtils
                .get()
                .url(URLs.GetClassList)
                .addParams("pageno", "1")
                .addParams("pagesize", "300")
                .addParams("timescode", String.valueOf(timesCode))
                .addParams("divisionCode", String.valueOf(divisionCode))
                .addParams("professionalCode", String.valueOf(professionalCode))
                .addParams("classcode", String.valueOf(classCode))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeProgressDialog();
                        Logs.i(e.toString());
                        ToastUtli.show(getApplicationContext(), "服务器繁忙，请重新查询");
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

    private void getJsonInfo(final String response) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean sucessed = jsonObject.getBoolean("sucessed");
                    if (sucessed) {
                        Logs.i("开始解析");

                        JSONArray data = jsonObject.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            Student student = new Student();
                            JSONObject studentInfo = data.getJSONObject(i);
                            // 学生姓名
                            student.setStudentName(studentInfo.getString("StudentName"));
                            // 电话
                            student.setYourPhone(studentInfo.getString("YourPhone"));
                            student.setClassId(studentInfo.getInt("ClassId"));

                            mStudentList.add(student);
                        }

                        int classId = mStudentList.get(0).getClassId();

                        // 获取数据库中的星标学生
                        List<StartStudent> startStudents = db.queryStartStudents(classId);
                        for (StartStudent startStudent : startStudents) {
                            Student student = new Student();
                            student.setStudentName(startStudent.getName());
                            // 对数据库中的手机号解密
                            String phone = String.valueOf(startStudent.getPhone()^ENCRYPTION_CODE);
                            student.setYourPhone(phone);

                            mStartStudentList.add(student);
                        }

                        // 获取数据库中学生的姓名集合
                        mStartNameList = db.queryStartNames(classId);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Logs.i("大小是：" + mStudentList.size());
                                mAdapter = new MyAdapter();
                                lv_student.setAdapter(mAdapter);
                                closeProgressDialog();
                            }
                        });
                    }
                } catch (JSONException e) {
                    // 解析错误
                    e.printStackTrace();
                    ToastUtli.show(getApplicationContext(), "加载失败");
                    closeProgressDialog();
                }
            }
        }.start();
    }

    private void initUI() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_student = (ListView) findViewById(R.id.lv_student);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_action_title = (TextView) findViewById(R.id.tv_action_title);

        lv_student.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            // 监听滑动
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(mStartStudentList!=null && mStudentList!=null){
                    if(firstVisibleItem < mStartStudentList.size()+1){
                        tv_title.setText("常用联系人("+mStartStudentList.size()+")");
                    }else{
                        tv_title.setText("所有学生("+mStudentList.size()+")");
                    }
                }
            }
        });

        lv_student.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0 || position == mStartStudentList.size()+1){
                    // 标题
                    return;
                }else{
                    if(position < mStartStudentList.size()+1){
                        Logs.i("星标学生："+mStartStudentList.get(position-1).getYourPhone());
                        mStudent = mStartStudentList.get(position-1); // 减掉一个标题
                        showStartPopupWindow(view);
                    }else{
                        mStudent = mStudentList.get(position-mStartStudentList.size()-2); // 减掉用户应用的个数和两个标题
                        // 已加星标学生
                        if(mStartNameList.contains(mStudentList.get(position-mStartStudentList.size()-2).getStudentName())){
                            Logs.i("已加星标的学生："+mStudentList.get(position-mStartStudentList.size()-2).getYourPhone());
                            showStartPopupWindow(view);
                        // 未加星标学生
                        } else {
                            Logs.i("未加星标的学生："+mStudentList.get(position-mStartStudentList.size()-2).getYourPhone());
                            showUnStartPopupWindow(view);
                        }
                    }
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mStudentList.size() + mStartStudentList.size() + 2; // 加上两个标题条目
        }

        @Override
        public Student getItem(int position) {
            if (position == 0 || position == mStartStudentList.size() + 1) {
                // 标题
                return null;
            }
            if (position < mStartStudentList.size() + 1) {
                return mStartStudentList.get(position - 1); // 减掉一个标题
            } else {
                return mStudentList.get(position - mStartStudentList.size() - 2);  // 减掉用户应用的个数和两个标题
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // 条目类型的个数  默认1个
        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == mStartStudentList.size() + 1) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            switch (type) {
                // 为标题栏的时候
                case 0:
                    TitleHolder titleHolder = null;
                    if (convertView == null) {
                        convertView = View.inflate(getApplicationContext(), R.layout.student_title_item, null);

                        titleHolder = new TitleHolder();
                        titleHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                        convertView.setTag(titleHolder);
                    } else {
                        titleHolder = (TitleHolder) convertView.getTag();
                    }

                    if (position == 0) {
                        titleHolder.tv_title.setText("常用联系人(" + mStartStudentList.size() + ")");
                    } else {
                        titleHolder.tv_title.setText("所有学生(" + mStudentList.size() + ")");
                    }
                    break;
                // 为学生的时候
                case 1:
                    ViewHoulder viewHoulder;
                    if (convertView == null) {
                        convertView = View.inflate(getApplicationContext(), R.layout.item_class, null);
                        viewHoulder = new ViewHoulder();
                        viewHoulder.tv_classdes = (TextView) convertView.findViewById(R.id.tv_classdes);
                        convertView.setTag(viewHoulder);
                    }
                    viewHoulder = (ViewHoulder) convertView.getTag();
                    viewHoulder.tv_classdes.setText(getItem(position).getStudentName());
                    break;
            }

            return convertView;
        }
    }

    static class ViewHoulder {
        TextView tv_classdes;
    }

    static class TitleHolder {
        TextView tv_title;
    }

    // 显示进度对话框
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中...");
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
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    // 弹出加星标popupWindow
    protected void showUnStartPopupWindow(View view) {
        View contentView = View.inflate(this, R.layout.popupwindow_layout, null);

        LinearLayout ll_start = (LinearLayout) contentView.findViewById(R.id.ll_start);
        LinearLayout ll_dial = (LinearLayout) contentView.findViewById(R.id.ll_dial);
        // 设置点击事件
        ll_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在名字集合里添加数据
                mStartNameList.add(mStudent.getStudentName());
                // 在星标学生集合里添加数据
                mStartStudentList.add(mStudent);
                // 向数据库中添加数据
                StartStudent startStudent = new StartStudent();
                startStudent.setName(mStudent.getStudentName());
                // 将手机号加密存储
                startStudent.setPhone(Long.valueOf(mStudent.getYourPhone())^ENCRYPTION_CODE);
                startStudent.setClassId(mStudent.getClassId());
                db.insertStart(startStudent);
                // 刷新数据适配器
                mAdapter.notifyDataSetChanged();
                // 关闭PopupWindow
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                ToastUtli.show(getApplicationContext(),"添加星标成功");
            }
        });
        // 拨号
        ll_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mStudent.getYourPhone()));
                startActivity(intent);
                // 关闭PopupWindow
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });

        // 设置动画(透明加缩放)
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(400);
        alphaAnimation.setFillAfter(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(400);
        scaleAnimation.setFillAfter(true);
        //动画集合Set
        AnimationSet animationSet = new AnimationSet(true);
        //添加两个动画
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);

        // 创建窗体对象  指定宽和高
        mPopupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, view.getHeight(), true);
        // 设置窗体背景  这里设置为透明
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 指定窗体位置
        mPopupWindow.showAsDropDown(view, 150, -view.getHeight());

        // 开启动画集合
        contentView.startAnimation(animationSet);
    }

    // 弹出取消星标popupWindow
    protected void showStartPopupWindow(View view) {
        View contentView = View.inflate(this, R.layout.popupwindow_unstart_layout, null);

        LinearLayout ll_start = (LinearLayout) contentView.findViewById(R.id.ll_start);
        LinearLayout ll_dial = (LinearLayout) contentView.findViewById(R.id.ll_dial);
        // 设置点击事件
        ll_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在名字集合里删除数据
                mStartNameList.remove(mStudent.getStudentName());
                // 在星标学生集合里删除数据
                mStartStudentList.remove(mStudent);
                // 在数据库中删除数据
                db.deleteStart(Long.valueOf(mStudent.getYourPhone())^ENCRYPTION_CODE);
                // 刷新数据适配器
                mAdapter.notifyDataSetChanged();
                // 关闭PopupWindow
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                ToastUtli.show(getApplicationContext(),"取消星标成功");
            }
        });
        // 拨号
        ll_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mStudent.getYourPhone()));
                startActivity(intent);
                // 关闭PopupWindow
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });

        // 设置动画(透明加缩放)
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(400);
        alphaAnimation.setFillAfter(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(400);
        scaleAnimation.setFillAfter(true);
        //动画集合Set
        AnimationSet animationSet = new AnimationSet(true);
        //添加两个动画
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);

        // 创建窗体对象  指定宽和高
        mPopupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, view.getHeight(), true);
        // 设置窗体背景  这里设置为透明
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 指定窗体位置
        mPopupWindow.showAsDropDown(view, 150, -view.getHeight());

        // 开启动画集合
        contentView.startAnimation(animationSet);
    }
}
