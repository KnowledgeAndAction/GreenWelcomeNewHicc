package com.hicc.cloud.teacher.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hicc.cloud.R;

/**
 * Created by Administrator on 2016/10/13/026.
 */
public class MarkHistoryViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_mark;
    public TextView tv_teacher;
    public TextView tv_course;

    public MarkHistoryViewHolder(View itemView, int viewType) {
        super(itemView);

        tv_course = (TextView) itemView.findViewById(R.id.tv_course);
        tv_mark = (TextView) itemView.findViewById(R.id.tv_mark);
        tv_teacher = (TextView) itemView.findViewById(R.id.tv_teacher);
    }
}
