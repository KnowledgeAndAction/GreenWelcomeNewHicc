package com.hicc.cloud.teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/13/013.
 * 学生表
 */

public class Student implements Serializable {
    private int id;
    // 姓名
    private String StudentName;
    // 学号
    private String StudentNu;
    // 专业
    private String ProfessionalDescription;
    // 性别
    private String GenderDescription;
    // 班级
    private String ClassDescription;
    // 缴费状态
    private String PaymentStausDescription;
    // 民族
    private String NationalDescription;
    // 省份
    private String ProvinceDescription;
    // 年级代码
    private int GradeCode;
    // 宿舍楼
    private String DormitoryDescription;
    // 宿舍号
    private int DormitoryNo;
    // 学部
    private String DivisionDescription;
    // 电话
    private String YourPhone;
    // 年级
    private String GradeDescription;
    // 床号
    private String BedNumber;
    // 毕业学校
    private String OldSchool;
    // 家庭住址
    private String HomeAddress;
    // 政治面貌
    private String PoliticsStatusDescription;
    // 籍贯
    private String NativePlace;
    // 现场报道
    private String LiveReportStatueDescription;
    // 网上报道
    private String OnlineReportStatueDescription;
    // 班级ID
    private int ClassId;
    // 照片
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getStudentNu() {
        return StudentNu;
    }

    public void setStudentNu(String studentNu) {
        StudentNu = studentNu;
    }

    public String getProfessionalDescription() {
        return ProfessionalDescription;
    }

    public void setProfessionalDescription(String professionalDescription) {
        ProfessionalDescription = professionalDescription;
    }

    public String getGenderDescription() {
        return GenderDescription;
    }

    public void setGenderDescription(String genderDescription) {
        GenderDescription = genderDescription;
    }

    public String getClassDescription() {
        return ClassDescription;
    }

    public void setClassDescription(String classDescription) {
        ClassDescription = classDescription;
    }

    public String getPaymentStausDescription() {
        return PaymentStausDescription;
    }

    public void setPaymentStausDescription(String paymentStausDescription) {
        PaymentStausDescription = paymentStausDescription;
    }

    public String getNationalDescription() {
        return NationalDescription;
    }

    public void setNationalDescription(String nationalDescription) {
        NationalDescription = nationalDescription;
    }

    public String getProvinceDescription() {
        return ProvinceDescription;
    }

    public void setProvinceDescription(String provinceDescription) {
        ProvinceDescription = provinceDescription;
    }

    public int getGradeCode() {
        return GradeCode;
    }

    public void setGradeCode(int gradeCode) {
        GradeCode = gradeCode;
    }

    public String getDormitoryDescription() {
        return DormitoryDescription;
    }

    public void setDormitoryDescription(String dormitoryDescription) {
        DormitoryDescription = dormitoryDescription;
    }

    public int getDormitoryNo() {
        return DormitoryNo;
    }

    public void setDormitoryNo(int dormitoryNo) {
        DormitoryNo = dormitoryNo;
    }

    public String getDivisionDescription() {
        return DivisionDescription;
    }

    public void setDivisionDescription(String divisionDescription) {
        DivisionDescription = divisionDescription;
    }

    public String getYourPhone() {
        return YourPhone;
    }

    public void setYourPhone(String yourPhone) {
        YourPhone = yourPhone;
    }

    public String getGradeDescription() {
        return GradeDescription;
    }

    public void setGradeDescription(String gradeDescription) {
        GradeDescription = gradeDescription;
    }

    public String getBedNumber() {
        return BedNumber;
    }

    public void setBedNumber(String bedNumber) {
        BedNumber = bedNumber;
    }

    public String getOldSchool() {
        return OldSchool;
    }

    public void setOldSchool(String oldSchool) {
        OldSchool = oldSchool;
    }

    public String getHomeAddress() {
        return HomeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        HomeAddress = homeAddress;
    }

    public String getPoliticsStatusDescription() {
        return PoliticsStatusDescription;
    }

    public void setPoliticsStatusDescription(String politicsStatusDescription) {
        PoliticsStatusDescription = politicsStatusDescription;
    }

    public String getNativePlace() {
        return NativePlace;
    }

    public void setNativePlace(String nativePlace) {
        NativePlace = nativePlace;
    }

    public String getLiveReportStatueDescription() {
        return LiveReportStatueDescription;
    }

    public void setLiveReportStatueDescription(String liveReportStatueDescription) {
        LiveReportStatueDescription = liveReportStatueDescription;
    }

    public String getOnlineReportStatueDescription() {
        return OnlineReportStatueDescription;
    }

    public void setOnlineReportStatueDescription(String onlineReportStatueDescription) {
        OnlineReportStatueDescription = onlineReportStatueDescription;
    }

    public int getClassId() {
        return ClassId;
    }

    public void setClassId(int classId) {
        ClassId = classId;
    }
}

