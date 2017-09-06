package com.hicc.cloud.teacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.bean.Dormitory;

import java.util.List;

/**
 * Created by Administrator on 2016/10/13/026.
 */
public class DormitoryScoreAdapter extends RecyclerView.Adapter<DormitoryScoreViewHolder> {

    private List<Dormitory> dormitoryList;
    private Context mContext;

    public DormitoryScoreAdapter(List<Dormitory> dormitoryList) {
        this.dormitoryList = dormitoryList;
    }


    @Override
    public DormitoryScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();

        View view;

        view = View.inflate(parent.getContext(), R.layout.item_historymark, null);

        return new DormitoryScoreViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(DormitoryScoreViewHolder holder, int position) {

        Dormitory dormitory = dormitoryList.get(position);

        holder.tv_time.setText(dormitory.getScoreTimeYear()+"."+dormitory.getScoreTimeMonth()+" \n第"+dormitory.getWeekCode()+"周");
        holder.tv_type.setText(dormitory.getCheckTypeDescription());
        holder.tv_score.setText(dormitory.getTotalScore()+"");
    }

    @Override
    public int getItemCount() {
        return (dormitoryList !=null? dormitoryList.size():0);
    }
}
