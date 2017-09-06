package com.hicc.cloud.teacher.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.activity.CollegeFacultyComparedActivity;
import com.hicc.cloud.teacher.activity.LiveReportsActivity;
import com.hicc.cloud.teacher.activity.NewsActivity;
import com.hicc.cloud.teacher.activity.OnlineReportActivity;
import com.hicc.cloud.teacher.activity.PayStatisticsActivity;
import com.hicc.cloud.teacher.activity.ScanResultActivity;
import com.hicc.cloud.teacher.bean.Picture;
import com.hicc.cloud.teacher.utils.ToastUtli;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by Administrator on 2016/9/24/024.
 * Alter by i_cassell on 2016/9/25.
 * 学院领导
 */

public class CollegeHomeFragment extends BaseFragment implements View.OnClickListener {
    private static final int SCAN_CODE = 0;
    private GridView gridView;
    private String[] titles2 = new String[]{"网上报道", "现场报道", "学部对比", "交费统计"};
    private int[] images2 = new int[]{R.drawable.icon_online_reports, R.drawable.icon_live_reports, R.drawable.icon_comparison, R.drawable.icon_payment};
    private LinearLayout ll_scan;
    private LinearLayout ll_shake;
    private LinearLayout ll_record;
    private RelativeLayout rl_news;

    // 加载数据
    @Override
    public void fetchData() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_college, container, false);

        initUI(view);

        gridView.setNumColumns(1);
        PictureAdapter adapter = new PictureAdapter(titles2, images2, getContext());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //设置点击事件
                switch (position) {
                    // 网上报道
                    case 0:
                        startActivity(new Intent(getContext(), OnlineReportActivity.class));
                        break;
                    // 现场报道
                    case 1:
                        startActivity(new Intent(getContext(), LiveReportsActivity.class));
                        break;
                    // 学部对比
                    case 2:
                        startActivity(new Intent(getContext(), CollegeFacultyComparedActivity.class));
                        break;
                    // 交费统计
                    case 3:
                        startActivity(new Intent(getContext(), PayStatisticsActivity.class));
                        break;
                }
            }
        });

        return view;
    }

    private void initUI(View view) {
        gridView = (GridView) view.findViewById(R.id.gv_menu);

        ll_scan = (LinearLayout) view.findViewById(R.id.ll_scan);
        ll_shake = (LinearLayout) view.findViewById(R.id.ll_shake);
        ll_record = (LinearLayout) view.findViewById(R.id.ll_record);

        rl_news = (RelativeLayout) view.findViewById(R.id.rl_news);

        ImageView iv_scan = (ImageView) view.findViewById(R.id.iv_scan);
        ImageView iv_shake = (ImageView) view.findViewById(R.id.iv_shake);
        ImageView iv_record = (ImageView) view.findViewById(R.id.iv_record);

        ll_scan.setOnClickListener(this);
        ll_shake.setOnClickListener(this);
        ll_record.setOnClickListener(this);

        iv_scan.setOnClickListener(this);
        iv_shake.setOnClickListener(this);
        iv_record.setOnClickListener(this);

        rl_news.setOnClickListener(this);

        // 迎新替换
        FancyButton btn_live = (FancyButton) view.findViewById(R.id.btn_live);
        FancyButton btn_online = (FancyButton) view.findViewById(R.id.btn_online);
        FancyButton btn_comparison = (FancyButton) view.findViewById(R.id.btn_comparison);
        FancyButton btn_pay = (FancyButton) view.findViewById(R.id.btn_pay);

        btn_live.setOnClickListener(this);
        btn_online.setOnClickListener(this);
        btn_comparison.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 扫一扫
            case R.id.ll_scan:
            case R.id.iv_scan:
                ToastUtli.show(getContext(), "努力开发中");
                break;
            // 摇一摇
            case R.id.ll_shake:
            case R.id.iv_shake:
                ToastUtli.show(getContext(), "努力开发中");
                //startActivity(new Intent(getContext(), ShakeActivity.class));
                break;
            // 记录
            case R.id.ll_record:
            case R.id.iv_record:
                ToastUtli.show(getContext(), "努力开发中");
                break;
            // 新闻
            case R.id.rl_news:
                startActivity(new Intent(getContext(), NewsActivity.class));
                break;
            // 现场报到
            case R.id.btn_live:
                startActivity(new Intent(getContext(), LiveReportsActivity.class));
                break;
            // 网上报到
            case R.id.btn_online:
                startActivity(new Intent(getContext(), OnlineReportActivity.class));
                break;
            // 交费统计
            case R.id.btn_pay:
                startActivity(new Intent(getContext(), PayStatisticsActivity.class));
                break;
            // 学部对比
            case R.id.btn_comparison:
                startActivity(new Intent(getContext(), CollegeFacultyComparedActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == SCAN_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    // 解析后操作
                    Intent intent = new Intent(getContext(), ScanResultActivity.class);
                    intent.putExtra("result", result);
                    startActivity(intent);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtli.show(getContext(), "解析二维码失败");
                }
            }
        }
    }

    //自定义适配器
    class PictureAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<Picture> pictures;

        public PictureAdapter(String[] titles, int[] images, Context context) {
            super();
            pictures = new ArrayList<Picture>();
            inflater = LayoutInflater.from(context);
            for (int i = 0; i < images.length; i++) {
                Picture picture = new Picture(titles[i], images[i]);
                pictures.add(picture);
            }
        }

        @Override
        public int getCount() {
            if (null != pictures) {
                return pictures.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return pictures.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.home_item, null);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(pictures.get(position).getTitle());
            viewHolder.image.setImageResource(pictures.get(position).getImageId());

            return convertView;
        }

    }

    class ViewHolder {
        public TextView title;
        public ImageView image;
    }

}
