package com.hicc.cloud.teacher;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.hicc.cloud.teacher.utils.ConstantValue;
import com.hicc.cloud.teacher.utils.Logs;
import com.hicc.cloud.teacher.utils.NetworkRequestUtil;
import com.hicc.cloud.teacher.utils.SpUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by Administrator on 2016/9/24/024.
 */

public class MyApplication extends Application {
    private static final String URL_ERROR = "http://suguan.hicc.cn/feedback1/error.do";

    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        ZXingLibrary.initDisplayOpinion(this);  //二维码扫描

        // 配置OkHttpUtils
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        return response.request().newBuilder()
                                .header("Authorization", "Bearer " + NetworkRequestUtil.getToken())
                                .build();
                    }
                })
                .build();

        OkHttpUtils.initClient(okHttpClient);


        // 捕获全局未捕获的异常
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                // 输出异常
                ex.printStackTrace();

                // 将打印的异常存到SD卡中
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"cloud_hicc_t_error.log";
                File file = new File(path);
                try {
                    PrintWriter printWriter = new PrintWriter(file);
                    ex.printStackTrace(printWriter);
                    printWriter.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // 将异常文件上传到服务器
                postErrorFile(file);

                // 手动退出应用
                System.exit(0);
            }
        });
    }

    // 将异常文件上传到服务器
    private void postErrorFile(final File file) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", SpUtils.getStringSp(getApplicationContext(), ConstantValue.USER_NAME,""));
        params.put("appId", "1");
        params.put("appVersion", String.valueOf(getVersionCode()));
        OkHttpUtils.post()
                .url(URL_ERROR)
                .params(params)
                .addFile("errorFile", "cloud_hicc_t_error.log", file)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("上传错误日志失败："+e.toString());
                        postErrorFile(file);
                        return;
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean sucessed = jsonObject.getBoolean("falg");
                            if(sucessed){
                                Logs.i("上传错误日志成功");
                            }else{
                                // 上传失败
                                Logs.i("错误日志:服务器未响应，上传失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Logs.i("错误日志：解析错误："+e.toString());
                        }
                    }
                });
    }

    // 获取本应用版本号
    private int getVersionCode() {
        // 拿到包管理者
        PackageManager pm = getPackageManager();
        // 获取包的基本信息
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            // 返回应用的版本号
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
