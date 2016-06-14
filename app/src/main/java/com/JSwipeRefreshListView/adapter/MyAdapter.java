package com.JSwipeRefreshListView.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.JSwipeRefreshListView.R;

import java.util.ArrayList;
import java.util.List;

/**
 * test Adapter
 * janedler
 */
public class MyAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private List<String> mList = new ArrayList<>();
    private Context mContext;

    public MyAdapter(Context context) {
        this.mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mList == null || mList.size() <= 0) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView mValue;//
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_layout, parent, false);
            holder = new ViewHolder();
            holder.mValue = (TextView) convertView.findViewById(R.id.value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String data = mList.get(position);
        holder.mValue.setText(data);
        return convertView;
    }


    public synchronized void setList(List<String> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        this.mList.clear();
        this.mList = list;
        notifyDataSetChanged();
    }

    public synchronized void addList(List<String> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public synchronized void cleanAdapter() {
        this.mList = new ArrayList<>();
        notifyDataSetInvalidated();
    }

    public synchronized int getSize() {
        if (mList == null || mList.size() <= 0) {
            return 0;
        }
        return mList.size();
    }

    public synchronized List<String> getList() {
        return mList;
    }

    /**
     * 判断listview是否为空
     */
    public synchronized boolean isEmpty() {
        return mList == null || mList.size() <= 0;
    }

}
