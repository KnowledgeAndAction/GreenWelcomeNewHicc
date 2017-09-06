package com.hicc.cloud.teacher.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.hicc.cloud.teacher.bean.PhoneInfo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by Administrator on 2016/10/24/024.
 */

public class PhoneInfoUtil {
    private static PhoneInfo phoneInfo;

    /**
     * 获取手机信息
     */
    public static PhoneInfo getPhoneInfo(Context context) {
        phoneInfo = new PhoneInfo();
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String mtyb = android.os.Build.BRAND;// 手机品牌
        phoneInfo.setPhoneBrand(mtyb);
        String mtype = android.os.Build.MODEL; // 手机型号
        phoneInfo.setPhoneBrandType(mtype);
        String imei = tm.getDeviceId();
        phoneInfo.setIMEI(imei);
        String imsi = tm.getSubscriberId();
        phoneInfo.setIMSI(imsi);
        String numer = tm.getLine1Number(); // 手机号码
        phoneInfo.setNumer(numer);
        String serviceName = tm.getSimOperatorName(); // 运营商
        phoneInfo.setServiceName(serviceName);
        phoneInfo.setCpuName(getCpuName());
        phoneInfo.setAndroidVersion("Android "+android.os.Build.VERSION.RELEASE);

        return phoneInfo;
    }

    /**
     * cpu名字
     * @return  cpu名字
     */
    private static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取包信息
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }
}
