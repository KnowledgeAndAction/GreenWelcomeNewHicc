package com.hicc.cloud.teacher.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具.
 */
public class SpUtils {
	private static SharedPreferences sp;

	public static void putBoolSp(Context context, String key, boolean value) {
		if (sp == null) {
			sp = context.getSharedPreferences("confing", Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}
	
	public static boolean getBoolSp(Context context, String key, boolean defValue){
		if(sp == null){
			sp = context.getSharedPreferences("confing", Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	}
	
	public static void putStringSp(Context context, String key, String value) {
		if (sp == null) {
			sp = context.getSharedPreferences("confing", Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}
	
	public static String getStringSp(Context context, String key, String defValue){
		if(sp == null){
			sp = context.getSharedPreferences("confing", Context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	}
	
	public static void putIntSp(Context context, String key, int value) {
		if (sp == null) {
			sp = context.getSharedPreferences("confing", Context.MODE_PRIVATE);
		}
		sp.edit().putInt(key, value).commit();
	}
	
	public static int getIntSp(Context context, String key, int defValue){
		if(sp == null){
			sp = context.getSharedPreferences("confing", Context.MODE_PRIVATE);
		}
		return sp.getInt(key, defValue);
	}

	/**删除存在sp中对应key的内容
	 * @param context  上下文环境
	 * @param key  要删除的key值
	 */
	public static void remove(Context context, String key) {
		if (sp == null) {
			sp = context.getSharedPreferences("confing", Context.MODE_PRIVATE);
		}
		sp.edit().remove(key).commit();
	}
}
