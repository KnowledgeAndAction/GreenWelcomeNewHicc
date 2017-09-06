package com.hicc.cloud.teacher.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hicc.cloud.R;
import com.hicc.cloud.teacher.bean.Family;
import com.hicc.cloud.teacher.bean.Student;
import com.hicc.cloud.teacher.fragment.DetailedInfoFragment;
import com.hicc.cloud.teacher.fragment.FamilyInfoFragment;
import com.hicc.cloud.teacher.fragment.RewardPunishmentInfoFragment;
import com.hicc.cloud.teacher.utils.Logs;
import com.hicc.cloud.teacher.utils.ToastUtli;
import com.hicc.cloud.teacher.utils.URLs;
import com.hicc.cloud.teacher.view.ScrollViewPager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/11/011.
 * 学生档案
 */
public class StudentProfileActivity extends AppCompatActivity {
    private ImageView iv_back;
    private TextView tv_name;
    private TextView tv_class;
    private TextView tv_sex;
    private TextView tv_stu_num;
    private ImageView iv_pic;
    private String URL = "http://suguan.hicc.cn/hicccloudt/getStudentInfo";
    private ProgressDialog progressDialog;
    private String stuName;
    private String stuNum;
    private String classDes;
    private String sex;
    private String imageUrl;
    private Student mStudent = new Student();
    private List<Family> mFamilyList = new ArrayList<>();
    private Student unFindStudent;
    private boolean familyIsOk = false;
    private boolean studentInfoIsOk = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    initUI();
                    tv_name.setText("姓名：" + stuName);
                    tv_sex.setText("性别：" + sex);
                    tv_stu_num.setText("学号：" + stuNum);
                    tv_class.setText("班级：" + classDes);
                    // 加载图片
                    Glide.with(StudentProfileActivity.this).load(mStudent.getImageUrl()).placeholder(R.drawable.icon_pic)
                            .centerCrop()
                            .error(R.drawable.icon_pic)
                            .into(iv_pic);
                    closeProgressDialog();
                    break;
                case 1:
                    unFindStudentUI(unFindStudent);
                    break;
                case 2:
                    closeProgressDialog();
                    ToastUtli.show(getApplicationContext(), "查询失败");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentprofile);

        // 找到相应控件
        findUI();

        // 获取从上一个activity传过来的学号 和学生信息
        String studentNu = getIntent().getStringExtra("studentNu");
        unFindStudent = (Student) getIntent().getSerializableExtra("student");

        // 显示进度对话框
        showProgressDialog();

        // 查询学号  请求网络查询
        queryFromServer(studentNu);

    }

    // 查询不到的学生展示UI方式
    private void unFindStudentUI(Student unFindStudent) {
        Logs.i("查找不到的学生");
        mStudent = unFindStudent;
        initUI();
        tv_name.setText("姓名：" + mStudent.getStudentName());
        tv_sex.setText("性别：" + mStudent.getGenderDescription());
        tv_stu_num.setText("学号：" + mStudent.getStudentNu());
        tv_class.setText("班级：" + mStudent.getClassDescription());
        // 加载图片
        Glide.with(StudentProfileActivity.this).load(mStudent.getImageUrl()).placeholder(R.drawable.icon_pic)
                .centerCrop()
                .error(R.drawable.icon_pic)
                .into(iv_pic);
        closeProgressDialog();
        ToastUtli.show(getApplicationContext(), "该学生信息不全");
    }

    // 网络查询
    private void queryFromServer(String stuNum) {
        // 发送GET请求
        OkHttpUtils
                .get()
                .url(URLs.GetStudentInfo)
                .addParams("studentNu", stuNum)
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

                        JSONObject dataInfo = jsonObject.getJSONObject("data");

                        // **学生姓名
                        stuName = dataInfo.getString("StudentName");
                        mStudent.setStudentName(stuName);
                        // **学号
                        stuNum = dataInfo.getString("StudentNu");
                        mStudent.setStudentNu(stuNum);
                        // **性别
                        sex = dataInfo.getString("GenderDescription");
                        mStudent.setGenderDescription(sex);
                        // **班级
                        classDes = dataInfo.getString("ClassDescription");
                        mStudent.setClassDescription(classDes);
                        // 照片
                        imageUrl = dataInfo.getString("NewImage");
                        if (!imageUrl.equals("null")) {
                            mStudent.setImageUrl("http://home.hicc.cn/StudentImage/" + imageUrl);
                        } else {
                            imageUrl = dataInfo.getString("OldImage");
                            mStudent.setImageUrl("http://home.hicc.cn/OldImage/" + imageUrl);
                        }
                        // -专业
                        String professional = dataInfo.getString("ProfessionalDescription");
                        mStudent.setProfessionalDescription(professional);
                        // -缴费状态
                        String paymentStausDes = dataInfo.getString("PaymentStausDescription");
                        mStudent.setPaymentStausDescription(paymentStausDes);
                        // -民族
                        String nationalDes = dataInfo.getString("NationalDescription");
                        mStudent.setNationalDescription(nationalDes);
                        // -省份
                        String provinceDes = dataInfo.getString("ProvinceDescription");
                        mStudent.setProvinceDescription(provinceDes);
                        // **年级代码
                        int gradeCode = dataInfo.getInt("GradeCode");
                        mStudent.setGradeCode(gradeCode);
                        // -宿舍
                        String dormitoryDes = dataInfo.getString("DormitoryDescription");
                        mStudent.setDormitoryDescription(dormitoryDes);
                        // 宿舍号
                        if (!dataInfo.getString("DormitoryNo").equals("null")) {
                            int dormitoryNo = dataInfo.getInt("DormitoryNo");
                            mStudent.setDormitoryNo(dormitoryNo);
                        }
                        // -学部
                        String division = dataInfo.getString("DivisionDescription");
                        mStudent.setDivisionDescription(division);
                        // -电话
                        String phone = dataInfo.getString("YourPhone");
                        mStudent.setYourPhone(phone);
                        // -年级
                        String grade = dataInfo.getString("GradeDescription");
                        mStudent.setGradeDescription(grade);
                        // -床号
                        String bedNumber = dataInfo.getString("BedNumber");
                        mStudent.setBedNumber(bedNumber);
                        // **毕业学校
                        String oldSchool = dataInfo.getString("OldSchool");
                        mStudent.setOldSchool(oldSchool);
                        // -家庭住址
                        String homeAddress = dataInfo.getString("HomeAddress");
                        mStudent.setHomeAddress(homeAddress);
                        // -政治面貌
                        String politicsStatusDes = dataInfo.getString("PoliticsStatusDescription");
                        mStudent.setPoliticsStatusDescription(politicsStatusDes);
                        // -籍贯
                        String nativePlace = dataInfo.getString("NativePlace");
                        mStudent.setNativePlace(nativePlace);
                        // -现场报道
                        String liveReportStatueDes = dataInfo.getString("LiveReportStatueDescription");
                        mStudent.setLiveReportStatueDescription(liveReportStatueDes);
                        // -网上报道
                        String onlineReportStatueDes = dataInfo.getString("OnlineReportStatueDescription");
                        mStudent.setOnlineReportStatueDescription(onlineReportStatueDes);
                        // 班级代码
                        mStudent.setClassId(dataInfo.getInt("ClassId"));

                        studentInfoIsOk = true;

                        // 获取家庭信息
                        getFamilyInfo(stuNum);

                        if (studentInfoIsOk && familyIsOk) {
                            mHandler.sendEmptyMessage(0);
                        }
                    } else {
                        // 查不到  学号或服务器错误
                        mHandler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    // 解析错误
                    mHandler.sendEmptyMessage(2);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // 获取学生家庭信息
    private void getFamilyInfo(String stuNum) {
        OkHttpUtils
                .get()
                .url(URLs.GetFamilyInfo)
                .addParams("studentNu", stuNum)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.d("获取家庭信息失败：" + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i("解析家庭信息");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean sucessed = jsonObject.getBoolean("sucessed");
                            if (sucessed) {
                                // 解析家庭信息
                                JSONArray dataFamily = jsonObject.getJSONArray("data");
                                for (int i = 0; i < dataFamily.length(); i++) {
                                    Family family = new Family();

                                    JSONObject familyInfo = dataFamily.getJSONObject(i);
                                    // 学号
                                    String stuNu = familyInfo.getString("StudentNu");
                                    family.setStudentNum(stuNu);
                                    // 姓名
                                    String name = familyInfo.getString("Name");
                                    family.setName(name);
                                    // 工作
                                    String workandPosition = familyInfo.getString("WorkandPosition");
                                    family.setWorkand(workandPosition);
                                    // 关系
                                    String relation = familyInfo.getString("Relation");
                                    family.setRelation(relation);
                                    // 电话
                                    String familyPhone = familyInfo.getString("Phone");
                                    family.setPhone(familyPhone);
                                    // 年龄
                                    int age = familyInfo.getInt("Age");
                                    family.setAge(age);
                                    // 政治面貌
                                    String politicsStatus = familyInfo.getString("PoliticsStatus");
                                    family.setPolitics(politicsStatus);
                                    // 联系地址
                                    String contactAddress = familyInfo.getString("ContactAddress");
                                    family.setContactAddress(contactAddress);

                                    mFamilyList.add(family);
                                }
                                familyIsOk = true;

                                if (studentInfoIsOk && familyIsOk) {
                                    mHandler.sendEmptyMessage(0);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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

    private void findUI() {
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
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_stu_num = (TextView) findViewById(R.id.tv_stu_num);
        iv_pic = (ImageView) findViewById(R.id.iv_pic);

        iv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfileActivity.this, ImageDetailsActivity.class);
                intent.putExtra("image_path", mStudent.getImageUrl());
                //android V4包的类,用于两个activity转场时的缩放效果实现
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        StudentProfileActivity.this, iv_pic, "image");
                try {
                    ActivityCompat.startActivity(StudentProfileActivity.this, intent, optionsCompat.toBundle());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    startActivity(intent);
                }
            }
        });
    }

    private void initUI() {
        // 设置viewpager
        ScrollViewPager viewPager = (ScrollViewPager) findViewById(R.id.viewpager);
        // 设置viewpager是否禁止滑动
        viewPager.setNoScroll(false);
        setupViewPager(viewPager);

        // 设置tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#9AB4E2"));
        tabLayout.setupWithViewPager(viewPager);
    }

    // 设置viewpager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DetailedInfoFragment(mStudent), "详细信息");
        adapter.addFrag(new FamilyInfoFragment(mFamilyList), "家庭信息");
        adapter.addFrag(new RewardPunishmentInfoFragment(), "奖惩信息");

        viewPager.setAdapter(adapter);
    }

    // viewPager适配器
    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    // TODO 更改为listview的方式  先展示导员所带班级列表  每个item的点击事件为跳转到相应的班级学生列表(也是用listview)  学生列表的item点击事件才是跳转到学生信息界面
    // 1.找到相应控件
    // 2.编写listview适配器
    // 3.发网络请求  获取导员所带班级信息
    // 4.解析json数据  并保存到List集合中
    // 5.得到集合后  给listview设置适配器
}
