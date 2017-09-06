package com.hicc.cloud.teacher.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/10/13/013.
 */

public class StudentInfoOpenHelper extends SQLiteOpenHelper {
    /**
     * 创建年级表语句
     */
    public static final String CREAT_GRADE = "create table Grade (id integer primary key autoincrement, grade_code integer)";
    /**
     * 创建学部表语句
     */
    public static final String CREAT_DIVISION = "create table Division (id integer primary key autoincrement, division_des text, division_code integer, grade_code integer)";
    /**
     * 创建专业表语句
     */
    public static final String CREAT_PROFESSIONAL = "create table Professional (id integer primary key autoincrement, professional_des text, professional_code integer, division_code integer)";
    /**
     * 创建班级表语句
     */
    public static final String CREAT_CLASS = "create table Clas (id integer primary key autoincrement, grade_code integer, class_des text, class_code integer, professional_code integer, class_qq_group text)";
    /**
     * 创建常用联系人表
     */
    public static final String CREAT_START_STUDENT = "create table StartStudent (id integer primary key autoincrement, name text, phone money, class_id integer)";


    public StudentInfoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_GRADE);    //年级表
        db.execSQL(CREAT_DIVISION);     //学部表
        db.execSQL(CREAT_PROFESSIONAL);     //专业表
        db.execSQL(CREAT_CLASS);       //班级表
        db.execSQL(CREAT_START_STUDENT);       //常用联系人表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
