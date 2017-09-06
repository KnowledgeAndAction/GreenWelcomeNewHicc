package com.hicc.cloud.teacher.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by Administrator on 2016/11/4/004.
 */

public class NetworkRequestUtil {
    private static final String URL_FUNCTION = "http://suguan.hicc.cn/feedback1/function.do";

    // 向服务器发送点击的功能
    public static void postClickFunction(final Context context, final String functionId) {
        // 发送GET请求
        OkHttpUtils
                .get()
                .url(URL_FUNCTION)
                .addParams("userId", SpUtils.getStringSp(context,ConstantValue.USER_NAME,""))
                .addParams("appId", "1")
                .addParams("appVersion", String.valueOf(getVersionCode(context)))
                .addParams("functionId", functionId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("上传点击功能失败："+e.toString());
                        // 上传失败  重新上传
                        //postClickFunction(context,functionId);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i(response);
                        // 解析json
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean sucessed = jsonObject.getBoolean("falg");
                            if(sucessed){
                                Logs.i("上传点击功能成功");
                            }else{
                                // 上传失败
                                Logs.i("点击功能:服务器未响应，上传失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Logs.i("点击功能,解析错误："+e.toString());
                        }
                    }
                });
    }

    // 获取当前时间
    private static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    // 获取本应用版本号
    private static int getVersionCode(Context context) {
        // 拿到包管理者
        PackageManager pm = context.getPackageManager();
        // 获取包的基本信息
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            // 返回应用的版本号
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取token令牌
     */
    public static String getToken() {
        String token = "";
        // 创建一个okhttp连接
        OkHttpClient client = new OkHttpClient.Builder().build();

        // 创建一个请求体
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                // 设置请求体类型
                return MediaType.parse("text/plain");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                // 写入请求体数据
                String post = "grant_type=client_credentials&client_id=StudentForest&client_secret=information12016921";
                sink.writeUtf8(post);
            }
        };

        // 创建一个post请求
        Request request = new Request.Builder()
                .url("http://api.hicc.cn//token")
                .post(requestBody)
                .build();

        try {
            // 通过okhttp发起一次请求，获得响应
            Response response = client.newCall(request).execute();

            // 获得响应体
            String result = response.body().string();
            // 解析json数据
            JSONObject jsonObject = new JSONObject(result);
            // 获得返回的token
            token = jsonObject.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }
}
