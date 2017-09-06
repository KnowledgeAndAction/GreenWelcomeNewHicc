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
import com.hicc.cloud.teacher.activity.AllActivity;
import com.hicc.cloud.teacher.activity.ClassComparedActivity;
import com.hicc.cloud.teacher.activity.ClassListActivity;
import com.hicc.cloud.teacher.activity.NewsActivity;
import com.hicc.cloud.teacher.activity.ScanResultActivity;
import com.hicc.cloud.teacher.bean.Picture;
import com.hicc.cloud.teacher.utils.ConstantValue;
import com.hicc.cloud.teacher.utils.NetworkRequestUtil;
import com.hicc.cloud.teacher.utils.ToastUtli;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by Administrator on 2016/9/24/024.
 * 导员
 */

public class TeacherHomeFragment extends BaseFragment implements View.OnClickListener {
    private static final int SCAN_CODE = 0;
    private GridView gridView;
    private String[] titles = new String[]{"报到情况", "学生档案", "学生成绩", "全部"};
    private int[] images = new int[]{R.drawable.icon_comparison, R.drawable.icon_file, R.drawable.icon_stu_ach, R.mipmap.icon_all};
    private LinearLayout ll_scan;
    private LinearLayout ll_shake;
    private LinearLayout ll_record;
    private LinearLayout ll_classrecord;


    // 加载数据
    @Override
    public void fetchData() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_teacher, container, false);

        initUI(view);

        PictureAdapter adapter = new PictureAdapter(titles, images, getContext());
        gridView.setNumColumns(4);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //设置点击事件
                switch (position) {
                    //报道对比
                    case 0:
                        Intent intent = new Intent(getContext(), ClassComparedActivity.class);
                        startActivity(intent);
                        break;
                    // 学生档案
                    case 1:
                        // 向服务器发送点击的功能
                        NetworkRequestUtil.postClickFunction(getContext(), "1");
                        Intent intent0 = new Intent(getContext(), ClassListActivity.class);
                        intent0.putExtra("type", ConstantValue.STUDENT_PROFILE);
                        startActivity(intent0);
                        break;
                    // 学生成绩
                    case 2:
                        // 向服务器发送点击的功能
                        NetworkRequestUtil.postClickFunction(getContext(), "2");
                        Intent intent1 = new Intent(getContext(), ClassListActivity.class);
                        intent1.putExtra("type", ConstantValue.STUDENT_MARK);
                        startActivity(intent1);
                        break;
                    // 全部
                    case 3:
                        startActivity(new Intent(getContext(), AllActivity.class));
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

        ll_classrecord = (LinearLayout) view.findViewById(R.id.ll_classrecord);
        RelativeLayout rl_news = (RelativeLayout) view.findViewById(R.id.rl_news);

        ImageView iv_scan = (ImageView) view.findViewById(R.id.iv_scan);
        ImageView iv_shake = (ImageView) view.findViewById(R.id.iv_shake);
        ImageView iv_record = (ImageView) view.findViewById(R.id.iv_record);

        ll_scan.setOnClickListener(this);
        ll_shake.setOnClickListener(this);
        ll_record.setOnClickListener(this);

        iv_scan.setOnClickListener(this);
        iv_shake.setOnClickListener(this);
        iv_record.setOnClickListener(this);

        ll_classrecord.setOnClickListener(this);
        rl_news.setOnClickListener(this);

        FancyButton btn_comparison = (FancyButton) view.findViewById(R.id.btn_comparison);
        FancyButton btn_file = (FancyButton) view.findViewById(R.id.btn_file);
        FancyButton btn_mark = (FancyButton) view.findViewById(R.id.btn_mark);
        FancyButton btn_dormitory = (FancyButton) view.findViewById(R.id.btn_dormitory);
        FancyButton btn_pay = (FancyButton) view.findViewById(R.id.btn_pay);

        btn_comparison.setOnClickListener(this);
        btn_file.setOnClickListener(this);
        btn_mark.setOnClickListener(this);
        btn_dormitory.setOnClickListener(this);
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
                //startActivity(new Intent(getContext(), ShakeActivity.class));
                ToastUtli.show(getContext(), "努力开发中");
                break;
            // 记录
            case R.id.ll_record:
            case R.id.iv_record:
                ToastUtli.show(getContext(), "努力开发中");
                break;
            // 记录班级成长
            case R.id.ll_classrecord:
                ToastUtli.show(getContext(), "努力开发中");
                break;
            // 新闻
            case R.id.rl_news:
                startActivity(new Intent(getContext(), NewsActivity.class));
                break;
            // 报到对比
            case R.id.btn_comparison:
                Intent intent = new Intent(getContext(), ClassComparedActivity.class);
                startActivity(intent);
                break;
            // 学生档案
            case R.id.btn_file:
                // 向服务器发送点击的功能
                NetworkRequestUtil.postClickFunction(getContext(), "1");
                Intent intent0 = new Intent(getContext(), ClassListActivity.class);
                intent0.putExtra("type", ConstantValue.STUDENT_PROFILE);
                startActivity(intent0);
                break;
            // 学生成绩
            case R.id.btn_mark:
                // 向服务器发送点击的功能
                NetworkRequestUtil.postClickFunction(getContext(), "2");
                Intent intent1 = new Intent(getContext(), ClassListActivity.class);
                intent1.putExtra("type", ConstantValue.STUDENT_MARK);
                startActivity(intent1);
                break;
            // 宿舍成绩
            case R.id.btn_dormitory:
                Intent intentD = new Intent(getContext(), ClassListActivity.class);
                intentD.putExtra("type", ConstantValue.DORMITORY_SCORE);
                startActivity(intentD);
                break;
            // 交费统计
            case R.id.btn_pay:
                Intent intent2 = new Intent(getContext(), ClassListActivity.class);
                intent2.putExtra("type", ConstantValue.PAY_STATISTICS);
                startActivity(intent2);
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

//            if((levelCode != 11 && levelCode != 16) && (position == 1 || position == 2 || position == 3 || position == 4 || position == 5)){
//                viewHolder.title.setTextColor(Color.parseColor("#d5d2d2"));
//
//            }

            return convertView;
        }

    }

    class ViewHolder {
        public TextView title;
        public ImageView image;
    }

}
