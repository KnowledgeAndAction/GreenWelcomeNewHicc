package com.hicc.cloud.teacher.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.utils.Logs;

/**
 * Created by Administrator on 2016/10/9/033.
 */
public class ShakeActivity extends Activity {

    SensorManager sm;
    SensorL listener;
    private boolean isRefresh = false;
    private Vibrator vibe;
    private AlertDialog.Builder builder;
    private ImageView iv_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        initUI();

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        listener = new SensorL();
        // 对加速计进行监听
        sm.registerListener(listener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

        builder = new AlertDialog.Builder(this);
        // 用来显示的对话框
        builder.setMessage("摇到了.................");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isRefresh = false;
                dialog.cancel();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            //当点击返回的按钮时执行
            @Override
            public void onCancel(DialogInterface dialog) {
                isRefresh = false;
                dialog.cancel();
            }
        });
    }

    private class SensorL implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                // 判断是否在刷新
                if (isRefresh) {
                    return;
                }
                float newX = Math.abs(event.values[SensorManager.DATA_X]);
                float newY = Math.abs(event.values[SensorManager.DATA_Y]);
                float newZ = Math.abs(event.values[SensorManager.DATA_Z]);
                // 这里是关键，判断某个方向上的加速度值是否达到自己想要的值
                if (newX >= 19 || newY >= 19 || newZ >= 19) {
                    // TODO 可能与云子对接
                    Logs.i("X轴：" + newX);
                    Logs.i("Y轴：" + newY);
                    Logs.i("Z轴：" + newZ);
                    isRefresh = true;
                    vibe.vibrate(200);
                    builder.show();
                    return;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        // ACTIVITY消失时取消监听
        sm.unregisterListener(listener);
        super.onPause();
    }

    private void initUI() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        sm.registerListener(listener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();
    }
}