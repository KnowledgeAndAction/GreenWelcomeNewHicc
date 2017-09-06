package com.hicc.cloud.teacher.utils;

import android.os.Environment;

/**
 * Created on 2016/9/24/023.
 * 存储常量
 */
public class ConstantValue {
    /**
     * 是否首次加载数据
     */
    public static final String FIRST_DATA = "first_data";
    /**
     * 是否记住密码
     */
    public static final String IS_REMBER_PWD = "is_rember_pwd";
    /**
     * 保存的用户账号
     */
    public static final String USER_NAME = "user_name";
    /**
     * 保存的用户密码
     */
    public static final String PASS_WORD = "pass_word";
    /**
     * 老师姓名
     */
    public static final String TEACHER_NAME = "teacher_name";
    /**
     * 老师职位
     */
    public static final String TEACHER_LEVEL = "teacher_level";
    /**
     * 老师电话
     */
    public static final String TEACHER_PHONE = "teacher_phone";
    /**
     * UserNo
     */
    public static final String USER_NO = "user_no";
    /**
     * RecordCode
     */
    public static final String RECORD_CODE = "record_code";
    /**
     * Nid
     */
    public static final String NID = "nid";
    /**
     * UserLevelCode
     */
    public static final String USER_LEVEL_CODE = "user_level_code";
    /**
     * 报道计数
     */
    public static final String REPORTED_COUNT = "reported_count";

    /**
     * 更新应用下载保存路径
     */
    public static final String downloadpathName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/云上工商.apk";
    /**
     * 学生档案标记
     */
    public static final int STUDENT_PROFILE = 1;
    /**
     * 学生成绩标记
     */
    public static final int STUDENT_MARK = 2;
    /**
     * 宿舍成绩标记
     */
    public static final int DORMITORY_SCORE = 3;
    /**
     * 交费统计标记
     */
    public static final int PAY_STATISTICS = 4;
}
