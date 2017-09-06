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

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.utils.ConstantValue;
import com.hicc.cloud.teacher.utils.Logs;
import com.hicc.cloud.teacher.utils.SpUtils;
import com.hicc.cloud.teacher.utils.ToastUtli;
import com.hicc.cloud.teacher.utils.URLs;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

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

// 交费统计
public class PayStatisticsActivity extends AppCompatActivity {

    private ImageView iv_back;
    private ProgressDialog progressDialog;
    private String num;
    private String timeCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_statistics);

        initUI();

        initData();

        getDate();
    }

    private void initData() {
        int userNo = SpUtils.getIntSp(getApplicationContext(), ConstantValue.USER_NO, 0);
        switch (userNo) {
            case 1001:
                num = "0";
                timeCode = "17";
                break;
            case 2001:
                num = "10";
                timeCode = "17";
                break;
            case 2002:
                num = "11";
                timeCode = "17";
                break;
            case 2003:
                num = "12";
                timeCode = "17";
                break;
            case 2004:
                num = "13";
                timeCode = "17";
                break;
            case 2005:
                num = "14";
                timeCode = "17";
                break;
            default:
                num = getIntent().getStringExtra("num");
                timeCode = getIntent().getStringExtra("timeCode");
                break;
        }
    }

    private void getDate() {
        showProgressDialog();
        // 获取交费信息
        OkHttpUtils
                .get()
                .url(URLs.GetPayCostNum)
                .addParams("userno", SpUtils.getIntSp(getApplicationContext(), ConstantValue.USER_NO, 0) + "")
                .addParams("num", num)
                .addParams("userlevelcode", SpUtils.getIntSp(getApplicationContext(), ConstantValue.USER_LEVEL_CODE, 0) + "")
                .addParams("account", SpUtils.getStringSp(getApplicationContext(), ConstantValue.USER_NAME, ""))
                .addParams("pas", SpUtils.getStringSp(getApplicationContext(), ConstantValue.PASS_WORD, ""))
                .addParams("timeCode", timeCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeProgressDialog();
                        Logs.i(e.toString());
                        getSupportFragmentManager().beginTransaction().add(R.id.container_online_c, new PlaceholderFragment(0, 0, 0, 0, 0, "", "", "", "")).commit();
                        getSupportFragmentManager().beginTransaction().add(R.id.container_online_p, new PlaceholderLiveFragment(0, 0, 0, 0, "", "", "", "")).commit();
                        ToastUtli.show(getApplicationContext(), "服务器繁忙，请重新查询");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logs.i(response);
                        // 解析json 崔
                        Logs.i("解析json");
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int yesPay = 0;
                            int noPay = 0;
                            int loan = 0;
                            int late = 0;
                            String pay = null;
                            String notPay = null;
                            String loanPay = null;
                            String latePay = null;
                            int all = 0;
                            int y = 0;
                            if (y != jsonArray.length()) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONArray data = jsonArray.getJSONArray(i);
                                    if (i == 0) {
                                        yesPay = data.getInt(1);
                                        noPay = data.getInt(0);
                                        loan = data.getInt(2);
                                        late = data.getInt(3);
                                    } else if (i == 1) {
                                        pay = data.getString(1);
                                        notPay = data.getString(0);
                                        loanPay = data.getString(2);
                                        latePay = data.getString(3);
                                    } else if (i == 2) {
                                        all = data.getInt(0);
                                    }
                                    y++;
                                }
                            }
                            if (yesPay == 0 && noPay == 0 && loan == 0 && late == 0) {
                                ToastUtli.show(PayStatisticsActivity.this, "暂无数据");
                            }
                            getSupportFragmentManager().beginTransaction().add(R.id.container_online_c, new PlaceholderFragment(all, yesPay, noPay, loan, late, pay, notPay, loanPay, latePay)).commit();
                            getSupportFragmentManager().beginTransaction().add(R.id.container_online_p, new PlaceholderLiveFragment(yesPay, noPay, loan, late, pay, notPay, loanPay, latePay)).commit();
                            closeProgressDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            closeProgressDialog();
                            getSupportFragmentManager().beginTransaction().add(R.id.container_online_c, new PlaceholderFragment(0, 0, 0, 0, 0, "", "", "", "")).commit();
                            getSupportFragmentManager().beginTransaction().add(R.id.container_online_p, new PlaceholderLiveFragment(0, 0, 0, 0, "", "", "", "")).commit();
                            ToastUtli.show(getApplicationContext(), "获取信息失败");
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
    }

    // 柱状图
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
        private int loan;
        private int late;
        String pay;
        String notPay;
        String loadPay;
        String latePay;

        @SuppressLint("ValidFragment")
        public PlaceholderFragment(int all, int yes, int no, int loan, int late, String pay, String notPay, String loadPay, String latePay) {
            this.all = all;
            this.yes = yes;
            this.no = no;
            this.loan = loan;
            this.late = late;
            this.latePay = latePay;
            this.loadPay = loadPay;
            this.pay = pay;
            this.notPay = notPay;
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

            generateDefaultData();

            return rootView;
        }

        // 添加数据
        private void generateDefaultData() {
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;

            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue(all, ChartUtils.pickColor()));
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);

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

            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue(loan, ChartUtils.pickColor()));
            Column column4 = new Column(values);
            column4.setHasLabels(hasLabels);
            column4.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column4);

            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue(late, ChartUtils.pickColor()));
            Column column5 = new Column(values);
            column5.setHasLabels(hasLabels);
            column5.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column5);

            data = new ColumnChartData(columns);

            // 坐标
            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisY.setName("人数");
                    ArrayList<AxisValue> axisValuesX = new ArrayList<AxisValue>();
                    axisValuesX.add(new AxisValue(0).setValue(0).setLabel("总数"));
                    axisValuesX.add(new AxisValue(1).setValue(1).setLabel(this.pay));
                    axisValuesX.add(new AxisValue(2).setValue(2).setLabel(this.notPay));
                    axisValuesX.add(new AxisValue(3).setValue(3).setLabel(this.loadPay));
                    axisValuesX.add(new AxisValue(4).setValue(4).setLabel(this.latePay));
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
                switch (columnIndex) {
                    case 0:
                        ToastUtli.show(getContext(), "总共" + subV + "人");
                        break;
                    case 1:
                        ToastUtli.show(getContext(), "未交费" + subV + "人");
                        break;
                    case 2:
                        ToastUtli.show(getContext(), "已缴费" + subV + "人");
                        break;
                    case 3:
                        ToastUtli.show(getContext(), "贷款缴费" + subV + "人");
                        break;
                    case 4:
                        ToastUtli.show(getContext(), "缓缴费" + subV + "人");
                        break;
                }
            }

            @Override
            public void onValueDeselected() {
            }
        }
    }


    // 饼状图
    public static class PlaceholderLiveFragment extends Fragment {
        private PieChartView chart;
        private PieChartData data;
        private int yes;
        private int no;
        private int loanPay;
        private int latePay;
        String pay;
        String notPay ;
        String loanPay1 ;
        String latePay1 ;

        @SuppressLint("ValidFragment")
        public PlaceholderLiveFragment(int yes, int no, int loanPay, int latePay, String pay, String notPay, String loanPay1, String latePay1) {
            this.yes = yes;
            this.no = no;
            this.loanPay = loanPay;
            this.latePay = latePay;
            this.pay=pay;
            this.notPay=notPay;
            this.latePay1=latePay1;
            this.loanPay1=loanPay1;
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
            if (yes != 0) {
                SliceValue sliceValue1 = new SliceValue(yes, ChartUtils.pickColor());
                sliceValue1.setLabel(this.pay + yes + "人");
                values.add(sliceValue1);
            }
            if (no != 0) {
                SliceValue sliceValue2 = new SliceValue(no, ChartUtils.pickColor());
                sliceValue2.setLabel(this.notPay+ no + "人");
                values.add(sliceValue2);
            }

            if (loanPay != 0) {
                SliceValue sliceValue3 = new SliceValue(loanPay, ChartUtils.pickColor());
                sliceValue3.setLabel(this.loanPay1 + loanPay + "人");
                values.add(sliceValue3);
            }

            if (latePay != 0) {
                SliceValue sliceValue4 = new SliceValue(latePay, ChartUtils.pickColor());
                sliceValue4.setLabel(this.latePay1 + latePay + "人");
                values.add(sliceValue4);
            }

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
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
