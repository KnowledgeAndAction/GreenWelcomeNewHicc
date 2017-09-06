package com.hicc.cloud.teacher.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hicc.cloud.R;

/**
 * Created by Administrator on 2016/10/13/026.
 */
public class FamilyViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_relation;
    public TextView tv_name;
    public TextView tv_age;
    public TextView tv_workand;
    public TextView tv_politics;
    public TextView tv_phone;
    public TextView tv_address;

    public FamilyViewHolder(View itemView, int viewType) {
        super(itemView);
        tv_relation = (TextView) itemView.findViewById(R.id.tv_relation);
        tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        tv_age = (TextView) itemView.findViewById(R.id.tv_age);
        tv_workand = (TextView) itemView.findViewById(R.id.tv_workand);
        tv_politics = (TextView) itemView.findViewById(R.id.tv_politics);
        tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
        tv_address = (TextView) itemView.findViewById(R.id.tv_address);
    }
}
