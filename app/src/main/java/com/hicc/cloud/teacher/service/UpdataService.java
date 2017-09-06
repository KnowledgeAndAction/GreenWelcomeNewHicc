package com.hicc.cloud.teacher.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2016/9/27/027.
 */

public class UpdataService extends Service {

    private IntentFilter filter;
    private BroadcastReceiver mApplicationsReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("UpdataService","启动了吗");
        filter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        filter.addDataScheme("package");
        mApplicationsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("UpdataService","安装的包名"+intent.getDataString());
            }
        };
        registerReceiver(mApplicationsReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mApplicationsReceiver != null){
            unregisterReceiver(mApplicationsReceiver);

        }
    }
}
