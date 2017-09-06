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
import com.hicc.cloud.teacher.activity.FacultyComparedActivity;
import com.hicc.cloud.teacher.activity.NewsActivity;
import com.hicc.cloud.teacher.activity.PayStatisticsActivity;
import com.hicc.cloud.teacher.activity.ScanActivity;
import com.hicc.cloud.teacher.activity.ScanResultActivity;
import com.hicc.cloud.teacher.bean.Picture;
import com.hicc.cloud.teacher.utils.ToastUtli;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by Administrator on 2016/9/24/024.
 * 学部
 */

public class FacultyHomeFragment extends BaseFragment implements View.OnClickListener {
    private static final int SCAN_CODE = 0;
    private GridView gridView;
    private String[] titles2 = new String[]{"报道对比","交费统计"};
    private int[] images2 = new int[]{R.drawable.icon_comparison,R.drawable.icon_payment};
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
        View view = inflater.inflate(R.layout.fragment_home_faculty, container, false);

        initUI(view);

        gridView.setNumColumns(2);
        PictureAdapter adapter = new PictureAdapter(titles2, images2, getContext());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //设置点击事件
                switch (position) {
                    // 报道对比
                    case 0:
                        startActivity(new Intent(getContext(), FacultyComparedActivity.class));
                        break;
                    // 交费统计
                    case 1:
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
        rl_news.setOnClickListener(this);

        iv_scan.setOnClickListener(this);
        iv_shake.setOnClickListener(this);
        iv_record.setOnClickListener(this);

        FancyButton btn_comparison = (FancyButton) view.findViewById(R.id.btn_comparison);
        FancyButton btn_scan = (FancyButton) view.findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(this);
        btn_comparison.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 扫一扫
            case R.id.ll_scan:
            case R.id.iv_scan:
                startActivityForResult(new Intent(getContext(), ScanActivity.class), SCAN_CODE);
                break;
            // 摇一摇
            case R.id.ll_shake:
            case R.id.iv_shake:
                //startActivity(new Intent(getContext(), ShakeActivity.class));
                ToastUtli.show(getContext(), "努力开发中");
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
            // 报到对比
            case R.id.btn_comparison:
                startActivity(new Intent(getContext(), FacultyComparedActivity.class));
                break;
            // 扫码报到
            case R.id.btn_scan:
                startActivityForResult(new Intent(getContext(), ScanActivity.class), SCAN_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        switch (requestCode) {
            case SCAN_CODE:
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
                        startActivityForResult(intent, 2);
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        ToastUtli.show(getContext(), "解析二维码失败");
                    }
                }
                break;
            case 2:
                startActivityForResult(new Intent(getContext(), ScanActivity.class), SCAN_CODE);
                break;
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
