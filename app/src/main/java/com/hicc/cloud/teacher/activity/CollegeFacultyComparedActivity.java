package com.hicc.cloud.teacher.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
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
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/15/008.
 * 网上报到
 * 柱形图
 */
public class CollegeFacultyComparedActivity extends AppCompatActivity {
    private View iv_back;
    private ProgressDialog progressDialog;
    public List<String> onlineDivisionarryList = new ArrayList<>();
    private List<Integer> onlineReportarryList = new ArrayList<>();
    private List<Integer> onlineNoReportarryList = new ArrayList<>();
    public List<String> liveDivisionarryList = new ArrayList<>();
    private List<Integer> liveReportarryList = new ArrayList<>();
    private List<Integer> liveNoReportarryList = new ArrayList<>();
    private List<String> onlinePercentageList = new ArrayList<>();
    private List<String> livePercentageList = new ArrayList<>();
    private TextView tv_online_percentage1;
    private TextView tv_online_percentage2;
    private TextView tv_online_percentage3;
    private TextView tv_online_percentage4;
    private TextView tv_online_percentage5;
    private TextView tv_live_percentage1;
    private TextView tv_live_percentage2;
    private TextView tv_live_percentage3;
    private TextView tv_live_percentage4;
    private TextView tv_live_percentage5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_faculty);

        initUI();

        // 获取网上报到数据
        getOnlineData();

        // 获取现场报到数据
        getLiveData();
    }

    private void getLiveData() {
        showProgressDialog();
        // 获取现场报道信息
        OkHttpUtils
                .get()
                .url("http://home.hicc.cn/PhoneInterface/SceneReportService.asmx/Getscenedivisioncomp")
                .addParams("userlevelcode", SpUtils.getIntSp(getApplicationContext(), ConstantValue.USER_LEVEL_CODE, 0) + "")
                .addParams("account", SpUtils.getStringSp(getApplicationContext(), ConstantValue.USER_NAME, ""))
                .addParams("pas", SpUtils.getStringSp(getApplicationContext(), ConstantValue.PASS_WORD, ""))
                .addParams("timecode", "17")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeProgressDialog();
                        Logs.e("获取现场报道数据失败：" + e.toString());
                        getSupportFragmentManager().beginTransaction().add(R.id.container_live, new PlaceholderFragment(liveDivisionarryList, liveReportarryList, liveNoReportarryList)).commit();
                        ToastUtli.show(getApplicationContext(), "获取现场报到数据失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject data = jsonArray.getJSONObject(0);
                            // 学部描述
                            JSONArray divisionarry = data.getJSONArray("divisionarry");
                            for (int i=0; i<divisionarry.length(); i++) {
                                liveDivisionarryList.add(divisionarry.getString(i));
                            }
                            // 已报到
                            JSONArray reportarry = data.getJSONArray("reportarry");
                            for (int i=0; i<reportarry.length(); i++) {
                                liveReportarryList.add(reportarry.getInt(i));
                            }
                            // 未报到
                            JSONArray noReportarry = data.getJSONArray("noreportarry");
                            for (int i=0; i<noReportarry.length(); i++) {
                                liveNoReportarryList.add(noReportarry.getInt(i));
                            }
                            // 计算各个学部报道率
                            for (int i=0; i<divisionarry.length(); i++) {
                                int all = liveReportarryList.get(i)+liveNoReportarryList.get(i);
                                int yes = liveReportarryList.get(i);
                                String s = String.format("%.2f", ((double) yes / all) * 100);
                                livePercentageList.add(s + "%");
                            }
                            tv_live_percentage1.setText(livePercentageList.get(0));
                            tv_live_percentage2.setText(livePercentageList.get(1));
                            tv_live_percentage3.setText(livePercentageList.get(2));
                            tv_live_percentage4.setText(livePercentageList.get(3));
                            tv_live_percentage5.setText(livePercentageList.get(4));

                            getSupportFragmentManager().beginTransaction().add(R.id.container_live, new PlaceholderFragment(liveDivisionarryList, liveReportarryList, liveNoReportarryList)).commit();
                            closeProgressDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            closeProgressDialog();
                            Logs.e("获取现场报到数据失败：" + e.toString());
                            getSupportFragmentManager().beginTransaction().add(R.id.container_live, new PlaceholderFragment(liveDivisionarryList, liveReportarryList, liveNoReportarryList)).commit();
                            ToastUtli.show(getApplicationContext(), "获取现场报到数据失败");
                        }
                    }
                });
    }

    private void getOnlineData() {
        showProgressDialog();
        // 获取网上报道信息
        OkHttpUtils
                .get()
                .url("http://home.hicc.cn/PhoneInterface/OnlineReportService.asmx/Getonlinedivisioncomp")
                .addParams("userlevelcode", SpUtils.getIntSp(getApplicationContext(), ConstantValue.USER_LEVEL_CODE, 0) + "")
                .addParams("account", SpUtils.getStringSp(getApplicationContext(), ConstantValue.USER_NAME, ""))
                .addParams("pas", SpUtils.getStringSp(getApplicationContext(), ConstantValue.PASS_WORD, ""))
                .addParams("timecode", "17")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeProgressDialog();
                        Logs.e("获取网上报道数据失败：" + e.toString());
                        getSupportFragmentManager().beginTransaction().add(R.id.container_online, new PlaceholderFragment(onlineDivisionarryList, onlineReportarryList, onlineNoReportarryList)).commit();
                        ToastUtli.show(getApplicationContext(), "获取网上报到数据失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject data = jsonArray.getJSONObject(0);
                            // 学部描述
                            JSONArray divisionarry = data.getJSONArray("divisionarry");
                            for (int i=0; i<divisionarry.length(); i++) {
                                onlineDivisionarryList.add(divisionarry.getString(i));
                            }
                            // 已报到
                            JSONArray reportarry = data.getJSONArray("reportarry");
                            for (int i=0; i<reportarry.length(); i++) {
                                onlineReportarryList.add(reportarry.getInt(i));
                            }
                            // 未报到
                            JSONArray noReportarry = data.getJSONArray("noreportarry");
                            for (int i=0; i<noReportarry.length(); i++) {
                                onlineNoReportarryList.add(noReportarry.getInt(i));
                            }

                            // 计算各个学部报道率
                            for (int i=0; i<divisionarry.length(); i++) {
                                int all = onlineReportarryList.get(i)+onlineNoReportarryList.get(i);
                                int yes = onlineReportarryList.get(i);
                                String s = String.format("%.2f", ((double) yes / all) * 100);
                                onlinePercentageList.add(s + "%");
                            }
                            tv_online_percentage1.setText(onlinePercentageList.get(0));
                            tv_online_percentage2.setText(onlinePercentageList.get(1));
                            tv_online_percentage3.setText(onlinePercentageList.get(2));
                            tv_online_percentage4.setText(onlinePercentageList.get(3));
                            tv_online_percentage5.setText(onlinePercentageList.get(4));

                            getSupportFragmentManager().beginTransaction().add(R.id.container_online, new PlaceholderFragment(onlineDivisionarryList, onlineReportarryList, onlineNoReportarryList)).commit();
                            closeProgressDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            closeProgressDialog();
                            Logs.e("获取网上报道数据失败：" + e.toString());
                            getSupportFragmentManager().beginTransaction().add(R.id.container_online, new PlaceholderFragment(onlineDivisionarryList, onlineReportarryList, onlineNoReportarryList)).commit();
                            ToastUtli.show(getApplicationContext(), "获取网上报到数据失败");
                        }
                    }
                });

    }

    // 柱状图
    public static class PlaceholderFragment extends Fragment {
        private ColumnChartView chart;
        private ColumnChartData data;

        private boolean hasAxes = true;
        private boolean hasAxesNames = true;
        private boolean hasLabels = true;
        private boolean hasLabelForSelected = false;

        private List<Integer> reportarry;
        private List<Integer> noReportarry;
        private List<String> divisionarry;

        @SuppressLint("ValidFragment")
        public PlaceholderFragment(List<String> divisionarry, List<Integer> reportarry, List<Integer> noReportarry) {
            this.divisionarry = divisionarry;
            this.reportarry = reportarry;
            this.noReportarry = noReportarry;
        }

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

            generateData();

            return rootView;
        }

        // 添加数据
        private void generateData() {
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            for (int i = 0; i < divisionarry.size(); ++i) {

                values = new ArrayList<SubcolumnValue>();
                // 全部
                //values.add(new SubcolumnValue((float) (reportarry.get(i)+noReportarry.get(i)), ChartUtils.pickColor()));
                // 未报到
                values.add(new SubcolumnValue((float) noReportarry.get(i), Color.parseColor("#FF4445")));
                // 已报到
                values.add(new SubcolumnValue((float) reportarry.get(i), Color.parseColor("#9BCC02")));

                Column column = new Column(values);
                column.setHasLabels(hasLabels);
                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
            }

            data = new ColumnChartData(columns);

            data.setStacked(true);

            // 坐标
            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisY.setName("人数");
                    //axisY.setTextColor(Color.parseColor("#3E3E3E"));
                    ArrayList<AxisValue> axisValuesX = new ArrayList<AxisValue>();
                    for (int i=0; i<divisionarry.size(); i++) {
                        if (divisionarry.get(i).equals("国际文化交流学部")) {
                            axisValuesX.add(new AxisValue(i).setValue(i).setLabel("国交学部"));
                        } else {
                            axisValuesX.add(new AxisValue(i).setValue(i).setLabel(divisionarry.get(i)));
                        }
                    }
                    axisX.setTextSize(10);
                    //axisX.setTextColor(Color.parseColor("#3E3E3E"));
                    axisX.setValues(axisValuesX);//为X轴显示的刻度值设置数据集合
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
                switch (subcolumnIndex) {
                    case 0:
                        ToastUtli.show(getContext(), "未报到" + subV + "人");
                        break;
                    case 1:
                        ToastUtli.show(getContext(), "已报到" + subV + "人");
                        break;
                }
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
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    // 关闭进度对话框
    private void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void initUI() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_online_percentage1 = (TextView) findViewById(R.id.tv_online_percentage1);
        tv_online_percentage2 = (TextView) findViewById(R.id.tv_online_percentage2);
        tv_online_percentage3 = (TextView) findViewById(R.id.tv_online_percentage3);
        tv_online_percentage4 = (TextView) findViewById(R.id.tv_online_percentage4);
        tv_online_percentage5 = (TextView) findViewById(R.id.tv_online_percentage5);

        tv_live_percentage1 = (TextView) findViewById(R.id.tv_live_percentage1);
        tv_live_percentage2 = (TextView) findViewById(R.id.tv_live_percentage2);
        tv_live_percentage3 = (TextView) findViewById(R.id.tv_live_percentage3);
        tv_live_percentage4 = (TextView) findViewById(R.id.tv_live_percentage4);
        tv_live_percentage5 = (TextView) findViewById(R.id.tv_live_percentage5);
    }
}
