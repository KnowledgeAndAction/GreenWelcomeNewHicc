package com.hicc.cloud.teacher.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26/026.
 * 备用ListView适配器
 */

public class MyBaseAdapter extends BaseAdapter {
    private List<Object> mList;

    MyBaseAdapter(List<Object> mList){
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
//            convertView = View.inflate(getApplicationContext(), R.layout.view_balcknumber_item, null);

            viewHolder = new ViewHolder();
//            viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);

            // 将存有findViewById的viewHolder存到convertView中
            convertView.setTag(viewHolder);
        }else{
            // 复用convertView里存的viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return null;
    }

    static class ViewHolder{
//        TextView tv_phone;
    }
}
