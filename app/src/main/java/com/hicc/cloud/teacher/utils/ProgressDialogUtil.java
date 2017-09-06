package com.hicc.cloud.teacher.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Administrator on 2016/10/24/024.
 * 进度对话框工具
 */
public class ProgressDialogUtil {
    private Context mContext;
    private ProgressDialog progressDialog;
    private String mDes;

    public ProgressDialogUtil(Context thisContext, String des){
        mContext = thisContext;
        mDes = des;
    }

    // 显示进度对话框
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage(mDes);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    return;
                }
            });
        }
        progressDialog.show();
    }

    // 关闭进度对话框
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
