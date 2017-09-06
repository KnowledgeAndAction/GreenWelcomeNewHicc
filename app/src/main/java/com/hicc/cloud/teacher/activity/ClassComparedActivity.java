package com.hicc.cloud.teacher.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.utils.ConstantValue;
import com.hicc.cloud.teacher.utils.Logs;
import com.hicc.cloud.teacher.utils.SpUtils;
import com.hicc.cloud.teacher.utils.ToastUtli;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;
import okhttp3.Call;

// 班级报道对比
public class ClassComparedActivity extends AppCompatActivity {

    private ImageView iv_back;
    private ProgressDialog progressDialog;
    public static int AllStuNum;
    public static int live;
    public static int notLive;
    public static int onLine;
    public static int notOnLine;
    private static List<Integer> flag = new ArrayList<>();
    private TextView tv_online_all;
    private TextView tv_online_yes;
    private TextView tv_online_no;
    private TextView tv_online_ratio;
    private TextView tv_live_all;
    private TextView tv_live_yes;
    private TextView tv_live_no;
    private TextView tv_live_ratio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_compared);

        initUI();

        getDate();
    }

    private void getDate() {
        showProgressDialog();
        // 获取班级报道信息 崔
        OkHttpUtils
                .get()
                .url("http://home.hicc.cn/PhoneInterface/LoginService.asmx/ClassInfoByuserNo")
                .addParams("userNo", SpUtils.getIntSp(getApplication(), ConstantValue.USER_NO, 0) + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeProgressDialog();
                        Logs.i(e.toString());
                        ToastUtli.show(getApplicationContext(), "服务器繁忙，请重新查询");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i(response);
                        // 解析json
                        Logs.i("解析json");
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int length = jsonArray.length();
                            for (int j=0; j < length; j++) {
                                flag.add(0);
                            }
                            for (int i = 0; i < length; i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                int ClassCode = data.getInt("ClassCode");
                                int Grade = data.getInt("TimeCode");
                                ClassInformation(ClassCode, Grade,i,length);
                            }

                            notLive = 0;
                            live = 0;
                            AllStuNum = 0;
                            onLine = 0;
                            notOnLine = 0;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            closeProgressDialog();
                            ToastUtli.show(getApplicationContext(), "11获取信息失败");
                        }
                    }
                });

    }

    private void ClassInformation(int ClassCode, int Grade, final int i, final int length) {
        // 获取现场报道信息
        OkHttpUtils
                .get()
                .url("http://home.hicc.cn/PhoneInterface/CoacherContrast.asmx/getclassreportsnum")
                .addParams("ClassID", ClassCode + "")
                .addParams("Grade", Grade + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeProgressDialog();
                        Logs.i(e.toString());
                        ToastUtli.show(getApplicationContext(), "服务器繁忙，请重新查询");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i(response);
                        // 解析json
                        Logs.i("解析json");
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject data = jsonArray.getJSONObject(0);

                            int Online = data.getInt("Online");
                            int Live = data.getInt("Live");
                            int NotOnline = data.getInt("NotOnline");
                            int NotLive = data.getInt("NotLive");
                            int all = data.getInt("AllStuNum");
                            AllStuNum += all;
                            live += Live;
                            notLive += NotLive;
                            onLine += Online;
                            notOnLine += NotOnline;
                            flag.remove(0);
                            if (i == (length-1) && flag.size()==0) {
                                tv_online_all.setText(AllStuNum+"");
                                tv_online_yes.setText(onLine+"");
                                tv_online_no.setText(notOnLine+"");
                                String s1 = String.format("%.2f", ((double) onLine / AllStuNum) * 100);
                                tv_online_ratio.setText(s1+"%");

                                tv_live_all.setText(AllStuNum+"");
                                tv_live_yes.setText(live+"");
                                tv_live_no.setText(notLive+"");
                                tv_live_ratio.setText(AllStuNum+"");
                                String s2 = String.format("%.2f", ((double) live / AllStuNum) * 100);
                                tv_live_ratio.setText(s2+"%");
                                getSupportFragmentManager().beginTransaction().add(R.id.container_online, new PlaceholderFragment(AllStuNum, onLine, notOnLine)).commit();
                                getSupportFragmentManager().beginTransaction().add(R.id.container_live, new PlaceholderLiveFragment(AllStuNum,live, notLive)).commit();
                                closeProgressDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtli.show(getApplicationContext(), "获取信息失败");
                            closeProgressDialog();
                        }
                    }
                });
    }


    private void initUI() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_online_all = (TextView) findViewById(R.id.tv_online_all);
        tv_online_yes = (TextView) findViewById(R.id.tv_online_yes);
        tv_online_no = (TextView) findViewById(R.id.tv_online_no);
        tv_online_ratio = (TextView) findViewById(R.id.tv_online_ratio);

        tv_live_all = (TextView) findViewById(R.id.tv_live_all);
        tv_live_yes = (TextView) findViewById(R.id.tv_live_yes);
        tv_live_no = (TextView) findViewById(R.id.tv_live_no);
        tv_live_ratio = (TextView) findViewById(R.id.tv_live_ratio);
    }


    // 网上报道 柱状图
    public static class PlaceholderFragment extends Fragment {
        private ColumnChartView chart;
        private ColumnChartData data;

        private boolean hasAxes = true;
        private boolean hasAxesNames = true;
        private boolean hasLabels = true;
        private boolean hasLabelForSelected = false;
        private int all;
        private int yes;
        private int no;

        @SuppressLint("ValidFragment")
        public PlaceholderFragment(int all, int yes, int no) {
            this.all = all;
            this.yes = yes;
            this.no = no;
        }

        @SuppressLint("ValidFragment")
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_column_chart, container, false);

            chart = (ColumnChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());
            // 禁止缩放
            chart.setZoomEnabled(false);

            generateDefaultData();

            return rootView;
        }

        // 添加数据
        private void generateDefaultData() {
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;


            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue(all, ChartUtils.pickColor()));
            Column column1 = new Column(values);
            column1.setHasLabels(hasLabels);
            column1.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column1);


            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue(yes, ChartUtils.pickColor()));
            Column column2 = new Column(values);
            column2.setHasLabels(hasLabels);
            column2.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column2);


            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue(no, ChartUtils.pickColor()));
            Column column3 = new Column(values);
            column3.setHasLabels(hasLabels);
            column3.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column3);
            data = new ColumnChartData(columns);


            // 坐标
            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisY.setName("人数");
                    ArrayList<AxisValue> axisValuesX = new ArrayList<AxisValue>();

                    axisValuesX.add(new AxisValue(0).setValue(0).setLabel("总人数"));
                    axisValuesX.add(new AxisValue(1).setValue(1).setLabel("已报到"));
                    axisValuesX.add(new AxisValue(2).setValue(2).setLabel("未报到"));
                    axisX.setValues(axisValuesX);
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            chart.setColumnChartData(data);
        }

        // 触摸事件
        private class ValueTouchListener implements ColumnChartOnValueSelectListener {
            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                // 获取点击的条目数值
                String vaule = value.toString();
                int end = vaule.length() - 3;
                // 截取所需字符串
                String subV = vaule.substring(19, end);
                switch (columnIndex) {
                    case 0:
                        ToastUtli.show(getContext(), "总共" + subV + "人");
                        break;
                    case 1:
                        ToastUtli.show(getContext(), "已报到" + subV + "人");
                        break;
                    case 2:
                        ToastUtli.show(getContext(), "未报到" + subV + "人");
                        break;
                }
            }

            @Override
            public void onValueDeselected() {
            }
        }
    }


    // 现场报道 饼状图
    public static class PlaceholderLiveFragment extends Fragment {
        private PieChartView chart;
        private PieChartData data;
        private int all;
        private int yes;
        private int no;

        @SuppressLint("ValidFragment")
        public PlaceholderLiveFragment(int all,int yes, int no) {
            this.all = all;
            this.yes = yes;
            this.no = no;
        }


        public PlaceholderLiveFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_pie_chart, container, false);

            chart = (PieChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());

            generateData();

            return rootView;
        }

        // 设置数据
        private void generateData() {

            List<SliceValue> values = new ArrayList<SliceValue>();

            String s1 = String.format("%.2f", ((double) yes / all) * 100);
            SliceValue sliceValue1 = new SliceValue(yes, ChartUtils.pickColor());
            sliceValue1.setLabel("已报到" + s1 + "%");
            values.add(sliceValue1);

            String s2 = String.format("%.2f", ((double) no / all) * 100);
            SliceValue sliceValue2 = new SliceValue(no, ChartUtils.pickColor());
            sliceValue2.setLabel("未报到" + s2 + "%");
            values.add(sliceValue2);

            data = new PieChartData(values);
            data.setHasLabels(true);
            data.setHasLabelsOnlyForSelected(false);
            data.setHasLabelsOutside(false);
            chart.setValueSelectionEnabled(true);
            chart.setCircleFillRatio(1.0f);
            chart.setPieChartData(data);
        }

        private class ValueTouchListener implements PieChartOnValueSelectListener {
            @Override
            public void onValueSelected(int arcIndex, SliceValue value) {
                // 点击事件
            }

            @Override
            public void onValueDeselected() {
            }
        }
    }

    // 显示进度对话框
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("正在加载...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    // 关闭进度对话框
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
