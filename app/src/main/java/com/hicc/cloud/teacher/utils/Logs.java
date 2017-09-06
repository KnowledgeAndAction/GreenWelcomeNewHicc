package com.hicc.cloud.teacher.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/9/29/029.
 */

public class Logs {
    private static final String TAG = "云上工商：";
    private static final int STATUS = 1;   // 当前打印日志状态
    private static final int TURN_ON = 1;
    private static final int TURN_OFF = 2;

    public static void i(String s) {
        if(STATUS == TURN_ON){
            Log.i(TAG,s);
        }
    }
    public static void d(String s) {
        if(STATUS == TURN_ON){
            Log.d(TAG,s);
        }
    }
    public static void e(String s) {
        if(STATUS == TURN_ON){
            Log.e(TAG,s);
        }
    }
    public static void v(String s) {
        if(STATUS == TURN_ON){
            Log.v(TAG,s);
        }
    }
    public static void w(String s) {
        if(STATUS == TURN_ON){
            Log.w(TAG,s);
        }
    }
}
