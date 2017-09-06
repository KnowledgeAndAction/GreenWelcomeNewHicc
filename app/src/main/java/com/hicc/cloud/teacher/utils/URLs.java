package com.hicc.cloud.teacher.utils;

/**
 * Created by cs on 2017/8/19/019.
 * 接口地址链接
 */

public class URLs {
    /**
     * 登录
     */
    public static final String Login = "http://home.hicc.cn/PhoneInterface/LoginService.asmx/LoginNew";
    /**
     * 获取用户信息
     */
    public static final String GetUserInfo = "http://api.hicc.cn/api/TUserInfo/GetDataByAccount";
    /**
     * 获取老师带班信息
     */
    public static final String GetClassByUserNo = "http://api.hicc.cn/api/TClass/GetclassByUserNo";
    /**
     * 获取班级学生列表
     */
    public static final String GetClassList = "http://api.hicc.cn/api/TStudentInfo/Getpagestudentinfo";
    /**
     * 获取学生信息
     */
    public static final String GetStudentInfo = "http://api.hicc.cn/api/TStudentInfo/GetStudentInfo";
    /**
     * 获取学生家庭信息
     */
    public static final String GetFamilyInfo = "http://api.hicc.cn/api/TFamilyMembers/GetStuMembersByNum";
    /**
     * 获取学生成绩
     */
    public static final String GetAllScore = "http://api.hicc.cn/api/TExamResult/GetAllScoreByStuNum";
    /**
     * 获取网上报道人数
     */
    public static final String GetOnlineNum = "http://home.hicc.cn/PhoneInterface/OnlineReportService.asmx/Getonlinereportnum";
    /**
     * 获取现场报道人数
     */
    public static final String GetLiveNum = "http://home.hicc.cn/PhoneInterface/SceneReportService.asmx/Getscenereportnum";
    /**
     * 交费统计
     */
    public static final String GetPayCostNum = "http://home.hicc.cn/PhoneInterface/SceneReportService.asmx/Getpaycostnum";
    /**
     * 获取班级宿舍信息
     */
    public static final String GetDormitory = "http://api.hicc.cn/api/TDormitory/GetDormitoryByClassCode";
    /**
     * 获取宿舍成绩
     */
    public static final String GetDormitoryScore = "http://api.hicc.cn/api/TDormitoryScore/GetDormitoryScoreByNo";
    /**
     * 获取宿舍成员
     */
    public static final String GetDormitoryMember = "http://api.hicc.cn/api/TDormitory/GetDormitoryByClassCode";
}
