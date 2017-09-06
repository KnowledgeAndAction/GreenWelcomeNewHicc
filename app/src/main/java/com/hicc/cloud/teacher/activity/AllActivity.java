package com.hicc.cloud.teacher.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.bean.Picture;
import com.hicc.cloud.teacher.utils.ConstantValue;
import com.hicc.cloud.teacher.utils.NetworkRequestUtil;
import com.hicc.cloud.teacher.utils.ToastUtli;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/8/008.
 * 全部页面
 */
public class AllActivity extends AppCompatActivity {

    private ImageView iv_back;
    private GridView gv_menu;
    private GridView gv_other;
    private String[] mTitles1 = new String[]{"学生成绩", "宿舍成绩", "请销假", "课堂签到", "学生社团", "班级成长", "学生档案", "交费统计"};
    private int[] mImages1 = new int[]{ R.drawable.icon_stu_ach, R.drawable.icon_room_ach,
            R.mipmap.leaveback, R.drawable.icon_check,
            R.mipmap.club, R.mipmap.classes,
            R.drawable.icon_file, R.drawable.icon_payment};

    private String[] mTitles_2 = new String[]{"报道对比","问卷调查"};
    private int[] mImages_2 = new int[]{R.drawable.icon_comparison, R.drawable.icon_questionnaire,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);

        initUI();
    }

    private void initUI() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gv_menu = (GridView) findViewById(R.id.gv_menu);
        gv_other = (GridView) findViewById(R.id.gv_other);

        // 常用应用
        PictureAdapter adapter1 = new PictureAdapter(mTitles1, mImages1, getApplicationContext(), 1);
        gv_menu.setAdapter(adapter1);
        gv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //点击事件
                switch (position){
                    // 学生成绩
                    case 0:
                        // 向服务器发送点击的功能
                        NetworkRequestUtil.postClickFunction(getApplicationContext(),"2");
                        Intent intent1 = new Intent(getApplicationContext(),ClassListActivity.class);
                        intent1.putExtra("type",ConstantValue.STUDENT_MARK);
                        startActivity(intent1);
                        break;
                    // 宿舍成绩
                    case 1:
                        Intent intentD = new Intent(getApplicationContext(), ClassListActivity.class);
                        intentD.putExtra("type", ConstantValue.DORMITORY_SCORE);
                        startActivity(intentD);
                        break;
                    // 请销假
                    case 2:
                        ToastUtli.show(getApplicationContext(),"努力开发中");
                        //startActivity(new Intent(getApplicationContext(),LeaveBackActivity.class));
                        break;
                    // 课堂签到
                    case 3:
                        ToastUtli.show(getApplicationContext(),"努力开发中");
                        //startActivity(new Intent(getApplicationContext(),ClassCheckActivity.class));
                        break;
                    // 学生社团
                    case 4:
                        ToastUtli.show(getApplicationContext(),"努力开发中");
                        //startActivity(new Intent(getApplicationContext(),StudentCommunityActivity.class));
                        break;
                    // 班级成长
                    case 5:
                        ToastUtli.show(getApplicationContext(),"努力开发中");
                        //startActivity(new Intent(getApplicationContext(),ClassGrowUpActivity.class));
                        break;
                    // 学生档案
                    case 6:
                        // 向服务器发送点击的功能
                        NetworkRequestUtil.postClickFunction(getApplicationContext(),"1");
                        Intent intent = new Intent(getApplicationContext(),ClassListActivity.class);
                        intent.putExtra("type", ConstantValue.STUDENT_PROFILE);
                        startActivity(intent);
                        break;
                    // 学生缴费
                    case 7:
                        Intent intent2 = new Intent(getApplicationContext(), ClassListActivity.class);
                        intent2.putExtra("type", ConstantValue.PAY_STATISTICS);
                        startActivity(intent2);
                        break;
                }
            }
        });

        // 其他应用
        PictureAdapter adapter2 = new PictureAdapter(mTitles_2, mImages_2, getApplicationContext(), 2);
        gv_other.setAdapter(adapter2);
        gv_other.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //点击事件
                switch (position){
                    // 报道对比
                    case 0:
                        startActivity(new Intent(getApplicationContext(),ClassComparedActivity.class));
                        break;
                    // 问卷调查
                    case 1:
                        ToastUtli.show(getApplicationContext(),"努力开发中");
                        break;
                }
            }
        });
    }

    //自定义适配器
    class PictureAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<Picture> pictures;
        private int type;

        public PictureAdapter(String[] titles, int[] images, Context context, int type) {
            super();
            pictures = new ArrayList<Picture>();
            inflater = LayoutInflater.from(context);
            this.type = type;
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

            if(type == 1){
                if(position == 2 || position == 3 || position == 4 || position == 5){
                    viewHolder.title.setTextColor(Color.parseColor("#d5d2d2"));
                }
            }

            if(type == 2){
                if(position == 1){
                    viewHolder.title.setTextColor(Color.parseColor("#d5d2d2"));
                }
            }

            return convertView;
        }
    }

    class ViewHolder {
        public TextView title;
        public ImageView image;
    }
}
