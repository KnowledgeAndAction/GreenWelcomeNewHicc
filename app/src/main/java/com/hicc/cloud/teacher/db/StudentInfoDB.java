package com.hicc.cloud.teacher.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hicc.cloud.teacher.bean.Clas;
import com.hicc.cloud.teacher.bean.Division;
import com.hicc.cloud.teacher.bean.Grade;
import com.hicc.cloud.teacher.bean.Professional;
import com.hicc.cloud.teacher.bean.StartStudent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/13/013.
 */

public class StudentInfoDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "hicc_student_info";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static StudentInfoDB studentInfoDB;
    private SQLiteDatabase db;

    // 私有化构造方法   单例模式
    private StudentInfoDB(Context context){
        StudentInfoOpenHelper dbHelper = new StudentInfoOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    // 获取 StudentInfoDB 实例
    public synchronized static StudentInfoDB getInstance(Context context){
        if(studentInfoDB == null){
            studentInfoDB = new StudentInfoDB(context);
        }
        return studentInfoDB;
    }

    /**
     * 年级
     * 将 Grade 实例存到数据库中
     */
    public void saveGrade(Grade grade){
        if(grade != null){
            ContentValues values = new ContentValues();
            values.put("grade_code", grade.getGradeCode());
            db.insert("Grade",null,values);
        }
    }

    /**
     * 获取所有年级
     * @return 年级集合
     */
    public List<Grade> getGrades(){
        List<Grade> list = new ArrayList<Grade>();
        Cursor cursor = db.query("Grade", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            Grade grade = new Grade();
            grade.setId(cursor.getInt(cursor.getColumnIndex("id")));
            grade.setGradeCode(cursor.getInt(cursor.getColumnIndex("grade_code")));

            list.add(grade);
        }
        cursor.close();
        return list;
    }

    /**
     * 学部
     * 将 Division 实例存到数据库中
     */
    public void saveDivision(Division division){
        if(division != null){
            ContentValues values = new ContentValues();
            values.put("division_des", division.getDivisionDes());
            values.put("division_code", division.getDivisionCode());
            values.put("grade_code", division.getGradeCode());
            db.insert("Division",null,values);
        }
    }

    /**
     * 获取年级下的所有学部
     * @param gradeCode 年级代码
     * @return 学部集合
     */
    public List<Division> getDivisions(int gradeCode){
        List<Division> list = new ArrayList<Division>();
        Cursor cursor = db.query("Division", null, "grade_code = ?", new String[]{String.valueOf(gradeCode)}, null, null, null);
        while (cursor.moveToNext()){
            Division division = new Division();
            division.setId(cursor.getInt(cursor.getColumnIndex("id")));
            division.setDivisionDes(cursor.getString(cursor.getColumnIndex("division_des")));
            division.setDivisionCode(cursor.getInt(cursor.getColumnIndex("division_code")));
            division.setGradeCode(gradeCode);

            list.add(division);
        }
        cursor.close();
        return list;
    }

    /**
     * 专业
     * 将 Professional 实例存到数据库中
     */
    public void saveProfessional(Professional professional){
        if(professional != null){
            ContentValues values = new ContentValues();
            values.put("professional_des", professional.getProfessionalDes());
            values.put("professional_code", professional.getProfessionalCode());
            values.put("division_code", professional.getDivisionCode());
            db.insert("Professional",null,values);
        }
    }

    /**
     * 获取学部下的所有专业
     * @param divisionCode 学部代码
     * @return 专业集合
     */
    public List<Professional> getProfessionals(int divisionCode){
        List<Professional> list = new ArrayList<Professional>();
        Cursor cursor = db.query("Professional", null, "division_code = ?", new String[]{String.valueOf(divisionCode)}, null, null, null);
        while (cursor.moveToNext()){
            Professional professional = new Professional();
            professional.setId(cursor.getInt(cursor.getColumnIndex("id")));
            professional.setProfessionalDes(cursor.getString(cursor.getColumnIndex("professional_des")));
            professional.setProfessionalCode(cursor.getInt(cursor.getColumnIndex("professional_code")));
            professional.setDivisionCode(cursor.getInt(cursor.getColumnIndex("division_code")));

            list.add(professional);
        }
        cursor.close();
        return list;
    }

    /**
     * 班级
     * 将 Clas 实例存到数据库中
     */
    public void saveClass(Clas clas){
        if(clas != null){
            ContentValues values = new ContentValues();
            values.put("class_des", clas.getClassDes());
            values.put("class_code", clas.getClassCode());
            values.put("professional_code", clas.getProfessionalCode());
            values.put("grade_code", clas.getGradeCode());
            values.put("class_qq_group", clas.getClassQQGroup());
            db.insert("Clas",null,values);
        }
    }

    /**
     * 获取专业下的所有班级
     * @param professionalCode 专业代码
     * @return 班级集合
     */
    public List<Clas> getClass(int professionalCode){
        List<Clas> list = new ArrayList<Clas>();
        Cursor cursor = db.query("Clas", null, "professional_code = ?", new String[]{String.valueOf(professionalCode)}, null, null, null);
        while (cursor.moveToNext()){
            Clas clas = new Clas();
            clas.setId(cursor.getInt(cursor.getColumnIndex("id")));
            clas.setClassDes(cursor.getString(cursor.getColumnIndex("class_des")));
            clas.setClassCode(cursor.getInt(cursor.getColumnIndex("class_code")));
            clas.setGradeCode(cursor.getInt(cursor.getColumnIndex("grade_code")));
            clas.setProfessionalCode(cursor.getInt(cursor.getColumnIndex("professional_code")));
            clas.setClassQQGroup(cursor.getString(cursor.getColumnIndex("class_qq_group")));

            list.add(clas);
        }
        cursor.close();
        return list;
    }

    /**
     * 获取指定班级的 id
     * @param calssDes 班级描述
     * @param gradeCode 年级代码
     * @return int 类型 id
     */
    public int getClasCodeForDB(String calssDes, int gradeCode){
        Cursor cursor = db.query("Clas", new String[]{"class_code","grade_code"}, "class_des = ?", new String[]{calssDes}, null, null, null);
        int classCode = -1;
        while (cursor.moveToNext()){
            if(cursor.getInt(cursor.getColumnIndex("grade_code")) == gradeCode){
                classCode = cursor.getInt(cursor.getColumnIndex("class_code"));
            }
        }
        return classCode;
    }

    /**增加一个常用联系人
     * @param startStudent  星标学生
     */
    public void insertStart(StartStudent startStudent){
        if(startStudent != null){
            ContentValues values = new ContentValues();
            values.put("name", startStudent.getName());
            values.put("phone", startStudent.getPhone());
            values.put("class_id", startStudent.getClassId());
            db.insert("StartStudent", null, values);
        }
    }

    /**删除常用联系人
     * @param phone  星标学生电话
     */
    public void deleteStart(long phone){
        db.delete("StartStudent", "phone = ?", new String[]{String.valueOf(phone)});
    }

    /**查询数据库中所有星标学生姓名
     * @return  返回包含星标学生姓名的集合
     */
    public List<String> queryStartNames(int classId){
        Cursor cursor = db.query("StartStudent", new String[]{"name"}, "class_id = ?", new String[]{String.valueOf(classId)}, null, null, null);
        List<String> startNameList = new ArrayList<String>();
        while(cursor.moveToNext()){
            startNameList.add(cursor.getString(0));
        }
        cursor.close();

        return startNameList;
    }

    /**查询数据库中所有星标学生
     * @return  返回包含星标学生的集合
     */
    public List<StartStudent> queryStartStudents(int classId){
        List<StartStudent> startStudentList = new ArrayList<StartStudent>();
        Cursor cursor = db.query("StartStudent", null, "class_id = ?", new String[]{String.valueOf(classId)}, null, null, null);
        while (cursor.moveToNext()){
            StartStudent startStudent = new StartStudent();
            startStudent.setName(cursor.getString(cursor.getColumnIndex("name")));
            startStudent.setPhone(cursor.getLong(cursor.getColumnIndex("phone")));

            startStudentList.add(startStudent);
        }
        cursor.close();

        return startStudentList;
    }
}
