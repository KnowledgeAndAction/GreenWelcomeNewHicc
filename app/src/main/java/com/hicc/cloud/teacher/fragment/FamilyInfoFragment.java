package com.hicc.cloud.teacher.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hicc.cloud.R;
import com.hicc.cloud.teacher.adapter.FamilyAdapter;
import com.hicc.cloud.teacher.bean.Family;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/24/024.
 * 学生档案  家庭信息
 */

@SuppressLint("ValidFragment")
public class FamilyInfoFragment extends BaseFragment {

    private List<Family> familyList = new ArrayList<Family>();
    private FamilyAdapter myBaseAdapter;
    private RecyclerView mRecyclerView;

    @SuppressLint("ValidFragment")
    public FamilyInfoFragment(List<Family> familyList){
        this.familyList = familyList;
    }

    @Override
    public void fetchData() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family_info, container, false);

        initUI(view);

        myBaseAdapter = new FamilyAdapter(familyList);
        mRecyclerView.setAdapter(myBaseAdapter);

        return view;
    }

    private void initUI(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 4;
            }
        });
        mRecyclerView.setLayoutManager(manager);
    }

    private LinearLayoutManager getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        return linearLayoutManager;
    }
}
