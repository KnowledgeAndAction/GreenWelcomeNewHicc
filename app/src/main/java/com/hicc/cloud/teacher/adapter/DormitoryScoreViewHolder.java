package com.hicc.cloud.teacher.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hicc.cloud.R;

/**
 * Created by Administrator on 2016/10/13/026.
 */
public class DormitoryScoreViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_time;
    public TextView tv_type;
    public TextView tv_score;

    public DormitoryScoreViewHolder(View itemView, int viewType) {
        super(itemView);

        tv_time = (TextView) itemView.findViewById(R.id.tv_course);
        tv_type = (TextView) itemView.findViewById(R.id.tv_teacher);
        tv_score = (TextView) itemView.findViewById(R.id.tv_mark);
    }
}
