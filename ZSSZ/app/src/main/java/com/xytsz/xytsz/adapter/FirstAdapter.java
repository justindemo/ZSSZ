package com.xytsz.xytsz.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xytsz.xytsz.R;
import com.xytsz.xytsz.bean.FirstDiease;

/**
 * Created by admin on 2017/3/9
 */
public class FirstAdapter extends BaseAdapter {

    private FirstDiease firstDiease;

    public FirstAdapter(FirstDiease firstDiease) {
        this.firstDiease = firstDiease;

    }

    @Override
    public int getCount() {
        return firstDiease.desc.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_first, null);
            holder.name = (TextView) convertView.findViewById(R.id.help_tv_name);
            holder.desc = (TextView) convertView.findViewById(R.id.help_tv_desc);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.help_iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //

        holder.name.setText(firstDiease.dieaseName);
        holder.ivIcon.setImageResource(firstDiease.imageId.get(0)[position]);
        holder.desc.setText(firstDiease.desc.get(position));
        return convertView;
    }

    static class ViewHolder {
        public TextView name;
        public TextView desc;
        public ImageView ivIcon;

    }
}
