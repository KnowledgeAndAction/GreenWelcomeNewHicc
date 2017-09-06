package com.hicc.cloud.teacher.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hicc.cloud.R;

/**
 * Created by Administrator on 2016/9/24/024.
 * 学生档案  奖惩信息
 */

public class RewardPunishmentInfoFragment extends BaseFragment {
    @Override
    public void fetchData() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reward_punishment_info, container, false);


        return view;
    }


    private LinearLayoutManager getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        return linearLayoutManager;
    }


}
