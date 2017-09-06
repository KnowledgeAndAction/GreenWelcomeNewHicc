package com.hicc.cloud.teacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.bean.Family;
import com.hicc.cloud.teacher.utils.Logs;

import java.util.List;

/**
 * Created by Administrator on 2016/10/13/026.
 */
public class FamilyAdapter extends RecyclerView.Adapter<FamilyViewHolder> {

    private List<Family> mFeedList;
    private Context mContext;

    public FamilyAdapter(List<Family> feedList) {
        mFeedList = feedList;
    }


    @Override
    public FamilyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();

        View view;

        view = View.inflate(parent.getContext(), R.layout.item_family, null);

        return new FamilyViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(FamilyViewHolder holder, int position) {

        Family family = mFeedList.get(position);

        holder.tv_relation.setText("关系：" + family.getRelation());
        holder.tv_name.setText("姓名：" + family.getName());
        holder.tv_age.setText("年龄：" + family.getAge());
        holder.tv_workand.setText("工作：" + family.getWorkand());
        holder.tv_phone.setText("电话：" + family.getPhone());
        holder.tv_politics.setText("政治面貌：" + family.getPolitics());
        holder.tv_address.setText("联系地址：" + family.getContactAddress());

        holder.tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到拨号界面
                TextView tv = (TextView) v;
                String number = tv.getText().toString();
                number = number.substring(3);
                Logs.i("电话为："+number);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }
}
