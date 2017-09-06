package com.hicc.cloud.teacher.bean;

/**
 * Created by Administrator on 2017/8/23/023.
 */

public class Dormitory {
    // 宿舍楼
    private String DormitoryBuildingDescription;
    // 检查类型
    private String CheckTypeDescription;
    // 宿舍楼编号
    private int DormitoryBuildingCode;
    // 宿舍号
    private int DormitoryNo;
    // 年
    private int ScoreTimeYear;
    // 月
    private int ScoreTimeMonth;
    // 周
    private int WeekCode;
    // 分数
    private int TotalScore;

    public String getCheckTypeDescription() {
        return CheckTypeDescription;
    }

    public void setCheckTypeDescription(String checkTypeDescription) {
        CheckTypeDescription = checkTypeDescription;
    }

    public int getScoreTimeYear() {
        return ScoreTimeYear;
    }

    public void setScoreTimeYear(int scoreTimeYear) {
        ScoreTimeYear = scoreTimeYear;
    }

    public int getScoreTimeMonth() {
        return ScoreTimeMonth;
    }

    public void setScoreTimeMonth(int scoreTimeMonth) {
        ScoreTimeMonth = scoreTimeMonth;
    }

    public int getWeekCode() {
        return WeekCode;
    }

    public void setWeekCode(int weekCode) {
        WeekCode = weekCode;
    }

    public int getTotalScore() {
        return TotalScore;
    }

    public void setTotalScore(int totalScore) {
        TotalScore = totalScore;
    }

    public String getDormitoryBuildingDescription() {
        return DormitoryBuildingDescription;
    }

    public void setDormitoryBuildingDescription(String dormitoryBuildingDescription) {
        DormitoryBuildingDescription = dormitoryBuildingDescription;
    }


    public int getDormitoryBuildingCode() {
        return DormitoryBuildingCode;
    }

    public void setDormitoryBuildingCode(int dormitoryBuildingCode) {
        DormitoryBuildingCode = dormitoryBuildingCode;
    }

    public int getDormitoryNo() {
        return DormitoryNo;
    }

    public void setDormitoryNo(int dormitoryNo) {
        DormitoryNo = dormitoryNo;
    }

}
