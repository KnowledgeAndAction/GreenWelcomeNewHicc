package com.hicc.cloud.teacher.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐司工具
 */
public class ToastUtli {
	public static void show(Context context, String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
