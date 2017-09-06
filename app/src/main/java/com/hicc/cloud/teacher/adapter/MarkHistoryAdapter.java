package com.hicc.cloud.teacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.bean.Mark;

import java.util.List;

/**
 * Created by Administrator on 2016/10/13/026.
 */
public class MarkHistoryAdapter extends RecyclerView.Adapter<MarkHistoryViewHolder> {

    private List<Mark> mFeedList;
    private Context mContext;

    public MarkHistoryAdapter(List<Mark> feedList) {
        mFeedList = feedList;
    }


    @Override
    public MarkHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();

        View view;

        view = View.inflate(parent.getContext(), R.layout.item_historymark, null);

        return new MarkHistoryViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(MarkHistoryViewHolder holder, int position) {

        Mark mark = mFeedList.get(position);

        holder.tv_teacher.setText(mark.getTeacher());
        holder.tv_mark.setText(""+mark.getMark());
        holder.tv_course.setText(mark.getCourse());
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }
}
